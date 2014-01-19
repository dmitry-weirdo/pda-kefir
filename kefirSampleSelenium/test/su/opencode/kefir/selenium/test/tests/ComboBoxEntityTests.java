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
import su.opencode.kefir.sampleSrv.ComboBoxEntity;
import su.opencode.kefir.selenium.AbstractTestCase;
import su.opencode.kefir.selenium.pages.ComboBoxEntityFormPage;
import su.opencode.kefir.selenium.pages.ComboBoxEntityListPage;
import su.opencode.kefir.selenium.pages.MainPage;
import su.opencode.kefir.selenium.test.dataprovider.ComboBoxEntityDataProvider;

import static su.opencode.kefir.selenium.test.dataprovider.ComboBoxEntityDataProvider.DATA_PROVIDER_NAME;

public class ComboBoxEntityTests extends AbstractTestCase
{
	private ComboBoxEntityListPage getListPage() {
		return new MainPage().clickButtonComboBoxEntityList();
	}
	@Test(dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ComboBoxEntityDataProvider.class)
	public void testCreate(ComboBoxEntity createEntity, ComboBoxEntity updateEntity) {
		createEntity(getListPage(), createEntity, mainPageClass);
	}
	@Test(dependsOnMethods = "testCreate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ComboBoxEntityDataProvider.class)
	public void testShow(ComboBoxEntity createEntity, ComboBoxEntity updateEntity) {
		showEntity(getListPage(), createEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testShow", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ComboBoxEntityDataProvider.class)
	public void testUpdate(ComboBoxEntity createEntity, ComboBoxEntity updateEntity) {
		updateEntity(getListPage(), createEntity, updateEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testUpdate", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ComboBoxEntityDataProvider.class)
	public void testDelete(ComboBoxEntity createEntity, ComboBoxEntity updateEntity) {
		deleteEntity(getListPage(), updateEntity, formPageClass, mainPageClass);
	}
	@Test(dependsOnMethods = "testDelete", dataProvider = DATA_PROVIDER_NAME, dataProviderClass = ComboBoxEntityDataProvider.class)
	public void testCreateIfNotExists(ComboBoxEntity createEntity, ComboBoxEntity updateEntity) {

		final ComboBoxEntityListPage listPage = getListPage();

		if (!selectEntityFromGrid(listPage, updateEntity))
			createEntity(listPage, updateEntity, mainPageClass);
	}

	private static final Class<ComboBoxEntityFormPage> formPageClass = ComboBoxEntityFormPage.class;
	private static final Class<MainPage> mainPageClass = MainPage.class;
}
