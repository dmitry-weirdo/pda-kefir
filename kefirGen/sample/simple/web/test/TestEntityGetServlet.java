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
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.web.JsonServlet;

public class TestEntityGetServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				TestService service = getService(TestService.class);

				String entityName = getEntityName();
				if (entityName == null || entityName.isEmpty())
				{ // return entity
					TestEntity testEntity = service.getTestEntity( getId() );
					writeJson(testEntity);
				}
				else
				{ // return VO
					VO testEntityVO = service.getTestEntityVO(getId(), getVOClass(entityName));
					writeJson(testEntityVO);
				}
			}
		};
	}
}