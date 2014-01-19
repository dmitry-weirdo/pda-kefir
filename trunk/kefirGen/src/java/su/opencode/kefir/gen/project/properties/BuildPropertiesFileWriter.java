/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 17:21:38$
*/
package su.opencode.kefir.gen.project.properties;

import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;

public class BuildPropertiesFileWriter extends PropertiesFileWriter
{
	public BuildPropertiesFileWriter(String fileName, ProjectConfig config) {
		super(fileName, config);
	}

	@Override
	protected void writeProperties() throws IOException {
		writeProperty(DEPLOY_PATH_PROPERTY_NAME, config.deployPath);
		writeProperty(DEPLOY_LIB_PROPERTY_NAME, config.deployLib);
		writeProperty(SRV_JAR_NAME, getSrvJarName(config));
		writeProperty(WEB_WAR_NAME, getWebWarName(config));
		writeProperty(EAR_NAME, getEarName(config));

		writeProperty(KEFIR_SRV_JAR_NAME, KEFIR_SRV_JAR_NAME_VALUE);
		writeProperty(KEFIR_UTIL_JAR_NAME, KEFIR_UTIL_JAR_NAME_VALUE);
	}

	public static final String SRV_JAR_NAME = "srv.jar.name";
	public static final String WEB_WAR_NAME = "web.war.name";
	public static final String EAR_NAME = "ear.name";
	public static final String DEPLOY_PATH_PROPERTY_NAME = "deploy.path";
	public static final String DEPLOY_LIB_PROPERTY_NAME = "deploy.lib";

	public static final String KEFIR_SRV_JAR_NAME = "kefirSrv.jar.name";
	public static final String KEFIR_SRV_JAR_NAME_VALUE = "kefirSrv.jar";

	public static final String KEFIR_UTIL_JAR_NAME = "kefirUtil.jar.name";
	public static final String KEFIR_UTIL_JAR_NAME_VALUE = "kefirUtil.jar";
}