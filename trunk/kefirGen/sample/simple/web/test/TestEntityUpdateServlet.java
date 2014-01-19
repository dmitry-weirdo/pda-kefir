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
import su.opencode.kefir.web.JsonServlet;

public class TestEntityUpdateServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				TestService service = getService(TestService.class);

				TestEntity testEntity = fromJson(TestEntity.class);
				service.updateTestEntity(getId(), testEntity);

				writeSuccess();
			}
		};
	}
}