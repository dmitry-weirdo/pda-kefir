/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import su.opencode.kefir.srv.json.JsonObject;

import static su.opencode.kefir.util.StringUtils.concat;

public abstract class AbstractTestCase extends TestCase
{
	protected AbstractTestCase() {
		initVariables();
	}

	private void initVariables() {
		if (siteURL != null)
			return;

		final String log4j_path = System.getProperty("log4j_path");
		log.info(concat("log4j_path=", log4j_path));
		if (log4j_path != null)
			DOMConfigurator.configure(log4j_path);
		else
			BasicConfigurator.configure();
		log.info(concat("log4j_path=", log4j_path));

		siteURL = System.getProperty("site_url");
		if (siteURL == null || siteURL.isEmpty())
			throw new RuntimeException("Не указан site_url");
		log.info(concat("site_url=", siteURL));

		appContext = System.getProperty("app_context");
		if (appContext == null || appContext.isEmpty())
			throw new RuntimeException("Не указан app_context");
		log.info(concat("app_context=", appContext));
	}

	@BeforeSuite
	public void setUp() {
		Context.initInstance(siteURL, appContext);
	}

	@AfterSuite
	public void tearDown() {
		Context.close();
	}

	protected void assertValidatePage(AbstractPage page) {
		assertTrue(concat(sb, page.getClass().getSimpleName(), " not found"), page.isValid());
	}

	protected boolean selectEntityFromGrid(AbstractListPage abstractListPage, JsonObject entity) {
		final GridElement gridElement = abstractListPage
			.findEntity(entity);

		final int rowCount = gridElement.rowCount();
		boolean isPresent = false;
		int i = 0;
		while (i < rowCount && !isPresent)
		{
			i++;
			gridElement.clickOnRow(i);
			final AbstractFormPage formPage = abstractListPage
				.clickShowButton();

			if (formPage.isFormEqual(entity))
				isPresent = true;

			formPage.clickCloseButton();
		}

		if (i > rowCount)
			throw new RuntimeException(concat("Entity not found in ", abstractListPage.getClass().getSimpleName()));

		return isPresent;
	}

	protected <T extends AbstractListPage, E extends AbstractFormPage> void findAndSelectEntityInGrid(JsonObject jsonObject, T listPage, Class<E> formPageClass) {
		final boolean selectedEntity = selectEntityFromGrid(listPage, jsonObject);
		assertTrue(concat(sb, formPageClass.getSimpleName(), " not equal to test data"), selectedEntity);
	}

	protected <E extends AbstractPage> void closeListPage(AbstractListPage listPage, Class<E> returnedClassInstance) {
		assertValidatePage(listPage.clickCloseButton(returnedClassInstance));
	}

	protected <E extends AbstractPage> void createEntity(AbstractListPage listPage, JsonObject createEntity, Class<E> returnedClassInstance) {
		listPage.clickCreateButton()
			.fillForm(createEntity)
			.clickSaveButton();

		closeListPage(listPage, returnedClassInstance);
	}

	protected <T extends AbstractFormPage, E extends AbstractPage> void showEntity(AbstractListPage listPage, JsonObject createEntity, Class<T> formPageClass, Class<E> returnedClassInstance) {
		findAndSelectEntityInGrid(createEntity, listPage, formPageClass);

		closeListPage(listPage, returnedClassInstance);
	}

	protected <T extends AbstractFormPage, E extends AbstractPage> void updateEntity(AbstractListPage listPage, JsonObject existsEntity, JsonObject updateEntity, Class<T> formPageClass, Class<E> returnedClassInstance) {
		findAndSelectEntityInGrid(existsEntity, listPage, formPageClass);

		listPage
			.clickUpdateButton()
			.fillForm(updateEntity)
			.clickSaveButton();

		closeListPage(listPage, returnedClassInstance);
	}

	protected <T extends AbstractFormPage, E extends AbstractPage> void deleteEntity(AbstractListPage listPage, JsonObject updateEntity, Class<T> formPageClass, Class<E> returnedClassInstance) {
		findAndSelectEntityInGrid(updateEntity, listPage, formPageClass);

		listPage
			.clickDeleteButton()
			.clickSaveButton();

		closeListPage(listPage, returnedClassInstance);
	}

	private static final Logger log = Logger.getLogger(AbstractTestCase.class);

	protected static final StringBuffer sb = new StringBuffer();

	private static String siteURL;
	private static String appContext;
}