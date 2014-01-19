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
import su.opencode.kefir.srv.VO;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class GetServletFileWriter extends EmptyJsonServletFileWriter
{
	public GetServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getGetServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();
		writeImport(VO.class);

		writeImport( getServiceClassName(extEntity, entityClass) );
		writeImport(entityClass.getName());
	}
	protected void writeActionBody() throws IOException {
		String className = entityClass.getSimpleName();
		String serviceName = getServiceClassSimpleName(extEntity, entityClass);
		String entityVarName = getSimpleName(entityClass);
		String entityNameVarName = "entityName";
		String entityVoVarName = concat(sb, getSimpleName(entityClass), "VO");

		writeGetService(serviceName);
		out.writeLn();
		out.writeLn("\t\t\t\tString ", entityNameVarName, " = ", GET_ENTITY_NAME_FUNCTION_NAME, "();");
		out.writeLn("\t\t\t\tif (", entityNameVarName, " == null || ", entityNameVarName, ".isEmpty())");
		out.writeLn("\t\t\t\t{ // return entity");
		out.writeLn("\t\t\t\t\t", className, " ", entityVarName, " = ", DEFAULT_SERVICE_VAR_NAME, ".", getGetMethodName(extEntity, entityClass), "( ", GET_ID_FUNCTION_NAME, "() );");
		out.writeLn("\t\t\t\t\t", WRITE_JSON_FUNCTION_NAME, "(", entityVarName, ");");
		out.writeLn("\t\t\t\t}"); // end if
		out.writeLn("\t\t\t\telse");
		out.writeLn("\t\t\t\t{ // return VO");
		out.writeLn("\t\t\t\t\t", VO.class.getSimpleName(), " ", entityVoVarName, " = ", DEFAULT_SERVICE_VAR_NAME, ".", getGetVOMethodName(extEntity, entityClass), "(", GET_ID_FUNCTION_NAME, "(), ", GET_VO_CLASS_FUNCTION_NAME, "(", entityNameVarName, "));");
		out.writeLn("\t\t\t\t\t", WRITE_JSON_FUNCTION_NAME, "(", entityVoVarName, ");");
		out.writeLn("\t\t\t\t}"); // end else
	}
}