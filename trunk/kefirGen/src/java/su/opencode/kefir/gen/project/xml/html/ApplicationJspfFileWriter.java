/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 17:44:38$
*/
package su.opencode.kefir.gen.project.xml.html;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.appender.jsInclude.JsIncludeAppender;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class ApplicationJspfFileWriter extends HtmlFileWriter
{
	public ApplicationJspfFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}
	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.writeComment( concat(sb, config.projectName, " scripts include"), true ); // without comment there will be empty string at the beginning of the file
		writeCssInclude( getMainCssIncludePath(config) );
		writer.writeLn();
		writeJsInclude( getConstantsJsIncludePath(config) );
		writeJsInclude( getLocalStoresJsIncludePath(config) );
		writeJsInclude( getRenderersJsIncludePath(config) );
		writeJsInclude( getVtypesJsIncludePath(config) );
		writeJsInclude( getViewConfigsJsIncludePath(config) );
		writer.writeLn();
		writeJsInclude( getAddressJsIncludePath(config) ); // include address script
		writer.writeLn();
		writer.writeLn();
		writer.writeComment(JsIncludeAppender.APPEND_INCLUDE_MARKER, true);
		writer.writeLn();
		writeJsInclude( getMainMenuJsIncludePath(config) ); // mainMenu.js of the application
	}
}