/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 15:04:58$
*/
package su.opencode.kefir.gen.appender.selenium;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.appender.Appender;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.gen.fileWriter.selenium.MainPageFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.DOUBLE_TAB;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.TAB;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.ObjectUtils.getClassForName;
import static su.opencode.kefir.util.StringUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Добавляет кнопку, открывающую список сущностей, в главное меню.
 */
public class MainPageAppender extends Appender
{
	public MainPageAppender(String filePath, ExtEntity extEntity, Class entityClass) {
		this.filePath = filePath;
		this.extEntity = extEntity;
		this.entityClass = entityClass;
		buttonId = concat(sb, getConstantName(getPlural(entityClass.getSimpleName())), BUTTON_ID_CONSTANT_POSTFIX);
	}
	@Override
	protected String getEncoding() {
		return CHARSET_CP1251;
	}
	public void appendMainMenuButton() throws IOException {
		if (!extEntity.hasListMainMenuButton())
			return;

		File file = new File(concat(sb, filePath, FILE_SEPARATOR, getSeleniumMainPagePackage().replace(PACKAGE_SEPARATOR, FILE_SEPARATOR), FILE_SEPARATOR, getSeleniumMainPageClassName(), ".java"));
		List<String> fileLines = readLinesFromFile(file);

		if (isEntityPresent(fileLines))
			return;

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private boolean isEntityPresent(List<String> fileLines) {
		for(String s : fileLines)
			if (s.contains(buttonId))
				return true;

		return false;
	}
	private boolean isEntityPresent() {
		final String mainPageClassName = concat(sb, getSeleniumPagesPackageName(entityClass), ".", getSeleniumMainPageClassName());
		final Class mainPage = getClassForName(mainPageClassName);
		try
		{
			mainPage.getDeclaredField(buttonId);
		}
		catch (NoSuchFieldException e)
		{
			return false;
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}

		return true;
	}
	private void appendLines(List<String> fileLines) throws IOException {
		for (i = 0; i < fileLines.size(); i++)
		{
			final String fileString = fileLines.get(i);

			if (append(fileLines, fileString, AppendMode.importPage)) continue;
			if (append(fileLines, fileString, AppendMode.isElementPresent)) continue;
			if (append(fileLines, fileString, AppendMode.clickButton)) continue;
			append(fileLines, fileString, AppendMode.listButtonId);
		}
	}
	private boolean append(List<String> fileLines, String fileString, AppendMode appendMode) {
		if (fileString.contains(getAppendMarker(appendMode)))
		{
			fileLines.add(i++, getLines(appendMode));
			return true;
		}

		return false;
	}
	private String getLines(AppendMode mode) {
		switch (mode)
		{
			case importPage: return getImportPageString();
			case isElementPresent: return getIsElementPresentString();
			case clickButton: return getClickButtonString();
			case listButtonId: return getListButtonId();

			default:
				throw new IllegalArgumentException(concat(sb, "Incorrect AppendMode for ", this.getClass().getName(), ":", mode));
		}
	}
	private String getImportPageString() {
		return concat("import ", getSeleniumPagesPackageName(entityClass), ".", getSeleniumListPageClassName(entityClass), ";");
	}
	private String getClickButtonString() {
		final String listPageClassName = getSeleniumListPageClassName(entityClass);
		return concat(sb,
			TAB, "public ", listPageClassName, " clickButton", entityClass.getSimpleName(), "List() {\n",
			DOUBLE_TAB, "clickById(", buttonId, ");\n",
			DOUBLE_TAB, "return new ", listPageClassName, "();\n",
			TAB, "}");
	}
	private String getListButtonId() {
		return concat(sb, TAB, "private static final String ", buttonId, " = \"", getListMainMenuButtonId(extEntity, entityClass), "\";");
	}
	private String getIsElementPresentString() {
		return concat(sb, DOUBLE_TAB, "isValid = isValid && isElementPresentById(", buttonId, ");");
	}
	private String getAppendMarker(AppendMode mode) {
		switch (mode)
		{
			case isElementPresent: return MainPageFileWriter.APPEND_IS_ELEMENT_PRESENT_MARKER;
			case clickButton: return MainPageFileWriter.APPEND_CLICK_BUTTON_ENTITY_LIST_MARKER;
			case listButtonId: return MainPageFileWriter.APPEND_LIST_BUTTON_ID_MARKER;
			case importPage: return MainPageFileWriter.APPEND_IMPORT_PAGE_MARKER;

			default:
				throw new IllegalArgumentException(concat(sb, "Incorrect AppendMode for ", this.getClass().getName(), ":", mode));
		}
	}

	private int i;
	private ExtEntity extEntity;
	private String buttonId;
	private Class entityClass;
	protected String filePath;

	public static final String BUTTON_ID_CONSTANT_POSTFIX = "_LIST_BUTTON_ID";

	private enum AppendMode
	{
		importPage, isElementPresent, clickButton, listButtonId
	}
}