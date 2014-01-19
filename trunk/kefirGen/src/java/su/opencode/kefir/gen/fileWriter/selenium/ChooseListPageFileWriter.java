/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.ChooseField;
import su.opencode.kefir.gen.field.searchField.SearchField;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractChooseListPage;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.srv.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.TreeSet;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.util.ObjectUtils.returnGetMethod;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.decapitalize;

public class ChooseListPageFileWriter extends ClassFileWriter
{
	public ChooseListPageFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getSeleniumPagesPackageName(entityClass);
		this.className = getSeleniumChooseListPageClassName(entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
		formPageClassName = getSeleniumFormPageClassName(entityClass);

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		for (String s : getImportsClasses())
			writeImport(s);
	}
	@Override
	protected void writeClassBody() throws IOException {
		final String className = entityClass.getSimpleName();

		out.writeLn("public class ", this.className, " extends ", ABSTRACT_CHOOSE_LIST_PAGE.getSimpleName());
		out.writeLn("{");

		writeConstructor();

		writeIsValidMethod();

		writeClickButton("Show");
		writeClickButton("Create");

		writeFindEntity(className);

		writeFields();

		out.writeLn("}");
	}
	private void writeFindEntity(String className) throws IOException {
		out.writeLn(TAB, "public ", GRID_ELEMENT.getSimpleName(), " findEntity(", JSON_OBJECT.getSimpleName(), " jsonObject) {");
		out.writeLn(TAB, TAB, "final ", className, " entity = (", className, ") jsonObject;");
		out.writeLn();

		for (Field f : entityClass.getDeclaredFields())
		{
			if (f.isAnnotationPresent(SearchField.class))
			{
				final Class fType = f.getType();
				if (!fType.getSuperclass().equals(JSON_OBJECT))
				{
					out.writeLn(TAB, TAB, "gridElement.fillSearchField(", getSearchFieldName(f), ", entity.", returnGetMethod(entityClass, f).getName(), "());");
					out.writeLn();
				}
				else
				{
					final SearchField annotation = f.getAnnotation(SearchField.class);
					final String methodName = returnGetMethod(fType, annotation.chooseFieldFieldName()).getName();
					final String simpleName = fType.getSimpleName();
					final String var = decapitalize(simpleName);
					out.writeLn(TAB, TAB, "final ", simpleName, " ", var, " = entity.", returnGetMethod(entityClass, f).getName(), "();");
					out.writeLn(TAB, TAB, "if (", var, " != null)");
					out.writeLn(TAB, TAB, TAB, "gridElement.fillSearchField(", getSearchFieldName(f), ", ", var, ".", methodName, "());");
					out.writeLn();
				}
			}
		}

		out.writeLn(TAB, TAB, "return gridElement;");
		out.writeLn(TAB, "}");
	}
	private void writeClickButton(String method) throws IOException {
		final String constant = method.toUpperCase();
		out.writeLn(TAB, "public ", formPageClassName, " click", method, "Button() {");
		out.writeLn(TAB, TAB, "clickById(", constant, "_BUTTON_ID);");
		out.writeLn(TAB, TAB, "return new ", formPageClassName, "(", constant, "_WINDOW_TITLE);");
		out.writeLn(TAB, "}");
	}
	private void writeFields() throws IOException {
		final String className = ExtEntityUtils.getJsFieldPrefix(extEntity, entityClass);

		out.writeLn();
		out.writeLn(TAB, STRING_VARIABLE, "WINDOW_ID = \"", className, "sWindow\";");
		out.writeLn(TAB, STRING_VARIABLE, "WINDOW_TITLE = \"", extEntity.listWindowTitle(), "\";");
		out.writeLn();
		out.writeLn(TAB, STRING_VARIABLE, "SHOW_WINDOW_TITLE = \"", extEntity.showWindowTitle(), "\";");
		out.writeLn(TAB, STRING_VARIABLE, "CREATE_WINDOW_TITLE = \"", extEntity.createWindowTitle(), "\";");
		out.writeLn();
		out.writeLn(TAB, STRING_VARIABLE, "CHOOSE_BUTTON_ID = \"", className, "Choose-chooseButton\";");
		out.writeLn(TAB, STRING_VARIABLE, "SHOW_BUTTON_ID = \"", className, "Choose-showButton\";");
		out.writeLn(TAB, STRING_VARIABLE, "CREATE_BUTTON_ID = \"", className, "Choose-createButton\";");
		out.writeLn(TAB, STRING_VARIABLE, "CLOSE_BUTTON_ID = \"", className, "List-closeButton\";");
		out.writeLn();

		for (Field f : entityClass.getDeclaredFields())
		{
			if (f.isAnnotationPresent(SearchField.class))
				out.writeLn(TAB, STRING_VARIABLE, getSearchFieldName(f), " = \"", className, "ChooseGrid-", f.getName(), "SearchField\";");
		}

		out.writeLn();
		final String simpleName = GRID_ELEMENT.getSimpleName();
		out.writeLn(TAB, "private ", simpleName, " gridElement = new ", simpleName, "(\"", className, "ChooseGrid\");");
	}
	private String getSearchFieldName(Field f) {
		final String fieldName = f.getAnnotation(SearchField.class).chooseFieldFieldName();

		return concat(sb, f.getName().toUpperCase(), "_", fieldName.toUpperCase(), "_SEARCH_FIELD_ID");
	}
	private void writeIsValidMethod() throws IOException {
		out.writeLn(TAB, "public boolean isValid() {");
		out.writeLn(TAB, TAB, "return isElementPresentById(WINDOW_ID);");
		out.writeLn(TAB, "}");
	}
	private void writeConstructor() throws IOException {
		out.writeLn(TAB, "public ", this.className, "() {");
		out.writeLn(TAB, TAB, "super(WINDOW_TITLE);");
		out.writeLn();
		out.writeLn(TAB, TAB, "windowId = WINDOW_ID;");
		out.writeLn();
		out.writeLn(TAB, TAB, "chooseButtonId = CHOOSE_BUTTON_ID;");
		out.writeLn(TAB, TAB, "closeButtonId = CLOSE_BUTTON_ID;");
		out.writeLn(TAB, "}");
		out.writeLn();
	}
	private TreeSet<String> getImportsClasses() {
		final TreeSet<String> importClassesSet = new TreeSet<>();

		importClassesSet.add(entityClass.getName());
		importClassesSet.add(ABSTRACT_CHOOSE_LIST_PAGE.getName());
		importClassesSet.add(GRID_ELEMENT.getName());
		importClassesSet.add(JSON_OBJECT.getName());

		for (Field f : entityClass.getDeclaredFields())
		{
			if (f.isAnnotationPresent(ChooseField.class))
				importClassesSet.add(f.getType().getName());
		}

		return importClassesSet;
	}

	private static final Class<JsonObject> JSON_OBJECT = JsonObject.class;
	private static final String STRING_VARIABLE = "private static final String ";
	private static final Class<AbstractChooseListPage> ABSTRACT_CHOOSE_LIST_PAGE = AbstractChooseListPage.class;
	private static final Class<GridElement> GRID_ELEMENT = GridElement.class;

	private ExtEntity extEntity;
	private Class entityClass;
	private final String formPageClassName;
}