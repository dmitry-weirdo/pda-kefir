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

public class DeleteServletFileWriter extends EmptyJsonServletFileWriter
{
	public DeleteServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getDeleteServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();

		writeImport( getServiceClassName(extEntity, entityClass) );
	}
	protected void writeActionBody() throws IOException {
		String serviceName = getServiceClassSimpleName(extEntity, entityClass);

		writeGetService(serviceName);
		out.writeLn("\t\t\t\t", DEFAULT_SERVICE_VAR_NAME, ".", getDeleteMethodName(extEntity, entityClass), "( ", GET_ID_FUNCTION_NAME, "() );");
		out.writeLn();
		out.writeLn("\t\t\t\t", WRITE_SUCCESS_FUNCTION_NAME, "();");
	}
}