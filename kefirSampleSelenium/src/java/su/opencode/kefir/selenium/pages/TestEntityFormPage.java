/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.pages;

import su.opencode.kefir.sampleSrv.*;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.selenium.AttachmentsElement;
import su.opencode.kefir.selenium.AttachmentsElement;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.srv.renderer.RenderersFactory;

import static su.opencode.kefir.sampleSrv.render.Renderers.TEST_ENUM_RENDERER;

public class TestEntityFormPage extends AbstractFormPage
{
	public TestEntityFormPage(String title) {
		super(title);

		windowId = WINDOW_ID;

		saveButtonId = SAVE_BUTTON_ID;
		cancelButtonId = CANCEL_BUTTON_ID;
		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isValid() {
		return isElementPresentById(WINDOW_ID);
	}
	public boolean isFormEqual(JsonObject o) {
		final TestEntity entity = (TestEntity) o;

		return isEqual(STR_FIELD_ID, entity.getStrField())
			&& isEqual(INT_FIELD_ID, entity.getIntField())
			&& isEqual(INT_SPINNER_FIELD_ID, entity.getIntSpinnerField())
			&& isEqual(DOUBLE_FIELD_ID, entity.getDoubleField())
			&& isEqual(DATE_FIELD_ID, entity.getDateField())
			&& isEqual(BOOLEAN_FIELD_ID, entity.getBooleanField())

			&& isEqual(ENUM_FIELD_ID, getEnumValue(entity.getEnumField()))

			&& isEqual(OGRN_FIELD_ID, entity.getOgrn())
			&& isEqual(INN_FIELD_ID, entity.getInn())
			&& isEqual(KPP_FIELD_ID, entity.getKpp())

			&& getComboBoxEntityEquals(entity)

			&& getChooseEntityEquals(entity);
	}
	public TestEntityFormPage fillForm(JsonObject jsonObject) {
		final TestEntity entity = (TestEntity) jsonObject;

		setValueById(STR_FIELD_ID, entity.getStrField());
		setValueById(INT_FIELD_ID, entity.getIntField());
		setValueById(INT_SPINNER_FIELD_ID, entity.getIntSpinnerField());

		setValueById(DOUBLE_FIELD_ID, renderer.getFloatCellRenderedValue(entity.getDoubleField()));

		setDateValueById(DATE_FIELD_ID, renderer.getDateCellRenderedValue(entity.getDateField()));

		setCheckboxValueById(BOOLEAN_FIELD_ID, entity.getBooleanField());

		setLocalComboBoxValueById(ENUM_FIELD_ID, getEnumValue(entity.getEnumField()));

		final ComboBoxEntity comboBoxEntity = entity.getComboBoxEntity();
		setComboBoxValueById(COMBO_BOX_ENTITY_FIELD_ID, comboBoxEntity == null ? null : comboBoxEntity.getCadastralNumber());

		fillChooseEntity(entity.getChooseEntity());

		uploadAttachments(attachmentsField, entity.getAttachmentsField());

		uploadAttachments(otherAttachmentsField, entity.getOtherAttachmentsField());

		setValueById(OGRN_FIELD_ID, entity.getOgrn());
		setValueById(KPP_FIELD_ID, entity.getKpp());
		setValueById(INN_FIELD_ID, entity.getInn());

		fillAddress(entity.getJuridicalAddress(), JURIDICAL_ADDRESS_FIELD_ID, JURIDICAL_ADDRESS_WINDOW_TITLE);

		fillAddress(entity.getPhysicalAddress(), PHYSICAL_ADDRESS_FIELD_ID, PHYSICAL_ADDRESS_WINDOW_TITLE);

		return this;
	}
	private void fillAddress(Address address, String addressFieldId, String addressWindowTitle) {
		if (address == null)
			return;

		clickById(addressFieldId);
		new AddressFormPage(addressWindowTitle)
			.fillForm(address)
			.clickSaveButton();
	}
	private boolean getChooseEntityEquals(TestEntity entity) {
		final ChooseEntity chooseEntityValue = entity.getChooseEntity();
		return isEqual(CHOOSE_ENTITY_NAME_FIELD_ID, chooseEntityValue == null ? "" : chooseEntityValue.getName())
			&& isEqual(CHOOSE_ENTITY_SHORT_NAME_FIELD_ID, chooseEntityValue == null ? "" : chooseEntityValue.getShortName())
			&& isEqual(CHOOSE_ENTITY_CORRESPONDENT_ACCOUNT_FIELD_ID, chooseEntityValue == null ? "" : chooseEntityValue.getCorrespondentAccount());
	}
	private boolean getComboBoxEntityEquals(TestEntity entity) {
		final ComboBoxEntity comboBoxEntity = entity.getComboBoxEntity();
		return isEqual(COMBO_BOX_ENTITY_FIELD_ID, comboBoxEntity == null ? "" : comboBoxEntity.getCadastralNumber());
	}
	private String getEnumValue(TestEnum enumField) {
		return renderer.getCellRenderedValue(TEST_ENUM_RENDERER, enumField);
	}
	private void fillChooseEntity(ChooseEntity chooseEntity) {
		if (chooseEntity == null)
			return;

		clickById(CHOOSE_ENTITY_CHOOSE_BUTTON_ID);
		new ChooseEntityChooseListPage().chooseEntity(chooseEntity);
	}
	public ChooseEntityFormPage showChooseEntity() {
		clickById(CHOOSE_ENTITY_SHOW_BUTTON_ID);
		return new ChooseEntityFormPage(null);
	}

	private static final String WINDOW_ID = "testEntityFormWindow";

	private static final String SAVE_BUTTON_ID = "testEntity-save";
	private static final String CANCEL_BUTTON_ID = "testEntity-cancel";
	private static final String CLOSE_BUTTON_ID = "testEntity-close";

	private static final String STR_FIELD_ID = "testEntity-strField";
	private static final String INT_FIELD_ID = "testEntity-intField";
	private static final String INT_SPINNER_FIELD_ID = "testEntity-intSpinnerField";
	private static final String DOUBLE_FIELD_ID = "testEntity-doubleField";
	private static final String DATE_FIELD_ID = "testEntity-dateField";
	private static final String BOOLEAN_FIELD_ID = "testEntity-booleanField";
	private static final String ENUM_FIELD_ID = "testEntity-enumField";
	private static final String OGRN_FIELD_ID = "testEntity-ogrn";
	private static final String INN_FIELD_ID = "testEntity-inn";
	private static final String KPP_FIELD_ID = "testEntity-kpp";
	private static final String COMBO_BOX_ENTITY_FIELD_ID = "testEntity-comboBoxEntity";

	private static final String CHOOSE_ENTITY_CHOOSE_BUTTON_ID = "testEntity-chooseEntity-choose";
	private static final String CHOOSE_ENTITY_SHOW_BUTTON_ID = "testEntity-chooseEntity-show";
	private static final String CHOOSE_ENTITY_NAME_FIELD_ID = "testEntity-chooseEntity-name";
	private static final String CHOOSE_ENTITY_SHORT_NAME_FIELD_ID = "testEntity-chooseEntity-shortName";
	private static final String CHOOSE_ENTITY_CORRESPONDENT_ACCOUNT_FIELD_ID = "testEntity-chooseEntity-correspondentAccount";

	private static final String ATTACHMENTS_FIELD_ID = "testEntity-attachmentsField";
	private AttachmentsElement attachmentsField = new AttachmentsElement(ATTACHMENTS_FIELD_ID);

	private static final String OTHER_ATTACHMENTS_FIELD_ID = "testEntity-otherAttachmentsField";
	private AttachmentsElement otherAttachmentsField = new AttachmentsElement(OTHER_ATTACHMENTS_FIELD_ID);

	private static final String JURIDICAL_ADDRESS_FIELD_ID = "testEntity-juridicalAddress-update";
	private static final String JURIDICAL_ADDRESS_WINDOW_TITLE = "Юридический адрес тестовой сущности";

	private static final String PHYSICAL_ADDRESS_FIELD_ID = "testEntity-physicalAddress-update";
	private static final String PHYSICAL_ADDRESS_WINDOW_TITLE = "Фактический адрес тестовой сущности";

	private static final RenderersFactory renderer = new RenderersFactory();
}