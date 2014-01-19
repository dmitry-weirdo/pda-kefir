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
import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.button.SecondRowButton;
import su.opencode.kefir.gen.field.searchField.FilterConfigField;
import su.opencode.kefir.gen.field.searchField.SearchField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.*;
import static su.opencode.kefir.gen.field.searchField.FilterConfigField.ID_FIELD_NAME;
import static su.opencode.kefir.util.StringUtils.capitalize;
import static su.opencode.kefir.util.StringUtils.concat;

public class ListJsFileWriter extends JsFileWriter
{
	public ListJsFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.dir = getJsDirectory(extEntity, entityClass);
		this.fileName = getListJsFileName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeFile() throws IOException {
		writeNamespace();
		writeNamespaceHeader( getListJsFullNamespace(extEntity, entityClass) );

		writeConstants();
		writeVariables();

		writeGetToolbar();
		writeGetButtons();

		if (hasListSecondRowButtons(extEntity))
		{
			writeGetSecondRowButtons();
			writeGetFbar();
		}
		out.writeLn();


		writeUpdateDataStore();
		writeUpdateGridPanel();
		writeInitGridPanel();
		out.writeLn();

		writeGetWindowTitle();
		writeInitWindow();
		writeShowWindow();
		out.writeLn();

		writeReturn();

		writeNamespaceFooter();
	}

	private void writeConstants() throws IOException {
		writeConstant(WINDOW_ID_CONSTANT_NAME, getListWindowId(extEntity, entityClass));
		writeConstant(WINDOW_TITLE_CONSTANT_NAME, extEntity.listWindowTitle());
		out.writeLn();

		if ( hasFilterConfigListFilterFields(entityClass) )
		{
			writeFilterFieldsWindowTitleConstants();
			out.writeLn();
		}

		writeConstant(GRID_URL_CONSTANT_NAME, getListServletUrl(extEntity, entityClass));
		writeConstant(GRID_PANEL_ID_CONSTANT_NAME, getListGridPanelId(extEntity, entityClass));
		writeConstant(VO_CLASS_CONSTANT_NAME, getListVOClassName(extEntity, entityClass));
		writeConstant(NOT_CHOSEN_TITLE_CONSTANT_NAME, extEntity.notChosenTitle());
		writeConstant(NOT_CHOSEN_MESSAGE_CONSTANT_NAME, extEntity.notChosenMessage());
		out.writeLn();
		writeButtonsConstants();
		out.writeLn();

		if (hasListSecondRowButtons(extEntity))
		{
			writeSecondRowButtonsConstants();
			out.writeLn();
		}

		writeSearchFieldsConstants();
	}
	private void writeFilterFieldsWindowTitleConstants() throws IOException {
		writeComment("filter constants");
		for (Field field : getFilterConfigListFilterFields(entityClass))
		{
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			writeConstant(getFilterFieldWindowTitleConstantName(field), filterConfigField.listWindowFilterTitle());
		}
	}
	private String getFilterFieldWindowTitleConstantName(Field field) {
		return concat(sb, getConstantName(field), FILTER_WINDOW_TITLE_CONSTANT_POSTFIX);
	}

	private void writeButtonsConstants() throws IOException {
		writeConstant(CREATE_BUTTON_ID_CONSTANT_NAME, getListCreateButtonId(extEntity, entityClass));
		writeConstant(CREATE_BUTTON_TEXT_CONSTANT_NAME, extEntity.listCreateButtonText());
		writeConstant(SHOW_BUTTON_ID_CONSTANT_NAME, getListShowButtonId(extEntity, entityClass));
		writeConstant(SHOW_BUTTON_TEXT_CONSTANT_NAME, extEntity.listShowButtonText());
		writeConstant(UPDATE_BUTTON_ID_CONSTANT_NAME, getListUpdateButtonId(extEntity, entityClass));
		writeConstant(UPDATE_BUTTON_TEXT_CONSTANT_NAME, extEntity.listUpdateButtonText());
		writeConstant(DELETE_BUTTON_ID_CONSTANT_NAME, getListDeleteButtonId(extEntity, entityClass));
		writeConstant(DELETE_BUTTON_TEXT_CONSTANT_NAME, extEntity.listDeleteButtonText());

		if (extEntity.listExportToExcelButtonPresent())
		{
			writeConstant(EXPORT_TO_EXCEL_URL_CONSTANT_NAME, getListExportToExcelServletUrl(extEntity, entityClass));
			writeConstant(EXPORT_TO_EXCEL_BUTTON_ID_CONSTANT_NAME, getListExportToExcelButtonId(extEntity, entityClass));
			writeConstant(EXPORT_TO_EXCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.listExportToExcelButtonText());
		}

		writeConstant(CLOSE_BUTTON_ID_CONSTANT_NAME, getListCloseButtonId(extEntity, entityClass));
		writeConstant(CLOSE_BUTTON_TEXT_CONSTANT_NAME, extEntity.listCloseButtonText());
	}

