/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.servlet;

import static su.opencode.kefir.util.StringUtils.concat;

import java.io.IOException;

import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

public class EmptyJsonServletFileWriter extends ClassFileWriter
{
	public EmptyJsonServletFileWriter(String baseDir, String packageName, String className) {
		super(baseDir, packageName, className);
	}
	protected void writeImports() throws IOException {
		writeImport(Action.class);
		writeImport(InitiableAction.class);
		writeImport(JsonServlet.class);
	}

	protected void writeClassBody() throws IOException {
		out.writeLn("public class ", className, " extends ", JsonServlet.class.getSimpleName());
		out.writeLn("{");

		out.writeLn(TAB, "protected ", Action.class.getSimpleName(), " ", GET_ACTION_FUNCTION_NAME, "() {");
		out.writeLn(DOUBLE_TAB, "return new ", InitiableAction.class.getSimpleName(), "()");
		out.writeLn(DOUBLE_TAB, "{");
		out.writeLn(TRIPLE_TAB, "public void ", DO_ACTION_FUNCTION_NAME, "() throws Exception {");

		writeActionBody();

		out.writeLn(TRIPLE_TAB, "}");
		out.writeLn(DOUBLE_TAB, "};");
		out.writeLn(TAB, "}");

		out.write("}");
	}

	protected void writeActionBody() throws IOException {
		writeComment("todo: implement method");
	}

	protected void writeGetService(String serviceClassSimpleName, String varName) throws IOException {
		out.writeLn(QUADRUPLE_TAB, serviceClassSimpleName, " ", varName, " = ", GET_SERVICE_FUNCTION_NAME, "(", serviceClassSimpleName, ".class);" );
	}
	protected void writeGetService(String serviceClassSimpleName) throws IOException {
		writeGetService(serviceClassSimpleName, DEFAULT_SERVICE_VAR_NAME);
	}

	/**
	 * Пишет комментарий 1-го уровня вложенности в doAction (с 4 табами впереди).
	 * @param comment значение комментария
	 * @throws IOException при ошибке записи в файл
	 */
	@Override
	protected void writeComment(String comment) throws IOException {
		writeComment(QUADRUPLE_TAB, comment);
	}

	protected ExtEntity extEntity;
	protected Class entityClass;

	public static final String DEFAULT_SERVICE_VAR_NAME = "service";

	// JsonServlet functions (not wrapped by Action class)
	public static final String GET_ACTION_FUNCTION_NAME = "getAction";
	public static final String DO_ACTION_FUNCTION_NAME = "doAction";

	// Action functions names (functions that wrap JsonServlet's functions)
	public static final String GET_SERVICE_FUNCTION_NAME = "getService";
	public static final String WRITE_SUCCESS_FUNCTION_NAME = "writeSuccess";
	public static final String WRITE_JSON_FUNCTION_NAME = "writeJson";
	public static final String GET_ID_FUNCTION_NAME = "getId";
	public static final String FROM_JSON_FUNCTION_NAME = "fromJson";
	public static final String GET_VO_CLASS_FUNCTION_NAME = "getVOClass";
	public static final String GET_ENTITY_NAME_FUNCTION_NAME = "getEntityName";
	public static final String FILL_SORT_CONFIG_FUNCTION_NAME = "fillSortConfig";
	public static final String GET_CHECK_GRID_IDS_FUNCTION_NAME = "getCheckGridIds";
	public static final String WRITE_TO_EXCEL_FUNCTION_NAME = "writeToExcel";

	// attachment service function names
	public static final String SET_ATTACHMENT_ENTITY_ID_FUNCTION_NAME = "setAttachmentEntityId";
}