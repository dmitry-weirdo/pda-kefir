/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import su.opencode.kefir.srv.json.JsonObject;

public abstract class AbstractChooseListPage extends AbstractPage
{

	public AbstractChooseListPage(String title) {
		super(title);
	}

	public abstract <T extends JsonObject> GridElement findEntity(T entity);
	public abstract AbstractFormPage clickShowButton();
	public abstract AbstractFormPage clickCreateButton();

	public void clickChooseButton() {
		clickById(chooseButtonId);
		waitUntilElementEnabledId(windowId);
	}

	public void chooseEntity(JsonObject entity) {
		selectEntity(entity);
		clickChooseButton();
	}

	public void clickCloseButton() {
		clickById(closeButtonId);
		waitUntilElementEnabledId(windowId);
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractChooseListPage> T selectEntity(JsonObject entity) {
		final GridElement gridElement = findEntity(entity);

		final int rowCount = gridElement.rowCount();
		if (rowCount == 0)
			throw new RuntimeException("Grid is empty");

		int i = 1;
		boolean isPresent = false;
		while (i <= rowCount && !isPresent)
		{
			gridElement.clickOnRow(i);
			final AbstractFormPage formPage = clickShowButton();

			if (formPage.isFormEqual(entity))
				isPresent = true;

			formPage.clickCloseButton();
			i++;
		}

		if (!isPresent)
			throw new RuntimeException("Entity not found in grid");

		return (T) this;
	}

	protected String windowId;

	protected String chooseButtonId;
	protected String closeButtonId;
}
