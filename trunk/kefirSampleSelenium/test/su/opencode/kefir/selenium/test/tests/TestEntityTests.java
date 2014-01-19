/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.test.tests;

import org.testng.annotations.Test;
import su.opencode.kefir.sampleSrv.TestEntity;
import su.opencode.kefir.selenium.AbstractTestCase;
import su.opencode.kefir.selenium.pages.MainPage;
import su.opencode.kefir.selenium.pages.TestEntityFormPage;
import su.opencode.kefir.selenium.pages.TestEntityListPage;
import su.opencode.kefir.selenium.pages.TestEntityFormPage;
import su.opencode.kefir.selenium.pages.TestEntityListPage;
import su.opencode.kefir.selenium.test.dataprovider.TestEntityDataProvider;

import static su.opencode.kefir.selenium.test.dataprovider.TestEntityDataProvider.DATA_PROVIDER_NAME;

public class TestEntityTests extends AbstractTestCase
{
	private TestEntityListPage getListPage() {
		return new MainPage().clickButtonTestEntitiesList();
	}

	@Test(dataProvider = DATA_PROVIDER_NAME, dataProviderClass = TestEntityDataProvider.class)
	public void testCreate(TestEntity createEntity, TestEntity updateEntity) {
		createEntity(getListPage(), createEntity, mainPageClass);
	}

	@Test(dependsOnMethods = "testCreate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = TestEntityDataProvider.class)
	public void testShow(TestEntity createEntity, TestEntity updateEntity) {
		showEntity(getListPage(), createEntity, formPageClass, mainPageClass);
	}

	@Test(dependsOnMethods = "testShow", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = TestEntityDataProvider.class)
	public void testUpdate(TestEntity createEntity, TestEntity updateEntity) {
		updateEntity(getListPage(), createEntity, updateEntity, formPageClass, mainPageClass);
	}

	@Test(dependsOnMethods = "testUpdate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = TestEntityDataProvider.class)
	public void testDelete(TestEntity createEntity, TestEntity updateEntity) {
		deleteEntity(getListPage(), updateEntity, formPageClass, mainPageClass);
	}

	@Test(dependsOnMethods = "testDelete", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = TestEntityDataProvider.class)
	public void testCreateIfNotExists(TestEntity createEntity, TestEntity updateEntity) {

		final TestEntityListPage listPage = getListPage();

		if (!selectEntityFromGrid(listPage, updateEntity))
			createEntity(listPage, updateEntity, mainPageClass);
	}

	private static final Class<TestEntityFormPage> formPageClass = TestEntityFormPage.class;
	private static final Class<MainPage> mainPageClass = MainPage.class;
}
