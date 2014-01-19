/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.test;

import org.apache.log4j.BasicConfigurator;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.GeneratorService;
import su.opencode.kefir.gen.GeneratorServiceImpl;
import su.opencode.kefir.gen.appender.css.ViewConfigStylesAppender;
import su.opencode.kefir.gen.appender.js.ViewConfigAppender;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.getExtEntityAnnotation;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getViewConfigAnnotation;
import static su.opencode.kefir.util.StringUtils.concat;

public class GeneratorServiceTestRunner
{
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		BasicConfigurator.configure();

		GeneratorService service = new GeneratorServiceImpl();

		Class<?> entityClass = Class.forName(ENTITY_CLASS_NAME);
		ExtEntity extEntity = getExtEntityAnnotation(entityClass);
		Class renderersClass = Class.forName(ENUM_CLASS_NAME);
		if (extEntity == null)
			throw new IllegalArgumentException( concat("ExtEntity annotation not present for class ", ENTITY_CLASS_NAME) );

		// vo method
		service.generateVO(OUTPUT_DIR, renderersClass, extEntity, entityClass);

//
//		// local service

//		// local service impl
//		String packageName = getPackageName(SERVICE_BEAN_CLASS_NAME);
//		String serviceBeanClassSimpleName = getSimpleName(SERVICE_BEAN_CLASS_NAME);
//		service.generateLocalServiceBean(OUTPUT_DIR, packageName, serviceBeanClassSimpleName, SERVICE_CLASS_NAME);

//		service.appendLocalServiceReference(WEB_XML_PATH, extEntity, entityClass);
//		service.appendLocalServiceBeanReference(JBOSS_WEB_XML_PATH, extEntity, entityClass);

		// orm mapping
//		service.appendOrmMapping(ORM_XML_PATH, extEntity, entityClass);
//		service.appendCreateTable(CREATE_TABLES_SQL_PATH, extEntity, entityClass);
//		service.appendDropTable(DROP_TABLES_SQL_PATH, extEntity, entityClass);

//
//		// list methods of service
//		service.generateEntityQueryBuilder(OUTPUT_DIR, extEntity, entityClass);
//		service.generateEntityFilterConfig(OUTPUT_DIR, extEntity, entityClass);
//		service.generateServiceListMethods(OUTPUT_DIR, extEntity, entityClass);
//		service.generateServiceBeanListMethods(OUTPUT_DIR, extEntity, entityClass);
//
//		// list servlet
//		service.generateListServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateListServletMapping(WEB_XML_PATH, extEntity, entityClass);

