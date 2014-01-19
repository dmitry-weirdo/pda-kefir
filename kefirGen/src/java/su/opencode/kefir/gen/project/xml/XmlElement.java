/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.project.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.getNotNullString;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public class XmlElement
{
	public XmlElement(String name) {
		this.name = name;
		this.map = new HashMap<String, String>();
		this.keys = new LinkedList<String>();
	}
	public XmlElement(String name, String value) {
		this.name = name;
		this.value = value;
		this.map = new HashMap<String, String>();
		this.keys = new LinkedList<String>();
	}
	public XmlElement(String name, String key, String value) { // конструктор для хэша из 1 элемента
		this(name);
		this.addAttribute(key, value);
	}

	public void clear() {
		keys.clear();
		map.clear();
	}
	public void addAttribute(String key, String value) {
		map.put(key, value);

		if ( !keys.contains(key) )
			keys.add(key);
	}

	public void addAttribute(String key, Integer value) {
		map.put(key, value.toString());

		if ( !keys.contains(key) )
			keys.add(key);
	}

	public String startElement() {
		return startElement(false);
	}
	public String startElement(boolean empty) {
		sb.delete(0, sb.length());
		sb.append(ELEMENT_OPEN_START_TAG).append(name);

		for (String key : keys)
			if ( notEmpty(map.get(key)) )
				sb.append(ARGUMENTS_SEPARATOR).append(key).append(ARGUMENT_KEY_VALUE_SEPARATOR).append(ARGUMENT_VALUE_QUOTE).append(map.get(key)).append(ARGUMENT_VALUE_QUOTE);

		sb.append(empty ? EMPTY_ELEMENT_END_TAG : ELEMENT_END_TAG);
		return sb.toString();
	}
	public String endElement() {
		sb.delete(0, sb.length());

		sb.append(ELEMENT_CLOSE_START_TAG)
			.append(name)
			.append(ELEMENT_END_TAG);

		return sb.toString();
	}
	public String simpleElement() {
		String startElement = startElement();
		String endElement = endElement();

		sb.delete(0, sb.length());
		sb.append(startElement).append(getNotNullString(value)).append(endElement);
		return sb.toString();
	}
	public String emptyElement() {
		return startElement(true);
	}

	private String name;
	private String value;
	private List<String> keys; // сохраняет ключи в порядке их добавления
	private Map<String, String> map;
	private StringBuffer sb = new StringBuffer();

	public static final String ELEMENT_OPEN_START_TAG  = "<";
	public static final String ELEMENT_CLOSE_START_TAG  = "</";
	public static final String ELEMENT_END_TAG  = ">";
	public static final String EMPTY_ELEMENT_END_TAG  = "/>";
	public static final String ARGUMENTS_SEPARATOR  = " ";
	public static final String ARGUMENT_KEY_VALUE_SEPARATOR  = "=";
	public static final String ARGUMENT_VALUE_QUOTE = "\"";
}