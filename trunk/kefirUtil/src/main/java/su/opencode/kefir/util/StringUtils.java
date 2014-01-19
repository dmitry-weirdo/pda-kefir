/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 25.09.2009 10:05:42$
*/
package su.opencode.kefir.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author pda (25.09.2009 10:05:42)
 * @version $Revision$
 */
public class StringUtils
{
	/**
	 * @param str строка
	 * @return <code>false</code> - если строка непустая
	 * <br/>
	 * <code>true</code> - если строка равна <code>null</code> или имеет нулевую длину.
	 */
	public static boolean empty(String str) { // todo: аналогичная функция, которая делает trim
		return (str == null || str.isEmpty());
	}
	/**
	 * @param str строка
	 * @return <code>true</code> - если строка непустая
	 * <br/>
	 * <code>false</code> - если строка равна <code>null</code> или имеет нулевую длину.
	 */
	public static boolean notEmpty(String str) { // todo: аналогичная функция, которая делает trim
		return (str != null && !str.isEmpty());
	}

	/**
	 * @param value значение
	 * @param defaultValue значение по умолчанию
	 * @return если значение равно <code>null</code> или пусто - то значение по умолчанию.<br/>В противном случае - значение.
	 */
	public static String getValue(String value, String defaultValue) {
		if (value != null && !value.isEmpty())
			return value;

		return defaultValue;
	}

	/**
	 * @param valueToCheck значение, которое проверяется на пустоту или равенство <code>null</code>
	 * @param value значение
	 * @param defaultValue значение по умолчанию
	 * @return если значение равно <code>null</code> или пусто - то значение по умолчанию.<br/>В противном случае - значение.
	 */
	public static String getValue(String valueToCheck, String value, String defaultValue) {
		if ( valueToCheck != null && !valueToCheck.isEmpty() )
			return value;

		return defaultValue;
	}

	/**
	 * @param valueToCheck булево значение, которое проверяется
	 * @param value значение
	 * @param defaultValue значение по умолчанию
	 * @return если значение неистинно - то значение по умолчанию.<br/>В противном случае - указанное значение.
	 */
	public static String getValue(boolean valueToCheck, String value, String defaultValue) {
		if (valueToCheck)
			return value;

		return defaultValue;
	}

	public static String concat(Object... objects) {
		return getConcatenation(objects);
	}
	public static String cat(Object... objects) {
		final StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		for (Object s : objects)
			if (s != null)
				sb.append(s);

		return sb.toString();
	}
	public static String concat(StringBuffer sb, Object... objects) {
		return getConcatenation(sb, objects);
	}

	public static String getConcatenation(Object... objects) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		for (Object s : objects)
			sb.append(s);

