/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.generated.ComboBoxEntityFilterConfig;
import su.opencode.kefir.generated.TestService;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

import static su.opencode.kefir.sampleSrv.render.Renderers.getRenderInputStream;

public class ComboBoxEntitysListToExcelServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				SortConfig sortConfig = fillSortConfig();
				ComboBoxEntityFilterConfig filterConfig = fromJson(ComboBoxEntityFilterConfig.class);

				TestService service = getService(TestService.class);
				int comboBoxEntitysCount = service.getComboBoxEntitysCount(filterConfig);

				sortConfig.setStart(0);
				sortConfig.setLimit(comboBoxEntitysCount);

				List<ComboBoxEntityVO> comboBoxEntitys = service.getComboBoxEntitys(filterConfig, sortConfig);
				writeToExcel(comboBoxEntitys, sortConfig.getEntityName(), "comboBoxEntitysList", getRenderInputStream());
			}
		};
	}
}