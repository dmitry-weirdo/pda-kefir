/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 28.03.2012 12:57:05$
*/
package su.opencode.kefir.gen.project.sql;

import su.opencode.kefir.gen.fileWriter.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.project.sql.SqlFileWriter.*;
import static su.opencode.kefir.gen.project.sql.SqlTableField.INTEGER_TYPE;
import static su.opencode.kefir.gen.project.sql.SqlTableField.getIdField;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public class SqlTable
{
	public SqlTable(String name) {
		this.name = name;
	}
	public SqlTable(Class entityClass) {
		this.name = getSqlTableName(name);
	}
	public SqlTable(Class entityClass, String comment) {
		this.name = getSqlTableName(entityClass);
		this.comment = comment;
	}
	public SqlTable(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

	public String getGeneratorName() {
		return generatorName;
	}
	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}
	public void setGeneratorName(Class entityClass) {
		this.generatorName = SqlFileWriter.getGeneratorName(entityClass);
	}

	public void addField(SqlTableField field) {
		fields.add(field);
	}

	/**
	 * Добавляет целое поле id, которое является primary key.
	 */
	public void addIdField() {
		this.addField( getIdField() );
		setPrimaryKey( new SqlTablePrimaryKey() );
	}
	/**
	 * Добавляет целое поле id с заданным названием, которое является primary key.
	 * @param fieldName имя id-поля
	 */
	public void addIdField(String fieldName) {
		this.addField( new SqlTableField(fieldName, INTEGER_TYPE, true) );
		setPrimaryKey( new SqlTablePrimaryKey(fieldName) );
	}

	public SqlTablePrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(SqlTablePrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void addForeignKey(SqlTableForeignKey foreignKey) {
		foreignKeys.add(foreignKey);
	}

	public void addUnique(SqlTableUnique unique) {
		uniques.add(unique);
	}

	public void writeCreateTable(FileWriter out) throws IOException {
		if ( notEmpty(comment) )
			out.writeLn(SQL_COMMENT_PREFIX, comment);

		out.writeLn(getCreateTableString(name));
		out.writeLn("(");

		writeFields(out);
		out.writeLn();
		writeKeys(out);

		out.writeLn(");");

		if (generatorName != null)
			writeGenerator(out);

		out.writeLn(); // empty line after create table
	}
	public void writeCreateTable(List<String> fileLines) throws IOException {
		if ( notEmpty(comment) )
			fileLines.add( concat(sb, SQL_COMMENT_PREFIX, comment) );

		fileLines.add(getCreateTableString(name));
		fileLines.add("(");

		writeFields(fileLines);
		fileLines.add("");
		writeKeys(fileLines);

		fileLines.add(");");

		if (generatorName != null)
			writeGenerator(fileLines);

		fileLines.add(""); // empty line after create table
	}
	public static String getCreateTableString(String tableName) {
		return concat("create table ", tableName);
	}

	private void writeFields(FileWriter out) throws IOException {
		String longestFieldName = getLongestFieldName();

		for (SqlTableField field : fields) // 	login           varchar(32) not null,
			out.writeLn(getFieldStr(longestFieldName, field));
	}
	private void writeFields(List<String> fileLines) throws IOException {
		String longestFieldName = getLongestFieldName();

		for (SqlTableField field : fields) // 	login           varchar(32) not null,
			fileLines.add(getFieldStr(longestFieldName, field));
	}
	private String getFieldStr(String longestFieldName, SqlTableField field) {
		return concat(sb, "\t", field.getName(), getSpaces(field.getName(), longestFieldName), field.getType(), field.getNotNullStr(), ",");
	}

	private void writeKeys(FileWriter out) throws IOException {
		out.writeLn(getKeysStr());
	}
	private void writeKeys(List<String> fileLines) throws IOException {
		fileLines.add(getKeysStr());
	}
	private String getKeysStr() {
		List<Object> keys = new ArrayList<Object>();

		if (primaryKey != null)
			keys.add(primaryKey);

		keys.addAll(foreignKeys);
		keys.addAll(uniques);

		sb.delete(0, sb.length());

		for (Object key : keys)
		{
			sb.append("\t").append(key).append(KEYS_SEPARATOR);
		}

		if (sb.length() > 0) // remove trailing comma
			sb.delete(sb.length() - KEYS_SEPARATOR.length(), sb.length());

		return sb.toString();
	}
	private void writeGenerator(FileWriter out) throws IOException {
		out.writeLn( getCreateGeneratorStr(sb, generatorName) );
		out.writeLn( getSetGeneratorStr(sb, generatorName, DEFAULT_GENERATOR_VALUE) );
	}
	private void writeGenerator(List<String> fileLines) throws IOException {
		fileLines.add( getCreateGeneratorStr(sb, generatorName) );
		fileLines.add( getSetGeneratorStr(sb, generatorName, DEFAULT_GENERATOR_VALUE) );
	}

	private String getLongestFieldName() {
		String longestName = "";

		for (SqlTableField field : fields)
			if (field.getName().length() > longestName.length())
				longestName = field.getName();

		return longestName;
	}
	private String getSpaces(String fieldName, String longestFieldName) {
		int spacesCount = longestFieldName.length() - fieldName.length() + 2;

		sb.delete(0, sb.length());
		for (int i = 0; i < spacesCount; i++)
			sb.append(" ");

		return sb.toString();
	}


	/**
	 * Комментарий перед create table
	 * Если равен <code>null</code> или пуст, то в скрипт не пишется.
	 */
	private String comment;

	/**
	 * Название таблицы
	 */
	private String name;

	/**
	 * Название генератора.
	 * Если указано, то после создания таблицы пишется создание генератора и установка его в 0.
	 */
	private String generatorName;

	/**
	 * Список полей таблицы.
	 */
	private List<SqlTableField> fields = new ArrayList<SqlTableField>();

	/**
	 * Главный ключ таблицы.
	 */
	private SqlTablePrimaryKey primaryKey;

	/**
	 * Foreign keys таблицы.
	 */
	private List<SqlTableForeignKey> foreignKeys = new ArrayList<SqlTableForeignKey>();

	/**
	 * Список уникальных полей таблицы.
	 */
	private List<SqlTableUnique> uniques = new ArrayList<SqlTableUnique>();

	protected StringBuffer sb = new StringBuffer();

	public static final String KEYS_SEPARATOR = ",\n";
}