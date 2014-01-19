Ext.namespace('test.testEntity');

test.testEntity.TestEntity = function() {
	var ACTION = ru.kg.gtn.form.ACTION;

	var WINDOW_ID = 'testEntityFormWindow';
	var WINDOW_WIDTH = 650;
	var LABEL_WIDTH = 150;
	var GET_URL = '/testEntityGet';

	// form buttons params
	var CREATE_URL = '/testEntityCreate';
	var CREATE_WINDOW_TITLE = 'Ввод тестовой сущности';
	var CREATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var CREATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var CREATE_SAVE_ERROR_MSG = 'Ошибка при сохранении тестовой сущности';
	var CREATE_CANCEL_BUTTON_ID = 'testEntity-cancel';
	var CREATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var SHOW_WINDOW_TITLE = 'Просмотр тестовой сущности';
	var SHOW_CANCEL_BUTTON_ID = 'testEntity-close';
	var SHOW_CANCEL_BUTTON_TEXT = 'Закрыть';

	var UPDATE_URL = '/testEntityUpdate';
	var UPDATE_WINDOW_TITLE = 'Изменение тестовой сущности';
	var UPDATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var UPDATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var UPDATE_SAVE_ERROR_MSG = 'Ошибка при изменении тестовой сущности';
	var UPDATE_CANCEL_BUTTON_ID = 'testEntity-cancel';
	var UPDATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var DELETE_URL = '/testEntityDelete';
	var DELETE_WINDOW_TITLE = 'Удаление тестовой сущности';
	var DELETE_SAVE_BUTTON_TEXT = 'Удалить';
	var DELETE_SAVE_WAIT_MSG = 'Выполняется удаление. Пожалуйста, подождите...';
	var DELETE_SAVE_ERROR_MSG = 'Ошибка при удалении тестовой сущности';
	var DELETE_CANCEL_BUTTON_ID = 'testEntity-cancel';
	var DELETE_CANCEL_BUTTON_TEXT = 'Отмена';

	// fields ids
	var ID_FIELD_ID = 'testEntity-id';
	var STR_FIELD_FIELD_ID = 'testEntity-strField';
	var INT_FIELD_FIELD_ID = 'testEntity-intField';
	var INT_SPINNER_FIELD_FIELD_ID = 'testEntity-intSpinnerField';
	var DOUBLE_FIELD_FIELD_ID = 'testEntity-doubleField';
	var DATE_FIELD_FIELD_ID = 'testEntity-dateField';
	var BOOLEAN_FIELD_FIELD_ID = 'testEntity-booleanField';
	var ENUM_FIELD_FIELD_ID = 'testEntity-enumField';
	var COMBO_FIELD_FIELD_ID = 'testEntity-comboField';

	var CHOOSE_FIELD_ID_FIELD_ID = 'testEntity-chooseField';
	var CHOOSE_FIELD_NAME_FIELD_ID = 'testEntity-chooseField-name';
	var CHOOSE_FIELD_SHORT_NAME_FIELD_ID = 'testEntity-chooseField-shortName';
	var CHOOSE_FIELD_CORRESPONDENT_ACCOUNT_FIELD_ID = 'testEntity-chooseField-correspondentAccount';
	var CHOOSE_FIELD_CHOOSE_BUTTON_ID = 'testEntity-chooseField-choose';
	var CHOOSE_FIELD_CHOOSE_BUTTON_TEXT = 'Выбрать застройщика';
	var CHOOSE_FIELD_SHOW_BUTTON_ID = 'testEntity-chooseField-show';
	var CHOOSE_FIELD_SHOW_BUTTON_TEXT = 'Просмотреть застройщнка';


	var SAVE_BUTTON_ID = 'testEntity-save';

	var FIELDS_IDS = [
		STR_FIELD_FIELD_ID, INT_FIELD_FIELD_ID, INT_SPINNER_FIELD_FIELD_ID, DOUBLE_FIELD_FIELD_ID, DATE_FIELD_FIELD_ID, BOOLEAN_FIELD_FIELD_ID, ENUM_FIELD_FIELD_ID, COMBO_FIELD_FIELD_ID, CHOOSE_FIELD_CHOOSE_BUTTON_ID, SAVE_BUTTON_ID, SHOW_CANCEL_BUTTON_ID
	];

	var CHOOSE_FIELD_FIELDS = [
		[ 'chooseField', 'id' ], 
		[ 'chooseFieldName', 'name' ], 
		[ 'chooseFieldShortName', 'shortName' ], 
		[ 'chooseFieldCorrespondentAccount', 'correspondentAccount' ]
	];


	var window;
	var panel;
	var action;
	var testEntity;
	var fillFormFunction;
	var successHandler;

	// functions
	function fillChooseFieldFields(chooseField) {
		var form = panel.getForm();
		ru.kg.gtn.form.fillFormFields(form, chooseField, CHOOSE_FIELD_FIELDS);
		Ext.getCmp(CHOOSE_FIELD_SHOW_BUTTON_ID).setDisabled(false);
		ru.kg.gtn.focusNext( Ext.getCmp(CHOOSE_FIELD_CHOOSE_BUTTON_ID) );
	}

	function fillFormFields() {
		var form = panel.getForm();

		ru.kg.gtn.form.fillFormField(form, testEntity, 'id');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'strField');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'intField');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'intSpinnerField');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'doubleField');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'dateField', ru.kg.gtn.getDate);
		ru.kg.gtn.form.fillFormField(form, testEntity, 'booleanField');
		ru.kg.gtn.form.fillFormField(form, testEntity, 'enumField');

		var comboFieldValue = ru.kg.gtn.getValue( ru.kg.gtn.getValue(testEntity, 'comboField'), 'id' );
		ru.kg.gtn.form.setComboBoxValue( form.findField('comboField'), comboFieldValue );

		fillChooseFieldFields( ru.kg.gtn.getValue(testEntity, 'chooseField') );
	}

	function getItemsLayout(config) {
		var chooseFieldFieldSet = {
			xtype: 'fieldset',
			title: 'Связанная сущность',
			layout: 'table',
			layoutConfig: { columns: 3 },

			items: [
				  ru.kg.gtn.form.getFormPanel({ width: 450 }, 240, [ config.chooseFieldNameField ])
				, config.chooseFieldChooseButton
				, config.chooseFieldShowButton

				, ru.kg.gtn.form.getFormPanel({ width: 440 }, 150, [ config.chooseFieldShortNameField ])
				, ru.kg.gtn.form.getFormPanel({}, 1, [])
				, ru.kg.gtn.form.getFormPanel({}, 1, [])

				, ru.kg.gtn.form.getFormPanel({ width: 450 }, 240, [ config.chooseFieldCorrespondentAccountField ])
				, ru.kg.gtn.form.getFormPanel({}, 1, [])
				, ru.kg.gtn.form.getFormPanel({}, 1, [])
			]
		};

		return [
			  config.idHiddenField
			, config.strField
			, config.intField
			, config.intSpinnerField
			, config.doubleField
			, config.dateField
			, config.booleanField
			, config.enumField
			, config.comboField

			// fields of chooseField
			, config.chooseFieldIdHiddenField
			, chooseFieldFieldSet

		];
	}
	function getItems() {
		var disabled = (action == ACTION.SHOW) || (action == ACTION.DELETE);

		var idHiddenField = ru.kg.gtn.form.getHiddenField({}, ID_FIELD_ID, 'id');
		var strField = ru.kg.gtn.form.getTextField({ disabled: disabled }, STR_FIELD_FIELD_ID, 'strField', 'Строковое поле', 200, 200, false);
		var intField = ru.kg.gtn.form.getNumberField({ disabled: disabled, allowDecimals: false, allowZero: true }, INT_FIELD_FIELD_ID, 'intField', 'Целое поле', 100, 9, false);
		var intSpinnerField = ru.kg.gtn.form.getSpinnerField({ disabled: disabled }, INT_SPINNER_FIELD_FIELD_ID, 'intSpinnerField', 'Целое спиннер поле', 0);
		var doubleField = ru.kg.gtn.form.getNumberField({ disabled: disabled }, DOUBLE_FIELD_FIELD_ID, 'doubleField', 'Дробное поле', 100, 10, false);
		var dateField = ru.kg.gtn.form.getDateField({ disabled: disabled }, DATE_FIELD_FIELD_ID, 'dateField', 'Датовое поле', false);
		var booleanField = ru.kg.gtn.form.getCheckbox({ disabled: disabled, checked: true }, BOOLEAN_FIELD_FIELD_ID, 'booleanField', 'Булево поле', false);
		var enumField = ru.kg.gtn.form.getLocalComboBox({ disabled: disabled, editable: false, style: { textTransform: 'none' } }, test.TestEnumStore, ENUM_FIELD_FIELD_ID, 'enumField', 'enumField', 'Энумовое поле', 200, 200, 100, false, true);
		var comboField = ru.kg.gtn.form.getComboBox({ disabled: disabled, displayField: 'cadastralNumber', fields: [ { name: 'id', type: 'int' }, { name: 'cadastralNumber', type: 'string' } ], params: { sort: 'cadastralNumber', dir: 'asc' } }, '/parcelsList', COMBO_FIELD_FIELD_ID, 'comboField', 'comboField', 'Селект поле', 400, 400, 25, false, true);

		// chooseField fields
		var chooseFieldIdHiddenField = ru.kg.gtn.form.getHiddenField({}, CHOOSE_FIELD_ID_FIELD_ID, 'chooseField');
		var chooseFieldNameField = ru.kg.gtn.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_FIELD_NAME_FIELD_ID, 'chooseFieldName', 'Поле выбора из другой сущности (имя)', 200, 255, false);
		var chooseFieldShortNameField = ru.kg.gtn.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_FIELD_SHORT_NAME_FIELD_ID, 'chooseFieldShortName', 'Поле выбора из другой сущности (краткое имя)', 200, 255, false);
		var chooseFieldCorrespondentAccountField = ru.kg.gtn.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_FIELD_CORRESPONDENT_ACCOUNT_FIELD_ID, 'chooseFieldCorrespondentAccount', 'Поле выбора из другой сущности (корр. счет)', 200, 20, false);
		var chooseFieldChooseButton = new Ext.Button({
			disabled: disabled,
			id: CHOOSE_FIELD_CHOOSE_BUTTON_ID,
			text: CHOOSE_FIELD_CHOOSE_BUTTON_TEXT,
			style: { marginLeft: 10, marginBottom: 5 },
			handler: function() {
				su.opencode.minstroy.leasing.developer.DeveloperChoose.init({
					successHandler: fillChooseFieldFields
				});
			}
		});
		var chooseFieldShowButton = new Ext.Button({
			disabled: true,
			id: CHOOSE_FIELD_SHOW_BUTTON_ID,
			text: CHOOSE_FIELD_SHOW_BUTTON_TEXT,
			style: { marginLeft: 10, marginBottom: 5 },
			handler: function() {
				su.opencode.minstroy.ejb.leasing.developer.Developer.initShowForm({
					id: panel.getForm().findField('chooseField').getValue()
				});
			}
		});

		var items = {
			  idHiddenField: idHiddenField
			, strField: strField
			, intField: intField
			, intSpinnerField: intSpinnerField
			, doubleField: doubleField
			, dateField: dateField
			, booleanField: booleanField
			, enumField: enumField
			, comboField: comboField

			// chooseField fields
			, chooseFieldIdHiddenField: chooseFieldIdHiddenField
			, chooseFieldNameField: chooseFieldNameField
			, chooseFieldShortNameField: chooseFieldShortNameField
			, chooseFieldCorrespondentAccountField: chooseFieldCorrespondentAccountField
			, chooseFieldChooseButton: chooseFieldChooseButton
			, chooseFieldShowButton: chooseFieldShowButton

		};

		return getItemsLayout(items);
	}

	function getSaveUrl() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_URL;
			case ACTION.UPDATE: return UPDATE_URL;
			case ACTION.DELETE: return DELETE_URL;

			default: ru.kg.gtn.showError('Incorrect action for getSaveUrl: ' + action); return null;
		}
	}
	function getSaveWaitMsg() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_WAIT_MSG;
			case ACTION.UPDATE: return UPDATE_SAVE_WAIT_MSG;
			case ACTION.DELETE: return DELETE_SAVE_WAIT_MSG;

			default: ru.kg.gtn.showError('Incorrect action for getSaveWaitMsg: ' + action); return null;
		}
	}
	function getSaveErrorMsgTitle() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_ERROR_MSG;
			case ACTION.UPDATE: return UPDATE_SAVE_ERROR_MSG;
			case ACTION.DELETE: return DELETE_SAVE_ERROR_MSG;

			default: ru.kg.gtn.showError('Incorrect action for getSaveErrorMsgTitle: ' + action); return null;
		}
	}
	function getSaveButtonText() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_BUTTON_TEXT;
			case ACTION.UPDATE: return UPDATE_SAVE_BUTTON_TEXT;
			case ACTION.DELETE: return DELETE_SAVE_BUTTON_TEXT;

			default: ru.kg.gtn.showError('Incorrect action for getSaveButtonText: ' + action); return null;
		}
	}
	function getSaveButton() {
		return {
			id: SAVE_BUTTON_ID,
			text: getSaveButtonText(),
			handler: function() {
				ru.kg.gtn.checkForm(panel, function() {
					panel.getForm().submit({
						url: ru.kg.gtn.contextPath + getSaveUrl(),
						waitMsg: getSaveWaitMsg(),

						success: function(form, action) {
							window.close();

							if (successHandler)
								successHandler(action.response.responseText);
						},
						failure: function(form, action) {
							Ext.MessageBox.alert(getSaveErrorMsgTitle(), action.response.responseText);
						}
					});
				});
			}
		};
	}

	function getCancelButtonId() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_CANCEL_BUTTON_ID;
			case ACTION.SHOW: return SHOW_CANCEL_BUTTON_ID;
			case ACTION.UPDATE: return UPDATE_CANCEL_BUTTON_ID;
			case ACTION.DELETE: return DELETE_CANCEL_BUTTON_ID;

			default: ru.kg.gtn.showError('Incorrect action for getCancelButtonId: ' + action); return null;
		}
	}
	function getCancelButtonText() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_CANCEL_BUTTON_TEXT;
			case ACTION.SHOW: return SHOW_CANCEL_BUTTON_TEXT;
			case ACTION.UPDATE: return UPDATE_CANCEL_BUTTON_TEXT;
			case ACTION.DELETE: return DELETE_CANCEL_BUTTON_TEXT;

			default: ru.kg.gtn.showError('Incorrect action for getCancelButtonText: ' + action); return null;
		}
	}
	function getCancelButton() {
		return {
			id: getCancelButtonId(),
			text: getCancelButtonText(),
			handler: function() { window.close(); }
		};
	}

	function getButtons() {
		var cancelButton = getCancelButton();
		if (action == ACTION.SHOW)
			return [ cancelButton ];

		var saveButton = getSaveButton();
		return [ saveButton, cancelButton ];
	}

	function initPanel() {
		panel = new ru.kg.gtn.FormPanel({
			fieldsIds: FIELDS_IDS, 
			autoHeight: true,
			labelWidth: LABEL_WIDTH, 

			items: getItems(),
			buttons: getButtons()
		});
	}

	function getDefaultButton() {
		switch (action)
		{
			case ACTION.SHOW: return SHOW_CANCEL_BUTTON_ID;
			case ACTION.DELETE: return SAVE_BUTTON_ID;

			default: return undefined;
		}
	}
	function getWindowTitle() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_WINDOW_TITLE;
			case ACTION.SHOW: return SHOW_WINDOW_TITLE;
			case ACTION.UPDATE: return UPDATE_WINDOW_TITLE;
			case ACTION.DELETE: return DELETE_WINDOW_TITLE;

			default: ru.kg.gtn.showError('Incorrect action for getWindowTitle: ' + action); return null;
		}
	}
	function initWindow() {
		initPanel();

		window = new Ext.Window({
			id: WINDOW_ID,
			title: getWindowTitle(),
			width: WINDOW_WIDTH,
			autoHeight: true,
			resizable: false,
			constrain: true,
			modal: true,
			layout: 'fit',

			defaultButton: getDefaultButton(),
			items: [ panel ]
		});

		window.on('show', fillFormFunction);
		window.on('show', function() { panel.focusFirst() });
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	function initParams(_action, _fillFormFunction, config) {
		action = _action;
		fillFormFunction = _fillFormFunction;
		testEntity = config.testEntity;
		successHandler = config.successHandler;
	}
	function init(_action, _fillFormFunction, config) {
		initParams(_action, _fillFormFunction, config);

		if (config.id)
		{
			ru.kg.gtn.ajaxRequest(GET_URL, { id: config.id }, function(_testEntity) {
				testEntity = _testEntity;
				showWindow();
			});
		}
		else
		{
			showWindow();
		}
	}

	return {
		initCreateForm: function(config) {
			init(ACTION.CREATE, Ext.emptyFn, config);
		},

		initShowForm: function(config) {
			init(ACTION.SHOW, fillFormFields, config);
		},

		initUpdateForm: function(config) {
			init(ACTION.UPDATE, fillFormFields, config);
		},

		initDeleteForm: function(config) {
			init(ACTION.DELETE, fillFormFields, config);
		}
	}
}();