/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.js;

import static su.opencode.kefir.util.StringUtils.concat;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class JsArray
{
	public JsArray() {
		values = new ArrayList<String>();
	}
	public JsArray(String... values) {
		this.values = new ArrayList<String>();
		this.values.addAll(Arrays.asList(values));
	}
	public JsArray(boolean strings, String... values) {
		this.values = new ArrayList<String>();

		if (strings)
			for (String value : values)
				this.addString(value);
		else
			this.values.addAll(Arrays.asList(values));
	}

	public void add(String value) {
		values.add(value);
	}
	public void add(int value) {
		values.add( Integer.toString(value) );
	}
	public void add(double value) {
		values.add( Double.toString(value) );
	}
	public void addString(String value) {
		values.add( concat(sb, "'", value, "'") );
	}
	public void add(JsHash hash) {
		values.add(hash.toString());
	}
	public void add(JsArray array) {
		values.add( array.toString() );
	}

	public boolean isEmpty() {
		return values == null || values.isEmpty();
	}

	/**
	 * @return значение массива в строчку
	 */
	public String toString() {
		if (isEmpty())
			return "[]";

		sb.delete(0, sb.length());
		sb.append("[ ");

		for (String value : values)
		{
			sb.append(value).append(FIELDS_SEPARATOR);
		}
		sb.delete(sb.length() - FIELDS_SEPARATOR.length(), sb.length()); // remove trailing ", "

		sb.append(" ]");
		return sb.toString();
	}

	private List<String> values;
	private StringBuffer sb = new StringBuffer();

	public static final String FIELDS_SEPARATOR = ", ";
}