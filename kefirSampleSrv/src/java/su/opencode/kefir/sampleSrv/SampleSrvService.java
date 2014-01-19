/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.util.List;

import su.opencode.kefir.generated.ChooseEntityFilterConfig;
//	${APPEND_IMPORT}

@Local
public interface SampleSrvService
{
	List<ChooseEntityVO> getChooseEntitys(ChooseEntityFilterConfig filterConfig, SortConfig sortConfig);
	int getChooseEntitysCount(ChooseEntityFilterConfig filterConfig);

	// ${APPEND_METHOD_DECLARATION}
}