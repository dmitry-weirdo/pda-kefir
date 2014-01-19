/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;
import su.opencode.minstroy.ejb.attachment.AttachmentService;

public class TestEntityCreateServlet extends JsonServlet
{
	protected su.opencode.kefir.web.Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				TestService service = getService(TestService.class);

				TestEntity testEntity = fromJson(TestEntity.class);

				// attachments variation
				Integer id = service.createTestEntity(testEntity);

				// проставить entityId всем связанным аттачам (там id не было, т.к. сущность не была создана) 
				AttachmentService attachmentService = getService(AttachmentService.class);
				for (Integer attachmentsFieldId : getCheckGridIds("attachmentsFieldIds"))
					attachmentService.setAttachmentEntityId(attachmentsFieldId, id);

				// non-attachments
//				service.createTestEntity(testEntity);

				writeSuccess(testEntity);
			}
		};
	}
}