	private void writeSecondRowButtonsConstants() throws IOException {
		for (SecondRowButton secondRowButton : extEntity.listSecondRowButtons())
		{
			writeConstant(getSecondRowButtonIdConstantName(secondRowButton), getSecondRowButtonId(secondRowButton, entityClass));
			writeConstant(getSecondRowButtonTextConstantName(secondRowButton), secondRowButton.text());
		}
	}
	private String getSecondRowButtonIdConstantName(SecondRowButton secondRowButton) {
		return concat(sb, getConstantName( getSecondRowButtonName(secondRowButton) ), SECOND_ROW_BUTTON_ID_CONSTANT_POSTFIX);
	}
	private String getSecondRowButtonTextConstantName(SecondRowButton secondRowButton) {
		return concat(sb, getConstantName( getSecondRowButtonName(secondRowButton) ), SECOND_ROW_BUTTON_TEXT_CONSTANT_POSTFIX);
	}

	private void writeSearchFieldsConstants() throws IOException {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
			return;

		String gridPanelId = getListGridPanelId(extEntity, entityClass);
		for (Field field : searchFields)
		{
			SearchField searchField = getSearchFieldAnnotation(field);
			writeConstant( getSearchFieldIdConstantName(field), concat(sb, gridPanelId, "-", getSearchFieldId(searchField, field)) );
			writeConstant( getSearchFieldLabelConstantName(field), searchField.label() );
			out.writeLn();
		}
	}

	private void writeVariables() throws IOException {
		writeVariable(WINDOW_VAR_NAME);
		writeVariable(DATA_STORE_VAR_NAME);
		writeVariable(GRID_PANEL_VAR_NAME);
		out.writeLn();

		if (hasFilterConfigListFilterFields(entityClass))
			writeFilterConfigVariables();
	}
	private void writeFilterConfigVariables() throws IOException {
		writeComment("filter params");

		for (Field field : getFilterConfigListFilterFields(entityClass))
		{
			writeVariable( getFilterVarName(field) );
		}

		out.writeLn();
	}

	private void writeGetToolbar() throws IOException {
		writeFunctionHeader(GET_TOOLBAR_FUNCTION_NAME);

		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
		{ // нет полей поиска
			writeFunctionReturn("[]");
		}
		else
		{ // есть поля поиска -> добавить их в тулбар
			if (isMultiLineToolbar(searchFields))
			{
				checkMultiLineToolbar(searchFields);
				writeMultiLineToolbar(searchFields);
			}
			else
			{
				writeSingleLineToolbar(searchFields);
			}
		}

		writeFunctionFooter(); // end function getToolbar
		out.writeLn();
	}
	private boolean isMultiLineToolbar(List<Field> searchFields) {
		return getMaxToolbarRowNum(searchFields) > SearchField.FIRST_ROW_NUM;
	}
	private int getMaxToolbarRowNum(List<Field> searchFields) {
		int max = SearchField.FIRST_ROW_NUM;
		for (Field field : searchFields)
		{
			SearchField searchField = getSearchFieldAnnotation(field);

			if (searchField.row() > max)
				max = searchField.row();
		}

		logger.info( concat(sb, "Toolbar rows count: ", max + 1));
		return max;
	}

	private void checkMultiLineToolbar(List<Field> searchFields) {
		List<Integer> rowNums = new ArrayList<>();
		for (Field field : searchFields)
		{
			int searchFieldRowNum = getSearchFieldRowNum(field);
			if ( !rowNums.contains(searchFieldRowNum) )
				rowNums.add(searchFieldRowNum);
		}

		for ( int i = 0; i < rowNums.size(); i++ )
			if ( rowNums.indexOf(i) == -1)
				throw new IllegalStateException( concat(sb, "Row number ", i, " is not present in ", SearchField.class.getName(), " annotations") );
	}

