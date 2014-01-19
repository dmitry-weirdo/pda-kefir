/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 14:41:52$
*/
package su.opencode.kefir.gen.helper;

import java.io.IOException;

public class GeneratorHelperRunner
{
	protected static GeneratorServiceHelper getHelper() {
		return new GeneratorServiceHelperImpl();
	}

	protected static void generateLocalService(HelperConfig config, GeneratorServiceHelper helper, String serviceClassName) throws IOException {
		config.serviceClassName = serviceClassName;
		helper.generateLocalInterface(config);
	}
	protected static void generateEntityMapping(HelperConfig config, GeneratorServiceHelper helper, Class entityClass) throws IOException {
		config.entityClass = entityClass;
		helper.generateEntityMapping(config);
	}
	protected static void generateEntityVO(HelperConfig config, GeneratorServiceHelper helper, Class entityClass) throws IOException {
		config.entityClass = entityClass;
		helper.generateEntityVO(config);
	}
	protected static void generateEntityListAndCrud(HelperConfig config, GeneratorServiceHelper helper, Class entityClass) throws IOException {
		config.entityClass = entityClass;
		helper.generateEntityList(config);
		helper.generateEntityCrud(config);
	}
	protected static void generateEnumJs(HelperConfig config, GeneratorServiceHelper helper, Class aClass) throws IOException {
		config.enumClass = aClass;
		helper.generateEnumJs(config);
	}
	protected static void generateEntityTests(HelperConfig config, GeneratorServiceHelper helper, Class entityClass) throws IOException {
		config.entityClass = entityClass;
		helper.generateEntityTests(config);
	}
	protected static void generateMainPage(HelperConfig config, GeneratorServiceHelper helper, String mainMenuTitle) throws IOException {
		helper.generateMainPage(config, mainMenuTitle);
	}
}