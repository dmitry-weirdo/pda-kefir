/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.js;

import su.opencode.kefir.gen.field.DateField;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.TAB;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.getJsString;

/**
 * Оболочка для js хэша.
 */
public class JsHash
{
	public JsHash() {
		this.map = new HashMap<String, Object>();
		this.keys = new LinkedList<String>();
	}
	public JsHash(String key, boolean value) { // конструктор для хэша из 1 элемента
		this();
		this.put(key, value);
	}
	public JsHash(String key, String value) { // конструктор для хэша из 1 элемента
		this();
		this.putString(key, value);
	}

	public String getIndent() {
		return indent;
	}
	public void setIndent(String indent) {
		this.indent = indent;
	}
	public boolean isAlignChildren() {
		return alignChildren;
	}
	public void setAlignChildren(boolean alignChildren) {
		this.alignChildren = alignChildren;
	}

	public void put(String key, String value) {
		map.put(key, value);

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void put(String key, int value) {
		map.put(key, Integer.toString(value));

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void put(String key, double value) {
		map.put(key, Double.toString(value));

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void put(String key, boolean value) {
		map.put(key, Boolean.toString(value));

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void put(String key, JsArray array) {
		map.put(key, array.toString());

		if ( !keys.contains(key) )
			keys.add(key);
	}
	public void put(String key, JsHash hash) {
		map.put(key, hash);

		if ( !keys.contains(key) )
			keys.add(key);
	}

	public void putString(String key, String value) {
		map.put( key, getJsString(value) );

		if ( !keys.contains(key) )
			keys.add(key);
	}

	public void putDate(String key, String value) {
		// todo: возможность указать new Date()

		if (value.equals(DateField.NULL_DATE_VALUE))
		{
			put(key, value);
		}
		else
		{
			putString(key, value);
		}
	}

	public void clear() {
		keys.clear();
		map.clear();
	}

	/**
	 * @return значение хэша в строчку
	 */
	public String toString() {
		if (indent != null) // указан отступ -> писать вертикалн
			return toStringAligned(indent);

		if (map.isEmpty())
			return concat(sb, HASH_OPENING_BRACKET, HASH_CLOSING_BRACKET);

		sb.delete(0, sb.length());
		sb.append(HASH_OPENING_BRACKET).append(" ");

		for (String key : keys)
		{
			sb.append(key).append(KEY_VALUE_SEPARATOR).append(" ").append(map.get(key)).append(FIELDS_SEPARATOR);
		}
		sb.delete(sb.length() - FIELDS_SEPARATOR.length(), sb.length()); // remove trailing ", "

		sb.append(" ").append(HASH_CLOSING_BRACKET);
		return sb.toString();
	}

	/**
	 * @param indent начальный отступ хэша. На этот отступ будет отстоять от начала строки закрывающая скобка хэша.
	 * @return значение хэша, отформатированное по кодстайлу и оттабленное указанным отступом.
	 * Вначале отступ не ставится (ибо сам хэш может быть частью хэша.
	 * Перенос строки в конце не ставится, ибо после закрывающей скобки может быть нужна запятая
	 */
	public String toStringAligned(String indent) {
		if (map.isEmpty())
			return concat(sb, HASH_OPENING_BRACKET, HASH_CLOSING_BRACKET);

		sb.delete(0, sb.length());
		sb.append(HASH_OPENING_BRACKET).append(NEW_LINE);

		for (String key : keys)
		{
			Object value = map.get(key);
			String valueToWrite = value.toString();
			if (value instanceof JsHash)
			{
				if (this.alignChildren)
				{
					JsHash hash = (JsHash) value;
					valueToWrite = hash.toStringAligned( concat(indent, TAB) );
				}
			}

			sb.append(indent).append(TAB).append(key).append(KEY_VALUE_SEPARATOR).append(" ").append(valueToWrite).append(FIELDS_SEPARATOR_SIMPLE).append(NEW_LINE);
		}

		sb.delete(
			sb.length() - NEW_LINE.length() - FIELDS_SEPARATOR_SIMPLE.length(),
			sb.length() - NEW_LINE.length()
		); // remove trailing ",", but not remove last "\n"

		sb.append(indent).append(HASH_CLOSING_BRACKET);
		return sb.toString();
	}

	/**
	 * Если не <code>null</code>, то в toString пишется этот хэш с указанным отступом
	 */
	private String indent = null;

	/**
	 * Если <code>true</code>, то при выравненном написании все непосредственно вложенные хэши
	 * будут отформатированы, вне зависимости от их {@linkplain #indent свойства indent}.
	 */
	private boolean alignChildren = false;

	private List<String> keys; // сохраняет ключи в порядке их добавления
	private Map<String, Object> map;
	private StringBuffer sb = new StringBuffer();

	public static final String FIELDS_SEPARATOR_SIMPLE = ",";
	public static final String NEW_LINE = "\n";
	public static final String FIELDS_SEPARATOR = ", ";
	public static final String KEY_VALUE_SEPARATOR = ":";
	public static final String HASH_OPENING_BRACKET = "{";
	public static final String HASH_CLOSING_BRACKET = "}";
}