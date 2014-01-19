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
import su.opencode.kefir.selenium.AbstractChooseListPage;
import su.opencode.kefir.selenium.AbstractChooseListPage;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.selenium.GridElement;
import su.opencode.kefir.srv.json.JsonObject;

public class ChooseEntityChooseListPage extends AbstractChooseListPage
{
	public ChooseEntityChooseListPage() {
		super(WINDOW_TITLE);

		windowId = WINDOW_ID;

		chooseButtonId = CHOOSE_BUTTON_ID;
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

	public GridElement findEntity(JsonObject jsonObject) {
		final ChooseEntity entity = (ChooseEntity) jsonObject;

		return gridElement.fillSearchField(NAME_SEARCH_FIELD_ID, entity.getName());
	}

	private static final String WINDOW_ID = "chooseEntityChooseWindow";
	private static final String WINDOW_TITLE = "Выбор связанной сущности";

	private static final String SHOW_WINDOW_TITLE = "Просмотр связанной сущности";
	private static final String CREATE_WINDOW_TITLE = "Ввод связанной сущности";

	private static final String CHOOSE_BUTTON_ID = "chooseEntityChoose-chooseButton";
	private static final String SHOW_BUTTON_ID = "chooseEntityChoose-showButton";
	private static final String CREATE_BUTTON_ID = "chooseEntityChoose-createButton";
	private static final String CLOSE_BUTTON_ID = "chooseEntitysList-closeButton";

	private static final String NAME_SEARCH_FIELD_ID = "chooseEntityChooseGrid-nameSearchField";

	private GridElement gridElement = new GridElement("chooseEntityChooseGrid");
}