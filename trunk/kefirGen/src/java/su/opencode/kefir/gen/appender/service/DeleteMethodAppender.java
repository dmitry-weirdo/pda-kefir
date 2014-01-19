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
import static su.opencode.kefir.gen.ExtEntityUtils.getDeleteMethodName;

import java.util.ArrayList;
import java.util.List;

public class DeleteMethodAppender extends ServiceMethodAppender
{
	public DeleteMethodAppender(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);
	}
	protected void appendImports(List<String> fileLines, List<String> imports) {
		// nothing needed to import
	}
	@Override
	protected List<String> getMethodSignatures() {
		List<String> signatures = new ArrayList<String>();
		signatures.add(getSignature());
		return signatures;
	}
	protected void appendMethods(List<String> fileLines) {
		addSignature(fileLines, getSignature());
		// add two empty lines after delete method
		appendEmptyLine(fileLines);
		appendEmptyLine(fileLines);
	}
	private String getSignature() {
		return concat(sb,
			"void ", getDeleteMethodName(extEntity, entityClass), "(Integer id)"
		);
	}
}