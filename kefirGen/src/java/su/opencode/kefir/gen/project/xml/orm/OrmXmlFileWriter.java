/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 12:05:14$
*/
package su.opencode.kefir.gen.project.xml.orm;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.xml.XmlFileWriter;

import java.io.IOException;

import static su.opencode.kefir.gen.project.xml.orm.EntityMappingAppender.APPEND_ENTITY_MAPPING_MARKER;
import static su.opencode.kefir.util.StringUtils.concat;

public class OrmXmlFileWriter extends XmlFileWriter
{
	public OrmXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}
	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.addAttribute(XMNLS_PARAMETER_NAME, XMLNS);
		writer.addAttribute(VERSION_PARAMETER_NAME, VERSION);
		writer.startElement(ENTITY_MAPPINGS_ELEMENT_NAME, true);

		writer.simpleElement(DESCRIPTION_ELEMENT_NAME, getDescription());
		writer.simpleElement(ACCESS_ELEMENT_NAME, ACCESS_ELEMENT_VALUE);

		// append entity mapping marker
		writer.characters("\n\t");
		writer.writeComment(APPEND_ENTITY_MAPPING_MARKER, true);

		writer.endElement(ENTITY_MAPPINGS_ELEMENT_NAME);
	}
	private String getDescription() {
		return concat(sb, "Definition of ", config.projectName, " orm mappings");
	}

	public static final String ENTITY_MAPPINGS_ELEMENT_NAME = "entity-mappings";
	public static final String XMLNS = "http://java.sun.com/xml/ns/persistence/orm";
	public static final String VERSION = "1.0";

	public static final String DESCRIPTION_ELEMENT_NAME = "description";
	public static final String ACCESS_ELEMENT_NAME = "access";
	public static final String ACCESS_ELEMENT_VALUE = "PROPERTY";
}