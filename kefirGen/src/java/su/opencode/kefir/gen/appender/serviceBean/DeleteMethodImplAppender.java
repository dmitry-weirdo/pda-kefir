/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.serviceBean;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.AttachmentsField;
import su.opencode.kefir.gen.field.linkedEntity.PreventDeleteEntity;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.util.EntityManagerUtils;

import javax.ejb.EJB;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.ATTACHMENT_SERVICE_FIELD_NAME;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.EM_FIELD_NAME;
import static su.opencode.kefir.util.EntityManagerUtils.REMOVE_METHOD_NAME;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.ObjectUtils.getSetterName;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.decapitalize;

public class DeleteMethodImplAppender extends ServiceBeanMethodAppender
{
	public DeleteMethodImplAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что и реализация сервиса -> добавить импорт сущности

		boolean addEjbImport = false;

		if (hasAttachmentsFields(entityClass))
		{ // если у сущности есть вложения, то нужно добавить поле attachmentService
			addEjbImport = true;
			addImport( AttachmentsField.SERVICE_CLASS_NAME, fileLines, imports );
		}

		if (hasPreventDeleteEntities(extEntity))
		{ // есть связанные сущности, которые нужно проверять на существование перед удалением
			addImport(ClientException.class, fileLines, imports);

			String thisServiceClassName = getServiceClassName(extEntity, entityClass);

			for (PreventDeleteEntity preventDeleteEntity : extEntity.preventDeleteEntities())
			{
				Class preventDeleteEntityClass = getPreventDeleteEntityClass(preventDeleteEntity);
				ExtEntity preventDeleteEntityExtEntity = getPreventDeleteEntityExtEntityAnnotation(preventDeleteEntity);

				addImport( getFilterConfigClassName(preventDeleteEntityExtEntity, preventDeleteEntityClass), fileLines, imports ); // импортировать FilterConfig удаляемой сущности

				String serviceClassName = getServiceClassName(preventDeleteEntityExtEntity, preventDeleteEntityClass);
				if ( !serviceClassName.equals(thisServiceClassName))
				{
					addEjbImport = true;
					addImport(serviceClassName, fileLines, imports ); // импортировать сервис удаляемой сущности
				}
			}
		}

		if ( hasAddressFields(entityClass) )
		{
			addStaticImport(EntityManagerUtils.class, REMOVE_METHOD_NAME, fileLines, imports);
		}

		if (addEjbImport)
			addImport(EJB.class, fileLines, imports);
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<>();
		signatures.add(getSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		String className = entityClass.getSimpleName();
		String varName = getSimpleName(entityClass);

		addMethodStart(fileLines, getSignature());

		fileLines.add( concat(sb,
			"\t\t", className, " ", varName, " = ", getGetMethodName(extEntity, entityClass), "(", ID_PARAM_NAME, ");"
		) );
		fileLines.add(concat(sb, "\t\t", "if (", varName, " == null)"));
		fileLines.add("\t\t\treturn;");
		appendEmptyLine(fileLines);

		if ( hasPreventDeleteEntities(extEntity) )
		{ // проверить связанные сущности
			for (PreventDeleteEntity preventDeleteEntity : extEntity.preventDeleteEntities())
			{
				Class preventDeleteEntityClass = getPreventDeleteEntityClass(preventDeleteEntity);
				ExtEntity preventDeleteEntityExtEntity = getPreventDeleteEntityExtEntityAnnotation(preventDeleteEntity);

				String filterConfigClassName = getFilterConfigClassSimpleName(preventDeleteEntityExtEntity, preventDeleteEntityClass);
				String filterConfigVarName = decapitalize(sb, filterConfigClassName);
				fileLines.add( concat(sb, "\t\t", filterConfigClassName, " ", filterConfigVarName, " = new ", filterConfigClassName, "();"));

				fileLines.add( concat(sb,
					"\t\t", filterConfigVarName, ".", getSetterName( getPreventDeleteEntityFilterConfigFieldName(preventDeleteEntity, entityClass) ), "(", ID_PARAM_NAME, ");"
				) );

				String countMethodPrefix = ""; // если count-метод в том же сервисе, то вызывать надо метод этого же класса
				String preventDeleteEntityServiceClassName = getServiceClassName(preventDeleteEntityExtEntity, preventDeleteEntityClass);
				String thisServiceClassName = getServiceClassName(extEntity, entityClass);
				if ( !preventDeleteEntityServiceClassName.equals(thisServiceClassName) )
					countMethodPrefix = concat( decapitalize( getSimpleName(preventDeleteEntityServiceClassName) ), ".");

				fileLines.add( concat(sb, "\t\tif ( ", countMethodPrefix, getCountMethodName(preventDeleteEntityExtEntity, preventDeleteEntityClass), "(", filterConfigVarName, ") > 0 )") );
				fileLines.add( concat(sb, "\t\t\tthrow new ", ClientException.class.getSimpleName(), "(\"", preventDeleteEntity.message(), "\");") );
				appendEmptyLine(fileLines);
			}
		}

		if ( hasAttachmentsFields(entityClass) )
		{ // удалить связанные аттачменты
			fileLines.add( concat(sb, "\t\t", ATTACHMENT_SERVICE_FIELD_NAME, ".deleteAttachments(", className, ".class.getName(), ", ID_PARAM_NAME, ");") );
			appendEmptyLine(fileLines);
		}

		fileLines.add( concat(sb, "\t\t", EM_FIELD_NAME, ".remove(", varName, ");") );

		if ( hasAddressFields(entityClass) )
		{
			appendEmptyLine(fileLines);

			for (Field field : getAddressFields(entityClass))
			{
				String getterName = getGetterName(field.getName());
				fileLines.add( concat(sb, "\t\t", REMOVE_METHOD_NAME, "(", EM_FIELD_NAME, ", ", varName, ".", getterName, "());") );
			}
		}

		addMethodEnd(fileLines);

		// add two lines after delete method impl
		appendEmptyLine(fileLines);
		appendEmptyLine(fileLines);
	}

	protected void appendFields(List<String> fileLines, List<String> fields) {
		if ( hasAttachmentsFields(entityClass) )
		{ // добавить поле @EJB attachmentService
			appendEjbField(AttachmentsField.SERVICE_CLASS_NAME, fileLines, fields);
		}

		if ( hasPreventDeleteEntities(extEntity) )
		{ // добавить поля сервисов связанных сущностей
			Set<String> servicesClassNames = new HashSet<>(); // исключить задвоения добавляемых полей сервисов

			for (PreventDeleteEntity preventDeleteEntity : extEntity.preventDeleteEntities())
			{
				Class preventDeleteEntityClass = getPreventDeleteEntityClass(preventDeleteEntity);
				ExtEntity preventDeleteEntityExtEntity = getPreventDeleteEntityExtEntityAnnotation(preventDeleteEntity);

				String preventDeleteEntityServiceClassName = getServiceClassName(preventDeleteEntityExtEntity, preventDeleteEntityClass);
				servicesClassNames.add(preventDeleteEntityServiceClassName);
			}

			// добавить поля накопленных сервисов
			for (String serviceClassName : servicesClassNames)
				appendEjbField(serviceClassName, fileLines, fields);
		}
	}
	private String getSignature() {
		return concat(sb,
			"void ", getDeleteMethodName(extEntity, entityClass), "(Integer ", ID_PARAM_NAME, ")"
		);
	}

	private static final String ID_PARAM_NAME = "id";
}