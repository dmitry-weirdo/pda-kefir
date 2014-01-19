/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 11:21:25$
*/
package su.opencode.kefir.gen.fileWriter.js.project;

import su.opencode.kefir.gen.fileWriter.js.JsFileWriter;

import java.io.IOException;

public class VtypesJsFileWriter extends JsFileWriter
{
	public VtypesJsFileWriter(String baseDir, String dir, String fileName) {
		super(baseDir, dir, fileName);
	}

	@Override
	protected void writeFile() throws IOException {
		writeNamespace(VTYPE_NAMESPACE);
	}
}