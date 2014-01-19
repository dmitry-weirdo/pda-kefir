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
import su.opencode.kefir.gen.field.AttachmentsField;

import java.io.IOException;
import java.lang.reflect.Field;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class CreateServletFileWriter extends EmptyJsonServletFileWriter
{
	public CreateServletFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.packageName = getServletPackageName(extEntity, entityClass);
		this.className = getCreateServletClassName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeImports() throws IOException {
		super.writeImports();

		writeImport( getServiceClassName(extEntity, entityClass) );
		writeImport(entityClass.getName());

		if (hasAttachmentsFields(entityClass))
			writeImport(AttachmentsField.SERVICE_CLASS_NAME);
	}
	protected void writeActionBody() throws IOException {
		String className = entityClass.getSimpleName();
		String serviceName = getServiceClassSimpleName(extEntity, entityClass);
		String entityVarName = getSimpleName(entityClass);

		writeGetService(serviceName);
		out.writeLn();

		out.writeLn("\t\t\t\t", className, " ", entityVarName, " = ", FROM_JSON_FUNCTION_NAME, "(", className, ".class);");

		if ( !hasAttachmentsFields(entityClass) )
		{ // класс без файловых вложений
			out.writeLn("\t\t\t\t", DEFAULT_SERVICE_VAR_NAME, ".", getCreateMethodName(extEntity, entityClass), "(", entityVarName, ");");
			out.writeLn();
			out.writeLn("\t\t\t\t", WRITE_SUCCESS_FUNCTION_NAME, "(", entityVarName, ");");
		}
		else
		{ // класс с файловыми вложениями -> проставить id созданной сущности в сохраненные аттачи
			String entityIdVarName = "id";
			String attachmentServiceVarName = "attachmentService";
			String attachmentServiceClassSimpleName = getSimpleName(AttachmentsField.SERVICE_CLASS_NAME);

			out.writeLn("\t\t\t\tInteger ", entityIdVarName, " = ", DEFAULT_SERVICE_VAR_NAME, ".", getCreateMethodName(extEntity, entityClass), "(", entityVarName, ");");
			out.writeLn();

			writeComment("fill entityId for attachments");
			out.writeLn("\t\t\t\t", attachmentServiceClassSimpleName, " ", attachmentServiceVarName, " = ", GET_SERVICE_FUNCTION_NAME, "(", attachmentServiceClassSimpleName, ".class);");
			// для каждого attachment поля взять id из соотв. поля и проставить им код сущности
			for (Field attachmentsField : getAttachmentsFields(entityClass))
			{
				String forIdVarName = concat(sb, getFieldName(attachmentsField), "Id");
				AttachmentsField attachmentsFieldAnnotation = getAttachmentsFieldAnnotation(attachmentsField);

				// for по id полей
				out.writeLn("\t\t\t\tfor (Integer ", forIdVarName, " : ", GET_CHECK_GRID_IDS_FUNCTION_NAME, "(\"", getAttachmentsFieldIdsParamName(attachmentsFieldAnnotation, attachmentsField), "\"))");
				out.writeLn("\t\t\t\t\t", attachmentServiceVarName, ".", SET_ATTACHMENT_ENTITY_ID_FUNCTION_NAME, "(", forIdVarName, ", ", entityIdVarName, ");");
				out.writeLn();
			}

			// в любом случае в response пишется созданная сущность
			out.writeLn("\t\t\t\t", WRITE_SUCCESS_FUNCTION_NAME, "(", entityVarName, ");");
		}
	}
}