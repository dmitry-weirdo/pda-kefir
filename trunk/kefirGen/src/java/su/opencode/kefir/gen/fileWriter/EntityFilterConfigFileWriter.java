/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.Relation;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.srv.ExtEntityFilterConfig;
import su.opencode.kefir.srv.QueryBuilder;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class EntityFilterConfigFileWriter extends ClassFileWriter
{
	public EntityFilterConfigFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getPackageName( getFilterConfigClassName(extEntity, entityClass) );
		this.className = getFilterConfigClassSimpleName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}
	protected void writeImports() throws IOException {
		writeImport(ExtEntityFilterConfig.class);
		writeImport(QueryBuilder.class);

		List<Field> searchFields = getSearchFields(entityClass);
		List<Field> filterConfigFields = getFilterConfigFields(entityClass);

		if ( !searchFields.isEmpty() || !filterConfigFields.isEmpty() )
			writeStaticImport( concat(sb, StringUtils.class.getName(), ".", CONCAT_METHOD_NAME) );

		if (hasFilterConfigNotUppercaseFields(entityClass))
			writeImport(Json.class);

		writeEnumImports();
	}
	private void writeEnumImports() throws IOException {
		// если участвуют поля-энумы, и они в другом пакете -> записать их в импорты
		Set<String> enumClassesNames = new HashSet<String>();

		for (Field searchField : getSearchFields(entityClass))
			if (searchField.getType().isEnum() && !isInPackage( searchField.getType().getName(), packageName))
				enumClassesNames.add(searchField.getType().getName());

		for (Field field : getFilterConfigFields(entityClass))
			if (field.getType().isEnum() && !isInPackage( field.getType().getName(), packageName))
				enumClassesNames.add(field.getType().getName());

		for (String enumClassName : enumClassesNames)
			writeImport(enumClassName);
	}

	protected void writeClassBody() throws IOException {
		writeClassHeader(ExtEntityFilterConfig.class);

		writeAddWhereParams();
		out.writeLn();
		writeSearchFieldsGettersAndSetters();
		writeFilterConfigFieldsGettersAndSetters();
		out.writeLn();
		writeSearchFields();
		writeFilterConfigFields();
		out.writeLn();
		writeFieldDeclaration(SB_FIELD_NAME, StringBuffer.class, concat(sb, "new ", StringBuffer.class.getSimpleName(), "()"));

		writeClassFooter();
	}
	private void writeAddWhereParams() throws IOException {
		String qbParamName = "qb";
		String entityPrefixParamName = "entityPrefix";

		out.writeLn("\tpublic void addWhereParams(", QueryBuilder.class.getSimpleName(), " ", qbParamName, ", String ", entityPrefixParamName, ") {");

		for (Field field : getSearchFields(entityClass))
		{
			writeAddSearchFieldWhereParam(field, qbParamName, entityPrefixParamName);
			out.writeLn();
		}

		for (Field field : getFilterConfigFields(entityClass))
		{
			writeAddFilterConfigFieldWhereParam(field, qbParamName, entityPrefixParamName);
			out.writeLn();
		}

		out.writeLn("\t}"); // end public void addWhereParams
	}
	private void writeAddSearchFieldWhereParam(Field field, String qbParamName, String entityPrefixParamName) throws IOException {
		SearchField searchField = getSearchFieldAnnotation(field);
		String fieldName = getSearchFieldParamName(searchField, field);
		String sqlParamName = getSearchFieldSqlParamName(searchField, field);
		Relation relation = searchField.relation();

		addWhereParam(field, qbParamName, entityPrefixParamName, fieldName, sqlParamName, relation);
	}
	private void writeAddFilterConfigFieldWhereParam(Field field, String qbParamName, String entityPrefixParamName) throws IOException {
		FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
		String fieldName = getFilterConfigFieldName(filterConfigField, field);
		String sqlParamName = getFilterConfigFieldSqlParamName(filterConfigField, field);
		Relation relation = filterConfigField.relation();

		addWhereParam(field, qbParamName, entityPrefixParamName, fieldName, sqlParamName, relation);
	}
	private void addWhereParam(Field field, String qbParamName, String entityPrefixParamName, String fieldName, String sqlParamName, Relation relation) throws IOException {
		// todo: когда будет булевский SearchField, его писать аналогичным образом
		if (isBoolean(field))
		{ // булево поле фильтрации - проверить на != null и на true
			out.writeLn("\t\tif (", fieldName, " != null && ", fieldName, ")");
		}
		else
		{ // обычное поле - проверить на неравенство null
			out.writeLn("\t\tif (", fieldName, " != null)");
		}

		String paramName = getSqlParamName(field, entityPrefixParamName, sqlParamName);
		String paramValue = getSqlParamValue(field, fieldName);

		switch (relation)
		{
			case like:
				out.writeLn(
					"\t\t\t", qbParamName, ".addLikeParam( ",
						paramName,
					 	", ", paramValue,
						", true",
					" );" // close addLikeParam
				);
				break;

			case equal:
				if (field.getType().isEnum())
				{ // special enum field treatment
					out.writeLn(
						"\t\t\t", qbParamName, ".addEqualEnumParam( ",
							paramName,
							 ", ", paramValue,
						" );" // close addEqualParam
					);
				}
				else if ( field.getType().getName().equals(String.class.getName()) )
				{
					out.writeLn(
						"\t\t\t", qbParamName, ".addEqualStringParam( ",
							paramName,
							 ", ", paramValue,
						" );" // close addEqualParam
					);
				}
				else
				{
					out.writeLn(
						"\t\t\t", qbParamName, ".addEqualParam( ",
							paramName,
							", ", paramValue,
						" );" // close addEqualParam
					);
				}

				break;

			// todo: impement other relations

			default:
				throw new IllegalArgumentException( concat(sb, relation, " sql argument relation not yet implemented") );
		}
	}
	private boolean isBoolean(Field field) {
		FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
		return filterConfigField != null && getFilterConfigFieldType(filterConfigField, field).equals(Boolean.class.getSimpleName());
	}
	private String getSqlParamName(Field field, String entityPrefixParamName, String sqlParamName) {
		// todo: такую же возможность (отменить entityPrefix) для SearchField
		FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
		if ( filterConfigField != null && !filterConfigField.addEntityPrefix() ) // не добавлять entityPrefix
			return concat(sb, "\"", sqlParamName, "\"");

		// добавить entityPrefix
		return concat(sb, CONCAT_METHOD_NAME, "(", SB_FIELD_NAME, ", ", entityPrefixParamName, ", \".", sqlParamName, "\"", ")");
	}
	private String getSqlParamValue(Field field, String fieldName) {
		FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
		if ( filterConfigField != null && !filterConfigField.sqlParamValue().isEmpty() ) // подставить явное значение
		{
			String type = getFilterConfigFieldType(filterConfigField, field);

			if (filterConfigField.sqlParamValue().equals(FilterConfigField.NULL_VALUE))
			{ // спецслучай для null - нужно привести null к String
				return "(String) null";
			}

			if ( type.equals(String.class.getSimpleName()) )
			{ // строка - обрамить значение кавычками
				return concat(sb, "\"", filterConfigField.sqlParamValue(), "\"");
			}

			// вернуть значение как есть
			return filterConfigField.sqlParamName();
		}

		// по умолчанию - значение поля FilterConfig
		return fieldName;
	}

	private void writeSearchFieldsGettersAndSetters() throws IOException {
		for (Field field : getSearchFields(entityClass))
		{
			SearchField searchField = getSearchFieldAnnotation(field);
			String fieldName = getSearchFieldParamName(searchField, field);
			writeGetter(fieldName, "String");
			writeSetter(fieldName, "String");
		}
	}
	private void writeFilterConfigFieldsGettersAndSetters() throws IOException {
		for (Field field : getFilterConfigFields(entityClass))
		{
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			String fieldName = getFilterConfigFieldName(filterConfigField, field);
			String fieldType = getFilterConfigFieldType(filterConfigField, field);

			writeGetter(fieldName, fieldType);
			writeSetter(fieldName, fieldType);
		}
	}

	private void writeSearchFields() throws IOException {
		for (Field field : getSearchFields(entityClass))
		{
			SearchField searchField = getSearchFieldAnnotation(field);
			writeFieldDeclaration(getSearchFieldParamName(searchField, field), "String");
		}

		// todo: для других типов полей поиска создавать не стринговые, а поля соответствующих типов
	}

	private void writeFilterConfigFields() throws IOException {
		for (Field field : getFilterConfigFields(entityClass))
		{
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);

			if (filterConfigField.uppercase())
			{
				writeFieldDeclaration(getFilterConfigFieldName(filterConfigField, field), getFilterConfigFieldType(filterConfigField, field));
			}
			else
			{ // не uppercase -> написать @Json(uppercase = false)
				out.writeLn("\t@", Json.class.getSimpleName(), "(uppercase = false)");
				writeFieldDeclaration(getFilterConfigFieldName(filterConfigField, field), getFilterConfigFieldType(filterConfigField, field));
				out.writeLn();
			}
		}
	}

	private ExtEntity extEntity;
	private Class entityClass;

	public static final String CONCAT_METHOD_NAME = "concat";
	public static final String EMPTY_METHOD_NAME = "empty";

	private static final String SB_FIELD_NAME = "sb";
}