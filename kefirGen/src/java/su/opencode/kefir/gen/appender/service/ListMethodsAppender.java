/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.service;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.srv.SortConfig;

import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.gen.ExtEntityUtils.*;

public class ListMethodsAppender extends ServiceMethodAppender
{
	public ListMethodsAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}

	protected void appendImports(List<String> fileLines, List<String> imports) {
		// на случай, если они были удалены при оптимизации импортов
		addImport( SortConfig.class, fileLines, imports );
		addImport( List.class, fileLines, imports );

		addImport( getListVOClassName(extEntity, entityClass), fileLines, imports ); // если VO не в том же пакете, что сервис -> добавить импорт VO
		addImport( getFilterConfigClassName(extEntity, entityClass), fileLines, imports ); // если FilterConfig не в том же пакете, что сервис -> добавить импорт FilterConfig
	}

	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<String>();
		signatures.add(getListMethodSignature());
		signatures.add(getCountMethodSignature());
		return signatures;
	}

	protected void appendMethods(List<String> fileLines) {
		addSignature(fileLines, getListMethodSignature());
		addSignature(fileLines, getCountMethodSignature());
		appendEmptyLine(fileLines); // blank line after list methods
	}
	private String getListMethodSignature() {
		return concat(sb,
			"List<", getListVOClassSimpleName(extEntity, entityClass), "> ", getListMethodName(extEntity, entityClass),
			"(",
				getFilterConfigClassSimpleName(extEntity, entityClass), " filterConfig, ",
				SortConfig.class.getSimpleName(), " sortConfig",
			")"
		);
	}
	private String getCountMethodSignature() {
		return concat(sb,
			"int ", getCountMethodName(extEntity, entityClass),
			"(",
				getFilterConfigClassSimpleName(extEntity, entityClass), " filterConfig",
			")"
		);
	}
}