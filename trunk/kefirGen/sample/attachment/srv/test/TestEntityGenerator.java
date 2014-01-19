/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.GeneratorService;
import su.opencode.kefir.gen.GeneratorServiceImpl;
import su.opencode.minstroy.ejb.attachment.Attachment;

import java.io.IOException;

public class TestEntityGenerator
{
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		Class<?> entityClass = Class.forName(ENTITY_CLASS_NAME);
		Class<?> entityClass = Class.forName("su.opencode.minstroy.ejb.attachment.Attachment");
		ExtEntity extEntity = ExtEntityUtils.getExtEntityAnnotation(entityClass);

//		Field[] fields = entityClass.getDeclaredFields();
//		for (Field field : fields)
//		{
//			System.out.println( concat("name: ", field.getCadastralNumber(), " type: ", field.getType() ) );
//		}

		GeneratorService service = new GeneratorServiceImpl();

//		service.generateEntityFilterConfig(SRV_SRC_DIR, extEntity, entityClass);
//		service.generateListToExcelServlet(WEB_SRC_DIR, extEntity, entityClass);
//		service.generateListToExcelServletMapping(WEB_XML_PATH, extEntity, entityClass);

//		service.generateEntityQueryBuilder(SRV_SRC_DIR, extEntity, entityClass);
		service.generateEntityFilterConfig(SRV_SRC_DIR, extEntity, entityClass);
//		service.generateServiceListMethods(SRV_SRC_DIR, extEntity, entityClass);
//		service.generateServiceBeanListMethods(SRV_SRC_DIR, extEntity, entityClass);

//		service.generateListServlet(WEB_SRC_DIR, extEntity, entityClass);
//		service.generateListServletMapping(WEB_XML_PATH, extEntity, entityClass);

//		service.generateListScript(JS_DIR, extEntity, entityClass);

		
//		service.generateChooseScript(JS_DIR, extEntity, entityClass);
//		service.generateFormScript(JS_DIR, extEntity, entityClass);
//		service.generateGetServlet(WEB_SRC_DIR, extEntity, entityClass);

//		service.generateEmptyServlet(WEB_SRC_DIR, "test", "TestEntityDeleteServlet");

//		GeneratorServiceHelper helper = new GeneratorServiceHelperImpl();
//		helper.generateLocalInterface(SRV_SRC_DIR, WEB_XML_PATH, JBOSS_WEB_XML_PATH, "su.opencode.minstroy.ejb.attachment.AttachmentService");
//		helper.generateEntityServiceCrudMethods(Attachment.class, SRV_SRC_DIR);


//		helper.generateEntityList(entityClass, SRV_SRC_DIR, WEB_SRC_DIR, WEB_XML_PATH, JS_DIR);
//		helper.generateEntityCrud(entityClass, SRV_SRC_DIR, WEB_SRC_DIR, WEB_XML_PATH, JS_DIR);

//		Class<?> enumClass = Class.forName(ENUM_CLASS_NAME);
//		helper.generateEnumJs(enumClass, CONSTANTS_JS_PATH, LOCAL_STORES_JS_PATH, RENDERERS_JS_PATH);
	}

	// todo: read all these constants from txt config
	public static final String BASE_DIR = "D:\\java\\minstroy\\";

	public static final String SRV_SRC_DIR = concat(BASE_DIR, "minstroySrv\\src\\java\\");
	public static final String WEB_SRC_DIR = concat(BASE_DIR, "minstroyWeb\\src\\java\\");
	public static final String JS_DIR = concat(BASE_DIR, "minstroyWeb\\web\\js");

	public static final String WEB_XML_PATH = concat(BASE_DIR, "minstroyWeb\\web\\WEB-INF\\web.xml");
	public static final String JBOSS_WEB_XML_PATH = concat(BASE_DIR, "minstroyWeb\\web\\WEB-INF\\jboss-web.xml");

	// for enum
	public static final String CONSTANTS_JS_PATH = concat(JS_DIR, "\\common-constants.js");
	public static final String LOCAL_STORES_JS_PATH = concat(JS_DIR, "\\localStores.js");
	public static final String RENDERERS_JS_PATH = concat(JS_DIR, "\\renderers.js");


	public static final String SERVICE_CLASS_NAME = "test.TestService";
	public static final String ENTITY_CLASS_NAME = "test.TestEntity";
	public static final String ENUM_CLASS_NAME = "test.TestEnum";
}