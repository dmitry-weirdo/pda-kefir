/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 24.09.2010 19:42:51$
*/
package su.opencode.kefir.util;

public class EnumUtils
{
	public static <T extends Enum> T getEnum(Class<T> aClass, String enumName) {
		for (Enum mode : aClass.getEnumConstants())
			if (mode.name().equals(enumName))
				return (T) mode;

		throw new RuntimeException(StringUtils.concat("Incorrect name '", enumName, "' for enum ", aClass.getSimpleName()));
	}
	public static <T extends Enum> T getEnum(Class<T> aClass, int enumOrdinal) {
		for (Enum mode : aClass.getEnumConstants())
			if (mode.ordinal() == enumOrdinal)
				return (T) mode;

		throw new RuntimeException(StringUtils.concat("Incorrect oridinal '", enumOrdinal, "' for enum ", aClass.getSimpleName()));
	}
}