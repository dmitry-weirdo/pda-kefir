/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 11:15:38$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.util.SaxWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XmlFileWriter
{
	public XmlFileWriter(String fileName) {
		this.fileName = fileName;
	}
	public XmlFileWriter(String fileName, ProjectConfig config) {
		this.fileName = fileName;
		this.config = config;
	}
	public void writeFile() throws IOException, SAXException {
		OutputStream out = null;

		try
		{
			out = new FileOutputStream(fileName);
			writer = new SaxWriter(out, XML_FILE_ENCODING, omitXmlDeclaration);
			writer.setIndent(true);

			writer.startDocument();
			writeContent();
			writer.endDocument();
		}
		finally
		{
			if (out != null)
				out.close();
		}
	}
	protected void writeContent() throws IOException, SAXException {
		// do nothing, this method should be overridden is subclasses
	}

	public boolean isOmitXmlDeclaration() {
		return omitXmlDeclaration;
	}
	public void setOmitXmlDeclaration(boolean omitXmlDeclaration) {
		this.omitXmlDeclaration = omitXmlDeclaration;
	}

	protected String fileName;
	protected boolean omitXmlDeclaration = false;
	protected ProjectConfig config;
	protected SaxWriter writer;
	protected StringBuffer sb = new StringBuffer();

	public static final String XML_FILE_ENCODING = "UTF-8";

	public static final String XMNLS_PARAMETER_NAME = "xmlns";
	public static final String XMLNS_XSI_PARAMETER_NAME = "xmlns:xsi";
	public static final String XSI_SCHEMA_LOCATION_PARAMETER_NAME = "xsi:schemaLocation";
	public static final String VERSION_PARAMETER_NAME = "version";
}