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
import su.opencode.kefir.sampleSrv.ChooseEntity;
import su.opencode.kefir.selenium.AbstractTestCase;
import su.opencode.kefir.selenium.pages.ChooseEntityFormPage;
import su.opencode.kefir.selenium.pages.ChooseEntityListPage;
import su.opencode.kefir.selenium.pages.MainPage;
import su.opencode.kefir.selenium.test.dataprovider.ChooseEntityDataProvider;

import static su.opencode.kefir.selenium.test.dataprovider.ChooseEntityDataProvider.DATA_PROVIDER_NAME;

public class ChooseEntityTests extends AbstractTestCase
{
	private ChooseEntityListPage getListPage() {
		return new MainPage().clickButtonChooseEntityList();
	}
	@Test(dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ChooseEntityDataProvider.class)
	public void testCreate(ChooseEntity createEntity, ChooseEntity updateEntity) {
		createEntity(getListPage(), createEntity, mainPageClass);
	}
	@Test(dependsOnMethods = "testCreate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ChooseEntityDataProvider.class)
	public void testShow(ChooseEntity createEntity, ChooseEntity updateEntity) {
		showEntity(getListPage(), createEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testShow", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ChooseEntityDataProvider.class)
	public void testUpdate(ChooseEntity createEntity, ChooseEntity updateEntity) {
		updateEntity(getListPage(), createEntity, updateEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testUpdate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ChooseEntityDataProvider.class)
	public void testDelete(ChooseEntity createEntity, ChooseEntity updateEntity) {
		deleteEntity(getListPage(), updateEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testDelete", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ChooseEntityDataProvider.class)
	public void testCreateIfNotExists(ChooseEntity createEntity, ChooseEntity updateEntity) {

		final ChooseEntityListPage listPage = getListPage();

		if (!selectEntityFromGrid(listPage, updateEntity))
			createEntity(listPage, updateEntity, mainPageClass);
	}

	private static final Class<ChooseEntityFormPage> formPageClass = ChooseEntityFormPage.class;
	private static final Class<MainPage> mainPageClass = MainPage.class;
}
