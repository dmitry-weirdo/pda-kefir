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

import su.opencode.kefir.gen.*;
import su.opencode.kefir.gen.GeneratorService;
import su.opencode.kefir.gen.GeneratorServiceImpl;
import su.opencode.kefir.gen.ExtEntityUtils;

import java.io.IOException;

public class TestEntityGenerator
{
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Class<?> entityClass = Class.forName(ENTITY_CLASS_NAME);
		ExtEntity extEntity = ExtEntityUtils.getExtEntityAnnotation(entityClass);

//		Field[] fields = entityClass.getDeclaredFields();
//		for (Field field : fields)
//		{
//			System.out.println( concat("name: ", field.getCadastralNumber(), " type: ", field.getType() ) );
//		}

		GeneratorService service = new GeneratorServiceImpl();
		service.generateListScript(JS_DIR, extEntity, entityClass);

//		service.generateEmptyServlet(WEB_SRC_DIR, "test", "TestEntityDeleteServlet");

//		GeneratorServiceHelper helper = new GeneratorServiceHelperImpl();
//		helper.generateLocalInterface(SRV_SRC_DIR, WEB_XML_PATH, JBOSS_WEB_XML_PATH, SERVICE_CLASS_NAME);
//		helper.generateEntityList(entityClass, SRV_SRC_DIR, WEB_SRC_DIR, WEB_XML_PATH, JS_DIR);
	}

	// todo: read all these constants from txt config
	public static final String BASE_DIR = "D:\\java\\minstroy\\";

	public static final String SRV_SRC_DIR = concat(BASE_DIR, "minstroySrv\\src\\java\\");
	public static final String WEB_SRC_DIR = concat(BASE_DIR, "minstroyWeb\\src\\java\\");
	public static final String JS_DIR = concat(BASE_DIR, "minstroyWeb\\web\\js");

	public static final String WEB_XML_PATH = concat(BASE_DIR, "minstroyWeb\\web\\WEB-INF\\web.xml");
	public static final String JBOSS_WEB_XML_PATH = concat(BASE_DIR, "minstroyWeb\\web\\WEB-INF\\jboss-web.xml");

	public static final String SERVICE_CLASS_NAME = "test.TestService";
	public static final String ENTITY_CLASS_NAME = "test.TestEntity";
}