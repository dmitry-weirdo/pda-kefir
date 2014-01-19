/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 19:17:41$
*/
package su.opencode.kefir.gen.project.xml.jsp;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.HTML_URL_SEPARATOR;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.*;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.util.FileUtils.CURRENT_DIR;
import static su.opencode.kefir.util.StringUtils.concat;

public class IndexJspFileWriter extends JspFileWriter
{
	public IndexJspFileWriter(String baseDir, String dir, String fileName, ProjectConfig config) {
		super(baseDir, dir, fileName);
		this.config = config;
	}

	@Override
	protected void writeFile() throws IOException {
		writeEncoding();
		writeElementStart(HTML_ELEMENT_NAME);

		writeElementStart(HEAD_ELEMENT_NAME);
		writeSimpleElement(TITLE_ELEMENT_NAME, config.htmlTitle);
		writeInclude( concat(sb, CURRENT_DIR, HTML_URL_SEPARATOR, KEFIR_INCLUDE_JSPF_FILE_NAME) );
		writeInclude( concat(sb, CURRENT_DIR, HTML_URL_SEPARATOR, APPLICATION_JSPF_FILE_NAME) );
		writeElementEnd(HEAD_ELEMENT_NAME);

		writeElementStart(BODY_ELEMENT_NAME);

		writeScriptElementStart();

		out.writeLn("\t", CONTEXT_PATH_CONSTANT_NAME, " = '<%=request.getContextPath()%>';");
		out.writeLn();

		out.writeLn("\t", EXT_ON_READY_FUNCTION_NAME, "(function() {");
		out.writeLn("\t\t", EXT_QUICK_TIPS_INIT_FUNCTION_NAME, "();");
		out.writeLn();

		out.writeLn("\t\t", getMainMenuInitFunctionFullName(config), "();");
		out.writeLn("\t});");

		writeScriptElementEnd();

		writeElementEnd(BODY_ELEMENT_NAME);
		writeElementEnd(HTML_ELEMENT_NAME, false);
	}
}