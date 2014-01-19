/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 11.04.2012 16:53:50$
*/
package su.opencode.kefir.gen.appender;

import su.opencode.kefir.gen.field.enumField.EnumField;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getEnumFieldRenderersConstantName;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getEnumRendererFullName;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.CLASS_FILE_ENCODING;

public class RenderersAppender extends Appender
{
	public RenderersAppender(String filePath, EnumField enumField, Class enumClass) {
		this.filePath = filePath;
		this.enumField = enumField;
		this.enumClass = enumClass;
	}

	protected String getEncoding() {
		return CLASS_FILE_ENCODING;
	}

	public void appendRendererConstant() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_RENDERER_CONSTANT_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
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
	private void appendRenderer(List<String> fileLinesToAppend, List<String> allFileLines) {
		addPublicStringConstant(getEnumFieldRenderersConstantName(enumField, enumClass), getEnumRendererFullName(enumField, enumClass), fileLinesToAppend, allFileLines);
	}

	protected String filePath;
	private EnumField enumField;
	private Class enumClass;

	public static final String APPEND_RENDERER_CONSTANT_MARKER = "${APPEND_RENDERER_CONSTANT}";
}