/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter.js;

import su.opencode.kefir.gen.ExtEntity;
import su.opencode.kefir.gen.field.*;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.web.JsonServlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.EXT_INPUT_TEXT_MASK_CLASS_NAME;
import static su.opencode.kefir.gen.fileWriter.js.JsUtils.ACTION_VAR_NAME;
import static su.opencode.kefir.util.StringUtils.capitalize;
import static su.opencode.kefir.util.StringUtils.concat;

public class FormJsFileWriter extends JsFileWriter
{
	public FormJsFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.dir = getJsDirectory(extEntity, entityClass);
		this.fileName = getFormJsFileName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeFile() throws IOException {
		writeNamespace();
		writeNamespaceHeader(getNamespace());

		writeConstants();
		writeVariables();

		// functions
		writeComment("functions");
		writeFillAddressFieldsFunctions();
		writeFillChooseFieldsFunctions(); // функции заполнения полей выбора
		out.writeLn();
		writeFillFilterChooseFieldsFunctions(); // функции жесткого заполнения полей выбора
		out.writeLn();
		writeFillFormFields();

		writeGetItems();

		// buttons functions
		// save button
		writeGetSaveUrl();
		writeGetSaveWaitMsg();
		writeGetSaveErrorMsgTitle();
		writeGetSaveButtonText();

		if (hasAttachmentsFields(entityClass))
			writeSaveButtonFunctionsForEntityWithAttachments();
		else
			writeGetSaveButton();

		// cancel button
		writeGetCancelButtonId();
		writeGetCancelButtonText();

		if (hasAttachmentsFields(entityClass))
			writeCancelButtonFunctionsForEntityWithAttachments();
		else
			writeGetCancelButton();

		writeGetButtons();

		// common form functions
		writeInitPanel();
		writeGetDefaultButton();
		writeGetWindowTitle();
		writeInitWindow();
		writeShowWindow();

		writeInitParams();
		writeInit();

		if (hasAttachmentsFields(entityClass))
		{ // functions for initial load of entity attachments to form
			writeLoadAttachmentsCallback();
			writeLoadAttachments();
		}

		// return hash
		if (hasAttachmentsFields(entityClass))
			writeReturnForEntityWithAtachments();
		else
			writeReturn();

		writeNamespaceFooter(); // end common namespace
	}
	private String getNamespace() {
		return concat( sb, getJsNamespace(extEntity, entityClass), PACKAGE_SEPARATOR, getFormJsNamespace(extEntity, entityClass) );
	}

	private void writeConstants() throws IOException {
		writeConstant(ACTION_CONSTANT_NAME, COMMON_ACTION_CONSTANT_NAME, false); // shortcut for common ACTION js constant

		if (hasAttachmentsFields(entityClass))
		{ // ENTITY_NAME constant
			writeConstant(ENTITY_NAME_CONSTANT_NAME, entityClass.getName());
		}

		out.writeLn();
		writeConstant(WINDOW_ID_CONSTANT_NAME, getFormWindowId(extEntity, entityClass));
		writeConstant(WINDOW_WIDTH_CONSTANT_NAME, extEntity.formWindowWidth());
		writeConstant(LABEL_WIDTH_CONSTANT_NAME, extEntity.formLabelWidth());
		writeConstant(GET_URL_CONSTANT_NAME, getGetServletUrl(extEntity, entityClass));

		if (extEntity.useVoForGetById())
			writeConstant(VO_CLASS_CONSTANT_NAME, getListVOClassName(extEntity, entityClass));

		out.writeLn();

		writeFormButtonsParamsConstants();

		// fields ids constants
		writeComment("fields ids");
		writeFieldsIdsConstants();

		if (hasAddressFields(entityClass))
		{
			writeComment("address fields");
			writeAddressFieldsConstants();
		}

		if (hasAttachmentsFields(entityClass))
		{
			// attachments fields constants
			writeComment("attachments");
			writeAttachmentFieldsConstants();
		}

		// save button
		writeConstant(SAVE_BUTTON_ID_CONSTANT_NAME, getFormSaveButtonId(extEntity, entityClass));
		out.writeLn();

		// FIELDS_IDS array constant
		writeFieldsIdsConstant();

		// choose fields filled fields (поля, заполняемые при выборе связанной сущности)
		writeChooseFieldsFilledFields();

		out.writeLn();
	}
	private void writeFormButtonsParamsConstants() throws IOException {
		writeComment("form buttons params");
		// create
		writeConstant(CREATE_URL_CONSTANT_NAME, getCreateServletUrl(extEntity, entityClass));
		writeConstant(CREATE_WINDOW_TITLE_CONSTANT_NAME, extEntity.createWindowTitle());
		writeConstant(CREATE_SAVE_BUTTON_TEXT_CONSTANT_NAME, extEntity.createSaveButtonText());
		writeConstant(CREATE_SAVE_WAIT_MSG_CONSTANT_NAME, extEntity.createSaveWaitMessage());
		writeConstant(CREATE_SAVE_ERROR_MSG_CONSTANT_NAME, extEntity.createSaveErrorMessage());
		writeConstant(CREATE_CANCEL_BUTTON_ID_CONSTANT_NAME, getCreateCancelButtonId(extEntity, entityClass));
		writeConstant(CREATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.createCancelButtonText());
		out.writeLn();

		// show
		// todo: write get servlet constant if needed
		writeConstant(SHOW_WINDOW_TITLE_CONSTANT_NAME, extEntity.showWindowTitle());
		writeConstant(SHOW_CANCEL_BUTTON_ID_CONSTANT_NAME, getShowCancelButtonId(extEntity, entityClass));
		writeConstant(SHOW_CANCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.showCancelButtonText());
		out.writeLn();

		// update
		writeConstant(UPDATE_URL_CONSTANT_NAME, getUpdateServletUrl(extEntity, entityClass));
		writeConstant(UPDATE_WINDOW_TITLE_CONSTANT_NAME, extEntity.updateWindowTitle());
		writeConstant(UPDATE_SAVE_BUTTON_TEXT_CONSTANT_NAME, extEntity.updateSaveButtonText());
		writeConstant(UPDATE_SAVE_WAIT_MSG_CONSTANT_NAME, extEntity.updateSaveWaitMessage());
		writeConstant(UPDATE_SAVE_ERROR_MSG_CONSTANT_NAME, extEntity.updateSaveErrorMessage());
		writeConstant(UPDATE_CANCEL_BUTTON_ID_CONSTANT_NAME, getUpdateCancelButtonId(extEntity, entityClass));
		writeConstant(UPDATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.updateCancelButtonText());
		out.writeLn();

		// delete
		writeConstant(DELETE_URL_CONSTANT_NAME, getDeleteServletUrl(extEntity, entityClass));
		writeConstant(DELETE_WINDOW_TITLE_CONSTANT_NAME, extEntity.deleteWindowTitle());
		writeConstant(DELETE_SAVE_BUTTON_TEXT_CONSTANT_NAME, extEntity.deleteSaveButtonText());
		writeConstant(DELETE_SAVE_WAIT_MSG_CONSTANT_NAME, extEntity.deleteSaveWaitMessage());
		writeConstant(DELETE_SAVE_ERROR_MSG_CONSTANT_NAME, extEntity.deleteSaveErrorMessage());
		writeConstant(DELETE_CANCEL_BUTTON_ID_CONSTANT_NAME, getDeleteCancelButtonId(extEntity, entityClass));
		writeConstant(DELETE_CANCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.deleteCancelButtonText());
		out.writeLn();
	}

	private void writeFieldsIdsConstants() throws IOException {
		String fieldPrefix = getJsFieldPrefix(extEntity, entityClass);
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields)
		{
			if ( !hasFieldAnnotation(field) || hasAttachmentsFieldAnnotation(field) || hasAddressFieldAnnotation(field) ) // если поле не помечено аннотацией или является аттачем, id для него не генерится
				continue;

			// todo: возможность исключения поля из формы через аннотацию
			ChooseField chooseField = getChooseFieldAnnotation(field);
			if (chooseField != null)
			{ // поле выбора другой сущности
				writeChooseFieldIdsConstants(fieldPrefix, field, chooseField);
			}
			else
			{ // обычное поле (не выбора)
				writeFieldIdConstant(field);
			}
		}

