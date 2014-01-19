/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.generated;

import org.apache.log4j.Logger;
import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import su.opencode.kefir.sampleSrv.ComboBoxEntityVO;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.sampleSrv.ComboBoxEntity;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.sampleSrv.ChooseEntityVO;
import su.opencode.kefir.sampleSrv.ChooseEntity;
import su.opencode.kefir.sampleSrv.TestEntityVO;
import su.opencode.kefir.sampleSrv.TestEntity;
import su.opencode.kefir.srv.attachment.AttachmentService;
import javax.ejb.EJB;
// ${APPEND_IMPORT}

@Stateless
public class TestServiceBean implements TestService
{
	@SuppressWarnings(value = "unchecked")
	public List<ComboBoxEntityVO> getComboBoxEntitys(ComboBoxEntityFilterConfig filterConfig, SortConfig sortConfig) {
		return new ComboBoxEntityQueryBuilder().getList(filterConfig, sortConfig, em, ComboBoxEntityVO.class);
	}
	public int getComboBoxEntitysCount(ComboBoxEntityFilterConfig filterConfig) {
		return new ComboBoxEntityQueryBuilder().getCount(filterConfig, em);
	}

	public ComboBoxEntity getComboBoxEntity(Integer id) {
		return em.find(ComboBoxEntity.class, id);
	}
	public <T extends VO> T getComboBoxEntityVO(Integer id, Class<T> voClass) {
		ComboBoxEntity comboBoxEntity = em.find(ComboBoxEntity.class, id);
		if (comboBoxEntity == null)
			return null;

		return VO.newInstance(comboBoxEntity, voClass);
	}
	public Integer createComboBoxEntity(ComboBoxEntity comboBoxEntity) {
		em.persist(comboBoxEntity);
		return comboBoxEntity.getId();
	}
	public void updateComboBoxEntity(Integer id, ComboBoxEntity comboBoxEntity) {
		ComboBoxEntity existingComboBoxEntity = em.find(ComboBoxEntity.class, id);
		if (existingComboBoxEntity == null)
			return;

		comboBoxEntity.setId(id);
		em.merge(comboBoxEntity);
	}
	public void deleteComboBoxEntity(Integer id) {
		ComboBoxEntity comboBoxEntity = em.find(ComboBoxEntity.class, id);
		if (comboBoxEntity == null)
			return;

		TestEntityFilterConfig testEntityFilterConfig = new TestEntityFilterConfig();
		testEntityFilterConfig.setComboBoxEntityId(id);
		if ( getTestEntitysCount(testEntityFilterConfig) > 0 )
			throw new ClientException("Невозможно удалить комбо сущность, т.к. существует тестовая сущность, связанная с ней.");

		em.remove(comboBoxEntity);
	}


	@SuppressWarnings(value = "unchecked")
	public List<ChooseEntityVO> getChooseEntitys(ChooseEntityFilterConfig filterConfig, SortConfig sortConfig) {
		return new ChooseEntityQueryBuilder().getList(filterConfig, sortConfig, em, ChooseEntityVO.class);
	}
	public int getChooseEntitysCount(ChooseEntityFilterConfig filterConfig) {
		return new ChooseEntityQueryBuilder().getCount(filterConfig, em);
	}

	public ChooseEntity getChooseEntity(Integer id) {
		return em.find(ChooseEntity.class, id);
	}
	public <T extends VO> T getChooseEntityVO(Integer id, Class<T> voClass) {
		ChooseEntity chooseEntity = em.find(ChooseEntity.class, id);
		if (chooseEntity == null)
			return null;

		return VO.newInstance(chooseEntity, voClass);
	}
	public Integer createChooseEntity(ChooseEntity chooseEntity) {
		em.persist(chooseEntity);
		return chooseEntity.getId();
	}
	public void updateChooseEntity(Integer id, ChooseEntity chooseEntity) {
		ChooseEntity existingChooseEntity = em.find(ChooseEntity.class, id);
		if (existingChooseEntity == null)
			return;

		chooseEntity.setId(id);
		em.merge(chooseEntity);
	}
	public void deleteChooseEntity(Integer id) {
		ChooseEntity chooseEntity = em.find(ChooseEntity.class, id);
		if (chooseEntity == null)
			return;

		TestEntityFilterConfig testEntityFilterConfig = new TestEntityFilterConfig();
		testEntityFilterConfig.setChooseEntityId(id);
		if ( getTestEntitysCount(testEntityFilterConfig) > 0 )
			throw new ClientException("Невозможно удалить связанную сущность, т.к. существует тестовая сущность, связанная с ней.");

		em.remove(chooseEntity);
	}


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
		em.persist(testEntity.getJuridicalAddress());
		em.persist(testEntity.getPhysicalAddress());

		em.persist(testEntity);
		return testEntity.getId();
	}
	public void updateTestEntity(Integer id, TestEntity testEntity) {
		TestEntity existingTestEntity = em.find(TestEntity.class, id);
		if (existingTestEntity == null)
			return;

		em.merge(testEntity.getJuridicalAddress());
		em.merge(testEntity.getPhysicalAddress());

		testEntity.setId(id);
		em.merge(testEntity);
	}
	public void deleteTestEntity(Integer id) {
		TestEntity testEntity = em.find(TestEntity.class, id);
		if (testEntity == null)
			return;

		attachmentService.deleteAttachments(TestEntity.class.getName(), id);

		em.remove(testEntity);

		em.remove(testEntity.getJuridicalAddress());
		em.remove(testEntity.getPhysicalAddress());
	}


	// ${APPEND_METHOD_IMPLEMENTATION}

	@EJB
	private AttachmentService attachmentService;

	// ${APPEND_FIELD}
	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(TestServiceBean.class);
}