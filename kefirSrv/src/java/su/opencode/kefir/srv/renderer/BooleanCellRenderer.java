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

public class BooleanCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (!(value instanceof Boolean))
			throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to Boolean"));

		return ((Boolean) value) ? "да" : "нет";
	}
}