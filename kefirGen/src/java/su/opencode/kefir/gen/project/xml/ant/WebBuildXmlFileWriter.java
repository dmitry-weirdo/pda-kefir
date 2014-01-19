/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 27.03.2012 11:18:27$
*/
package su.opencode.kefir.gen.project.xml.ant;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.ProjectConfigUtils;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter.DEPLOY_PATH_PROPERTY_NAME;
import static su.opencode.kefir.gen.project.properties.BuildPropertiesFileWriter.WEB_WAR_NAME;
import static su.opencode.kefir.util.FileUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class WebBuildXmlFileWriter extends AntFileWriter
{
	public WebBuildXmlFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeContent() throws IOException, SAXException {
		writer.addAttribute("name", getWebModuleName(config));
		writer.addAttribute("default", DEPLOY_TARGET_NAME);
		writer.addAttribute("basedir", BASE_DIR_VALUE);

		writer.startElement(PROJECT_ELEMENT_NAME, true);

		writeDescription( getDescription(getWebModuleName(config)) );
		writer.writeLn();

		writeProperties();
		writer.writeLn();

		writeCompileClassPath();
		writer.writeLn();

		writeTargets();

		writer.endElement(PROJECT_ELEMENT_NAME);
	}

	private void writeProperties() throws SAXException {
		writeBuildPropertiesFileProperty(concat(sb, UPPER_DIR, STRAIGHT_FILE_SEPARATOR));
		writeLocationProperty(SRC_PROPERTY_NAME, concat(sb, ProjectConfigUtils.SRC_DIR_NAME, STRAIGHT_FILE_SEPARATOR, ProjectConfigUtils.JAVA_SRC_DIR_NAME) );
		writeLocationProperty(BUILD_PROPERTY_NAME, BUILD_PROPERTY_VALUE);
		writeLocationProperty(DIST_PROPERTY_NAME, DIST_PROPERTY_VALUE);
		writeLocationProperty(SRV_DIST_PROPERTY_NAME, concat(sb, UPPER_DIR, STRAIGHT_FILE_SEPARATOR, getSrvModuleName(config), STRAIGHT_FILE_SEPARATOR, DIST_PROPERTY_VALUE));
		writeLocationProperty(DOC_PROPERTY_NAME, DOC_PROPERTY_VALUE);
		writeLocationProperty( COMMON_LIB_PROPERTY_NAME, concat(sb, UPPER_DIR, STRAIGHT_FILE_SEPARATOR, getCommonLibPath(config)) );
		writeLocationProperty( LIB_PROPERTY_NAME, getWebLibPath(config));
	}

	private void writeCompileClassPath() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("id", COMPILE_CLASSPATH_ID);
		writer.startElement(PATH_ELEMENT_NAME, true);

		writeFileSet( concat(sb, getPropertyValue(COMMON_LIB_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, "*.jar") ); // <fileset file="${commonLib}/*.jar"/>
		writeFileSet( concat(sb, getPropertyValue(LIB_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, "*.jar") ); // <fileset file="${lib}/*.jar"/>
		writeFileSet( concat(sb, getPropertyValue(SRV_DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, "*.jar") ); // <fileset file="${srvDist}/*.jar"/>

		writer.endElement(PATH_ELEMENT_NAME);
	}

	private void writeTargets() throws SAXException {
		writeCleanTarget();
		writer.writeLn();

		writeInitTarget();
		writer.writeLn();

		writeCompileTarget();
		writer.writeLn();

		writeDistTarget();
		writer.writeLn();

		writeDeployTarget();
		writer.writeLn();

		writeJavadocTarget();
	}
	private void writeCleanTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", CLEAN_TARGET_NAME);
		writer.addAttribute("description", "clean up");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writeDeleteDir(getPropertyValue(BUILD_PROPERTY_NAME));
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

		writeMakeDir(getPropertyValue(BUILD_PROPERTY_NAME));
		writeMakeDir(getPropertyValue(DIST_PROPERTY_NAME));
		writeMakeDir(getPropertyValue(DOC_PROPERTY_NAME));

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeCompileTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", COMPILE_TARGET_NAME);
		writer.addAttribute("depends", concat(sb, CLEAN_TARGET_NAME, ", ", INIT_TARGET_NAME));
		writer.addAttribute("description", "compile the source");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("srcdir", getPropertyValue(SRC_PROPERTY_NAME));
		writer.addAttribute("destdir", getPropertyValue(BUILD_PROPERTY_NAME));
		writer.addAttribute("debug", JAVAC_DEBUG_PARAM_VALUE);
		writer.startElement(JAVAC_TASK_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("refid", COMPILE_CLASSPATH_ID);
		writer.emptyElement(CLASSPATH_ELEMENT_NAME, true);

		writer.endElement(JAVAC_TASK_NAME);

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeDistTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", DIST_TARGET_NAME);
		writer.addAttribute("depends", COMPILE_TARGET_NAME);
		writer.addAttribute("description", "generate the distribution");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("warfile", concat(sb, getPropertyValue(DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(WEB_WAR_NAME))); // warfile="${dist}/${web.war.name}"
		writer.addAttribute("basedir", WEB_DIR_NAME); // basedir="${build}"
		writer.addAttribute("webxml", concat(sb, CURRENT_DIR, STRAIGHT_FILE_SEPARATOR, WEB_DIR_NAME, STRAIGHT_FILE_SEPARATOR, STRAIGHT_FILE_SEPARATOR, WEB_INF_DIR_NAME, STRAIGHT_FILE_SEPARATOR, WEB_XML_FILE_NAME)); // basedir="${build}"
		writer.startElement(WAR_TASK_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("dir", getPropertyValue(BUILD_PROPERTY_NAME));
		writer.emptyElement(CLASSES_ELEMENT_NAME, true);

		writer.endElement(WAR_TASK_NAME);

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeDeployTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", DEPLOY_TARGET_NAME);
		writer.addAttribute("depends", DIST_TARGET_NAME);
		writer.addAttribute("description", "deploy on server");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("file", concat(sb, getPropertyValue(DIST_PROPERTY_NAME), STRAIGHT_FILE_SEPARATOR, getPropertyValue(WEB_WAR_NAME)));
		writer.addAttribute("todir", getPropertyValue(DEPLOY_PATH_PROPERTY_NAME));
		writer.emptyElement(COPY_TASK_NAME, true); // <copy file="${dist}/${srv.jar.name}" todir="${deploy.path}"/>

		writer.endElement(TARGET_ELEMENT_NAME);
	}
	private void writeJavadocTarget() throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", JAVADOC_TARGET_NAME);
		writer.addAttribute("depends", COMPILE_TARGET_NAME);
		writer.addAttribute("description", "generate javadoc");
		writer.startElement(TARGET_ELEMENT_NAME, true);

		writer.clearAttributes();
		writer.addAttribute("sourcepath", getPropertyValue(SRC_PROPERTY_NAME));
		writer.addAttribute("destdir", getPropertyValue(DOC_PROPERTY_NAME));
		writer.addAttribute("classpath", getPropertyValue(BUILD_PROPERTY_NAME));
		writer.addAttribute("classpathref", COMPILE_CLASSPATH_ID);
		writer.addAttribute("package", config.webBasePackage);
		writer.addAttribute("access", JAVADOC_ACCESS_PARAM_VALUE);
		writer.emptyElement(JAVADOC_TASK_NAME, true);

		writer.endElement(TARGET_ELEMENT_NAME);
	}
}