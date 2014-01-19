/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.generated.ComboBoxEntityFilterConfig;
import su.opencode.kefir.generated.TestService;

import java.util.List;

public class ComboBoxEntitysListServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				SortConfig sortConfig = fillSortConfig();
				ComboBoxEntityFilterConfig filterConfig = fromJson(ComboBoxEntityFilterConfig.class);

				TestService service = getService(TestService.class);
				List<ComboBoxEntityVO> comboBoxEntitys = service.getComboBoxEntitys(filterConfig, sortConfig);
				int comboBoxEntitysCount = service.getComboBoxEntitysCount(filterConfig);

				writeJson(comboBoxEntitys, comboBoxEntitysCount);
			}
		};
	}
}