/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 06.04.2012 11:40:50$
*/
package su.opencode.kefir.gen.project.xml.orm;

import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static su.opencode.kefir.gen.project.sql.SqlTableField.isReservedWord;
import static su.opencode.kefir.gen.project.xml.XmlFileWriter.XML_FILE_ENCODING;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.empty;
import static su.opencode.kefir.util.StringUtils.hasUpperCaseChars;

public abstract class EntityMappingAppender extends Appender
{
	public EntityMappingAppender(String filePath) {
		this.filePath = filePath;
	}
	public EntityMappingAppender(String filePath, ProjectConfig config) {
		this.filePath = filePath;
		this.config = config;
	}

	public void appendEntityMapping() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_ENTITY_MAPPING_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendEntityMapping(appendedFileLines, fileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	protected abstract void appendEntityMapping(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException;

	protected void startEntity(List<String> fileLines, String className) {
		fileLines.add(getStartEntityString(className));
	}
	protected void startEntity(List<String> fileLines, Class entityClass) {
		fileLines.add(getStartEntityString(entityClass.getName()));
	}
	protected String getStartEntityString(String className) {
		return concat(sb, "\t<", ENTITY_ELEMENT_NAME, " class=\"", className, "\">");
	}
	protected String getStartEntityString(Class entityClass) {
		return getStartEntityString(entityClass.getName());
	}

	protected void endEntity(List<String> fileLines) {
		fileLines.add( concat(sb, "\t</", ENTITY_ELEMENT_NAME, ">") );
	}

	protected void appendTable(List<String> fileLines, String tableName) {
		fileLines.add( concat(sb, "\t\t<", TABLE_ELEMENT_NAME, " name=\"", tableName, "\"/>") );
	}

	protected void appendSequenceGenerator(List<String> fileLines, String name, String sequenceName) {
		fileLines.add( concat(sb, "\t\t<", SEQUENCE_GENERATOR_ELEMENT_NAME, " name=\"", name, "\" sequence-name=\"", sequenceName, "\"/>") );
	}

	protected void startAttributes(List<String> fileLines) {
		fileLines.add( concat(sb, "\t\t<", ATTRIBUTES_ELEMENT_NAME, ">") );
	}
	protected void endAttributes(List<String> fileLines) {
		fileLines.add( concat(sb, "\t\t</", ATTRIBUTES_ELEMENT_NAME, ">") );
	}

	protected void appendId(List<String> fileLines, String idName, String generatorName) {
		fileLines.add( concat(sb, "\t\t\t<", ID_ELEMENT_NAME, " name=\"", idName, "\"><", GENERATED_VALUE_ELEMENT_NAME, " strategy=\"AUTO\" generator=\"", generatorName, "\"/></", ID_ELEMENT_NAME, ">") );
	}
	protected void appendId(List<String> fileLines, String generatorName) {
		appendId(fileLines, ID_FIELD_NAME, generatorName);
	}

	protected void appendManyToOne(List<String> fileLines, String fieldName, String joinColumnName) {
		fileLines.add( concat(sb, "\t\t\t<", MANY_TO_ONE_ELEMENT_NAME, " name=\"", fieldName, "\"><", JOIN_COLUMN_ELEMENT_NAME, " name=\"", joinColumnName, "\"/></", MANY_TO_ONE_ELEMENT_NAME, ">"));
	}
	protected void appendOneToOne(List<String> fileLines, String fieldName, String joinColumnName) { // todo: возможность передавать mappedBy
		fileLines.add( concat(sb, "\t\t\t<", ONE_TO_ONE_ELEMENT_NAME, " name=\"", fieldName, "\"><", JOIN_COLUMN_ELEMENT_NAME, " name=\"", joinColumnName, "\"/></", ONE_TO_ONE_ELEMENT_NAME, ">"));
	}

	protected void appendBasic(List<String> fileLines, String fieldName, String columnName) {
		fileLines.add( concat(sb, "\t\t\t<", BASIC_ELEMENT_NAME, " name=\"", fieldName, "\"><", COLUMN_ELEMENT_NAME, " name=\"", columnName, "\"/></", BASIC_ELEMENT_NAME, ">") );
	}

	protected void appendLobBasic(List<String> fileLines, String fieldName, String columnName) {
		if ( empty(columnName) ) // do not add column name
			fileLines.add( concat(sb, "\t\t\t<", BASIC_ELEMENT_NAME, " name=\"", fieldName, "\"><", LOB_ELEMENT_NAME, "/></", BASIC_ELEMENT_NAME, ">") );
		else // add column name
			fileLines.add( concat(sb, "\t\t\t<", BASIC_ELEMENT_NAME, " name=\"", fieldName, "\"><", COLUMN_ELEMENT_NAME, " name=\"", columnName, "\"/><", LOB_ELEMENT_NAME, "/></", BASIC_ELEMENT_NAME, ">") );
	}

	protected void appendBasic(List<String> fileLines, String fieldName) {
		if ( isReservedWord(fieldName) )
		{ // специальные названия полей, которые обрамляются кавычками как имена sql полей
			appendBasic(fileLines, fieldName, getQuotedSqlColumnName(fieldName));
			return;
		}

		if ( !hasUpperCaseChars(fieldName) ) // поле без больших букв называется так же, как sql поле
			return;

		appendBasic(fileLines, fieldName, getSqlColumnName(fieldName));
	}
	protected void appendLobBasic(List<String> fileLines, String fieldName) {
		if ( isReservedWord(fieldName) )
		{ // специальные названия полей, которые обрамляются кавычками как имена sql полей
			appendLobBasic(fileLines, fieldName, getQuotedSqlColumnName(fieldName));
			return;
		}

		if ( !hasUpperCaseChars(fieldName) ) // поле без больших букв называется так же, как sql поле -> Добавить только Lob
		{
			appendLobBasic(fileLines, fieldName, null);
			return;
		}

		appendLobBasic(fileLines, fieldName, getSqlColumnName(fieldName));
	}
	protected void appendEnumeratedBasic(List<String> fileLines, String fieldName, String enumFieldColumnName) {
		String columnName = enumFieldColumnName;

		if ( isReservedWord(fieldName) )
		{ // специальные названия полей, которые обрамляются кавычками как имена sql полей
			columnName = getQuotedSqlColumnName(fieldName);
		}

		if ( !hasUpperCaseChars(fieldName) ) // поле без больших букв называется так же, как sql поле
			columnName = null;

		String columnNameElement = isEmpty(columnName) ? "" : concat(sb, "<", COLUMN_ELEMENT_NAME, " name=\"", columnName, "\"/>");
		String enumeratedElement = concat(sb, "<", ENUMERATED_ELEMENT_NAME, ">", ENUMERATED_ELEMENT_VALUE, "</", ENUMERATED_ELEMENT_NAME, ">");

		fileLines.add( concat(sb, "\t\t\t<", BASIC_ELEMENT_NAME, " name=\"", fieldName, "\">", columnNameElement, enumeratedElement, "</", BASIC_ELEMENT_NAME, ">") );
	}

	public static String getQuotedSqlColumnName(String fieldName) {
		return concat(XML_QUOTE_SYMBOL, fieldName, XML_QUOTE_SYMBOL);
	}
	public static String getSqlColumnName(String fieldName) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < fieldName.length(); i++)
		{
			char ch = fieldName.charAt(i);

			if ( i != 0 && Character.isUpperCase(ch) ) // перед первым символом в строке подчеркивание не ставится
				sb.append(SQL_COLUMN_NAME_SEPARATOR).append( Character.toLowerCase(ch) );
			else
				sb.append( Character.toLowerCase(ch) );
		}

		return sb.toString();
	}
	public static String getSqlColumnName(Field field) {
		return getSqlColumnName(field.getName());
	}
	public static String getSqlTableName(String className) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < className.length(); i++)
		{
			char ch = className.charAt(i);

			if ( i != 0 && Character.isUpperCase(ch) ) // перед первым символом в строке подчеркивание не ставится
				sb.append(SQL_TABLE_NAME_SEPARATOR).append( Character.toLowerCase(ch));
			else
				sb.append(ch);
		}

		return sb.toString();
	}
	public static String getSqlTableName(Class entityClass) {
		return getSqlTableName(entityClass.getSimpleName());
	}

	public static String getSqlSequenceName(String fieldName) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < fieldName.length(); i++)
		{
			char ch = fieldName.charAt(i);

			if ( i != 0 && Character.isUpperCase(ch) ) // перед первым символом в строке подчеркивание не ставится
				sb.append(SQL_SEQUENCE_NAME_SEPARATOR).append( Character.toLowerCase(ch) );
			else
				sb.append( Character.toLowerCase(ch) );
		}

		sb.append(SQL_SEQUENCE_NAME_POSTFIX); // add _gen
		return sb.toString();
	}
	public static String getSqlSequenceName(Class entityClass) {
		return getSqlSequenceName(entityClass.getSimpleName());
	}

	@Override
	protected String getEncoding() {
		return XML_FILE_ENCODING;
	}

	protected String filePath;
	protected ProjectConfig config;

	public static final String ID_FIELD_NAME = "id";

	public static final String ENTITY_ELEMENT_NAME = "entity";
	public static final String TABLE_ELEMENT_NAME = "table";
	public static final String SEQUENCE_GENERATOR_ELEMENT_NAME = "sequence-generator";
	public static final String ATTRIBUTES_ELEMENT_NAME = "attributes";
	public static final String GENERATED_VALUE_ELEMENT_NAME = "generated-value";
	public static final String ID_ELEMENT_NAME = "id";
	public static final String BASIC_ELEMENT_NAME = "basic";
	public static final String COLUMN_ELEMENT_NAME = "column";
	public static final String LOB_ELEMENT_NAME = "lob";
	public static final String ENUMERATED_ELEMENT_NAME = "enumerated";
	public static final String ENUMERATED_ELEMENT_VALUE = "STRING";
	public static final String MANY_TO_ONE_ELEMENT_NAME = "many-to-one";
	public static final String ONE_TO_ONE_ELEMENT_NAME = "one-to-one";
	public static final String JOIN_COLUMN_ELEMENT_NAME = "join-column";

	public static final String SQL_COLUMN_NAME_SEPARATOR = "_";
	public static final String SQL_TABLE_NAME_SEPARATOR = "_";
	public static final String SQL_SEQUENCE_NAME_SEPARATOR = "_";
	public static final String SQL_SEQUENCE_NAME_POSTFIX = "_gen";
	public static final String ORM_SEQUENCE_NAME_POSTFIX = "Generator";
	public static final String ID_POSTFIX = "_id";

	public static final String XML_QUOTE_SYMBOL = "&quot;";
	public static final String APPEND_ENTITY_MAPPING_MARKER = "${APPEND_ENTITY_MAPPING}";
}