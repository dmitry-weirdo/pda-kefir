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
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.*;
import su.opencode.kefir.srv.renderer.RenderersFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static su.opencode.kefir.gen.ExtEntityUtils.getExtEntityAnnotation;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.field.FieldSets.FIELD_SETS_PROPERTY_NAME;
import static su.opencode.kefir.gen.fileWriter.ClassAnnotation.FIELDS_SEPARATOR_SIMPLE;
import static su.opencode.kefir.gen.fileWriter.ClassAnnotation.NEW_LINE;
import static su.opencode.kefir.srv.json.ColumnGroups.GROUPS_PROPERTY_NAME;
import static su.opencode.kefir.srv.json.ViewConfig.VIEW_CONFIG_PROPERTY_NAME;
import static su.opencode.kefir.srv.renderer.RenderersFactory.getRendererConstantName;
import static su.opencode.kefir.util.ObjectUtils.*;
import static su.opencode.kefir.util.StringUtils.*;

public class VOClassFileWriter extends ClassFileWriter
{
	public VOClassFileWriter(String baseDir, String packageName, String className, ExtEntity extEntity, Class entityClass, Class renderersClass) {
		super(baseDir, packageName, className);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
		this.renderersClass = renderersClass;
		this.packageName = ExtEntityUtils.getListVOClassPackageName(extEntity, entityClass);
		this.className = ExtEntityUtils.getListVOClassSimpleName(extEntity, entityClass);
		this.failIfFileExists = false; // если VO класс уже есть, не перезаписывать его
	}
	@Override
	protected void writeImports() throws IOException {
		writeStaticImportAll(RenderersFactory.class);

		Set<String> imports = new HashSet<>();
		imports.add(VO.class.getName());
		imports.add(ColumnModel.class.getName());

		if (hasColumnGroupsAnnotation(entityClass))
		{
			imports.add(ColumnGroups.class.getName());
			imports.add(ColumnGroup.class.getName());
		}
		if (hasFieldSetsAnnotation(entityClass))
		{
			imports.add(FieldSets.class.getName());
			imports.add(FieldSet.class.getName());
		}
		if (hasViewConfigAnnotation(entityClass))
		{
			imports.add(ViewConfig.class.getName());
		}

		imports.add(entityClass.getName());

		for (Field field : entityClass.getDeclaredFields())
		{
			if (!hasAttachmentsFieldAnnotation(field))
			{
				imports.add(field.getType().getName());
				if (hasChooseFieldAnnotation(field) || hasComboBoxFieldAnnotation(field))
				{
					if (!hasColumnModelExcludeAnnotation(field))
					{
						String fieldVOClassName = ExtEntityUtils.getListVOClassName(getExtEntityAnnotation(field.getType()), field.getType());
						imports.add(fieldVOClassName);

						for (VOField voField : getVOFields(field))
						{
							imports.add(getVOFieldClassName(voField, field));
						}
					}
				}
				if (hasEnumFieldAnnotation(field.getType()))
				{
					writeStaticImportAll(renderersClass.getName());
				}
			}
		}

		for (String className : imports)
			writeImport(className);
	}

