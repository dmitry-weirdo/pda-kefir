/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 14.09.2010 13:19:52$
*/
package su.opencode.kefir.web.util;

import javax.servlet.*;
import java.io.IOException;

public class CharsetFilter implements Filter
{
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = filterConfig.getInitParameter(REQUEST_ENCODING_PARAM_NAME);

		if (encoding == null)
			encoding = "utf-8";

		if (!"true".equals(filterConfig.getInitParameter("ServiceFactory.Config.disable")))
			factoryConfig = ServiceFactory.configure(filterConfig.getServletContext());
	}
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		servletRequest.setCharacterEncoding(encoding);
		servletResponse.setCharacterEncoding(encoding);

		if (factoryConfig != null)
		{
			ServiceFactory.register(factoryConfig);
			filterChain.doFilter(servletRequest, servletResponse);
			ServiceFactory.unregister();
		}
		else
		{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
	public void destroy() {
		// nothing needed
	}

	private String encoding;
	private ServiceFactory.Config factoryConfig;

	public static final String REQUEST_ENCODING_PARAM_NAME = "requestEncoding";
}