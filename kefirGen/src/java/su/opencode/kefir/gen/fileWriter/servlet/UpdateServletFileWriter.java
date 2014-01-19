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

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.*;

public class UpdateServletFileWriter extends EmptyJsonServletFileWriter
{
	public UpdateServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getUpdateServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();

		writeImport( getServiceClassName(extEntity, entityClass) );
		writeImport(entityClass.getName());
	}
	protected void writeActionBody() throws IOException {
		String className = entityClass.getSimpleName();
		String serviceName = getServiceClassSimpleName(extEntity, entityClass);
		String entityVarName = getSimpleName(entityClass);

		writeGetService(serviceName);
		out.writeLn();

		out.writeLn("\t\t\t\t", className, " ", entityVarName, " = ", FROM_JSON_FUNCTION_NAME, "(", className, ".class);");
		out.writeLn("\t\t\t\t", DEFAULT_SERVICE_VAR_NAME, ".", getUpdateMethodName(extEntity, entityClass), "(", GET_ID_FUNCTION_NAME, "(), ", entityVarName, ");");
		out.writeLn();
		out.writeLn("\t\t\t\t", WRITE_SUCCESS_FUNCTION_NAME, "();"); // todo: если надо, в писать в респонс измененную сущность
	}
}