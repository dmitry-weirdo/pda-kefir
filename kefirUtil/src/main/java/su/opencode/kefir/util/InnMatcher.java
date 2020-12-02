/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 12.10.2010 15:52:33$
*/
package su.opencode.kefir.util;

public class InnMatcher
{
	public static boolean isValid(String str) {
		try
		{
			if (str.length() == NOT_JUIRISTIC_INN_SYMBOLS_COUNT)
			{
				String s[] = str.split("");
				int i = 0;
				final int n1 = getInt(s[i++]);
				final int n2 = getInt(s[i++]);
				final int n3 = getInt(s[i++]);
				final int n4 = getInt(s[i++]);
				final int n5 = getInt(s[i++]);
				final int n6 = getInt(s[i++]);
				final int n7 = getInt(s[i++]);
				final int n8 = getInt(s[i++]);
				final int n9 = getInt(s[i++]);
				final int n10 = getInt(s[i++]);
				final int n11 = getInt(s[i++]);
				final int n12 = getInt(s[i]);

				final int valN11 =
					((7 * n1 + 2 * n2 + 4 * n3 + 10 * n4 + 3 * n5 + 5 * n6 + 9 * n7 + 4 * n8 + 6 * n9 + 8 * n10) % 11) % 10;

				if (n11 != valN11)
					return false;

				final int valN12 =
					((3 * n1 + 7 * n2 + 2 * n3 + 4 * n4 + 10 * n5 + 3 * n6 + 5 * n7 + 9 * n8 + 4 * n9 + 6 * n10 + 8 * n11) % 11)
						% 10;

				return n12 == valN12;
			}
			if (str.length() == JUIRISTIC_INN_SYMBOLS_COUNT)
			{
				String s[] = str.split("");
				int i = 0;
				final int n1 = getInt(s[i++]);
				final int n2 = getInt(s[i++]);
				final int n3 = getInt(s[i++]);
				final int n4 = getInt(s[i++]);
				final int n5 = getInt(s[i++]);
				final int n6 = getInt(s[i++]);
				final int n7 = getInt(s[i++]);
				final int n8 = getInt(s[i++]);
				final int n9 = getInt(s[i++]);
				final int n10 = getInt(s[i]);

				final int valN10 =
					((2 * n1 + 4 * n2 + 10 * n3 + 3 * n4 + 5 * n5 + 9 * n6 + 4 * n7 + 6 * n8 + 8 * n9) % 11) % 10;

				return n10 == valN10;
			}

			return false;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	private static int getInt(String str) throws NumberFormatException {
		return Integer.parseInt(str);
	}
	public static String getCorrectInn(Long innNumber, int userType) {
		return getCorrectInn(innNumber.toString(), userType);
	}
	public static String getCorrectInn(String inn, int userType) {
		switch (userType) {
			case JUIRISTIC_INN_SYMBOLS_COUNT:
				return getCorrectLengthInn(inn, JUIRISTIC_INN_SYMBOLS_COUNT);

			case NOT_JUIRISTIC_INN_SYMBOLS_COUNT:
				return getCorrectLengthInn(inn, NOT_JUIRISTIC_INN_SYMBOLS_COUNT);

			default:
				throw new RuntimeException("Incorrect UserType");
		}
	}
	private static String getCorrectLengthInn(String inn, int correctInnSymbolsCount) {
		final int countDigits = inn.length();
		if (countDigits == correctInnSymbolsCount)
			return inn;

		if (countDigits > correctInnSymbolsCount)
			throw new RuntimeException();

		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < correctInnSymbolsCount - countDigits; i++)
			sb.append("0");

		return sb.append(inn).toString();
	}

	public static final int JUIRISTIC_INN_SYMBOLS_COUNT = 10;
	public static final int NOT_JUIRISTIC_INN_SYMBOLS_COUNT = 12;
}
