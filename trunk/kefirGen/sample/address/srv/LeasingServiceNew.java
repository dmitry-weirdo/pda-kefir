/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.minstroy.ejb.leasing;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.util.List;

import su.opencode.kefir.srv.VO;
//	${APPEND_IMPORT}

@Local
public interface LeasingServiceNew
{
	List<DeveloperVO> getDevelopers(DeveloperFilterConfigNew filterConfig, SortConfig sortConfig);
	int getDevelopersCount(DeveloperFilterConfigNew filterConfig);

	Developer getDeveloper(Integer id);
	<T extends VO> T getDeveloperVO(Integer id, Class<T> voClass);
	Integer createDeveloper(Developer developer);
	void updateDeveloper(Integer id, Developer developer);
	void deleteDeveloper(Integer id);


	// ${APPEND_METHOD_DECLARATION}
}