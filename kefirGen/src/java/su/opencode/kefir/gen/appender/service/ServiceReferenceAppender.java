/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.service;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.appender.XmlAppender;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getServiceClassName;
import static su.opencode.kefir.gen.ExtEntityUtils.getServiceReferenceName;
import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Добавляет описание локального сервиса в web.xml.
 */
public class ServiceReferenceAppender extends XmlAppender
{
	public ServiceReferenceAppender(String webXmlPath, Class serviceClass) {
		this.webXmlPath = webXmlPath;
		this.serviceClassName = serviceClass.getName();
		this.serviceReferenceName = serviceClass.getSimpleName();
	}
	public ServiceReferenceAppender(String webXmlPath, String serviceClassName, String serviceReferenceName) {
		this.webXmlPath = webXmlPath;
		this.serviceClassName = serviceClassName;
		this.serviceReferenceName = serviceReferenceName;
	}
	public ServiceReferenceAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		this.webXmlPath = webXmlPath;
		this.serviceClassName = getServiceClassName(extEntity, entityClass);
		this.serviceReferenceName = getServiceReferenceName(extEntity, entityClass);
	}
	protected String getEncoding() {
		return WEB_XML_ENCODING;
	}

	public void appendEjbLocalReference() throws IOException {
		File file = new File(webXmlPath);
		List<String> fileLines = readLinesFromFile(file);
		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}

	private void appendLines(List<String> fileLines) {
		if (serviceIsPresent(fileLines))
		{
			return;
		}
		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(APPEND_EJB_LOCAL_REFERENCE_MARKER))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendReference(appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendReference(List<String> fileLines) {
		appendStartElement(EJB_LOCAL_REF_ELEMENT_NAME, fileLines);
		appendSimpleElement(EJB_LOCAL_REF_NAME_ELEMENT_NAME, getServiceReferenceNameString(), fileLines);
		appendSimpleElement(EJB_REF_TYPE_ELEMENT_NAME, "Session", fileLines);
		appendSimpleElement(LOCAL_HOME_ELEMENT_NAME, "java.lang.Object", fileLines);
		appendSimpleElement(LOCAL_ELEMENT_NAME, serviceClassName, fileLines);
		appendEndElement(EJB_LOCAL_REF_ELEMENT_NAME, fileLines);
	}
	private boolean serviceIsPresent(List<String> fileLines) {
		String startMarker = getStartMarker();
		String endMarker = getEndMarker();
		String strToFind = getEjbRefNameString();

		int startIndex;
		int endIndex;

		int i = 0;
		while (i < fileLines.size())
		{
			if ( fileLines.get(i).contains(startMarker) )
			{
				startIndex = i;
				endIndex = -1;

					// search for </servlet>
				for (int j = i + 1; j < fileLines.size(); j++)
				{
					if ( fileLines.get(j).contains(endMarker) )
					{
						endIndex = j;
						break;
					}
				}

				for (int k = startIndex + 1; k < endIndex; k++)
				{
					if ( fileLines.get(k).contains(strToFind) )
					{
						logger.info( getServiceIsPresentLogMessage() );
						return true;
					}
				}

				if (endIndex != -1)
					i = endIndex;
			}

			i++;
		}

		return false;
	}

	private String getStartMarker() {
		return getStartElement(EJB_LOCAL_REF_ELEMENT_NAME);
	}
	private String getEndMarker() {
		return getEndElement(EJB_LOCAL_REF_ELEMENT_NAME);
	}

	private String getServiceIsPresentLogMessage() {
		return concat(sb, "service \"", getServiceReferenceNameString(), "\" declaration is already present");
	}

	private String getEjbRefNameString() {
		return getSimpleElement(EJB_LOCAL_REF_NAME_ELEMENT_NAME, getServiceReferenceNameString());
	}
	private String getServiceReferenceNameString() {
		return concat(sb, "ejb/", serviceReferenceName);
	}

	protected String webXmlPath;
	protected String serviceClassName;
	protected String serviceReferenceName;

	public static final String WEB_XML_ENCODING = "UTF8";
	public static final String APPEND_EJB_LOCAL_REFERENCE_MARKER = "${APPEND_EJB_LOCAL_REFERENCE}";

	public static final String EJB_LOCAL_REF_ELEMENT_NAME = "ejb-local-ref";
	public static final String EJB_LOCAL_REF_NAME_ELEMENT_NAME = "ejb-ref-name";
	public static final String EJB_REF_TYPE_ELEMENT_NAME = "ejb-ref-type";
	public static final String LOCAL_HOME_ELEMENT_NAME = "local-home";
	public static final String LOCAL_ELEMENT_NAME = "local";
}