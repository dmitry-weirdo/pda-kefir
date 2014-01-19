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

public class ChooseJsFileWriter extends JsFileWriter
{
	public ChooseJsFileWriter(String baseDir, ExtEntity extEntity, Class entityClass) {
		super(baseDir, null, null);

		this.dir = getJsDirectory(extEntity, entityClass);
		this.fileName = getChooseJsFileName(extEntity, entityClass);
		this.extEntity = extEntity;
		this.entityClass = entityClass;
	}

	protected void writeFile() throws IOException {
		writeNamespace();
		writeNamespaceHeader( getChooseJsFullNamespace(extEntity, entityClass) );

		writeConstants();
		writeVariables();

		writeProcessSuccess();

		writeGetToolbar();
		writeGetButtons();

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
		writeConstant(WINDOW_ID_CONSTANT_NAME, getChooseWindowId(extEntity, entityClass));
		writeConstant(WINDOW_TITLE_CONSTANT_NAME, extEntity.chooseWindowTitle());
		writeConstant(WINDOW_WIDTH_CONSTANT_NAME, extEntity.chooseWindowWidth());
		writeConstant(WINDOW_HEIGHT_CONSTANT_NAME, extEntity.chooseWindowHeight());
		out.writeLn();

		if ( hasFilterConfigListFilterFields(entityClass) )
		{
			writeFilterFieldsWindowTitleConstants();
			out.writeLn();
		}

		writeConstant(GRID_URL_CONSTANT_NAME, getChooseServletUrl(extEntity, entityClass));

		if (extEntity.chooseCreateButtonPresent())
			writeConstant(GET_URL_CONSTANT_NAME, getGetServletUrl(extEntity, entityClass));

		writeConstant(GRID_PANEL_ID_CONSTANT_NAME, getChooseGridPanelId(extEntity, entityClass));
		writeConstant(VO_CLASS_CONSTANT_NAME, getChooseVOClassName(extEntity, entityClass));
		writeConstant(NOT_CHOSEN_TITLE_CONSTANT_NAME, extEntity.notChosenTitle());
		writeConstant(NOT_CHOSEN_MESSAGE_CONSTANT_NAME, extEntity.notChosenMessage());
		out.writeLn();
		writeButtonsConstants();
		out.writeLn();
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
		writeConstant(CHOOSE_BUTTON_ID_CONSTANT_NAME, getChooseChooseButtonId(extEntity, entityClass));
		writeConstant(CHOOSE_BUTTON_TEXT_CONSTANT_NAME, extEntity.chooseChooseButtonText());
		writeConstant(SHOW_BUTTON_ID_CONSTANT_NAME, getChooseShowButtonId(extEntity, entityClass));
		writeConstant(SHOW_BUTTON_TEXT_CONSTANT_NAME, extEntity.chooseShowButtonText());

		if (extEntity.chooseCreateButtonPresent())
		{
			writeConstant(CREATE_BUTTON_ID_CONSTANT_NAME, getChooseCreateButtonId(extEntity, entityClass));
			writeConstant(CREATE_BUTTON_TEXT_CONSTANT_NAME, extEntity.chooseCreateButtonText());
		}

		writeConstant(CANCEL_BUTTON_ID_CONSTANT_NAME, getChooseCancelButtonId(extEntity, entityClass));
		writeConstant(CANCEL_BUTTON_TEXT_CONSTANT_NAME, extEntity.chooseCancelButtonText());
	}
	private void writeSearchFieldsConstants() throws IOException {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
			return;

		String gridPanelId = getChooseGridPanelId(extEntity, entityClass);
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
		writeVariable(SUCCESS_HANDLER_VAR_NAME);
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

	private void writeProcessSuccess() throws IOException {
		String paramName = getSimpleName(entityClass);

		writeFunctionHeader(PROCESS_SUCCESS_FUNCTION_NAME, paramName);
		out.writeLn("\t\tif (", SUCCESS_HANDLER_VAR_NAME, ")");
		out.writeLn("\t\t\t", SUCCESS_HANDLER_VAR_NAME, "(", paramName, ");");
		out.writeLn();
		out.writeLn("\t\t", WINDOW_VAR_NAME, ".close();");
		out.writeLn("\t}");
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

		// todo: do buttons presence configurable if needed
		writeChooseButton();

		if (extEntity.chooseCreateButtonPresent())
			writeCreateButton();

		writeShowButton();
		writeCloseButton();

		// array of buttons
		JsArray buttons = new JsArray();
		buttons.add(CHOOSE_BUTTON_VAR_NAME);
		buttons.add(SHOW_BUTTON_VAR_NAME);

		if (extEntity.chooseCreateButtonPresent())
			buttons.add(CREATE_BUTTON_VAR_NAME);

		buttons.add(CANCEL_BUTTON_VAR_NAME);

		writeFunctionReturn(buttons);
		out.writeLn("\t}");
		out.writeLn();
	}
	private void writeChooseButton() throws IOException {
		out.writeLn("\t\tvar ", CHOOSE_BUTTON_VAR_NAME, " = {");
		out.writeLn("\t\t\tid: ", CHOOSE_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", CHOOSE_BUTTON_TEXT_CONSTANT_NAME, ",");
		out.writeLn("\t\t\thandler: function() {");

		String selectedVarName = getSimpleName(entityClass);
		writeCheckSelected(selectedVarName);

		out.writeLn("\t\t\t\t", PROCESS_SUCCESS_FUNCTION_NAME, "(", selectedVarName, ");");

		out.writeLn("\t\t\t}");
		out.writeLn("\t\t};");
		out.writeLn();
	}
	private void writeShowButton() throws IOException {
		out.writeLn("\t\tvar ", SHOW_BUTTON_VAR_NAME, " = {");
		out.writeLn("\t\t\tid: ", SHOW_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", SHOW_BUTTON_TEXT_CONSTANT_NAME, ",");
		out.writeLn("\t\t\thandler: function() {");

		String selectedVarName = getSimpleName(entityClass);
		writeCheckSelected(selectedVarName);

		// call showHandler
		// todo: configurable successHandler etc (in CUD functions)
		// todo: pass other params to show function if needed
		String namespaceFunctionName = getFormJsFullNamespace(extEntity, entityClass);

		out.writeLn("\t\t\t\t", namespaceFunctionName, PACKAGE_SEPARATOR, extEntity.formShowFunctionName(), "({");
		out.writeLn("\t\t\t\t\t", getSimpleName(entityClass), ": ", selectedVarName, "");
		out.writeLn("\t\t\t\t});"); // end initShowForm({

		out.writeLn("\t\t\t}");
		out.writeLn("\t\t};");
		out.writeLn();
	}
	private void writeCheckSelected(String selectedVarName) throws IOException {
		writeVariable( selectedVarName, concat(sb, GRID_PANEL_VAR_NAME, ".selModel.getSelected()"), "\t\t\t\t" );
		out.writeLn("\t\t\t\tif (!", selectedVarName, ")");
		out.writeLn("\t\t\t\t{");
		out.writeLn("\t\t\t\t\t", EXT_ALERT_FUNCTION_NAME, "(", NOT_CHOSEN_TITLE_CONSTANT_NAME, ", ", NOT_CHOSEN_MESSAGE_CONSTANT_NAME, ");");
		writeFunctionReturn(null, "\t\t\t\t\t");
		out.writeLn("\t\t\t\t}");
		out.writeLn();
	}

	private void writeCreateButton() throws IOException {
		out.writeLn("\t\tvar ", CREATE_BUTTON_VAR_NAME, " = {");
		out.writeLn("\t\t\tid: ", CREATE_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", CREATE_BUTTON_TEXT_CONSTANT_NAME, ",");

		out.writeLn("\t\t\thandler: function() {");

		String namespaceFunctionName = getFormJsFullNamespace(extEntity, entityClass);
		String responseParamName = "response";
		out.writeLn("\t\t\t\t", namespaceFunctionName, PACKAGE_SEPARATOR, extEntity.formCreateFunctionName(), "({");
		writeCrudButtonFilterParams("\t\t\t\t\t");
		out.writeLn("\t\t\t\t\tsuccessHandler: function(", responseParamName, ") {");

		String entityVarName = getSimpleName(entityClass);
		String idVarName = "id";
		writeVariable(entityVarName, concat(sb, EXT_DECODE_FUNCTION_NAME, "(", responseParamName, ")"), "\t\t\t\t\t\t");
		writeVariable(idVarName, concat(sb, GET_ID_FUNCTION_NAME, "(", entityVarName, ")"), "\t\t\t\t\t\t");
		out.writeLn();

		String voParamName = concat(sb, getSimpleName(entityClass), "VO");
		out.writeLn(
			"\t\t\t\t\t\t", AJAX_REQUEST_FUNCTION_NAME, "(", GET_URL_CONSTANT_NAME, ", { id: ", idVarName, ", entityName: ", VO_CLASS_CONSTANT_NAME, " }, function(", voParamName, ") {"
		);
		out.writeLn("\t\t\t\t\t\t\t", PROCESS_SUCCESS_FUNCTION_NAME, "(", voParamName, ");");
		out.writeLn("\t\t\t\t\t\t});"); // end Kefir.ajaxRequest

		out.writeLn("\t\t\t\t\t}"); // end successHandler: function(response) {
		out.writeLn("\t\t\t\t});"); // end initCreateForm({

		out.writeLn("\t\t\t}"); // end handler

		out.writeLn("\t\t};"); // end var createButton =
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

	private void writeCloseButton() throws IOException {
		out.writeLn("\t\tvar ", CANCEL_BUTTON_VAR_NAME, " = {");
		out.writeLn("\t\t\tid: ", CANCEL_BUTTON_ID_CONSTANT_NAME, ",");
		out.writeLn("\t\t\ttext: ", CANCEL_BUTTON_TEXT_CONSTANT_NAME, ",");
		out.writeLn("\t\t\thandler: function() { ", WINDOW_VAR_NAME, ".close(); }");
		out.writeLn("\t\t};");
		out.writeLn();
	}

	private void writeUpdateDataStore() throws IOException {
		List<Field> searchFields = getSearchFields(entityClass);
		if (searchFields == null || searchFields.isEmpty())
			return;

		String paramName = DATA_STORE_VAR_NAME;
		writeFunctionHeader(UPDATE_DATA_STORE_FUNCTION_NAME, paramName);

		for (Field field : searchFields)
			out.writeLn("\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", getSearchFieldIdConstantName(field), ").store = ", paramName, ";");

		out.writeLn("\t}"); // end function updateDataStore
	}
	private void writeUpdateGridPanel() throws IOException {
		String paramName = GRID_PANEL_VAR_NAME;

		writeFunctionHeader(UPDATE_GRID_PANEL_FUNCTION_NAME, paramName);
		out.writeLn("\t\t", paramName, ".on('rowdblclick', function(", paramName, ") {");
		out.writeLn("\t\t\t", PROCESS_SUCCESS_FUNCTION_NAME, "(", paramName, ".selModel.getSelected());");
		out.writeLn("\t\t});");
		out.writeLn("\t}");
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

		writeFunctionFooter();
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
		dsConfig.put("autoLoad", extEntity.chooseListAutoLoad());

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
				paramValue = concat(sb, GET_VALUE_FUNCTION_NAME, "(", getFilterVarName(field), ", '", paramName, "')");

			baseParams.put(paramName, paramValue);
		}

		return baseParams;
	}
	private JsHash getGpConfig() {
		JsHash gpConfig = new JsHash();
		gpConfig.put("id", GRID_PANEL_ID_CONSTANT_NAME);
		gpConfig.put("tbar", concat(sb, GET_TOOLBAR_FUNCTION_NAME, "()"));
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
		out.writeLn("\t\t\twidth: ", WINDOW_WIDTH_CONSTANT_NAME, ",");
		out.writeLn("\t\t\theight: ", WINDOW_HEIGHT_CONSTANT_NAME, ",");

		out.writeLn("\t\t\tautoScroll: true,");
		out.writeLn("\t\t\tconstrain: true,");
		out.writeLn("\t\t\tmodal: true,");
		out.writeLn("\t\t\tmaximized: ", Boolean.toString(extEntity.chooseWindowMaximized()), ",");
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
		out.writeLn("\t\t", extEntity.chooseInitFunctionName(), ": function(", configParamName,") {");
		out.writeLn("\t\t\t", SUCCESS_HANDLER_VAR_NAME, " = ", configParamName, ".", extEntity.chooseInitConfigSuccessHandlerParamName(), ";"); // todo: make config's "successHandler" property name a common constant and use it in all the places

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
	private static final String WINDOW_WIDTH_CONSTANT_NAME = "WINDOW_WIDTH";
	private static final String WINDOW_HEIGHT_CONSTANT_NAME = "WINDOW_HEIGHT";

	private static final String FILTER_WINDOW_TITLE_CONSTANT_POSTFIX = "_FILTER_WINDOW_TITLE";

	private static final String GRID_URL_CONSTANT_NAME = "GRID_URL";
	private static final String GET_URL_CONSTANT_NAME = "GET_URL";
	private static final String GRID_PANEL_ID_CONSTANT_NAME = "GRID_PANEL_ID";
	private static final String VO_CLASS_CONSTANT_NAME = "VO_CLASS";
	private static final String NOT_CHOSEN_TITLE_CONSTANT_NAME = "NOT_CHOSEN_TITLE";
	private static final String NOT_CHOSEN_MESSAGE_CONSTANT_NAME = "NOT_CHOSEN_MESSAGE";

	// buttons
	private static final String CHOOSE_BUTTON_ID_CONSTANT_NAME = "CHOOSE_BUTTON_ID";
	private static final String CHOOSE_BUTTON_TEXT_CONSTANT_NAME = "CHOOSE_BUTTON_TEXT";
	private static final String SHOW_BUTTON_ID_CONSTANT_NAME = "SHOW_BUTTON_ID";
	private static final String SHOW_BUTTON_TEXT_CONSTANT_NAME = "SHOW_BUTTON_TEXT";
	private static final String CREATE_BUTTON_ID_CONSTANT_NAME = "CREATE_BUTTON_ID";
	private static final String CREATE_BUTTON_TEXT_CONSTANT_NAME = "CREATE_BUTTON_TEXT";
	private static final String CANCEL_BUTTON_ID_CONSTANT_NAME = "CANCEL_BUTTON_ID";
	private static final String CANCEL_BUTTON_TEXT_CONSTANT_NAME = "CANCEL_BUTTON_TEXT";

	// search fields
	private static final String SEARCH_FIELD_ID_CONSTANT_POSTFIX = "_SEARCH_FIELD_ID";
	private static final String SEARCH_FIELD_LABEL_CONSTANT_POSTFIX = "_SEARCH_FIELD_LABEL";

	// variable names
	private static final String WINDOW_VAR_NAME = "window";
	private static final String DATA_STORE_VAR_NAME = "dataStore";
	private static final String GRID_PANEL_VAR_NAME = "gridPanel";
	private static final String SUCCESS_HANDLER_VAR_NAME = "successHandler";

	private static final String CHOOSE_BUTTON_VAR_NAME = "chooseButton";
	private static final String CREATE_BUTTON_VAR_NAME = "createButton";
	private static final String SHOW_BUTTON_VAR_NAME = "showButton";
	private static final String CANCEL_BUTTON_VAR_NAME = "cancelButton";

	// js function names
	private static final String PROCESS_SUCCESS_FUNCTION_NAME = "processSuccess";
	private static final String GET_TOOLBAR_FUNCTION_NAME = "getToolbar";
	private static final String GET_BUTTONS_FUNCTION_NAME = "getButtons";
	private static final String UPDATE_DATA_STORE_FUNCTION_NAME = "updateDataStore";
	private static final String UPDATE_GRID_PANEL_FUNCTION_NAME = "updateGridPanel";
	private static final String INIT_GRID_PANEL_FUNCTION_NAME = "initGridPanel";
	private static final String GET_WINDOW_TITLE_FUNCTION_NAME = "getWindowTitle";
	private static final String INIT_WINDOW_FUNCTION_NAME = "initWindow";
	private static final String SHOW_WINDOW_FUNCTION_NAME = "showWindow";
}