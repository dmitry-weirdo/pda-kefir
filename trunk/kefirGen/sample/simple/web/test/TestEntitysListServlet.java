/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.web.JsonServlet;
import test.TestEntityFilterConfig;
import test.TestService;
import test.TestEntityVO;

import java.util.List;

public class TestEntitysListServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				SortConfig sortConfig = fillSortConfig();
				TestEntityFilterConfig filterConfig = fromJson(TestEntityFilterConfig.class);

				TestService service = getService(TestService.class);
				List<TestEntityVO> testEntitys = service.getTestEntitys(filterConfig, sortConfig);
				int testEntitysCount = service.getTestEntitysCount(filterConfig);

				writeJson(testEntitys, testEntitysCount);
			}
		};
	}
}