	@Override
	protected void writeClassBody() throws IOException {
		writeColumnGroupsAnnotation();
		writeFieldSetsAnnotation();
		writeViewConfigAnnotation();
		writeClassHeader(VO.class);
		writeConstructors();
		writeGettersAndSetters();
		writeFields();
		writeClassFooter();
	}
	private void writeColumnGroupsAnnotation() throws IOException {
		if (!hasColumnGroupsAnnotation(entityClass))
			return;

		List<String> lines = new ArrayList<>();
		lines.add(concat("@", ColumnGroups.class.getSimpleName(), "(", GROUPS_PROPERTY_NAME, " = {\n"));

		groupIds.clear();
		for (ColumnGroup columnGroup : getColumnGroupsAnnotation(entityClass).groups())
		{
			ClassAnnotation classAnnotation = new ClassAnnotation(ColumnGroup.class);
			classAnnotation.putString("id", columnGroup.id());
			classAnnotation.putString("header", columnGroup.header());
			lines.add(concat(sb, TAB, classAnnotation, FIELDS_SEPARATOR_SIMPLE, NEW_LINE));

			if (groupIds.contains(columnGroup.id()))
				throw new IllegalStateException(concat(sb, "Column group id \"", columnGroup.id(), "\" is present more than once in ", ColumnGroups.class.getName(), " annotation in class ", entityClass.getName()));

			groupIds.add(columnGroup.id());
		}

		String lastLine = lines.get(lines.size() - 1);
		String lastLineWithoutComma = lastLine.substring(0, lastLine.length() - FIELDS_SEPARATOR_SIMPLE.length() - NEW_LINE.length());
		lines.remove(lines.size() - 1);
		lines.add(lastLineWithoutComma);
		lines.add(NEW_LINE);
		lines.add("})\n");

		out.write(lines.toArray());
	}
	private void writeFieldSetsAnnotation() throws IOException {
		if (!hasFieldSetsAnnotation(entityClass))
			return;

		List<String> lines = new ArrayList<>();
		lines.add(concat("@", FieldSets.class.getSimpleName(), "(", FIELD_SETS_PROPERTY_NAME, " = {\n"));

		fieldSetIds.clear();
		for (FieldSet fieldSet : getFieldSetsAnnotation(entityClass).fieldSets())
		{
			ClassAnnotation classAnnotation = new ClassAnnotation(FieldSet.class);
			classAnnotation.putString("id", fieldSet.id());
			classAnnotation.putString("title", fieldSet.title());
			lines.add(concat(sb, TAB, classAnnotation, FIELDS_SEPARATOR_SIMPLE, NEW_LINE));

			if (fieldSetIds.contains(fieldSet.id()))
				throw new IllegalStateException(concat(sb, "Field set id \"", fieldSet.id(), "\" is present more than once in ", FieldSets.class.getName(), " annotation in class ", entityClass.getName()));

			fieldSetIds.add(fieldSet.id());
		}

		String lastLine = lines.get(lines.size() - 1);
		String lastLineWithoutComma = lastLine.substring(0, lastLine.length() - FIELDS_SEPARATOR_SIMPLE.length() - NEW_LINE.length());
		lines.remove(lines.size() - 1);
		lines.add(lastLineWithoutComma);
		lines.add(NEW_LINE);
		lines.add("})\n");

		out.write(lines.toArray());
	}
	private void writeViewConfigAnnotation() throws IOException{
		if (!hasViewConfigAnnotation(entityClass))
			return;

		ViewConfig viewConfig = getViewConfigAnnotation(entityClass);
		out.writeLn(concat(sb, "@", ViewConfig.class.getSimpleName(), "(", VIEW_CONFIG_PROPERTY_NAME, " = \"", viewConfig.viewConfig(), "\")"));
		out.writeLn();
	}
	private void writeConstructors() throws IOException {
		writeEmptyConstructor();
		writeConstructorWithExtEntityArgument();
	}
	private void writeConstructorWithExtEntityArgument() throws IOException {
		String paramName = ExtEntityUtils.getSimpleName(entityClass);
		String arguments = concat(sb, entityClass.getSimpleName(), " ", paramName);
		String superCall = concat(sb, "super(", paramName, ");");
		List<String> bodyStrings = new ArrayList<>();
		bodyStrings.add(superCall);
		bodyStrings.add("");

		for (Field field : entityClass.getDeclaredFields())
		{
			if (hasChooseFieldAnnotation(field) || hasComboBoxFieldAnnotation(field))
			{
				VOFieldsTree tree = new VOFieldsTree(field, paramName, !hasColumnModelExcludeAnnotation(field));
				tree.fill(getVOFields(field));
				bodyStrings.addAll(tree.getFileLines());
				bodyStrings.add(""); // empty string after assigning fields of linked entity
			}
		}

		writeConstructorWithArguments(arguments, bodyStrings.toArray());
	}

	private void writeGettersAndSetters() throws IOException {
		writeGettersAndSettersDefinedByEntityFields();
		writeGettersAndSettersDefinedByColumnModelAnnotationsOnGetters();
	}
	private void writeGettersAndSettersDefinedByEntityFields() throws IOException {
		for (Field field : entityClass.getDeclaredFields())
		{
			if (isStatic(field) || hasAttachmentsFieldAnnotation(field))
			{ // static fields and attachment fields are not added to VO
				continue;
			}

			if (hasChooseFieldAnnotation(field) || hasComboBoxFieldAnnotation(field))
			{
				if (!hasColumnModelExcludeAnnotation(field))
				{
					ExtEntity chooseFieldExtEntityAnnotation = getExtEntityAnnotation(field.getType());
					String fieldVOClassName = ExtEntityUtils.getListVOClassSimpleName(chooseFieldExtEntityAnnotation, field.getType());
					writeGetterAndSetter(field.getName(), fieldVOClassName);
				}

				for (VOField voField : getVOFields(field))
					writeGetterAndSetter(getVOFieldName(voField, field), getVOFieldClassSimpleName(voField, field));

				continue;
			}
			writeGetterAndSetter(field);

			if (hasAddressFieldAnnotation(field)) // для адресного поля помимо одноименного поля добавляется str поле, содержащее строковое представление адреса
				writeGetterAndSetter(getAddressFieldStrFieldName(field), String.class);
		}
	}
	private String getAddressFieldStrFieldName(Field field) {
		return concat( sb, field.getName(), "Str" );
	}
	private void writeGettersAndSettersDefinedByColumnModelAnnotationsOnGetters() throws IOException {
		for (Method method : entityClass.getDeclaredMethods())
			if (hasColumnModelAnnotation(method))
				writeGetterAndSetter( getFieldNameByGetter(method), method.getReturnType() );
	}

