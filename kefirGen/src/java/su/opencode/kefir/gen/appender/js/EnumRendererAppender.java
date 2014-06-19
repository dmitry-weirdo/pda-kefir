/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.js;

import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.PACKAGE_SEPARATOR;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.field.enumField.EnumField.DEFAULT_RENDERER_NAMESPACE;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.SHOW_ERROR_FUNCTION_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class EnumRendererAppender extends JsAppender
{
	public EnumRendererAppender(String filePath, EnumField enumField, Class enumClass) {
		this.filePath = filePath;
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	public void appendEnumRenderer() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) {
		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(APPEND_RENDERER_MARKER))
			{
				List<String> appendedFileLines = new LinkedList<>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendRenderer(appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendRenderer(List<String> fileLines) {
		String namespace = enumField.rendererNamespace();
		String hashName = getEnumHashFullName(enumField, enumClass);
		String valueParamName = "value";

		if (!namespace.equals(DEFAULT_RENDERER_NAMESPACE)) // если рендерер находится не в стандартном неймспейсе, добавить объявление этого неймспейса
			appendNamespace(fileLines, namespace);

		fileLines.add( concat(sb, getEnumRendererFullName(enumField, enumClass), " = function(", valueParamName, ") {") );
		fileLines.add( concat("\tif (!", valueParamName, ")"));
		fileLines.add("\t\treturn '';");
		fileLines.add("");

		fileLines.add( concat(sb, "\tswitch (", valueParamName, ")") );
		fileLines.add("\t{");

		for (Field field : enumClass.getDeclaredFields())
		{
			if (field.isEnumConstant() && hasEnumFieldValueAnnotation(field))
			{
				EnumFieldValue enumValueField = getEnumFieldValueAnnotation(field);
				fileLines.add( concat(sb,
					"\t\tcase ", hashName, PACKAGE_SEPARATOR, getEnumFieldValueHashName(enumValueField, field), ": ",
					"return '", getEnumFieldValueRendererValue(enumValueField), "';"
				) );
			}
		}

		fileLines.add( concat(sb, "\t\tdefault: ", SHOW_ERROR_FUNCTION_NAME, "('Unknown ", hashName, " value: ' + ", valueParamName, ");") );
		fileLines.add("\t}"); // end switch

		fileLines.add("};"); // end function(value) {
		fileLines.add("");
	}

	private EnumField enumField;
	private Class enumClass;

	public static final String APPEND_RENDERER_MARKER = "${APPEND_RENDERER}";
}