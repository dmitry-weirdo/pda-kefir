/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 20.12.2010 14:39:58$
*/
package su.opencode.kefir.srv;

import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.JsonUtils.getStringParam;
import static su.opencode.kefir.util.StringUtils.getConcatenation;
import su.opencode.kefir.srv.json.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

public abstract class FilterConfig extends JsonObject
{
	protected FilterConfig() {
	}
	protected FilterConfig(Map<String, String[]> map) {
		fromJson(map);
		entityName = getStringParam(map, "entityName", false);
	}

	public abstract void setSqlWhere(StringBuffer sb);

	@SuppressWarnings("unchecked")
	public static <T extends FilterConfig> T getNewInstance(Map<String, String[]> map) {
		final String filterConfig = getStringParam(map, "filterConfig", false);
		if (filterConfig == null)
			return null;

		try
		{
			final Class<T> aClass = (Class<T>) Class.forName(filterConfig);
			final Constructor<T> aConstructor = aClass.getConstructor(java.util.Map.class);

			return aConstructor.newInstance(map);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	protected void addValue(StringBuffer sb, String fieldName, String relation, Object value) {
		addValue(sb, null, fieldName, relation, value);
	}
	protected void addInValues(StringBuffer sb, String fieldName, int... values) {
		addInValues(sb, null, fieldName, values);
	}
	protected void addInValues(StringBuffer sb, String prefix, String fieldName, int... values) {
		addValues(sb, prefix, fieldName, IN, values);
	}
	protected void addNotInValues(StringBuffer sb, String fieldName, int... values) {
		addNotInValues(sb, null, fieldName, values);
	}
	protected void addNotInValues(StringBuffer sb, String prefix, String fieldName, int... values) {
		addValues(sb, prefix, fieldName, NOT_IN, values);
	}
	protected void addValues(StringBuffer sb, String prefix, String fieldName, String relation, int... values) {
		FilterConfig.sb.delete(0, FilterConfig.sb.length())
			.append("(");

		for (Object ob : values)
			FilterConfig.sb.append(ob).append(", ");

		FilterConfig.sb.delete(FilterConfig.sb.length() - 2, FilterConfig.sb.length())
			.append(")");

		addString(sb, prefix);
		fieldName = getFieldName(fieldName);

		sb.append(fieldName).append(" ")
			.append(relation).append(" (");

		for (Object ob : values)
			sb.append(ob).append(", ");

		sb.delete(sb.length() - 2, sb.length())
			.append(") ");
	}
	protected void addValue(StringBuffer sb, String prefix, String fieldName, String relation, Object value) {
		if (value == null)
			return;

		addString(sb, prefix);
		fieldName = getFieldName(fieldName);
		value = getValue(relation, value);

		sb.append(fieldName).append(" ")
			.append(relation).append(" ")
			.append(value).append(" ");
	}
	protected void addUpperValue(StringBuffer sb, String prefix, String fieldName, String relation, Object value) {
		if (value == null)
			return;

		addString(sb, prefix);
		fieldName = getFieldName(fieldName);
		value = getValue(relation, value);

		sb.append("upper(").append(fieldName).append(") ")
			.append(relation).append(" ")
			.append("upper(").append(value).append(") ");
	}
	protected void addString(StringBuffer sb, String prefix) {
		if (prefix != null && !prefix.isEmpty())
			sb.append(prefix).append(" ");
	}
	private String getFieldName(String fieldName) {
		if (tablePrefix == null || tablePrefix.equals(""))
			return fieldName;

		if (tablePrefix.contains("."))
			return getConcatenation(tablePrefix, fieldName);

		return getConcatenation(tablePrefix, ".", fieldName);
	}
	private String getValue(String relation, Object value) {
		if (value instanceof String)
		{
			value = ((String) value).replace("'", "''");
			if (!LIKE.equals(relation))
				return getConcatenation("'", value, "'");
			else
				return getConcatenation("'%", value, "%'");
		}

		if (value instanceof Date)
			return getConcatenation("'", getDayMonthYear((Date) value), "'");

		if (value instanceof Enum)
			return getConcatenation("'", value.toString(), "'");

		return value.toString();
	}
	protected void addNumberSearchValue(StringBuffer sb, String numberField, String numberValue, int productionCorrectNumberDigitsCount) {
		if (numberValue == null)
			return;

		sb.append(AND)
			.append(" (");

		addValue(sb, numberField, LIKE, numberValue);

		final int numberCount = numberValue.length();
		if (numberCount > 1 && numberValue.substring(0, 1).equals("0"))
		{
			final int aNumber = Integer.parseInt(numberValue);
			for (int i = 0; i <= productionCorrectNumberDigitsCount - numberCount; i++)
			{
				addString(sb, OR);
				addString(sb, numberField);
				addString(sb, LIKE);
				addString(sb, getSearchNumberValue(aNumber, i));
			}
		}

		sb.append(")");
	}
	private String getSearchNumberValue(int aNumber, int size) {
		sb.delete(0, sb.length())
			.append("'")
			.append(aNumber);

		for (int i = 0; i < size; i++)
			sb.append("_");

		return sb.append("'").toString();
	}

	protected String tablePrefix = "o.";

	private String entityName;

	protected static final String EQUAL = "=";
	protected static final String NOT_EQUAL = "<>";
	protected static final String MORE = ">";
	protected static final String MORE_EQUAL = ">=";
	protected static final String LESS = "<";
	protected static final String LESS_EQUAL = "<=";
	protected static final String LIKE = "like";
	protected static final String AND = "and";
	protected static final String OR = "or";
	protected static final String IS_NULL = "is null";
	protected static final String IS_NOT_NULL = "is not null";
	protected static final String NOT = "not";
	protected static final String IN = "in";
	protected static final String NOT_IN = "not in";
	protected static final String WHERE = "where";

	private static final StringBuffer sb = new StringBuffer();
}