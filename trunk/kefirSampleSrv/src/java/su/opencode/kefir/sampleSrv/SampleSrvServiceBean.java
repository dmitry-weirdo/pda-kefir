/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import org.apache.log4j.Logger;
import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import su.opencode.kefir.generated.ChooseEntityFilterConfig;
import su.opencode.kefir.generated.ChooseEntityQueryBuilder;
// ${APPEND_IMPORT}

@Stateless
public class SampleSrvServiceBean implements SampleSrvService
{
	@SuppressWarnings(value = "unchecked")
	public List<ChooseEntityVO> getChooseEntitys(ChooseEntityFilterConfig filterConfig, SortConfig sortConfig) {
		return new ChooseEntityQueryBuilder().getList(filterConfig, sortConfig, em, ChooseEntityVO.class);
	}
	public int getChooseEntitysCount(ChooseEntityFilterConfig filterConfig) {
		return new ChooseEntityQueryBuilder().getCount(filterConfig, em);
	}

	// ${APPEND_METHOD_IMPLEMENTATION}

	// ${APPEND_FIELD}
	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(SampleSrvServiceBean.class);
}