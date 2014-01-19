/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import org.apache.log4j.BasicConfigurator;
import su.opencode.kefir.gen.helper.GeneratorHelperRunner;
import su.opencode.kefir.gen.helper.GeneratorServiceHelper;
import su.opencode.kefir.gen.helper.HelperConfig;
import su.opencode.kefir.sampleSrv.ChooseEntity;

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
	private static void generateApp(GeneratorServiceHelper helper, HelperConfig config) throws IOException {
		//todo
//		generateMainPage(config, helper, ChooseEntity.class, MAIN_MENU_TITLE);
//		generateEntityTests(config, helper, ChooseEntity.class);
	}

	public static final String PROPERTIES_FILE_NAME = "su/opencode/kefir/sampleSrv/generator.properties";

	private static final String MAIN_MENU_TITLE = "";
}