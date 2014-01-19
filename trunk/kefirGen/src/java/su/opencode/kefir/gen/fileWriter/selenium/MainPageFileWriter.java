/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractPage;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.getSeleniumMainPageClassName;
import static su.opencode.kefir.gen.ExtEntityUtils.getSeleniumMainPagePackage;

public class MainPageFileWriter extends ClassFileWriter
{
	public MainPageFileWriter(String baseDir, String mainMenuTitle) {
		super(baseDir, getSeleniumMainPagePackage(), getSeleniumMainPageClassName());

		this.mainMenuTitle = mainMenuTitle;

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		writeImport(ABSTRACT_PAGE.getName());
		writeComment(APPEND_IMPORT_PAGE_MARKER);
	}
	@Override
	protected void writeClassBody() throws IOException {
		writeClassHeader(ABSTRACT_PAGE);

		writeConstructor("super(TITLE);");

		writeIsValidMethod();

		writeClickButtonEntityListAppenderMark();

		writeFields();

		writeClassFooter();
	}
	private void writeClickButtonEntityListAppenderMark() throws IOException {
		out.writeLn();
		writeComment(APPEND_CLICK_BUTTON_ENTITY_LIST_MARKER);
	}
	private void writeFields() throws IOException {
		out.writeLn();
		writePublicStringConstant(TITLE_CONSTANT_NAME, mainMenuTitle);
		out.writeLn();
		writeComment(APPEND_LIST_BUTTON_ID_MARKER);
	}
	private void writeIsValidMethod() throws IOException {
		out.writeLn(TAB, "public boolean isValid() {");
		out.writeLn(DOUBLE_TAB, "boolean isValid = isTextPresentInHtml(TITLE);");
		out.writeLn();
		writeComment(DOUBLE_TAB, APPEND_IS_ELEMENT_PRESENT_MARKER);
		out.writeLn();
		out.writeLn(DOUBLE_TAB, "return isValid;");
		out.writeLn(TAB, "}");
	}

	private static final Class<AbstractPage> ABSTRACT_PAGE = AbstractPage.class;

	private String mainMenuTitle;

	public static final String TITLE_CONSTANT_NAME = "TITLE";
	public static final String APPEND_IS_ELEMENT_PRESENT_MARKER = "${APPEND_IS_ELEMENT_PRESENT}";
	public static final String APPEND_LIST_BUTTON_ID_MARKER = "${APPEND_LIST_BUTTON_ID}";
	public static final String APPEND_CLICK_BUTTON_ENTITY_LIST_MARKER = "${APPEND_CLICK_BUTTON_ENTITY_LIST}";
	public static final String APPEND_IMPORT_PAGE_MARKER = "${APPEND_IMPORT_PAGE}";
}