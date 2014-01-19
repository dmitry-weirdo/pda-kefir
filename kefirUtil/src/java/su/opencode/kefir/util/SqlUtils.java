/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 21.09.2010 10:47:13$
*/
package su.opencode.kefir.util;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;

import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.empty;

public class SqlUtils
{
	// common functions
	public static Byte getByte(Object value) {
		if (value == null)
			return null;

		if (value instanceof Short)
			return ((Short) value).byteValue();

		return (Byte) value;
	}
	public static Integer getInteger(Object value) {
		if (value == null)
			return null;

		if (value instanceof Short)
			return ((Short) value).intValue();

		if (value instanceof String)
			return Integer.parseInt( (String) value ); // значение, замапленное на Integer, хранитс€ в Ѕƒ, как строка

		return (Integer) value;
	}
	public static Long getLong(Object value) {
		if (value == null)
			return null;

		if (value instanceof BigDecimal)
			return ((BigDecimal) value).longValue();

		if (value instanceof Integer)
			return ((Integer) value).longValue();

		return ((BigInteger) value).longValue();
	}
	public static Double getDouble(Object value) {
		if (value == null)
			return null;

		if (value instanceof BigDecimal)
			return ((BigDecimal) value).doubleValue();

		if (value instanceof Integer)
			return ((Integer) value).doubleValue();

		if (value instanceof BigInteger)
			return ((BigInteger) value).doubleValue();

		return (Double) value;
	}
	public static String getString(Object value) {
		if (value == null)
			return null;

		if (value instanceof Character)
			return value.toString();

		return (String) value;
	}
	public static Boolean getBoolean(Object value) {
		if (value == null)
			return null;

		Boolean booleanValue = null;
		try
		{
			booleanValue = (Boolean) value;
			return booleanValue;
		}
		catch (ClassCastException e)
		{ // представление Boolean как числа 0/1
			Byte byteValue = SqlUtils.getByte(value);
			if (byteValue == null)
				return null;
			else if (byteValue == 1)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}
	}

	public static Date getDate(Object value) {
		if (value == null)
			return null;

		return (Date) value;
	}

	public static int getCountValue(Query query) {
		Object singleResult = query.getSingleResult();
		if (singleResult == null)
			return 0;

		return ( (Number) singleResult).intValue();
	}

	// firebird functions
	public static String getSQLStringField(String value) {
		if (value == null || isEmpty(value) || value.equalsIgnoreCase("null"))
			return "null";

		return concat("'", str, "'");
	}
	public static String getSQLGeneratorField(String generatorName) {
		return concat("GEN_ID(", generatorName, ", 1)");
	}
	public static String getSQLTimestampField(String value) {
		if (isEmpty(value))
			return "null";

		return concat("CAST('", str, "' AS TIMESTAMP)");
	}
	public static String getSQLTimestampField(Date date) {
		if (date == null)
			return "null";

		return concat("CAST('", getDayMonthYear(date), "' AS TIMESTAMP)");
	}
	public static String getSQLDateField(String value) {
		if (isEmpty(value))
			return "null";

		return concat("CAST('", str, "' AS DATE)");
	}
	public static String getSQLDateField(Date date) {
		if (date == null)
			return "null";

		return concat("CAST('", getDayMonthYear(date), "' AS DATE)");
	}
	public static String getSQLIntegerField(String value) {
		if (isEmpty(value))
			return "null";

		return str;
	}
	private static boolean isEmpty(String value) {
		if (value == null)
			return true;

		str = value.trim();
		str = str.replaceAll("'", "''");
		str = str.replaceAll("\15\12", " ");

		return str.isEmpty();
	}
	public static String getSQLNumericField(String value, int precision) {
		if (isEmpty(value))
			return "null";

		String width = value.replace(',', '.').replaceAll("†", "");

		if (width.indexOf('E') > -1)
			width = width.substring(0, width.indexOf('.') + precision);

		return width;
	}

	public static String getSqlString(String str) {
		if (str == null)
			return NULL;

		return concat(QUOTE, str, QUOTE);
	}

	public static String getSqlDate(DateFormat format, Date date) {
		if (date == null)
			return NULL;

		return concat(QUOTE, format.format(date), QUOTE);
	}

	/**
	 * @param search параметр поиска
	 * @return строка, подготовленна€ дл€ поиска в sql-выражении like
	 */
	public static String getSearchParamFirebird(String search) {
		if ( empty(search) )
			return null;  // todo: solve this hack

		String result = search;
		result = result.replaceAll("'", "''");
		result = result.replaceAll("\\\\", "\\\\\\\\"); // replace '\' to '\\'
		result = result.replaceAll("%", "\\\\%"); // replace '%' to '\%'
		result = result.replaceAll("\\_", "\\\\_"); // replace '_' to '\_'

		result = concat("%", result, "%");

		return concat("'", result, "' escape '\\'");
	}


	// postgreSQL methods
	public static String getSearchParamPostrgreSql(String search) { // method is actual only for PostgreSQL
		if ( empty(search) )
			return "'%'"; // allow search by like on empty string // todo: solve this hack
//			return null;

		String result = search;
		result = result.replaceAll("'", "''");
		result = result.replaceAll("%", "\\\\\\\\%"); // replace '%' to '\\%'
		result = result.replaceAll("\\_", "\\\\\\\\_"); // replace '_' to '\\%'

		if ( !result.startsWith("%") )
			result = concat("%", result, "%"); // postgreSql escape symbol

		return concat("E'", result, "'"); // postgreSql escape ( ilike E'%\\%%' )
	}

