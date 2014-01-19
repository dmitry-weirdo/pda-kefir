/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 15:04:58$
*/
package su.opencode.kefir.gen.appender.js;

import su.opencode.kefir.gen.ExtEntity;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.js.project.MainMenuJsFileWriter.ADD_MAIN_MENU_BUTTON_FUNCTION_NAME;
import static su.opencode.kefir.util.StringUtils.*;

/**
 * Добавляет кнопку, открывающую список сущностей, в главное меню.
 */
public class ListMainMenuButtonAppender extends JsAppender
{
	public ListMainMenuButtonAppender(String filePath, ExtEntity extEntity, Class entityClass) {
		this.filePath = filePath;
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	public void appendMainMenuButton() throws IOException {
		if ( !extEntity.hasListMainMenuButton() ) // если кнопки нет - ничего не делать
			return;

		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines, AppendMode.constants);
		appendLines(fileLines, AppendMode.variable);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines, AppendMode mode) throws IOException {
		String appendMarker = getAppendMarker(mode);
		if ( mainMenuButtonIsPresent(fileLines, mode))
		{
			return;
		}

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<String>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendButton(mode, appendedFileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private boolean mainMenuButtonIsPresent(List<String> fileLines, AppendMode mode) {
		String strToFind = getStrToFind(mode);

		for (String fileLine : fileLines)
		{
			if (fileLine.contains(strToFind))
				{
					logger.info(getListMainMenuIsPresentLogMessage(mode));
					return true;
				}
		}

		return false;
	}
	private String getStrToFind(AppendMode mode) {
		switch (mode)
		{
			case constants: return concat(sb, "var", " ", getButtonIdConstantName());
			case variable: return concat(sb, ADD_MAIN_MENU_BUTTON_FUNCTION_NAME, "(", getButtonIdConstantName());

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}

	private String getListMainMenuIsPresentLogMessage(AppendMode mode){
		switch (mode)
		{
			case constants: return concat(sb, "constant \"", getButtonIdConstantName(), "\" declaration is already present");
			case variable: return concat(sb, "adding button with id \"", getButtonIdConstantName(), "\" is already present");

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode: ", mode) );
		}
	}

	private String getAppendMarker(AppendMode mode) {
		switch (mode)
		{
			case constants: return APPEND_BUTTON_CONSTANTS_MARKER;
			case variable: return APPEND_BUTTON_VARIABLE_MARKER;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode for ", this.getClass().getName(), ":", mode) );
		}
	}
	private void appendButton(AppendMode mode, List<String> fileLines) throws IOException {
		// todo: проверить, что кнопки с таким id еще не добавлено

		switch (mode)
		{
			case constants: appendButtonConstants(fileLines); return;
			case variable: appendButtonVariable(fileLines); return;

			default: throw new IllegalArgumentException( concat(sb, "Incorrect AppendMode for ", this.getClass().getName(), ":", mode) );
		}
	}

	private void appendButtonConstants(List<String> fileLines) throws IOException {
		appendConstant(fileLines, getButtonIdConstantName(), getListMainMenuButtonId(extEntity, entityClass));
		appendConstant(fileLines, getButtonTextConstantName(), getListMainMenuButtonText(extEntity, entityClass));

		if ( notEmpty( getListMainMenuButtonToolTip(extEntity, entityClass) ) ) // есть отдельный тултип у кнопки
			appendConstant(fileLines, getButtonToolTipConstantName(), getListMainMenuButtonToolTip(extEntity, entityClass));

		fileLines.add(""); // empty line after each button's constants
	}

	private void appendButtonVariable(List<String> fileLines) {
	  String toolTipConstantName = null;
		if ( notEmpty(getListMainMenuButtonToolTip(extEntity, entityClass)) )
			toolTipConstantName = getButtonToolTipConstantName();

//		addMainMenuButton(COMBO_BOX_ENTITYS_LIST_BUTTON_ID, COMBO_BOX_ENTITYS_LIST_BUTTON_TEXT, null, function() {
		fileLines.add( concat(sb, "\t\t", ADD_MAIN_MENU_BUTTON_FUNCTION_NAME, "(", getButtonIdConstantName(), ", ", getButtonTextConstantName(), ", ", toolTipConstantName, ", function() {") );
		fileLines.add( concat(sb, "\t\t\t", getListInitFunctionFullName(extEntity, entityClass), "({});" )); // su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntitysList.init({});
		fileLines.add("\t\t});");

		fileLines.add(""); // empty line after each button's addition
	}

	private String getButtonIdConstantName() {
		return concat( sb, getConstantName( getPlural(entityClass.getSimpleName()) ), BUTTON_ID_CONSTANT_POSTFIX );
	}
	private String getButtonTextConstantName() {
		return concat( sb, getConstantName( getPlural(entityClass.getSimpleName()) ), BUTTON_TEXT_CONSTANT_POSTFIX );
	}
	private String getButtonToolTipConstantName() {
		return concat( sb, getConstantName( getPlural(entityClass.getSimpleName()) ), BUTTON_TOOL_TIP_CONSTANT_POSTFIX );
	}

	private ExtEntity extEntity;
	private Class entityClass;

	public static final String BUTTON_ID_CONSTANT_POSTFIX = "_LIST_BUTTON_ID";
	public static final String BUTTON_TEXT_CONSTANT_POSTFIX = "_LIST_BUTTON_TEXT";
	public static final String BUTTON_TOOL_TIP_CONSTANT_POSTFIX = "_LIST_BUTTON_TOOL_TIP";

	public static final String APPEND_BUTTON_CONSTANTS_MARKER = "${APPEND_BUTTON_CONSTANTS}";
	public static final String APPEND_BUTTON_VARIABLE_MARKER = "${APPEND_BUTTON_VARIABLE}";

	public enum AppendMode { constants, variable }
}