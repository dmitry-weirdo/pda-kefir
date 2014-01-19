/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.pages;

import su.opencode.kefir.sampleSrv.ComboBoxEntity;
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.AbstractListPage;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.srv.json.JsonObject;

public class ComboBoxEntityListPage extends AbstractListPage
{
	public ComboBoxEntityListPage() {
		super(TITLE);

		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isValid() {
		return isElementPresentById(WINDOW_ID);
	}

	public ComboBoxEntityFormPage clickShowButton() {
		clickById(SHOW_BUTTON_ID);
		return new ComboBoxEntityFormPage(SHOW_WINDOW_TITLE);
	}

	public ComboBoxEntityFormPage clickCreateButton() {
		clickById(CREATE_BUTTON_ID);
		return new ComboBoxEntityFormPage(CREATE_WINDOW_TITLE);
	}

	public ComboBoxEntityFormPage clickUpdateButton() {
		clickById(UPDATE_BUTTON_ID);
		return new ComboBoxEntityFormPage(UPDATE_WINDOW_TITLE);
	}

	public ComboBoxEntityFormPage clickDeleteButton() {
		clickById(DELETE_BUTTON_ID);
		return new ComboBoxEntityFormPage(DELETE_WINDOW_TITLE);
	}

	public GridElement findEntity(JsonObject jsonObject) {
		final ComboBoxEntity entity = (ComboBoxEntity) jsonObject;

		return gridElement.fillSearchField(CADASTRAL_NUMBER_SEARCH_FIELD_ID, entity.getCadastralNumber());
	}

	private static final String WINDOW_ID = "comboBoxEntitysWindow";
	private static final String TITLE = "Список комбо сущностей";

	private static final String CREATE_WINDOW_TITLE = "Ввод комбо сущности";
	private static final String SHOW_WINDOW_TITLE = "Просмотр комбо сущности";
	private static final String UPDATE_WINDOW_TITLE = "Изменение комбо сущности";
	private static final String DELETE_WINDOW_TITLE = "Удаление комбо сущности";

	private static final String CREATE_BUTTON_ID = "comboBoxEntitysList-createButton";
	private static final String SHOW_BUTTON_ID = "comboBoxEntitysList-showButton";
	private static final String UPDATE_BUTTON_ID = "comboBoxEntitysList-updateButton";
	private static final String DELETE_BUTTON_ID = "comboBoxEntitysList-deleteButton";
	private static final String EXPORT_TO_EXCEL_BUTTON_ID = "comboBoxEntitysList-exportToExcelButton";
	private static final String CLOSE_BUTTON_ID = "comboBoxEntitysList-closeButton";

	private static final String CADASTRAL_NUMBER_SEARCH_FIELD_ID = "comboBoxEntitysGrid-cadastralNumberSearchField";

	public GridElement gridElement = new GridElement("comboBoxEntitysGrid");
}