	private static String str = "null";

	public static final String SPACE = " ";
	public static final String SELECT_OPERATOR = "select";
	public static final String SELECT_OPERATOR_WITH_SPACE = "select ";
	public static final String SELECT_OPERATOR_WITH_SPACES = " select ";
	public static final String FROM_OPERATOR = "from";
	public static final String FROM_OPERATOR_WITH_SPACE = "from ";
	public static final String FROM_OPERATOR_WITH_SPACES = " from ";
	public static final String AS_OPERATOR = "as";
	public static final String AS_OPERATOR_WITH_SPACES = " as ";
	public static final String DISTINCT_OPERATOR = "distinct";
	public static final String AND_OPERATOR = "and";
	public static final String AND_OPERATOR_WITH_SPACES = " and ";
	public static final String OR_OPERATOR = "or";
	public static final String OR_OPERATOR_WITH_SPACES = " or ";
	public static final String WHERE_OPERATOR = "where";
	public static final String WHERE_OPERATOR_WITH_SPACES = " where ";
	public static final String LIKE_OPERATOR = "like";
	public static final String LIKE_OPERATOR_WITH_SPACES = " like ";
	public static final String EXISTS_OPERATOR = "exists";
	public static final String EXISTS_OPERATOR_WITH_SPACES = " exists ";
	public static final String JOIN_OPERATOR = "join";
	public static final String JOIN_OPERATOR_WITH_SPACES = " join ";
	public static final String LEFT_JOIN_OPERATOR = "left join";
	public static final String LEFT_JOIN_OPERATOR_WITH_SPACES = " left join ";
	public static final String NOT_OPERATOR = "not";
	public static final String NOT_OPERATOR_WITH_SPACES = " not ";
	public static final String IN_OPERATOR = "in";
	public static final String IN_OPERATOR_WITH_SPACES = " in ";
	public static final String ON_OPERATOR = "on";
	public static final String ON_OPERATOR_WITH_SPACES = " on ";
	public static final String ORDER_BY_OPERATOR = "order by";
	public static final String ORDER_BY_OPERATOR_WITH_SPACES = " order by ";
	public static final String ASC_OPERATOR = "asc";
	public static final String ASC_OPERATOR_WITH_SPACES = " asc ";
	public static final String DESC_OPERATOR = "desc";
	public static final String DESC_OPERATOR_WITH_SPACES = " desc ";
	public static final String EQUALS_OPERATOR = "=";
	public static final String EQUALS_OPERATOR_WITH_SPACES = " = ";
	public static final String NOT_EQUALS_OPERATOR = "<>";
	public static final String NOT_EQUALS_OPERATOR_WITH_SPACES = " <> ";
	public static final String LESS_OPERATOR = "<";
	public static final String LESS_OPERATOR_WITH_SPACES = " < ";
	public static final String LESS_OR_EQUALS_OPERATOR = "<=";
	public static final String LESS_OR_EQUALS_OPERATOR_WITH_SPACES = " <= ";
	public static final String MORE_OPERATOR = ">";
	public static final String MORE_OPERATOR_WITH_SPACES = " > ";
	public static final String MORE_OR_EQUALS_OPERATOR = ">=";
	public static final String MORE_OR_EQUALS_OPERATOR_WITH_SPACES = " >= ";
	public static final String CONCAT_OPERATOR = "||";
	public static final String OPENING_BRACKET = "(";
	public static final String OPENING_BRACKET_WITH_SPACE = "( ";
	public static final String CLOSING_BRACKET = ")";
	public static final String CLOSING_BRACKET_WITH_SPACE = " )";
	public static final String COMMA_OPERATOR = ",";
	public static final String COMMA_OPERATOR_WITH_SPACE = ", ";
	public static final String QUOTE = "'";
	public static final String NULL = "null";
	public static final String IS_NULL = "is null";
	public static final String IS_NULL_WITH_SPACE = " is null";
	public static final String IS_NOT_NULL = "is not null";
	public static final String IS_NOT_NULL_WITH_SPACE = " is not null";

	public static final String TABLE_FIELD_DELIMITER = ".";

	public static final String POSTGRE_SQL_IGNORE_CASE_LIKE_OPERATOR = "ilike";

	/**
	 * MsSQL format for convert function for values like '2013-05-21 00:00:00.000'.
	 */
	public static final String MS_SQL_YYYY_MM_DD_HH_MM_SS_MS_DATE_FORMAT = "120";

	public static final String FIREBIRD_BOOLEAN_TRUE = "1";
	public static final String FIREBIRD_BOOLEAN_FALSE = "0";

	/**
	 * ћаксимальное число элементов в одном выражении <code>in</code> в Firebird.
	 * ѕри превышении этого числа при выполнении запроса вылетает "Implementation limit exceeded".
	 */
	public static final int FIREBIRD_MAX_IN_ELEMENTS_COUNT = 1499;
}