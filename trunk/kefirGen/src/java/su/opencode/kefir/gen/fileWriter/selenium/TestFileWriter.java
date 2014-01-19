/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import org.testng.annotations.Test;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractTestCase;

import java.io.IOException;
import java.util.TreeSet;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.selenium.DataProviderFileWriter.DATA_PROVIDER_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class TestFileWriter extends ClassFileWriter
{
	public TestFileWriter(String baseDir, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getSeleniumTestPackageName(entityClass);
		this.className = getSeleniumTestClassName(entityClass);
		this.entityClass = entityClass;

		dataProviderClassName = getSeleniumDataProviderClassName(entityClass);
		listPageClassName = getSeleniumListPageClassName(entityClass);
		dataProviderPackageName = getSeleniumDataProviderPackageName(entityClass);
		formPageClassName = getSeleniumFormPageClassName(entityClass);
		mainPageClassName = getSeleniumMainPageClassName();

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		for (String s : getImportsClasses())
			writeImport(s);

		out.writeLn();
		writeStaticImport(dataProviderPackageName, dataProviderClassName, DATA_PROVIDER_NAME);
	}
	@Override
	protected void writeClassBody() throws IOException {
		final String className = entityClass.getSimpleName();

		out.writeLn("public class ", this.className, " extends ", ABSTRACT_TEST_CASE.getSimpleName());
		out.writeLn("{");

		writeGetListPageMethod(className);

		writeTestCreateMethod(className);
		writeTestShowMethod(className);
		writeTestUpdateMethod(className);
		writeTestDeleteMethod(className);
		writeTestCreateIfNotExistsMethod(className);

		writeFields();

		out.writeLn("}");
	}
	private void writeTestCreateMethod(String className) throws IOException {
		out.writeLn(TAB, "@Test(dataProvider = ", DATA_PROVIDER_NAME, ", dataProviderClass = ", dataProviderClassName, ".class)");
		out.writeLn(TAB, "public void testCreate(", className, " createEntity, ", className, " updateEntity) {");
		out.writeLn(TAB, TAB, "createEntity(getListPage(), createEntity, mainPageClass);");
		out.writeLn(TAB, "}");
	}
	private void writeTestShowMethod(String className) throws IOException {
		out.writeLn(TAB, "@Test(dependsOnMethods = \"testCreate\", dataProvider = ", DATA_PROVIDER_NAME, ", dataProviderClass = ", dataProviderClassName, ".class)");
		out.writeLn(TAB, "public void testShow(", className, " createEntity, ", className, " updateEntity) {");
		out.writeLn(TAB, TAB, "showEntity(getListPage(), createEntity, formPageClass, mainPageClass);");
		out.writeLn(TAB, "}");
	}
	private void writeTestUpdateMethod(String className) throws IOException {
		out.writeLn(TAB, "@Test(dependsOnMethods = \"testShow\", dataProvider = ", DATA_PROVIDER_NAME, ", dataProviderClass = ", dataProviderClassName, ".class)");
		out.writeLn(TAB, "public void testUpdate(", className, " createEntity, ", className, " updateEntity) {");
		out.writeLn(TAB, TAB, "updateEntity(getListPage(), createEntity, updateEntity, formPageClass, mainPageClass);");
		out.writeLn(TAB, "}");
	}
	private void writeTestDeleteMethod(String className) throws IOException {
		out.writeLn(TAB, "@Test(dependsOnMethods = \"testUpdate\", dataProvider = ", DATA_PROVIDER_NAME, ", dataProviderClass = ", dataProviderClassName, ".class)");
		out.writeLn(TAB, "public void testDelete(", className, " createEntity, ", className, " updateEntity) {");
		out.writeLn(TAB, TAB, "deleteEntity(getListPage(), updateEntity, formPageClass, mainPageClass);");
		out.writeLn(TAB, "}");
	}
	private void writeTestCreateIfNotExistsMethod(String className) throws IOException {
		out.writeLn(TAB, "@Test(dependsOnMethods = \"testDelete\", dataProvider = ", DATA_PROVIDER_NAME, ", dataProviderClass = ", dataProviderClassName, ".class)");
		out.writeLn(TAB, "public void testCreateIfNotExists(", className, " createEntity, ", className, " updateEntity) {");
		out.writeLn(TAB, TAB, "final ", listPageClassName, " listPage = getListPage();");
		out.writeLn(TAB, TAB, "if (!selectEntityFromGrid(listPage, updateEntity))");
		out.writeLn(TAB, TAB, TAB, "createEntity(listPage, updateEntity, mainPageClass);");
		out.writeLn(TAB, "}");
	}
	private void writeFields() throws IOException {
		out.writeLn();
		out.writeLn(TAB, CLASS_VARIABLE, "<", formPageClassName, "> formPageClass = ", formPageClassName, ".class;");
		out.writeLn(TAB, CLASS_VARIABLE, "<", mainPageClassName, "> mainPageClass = ", mainPageClassName, ".class;");
	}
	private void writeGetListPageMethod(String className) throws IOException {
		out.writeLn(TAB, "private ", listPageClassName, " getListPage() {");
		out.writeLn(TAB, TAB, "return new ", mainPageClassName, "().clickButton", className, "List();");
		out.writeLn(TAB, "}");
	}
	private TreeSet<String> getImportsClasses() {
		final TreeSet<String> importClassesSet = new TreeSet<>();

		final String pagesPackageName = getSeleniumPagesPackageName(entityClass);
		importClassesSet.add(entityClass.getName());
		importClassesSet.add(ABSTRACT_TEST_CASE.getName());
		importClassesSet.add(Test.class.getName());
		importClassesSet.add(concat(sb, getSeleniumMainPagePackage(), ".", mainPageClassName));
		importClassesSet.add(concat(sb, pagesPackageName, ".", formPageClassName));
		importClassesSet.add(concat(sb, pagesPackageName, ".", listPageClassName));
		importClassesSet.add(concat(sb, dataProviderPackageName, ".", dataProviderClassName));

		return importClassesSet;
	}

	private static final String CLASS_VARIABLE = "private static final Class";
	private static final Class<AbstractTestCase> ABSTRACT_TEST_CASE = AbstractTestCase.class;

	private final Class entityClass;
	private final String dataProviderPackageName;
	private final String dataProviderClassName;
	private final String formPageClassName;
	private final String listPageClassName;
	private final String mainPageClassName;
}