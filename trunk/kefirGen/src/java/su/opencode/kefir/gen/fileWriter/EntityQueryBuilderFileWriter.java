/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.srv.EntityQueryBuilder;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.*;

public class EntityQueryBuilderFileWriter extends ClassFileWriter
{
	public EntityQueryBuilderFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getPackageName( getQueryBuilderClassName(extEntity, entityClass) );
		this.className = getQueryBuilderClassSimpleName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}
	protected void writeImports() throws IOException {
		if ( !samePackage( getQueryBuilderClassName(extEntity, entityClass), entityClass.getName()) )
		{ // добавить импорт класса сущности, если билдер находится в другом пакете
			writeImport(entityClass.getName());
		}

		writeImport(EntityQueryBuilder.class);
	}

	protected void writeClassBody() throws IOException {
		writeClassHeader(EntityQueryBuilder.class);

		out.writeLn("\tpublic Class getEntityClass() {");
		out.writeLn("\t\treturn ", entityClass.getSimpleName(), ".class;");
		out.writeLn("\t}");

		out.writeLn("\tpublic String getSqlEntityName() {");
		out.writeLn("\t\treturn \"", entityClass.getSimpleName(), "\";");
		out.writeLn("\t}");

		if ( !extEntity.queryBuilderJoin().isEmpty())
		{ // write getJoin(String entityPrefix) method
			out.writeLn("\tpublic String getJoin(String entityPrefix) {");
			out.writeLn("\t\treturn \"", extEntity.queryBuilderJoin(), "\";");
			out.writeLn("\t}");
		}

		writeClassFooter();
	}

	private ExtEntity extEntity;
	private Class entityClass;
}