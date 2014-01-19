Ext.namespace('su.opencode.kefir.sampleSrv.comboBoxEntity');

su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity = function() {
	var ACTION = Kefir.form.ACTION;

	var WINDOW_ID = 'comboBoxEntityFormWindow';
	var WINDOW_WIDTH = 800;
	var LABEL_WIDTH = 150;
	var GET_URL = '/comboBoxEntityGet';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ComboBoxEntityVO';

	// form buttons params
	var CREATE_URL = '/comboBoxEntityCreate';
	var CREATE_WINDOW_TITLE = 'Ввод комбо сущности';
	var CREATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var CREATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var CREATE_SAVE_ERROR_MSG = 'Ошибка при сохранении комбо сущности';
	var CREATE_CANCEL_BUTTON_ID = 'comboBoxEntity-cancel';
	var CREATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var SHOW_WINDOW_TITLE = 'Просмотр комбо сущности';
	var SHOW_CANCEL_BUTTON_ID = 'comboBoxEntity-close';
	var SHOW_CANCEL_BUTTON_TEXT = 'Закрыть';

	var UPDATE_URL = '/comboBoxEntityUpdate';
	var UPDATE_WINDOW_TITLE = 'Изменение комбо сущности';
	var UPDATE_SAVE_BUTTON_TEXT = 'Сохранить';
	var UPDATE_SAVE_WAIT_MSG = 'Выполняется сохранение. Пожалуйста, подождите...';
	var UPDATE_SAVE_ERROR_MSG = 'Ошибка при изменении комбо сущности';
	var UPDATE_CANCEL_BUTTON_ID = 'comboBoxEntity-cancel';
	var UPDATE_CANCEL_BUTTON_TEXT = 'Отмена';

	var DELETE_URL = '/comboBoxEntityDelete';
	var DELETE_WINDOW_TITLE = 'Удаление комбо сущности';
	var DELETE_SAVE_BUTTON_TEXT = 'Удалить';
	var DELETE_SAVE_WAIT_MSG = 'Выполняется удаление. Пожалуйста, подождите...';
	var DELETE_SAVE_ERROR_MSG = 'Ошибка при удалении комбо сущности';
	var DELETE_CANCEL_BUTTON_ID = 'comboBoxEntity-cancel';
	var DELETE_CANCEL_BUTTON_TEXT = 'Отмена';

	// fields ids
	var ID_FIELD_ID = 'comboBoxEntity-id';
	var CADASTRAL_NUMBER_FIELD_ID = 'comboBoxEntity-cadastralNumber';

	var SAVE_BUTTON_ID = 'comboBoxEntity-save';

	var FIELDS_IDS = [
		CADASTRAL_NUMBER_FIELD_ID, SAVE_BUTTON_ID, SHOW_CANCEL_BUTTON_ID
	];


	var window;
	var panel;
	var action;
	var comboBoxEntity;
	var fillFormFunction;
	var successHandler;

	// functions



	function fillFormFields() {
		var form = panel.getForm();

		Kefir.form.fillFormField(form, comboBoxEntity, 'id');
		Kefir.form.fillFormField(form, comboBoxEntity, 'cadastralNumber');
	}

	function getItems() {
		var disabled = (action == ACTION.SHOW) || (action == ACTION.DELETE);

		var idHiddenField = Kefir.form.getHiddenField({}, ID_FIELD_ID, 'id');
		var cadastralNumber = Kefir.form.getTextField({ disabled: disabled }, CADASTRAL_NUMBER_FIELD_ID, 'cadastralNumber', 'Кадастровый номер', 200, 25, false);

		var items = {
			  idHiddenField: idHiddenField
			, cadastralNumber: cadastralNumber
		};

		return su.opencode.kefir.sampleSrv.comboBoxEntity.getFormItemsLayout(items);
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
		comboBoxEntity = config.comboBoxEntity;
		successHandler = config.successHandler;
	}
	function init(_action, _fillFormFunction, config) {
		initParams(_action, _fillFormFunction, config);

		if (config.id)
		{
			Kefir.ajaxRequest(GET_URL, { id: config.id, entityName: VO_CLASS }, function(_comboBoxEntity) {
				comboBoxEntity = _comboBoxEntity;
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