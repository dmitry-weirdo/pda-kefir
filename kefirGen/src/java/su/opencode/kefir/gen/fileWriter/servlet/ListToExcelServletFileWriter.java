/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.servlet;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.srv.SortConfig;

import java.io.IOException;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.RenderersFileWriter.GET_RENDER_INPUT_STREAM_METHOD_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class ListToExcelServletFileWriter extends EmptyJsonServletFileWriter
{
	public ListToExcelServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass, Class renderersClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getListExportToExcelServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
		this.renderersClass = renderersClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();

		writeImport(SortConfig.class);
		writeImport( getFilterConfigClassName(extEntity, entityClass) );
		writeImport( getServiceClassName(extEntity, entityClass) );
		writeImport( getListVOClassName(extEntity, entityClass) );

		out.writeLn();
		writeImport(List.class);

		out.writeLn();
		writeStaticImport(renderersClass, GET_RENDER_INPUT_STREAM_METHOD_NAME);
	}
	protected void writeActionBody() throws IOException {
		String sortConfigVarName = "sortConfig";
		out.writeLn("\t\t\t\t", SortConfig.class.getSimpleName(), " ", sortConfigVarName, " = ", FILL_SORT_CONFIG_FUNCTION_NAME, "();");

		String filterConfigVarName = "filterConfig";
		String filterConfigClassName = getFilterConfigClassSimpleName(extEntity, entityClass);
		out.writeLn("\t\t\t\t", filterConfigClassName, " ", filterConfigVarName, " = ", FROM_JSON_FUNCTION_NAME, "(", filterConfigClassName, ".class);");

		out.writeLn();

		String serviceName = getServiceClassSimpleName(extEntity, entityClass);
		writeGetService(serviceName);

		String countVarName = concat(sb, getSimpleName(entityClass), "sCount");
		out.writeLn("\t\t\t\tint ", countVarName, " = ", DEFAULT_SERVICE_VAR_NAME, ".",  getCountMethodName(extEntity, entityClass), "(", filterConfigVarName, ");");
		out.writeLn();

		// set sortConfig start to 0, end to count
		out.writeLn("\t\t\t\t", sortConfigVarName, ".setStart(0);");
		out.writeLn("\t\t\t\t", sortConfigVarName, ".setLimit(", countVarName, ");");
		out.writeLn();

		String voListVarName = concat(sb, getSimpleName(entityClass), "s");
		String fileName = concat("\"", getListExportToExcelFileName(extEntity, entityClass), "\"");
		out.writeLn(
			"\t\t\t\tList<", getListVOClassSimpleName(extEntity, entityClass), "> ", voListVarName,
			" = ",
			DEFAULT_SERVICE_VAR_NAME, ".", getListMethodName(extEntity, entityClass), "(", filterConfigVarName, ", ", sortConfigVarName, ");"
		);
		out.writeLn("\t\t\t\t", WRITE_TO_EXCEL_FUNCTION_NAME, "(", voListVarName, ", ", sortConfigVarName, ".", GET_ENTITY_NAME_FUNCTION_NAME, "(), ", fileName, ", ", GET_RENDER_INPUT_STREAM_METHOD_NAME, "());");
	}

	protected Class renderersClass;
}