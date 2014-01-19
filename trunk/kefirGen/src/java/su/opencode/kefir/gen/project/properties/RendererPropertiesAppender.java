/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 11.04.2012 12:45:44$
*/
package su.opencode.kefir.gen.project.properties;

import su.opencode.kefir.gen.field.enumField.EnumField;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getEnumFieldRendererClassFullName;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getEnumRendererFullName;

public class RendererPropertiesAppender extends PropertiesAppender
{
	public RendererPropertiesAppender(String filePath, EnumField enumField, Class enumClass) {
		super(filePath);
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	public void appendRenderer() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_RENDERER_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.indexOf(appendMarker) >= 0)
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendRenderer(appendedFileLines, fileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	protected void appendRenderer(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException {
		String key = getEnumRendererFullName(enumField, enumClass);
		String value = getEnumFieldRendererClassFullName(enumField, enumClass);

		// если уже есть такое свойство, не добавлять его
		for (String allFileLine : allFileLines)
			if (allFileLine.startsWith(key)) // todo: нормальная проверка (не учитывать комменты)
				return;

		appendProperty(fileLinesToAppend, key, value);
	}

	private EnumField enumField;
	private Class enumClass;

	public static final String APPEND_RENDERER_MARKER = "${APPEND_RENDERER}";
}