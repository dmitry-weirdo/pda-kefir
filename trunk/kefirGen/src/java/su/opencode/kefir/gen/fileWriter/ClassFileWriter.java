/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.apache.commons.lang.StringEscapeUtils.escapeJava;
import static su.opencode.kefir.gen.ExtEntityUtils.isInPackage;
import static su.opencode.kefir.gen.fileWriter.Method.*;
import static su.opencode.kefir.util.FileUtils.FILE_SEPARATOR;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.ObjectUtils.getSetterName;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public abstract class ClassFileWriter
{
	public ClassFileWriter() {
	}
	public ClassFileWriter(String baseDir, String packageName, String className) {
		this.baseDir = baseDir;
		this.packageName = packageName;
		this.className = className;
	}

	public void createFile() throws IOException {
		File file = createClassFile(baseDir, packageName, className);
		if (file.exists())
		{ // todo: correct handling
			if (failIfFileExists)
			{
				throw new IllegalStateException( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists") );
			}
			else
			{ // оставить существующий файл как есть
				final String overwritten = overwriteIfFileExists ? "overwritten" : "not overwritten";
				logger.info( concat(sb, "file \"", file.getAbsolutePath(), "\" already exists. It is ", overwritten, ".") );

				if (!overwriteIfFileExists)
					return;
			}
		}

		out = new FileWriter(file, CLASS_FILE_ENCODING);

		try
		{
			writeSvnHeader();

			writePackage();
			out.writeLn();

			writeImports();
			out.writeLn();

			writeClassBody();
		}
		finally
		{
			out.close();
		}
	}

	protected void writeSvnHeader() throws IOException { // todo: этот заголовок нужно брать из внешнего файла, а не жестко прописывать в коде
		out.writeLn("/**");
		out.writeLn(" Copyright 2013 LLC \"Open Code\"");
		out.writeLn(" http://www.o-code.ru");
		out.writeLn(" $", "HeadURL", "$"); // concat чтобы svn не заменял этот заголовок на значения класса ClassFileWriter
		out.writeLn(" $", "Author", "$");
		out.writeLn(" $", "Revision", "$");
		out.writeLn(" $", "Date::                      ", "$");
		out.writeLn(" */");
	}
	protected void writePackage() throws IOException {
		out.writeLn(PACKAGE_KEYWORD, " ", packageName, SEMICOLON);
	}
	protected abstract void writeImports() throws IOException;

	protected final void writeImport(Class classToImport) throws IOException {
		writeImport(classToImport.getName());
	}
	protected final void writeImport(String... stringsToConcatenate) throws IOException {
		sb.delete(0, sb.length());
		for (String s : stringsToConcatenate)
			sb.append(s);

		writeImport(sb.toString());
	}
	protected final void writeImport(String className) throws IOException {
		if ( isInPackage(className, packageName) || isInPackage(className, JAVA_LANG_PACKAGE_NAME) )
			return; // если импортируемый класс находится в том же пакете, то импорт добавлять не надо

		out.writeLn( getImport(sb, className) );
	}
	public static String getImport(StringBuffer sb, String className) {
		return concat(sb, IMPORT_KEYWORD, " ", className, SEMICOLON);
	}

	protected final void writeImportAll(Class classToImport) throws IOException {
		writeImportAll(classToImport.getName());
	}
	protected final void writeImportAll(String className) throws IOException {
		if ( isInPackage(className, packageName) )
			return; // если импортируемый класс находится в том же пакете, то импорт добавлять не надо

		out.writeLn( getImportAll(sb, className) );
	}
	public static String getImportAll(StringBuffer sb, String className) {
		return concat(sb, IMPORT_KEYWORD, " ", className, METHOD_CALL_SEPARATOR, ALL_MEMBERS_IMPORT, SEMICOLON);
	}

	protected final void writeStaticImportAll(Class classToImport) throws IOException {
		writeStaticImportAll(classToImport.getName());
	}
	protected final void writeStaticImportAll(String className) throws IOException {
		if ( isInPackage(className, packageName) )
			return; // если импортируемый класс находится в том же пакете, то импорт добавлять не надо

		out.writeLn( getStaticImportAll(sb, className) );
	}
	public static String getStaticImportAll(StringBuffer sb, String className) {
		return concat(sb, IMPORT_STATIC, " ", className, METHOD_CALL_SEPARATOR, ALL_MEMBERS_IMPORT, SEMICOLON);
	}

	public static String getStaticImport(StringBuffer sb, String className, String methodName) {
		return concat(sb, IMPORT_STATIC, " ", className, METHOD_CALL_SEPARATOR, methodName, SEMICOLON);
	}

	protected final void writeStaticImport(String className) throws IOException {
		out.writeLn(IMPORT_STATIC, " ", className, SEMICOLON);
	}
	protected final void writeStaticImport(String className, String methodName) throws IOException {
		out.writeLn( getStaticImport(sb, className, methodName) );
	}
	protected final void writeStaticImport(String packageName, String classSimpleName, String methodName) throws IOException {
		String classFullName = concat(sb, packageName, METHOD_CALL_SEPARATOR, classSimpleName);
		out.writeLn( getStaticImport(sb, classFullName, methodName) );
	}
	protected final void writeStaticImport(Class classToImport, String methodName) throws IOException {
		writeStaticImport(classToImport.getName(), methodName);
	}

	protected void writeClassHeader() throws IOException {
		out.writeLn(PUBLIC_CLASS, " ", className);
		out.writeLn(BLOCK_OPENING_BRACKET);
	}
	protected void writeClassHeader(String parentClassName) throws IOException {
		out.writeLn(PUBLIC_CLASS, " ", className, " ", EXTENDS_KEYWORD, " ", parentClassName);
		out.writeLn(BLOCK_OPENING_BRACKET);
	}
	protected void writeClassHeader(Class parentClass) throws IOException {
		writeClassHeader(parentClass.getSimpleName());
	}
	protected void writeImplementsClassHeader(String interfaceClassName) throws IOException {
		out.writeLn(PUBLIC_CLASS, " ", className, " ", IMPLEMENTS_KEYWORD, " ", interfaceClassName);
		out.writeLn(BLOCK_OPENING_BRACKET);
	}
	protected void writeImplementsClassHeader(Class interfaceClassName) throws IOException {
		writeImplementsClassHeader(interfaceClassName.getSimpleName());
	}

	protected void writeInterfaceHeader() throws IOException {
		out.writeLn(PUBLIC_INTERFACE, " ", className);
		out.writeLn(BLOCK_OPENING_BRACKET);
	}

	protected void writeClassFooter() throws IOException {
		out.write(BLOCK_CLOSING_BRACKET);
	}

	protected void writeFieldDeclaration(String fieldName, String type) throws IOException {
		out.writeLn(TAB, PRIVATE_KEYWORD, " ", type, " ", fieldName, SEMICOLON);
	}
	protected void writeFieldDeclaration(String fieldName, Class fieldClass) throws IOException {
		writeFieldDeclaration(fieldName, fieldClass.getSimpleName());
	}
	protected void writeFieldDeclaration(ClassField field) throws IOException {
		writeFieldDeclaration(field.getName(), field.getType());
	}
	protected void writeFieldDeclaration(Field field) throws IOException {
		writeFieldDeclaration(field.getName(), field.getType());
	}

	protected void writeFieldDeclarationWithAnnotation(String fieldName, String type, Class annotationClass) throws IOException {
		writeEmptyAnnotation(annotationClass);
		out.writeLn(TAB, PRIVATE_KEYWORD, " ", type, " ", fieldName, SEMICOLON);
		out.writeLn();
	}
	protected void writeFieldDeclarationWithAnnotation(String fieldName, Class type, Class annotationClass) throws IOException {
		writeFieldDeclarationWithAnnotation(fieldName, type.getSimpleName(), annotationClass);
	}
	protected void writeFieldDeclaration(String fieldName, String type, String value) throws IOException {
		out.writeLn(TAB, PRIVATE_KEYWORD, " ", type, " ", fieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, value, SEMICOLON);
	}
	protected void writeFieldDeclaration(String fieldName, Class type, String value) throws IOException {
		writeFieldDeclaration(fieldName, type.getSimpleName(), value);
	}

	protected void writePublicConstant(String fieldName, String className, String value) throws IOException {
		out.writeLn(TAB, PUBLIC_STATIC_FINAL, " ", className, " ", fieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, value, SEMICOLON);
	}
	protected void writePublicStringConstant(String fieldName, String value) throws IOException {
		String fieldValue = (value == null) ? NULL_KEYWORD : getJavaString(value);
		out.writeLn(TAB, PUBLIC_STATIC_FINAL, " ", String.class.getSimpleName(), " ", fieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, fieldValue, SEMICOLON);
	}
	protected void writePrivateStringConstant(String fieldName, String value) throws IOException {
		String fieldValue = (value == null) ? NULL_KEYWORD : getJavaString(value);
		out.writeLn(TAB, PRIVATE_STATIC_FINAL, " ", String.class.getSimpleName(), " ", fieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, fieldValue, SEMICOLON);
	}

	protected void writeLoggerFieldDeclaration() throws IOException {
		writeLoggerFieldDeclaration(DEFAULT_LOGGER_FIELD_NAME);
	}
	protected void writeLoggerFieldDeclaration(String loggerFieldName) throws IOException {
		String getLoggerArgument = getClassLink(className);
		String getLoggerMethodCall = getStaticMethodCall(Logger.class, LOGGER_GET_LOGGER_METHOD_NAME, true, getLoggerArgument);
		out.writeLn(TAB, PRIVATE_STATIC_FINAL, " ", Logger.class.getSimpleName(), " ", loggerFieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, getLoggerMethodCall );
	}

	protected void writeGetter(String fieldName, String type) throws IOException {
		out.writeLn(TAB, PUBLIC_KEYWORD, " ", type, " ", getGetterName(fieldName), ARGUMENTS_LIST_OPEN_BRACKET, ARGUMENTS_LIST_CLOSE_BRACKET, " ", METHOD_BODY_OPEN_BRACKET);
		out.writeLn(TAB, TAB, RETURN_KEYWORD, " ", fieldName, SEMICOLON);
		out.writeLn(TAB, METHOD_BODY_CLOSE_BRACKET);
	}
	protected void writeGetter(String fieldName, Class type) throws IOException {
		writeGetter(fieldName, type.getSimpleName());
	}
	protected void writeSetter(String fieldName, String type) throws IOException {
		out.writeLn(TAB, PUBLIC_KEYWORD, " ", VOID_RETURN_TYPE, " ", getSetterName(fieldName), ARGUMENTS_LIST_OPEN_BRACKET, type, " ", fieldName, ARGUMENTS_LIST_CLOSE_BRACKET, " ", METHOD_BODY_OPEN_BRACKET);
		out.writeLn(DOUBLE_TAB, THIS_KEYWORD, METHOD_CALL_SEPARATOR, fieldName, ASSIGNMENT_OPERATOR_WITH_SPACES, fieldName, SEMICOLON);
		out.writeLn(TAB, METHOD_BODY_CLOSE_BRACKET);
	}
	protected void writeSetter(String fieldName, Class type) throws IOException {
		writeSetter(fieldName, type.getSimpleName());
	}

	protected void writeEmptyConstructor() throws IOException {
		writeConstructor();
	}
	protected void writeConstructor(String... lines) throws IOException {
		out.writeLn(TAB, PUBLIC_KEYWORD, " ", this.className, ARGUMENTS_LIST_OPEN_BRACKET, ARGUMENTS_LIST_CLOSE_BRACKET, " ", METHOD_BODY_OPEN_BRACKET);

		for (String line : lines)
			out.writeLn(DOUBLE_TAB, line);

		out.writeLn(TAB, METHOD_BODY_CLOSE_BRACKET);
		out.writeLn();
	}
	protected void writeConstructorWithArguments(String args, Object... lines) throws IOException{
		out.writeLn(TAB, PUBLIC_KEYWORD, " ", this.className, ARGUMENTS_LIST_OPEN_BRACKET, args, ARGUMENTS_LIST_CLOSE_BRACKET, " ", METHOD_BODY_OPEN_BRACKET);

		for (Object line : lines)
			out.writeLn(TAB, TAB, line);

		out.writeLn(TAB, METHOD_BODY_CLOSE_BRACKET);
		out.writeLn();
	}
	protected void writeGetterAndSetter(String fieldName, String type) throws IOException {
		writeGetter(fieldName, type);
		writeSetter(fieldName, type);
	}
	protected void writeGetterAndSetter(String fieldName, Class fieldType) throws IOException {
		writeGetterAndSetter(fieldName, fieldType.getSimpleName());
	}
	protected void writeGetterAndSetter(ClassField field) throws IOException {
		writeGetterAndSetter(field.getName(), field.getType());
	}
	protected void writeGetterAndSetter(Field field) throws IOException {
		writeGetterAndSetter(field.getName(), field.getType());
	}

	protected void writeMethodHeader(Method method, String indent) throws IOException {
		out.writeLn(indent, method.startMethod());
	}
	protected void writeMethodHeader(Method method) throws IOException {
		writeMethodHeader(method, TAB);
	}
	protected void writeMethodFooter(Method method, String indent) throws IOException {
		out.writeLn(indent, method.endMethod());
	}
	protected void writeMethodFooter(Method method) throws IOException {
		writeMethodFooter(method, TAB);
	}

	protected void writeMethodCall(String indent, String methodName, String... arguments) throws IOException {
		out.writeLn(indent, getMethodCall(methodName, true, arguments));
	}

	protected void writeVariableDeclaration(String indent, String type, String name, String value) throws IOException {
		out.writeLn(indent, type, " ", name, ASSIGNMENT_OPERATOR_WITH_SPACES, value, SEMICOLON);
	}
	protected void writeVariableDeclaration(String indent, Class type, String name, String value) throws IOException {
		writeVariableDeclaration(indent, type.getSimpleName(), name, value);
	}
	protected void writeVariableDeclaration(String type, String name, String value) throws IOException {
		writeVariableDeclaration(DOUBLE_TAB, type, name, value);
	}
	protected void writeVariableDeclaration(Class type, String name, String value) throws IOException {
		writeVariableDeclaration(type.getSimpleName(), name, value);
	}

	/**
	 * Пишет комментарий с указанным отступом.
	 *
	 * @param indent отступ (как правило, содержит один или несколько табов)
	 * @param comment значение комментария
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeComment(String indent, String comment) throws IOException {
		out.writeLn(indent, ONE_STRING_COMMENT, " ", comment);
	}
	/**
	 * Пишет комментарий 1-го уровня вложенности (с одним табом впереди).
	 * @param comment значение комментария
	 * @throws IOException при ошибке записи в файл
	 */
	protected void writeComment(String comment) throws IOException {
		writeComment(TAB, comment);
	}

	protected void writeEmptyAnnotation(String indent, Class annotationClass) throws IOException {
		out.writeLn(indent, ANNOTATION_PREFIX, annotationClass.getSimpleName());
	}
	protected void writeEmptyAnnotation(Class annotationClass) throws IOException {
		writeEmptyAnnotation(TAB, annotationClass);
	}

	protected abstract void writeClassBody() throws IOException;

	private File createClassFile(String baseDir, String packageName, String className) {
	 	String dirPath = concat(sb, baseDir, FILE_SEPARATOR, packageName.replace(PACKAGE_SEPARATOR, FILE_SEPARATOR));
		new File(dirPath).mkdirs(); // если директорий нет, создать их
		return new File( concat(sb, dirPath, FILE_SEPARATOR, className, CLASS_FILE_EXTENSION) );
	}

	public static String getConstantFullName(String className, String constantName) {
		return concat(className, CLASS_CONSTANT_SEPARATOR, constantName);
	}
	public static String getConstantFullName(Class containingClass, String constantName) {
		return getConstantFullName(containingClass.getSimpleName(), constantName);
	}

	public static String getArrayType(String className) {
		return concat(className, ARRAY_TYPE_OPEN_BRACKET, ARRAY_TYPE_CLOSE_BRACKET);
	}
	public static String getArrayType(Class type) {
		return getArrayType(type.getSimpleName());
	}

	protected void writeIf(String indent, String clause, boolean addSpaces) throws IOException {
		out.writeLn( getIf(indent, clause, addSpaces) );
	}
	protected void writeIf(String indent, String clause) throws IOException {
		out.writeLn( getIf(indent, clause, false) );
	}

	protected void writeIfHeader(String indent, String clause, boolean addSpaces) throws IOException {
		writeIf(indent, clause, addSpaces);
		out.writeLn(indent, BLOCK_OPENING_BRACKET);
	}
	protected void writeIfHeader(String indent, String clause) throws IOException {
		writeIfHeader(indent, clause, false);
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

	// todo: "else if" branch writing

	protected void writeReturn(String value, String indent) throws IOException {
		out.writeLn(indent, getReturn(value, true));
	}
	protected void writeReturn(String value) throws IOException {
		writeReturn(value, DOUBLE_TAB);
	}
	protected void writeReturn(Object value, String indent) throws IOException {
		writeReturn(value.toString(), indent);
	}
	protected void writeReturn(Object value) throws IOException {
		writeReturn(value.toString());
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

	public static String getJavaString(String str) {
		return concat("\"", escapeJava(str), "\"");
	}

	public static String getClassNameLink(Class cls) {
		return getClassLink( cls.getName() );
	}
	public static String getClassSimpleNameLink(Class cls) {
		return getClassLink( cls.getSimpleName() );
	}
	public static String getClassLink(String className) {
		return concat(className, CLASS_POSTFIX);
	}

	protected boolean failIfFileExists = true;
	protected boolean overwriteIfFileExists = false;

	public static final String NO_TAB = "";
	public static final String TAB = "\t";
	public static final String DOUBLE_TAB = "\t\t";
	public static final String TRIPLE_TAB = "\t\t\t";
	public static final String QUADRUPLE_TAB = "\t\t\t\t";
	public static final String QUINARY_TAB = "\t\t\t\t\t";

	protected String baseDir;
	protected String packageName;
	protected String className;

	protected FileWriter out;
	protected StringBuffer sb = new StringBuffer();

	protected static final Logger logger = Logger.getLogger(ClassFileWriter.class);

	public static final String PACKAGE_KEYWORD = "package";
	public static final String IMPORT_KEYWORD = "import";
	public static final String IMPORT_STATIC = "import static";
	public static final String PUBLIC_KEYWORD = "public";
	public static final String PROTECTED_KEYWORD = "protected";
	public static final String PRIVATE_KEYWORD = "private";
	public static final String EXTENDS_KEYWORD = "extends";
	public static final String IMPLEMENTS_KEYWORD = "implements";
	public static final String FINAL_KEYWORD = "final";
	public static final String STATIC_KEYWORD = "static";
	public static final String THIS_KEYWORD = "this";

	public static final String PUBLIC_STATIC_FINAL = "public static final";
	public static final String PRIVATE_STATIC_FINAL = "private static final";

	public static final String CLASS_KEYWORD = "class";
	public static final String PUBLIC_CLASS = "public class";

	public static final String INTERFACE_KEYWORD = "interface";
	public static final String PUBLIC_INTERFACE = "public interface";

	public static final String ANNOTATION_PREFIX = "@";

	public static final String DEFAULT_LOGGER_FIELD_NAME = "logger";
	public static final String CLASS_FILE_ENCODING = "Cp1251";
	public static final String CLASS_FILE_EXTENSION = ".java";
	public static final String CLASS_POSTFIX = ".class";
	public static final String SUPER = "super";
	public static final String METHOD_CALL_SEPARATOR = ".";
	public static final String CLASS_CONSTANT_SEPARATOR = ".";
	public static final String PACKAGE_SEPARATOR = ".";
	public static final String ALL_MEMBERS_IMPORT = "*";
	public static final String ARRAY_TYPE_OPEN_BRACKET = "[";
	public static final String ARRAY_TYPE_CLOSE_BRACKET = "]";

	public static final String BLOCK_OPENING_BRACKET = "{"; // for "if () {" etc
	public static final String BLOCK_CLOSING_BRACKET = "}"; // for "if () ... }" etc

	public static final String ONE_STRING_COMMENT = "//";
	public static final String OPENING_BRACKET = "("; // for "if ()" etc
	public static final String CLOSING_BRACKET = ")"; // for "if ()" etc

	public static final String NEW_KEYWORD = "new";
	public static final String IF_KEYWORD = "if";
	public static final String ELSE_KEYWORD = "else";
	public static final String SWITCH_KEYWORD = "switch";
	public static final String CASE_KEYWORD = "case";
	public static final String CASE_KEY_VALUE_SEPARATOR = ":";
	public static final String DEFAULT_KEYWORD = "default";
	public static final String BREAK_KEYWORD = "break";
	public static final String RETURN_KEYWORD = "return";
	public static final String NULL_KEYWORD = "null";

	public static final String SEMICOLON = ";";

	public static final String ASSIGNMENT_OPERATOR = "=";
	public static final String ASSIGNMENT_OPERATOR_WITH_SPACES = " = ";
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

	public static final String JAVA_LANG_PACKAGE_NAME = "java.lang";
	public static final String LOGGER_GET_LOGGER_METHOD_NAME = "getLogger";
}