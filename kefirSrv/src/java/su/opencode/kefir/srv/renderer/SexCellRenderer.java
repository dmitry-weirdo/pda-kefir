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

public class SexCellRenderer implements CellRenderer
{
	public String render(Object value) {
		if (value == null)
			return null;

		if (value instanceof String)
		{
			switch ((String) value)
			{
				case "1":
				case "M":
				case "m":
				case MALE:
				case "�":
					return MALE;

				case "0":
				case "F":
				case "f":
				case FEMALE:
				case "�":
					return FEMALE;
			}
		}

		if (value instanceof Boolean)
			return ((Boolean) value) ? MALE : FEMALE;

		throw new ClassCastException(concat(value.getClass().getName(), " can't be cast to String or Boolean"));
	}

	private static final String MALE = "�";
	private static final String FEMALE = "�";
}