	private void writeFields() throws IOException {
		writeVOFieldsDefinedByEntityFields();
		writeVOFieldsDefinedByColumnModelAnnotationsOnGetters();
	}
	private void writeVOFieldsDefinedByEntityFields() throws IOException {
		for (Field field : entityClass.getDeclaredFields())
		{
			if ( isStatic(field) || hasAttachmentsFieldAnnotation(field) )
			{ // static fields and attachment fields are not added to VO
				continue;
			}

			out.writeLn(); // empty string before field declaration

			if (hasIdFieldAnnotation(field))
			{
				writeFieldDeclaration(field);
				continue;
			}

			if (hasChooseFieldAnnotation(field) || hasComboBoxFieldAnnotation(field))
			{
				writeChooseFieldFields(field);
				continue;
			}

			if (hasAddressFieldAnnotation(field))
			{
				writeAddressFieldFields(field);
				continue;
			}

			if (hasFieldAnnotation(field))
			{
				if (!hasColumnModelExcludeAnnotation(field) && !hasEnumFieldAnnotation(field))
					writeColumnModelAnnotation(getFieldColumnHeader(field), getFieldColumnRenderer(field), getFieldSortable(field), getFieldSortParam(field), getFieldGroupId(field), getFieldTooltip(field));

				if (hasEnumFieldAnnotation(field.getType()))
				{
					String renderersConstantName = getEnumFieldRenderersConstantName(getEnumFieldAnnotation(field.getType()), field.getType());
					writeColumnModelAnnotation(getFieldColumnHeader(field), renderersConstantName, null);
				}

				writeFieldDeclaration(field);
				continue;
			}

			if (hasColumnModelAnnotation(field))
			{
				ColumnModel columnModel = ExtEntityFieldsUtils.getColumnModelAnnotation(field);
				writeColumnModelAnnotation(columnModel);
				writeFieldDeclaration(getFieldName(field), field.getType());
				out.writeLn();
				continue;
			}

			// просто поле сущности - переносится без ColumnModel, как есть
			writeFieldDeclaration(field);
		}
	}

	private void writeVOFieldsDefinedByColumnModelAnnotationsOnGetters() throws IOException {
		for (Method method : entityClass.getDeclaredMethods())
		{
			if (hasColumnModelAnnotation(method))
			{
				out.writeLn();
				ColumnModel columnModel = ExtEntityFieldsUtils.getColumnModelAnnotation(method);
				writeColumnModelAnnotation(columnModel);
				writeFieldDeclaration(getFieldNameByGetter(method), method.getReturnType());
			}
		}
	}

