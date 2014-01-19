/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.selenium;

import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.selenium.AbstractFormPage;
import su.opencode.kefir.srv.json.JsonObject;

import java.io.IOException;

import static su.opencode.kefir.gen.ExtEntityUtils.getSeleniumMainPagePackage;
import static su.opencode.kefir.gen.fileWriter.FileWriter.NEW_LINE;
import static su.opencode.kefir.gen.project.address.AddressClassFileWriter.ADDRESS_CLASS_SIMPLE_NAME;

public class AddressFormPageFileWriter extends ClassFileWriter
{
	public AddressFormPageFileWriter(String baseDir, String addressClassPackage) {
		super(baseDir, getSeleniumMainPagePackage(), ADDRESS_FORM_SIMPLE_NAME);

		this.addressClassPackage = addressClassPackage;

		failIfFileExists = false;
		overwriteIfFileExists = true;
	}
	@Override
	protected void writeImports() throws IOException {
		writeImport(addressClassPackage, ".", ADDRESS_CLASS_SIMPLE_NAME);
		writeImport(ABSTRACT_FORM_PAGE.getName());
		writeImport(JSON_OBJECT.getName());
	}
	@Override
	protected void writeClassBody() throws IOException {
		writeClassHeader(ABSTRACT_FORM_PAGE);

		writeConstructorWithArguments("String title", "super(title);", "", "windowId = WINDOW_ID;", "",
			"saveButtonId = SAVE_BUTTON_ID;", "cancelButtonId = CANCEL_BUTTON_ID;", "closeButtonId = CLOSE_BUTTON_ID;");

		writeIsFormEqualMethod();

		writeFillFormMethod();

		writeFields();

		writeClassFooter();
	}
	private void writeFillFormMethod() throws IOException {
		out.writeLn(TAB, "public ", ADDRESS_FORM_SIMPLE_NAME, " fillForm(", JSON_OBJECT.getSimpleName(), " jsonObject) {");
		out.writeLn(DOUBLE_TAB, "final ", ADDRESS_CLASS_SIMPLE_NAME, " entity = (", ADDRESS_CLASS_SIMPLE_NAME, ") jsonObject;");
		out.writeLn();
		out.writeLn(DOUBLE_TAB, "setValueById(ZIP_CODE_FIELD_ID, entity.getZipCode());");
		out.writeLn(DOUBLE_TAB, "setValueById(SUBJECT_FIELD_ID, entity.getSubject());");
		out.writeLn(DOUBLE_TAB, "setValueById(DISTRICT_FIELD_ID, entity.getDistrict());");
		out.writeLn(DOUBLE_TAB, "setValueById(CITY_FIELD_ID, entity.getCity());");
		out.writeLn(DOUBLE_TAB, "setValueById(CITY_DISTRICT_FIELD_ID, entity.getCityDistrict());");
		out.writeLn(DOUBLE_TAB, "setValueById(LOCALITY_FIELD_ID, entity.getLocality());");
		out.writeLn(DOUBLE_TAB, "setValueById(STREET_FIELD_ID, entity.getStreet());");
		out.writeLn(DOUBLE_TAB, "setValueById(BLOCK_FIELD_ID, entity.getBlock());");
		out.writeLn(DOUBLE_TAB, "setValueById(HOUSE_FIELD_ID, entity.getHouse());");
		out.writeLn(DOUBLE_TAB, "setValueById(BUILDING_FIELD_ID, entity.getBuilding());");
		out.writeLn(DOUBLE_TAB, "setValueById(APARTMENT_FIELD_ID, entity.getApartment());");
		out.writeLn();
		out.writeLn(DOUBLE_TAB, "return this;");
		out.writeLn(TAB, "}");
	}
	private void writeFields() throws IOException {
		out.writeLn();
		writePrivateStringConstant("WINDOW_ID", "addressWindow");
		out.writeLn();
		writePrivateStringConstant("ZIP_CODE_FIELD_ID", "address-zipCode");
		writePrivateStringConstant("SUBJECT_FIELD_ID", "address-subject");
		writePrivateStringConstant("DISTRICT_FIELD_ID", "address-district");
		writePrivateStringConstant("CITY_FIELD_ID", "address-city");
		writePrivateStringConstant("CITY_DISTRICT_FIELD_ID", "address-cityDistrict");
		writePrivateStringConstant("LOCALITY_FIELD_ID", "address-locality");
		writePrivateStringConstant("STREET_FIELD_ID", "address-street");
		writePrivateStringConstant("BLOCK_FIELD_ID", "address-block");
		writePrivateStringConstant("HOUSE_FIELD_ID", "address-house");
		writePrivateStringConstant("BUILDING_FIELD_ID", "address-building");
		writePrivateStringConstant("APARTMENT_FIELD_ID", "address-apartment");
		out.writeLn();
		writePrivateStringConstant("SAVE_BUTTON_ID", "address-save");
		writePrivateStringConstant("CANCEL_BUTTON_ID", "address-cancel");
		writePrivateStringConstant("CLOSE_BUTTON_ID", "address-close");
		out.writeLn();
	}
	private void writeIsFormEqualMethod() throws IOException {
		out.writeLn(TAB, "public boolean isFormEqual(", JSON_OBJECT.getSimpleName(), " jsonObject) {");
		out.writeLn(DOUBLE_TAB, "final Address entity = (Address) jsonObject;");
		out.writeLn(DOUBLE_TAB, "return isEqual(getValueById(ZIP_CODE_FIELD_ID), entity.getZipCode())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(SUBJECT_FIELD_ID), entity.getSubject())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(DISTRICT_FIELD_ID), entity.getDistrict())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(CITY_FIELD_ID), entity.getCity())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(CITY_DISTRICT_FIELD_ID), entity.getCityDistrict())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(LOCALITY_FIELD_ID), entity.getLocality())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(STREET_FIELD_ID), entity.getStreet())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(BLOCK_FIELD_ID), entity.getBlock())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(HOUSE_FIELD_ID), entity.getHouse())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(BUILDING_FIELD_ID), entity.getBuilding())", NEW_LINE,
			TRIPLE_TAB, "&& isEqual(getValueById(APARTMENT_FIELD_ID), entity.getApartment());");
		out.writeLn(TAB, "}");
	}

	private static final Class ABSTRACT_FORM_PAGE = AbstractFormPage.class;
	private static final Class JSON_OBJECT = JsonObject.class;

	private String addressClassPackage;

	public static final String ADDRESS_FORM_SIMPLE_NAME = "AddressFormPage";
}