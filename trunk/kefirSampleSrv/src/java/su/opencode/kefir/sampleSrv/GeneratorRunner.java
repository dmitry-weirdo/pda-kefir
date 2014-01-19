/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import org.apache.log4j.BasicConfigurator;
import su.opencode.kefir.gen.helper.GeneratorHelperRunner;
import su.opencode.kefir.gen.helper.GeneratorServiceHelper;
import su.opencode.kefir.gen.helper.HelperConfig;

import java.io.IOException;
import java.io.InputStream;

public class GeneratorRunner extends GeneratorHelperRunner
{
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		BasicConfigurator.configure();
		GeneratorServiceHelper helper = getHelper();

		InputStream in = GeneratorRunner.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
		HelperConfig config = HelperConfig.create(in);
		generateApp(helper, config);
	}

	private static void generateApp(GeneratorServiceHelper helper, HelperConfig config) throws IOException, ClassNotFoundException {
//		GeneratorService service = new GeneratorServiceImpl();
//		service.generateListMainMenuButton(config.mainMenuJsFilePath, getExtEntityAnnotation(TestEntity.class), TestEntity.class );
//		if (1 == 1)
//			return;
//
//		generateLocalService(config, helper, SERVICE_CLASS_NAME);

//		generateEnumJs(config, helper, TestEnum.class);

//		generateEntityMapping(config, helper, ComboBoxEntity.class);
//		generateEntityMapping(config, helper, ChooseEntity.class);
//		generateEntityMapping(config, helper, TestEntity.class);

//		generateEntityListAndCrud(config, helper, ComboBoxEntity.class);
		generateEntityListAndCrud(config, helper, ChooseEntity.class);
//		generateEntityListAndCrud(config, helper, TestEntity.class);
	}

	private static final String MAIN_MENU_TITLE = "";

	public static final String PROPERTIES_FILE_NAME = "su/opencode/kefir/sampleSrv/generator.properties";
	public static final String SERVICE_CLASS_NAME = "su.opencode.kefir.generated.TestService";
	public static final String SERVICE_BEAN_CLASS_NAME = "su.opencode.kefir.generated.TestServiceBean";
}