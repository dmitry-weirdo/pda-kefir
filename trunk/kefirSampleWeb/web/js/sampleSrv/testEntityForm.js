Ext.namespace('su.opencode.kefir.sampleSrv.testEntity');

su.opencode.kefir.sampleSrv.testEntity.TestEntity = function() {
	var ACTION = Kefir.form.ACTION;
	var ENTITY_NAME = 'su.opencode.kefir.sampleSrv.TestEntity';

	var WINDOW_ID = 'testEntityFormWindow';
	var WINDOW_WIDTH = 650;
	var LABEL_WIDTH = 150;
	var GET_URL = '/testEntityGet';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.TestEntityVO';

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
	var COMBO_BOX_ENTITY_FIELD_ID = 'testEntity-comboBoxEntity';

	var CHOOSE_ENTITY_ID_FIELD_ID = 'testEntity-chooseEntity';
	var CHOOSE_ENTITY_NAME_FIELD_ID = 'testEntity-chooseEntity-name';
	var CHOOSE_ENTITY_SHORT_NAME_FIELD_ID = 'testEntity-chooseEntity-shortName';
	var CHOOSE_ENTITY_CORRESPONDENT_ACCOUNT_FIELD_ID = 'testEntity-chooseEntity-correspondentAccount';
	var CHOOSE_ENTITY_CHOOSE_BUTTON_ID = 'testEntity-chooseEntity-choose';
	var CHOOSE_ENTITY_CHOOSE_BUTTON_TEXT = 'Выбрать';
	var CHOOSE_ENTITY_SHOW_BUTTON_ID = 'testEntity-chooseEntity-show';
	var CHOOSE_ENTITY_SHOW_BUTTON_TEXT = 'Просмотреть';

	var OGRN_FIELD_ID = 'testEntity-ogrn';
	var KPP_FIELD_ID = 'testEntity-kpp';
	var INN_FIELD_ID = 'testEntity-inn';

	// address fields
	var JURIDICAL_ADDRESS_TEXT_FIELD_ID = 'testEntity-juridicalAddress';
	var JURIDICAL_ADDRESS_UPDATE_BUTTON_ID = 'testEntity-juridicalAddress-update';
	var JURIDICAL_ADDRESS_UPDATE_BUTTON_TEXT = 'Изменить';
	var JURIDICAL_ADDRESS_WINDOW_TITLE = 'Юридический адрес тестовой сущности';

	var PHYSICAL_ADDRESS_TEXT_FIELD_ID = 'testEntity-physicalAddress';
	var PHYSICAL_ADDRESS_UPDATE_BUTTON_ID = 'testEntity-physicalAddress-update';
	var PHYSICAL_ADDRESS_UPDATE_BUTTON_TEXT = 'Изменить';
	var PHYSICAL_ADDRESS_WINDOW_TITLE = 'Фактический адрес тестовой сущности';

	// attachments
	var ATTACHMENT_FIELDS_COUNT = 2;

	// attachmentsField field UploadPanel constants
	var ATTACHMENTS_FIELD_ENTITY_FIELD_NAME = 'attachmentsField';
	var ATTACHMENTS_FIELD_PANEL_ID = 'testEntity-attachmentsField';
	var ATTACHMENTS_FIELD_PANEL_WIDTH = 600;
	var ATTACHMENTS_FIELD_PANEL_HEIGHT = 200;

	// otherAttachmentsField field UploadPanel constants
	var OTHER_ATTACHMENTS_FIELD_ENTITY_FIELD_NAME = 'otherAttachmentsField';
	var OTHER_ATTACHMENTS_FIELD_PANEL_ID = 'testEntity-otherAttachmentsField';
	var OTHER_ATTACHMENTS_FIELD_PANEL_WIDTH = 600;
	var OTHER_ATTACHMENTS_FIELD_PANEL_HEIGHT = 200;

	var SAVE_BUTTON_ID = 'testEntity-save';

	var FIELDS_IDS = [
		STR_FIELD_FIELD_ID, INT_FIELD_FIELD_ID, INT_SPINNER_FIELD_FIELD_ID, DOUBLE_FIELD_FIELD_ID, DATE_FIELD_FIELD_ID, BOOLEAN_FIELD_FIELD_ID, ENUM_FIELD_FIELD_ID, COMBO_BOX_ENTITY_FIELD_ID, CHOOSE_ENTITY_CHOOSE_BUTTON_ID, OGRN_FIELD_ID, KPP_FIELD_ID, INN_FIELD_ID, JURIDICAL_ADDRESS_UPDATE_BUTTON_ID, PHYSICAL_ADDRESS_UPDATE_BUTTON_ID, SAVE_BUTTON_ID, SHOW_CANCEL_BUTTON_ID
	];

	var CHOOSE_ENTITY_FIELDS = [
		[ 'chooseEntity', 'id' ], 
		[ 'chooseEntityName', 'name' ], 
		[ 'chooseEntityShortName', 'shortName' ], 
		[ 'chooseEntityCorrespondentAccount', 'correspondentAccount' ]
	];


	var window;
	var panel;
	var action;
	var testEntity;
	var fillFormFunction;
	var successHandler;

	// filter params
	var comboBoxEntity;
	var chooseEntity;

	var juridicalAddress;
	var physicalAddress;

	var attachmentsFieldsLoadCount;
	var attachmentsFieldAttachments;
	var otherAttachmentsFieldAttachments;

	// functions
	function fillJuridicalAddressFields(_address) {
		Ext.getCmp(JURIDICAL_ADDRESS_TEXT_FIELD_ID).setValue( su.opencode.kefir.sampleSrv.address.Address.getAddressStr(_address, false) );
		juridicalAddress = _address;
	}
	function fillPhysicalAddressFields(_address) {
		Ext.getCmp(PHYSICAL_ADDRESS_TEXT_FIELD_ID).setValue( su.opencode.kefir.sampleSrv.address.Address.getAddressStr(_address, false) );
		physicalAddress = _address;
	}

	function fillChooseEntityFields(chooseEntity) {
		var form = panel.getForm();
		Kefir.form.fillFormFields(form, chooseEntity, CHOOSE_ENTITY_FIELDS);
		Ext.getCmp(CHOOSE_ENTITY_SHOW_BUTTON_ID).setDisabled(false);
		Kefir.focusNext( Ext.getCmp(CHOOSE_ENTITY_CHOOSE_BUTTON_ID) );
	}

	function fillChooseEntity(chooseEntity) {
		if (!chooseEntity)
			return;

		fillChooseEntityFields(chooseEntity);
		Ext.getCmp(CHOOSE_ENTITY_CHOOSE_BUTTON_ID).disable();
	}

	function fillFormFields() {
		var form = panel.getForm();

		Kefir.form.fillFormField(form, testEntity, 'id');
		Kefir.form.fillFormField(form, testEntity, 'strField');
		Kefir.form.fillFormField(form, testEntity, 'intField');
		Kefir.form.fillFormField(form, testEntity, 'intSpinnerField');
		Kefir.form.fillFormField(form, testEntity, 'doubleField');
		Kefir.form.fillFormField(form, testEntity, 'dateField', Kefir.getDate);
		Kefir.form.fillFormField(form, testEntity, 'booleanField');
		Kefir.form.fillFormField(form, testEntity, 'enumField');

		var comboBoxEntityValue = Kefir.getValue( Kefir.getValue(testEntity, 'comboBoxEntity'), 'id' );
		Kefir.form.setComboBoxValue( form.findField('comboBoxEntity'), comboBoxEntityValue );

		fillChooseEntityFields( Kefir.getValue(testEntity, 'chooseEntity') );
		Kefir.form.fillFormField(form, testEntity, 'ogrn');
		Kefir.form.fillFormField(form, testEntity, 'kpp');
		Kefir.form.fillFormField(form, testEntity, 'inn');
		fillJuridicalAddressFields( Kefir.getValue(testEntity, 'juridicalAddress') );
		fillPhysicalAddressFields( Kefir.getValue(testEntity, 'physicalAddress') );
	}

	function getItems() {
		var disabled = (action == ACTION.SHOW) || (action == ACTION.DELETE);

		var idHiddenField = Kefir.form.getHiddenField({}, ID_FIELD_ID, 'id');
		var strField = Kefir.form.getTextField({ disabled: disabled }, STR_FIELD_FIELD_ID, 'strField', 'Строковое поле', 200, 200, false);
		var intField = Kefir.form.getNumberField({ disabled: disabled, allowDecimals: false, allowZero: true }, INT_FIELD_FIELD_ID, 'intField', 'Целое поле', 100, 9, false);
		var intSpinnerField = Kefir.form.getSpinnerField({ disabled: disabled }, INT_SPINNER_FIELD_FIELD_ID, 'intSpinnerField', 'Целое спиннер поле', 0);
		var doubleField = Kefir.form.getNumberField({ disabled: disabled }, DOUBLE_FIELD_FIELD_ID, 'doubleField', 'Дробное поле', 100, 10, false);
		var dateField = Kefir.form.getDateField({ disabled: disabled, minValue: null, maxValue: null }, DATE_FIELD_FIELD_ID, 'dateField', 'Датовое поле', false);
		var booleanField = Kefir.form.getCheckbox({ disabled: disabled, checked: true }, BOOLEAN_FIELD_FIELD_ID, 'booleanField', 'Булево поле', false);
		var enumField = Kefir.form.getLocalComboBox({ disabled: disabled, editable: false, style: { textTransform: 'none' } }, su.opencode.kefir.sampleSrv.TestEnumStore, ENUM_FIELD_FIELD_ID, 'enumField', 'enumField', 'Энумовое поле', 200, 200, 100, false, true);
		var comboBoxEntity = Kefir.form.getComboBox({ disabled: disabled, queryParam: 'cadastralNumber', displayField: 'cadastralNumber', fields: [ { name: 'id', type: 'int' }, { name: 'cadastralNumber', type: 'string' } ], params: { sort: 'cadastralNumber', dir: 'asc' } }, '/comboBoxEntitysList', COMBO_BOX_ENTITY_FIELD_ID, 'comboBoxEntity', 'comboBoxEntity', 'Селект поле', 400, 400, 25, false, true);

		// chooseEntity fields
		var chooseEntityIdHiddenField = Kefir.form.getHiddenField({}, CHOOSE_ENTITY_ID_FIELD_ID, 'chooseEntity');
		var chooseEntityNameField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_ENTITY_NAME_FIELD_ID, 'chooseEntityName', 'Поле выбора из связанной сущности (имя)', 200, 255, false);
		var chooseEntityShortNameField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_ENTITY_SHORT_NAME_FIELD_ID, 'chooseEntityShortName', 'Поле выбора из связанной сущности (краткое имя)', 200, 255, false);
		var chooseEntityCorrespondentAccountField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, CHOOSE_ENTITY_CORRESPONDENT_ACCOUNT_FIELD_ID, 'chooseEntityCorrespondentAccount', 'Поле выбора из связанной сущности (корр. счет)', 200, 20, false);
		var chooseEntityChooseButton = new Ext.Button({
			disabled: disabled,
			id: CHOOSE_ENTITY_CHOOSE_BUTTON_ID,
			text: CHOOSE_ENTITY_CHOOSE_BUTTON_TEXT,
			style: { marginLeft: 10, marginBottom: 5 },
			handler: function() {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntityChoose.init({
					comboBoxEntity: comboBoxEntity,
					successHandler: fillChooseEntityFields
				});
			}
		});
		var chooseEntityShowButton = new Ext.Button({
			disabled: true,
			id: CHOOSE_ENTITY_SHOW_BUTTON_ID,
			text: CHOOSE_ENTITY_SHOW_BUTTON_TEXT,
			style: { marginLeft: 10, marginBottom: 5 },
			handler: function() {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initShowForm({
					id: panel.getForm().findField('chooseEntity').getValue()
				});
			}
		});
		var attachmentsFieldPanel = Kefir.form.getMultiUploadPanel({ readOnly: disabled, attachments: attachmentsFieldAttachments }, ATTACHMENTS_FIELD_PANEL_ID, ENTITY_NAME, ATTACHMENTS_FIELD_ENTITY_FIELD_NAME, Kefir.getId(testEntity), ATTACHMENTS_FIELD_PANEL_WIDTH, ATTACHMENTS_FIELD_PANEL_HEIGHT);
		var otherAttachmentsFieldPanel = Kefir.form.getMultiUploadPanel({ readOnly: disabled, attachments: otherAttachmentsFieldAttachments }, OTHER_ATTACHMENTS_FIELD_PANEL_ID, ENTITY_NAME, OTHER_ATTACHMENTS_FIELD_ENTITY_FIELD_NAME, Kefir.getId(testEntity), OTHER_ATTACHMENTS_FIELD_PANEL_WIDTH, OTHER_ATTACHMENTS_FIELD_PANEL_HEIGHT);
		var ogrn = Kefir.form.getOgrnTextField({ disabled: disabled }, OGRN_FIELD_ID, 'ogrn', 'ОГРН', 150, false);
		var kpp = Kefir.form.getKppTextField({ disabled: disabled }, KPP_FIELD_ID, 'kpp', 'КПП', 150, false);
		var inn = Kefir.form.getInnJuridicalTextField({ disabled: disabled }, INN_FIELD_ID, 'inn', 'ИНН', 150, false);

		// juridicalAddress fields
		var juridicalAddressTextField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, JURIDICAL_ADDRESS_TEXT_FIELD_ID, 'juridicalAddressFull', 'Юридический адрес', 400, 400, false);
		var juridicalAddressUpdateButton = Kefir.form.getButton({ disabled: disabled, style: { marginLeft: 10 } }, JURIDICAL_ADDRESS_UPDATE_BUTTON_ID, JURIDICAL_ADDRESS_UPDATE_BUTTON_TEXT, function() {
			su.opencode.kefir.sampleSrv.address.Address.init({
				windowTitle: JURIDICAL_ADDRESS_WINDOW_TITLE,
				successHandler: fillJuridicalAddressFields,
				address: juridicalAddress,
				formFieldId: JURIDICAL_ADDRESS_UPDATE_BUTTON_ID
			});
		});

		// physicalAddress fields
		var physicalAddressTextField = Kefir.form.getTextField({ disabled: disabled, readOnly: true }, PHYSICAL_ADDRESS_TEXT_FIELD_ID, 'physicalAddressFull', 'Фактический адрес', 400, 400, false);
		var physicalAddressUpdateButton = Kefir.form.getButton({ disabled: disabled, style: { marginLeft: 10 } }, PHYSICAL_ADDRESS_UPDATE_BUTTON_ID, PHYSICAL_ADDRESS_UPDATE_BUTTON_TEXT, function() {
			su.opencode.kefir.sampleSrv.address.Address.init({
				windowTitle: PHYSICAL_ADDRESS_WINDOW_TITLE,
				successHandler: fillPhysicalAddressFields,
				address: physicalAddress,
				formFieldId: PHYSICAL_ADDRESS_UPDATE_BUTTON_ID
			});
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
			, comboBoxEntity: comboBoxEntity

			// chooseEntity fields
			, chooseEntityIdHiddenField: chooseEntityIdHiddenField
			, chooseEntityNameField: chooseEntityNameField
			, chooseEntityShortNameField: chooseEntityShortNameField
			, chooseEntityCorrespondentAccountField: chooseEntityCorrespondentAccountField
			, chooseEntityChooseButton: chooseEntityChooseButton
			, chooseEntityShowButton: chooseEntityShowButton

			, attachmentsFieldPanel: attachmentsFieldPanel
			, otherAttachmentsFieldPanel: otherAttachmentsFieldPanel
			, ogrn: ogrn
			, kpp: kpp
			, inn: inn

			// juridicalAddress fields
			, juridicalAddressTextField: juridicalAddressTextField
			, juridicalAddressUpdateButton: juridicalAddressUpdateButton


			// physicalAddress fields
			, physicalAddressTextField: physicalAddressTextField
			, physicalAddressUpdateButton: physicalAddressUpdateButton

		};

		return su.opencode.kefir.sampleSrv.testEntity.getFormItemsLayout(items);
	}

	function getSaveUrl() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_URL;
			case ACTION.UPDATE: return UPDATE_URL;
			case ACTION.DELETE: return DELETE_URL;

			default: Kefir.showError('Incorrect action for getSaveUrl: ' + action); return null;
		}
	}
	function getSaveWaitMsg() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_WAIT_MSG;
			case ACTION.UPDATE: return UPDATE_SAVE_WAIT_MSG;
			case ACTION.DELETE: return DELETE_SAVE_WAIT_MSG;

			default: Kefir.showError('Incorrect action for getSaveWaitMsg: ' + action); return null;
		}
	}
	function getSaveErrorMsgTitle() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_ERROR_MSG;
			case ACTION.UPDATE: return UPDATE_SAVE_ERROR_MSG;
			case ACTION.DELETE: return DELETE_SAVE_ERROR_MSG;

			default: Kefir.showError('Incorrect action for getSaveErrorMsgTitle: ' + action); return null;
		}
	}
	function getSaveButtonText() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_SAVE_BUTTON_TEXT;
			case ACTION.UPDATE: return UPDATE_SAVE_BUTTON_TEXT;
			case ACTION.DELETE: return DELETE_SAVE_BUTTON_TEXT;

			default: Kefir.showError('Incorrect action for getSaveButtonText: ' + action); return null;
		}
	}
	function saveButtonHandler() {
		var params = { attachmentsFieldIds: Ext.getCmp(ATTACHMENTS_FIELD_PANEL_ID).getAttachmentsIds(), otherAttachmentsFieldIds: Ext.getCmp(OTHER_ATTACHMENTS_FIELD_PANEL_ID).getAttachmentsIds(), juridicalAddress: Ext.encode(juridicalAddress), physicalAddress: Ext.encode(physicalAddress) };
		Ext.apply( params, Kefir.form.getNegativeCheckboxParams(panel.getForm(), [ 'booleanField' ]) );

		panel.getForm().submit({
			url: Kefir.contextPath + getSaveUrl(),
			params: params,
			waitMsg: getSaveWaitMsg(),

			success: function(form, action) {
				window.close();

				if (successHandler)
					successHandler(action.response.responseText);
			},
			failure: function(form, action) {
				Kefir.showFormResponseMessage(getSaveUrl(), action, form, getSaveErrorMsgTitle());
			}
		});
	}
	function attachmentsPanelsLoadCallback() {
		attachmentsFieldsLoadCount++;

		if (attachmentsFieldsLoadCount == ATTACHMENT_FIELDS_COUNT)
			saveButtonHandler();
	}
	function getSaveButton() {
		return {
			id: SAVE_BUTTON_ID,
			text: getSaveButtonText(),
			handler: function() {
				Kefir.checkForm(panel, function() {
					attachmentsFieldsLoadCount = 0;

					Ext.getCmp(ATTACHMENTS_FIELD_PANEL_ID).loadAttachments(attachmentsPanelsLoadCallback);
					Ext.getCmp(OTHER_ATTACHMENTS_FIELD_PANEL_ID).loadAttachments(attachmentsPanelsLoadCallback);
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

			default: Kefir.showError('Incorrect action for getCancelButtonId: ' + action); return null;
		}
	}
	function getCancelButtonText() {
		switch (action)
		{
			case ACTION.CREATE: return CREATE_CANCEL_BUTTON_TEXT;
			case ACTION.SHOW: return SHOW_CANCEL_BUTTON_TEXT;
			case ACTION.UPDATE: return UPDATE_CANCEL_BUTTON_TEXT;
			case ACTION.DELETE: return DELETE_CANCEL_BUTTON_TEXT;

			default: Kefir.showError('Incorrect action for getCancelButtonText: ' + action); return null;
		}
	}
	function cancelButtonHandler() {
		window.close();
	}
	function attachmentsPanelsCancelCallback() {
		attachmentsFieldsLoadCount++;

		if (attachmentsFieldsLoadCount == ATTACHMENT_FIELDS_COUNT)
			cancelButtonHandler();
	}
	function getCancelButton() {
		return {
			id: getCancelButtonId(),
			text: getCancelButtonText(),
			handler: function() {
				attachmentsFieldsLoadCount = 0;

				if (action == ACTION.CREATE)
				{ // remove saved attachments
					Ext.getCmp(ATTACHMENTS_FIELD_PANEL_ID).removeLoadedAttachments(attachmentsPanelsCancelCallback);
					Ext.getCmp(OTHER_ATTACHMENTS_FIELD_PANEL_ID).removeLoadedAttachments(attachmentsPanelsCancelCallback);
				}
				else
				{
					cancelButtonHandler();
				}
			}
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
		panel = new Kefir.FormPanel({
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

			default: Kefir.showError('Incorrect action for getWindowTitle: ' + action); return null;
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
		window.on('show', function() { fillChooseEntity(chooseEntity); });
		window.on('show', function() { panel.focusFirst(); });
		Kefir.bindMask(window);
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
		comboBoxEntity = config.comboBoxEntity;
		chooseEntity = config.chooseEntity;
	}
	function init(_action, _fillFormFunction, config) {
		initParams(_action, _fillFormFunction, config);

		if (config.id)
		{
			Kefir.ajaxRequest(GET_URL, { id: config.id, entityName: VO_CLASS }, function(_testEntity) {
				testEntity = _testEntity;
				showWindow();
			});
		}
		else
		{
			showWindow();
		}
	}

	function loadAttachmentsCallback(callback) {
		attachmentsFieldsLoadCount++;

		if (attachmentsFieldsLoadCount == ATTACHMENT_FIELDS_COUNT)
			callback();
	}
	function loadAttachments(config, callback) {
		attachmentsFieldsLoadCount = 0;
		var id = config.id || Kefir.getId(config.testEntity);

		Kefir.ajaxRequest(Kefir.form.ATTACHMENTS_LIST_URL, { entityId: id, entityName: ENTITY_NAME, entityFieldName: ATTACHMENTS_FIELD_ENTITY_FIELD_NAME }, function(json) {
			attachmentsFieldAttachments = json.results;
			loadAttachmentsCallback(callback);
		});

		Kefir.ajaxRequest(Kefir.form.ATTACHMENTS_LIST_URL, { entityId: id, entityName: ENTITY_NAME, entityFieldName: OTHER_ATTACHMENTS_FIELD_ENTITY_FIELD_NAME }, function(json) {
			otherAttachmentsFieldAttachments = json.results;
			loadAttachmentsCallback(callback);
		});

	}
	return {
		initCreateForm: function(config) {
			juridicalAddress = null;
			physicalAddress = null;
			attachmentsFieldAttachments = null;
			otherAttachmentsFieldAttachments = null;

			init(ACTION.CREATE, Ext.emptyFn, config);
		},

		initShowForm: function(config) {
			loadAttachments(config, function() {
				init(ACTION.SHOW, fillFormFields, config);
			});
		},

		initUpdateForm: function(config) {
			loadAttachments(config, function() {
				init(ACTION.UPDATE, fillFormFields, config);
			});
		},

		initDeleteForm: function(config) {
			loadAttachments(config, function() {
				init(ACTION.DELETE, fillFormFields, config);
			});
		}
	}
}();