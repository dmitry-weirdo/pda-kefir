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

public class GetServletMappingAppender extends ServletMappingAppender
{
	public GetServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		super(webXmlPath, extEntity, entityClass);
	}
	protected String getServletClassFullName() {
		return getGetServletClassFullName(extEntity, entityClass);
	}
	protected String getServletName() {
		return getGetServletName(extEntity, entityClass);
	}
	protected String getServletUrl() {
		return getGetServletUrl(extEntity, entityClass);
	}
}