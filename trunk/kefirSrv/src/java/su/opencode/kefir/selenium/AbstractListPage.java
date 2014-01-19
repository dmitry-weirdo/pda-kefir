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

import static su.opencode.kefir.util.ObjectUtils.getNewInstance;

public abstract class AbstractListPage extends AbstractPage
{

	public AbstractListPage(String title) {
		super(title);
	}

	public abstract AbstractFormPage clickShowButton();
	public abstract AbstractFormPage clickCreateButton();
	public abstract AbstractFormPage clickUpdateButton();
	public abstract AbstractFormPage clickDeleteButton();
	public abstract <T extends JsonObject> GridElement findEntity(T entity);

	public <T extends AbstractPage> T clickCloseButton(Class<T> returnedClassInstance) {
		clickById(closeButtonId);
		return getNewInstance(returnedClassInstance);
	}

	protected String closeButtonId;
}
