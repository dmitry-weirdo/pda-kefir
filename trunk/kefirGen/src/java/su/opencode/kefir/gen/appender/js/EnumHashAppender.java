/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.js;

import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.gen.ExtEntityUtils.PACKAGE_SEPARATOR;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;
import su.opencode.kefir.gen.fileWriter.js.JsHash;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class EnumHashAppender extends JsAppender
{
	public EnumHashAppender(String filePath, EnumField enumField, Class enumClass) {
		this.filePath = filePath;
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	public void appendEnumHash() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}

	private void appendLines(List<String> fileLines) {
		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.indexOf(APPEND_HASH_MARKER) >= 0)
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendHash(appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendHash(List<String> fileLines) {
		String namespace = getEnumHashNamespace(enumField, enumClass);
		String name = getEnumHashName(enumField, enumClass);

		JsHash hash = new JsHash();

		for (Field field : enumClass.getDeclaredFields())
		{
			if (field.isEnumConstant() && hasEnumFieldValueAnnotation(field))
			{
				EnumFieldValue enumValueField = getEnumFieldValueAnnotation(field);
				hash.putString( getEnumFieldValueHashName(enumValueField, field), getEnumFieldValueHashValue(enumValueField, field) );
			}
		}

		appendNamespace( fileLines, namespace );
		fileLines.add( concat(sb, namespace, PACKAGE_SEPARATOR, name, " = ", hash, ";") );
		fileLines.add("");
	}

	private EnumField enumField;
	private Class enumClass;

	public static final String APPEND_HASH_MARKER = "${APPEND_HASH_CONSTANT}";
}