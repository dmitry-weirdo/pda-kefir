/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.srv.VO;

import javax.ejb.Local;
import java.util.List;
//	${APPEND_IMPORT}

@Local
public interface AttachmentService
{
	List<AttachmentVO> getAttachments(AttachmentFilterConfig filterConfig, SortConfig sortConfig);
	int getAttachmentsCount(AttachmentFilterConfig filterConfig);

	Attachment getAttachment(Integer id);
	<T extends VO> T getAttachmentVO(Integer id, Class<T> voClass);
	Integer createAttachment(Attachment attachment);
	void updateAttachment(Integer id, Attachment attachment);
	void setAttachmentEntityId(Integer attachmentId, Integer entityId);
	void deleteAttachment(Integer id);
	void deleteAttachments(String entityName, Integer entityId);

//	${APPEND_METHOD_DECLARATION}
}