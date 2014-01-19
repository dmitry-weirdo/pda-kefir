/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 19:06:44$
*/
package su.opencode.kefir.gen.project.xml.jsp;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.fileWriter.FileWriter;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.xml.XmlElement;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.gen.project.xml.html.HtmlFileWriter.*;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.FileUtils.createDirs;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class JspFileWriter
{
	public JspFileWriter(String baseDir, String dir, String fileName) {
		this.baseDir = baseDir;
		this.dir = dir;
		this.fileName = fileName;
	}

	public void createFile() throws IOException {
		File file = createJspFile();
		if (file.exists())
		{ // todo: возможность внешним образом задавать перезапись существующих файлов
			if (failIfFileExists)
			{
				throw new IllegalStateException( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists") );
			}
			else
			{ // оставить существующий файл как есть
				logger.info( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists. It is not overwritten.") );
				return;
			}
		}

		out = new FileWriter(file, JSP_FILE_ENCODING);

		try
		{
			writeFile();
		}
		finally
		{
			out.close();
		}
	}

	private File createJspFile() {
		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, dir);
		createDirs(dirPath); // если директорий нет, создать их

		String filePath;
		if (fileName.endsWith(JSP_FILE_EXTENSION) || fileName.endsWith(HTML_FILE_EXTENSION)) // если имя файла уже содержит ".jsp" или ".html", не добавлять его
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName);
		else
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName, JSP_FILE_EXTENSION); // добавить ".jsp" к имени файла

		return new File(filePath);
	}

	protected void writeEncoding() throws IOException {
		out.writeLn( "<%@ page pageEncoding=\"", JSP_FILE_ENCODING, "\" language=\"java\" %>" );
	}

	protected void writeInclude(String filePath, String indent) throws IOException {
		out.writeLn(indent, "<%@ include file=\"", filePath, "\" %>");
	}
	protected void writeInclude(String filePath) throws IOException {
		writeInclude(filePath, "\t");
	}

	protected void writeMeta() throws IOException {
		out.writeLn("\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=", JSP_FILE_ENCODING, "\"/>");
	}
	protected void writeDoctype() throws IOException {
		out.writeLn(DOCTYPE);
	}

	protected void writeScriptElementStart() throws IOException {
		out.writeLn("<", SCRIPT_ELEMENT_NAME, " type=\"", JS_SCRIPT_TYPE, "\">");
	}
	protected void writeScriptElementEnd() throws IOException {
		writeElementEnd(SCRIPT_ELEMENT_NAME);
	}
	protected void writeScriptInclude(String src) throws IOException {
		XmlElement script = new XmlElement(SCRIPT_ELEMENT_NAME, "");
		script.addAttribute(TYPE_ATTRIBUTE_NAME, JS_SCRIPT_TYPE);
		script.addAttribute(SRC_ATTRIBUTE_NAME, src);

		out.writeLn(script.simpleElement()); // need to use <script></script>
	}

	protected String getElementStart(String elementName) {
		return concat(sb, "<", elementName, ">");
	}
	protected void writeElementStart(String elementName, String indent) throws IOException {
		out.writeLn(indent, getElementStart(elementName));
	}
	protected void writeElementStart(String elementName) throws IOException {
		writeElementStart(elementName, "");
	}

	protected String getElementEnd(String elementName) {
		return concat(sb, "</", elementName, ">");
	}
	protected void writeElementEnd(String elementName, String indent) throws IOException {
		out.writeLn(indent, getElementEnd(elementName));
	}
	protected void writeElementEnd(String elementName) throws IOException {
		out.writeLn(getElementEnd(elementName));
	}
	protected void writeElementEnd(String elementName, boolean newLine) throws IOException {
		if (newLine)
			writeElementEnd(elementName);
		else
			out.write(getElementEnd(elementName));
	}

	protected String getSimpleElement(String elementName, String value) {
		return concat(sb, getElementStart(elementName), value, getElementEnd(elementName));
	}
	protected void writeSimpleElement(String elementName, String value, String indent) throws IOException {
		out.writeLn(indent, getSimpleElement(elementName, value));
	}
	protected void writeSimpleElement(String elementName, String value) throws IOException {
		writeSimpleElement(elementName, value, "\t");
	}

	protected void writeScriptletStart() throws IOException {
		out.writeLn(SCRIPTLET_START);
	}
	protected void writeScriptletEnd() throws IOException {
		out.writeLn(SCRIPTLET_END);
	}
	protected String getScriptletVariableAssignation(String varName) {
		return concat(sb, SCRIPTLET_VARIABLE_START, varName, SCRIPTLET_VARIABLE_END);
	}

	protected abstract void writeFile() throws IOException;

	protected boolean failIfFileExists = false;

	protected ProjectConfig config;

	protected String baseDir;
	protected String dir;
	protected String fileName;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	protected static final Logger logger = Logger.getLogger(JspFileWriter.class);

	public static final String JSP_FILE_ENCODING = "UTF-8";
	public static final String JSP_FILE_EXTENSION = ".jsp";

	public static final String SCRIPTLET_START = "<%";
	public static final String SCRIPTLET_END = "%>";

	public static final String SCRIPTLET_VARIABLE_START = "<%=";
	public static final String SCRIPTLET_VARIABLE_END = "%>";

	public static final String NBSP = "&nbsp;";
	public static final String LAQUO_MNEMONIC = "&laquo;";
	public static final String RAQUO_MNEMONIC = "&raquo;";

	public static final String HTML_ELEMENT_NAME = "html";
	public static final String HEAD_ELEMENT_NAME = "head";
	public static final String TITLE_ELEMENT_NAME = "title";
	public static final String BODY_ELEMENT_NAME = "body";

	public static final String H1_ELEMENT_NAME = "h1";
	public static final String H2_ELEMENT_NAME = "h2";
	public static final String H3_ELEMENT_NAME = "h3";
	public static final String H4_ELEMENT_NAME = "h4";

	public static final String DIV_ELEMENT_NAME = "div";
	public static final String SPAN_ELEMENT_NAME = "span";
	public static final String FORM_ELEMENT_NAME = "form";
	public static final String IMG_ELEMENT_NAME = "img";

	public static final String TABLE_ELEMENT_NAME = "table";
	public static final String TR_ELEMENT_NAME = "tr";
	public static final String TD_ELEMENT_NAME = "td";
	public static final String COL_ELEMENT_NAME = "col";

	public static final String A_ELEMENT_NAME = "a";
	public static final String LABEL_ELEMENT_NAME = "label";

	public static final String ID_ATTRIBUTE_NAME = "id";
	public static final String CLASS_ATTRIBUTE_NAME = "class";
	public static final String STYLE_ATTRIBUTE_NAME = "style";
	public static final String TITLE_ATTRIBUTE_NAME = "title";
	public static final String COLSPAN_ATTRIBUTE_NAME = "colspan";
	public static final String ROWSPAN_ATTRIBUTE_NAME = "rowspan";
	public static final String TYPE_ATTRIBUTE_NAME = "type";
	public static final String SRC_ATTRIBUTE_NAME = "src";
	public static final String ALT_ATTRIBUTE_NAME = "alt";
	public static final String FOR_ATTRIBUTE_NAME = "for";
	public static final String ACTION_ATTRIBUTE_NAME = "action";
	public static final String WIDTH_ATTRIBUTE_NAME = "width";

	public static final String SESSION_VAR_NAME = "session";

	public static final String REQUEST_VAR_NAME = "request";
	public static final String REQUEST_GET_PARAMETER_METHOD_NAME = "getParameter";

	public static final String DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
}