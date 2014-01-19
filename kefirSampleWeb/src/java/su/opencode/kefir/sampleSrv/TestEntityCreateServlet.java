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
import su.opencode.kefir.generated.TestService;
import su.opencode.kefir.srv.attachment.AttachmentService;

public class TestEntityCreateServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				TestService service = getService(TestService.class);

				TestEntity testEntity = fromJson(TestEntity.class);
				Integer id = service.createTestEntity(testEntity);

				// fill entityId for attachments
				AttachmentService attachmentService = getService(AttachmentService.class);
				for (Integer attachmentsFieldId : getCheckGridIds("attachmentsFieldIds"))
					attachmentService.setAttachmentEntityId(attachmentsFieldId, id);

				for (Integer otherAttachmentsFieldId : getCheckGridIds("otherAttachmentsFieldIds"))
					attachmentService.setAttachmentEntityId(otherAttachmentsFieldId, id);

				writeSuccess(testEntity);
			}
		};
	}
}