/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import org.apache.log4j.Logger;
import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import su.opencode.kefir.srv.VO;
import su.opencode.minstroy.ejb.attachment.AttachmentService;
//	${APPEND_IMPORT}

@Stateless
public class TestServiceBean implements TestService
{
	@SuppressWarnings(value = "unchecked")
	public List<TestEntityVO> getTestEntitys(TestEntityFilterConfig filterConfig, SortConfig sortConfig) {
		return new TestEntityQueryBuilder().getList(filterConfig, sortConfig, em, TestEntityVO.class);
	}
	public int getTestEntitysCount(TestEntityFilterConfig filterConfig) {
		return new TestEntityQueryBuilder().getCount(filterConfig, em);
	}

	public TestEntity getTestEntity(Integer id) {
		return em.find(TestEntity.class, id);
	}
	public <T extends VO> T getTestEntityVO(Integer id, Class<T> voClass) {
		TestEntity testEntity = em.find(TestEntity.class, id);
		if (testEntity == null)
			return null;

		return VO.newInstance(testEntity, voClass);
	}
	public Integer createTestEntity(TestEntity testEntity) {
		em.persist(testEntity);
		return testEntity.getId();
	}
	public void updateTestEntity(Integer id, TestEntity testEntity) {
		TestEntity existingTestEntity = em.find(TestEntity.class, id);
		if (existingTestEntity == null)
			return;

		testEntity.setId(id);
		em.merge(testEntity);
	}
	public void deleteTestEntity(Integer id) {
		TestEntity testEntity = em.find(TestEntity.class, id);
		if (testEntity == null)
			return;

		attachmentService.deleteAttachments(TestEntity.class.getName(), id);
		em.remove(testEntity);
	}

	// ${APPEND_METHOD_IMPLEMENTATION}

	private StringBuffer sb = new StringBuffer();

	// ${APPEND_FIELD}
	@EJB
	private AttachmentService attachmentService;

	@PersistenceContext
	private EntityManager em;

	private static final Logger logger = Logger.getLogger(TestServiceBean.class);
}