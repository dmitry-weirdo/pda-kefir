/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.servlet;

import su.opencode.kefir.gen.ExtEntity;

import static su.opencode.kefir.gen.ExtEntityUtils.*;

public class CreateServletMappingAppender extends ServletMappingAppender
{
	public CreateServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		super(webXmlPath, extEntity, entityClass);
	}
	protected String getServletClassFullName() {
		return getCreateServletClassFullName(extEntity, entityClass);
	}
	protected String getServletName() {
		return getCreateServletName(extEntity, entityClass);
	}
	protected String getServletUrl() {
		return getCreateServletUrl(extEntity, entityClass);
	}
}