		return sb.toString();
	}
	public static String getConcatenation(StringBuffer sb, Object... objects) {
		sb.delete(0, sb.length());

		for (Object s : objects)
			sb.append(s);

		return sb.toString();
	}
	public static String getConcatenation(StringBuffer sb, String... strings) {
		sb.delete(0, sb.length());

		for (String s : strings)
			sb.append(s);

		return sb.toString();
	}
	public static String getConcatenation(StringBuilder sb, String... strings) {
		sb.delete(0, sb.length());

		for (String s : strings)
			sb.append(s);

		return sb.toString();
	}

	public static String getConcatenation(String... strings) {
		return getConcatenation(new StringBuffer(), strings);
	}
	public static String getCommasLineString(List list) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		for (Object id : list)
			sb.append(id).append(", ");

		sb.delete(sb.length() - 2, sb.length());

		return sb.toString();
	}

	public static boolean hasUpperCaseChars(String str) {
		for (int i = 0; i < str.length(); i++)
			if (Character.isUpperCase(str.charAt(i)))
				return true;

		return false;
	}

	public static void addAll(StringBuffer sb, String... strings) {
		for (String s : strings)
			sb.append(s);
	}
	public static void append(StringBuffer sb, String... strings) {
		for (String s : strings)
			sb.append(s);
	}
	public static void appendLine(StringBuffer sb, String... strings) {
		append(sb, strings);
		sb.append("\n");
	}
	public static String getEncodedString(String str) throws UnsupportedEncodingException {
		if (str == null)
			return null;

		byte[] bytes = str.trim().getBytes("utf-8");
		int len = bytes.length;
		if (len == 0)
			return null;

		// fix 'И' letter tomcat problem
		for (int i = 0; i < len; i++)
		{
			if (bytes[i] == -48 && bytes[i + 1] == 63) // буква И в глючном случае
			{
				bytes[i] = (byte) 208;
				bytes[i + 1] = (byte) 152;
			}
		}

		return new String(bytes, "utf-8");
//		return new String(correctedStr.getBytes("cp1251"), "utf-8");
	}
	public static String getStringTo(String str, String charset) {
		try
		{
			return str == null ? null : new String(str.getBytes(charset));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(
				getConcatenation("Can't convert string to ", charset, " charset '", str, "'\n", e.getMessage()));
		}
	}
	public static String getStringFromUTF8(String str) {
		if (str == null)
			return null;

		byte[] bytes = new byte[0];
		try
		{
			bytes = str.trim().getBytes("utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException( concat("Cannot get utf-8 bytes from string: ", str) );
		}

		int len = bytes.length;
		if (len == 0)
			return null;

		// fix 'И' letter tomcat problem
		for (int i = 0; i < len; i++)
		{
			if (bytes[i] == -48 && bytes[i + 1] == 63) // буква И в глючном случае
			{
				bytes[i] = (byte) 208;
				bytes[i + 1] = (byte) 152;
			}
		}

		try
		{
			return new String(bytes, CHARSET_UTF8);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(
				getConcatenation("Can't convert string from ", CHARSET_UTF8, " charset '", str, "'\n", e.getMessage()));
		}
	}
	public static String getStringToUTF8(String str) {
		return getStringTo(str, CHARSET_UTF8);
	}
	public static String getStringTo866(String str) {
		return getStringTo(str, CHARSET_CP866);
	}
	public static String getStringFrom866(String str) {
		if (str == null)
			return null;

		byte[] bytes;
		bytes = str.trim().getBytes();

		int len = bytes.length;
		if (len == 0)
			return null;

		// fix 'Ш' letter problem
		for (int i = 0; i < len; i++)
		{
			if (bytes[i] == 63) // буква Ш в глючном случае
				bytes[i] = (byte) -104;
		}

		try
		{
			return new String(bytes, CHARSET_CP866);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(
				getConcatenation("Can't convert string from ", CHARSET_CP866, " charset '", str, "'\n", e.getMessage()));
		}
	}
	public static String getEncodedUppercaseString(String str) throws UnsupportedEncodingException {
		String encoded = getEncodedString(str);
		if (encoded == null)
			return null;

		return encoded.toUpperCase();
	}

	public static String replaceDecimalSeparator(Double value) {
		if (value == null)
			return "";

		return value.toString().replace('.', ',');
	}
	public static String replaceDecimalSeparator(String str) {
		if (str == null)
			return "";

		return str.replace(',', '.');
	}

	/**
	 * @param separator разделитель
	 * @param strings список строка
	 * @return строка, представляющая собой строки из списка, разделенные разелителем. В конце строки разделителя нет.
	 * <br/>
	 * Если список пуст или равен <code>null</code>, возвращается пустая строка.
	 */
	public static String getSeparatedString(String separator, List<String> strings) {
		if (strings == null || strings.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();

		for (String string : strings)
			sb.append(string).append(separator);

		sb.delete(sb.length() - separator.length(), sb.length()); // remove trailing separator
		return sb.toString();
	}

	public static String getSeparatedString(String separator, String... strings) {
		return getSeparatedString(separator, getList(strings));
	}

	public static String getSeparatedString(String separator, Object... objects) {
		return getSeparatedString( separator, getList(objects) );
	}

	public static List<String> split(String str, String separator) {
		if ( empty(str) )
			return Collections.emptyList();

		List<String> result = new ArrayList<>();
		for (String part : str.split(separator))
			result.add(part.trim());

		return result;
	}

	public static String getHexString(byte[] bytes, String separator) {
		if ( bytes == null )
			return null;

		StringBuilder sb = new StringBuilder( (2 * bytes.length) + (separator.length() * bytes.length) );
		for ( byte b : bytes )
		{
			sb.append(HEX_DIGITS.charAt((b & 0xF0) >> 4))
			  .append(HEX_DIGITS.charAt((b & 0x0F)))
			  .append(separator);
		}

		sb.delete( sb.length() - separator.length(), sb.length() ); // remove trailing separator
		return sb.toString();
	}
	public static String getHexString(byte[] bytes) {
		return getHexString(bytes, " ");
	}

	public static List<String> getList(String... values) {
		List<String> result = new ArrayList<>();
		Collections.addAll(result, values);
		return result;
	}
	public static List<String> getList(Object... values) {
		List<String> result = new ArrayList<>();
		for (Object value : values)
			result.add(value.toString());

		return result;
	}

	public static String[] getArray(List<String> list) {
		String[] result = new String[list.size()];

		for (int i = 0; i < list.size(); i++)
			result[i] = list.get(i);

		return result;
 	}

	public static Double getDouble(String str) {
		return Double.parseDouble(replaceDecimalSeparator(str));
	}

	public static String escapeHtml(String inputString) {
		if (inputString == null)
			return "";

		int i, start;
		String filtered;
		StringBuilder builder = null;
		char[] input = inputString.toCharArray();

		for (start = i = 0; i < input.length; i++)
		{
			switch (input[i])
			{
				case '<':
					filtered = "&lt;";
					break;
				case '>':
					filtered = "&gt;";
					break;
				case '&':
					filtered = "&amp;";
					break;
//				case '"':             // for JSON output this should not be escaped, done by extjs
//					filtered = "&#34;";
//					break;
				case '\'':
					filtered = "&#39;";
					break;
				default:
					continue;
			}

			if (builder == null)
				builder = new StringBuilder();

			builder.append(input, start, i - start);
			builder.append(filtered);
			start = i + 1;
		}

		return start == 0 ? inputString : builder.append(input, start, i - start).toString();
	}

	public static String getNotNullString(String str) {
		if (str == null)
			return "";

		return str;
	}

	public static String getNotNullString(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	public static String getNotNullString(Date date, DateFormat format) {
		if (date == null)
			return "";

		return format.format(date);
	}

	public static String formatDouble(Double value) {
		if (value == null)
			return "";

		return getNumberFormat().format(value);
	}
	public static String formatDouble(float value) {
		return getNumberFormat().format(value).replace(',', '.');
	}

	public static NumberFormat getNumberFormat() {
		final DecimalFormat format = new DecimalFormat();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);

		final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		format.setDecimalFormatSymbols(decimalFormatSymbols);

		return format;
	}

	public static String longToString(Long value) {
		if (value == null)
			return null;

		return Long.toString(value);
	}

	public static String reverse(String str) {
		if (str == null)
			return null;

		return new StringBuffer(str).reverse().toString();
	}

	public static String cutUtf8Mark(String str) {
		if (str.startsWith(UTF8_BYTE_ORDER_MARK))
			str = str.substring(UTF8_BYTE_ORDER_MARK.length());

		return str;
	}
	public static String truncate(String str, int length) {
		return str == null || str.length() <= length ? str : str.substring(0, length);
	}
	public static String getNumber(int stringLength, Integer value) {
		return getString(stringLength, value.toString(), "0", false);
	}
	public static String getString(int stringLength, String str, String symbol, boolean addSymbolToEnd) {
		final StringBuilder sb = new StringBuilder();
		if (str == null)
			str = "";

		if (addSymbolToEnd)
			sb.append(str);

		for (int i = 0; i < stringLength - str.length(); i++)
			sb.append(symbol);

		if (!addSymbolToEnd)
			sb.append(str);

		return sb.toString();
	}

	public static String capitalize(StringBuffer sb, String s) {
		return concat(sb, s.substring(0, 1).toUpperCase(), s.substring(1));
	}
	public static String capitalize(String s) {
		return capitalize(new StringBuffer(), s);
	}

	public static String decapitalize(StringBuffer sb, String s) {
		return concat(sb, s.substring(0, 1).toLowerCase(), s.substring(1));
	}
	public static String decapitalize(String s) {
		return decapitalize(new StringBuffer(), s);
	}

	public static String getJavaString(String str) {
		if (str == null)
			return "null";

		return concat("\"", str, "\"");
	}

	public static String getJsString(String str) {
		if (str == null)
			return "null";

		return concat("'", str, "'");
	}

	/**
	 * @param quarter квартал
	 * @param year год
	 * @return строка вида "1 кв. 2012 г."
	 */
	public static String getQuarter(Integer quarter, Integer year) {
		if (quarter == null && year == null)
			return "";

		if (quarter == null)
			return concat(Integer.toString(year), " г.");

		if (year == null)
			return concat(Integer.toString(quarter), " кв.");

		return concat(Integer.toString(quarter), " кв. ", Integer.toString(year), " г.");
	}

	/**
	 * @param singular английское существительное в единственном числе.
	 * @return существительное во множественном числе, согласно правилам английского языка
	 */
	public static String getPlural(String singular) {
		if (singular.endsWith("y"))
			return concat(singular.substring(0, singular.length() - 1), "ies");

		// todo: implement method as it should be
		return concat(singular, "s");
	}

	public static String toMD5(String str) {
		return toMD5(str, "UTF-8");
	}

	public static String toMD5(String str, String charset) {
		if (str == null)
			return null;

		try
		{
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] hash = md.digest(str.getBytes(charset));

			final StringBuilder hex = new StringBuilder();
			for (byte aHash : hash)
			{
				hex.append(Character.forDigit((aHash & 0xF0) >> 4, 16));
				hex.append(Character.forDigit((aHash & 0x0F), 16));
			}

			return hex.toString();
		}
		catch (UnsupportedEncodingException | NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final String UTF8_BYTE_ORDER_MARK = new String(new char[]{ 1087, 187, 1111 });

	public static final String CHARSET_UTF8 = "UTF8";
	public static final String CHARSET_CP866 = "Cp866";
	public static final String CHARSET_CP1251 = "Cp1251";

	public static final String HEX_DIGITS = "0123456789ABCDEF";
}