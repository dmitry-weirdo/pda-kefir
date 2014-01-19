/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 22.03.2012 16:04:34$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.appender.service.ServiceReferenceAppender;
import su.opencode.kefir.gen.appender.servlet.ServletMappingAppender;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.web.util.CharsetFilter;
import su.opencode.kefir.web.util.ServiceFactory;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.INDEX_JSP_FILE_NAME;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getEarNameWithNoExtension;
import static su.opencode.kefir.util.StringUtils.concat;

public class WebXmlFileWriter extends XmlFileWriter
{
	public WebXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.clearAttributes();
		writer.addAttribute(XMNLS_PARAMETER_NAME, XMLNS);
		writer.addAttribute(XMLNS_XSI_PARAMETER_NAME, XMLNS_XSI);
		writer.addAttribute(XSI_SCHEMA_LOCATION_PARAMETER_NAME, XSI_SCHEMA_LOCATION);
		writer.addAttribute(VERSION_PARAMETER_NAME, VERSION);

		writer.startElement(WEB_APP_ELEMENT_NAME, true);

		writer.simpleElement(DESCRIPTION_ELEMENT_NAME, getDescription());
		writer.writeLn();

		writeContextParams();
		writer.writeLn();

		writerFilters();
		writer.writeLn();

		writeFilterMappings();
		writer.writeLn();

		writer.characters("");
		writer.writeComment("          servlets declaration          ");
		writer.characters("");
		writer.writeComment(ServletMappingAppender.APPEND_DECLARATION_MARKER);
		writer.writeLn();

		writer.characters("");
		writer.writeComment("          servlets mappings          ");
		writer.characters("");
		writer.writeComment(ServletMappingAppender.APPEND_MAPPING_MARKER);
		writer.writeLn();

		writer.characters("");
		writer.writeComment("          ejb references         ");
		writer.characters("");
		writer.writeComment(ServiceReferenceAppender.APPEND_EJB_LOCAL_REFERENCE_MARKER);
		writer.writeLn();

		writeWelcomeFilesList();

