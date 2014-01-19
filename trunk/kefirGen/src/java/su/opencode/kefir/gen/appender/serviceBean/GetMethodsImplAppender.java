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
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.util.StringUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getIdField;
import static su.opencode.kefir.gen.fileWriter.EntityFilterConfigFileWriter.CONCAT_METHOD_NAME;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.EM_FIELD_NAME;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.SB_FIELD_NAME;
import static su.opencode.kefir.util.StringUtils.concat;

public class GetMethodsImplAppender extends ServiceBeanMethodAppender
{
	public GetMethodsImplAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport(VO.class, fileLines, imports);
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что и реализация сервиса -> добавить импорт сущности

		if ( extEntity.getUsingSelectById() )
		{
			addImport(Query.class, fileLines, imports);
			addStaticImport(StringUtils.class, CONCAT_METHOD_NAME, fileLines, imports);
		}
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<>();
		signatures.add(getGetEntityMethodSignature());
		signatures.add(getGetVOMethodSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		String className = entityClass.getSimpleName();
		appendGetMethod(fileLines, className);
		appendGetVOMethod(fileLines, className);
	}
	private void appendGetMethod(List<String> fileLines, String className) {
		addMethodStart(fileLines, getGetEntityMethodSignature());

		if (extEntity.getUsingSelectById())
		{ // использовать запрос "select Entity o where o.id = :id"
			String queryStrVarName = "queryStr";
			String queryAlias = "o";
			String idFieldName = getIdField(entityClass).getName();

			String queryVarName = "query";
			String resultListVarName = "resultList";

			fileLines.add( concat(sb,
				"\t\t", String.class.getSimpleName(), " ", queryStrVarName, " = ", CONCAT_METHOD_NAME, "(",
					SB_FIELD_NAME, ", \"select ", queryAlias, " from ", className, " ", queryAlias, " where ", queryAlias, ".", idFieldName, " = \", ", ID_PARAM_NAME,
				");"
			) ); //			String queryStr = concat(sb, "select o from Holder o where o.id = ", id);

			fileLines.add( concat(sb, "\t\t", Query.class.getSimpleName(), " ", queryVarName, " = ", EM_FIELD_NAME, ".createQuery(", queryStrVarName, ");") ); //			Query query = em.createQuery(queryStr);
			fileLines.add( concat(sb, "\t\t", List.class.getSimpleName(), " ", resultListVarName, " = ", queryVarName, ".getResultList();") ); //			List resultList = query.getResultList();
			fileLines.add( concat(sb, "\t\tif (", resultListVarName, ".isEmpty())") ); //			if (resultList.isEmpty())
			fileLines.add("\t\t\treturn null;"); // return null

			appendEmptyLine(fileLines);

			fileLines.add( concat(sb, "\t\treturn (", className, ") ", resultListVarName, ".get(0);") ); // return (Holder) resultList.get(0);
		}
		else
		{ // использовать em.find
			fileLines.add( concat(sb, "\t\treturn ", EM_FIELD_NAME, ".find(", className, ".class, ", ID_PARAM_NAME, ");") );
		}

		addMethodEnd(fileLines);
	}
	private String getGetEntityMethodSignature() {
		return concat(sb,
			entityClass.getSimpleName(), " ", getGetMethodName(extEntity, entityClass), "(", Integer.class.getSimpleName(), " ", ID_PARAM_NAME, ")"
		);
	}

	private void appendGetVOMethod(List<String> fileLines, String className) {
		addMethodStart(fileLines, getGetVOMethodSignature());

		String entityVarName = getSimpleName(entityClass);
		fileLines.add( concat(sb, "\t\t", className, " ", entityVarName, " = ", getGetMethodName(extEntity, entityClass), "(", ID_PARAM_NAME, ");") );
		fileLines.add( concat(sb, "\t\tif (", entityVarName, " == null)") );
		fileLines.add("\t\t\treturn null;");
		appendEmptyLine(fileLines);

		fileLines.add( concat(sb, "\t\treturn ", VO.class.getSimpleName(), ".newInstance(", entityVarName, ", ", VO_CLASS_PARAM_NAME, ");") );
		addMethodEnd(fileLines);
	}
	private String getGetVOMethodSignature() {
		String templateName = "T";

		return concat(sb,
			"<", templateName, " extends ", VO.class.getSimpleName(), "> ", templateName, " ", getGetVOMethodName(extEntity, entityClass), "(", Integer.class.getSimpleName(), " ", ID_PARAM_NAME, ", ", Class.class.getSimpleName(), "<", templateName, "> ", VO_CLASS_PARAM_NAME, ")"
		);
	}

	private static final String ID_PARAM_NAME = "id";
	private static final String VO_CLASS_PARAM_NAME = "voClass";
}