/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import org.apache.log4j.Logger;
import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.srv.SortConfig;
import su.opencode.kefir.srv.VO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
//	${APPEND_IMPORT}

@Stateless
public class AttachmentServiceBean implements AttachmentService
{
	@SuppressWarnings(value = "unchecked")
	public List<AttachmentVO> getAttachments(AttachmentFilterConfig filterConfig, SortConfig sortConfig) {
		return new AttachmentQueryBuilder().getList(filterConfig, sortConfig, em, AttachmentVO.class);
	}
	public int getAttachmentsCount(AttachmentFilterConfig filterConfig) {
		return new AttachmentQueryBuilder().getCount(filterConfig, em);
	}

	public Attachment getAttachment(Integer id) {
		return em.find(Attachment.class, id);
	}
	public <T extends VO> T getAttachmentVO(Integer id, Class<T> voClass) {
		Attachment attachment = em.find(Attachment.class, id);
		if (attachment == null)
			return null;

		return VO.newInstance(attachment, voClass);
	}
	public Integer createAttachment(Attachment attachment) {
		em.persist(attachment);
		return attachment.getId();
	}
	public void updateAttachment(Integer id, Attachment attachment) {
		Attachment existingAttachment = em.find(Attachment.class, id);
		if (existingAttachment == null)
			return;

		attachment.setId(id);
		em.merge(attachment);
	}
	public void setAttachmentEntityId(Integer attachmentId, Integer entityId) {
		Attachment attachment = getAttachment(attachmentId);
		if (attachment == null)
			return;

		attachment.setEntityId(entityId);
	}
	public void deleteAttachment(Integer id) {
		Attachment attachment = em.find(Attachment.class, id);
		if (attachment == null)
			return;

		em.remove(attachment);
	}
	public void deleteAttachments(String entityName, Integer entityId) {
		logger.info( concat(sb, "removing attachments for entityName = ", entityName, ", id = ", entityId) );

		AttachmentFilterConfig filterConfig = new AttachmentFilterConfig();
		filterConfig.setEntityName(entityName);
		filterConfig.setEntityId(entityId);

		SortConfig sortConfig = new SortConfig();
		sortConfig.setSortBy("id");

		for (AttachmentVO attachment : getAttachments(filterConfig, sortConfig))
			deleteAttachment(attachment.getId());
	}

	//	${APPEND_METHOD_IMPLEMENTATION}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(AttachmentServiceBean.class);
}