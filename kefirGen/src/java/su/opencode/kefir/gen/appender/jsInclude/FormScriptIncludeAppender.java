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
import static su.opencode.kefir.gen.ExtEntityUtils.getFormJsFileName;
import static su.opencode.kefir.gen.ExtEntityUtils.getJsDirectory;

public class FormScriptIncludeAppender extends JsIncludeAppender
{
	public FormScriptIncludeAppender(String includeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) {
		super(includeFilePath, baseJsPath, getJsDirectory(extEntity, entityClass), getFormJsFileName(extEntity, entityClass));
	}
}