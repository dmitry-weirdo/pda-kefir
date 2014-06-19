/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 04.04.2012 17:32:36$
*/
package su.opencode.kefir.gen.project.address;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.fileWriter.ClassAnnotation;
import su.opencode.kefir.gen.fileWriter.ClassField;
import su.opencode.kefir.gen.fileWriter.ClassFileWriter;
import su.opencode.kefir.gen.project.ProjectConfig;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.StringUtils;

import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.fileWriter.EntityFilterConfigFileWriter.CONCAT_METHOD_NAME;
import static su.opencode.kefir.gen.fileWriter.EntityFilterConfigFileWriter.EMPTY_METHOD_NAME;
import static su.opencode.kefir.gen.project.xml.orm.EntityMappingAppender.ID_FIELD_NAME;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public class AddressClassFileWriter extends ClassFileWriter
{
	public AddressClassFileWriter(String baseDir, String packageName, ProjectConfig config) {
		super(baseDir, packageName, ADDRESS_CLASS_SIMPLE_NAME);
		this.config = config;
		this.failIfFileExists = false; // если уже есть класс адреса, не перезаписывать ее
	}

	@Override
	protected void writeImports() throws IOException {
		writeImport(ExtEntity.class);
		writeImport(JsonObject.class);
		writeImport(Transient.class);
		out.writeLn();
		writeStaticImport(StringUtils.class, CONCAT_METHOD_NAME);
		writeStaticImport(StringUtils.class, GET_NOT_NULL_STRING_METHOD_NAME);
		writeStaticImport(StringUtils.class, EMPTY_METHOD_NAME);
	}

	@Override
	protected void writeClassBody() throws IOException {
		writeExtEntityAnnotation();
		writeClassHeader(JsonObject.class);

		List<ClassField> fields = getFields();
		for (ClassField field : fields)
			writeGetterAndSetter(field);

		// isEmpty, getAddress and getBuildingAddress methods
		out.writeLn();
		writeIsEmptyMethod();
		writeGetAddressMethod();
		writeGetBuildingAddressMethod();

		out.writeLn();
		for (ClassField field : fields)
			writeFieldDeclaration(field);

		// address_separator constant field
		out.writeLn();
		writePublicStringConstant(ADDRESS_SEPARATOR_FIELD_NAME, ADDRESS_SEPARATOR_FIELD_VALUE);

		writeClassFooter();
	}
	private void writeExtEntityAnnotation() throws IOException {
		ClassAnnotation annotation = new ClassAnnotation(ExtEntity.class);
		annotation.putString(LIST_WINDOW_TITLE_PARAM_NAME, LIST_WINDOW_TITLE_PARAM_VALUE);
		annotation.putString(NOT_CHOSEN_TITLE_PARAM_NAME, NOT_CHOSEN_TITLE_PARAM_VALUE);
		annotation.putString(NOT_CHOSEN_MESSAGE_PARAM_NAME, NOT_CHOSEN_MESSAGE_PARAM_VALUE);
		annotation.putString(CHOOSE_WINDOW_TITLE_PARAM_NAME, CHOOSE_WINDOW_TITLE_PARAM_VALUE);
		annotation.putString(CREATE_WINDOW_TITLE_PARAM_NAME, CREATE_WINDOW_TITLE_PARAM_VALUE);
		annotation.putString(SHOW_WINDOW_TITLE_PARAM_NAME, SHOW_WINDOW_TITLE_PARAM_VALUE);
		annotation.putString(UPDATE_WINDOW_TITLE_PARAM_NAME, UPDATE_WINDOW_TITLE_PARAM_VALUE);
		annotation.putString(DELETE_WINDOW_TITLE_PARAM_NAME, DELETE_WINDOW_TITLE_PARAM_VALUE);

		if ( notEmpty(config.addressJsNamespace) )
			annotation.putString(JS_NAMESPACE_PARAM_NAME, config.addressJsNamespace);

		if ( notEmpty(config.addressFormJsNamespace) )
			annotation.putString(FORM_JS_NAMESPACE_TITLE_PARAM_NAME, config.addressFormJsNamespace);

		out.writeLn(annotation.toStringAligned(""));
	}
	private void writeIsEmptyMethod() throws IOException {
		writeEmptyAnnotation(Transient.class);

		out.writeLn("\tpublic boolean ", IS_EMPTY_METHOD_NAME, "() {");

		out.writeLn("\t\treturn ", ZIP_CODE_FIELD_NAME, " == null");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", SUBJECT_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", DISTRICT_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", CITY_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", CITY_DISTRICT_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", LOCALITY_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", STREET_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", BLOCK_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", HOUSE_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", BUILDING_FIELD_NAME, ")");
		out.writeLn("\t\t\t&& ", EMPTY_METHOD_NAME, "(", APARTMENT_FIELD_NAME, ");");
		// todo: comment

		out.writeLn("\t}");
	}
	private void writeGetAddressMethod() throws IOException {
		String paramName = "address";

		out.writeLn("\tpublic static String ", GET_ADDRESS_METHOD_NAME, "(", ADDRESS_CLASS_SIMPLE_NAME, " ", paramName, ") {");
		out.writeLn("\t\tif (", paramName, " == null)");
		out.writeLn("\t\t\treturn null;");
		out.writeLn();

		out.writeLn("\t\treturn ", CONCAT_METHOD_NAME, "(");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(ZIP_CODE_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(SUBJECT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(DISTRICT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(CITY_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(CITY_DISTRICT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(LOCALITY_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(STREET_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(BLOCK_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(HOUSE_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(BUILDING_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(APARTMENT_FIELD_NAME), "())");
		out.writeLn("\t\t);");

		out.writeLn("\t}");
	}
	private void writeGetBuildingAddressMethod() throws IOException {
		String paramName = "address";
		out.writeLn("\tpublic static String ", GET_BUILDING_ADDRESS_METHOD_NAME, "(", ADDRESS_CLASS_SIMPLE_NAME, " ", paramName, ") {");
		out.writeLn("\t\tif (", paramName, " == null)");
		out.writeLn("\t\t\treturn null;");
		out.writeLn();

		out.writeLn("\t\treturn ", CONCAT_METHOD_NAME, "(");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(SUBJECT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(DISTRICT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(CITY_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(CITY_DISTRICT_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(LOCALITY_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(STREET_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(BLOCK_FIELD_NAME), "()), ", ADDRESS_SEPARATOR_FIELD_NAME, ",");
		out.writeLn("\t\t\t", GET_NOT_NULL_STRING_METHOD_NAME, "(", paramName, ".", getGetterName(HOUSE_FIELD_NAME), "())");
		out.writeLn("\t\t);");

		out.writeLn("\t}");
	}

	public static List<ClassField> getFields() {
		List<ClassField> fields = new ArrayList<ClassField>();

		fields.add( new ClassField(ID_FIELD_NAME, Integer.class) );
		fields.add( new ClassField(ZIP_CODE_FIELD_NAME, Integer.class) );
		fields.add( new ClassField(SUBJECT_FIELD_NAME, String.class) );
		fields.add( new ClassField(DISTRICT_FIELD_NAME, String.class) );
		fields.add( new ClassField(CITY_FIELD_NAME, String.class) );
		fields.add( new ClassField(CITY_DISTRICT_FIELD_NAME, String.class) );
		fields.add( new ClassField(LOCALITY_FIELD_NAME, String.class) );
		fields.add( new ClassField(STREET_FIELD_NAME, String.class) );
		fields.add( new ClassField(BLOCK_FIELD_NAME, String.class) );
		fields.add( new ClassField(HOUSE_FIELD_NAME, String.class) );
		fields.add( new ClassField(BUILDING_FIELD_NAME, String.class) );
		fields.add( new ClassField(APARTMENT_FIELD_NAME, String.class) );

		return fields;
	}

	protected ProjectConfig config;

	public static final String GET_NOT_NULL_STRING_METHOD_NAME = "getNotNullString";

	public static final String IS_EMPTY_METHOD_NAME = "isEmpty";
	public static final String GET_ADDRESS_METHOD_NAME = "getAddress";
	public static final String GET_BUILDING_ADDRESS_METHOD_NAME = "getBuildingAddress";

	public static final String ADDRESS_CLASS_SIMPLE_NAME = "Address";
	public static final String ADDRESS_SEPARATOR_FIELD_NAME = "ADDRESS_SEPARATOR";
	public static final String ADDRESS_SEPARATOR_FIELD_VALUE = ",";

	public static final String ZIP_CODE_FIELD_NAME = "zipCode";
	public static final String SUBJECT_FIELD_NAME = "subject";
	public static final String DISTRICT_FIELD_NAME = "district";
	public static final String CITY_FIELD_NAME = "city";
	public static final String CITY_DISTRICT_FIELD_NAME = "cityDistrict";
	public static final String LOCALITY_FIELD_NAME = "locality";
	public static final String STREET_FIELD_NAME = "street";
	public static final String BLOCK_FIELD_NAME = "block";
	public static final String HOUSE_FIELD_NAME = "house";
	public static final String BUILDING_FIELD_NAME = "building";
	public static final String APARTMENT_FIELD_NAME = "apartment";

	// ExtEntity annotation
	public static final String LIST_WINDOW_TITLE_PARAM_NAME = "listWindowTitle";
	public static final String NOT_CHOSEN_TITLE_PARAM_NAME = "notChosenTitle";
	public static final String NOT_CHOSEN_MESSAGE_PARAM_NAME = "notChosenMessage";
	public static final String CHOOSE_WINDOW_TITLE_PARAM_NAME = "chooseWindowTitle";
	public static final String CREATE_WINDOW_TITLE_PARAM_NAME = "createWindowTitle";
	public static final String SHOW_WINDOW_TITLE_PARAM_NAME = "showWindowTitle";
	public static final String UPDATE_WINDOW_TITLE_PARAM_NAME = "updateWindowTitle";
	public static final String DELETE_WINDOW_TITLE_PARAM_NAME = "deleteWindowTitle";
	public static final String JS_NAMESPACE_PARAM_NAME = "jsNamespace";
	public static final String FORM_JS_NAMESPACE_TITLE_PARAM_NAME = "formJsNamespace";

	// values // todo: read this values from property file
	public static final String LIST_WINDOW_TITLE_PARAM_VALUE = "Список адресов";
	public static final String NOT_CHOSEN_TITLE_PARAM_VALUE = "Адрес не выбран";
	public static final String NOT_CHOSEN_MESSAGE_PARAM_VALUE = "Выберите адрес";
	public static final String CHOOSE_WINDOW_TITLE_PARAM_VALUE = "Выбор адреса";
	public static final String CREATE_WINDOW_TITLE_PARAM_VALUE = "Ввод адреса";
	public static final String SHOW_WINDOW_TITLE_PARAM_VALUE = "Просмотр адреса";
	public static final String UPDATE_WINDOW_TITLE_PARAM_VALUE = "Изменение адреса";
	public static final String DELETE_WINDOW_TITLE_PARAM_VALUE = "Удаление адреса";
}