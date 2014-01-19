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

import java.util.List;

public class TestEntitysListToExcelServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				SortConfig sortConfig = fillSortConfig();
				TestEntityFilterConfig filterConfig = fromJson(TestEntityFilterConfig.class);

				TestService service = getService(TestService.class);
				int testEntitysCount = service.getTestEntitysCount(filterConfig);

				sortConfig.setStart(0);
				sortConfig.setLimit(testEntitysCount);

				List<TestEntityVO> testEntitys = service.getTestEntitys(filterConfig, sortConfig);
				writeToExcel(testEntitys, sortConfig.getEntityName(), "testEntitysList");
			}
		};
	}
}