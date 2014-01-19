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

import static su.opencode.kefir.gen.ExtEntityUtils.getChooseJsFileName;
import static su.opencode.kefir.gen.ExtEntityUtils.getJsDirectory;

public class ChooseScriptIncludeAppender extends JsIncludeAppender
{
	public ChooseScriptIncludeAppender(String includeFilePath, String baseJsPath, ExtEntity extEntity, Class entityClass) {
		super(includeFilePath, baseJsPath, getJsDirectory(extEntity, entityClass), getChooseJsFileName(extEntity, entityClass));
	}
}