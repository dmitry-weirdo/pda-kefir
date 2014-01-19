/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 10:52:41$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;

import java.io.IOException;

public class JbossXmlFileWriter extends XmlFileWriter
{
	public JbossXmlFileWriter(String fileName) {
		super(fileName);
	}
	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.startDTD(DOCTYPE);
		writer.endDTD();

		writer.startElement(JBOSS_ELEMENT_NAME);
		// todo: security domain
		writer.simpleElement(UNAUTHENTICATED_PRINCIPAL_ELEMENT_NAME, UNAUTHENTICATED_PRINCIPAL_VALUE);
		writer.endElement(JBOSS_ELEMENT_NAME);
	}

	public static final String DOCTYPE = "jboss PUBLIC \"-//JBoss//DTD JBOSS 4.2//EN\" \"http://www.jboss.org/j2ee/dtd/jboss_4_2.dtd\"";

	public static final String JBOSS_ELEMENT_NAME = "jboss";
	public static final String UNAUTHENTICATED_PRINCIPAL_ELEMENT_NAME = "unauthenticated-principal";
	public static final String UNAUTHENTICATED_PRINCIPAL_VALUE = "guest";
}