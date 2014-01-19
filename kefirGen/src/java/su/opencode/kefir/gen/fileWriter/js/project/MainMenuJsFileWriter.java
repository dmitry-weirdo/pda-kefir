/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 23.03.2012 16:55:23$
*/
package su.opencode.kefir.gen.fileWriter.js.project;

import su.opencode.kefir.gen.fileWriter.js.JsFileWriter;
import su.opencode.kefir.gen.fileWriter.js.JsHash;
import su.opencode.kefir.gen.project.ProjectConfig;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.getFullName;
import static su.opencode.kefir.gen.appender.js.ListMainMenuButtonAppender.APPEND_BUTTON_CONSTANTS_MARKER;
import static su.opencode.kefir.gen.appender.js.ListMainMenuButtonAppender.APPEND_BUTTON_VARIABLE_MARKER;
import static su.opencode.kefir.gen.fileWriter.js.JsUtils.getFunctionSignature;
import static su.opencode.kefir.gen.project.ProjectConfigUtils.*;

public class MainMenuJsFileWriter extends JsFileWriter
{
	public MainMenuJsFileWriter(ProjectConfig config, String baseDir, String dir, String fileName) {
		super(baseDir, dir, fileName);
		this.config = config;
	}

	@Override
	protected void writeFile() throws IOException {
		writeNamespace(getMainMenuNamespace(config));
		writeNamespaceHeader(getMainMenuFullNamespace(config));

		writeConstants();
		out.writeLn();

		writeVariables();
		out.writeLn();

		writeFunctions();
		out.writeLn();

		writeReturn();

		writeNamespaceFooter();
	}
	private void writeConstants() throws IOException {
		writeConstant(TITLE_CONSTANT_NAME, getMainMenuTitle(config));
		writeConstant(BUTTON_WIDTH_CONSTANT_NAME, getMainMenuButtonWidth(config));
		out.writeLn();
		writeComment(APPEND_BUTTON_CONSTANTS_MARKER);
	}

	private void writeVariables() throws IOException {
		writeVariable(BUTTONS_VAR_NAME, "new Array()", "\t");
	}

	private void writeFunctions() throws IOException {
		writeAddButton();
		writeGetMainMenuButton();
		writeAddMainMenuButton();
		out.writeLn();
		writeGetButtons();
	}
	private void writeAddButton() throws IOException {
		String buttonParamName = "button";

		writeFunctionHeader(ADD_BUTTON_FUNCTION_NAME, buttonParamName);
		out.writeLn("\t\t", BUTTONS_VAR_NAME, ".push(", buttonParamName, ");");
		writeFunctionFooter();
	}
	private void writeGetMainMenuButton() throws IOException {
		String idParamName = "id";
		String textParamName = "text";
		String toolTipParamName = "toolTip";
		String handlerParamName = "handler";

		writeFunctionHeader(GET_MAIN_MENU_BUTTON_FUNCTION_NAME, idParamName, textParamName, toolTipParamName, handlerParamName);

		JsHash params = new JsHash();
		params.put("id", idParamName);
		params.put("text", textParamName);
		params.put("tooltip", toolTipParamName);
		params.put("width", BUTTON_WIDTH_CONSTANT_NAME);
		params.put("handler", handlerParamName);

		// Kefir.mainMenu.MainMenu.getMainMenuButton
		String getMainMenuButtonFunctionFullName = getFullName(KEFIR_MAIN_MENU_NAMESPACE, KEFIR_MAIN_MENU_GET_MAIN_MENU_BUTTON_FUNCTION_NAME);// Kefir.mainMenu.MainMenu.getMainMenuButton
		writeReturnFunctionCall(getMainMenuButtonFunctionFullName, params, "\t\t");

		writeFunctionFooter();
	}
	private void writeAddMainMenuButton() throws IOException {
		String idParamName = "id";
		String textParamName = "text";
		String toolTipParamName = "toolTip";
		String handlerParamName = "handler";

		writeFunctionHeader(ADD_MAIN_MENU_BUTTON_FUNCTION_NAME, idParamName, textParamName, toolTipParamName, handlerParamName);

		out.writeLn(
			"\t\t", ADD_BUTTON_FUNCTION_NAME, "( ",
			getFunctionSignature(GET_MAIN_MENU_BUTTON_FUNCTION_NAME, idParamName, textParamName, toolTipParamName, handlerParamName)
			," );"
		);

		writeFunctionFooter();
	}
	private void writeGetButtons() throws IOException {
		writeFunctionHeader(GET_BUTTONS_FUNCTION_NAME);

		out.writeLn("\t\t", BUTTONS_VAR_NAME,".length = 0;"); // .clear() throws an error if the array is empty
		out.writeLn();
		writeComment(APPEND_BUTTON_VARIABLE_MARKER, "\t\t");
		out.writeLn();
		writeFunctionReturn(BUTTONS_VAR_NAME);

		writeFunctionFooter();
	}

	private void writeReturn() throws IOException {
		out.writeLn("\treturn {");

		out.writeLn("\t\t", MAIN_MENU_INIT_FUNCTION_NAME,": function() {");

		JsHash params = new JsHash();
		params.put("title", TITLE_CONSTANT_NAME);
		params.put("buttons", getFunctionSignature(GET_BUTTONS_FUNCTION_NAME));

		String kefirMainMenuInitFunctionFullName = getFullName(KEFIR_MAIN_MENU_NAMESPACE, KEFIR_MAIN_MENU_INIT_FUNCTION_NAME);
		writeFunctionCall(kefirMainMenuInitFunctionFullName, params, "\t\t\t");

		out.writeLn("\t\t}"); // end init: function() {

		out.writeLn("\t}");
	}

	private ProjectConfig config;

	private static final String TITLE_CONSTANT_NAME = "TITLE";
	private static final String BUTTON_WIDTH_CONSTANT_NAME = "BUTTON_WIDTH";

	private static final String BUTTONS_VAR_NAME = "buttons";

	private static final String ADD_BUTTON_FUNCTION_NAME = "addButton";
	private static final String GET_MAIN_MENU_BUTTON_FUNCTION_NAME = "getMainMenuButton";
	public static final String ADD_MAIN_MENU_BUTTON_FUNCTION_NAME = "addMainMenuButton";
	private static final String GET_BUTTONS_FUNCTION_NAME = "getButtons";
}