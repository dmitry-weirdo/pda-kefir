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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.notEmpty;

public class FormLayoutJsFileWriter extends FormJsFileWriter
{
	public FormLayoutJsFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, extEntity, entityClass);

		this.failIfFileExists = false; // если файл уже существует, это нормально, не перезаписывать его
		this.dir = getJsDirectory(extEntity, entityClass);
		this.fileName = getFormLayoutJsFileName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeFile() throws IOException {
		writeNamespace();

		writeGetFormItemsLayout();
	}
	private void writeGetFormItemsLayout() throws IOException {
		String paramName = "config";

		out.writeLn(getGetFormItemsLayoutFunctionFullName(extEntity, entityClass), " = function(", paramName, ") {");

		// создать переменные с колумнпанелями полей адреса
		writeAddressFieldsColumnPanels(paramName);

		// создать переменные с филдсетами полей выбора
		writeChooseFieldsFieldSets(paramName);

		// создать переменные с филдсетами upload panel
		writeAttachmentsFieldsFieldSets(paramName);

		if (hasFieldSetsAnnotation(entityClass))
		{
			checkFieldSets();
			writeFieldSets(paramName);
		}

		writeReturn(paramName);

		writeFunctionFooterWithSemicolon(); // end function getFormItemsLayout
	}
	private void writeReturn(String paramName) throws IOException {
		out.writeLn("\treturn [");

		// сначала написать id поле без запятой впереди
		out.writeLn("\t\t  ", paramName, ".", getIdHiddenFieldVarName());

		Set<String> fieldSetsIds = new HashSet<>();
		for (int i = 0; i < entityClass.getDeclaredFields().length; i++)
		{
			Field field = entityClass.getDeclaredFields()[i];
			if (!hasFieldAnnotation(field))
				continue;

			String fieldSetId = getFieldFieldSetId(field);
			if (notEmpty(fieldSetId))
			{
				if (!fieldSetsIds.contains(fieldSetId))
				{
					fieldSetsIds.add(fieldSetId);
					out.writeLn("\t\t, ", getFieldSetVarName(getFieldSet(entityClass, fieldSetId)));
				}
				continue;
			}

			if (hasAddressFieldAnnotation(field))
			{ // адресное поле -> написать columnPanel
				out.writeLn("\t\t, ", getAddressFieldColumnPanelVarName(field)); // column panel
				continue;
			}
			else if (hasChooseFieldAnnotation(field))
			{ // поле выбора -> написать скрытое поле и филдсет
				String hiddenIdVarName = getChooseFieldHiddenIdVarName(field);
				String fieldSetVarName = getChooseFieldFieldSetVarName(field);

				out.writeLn();
				out.writeLn("\t\t// fields of ", field.getName());
				out.writeLn("\t\t, ", paramName, ".", hiddenIdVarName); // скрытое поле id,
				out.writeLn("\t\t, ", fieldSetVarName); // fieldset
				out.writeLn();

				continue;
			}
			else if (hasAttachmentsFieldAnnotation(field))
			{
				out.writeLn("\t\t, ", getAttachmentsFieldFieldSetVarName(field)); // fieldset
				continue;
			}
			else if (hasIdFieldAnnotation(field))
			{ // id - поле -> ничего не делать, оно уже написано в начале
				continue;
			}

			// обычное поле -> просто написать в строчку
			out.writeLn("\t\t, ", paramName, ".", field.getName());
		}

		out.writeLn("\t];"); // end of "return ["
	}
	private void checkFieldSets() {
		Set<String> ids = new HashSet<>();
		for (FieldSet fieldSet : getFieldSetsAnnotation(entityClass).fieldSets())
		{
			if (ids.contains(fieldSet.id()))
				throw new IllegalStateException(concat(sb, FieldSet.class.getName(), " with id \"", fieldSet.id(), "\" is present more than once in class ", entityClass.getName()));

			ids.add(fieldSet.id());
		}
	}
	private void writeFieldSets(String configParamName) throws IOException {
		for (FieldSet fieldSet : getFieldSetsAnnotation(entityClass).fieldSets())
		{
			String fieldSetVarName = getFieldSetVarName(fieldSet);

			out.writeLn("\tvar ", fieldSetVarName, " = {");
			out.writeLn("\t\txtype: 'fieldset',");
			out.writeLn("\t\ttitle: '", fieldSet.title(), "',");

			if ( !fieldSet.layout().isEmpty() )
				out.writeLn("\t\tlayout: '", fieldSet.layout(), "',");

			if ( !fieldSet.layoutConfig().isEmpty() )
				out.writeLn("\t\tlayoutConfig: ", fieldSet.layoutConfig(), ",");

			JsArray items = new JsArray();
			for (Field field : entityClass.getDeclaredFields())
			{
				if (!fieldSet.id().equals(getFieldFieldSetId(field)))
					continue;

				if (hasAddressFieldAnnotation(field))
				{
					items.add(getAddressFieldColumnPanelVarName(field));
					continue;
				}

				if (hasChooseFieldAnnotation(field))
				{
					items.add(getChooseFieldFieldSetVarName(field));
					continue;
				}

				if (hasAttachmentsFieldAnnotation(field))
				{
					items.add(getAttachmentsFieldFieldSetVarName(field));
					continue;
				}

				items.add(concat(sb, configParamName, ".", field.getName()));
			}

			if (items.isEmpty())
				throw new IllegalStateException(concat(sb, "No fields for fieldset with id \"", fieldSet.id(), "\" in class ", entityClass.getName()));

			out.writeLn("\t\titems: ",items);
			out.writeLn("\t};");
			out.writeLn();
		}
	}

	private void writeAddressFieldsColumnPanels(String configParamName) throws IOException {
		for (Field field : getAddressFields(entityClass))
			writeAddressFieldColumnPanel(field, configParamName);
	}
	private void writeAddressFieldColumnPanel(Field field, String configParamName) throws IOException {
		AddressField addressField = getAddressFieldAnnotation(field);
		String varName = getAddressFieldColumnPanelVarName(field);

		JsHash columnPanelConfig = new JsHash();
		columnPanelConfig.put("style", addressField.columnPanelStyle());

		JsHash textFieldFormPanelConfig = new JsHash();
		textFieldFormPanelConfig.put("width", addressField.textFieldFormPanelWidth());

//		var juridicalAddressColumnPanel = Kefir.form.getColumnPanel({ style: { padding: 5 }}, [
		out.writeLn("\tvar ", varName, " = ", GET_COLUMN_PANEL_FUNCTION_NAME, "(", columnPanelConfig, ", [");

		out.writeLn( // Kefir.form.getFormPanel({ width: 610 }, 150, [ config.juridicalAddressTextField ]),
			"\t\t", GET_FORM_PANEL_FUNCTION_NAME, "(", textFieldFormPanelConfig, ", ", addressField.textFieldFormPanelLabelWidth(), ", [ ", configParamName, ".", getAddressFieldTextFieldVarName(field), " ]),"
		);

		out.writeLn("\t\t", configParamName, ".", getAddressFieldUpdateButtonVarName(field)); // config.juridicalAddressUpdateButton
		out.writeLn("\t]);"); // end Kefir.form.getColumnPanel({}, [
		out.writeLn();
	}
	private String getAddressFieldColumnPanelVarName(Field field) {
		return concat(sb, field.getName(), "ColumnPanel");
	}

	private void writeChooseFieldsFieldSets(String configParamName) throws IOException {
		for (Field field : entityClass.getDeclaredFields())
			if ( hasChooseFieldAnnotation(field) )
				writeChooseFieldFieldSet(field, configParamName);
	}
	private void writeChooseFieldFieldSet(Field field, String configParamName) throws IOException {
		ChooseField chooseField = getChooseFieldAnnotation(field);
		String varName = getChooseFieldFieldSetVarName(field);

		String chooseButtonVarName = getChooseFieldChooseButtonVarName(field);
		String showButtonVarName = getChooseFieldShowButtonVarName(field);

		out.writeLn("\tvar ", varName, " = {");
		out.writeLn("\t\txtype: 'fieldset',");
		out.writeLn("\t\ttitle: '", chooseField.fieldSetName(), "',");
		out.writeLn("\t\tlayout: 'table',");
		out.writeLn("\t\tlayoutConfig: { columns: 3 },");
		out.writeLn();

		out.writeLn("\t\titems: [");

		// 1st row (1st field with choose and show buttons
		ChooseFieldTextField[] textFields = chooseField.fields();
		String firstTextFieldFormPanel = getChooseFieldTextFieldFormPanel(field, textFields[0], configParamName);
		out.writeLn("\t\t\t  ", firstTextFieldFormPanel); // пробел для выравнивания с полями, которые стоят после запятой
		out.writeLn("\t\t\t", ", ", configParamName, ".", chooseButtonVarName);
		out.writeLn("\t\t\t", ", ", configParamName, ".", showButtonVarName);

		for (int i = 1; i < textFields.length; i++)
		{ // 2nd and more rows - formPanel and two empty panels
			out.writeLn();

			String textFieldFormPanel = getChooseFieldTextFieldFormPanel(field, textFields[i], configParamName);
			out.writeLn("\t\t\t, ", textFieldFormPanel);
			out.writeLn("\t\t\t, ", getEmptyFormPanel());
			out.writeLn("\t\t\t, ", getEmptyFormPanel());
		}

		out.writeLn("\t\t]"); // end of items

		out.writeLn("\t};");
		out.writeLn();
	}
	private String getChooseFieldFieldSetVarName(Field field) {
		return concat(sb, field.getName(), "FieldSet");
	}
	private String getFieldSetVarName(FieldSet fieldSet) {
		return concat(sb, fieldSet.id(), "FieldSet");
	}
	private String getChooseFieldTextFieldFormPanel(Field field, ChooseFieldTextField textField, String configParamName) {
		String configVarName = getChooseFieldTextFieldVarName(field, textField);

		return concat(sb,
			GET_FORM_PANEL_FUNCTION_NAME, "({ width: ", textField.formPanelWidth(), " }, ", textField.labelWidth(), ", [ ", configParamName, ".", configVarName, " ])"
		);
	}
	private String getEmptyFormPanel() {
		return concat(sb, GET_FORM_PANEL_FUNCTION_NAME, "({}, 1, [])");
	}

	private void writeAttachmentsFieldsFieldSets(String configParamName) throws IOException {
		for (Field field : getAttachmentsFields(entityClass))
			writeAttachmentsFieldFieldSet(field, configParamName);
	}
	private void writeAttachmentsFieldFieldSet(Field field, String configParamName) throws IOException {
		String varName = getAttachmentsFieldFieldSetVarName(field);
		AttachmentsField attachmentsField = getAttachmentsFieldAnnotation(field);

		out.writeLn("\tvar ", varName, " = {");
		out.writeLn("\t\txtype: 'fieldset',");
		out.writeLn("\t\ttitle: '", attachmentsField.fieldSetTitle(), "',");
		out.writeLn("\t\titems: [ ", configParamName, ".", getAttachmentsFieldPanelVarName(field), " ]");
		out.writeLn("\t};");
		out.writeLn();
	}
	private String getAttachmentsFieldFieldSetVarName(Field field) {
		return concat(sb, field.getName(), "FieldSet");
	}
}