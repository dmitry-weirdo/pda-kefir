/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 29.03.2012 16:44:25$
*/
package su.opencode.kefir.gen.project.sql;

import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.SqlColumn;

import java.lang.reflect.Field;

import static su.opencode.kefir.gen.project.sql.SqlTablePrimaryKey.ID_FIELD_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class SqlTableField
{
	public SqlTableField(String name, String type, boolean notNull) {
		this.name = name;
		this.type = type;
		this.notNull = notNull;
	}
	public SqlTableField(SqlColumn column, Field field) {
		this.name = ExtEntityUtils.getSqlColumnName(column, field);
		this.notNull = column.notNull();

		switch (column.type())
		{
			case VARCHAR_TYPE: this.type = concat(VARCHAR_TYPE, "(", Integer.toString(column.length()), ")"); break;
			case NUMERIC_TYPE: this.type = concat(NUMERIC_TYPE, "(", column.digits(), ", ", column.precision(), ")"); break;

			default: this.type = column.type();
		}
	}

	public static SqlTableField getIdField() {
		return new SqlTableField(ID_FIELD_NAME, INTEGER_TYPE, true);
	}
	public static SqlTableField getIdField(String name) {
		return new SqlTableField(name, INTEGER_TYPE, true);
	}

	public static SqlTableField getIntegerField(String name, boolean notNull) {
		return new SqlTableField(name, INTEGER_TYPE, notNull);
	}
	public static SqlTableField getVarcharField(String name, int length, boolean notNull) {
		return new SqlTableField(name, concat(VARCHAR_TYPE, "(", Integer.toString(length), ")"), notNull);
	}
	public static SqlTableField getSmallintField(String name, boolean notNull) {
		return new SqlTableField(name, SMALLINT_TYPE, notNull);
	}
	public static SqlTableField getDateField(String name, boolean notNull) {
		return new SqlTableField(name, DATE_TYPE, notNull);
	}
	public static SqlTableField getBlobField(String name) {
		return new SqlTableField(name, BLOB_TYPE, false);
	}
	public static SqlTableField getNumericField(String name, boolean notNull) { // todo: copy of this method with passing 15, 2 as params
		return getNumericField(name, notNull, DEFAULT_NUMERIC_FIELD_DIGITS, DEFAULT_NUMERIC_FIELD_PRECISION);
	}
	public static SqlTableField getNumericField(String name, boolean notNull, int digits, int precision) {
		return new SqlTableField(name, concat(NUMERIC_TYPE, "(", digits, ", ", precision, ")"), notNull);
	}

	public static boolean isReservedWord(String fieldName) {
		for (String reservedWord : RESERVED_WORDS)
			if (reservedWord.equalsIgnoreCase(fieldName))
				return true;

		return false;
	}

	public static String getName(String name) {
		if (isReservedWord(name))
			return concat("\"", name, "\"");

		return name;
	}

	public String getName() {
		return getName(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isNotNull() {
		return notNull;
	}
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public String getNotNullStr() {
		return notNull ? " not null" : "";
	}

	private String name;
	private String type;
	private boolean notNull;

	public static final String INTEGER_TYPE = "integer";
	public static final String VARCHAR_TYPE = "varchar";
	public static final String SMALLINT_TYPE = "smallint";
	public static final String DATE_TYPE = "date";
	public static final String TIMESTAMP_TYPE = "timestamp";
	public static final String BLOB_TYPE = "blob";
	public static final String NUMERIC_TYPE = "numeric";

	public static final int DEFAULT_NUMERIC_FIELD_DIGITS = 15;
	public static final int DEFAULT_NUMERIC_FIELD_PRECISION = 2;

	public static final String RESERVED_WORDS[] = { "length", "date", "order", "password", "role", "key", "value", "level", "position", "year", "active" };
}