/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 14.05.2012 19:17:19$
*/
package su.opencode.kefir.gen.appender;

import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public abstract class XmlAppender extends Appender
{
	protected String getStartElement(String elementName) {
		return concat(sb, "<", elementName, ">");
	}
	protected String getEndElement(String elementName) {
		return concat(sb, "</", elementName, ">");
	}

	protected String getSimpleElement(String elementName, String value) {
		return concat(sb, getStartElement(elementName), value, getEndElement(elementName));
	}

	protected void appendStartElement(String elementName, List<String> fileLines, String indent) {
		fileLines.add( concat(sb, indent, getStartElement(elementName)) );
	}
	protected void appendStartElement(String elementName, List<String> fileLines) {
		appendStartElement(elementName, fileLines, "\t");
	}

	protected void appendEndElement(String elementName, List<String> fileLines, String indent) {
		fileLines.add( concat(sb, indent, getEndElement(elementName)) );
	}
	protected void appendEndElement(String elementName, List<String> fileLines) {
		appendEndElement(elementName, fileLines, "\t");
	}

	protected void appendSimpleElement(String elementName, String value, List<String> fileLines, String indent) {
		fileLines.add( concat(sb, indent, getSimpleElement(elementName, value)) );
	}
	protected void appendSimpleElement(String elementName, String value, List<String> fileLines) {
		appendSimpleElement(elementName, value, fileLines, "\t\t");
	}
}