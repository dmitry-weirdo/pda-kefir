/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.js;

import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.*;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.*;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public class JsUtils
{
	public static String getFunctionHeader(StringBuffer sb, String functionName, int indentTabsCount, String... arguments) {
		sb.delete(0, sb.length());

		for (int i = 0; i < indentTabsCount; i++)
			sb.append(TAB);

		sb.append(FUNCTION_KEYWORD);

		if ( notEmpty(functionName) ) // allow anonymous (no-name) functions
			sb.append(" ").append(functionName);

		sb.append(METHOD_CALL_OPENING_BRACKET);

		if (arguments.length > 0)
		{
			for (String arg : arguments)
			{
				sb.append(arg).append(FUNCTION_PARAMS_SEPARATOR);
			}
			sb.delete(sb.length() - FUNCTION_PARAMS_SEPARATOR.length(), sb.length()); // remove trailing ", "
		}

		sb.append(METHOD_CALL_CLOSING_BRACKET).append(" ").append(FUNCTION_BODY_OPENING_BRACKET);
		return sb.toString();
	}

	/**
	 * @param arguments список аргументов функции
	 * @return заголовок анонимной (без имени) js-функции без табов впереди.
	 */
	public static String getAnonymousFunctionHeader(String... arguments) {
		return getFunctionHeader(new StringBuffer(), null, 0, arguments);
	}

	public static String getSimpleAnonymousFunction(String functionBody, String... arguments) {
		StringBuilder sb = new StringBuilder();

		sb.append( getAnonymousFunctionHeader(arguments) )
		  .append(" ")
		  .append(functionBody)
		  .append(" ")
		  .append(FUNCTION_BODY_CLOSING_BRACKET);

		return sb.toString();
	}

	public static String getFunctionSignature(String functionName, String... arguments) {
		StringBuilder sb = new StringBuilder();

		sb.append(functionName).append(METHOD_CALL_OPENING_BRACKET);

		if (arguments.length > 0)
		{
			for (String arg : arguments)
			{
				sb.append(arg).append(FUNCTION_PARAMS_SEPARATOR);
			}
			sb.delete(sb.length() - FUNCTION_PARAMS_SEPARATOR.length(), sb.length()); // remove trailing ", "
		}

		sb.append(METHOD_CALL_CLOSING_BRACKET);
		return sb.toString();
	}

	public static String getFunctionHeader(String functionName, int indentTabsCount, String... arguments) {
		return getFunctionHeader(new StringBuffer(), functionName, indentTabsCount, arguments);
	}
	public static String getFunctionHeader(String functionName, String... arguments) { // заголовок функции с отступом по умолчанию (один таб)
		return getFunctionHeader(new StringBuffer(), functionName, DEFAULT_FUNCTION_INDENT_TABS_COUNT, arguments);
	}
	public static String getFunctionHeader(StringBuffer sb, String functionName, String... arguments) { // заголовок функции с отступом по умолчанию (один таб)
		return getFunctionHeader(sb, functionName, DEFAULT_FUNCTION_INDENT_TABS_COUNT, arguments);
	}

	public static final String ACTION_VAR_NAME = "action";

	public static final int DEFAULT_FUNCTION_INDENT_TABS_COUNT = 1;
	public static final String DEFAULT_FUNCTION_VAR_INDENT = DOUBLE_TAB;
	public static final String DEFAULT_CASE_INDENT = TRIPLE_TAB;
}