/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 21.03.2012 14:32:38$
*/
package su.opencode.kefir.gen.fileWriter;

import org.apache.log4j.BasicConfigurator;
import su.opencode.kefir.gen.helper.GeneratorHelperRunner;
import su.opencode.kefir.gen.helper.GeneratorServiceHelper;
import su.opencode.kefir.gen.helper.HelperConfig;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang.StringEscapeUtils.escapeJava;
import static su.opencode.kefir.gen.ExtEntityUtils.PACKAGE_SEPARATOR;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.GENERATOR_PROPERTIES_FILE_NAME;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.getMainMenuTitle;
import static su.opencode.kefir.util.FileUtils.STRAIGHT_FILE_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

public class GeneratorRunnerClassWriter extends ClassFileWriter
{
	public GeneratorRunnerClassWriter(String baseDir, String packageName, String className, ProjectConfig config) {
		super(baseDir, packageName, className);
		this.config = config;
		this.failIfFileExists = false;
	}
	@Override
	protected void writeImports() throws IOException {
		writeImport(BasicConfigurator.class);
		writeImport(GeneratorHelperRunner.class);
		writeImport(GeneratorServiceHelper.class);
		writeImport(HelperConfig.class);
		out.writeLn();
		writeImport(IOException.class);
		writeImport(InputStream.class);
		out.writeLn();
	}
	@Override
	protected void writeClassBody() throws IOException {
		writeClassHeader(GeneratorHelperRunner.class);

		writeMain();
		writeGenerateApp();
		out.writeLn();
		writeFields();

		writeClassFooter();
	}
	private void writeMain() throws IOException {
		String inputStreamVarName = "in";
		String helperVarName = "helper";
		String configVarName = "config";

		out.writeLn(
			TAB, "public static void ", MAIN_METHOD_NAME, "(String[] args) ",
			"throws ", IOException.class.getSimpleName(), ", ", ClassNotFoundException.class.getSimpleName(), " {"
		);
		out.writeLn(DOUBLE_TAB, BasicConfigurator.class.getSimpleName(), ".configure();");
		out.writeLn(DOUBLE_TAB, GeneratorServiceHelper.class.getSimpleName(), " ", helperVarName, " = ", GET_HELPER_METHOD_NAME, "();");

		out.writeLn();
		out.writeLn(DOUBLE_TAB, InputStream.class.getSimpleName(), " ", inputStreamVarName, " = ", className, ".class.getClassLoader().getResourceAsStream(", PROPERTIES_FILE_NAME_FIELD_NAME, ");");

		out.writeLn(
			DOUBLE_TAB, HelperConfig.class.getSimpleName(), " ", configVarName,
			" = ",
			HelperConfig.class.getSimpleName(), ".", HELPER_CREATE_METHOD_NAME, "(", inputStreamVarName, ");"
		);

		out.writeLn(DOUBLE_TAB, configVarName, ".setPathPrefix(System.getProperty(APP_FOLDER));");
		out.writeLn(DOUBLE_TAB, GENERATE_APP_METHOD_NAME, "(", helperVarName, ", ", configVarName, ");");

		out.writeLn(TAB, "}"); // end public static void main
	}
	private void writeGenerateApp() throws IOException {
		String helperParamName = "helper";
		String configParamName = "config";

		out.writeLn(
			TAB, "private static void ", GENERATE_APP_METHOD_NAME, "(", GeneratorServiceHelper.class.getSimpleName(), " ", helperParamName, ", ", HelperConfig.class.getSimpleName(), " ", configParamName, ") ",
			"throws ", IOException.class.getSimpleName(), ", ", ClassNotFoundException.class.getSimpleName(), " {"
		);
		writeComment(DOUBLE_TAB, "todo: implement method");
		out.writeLn(TAB, "}"); // end private static void generarteApp
	}
	private void writeFields() throws IOException {
		String fileName = concat(sb, packageName.replace(PACKAGE_SEPARATOR, STRAIGHT_FILE_SEPARATOR), STRAIGHT_FILE_SEPARATOR, GENERATOR_PROPERTIES_FILE_NAME);
		writePublicStringConstant(PROPERTIES_FILE_NAME_FIELD_NAME, fileName);
		out.writeLn();
		out.writeLn(TAB, "private static final String MAIN_MENU_TITLE = \"", escapeJava(getMainMenuTitle(config)), "\";");
		writePrivateStringConstant("APP_FOLDER", "appFolder");
	}

	protected ProjectConfig config;

	public static final String MAIN_METHOD_NAME = "main";
	public static final String GENERATE_APP_METHOD_NAME = "generateApp";
	public static final String GET_HELPER_METHOD_NAME = "getHelper";
	public static final String HELPER_CREATE_METHOD_NAME = "create";
	public static final String PROPERTIES_FILE_NAME_FIELD_NAME = "PROPERTIES_FILE_NAME";
}