		out.writeLn();
	}

	private void writeChooseFieldIdsConstants(String fieldPrefix, Field field, ChooseField chooseField) throws IOException {
		out.writeLn();

		// id: constant - пишется всегда
		writeFieldIdConstant(field);

		// текстовые поля сущности, которые нужно заполнять
		for (ChooseFieldTextField textField : chooseField.fields())
		{
			String fieldIdConstantName = getChooseFieldTextFieldIdConstantName(field, textField);
			String fieldIdConstantValue = concat( sb, fieldPrefix, JS_FIELD_NAME_SEPARATOR, field.getName(), JS_FIELD_NAME_SEPARATOR, textField.name() );
			writeConstant(fieldIdConstantName, fieldIdConstantValue);
		}

		// choose button id constant
		String chooseButtonIdConstantName = getChooseButtonIdConstantName(field);
		writeConstant(chooseButtonIdConstantName, getChooseButtonId(extEntity, entityClass, chooseField, field));

		// choose button text constant
		String chooseButtonTextConstantName = getChooseButtonTextConstantName(field);
		writeConstant(chooseButtonTextConstantName, chooseField.chooseButtonText());

		// show button id constant
		String showButtonIdConstantName = getShowButtonIdConstantName(field);
		writeConstant(showButtonIdConstantName, getShowButtonId(extEntity, entityClass, chooseField, field));

		// show button text constant
		String showButtonTextConstantName = getShowButtonTextConstantName(field);
		writeConstant(showButtonTextConstantName, chooseField.showButtonText());

		out.writeLn();
	}
	private String getChooseFieldIdIdConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_ID_CONSTANT_POSTFIX );
	}
	private String getChooseFieldTextFieldIdConstantName(Field field, ChooseFieldTextField textField) {
		return concat( sb, getConstantName(field), CONSTANT_NAME_SEPARATOR, getConstantName(textField.name()), FIELD_ID_CONSTANT_POSTFIX );
	}
	private String getChooseButtonIdConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_CHOOSE_BUTTON_ID_CONSTANT_POSTFIX );
	}
	private String getChooseButtonTextConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_CHOOSE_BUTTON_TEXT_CONSTANT_POSTFIX );
	}
	private String getShowButtonIdConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_SHOW_BUTTON_ID_CONSTANT_POSTFIX );
	}
	private String getShowButtonTextConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_SHOW_BUTTON_TEXT_CONSTANT_POSTFIX );
	}
	private void writeFieldIdConstant(Field field) throws IOException {
		String constantName = hasChooseFieldAnnotation(field) ? getChooseFieldIdIdConstantName(field) : getFieldIdConstantName(field);
		writeConstant(constantName, getFieldId(extEntity, entityClass, field) );
	}
	private String getFieldIdConstantName(Field field) {
		return concat( sb, getConstantName(field), FIELD_ID_CONSTANT_POSTFIX );
	}

	private void writeFieldsIdsConstant() throws IOException {
		List<String> fieldsIds = collectFieldsIds();

		out.writeLn("\tvar ", FIELDS_IDS_CONSTANT_NAME, " = [");

		out.write("\t\t");

		// all fields except last with comma after them
		for (int i = 0; i < fieldsIds.size() - 1; i++)
			out.write(fieldsIds.get(i), ", ");

		// last field without comma
		out.write(fieldsIds.get( fieldsIds.size() - 1) );

		out.writeLn();

		out.writeLn( "\t];");
		out.writeLn();
	}
	private List<String> collectFieldsIds() {
		List<String> fieldsIds = new ArrayList<String>();

		// фокус полей формы
		for (Field field : entityClass.getDeclaredFields())
		{
			if ( !hasFieldAnnotation(field) || hasIdFieldAnnotation(field) || hasAttachmentsFieldAnnotation(field) )
				continue;

			if ( hasAddressFieldAnnotation(field) )
			{
				fieldsIds.add( getAddressFieldUpdateButtonIdConstantName(field) );
			}
			else if ( hasChooseFieldAnnotation(field) )
			{ // поле выбора -> фокусируется кнопка выбора
				fieldsIds.add( getChooseButtonIdConstantName(field) );
			}
			else
			{ // обычное поле -> фокусируется само поле
				fieldsIds.add( getFieldIdConstantName(field) );
			}
		}

		// последней фокусируется кнопка сохранения
		fieldsIds.add(SAVE_BUTTON_ID_CONSTANT_NAME);

		// или кнопка закрытия для формы показа
		fieldsIds.add(SHOW_CANCEL_BUTTON_ID_CONSTANT_NAME);
		return fieldsIds;
	}

	private void writeAddressFieldsConstants() throws IOException {
		for (Field field : getAddressFields(entityClass))
		{
			AddressField addressField = getAddressFieldAnnotation(field);

			writeConstant( getAddressFieldTextFieldIdConstantName(field), getTextFieldId(extEntity, entityClass, addressField, field) );
			writeConstant( getAddressFieldUpdateButtonIdConstantName(field), getUpdateButtonId(extEntity, entityClass, addressField, field) );
			writeConstant( getAddressFieldUpdateButtonTextConstantName(field), addressField.updateButtonText() );
			writeConstant( getAddressFieldWindowTitleConstantName(field), addressField.addressWindowTitle() );
			out.writeLn();
		}
	}
	private String getAddressFieldTextFieldIdConstantName(Field field) {
		return concat( sb, getConstantName(field), ADDRESS_FIELD_TEXT_FIELD_ID_CONSTANT_POSTFIX );
	}
	private String getAddressFieldUpdateButtonIdConstantName(Field field) {
		return concat( sb, getConstantName(field), ADDRESS_FIELD_UPDATE_BUTTON_ID_CONSTANT_POSTFIX );
	}
	private String getAddressFieldUpdateButtonTextConstantName(Field field) {
		return concat( sb, getConstantName(field), ADDRESS_FIELD_UPDATE_BUTTON_TEXT_CONSTANT_POSTFIX );
	}
	private String getAddressFieldWindowTitleConstantName(Field field) {
		return concat( sb, getConstantName(field), ADDRESS_FIELD_WINDOW_TITLE_CONSTANT_POSTFIX );
	}

	private void writeAttachmentFieldsConstants() throws IOException {
		List<Field> attachmentsFields = getAttachmentsFields(entityClass);
		writeConstant(ATTACHMENT_FIELDS_COUNT_CONSTANT_NAME, attachmentsFields.size());
		out.writeLn();

		for (Field field : attachmentsFields)
		{
			AttachmentsField attachmentsField = getAttachmentsFieldAnnotation(field);

			writeComment(concat(sb, field.getName(), " field UploadPanel constants"));
			writeConstant( getAttachmentsFieldEntityFieldNameConstantName(field), getAttachmentsFieldEntityFieldName(attachmentsField, field) );
			writeConstant( getAttachmentsFieldPanelIdConstantName(field), getAttachmentsFieldPanelId(attachmentsField, field, extEntity, entityClass) );
			writeConstant( getAttachmentsFieldPanelWidthConstantName(field), attachmentsField.panelWidth() );
			writeConstant( getAttachmentsFieldPanelHeightConstantName(field), attachmentsField.panelHeight() );
			out.writeLn();
		}
	}
	private String getAttachmentsFieldEntityFieldNameConstantName(Field field) {
		return concat( sb, getConstantName(field), ATTACHMENT_FIELD_ENTITY_FIELD_NAME_CONSTANT_POSTFIX );
	}
	private String getAttachmentsFieldPanelIdConstantName(Field field) {
		return concat( sb, getConstantName(field), ATTACHMENT_FIELD_PANEL_ID_CONSTANT_POSTFIX );
	}
	private String getAttachmentsFieldPanelWidthConstantName(Field field) {
		return concat( sb, getConstantName(field), ATTACHMENT_FIELD_PANEL_WIDTH_CONSTANT_POSTFIX );
	}
	private String getAttachmentsFieldPanelHeightConstantName(Field field) {
		return concat( sb, getConstantName(field), ATTACHMENT_FIELD_PANEL_HEIGHT_CONSTANT_POSTFIX );
	}

	private void writeChooseFieldsFilledFields() throws IOException {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasChooseFieldAnnotation(field) )
				writeChooseFieldFilledFields(field);
	}
	private void writeChooseFieldFilledFields(Field field) throws IOException {
		String fieldsConstantName = getChooseFieldFieldsConstantName(field);
		out.writeLn("\tvar ", fieldsConstantName, " = [");

		List<ChooseFieldField> chooseFieldFields = collectChooseFieldFields(field);
		for (int i = 0; i < chooseFieldFields.size() - 1; i++)
		{
			ChooseFieldField chooseFieldField = chooseFieldFields.get(i);

			String jsArrayElement = chooseFieldField.getJsArrayElement();
			out.writeLn("\t\t", jsArrayElement, ", ");
		}

		// last array element without comma
		ChooseFieldField chooseFieldField = chooseFieldFields.get( chooseFieldFields.size() - 1 );
		String jsArrayElement = chooseFieldField.getJsArrayElement();
		out.writeLn("\t\t", jsArrayElement);

		out.writeLn("\t];");
		out.writeLn();
	}
	private List<ChooseFieldField> collectChooseFieldFields(Field field) {
		List<ChooseFieldField> fields = new ArrayList<ChooseFieldField>();
		ChooseField chooseField = getChooseFieldAnnotation(field);

		fields.add( new ChooseFieldField(getFieldName(field), chooseField.idFieldName(), null) ); // поле, содержащее id выбранной сущности в форме имеет имя, равное имени поля

		for (ChooseFieldTextField textField : chooseField.fields())
		{
			String formFieldName = getChooseFieldFieldName(field, textField); // имя поля в форме сущности, которое будет заполняться полем из выбранной связанной сущности
			fields.add( new ChooseFieldField(formFieldName, textField.name(), textField.renderer()) );
		}

		return fields;
	}
	private String getChooseFieldFieldsConstantName(Field field) {
		return concat( sb, getConstantName(field), CHOOSE_FIELD_FIELDS_CONSTANT_POSTFIX );
	}
	private String getChooseFieldFieldName(Field field, ChooseFieldTextField textField) {
		return concat( sb, field.getName(), capitalize( textField.name() ) );
	}

	private void writeVariables() throws IOException {
		writeVariable(WINDOW_VAR_NAME);
		writeVariable(PANEL_VAR_NAME);
		writeVariable(ACTION_VAR_NAME);
		writeVariable( getFormEntityVarName(extEntity, entityClass) );
		writeVariable(FILL_FORM_FUNCTION_VAR_NAME);
		writeVariable(SUCCESS_HANDLER_VAR_NAME);
		out.writeLn();

		if (hasFilterConfigFormFilterFields(entityClass))
			writeFilterConfigVariables();

		if (hasAddressFields(entityClass))
			writeAddressFieldsVariables();

		if (hasAttachmentsFields(entityClass))
			writeAttachmentsVariables();
	}
	private void writeFilterConfigVariables() throws IOException {
		writeComment("filter params");

		for (Field field : getFilterConfigFormFilterFields(entityClass))
		{
			writeVariable( getFilterVarName(field) );
		}

		out.writeLn();
	}

	private void writeAddressFieldsVariables() throws IOException {
		for (Field field : getAddressFields(entityClass))
			writeVariable(getAddressFieldVarName(field));

		out.writeLn();
	}
	private String getAddressFieldVarName(Field field) {
		return field.getName();
	}

	private void writeAttachmentsVariables() throws IOException {
		writeVariable(ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME); // переменная-счетчик для количества загрузок аттачей в разные UploadPanel

		for (Field field : getAttachmentsFields(entityClass))
			writeVariable(getAttachmentsFieldAttachmentsVarName(field));

		out.writeLn();
	}
	private String getAttachmentsFieldAttachmentsVarName(Field field) {
		return concat( sb, getFieldName(field), ATTACHMENT_FIELD_ATTACHMENTS_VAR_POSTFIX );
	}

	private void writeFillAddressFieldsFunctions() throws IOException {
		for (Field field : getAddressFields(entityClass))
			writeFillAddressFieldFieldsFunction(field);

		out.writeLn();
	}
	private void writeFillAddressFieldFieldsFunction(Field field) throws IOException {
		AddressField addressField = getAddressFieldAnnotation(field);

		String paramName = "_address";
		writeFunctionHeader( getFillAddressFieldFieldsFunctionName(field), paramName);

		out.writeLn(
			"\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getAddressFieldTextFieldIdConstantName(field), ").setValue( ", getGetAddressStrFunctionName(addressField, field), "(", paramName, ", ", addressField.building(), ") );"
		);
		out.writeLn("\t\t", getAddressFieldVarName(field), " = ", paramName, ";");

		writeFunctionFooter();
	}
	private String getFillAddressFieldFieldsFunctionName(Field field) {
		return concat(sb, "fill", capitalize( field.getName() ), "Fields");
	}

	private void writeFillChooseFieldsFunctions() throws IOException {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasChooseFieldAnnotation(field) )
				writeFillChooseFieldFieldsFunction(field);
	}
	private void writeFillChooseFieldFieldsFunction(Field field) throws IOException {
		String functionName = getFillChooseFieldFieldsFunctionName(field);
		String paramName = field.getName();
		writeFunctionHeader(functionName, paramName);

		out.writeLn("\t\tif (", paramName, ")"); // заполнять и разрешать просмотр только в случае, если связанная сущность есть
		out.writeLn("\t\t{");

		String formVarName = "form";
		writeVariable( formVarName, concat(sb, PANEL_VAR_NAME, ".getForm()"), "\t\t\t" );
		out.writeLn("\t\t\t", JsFileWriter.FILL_FORM_FIELDS_FUNCTION_NAME, "(", formVarName, ", ", paramName, ", ", getChooseFieldFieldsConstantName(field), ");");
		out.writeLn("\t\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getShowButtonIdConstantName(field), ").setDisabled(false);");

		out.writeLn("\t\t}"); // end if ()
		out.writeLn();

		out.writeLn("\t\t", FOCUS_NEXT_FUNCTION_NAME, "( ", EXT_GET_CMP_FUNCTION_NAME, "(", getChooseButtonIdConstantName(field), ") );");

		writeFunctionFooter(); // end fill...Fields function
	}
	private String getFillChooseFieldFieldsFunctionName(Field field) {
		return concat(sb, "fill", capitalize( field.getName() ), "Fields");
	}

	private void writeFillFilterChooseFieldsFunctions() throws IOException {
		for (Field field : getFilterConfigFormFilterChooseFields(entityClass))
			writeFillFilterChooseFieldFunction(field);
	}
	private void writeFillFilterChooseFieldFunction(Field field) throws IOException {
		String functionName = getFillFilterChooseFieldFunctionName(field);
		String paramName = field.getName();
		writeFunctionHeader(functionName, paramName);

		out.writeLn("\t\tif (!", paramName, ")");
		writeFunctionReturn("", "\t\t\t");
		out.writeLn();

		out.writeLn("\t\t", getFillChooseFieldFieldsFunctionName(field), "(", paramName, ");");
		out.writeLn("\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getChooseButtonIdConstantName(field), ").disable();");

		writeFunctionFooter(); // end fill... function
	}
	private String getFillFilterChooseFieldFunctionName(Field field) {
		return concat(sb, "fill", capitalize( field.getName()));
	}

	private void writeFillFormFields() throws IOException {
		writeFunctionHeader(FILL_FORM_FIELDS_FUNCTION_NAME);

		String formVarName = "form";
		writeVariable( formVarName, concat(sb, PANEL_VAR_NAME, ".getForm()") );
		out.writeLn();

		String entityVarName = getFormEntityVarName(extEntity, entityClass);

		for (Field field : entityClass.getDeclaredFields())
		{
			if ( !hasFieldAnnotation(field) )
				continue;

			if ( hasAddressFieldAnnotation(field) )
			{
				writeFillAddressField(field, entityVarName);
				continue;
			}

			if ( hasChooseFieldAnnotation(field) )
			{ // поле выбора -> вызвать функцию заполнения
				writeFillChooseField(field, entityVarName);
				continue;
			}

			if ( hasComboBoxFieldAnnotation(field) )
			{ // поле комбобокса -> вызвать спецфункцию заполнения комбобокса
				writeFillComboBox(field, formVarName, entityVarName);
				continue;
			}

			if ( hasAttachmentsFieldAnnotation(field) ) // аттачмент панели заполняются через отдельные ajaxRequest
				continue;

			// обычное поле -> заполнить через стандартную функцию Kefir.form.fillFormField
			writeFillField(field, formVarName, entityVarName);
		}

		out.writeLn("\t}");
		out.writeLn();
	}
	private void writeFillAddressField(Field field, String entityVarName) throws IOException {
		String fillFunctionName = getFillAddressFieldFieldsFunctionName(field);
		out.writeLn("\t\t", fillFunctionName, "( ", GET_VALUE_FUNCTION_NAME, "(", entityVarName, ", '", getVoFieldName(getAddressFieldAnnotation(field), field), "') );");
	}
	private void writeFillChooseField(Field field, String entityVarName) throws IOException {
		String fillFunctionName = getFillChooseFieldFieldsFunctionName(field);
		out.writeLn("\t\t", fillFunctionName, "( ", GET_VALUE_FUNCTION_NAME, "(", entityVarName, ", '", field.getName(), "') );");
	}
	private void writeFillComboBox(Field field, String formVarName, String entityVarName) throws IOException {
		ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);

		out.writeLn();

		String fieldValueVarName = concat(sb, field.getName(), "Value");
		writeVariable( fieldValueVarName, concat(sb, GET_VALUE_FUNCTION_NAME, "( ", GET_VALUE_FUNCTION_NAME, "(", entityVarName, ", '", field.getName(), "'), '", comboBoxField.valueField(), "' )") );

		out.writeLn(
			"\t\t", SET_COMBO_BOX_VALUE_FUNCTION_NAME, "( ", formVarName, ".findField('", getFieldName(field), "'), ", fieldValueVarName, " );"
		);
		// todo: если понадобится, выставлять rawValue, учитывая renderer из ComboBoxField

		out.writeLn();
	}
	private void writeFillField(Field field, String formVarName, String entityVarName) throws IOException {
		String entityFieldName = field.getName();
		String formFieldName = getFieldName(field);

		String fieldName;
		if (entityFieldName.equals(formFieldName))
			fieldName = concat(sb, "'", entityFieldName, "'");
		else
			fieldName = concat(sb, "[ '", formFieldName, "', '", entityFieldName, "' ]");

		String renderer = "";
		String fieldRenderer = getFieldRenderer(field);
		if ( fieldRenderer != null && !fieldRenderer.isEmpty() )
			renderer = concat(sb, ", ", fieldRenderer);

		out.writeLn(
			"\t\t", FILL_FORM_FIELD_FUNCTION_NAME, "(", formVarName, ", ", entityVarName, ", ", fieldName, renderer, ");"
		);
	}

	protected String getAddressFieldTextFieldVarName(Field field) {
		return concat(sb, field.getName(), "TextField");
	}
	protected String getAddressFieldUpdateButtonVarName(Field field) {
		return concat(sb, field.getName(), "UpdateButton");
	}

	protected String getChooseFieldChooseButtonVarName(Field field) {
		return concat(sb, field.getName(), "ChooseButton");
	}
	protected String getChooseFieldShowButtonVarName(Field field) {
		return concat(sb, field.getName(), "ShowButton");
	}
	protected String getChooseFieldTextFieldVarName(Field field, ChooseFieldTextField textField) {
		return concat(sb, field.getName(), capitalize(textField.name()), "Field");
	}
	protected String getChooseFieldHiddenIdVarName(Field field) {
		return concat(sb, field.getName(), "IdHiddenField");
	}

	protected String getAttachmentsFieldPanelVarName(Field field) { // название параметра конфига
		return concat(sb, field.getName(), "Panel");
	}

	protected String getIdHiddenFieldVarName() { // имя переменной, в которой передается поле id основной сущности
		return "idHiddenField";
	}

	// getItems
	private void writeGetItems() throws IOException {
		writeFunctionHeader(GET_ITEMS_FUNCTION_NAME);

		String disabledVarName = "disabled";
		writeVariable( disabledVarName, concat(sb, "(", ACTION_VAR_NAME, " == ", ACTION_CONSTANT_NAME, ".", ACTION_SHOW_CONSTANT_NAME, ") || (", ACTION_VAR_NAME, " == ", ACTION_CONSTANT_NAME, ".", ACTION_DELETE_CONSTANT_NAME, ")") );
		out.writeLn();

		// id hidden field
		Field idField = getIdField(entityClass);
		String idHiddenFieldVarName = getIdHiddenFieldVarName();
		writeVariable( idHiddenFieldVarName, concat(sb, GET_HIDDEN_FIELD_FUNCTION_NAME, "({}, ", getFieldIdConstantName(idField), ", '", getFieldName(idField), "')") );

		// other field variables
		writeFieldVariables(disabledVarName);

		// items var
		String itemsVarName = "items";
		out.writeLn();
		out.writeLn("\t\tvar ", itemsVarName, " = {");

		// первым без запятой пишется скрытое поле id
		out.writeLn("\t\t\t  ", idHiddenFieldVarName, ": ", idHiddenFieldVarName);
		writeFieldsHash();
		out.writeLn("\t\t};"); // end of items
		out.writeLn();

		writeFunctionReturn( concat(sb, getGetFormItemsLayoutFunctionFullName(extEntity, entityClass), "(", itemsVarName, ")") );
		out.writeLn("\t}"); // end of function getItems() {
		out.writeLn();
	}
	private void writeFieldsHash() throws IOException {
		for (Field field : entityClass.getDeclaredFields())
		{
			if ( !hasFieldAnnotation(field) )
				continue;

			// id поле уже учтено и написано самым первым, без запятой впереди
			if ( hasIdFieldAnnotation(field) )
				continue;

			if ( hasAddressFieldAnnotation(field) )
			{
				writeAddressFieldHashParams(field);
				continue;
			}

			if ( hasChooseFieldAnnotation(field) )
			{
				writeChooseFieldHashParams(field);
				continue;
			}

			if ( hasAttachmentsFieldAnnotation(field) )
			{
				writeAttachmentsFieldHashParam(field);
				continue;
			}

			// обычное поле — создается поле соответствующего типа
			writeFieldHashParams(field);
		}
	}
	private void writeAddressFieldHashParams(Field field) throws IOException {
		out.writeLn();
		writeComment( concat(sb, field.getName(), " fields"), "\t\t\t" );
		out.writeLn("\t\t\t, ", getAddressFieldTextFieldVarName(field), ": ", getAddressFieldTextFieldVarName(field));
		out.writeLn("\t\t\t, ", getAddressFieldUpdateButtonVarName(field), ": ", getAddressFieldUpdateButtonVarName(field));

		out.writeLn();
	}
	private void writeChooseFieldHashParams(Field field) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);

		out.writeLn();
		writeComment( concat(sb, field.getName(), " fields"), "\t\t\t" );
		out.writeLn("\t\t\t, ", getChooseFieldHiddenIdVarName(field), ": ", getChooseFieldHiddenIdVarName(field));

		for (ChooseFieldTextField textField : chooseField.fields())
			out.writeLn("\t\t\t, ", getChooseFieldTextFieldVarName(field, textField), ": ", getChooseFieldTextFieldVarName(field, textField));

		out.writeLn("\t\t\t, ", getChooseFieldChooseButtonVarName(field), ": ", getChooseFieldChooseButtonVarName(field));
		out.writeLn("\t\t\t, ", getChooseFieldShowButtonVarName(field), ": ", getChooseFieldShowButtonVarName(field));
		out.writeLn();
	}
	private void writeAttachmentsFieldHashParam(Field field) throws IOException {
		out.writeLn("\t\t\t, ", getAttachmentsFieldPanelVarName(field), ": ", getAttachmentsFieldPanelVarName(field)); // передать uploadPanel
	}

	private void writeFieldHashParams(Field field) throws IOException {
		out.writeLn("\t\t\t, ", field.getName(), ": ", getFieldName(field)); // названием поля конфига служит строго имя поля в классе, а значение (название переменной) уже может быть перегружено
	}

	private void writeFieldVariables(String disabledVarName) throws IOException {
		for (Field field : entityClass.getDeclaredFields())
		{
			if ( !hasFieldAnnotation(field) )
				continue;

			// id поле уже учтено и написано самым первым, без запятой впереди
			if ( hasIdFieldAnnotation(field) )
				continue;

			// обычное поле — создается поле соответствующего типа
			writeFieldVariable(field, disabledVarName);
		}
	}
	private void writeFieldVariable(Field field, String disabledVarName) throws IOException {
		if ( hasTextFieldAnnotation(field) )
		{
			writeTextFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasTextAreaFieldAnnotation(field) )
		{
			writeTextAreaFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasOgrnTextFieldAnnotation(field) )
		{
			writeOgrnTextFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasKppTextFieldAnnotation(field) )
		{
			writeKppTextFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasInnJuridicalTextFieldAnnotation(field) )
		{
			writeInnJuridicalTextFieldVariable(field, disabledVarName);
			return;
		}

		if ( hasIntegerFieldAnnotation(field) )
		{
			writeIntegerFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasSpinnerFieldAnnotation(field) )
		{
			writeSpinnerFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasNumberFieldAnnotation(field) )
		{
			writeNumberFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasDateFieldAnnotation(field) )
		{
			writeDateFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasCheckboxFieldAnnotation(field) )
		{
			writeCheckboxFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasLocalComboBoxFieldAnnotation(field) )
		{
			writeLocalComboBoxFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasComboBoxFieldAnnotation(field) )
		{
			writeComboBoxFieldVariable(field, disabledVarName);
			return;
		}
		if ( hasAddressFieldAnnotation(field) )
		{ // поле адреса - создаются поля: текстовое поле для адреса одной строкой, кнопка изменения адреса
			writeAddressFieldVariables(field, disabledVarName);
			return;
		}
		if ( hasChooseFieldAnnotation(field) )
		{ // поле выбора - создаются поля: скрытое id, выбираемые поля, кнопка выбора, кнопка просмотра
			writeChooseFieldVariables(field, disabledVarName);
			return;
		}
		if ( hasAttachmentsFieldAnnotation(field) )
		{
			writeAttachmentsFieldVariable(field, disabledVarName);
			return;
		}

		throw new IllegalArgumentException( concat(sb, "Unknown field annotation for field: ", field.getName()) );
	}

	private void writeTextFieldVariable(Field field, String disabledVarName) throws IOException {
		TextField textField = getTextFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( !textField.vtype().isEmpty() )
			config.putString("vtype", textField.vtype());

		if ( !textField.mask().isEmpty() )
			config.put("plugins", new JsArray( concat(sb, "new ", EXT_INPUT_TEXT_MASK_CLASS_NAME, "('", textField.mask(), "', true)") ));

		if ( !textField.style().isEmpty() )
		{
			config.put("style", textField.style());
		}
		else
		{
			if ( !textField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
				config.put("style", "{ textTransform: 'none'}");
		}

//		var strField = Kefir.form.getTextField({ disabled: disabled }, STR_FIELD_FIELD_ID, 'strField', 'Строковое поле', 200, 200, false);
		String varValue = concat(sb,
			GET_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", textField.label(), "'",
				", ", textField.width(),
				", ", textField.maxLength(),
				", ", textField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}

	private void writeTextAreaFieldVariable(Field field, String disabledVarName) throws IOException {
		TextAreaField textAreaField = getTextAreaFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( !textAreaField.vtype().isEmpty() )
			config.putString("vtype", textAreaField.vtype());

		if ( !textAreaField.style().isEmpty() )
		{
			config.put("style", textAreaField.style());
		}

		int maxLength = textAreaField.maxLength();
		if (textAreaField.blob())
			maxLength = TextAreaField.BLOB_MAX_LENGTH;

		String varValue = concat(sb,
			GET_TEXT_AREA_FIELD_FUNCTION_NAME, "(",
			config,
			", ", getFieldIdConstantName(field),
			", '", getFieldName(field), "'",
			", '", textAreaField.label(), "'",
			", ", textAreaField.width(),
			", ", maxLength,
			", ", textAreaField.rows(),
			", ", textAreaField.cols(),
			", ", textAreaField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}

	private void writeOgrnTextFieldVariable(Field field, String disabledVarName) throws IOException {
		OgrnTextField ogrnTextField = getOgrnTextFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

//		Kefir.form.getOgrnTextField({}, 'developer-ogrn', 'ogrn', 'ОГРН', 150, false),
		String varValue = concat(sb,
			GET_OGRN_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", ogrnTextField.label(), "'",
				", ", ogrnTextField.width(),
				", ", ogrnTextField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeKppTextFieldVariable(Field field, String disabledVarName) throws IOException {
		KppTextField kppTextField = getKppTextFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

//		Kefir.form.getKppTextField({}, 'developer-kpp', 'kpp', 'КПП', 150, false),
		String varValue = concat(sb,
			GET_KPP_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", kppTextField.label(), "'",
				", ", kppTextField.width(),
				", ", kppTextField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeInnJuridicalTextFieldVariable(Field field, String disabledVarName) throws IOException {
		InnJuridicalTextField innJuridicalTextField = getInnJuridicalTextFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

//		Kefir.form.getInnJuridicalTextField({}, 'developer-inn', 'inn', 'ИНН', 150, false),
		String varValue = concat(sb,
			GET_INN_JURIDICAL_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", innJuridicalTextField.label(), "'",
				", ", innJuridicalTextField.width(),
				", ", innJuridicalTextField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeIntegerFieldVariable(Field field, String disabledVarName) throws IOException {
		IntegerField integerField = getIntegerFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);
		config.put("allowDecimals", "false");

		if ( !integerField.vtype().isEmpty() )
			config.putString("vtype", integerField.vtype());

		if ( integerField.allowZero() )
			config.put("allowZero", "true");

		if ( integerField.allowNegative() )
			config.put("allowNegative", "true");

		if ( integerField.minValue() != IntegerField.DEFAULT_MIN_VALUE )
			config.put("minValue", integerField.minValue());

		if ( integerField.maxValue() != IntegerField.DEFAULT_MAX_VALUE )
			config.put("maxValue", integerField.maxValue());

//		var intField = Kefir.form.getNumberField({ disabled: disabled, allowZero: true, allowDecimals: false }, INT_FIELD_FIELD_ID, 'intField', 'Целое поле', 100, 9, false);
		String varValue = concat(sb,
			GET_NUMBER_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", integerField.label(), "'",
				", ", integerField.width(),
				", ", integerField.maxLength(),
				", ", integerField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeSpinnerFieldVariable(Field field, String disabledVarName) throws IOException {
		SpinnerField spinnerField = getSpinnerFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( spinnerField.allowBlank() )
			config.put("allowBlank", "true");

		if ( spinnerField.maxLength() != SpinnerField.DEFAULT_MAX_LENGTH )
			config.put("maxLength", spinnerField.maxLength());

		if ( spinnerField.width() != SpinnerField.DEFAULT_WIDTH )
			config.put("width", spinnerField.width());

		if ( !spinnerField.vtype().isEmpty() )
			config.putString("vtype", spinnerField.vtype());

		if ( spinnerField.allowNegative() )
			config.put("allowNegative", "true");

		if ( spinnerField.minValue() != SpinnerField.DEFAULT_MIN_VALUE )
			config.put("minValue", spinnerField.minValue());

		if ( spinnerField.maxValue() != SpinnerField.DEFAULT_MAX_VALUE )
			config.put("maxValue", spinnerField.maxValue());

		String defaultValue = Integer.toString(spinnerField.defaultValue());
		if ( spinnerField.noDefaultValue() )
			defaultValue = "null";

//		var intSpinnerField = Kefir.form.getSpinnerField({ disabled: disabled, maxLength: 2, minValue: 10, maxValue: 90 }, INT_SPINNER_FIELD_FIELD_ID, 'intSpinnerField', 'Целое спиннер поле', 15); // последнее - исходное значение
		String varValue = concat(sb,
			GET_SPINNER_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", spinnerField.label(), "'",
				", ", defaultValue,
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeNumberFieldVariable(Field field, String disabledVarName) throws IOException {
		NumberField numberField = getNumberFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( !numberField.vtype().isEmpty() )
			config.putString("vtype", numberField.vtype());

		if ( numberField.allowNegative() )
			config.put("allowNegative", "true");

		if ( numberField.minValue() != NumberField.DEFAULT_MIN_VALUE )
			config.put("minValue", numberField.minValue());

		if ( numberField.maxValue() != NumberField.DEFAULT_MAX_VALUE )
			config.put("maxValue", numberField.maxValue());

//		var doubleField = Kefir.form.getNumberField({ disabled: disabled }, DOUBLE_FIELD_FIELD_ID, 'doubleField', 'Дробное поле', 100, 10, false);
		String varValue = concat(sb,
			GET_NUMBER_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", numberField.label(), "'",
				", ", numberField.width(),
				", ", numberField.maxLength(),
				", ", numberField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeDateFieldVariable(Field field, String disabledVarName) throws IOException {
		DateField dateField = getDateFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( !dateField.minValue().isEmpty() )
			config.putDate("minValue", dateField.minValue());

		if ( !dateField.maxValue().isEmpty() )
			config.putDate("maxValue", dateField.maxValue()); // здесь по умолчанию new Date()

		// todo: установка defaultValue с возможностью указания NOW (и в js будет писаться defaultValue: new Date()

//		var dateField = Kefir.form.getDateField({ disabled: disabled }, DATE_FIELD_FIELD_ID, 'dateField', 'Датовое поле', false);
		String varValue = concat(sb,
			GET_DATE_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", dateField.label(), "'",
				", ", dateField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeCheckboxFieldVariable(Field field, String disabledVarName) throws IOException {
		CheckboxField checkboxField = getCheckboxFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( checkboxField.defaultValue() )
			config.put("checked", "true");

//		var booleanField = Kefir.form.getCheckbox({ disabled: disabled, checked: true }, BOOLEAN_FIELD_FIELD_ID, 'booleanField', 'Булево поле', false);
		String varValue = concat(sb,
			GET_CHECKBOX_FUNCTION_NAME, "(",
				config,
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", checkboxField.label(), "'",
				", ", checkboxField.allowBlank(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeLocalComboBoxFieldVariable(Field field, String disabledVarName) throws IOException {
		LocalComboBoxField localComboBoxField = getLocalComboBoxFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);
		config.put("editable", localComboBoxField.editable());

		if ( !localComboBoxField.vtype().isEmpty() )
			config.putString("vtype", localComboBoxField.vtype());

		if ( !localComboBoxField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
			config.put("style", "{ textTransform: 'none' }");

		if ( !localComboBoxField.valueField().equals(LocalComboBoxField.DEFAULT_VALUE_FIELD_NAME) )
			config.putString("valueField", localComboBoxField.valueField());

		if ( !localComboBoxField.displayField().equals(LocalComboBoxField.DEFAULT_DISPLAY_FIELD_NAME) )
			config.putString("displayField", localComboBoxField.displayField());

//		var enumField = Kefir.form.getLocalComboBox({ disabled: disabled, editable: false, style: {} }, test.TestEnumStore, ENUM_FIELD_FIELD_ID, 'enumField', 'enumField', 'Энумовое поле', 200, 200, 100, false, true);
		String varValue = concat(sb,
			GET_LOCAL_COMBO_BOX_FUNCTION_NAME, "(",
				config,
				", ", getLocalComboBoxStore(localComboBoxField, field),
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", getLocalComboBoxHiddenName(localComboBoxField, field), "'",
				", '", localComboBoxField.label(), "'",
				", ", localComboBoxField.width(),
				", ", getLocalComboBoxListWidth(localComboBoxField, field),
				", ", localComboBoxField.maxLength(),
				", ", localComboBoxField.allowBlank(),
				", ", localComboBoxField.forceSelection(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}
	private void writeComboBoxFieldVariable(Field field, String disabledVarName) throws IOException {
		ComboBoxField comboBoxField = getComboBoxFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);

		if ( !comboBoxField.editable() )
			config.put("editable", comboBoxField.editable());

		if ( comboBoxField.typeAhead() )
			config.put("typeAhead", comboBoxField.typeAhead());

		if ( !comboBoxField.vtype().isEmpty() )
			config.putString("vtype", comboBoxField.vtype());

		if ( !comboBoxField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
			config.put("style", "{ textTransform: 'none' }");

		if ( !comboBoxField.queryParam().equals(ComboBoxField.DEFAULT_QUERY_PARAM) )
			config.putString("queryParam", comboBoxField.queryParam());

		if ( !comboBoxField.valueField().equals(ComboBoxField.DEFAULT_VALUE_FIELD_NAME) )
			config.putString("valueField", comboBoxField.valueField());

		if ( !comboBoxField.displayField().equals(ComboBoxField.DEFAULT_DISPLAY_FIELD_NAME) )
			config.putString("displayField", comboBoxField.displayField());

		if ( comboBoxField.fields().length != 0 )
		{ // переопределен набор полей для store комбобокса
			JsArray fieldsArray = new JsArray();

			for (ComboBoxStoreField storeField : comboBoxField.fields())
			{
				JsHash fieldHash = new JsHash();
			 	fieldHash.putString("name", storeField.name());
			 	fieldHash.putString("type", storeField.type());
				fieldsArray.add(fieldHash);
			}

			config.put("fields", fieldsArray);
		}

		// put sort params to config (считаем, что используется обычный listServlet, поэтому нужно передавать параметры сортировки)
		JsHash paramsHash = new JsHash();
		paramsHash.putString("sort", comboBoxField.sortBy());
		paramsHash.putString("dir", comboBoxField.sortDir());
		config.put("params", paramsHash);

//		var comboField = Kefir.form.getComboBox(
//			{
//				disabled: disabled, typeAhead: false, queryParam: 'cadastralNumber', displayField: 'cadastralNumber', params: { sort: 'cadastralNumber', dir: 'asc' },
//				fields: [ { name: 'id', type: 'int' }, { name: 'cadastralNumber', type: 'string' } ]
//			},
//			'/parcelsList', COMBO_FIELD_FIELD_ID, 'comboField', 'comboField', 'Селект поле', 400, 400, 25, false, true
//		);
		String varValue = concat(sb,
			GET_COMBO_BOX_FUNCTION_NAME, "(",
				config,
				", '", comboBoxField.url(), "'",
				", ", getFieldIdConstantName(field),
				", '", getFieldName(field), "'",
				", '", getComboBoxHiddenName(comboBoxField, field), "'",
				", '", comboBoxField.label(), "'",
				", ", comboBoxField.width(),
				", ", getComboBoxListWidth(comboBoxField, field),
				", ", comboBoxField.maxLength(),
				", ", comboBoxField.allowBlank(),
				", ", comboBoxField.forceSelection(),
			")"
		);

		writeVariable(getFieldName(field), varValue);
	}

	private void writeAddressFieldVariables(Field field, String disabledVarName) throws IOException {
		out.writeLn();
		writeComment( concat(sb, field.getName()," fields"), "\t\t");

		writeAddressFieldTextFieldVariable(field, disabledVarName); // text field
		writeAddressFieldUpdateButtonVariable(field, disabledVarName); // update button
	}
	private void writeAddressFieldTextFieldVariable(Field field, String disabledVarName) throws IOException {
		AddressField addressField = getAddressFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);
		config.put("readOnly", true);

		if ( !addressField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
			config.put("style", "{ textTransform: 'none' }");

		String varValue = concat(sb,
			GET_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getAddressFieldTextFieldIdConstantName(field),
				", '", getTextFieldName(addressField, field), "'",
				", '", addressField.textFieldLabel(), "'",
				", ", addressField.textFieldWidth(),
				", ", addressField.textFieldMaxLength(),
				", ", addressField.allowBlank(),
			")"
		);

//		var juridicalAddressTextField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, JURIDICAL_ADDRESS_TEXT_FIELD_ID, 'juridicalAddressFull', 'Юридический адрес', 400, 400, false)
		writeVariable(getAddressFieldTextFieldVarName(field), varValue);
	}
	private void writeAddressFieldUpdateButtonVariable(Field field, String disabledVarName) throws IOException {
		AddressField addressField = getAddressFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);
		config.put("style", addressField.updateButtonStyle());

//		var juridicalAddressUpdateButton = Kefir.form.getButton({ disabled: disabled, style: { marginLeft: 10 } }, JURIDICAL_ADDRESS_UPDATE_BUTTON_ID, JURIDICAL_ADDRESS_UPDATE_BUTTON_TEXT, function() {
		out.writeLn(
			"\t\tvar ", getAddressFieldUpdateButtonVarName(field), " = ", GET_BUTTON_FUNCTION_NAME, "(", config, ", ", getAddressFieldUpdateButtonIdConstantName(field), ", ", getAddressFieldUpdateButtonTextConstantName(field), ", function() {"
		);
		out.writeLn("\t\t\t", getAddressInitFunctionName(addressField, field), "({");

		if (addressField.building())
			out.writeLn("\t\t\t\tbuilding: true,");

		out.writeLn("\t\t\t\twindowTitle: ", getAddressFieldWindowTitleConstantName(field), ",");
		out.writeLn("\t\t\t\tsuccessHandler: ", getFillAddressFieldFieldsFunctionName(field), ",");
		out.writeLn("\t\t\t\taddress: ", getAddressFieldVarName(field), ",");
		out.writeLn("\t\t\t\tformFieldId: ", getAddressFieldUpdateButtonIdConstantName(field));

		out.writeLn("\t\t\t});"); // end of su.opencode.minstroy.address.Address.init({

		out.writeLn("\t\t});"); // end of Kefir.form.getButton
	}

	private void writeChooseFieldVariables(Field field, String disabledVarName) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);

		out.writeLn();
		writeComment( concat(sb, field.getName()," fields"), "\t\t");

		// hidden id field
		writeChooseFieldHiddenIdFieldVariable(field);

		// text fields
		for (ChooseFieldTextField textField : chooseField.fields())
			writeChooseFieldTextFieldVariable(field, textField, disabledVarName);

		// choose button
		writeChooseFieldChooseButtonVariable(field, disabledVarName);

		// show button
		writeChooseFieldShowButtonVariable(field);
	}
	private void writeChooseFieldHiddenIdFieldVariable(Field field) throws IOException {
//		Kefir.form.getHiddenField({}, CHOOSE_FIELD_ID_FIELD_ID, 'chooseField');
 		String varValue = concat(sb,
			GET_HIDDEN_FIELD_FUNCTION_NAME, "({}, ", getChooseFieldIdIdConstantName(field), ", '", getFieldName(field), "')"
		);
		writeVariable(getChooseFieldHiddenIdVarName(field), varValue);
	}
	private void writeChooseFieldTextFieldVariable(Field field, ChooseFieldTextField textField, String disabledVarName) throws IOException {
		JsHash config = new JsHash();
		config.put("disabled", disabledVarName);
		config.put("readOnly", "true");

		if ( !textField.vtype().isEmpty() )
			config.putString("vtype", textField.vtype());

		if ( !textField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
			config.put("style", "{ textTransform: 'none' }");

//		var strField = Kefir.form.getTextField({ disabled: disabled }, STR_FIELD_FIELD_ID, 'strField', 'Строковое поле', 200, 200, false);
		String varValue = concat(sb,
			GET_TEXT_FIELD_FUNCTION_NAME, "(",
				config,
				", ", getChooseFieldTextFieldIdConstantName(field, textField),
				", '", getChooseFieldFieldName(field, textField), "'",
				", '", textField.label(), "'",
				", ", textField.width(),
				", ", textField.maxLength(),
				", ", textField.allowBlank(),
			")"
		);

		writeVariable(getChooseFieldTextFieldVarName(field, textField), varValue);
	}
	private void writeChooseFieldChooseButtonVariable(Field field, String disabledVarName) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);

		out.writeLn("\t\tvar ", getChooseFieldChooseButtonVarName(field), " = new ", EXT_BUTTON_CLASS_NAME, "({");
		out.writeLn("\t\t\tdisabled: ", disabledVarName, ",");
		out.writeLn("\t\t\tid: ", getChooseButtonIdConstantName(field), ",");
		out.writeLn("\t\t\ttext: ", getChooseButtonTextConstantName(field), ",");
		out.writeLn("\t\t\tstyle: ", chooseField.chooseButtonStyle(), ",");

		out.writeLn("\t\t\thandler: function() {");
		out.writeLn("\t\t\t\t", getChooseFieldChooseFunctionName(field),"({");
		writeChooseFunctionInitParams(field); // передать дополнительные параметры
		writeChooseFunctionFilterParams(field); // передать параметры фильтрации, кроме себя
		out.writeLn("\t\t\t\t\t", getChooseFieldChooseFunctionSuccessHandlerParamName(field), ": ", getFillChooseFieldFieldsFunctionName(field));
		out.writeLn("\t\t\t\t});");
		out.writeLn("\t\t\t}"); // end of handler

		out.writeLn("\t\t});"); // end of new Ext.Button
	}
	private void writeChooseFunctionInitParams(Field field) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		for (ChooseFieldInitParam param : chooseField.initParams())
			out.writeLn("\t\t\t\t\t", param.name(), ": ", param.value(), ",");
	}
	private void writeChooseFunctionFilterParams(Field field) throws IOException {
		for (Field filterField : getFilterConfigFormFilterFields(entityClass))
		{
			if ( !filterField.getName().equals(field.getName()) ) // самого себя в параметр выбора не передавать
			{
				out.writeLn("\t\t\t\t\t", getListFilterParamName(filterField, filterField.getType(), field.getType()), ": ", getFilterVarName(filterField), ",");
			}
		}
	}

	private void writeChooseFieldShowButtonVariable(Field field) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);

		out.writeLn("\t\tvar ", getChooseFieldShowButtonVarName(field), " = new ", EXT_BUTTON_CLASS_NAME, "({");
		out.writeLn("\t\t\tdisabled: true,"); // по умолчанию кнопка просмотра недоступна
		out.writeLn("\t\t\tid: ", getShowButtonIdConstantName(field), ",");
		out.writeLn("\t\t\ttext: ", getShowButtonTextConstantName(field), ",");
		out.writeLn("\t\t\tstyle: ", chooseField.showButtonStyle(), ",");

		out.writeLn("\t\t\thandler: function() {");
		out.writeLn("\t\t\t\t", getChooseFieldShowFunctionName(field),"({");
		out.writeLn("\t\t\t\t\t", getChooseFieldShowFunctionIdParamName(field), ": ", PANEL_VAR_NAME, ".getForm().findField('", getFieldName(field), "').getValue()"); // get id from chosenField's hidden id value
		out.writeLn("\t\t\t\t});");
		out.writeLn("\t\t\t}"); // end of handler

		out.writeLn("\t\t});"); // end of new Ext.Button
	}

	private void writeAttachmentsFieldVariable(Field field, String disabledVarName) throws IOException {
		JsHash config = new JsHash();
		config.put("readOnly", disabledVarName);
		config.put("attachments", getAttachmentsFieldAttachmentsVarName(field));

		String varValue = ( concat(sb,
			GET_MULTI_UPLOAD_PANEL_FUNCTION_NAME, "(",
				config,
				", ", getAttachmentsFieldPanelIdConstantName(field),
				", ", ENTITY_NAME_CONSTANT_NAME,
				", ", getAttachmentsFieldEntityFieldNameConstantName(field),
				", ", GET_ID_FUNCTION_NAME, "(", getFormEntityVarName(extEntity, entityClass), ")", // entityId
				", ", getAttachmentsFieldPanelWidthConstantName(field),
				", ", getAttachmentsFieldPanelHeightConstantName(field),
			")"
		) );

		writeVariable(getAttachmentsFieldPanelVarName(field), varValue);
	}

	// save button functions
	private void writeGetSaveUrl() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_URL_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_URL_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_URL_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_SAVE_URL_FUNCTION_NAME, constants);
	}
	private void writeGetSaveWaitMsg() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_SAVE_WAIT_MSG_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_SAVE_WAIT_MSG_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_SAVE_WAIT_MSG_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_SAVE_WAIT_MSG_FUNCTION_NAME, constants);
	}
	private void writeGetSaveErrorMsgTitle() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_SAVE_ERROR_MSG_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_SAVE_ERROR_MSG_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_SAVE_ERROR_MSG_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_SAVE_ERROR_MSG_TITLE_FUNCTION_NAME, constants);
	}
	private void writeGetSaveButtonText() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_SAVE_BUTTON_TEXT_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_SAVE_BUTTON_TEXT_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_SAVE_BUTTON_TEXT_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_SAVE_BUTTON_TEXT_FUNCTION_NAME, constants);
	}
	private void writeGetSaveButton() throws IOException {
		writeFunctionHeader(GET_SAVE_BUTTON_FUNCTION_NAME);
		out.writeLn("\t\treturn {");
		out.writeLn("\t\t\tid: ", SAVE_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", GET_SAVE_BUTTON_TEXT_FUNCTION_NAME, "(),");

		out.writeLn("\t\t\thandler: function() {");

		// todo: весь сабмит делать через общую функцию, написанную по аналогии с Kefir.formSubmit
		out.writeLn("\t\t\t\t", CHECK_FORM_FUNCTION_NAME, "(", PANEL_VAR_NAME, ", function() {");

		JsHash params = new JsHash();
		for (Field field : getAddressFields(entityClass))
		{ // physicalAddress: Ext.encode(physicalAddress)
			String paramName = field.getName();
			String paramValue = concat(sb, ENCODE_FUNCTION_NAME, "(", getAddressFieldVarName(field), ")");
			params.put(paramName, paramValue);
		}

		String paramsVarName = "params";
		writeVariable(paramsVarName, params.toString(), "\t\t\t\t\t");

		if (hasCheckboxFields(entityClass))
		{ // добавить заполнение false чекбоксов параметрами
			JsArray checkboxFieldsNames = getCheckboxFieldsNames();
			out.writeLn("\t\t\t\t\t", EXT_APPLY_FUNCTION_NAME, "( ", paramsVarName, ", ", GET_NEGATIVE_CHECKBOX_PARAMS_FUNCTION_NAME, "(", PANEL_VAR_NAME, ".getForm(), ", checkboxFieldsNames, ") );");
		}

		out.writeLn("\t\t\t\t\t", PANEL_VAR_NAME, ".getForm().submit({");
		out.writeLn("\t\t\t\t\t\turl: ", CONTEXT_PATH_CONSTANT_NAME, " + ", GET_SAVE_URL_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\t\t\t\tparams: ", paramsVarName, ",");
		out.writeLn("\t\t\t\t\t\twaitMsg: ", GET_SAVE_WAIT_MSG_FUNCTION_NAME, "(),");
		out.writeLn();

		String successFormParamName = "form";
		String successActionParamName = "action";
		out.writeLn("\t\t\t\t\t\tsuccess: function(", successFormParamName, ", ", successActionParamName, ") {");
		out.writeLn("\t\t\t\t\t\t\t", WINDOW_VAR_NAME, ".close();");
		out.writeLn();
		out.writeLn("\t\t\t\t\t\t\tif (", SUCCESS_HANDLER_VAR_NAME, ")");
		out.writeLn("\t\t\t\t\t\t\t\t", SUCCESS_HANDLER_VAR_NAME, "(", successActionParamName, ".response.responseText);");
		out.writeLn("\t\t\t\t\t\t},"); // end of success

		String failureFormParamName = "form";
		String failureActionParamName = "action";
		out.writeLn("\t\t\t\t\t\tfailure: function(", failureFormParamName, ", ", failureActionParamName, ") {");
		// todo: использовать GET_SAVE_ERROR_MSG_TITLE_FUNCTION_NAME() и православную функцию, которая будет принимать заголовок окна с ошибкой
		out.writeLn("\t\t\t\t\t\t\t", SHOW_FORM_RESPONSE_MESSAGE_CONSTANT_NAME, "(", GET_SAVE_URL_FUNCTION_NAME, "(), ", failureActionParamName, ", ", failureFormParamName, ", ", GET_SAVE_ERROR_MSG_TITLE_FUNCTION_NAME, "());");
		out.writeLn("\t\t\t\t\t\t}"); // end of failure

		out.writeLn("\t\t\t\t\t});"); // end of panel.getForm().submit

		out.writeLn("\t\t\t\t});"); // end of Kefir.checkForm

		out.writeLn("\t\t\t}"); // end of handler

		out.writeLn("\t\t};"); // end of return

		out.writeLn("\t}"); // end of function getSaveButton()
		out.writeLn();
	}

	private void writeSaveButtonFunctionsForEntityWithAttachments() throws IOException {
		writeSaveButtonHandler();
		writeAttachmentsPanelsLoadCallback();
		writeGetSaveButtonForEntityWithAttachments();
		out.writeLn();
	}
	private void writeSaveButtonHandler() throws IOException {
		writeFunctionHeader(SAVE_BUTTON_HANDLER_FUNCTION_NAME);

		JsHash params = new JsHash();
		for (Field field : getAttachmentsFields(entityClass))
		{ // передать id сохраненных Attachment в createServlet
			AttachmentsField attachmentsField = getAttachmentsFieldAnnotation(field);
			String paramName = getAttachmentsFieldIdsParamName(attachmentsField, field);
			String paramValue = concat(sb, EXT_GET_CMP_FUNCTION_NAME, "(", getAttachmentsFieldPanelIdConstantName(field), ").getAttachmentsIds()");
			params.put(paramName, paramValue);
		}

		for (Field field : getAddressFields(entityClass))
		{ // physicalAddress: Ext.encode(physicalAddress)
			String paramName = field.getName();
			String paramValue = concat(sb, ENCODE_FUNCTION_NAME, "(", getAddressFieldVarName(field), ")");
			params.put(paramName, paramValue);
		}

		String paramsVarName = "params";
		writeVariable(paramsVarName, params.toString());

		if (hasCheckboxFields(entityClass))
		{ // добавить заполнение false чекбоксов параметрами
			JsArray checkboxFieldsNames = getCheckboxFieldsNames();
			out.writeLn("\t\t", EXT_APPLY_FUNCTION_NAME, "( ", paramsVarName, ", ", GET_NEGATIVE_CHECKBOX_PARAMS_FUNCTION_NAME, "(", PANEL_VAR_NAME, ".getForm(), ", checkboxFieldsNames, ") );");
		}

		out.writeLn();
		out.writeLn("\t\t", PANEL_VAR_NAME, ".getForm().submit({");
		out.writeLn("\t\t\turl: ", CONTEXT_PATH_CONSTANT_NAME, " + ", GET_SAVE_URL_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\tparams: ", paramsVarName, ",");
		out.writeLn("\t\t\twaitMsg: ", GET_SAVE_WAIT_MSG_FUNCTION_NAME, "(),");
		out.writeLn();

		String successFormParamName = "form";
		String successActionParamName = "action";
		out.writeLn("\t\t\tsuccess: function(", successFormParamName, ", ", successActionParamName, ") {");
		out.writeLn("\t\t\t\t", WINDOW_VAR_NAME, ".close();");
		out.writeLn();
		out.writeLn("\t\t\t\tif (", SUCCESS_HANDLER_VAR_NAME, ")");
		out.writeLn("\t\t\t\t\t", SUCCESS_HANDLER_VAR_NAME, "(", successActionParamName, ".response.responseText);");
		out.writeLn("\t\t\t},"); // end of success

		String failureFormParamName = "form";
		String failureActionParamName = "action";
		out.writeLn("\t\t\tfailure: function(", failureFormParamName, ", ", failureActionParamName, ") {");
		out.writeLn(
			"\t\t\t\t", SHOW_FORM_RESPONSE_MESSAGE_CONSTANT_NAME, "(", GET_SAVE_URL_FUNCTION_NAME, "(), ", failureActionParamName, ", ", failureFormParamName, ", ", GET_SAVE_ERROR_MSG_TITLE_FUNCTION_NAME, "());"
		);
		out.writeLn("\t\t\t}"); // end of failure

		out.writeLn("\t\t});"); // end of panel.getForm().submit

		out.writeLn("\t}"); // end of function saveButtonHandler() {
	}
	private JsArray getCheckboxFieldsNames() {
		JsArray checkboxFieldsNames = new JsArray();
		for (Field field : getCheckboxFields(entityClass))
			checkboxFieldsNames.addString( getFieldName(field) );

		return checkboxFieldsNames;
	}

	private void writeAttachmentsPanelsLoadCallback() throws IOException {
		writeFunctionHeader(ATTACHMENTS_PANELS_LOAD_CALLBACK_FUNCTION_NAME);

		out.writeLn("\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, "++;");
		out.writeLn();

		out.writeLn("\t\tif (", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " == ", ATTACHMENT_FIELDS_COUNT_CONSTANT_NAME, ")");
		out.writeLn("\t\t\t", SAVE_BUTTON_HANDLER_FUNCTION_NAME, "();");

		out.writeLn("\t}"); // end of function attachmentsPanelsLoadCallback() {
	}
	private void writeGetSaveButtonForEntityWithAttachments() throws IOException {
		writeFunctionHeader(GET_SAVE_BUTTON_FUNCTION_NAME);

		out.writeLn("\t\treturn {");
		out.writeLn("\t\t\tid: ", SAVE_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", GET_SAVE_BUTTON_TEXT_FUNCTION_NAME, "(),");

		out.writeLn("\t\t\thandler: function() {");

		out.writeLn("\t\t\t\t", CHECK_FORM_FUNCTION_NAME, "(", PANEL_VAR_NAME, ", function() {");
		out.writeLn("\t\t\t\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " = 0;");
		out.writeLn();

		for (Field field : getAttachmentsFields(entityClass))
			out.writeLn("\t\t\t\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getAttachmentsFieldPanelIdConstantName(field), ").loadAttachments(", ATTACHMENTS_PANELS_LOAD_CALLBACK_FUNCTION_NAME, ");");

		out.writeLn("\t\t\t\t});"); // end of Kefir.checkForm

		out.writeLn("\t\t\t}"); // end of handler

		out.writeLn("\t\t};"); // end of return

		out.writeLn("\t}"); // end of function getSaveButton()
	}

	// cancel button functions
	private void writeGetCancelButtonId() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_CANCEL_BUTTON_ID_CONSTANT_NAME },
			{ ACTION_SHOW_CONSTANT_NAME, SHOW_CANCEL_BUTTON_ID_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_CANCEL_BUTTON_ID_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_CANCEL_BUTTON_ID_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_CANCEL_BUTTON_ID_FUNCTION_NAME, constants);
	}
	private void writeGetCancelButtonText() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME },
			{ ACTION_SHOW_CONSTANT_NAME, SHOW_CANCEL_BUTTON_TEXT_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_CANCEL_BUTTON_TEXT_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_CANCEL_BUTTON_TEXT_FUNCTION_NAME, constants);
	}
	private void writeGetCancelButton() throws IOException {
		writeFunctionHeader(GET_CANCEL_BUTTON_FUNCTION_NAME);

		out.writeLn("\t\treturn {");
		out.writeLn("\t\t\tid: ", GET_CANCEL_BUTTON_ID_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\ttext: ", GET_CANCEL_BUTTON_TEXT_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\thandler: function() { ", WINDOW_VAR_NAME, ".close(); }");

		out.writeLn("\t\t};"); // end of return

		out.writeLn("\t}"); // end of function getCancelButton()
		out.writeLn();
	}

	private void writeCancelButtonFunctionsForEntityWithAttachments() throws IOException {
		writeCancelButtonHandler();
		writeAttachmentsPanelsCancelCallback();
		writeGetCancelButtonForEntityWithAttachments();
		out.writeLn();
	}
	private void writeCancelButtonHandler() throws IOException {
		writeFunctionHeader(CANCEL_BUTTON_HANDLER_FUNCTION_NAME);
		out.writeLn("\t\t", WINDOW_VAR_NAME, ".close();");
		out.writeLn("\t}"); // end of function cancelButtonHandler() {
	}
	private void writeAttachmentsPanelsCancelCallback() throws IOException {
		writeFunctionHeader(ATTACHMENTS_PANELS_CANCEL_CALLBACK_FUNCTION_NAME);

		out.writeLn("\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, "++;");
		out.writeLn();

		out.writeLn("\t\tif (", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " == ", ATTACHMENT_FIELDS_COUNT_CONSTANT_NAME, ")");
		out.writeLn("\t\t\t", CANCEL_BUTTON_HANDLER_FUNCTION_NAME, "();");

		out.writeLn("\t}"); // end of function attachmentsPanelsLoadCallback() {
	}
	private void writeGetCancelButtonForEntityWithAttachments() throws IOException {
		writeFunctionHeader(GET_CANCEL_BUTTON_FUNCTION_NAME);

		out.writeLn("\t\treturn {");
		out.writeLn("\t\t\tid: ", GET_CANCEL_BUTTON_ID_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\ttext: ", GET_CANCEL_BUTTON_TEXT_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\thandler: function() {");

		out.writeLn("\t\t\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " = 0;");
		out.writeLn();

		out.writeLn("\t\t\t\tif (", ACTION_VAR_NAME, " == ", ACTION_CONSTANT_NAME, ".", ACTION_CREATE_CONSTANT_NAME, ")");
		out.writeLn("\t\t\t\t{ // remove saved attachments");

		for (Field field : getAttachmentsFields(entityClass))
			out.writeLn("\t\t\t\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getAttachmentsFieldPanelIdConstantName(field), ").removeLoadedAttachments(", ATTACHMENTS_PANELS_CANCEL_CALLBACK_FUNCTION_NAME, ");");

		out.writeLn("\t\t\t\t}");
		out.writeLn("\t\t\t\telse");
		out.writeLn("\t\t\t\t{");
		out.writeLn("\t\t\t\t\t", CANCEL_BUTTON_HANDLER_FUNCTION_NAME, "();");
		out.writeLn("\t\t\t\t}");

		out.writeLn("\t\t\t}"); // end of handler: function() {
		out.writeLn("\t\t};"); // end of return

		out.writeLn("\t}"); // end of function getCancelButton()
	}

	private void writeGetButtons() throws IOException {
		writeFunctionHeader(GET_BUTTONS_FUNCTION_NAME);

		String cancelButtonVarName = "cancelButton";
		String saveButtonVarName = "saveButton";

		writeVariable(cancelButtonVarName, concat(GET_CANCEL_BUTTON_FUNCTION_NAME, "()"));
		out.writeLn("\t\tif (", ACTION_VAR_NAME, " == ", ACTION_CONSTANT_NAME, ".", ACTION_SHOW_CONSTANT_NAME, ")");
		writeFunctionReturn( new JsArray(cancelButtonVarName), "\t\t\t" );
		out.writeLn();

		writeVariable(saveButtonVarName, concat(GET_SAVE_BUTTON_FUNCTION_NAME, "()"));
		writeFunctionReturn( new JsArray(saveButtonVarName, cancelButtonVarName) );

		out.writeLn("\t}"); // end of function getButtons()
		out.writeLn();
	}

	// common form functions
	private void writeInitPanel() throws IOException {
		writeFunctionHeader(INIT_PANEL_FUNCTION_NAME);

		out.writeLn("\t\t", PANEL_VAR_NAME, " = new ", FORM_PANEL_CLASS_NAME, "({");
		out.writeLn("\t\t\tfieldsIds: ", FIELDS_IDS_CONSTANT_NAME, ", ");
		out.writeLn("\t\t\tautoHeight: true,"); // todo: think about moving autoHeight: true to Kefir.FormPanel
		out.writeLn("\t\t\tlabelWidth: ", LABEL_WIDTH_CONSTANT_NAME, ", ");
		out.writeLn();
		out.writeLn("\t\t\titems: ", GET_ITEMS_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\tbuttons: ", GET_BUTTONS_FUNCTION_NAME, "()");
		out.writeLn("\t\t});"); // end panel = new new Kefir.FormPanel({

		out.writeLn("\t}"); // end of function initPanel()
		out.writeLn();
	}
	private void writeGetDefaultButton() throws IOException {
		String[][] constants = {
			{ ACTION_SHOW_CONSTANT_NAME, SHOW_CANCEL_BUTTON_ID_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, SAVE_BUTTON_ID_CONSTANT_NAME },
		};

		String defaultAction;
		String firstElementId = collectFieldsIds().get(0);
		if (isButtonId(firstElementId))
			defaultAction = concat(sb, "return ", firstElementId); // первый фокусируемый элемент - кнопка -> фокусировать его
		else
			defaultAction = "return undefined";

		writeSwitchReturnFunction(GET_DEFAULT_BUTTON_FUNCTION_NAME, ACTION_VAR_NAME, constants, defaultAction);
	}
	private boolean isButtonId(String id) {
		return id.indexOf("BUTTON") >= 0;
	}

	private void writeGetWindowTitle() throws IOException {
		String[][] constants = {
			{ ACTION_CREATE_CONSTANT_NAME, CREATE_WINDOW_TITLE_CONSTANT_NAME },
			{ ACTION_SHOW_CONSTANT_NAME, SHOW_WINDOW_TITLE_CONSTANT_NAME },
			{ ACTION_UPDATE_CONSTANT_NAME, UPDATE_WINDOW_TITLE_CONSTANT_NAME },
			{ ACTION_DELETE_CONSTANT_NAME, DELETE_WINDOW_TITLE_CONSTANT_NAME },
		};
		writeSwitchReturnFunction(GET_WINDOW_TITLE_FUNCTION_NAME, constants);
	}
	private void writeInitWindow() throws IOException {
		writeFunctionHeader(INIT_WINDOW_FUNCTION_NAME);

		out.writeLn("\t\t", INIT_PANEL_FUNCTION_NAME, "();");
		out.writeLn();

		out.writeLn("\t\t", WINDOW_VAR_NAME, " = new ", EXT_WINDOW_CLASS_NAME, "({");
		out.writeLn("\t\t\tid: ", WINDOW_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttitle: ", GET_WINDOW_TITLE_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\twidth: ", WINDOW_WIDTH_CONSTANT_NAME, ",");
		out.writeLn("\t\t\tautoHeight: true,");
		out.writeLn("\t\t\tresizable: false,");
		out.writeLn("\t\t\tconstrain: true,");
		out.writeLn("\t\t\tmodal: true,");
		out.writeLn("\t\t\tlayout: 'fit',");
		out.writeLn();
		out.writeLn("\t\t\tdefaultButton: ", GET_DEFAULT_BUTTON_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\titems: [ ", PANEL_VAR_NAME, " ]");
		out.writeLn("\t\t});");
		out.writeLn();
		// todo: window close and show handlers
		out.writeLn("\t\t", WINDOW_VAR_NAME, ".on('show', ", FILL_FORM_FUNCTION_VAR_NAME, ");");

		// заполнить жестко подставляемые поля выбора
		for (Field field : getFilterConfigFormFilterChooseFields(entityClass))
		{
			out.writeLn("\t\t", WINDOW_VAR_NAME, ".on('show', function() { ", getFillFilterChooseFieldFunctionName(field), "(", getFilterVarName(field), "); });"); // window.on('show', function() { fillParcel(parcel); } );
		}

		out.writeLn("\t\t", WINDOW_VAR_NAME, ".on('show', function() { ", PANEL_VAR_NAME, ".focusFirst(); });"); // todo: think about moving focusFirst and on show to superclass of window

		if (extEntity.formBindMask())
			out.writeLn("\t\t", BIND_MASK_FUNCTION_NAME, "(", WINDOW_VAR_NAME, ");");

		out.writeLn("\t}"); // end function initWindow()
	}
	private void writeShowWindow() throws IOException {
		writeFunctionHeader(SHOW_WINDOW_FUNCTION_NAME);
		out.writeLn("\t\t", INIT_WINDOW_FUNCTION_NAME, "();");
		out.writeLn("\t\t", WINDOW_VAR_NAME, ".show();");
		out.writeLn("\t}"); // end function showWindow()
		out.writeLn();
	}

	private void writeInitParams() throws IOException {
		String actionParamName = "_action";
		String fillFormFunctionParamName = "_fillFormFunction";
		String configParamName = "config";

		writeFunctionHeader(INIT_PARAMS_FUNCTION_NAME, actionParamName, fillFormFunctionParamName, configParamName);

		out.writeLn("\t\t", ACTION_VAR_NAME, " = ", actionParamName, ";");
		out.writeLn("\t\t", FILL_FORM_FUNCTION_VAR_NAME, " = ", fillFormFunctionParamName, ";");

		String entityVarName = getFormEntityVarName(extEntity, entityClass);
		out.writeLn("\t\t", entityVarName, " = ", configParamName, ".", entityVarName, ";"); // testEntity = config.testEntity
		out.writeLn("\t\t", SUCCESS_HANDLER_VAR_NAME, " = ", configParamName, ".", SUCCESS_HANDLER_VAR_NAME, ";"); // successHandler = config.successHandler

		if (hasFilterConfigFormFilterFields(entityClass))
			writeInitFilterConfigParams(configParamName);

		writeFunctionFooter(); // end function initParams()
	}
	private void writeInitFilterConfigParams(String configParamName) throws IOException {
		// parcel = config.parcel; etc
		for (Field field : getFilterConfigFormFilterFields(entityClass))
		{
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			out.writeLn("\t\t", getFilterVarName(field), " = ", configParamName, ".", getFilterConfigFormInitFunctionsParamName(filterConfigField, field), ";");
		}
	}

	private void writeInit() throws IOException {
		String actionParamName = "_action";
		String fillFormFunctionParamName = "_fillFormFunction";
		String configParamName = "config";

		writeFunctionHeader(INIT_FUNCTION_NAME, actionParamName, fillFormFunctionParamName, configParamName);
		out.writeLn("\t\t", INIT_PARAMS_FUNCTION_NAME, "(", actionParamName, ", ", fillFormFunctionParamName, ", ", configParamName, ");");
		out.writeLn();

		out.writeLn("\t\tif (", configParamName, ".", extEntity.formConfigEntityIdParamName(), ")"); // if (config.id)
		out.writeLn("\t\t{");

		String entityVarName = getFormEntityVarName(extEntity, entityClass);
		String getFunctionCallbackParamName = concat(sb, "_", entityVarName);

		JsHash getParams = new JsHash();
		getParams.put("id", concat(sb, configParamName,".", extEntity.formConfigEntityIdParamName()));
		if (extEntity.useVoForGetById())
			getParams.put(JsonServlet.ENTITY_NAME_PARAM_NAME, VO_CLASS_CONSTANT_NAME);

		out.writeLn("\t\t\t", AJAX_REQUEST_FUNCTION_NAME, "(", GET_URL_CONSTANT_NAME,", ", getParams, ", function(", getFunctionCallbackParamName, ") {");
		out.writeLn("\t\t\t\t", entityVarName, " = ", getFunctionCallbackParamName, ";");
		out.writeLn("\t\t\t\t", SHOW_WINDOW_FUNCTION_NAME, "();");
		out.writeLn("\t\t\t});"); // end Kefir.ajaxRequest

		out.writeLn("\t\t}"); // end if
		out.writeLn("\t\telse");
		out.writeLn("\t\t{");
		out.writeLn("\t\t\t", SHOW_WINDOW_FUNCTION_NAME, "();");
		out.writeLn("\t\t}"); // end else

		out.writeLn("\t}"); // end function init()
		out.writeLn();
	}

	private void writeLoadAttachmentsCallback() throws IOException {
		String callbackParamName = "callback";

		writeFunctionHeader(LOAD_ATTACHMENTS_CALLBACK_FUNCTION_NAME, callbackParamName);

		out.writeLn("\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, "++;");
		out.writeLn();

		out.writeLn("\t\tif (", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " == ", ATTACHMENT_FIELDS_COUNT_CONSTANT_NAME, ")");
		out.writeLn("\t\t\t", callbackParamName, "();");

		out.writeLn("\t}"); // end of function loadAttachmentsCallback() {
	}
	private void writeLoadAttachments() throws IOException {
		String configParamName = "config";
		String callbackParamName = "callback";

		writeFunctionHeader(LOAD_ATTACHMENTS_FUNCTION_NAME, configParamName, callbackParamName);
		out.writeLn("\t\t", ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME, " = 0;");

		String idVarName = "id";
		String idVarValue = concat(sb, configParamName, ".", extEntity.formConfigEntityIdParamName(), " || ", GET_ID_FUNCTION_NAME, "(", configParamName, ".", getFormEntityVarName(extEntity, entityClass), ")");
		writeVariable(idVarName, idVarValue);
		out.writeLn();

		String jsonParamName = "json";
		for (Field field : getAttachmentsFields(entityClass))
		{ // для каждого аттачмент поля загрузить аттачи по entityName и entityFieldName

			// { entityId: id, entityName: ENTITY_NAME, entityFieldName: ATTACHMENTS_FIELD_ENTITY_FIELD_NAME }
			JsHash params = new JsHash();
			params.put("entityId", idVarName);
			params.put("entityName", ENTITY_NAME_CONSTANT_NAME);
			params.put("entityFieldName", getAttachmentsFieldEntityFieldNameConstantName(field));

			out.writeLn("\t\t", AJAX_REQUEST_FUNCTION_NAME, "(", ATTACHMENTS_LIST_URL_CONSTANT_NAME, ", ", params, ", function(", jsonParamName, ") {");
			out.writeLn("\t\t\t", getAttachmentsFieldAttachmentsVarName(field), " = ", jsonParamName, ".", JsonServlet.JSON_RESULTS_PROPERTY, ";");
			out.writeLn("\t\t\t", LOAD_ATTACHMENTS_CALLBACK_FUNCTION_NAME, "(", callbackParamName, ");");

			out.writeLn("\t\t});"); // end Kefir.ajaxRequest(
			out.writeLn();
		}

		out.writeLn("\t}"); // end of function loadAttachments() {
	}

	// return hash
	private void writeReturn() throws IOException {
		String configParamName = "config";

		out.writeLn("\treturn {");

		out.writeLn("\t\t", extEntity.formCreateFunctionName(), ": function(", configParamName, ") {");
		// очистить загруженные адресы
		for (Field field : getAddressFields(entityClass))
			out.writeLn("\t\t\t", getAddressFieldVarName(field), " = null;");

		out.writeLn("\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_CREATE_CONSTANT_NAME, ", ", EXT_EMPTY_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t},"); // end initCreateForm
 		out.writeLn();

		out.writeLn("\t\t", extEntity.formShowFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_SHOW_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t},"); // end initShowForm
 		out.writeLn();

		out.writeLn("\t\t", extEntity.formUpdateFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_UPDATE_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t},"); // end initUpdateForm
 		out.writeLn();

		out.writeLn("\t\t", extEntity.formDeleteFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_DELETE_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t}"); // end initDeleteForm

		out.writeLn("\t}"); // end return {
	}
	private void writeReturnForEntityWithAtachments() throws IOException {
		String configParamName = "config";

		out.writeLn("\treturn {");

		// initCreateForm
		out.writeLn("\t\t", extEntity.formCreateFunctionName(), ": function(", configParamName, ") {");

		// очистить загруженные адресы
		for (Field field : getAddressFields(entityClass))
			out.writeLn("\t\t\t", getAddressFieldVarName(field), " = null;");

		// очистить загруженные аттачи всех полей сущности во избежание начальной загрузки аттачей, сохраненных при RUD загрузке аттачей сущностей
		for (Field field : getAttachmentsFields(entityClass))
			out.writeLn("\t\t\t", getAttachmentsFieldAttachmentsVarName(field), " = null;");
		out.writeLn();

		out.writeLn("\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_CREATE_CONSTANT_NAME, ", ", EXT_EMPTY_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t},"); // end initCreateForm
 		out.writeLn();

		// initShowForm
		out.writeLn("\t\t", extEntity.formShowFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", LOAD_ATTACHMENTS_FUNCTION_NAME, "(", configParamName, ", function() {");
		out.writeLn("\t\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_SHOW_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t\t});"); // end loadAttachments(config, function() {
		out.writeLn("\t\t},"); // end initShowForm
 		out.writeLn();

		// initUpdateForm
		out.writeLn("\t\t", extEntity.formUpdateFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", LOAD_ATTACHMENTS_FUNCTION_NAME, "(", configParamName, ", function() {");
		out.writeLn("\t\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_UPDATE_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t\t});"); // end loadAttachments(config, function() {
		out.writeLn("\t\t},"); // end initUpdateForm
 		out.writeLn();

		// initDeleteForm
		out.writeLn("\t\t", extEntity.formDeleteFunctionName(), ": function(", configParamName, ") {");
		out.writeLn("\t\t\t", LOAD_ATTACHMENTS_FUNCTION_NAME, "(", configParamName, ", function() {");
		out.writeLn("\t\t\t\t", INIT_FUNCTION_NAME, "(", ACTION_CONSTANT_NAME, ".", ACTION_DELETE_CONSTANT_NAME, ", ", FILL_FORM_FIELDS_FUNCTION_NAME, ", ", configParamName, ");");
		out.writeLn("\t\t\t});"); // end loadAttachments(config, function() {
		out.writeLn("\t\t}"); // end initDeleteForm

		out.writeLn("\t}"); // end return {
	}

	// constant variable names
	public static final String ENTITY_NAME_CONSTANT_NAME = "ENTITY_NAME"; // используется как параметр аттачмента

	private static final String WINDOW_ID_CONSTANT_NAME = "WINDOW_ID";
	private static final String WINDOW_WIDTH_CONSTANT_NAME = "WINDOW_WIDTH";
	private static final String LABEL_WIDTH_CONSTANT_NAME = "LABEL_WIDTH";
	private static final String GET_URL_CONSTANT_NAME = "GET_URL";
	private static final String VO_CLASS_CONSTANT_NAME = "VO_CLASS";

	private static final String CREATE_URL_CONSTANT_NAME = "CREATE_URL";
	private static final String CREATE_WINDOW_TITLE_CONSTANT_NAME = "CREATE_WINDOW_TITLE";
	private static final String CREATE_SAVE_BUTTON_TEXT_CONSTANT_NAME = "CREATE_SAVE_BUTTON_TEXT";
	private static final String CREATE_SAVE_WAIT_MSG_CONSTANT_NAME = "CREATE_SAVE_WAIT_MSG";
	private static final String CREATE_SAVE_ERROR_MSG_CONSTANT_NAME = "CREATE_SAVE_ERROR_MSG";
	private static final String CREATE_CANCEL_BUTTON_ID_CONSTANT_NAME = "CREATE_CANCEL_BUTTON_ID";
	private static final String CREATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME = "CREATE_CANCEL_BUTTON_TEXT";

	private static final String SHOW_WINDOW_TITLE_CONSTANT_NAME = "SHOW_WINDOW_TITLE";
	private static final String SHOW_CANCEL_BUTTON_ID_CONSTANT_NAME = "SHOW_CANCEL_BUTTON_ID";
	private static final String SHOW_CANCEL_BUTTON_TEXT_CONSTANT_NAME = "SHOW_CANCEL_BUTTON_TEXT";

	private static final String UPDATE_URL_CONSTANT_NAME = "UPDATE_URL";
	private static final String UPDATE_WINDOW_TITLE_CONSTANT_NAME = "UPDATE_WINDOW_TITLE";
	private static final String UPDATE_SAVE_BUTTON_TEXT_CONSTANT_NAME = "UPDATE_SAVE_BUTTON_TEXT";
	private static final String UPDATE_SAVE_WAIT_MSG_CONSTANT_NAME = "UPDATE_SAVE_WAIT_MSG";
	private static final String UPDATE_SAVE_ERROR_MSG_CONSTANT_NAME = "UPDATE_SAVE_ERROR_MSG";
	private static final String UPDATE_CANCEL_BUTTON_ID_CONSTANT_NAME = "UPDATE_CANCEL_BUTTON_ID";
	private static final String UPDATE_CANCEL_BUTTON_TEXT_CONSTANT_NAME = "UPDATE_CANCEL_BUTTON_TEXT";

	private static final String DELETE_URL_CONSTANT_NAME = "DELETE_URL";
	private static final String DELETE_WINDOW_TITLE_CONSTANT_NAME = "DELETE_WINDOW_TITLE";
	private static final String DELETE_SAVE_BUTTON_TEXT_CONSTANT_NAME = "DELETE_SAVE_BUTTON_TEXT";
	private static final String DELETE_SAVE_WAIT_MSG_CONSTANT_NAME = "DELETE_SAVE_WAIT_MSG";
	private static final String DELETE_SAVE_ERROR_MSG_CONSTANT_NAME = "DELETE_SAVE_ERROR_MSG";
	private static final String DELETE_CANCEL_BUTTON_ID_CONSTANT_NAME = "DELETE_CANCEL_BUTTON_ID";
	private static final String DELETE_CANCEL_BUTTON_TEXT_CONSTANT_NAME = "DELETE_CANCEL_BUTTON_TEXT";

	// fields ids
	private static final String FIELD_ID_CONSTANT_POSTFIX = "_FIELD_ID";
	private static final String CHOOSE_FIELD_ID_CONSTANT_POSTFIX = "_ID_FIELD_ID";
	private static final String CHOOSE_FIELD_CHOOSE_BUTTON_ID_CONSTANT_POSTFIX = "_CHOOSE_BUTTON_ID";
	private static final String CHOOSE_FIELD_CHOOSE_BUTTON_TEXT_CONSTANT_POSTFIX = "_CHOOSE_BUTTON_TEXT";
	private static final String CHOOSE_FIELD_SHOW_BUTTON_ID_CONSTANT_POSTFIX = "_SHOW_BUTTON_ID";
	private static final String CHOOSE_FIELD_SHOW_BUTTON_TEXT_CONSTANT_POSTFIX = "_SHOW_BUTTON_TEXT";
	private static final String CHOOSE_FIELD_FIELDS_CONSTANT_POSTFIX = "_FIELDS";

	// address fields
	private static final String ADDRESS_FIELD_TEXT_FIELD_ID_CONSTANT_POSTFIX = "_TEXT_FIELD_ID";
	private static final String ADDRESS_FIELD_UPDATE_BUTTON_ID_CONSTANT_POSTFIX = "_UPDATE_BUTTON_ID";
	private static final String ADDRESS_FIELD_UPDATE_BUTTON_TEXT_CONSTANT_POSTFIX = "_UPDATE_BUTTON_TEXT";
	private static final String ADDRESS_FIELD_WINDOW_TITLE_CONSTANT_POSTFIX = "_WINDOW_TITLE";
	// attachments
	private static final String ATTACHMENT_FIELDS_COUNT_CONSTANT_NAME = "ATTACHMENT_FIELDS_COUNT";
	private static final String ATTACHMENT_FIELD_ENTITY_FIELD_NAME_CONSTANT_POSTFIX = "_ENTITY_FIELD_NAME";
	private static final String ATTACHMENT_FIELD_PANEL_ID_CONSTANT_POSTFIX = "_PANEL_ID";
	private static final String ATTACHMENT_FIELD_PANEL_WIDTH_CONSTANT_POSTFIX = "_PANEL_WIDTH";
	private static final String ATTACHMENT_FIELD_PANEL_HEIGHT_CONSTANT_POSTFIX = "_PANEL_HEIGHT";

	private static final String SAVE_BUTTON_ID_CONSTANT_NAME = "SAVE_BUTTON_ID";

	private static final String FIELDS_IDS_CONSTANT_NAME = "FIELDS_IDS";

	// variable names
	private static final String WINDOW_VAR_NAME = "window";
	private static final String PANEL_VAR_NAME = "panel";
	private static final String FILL_FORM_FUNCTION_VAR_NAME = "fillFormFunction";
	private static final String SUCCESS_HANDLER_VAR_NAME = "successHandler";

	// attachment variable names
	private static final String ATTACHMENT_FIELDS_LOAD_COUNT_VAR_NAME = "attachmentsFieldsLoadCount";
	private static final String ATTACHMENT_FIELD_ATTACHMENTS_VAR_POSTFIX = "Attachments";

	// function names
	private static final String FILL_FORM_FIELDS_FUNCTION_NAME = "fillFormFields";
	private static final String GET_ITEMS_FUNCTION_NAME = "getItems";

	private static final String GET_SAVE_URL_FUNCTION_NAME = "getSaveUrl";
	private static final String GET_SAVE_WAIT_MSG_FUNCTION_NAME = "getSaveWaitMsg";
	private static final String GET_SAVE_ERROR_MSG_TITLE_FUNCTION_NAME = "getSaveErrorMsgTitle";
	private static final String GET_SAVE_BUTTON_TEXT_FUNCTION_NAME = "getSaveButtonText";
	private static final String GET_SAVE_BUTTON_FUNCTION_NAME = "getSaveButton";

	// save button functions for entity with attachments
	private static final String SAVE_BUTTON_HANDLER_FUNCTION_NAME = "saveButtonHandler";
	private static final String ATTACHMENTS_PANELS_LOAD_CALLBACK_FUNCTION_NAME = "attachmentsPanelsLoadCallback";

	private static final String GET_CANCEL_BUTTON_ID_FUNCTION_NAME = "getCancelButtonId";
	private static final String GET_CANCEL_BUTTON_TEXT_FUNCTION_NAME = "getCancelButtonText";
	private static final String GET_CANCEL_BUTTON_FUNCTION_NAME = "getCancelButton";

	// cancel button functions for entity with attachments
	private static final String CANCEL_BUTTON_HANDLER_FUNCTION_NAME = "cancelButtonHandler";
	private static final String ATTACHMENTS_PANELS_CANCEL_CALLBACK_FUNCTION_NAME = "attachmentsPanelsCancelCallback";

	private static final String GET_BUTTONS_FUNCTION_NAME = "getButtons";
	private static final String INIT_PANEL_FUNCTION_NAME = "initPanel";
	private static final String GET_DEFAULT_BUTTON_FUNCTION_NAME = "getDefaultButton";
	private static final String GET_WINDOW_TITLE_FUNCTION_NAME = "getWindowTitle";
	private static final String INIT_WINDOW_FUNCTION_NAME = "initWindow";
	private static final String SHOW_WINDOW_FUNCTION_NAME = "showWindow";
	private static final String INIT_PARAMS_FUNCTION_NAME = "initParams";
	private static final String INIT_FUNCTION_NAME = "init";

	// load entity attachments functions
	private static final String LOAD_ATTACHMENTS_CALLBACK_FUNCTION_NAME = "loadAttachmentsCallback";
	private static final String LOAD_ATTACHMENTS_FUNCTION_NAME = "loadAttachments";
}