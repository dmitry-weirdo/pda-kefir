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
import static su.opencode.kefir.util.StringUtils.formatDouble;

public class DontShowNilFloatCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (!(value instanceof Number))
			throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to Number"));

		final Number number = (Number) value;

		return number.equals(0) ? "" : formatDouble((Double) value);
	}
}