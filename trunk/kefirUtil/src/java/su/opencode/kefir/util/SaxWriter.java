/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 24.03.2011 13:09:20$
*/
package su.opencode.kefir.util;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static su.opencode.kefir.util.StringUtils.concat;

public class SaxWriter
{
	public SaxWriter(OutputStream stream, String encoding) {
		handler = getHandler(stream, encoding, false);
	}
	public SaxWriter(OutputStream stream, String encoding, boolean omitXmlEncoding) {
		handler = getHandler(stream, encoding, omitXmlEncoding);
	}
	public SaxWriter(HttpServletResponse response, String encoding) throws IOException {
		response.setContentType("text/xml");
		response.setCharacterEncoding(encoding);
		handler = getHandler(response.getOutputStream(), encoding, false);
	}

	public boolean isIndent() {
		return indent;
	}
	public void setIndent(boolean indent) {
		this.indent = indent;
	}
	public int getDepth() {
		return depth;
	}

	public void clearAttributes() {
		attrs.clear();
	}
	public void addAttribute(String name, String value) {
		attrs.addAttribute("", name, name, "CDATA", value);
	}
	public int getAttributeIndex(String name) {
		return attrs.getIndex(name);
	}
	public void setAttributeValue(int index, String value) {
		attrs.setValue(index, value);
	}
	public void removeAttribute(int index) {
		attrs.removeAttribute(index);
	}

	public void processingInstruction(String target, String data) throws SAXException {
		printIndent(depth);
		handler.processingInstruction(target, data);
	}
	public void startDTD(String name) throws SAXException {
		printIndent(depth);
		handler.startDTD(name, null, null);
	}
	public void endDTD() throws SAXException {
		handler.endDTD();
	}
	public void writeComment(String comment) throws SAXException {
		handler.comment(comment.toCharArray(), 0, comment.length());
	}
	public void writeComment(String comment, boolean addSpaces) throws SAXException {
		if (!addSpaces)
			writeComment(comment);
		else
			writeComment( concat(" ", comment, " ") );
	}
	public void startDocument() throws SAXException {
		handler.startDocument();
	}
	public void endDocument() throws SAXException {
		handler.endDocument();
	}
	public void startElement(String name) throws SAXException {
		attrs.clear();
		printIndent(depth++);
		handler.startElement("", name, name, attrs);
	}
	public void startElement(String name, boolean hasAttributes) throws SAXException {
		if (!hasAttributes)
			attrs.clear();
		printIndent(depth++);
		handler.startElement("", name, name, attrs);
	}
	public void endElement(String name) throws SAXException {
		printIndent(--depth);
		handler.endElement("", name, name);
	}
	public void simpleElement(String name, String data) throws SAXException {
		printIndent(depth);
		attrs.clear();
		handler.startElement("", name, name, attrs);
		handler.characters(data.toCharArray(), 0, data.length());
		handler.endElement("", name, name);
	}
	public void simpleElement(String name, String data, boolean hasAttributes) throws SAXException {
		printIndent(depth);
		if (!hasAttributes)
			attrs.clear();
		handler.startElement("", name, name, attrs);
		handler.characters(data.toCharArray(), 0, data.length());
		handler.endElement("", name, name);
	}
	public void simpleElement(String name, int data) throws SAXException {
		simpleElement(name, Integer.toString(data));
	}
	public void simpleElement(String name, int data, boolean hasAttributes) throws SAXException {
		simpleElement(name, Integer.toString(data), hasAttributes);
	}
	public void simpleElement(String name, double data) throws SAXException {
		simpleElement(name, Double.toString(data));
	}
	public void simpleElement(String name, double data, boolean hasAttributes) throws SAXException {
		simpleElement(name, Double.toString(data), hasAttributes);
	}
	public void simpleElement(String name, Date data) throws SAXException {
		simpleElement(name, dateFormat.format(data));
	}
	public void simpleElement(String name, Date data, boolean hasAttributes) throws SAXException {
		simpleElement(name, dateFormat.format(data), hasAttributes);
	}
	public void emptyElement(String name, boolean hasAttributes) throws SAXException {
		printIndent(depth);
		if (!hasAttributes)
			attrs.clear();
		handler.startElement("", name, name, attrs);
		handler.endElement("", name, name);
	}
	public void characters(String data) throws SAXException {
		printIndent(depth);
		handler.characters(data.toCharArray(), 0, data.length());
	}
	public void writeLn() throws SAXException {
		handler.characters("\n".toCharArray(), 0, 1);
	}
	public void characters(char[] ch, int start, int length) throws SAXException {
		printIndent(depth);
		handler.characters(ch, start, length);
	}

	private void printIndent(int depth) throws SAXException {
		if (indent)
		{
			if (depth < INDENTS.length)
				handler.characters(INDENTS, 0, depth + 1);
			else
			{
				handler.characters(INDENTS, 0, INDENTS.length);
				char[] ch = new char[depth + 1 - INDENTS.length];
				Arrays.fill(ch, '\t');
				handler.characters(ch, 0, ch.length);
			}
		}
	}
	private static TransformerHandler getHandler(OutputStream stream, String encoding, boolean omitXmlDeclaration) {
		TransformerHandler handler;

		try
		{
			handler = ((SAXTransformerFactory) SAXTransformerFactory.newInstance()).newTransformerHandler();

			Transformer serializer = handler.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, encoding);
			serializer.setOutputProperty(OutputKeys.INDENT, "no");
			serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION , omitXmlDeclaration ? "yes" : "no");

			handler.setResult(new StreamResult(stream));
		}
		catch (TransformerConfigurationException e)
		{
			throw new RuntimeException("Error in XML transformer configuration.", e);
		}

		return handler;
	}

	private final TransformerHandler handler;
	private final AttributesImpl attrs = new AttributesImpl();
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	private boolean indent;
	private int depth;

	private static final char[] INDENTS = "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t".toCharArray();
}