	private void writeMultiLineToolbar(List<Field> searchFields) throws IOException {
		out.writeLn("\t\treturn new ", EXT_PANEL_CLASS_NAME, "({");
		out.writeLn("\t\t\titems: [");

		int maxNum = getMaxToolbarRowNum(searchFields);
		for (int i = 0; i <= maxNum; i++) {
			out.writeLn("\t\t\t\t{");
			out.writeLn("\t\t\t\t\txtype: 'toolbar',");
			out.writeLn("\t\t\t\t\titems: [");
			out.writeLn("\t\t\t\t\t\t'", TOOLBAR_TBFILL_VALUE, "'");
			writeSearchFields(getRowSearchFields(searchFields, i), "\t\t\t\t\t\t");
			out.writeLn("\t\t\t\t\t]");
			out.writeLn(getToolbarRowClosingBracket(i, maxNum));
		}

		out.writeLn("\t\t\t]");
		out.writeLn("\t\t});");
	}
	private String getToolbarRowClosingBracket(int rowNum, int maxRowNum) {
		if (rowNum == maxRowNum)
			return "\t\t\t\t}";

		return "\t\t\t\t},";
	}
	private List<Field> getRowSearchFields(List<Field> searchFields, int row) {
		List<Field> fields = new ArrayList<>();

		for (Field field : searchFields)
			if (getSearchFieldRowNum(field) == row)
				fields.add(field);

		return fields;
	}
	private void writeSingleLineToolbar(List<Field> searchFields) throws IOException {
		out.writeLn("\t\treturn new ", EXT_TOOLBAR_CLASS_NAME, "({");
		out.writeLn("\t\t\titems: [");
		out.writeLn("\t\t\t\t'", TOOLBAR_TBFILL_VALUE, "'"); // align to right
		writeSearchFields(searchFields, "\t\t\t\t");
		out.writeLn("\t\t\t]"); // end items [
		out.writeLn("\t\t});"); // end new Ext.Toolbar({
	}
	private void writeSearchFields(List<Field> fields, String indent) throws IOException {
		writeSearchField(fields.get(0), indent); // 0th search field without separator before

		for (int i = 1; i < fields.size(); i++)
		{
			out.writeLn(indent, ", '-'");
			writeSearchField(fields.get(i), indent);
		}
	}
	private void writeSearchField(Field field, String indent) throws IOException {
		SearchField searchField = getSearchFieldAnnotation(field);

		JsHash config = new JsHash();
		config.put("id", getSearchFieldIdConstantName(field));
		config.putString("paramName", getSearchFieldParamName(searchField, field));
		config.put("width", searchField.width());
		config.put("maxLength", getSearchFieldMaxLength(searchField, field));

		if ( !searchField.uppercase() ) // todo: возможно, здесь нужно сделать обратный инверт, если в приложении не будут дефолтно большие буквы
			config.put("style", "{ textTransform: 'none' }");

		if ( !searchField.mask().isEmpty() )
			config.put("plugins", new JsArray( concat(sb, "new ", EXT_INPUT_TEXT_MASK_CLASS_NAME, "('", searchField.mask(), "', false)") ));

		out.writeLn(
			indent, ", ", getSearchFieldLabelConstantName(field), ", ' ', new ", SEARCH_FIELD_CLASS_NAME, "(", config, ")"
		);
	}
	private String getSearchFieldIdConstantName(Field field) {
		if (hasChooseFieldAnnotation(field))
		{
			SearchField searchField = getSearchFieldAnnotation(field);
			String fullFieldName = concat(sb, field.getName(), capitalize(searchField.chooseFieldFieldName()));
			return concat(sb, getConstantName(fullFieldName), SEARCH_FIELD_ID_CONSTANT_POSTFIX);
		}

		return concat(sb, getConstantName(field.getName()), SEARCH_FIELD_ID_CONSTANT_POSTFIX);
	}
	private String getSearchFieldLabelConstantName(Field field) {
		if (hasChooseFieldAnnotation(field))
		{
			SearchField searchField = getSearchFieldAnnotation(field);
			String fullFieldName = concat(sb, field.getName(), capitalize(searchField.chooseFieldFieldName()));
			return concat(sb, getConstantName(fullFieldName), SEARCH_FIELD_LABEL_CONSTANT_POSTFIX);
		}

		return concat(sb, getConstantName(field.getName()), SEARCH_FIELD_LABEL_CONSTANT_POSTFIX);
	}

	private void writeGetButtons() throws IOException {
		writeFunctionHeader(GET_BUTTONS_FUNCTION_NAME);

		// todo: do buttons presence configurable
		writeCreateButton();
		writeShowButton();
		writeUpdateButton();
		writeDeleteButton();

		if (extEntity.listExportToExcelButtonPresent())
			writeExportToExcelButton();

		writeCloseButton();

		// todo: use configurable set of buttons
		JsArray buttons = new JsArray();
		buttons.add(CREATE_BUTTON_VAR_NAME);
		buttons.add(SHOW_BUTTON_VAR_NAME);
		buttons.add(UPDATE_BUTTON_VAR_NAME);
		buttons.add(DELETE_BUTTON_VAR_NAME);

		if (extEntity.listExportToExcelButtonPresent())
			buttons.add(EXPORT_TO_EXCEL_BUTTON_VAR_NAME);

		buttons.add(CLOSE_BUTTON_VAR_NAME);

		writeFunctionReturn(buttons);
		writeFunctionFooter(); // end function getButtons() {
	}

