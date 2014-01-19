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
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.ClientException;
// ${APPEND_IMPORT}

@Stateless
public class LeasingServiceNewBean implements LeasingServiceNew
{
	@SuppressWarnings(value = "unchecked")
	public List<ParcelVO> getParcels(ParcelFilterConfigNew filterConfig, SortConfig sortConfig) {
		return new ParcelQueryBuilderNew().getList(filterConfig, sortConfig, em, ParcelVO.class);
	}
	public int getParcelsCount(ParcelFilterConfigNew filterConfig) {
		return new ParcelQueryBuilderNew().getCount(filterConfig, em);
	}

	public Parcel getParcel(Integer id) {
		return em.find(Parcel.class, id);
	}
	public <T extends VO> T getParcelVO(Integer id, Class<T> voClass) {
		Parcel parcel = em.find(Parcel.class, id);
		if (parcel == null)
			return null;

		return VO.newInstance(parcel, voClass);
	}
	public Integer createParcel(Parcel parcel) {
		em.persist(parcel);
		return parcel.getId();
	}
	public void updateParcel(Integer id, Parcel parcel) {
		Parcel existingParcel = em.find(Parcel.class, id);
		if (existingParcel == null)
			return;

		parcel.setId(id);
		em.merge(parcel);
	}
	public void deleteParcel(Integer id) {
		Parcel parcel = em.find(Parcel.class, id);
		if (parcel == null)
			return;

		LeaseContractFilterConfig leaseContractFilterConfig = new LeaseContractFilterConfig();
		leaseContractFilterConfig.setParcelId(id);
		if ( leasingService.getLeaseContractsCount(leaseContractFilterConfig) > 0 )
			throw new ClientException("Невозможно удалить земельный участок, т.к. существуют договоры аренды, связанные с ним.");

		em.remove(parcel);
	}


	// ${APPEND_METHOD_IMPLEMENTATION}

	// ${APPEND_FIELD}
	@PersistenceContext
	private EntityManager em;

	@EJB
	private LeasingService leasingService;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(LeasingServiceNewBean.class);
}