		// list to excel servlet
//		service.generateListToExcelServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateListToExcelServletMapping(WEB_XML_PATH, extEntity, entityClass);

//
////		// js generation
//		service.generateListScript(JS_DIR, JS_INCLUDE_FILE_PATH, BASE_JS_PATH, extEntity, entityClass);
//		service.generateChooseScript(JS_DIR, JS_INCLUDE_FILE_PATH, BASE_JS_PATH, extEntity, entityClass);
//
//
//		// get method of service
		service.generateServiceGetMethod(OUTPUT_DIR, extEntity, entityClass);
		service.generateServiceBeanGetMethod(OUTPUT_DIR, extEntity, entityClass);
//
//		// get servlet
//		service.generateGetServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateGetServletMapping(WEB_XML_PATH, extEntity, entityClass);
//
//
////		// create method of service
//		service.generateServiceCreateMethod(OUTPUT_DIR, extEntity, entityClass);
//		service.generateServiceBeanCreateMethod(OUTPUT_DIR, extEntity, entityClass);
//
//		// create servlet
//		service.generateCreateServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateCreateServletMapping(WEB_XML_PATH, extEntity, entityClass);
//
//
//		// update method of service
//		service.generateServiceUpdateMethod(OUTPUT_DIR, extEntity, entityClass);
//		service.generateServiceBeanUpdateMethod(OUTPUT_DIR, extEntity, entityClass);
//
//		// update servlet
//		service.generateUpdateServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateUpdateServletMapping(WEB_XML_PATH, extEntity, entityClass);
//
//

//		// delete method of service
//		service.generateServiceDeleteMethod(OUTPUT_DIR, extEntity, entityClass);
//		service.generateServiceBeanDeleteMethod(OUTPUT_DIR, extEntity, entityClass);

//
//		// delete servlet
//		service.generateDeleteServlet(OUTPUT_DIR, extEntity, entityClass);
//		service.generateDeleteServletMapping(WEB_XML_PATH, extEntity, entityClass);


//		// js CRUD form
//		service.generateFormScript(JS_DIR, JS_INCLUDE_FILE_PATH, BASE_JS_PATH, extEntity, entityClass);

//		new FormLayoutJsFileWriter(OUTPUT_DIR, extEntity, entityClass).createFile();
		new ViewConfigAppender("D:\\java\\kefir\\kefirGen\\src\\java\\su\\opencode\\kefir\\gen\\appender\\js\\viewConfigs.js",
			getViewConfigAnnotation(entityClass), entityClass).appendViewConfig();
		new ViewConfigStylesAppender("D:\\java\\kefir\\kefirSampleWeb\\web\\css\\main.css",
			getViewConfigAnnotation(entityClass), entityClass).appendStyles();
		// selenium
//		service.generateFormPage(OUTPUT_DIR, extEntity, entityClass);
//		service.generateListPage(OUTPUT_DIR, extEntity, entityClass);
//		service.generateChooseListPage(OUTPUT_DIR, extEntity, entityClass);
//		service.generateDataProvider(OUTPUT_DIR, entityClass);
//		service.generateTest(OUTPUT_DIR, entityClass);

//		// enum
//		Class<?> enumClass = Class.forName(ENUM_CLASS_NAME);
//		EnumField enumField = getEnumFieldAnnotation(enumClass);
//		if (enumField == null)
//			throw new IllegalArgumentException( concat("EnumField annotation not present for class ", ENUM_CLASS_NAME) );
//
//		service.generateEnumHash(CONSTANTS_JS_PATH, enumField, enumClass);
//		service.generateEnumStore(LOCAL_STORES_JS_PATH, enumField, enumClass);
//		service.generateEnumRenderer(RENDERERS_JS_PATH, enumField, enumClass);
//		service.generateEnumCellRenderer(OUTPUT_DIR, enumField, enumClass);


//		// generate empty servlet class
//		service.generateEmptyServlet(OUTPUT_DIR, "su.opencode.minstroy.leasing", "ParcelsEmptyServlet");

//		// generate local service
//		service.generateLocalService(SRV_SRC_DIR, "su.opencode.minstroy.ejb.leasing", "LeasingService");

//		// generate local service implementation
//		service.generateLocalServiceBean(SRV_SRC_DIR, "su.opencode.minstroy.ejb.leasing", "LeasingServiceBean", "su.opencode.minstroy.ejb.leasing.LeasingService");

//		// append local service reference to web.xml
//		service.appendLocalServiceReference(WEB_XML_PATH, "su.opencode.minstroy.ejb.leasing.LeasingService", "LeasingService666");

//		// append local service reference to jboss-web.xml
//		service.appendLocalServiceBeanReference(JBOSS_WEB_XML_PATH, "LeasingService777", "LeasingServiceBean888");
	}

//	public static final String BASE_DIR = "D:\\_gen\\";
//	public static final String BASE_DIR = "D:\\java\\kefir\\kefirGen\\build";
	public static final String BASE_DIR = "D:\\java\\kefir\\kefirGen\\src\\java";

	public static final String OUTPUT_DIR = BASE_DIR;
	public static final String JS_DIR = concat(BASE_DIR, "js");
	public static final String JS_INCLUDE_FILE_PATH = concat(BASE_DIR, "application.jspf");
	public static final String BASE_JS_PATH = "./js";
	public static final String WEB_XML_PATH = concat(BASE_DIR, "web.xml");
	public static final String JBOSS_WEB_XML_PATH = concat(BASE_DIR, "jboss-web.xml");

	public static final String ORM_XML_PATH = concat(BASE_DIR, "orm.xml");
	public static final String CREATE_TABLES_SQL_PATH = concat(BASE_DIR, "createTables.sql");
	public static final String DROP_TABLES_SQL_PATH = concat(BASE_DIR, "dropTables.sql");

	// for enum
	public static final String CONSTANTS_JS_PATH = concat(JS_DIR, "\\common-constants.js");
	public static final String LOCAL_STORES_JS_PATH = concat(JS_DIR, "\\localStores.js");
	public static final String RENDERERS_JS_PATH = concat(JS_DIR, "\\renderers.js");

//	public static final String ENTITY_CLASS_NAME = "su.opencode.kefir.sampleSrv.ChooseEntity";
	public static final String ENTITY_CLASS_NAME = "su.opencode.kefir.gen.test.TestEntity";//ChooseEntity//ComboBoxEntity
	public static final String ENUM_CLASS_NAME = "su.opencode.kefir.gen.test.TestEnum";

	public static final String SERVICE_CLASS_NAME = "su.opencode.kefir.gen.test.TestService";
	public static final String SERVICE_BEAN_CLASS_NAME = "su.opencode.kefir.gen.test.TestServiceBean";
}