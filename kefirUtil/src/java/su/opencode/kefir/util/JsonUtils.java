/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 24.09.2010 19:42:51$
*/
package su.opencode.kefir.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static su.opencode.kefir.util.DateUtils.getDayMonthYearFormat;
import static su.opencode.kefir.util.DateUtils.getJsDateFormat;

public class JsonUtils
{
	public static void putToJson(JSONObject json, String key, Object value) {
		try
		{
			json.put(key, value);
		}
		catch (JSONException e)
		{
			throw new RuntimeException(StringUtils.getConcatenation("Can't put ", value, " to key = ", key));
		}
	}

	public static void putToJson(JSONArray json, Object value) {
		if (value != null)
			json.put(value);
	}

	public static String getStringParam(Map<String, String[]> map, String paramName) {
		try
		{
			String[] values = map.get(paramName);
			if (values == null)
				return null;

			return check4nullString(StringUtils.getEncodedUppercaseString(values[0]));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static String check4nullString(String param) {
		if (param == null)
			return null;

		param = param.trim();
		if (param.isEmpty())
			return null;
		else
			return param;
	}

	public static String getStringParam(Map<String, String[]> map, String paramName, boolean upperCase) {
		String[] values = map.get(paramName);
		if (values == null)
			return null;

		final String rv = check4nullString(StringUtils.getStringFromUTF8(values[0]));
		if (rv == null)
			return null;

		return upperCase ? rv.toUpperCase() : rv;
	}

	public static Integer getIntegerParam(Map<String, String[]> map, String paramName) {
		try
		{
			String[] values = map.get(paramName);
			if (values == null)
				return null;

			return values[0] == null || values[0].isEmpty() ? null : Integer.parseInt(values[0]);
		}
		catch (NumberFormatException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Boolean getBooleanParam(Map<String, String[]> map, String paramName) {
		String[] values = map.get(paramName);
		return values != null
			&& values[0] != null
			&& (values[0].equalsIgnoreCase("true") || values[0].equalsIgnoreCase("on"));
	}

	public static Long getLongParam(Map<String, String[]> map, String paramName) {
		String[] values = map.get(paramName);
		if (values == null)
			return null;

		return values[0] == null || values[0].isEmpty() ? null : Long.parseLong(values[0]);
	}

	public static Short getShortParam(Map<String, String[]> map, String paramName) {
		String[] values = map.get(paramName);
		if (values == null)
			return null;

		return values[0] == null || values[0].isEmpty() ? null : Short.parseShort(values[0]);
	}

	public static Double getDoubleParam(Map<String, String[]> map, String paramName) {
		try
		{
			String[] values = map.get(paramName);
			if (values == null)
				return null;

			return values[0] == null || values[0].isEmpty() ? null : StringUtils.getNumberFormat().parse(values[0]).doubleValue();
		}
		catch (ParseException e)
		{
			throw new RuntimeException(StringUtils.getConcatenation("Parsing error for field '", paramName, "'"), e);
		}
	}

	public static Float getFloatParam(Map<String, String[]> map, String paramName) {
		String[] values = map.get(paramName);
		if (values == null)
			return null;

		return values[0] == null || values[0].isEmpty() ? null : Float.parseFloat(values[0]);
	}

	public static Date getDateParam(Map<String, String[]> map, String paramName) {
		String[] values = map.get(paramName);
		if (values == null)
			return null;

		try
		{
			return (values[0] = values[0].trim()).isEmpty() ? null : getDayMonthYearFormat().parse(values[0]);
		}
		catch (ParseException e)
		{
			try
			{
				return getJsDateFormat().parse(values[0]);
			}
			catch (ParseException e1)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public static Object getFromJson(JSONObject json, String key) {
		try
		{
			return json.get(key);
		}
		catch (JSONException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static HashMap<String, String[]> getMapFromJson(JSONObject json) {
		final HashMap<String, String[]> map = new HashMap<>();
		final Iterator keys = json.keys();
		while (keys.hasNext())
		{
			final String key = (String) keys.next();
			final Object value = getFromJson(json, key);

			map.put(key, new String[] { getStringValue(value) });
		}

		return map;
	}

	private static String getStringValue(Object value) {
		return value.toString();
	}

}