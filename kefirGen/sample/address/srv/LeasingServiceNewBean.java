/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.minstroy.ejb.leasing;

import org.apache.log4j.Logger;
import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.attachment.AttachmentService;
import su.opencode.kefir.srv.ClientException;

import javax.ejb.EJB;
// ${APPEND_IMPORT}

@Stateless
public class LeasingServiceNewBean implements LeasingServiceNew
{
	@SuppressWarnings(value = "unchecked")
	public List<DeveloperVO> getDevelopers(DeveloperFilterConfigNew filterConfig, SortConfig sortConfig) {
		return new DeveloperQueryBuilderNew().getList(filterConfig, sortConfig, em, DeveloperVO.class);
	}
	public int getDevelopersCount(DeveloperFilterConfigNew filterConfig) {
		return new DeveloperQueryBuilderNew().getCount(filterConfig, em);
	}

	public Developer getDeveloper(Integer id) {
		return em.find(Developer.class, id);
	}
	public <T extends VO> T getDeveloperVO(Integer id, Class<T> voClass) {
		Developer developer = em.find(Developer.class, id);
		if (developer == null)
			return null;

		return VO.newInstance(developer, voClass);
	}
	public Integer createDeveloper(Developer developer) {
		em.persist(developer.getJuridicalAddress());
		em.persist(developer.getPhysicalAddress());
		em.persist(developer);
		return developer.getId();
	}
	public void updateDeveloper(Integer id, Developer developer) {
		Developer existingDeveloper = em.find(Developer.class, id);
		if (existingDeveloper == null)
			return;

		em.merge(developer.getJuridicalAddress());
		em.merge(developer.getPhysicalAddress());

		developer.setId(id);
		em.merge(developer);
	}
	public void deleteDeveloper(Integer id) {
		Developer developer = em.find(Developer.class, id);
		if (developer == null)
			return;

		LeaseContractFilterConfig leaseContractFilterConfig = new LeaseContractFilterConfig();
		leaseContractFilterConfig.setDeveloperId(id);
		if ( leasingService.getLeaseContractsCount(leaseContractFilterConfig) > 0 )
			throw new ClientException("Невозможно удалить застройщика, т.к. существуют договоры аренды, связанные с ним.");

		attachmentService.deleteAttachments(Developer.class.getName(), id);

		em.remove(developer);

		em.remove(developer.getJuridicalAddress());
		em.remove(developer.getPhysicalAddress());
	}


	// ${APPEND_METHOD_IMPLEMENTATION}

	@EJB
	private AttachmentService attachmentService;

	@EJB
	private LeasingService leasingService;

	// ${APPEND_FIELD}
	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(LeasingServiceNewBean.class);
}