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

public class ListToExcelServletMappingAppender extends ServletMappingAppender
{
	public ListToExcelServletMappingAppender(String webXmlPath, ExtEntity extEntity, Class entityClass) {
		super(webXmlPath, extEntity, entityClass);
	}
	protected String getServletClassFullName() {
		return getListExportToExcelServletClassFullName(extEntity, entityClass);
	}
	protected String getServletName() {
		return getListExportToExcelServletName(extEntity, entityClass);
	}
	protected String getServletUrl() {
		return getListExportToExcelServletUrl(extEntity, entityClass);
	}
}