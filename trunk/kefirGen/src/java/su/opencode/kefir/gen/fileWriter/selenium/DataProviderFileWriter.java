/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import org.testng.annotations.DataProvider;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractDataProvider;
import su.opencode.kefir.srv.json.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import static su.opencode.kefir.gen.ExtEntityUtils.getSeleniumDataProviderClassName;
import static su.opencode.kefir.gen.ExtEntityUtils.getSeleniumDataProviderPackageName;

public class DataProviderFileWriter extends ClassFileWriter
{
	public DataProviderFileWriter(String baseDir, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getSeleniumDataProviderPackageName(entityClass);
		this.className = getSeleniumDataProviderClassName(entityClass);
		this.entityClass = entityClass;

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		for (String s : getImportsClasses())
			writeImport(s);

		out.writeLn();
		writeImport(HashMap.class.getName());
		writeImport(Iterator.class.getName());
		out.writeLn();
		writeStaticImport(JSON_OBJECT, "fromJson");
	}
	@Override
	protected void writeClassBody() throws IOException {
		final String className = entityClass.getSimpleName();

		out.writeLn("public class ", this.className, " extends ", ABSTRACT_DATA_PROVIDER.getSimpleName());
		out.writeLn("{");

		writeLoadDataMethod();

		writeGetEntityFromJsonMethod();

		writeFields(className);

		out.writeLn("}");
	}
	private void writeGetEntityFromJsonMethod() throws IOException {
		out.writeLn(TAB, "@Override");
		out.writeLn(TAB, "protected Object[] getEntityFromJson(HashMap<String, String[]> createEntityData, HashMap<String, String[]> updateEntityData) {");
		out.writeLn(TAB, TAB, "return new Object[] { fromJson(createEntityData, ENTITY_CLASS), fromJson(updateEntityData, ENTITY_CLASS) };");
		out.writeLn(TAB, "}");
	}
	private void writeFields(String className) throws IOException {
		out.writeLn();
		out.writeLn(TAB, "private static final Class<? extends ", JSON_OBJECT.getSimpleName(), "> ENTITY_CLASS = ", className, ".class;");
		out.writeLn(TAB, STRING_VARIABLE, "VO_DATA_FILE = \"", className, ".json\";");
		out.writeLn();
		out.writeLn(TAB, "public static final String ", DATA_PROVIDER_NAME, " = \"", className, "\";");
	}
	private void writeLoadDataMethod() throws IOException {
		out.writeLn(TAB, "@DataProvider(name = ", DATA_PROVIDER_NAME, ")");
		out.writeLn(TAB, "public static Iterator<Object[]> loadData() {");
		out.writeLn(TAB, TAB, "return new ", className, "().loadDataFromJson(VO_DATA_FILE);");
		out.writeLn(TAB, "}");
	}
	private TreeSet<String> getImportsClasses() {
		final TreeSet<String> importClassesSet = new TreeSet<>();

		importClassesSet.add(entityClass.getName());
		importClassesSet.add(ABSTRACT_DATA_PROVIDER.getName());
		importClassesSet.add(DATA_PROVIDER);
		importClassesSet.add(JSON_OBJECT.getName());

		return importClassesSet;
	}

	public static final String DATA_PROVIDER_NAME = "DATA_PROVIDER_NAME";

	private static final String STRING_VARIABLE = "private static final String ";
	private static final String DATA_PROVIDER = DataProvider.class.getName();
	private static final Class<JsonObject> JSON_OBJECT = JsonObject.class;
	private static final Class<AbstractDataProvider> ABSTRACT_DATA_PROVIDER = AbstractDataProvider.class;

	private Class entityClass;
}