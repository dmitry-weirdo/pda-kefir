/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.jsInclude;

import su.opencode.kefir.gen.ExtEntity;

import static su.opencode.kefir.gen.ExtEntityUtils.getFormLayoutJsFileName;
import static su.opencode.kefir.gen.ExtEntityUtils.getJsDirectory;

public class FormLayoutScriptIncludeAppender extends JsIncludeAppender
{
	public FormLayoutScriptIncludeAppender(String includeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) {
		super(includeFilePath, baseJsPath, getJsDirectory(extEntity, entityClass), getFormLayoutJsFileName(extEntity, entityClass));
	}
}