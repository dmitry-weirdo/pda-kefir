/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.renderer;


public class StringCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		return value.toString();
	}
}