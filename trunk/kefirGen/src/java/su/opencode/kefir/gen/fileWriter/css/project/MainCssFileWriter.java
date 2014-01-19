/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 12:34:30$
*/
package su.opencode.kefir.gen.fileWriter.css.project;

import su.opencode.kefir.gen.fileWriter.css.CssFileWriter;

import java.io.IOException;

import static su.opencode.kefir.gen.appender.css.ViewConfigStylesAppender.APPEND_STYLE_MARKER;

public class MainCssFileWriter extends CssFileWriter
{
	public MainCssFileWriter(String baseDir, String dir, String fileName) {
		super(baseDir, dir, fileName);
	}

	@Override
	protected void writeFile() throws IOException {
		writeComment(APPEND_STYLE_MARKER);
	}
}