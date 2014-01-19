/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.serviceBean;

import static su.opencode.kefir.util.StringUtils.concat;

import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.gen.ExtEntity;
import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.fileWriter.LocalServiceBeanFileWriter.EM_FIELD_NAME;

import java.util.ArrayList;
import java.util.List;

public class ListMethodsImplAppender extends ServiceBeanMethodAppender
{
	public ListMethodsImplAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		// на случай, если они были удалены при оптимизации импортов
		addImport(SortConfig.class, fileLines, imports);
		addImport(List.class, fileLines, imports);

		addImport( getListVOClassName(extEntity, entityClass), fileLines, imports ); // если VO не в том же пакете, что и реализация сервиса -> добавить импорт VO
		addImport( getFilterConfigClassName(extEntity, entityClass), fileLines, imports ); // если FilterConfig не в том же пакете, что и реализация сервиса -> добавить импорт FilterConfig
		addImport( getQueryBuilderClassName(extEntity, entityClass), fileLines, imports ); // если QueryBuilder не в том же пакете, что и реализация сервиса -> добавить импорт QueryBuilder
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<>();
		signatures.add(getListMethodSignature());
		signatures.add(getCountMethodSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		String voClassSimpleName = getListVOClassSimpleName(extEntity, entityClass);
		String qbClassSimpleName = getQueryBuilderClassSimpleName(extEntity, entityClass);

		fileLines.add("\t@SuppressWarnings(value = \"unchecked\")");
		addMethodStart(fileLines, getListMethodSignature());
		fileLines.add(concat(sb,
			"\t\treturn new ", qbClassSimpleName, "().getList(", FILTER_CONFIG_PARAM_NAME, ", ", SORT_CONFIG_PARAM_NAME, ", ", EM_FIELD_NAME, ", ", voClassSimpleName, ".class);"
		));
		addMethodEnd(fileLines);

		addMethodStart(fileLines, getCountMethodSignature());
		fileLines.add( concat(sb,
			"\t\treturn new ", qbClassSimpleName, "().getCount(", FILTER_CONFIG_PARAM_NAME, ", ", EM_FIELD_NAME, ");"
		) );
		addMethodEnd(fileLines);

		appendEmptyLine(fileLines); // new line after pair of list\count methods
	}

	private String getListMethodSignature() {
		return concat(sb,
			"List<", getListVOClassSimpleName(extEntity, entityClass), "> ", getListMethodName(extEntity, entityClass),
			"(",
				getFilterConfigClassSimpleName(extEntity, entityClass), " ", FILTER_CONFIG_PARAM_NAME, ", ",
				SortConfig.class.getSimpleName(), " ", SORT_CONFIG_PARAM_NAME,
			")"
		);
	}
	private String getCountMethodSignature() {
		return concat(sb,
			"int ", getCountMethodName(extEntity, entityClass),
			"(",
				getFilterConfigClassSimpleName(extEntity, entityClass), " ", FILTER_CONFIG_PARAM_NAME,
			")"
		);
	}

	private static final String FILTER_CONFIG_PARAM_NAME = "filterConfig";
	private static final String SORT_CONFIG_PARAM_NAME = "sortConfig";
}