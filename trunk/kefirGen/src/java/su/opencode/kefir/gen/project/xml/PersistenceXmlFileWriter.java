/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 11:30:59$
*/
package su.opencode.kefir.gen.project.xml;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.getJtaDataSourceName;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getPersistenceUnitName;
import static su.opencode.kefir.util.StringUtils.concat;

public class PersistenceXmlFileWriter extends XmlFileWriter
{
	public PersistenceXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.addAttribute(XMNLS_PARAMETER_NAME, XMLNS);
		writer.addAttribute(VERSION_PARAMETER_NAME, VERSION);
		writer.startElement(PERSISTENCE_ELEMENT_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("name", getPersistenceUnitName(config));
		writer.startElement(PERSISTENCE_UNIT_ELEMENT_NAME, true);

		writer.simpleElement(JTA_DATA_SOURCE_ELEMENT_NAME, concat(sb, JTA_DATA_SOURCE_VALUE_PREFIX, getJtaDataSourceName(config)));

		writer.startElement(PROPERTIES_ELEMENT_NAME);

		// hibernate.show_sql property
		writer.clearAttributes();
		writer.addAttribute("name", HIBERNATE_SHOW_SQL_PROPERTY_NAME);
		writer.addAttribute("value", HIBERNATE_SHOW_SQL_PROPERTY_VALUE);
		writer.emptyElement(PROPERTY_ELEMENT_NAME, true);

		// hibernate.dialect property
		writer.clearAttributes();
		writer.addAttribute("name", HIBERNATE_DIALECT_PROPERTY_NAME);
		writer.addAttribute("value", HIBERNATE_DIALECT_PROPERTY_VALUE);
		writer.emptyElement(PROPERTY_ELEMENT_NAME, true);

		writer.endElement(PROPERTIES_ELEMENT_NAME);

		writer.endElement(PERSISTENCE_UNIT_ELEMENT_NAME);

		writer.endElement(PERSISTENCE_ELEMENT_NAME);
	}

	public static final String PERSISTENCE_ELEMENT_NAME = "persistence";
	public static final String XMLNS = "http://java.sun.com/xml/ns/persistence";
	public static final String VERSION = "1.0";

	public static final String PERSISTENCE_UNIT_ELEMENT_NAME = "persistence-unit";

	public static final String JTA_DATA_SOURCE_ELEMENT_NAME = "jta-data-source";
	public static final String JTA_DATA_SOURCE_VALUE_PREFIX = "java:/";

	public static final String PROPERTIES_ELEMENT_NAME = "properties";
	public static final String PROPERTY_ELEMENT_NAME = "property";

	public static final String HIBERNATE_SHOW_SQL_PROPERTY_NAME = "hibernate.show_sql";
	public static final String HIBERNATE_SHOW_SQL_PROPERTY_VALUE = "false";

	public static final String HIBERNATE_DIALECT_PROPERTY_NAME = "hibernate.dialect";
	public static final String HIBERNATE_DIALECT_PROPERTY_VALUE = "org.hibernate.dialect.FirebirdDialect";
}