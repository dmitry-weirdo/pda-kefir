/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.service;

import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.gen.ExtEntity;

import static su.opencode.kefir.gen.ExtEntityUtils.getSimpleName;
import static su.opencode.kefir.gen.ExtEntityUtils.getUpdateMethodName;

import java.util.ArrayList;
import java.util.List;

public class UpdateMethodAppender extends ServiceMethodAppender
{
	public UpdateMethodAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}
	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что сервис -> добавить импорт сущности
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<String>();
		signatures.add(getSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		addSignature(fileLines, getSignature());
	}
	private String getSignature() {
		return concat(sb,
			"void ", getUpdateMethodName(extEntity, entityClass), "(Integer id, ", entityClass.getSimpleName(), " ", getSimpleName(entityClass), ")"
		);
	}
}