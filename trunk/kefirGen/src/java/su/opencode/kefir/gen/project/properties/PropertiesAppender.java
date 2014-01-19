/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 11.04.2012 12:40:40$
*/
package su.opencode.kefir.gen.project.properties;

import org.apache.commons.lang.StringEscapeUtils;
import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.project.properties.PropertiesFileWriter.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class PropertiesAppender extends Appender
{
	public PropertiesAppender(String filePath) {
		this.filePath = filePath;
	}
	public PropertiesAppender(String filePath, ProjectConfig config) {
		this.filePath = filePath;
		this.config = config;
	}

	@Override
	protected String getEncoding() {
		return PROPERTIES_FILE_ENCODING;
	}

	protected void appendProperty(List<String> fileLines, String key, String value) throws IOException {
		fileLines.add( concat(sb, key, KEY_VALUE_SEPARATOR, StringEscapeUtils.escapeJava(value)) );
	}
	protected void appendComment(List<String> fileLines, String comment) {
		fileLines.add( concat(sb, COMMENT_MARKER, comment));
	}

	protected String filePath;
	protected ProjectConfig config;
}