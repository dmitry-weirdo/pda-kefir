/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.util.List;

import su.opencode.kefir.srv.VO;
//	${APPEND_IMPORT}

@Local
public interface TestService
{
	List<TestEntityVO> getTestEntitys(TestEntityFilterConfig filterConfig, SortConfig sortConfig);
	int getTestEntitysCount(TestEntityFilterConfig filterConfig);

	TestEntity getTestEntity(Integer id);
	<T extends VO> T getTestEntityVO(Integer id, Class<T> voClass);
	Integer createTestEntity(TestEntity testEntity);
	void updateTestEntity(Integer id, TestEntity testEntity);
	void deleteTestEntity(Integer id);

	//	${APPEND_METHOD_DECLARATION}
}