/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.js;

import org.apache.log4j.Logger;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.fileWriter.FileWriter;

import java.io.File;
import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.getJsNamespace;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.*;
import static su.opencode.kefir.gen.fileWriter.js.JsUtils.*;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.FileUtils.createDirs;
import static su.opencode.kefir.util.StringUtils.*;

public abstract class JsFileWriter
{
	public JsFileWriter(String baseDir, String dir, String fileName) {
		this.baseDir = baseDir;
		this.dir = dir;
		this.fileName = fileName;
	}

	public void createFile() throws IOException {
		File file = createJsFile();
		if (file.exists())
		{ // todo: возможность внешним образом задавать перезапись существующих файлов
			if (failIfFileExists)
			{
				throw new IllegalStateException( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists") );
			}
			else
			{ // оставить существующий файл как есть
				logger.info( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists. It is not overwritten.") );
				return;
			}
		}

		out = new FileWriter(file, JS_FILE_ENCODING);

		try
		{
			writeFile();
		}
		finally
		{
			out.close();
		}
	}
	private File createJsFile() {
		String dirPath = concat(sb, baseDir, FILE_SEPARATOR, dir);
		createDirs(dirPath); // если директорий нет, создать их

		String filePath;
		if (fileName.endsWith(JS_FILE_EXTENSION)) // если имя файла уже содержит ".js", не добавлять его
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName);
		else
			filePath = concat(sb, dirPath, FILE_SEPARATOR, fileName, JS_FILE_EXTENSION); // добавить ".js" к имени файла

		return new File(filePath);
	}

	protected abstract void writeFile() throws IOException;

	protected void writeNamespace() throws IOException {
		out.writeLn(EXT_NAMESPACE_FUNCTION_NAME, METHOD_CALL_OPENING_BRACKET, getJsString( getJsNamespace(extEntity, entityClass) ), METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
		out.writeLn();
	}
	protected void writeNamespace(String namespace) throws IOException {
		out.writeLn(EXT_NAMESPACE_FUNCTION_NAME, METHOD_CALL_OPENING_BRACKET, getJsString(namespace), METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
		out.writeLn();
	}

	protected void writeNamespaceHeader(String namespace) throws IOException {
		out.writeLn(namespace, " ", ASSIGNMENT_OPERATOR, " ", FUNCTION_KEYWORD, METHOD_CALL_OPENING_BRACKET, METHOD_CALL_CLOSING_BRACKET, " ", FUNCTION_BODY_OPENING_BRACKET);
	}
	protected void writeNamespaceFooter() throws IOException {
		out.write(FUNCTION_BODY_CLOSING_BRACKET, METHOD_CALL_OPENING_BRACKET, METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
	}

	/**
	 * Пишет строковую константу 1-го уровня вложенности в функцию-неймспейс.
	 * @param name название константы
	 * @param value значение константы (без оборачивающих кавычек)
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeConstant(String name, String value) throws IOException {
		out.writeLn(TAB, VAR_KEYWORD, " ", name, " ", ASSIGNMENT_OPERATOR, " ", getJsString(value), SEMICOLON);
	}
	/**
	 * Пишет константу 1-го уровня вложенности с кавычками или без в функцию-неймспейс.
	 * @param name название константы
	 * @param value значение константы (без оборачивающих кавычек)
	 * @param quoted <code>true</code> — если нужно оборачивать значение кавычками, <code>false</code> — если значение нужно писать как есть
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeConstant(String name, String value, boolean quoted) throws IOException {
		if (quoted)
			writeConstant(name, value);
		else
			out.writeLn(TAB, VAR_KEYWORD, " ", name, " ", ASSIGNMENT_OPERATOR, " ", value, SEMICOLON);
	}
	/**
	 * Пишет целую константу 1-го уровня вложенности в функцию-неймспейс.
	 * @param name название константы
	 * @param value значение константы
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeConstant(String name, int value) throws IOException {
		out.writeLn(TAB, VAR_KEYWORD, " ", name, " ", ASSIGNMENT_OPERATOR, " ", Integer.toString(value), SEMICOLON);
	}
	/**
	 * Пишет объявление (без указания значения) переменной 1-го уровня вложенности в функцию-неймспейс.
	 * @param name имя переменной
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeVariable(String name) throws IOException {
		out.writeLn(TAB, VAR_KEYWORD, " ", name, SEMICOLON);
	}

	protected void writeFunctionHeader(String functionName, String... args) throws IOException {
		out.writeLn( getFunctionHeader(sb, functionName, args) );
	}
	protected void writeFunctionHeader(String functionName, int indentTabsCount, String... args) throws IOException {
		out.writeLn( getFunctionHeader(sb, functionName, indentTabsCount, args) );
	}

	protected void writeFunctionReturn(String value, String indent) throws IOException {
		out.writeLn(indent, getReturn(value, true));
	}
	protected void writeFunctionReturn(String value) throws IOException {
		writeFunctionReturn(value, DOUBLE_TAB);
	}
	protected void writeFunctionReturn(Object value, String indent) throws IOException {
		writeFunctionReturn(value.toString(), indent);
	}
	protected void writeFunctionReturn(Object value) throws IOException {
		writeFunctionReturn(value.toString());
	}

	public static String getReturn(String value, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();

		sb.append(RETURN_KEYWORD);

		if ( notEmpty(value) )
			sb.append(" ").append(value);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}

	protected void writeFunctionFooter(String indent) throws IOException {
		out.writeLn( getFunctionFooter(indent, false) );
	}
	protected void writeFunctionFooter() throws IOException {
		writeFunctionFooter(TAB);
	}
	protected void writeFunctionFooterWithSemicolon(String indent) throws IOException {
		out.writeLn( getFunctionFooter(indent, true) );
	}
	protected void writeFunctionFooterWithSemicolon() throws IOException {
		writeFunctionFooterWithSemicolon(""); // no indent by default
	}

	public static String getFunctionFooter(String indent, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(FUNCTION_BODY_CLOSING_BRACKET);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}

	protected void writeMethodCall(String indent, String objectName, String methodName, String... params) throws IOException {
		out.writeLn(indent, getMethodCall(objectName, methodName, params));
	}

	public static String getMethodCall(String objectName, String methodName, boolean addSemicolon, String... params) {
		String methodCall = getFunctionCall(methodName, addSemicolon, params);
		return concat(objectName, METHOD_CALL_SEPARATOR, methodCall);
	}
	public static String getMethodCall(String objectName, String methodName, String... params) {
		return getMethodCall(objectName, methodName, true, params);
	}

	protected void writeFunctionCall(String functionName, JsHash hash, String indent) throws IOException {
		out.writeLn(indent, functionName, METHOD_CALL_OPENING_BRACKET, hash.toStringAligned(indent), METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
	}
	protected void writeReturnFunctionCall(String functionName, JsHash hash, String indent) throws IOException {
		out.writeLn(indent, RETURN_KEYWORD, " ", functionName, METHOD_CALL_OPENING_BRACKET, hash.toStringAligned(indent), METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
	}

	public static String getFunctionCall(String functionName, boolean addSemicolon, String... params) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append(functionName).append(METHOD_CALL_OPENING_BRACKET);

		if (params.length > 0)
		{
			for (String param : params)
				sb.append(param).append(FUNCTION_PARAMS_SEPARATOR);

			sb.delete(sb.length() - FUNCTION_PARAMS_SEPARATOR.length(), sb.length());
		}

		sb.append(METHOD_CALL_CLOSING_BRACKET);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}
	public static String getFunctionCall(String functionName, String... params) {
		return getFunctionCall(functionName, true, params);
	}

	protected void writeFunctionCall(String indent, String functionName, String... params) throws IOException {
		out.write(indent, functionName, METHOD_CALL_OPENING_BRACKET);

		if (params.length > 0)
		{
			// first param with no ", " ahead
			out.write(params[0]);

			for (int i = 1; i < params.length; i++)
				out.write(FUNCTION_PARAMS_SEPARATOR, params[i]);
		}

		out.writeLn(METHOD_CALL_CLOSING_BRACKET, SEMICOLON);
	}

	protected void writeConstructorCall(String className, JsHash config, String indent) throws IOException {
		out.writeLn(indent, getConstructorCall(className, config, indent, true));
	}
	public static String getConstructorCall(String className, JsHash config, String indent, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();

		sb.append(NEW_KEYWORD).append(" ");
		sb.append(className);
		sb.append(METHOD_CALL_OPENING_BRACKET);

		if (config != null)
		{
			config.setAlignChildren(true);

			if ( notEmpty(indent) )
				sb.append( config.toStringAligned(indent) );
			else
				sb.append(config.toString());
		}

		sb.append(METHOD_CALL_CLOSING_BRACKET);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}

	protected void writeAssignment(String indent, String varName, String value) throws IOException {
		out.writeLn(indent, getAssignment(varName, value));
	}

	public static String getAssignment(String varName, String value, boolean addSemicolon) {
		return getAssignment(varName, ASSIGNMENT_OPERATOR, value, addSemicolon);
	}
	public static String getAssignment(String varName, String value) {
		return getAssignment(varName, value, true);
	}

	public static String getAssignment(String varName, String operator, String value, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();
		sb.append(varName).append(" ").append(operator).append(" ").append(value);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}
	protected String getAssignment(String varName, String operator, String value) {
		return getAssignment(varName, operator, value, true);
	}

	protected String getProperty(String objectName, String propertyName) {
		return concat(sb, objectName, METHOD_CALL_SEPARATOR, propertyName);
	}

	/**
	 * Пишет объявление переменной с указанным значением с указанным отступом, с двоеточием в конце.
	 * @param name имя переменной
	 * @param value значение переменной
	 * @param indent отступ (как правило, содержит один или несколько табов)
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeVariable(String name, String value, String indent) throws IOException {
		writeVariable(name, value, indent, true);
	}
	protected void writeVariable(String name, String value, String indent, boolean addSemicolon) throws IOException {
		String semicolon = addSemicolon ? SEMICOLON : "";
		out.writeLn(indent, VAR_KEYWORD, " ", name, " ", ASSIGNMENT_OPERATOR, " ", value, semicolon);
	}

	/**
	 * Пишет объявление переменной с указанным значением с {@linkplain JsUtils#DEFAULT_FUNCTION_VAR_INDENT отступом по умолчанию (2 таба)},
	 * с двоеточием в конце.
	 * @param name имя переменной
	 * @param value значение переменной
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeVariable(String name, String value) throws IOException {
		writeVariable(name, value, DEFAULT_FUNCTION_VAR_INDENT);
	}

	public static String getVariableDeclaration(String name, String value, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();
		sb.append(VAR_KEYWORD).append(" ").append(name).append(" ").append(ASSIGNMENT_OPERATOR).append(" ").append(value);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}
	public static String getVariableDeclaration(String name, String value) {
		return getVariableDeclaration(name, value, true);
	}

	protected void writeFunctionAssignmentHeader(String indent, String functionName, String... arguments) throws IOException {
		out.writeLn( getFunctionAssignmentHeader(indent, functionName, arguments) );
	}
	public static String getFunctionAssignmentHeader(String indent, String functionName, String... arguments)  {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(getAssignment(functionName, getAnonymousFunctionHeader(arguments), false) );

		return sb.toString();
	}

	/**
	 * Пишет комментарий с указанным отступом.
	 * @param comment значение комментария
	 * @param indent отступ (как правило, содержит один или несколько табов)
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeComment(String comment, String indent) throws IOException {
		out.writeLn(indent, ONE_STRING_COMMENT, " ", comment);
	}
	/**
	 * Пишет комментарий 1-го уровня вложенности (с одним табом впереди).
	 * @param comment значение комментария
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeComment(String comment) throws IOException {
		writeComment(comment, TAB);
	}

	protected void writeIf(String indent, String clause, boolean addSpaces) throws IOException {
		out.writeLn( getIf(indent, clause, addSpaces) );
	}
	protected void writeIf(String indent, String clause) throws IOException {
		out.writeLn( getIf(indent, clause, false) );
	}
	protected void writeNegationIf(String indent, String clause, boolean addSpaces) throws IOException {
		writeIf(indent, getNegation(clause), addSpaces);
	}
	protected void writeNegationIf(String indent, String clause) throws IOException {
		writeIf(indent, getNegation(clause));
	}

	protected void writeIfHeader(String indent, String clause, boolean addSpaces) throws IOException {
		writeIf(indent, clause, addSpaces);
		out.writeLn(indent, BLOCK_OPENING_BRACKET);
	}
	protected void writeIfHeader(String indent, String clause) throws IOException {
		writeIfHeader(indent, clause, false);
	}
	protected void writeNegationIfHeader(String indent, String clause, boolean addSpaces) throws IOException {
		writeIfHeader(indent, getNegation(clause), addSpaces);
	}
	protected void writeNegationIfHeader(String indent, String clause) throws IOException {
		writeIfHeader(indent, getNegation(clause));
	}

	protected void writeIfFooter(String indent) throws IOException {
		out.writeLn(indent, BLOCK_CLOSING_BRACKET);
	}

	public static String getIf(String indent, String clause, boolean addSpaces) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(IF_KEYWORD).append(" ");
		sb.append(OPENING_BRACKET);

		if (addSpaces)
			sb.append(" ");

		sb.append(clause);

		if (addSpaces)
			sb.append(" ");

		sb.append(CLOSING_BRACKET);
		return sb.toString();
	}

	protected void writeElse(String indent) throws IOException {
		out.writeLn(indent, ELSE_KEYWORD);
	}

	protected void writeElseHeader(String indent) throws IOException {
		writeElse(indent);
		out.writeLn(indent, BLOCK_OPENING_BRACKET);
	}

	protected void writeElseFooter(String indent) throws IOException {
		out.writeLn(indent, BLOCK_CLOSING_BRACKET);
	}

	// todo: "else if" branch writing

	public static String getNegation(String value) {
		return concat(NEGATION_OPERATOR, value);
	}

	public static String getComparison(String value1, String operator, String value2) {
		return getComparison(value1, operator, value2, false);
	}
	public static String getComparison(String value1, String operator, String value2, boolean addBrackets) {
		StringBuilder sb = new StringBuilder();

		if (addBrackets)
			sb.append(OPENING_BRACKET);

		sb.append(value1).append(" ").append(operator).append(" ").append(value2);

		if (addBrackets)
			sb.append(CLOSING_BRACKET);

		return sb.toString();
	}

	protected void writeVarModifying(String indent, String varName, String operator, boolean addSemicolon) throws IOException {
		out.writeLn(getVarModifying(indent, varName, operator, addSemicolon));
	}
	protected void writeVarModifying(String indent, String varName, String operator) throws IOException {
		writeVarModifying(indent, varName, operator, true);
	}

	protected void writeVarInc(String indent, String varName, boolean addSemicolon) throws IOException {
		writeVarModifying(indent, varName, INC_OPERATOR, addSemicolon);
	}
	protected void writeVarInc(String indent, String varName) throws IOException {
		writeVarInc(indent, varName, true);
	}

	protected void writeVarDec(String indent, String varName, boolean addSemicolon) throws IOException {
		writeVarModifying(indent, varName, DEC_OPERATOR, addSemicolon);
	}
	protected void writeVarDec(String indent, String varName) throws IOException {
		writeVarDec(indent, varName, true);
	}

	public static String getVarModifying(String indent, String varName, String operator, boolean addSemicolon) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(varName);
		sb.append(operator);

		if (addSemicolon)
			sb.append(SEMICOLON);

		return sb.toString();
	}

	protected void writeSwitchHeader(String indent, String clause, boolean addSpaces) throws IOException {
		out.writeLn(getSwitch(indent, clause, addSpaces));
		out.writeLn(indent, BLOCK_OPENING_BRACKET);
	}
	protected void writeSwitchHeader(String indent, String clause) throws IOException {
		writeSwitchHeader(indent, clause, false);
	}

	protected void writeSwitchFooter(String indent) throws IOException {
		out.writeLn(indent, BLOCK_CLOSING_BRACKET);
	}

	public static String getSwitch(String indent, String clause, boolean addSpaces) throws IOException {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(SWITCH_KEYWORD)
		  .append(" ")
		  .append(OPENING_BRACKET);

		if (addSpaces)
			sb.append(" ");

		sb.append(clause);

		if (addSpaces)
			sb.append(" ");

		sb.append(CLOSING_BRACKET);
		return sb.toString();
	}

	protected void writeCase(String indent, String key, String caseBody, boolean addBreak) throws IOException {
		out.writeLn( getCase(indent, key, caseBody, addBreak) );
	}
	protected void writeCase(String indent, String key, String caseBody) throws IOException {
		out.writeLn( getCase(indent, key, caseBody, true) );
	}
	protected void writeCaseReturn(String indent, String key, String valueToReturn) throws IOException {
		out.writeLn( getCase(indent, key, getReturn(valueToReturn, false), false) );
	}

	public static String getCase(String indent, String key, String caseBody, boolean addBreak) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(CASE_KEYWORD).append(" ").append(key).append(CASE_KEY_VALUE_SEPARATOR).append(" ");

		sb.append(caseBody);
		if ( !caseBody.endsWith(SEMICOLON) )
			sb.append(SEMICOLON);

		if (addBreak)
			sb.append(" ").append(BREAK_KEYWORD).append(SEMICOLON);

		return sb.toString();
	}

	protected void writeDefault(String indent, String defaultBody) throws IOException {
		out.writeLn( getDefault(indent, defaultBody) );
	}
	protected void writeDefaultReturn(String indent, String valueToReturn) throws IOException {
		out.writeLn( getDefault(indent, getReturn(valueToReturn, false)) );
	}

	public static String getDefault(String indent, String defaultBody) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(indent) )
			sb.append(indent);

		sb.append(DEFAULT_KEYWORD).append(CASE_KEY_VALUE_SEPARATOR).append(" ");

		sb.append(defaultBody);
		if ( !defaultBody.endsWith(SEMICOLON) )
			sb.append(SEMICOLON);

		return sb.toString();
	}

	/**
	 * Пишет ветвь case return: возврат указанного значения по указанному значению указанного энума.
	 * @param enumName название класса энума
	 * @param enumValue название значения энума
	 * @param returnValue возвращаемое из ветви значение
	 * @param indent отступ (как правило, содержит один или несколько табов)
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeCaseReturn(String enumName, String enumValue, String returnValue, String indent) throws IOException {
		out.writeLn(indent, CASE_KEYWORD, " ", enumName, ".", enumValue, ": ", RETURN_KEYWORD, " ", returnValue, SEMICOLON);
	}
	/**
	 * Пишет ветвь case return: возврат указанного значения по указанному значению энума,
	 * заданного {@linkplain #ACTION_CONSTANT_NAME константой по умолчанию},
	 * с {@linkplain JsUtils#DEFAULT_CASE_INDENT отступом по умолчанию}.
	 * @param enumValue название значения энума
	 * @param returnValue возвращаемое из ветви значение
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeCaseReturn(String enumValue, String returnValue) throws IOException {
		writeCaseReturn(ACTION_CONSTANT_NAME, enumValue, returnValue, DEFAULT_CASE_INDENT);
	}

	/**
	 * Пишет функцию, выполняющую case\return по энуму ACTION.
	 * @param functionName название функции
	 * @param actionVarName название переменной, по которой выполняется switch
	 * @param constants массив, каждым элементом которого является массив из 2 элементов:
	 * 0-й элемент — это название константы из энума ACTION, 1-й элемент — это возвращаемое из ветви case значение.
	 * @param defaultAction действие по умолчанию (без последней ;)
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeSwitchReturnFunction(String functionName, String actionVarName, String[][] constants, String defaultAction) throws IOException {
		writeFunctionHeader(functionName);

		out.writeLn(DOUBLE_TAB, SWITCH_KEYWORD, " ", METHOD_CALL_OPENING_BRACKET, actionVarName, METHOD_CALL_CLOSING_BRACKET);
		out.writeLn(DOUBLE_TAB, FUNCTION_BODY_OPENING_BRACKET);

		for (String[] constant : constants)
			writeCaseReturn(constant[0], constant[1]);

		out.writeLn("");
		out.writeLn(TRIPLE_TAB, DEFAULT_KEYWORD, ": ", defaultAction, SEMICOLON);
		out.writeLn(DOUBLE_TAB, FUNCTION_BODY_CLOSING_BRACKET); // end of switch

		writeFunctionFooter();
	}
	/**
	 * Пишет функцию, выполняющую case\return по энуму ACTION, со стандартным действием по умолчанию (Kefir.showError).
	 * Название переменной, по которой берется свитч, использвуется по умолчанию {@linkplain JsUtils#ACTION_VAR_NAME action}.
	 * @param functionName название функции
	 * @param constants массив, каждым элементом которого является массив из 2 элементов:
	 * 0-й элемент — это название константы из энума ACTION, 1-й элемент — это возвращаемое из ветви case значение.
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeSwitchReturnFunction(String functionName, String[][] constants) throws IOException {
		writeFunctionHeader(functionName);

		out.writeLn(DOUBLE_TAB, SWITCH_KEYWORD, " ", METHOD_CALL_OPENING_BRACKET, ACTION_VAR_NAME, METHOD_CALL_CLOSING_BRACKET);
		out.writeLn(DOUBLE_TAB, FUNCTION_BODY_OPENING_BRACKET);

		for (String[] constant : constants)
			writeCaseReturn(constant[0], constant[1]);

		out.writeLn("");

		String errorStringPrefix = getJsString( concat(sb, "Incorrect action for ", functionName, ": ") );
		String errorString = concat(sb, errorStringPrefix, " + ", ACTION_VAR_NAME);
		out.writeLn(TRIPLE_TAB, DEFAULT_KEYWORD, ": "
			, getFunctionCall(SHOW_ERROR_FUNCTION_NAME, errorString)
			, " "
			, RETURN_KEYWORD, " ", NULL_KEYWORD, SEMICOLON
		);

		out.writeLn(DOUBLE_TAB, FUNCTION_BODY_CLOSING_BRACKET); // end of switch

		writeFunctionFooter();
	}

	protected boolean failIfFileExists = true;

	protected String baseDir;
	protected String dir;
	protected String fileName;

	protected ExtEntity extEntity;
	protected Class entityClass;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	protected static final Logger logger = Logger.getLogger(JsFileWriter.class);

	public static final String JS_FILE_ENCODING = "UTF8";
	public static final String JS_FILE_ENCODING_FOR_INCLUDE = "UTF-8";
	public static final String JS_FILE_EXTENSION = ".js";

	public static final String NEW_KEYWORD = "new";
	public static final String FUNCTION_KEYWORD = "function";
	public static final String RETURN_KEYWORD = "return";
	public static final String VAR_KEYWORD = "var";
	public static final String IF_KEYWORD = "if";
	public static final String ELSE_KEYWORD = "else";
	public static final String SWITCH_KEYWORD = "switch";
	public static final String CASE_KEYWORD = "case";
	public static final String CASE_KEY_VALUE_SEPARATOR = ":";
	public static final String DEFAULT_KEYWORD = "default";
	public static final String BREAK_KEYWORD = "break";
	public static final String NULL_KEYWORD = "null";

	public static final String FUNCTION_PARAMS_SEPARATOR = ", ";
	public static final String FUNCTION_BODY_OPENING_BRACKET = "{";
	public static final String FUNCTION_BODY_CLOSING_BRACKET = "}";
	public static final String METHOD_CALL_SEPARATOR = ".";
	public static final String METHOD_CALL_OPENING_BRACKET = "(";
	public static final String METHOD_CALL_CLOSING_BRACKET = ")";
	public static final String ASSIGNMENT_OPERATOR = "=";
	public static final String AND_ASSIGNMENT_OPERATOR = "&=";
	public static final String OR_ASSIGNMENT_OPERATOR = "|=";
	public static final String NEGATION_OPERATOR = "!";
	public static final String SEMICOLON = ";";

	public static final String EQUALS_OPERATOR = "==";
	public static final String NOT_EQUALS_OPERATOR = "!=";
	public static final String MORE_OPERATOR = ">";
	public static final String MORE_OR_EQUALS_OPERATOR = ">=";
	public static final String LESS_OPERATOR = "<";
	public static final String LESS_OR_EQUALS_OPERATOR = "<=";

	public static final String AND_OPERATOR = "&&";
	public static final String OR_OPERATOR = "||";

	public static final String INC_OPERATOR = "++";
	public static final String DEC_OPERATOR = "--";

	public static final String ONE_STRING_COMMENT = "//";
	public static final String OPENING_BRACKET = "("; // for "if ()" etc
	public static final String CLOSING_BRACKET = ")"; // for "if ()" etc

	public static final String BLOCK_OPENING_BRACKET = "{"; // for "if () {" etc
	public static final String BLOCK_CLOSING_BRACKET = "}"; // for "if () ... }" etc

	// Extjs js objects
	public static final String EXT_NAMESPACE_FUNCTION_NAME = "Ext.namespace";
	public static final String EXT_APPLY_FUNCTION_NAME = "Ext.apply";
	public static final String EXT_EMPTY_FUNCTION_NAME = "Ext.emptyFn";
	public static final String EXT_ENCODE_FUNCTION_NAME = "Ext.encode";
	public static final String EXT_DECODE_FUNCTION_NAME = "Ext.decode";
	public static final String EXT_ALERT_FUNCTION_NAME = "Ext.Msg.alert";
	public static final String EXT_GET_FUNCTION_NAME = "Ext.get";
	public static final String EXT_GET_CMP_FUNCTION_NAME = "Ext.getCmp";
	public static final String EXT_ADD_CLASS_FUNCTION_NAME = "addCls";
	public static final String EXT_REMOVE_CLASS_FUNCTION_NAME = "removeCls";

	public static final String EXT_ELEMENT_IS_VALID_FUNCTION_NAME = "isValid";
	public static final String EXT_ELEMENT_GET_VALUE_FUNCTION_NAME = "getValue";
	public static final String EXT_ELEMENT_SET_VALUE_FUNCTION_NAME = "setValue";
	public static final String EXT_ELEMENT_ADD_LISTENER_FUNCTION_NAME = "addListener";

	public static final String EXT_CMP_GET_EL_FUNCTION_NAME = "getEl";

	public static final String EXT_ARRAY_STORE_CLASS_NAME = "Ext.data.ArrayStore";
	public static final String EXT_TOOLBAR_CLASS_NAME = "Ext.Toolbar";
	public static final String EXT_WINDOW_CLASS_NAME = "Ext.Window";
	public static final String EXT_FORM_PANEL_CLASS_NAME = "Ext.form.Panel";
	public static final String EXT_PANEL_CLASS_NAME = "Ext.Panel";
	public static final String EXT_BUTTON_CLASS_NAME = "Ext.Button";
	public static final String EXT_LABEL_CLASS_NAME = "Ext.form.Label";
	public static final String EXT_BLANK_IMAGE_URL_CONSTANT_NAME = "Ext.BLANK_IMAGE_URL";
	public static final String EXT_ON_READY_FUNCTION_NAME = "Ext.onReady";
	public static final String EXT_QUICK_TIPS_INIT_FUNCTION_NAME = "Ext.QuickTips.init";
	public static final String EXT_INPUT_TEXT_MASK_CLASS_NAME = "Ext.ux.InputTextMask";

	public static final String EXT_TEXT_FIELD_CLASS_NAME = "Ext.form.TextField";
	public static final String EXT_HIDDEN_FIELD_CLASS_NAME = "Ext.form.Hidden";
	public static final String EXT_CHECKBOX_FIELD_CLASS_NAME = "Ext.form.Checkbox";
	public static final String EXT_TEXT_AREA_CLASS_NAME = "Ext.form.TextArea";

	public static final String EXT_CLICK_EVENT_NAME = "click";

	// Kefir js objects
	public static final String CONTEXT_PATH_CONSTANT_NAME = "Kefir.contextPath";
	public static final String INIT_DYNAMIC_GRID_FUNCTION_NAME = "Kefir.initDynamicGrid";
	public static final String RELOAD_DATA_STORE_FUNCTION_NAME = "Kefir.reload";
	public static final String SEARCH_FIELD_CLASS_NAME = "Kefir.form.SearchField";
	public static final String SHOW_ERROR_FUNCTION_NAME = "Kefir.showError";

	public static final String GET_BUTTON_FUNCTION_NAME = "Kefir.form.getButton";
	public static final String SELECT_GRID_PANEL_RECORD_FUNCTION_NAME = "Kefir.selectGridPanelRecord";
	public static final String WINDOW_TITLE_FILTERS_SEPARATOR_CONSTANT_NAME = "Kefir.form.WINDOW_TITLE_FILTERS_SEPARATOR";
	public static final String TOOLBAR_TBFILL_VALUE = "->";
	public static final String TOOLBAR_SEPARATOR = "-";

	public static final String AJAX_REQUEST_FUNCTION_NAME = "Kefir.ajaxRequest";
	public static final String GET_ID_FUNCTION_NAME = "Kefir.getId";
	public static final String GET_VALUE_FUNCTION_NAME = "Kefir.getValue";
	public static final String GET_DATE_FUNCTION_NAME = "Kefir.getDate";
	public static final String ENCODE_FUNCTION_NAME = "Kefir.encode";
	public static final String BIND_MASK_FUNCTION_NAME = "Kefir.bindMask";
	public static final String GET_NEGATIVE_CHECKBOX_PARAMS_FUNCTION_NAME = "Kefir.form.getNegativeCheckboxParams";
	public static final String ATTACHMENTS_LIST_URL_CONSTANT_NAME = "Kefir.form.ATTACHMENTS_LIST_URL";
	public static final String SHOW_FORM_RESPONSE_MESSAGE_CONSTANT_NAME = "Kefir.showFormResponseMessage";

	public static final String ACTION_CONSTANT_NAME = "ACTION";
	public static final String COMMON_ACTION_CONSTANT_NAME = "Kefir.form.ACTION";
	public static final String ACTION_CREATE_CONSTANT_NAME = "CREATE";
	public static final String ACTION_SHOW_CONSTANT_NAME = "SHOW";
	public static final String ACTION_UPDATE_CONSTANT_NAME = "UPDATE";
	public static final String ACTION_DELETE_CONSTANT_NAME = "DELETE";

	public static final String FORM_PANEL_CLASS_NAME = "Kefir.FormPanel";

	public static final String FILL_FORM_FIELDS_FUNCTION_NAME = "Kefir.form.fillFormFields";
	public static final String FILL_FORM_FIELD_FUNCTION_NAME = "Kefir.form.fillFormField";
	public static final String FOCUS_NEXT_FUNCTION_NAME = "Kefir.focusNext";
	public static final String CHECK_FORM_FUNCTION_NAME = "Kefir.checkForm";

	public static final String GET_HIDDEN_FIELD_FUNCTION_NAME = "Kefir.form.getHiddenField";
	public static final String GET_TEXT_FIELD_FUNCTION_NAME = "Kefir.form.getTextField";
	public static final String GET_TEXT_AREA_FIELD_FUNCTION_NAME = "Kefir.form.getTextArea";
	public static final String GET_OGRN_TEXT_FIELD_FUNCTION_NAME = "Kefir.form.getOgrnTextField";
	public static final String GET_KPP_TEXT_FIELD_FUNCTION_NAME = "Kefir.form.getKppTextField";
	public static final String GET_INN_JURIDICAL_TEXT_FIELD_FUNCTION_NAME = "Kefir.form.getInnJuridicalTextField";

	public static final String GET_NUMBER_FIELD_FUNCTION_NAME = "Kefir.form.getNumberField";
	public static final String GET_SPINNER_FIELD_FUNCTION_NAME = "Kefir.form.getSpinnerField";
	public static final String GET_DATE_FIELD_FUNCTION_NAME = "Kefir.form.getDateField";
	public static final String GET_CHECKBOX_FUNCTION_NAME = "Kefir.form.getCheckbox";

	public static final String GET_LOCAL_COMBO_BOX_FUNCTION_NAME = "Kefir.form.getLocalComboBox";
	public static final String GET_COMBO_BOX_FUNCTION_NAME = "Kefir.form.getComboBox";
	public static final String SET_COMBO_BOX_VALUE_FUNCTION_NAME = "Kefir.form.setComboBoxValue";

	public static final String GET_MULTI_UPLOAD_PANEL_FUNCTION_NAME = "Kefir.form.getMultiUploadPanel";

	public static final String GET_FORM_PANEL_FUNCTION_NAME = "Kefir.form.getFormPanel";
	public static final String GET_COLUMN_PANEL_FUNCTION_NAME = "Kefir.form.getColumnPanel";

	public static final String RENDERER_NAMESPACE = "Kefir.render";
	public static final String VIEW_CONFIG_NAMESPACE = "Kefir.viewConfig";
	public static final String VTYPE_NAMESPACE = "Kefir.vtype";

	public static final String KEFIR_MAIN_MENU_NAMESPACE = "Kefir.mainMenu.MainMenu";
	public static final String KEFIR_MAIN_MENU_INIT_FUNCTION_NAME = "init";
	public static final String KEFIR_MAIN_MENU_GET_MAIN_MENU_BUTTON_FUNCTION_NAME = "getMainMenuButton";
}