/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 13:03:08$
*/
package su.opencode.kefir.gen.project.properties;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

public class GeneratorPropertiesFileWriter extends PropertiesFileWriter
{
	public GeneratorPropertiesFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeProperties() throws IOException {
		writeProperty("srvSrcDir", concat(sb, getSrvJavaSrcPath(config), FILE_SEPARATOR));
		writeProperty("ormXmlPath", getOrmXmlPath(config));
		writeProperty("createTablesSqlPath", getCreateTablesSqlPath(config));
		writeProperty("dropTablesSqlPath", getDropTablesSqlPath(config));
		writeProperty("webSrcDir", concat(sb, getWebJavaSrcPath(config), FILE_SEPARATOR));
		writeProperty("webXmlPath", getWebXmlPath(config));
		writeProperty("jbossWebXmlPath", getJbossWebXmlPath(config));
		writeProperty("jsDir", concat(sb, getJsDirPath(config), FILE_SEPARATOR));
		writeProperty("jsIncludeFilePath", getApplicationJspfPath(config));
		writeProperty("baseJsPath", getBaseJsPath(config));
		writeProperty("mainMenuJsFilePath", getMainMenuJsPath(config));
		writeProperty("viewConfigsJsPath", getViewConfigsJsPath(config));
		writeProperty("mainCssPath", getMainCssPath(config));
		out.writeLn();
		writeComment("properties for enum js generation");
		writeProperty("constantsJsFilePath", getConstantsJsPath(config));
		writeProperty("localStoresJsFilePath", getLocalStoresJsPath(config));
		writeProperty("renderersJsFilePath", getRenderersJsPath(config));
		writeProperty("rendererPropertiesFilePath", getRendererPropertiesPath(config));
		out.writeLn();
		writeProperty("renderersClass", getRenderersClassFullName(config));
		writeProperty("renderersClassFilePath", getRenderersClassPath(config));
		out.writeLn();
		writeProperty("addressClass", getAddressClassFullName(config));
		writeComment("entityClass=");
		writeComment("serviceClassName=");
		writeComment("serviceBeanClassName=");
		writeComment("enumClass=");
		out.writeLn();
		writeProperty("seleniumSrcDir", concat(sb, getSeleniumJavaSrcPath(config), FILE_SEPARATOR));
		writeProperty("seleniumTestDir", concat(sb, getSeleniumJavaTestPath(config), FILE_SEPARATOR));
		writeProperty("seleniumTestDataDir", concat(sb, getSeleniumJavaTestDataPath(config), FILE_SEPARATOR));
	}
}