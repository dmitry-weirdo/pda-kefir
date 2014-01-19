/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.web.fileupload;

import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.srv.attachment.AttachmentFilterConfig;
import su.opencode.kefir.srv.attachment.AttachmentService;
import su.opencode.kefir.srv.attachment.AttachmentVO;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

public class AttachmentsListServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				SortConfig sortConfig = fillSortConfig();
				AttachmentFilterConfig filterConfig = fromJson(AttachmentFilterConfig.class);

				AttachmentService service = getService(AttachmentService.class);
				List<AttachmentVO> attachments = service.getAttachments(filterConfig, sortConfig);
				int attachmentsCount = service.getAttachmentsCount(filterConfig);

				writeJson(attachments, attachmentsCount);
			}
		};
	}
}