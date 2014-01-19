/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.generated;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.util.List;

import su.opencode.kefir.sampleSrv.ComboBoxEntityVO;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.sampleSrv.ComboBoxEntity;
import su.opencode.kefir.sampleSrv.ChooseEntityVO;
import su.opencode.kefir.sampleSrv.ChooseEntity;
import su.opencode.kefir.sampleSrv.TestEntityVO;
import su.opencode.kefir.sampleSrv.TestEntity;
//	${APPEND_IMPORT}

@Local
public interface TestService
{
	List<ComboBoxEntityVO> getComboBoxEntitys(ComboBoxEntityFilterConfig filterConfig, SortConfig sortConfig);
	int getComboBoxEntitysCount(ComboBoxEntityFilterConfig filterConfig);

	ComboBoxEntity getComboBoxEntity(Integer id);
	<T extends VO> T getComboBoxEntityVO(Integer id, Class<T> voClass);
	Integer createComboBoxEntity(ComboBoxEntity comboBoxEntity);
	void updateComboBoxEntity(Integer id, ComboBoxEntity comboBoxEntity);
	void deleteComboBoxEntity(Integer id);


	List<ChooseEntityVO> getChooseEntitys(ChooseEntityFilterConfig filterConfig, SortConfig sortConfig);
	int getChooseEntitysCount(ChooseEntityFilterConfig filterConfig);

	ChooseEntity getChooseEntity(Integer id);
	<T extends VO> T getChooseEntityVO(Integer id, Class<T> voClass);
	Integer createChooseEntity(ChooseEntity chooseEntity);
	void updateChooseEntity(Integer id, ChooseEntity chooseEntity);
	void deleteChooseEntity(Integer id);


	List<TestEntityVO> getTestEntitys(TestEntityFilterConfig filterConfig, SortConfig sortConfig);
	int getTestEntitysCount(TestEntityFilterConfig filterConfig);

	TestEntity getTestEntity(Integer id);
	<T extends VO> T getTestEntityVO(Integer id, Class<T> voClass);
	Integer createTestEntity(TestEntity testEntity);
	void updateTestEntity(Integer id, TestEntity testEntity);
	void deleteTestEntity(Integer id);


	// ${APPEND_METHOD_DECLARATION}
}