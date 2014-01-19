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
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.srv.json.JsonObject;

public class ComboBoxEntityFormPage extends AbstractFormPage
{
	public ComboBoxEntityFormPage(String title) {
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
		final ComboBoxEntity entity = (ComboBoxEntity) jsonObject;

		return isEqual(CADASTRAL_NUMBER_ID, entity.getCadastralNumber());
	}

	public ComboBoxEntityFormPage fillForm(JsonObject jsonObject) {
		final ComboBoxEntity entity = (ComboBoxEntity) jsonObject;

		setValueById(CADASTRAL_NUMBER_ID, entity.getCadastralNumber());

		return this;
	}

	private static final String WINDOW_ID = "comboBoxEntityFormWindow";

	private static final String SAVE_BUTTON_ID = "comboBoxEntity-save";
	private static final String CLOSE_BUTTON_ID = "comboBoxEntity-close";
	private static final String CANCEL_BUTTON_ID = "comboBoxEntity-cancel";

	private static final String CADASTRAL_NUMBER_ID = "comboBoxEntity-cadastralNumber";
}