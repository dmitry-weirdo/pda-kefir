/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.pages;

import su.opencode.kefir.sampleSrv.ChooseEntity;
import su.opencode.kefir.sampleSrv.TestEntity;
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.srv.json.JsonObject;

public class TestEntityListPage extends AbstractListPage
{
	public TestEntityListPage() {
		super(TITLE);

		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isValid() {
		return isElementPresentById(WINDOW_ID);
	}

	public TestEntityFormPage clickShowButton() {
		clickById(SHOW_BUTTON_ID);
		return new TestEntityFormPage(SHOW_WINDOW_TITLE);
	}

	public TestEntityFormPage clickCreateButton() {
		clickById(CREATE_BUTTON_ID);
		return new TestEntityFormPage(CREATE_WINDOW_TITLE);
	}

	public TestEntityFormPage clickUpdateButton() {
		clickById(UPDATE_BUTTON_ID);
		return new TestEntityFormPage(UPDATE_WINDOW_TITLE);
	}

	public TestEntityFormPage clickDeleteButton() {
		clickById(DELETE_BUTTON_ID);
		return new TestEntityFormPage(DELETE_WINDOW_TITLE);
	}

	public GridElement findEntity(JsonObject jsonObject) {
		final TestEntity entity = (TestEntity) jsonObject;

		gridElement.fillSearchField(STR_FIELD_SEARCH_FIELD_ID, entity.getStrField());

		final ChooseEntity chooseEntity = entity.getChooseEntity();
		if (chooseEntity != null)
			gridElement.fillSearchField(CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID, chooseEntity.getName());

		return gridElement;
	}

	private static final String WINDOW_ID = "testEntitysWindow";
	private static final String TITLE = "Список тестовых сущностей";

	private static final String CREATE_WINDOW_TITLE = "Ввод тестовой сущности";
	private static final String SHOW_WINDOW_TITLE = "Просмотр тестовой сущности";
	private static final String UPDATE_WINDOW_TITLE = "Изменение тестовой сущности";
	private static final String DELETE_WINDOW_TITLE = "Удаление тестовой сущности";

	private static final String CREATE_BUTTON_ID = "testEntitysList-createButton";
	private static final String SHOW_BUTTON_ID = "testEntitysList-showButton";
	private static final String UPDATE_BUTTON_ID = "testEntitysList-updateButton";
	private static final String DELETE_BUTTON_ID = "testEntitysList-deleteButton";
	private static final String EXPORT_TO_EXCEL_BUTTON_ID = "testEntitysList-exportToExcelButton";
	private static final String CLOSE_BUTTON_ID = "testEntitysList-closeButton";

	private static final String STR_FIELD_SEARCH_FIELD_ID = "testEntitysGrid-strFieldSearchField";
	private static final String CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID = "testEntitysGrid-chooseEntityNameSearchField";

	private GridElement gridElement = new GridElement("testEntitysGrid");
}