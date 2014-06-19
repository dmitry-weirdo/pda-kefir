/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.03.2012 14:37:59$
*/
package su.opencode.kefir.gen.project.xml.html;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.xml.XmlFileWriter;

import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.JS_FILE_ENCODING_FOR_INCLUDE;

public class HtmlFileWriter extends XmlFileWriter
{
	public HtmlFileWriter(String fileName) {
		super(fileName);
		this.omitXmlDeclaration = true;
	}
	public HtmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
		this.omitXmlDeclaration = true;
	}

	protected void writeCssInclude(String href) throws SAXException {
//		<link rel="stylesheet" type="text/css" href="/kefirStatic/fileupload/css/fileuploadfield.css"/>
		writer.clearAttributes();
		writer.addAttribute("rel", "stylesheet");
		writer.addAttribute("type", "text/css");
		writer.addAttribute("href", href);

		writer.emptyElement("link", true);
	}

	protected void writeJsInclude(String src) throws SAXException {
//		<script type="text/javascript" src="/kefirStatic/extUtils/common.js" charset="UTF-8"></script>
		writer.clearAttributes();
		writer.addAttribute("type", JS_SCRIPT_TYPE);
		writer.addAttribute("src", src);
		writer.addAttribute("charset", JS_FILE_ENCODING_FOR_INCLUDE);

		writer.simpleElement(SCRIPT_ELEMENT_NAME, " ", true); // пробел будет в дальнейшем удален с помощью EmptyXmlTagsFormatter
	}

	public static final String SCRIPT_ELEMENT_NAME = "script";
	public static final String JS_SCRIPT_TYPE  = "text/javascript";
	public static final String HTML_FILE_EXTENSION = "html";
}