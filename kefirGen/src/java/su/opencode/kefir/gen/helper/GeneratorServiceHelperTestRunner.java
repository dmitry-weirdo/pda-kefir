/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.helper;

import static su.opencode.kefir.util.StringUtils.concat;

import java.io.IOException;

public class GeneratorServiceHelperTestRunner
{
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		GeneratorServiceHelper helper = new GeneratorServiceHelperImpl();

		Class<?> entityClass = Class.forName(ENTITY_CLASS_NAME);

		HelperConfig config = new HelperConfig();
		config.entityClass = entityClass;
		config.serviceClassName = SERVICE_CLASS_NAME;
		config.srvSrcDir = SRV_SRC_DIR;
		config.ormXmlPath = ORM_XML_PATH;
		config.createTablesSqlPath = CREATE_TABLES_SQL_PATH;
		config.dropTablesSqlPath = DROP_TABLES_SQL_PATH;
		config.webSrcDir = WEB_SRC_DIR;
		config.webXmlPath = WEB_XML_PATH;
		config.jbossWebXmlPath = JBOSS_WEB_XML_PATH;
		config.jsDir = JS_DIR;
		config.jsIncludeFilePath = JS_INCLUDE_FILE_PATH;
		config.baseJsPath = BASE_JS_PATH;

		helper.generateLocalInterface(config);
		helper.generateEntityMapping(config);
		helper.generateEntityList(config);
		helper.generateEntityCrud(config);

		config.enumClass = Class.forName(ENUM_CLASS_NAME);
		config.constantsJsFilePath = CONSTANTS_JS_PATH;
		config.localStoresJsFilePath = LOCAL_STORES_JS_PATH;
		config.renderersJsFilePath = RENDERERS_JS_PATH;
		config.renderersClass = Class.forName(RENDERERS_CLASS_NAME);
		helper.generateEnumJs(config);
	}

	public static final String BASE_DIR = "D:\\java\\minstroy\\extgen\\testOutput\\_helper\\";

	public static final String SRV_SRC_DIR = concat(BASE_DIR, "srv");
	public static final String ORM_XML_PATH = concat(BASE_DIR, "\\orm.xml");
	public static final String CREATE_TABLES_SQL_PATH = concat(BASE_DIR, "\\createTables.sql");
	public static final String DROP_TABLES_SQL_PATH = concat(BASE_DIR, "\\dropTables.sql");
	public static final String WEB_SRC_DIR = concat(BASE_DIR, "web");
	public static final String JS_DIR = concat(BASE_DIR, "js");
	public static final String JS_INCLUDE_FILE_PATH = concat(BASE_DIR, "application.jspf");
	public static final String BASE_JS_PATH = "./js";

	public static final String WEB_XML_PATH = concat(BASE_DIR, "web.xml");
	public static final String JBOSS_WEB_XML_PATH = concat(BASE_DIR, "jboss-web.xml");

	// for enum
	public static final String CONSTANTS_JS_PATH = concat(JS_DIR, "\\constants.js");
	public static final String LOCAL_STORES_JS_PATH = concat(JS_DIR, "\\localStores.js");
	public static final String RENDERERS_JS_PATH = concat(JS_DIR, "\\renderers.js");
	public static final String RENDERERS_CLASS_NAME = concat("su.opencode.kefir.gen.test.render.Renderers");

	public static final String SERVICE_CLASS_NAME = "su.opencode.kefir.gen.test.TestService";
	public static final String ENTITY_CLASS_NAME = "su.opencode.kefir.gen.test.TestEntity";
	public static final String ENUM_CLASS_NAME = "su.opencode.kefir.gen.test.TestEnum";
}