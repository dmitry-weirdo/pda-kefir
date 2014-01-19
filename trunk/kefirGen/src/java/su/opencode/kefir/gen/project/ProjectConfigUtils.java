/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 18:59:26$
*/
package su.opencode.kefir.gen.project;

import org.apache.log4j.Logger;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.CLASS_FILE_EXTENSION;
import static su.opencode.kefir.gen.project.address.AddressClassFileWriter.ADDRESS_CLASS_SIMPLE_NAME;
import static su.opencode.kefir.util.FileUtils.CURRENT_DIR;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.*;

public class ProjectConfigUtils
{
	public static String getProjectFullPath(ProjectConfig config) {
		return concat(config.baseDir, FILE_SEPARATOR, config.projectName);
	}

	public static String getCommonLibPath(ProjectConfig config) {
		return getValue(config.commonLibPath, "lib");
	}
	public static String getCommonLibFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, getCommonLibPath(config));
	}
	public static String getApplicationXmlFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, APPLICATION_XML_FILE_NAME);
	}
	public static String getCommonBuildXmlFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, BUILD_XML_FILE_NAME);
	}

	public static String getBuildPropertiesFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, BUILD_PROPERTIES_FILE_NAME);
	}

	public static String getSrvModuleName(ProjectConfig config) {
		return getValue(config.srvModuleName, concat(config.projectName, "Srv"));
	}
	public static String getSrvModuleFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, getSrvModuleName(config));
	}
	public static String getSrvLibPath(ProjectConfig config) {
		return getValue(config.srvLibPath, "lib");
	}
	public static String getSrvLibFullPath(ProjectConfig config) {
		return concat(getSrvModuleFullPath(config), FILE_SEPARATOR, getSrvLibPath(config));
	}
	public static String getSrvBuildXmlFullPath(ProjectConfig config) {
		return concat(getSrvModuleFullPath(config), FILE_SEPARATOR, BUILD_XML_FILE_NAME);
	}

	public static String getSrvMetaInfFullPath(ProjectConfig config) {
		return concat(getSrvModuleFullPath(config), FILE_SEPARATOR, META_INF_DIR_NAME);
	}
	public static String getSrvMetaInfPath(ProjectConfig config) {
		return concat(getSrvModuleName(config), FILE_SEPARATOR, META_INF_DIR_NAME);
	}
	public static String getEjbJarFullPath(ProjectConfig config) {
		return concat(getSrvMetaInfFullPath(config), FILE_SEPARATOR, EJB_JAR_XML_FILE_NAME);
	}
	public static String getJbossXmlFullPath(ProjectConfig config) {
		return concat(getSrvMetaInfFullPath(config), FILE_SEPARATOR, JBOSS_XML_FILE_NAME);
	}

	public static String getPersistenceXmlFullPath(ProjectConfig config) {
		return concat(getSrvMetaInfFullPath(config), FILE_SEPARATOR, PERSISTENCE_XML_FILE_NAME);
	}
	public static String getPersistenceUnitName(ProjectConfig config) {
		return getValue(config.persistenceUnitName, config.projectName);
	}
	public static String getJtaDataSourceName(ProjectConfig config) {
		return getValue( config.jtaDataSourceName, concat(config.projectName, "DS") );
	}

	public static String getOrmXmlFullPath(ProjectConfig config) {
		return concat(getSrvMetaInfFullPath(config), FILE_SEPARATOR, ORM_XML_FILE_NAME);
	}
	public static String getOrmXmlPath(ProjectConfig config) {
		return concat(getSrvMetaInfPath(config), FILE_SEPARATOR, ORM_XML_FILE_NAME);
	}

	public static String getSrvJavaSrcFullPath(ProjectConfig config) {
		return concat(getSrvModuleFullPath(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getSrvJavaSrcPath(ProjectConfig config) {
		return concat(getSrvModuleName(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getSrvBasePackageFullPath(ProjectConfig config) {
		return concat( getSrvJavaSrcFullPath(config), FILE_SEPARATOR, config.srvBasePackage.replace(PACKAGE_SEPARATOR, FILE_SEPARATOR) );
	}

	public static String getRenderersPackage(ProjectConfig config) {
		return getValue( config.renderersPackage, concat(config.srvBasePackage, PACKAGE_SEPARATOR, RENDERERS_PAGKAGE) );
	}
	public static String getRenderersPackageFullPath(ProjectConfig config) {
		return concat( getSrvJavaSrcFullPath(config), FILE_SEPARATOR, getRenderersPackage(config).replace(PACKAGE_SEPARATOR, FILE_SEPARATOR) );
	}
	public static String getRenderersPackagePath(ProjectConfig config) {
		return concat( getSrvJavaSrcPath(config), FILE_SEPARATOR, getRenderersPackage(config).replace(PACKAGE_SEPARATOR, FILE_SEPARATOR) );
	}
	public static String getRendererPropertiesPath(ProjectConfig config) {
		return concat( getRenderersPackagePath(config), FILE_SEPARATOR, RENDERER_PROPERTIES_FILE_NAME );
	}
	public static String getRendererPropertiesFullPath(ProjectConfig config) {
		return concat( getRenderersPackageFullPath(config), FILE_SEPARATOR, RENDERER_PROPERTIES_FILE_NAME );
	}
	public static String getRenderersClassFullName(ProjectConfig config) {
		return concat( getRenderersPackage(config), PACKAGE_SEPARATOR, RENDERERS_CLASS_NAME );
	}
	public static String getRenderersClassFullPath(ProjectConfig config) {
		return concat( getRenderersPackageFullPath(config), FILE_SEPARATOR, RENDERERS_CLASS_NAME, CLASS_FILE_EXTENSION);
	}
	public static String getRenderersClassPath(ProjectConfig config) {
		return concat( getRenderersPackagePath(config), FILE_SEPARATOR, RENDERERS_CLASS_NAME, CLASS_FILE_EXTENSION);
	}

	public static String getGeneratorPropertiesFullPath(ProjectConfig config) {
		return concat( getSrvBasePackageFullPath(config), FILE_SEPARATOR, GENERATOR_PROPERTIES_FILE_NAME );
	}

	public static String getSrvSqlSrcFullPath(ProjectConfig config) {
		return concat(getSrvModuleFullPath(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, SQL_SRC_DIR_NAME);
	}
	public static String getSrvSqlSrcPath(ProjectConfig config) {
		return concat(getSrvModuleName(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, SQL_SRC_DIR_NAME);
	}
	public static String getCreateTablesSqlFullPath(ProjectConfig config) {
		return concat( getSrvSqlSrcFullPath(config), FILE_SEPARATOR, CREATE_TABLES_SQL_FILE_NAME );
	}
	public static String getCreateTablesSqlPath(ProjectConfig config) {
		return concat( getSrvSqlSrcPath(config), FILE_SEPARATOR, CREATE_TABLES_SQL_FILE_NAME );
	}
	public static String getDropTablesSqlFullPath(ProjectConfig config) {
		return concat( getSrvSqlSrcFullPath(config), FILE_SEPARATOR, DROP_TABLES_SQL_FILE_NAME );
	}
	public static String getDropTablesSqlPath(ProjectConfig config) {
		return concat( getSrvSqlSrcPath(config), FILE_SEPARATOR, DROP_TABLES_SQL_FILE_NAME );
	}

	public static String getWebModuleName(ProjectConfig config) {
		return getValue(config.webModuleName, concat(config.projectName, "Web"));
	}
	public static String getWebModuleFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, getWebModuleName(config));
	}
	public static String getWebLibFullPath(ProjectConfig config) {
		return concat(getWebModuleFullPath(config), FILE_SEPARATOR, getWebLibPath(config));
	}
	public static String getWebLibPath(ProjectConfig config) {
		return getValue(config.webLibPath, "lib");
	}
	public static String getWebBuildXmlFullPath(ProjectConfig config) {
		return concat(getWebModuleFullPath(config), FILE_SEPARATOR, BUILD_XML_FILE_NAME);
	}

	public static String getWebJavaSrcFullPath(ProjectConfig config) {
		return concat(getWebModuleFullPath(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getWebJavaSrcPath(ProjectConfig config) {
		return concat(getWebModuleName(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getWebBasePackageFullPath(ProjectConfig config) {
		return concat( getWebJavaSrcFullPath(config), FILE_SEPARATOR, config.webBasePackage.replace(PACKAGE_SEPARATOR, FILE_SEPARATOR) );
	}

	public static String getWebDirFullPath(ProjectConfig config) {
		return concat(getWebModuleFullPath(config), FILE_SEPARATOR, WEB_DIR_NAME);
	}
	public static String getWebDirPath(ProjectConfig config) {
		return concat(getWebModuleName(config), FILE_SEPARATOR, WEB_DIR_NAME);
	}
	public static String getApplicationJspfFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, APPLICATION_JSPF_FILE_NAME);
	}
	public static String getApplicationJspfPath(ProjectConfig config) {
		return concat(getWebDirPath(config), FILE_SEPARATOR, APPLICATION_JSPF_FILE_NAME);
	}
	public static String getKefirIncludeJspfFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, KEFIR_INCLUDE_JSPF_FILE_NAME);
	}

	public static String getWebInfDirFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, WEB_INF_DIR_NAME);
	}
	public static String getWebInfDirPath(ProjectConfig config) {
		return concat(getWebDirPath(config), FILE_SEPARATOR, WEB_INF_DIR_NAME);
	}
	public static String getWebXmlFullPath(ProjectConfig config) {
		return concat(getWebInfDirFullPath(config), FILE_SEPARATOR, WEB_XML_FILE_NAME);
	}
	public static String getWebXmlPath(ProjectConfig config) {
		return concat(getWebInfDirPath(config), FILE_SEPARATOR, WEB_XML_FILE_NAME);
	}
	public static String getJbossWebXmlFullPath(ProjectConfig config) {
		return concat(getWebInfDirFullPath(config), FILE_SEPARATOR, JBOSS_WEB_XML_FILE_NAME);
	}
	public static String getJbossWebXmlPath(ProjectConfig config) {
		return concat(getWebInfDirPath(config), FILE_SEPARATOR, JBOSS_WEB_XML_FILE_NAME);
	}
	public static String getContextPath(ProjectConfig config) {
		return getValue(config.contextPath, config.projectName);
	}
	public static String getExtContextPath(ProjectConfig config) {
		return getValue(config.extContextPath, DEFAULT_EXT_CONTEXT_PATH);
	}
	public static String getKefirStaticContextPath(ProjectConfig config) {
		return getValue(config.kefirStaticContextPath, DEFAULT_KEFIR_STATIC_CONTEXT_PATH);
	}

	public static String getCssDirFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, CSS_DIR_NAME);
	}
	public static String getMainCssFullPath(ProjectConfig config) {
		return concat(getCssDirFullPath(config), FILE_SEPARATOR, MAIN_CSS_FILE_NAME);
	}
	public static String getMainCssPath(ProjectConfig config) {
		return concat(getCssDirPath(config), FILE_SEPARATOR, MAIN_CSS_FILE_NAME);
	}

	public static String getBaseCssPath(ProjectConfig config) {
		return concat(CURRENT_DIR, HTML_URL_SEPARATOR, CSS_DIR_NAME);
	}
	public static String getCssDirPath(ProjectConfig config) {
		return concat(getWebDirPath(config), FILE_SEPARATOR, CSS_DIR_NAME);
	}
	public static String getMainCssIncludePath(ProjectConfig config) {
		return concat( getBaseCssPath(config), HTML_URL_SEPARATOR, MAIN_CSS_FILE_NAME );
	}

	public static String getJsDirFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, JS_DIR_NAME);
	}
	public static String getJsDirPath(ProjectConfig config) {
		return concat(getWebDirPath(config), FILE_SEPARATOR, JS_DIR_NAME);
	}
	public static String getBaseJsPath(ProjectConfig config) {
		return concat(CURRENT_DIR, HTML_URL_SEPARATOR, JS_DIR_NAME);
	}
	public static String getConstantsJsFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, CONSTANTS_FILE_NAME);
	}
	public static String getConstantsJsPath(ProjectConfig config) {
		return concat(getJsDirPath(config), FILE_SEPARATOR, CONSTANTS_FILE_NAME);
	}
	public static String getConstantsJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, CONSTANTS_FILE_NAME);
	}
	public static String getLocalStoresJsFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, LOCAL_STORES_FILE_NAME);
	}
	public static String getLocalStoresJsPath(ProjectConfig config) {
		return concat(getJsDirPath(config), FILE_SEPARATOR, LOCAL_STORES_FILE_NAME);
	}
	public static String getLocalStoresJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, LOCAL_STORES_FILE_NAME);
	}
	public static String getRenderersJsFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, RENDERERS_FILE_NAME);
	}
	public static String getRenderersJsPath(ProjectConfig config) {
		return concat(getJsDirPath(config), FILE_SEPARATOR, RENDERERS_FILE_NAME);
	}
	public static String getRenderersJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, RENDERERS_FILE_NAME);
	}
	public static String getViewConfigsJsFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, VIEW_CONFIGS_FILE_NAME);
	}
	public static String getViewConfigsJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, VIEW_CONFIGS_FILE_NAME);
	}
	public static String getViewConfigsJsPath(ProjectConfig config) {
		return concat(getJsDirPath(config), FILE_SEPARATOR, VIEW_CONFIGS_FILE_NAME);
	}
	public static String getVtypesJsFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, VTYPES_FILE_NAME);
	}
	public static String getVtypesJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, VTYPES_FILE_NAME);
	}

	// main menu
	public static String getMainMenuDirFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, MAIN_MENU_DIR_NAME);
	}
	public static String getMainMenuDirPath(ProjectConfig config) {
		return concat(getJsDirPath(config), FILE_SEPARATOR, MAIN_MENU_DIR_NAME);
	}
	public static String getMainMenuJsFullPath(ProjectConfig config) {
		return concat( getMainMenuDirFullPath(config), FILE_SEPARATOR, MAIN_MENU_JS_FILE_NAME);
	}
	public static String getMainMenuJsPath(ProjectConfig config) {
		return concat( getMainMenuDirPath(config), FILE_SEPARATOR, MAIN_MENU_JS_FILE_NAME);
	}
	public static String getMainMenuJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, MAIN_MENU_DIR_NAME, HTML_URL_SEPARATOR, MAIN_MENU_JS_FILE_NAME);
	}

	public static int getMainMenuButtonWidth(ProjectConfig config) {
		String strValue = getValue( config.mainMenuButtonWidth, Integer.toString(DEFAULT_MAIN_MENU_BUTTON_WIDTH) );
		return Integer.parseInt(strValue);
	}
	public static String getMainMenuNamespace(ProjectConfig config) {
		return getValue(config.mainMenuNamespace, config.projectName);
	}
	public static String getMainMenuFullNamespace(ProjectConfig config) {
		return getFullName( getMainMenuNamespace(config), MAIN_MENU_CLASS_NAME );
	}
	public static String getMainMenuInitFunctionFullName(ProjectConfig config) {
		return getFullName( getMainMenuFullNamespace(config), MAIN_MENU_INIT_FUNCTION_NAME );
	}
	public static String getMainMenuTitle(ProjectConfig config) {
		return getValue(config.mainMenuTitle, config.htmlTitle);
	}

	public static String getAddressDirFullPath(ProjectConfig config) {
		return concat(getJsDirFullPath(config), FILE_SEPARATOR, ADDRESS_DIR_NAME);
	}
	public static String getAddressJsFullPath(ProjectConfig config) {
		return concat( getAddressDirFullPath(config), FILE_SEPARATOR, ADDRESS_JS_FILE_NAME);
	}
	public static String getAddressJsIncludePath(ProjectConfig config) {
		return concat(getBaseJsPath(config), HTML_URL_SEPARATOR, ADDRESS_DIR_NAME, HTML_URL_SEPARATOR, ADDRESS_JS_FILE_NAME);
	}
	public static String getAddressTemplateJsFullPath(ProjectConfig config) {
		return concat( config.templatePath, FILE_SEPARATOR, ADDRESS_TEMPLATE_JS_FILE_NAME);
	}

	public static String getAddressClassPackage(ProjectConfig config) {
		return getValue(config.addressClassPackage, concat( config.srvBasePackage, PACKAGE_SEPARATOR, decapitalize(ADDRESS_CLASS_SIMPLE_NAME) ));
	}
	public static String getAddressClassFullName(ProjectConfig config) {
		return concat(getAddressClassPackage(config), PACKAGE_SEPARATOR, ADDRESS_CLASS_SIMPLE_NAME);
	}
	public static String getAddressJsNamespace(ProjectConfig config) {
		return getValue(config.addressJsNamespace, concat( getAddressClassPackage(config), PACKAGE_SEPARATOR, decapitalize(ADDRESS_CLASS_SIMPLE_NAME))); // определятеся аналогично ExtEntity.jsNamespace
	}
	public static String getAddressFormJsNamespace(ProjectConfig config) {
		return getValue(config.addressFormJsNamespace, ADDRESS_CLASS_SIMPLE_NAME);
	}
	public static String getAddressFullFormJsNamespace(ProjectConfig config) {
		return concat(getAddressJsNamespace(config), PACKAGE_SEPARATOR, getAddressFormJsNamespace(config));
	}

	public static String getIndexJspFullPath(ProjectConfig config) {
		return concat(getWebDirFullPath(config), FILE_SEPARATOR, INDEX_JSP_FILE_NAME);
	}

	// build jar, war and ear files
	public static String getSrvJarName(ProjectConfig config) {
		return getValue( config.srvJarName, concat(getSrvModuleName(config), JAR_FILE_EXTENSION) );
	}
	public static String getWebWarName(ProjectConfig config) {
		return getValue( config.webWarName, concat(getWebModuleName(config), WAR_FILE_EXTENSION) );
	}
	public static String getEarName(ProjectConfig config) {
		return getValue( config.earName, concat(config.projectName, EAR_FILE_EXTENSION) );
	}
	public static String getEarNameWithNoExtension(ProjectConfig config) {
		String earFullName = getValue(config.earName, concat(config.projectName, EAR_FILE_EXTENSION));
		return earFullName.substring(0, earFullName.length() - EAR_FILE_EXTENSION.length());
	}

	// build selenium files
	public static String getSeleniumSrcPath(ProjectConfig config) {
		return concat(getSeleniumModuleFullPath(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getSeleniumModuleFullPath(ProjectConfig config) {
		return concat(getProjectFullPath(config), FILE_SEPARATOR, getSeleniumModuleName(config));
	}
	public static String getSeleniumModuleName(ProjectConfig config) {
		return getValue(config.seleniumModuleName, concat(config.projectName, "Selenium"));
	}
	public static String getSeleniumLibFullPath(ProjectConfig config) {
		return concat(getSeleniumModuleFullPath(config), FILE_SEPARATOR, getSeleniumLibPath(config));
	}
	public static String getSeleniumLibPath(ProjectConfig config) {
		return getValue(config.seleniumLibPath, "lib");
	}
	public static String getSeleniumJavaSrcFullPath(ProjectConfig config) {
		return concat(getSeleniumModuleFullPath(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getSeleniumJavaSrcPath(ProjectConfig config) {
		return concat(getSeleniumModuleName(config), FILE_SEPARATOR, SRC_DIR_NAME, FILE_SEPARATOR, JAVA_SRC_DIR_NAME);
	}
	public static String getSeleniumJavaTestPath(ProjectConfig config) {
		return concat(getSeleniumModuleName(config), FILE_SEPARATOR, TEST_DIR_NAME);
	}
	public static String getSeleniumJavaTestDataPath(ProjectConfig config) {
		return concat(getSeleniumModuleName(config), FILE_SEPARATOR, TEST_DATA_DIR_NAME);
	}
	public static String getSeleniumJavaTestFullPath(ProjectConfig config) {
		return concat(getSeleniumModuleFullPath(config), FILE_SEPARATOR, TEST_DIR_NAME);
	}
	public static String getSeleniumBuildXmlFullPath(ProjectConfig config) {
		return concat(getSeleniumModuleFullPath(config), FILE_SEPARATOR, BUILD_XML_FILE_NAME);
	}

	private static final Logger logger = Logger.getLogger(ProjectConfigUtils.class);

	public static final String META_INF_DIR_NAME = "META-INF";
	public static final String EJB_JAR_XML_FILE_NAME = "ejb-jar.xml";
	public static final String JBOSS_XML_FILE_NAME = "jboss.xml";
	public static final String PERSISTENCE_XML_FILE_NAME = "persistence.xml";
	public static final String ORM_XML_FILE_NAME = "orm.xml";

	public static final String RENDERER_PROPERTIES_FILE_NAME = "renderer.properties";
	public static final String RENDERERS_CLASS_NAME = "Renderers";

	public static final String GENERATOR_PROPERTIES_FILE_NAME = "generator.properties";
	public static final String GENERATOR_RUNNER_CLASS_NAME = "GeneratorRunner";

	public static final String SRC_DIR_NAME = "src";
	public static final String TEST_DIR_NAME = "test";
	public static final String TEST_DATA_DIR_NAME = "data";
	public static final String JAVA_SRC_DIR_NAME = "java";
	public static final String SQL_SRC_DIR_NAME = "sql";

	public static final String RENDERERS_PAGKAGE = "render";

	public static final String CREATE_TABLES_SQL_FILE_NAME = "createTables.sql";
	public static final String DROP_TABLES_SQL_FILE_NAME = "dropTables.sql";

	public static final String WEB_INF_DIR_NAME = "WEB-INF";
	public static final String WEB_XML_FILE_NAME = "web.xml";
	public static final String JBOSS_WEB_XML_FILE_NAME = "jboss-web.xml";

	public static final String WEB_DIR_NAME = "web";
	public static final String APPLICATION_JSPF_FILE_NAME = "application.jspf";
	public static final String KEFIR_INCLUDE_JSPF_FILE_NAME = "kefirInclude.jspf";
	public static final String INDEX_JSP_FILE_NAME = "index.jsp";

	public static final String DEFAULT_EXT_CONTEXT_PATH = "/ext";
	public static final String DEFAULT_KEFIR_STATIC_CONTEXT_PATH = "/kefirStatic";

	public static final String CSS_DIR_NAME = "css";
	public static final String MAIN_CSS_FILE_NAME = "main.css";

	public static final String JS_DIR_NAME = "js";
	public static final String CONSTANTS_FILE_NAME = "constants.js";
	public static final String LOCAL_STORES_FILE_NAME = "localStores.js";
	public static final String RENDERERS_FILE_NAME = "renderers.js";
	public static final String VIEW_CONFIGS_FILE_NAME = "viewConfigs.js";
	public static final String VTYPES_FILE_NAME = "vtypes.js";

	public static final String MAIN_MENU_DIR_NAME = "menu";
	public static final String MAIN_MENU_JS_FILE_NAME = "mainMenu.js";
	public static final int DEFAULT_MAIN_MENU_BUTTON_WIDTH = 400;
	public static final String MAIN_MENU_CLASS_NAME = "MainMenu";
	public static final String MAIN_MENU_INIT_FUNCTION_NAME = "init";

	public static final String ADDRESS_DIR_NAME = "address";
	public static final String ADDRESS_JS_FILE_NAME = "address.js";
	public static final String ADDRESS_TEMPLATE_JS_FILE_NAME = "addressTemplate.js";

	public static final String APPLICATION_XML_FILE_NAME = "application.xml";
	public static final String BUILD_PROPERTIES_FILE_NAME = "build.properties";
	public static final String BUILD_XML_FILE_NAME = "build.xml";

	public static final String JAR_FILE_EXTENSION = ".jar";
	public static final String WAR_FILE_EXTENSION = ".war";
	public static final String EAR_FILE_EXTENSION = ".ear";
}