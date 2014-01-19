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
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.srv.json.JsonObject;

public class ChooseEntityListPage extends AbstractListPage
{
	public ChooseEntityListPage() {
		super(TITLE);

		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isValid() {
		return isElementPresentById(WINDOW_ID);
	}

	public ChooseEntityFormPage clickShowButton() {
		clickById(SHOW_BUTTON_ID);
		return new ChooseEntityFormPage(SHOW_WINDOW_TITLE);
	}

	public ChooseEntityFormPage clickCreateButton() {
		clickById(CREATE_BUTTON_ID);
		return new ChooseEntityFormPage(CREATE_WINDOW_TITLE);
	}

	public ChooseEntityFormPage clickUpdateButton() {
		clickById(UPDATE_BUTTON_ID);
		return new ChooseEntityFormPage(UPDATE_WINDOW_TITLE);
	}

	public ChooseEntityFormPage clickDeleteButton() {
		clickById(DELETE_BUTTON_ID);
		return new ChooseEntityFormPage(DELETE_WINDOW_TITLE);
	}

	public GridElement findEntity(JsonObject jsonObject) {
		final ChooseEntity entity = (ChooseEntity) jsonObject;

		return gridElement.fillSearchField(NAME_SEARCH_FIELD_ID, entity.getName());
	}

	private static final String WINDOW_ID = "chooseEntitysWindow";
	private static final String TITLE = "Список связанных сущностей";

	private static final String CREATE_WINDOW_TITLE = "Ввод связанной сущности";
	private static final String SHOW_WINDOW_TITLE = "Просмотр связанной сущности";
	private static final String UPDATE_WINDOW_TITLE = "Изменение связанной сущности";
	private static final String DELETE_WINDOW_TITLE = "Удаление связанной сущности";

	private static final String CREATE_BUTTON_ID = "chooseEntitysList-createButton";
	private static final String SHOW_BUTTON_ID = "chooseEntitysList-showButton";
	private static final String UPDATE_BUTTON_ID = "chooseEntitysList-updateButton";
	private static final String DELETE_BUTTON_ID = "chooseEntitysList-deleteButton";
	private static final String EXPORT_TO_EXCEL_BUTTON_ID = "chooseEntitysList-exportToExcelButton";
	private static final String CLOSE_BUTTON_ID = "chooseEntitysList-closeButton";

	private static final String NAME_SEARCH_FIELD_ID = "chooseEntitysGrid-nameSearchField";

	private GridElement gridElement = new GridElement("chooseEntitysGrid");
}