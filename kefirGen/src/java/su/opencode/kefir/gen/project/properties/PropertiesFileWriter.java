/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 12:54:37$
*/
package su.opencode.kefir.gen.project.properties;

import org.apache.commons.lang.StringEscapeUtils;
import su.opencode.kefir.gen.fileWriter.FileWriter;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.util.StringUtils.concat;

public class PropertiesFileWriter
{
	public PropertiesFileWriter(String fileName) {
		this.fileName = fileName;
	}
	public PropertiesFileWriter(String fileName, ProjectConfig config) {
		this.fileName = fileName;
		this.config = config;
	}

	public void writeFile() throws IOException {
		File file = new File(fileName);
		out = new FileWriter(file, PROPERTIES_FILE_ENCODING);

		try
		{
			writeProperties();
		}
		finally
		{
			out.close();
		}
	}
	protected void writeProperties() throws IOException {
		// do nothing, this method should be overridden is subclasses
	}
	protected void writeProperty(String key, String value) throws IOException {
		out.writeLn(key, KEY_VALUE_SEPARATOR, StringEscapeUtils.escapeJava(value));
	}
	protected void writeComment(String comment) throws IOException {
		out.writeLn(COMMENT_MARKER, comment);
	}

	protected String fileName;
	protected ProjectConfig config;
	protected FileWriter out;


	protected StringBuffer sb = new StringBuffer();

	public static final String KEY_VALUE_SEPARATOR = "=";
	public static final String COMMENT_MARKER = "#";
	public static final String PROPERTIES_FILE_ENCODING = "UTF8";
}