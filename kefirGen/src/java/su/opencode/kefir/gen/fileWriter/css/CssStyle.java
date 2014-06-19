/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.css;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Оболочка для css-стиля.
 */
public class CssStyle
{
	public CssStyle(String name) {
		this.name = name;
		this.map = new HashMap<String, Object>();
		this.keys = new LinkedList<String>();
	}

	public void put(String key, String value) {
		map.put(key, value);

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void putHexColor(String key, String color) {
		if (color.startsWith(HEX_COLOR_PREFIX))
			map.put(key, color);
		else
			map.put(key, concat(sb, HEX_COLOR_PREFIX, color));

		if ( !keys.contains(key) )
			keys.add(key);
	}

	/**
	 * @return значение стиля в строчку.
	 */
	public String toString() {
		if (map.isEmpty())
			return getEmptyStyle();

		sb.delete(0, sb.length());
		sb.append(name).append(" ");

		sb.append(STYLE_OPEN_BRACKET).append(" ");

		for (String key : keys)
			sb.append(key).append(KEY_VALUE_SEPARATOR).append(map.get(key)).append(FIELDS_SEPARATOR);

		sb.append(STYLE_CLOSE_BRACKET);
		return sb.toString();
	}

	public String toStringVertically() {
		if (map.isEmpty())
			return getEmptyStyle();

		sb.delete(0, sb.length());
		sb.append(name).append(" ").append(STYLE_OPEN_BRACKET).append(NEW_LINE);

		for (String key : keys)
			sb.append(VERTICAL_INDENT).append(key).append(KEY_VALUE_SEPARATOR).append(map.get(key)).append(FIELDS_SEPARATOR_SIMPLE).append(NEW_LINE);

		sb.append(STYLE_CLOSE_BRACKET);
		return sb.toString();
	}
	private String getEmptyStyle() {
		return concat(sb, name, " ", STYLE_OPEN_BRACKET, STYLE_CLOSE_BRACKET);
	}

	public static String getCssClassName(String name) {
		return concat(CLASS_NAME_PREFIX, name);
	}

	private String name; // имя стиля
	private List<String> keys; // сохраняет ключи в порядке их добавления
	private Map<String, Object> map;
	private StringBuffer sb = new StringBuffer();

	public static final String FIELDS_SEPARATOR_SIMPLE = ";";
	public static final String NEW_LINE = "\n";
	public static final String FIELDS_SEPARATOR = "; ";

	public static final String HEX_COLOR_PREFIX = "#";
	public static final String STYLE_OPEN_BRACKET = "{";
	public static final String STYLE_CLOSE_BRACKET = "}";
	public static final String KEY_VALUE_SEPARATOR = ": ";
	public static final String CLASS_NAME_PREFIX = ".";

	public static final String VERTICAL_INDENT = "\t";

	public static final String IMPORTANT = "!important";
	public static final String BACKGROUND_COLOR_PROPERTY_NAME = "background-color";
	public static final String COLOR_PROPERTY_NAME = "color";
}