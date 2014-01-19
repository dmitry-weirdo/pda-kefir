/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 16:43:23$
*/
package su.opencode.kefir.gen.project.xml.ant;

import org.xml.sax.SAXException;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.gen.project.xml.XmlFileWriter;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.BUILD_PROPERTIES_FILE_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class AntFileWriter extends XmlFileWriter
{
	public AntFileWriter(String fileName) {
		super(fileName);
	}
	public AntFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	protected void writeDescription(String description) throws SAXException {
		writer.simpleElement(DESCRIPTION_ELEMENT_NAME, description);
	}
	protected String getDescription(String moduleName) {
		return concat(sb, moduleName, " build file");
	}

	protected void writeProperty() throws SAXException {
		writer.emptyElement(PROPERTY_ELEMENT_NAME, true);
	}
	protected void writeProperty(String name, String value) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", name);
		writer.addAttribute("value", value);
		writeProperty();
	}
	protected void writePropertyFile(String fileName) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("file", fileName);
		writeProperty();
	}
	protected void writeBuildPropertiesFileProperty(String relativePath) throws SAXException {
		writePropertyFile(concat(sb, relativePath, BUILD_PROPERTIES_FILE_NAME));
	}

	protected void writeLocationProperty(String name, String location) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("name", name);
		writer.addAttribute("location", location);
		writeProperty();
	}

	protected String getPropertyValue(String name) {
		return concat(sb, "${", name, "}");
	}

	protected void writeFileSet(String file) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("file", file);
		writer.emptyElement(FILESET_ELEMENT_NAME, true);
	}

	protected void writeTimestamp() throws SAXException {
		writer.emptyElement(TIMESTAMP_TASK_NAME, false);
	}

	protected void writeDeleteDir(String dir) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("dir", dir);
		writer.emptyElement(DELETE_TASK_NAME, true);
	}
	protected void writeMakeDir(String dir) throws SAXException {
		writer.clearAttributes();
		writer.addAttribute("dir", dir);
		writer.emptyElement(MAKE_DIR_TASK_NAME, true);
	}

	public static final String PROJECT_ELEMENT_NAME = "project";
	public static final String DESCRIPTION_ELEMENT_NAME = "description";

	public static final String PROPERTY_ELEMENT_NAME = "property";
	public static final String SRC_PROPERTY_NAME = "src";
	public static final String SRC_TEST_PROPERTY_NAME = "test-src";
	public static final String RENDERER_PROPERTIES_PATH_PROPERTY_NAME = "renderPropertiesPath";
	public static final String BUILD_PROPERTY_NAME = "build";
	public static final String BUILD_PROPERTY_VALUE = "build";

	public static final String DIST_PROPERTY_NAME = "dist";
	public static final String DIST_PROPERTY_VALUE = "dist";

	public static final String SRV_DIST_PROPERTY_NAME = "srvDist";

	public static final String DOC_PROPERTY_NAME = "doc";
	public static final String DOC_PROPERTY_VALUE = "doc";

	public static final String COMMON_LIB_PROPERTY_NAME = "commonLib";
	public static final String LIB_PROPERTY_NAME = "lib";
	public static final String COMMON_DIST_PROPERTY_NAME = "commonDist";

	public static final String PATH_ELEMENT_NAME = "path";
	public static final String COMPILE_CLASSPATH_ID = "compile.classpath";


	public static final String FILESET_ELEMENT_NAME = "fileset";

	public static final String TARGET_ELEMENT_NAME = "target";

	public static final String CLEAN_TARGET_NAME = "clean";
	public static final String DELETE_TASK_NAME = "delete";

	public static final String INIT_TARGET_NAME = "init";
	public static final String TIMESTAMP_TASK_NAME = "tstamp";
	public static final String MAKE_DIR_TASK_NAME = "mkdir";

	public static final String COMPILE_TARGET_NAME = "compile";
	public static final String JAVAC_TASK_NAME = "javac";
	public static final String JAVAC_DEBUG_PARAM_VALUE = "true";
	public static final String CLASSPATH_ELEMENT_NAME = "classpath";

	public static final String DIST_TARGET_NAME = "dist";
	public static final String JAR_TASK_NAME = "jar";
	public static final String META_INF_ELEMENT_NAME = "metainf";

	public static final String WAR_TASK_NAME = "war";
	public static final String CLASSES_ELEMENT_NAME = "classes";

	public static final String DEPLOY_TARGET_NAME = "deploy";
	public static final String COPY_TASK_NAME = "copy";

	public static final String EAR_PATH_PROPERTY_NAME = "ear.path";
	public static final String EAR_TASK_NAME = "ear";

	public static final String TOUCH_TASK_NAME = "touch";
	public static final String ANT_TASK_NAME = "ant";

	public static final String JAVADOC_TARGET_NAME = "javadoc";
	public static final String JAVADOC_TASK_NAME = "javadoc";
	public static final String JAVADOC_ACCESS_PARAM_VALUE = "private";

	public static final String BASE_DIR_VALUE = ".";
}