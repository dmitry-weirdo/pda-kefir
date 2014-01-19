/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 09.11.2010 14:26:20$
*/
package su.opencode.kefir.web;

import org.json.JSONObject;
import su.opencode.kefir.srv.dynamicGrid.DynamicColumnVO;
import su.opencode.kefir.srv.dynamicGrid.DynamicGridService;
import su.opencode.kefir.srv.VO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetGridParamsServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				final String entityName = getStringParam("entityName", false);
				final DynamicGridService dynamicGridService = getService(DynamicGridService.class);
				final VO vo = VO.getInstance(entityName);

				final Map<String, Object> servicesMap = new HashMap<String, Object>();
				final String[] stringList = vo.getServices();
				if (stringList != null)
				{
					for (String s : stringList)
						putService(servicesMap, s);
				}

				final List<DynamicColumnVO> dynamicColumns = vo.getDynamicGridColumns(servicesMap);

				final JSONObject jsonObject = dynamicGridService.getDynamicGridParams(entityName, dynamicColumns);
				writeJson(jsonObject, false);
			}

			private void putService(Map<String, Object> map, String serviceClass) {
				map.put(serviceClass, getService(serviceClass));
			}
		};
	}
}