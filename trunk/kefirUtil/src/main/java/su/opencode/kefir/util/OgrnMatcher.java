/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 12.10.2010 15:52:33$
*/
package su.opencode.kefir.util;

public class OgrnMatcher
{
	public static boolean isValid(String str) {
		try
		{
			if (str.length() != OGRN_SYMBOLS_COUNT)
				return false;

			final long value = getLong(str.substring(0, 12));
			final long controlNumber = getLong(str.substring(12, 13));

			final long val = (value % 11) % 10;

			return val == controlNumber;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	private static long getLong(String str) throws NumberFormatException {
		return Long.parseLong(str);
	}

	private static final int OGRN_SYMBOLS_COUNT = 13;
}