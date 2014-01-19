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
import static su.opencode.kefir.util.StringUtils.concat;

public class ListServletFileWriter extends EmptyJsonServletFileWriter
{
	public ListServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getListServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();

		writeImport(SortConfig.class);
		writeImport( getFilterConfigClassName(extEntity, entityClass) );
		writeImport( getServiceClassName(extEntity, entityClass) );
		writeImport( getListVOClassName(extEntity, entityClass) );

		out.writeLn();
		writeImport(List.class);
	}
	protected void writeActionBody() throws IOException {
		String sortConfigVarName = "sortConfig";
		out.writeLn("\t\t\t\t", SortConfig.class.getSimpleName(), " ", sortConfigVarName, " = ", FILL_SORT_CONFIG_FUNCTION_NAME, "();");

		String filterConfigVarName = "filterConfig";
		String filterConfigName = getFilterConfigClassSimpleName(extEntity, entityClass);
		out.writeLn("\t\t\t\t", filterConfigName, " ", filterConfigVarName, " = ", FROM_JSON_FUNCTION_NAME, "(", filterConfigName, ".class);");

		out.writeLn();

		String serviceName = getServiceClassSimpleName(extEntity, entityClass);

		String voListVarName = concat(sb, getSimpleName(entityClass), "s");
		String countVarName = concat(sb, getSimpleName(entityClass), "sCount");

		writeGetService(serviceName);

		out.writeLn(
			"\t\t\t\tList<", getListVOClassSimpleName(extEntity, entityClass), "> ", voListVarName,
			" = ",
			DEFAULT_SERVICE_VAR_NAME, ".", getListMethodName(extEntity, entityClass), "(", filterConfigVarName, ", ", sortConfigVarName, ");"
		);
		out.writeLn("\t\t\t\tint ", countVarName, " = ", DEFAULT_SERVICE_VAR_NAME, ".",  getCountMethodName(extEntity, entityClass), "(", filterConfigVarName, ");");

		out.writeLn();
		out.writeLn("\t\t\t\t", WRITE_JSON_FUNCTION_NAME, "(", voListVarName, ", ",  countVarName, ");");
	}
}