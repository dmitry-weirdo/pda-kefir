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
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.srv.json.JsonObject;

public class ChooseEntityFormPage extends AbstractFormPage
{
	public ChooseEntityFormPage(String title) {
		super(title);

		windowId = WINDOW_ID;

		saveButtonId = SAVE_BUTTON_ID;
		cancelButtonId = CANCEL_BUTTON_ID;
		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isValid() {
		return isElementPresentById(WINDOW_ID);
	}

	public boolean isFormEqual(JsonObject jsonObject) {
		final ChooseEntity entity = (ChooseEntity) jsonObject;

		return isEqual(NUMBER_ID, entity.getName())
			&& isEqual(SHORT_NAME_ID, entity.getShortName())
			&& isEqual(CORRESPONDENT_ACCOUNT_ID, entity.getCorrespondentAccount());
	}

	public ChooseEntityFormPage fillForm(JsonObject jsonObject) {
		final ChooseEntity entity = (ChooseEntity) jsonObject;

		setValueById(NUMBER_ID, entity.getName());
		setValueById(SHORT_NAME_ID, entity.getShortName());
		setValueById(CORRESPONDENT_ACCOUNT_ID, entity.getCorrespondentAccount());

		return this;
	}

	private static final String WINDOW_ID = "chooseEntityFormWindow";

	private static final String SAVE_BUTTON_ID = "chooseEntity-save";
	private static final String CLOSE_BUTTON_ID = "chooseEntity-close";
	private static final String CANCEL_BUTTON_ID = "chooseEntity-cancel";

	private static final String NUMBER_ID = "chooseEntity-name";
	private static final String SHORT_NAME_ID = "chooseEntity-shortName";
	private static final String CORRESPONDENT_ACCOUNT_ID = "chooseEntity-correspondentAccount";
}