	private void writeAddressFieldFields(Field field) throws IOException {
		writeFieldDeclaration(field);// Address поле (без аннотации)
		out.writeLn();

		if (!hasColumnModelExcludeAnnotation(field))
			writeColumnModelAnnotation(getFieldColumnHeader(field), null, false);

		writeFieldDeclaration(getAddressFieldStrFieldName(field), String.class); // str поле
		out.writeLn();
	}
	private void writeChooseFieldFields(Field field) throws IOException {
		out.writeLn();

		if (!hasColumnModelExcludeAnnotation(field))
			writeChooseFieldField(field);

		writeChooseFieldVOFields(field);
	}
	private void writeChooseFieldField(Field field) throws IOException {
		String fieldVOClassName = ExtEntityUtils.getListVOClassSimpleName(getExtEntityAnnotation(field.getType()), field.getType());
		writeFieldDeclaration(field.getName(), fieldVOClassName);
	}
	private void writeChooseFieldVOFields(Field field) throws IOException {
		for (VOField voField : getVOFields(field))
		{
			out.writeLn();
			writeVOFieldColumnModelAnnotation(voField, field);
			writeFieldDeclaration(getVOFieldName(voField, field), getVOFieldClassSimpleName(voField, field));
		}
		out.writeLn();
	}
	private void writeVOFieldColumnModelAnnotation(VOField voField, Field field) throws IOException {
		String header = getVOFieldHeader(voField, field);
		String renderer = getVOFieldRenderer(voField, field);
		Boolean sortable = getVOFieldSortable(voField, field);
		String sortParam = getVOFieldSortParam(voField, field);
		String tooltip = getVOFieldTooltip(voField, field);

		writeColumnModelAnnotation(header, renderer, sortable, sortParam, voField.groupId(), tooltip);
	}
	private String getVOFieldHeader(VOField voField, Field field) {
		if (notEmpty(voField.header()))
			return voField.header();

		Object chooseEntityFieldOrMethod = getChooseEntityField(field, voField.name());

		if (chooseEntityFieldOrMethod instanceof Field)
			return getFieldColumnHeader((Field) chooseEntityFieldOrMethod);

		if (chooseEntityFieldOrMethod instanceof Method)
			return getHeader((Method) chooseEntityFieldOrMethod);

		throw new IllegalArgumentException( concat(sb, "Object for VoFieldHeader for field ", voField.name(), " is neither ", Field.class.getName(), " nor ", Method.class.getName()) );
	}
	private String getVOFieldRenderer(VOField voField, Field field) {
		if ( notEmpty(voField.renderer()) )
			return voField.renderer();

		Object chooseEntityFieldOrMethod = getChooseEntityField(field, voField.name());

		if (chooseEntityFieldOrMethod instanceof Field)
			return getFieldColumnRenderer((Field) chooseEntityFieldOrMethod);

		if (chooseEntityFieldOrMethod instanceof Method)
			return getRenderer((Method) chooseEntityFieldOrMethod);

		throw new IllegalArgumentException( concat(sb, "Object for VoFieldRenderer for field ", voField.name(), " is neither ", Field.class.getName(), " nor ", Method.class.getName()) );
	}
	private Boolean getVOFieldSortable(VOField voField, Field field) {
		if (voField.sortable() != ColumnModel.DEFAULT_SORTABLE)
			return voField.sortable();

		Object chooseEntityFieldOrMethod = getChooseEntityField(field, voField.name());

		if (chooseEntityFieldOrMethod instanceof Field)
			return getFieldSortable((Field) chooseEntityFieldOrMethod);

		if (chooseEntityFieldOrMethod instanceof Method)
			return getSortable((Method) chooseEntityFieldOrMethod);

		throw new IllegalArgumentException( concat(sb, "Object for VoFieldSortable for field ", voField.name(), " is neither ", Field.class.getName(), " nor ", Method.class.getName()) );
	}
	private String getVOFieldSortParam(VOField voField, Field field) {
		if ( notEmpty(voField.sortParam()) )
			return voField.sortParam();

		return concat(sb, field.getName(), ".", voField.name()); // по умолчанию - (имя поля + "." + имя поля связанной сущности)
	}
	private String getVOFieldTooltip(VOField voField, Field field) {
		if (notEmpty(voField.tooltip()))
			return voField.tooltip();

		Object chooseEntityFieldOrMethod = getChooseEntityField(field, voField.name());

		if (chooseEntityFieldOrMethod instanceof Field)
			return getFieldTooltip((Field) chooseEntityFieldOrMethod);

		if (chooseEntityFieldOrMethod instanceof Method)
			return getTooltip((Method) chooseEntityFieldOrMethod);

		throw new IllegalArgumentException(concat(sb, "Object for VoFieldTooltip for field ", voField.name(), " is neither ", Field.class.getName(), " nor ", Method.class.getName()));
	}

	private Object getChooseEntityField(Field field, String fieldName) {
		String[] names = fieldName.split("\\.");

		Field entityField = field;
		Class entityFieldType = field.getType();
		Method getter;

		Object result = null;

		for (int i = 0, namesLength = names.length; i < namesLength; i++)
		{
			String name = names[i];

			try
			{
				entityField = getField(entityFieldType, name);
				entityFieldType = entityField.getType();

				if ( i == (names.length - 1) )
					result = entityField;
			}
			catch (Exception e)
			{
				getter = getMethod(entityFieldType, getGetterName(name));
				entityFieldType = getter.getReturnType();
				logger.info(concat(sb, "Field \"", name, "\" not found in class ", entityFieldType.getName(), ". Using ", getter.getName(), " method"));

				if ( i == (names.length - 1) )
					result = getter;
			}
		}

		return result;
	}

