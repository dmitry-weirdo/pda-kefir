/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 13.04.2012 13:59:48$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter.KEFIR_SRV_JAR_NAME_VALUE;
import static su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter.KEFIR_UTIL_JAR_NAME_VALUE;

public class ApplicationXmlFileWriter extends XmlFileWriter
{
	public ApplicationXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.clearAttributes();
		writer.addAttribute(XMNLS_PARAMETER_NAME, XMLNS);
		writer.addAttribute(XMLNS_XSI_PARAMETER_NAME, XMLNS_XSI);
		writer.addAttribute(XSI_SCHEMA_LOCATION_PARAMETER_NAME, XSI_SCHEMA_LOCATION);
		writer.addAttribute(VERSION_PARAMETER_NAME, VERSION);

		writer.startElement(APPLICATION_ELEMENT_NAME, true);

		writeKefirUtilModule();
		writeKefirSrvModule();
		writeSrvModule();
		writeWebModule();

		writer.endElement(APPLICATION_ELEMENT_NAME);
	}
	private void writeKefirUtilModule() throws SAXException {
		writer.startElement(MODULE_ELEMENT_NAME);
		writer.simpleElement(JAVA_ELEMENT_NAME, KEFIR_UTIL_JAR_NAME_VALUE);
		writer.endElement(MODULE_ELEMENT_NAME);
	}
	private void writeKefirSrvModule() throws SAXException {
		writer.startElement(MODULE_ELEMENT_NAME);
		writer.simpleElement(EJB_ELEMENT_NAME, KEFIR_SRV_JAR_NAME_VALUE);
		writer.endElement(MODULE_ELEMENT_NAME);
	}
	private void writeSrvModule() throws SAXException {
		writer.startElement(MODULE_ELEMENT_NAME);
		writer.simpleElement(EJB_ELEMENT_NAME, getSrvJarName(config));
		writer.endElement(MODULE_ELEMENT_NAME);
	}
	private void writeWebModule() throws SAXException {
		writer.startElement(MODULE_ELEMENT_NAME);

		writer.startElement(WEB_ELEMENT_NAME);
		writer.simpleElement(WEB_URI_ELEMENT_NAME, getWebWarName(config));
		writer.simpleElement(CONTEXT_ROOT_ELEMENT_NAME, getContextPath(config));
		writer.endElement(WEB_ELEMENT_NAME);

		writer.endElement(MODULE_ELEMENT_NAME);
	}

	public static final String APPLICATION_ELEMENT_NAME = "application";
	public static final String XMLNS = "http://java.sun.com/xml/ns/javaee";
	public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSI_SCHEMA_LOCATION = "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/application_5.xsd";
	public static final String VERSION = "5";

	public static final String MODULE_ELEMENT_NAME = "module";
	public static final String JAVA_ELEMENT_NAME = "java";
	public static final String EJB_ELEMENT_NAME = "ejb";

	public static final String WEB_ELEMENT_NAME = "web";
	public static final String WEB_URI_ELEMENT_NAME = "web-uri";
	public static final String CONTEXT_ROOT_ELEMENT_NAME = "context-root";
}