		writer.endElement(WEB_APP_ELEMENT_NAME);
	}
	private String getDescription() {
		return concat(sb, config.projectName, " application");
	}

	private void writeContextParams() throws SAXException {
		writeServiceFactoryModeContextParam();
		writeServiceFactoryEarNameContextParam();
		writeFileEncodingContextParam();
	}
	private void writeServiceFactoryModeContextParam() throws SAXException {
		writer.startElement(CONTEXT_PARAM_ELEMENT_NAME);
		writer.simpleElement(CONTEXT_PARAM_DESCRIPTION_ELEMENT_NAME, concat(sb, ServiceFactory.class.getName(), " work mode"));
		writer.simpleElement(CONTEXT_PARAM_NAME_ELEMENT_NAME, ServiceFactory.CONFIG_KEY);
		writer.simpleElement(CONTEXT_PARAM_VALUE_ELEMENT_NAME, ServiceFactory.LOCAL_MODE);
		writer.endElement(CONTEXT_PARAM_ELEMENT_NAME);
	}
	private void writeServiceFactoryEarNameContextParam() throws SAXException {
		writer.startElement(CONTEXT_PARAM_ELEMENT_NAME);
		writer.simpleElement(CONTEXT_PARAM_DESCRIPTION_ELEMENT_NAME, "application ear name");
		writer.simpleElement(CONTEXT_PARAM_NAME_ELEMENT_NAME, ServiceFactory.EAR_NAME_KEY);
		writer.simpleElement(CONTEXT_PARAM_VALUE_ELEMENT_NAME, getEarNameWithNoExtension(config));
		writer.endElement(CONTEXT_PARAM_ELEMENT_NAME);
	}
	private void writeFileEncodingContextParam() throws SAXException {
		writer.startElement(CONTEXT_PARAM_ELEMENT_NAME);
		writer.simpleElement(CONTEXT_PARAM_NAME_ELEMENT_NAME, FILE_ENCODING_PARAM_NAME);
		writer.simpleElement(CONTEXT_PARAM_VALUE_ELEMENT_NAME, FILE_ENCODING_PARAM_VALUE);
		writer.endElement(CONTEXT_PARAM_ELEMENT_NAME);
	}

	private void writerFilters() throws SAXException {
		writeCharsetFilter();
	}
	private void writeCharsetFilter() throws SAXException {
		writer.startElement(FILTER_ELEMENT_NAME);

		writer.simpleElement(FILTER_NAME_ELEMENT_NAME, CHARSET_FILTER_NAME);
		writer.simpleElement(FILTER_CLASS_ELEMENT_NAME, CharsetFilter.class.getName());

		writer.startElement(FILTER_INIT_PARAM_ELEMENT_NAME);
		writer.simpleElement(FILTER_INIT_PARAM_NAME_ELEMENT_NAME, CharsetFilter.REQUEST_ENCODING_PARAM_NAME);
		writer.simpleElement(FILTER_INIT_PARAM_VALUE_ELEMENT_NAME, CHARSET_FILTER_REQUEST_ENCODING_PARAM_VALUE);
		writer.endElement(FILTER_INIT_PARAM_ELEMENT_NAME);

		writer.endElement(FILTER_ELEMENT_NAME);
	}

	private void writeFilterMappings() throws SAXException {
		writeCharsetFilterMapping();
	}
	private void writeCharsetFilterMapping() throws SAXException {
		writer.startElement(FILTER_MAPPING_ELEMENT_NAME);

		writer.simpleElement(FILTER_NAME_ELEMENT_NAME, CHARSET_FILTER_NAME);
		writer.simpleElement(FILTER_MAPPING_URL_PATTERN_ELEMENT_NAME, CHARSET_FILTER_URL_PATTERN);

		writer.endElement(FILTER_MAPPING_ELEMENT_NAME);
	}

	private void writeWelcomeFilesList() throws SAXException {
		writer.startElement(WELCOME_FILE_LIST_ELEMENT_NAME);
		writer.simpleElement(WELCOME_FILE_ELEMENT_NAME, INDEX_JSP_FILE_NAME);
		writer.endElement(WELCOME_FILE_LIST_ELEMENT_NAME);
	}

	public static final String WEB_APP_ELEMENT_NAME = "web-app";
	public static final String XMLNS = "http://java.sun.com/xml/ns/javaee";
	public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSI_SCHEMA_LOCATION = "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd";
	public static final String VERSION = "2.5";

	public static final String DESCRIPTION_ELEMENT_NAME = "description";


	public static final String CONTEXT_PARAM_ELEMENT_NAME = "context-param";
	public static final String CONTEXT_PARAM_DESCRIPTION_ELEMENT_NAME = "description";
	public static final String CONTEXT_PARAM_NAME_ELEMENT_NAME = "param-name";
	public static final String CONTEXT_PARAM_VALUE_ELEMENT_NAME = "param-value";

	public static final String FILE_ENCODING_PARAM_NAME = "fileEncoding";
	public static final String FILE_ENCODING_PARAM_VALUE = "UTF-8";

	public static final String FILTER_ELEMENT_NAME = "filter";
	public static final String FILTER_NAME_ELEMENT_NAME = "filter-name";
	public static final String FILTER_CLASS_ELEMENT_NAME = "filter-class";
	public static final String FILTER_INIT_PARAM_ELEMENT_NAME = "init-param";
	public static final String FILTER_INIT_PARAM_NAME_ELEMENT_NAME = "param-name";
	public static final String FILTER_INIT_PARAM_VALUE_ELEMENT_NAME = "param-value";
	public static final String CHARSET_FILTER_NAME = "charsetFilter";
	public static final String CHARSET_FILTER_REQUEST_ENCODING_PARAM_VALUE = "UTF-8";

	public static final String FILTER_MAPPING_ELEMENT_NAME = "filter-mapping";
	public static final String FILTER_MAPPING_URL_PATTERN_ELEMENT_NAME = "url-pattern";

	public static final String CHARSET_FILTER_URL_PATTERN = "/*";

	public static final String WELCOME_FILE_LIST_ELEMENT_NAME = "welcome-file-list";
	public static final String WELCOME_FILE_ELEMENT_NAME = "welcome-file";
}