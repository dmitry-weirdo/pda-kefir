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
import su.opencode.kefir.srv.VO;

import static su.opencode.kefir.gen.ExtEntityUtils.getGetMethodName;
import static su.opencode.kefir.gen.ExtEntityUtils.getGetVOMethodName;

import java.util.ArrayList;
import java.util.List;

public class GetMethodsAppender extends ServiceMethodAppender
{
	public GetMethodsAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}
	protected void appendImports(List<String> fileLines, List<String> imports) {
		addImport(VO.class, fileLines, imports);
		addImport( entityClass.getName(), fileLines, imports ); // если сущность не в том же пакете, что сервис -> добавить импорт сущности
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<String>();
		signatures.add(getGetEntityMethodSignature());
		signatures.add(getGetVOMethodSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		addSignature(fileLines, getGetEntityMethodSignature());
		addSignature(fileLines, getGetVOMethodSignature());
	}
	private String getGetEntityMethodSignature() {
		return concat(sb,
			entityClass.getSimpleName(), " ", getGetMethodName(extEntity, entityClass), "(Integer id)"
		);
	}
	private String getGetVOMethodSignature() {
		return concat(sb,
			"<T extends ", VO.class.getSimpleName(), "> T ", getGetVOMethodName(extEntity, entityClass), "(Integer id, Class<T> voClass)"
		);
	}
}