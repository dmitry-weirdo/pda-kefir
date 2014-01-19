Ext.namespace('su.opencode.minstroy.ejb.leasing.developer');

su.opencode.minstroy.ejb.leasing.developer.Developer = function() {
	var ACTION = ru.kg.gtn.form.ACTION;
	var ENTITY_NAME = 'su.opencode.minstroy.ejb.leasing.Developer';

	var WINDOW_ID = 'developerFormWindow';
	var WINDOW_WIDTH = 800;
	var LABEL_WIDTH = 150;
	var GET_URL = '/developerGetNew';

	// form buttons params
	var CREATE_URL = '/developerCreateNew';
	var CREATE_WINDOW_TITLE = 'Ввод застройщика';
	var CREATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var CREATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var CREATE_SAVE_ERROR_MSG = 'Ошибка при сохранении земельного участка';
	var CREATE_CANCEL_BUTTON_ID = 'developer-cancel';
	var CREATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var SHOW_WINDOW_TITLE = 'Просмотр застройщика';
	var SHOW_CANCEL_BUTTON_ID = 'developer-close';
	var SHOW_CANCEL_BUTTON_TEXT = 'Закрыть';

	var UPDATE_URL = '/developerUpdate';
	var UPDATE_WINDOW_TITLE = 'Изменение застройщика';
	var UPDATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var UPDATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var UPDATE_SAVE_ERROR_MSG = 'Ошибка при изменении земельного участка';
	var UPDATE_CANCEL_BUTTON_ID = 'developer-cancel';
	var UPDATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var DELETE_URL = '/developerDelete';
	var DELETE_WINDOW_TITLE = 'Удаление застройщика';
	var DELETE_SAVE_BUTTON_TEXT = 'Удалить';
	var DELETE_SAVE_WAIT_MSG = 'Выполняется удаление. Пожалуйста, подождите...';
	var DELETE_SAVE_ERROR_MSG = 'Ошибка при удалении земельного участка';
	var DELETE_CANCEL_BUTTON_ID = 'developer-cancel';
	var DELETE_CANCEL_BUTTON_TEXT = 'Отмена';

	// fields ids
	var ID_FIELD_ID = 'developer-id';
	var NAME_FIELD_ID = 'developer-name';
	var SHORT_NAME_FIELD_ID = 'developer-shortName';
	var BANKRUPT_FIELD_ID = 'developer-bankrupt';
	var MANAGER_LAST_NAME_FIELD_ID = 'developer-managerLastName';
	var MANAGER_FIRST_NAME_FIELD_ID = 'developer-managerFirstName';
	var MANAGER_MIDDLE_NAME_FIELD_ID = 'developer-managerMiddleName';
	var CONTACT_LAST_NAME_FIELD_ID = 'developer-contactLastName';
	var CONTACT_FIRST_NAME_FIELD_ID = 'developer-contactFirstName';
	var CONTACT_MIDDLE_NAME_FIELD_ID = 'developer-contactMiddleName';
	var FOUNDER_LAST_NAME_FIELD_ID = 'developer-founderLastName';
	var FOUNDER_FIRST_NAME_FIELD_ID = 'developer-founderFirstName';
	var FOUNDER_MIDDLE_NAME_FIELD_ID = 'developer-founderMiddleName';
	var BANK_NAME_FIELD_ID = 'developer-bankName';
	var BIK_FIELD_ID = 'developer-bik';
	var OGRN_FIELD_ID = 'developer-ogrn';
	var INN_FIELD_ID = 'developer-inn';
	var KPP_FIELD_ID = 'developer-kpp';
	var CURRENT_ACCOUNT_FIELD_ID = 'developer-currentAccount';
	var CORRESPONDENT_ACCOUNT_FIELD_ID = 'developer-correspondentAccount';
	var LICENCE_NUMBER_FIELD_ID = 'developer-licenceNumber';
	var LICENCE_ISSUE_DATE_FIELD_ID = 'developer-licenceIssueDate';
	var EGRUL_NUMBER_FIELD_ID = 'developer-egrulNumber';
	var EGRUL_ISSUE_DATE_FIELD_ID = 'developer-egrulIssueDate';
	var FNS_NUMBER_FIELD_ID = 'developer-fnsNumber';
	var FNS_ISSUE_DATE_FIELD_ID = 'developer-fnsIssueDate';

	// address fields
	var JURIDICAL_ADDRESS_TEXT_FIELD_ID = 'developer-juridicalAddress';
	var JURIDICAL_ADDRESS_UPDATE_BUTTON_ID = 'developer-juridicalAddress-update';
	var JURIDICAL_ADDRESS_UPDATE_BUTTON_TEXT = 'Изменить';
	var JURIDICAL_ADDRESS_WINDOW_TITLE = 'Юридический адрес застройщика';

	var PHYSICAL_ADDRESS_TEXT_FIELD_ID = 'developer-physicalAddress';
	var PHYSICAL_ADDRESS_UPDATE_BUTTON_ID = 'developer-physicalAddress-update';
	var PHYSICAL_ADDRESS_UPDATE_BUTTON_TEXT = 'Изменить';
	var PHYSICAL_ADDRESS_WINDOW_TITLE = 'Фактический адрес застройщика';

	// attachments
	var ATTACHMENT_FIELDS_COUNT = 3;

	// licenceAttachments field UploadPanel constants
	var LICENCE_ATTACHMENTS_ENTITY_FIELD_NAME = 'licenceAttachments';
	var LICENCE_ATTACHMENTS_PANEL_ID = 'developer-licenceAttachments';
	var LICENCE_ATTACHMENTS_PANEL_WIDTH = 728;
	var LICENCE_ATTACHMENTS_PANEL_HEIGHT = 150;

	// egrulAttachments field UploadPanel constants
	var EGRUL_ATTACHMENTS_ENTITY_FIELD_NAME = 'egrulAttachments';
	var EGRUL_ATTACHMENTS_PANEL_ID = 'developer-egrulAttachments';
	var EGRUL_ATTACHMENTS_PANEL_WIDTH = 728;
	var EGRUL_ATTACHMENTS_PANEL_HEIGHT = 150;

	// fnsAttachments field UploadPanel constants
	var FNS_ATTACHMENTS_ENTITY_FIELD_NAME = 'fnsAttachments';
	var FNS_ATTACHMENTS_PANEL_ID = 'developer-fnsAttachments';
	var FNS_ATTACHMENTS_PANEL_WIDTH = 728;
	var FNS_ATTACHMENTS_PANEL_HEIGHT = 150;

	var SAVE_BUTTON_ID = 'developer-save';

	var FIELDS_IDS = [
		NAME_FIELD_ID, SHORT_NAME_FIELD_ID, JURIDICAL_ADDRESS_UPDATE_BUTTON_ID, PHYSICAL_ADDRESS_UPDATE_BUTTON_ID, BANKRUPT_FIELD_ID, MANAGER_LAST_NAME_FIELD_ID, MANAGER_FIRST_NAME_FIELD_ID, MANAGER_MIDDLE_NAME_FIELD_ID, CONTACT_LAST_NAME_FIELD_ID, CONTACT_FIRST_NAME_FIELD_ID, CONTACT_MIDDLE_NAME_FIELD_ID, FOUNDER_LAST_NAME_FIELD_ID, FOUNDER_FIRST_NAME_FIELD_ID, FOUNDER_MIDDLE_NAME_FIELD_ID, BANK_NAME_FIELD_ID, BIK_FIELD_ID, OGRN_FIELD_ID, INN_FIELD_ID, KPP_FIELD_ID, CURRENT_ACCOUNT_FIELD_ID, CORRESPONDENT_ACCOUNT_FIELD_ID, LICENCE_NUMBER_FIELD_ID, LICENCE_ISSUE_DATE_FIELD_ID, EGRUL_NUMBER_FIELD_ID, EGRUL_ISSUE_DATE_FIELD_ID, FNS_NUMBER_FIELD_ID, FNS_ISSUE_DATE_FIELD_ID, SAVE_BUTTON_ID, SHOW_CANCEL_BUTTON_ID
	];


	var window;
	var panel;
	var action;
	var developer;
	var fillFormFunction;
	var successHandler;

	var juridicalAddress;
	var physicalAddress;

	var attachmentsFieldsLoadCount;
	var licenceAttachmentsAttachments;
	var egrulAttachmentsAttachments;
	var fnsAttachmentsAttachments;

	// functions
	function fillJuridicalAddressFields(address) {
		Ext.getCmp(JURIDICAL_ADDRESS_TEXT_FIELD_ID).setValue( su.opencode.minstroy.address.Address.getAddressStr(address, false) );
		juridicalAddress = address;
	}
	function fillPhysicalAddressFields(address) {
		Ext.getCmp(PHYSICAL_ADDRESS_TEXT_FIELD_ID).setValue( su.opencode.minstroy.address.Address.getAddressStr(address, false) );
		physicalAddress = address;
	}

	function fillFormFields() {
		var form = panel.getForm();

		ru.kg.gtn.form.fillFormField(form, developer, 'id');
		ru.kg.gtn.form.fillFormField(form, developer, 'name');
		ru.kg.gtn.form.fillFormField(form, developer, 'shortName');
		fillJuridicalAddressFields( ru.kg.gtn.getValue(developer, 'juridicalAddress') );
		fillPhysicalAddressFields( ru.kg.gtn.getValue(developer, 'physicalAddress') );
		ru.kg.gtn.form.fillFormField(form, developer, 'bankrupt');
		ru.kg.gtn.form.fillFormField(form, developer, 'managerLastName');
		ru.kg.gtn.form.fillFormField(form, developer, 'managerFirstName');
		ru.kg.gtn.form.fillFormField(form, developer, 'managerMiddleName');
		ru.kg.gtn.form.fillFormField(form, developer, 'contactLastName');
		ru.kg.gtn.form.fillFormField(form, developer, 'contactFirstName');
		ru.kg.gtn.form.fillFormField(form, developer, 'contactMiddleName');
		ru.kg.gtn.form.fillFormField(form, developer, 'founderLastName');
		ru.kg.gtn.form.fillFormField(form, developer, 'founderFirstName');
		ru.kg.gtn.form.fillFormField(form, developer, 'founderMiddleName');
		ru.kg.gtn.form.fillFormField(form, developer, 'bankName');
		ru.kg.gtn.form.fillFormField(form, developer, 'bik');
		ru.kg.gtn.form.fillFormField(form, developer, 'ogrn');
		ru.kg.gtn.form.fillFormField(form, developer, 'inn');
		ru.kg.gtn.form.fillFormField(form, developer, 'kpp');
		ru.kg.gtn.form.fillFormField(form, developer, 'currentAccount');
		ru.kg.gtn.form.fillFormField(form, developer, 'correspondentAccount');
		ru.kg.gtn.form.fillFormField(form, developer, 'licenceNumber');
		ru.kg.gtn.form.fillFormField(form, developer, 'licenceIssueDate', ru.kg.gtn.getDate);
		ru.kg.gtn.form.fillFormField(form, developer, 'egrulNumber');
		ru.kg.gtn.form.fillFormField(form, developer, 'egrulIssueDate', ru.kg.gtn.getDate);
		ru.kg.gtn.form.fillFormField(form, developer, 'fnsNumber');
		ru.kg.gtn.form.fillFormField(form, developer, 'fnsIssueDate', ru.kg.gtn.getDate);
	}

	function getItems() {
		var disabled = (action == ACTION.SHOW) || (action == ACTION.DELETE);

		var idHiddenField = ru.kg.gtn.form.getHiddenField({}, ID_FIELD_ID, 'id');
		var name = ru.kg.gtn.form.getTextField({ disabled: disabled }, NAME_FIELD_ID, 'name', 'Полное наименование', 300, 255, false);
		var shortName = ru.kg.gtn.form.getTextField({ disabled: disabled }, SHORT_NAME_FIELD_ID, 'shortName', 'Сокращенное наименование', 300, 255, false);

		// juridicalAddress fields
		var juridicalAddressTextField = ru.kg.gtn.form.getTextField({ disabled: disabled, readOnly: true }, JURIDICAL_ADDRESS_TEXT_FIELD_ID, 'juridicalAddressFull', 'Юридический адрес', 400, 400, false);
		var juridicalAddressUpdateButton = ru.kg.gtn.form.getButton({ disabled: disabled, style: { marginLeft: 10 } }, JURIDICAL_ADDRESS_UPDATE_BUTTON_ID, JURIDICAL_ADDRESS_UPDATE_BUTTON_TEXT, function() {
			su.opencode.minstroy.address.Address.init({
				windowTitle: JURIDICAL_ADDRESS_WINDOW_TITLE,
				successHandler: fillJuridicalAddressFields,
				address: juridicalAddress,
				formFieldId: JURIDICAL_ADDRESS_UPDATE_BUTTON_ID
			});
		});

		// physicalAddress fields
		var physicalAddressTextField = ru.kg.gtn.form.getTextField({ disabled: disabled, readOnly: true }, PHYSICAL_ADDRESS_TEXT_FIELD_ID, 'physicalAddressFull', 'Фактический адрес', 400, 400, false);
		var physicalAddressUpdateButton = ru.kg.gtn.form.getButton({ disabled: disabled, style: { marginLeft: 10 } }, PHYSICAL_ADDRESS_UPDATE_BUTTON_ID, PHYSICAL_ADDRESS_UPDATE_BUTTON_TEXT, function() {
			su.opencode.minstroy.address.Address.init({
				windowTitle: PHYSICAL_ADDRESS_WINDOW_TITLE,
				successHandler: fillPhysicalAddressFields,
				address: physicalAddress,
				formFieldId: PHYSICAL_ADDRESS_UPDATE_BUTTON_ID
			});
		});
		var bankrupt = ru.kg.gtn.form.getCheckbox({ disabled: disabled }, BANKRUPT_FIELD_ID, 'bankrupt', 'Банкрот', false);
		var managerLastName = ru.kg.gtn.form.getTextField({ disabled: disabled }, MANAGER_LAST_NAME_FIELD_ID, 'managerLastName', 'Фамилия', 170, 100, false);
		var managerFirstName = ru.kg.gtn.form.getTextField({ disabled: disabled }, MANAGER_FIRST_NAME_FIELD_ID, 'managerFirstName', 'Имя', 170, 100, false);
		var managerMiddleName = ru.kg.gtn.form.getTextField({ disabled: disabled }, MANAGER_MIDDLE_NAME_FIELD_ID, 'managerMiddleName', 'Отчество', 170, 100, false);
		var contactLastName = ru.kg.gtn.form.getTextField({ disabled: disabled }, CONTACT_LAST_NAME_FIELD_ID, 'contactLastName', 'Фамилия', 170, 100, false);
		var contactFirstName = ru.kg.gtn.form.getTextField({ disabled: disabled }, CONTACT_FIRST_NAME_FIELD_ID, 'contactFirstName', 'Имя', 170, 100, false);
		var contactMiddleName = ru.kg.gtn.form.getTextField({ disabled: disabled }, CONTACT_MIDDLE_NAME_FIELD_ID, 'contactMiddleName', 'Отчество', 170, 100, false);
		var founderLastName = ru.kg.gtn.form.getTextField({ disabled: disabled }, FOUNDER_LAST_NAME_FIELD_ID, 'founderLastName', 'Фамилия', 170, 100, false);
		var founderFirstName = ru.kg.gtn.form.getTextField({ disabled: disabled }, FOUNDER_FIRST_NAME_FIELD_ID, 'founderFirstName', 'Имя', 170, 100, false);
		var founderMiddleName = ru.kg.gtn.form.getTextField({ disabled: disabled }, FOUNDER_MIDDLE_NAME_FIELD_ID, 'founderMiddleName', 'Отчество', 170, 100, false);
		var bankName = ru.kg.gtn.form.getTextField({ disabled: disabled }, BANK_NAME_FIELD_ID, 'bankName', 'Наименование банка', 400, 255, false);
		var bik = ru.kg.gtn.form.getTextField({ disabled: disabled, vtype: 'bik' }, BIK_FIELD_ID, 'bik', 'БИК', 150, 9, false);
		var ogrn = ru.kg.gtn.form.getOgrnTextField({ disabled: disabled }, OGRN_FIELD_ID, 'ogrn', 'ОГРН', 150, false);
		var inn = ru.kg.gtn.form.getInnJuridicalTextField({ disabled: disabled }, INN_FIELD_ID, 'inn', 'ИНН', 150, false);
		var kpp = ru.kg.gtn.form.getKppTextField({ disabled: disabled }, KPP_FIELD_ID, 'kpp', 'КПП', 150, false);
		var currentAccount = ru.kg.gtn.form.getTextField({ disabled: disabled }, CURRENT_ACCOUNT_FIELD_ID, 'currentAccount', 'Расчетный счет', 300, 30, false);
		var correspondentAccount = ru.kg.gtn.form.getTextField({ disabled: disabled }, CORRESPONDENT_ACCOUNT_FIELD_ID, 'correspondentAccount', 'Корреспондентский счет', 300, 20, false);
		var licenceNumber = ru.kg.gtn.form.getTextField({ disabled: disabled }, LICENCE_NUMBER_FIELD_ID, 'licenceNumber', 'Номер', 170, 100, false);
		var licenceIssueDate = ru.kg.gtn.form.getDateField({ disabled: disabled }, LICENCE_ISSUE_DATE_FIELD_ID, 'licenceIssueDate', 'Дата выдачи', false);
		var licenceAttachmentsPanel = ru.kg.gtn.form.getMultiUploadPanel({ readOnly: disabled, attachments: licenceAttachmentsAttachments }, LICENCE_ATTACHMENTS_PANEL_ID, ENTITY_NAME, LICENCE_ATTACHMENTS_ENTITY_FIELD_NAME, ru.kg.gtn.getId(developer), LICENCE_ATTACHMENTS_PANEL_WIDTH, LICENCE_ATTACHMENTS_PANEL_HEIGHT);
		var egrulNumber = ru.kg.gtn.form.getTextField({ disabled: disabled }, EGRUL_NUMBER_FIELD_ID, 'egrulNumber', 'Номер', 170, 100, false);
		var egrulIssueDate = ru.kg.gtn.form.getDateField({ disabled: disabled }, EGRUL_ISSUE_DATE_FIELD_ID, 'egrulIssueDate', 'Дата выдачи', false);
		var egrulAttachmentsPanel = ru.kg.gtn.form.getMultiUploadPanel({ readOnly: disabled, attachments: egrulAttachmentsAttachments }, EGRUL_ATTACHMENTS_PANEL_ID, ENTITY_NAME, EGRUL_ATTACHMENTS_ENTITY_FIELD_NAME, ru.kg.gtn.getId(developer), EGRUL_ATTACHMENTS_PANEL_WIDTH, EGRUL_ATTACHMENTS_PANEL_HEIGHT);
		var fnsNumber = ru.kg.gtn.form.getTextField({ disabled: disabled }, FNS_NUMBER_FIELD_ID, 'fnsNumber', 'Номер', 170, 100, false);
		var fnsIssueDate = ru.kg.gtn.form.getDateField({ disabled: disabled }, FNS_ISSUE_DATE_FIELD_ID, 'fnsIssueDate', 'Дата выдачи', false);
		var fnsAttachmentsPanel = ru.kg.gtn.form.getMultiUploadPanel({ readOnly: disabled, attachments: fnsAttachmentsAttachments }, FNS_ATTACHMENTS_PANEL_ID, ENTITY_NAME, FNS_ATTACHMENTS_ENTITY_FIELD_NAME, ru.kg.gtn.getId(developer), FNS_ATTACHMENTS_PANEL_WIDTH, FNS_ATTACHMENTS_PANEL_HEIGHT);

		var items = {
			  idHiddenField: idHiddenField
			, name: name
			, shortName: shortName

			// juridicalAddress fields
			, juridicalAddressTextField: juridicalAddressTextField
			, juridicalAddressUpdateButton: juridicalAddressUpdateButton


			// physicalAddress fields
			, physicalAddressTextField: physicalAddressTextField
			, physicalAddressUpdateButton: physicalAddressUpdateButton

			, bankrupt: bankrupt
			, managerLastName: managerLastName
			, managerFirstName: managerFirstName
			, managerMiddleName: managerMiddleName
			, contactLastName: contactLastName
			, contactFirstName: contactFirstName
			, contactMiddleName: contactMiddleName
			, founderLastName: founderLastName
			, founderFirstName: founderFirstName
			, founderMiddleName: founderMiddleName
			, bankName: bankName
			, bik: bik
			, ogrn: ogrn
			, inn: inn
			, kpp: kpp
			, currentAccount: currentAccount
			, correspondentAccount: correspondentAccount
			, licenceNumber: licenceNumber
			, licenceIssueDate: licenceIssueDate
			, licenceAttachmentsPanel: licenceAttachmentsPanel
			, egrulNumber: egrulNumber
			, egrulIssueDate: egrulIssueDate
			, egrulAttachmentsPanel: egrulAttachmentsPanel
			, fnsNumber: fnsNumber
			, fnsIssueDate: fnsIssueDate
			, fnsAttachmentsPanel: fnsAttachmentsPanel
		};

		return su.opencode.minstroy.ejb.leasing.developer.getFormItemsLayout(items);
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
	function saveButtonHandler() {
		var params = { licenceAttachmentsIds: Ext.getCmp(LICENCE_ATTACHMENTS_PANEL_ID).getAttachmentsIds(), egrulAttachmentsIds: Ext.getCmp(EGRUL_ATTACHMENTS_PANEL_ID).getAttachmentsIds(), fnsAttachmentsIds: Ext.getCmp(FNS_ATTACHMENTS_PANEL_ID).getAttachmentsIds(), juridicalAddress: Ext.encode(juridicalAddress), physicalAddress: Ext.encode(physicalAddress) };
		Ext.apply( params, ru.kg.gtn.form.getNegativeCheckboxParams(panel.getForm(), [ 'bankrupt' ]) );

		panel.getForm().submit({
			url: ru.kg.gtn.contextPath + getSaveUrl(),
			params: params,
			waitMsg: getSaveWaitMsg(),

			success: function(form, action) {
				window.close();

				if (successHandler)
					successHandler(action.response.responseText);
			},
			failure: function(form, action) {
				ru.kg.gtn.showFormResponseMessage(getSaveUrl(), action, form, getSaveErrorMsgTitle());
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
				ru.kg.gtn.checkForm(panel, function() {
					attachmentsFieldsLoadCount = 0;

					Ext.getCmp(LICENCE_ATTACHMENTS_PANEL_ID).loadAttachments(attachmentsPanelsLoadCallback);
					Ext.getCmp(EGRUL_ATTACHMENTS_PANEL_ID).loadAttachments(attachmentsPanelsLoadCallback);
					Ext.getCmp(FNS_ATTACHMENTS_PANEL_ID).loadAttachments(attachmentsPanelsLoadCallback);
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
					Ext.getCmp(LICENCE_ATTACHMENTS_PANEL_ID).removeLoadedAttachments(attachmentsPanelsCancelCallback);
					Ext.getCmp(EGRUL_ATTACHMENTS_PANEL_ID).removeLoadedAttachments(attachmentsPanelsCancelCallback);
					Ext.getCmp(FNS_ATTACHMENTS_PANEL_ID).removeLoadedAttachments(attachmentsPanelsCancelCallback);
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
		ru.kg.gtn.bindMask(window);
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	function initParams(_action, _fillFormFunction, config) {
		action = _action;
		fillFormFunction = _fillFormFunction;
		developer = config.developer;
		successHandler = config.successHandler;
	}
	function init(_action, _fillFormFunction, config) {
		initParams(_action, _fillFormFunction, config);

		if (config.id)
		{
			ru.kg.gtn.ajaxRequest(GET_URL, { id: config.id }, function(_developer) {
				developer = _developer;
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
		var id = config.id || ru.kg.gtn.getId(config.developer);

		ru.kg.gtn.ajaxRequest(ru.kg.gtn.form.ATTACHMENTS_LIST_URL, { entityId: id, entityName: ENTITY_NAME, entityFieldName: LICENCE_ATTACHMENTS_ENTITY_FIELD_NAME }, function(json) {
			licenceAttachmentsAttachments = json.results;
			loadAttachmentsCallback(callback);
		});

		ru.kg.gtn.ajaxRequest(ru.kg.gtn.form.ATTACHMENTS_LIST_URL, { entityId: id, entityName: ENTITY_NAME, entityFieldName: EGRUL_ATTACHMENTS_ENTITY_FIELD_NAME }, function(json) {
			egrulAttachmentsAttachments = json.results;
			loadAttachmentsCallback(callback);
		});

		ru.kg.gtn.ajaxRequest(ru.kg.gtn.form.ATTACHMENTS_LIST_URL, { entityId: id, entityName: ENTITY_NAME, entityFieldName: FNS_ATTACHMENTS_ENTITY_FIELD_NAME }, function(json) {
			fnsAttachmentsAttachments = json.results;
			loadAttachmentsCallback(callback);
		});

	}
	return {
		initCreateForm: function(config) {
			juridicalAddress = null;
			physicalAddress = null;
			licenceAttachmentsAttachments = null;
			egrulAttachmentsAttachments = null;
			fnsAttachmentsAttachments = null;

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