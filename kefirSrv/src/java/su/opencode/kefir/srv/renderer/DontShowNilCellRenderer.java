/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.renderer;


import static su.opencode.kefir.util.StringUtils.concat;

public class DontShowNilCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (!(value instanceof Number))
			throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to Number"));

		final Long number = ((Number) value).longValue();

		return number.equals((long) 0) ? "" : number.toString();
	}
}