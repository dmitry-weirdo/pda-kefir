/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv.render;

import su.opencode.kefir.sampleSrv.TestEnum;
import su.opencode.kefir.srv.renderer.CellRenderer;

import static su.opencode.kefir.util.StringUtils.concat;

public class TestEnumCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (!(value instanceof TestEnum))
			throw new ClassCastException( concat(value.getClass().getName(), " can't be cast to ", TestEnum.class.getName()) );

		switch ((TestEnum) value)
		{
			case sugar: return "Сахар";
			case plum: return "Слива";
			case fairy: return "Фея";
			default: throw new IllegalArgumentException( concat("Incorrect ", TestEnum.class.getName(), " value: \"", value, "\"") );
		}
	}
}