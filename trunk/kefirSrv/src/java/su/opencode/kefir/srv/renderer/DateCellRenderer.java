/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.renderer;


import java.util.Date;

import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.StringUtils.concat;

public class DateCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (!(value instanceof Date))
			throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to Date"));

		return getDayMonthYear((Date) value);
	}
}