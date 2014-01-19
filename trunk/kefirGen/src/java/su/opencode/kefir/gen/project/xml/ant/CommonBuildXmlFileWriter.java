/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 27.03.2012 13:18:30$
*/
package su.opencode.kefir.gen.project.xml.ant;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter.*;
import static su.opencode.kefir.util.FileUtils.CURRENT_DIR;
import static su.opencode.kefir.util.FileUtils.STRAIGHT_FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

public class CommonBuildXmlFileWriter extends AntFileWriter
{
	public CommonBuildXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.addAttribute("name", config.projectName);
		writer.addAttribute("default", DEPLOY_TARGET_NAME);
		writer.addAttribute("basedir", BASE_DIR_VALUE);

		writer.startElement(PROJECT_ELEMENT_NAME, true);

		writeDescription( getDescription(config.projectName) );
		writer.writeLn();

		writeProperties();
		writer.writeLn();

		writeTargets();

		writer.endElement(PROJECT_ELEMENT_NAME);
	}

	private void writeProperties() throws SAXException {
		writeBuildPropertiesFileProperty(concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR));
		writeLocationProperty(DIST_PROPERTY_NAME, DIST_PROPERTY_VALUE);
		writeLocationProperty(DOC_PROPERTY_NAME, DOC_PROPERTY_VALUE);
		writeLocationProperty( COMMON_LIB_PROPERTY_NAME, concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR, getCommonLibPath(config)) );
		writeLocationProperty( SRV_DIST_PROPERTY_NAME, concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR, getSrvModuleName(config), STRAIGHT_FILE_SEPARATOR, DIST_PROPERTY_VALUE) );
		writeLocationProperty( WEB_DIST_PROPERTY_NAME, concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR, getWebModuleName(config), STRAIGHT_FILE_SEPARATOR, DIST_PROPERTY_VALUE) );
		writeLocationProperty(EAR_PATH_PROPERTY_NAME, concat(sb, getPropertyValue(DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(EAR_NAME)));
	}

	private void writeTargets() throws SAXException {
		writeCleanTarget();
		writer.writeLn();

		writeInitTarget();
		writer.writeLn();

		writeDistTarget();
		writer.writeLn();

		writeDeployTarget();
		// todo: common javadoc target
	}
	private void writeCleanTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", CLEAN_TARGET_NAME);
		writer.addAttribute("description", "clean up");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writeDeleteDir(getPropertyValue(DIST_PROPERTY_NAME));
		writeDeleteDir(getPropertyValue(DOC_PROPERTY_NAME));

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeInitTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", INIT_TARGET_NAME);
		writer.addAttribute("description", "init build dirs");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writeTimestamp();

		writeMakeDir(getPropertyValue(DIST_PROPERTY_NAME));
		writeMakeDir(getPropertyValue(DOC_PROPERTY_NAME));

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeDistTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", DIST_TARGET_NAME);
		writer.addAttribute("depends", concat(sb, CLEAN_TARGET_NAME, ", ", INIT_TARGET_NAME));
		writer.addAttribute("description", "generate the distribution");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		// dist srv module
		writer.clearAttributes();
		writer.addAttribute("target", DIST_TARGET_NAME);
		writer.addAttribute("dir", getSrvModuleName(config));
		writer.emptyElement(ANT_TASK_NAME, true); // <ant target="dist" dir="otherTestProject888Srv"/>

		// dist web module
		writer.clearAttributes();
		writer.addAttribute("target", DIST_TARGET_NAME);
		writer.addAttribute("dir", getWebModuleName(config));
		writer.emptyElement(ANT_TASK_NAME, true); // <ant target="dist" dir="otherTestProject888Web"/>

		writer.writeLn();

		writer.clearAttributes();
		writer.addAttribute("destfile", getPropertyValue(EAR_PATH_PROPERTY_NAME));
		writer.addAttribute("appxml", concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR, APPLICATION_XML_FILE_NAME));
		writer.startElement(EAR_TASK_NAME, true);

		writeFileSet( concat(sb, getPropertyValue(COMMON_LIB_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(KEFIR_UTIL_JAR_NAME)) );
		writeFileSet( concat(sb, getPropertyValue(COMMON_LIB_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(KEFIR_SRV_JAR_NAME)) );
		writeFileSet( concat(sb, getPropertyValue(SRV_DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(SRV_JAR_NAME)) );
		writeFileSet( concat(sb, getPropertyValue(WEB_DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(WEB_WAR_NAME)) );

		writer.endElement(EAR_TASK_NAME);

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeDeployTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", DEPLOY_TARGET_NAME);
		writer.addAttribute("depends", DIST_TARGET_NAME);
		writer.addAttribute("description", concat(sb, "deploy whole ", config.projectName, " application"));
		writer.startElement(TARGET_ELEMENT_NAME, true);

		// deploy application ear
		writer.clearAttributes();
		writer.addAttribute("file", getPropertyValue(EAR_PATH_PROPERTY_NAME));
		writer.addAttribute("todir", getPropertyValue(DEPLOY_PATH_PROPERTY_NAME));
		writer.emptyElement(COPY_TASK_NAME, true);

		writer.endElement(TARGET_ELEMENT_NAME);
	}

	public static final String DIST_PROPERTY_NAME = "_dist";
	public static final String DOC_PROPERTY_NAME = "_doc";

	public static final String SRV_DIST_PROPERTY_NAME = "srv.dist";
	public static final String WEB_DIST_PROPERTY_NAME = "web.dist";
}