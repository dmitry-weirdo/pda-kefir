/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.fileWriter.SimpleFileWriter;
import su.opencode.kefir.util.DateUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

public class TestDataJsonSimpleFileWriter extends SimpleFileWriter
{
	public TestDataJsonSimpleFileWriter(String baseDir, Class entityClass) {
		this.baseDir = baseDir;
		this.fileName = concat(sb, entityClass.getSimpleName(), ".json");
		this.entityClass = entityClass;

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeFile() throws IOException {
		final Field[] declaredFields = entityClass.getDeclaredFields();

		out.writeLn("[");
		out.writeLn(TAB, "{");
		out.writeLn(DOUBLE_TAB, "\"createEntityData\":{");

		writeData(declaredFields, true);

		out.writeLn(DOUBLE_TAB, "},");

		out.writeLn();

		out.writeLn(DOUBLE_TAB, "\"updateEntityData\":{");

		writeData(declaredFields, false);

		out.writeLn(DOUBLE_TAB, "}");
		out.writeLn(TAB, "}");
		out.writeLn("]");
	}
	private void writeData(Field[] declaredFields, boolean create) throws IOException {
		boolean isFirstTime = true;
		for (final Field f : declaredFields)
		{
			final String comma = isFirstTime ? "  " : ", ";
			for (Annotation a : f.getDeclaredAnnotations())
			{
				final String fieldName = f.getName();
				if (a instanceof TextField)
				{
					isFirstTime = writeString(create, comma, fieldName, ((TextField) a).maxLength());
					continue;
				}

				if (a instanceof TextAreaField)
				{
					isFirstTime = writeString(create, comma, fieldName, ((TextAreaField) a).maxLength());
					continue;
				}

				if (a instanceof OgrnTextField)
				{
					isFirstTime = false;
					out.writeLn(TRIPLE_TAB, comma, "\"", fieldName, "\":\"", OGRN_CORRECT_NUMBER, "\"");
					continue;
				}

				if (a instanceof KppTextField)
				{
					isFirstTime = false;
					out.writeLn(TRIPLE_TAB, comma, "\"", fieldName, "\":\"", KPP_CORRECT_NUMBER, "\"");
					continue;
				}

				if (a instanceof InnJuridicalTextField)
				{
					isFirstTime = false;
					out.writeLn(TRIPLE_TAB, comma, "\"", fieldName, "\":\"", INN_JURIDICAL_CORRECT_NUMBER, "\"");
					continue;
				}

				if (a instanceof DateField)
				{
					isFirstTime = false;
					out.writeLn(TRIPLE_TAB, comma, "\"", fieldName, "\":\"", DateUtils.getDayMonthYear(new Date()), "\"");
//					continue;
				}
			}
		}
	}
	private boolean writeString(boolean create, String comma, String fieldName, int maxLength) throws IOException {
		boolean isFirstTime;
		isFirstTime = false;
		String str = create ? fieldName : concat(sb, fieldName, "Update");
		if (str.length() > maxLength)
			str = str.substring(0, maxLength);

		out.writeLn(TRIPLE_TAB, comma, "\"", fieldName, "\":\"", str, "\"");
		return isFirstTime;
	}

	private static final Long OGRN_CORRECT_NUMBER = 1111111111110L;
	private static final Long KPP_CORRECT_NUMBER = 123456789L;
	private static final Long INN_PHYSICS_CORRECT_NUMBER = 111111111130L;
	private static final Long INN_JURIDICAL_CORRECT_NUMBER = 1111111117L;

	private final Class entityClass;
}