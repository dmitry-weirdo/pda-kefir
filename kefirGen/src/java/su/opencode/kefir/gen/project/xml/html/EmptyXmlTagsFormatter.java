/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 15:54:15$
*/
package su.opencode.kefir.gen.project.xml.html;

import su.opencode.kefir.gen.appender.Appender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.project.xml.XmlFileWriter.XML_FILE_ENCODING;

/**
 * Удаляет пробелы в пустых простых тэгах.
 * Например "&lt;script&gt; &lt;/script&gt;" превратится в "&lt;script&gt;&lt;/script&gt;"
 */
public class EmptyXmlTagsFormatter extends Appender
{
	public EmptyXmlTagsFormatter(String filePath) {
		this.filePath = filePath;
	}
	@Override
	protected String getEncoding() {
		return XML_FILE_ENCODING;
	}

	public void clearSpaces() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		List<String> correctedFileLines = deleteSpaces(fileLines);
		writeLinesToFile(file, correctedFileLines);
	}
	private List<String> deleteSpaces(List<String> fileLines) {
		List<String> correctedFileLines = new ArrayList<String>();

		for (String fileLine : fileLines)
			correctedFileLines.add( fileLine.replaceAll(">\\s+<", "><") );

		fileLines.clear();
		return correctedFileLines;
	}

	/**
	 * Название xml (html) файла, в котором выполняется удаление пробелов.
	 */
	private String filePath;
}