	private VOField[] getVOFields(Field field) {
		if (hasChooseFieldAnnotation(field))
			return getChooseFieldAnnotation(field).voFields();

		if (hasComboBoxFieldAnnotation(field))
			return getComboBoxFieldAnnotation(field).voFields();

		throw new IllegalArgumentException(concat(sb, "Field \"", field.getName(), "\" in class ", entityClass.getName(), " has neither ", ChooseField.class.getName(), " nor ", ComboBoxField.class.getName(), " annotation"));
	}
	public static String getVOFieldName(VOField voField, Field field) {
		String defaultFieldName = getDefaultVOFieldName(voField, field);
		String voFieldName = getValue(voField.voFieldName(), defaultFieldName);
		logger.info(concat( "VO field name: \"", voFieldName, "\""));
		return voFieldName;
	}
	public static String getDefaultVOFieldName(VOField voField, Field field) {
		StringBuffer sb = new StringBuffer();
		sb.append(field.getName());

		for (String name : voField.name().split("\\."))
			sb.append(capitalize(name));

		return sb.toString();
	}

	private String getVOFieldClassSimpleName(VOField voField, Field field) {
		String defaultClassName = getChooseEntityFieldClass(voField, field).getSimpleName();

		String voFieldClassName = getValue(voField.voFieldClassName(), defaultClassName);
		logger.info( concat(sb, "VO field class name: ", voFieldClassName) );
		return voFieldClassName;
	}
	private String getVOFieldClassName(VOField voField, Field field) {
		String defaultClassName = getChooseEntityFieldClass(voField, field).getName();

		String voFieldClassName = getValue(voField.voFieldClassName(), defaultClassName);
		logger.info(concat(sb, "VO field class name: ", voFieldClassName));
		return voFieldClassName;
	}
	private Class getChooseEntityFieldClass(VOField voField, Field field) {
		Object chooseEntityFieldOrMethod = getChooseEntityField(field, voField.name());

		if (chooseEntityFieldOrMethod instanceof Field)
			return ((Field) chooseEntityFieldOrMethod).getType();

		if (chooseEntityFieldOrMethod instanceof Method)
			return ((Method) chooseEntityFieldOrMethod).getReturnType();

		throw new IllegalArgumentException( concat(sb, "Object for VoFieldHeader for field ", voField.name(), " is neither ", Field.class.getName(), " nor ", Method.class.getName()) );
	}
	private String getVOFieldColumnHeader(VOField voField, Field field) {
		Field chooseEntityField = getField(field.getType(), voField.name());
		String defaultHeader = getFieldColumnHeader(chooseEntityField);

		String voFieldColumnHeader = getValue(voField.header(), defaultHeader);
		logger.info(concat(sb, "VO field column header: \"", voFieldColumnHeader, "\""));
		return voFieldColumnHeader;
	}

	protected void writeColumnModelAnnotation(ColumnModel columnModel) throws IOException {
		out.writeLn(TAB, getColumnModelAnnotation(columnModel.header(), getRendererConstantName(columnModel.renderer()), columnModel.sortable(), columnModel.sortParam(), columnModel.groupId(), columnModel.tooltip()));
	}
	protected void writeColumnModelAnnotation(String header, String renderer, Boolean sortable) throws IOException {
		out.writeLn(TAB, getColumnModelAnnotation(header, renderer, sortable, null, null, null));
	}
	protected void writeColumnModelAnnotation(String header, String renderer, Boolean sortable, String sortParam, String groupId, String tooltip) throws IOException {
		out.writeLn(TAB, getColumnModelAnnotation(header, renderer, sortable, sortParam, groupId, tooltip));
	}
	private ClassAnnotation getColumnModelAnnotation(String header, String renderer, Boolean sortable, String sortParam, String groupId, String tooltip) {
		ClassAnnotation annotation = new ClassAnnotation(ColumnModel.class);

		if ( notEmpty(header) )
			annotation.putString("header", header);

		if ( notEmpty(renderer) )
			annotation.put("renderer", renderer);

		if ( sortable != null )
			annotation.put("sortable", sortable);

		if ( notEmpty(sortParam) )
			annotation.putString("sortParam", sortParam);

		if ( notEmpty(groupId) )
		{
			if (!groupIds.contains(groupId))
				throw new IllegalStateException(concat(sb, "Group id \"", groupId, "\" is not present in ", ColumnGroups.class.getName(), " annotation in class ", entityClass.getName()));

			annotation.putString("groupId", groupId);
		}

		if ( notEmpty(tooltip) )
			annotation.putString("tooltip", tooltip);

		return annotation;
	}

	protected ExtEntity extEntity;
	private Class entityClass;
	private Class renderersClass;
	private List<String> groupIds = new ArrayList<>();
	private List<String> fieldSetIds = new ArrayList<>();
}