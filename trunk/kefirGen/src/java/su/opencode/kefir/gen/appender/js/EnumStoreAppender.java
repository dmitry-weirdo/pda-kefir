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
import su.opencode.kefir.gen.fileWriter.js.JsArray;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.EXT_ARRAY_STORE_CLASS_NAME;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class EnumStoreAppender extends JsAppender
{
	public EnumStoreAppender(String filePath, EnumField enumField, Class enumClass) {
		this.filePath = filePath;
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	public void appendEnumStore() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) {
		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.indexOf(APPEND_STORE_MARKER) >= 0)
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendStore(appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendStore(List<String> fileLines) {
		String namespace = getEnumStoreNamespace(enumField, enumClass);
		String name = getEnumStoreName(enumField, enumClass);
		String hashName = getEnumHashFullName(enumField, enumClass);

		JsArray fields = new JsArray();
		fields.addString(enumField.storeValueFieldName());
		fields.addString(enumField.storeDisplayFieldName());

		appendNamespace( fileLines, namespace );
		fileLines.add( concat(sb, namespace, PACKAGE_SEPARATOR, name, " = new ", EXT_ARRAY_STORE_CLASS_NAME, "({") );
		fileLines.add( concat(sb, "\tfields: ", fields, ",") );

		fileLines.add("\tdata: [");
		for (Field field : enumClass.getDeclaredFields())
		{
			if (field.isEnumConstant() && hasEnumFieldValueAnnotation(field))
			{
				EnumFieldValue enumValueField = getEnumFieldValueAnnotation(field);

				JsArray enumFieldValueData = new JsArray();
				enumFieldValueData.add( concat(sb, hashName, PACKAGE_SEPARATOR, getEnumFieldValueHashName(enumValueField, field)) );
				enumFieldValueData.addString(enumValueField.storeValue());

				fileLines.add( concat(sb, "\t\t", enumFieldValueData, ",") );
			}
		}
		// remove last ","
		String lastString = fileLines.get(fileLines.size() - 1);
		if (lastString.endsWith(","))
			fileLines.set( fileLines.size() - 1, lastString.substring(0, lastString.length() - 1) );

		fileLines.add("\t]"); // end  "data ["

		fileLines.add("});"); // end new Ext.data.ArrayStore({
		fileLines.add("");
	}

	private EnumField enumField;
	private Class enumClass;

	public static final String APPEND_STORE_MARKER = "${APPEND_STORE}";
}