/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 03.04.2012 15:44:31$
*/
package su.opencode.kefir.gen.project.sql.appender;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class CreateTablesSqlAppender extends SqlAppender
{
	public CreateTablesSqlAppender(String filePath, ProjectConfig config) {
		super(filePath, config);
	}

	public void appendCreateTable() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_CREATE_TABLE_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.indexOf(appendMarker) >= 0)
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendCreateTable(appendedFileLines, fileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	protected abstract void appendCreateTable(List<String> fileLinesToAppend, List<String> allFileLines) throws IOException;

	public static final String APPEND_CREATE_TABLE_MARKER = "${APPEND_CREATE_TABLE}";
}