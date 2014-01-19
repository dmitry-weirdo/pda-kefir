/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 19:58:34$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;

import java.io.IOException;

public class EjbJarFileWriter extends XmlFileWriter
{
	public EjbJarFileWriter(String fileName) {
		super(fileName);
	}
	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.addAttribute(XMNLS_PARAMETER_NAME, XMLNS);
		writer.addAttribute(XMLNS_XSI_PARAMETER_NAME, XMLNS_XSI);
		writer.addAttribute(XSI_SCHEMA_LOCATION_PARAMETER_NAME, XSI_SCHEMA_LOCATION);
		writer.addAttribute(VERSION_PARAMETER_NAME, VERSION);

		writer.startElement(EJB_JAR_ELEMENT_NAME, true);
		writer.endElement(EJB_JAR_ELEMENT_NAME);
	}

	public static final String XMLNS = "http://java.sun.com/xml/ns/javaee";
	public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSI_SCHEMA_LOCATION = "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd";
	public static final String VERSION = "3.0";

	public static final String EJB_JAR_ELEMENT_NAME = "ejb-jar";
}