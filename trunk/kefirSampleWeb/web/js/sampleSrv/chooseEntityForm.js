Ext.namespace('su.opencode.kefir.sampleSrv.chooseEntity');

su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity = function() {
	var ACTION = Kefir.form.ACTION;

	var WINDOW_ID = 'chooseEntityFormWindow';
	var WINDOW_WIDTH = 800;
	var LABEL_WIDTH = 150;
	var GET_URL = '/chooseEntityGet';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ChooseEntityVO';

	// form buttons params
	var CREATE_URL = '/chooseEntityCreate';
	var CREATE_WINDOW_TITLE = 'Ввод связанной сущности';
	var CREATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var CREATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var CREATE_SAVE_ERROR_MSG = 'Ошибка при сохранении связанной сущности';
	var CREATE_CANCEL_BUTTON_ID = 'chooseEntity-cancel';
	var CREATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var SHOW_WINDOW_TITLE = 'Просмотр связанной сущности';
	var SHOW_CANCEL_BUTTON_ID = 'chooseEntity-close';
	var SHOW_CANCEL_BUTTON_TEXT = 'Закрыть';

	var UPDATE_URL = '/chooseEntityUpdate';
	var UPDATE_WINDOW_TITLE = 'Изменение связанной сущности';
	var UPDATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var UPDATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var UPDATE_SAVE_ERROR_MSG = 'Ошибка при изменении связанной сущности';
	var UPDATE_CANCEL_BUTTON_ID = 'chooseEntity-cancel';
	var UPDATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var DELETE_URL = '/chooseEntityDelete';
	var DELETE_WINDOW_TITLE = 'Удаление связанной сущности';
	var DELETE_SAVE_BUTTON_TEXT = 'Удалить';
	var DELETE_SAVE_WAIT_MSG = 'Выполняется удаление. Пожалуйста, подождите...';
	var DELETE_SAVE_ERROR_MSG = 'Ошибка при удалении связанной сущности';
	var DELETE_CANCEL_BUTTON_ID = 'chooseEntity-cancel';
	var DELETE_CANCEL_BUTTON_TEXT = 'Отмена';

	// fields ids
	var ID_FIELD_ID = 'chooseEntity-id';
	var NAME_FIELD_ID = 'chooseEntity-name';
	var SHORT_NAME_FIELD_ID = 'chooseEntity-shortName';
	var CORRESPONDENT_ACCOUNT_FIELD_ID = 'chooseEntity-correspondentAccount';
	var INFO_FIELD_ID = 'chooseEntity-info';

	var SAVE_BUTTON_ID = 'chooseEntity-save';

	var FIELDS_IDS = [
		NAME_FIELD_ID, SHORT_NAME_FIELD_ID, CORRESPONDENT_ACCOUNT_FIELD_ID, INFO_FIELD_ID, SAVE_BUTTON_ID, SHOW_CANCEL_BUTTON_ID
	];


	var window;
	var panel;
	var action;
	var chooseEntity;
	var fillFormFunction;
	var successHandler;

	// functions



	function fillFormFields() {
		var form = panel.getForm();

		Kefir.form.fillFormField(form, chooseEntity, 'id');
		Kefir.form.fillFormField(form, chooseEntity, 'name');
		Kefir.form.fillFormField(form, chooseEntity, 'shortName');
		Kefir.form.fillFormField(form, chooseEntity, 'correspondentAccount');
		Kefir.form.fillFormField(form, chooseEntity, 'info');
	}

	function getItems() {
		var disabled = (action == ACTION.SHOW) || (action == ACTION.DELETE);

		var idHiddenField = Kefir.form.getHiddenField({}, ID_FIELD_ID, 'id');
		var name = Kefir.form.getTextField({ disabled: disabled }, NAME_FIELD_ID, 'name', 'Полное наименование', 300, 255, false);
		var shortName = Kefir.form.getTextField({ disabled: disabled }, SHORT_NAME_FIELD_ID, 'shortName', 'Сокращенное наименование', 300, 255, false);
		var correspondentAccount = Kefir.form.getTextField({ disabled: disabled }, CORRESPONDENT_ACCOUNT_FIELD_ID, 'correspondentAccount', 'Корреспондентский счет', 300, 20, false);
		var info = Kefir.form.getTextArea({ disabled: disabled }, INFO_FIELD_ID, 'info', 'Информация', 300, 255, 20, 10, false);

		var items = {
			  idHiddenField: idHiddenField
			, name: name
			, shortName: shortName
			, correspondentAccount: correspondentAccount
			, info: info
		};

		return su.opencode.kefir.sampleSrv.chooseEntity.getFormItemsLayout(items);
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
	function getSaveButton() {
		return {
			id: SAVE_BUTTON_ID,
			text: getSaveButtonText(),
			handler: function() {
				Kefir.checkForm(panel, function() {
					var params = {};
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
		window.on('show', function() { panel.focusFirst(); });
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	function initParams(_action, _fillFormFunction, config) {
		action = _action;
		fillFormFunction = _fillFormFunction;
		chooseEntity = config.chooseEntity;
		successHandler = config.successHandler;
	}
	function init(_action, _fillFormFunction, config) {
		initParams(_action, _fillFormFunction, config);

		if (config.id)
		{
			Kefir.ajaxRequest(GET_URL, { id: config.id, entityName: VO_CLASS }, function(_chooseEntity) {
				chooseEntity = _chooseEntity;
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