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

public class DeleteServletMappingAppender extends ServletMappingAppender
{
	public DeleteServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		super(webXmlPath, extEntity, entityClass);
	}
	protected String getServletClassFullName() {
		return getDeleteServletClassFullName(extEntity, entityClass);
	}
	protected String getServletName() {
		return getDeleteServletName(extEntity, entityClass);
	}
	protected String getServletUrl() {
		return getDeleteServletUrl(extEntity, entityClass);
	}
}