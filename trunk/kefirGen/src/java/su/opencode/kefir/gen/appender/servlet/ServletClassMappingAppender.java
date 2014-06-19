/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 22.03.2012 17:09:20$
*/
package su.opencode.kefir.gen.appender.servlet;

import su.opencode.kefir.gen.ExtEntityUtils;

import static su.opencode.kefir.gen.ExtEntityUtils.SERVLET_URL_PREFIX;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.getValue;

/**
 * Добавляет маппинг сервлета по указанному классу сервлета.
 * <br/>
 * Если имя сервлета не задано, берется (имя сервлета без постфикса "Servlet" с маленькой буквы).
 * <br/>
 * Если урл сервлета не задан, берется ("/" + (имя сервлета без постфикса "Servlet") с маленькой буквы).
 */
public class ServletClassMappingAppender extends ServletMappingAppender
{
	public ServletClassMappingAppender(String webXmlPath, Class servletClass) {
		super(webXmlPath, null, null);
		this.servletClass = servletClass;
	}
	public ServletClassMappingAppender(String webXmlPath, Class servletClass, String servletName, String servletUrl) {
		super(webXmlPath, null, null);
		this.servletClass = servletClass;
		this.name = servletName;
		this.url = servletUrl;
	}

	@Override
	protected String getServletClassFullName() {
		return servletClass.getName();
	}
	@Override
	protected String getServletName() {
		return getValue(name, getServletSimpleName());
	}
	@Override
	protected String getServletUrl() {
		return getValue(url, concat(sb, SERVLET_URL_PREFIX, getServletSimpleName()));
	}

	private String getServletSimpleName() {
		String simpleName = ExtEntityUtils.getSimpleName(servletClass);

		if ( !simpleName.endsWith(SERVLET_CLASS_NAME_POSTFIX) )
			return simpleName;

		return simpleName.substring(0, simpleName.length() - SERVLET_CLASS_NAME_POSTFIX.length());
	}

	protected Class servletClass;
	protected String name;
	protected String url;

	public static final String SERVLET_CLASS_NAME_POSTFIX = "Servlet";
}