/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.pages;

import su.opencode.kefir.selenium.AbstractPage;

public class MainPage extends AbstractPage
{
	public MainPage() {
		super(TITLE);
	}

	public boolean isValid() {
		return isTextPresentInHtml(TITLE)
			&& isElementPresentById(COMBO_BOX_ENTITY_LIST_BUTTON_ID)
			&& isElementPresentById(CHOOSE_ENTITY_LIST_BUTTON_ID)
			&& isElementPresentById(TEST_ENTITY_LIST_BUTTON_ID);
	}

	public ComboBoxEntityListPage clickButtonComboBoxEntityList() {
		clickById(COMBO_BOX_ENTITY_LIST_BUTTON_ID);
		return new ComboBoxEntityListPage();
	}

	public ChooseEntityListPage clickButtonChooseEntityList() {
		clickById(CHOOSE_ENTITY_LIST_BUTTON_ID);
		return new ChooseEntityListPage();
	}

	public TestEntityListPage clickButtonTestEntitiesList() {
		clickById(TEST_ENTITY_LIST_BUTTON_ID);
		return new TestEntityListPage();
	}

	private static final String TITLE = "Test Kefir App";

	private static final String COMBO_BOX_ENTITY_LIST_BUTTON_ID = "comboBoxEntity-list-mainMenu";
	private static final String CHOOSE_ENTITY_LIST_BUTTON_ID = "chooseEntity-list-mainMenu";
	private static final String TEST_ENTITY_LIST_BUTTON_ID = "testEntity-list-mainMenu";
}