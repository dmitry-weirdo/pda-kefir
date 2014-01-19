/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium.pages;

import su.opencode.kefir.sampleSrv.Address;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.srv.json.JsonObject;

public class AddressFormPage extends AbstractFormPage
{
	public AddressFormPage(String title) {
		super(title);

		windowId = WINDOW_ID;

		saveButtonId = SAVE_BUTTON_ID;
		cancelButtonId = CANCEL_BUTTON_ID;
		closeButtonId = CLOSE_BUTTON_ID;
	}

	public boolean isFormEqual(JsonObject jsonObject) {
		final Address entity = (Address) jsonObject;

		return isEqual(getValueById(ZIP_CODE_FIELD_ID), entity.getZipCode())
			&& isEqual(getValueById(SUBJECT_FIELD_ID), entity.getSubject())
			&& isEqual(getValueById(DISTRICT_FIELD_ID), entity.getDistrict())
			&& isEqual(getValueById(CITY_FIELD_ID), entity.getCity())
			&& isEqual(getValueById(CITY_DISTRICT_FIELD_ID), entity.getCityDistrict())
			&& isEqual(getValueById(LOCALITY_FIELD_ID), entity.getLocality())
			&& isEqual(getValueById(STREET_FIELD_ID), entity.getStreet())
			&& isEqual(getValueById(BLOCK_FIELD_ID), entity.getBlock())
			&& isEqual(getValueById(HOUSE_FIELD_ID), entity.getHouse())
			&& isEqual(getValueById(BUILDING_FIELD_ID), entity.getBuilding())
			&& isEqual(getValueById(APARTMENT_FIELD_ID), entity.getApartment());
	}

	public AddressFormPage fillForm(JsonObject jsonObject) {
		final Address entity = (Address) jsonObject;

		setValueById(ZIP_CODE_FIELD_ID, entity.getZipCode());
		setValueById(SUBJECT_FIELD_ID, entity.getSubject());
		setValueById(DISTRICT_FIELD_ID, entity.getDistrict());
		setValueById(CITY_FIELD_ID, entity.getCity());
		setValueById(CITY_DISTRICT_FIELD_ID, entity.getCityDistrict());
		setValueById(LOCALITY_FIELD_ID, entity.getLocality());
		setValueById(STREET_FIELD_ID, entity.getStreet());
		setValueById(BLOCK_FIELD_ID, entity.getBlock());
		setValueById(HOUSE_FIELD_ID, entity.getHouse());
		setValueById(BUILDING_FIELD_ID, entity.getBuilding());
		setValueById(APARTMENT_FIELD_ID, entity.getApartment());

		return this;
	}

	private static final String WINDOW_ID = "addressWindow";

	private static final String ZIP_CODE_FIELD_ID = "address-zipCode";
	private static final String SUBJECT_FIELD_ID = "address-subject";
	private static final String DISTRICT_FIELD_ID = "address-district";
	private static final String CITY_FIELD_ID = "address-city";
	private static final String CITY_DISTRICT_FIELD_ID = "address-cityDistrict";
	private static final String LOCALITY_FIELD_ID = "address-locality";
	private static final String STREET_FIELD_ID = "address-street";
	private static final String BLOCK_FIELD_ID = "address-block";
	private static final String HOUSE_FIELD_ID = "address-house";
	private static final String BUILDING_FIELD_ID = "address-building";
	private static final String APARTMENT_FIELD_ID = "address-apartment";

	private static final String SAVE_BUTTON_ID = "address-save";
	private static final String CANCEL_BUTTON_ID = "address-cancel";
	private static final String CLOSE_BUTTON_ID = "address-close";
}