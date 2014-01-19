/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.servlet;

import su.opencode.kefir.gen.appender.XmlAppender;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public abstract class ListenerDeclarationAppender extends XmlAppender
{
	public ListenerDeclarationAppender(String webXmlPath) {
		this.webXmlPath = webXmlPath;
	}
	@Override
	protected String getEncoding() {
		return ServletMappingAppender.WEB_XML_ENCODING;
	}

	public void appendListenerDeclaration() throws IOException {
		File file = new File(webXmlPath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) {
		String appendMarker = APPEND_LISTENER_MARKER;

		if ( listenerIsPresent(fileLines) )
		{ // listener declaration is already present
			logger.info( concat(sb, "listener ", getListenerClassName(), " declaration is already present") );
			return;
		}

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendListenerDeclaration(appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}

	private void appendListenerDeclaration(List<String> fileLines) {
 		appendStartElement(LISTENER_ELEMENT_NAME, fileLines);
		appendSimpleElement(LISTENER_CLASS_ELEMENT_NAME, getListenerClassName(), fileLines);
 		appendEndElement(LISTENER_ELEMENT_NAME, fileLines);
	}

	private boolean listenerIsPresent(List<String> fileLines) {
		String listenerDeclarationString = getListenerDeclarationString();

		for (String line : fileLines)
			if (line.contains(listenerDeclarationString))
				return true;

		return false;
	}
	private String getListenerDeclarationString() {
		return getSimpleElement(LISTENER_CLASS_ELEMENT_NAME, getListenerClassName());
	}

	protected abstract String getListenerClassName();

	protected String webXmlPath;

	public static final String LISTENER_ELEMENT_NAME = "listener";
	public static final String LISTENER_CLASS_ELEMENT_NAME = "listener-class";

	public static final String APPEND_LISTENER_MARKER = "${APPEND_LISTENER}";
}