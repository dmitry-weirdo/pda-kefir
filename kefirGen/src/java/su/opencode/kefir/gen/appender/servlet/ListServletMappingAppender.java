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

public class ListServletMappingAppender extends ServletMappingAppender
{
	public ListServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		super(webXmlPath, extEntity, entityClass);
	}
	protected String getServletClassFullName() {
		return getListServletClassFullName(extEntity, entityClass);
	}
	protected String getServletName() {
		return getListServletName(extEntity, entityClass);
	}
	protected String getServletUrl() {
		return getListServletUrl(extEntity, entityClass);
	}
}