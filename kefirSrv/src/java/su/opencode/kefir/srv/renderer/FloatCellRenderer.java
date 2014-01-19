/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.renderer;

import su.opencode.kefir.util.StringUtils;

import static su.opencode.kefir.util.StringUtils.concat;

public class FloatCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (value instanceof Float)
			return StringUtils.formatDouble((Float) value);

		if (value instanceof Double)
			return StringUtils.formatDouble((Double) value);

		throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to Float"));
	}
}