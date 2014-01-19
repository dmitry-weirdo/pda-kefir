/**
 Copyright 2013 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.util.test;

import junit.framework.TestCase;
import su.opencode.kefir.util.SqlUtils;

import static su.opencode.kefir.util.StringUtils.concat;

public class SqlUtilsTests extends TestCase
{
	public void testGetSearchParamFirebird() { // todo: move to SqlUtilsTest class
		String value;
		String escapedValue;
		StringBuilder sb = new StringBuilder();

		value = "search";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%search%' escape '\\'", escapedValue);

		value = "some%value";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%some\\%value%' escape '\\'", escapedValue);

		value = "%some%value%";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%\\%some\\%value\\%%' escape '\\'", escapedValue);

		value = "'some'value'";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%''some''value''%' escape '\\'", escapedValue);

		value = "\\some\\value\\";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%\\\\some\\\\value\\\\%' escape '\\'", escapedValue);

		value = "_some_value_";
		escapedValue = SqlUtils.getSearchParamFirebird(value);
		System.out.println( concat(sb, "value: ", value, "\nescaped value: ", escapedValue, "\n"));
		assertEquals("'%\\_some\\_value\\_%' escape '\\'", escapedValue);
	}
}