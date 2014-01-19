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
import su.opencode.kefir.util.EntityManagerUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getCreateMethodName;
import static su.opencode.kefir.gen.ExtEntityUtils.getSimpleName;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getAddressFields;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.hasAddressFields;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.EM_FIELD_NAME;
import static su.opencode.kefir.util.EntityManagerUtils.PERSIST_METHOD_NAME;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.StringUtils.concat;

public class CreateMethodImplAppender extends ServiceBeanMethodAppender
{
	public CreateMethodImplAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что и реализация сервиса -> добавить импорт сущности

		if ( hasAddressFields(entityClass) )
		{
			addStaticImport(EntityManagerUtils.class, PERSIST_METHOD_NAME, fileLines, imports);
		}
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<>();
		signatures.add(getSignature());
		return signatures;
	}

	protected void appendMethods(List<String> fileLines) {
		String paramName = getSimpleName(entityClass);

		addMethodStart(fileLines, getSignature());

		if ( hasAddressFields(entityClass) )
		{
			for (Field field : getAddressFields(entityClass))
			{
				String getterName = getGetterName(field.getName());
				fileLines.add( concat(sb, "\t\t", PERSIST_METHOD_NAME, "(", EM_FIELD_NAME, ", ", paramName, ".", getterName, "());") );
			}

			appendEmptyLine(fileLines);
		}

		fileLines.add( concat(sb, "\t\t", EM_FIELD_NAME, ".persist(", paramName, ");") );
		fileLines.add( concat(sb, "\t\treturn ", paramName, ".getId();") );
		addMethodEnd(fileLines);
	}

	private String getSignature() {
		return concat(sb,
			"Integer ", getCreateMethodName(extEntity, entityClass), "(", entityClass.getSimpleName(), " ", getSimpleName(entityClass), ")"
		);
	}
}