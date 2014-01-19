/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import su.opencode.kefir.srv.attachment.Attachment;
import su.opencode.kefir.srv.json.JsonObject;

import java.util.List;

public abstract class AbstractFormPage extends AbstractPage
{

	public AbstractFormPage(String title) {
		super(title);
	}

	public abstract <T extends JsonObject> AbstractFormPage fillForm(T entity);
	public abstract <T extends JsonObject> boolean isFormEqual(T entity);

	public void clickSaveButton() {
		clickById(saveButtonId);
//		waitUntilElementEnabledId(windowId); //todo: возможно сделать проверку форма успешно сохранилась и окно исчезло
	}

	public void clickCancelButton() {
		clickById(cancelButtonId);
		waitUntilElementEnabledId(windowId);
	}

	public void clickCloseButton() {
		clickById(closeButtonId);
		waitUntilElementEnabledId(windowId);
	}

	protected void uploadAttachments(AttachmentsElement attachmentsElement, List<Attachment> attachmentList) {
		attachmentsElement
			.addAttachments(attachmentList)
			.uploadAllAttachments();
	}

	protected String windowId;

	protected String saveButtonId;
	protected String cancelButtonId;
	protected String closeButtonId;
}
