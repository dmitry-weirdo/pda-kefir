/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 22.03.2012 18:12:20$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.appender.serviceBean.ServiceBeanReferenceAppender;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.getContextPath;

public class JbossWebXmlFileWriter extends XmlFileWriter
{
	public JbossWebXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.startDTD(DOCTYPE);
		writer.endDTD();

		writer.startElement(JBOSS_WEB_ELEMENT_NAME);

		writeContextRoot();
		writer.writeLn();

		writeUseSessionCookies();
		writer.writeLn();

		writer.characters("");
		writer.writeComment(ServiceBeanReferenceAppender.APPEND_EJB_LOCAL_REFERENCE_MARKER);

		writer.endElement(JBOSS_WEB_ELEMENT_NAME);
	}
	private void writeContextRoot() throws SAXException {
		writer.simpleElement(CONTEXT_ROOT_ELEMENT_NAME, getContextPath(config));
	}
	private void writeUseSessionCookies() throws SAXException {
		writer.simpleElement(USE_SESSION_COOKIES_ELEMENT_NAME, USE_SESSION_COOKIES_VALUE);
	}

	public static final String DOCTYPE = "jboss-web PUBLIC \"-//JBoss//DTD Web Application 4.2//EN\" \"http://www.jboss.org/j2ee/dtd/jboss-web_4_2.dtd\"";
	public static final String JBOSS_WEB_ELEMENT_NAME = "jboss-web";
	public static final String CONTEXT_ROOT_ELEMENT_NAME = "context-root";

	public static final String USE_SESSION_COOKIES_ELEMENT_NAME = "use-session-cookies";
	public static final String USE_SESSION_COOKIES_VALUE = "true";
}