/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.METHOD_CALL_SEPARATOR;
import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Оболочка для заголовка java-метода.
 */
public class Method
{
	public Method(AccessModifier accessModifier, String returnType, String name) {
		this.accessModifier = accessModifier;
		this.returnType = returnType;
		this.name = name;

		this.argumentNames = new HashSet<String>();
		this.arguments = new ArrayList<MethodArgument>();
		this.throwsList = new ArrayList<String>();
	}
	public Method(String name) {
		this(AccessModifier.public_, VOID_RETURN_TYPE, name);
	}
	public Method(String returnType, String name) {
		this(AccessModifier.public_, returnType, name);
	}

	public void clear() {
		argumentNames.clear();
		arguments.clear();
		throwsList.clear();
	}

	public void addArgument(String type, String name) {
		if (argumentNames.contains(name))
			return;

		argumentNames.add(name);
		arguments.add(new MethodArgument(type, name));
	}
	public void addArgument(Class type, String name) {
		addArgument(type.getSimpleName(), name);
	}

	public void addThrows(String exceptionClassName) {
		throwsList.add(exceptionClassName);
	}
	public void addThrows(Class exceptionClass) {
		addThrows(exceptionClass.getSimpleName());
	}

	public String startMethod() {
		sb.delete(0, sb.length());

		sb.append(accessModifier.getModifier()).append(" ").append(returnType).append(" ").append(name).append(ARGUMENTS_LIST_OPEN_BRACKET);

		if (!arguments.isEmpty())
		{
			for (MethodArgument argument : arguments)
				sb.append(argument.getType()).append(" ").append(argument.getName()).append(METHOD_ARGUMENTS_SEPARATOR);

			sb.delete(sb.length() - METHOD_ARGUMENTS_SEPARATOR.length(), sb.length());
		}

		sb.append(ARGUMENTS_LIST_CLOSE_BRACKET);

		if (throwsList != null && !throwsList.isEmpty())
		{
			sb.append(" ").append(THROWS_KEYWORD).append(" ");

			for (String exceptionName : throwsList)
				sb.append(exceptionName).append(THROWS_LIST_SEPARATOR);

			sb.delete(sb.length() - THROWS_LIST_SEPARATOR.length(), sb.length());
		}

		sb.append(" ").append(METHOD_BODY_OPEN_BRACKET);
		return sb.toString();
	}
	public String endMethod() {
		return METHOD_BODY_CLOSE_BRACKET;
	}

	public static String getMethodCall(String methodName, boolean addSemicolon, String... arguments) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append(methodName).append(ARGUMENTS_LIST_OPEN_BRACKET);

		if (arguments.length > 0)
		{
			for (String argument : arguments)
				sb.append(argument).append(METHOD_ARGUMENTS_SEPARATOR);

			sb.delete(sb.length() - METHOD_ARGUMENTS_SEPARATOR.length(), sb.length());
		}

		sb.append(ARGUMENTS_LIST_CLOSE_BRACKET);

		if (addSemicolon)
			sb.append(";");

		return sb.toString();
	}
	public static String getMethodCall(String methodName, String... arguments) {
		return getMethodCall(methodName, true, arguments);
	}

	public static String getMethodCallName(String varName, String methodName) {
		return concat(varName, METHOD_CALL_SEPARATOR, methodName);
	}
	public static String getStaticMethodFullName(Class containingClass, String methodName) {
		return getMethodCallName(containingClass.getSimpleName(), methodName);
	}
	public static String getStaticMethodFullName(String className, String methodName) {
		return getMethodCallName(className, methodName);
	}

	public static String getStaticMethodCall(String className, String methodName, boolean addSemicolon, String... arguments) {
		String staticMethodName = getStaticMethodFullName(className, methodName);
		return getMethodCall(staticMethodName, addSemicolon, arguments);
	}
	public static String getStaticMethodCall(Class classContainingStaticMethod, String methodName, boolean addSemicolon, String... arguments) {
		return getStaticMethodCall( classContainingStaticMethod.getSimpleName(), methodName, addSemicolon, arguments );
	}

	/**
	 * Модификатор доступа.
	 */
	private AccessModifier accessModifier;

	/**
	 * Тип возврврата
	 */
	private String returnType;

	/**
	 * Имя метода.
	 */
	private String name;

	/**
	 * Множество имен аргументов.
	 */
	private Set<String> argumentNames;

	/**
	 * Список аргументов (содержит тип и имя аргумента).
	 */
	private List<MethodArgument> arguments;

	/**
	 * Список выбрасываемых исключений.
	 */
	private List<String> throwsList;

	private StringBuffer sb = new StringBuffer();

	public static final String VOID_RETURN_TYPE = "void";
	public static final String THROWS_KEYWORD = "throws";
	public static final String ARGUMENTS_LIST_OPEN_BRACKET = "(";
	public static final String ARGUMENTS_LIST_CLOSE_BRACKET = ")";
	public static final String METHOD_ARGUMENTS_SEPARATOR = ", ";
	public static final String THROWS_LIST_SEPARATOR = ", ";
	public static final String METHOD_BODY_OPEN_BRACKET = "{";
	public static final String METHOD_BODY_CLOSE_BRACKET = "}";
}