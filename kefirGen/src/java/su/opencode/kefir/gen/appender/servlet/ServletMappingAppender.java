/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.servlet;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.appender.XmlAppender;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Добавляет описание и маппинг сервлета в указанный файл web.xml в кодировке UTF-8.
 * в места, помеченные специальными маркерами.
 */
public abstract class ServletMappingAppender extends XmlAppender
{
	public ServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		this.webXmlPath = webXmlPath;
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}
	protected String getEncoding() {
		return WEB_XML_ENCODING;
	}

	public void appendServletMapping() throws IOException {
		File file = new File(webXmlPath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines, AppendMode.declaration);
		appendLines(fileLines, AppendMode.mapping);
		writeLinesToFile(file, fileLines);
	}

	private void appendLines(List<String> fileLines, AppendMode mode) {
		String appendMarker = getAppendMarker(mode);

		if ( servletIsPresent(fileLines, mode) )
		{ // servlet mapping or declaration is already present
			return;
		}

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendServletMapping(mode, appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private boolean servletIsPresent(List<String> fileLines, AppendMode mode) {
		String startMarker = getStartMarker(mode);
		String endMarker = getEndMarker(mode);
		String strToFind = getServletNameString();

		int startIndex = -1;
		int endIndex = -1;

		int i = 0;
		while (i < fileLines.size())
		{ // search for <servlet>
			if ( fileLines.get(i).contains(startMarker) )
			{ // <servlet> found -> find next </servlet>
				startIndex = i;
				endIndex = -1;


				for (int j = i + 1; j < fileLines.size(); j++)
				{ // search for </servlet>
					if ( fileLines.get(j).contains(endMarker) )
					{
						endIndex = j;
						break;
					}
				}

				for (int k = startIndex + 1; k < endIndex; k++)
				{ // search for <servlet-name> element inside <servlet> and next </servlet>
					if ( fileLines.get(k).contains(strToFind) )
					{
						logger.info( getServletIsPresentLogMessage(mode) );
						return true;
					}
				}

				if (endIndex != -1)
					i = endIndex;
			}

			i++;
		}

		return false; // <servlet-name> not found -> it must be appended
	}
	private String getStartMarker(AppendMode mode) {
		switch (mode)
		{
			case declaration: return getStartElement(SERVLET_ELEMENT_NAME);
			case mapping: return getStartElement(SERVLET_MAPPING_ELEMENT_NAME);

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}
	private String getEndMarker(AppendMode mode) {
		switch (mode)
		{
			case declaration: return getEndElement(SERVLET_ELEMENT_NAME);
			case mapping: return getEndElement(SERVLET_MAPPING_ELEMENT_NAME);

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}
	private String getServletIsPresentLogMessage(AppendMode mode) {
		switch (mode)
		{
			case declaration: return concat(sb, "servlet \"", getServletName(), "\" declaration is already present");
			case mapping: return concat(sb, "servlet \"", getServletName(), "\" mapping is already present");

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}

	private String getAppendMarker(AppendMode mode) {
		switch (mode)
		{
			case declaration: return APPEND_DECLARATION_MARKER;
			case mapping: return APPEND_MAPPING_MARKER;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}
	private void appendServletMapping(AppendMode mode, List<String> fileLines) {
		switch (mode)
		{
			case declaration:
				appendStartElement(SERVLET_ELEMENT_NAME, fileLines);
				appendSimpleElement(SERVLET_NAME_ELEMENT_NAME, getServletName(), fileLines);
				appendSimpleElement(SERVLET_CLASS_ELEMENT_NAME, getServletClassFullName(), fileLines);
				appendEndElement(SERVLET_ELEMENT_NAME, fileLines);
				return;

			case mapping:
				appendStartElement(SERVLET_MAPPING_ELEMENT_NAME, fileLines);
				appendSimpleElement(SERVLET_NAME_ELEMENT_NAME, getServletName(), fileLines);
				appendSimpleElement(URL_PATTERN_ELEMENT_NAME, getServletUrl(), fileLines);
				appendEndElement(SERVLET_MAPPING_ELEMENT_NAME, fileLines);
				return;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode:", mode) );
		}
	}
	private String getServletNameString() {
		return getSimpleElement(SERVLET_NAME_ELEMENT_NAME, getServletName());
	}

	protected abstract String getServletClassFullName();
	protected abstract String getServletName();
	protected abstract String getServletUrl();

	protected String webXmlPath;
	protected ExtEntity extEntity;
	protected Class entityClass;

	public static final String WEB_XML_ENCODING = "UTF8";
	public static final String APPEND_DECLARATION_MARKER = "${APPEND_DECLARATION}";
	public static final String APPEND_MAPPING_MARKER = "${APPEND_MAPPING}";

	public static final String SERVLET_ELEMENT_NAME = "servlet";
	public static final String SERVLET_MAPPING_ELEMENT_NAME = "servlet-mapping";
	public static final String SERVLET_NAME_ELEMENT_NAME = "servlet-name";
	public static final String SERVLET_CLASS_ELEMENT_NAME = "servlet-class";
	public static final String URL_PATTERN_ELEMENT_NAME = "url-pattern";

	public enum AppendMode { declaration, mapping }
}