	private void writeCreateButton() throws IOException {
		writeButtonVarHeader(CREATE_BUTTON_VAR_NAME, CREATE_BUTTON_ID_CONSTANT_NAME, CREATE_BUTTON_TEXT_CONSTANT_NAME);

		out.writeLn("\t\t\t", getFormJsNamespaceFullName(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.formCreateFunctionName(), "({");
		writeCrudButtonFilterParams("\t\t\t\t");

		writeReloadDataStoreSuccessHandler("\t\t\t\t"); // todo: configurable create success handler
		out.writeLn("\t\t\t});"); // end TestEntity.initCreateForm({

		writeButtonVarFooter();
		out.writeLn();
	}
	private void writeCrudButtonFilterParams(String indent) throws IOException {
		if ( !hasFilterConfigListFilterFields(entityClass) )
			return;

		for (Field field : getFilterConfigListFilterFields(entityClass))
		{ // parcel: parcel, etc
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			out.writeLn(indent, getFilterConfigFormInitFunctionsParamName(filterConfigField, field), ": ", getFilterVarName(field), ",");
		}
	}

	private void writeShowButton() throws IOException {
		writeButtonVarHeader(SHOW_BUTTON_VAR_NAME, SHOW_BUTTON_ID_CONSTANT_NAME, SHOW_BUTTON_TEXT_CONSTANT_NAME);

		String selectedVarName = getSimpleName(entityClass);
		writeSelectedGridPanelRecordHeader(selectedVarName); // Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		// call showHandler
		out.writeLn("\t\t\t\t", getFormJsNamespaceFullName(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.formShowFunctionName(), "({");
		out.writeLn("\t\t\t\t\t", getSimpleName(entityClass), ": ", selectedVarName, "");
		out.writeLn("\t\t\t\t});"); // end TestEntity.initShowForm({

		writeSelectedGridPanelRecordFooter(); // end Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		writeButtonVarFooter();
		out.writeLn();
	}
	private void writeUpdateButton() throws IOException {
		writeButtonVarHeader(UPDATE_BUTTON_VAR_NAME, UPDATE_BUTTON_ID_CONSTANT_NAME, UPDATE_BUTTON_TEXT_CONSTANT_NAME);

		String selectedVarName = getSimpleName(entityClass);
		writeSelectedGridPanelRecordHeader(selectedVarName); // Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		out.writeLn("\t\t\t\t", getFormJsNamespaceFullName(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.formUpdateFunctionName(), "({");
		out.writeLn("\t\t\t\t\t", getSimpleName(entityClass), ": ", selectedVarName, ",");
		writeCrudButtonFilterParams("\t\t\t\t\t");

		writeReloadDataStoreSuccessHandler(); // todo: configurable update success handler
		out.writeLn("\t\t\t\t});"); // end TestEntity.initUpdateForm({

		writeSelectedGridPanelRecordFooter(); // end Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		writeButtonVarFooter();
		out.writeLn();
	}
	private void writeDeleteButton() throws IOException {
		writeButtonVarHeader(DELETE_BUTTON_VAR_NAME, DELETE_BUTTON_ID_CONSTANT_NAME, DELETE_BUTTON_TEXT_CONSTANT_NAME);

		String selectedVarName = getSimpleName(entityClass);
		writeSelectedGridPanelRecordHeader(selectedVarName); // Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		out.writeLn("\t\t\t\t", getFormJsNamespaceFullName(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.formDeleteFunctionName(), "({");
		out.writeLn("\t\t\t\t\t", getSimpleName(entityClass), ": ", selectedVarName, ",");
		writeReloadDataStoreSuccessHandler(); // todo: configurable delete success handler
		out.writeLn("\t\t\t\t});"); // end TestEntity.initDeleteForm({

		writeSelectedGridPanelRecordFooter(); // end Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {

		writeButtonVarFooter();
		out.writeLn();
	}
	private void writeExportToExcelButton() throws IOException {
		writeButtonVarHeader(EXPORT_TO_EXCEL_BUTTON_VAR_NAME, EXPORT_TO_EXCEL_BUTTON_ID_CONSTANT_NAME, EXPORT_TO_EXCEL_BUTTON_TEXT_CONSTANT_NAME);

		String dataStoreParamsVarName = "dataStoreParams";
		String paramVarName = "param";

		writeVariable(dataStoreParamsVarName, "''", "\t\t\t");
		out.writeLn("\t\t\tfor (var ", paramVarName, " in ", DATA_STORE_VAR_NAME, ".baseParams)");
		out.writeLn("\t\t\t\tif (", DATA_STORE_VAR_NAME, ".baseParams[", paramVarName, "])");
		out.writeLn("\t\t\t\t\t", dataStoreParamsVarName, " += ( '&' + ", paramVarName, " + '=' + ", DATA_STORE_VAR_NAME, ".baseParams[", paramVarName, "] );");
		out.writeLn();

		out.writeLn("\t\t\tlocation.href = ", CONTEXT_PATH_CONSTANT_NAME, " + ", EXPORT_TO_EXCEL_URL_CONSTANT_NAME);
		out.writeLn("\t\t\t\t+ '?entityName=' + ", VO_CLASS_CONSTANT_NAME);
		out.writeLn("\t\t\t\t+ '&sort=' + ", DATA_STORE_VAR_NAME, ".sortInfo.field");
		out.writeLn("\t\t\t\t+ '&dir=' + ", DATA_STORE_VAR_NAME, ".sortInfo.direction");
		out.writeLn("\t\t\t\t+ ", dataStoreParamsVarName, ";");

		writeButtonVarFooter();
		out.writeLn();
	}
	private void writeCloseButton() throws IOException {
		writeButtonVarHeader(CLOSE_BUTTON_VAR_NAME, CLOSE_BUTTON_ID_CONSTANT_NAME, CLOSE_BUTTON_TEXT_CONSTANT_NAME);
		out.writeLn("\t\t\t", WINDOW_VAR_NAME, ".close();");
		writeButtonVarFooter();
		out.writeLn();
	}

	private void writeGetSecondRowButtons() throws IOException {
		writeFunctionHeader(GET_SECOND_ROW_BUTTONS_FUNCTION_NAME);

		JsArray buttons = new JsArray();

		for (SecondRowButton secondRowButton : extEntity.listSecondRowButtons())
		{
			writeSecondRowButton(secondRowButton);
			buttons.add(getSecondRowButtonName(secondRowButton));
		}

		writeFunctionReturn( buttons.toString() );
		writeFunctionFooter();
	}
	private void writeSecondRowButton(SecondRowButton secondRowButton) throws IOException {
		writeButtonVarHeader( getSecondRowButtonName(secondRowButton), getSecondRowButtonIdConstantName(secondRowButton), getSecondRowButtonTextConstantName(secondRowButton) );

		// todo: добавить вставку произвольно заданного handler'a
		if ( !secondRowButton.listEntityClassName().isEmpty() )
		{ // вызов связанного списка
			Class listEntityClass = ExtEntityUtils.getClass(secondRowButton.listEntityClassName());
			if ( !hasExtEntityAnnotation(listEntityClass) )
				throw new IllegalArgumentException( concat(sb, "Incorrect @SecondRowButton.listEntityClassName() for @ExtEntity.listSecondRowButtons() on class ", entityClass.getName(), ": @ExtEntity annotation not present for class ", listEntityClass.getName()) );

			ExtEntity listEntityExtEntity = getExtEntityAnnotation(listEntityClass);

			String selectedVarName = getSimpleName(entityClass);
			writeSelectedGridPanelRecordHeader(selectedVarName);

			out.writeLn("\t\t\t\t", getListInitFunctionFullName(listEntityExtEntity, listEntityClass), "({");

			// pass other params from filter vars
			if ( hasFilterConfigListFilterFields(entityClass) )
			{
				for (Field filterField : getFilterConfigListFilterFields(entityClass))
				{
					out.writeLn("\t\t\t\t\t", getListFilterParamName(filterField, filterField.getType(), listEntityClass), ": ", getFilterVarName(filterField), ",");
				}
			}

			// pass this entity
			out.writeLn("\t\t\t\t\t", getSecondRowButtonListEntityParamName(secondRowButton, entityClass), ": ", selectedVarName);

			out.writeLn("\t\t\t\t});");

			writeSelectedGridPanelRecordFooter(); // end Kefir.selectGridPanelRecord(
		}
		else
		{
			out.writeLn("\t\t\t// todo: implement handler");
		}

		writeButtonVarFooter();
		out.writeLn();
	}

	private void writeGetFbar() throws IOException {
		writeFunctionHeader(GET_FBAR_FUNCTION_NAME);

		out.writeLn("\t\treturn new ", EXT_PANEL_CLASS_NAME, "({");
		out.writeLn("\t\t\titems: [");

		// row with buttons
		JsArray firstRowItems = new JsArray();
		firstRowItems.addString(TOOLBAR_TBFILL_VALUE);
		firstRowItems.add( concat(sb, GET_BUTTONS_FUNCTION_NAME, "()") );

		JsHash firstRow = new JsHash("xtype", "toolbar");
		firstRow.put("items", firstRowItems);

		// row with second row buttons
		JsArray secondRowItems = new JsArray();
		secondRowItems.addString(TOOLBAR_TBFILL_VALUE);
		secondRowItems.add( concat(sb, GET_SECOND_ROW_BUTTONS_FUNCTION_NAME, "()") );

		JsHash secondRow = new JsHash("xtype", "toolbar");
		secondRow.put("items", secondRowItems);

		if (extEntity.listSecondRowBeforeFirstRow())
		{
			out.writeLn("\t\t\t\t", secondRow, ",");
			out.writeLn("\t\t\t\t", firstRow);
		}
		else
		{
			out.writeLn("\t\t\t\t", firstRow, ",");
			out.writeLn("\t\t\t\t", secondRow);
		}

		out.writeLn("\t\t \t]");
		out.writeLn("\t\t});"); // end return new Ext.Panel

		writeFunctionFooter(); // end function getFbar() {
	}

	private void writeButtonVarHeader(String buttonVarName, String idConstantName, String textConstantName) throws IOException {
		JsHash config = hasListSecondRowButtons(extEntity) ? new JsHash("tbar", true) : new JsHash(); // при 2 рядах кнопок все кнопки создаются тулбарными
		out.writeLn("\t\tvar ", buttonVarName, " = ", GET_BUTTON_FUNCTION_NAME, "(", config, ", ", idConstantName, ", ", textConstantName, ", function() {");
	}
	private void writeButtonVarFooter() throws IOException {
		out.writeLn("\t\t});");
	}

	private void writeSelectedGridPanelRecordHeader(String selectedRecordParamName) throws IOException {
		out.writeLn(
			"\t\t\t", SELECT_GRID_PANEL_RECORD_FUNCTION_NAME, "(", GRID_PANEL_VAR_NAME, ", ", NOT_CHOSEN_TITLE_CONSTANT_NAME, ", ", NOT_CHOSEN_MESSAGE_CONSTANT_NAME, ", function(", selectedRecordParamName, ") {"
		);
	}
	private void writeSelectedGridPanelRecordFooter() throws IOException {
		out.writeLn("\t\t\t});");
	}

	private void writeReloadDataStoreSuccessHandler() throws IOException {
		writeReloadDataStoreSuccessHandler("\t\t\t\t\t");
	}
	private void writeReloadDataStoreSuccessHandler(String indent) throws IOException {
		out.writeLn(indent, "successHandler: function() {");
		out.writeLn(indent, "\t", RELOAD_DATA_STORE_FUNCTION_NAME, "(", DATA_STORE_VAR_NAME, ");");
		out.writeLn(indent, "}");
	}


	private void writeUpdateDataStore() throws IOException {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
			return;

		String paramName = DATA_STORE_VAR_NAME;
		writeFunctionHeader(UPDATE_DATA_STORE_FUNCTION_NAME, paramName);

		for (Field field : searchFields)
			out.writeLn("\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getSearchFieldIdConstantName(field), ").store = ", paramName, ";");

		writeFunctionFooter(); // end function updateDataStore
	}
	private void writeUpdateGridPanel() throws IOException {
		String paramName = GRID_PANEL_VAR_NAME;

		writeFunctionHeader(UPDATE_GRID_PANEL_FUNCTION_NAME, paramName);

		// bind row dblclick on show function
		out.writeLn("\t\t", paramName, ".on('rowdblclick', function(", paramName, ") {");

		// call showHandler
		out.writeLn("\t\t\t", getFormJsNamespaceFullName(extEntity, entityClass), PACKAGE_SEPARATOR, extEntity.formShowFunctionName(), "({");
		out.writeLn("\t\t\t\t", getSimpleName(entityClass), ": ", paramName, ".selModel.getSelected()");
		out.writeLn("\t\t\t});");
		out.writeLn("\t\t});"); // end grid.on('rowdblclick', function(gridPanel) {

		writeFunctionFooter(); // end function updateGridPanel
	}
	private void writeInitGridPanel() throws IOException {
		String callbackParamName = "callback";
		String initGridFunctionName = "initGrid";
		String resultParamName = "result";

		writeFunctionHeader(INIT_GRID_PANEL_FUNCTION_NAME, callbackParamName);

		out.writeLn("\t\tvar ", initGridFunctionName, " = function(", resultParamName, ") {");
		out.writeLn("\t\t\t", DATA_STORE_VAR_NAME, " = ", resultParamName, ".dataStore;");
		out.writeLn("\t\t\t", GRID_PANEL_VAR_NAME, " = ", resultParamName, ".gridPanel;");
		out.writeLn();
		out.writeLn("\t\t\tif (", callbackParamName, ")");
		out.writeLn("\t\t\t\t", callbackParamName, "();");
		out.writeLn("\t\t};");
		out.writeLn();

		JsHash config = new JsHash();
		config.setAlignChildren(true);
		config.put("vo", VO_CLASS_CONSTANT_NAME);
		config.put("callback", initGridFunctionName);
		config.put("dsUpdate", getDsUpdateFunction());
		config.put("dsConfig", getDsConfig());
		config.put("gpUpdate", UPDATE_GRID_PANEL_FUNCTION_NAME);
		config.put("gpConfig", getGpConfig());

		out.writeLn("\t\t", INIT_DYNAMIC_GRID_FUNCTION_NAME, "(", config.toStringAligned("\t\t"), ");");

		writeFunctionFooter(); // end function initGridPanel(callback) {
	}
	private String getDsUpdateFunction() {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
			return EXT_EMPTY_FUNCTION_NAME;

		return UPDATE_DATA_STORE_FUNCTION_NAME;
	}
	private JsHash getDsConfig() {
		JsHash dsConfig = new JsHash();
		dsConfig.setAlignChildren(true);
		dsConfig.put("url", GRID_URL_CONSTANT_NAME);

		if ( hasFilterConfigListFilterFields(entityClass) )
			dsConfig.put("baseParams", getDsBaseParams()); // передать параметры фильтрации

		return dsConfig;
	}
	private JsHash getDsBaseParams() {
		JsHash baseParams = new JsHash();

		List<Field> list = getFilterConfigListFilterFields(entityClass);
		for (Field field : list)
		{ // parcelId: Kefir.getId(parcel),
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			String paramName = getFilterConfigFieldName(filterConfigField, field);

			String paramValue;
			String fieldName = getFilterConfigFilterFieldName(filterConfigField, field);
			if (hasChooseFieldAnnotation(field))
				fieldName = filterConfigField.chooseFieldFieldName();

			if (filterConfigField.filterPassSelfAsParam()) // передается сама перемнная (актуально для примитивных фильтров)
				paramValue = getFilterVarName(field);
			else if (fieldName.equals(ID_FIELD_NAME)) // передается поле id из переменной
				paramValue = concat(sb, GET_ID_FUNCTION_NAME, "(", getFilterVarName(field), ")"); // id получается через Kefir.getId()
			else // передается другое поле (не id) из переменной
				paramValue = concat(sb, GET_VALUE_FUNCTION_NAME, "(", getFilterVarName(field), ", '", paramName, "')"); // обычные поля получаются через Kefir.getValue()

			baseParams.put(paramName, paramValue);
		}

		return baseParams;
	}
	private JsHash getGpConfig() {
		JsHash gpConfig = new JsHash();
		gpConfig.put("id", GRID_PANEL_ID_CONSTANT_NAME);
		gpConfig.put("tbar", concat(sb, GET_TOOLBAR_FUNCTION_NAME, "()"));

		if (hasListSecondRowButtons(extEntity)) // два ряда кнопок
			gpConfig.put("fbar", concat(sb, GET_FBAR_FUNCTION_NAME, "()"));
		else // один ряд кнопок
			gpConfig.put("buttons", concat(sb, GET_BUTTONS_FUNCTION_NAME, "()"));

		return gpConfig;
	}

	private void writeGetWindowTitle() throws IOException {
		writeFunctionHeader(GET_WINDOW_TITLE_FUNCTION_NAME);

		if ( !hasFilterConfigListFilterFields(entityClass) )
		{ // нет фильтров -> просто вернуть название окна
			writeFunctionReturn(WINDOW_TITLE_CONSTANT_NAME);
		}
		else
		{
			String varName = "windowTitle";
			writeVariable(varName, "''");
			out.writeLn();

			for (Field field : getFilterConfigListFilterFields(entityClass))
			{
				FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);

				String filterVarName = getFilterVarName(field);
				out.writeLn("\t\tif (", filterVarName, ")");

				if (filterConfigField.listWindowTitleOnly()) // использовать только window_title для параметра-фильтра
					out.writeLn("\t\t\t", varName, " += ( ", getFilterFieldWindowTitleConstantName(field), " + ", WINDOW_TITLE_FILTERS_SEPARATOR_CONSTANT_NAME, " );");
				else // использовать ( window_title для параметра-фильтра + ": " + значение поля из переменной фильтра)
					out.writeLn("\t\t\t", varName, " += ( ", getFilterFieldWindowTitleConstantName(field), " + ': ' + ", GET_VALUE_FUNCTION_NAME, "(", filterVarName, ", '", filterConfigField.listWindowTitleParamName(),"') + ", WINDOW_TITLE_FILTERS_SEPARATOR_CONSTANT_NAME, " );");

				out.writeLn();
			}

			out.writeLn("\t\tif (!", varName, ")"); // примененных фильтров нет -> просто вернуть имя окна
			writeFunctionReturn(WINDOW_TITLE_CONSTANT_NAME, "\t\t\t");

			out.writeLn();
			String windowTitle = concat(sb, WINDOW_TITLE_CONSTANT_NAME, " + ' (' + ", varName, ".substring(0, ", varName, ".length - ", WINDOW_TITLE_FILTERS_SEPARATOR_CONSTANT_NAME, ".length) + ')'");
			writeFunctionReturn(windowTitle);
		}

		writeFunctionFooter();
	}
	private void writeInitWindow() throws IOException {
		writeFunctionHeader(INIT_WINDOW_FUNCTION_NAME);
		out.writeLn("\t\t", WINDOW_VAR_NAME, " = new ", EXT_WINDOW_CLASS_NAME, "({");
		out.writeLn("\t\t\tid: ", WINDOW_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttitle: ", GET_WINDOW_TITLE_FUNCTION_NAME, "(),");
		out.writeLn("\t\t\tautoScroll: true,");
		out.writeLn("\t\t\tconstrain: true,");
		out.writeLn("\t\t\tmodal: true,");
		out.writeLn("\t\t\tmaximized: ", Boolean.toString(extEntity.listWindowMaximized()), ",");
		out.writeLn("\t\t\tresizable: false,");
		out.writeLn("\t\t\tlayout: 'fit',");
		writeDefaultButton();
		out.writeLn();
		out.writeLn("\t\t\titems: [ ", GRID_PANEL_VAR_NAME, " ]");
		out.writeLn("\t\t});");

		// todo: window close and show handlers
		writeFunctionFooter(); // end function initWindow()
	}
	private void writeDefaultButton() throws IOException {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty()) // нет полей поиска -> ничего не фокусировать
			return;

		String id = getSearchFieldIdConstantName(getDefaultFocusedSearchField(searchFields));
		out.writeLn("\t\t\tdefaultButton: ", id, ",");
	}
	private Field getDefaultFocusedSearchField(List<Field> searchFields) {
		Field defaultFocusedSearchField = getSetDefaultFocusedSearchField(searchFields); // если явно задано поле, получающее фокус, вернуть его
		if (defaultFocusedSearchField != null)
			return defaultFocusedSearchField;

		// по умолчанию вернуть первое поле поиска в первом ряде тулбара
		for (Field field : searchFields)
			if ( getSearchFieldAnnotation(field).row() == SearchField.FIRST_ROW_NUM )
				return field;

		return null;
	}
	private Field getSetDefaultFocusedSearchField(List<Field> searchFields) {
		for (Field field : searchFields)
			if ( getSearchFieldAnnotation(field).defaultFocused() )
				return field;

		return null;
	}

	private void writeShowWindow() throws IOException {
		writeFunctionHeader(SHOW_WINDOW_FUNCTION_NAME);
		out.writeLn("\t\t", INIT_WINDOW_FUNCTION_NAME, "();");
		out.writeLn("\t\t", WINDOW_VAR_NAME, ".show();");
		writeFunctionFooter(); // end function showWindow()
	}
	private void writeReturn() throws IOException {
		String configParamName = "config";

		out.writeLn("\treturn {");
		out.writeLn("\t\t", extEntity.listInitFunctionName(), ": function(", configParamName, ") {");

		if (hasFilterConfigListFilterFields(entityClass))
			writeInitFilterFields(configParamName);

		out.writeLn("\t\t\t", INIT_GRID_PANEL_FUNCTION_NAME, "(", SHOW_WINDOW_FUNCTION_NAME, ");");
		out.writeLn("\t\t}");
		out.writeLn("\t}");
	}
	private void writeInitFilterFields(String configParamName) throws IOException {
		// 			parcel = config.parcel; etc
		for (Field field : getFilterConfigListFilterFields(entityClass))
		{
			FilterConfigField filterConfigField = getFilterConfigFieldAnnotation(field);
			out.writeLn("\t\t\t", getFilterVarName(field), " = ", configParamName, ".", getFilterConfigListInitFunctionParamName(filterConfigField, field), ";");
		}

		out.writeLn();
	}

	// constant variable names
	private static final String WINDOW_ID_CONSTANT_NAME = "WINDOW_ID";
	private static final String WINDOW_TITLE_CONSTANT_NAME = "WINDOW_TITLE";

	private static final String FILTER_WINDOW_TITLE_CONSTANT_POSTFIX = "_FILTER_WINDOW_TITLE";

	private static final String GRID_URL_CONSTANT_NAME = "GRID_URL";
	private static final String GRID_PANEL_ID_CONSTANT_NAME = "GRID_PANEL_ID";
	private static final String VO_CLASS_CONSTANT_NAME = "VO_CLASS";
	private static final String NOT_CHOSEN_TITLE_CONSTANT_NAME = "NOT_CHOSEN_TITLE";
	private static final String NOT_CHOSEN_MESSAGE_CONSTANT_NAME = "NOT_CHOSEN_MESSAGE";

 	// buttons
	private static final String CREATE_BUTTON_ID_CONSTANT_NAME = "CREATE_BUTTON_ID";
	private static final String CREATE_BUTTON_TEXT_CONSTANT_NAME = "CREATE_BUTTON_TEXT";
	private static final String SHOW_BUTTON_ID_CONSTANT_NAME = "SHOW_BUTTON_ID";
	private static final String SHOW_BUTTON_TEXT_CONSTANT_NAME = "SHOW_BUTTON_TEXT";
	private static final String UPDATE_BUTTON_ID_CONSTANT_NAME = "UPDATE_BUTTON_ID";
	private static final String UPDATE_BUTTON_TEXT_CONSTANT_NAME = "UPDATE_BUTTON_TEXT";
	private static final String DELETE_BUTTON_ID_CONSTANT_NAME = "DELETE_BUTTON_ID";
	private static final String DELETE_BUTTON_TEXT_CONSTANT_NAME = "DELETE_BUTTON_TEXT";
	private static final String EXPORT_TO_EXCEL_URL_CONSTANT_NAME = "EXPORT_TO_EXCEL_URL";
	private static final String EXPORT_TO_EXCEL_BUTTON_ID_CONSTANT_NAME = "EXPORT_TO_EXCEL_BUTTON_ID";
	private static final String EXPORT_TO_EXCEL_BUTTON_TEXT_CONSTANT_NAME = "EXPORT_TO_EXCEL_BUTTON_TEXT";
	private static final String CLOSE_BUTTON_ID_CONSTANT_NAME = "CLOSE_BUTTON_ID";
	private static final String CLOSE_BUTTON_TEXT_CONSTANT_NAME = "CLOSE_BUTTON_TEXT";

	// second row buttons
	private static final String SECOND_ROW_BUTTON_ID_CONSTANT_POSTFIX = "_ID";
	private static final String SECOND_ROW_BUTTON_TEXT_CONSTANT_POSTFIX = "_TEXT";

	// search fields
	private static final String SEARCH_FIELD_ID_CONSTANT_POSTFIX = "_SEARCH_FIELD_ID";
	private static final String SEARCH_FIELD_LABEL_CONSTANT_POSTFIX = "_SEARCH_FIELD_LABEL";

	// variable names
	private static final String WINDOW_VAR_NAME = "window";
	private static final String DATA_STORE_VAR_NAME = "dataStore";
	private static final String GRID_PANEL_VAR_NAME = "gridPanel";

	private static final String CREATE_BUTTON_VAR_NAME = "createButton";
	private static final String SHOW_BUTTON_VAR_NAME = "showButton";
	private static final String UPDATE_BUTTON_VAR_NAME = "updateButton";
	private static final String DELETE_BUTTON_VAR_NAME = "deleteButton";
	private static final String EXPORT_TO_EXCEL_BUTTON_VAR_NAME = "exportToExcelButton";
	private static final String CLOSE_BUTTON_VAR_NAME = "closeButton";

	// js function names
	private static final String GET_TOOLBAR_FUNCTION_NAME = "getToolbar";
	private static final String GET_BUTTONS_FUNCTION_NAME = "getButtons";

	// second row buttons function names
	private static final String GET_SECOND_ROW_BUTTONS_FUNCTION_NAME = "getSecondRowButtons";
	private static final String GET_FBAR_FUNCTION_NAME = "getFbar";

	private static final String UPDATE_DATA_STORE_FUNCTION_NAME = "updateDataStore";
	private static final String UPDATE_GRID_PANEL_FUNCTION_NAME = "updateGridPanel";
	private static final String INIT_GRID_PANEL_FUNCTION_NAME = "initGridPanel";
	private static final String GET_WINDOW_TITLE_FUNCTION_NAME = "getWindowTitle";
	private static final String INIT_WINDOW_FUNCTION_NAME = "initWindow";
	private static final String SHOW_WINDOW_FUNCTION_NAME = "showWindow";
}