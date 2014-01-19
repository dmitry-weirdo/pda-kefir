Ext.namespace('su.opencode.kefir.sampleSrv.comboBoxEntity');

su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntityChoose = function() {
	var WINDOW_ID = 'comboBoxEntityChooseWindow';
	var WINDOW_TITLE = 'Выбор комбо сущности';
	var WINDOW_WIDTH = 1000;
	var WINDOW_HEIGHT = 600;

	var GRID_URL = '/comboBoxEntitysList';
	var GET_URL = '/comboBoxEntityGet';
	var GRID_PANEL_ID = 'comboBoxEntityChooseGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ComboBoxEntityVO';
	var NOT_CHOSEN_TITLE = 'Комбо сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите комбо сущность';

	var CHOOSE_BUTTON_ID = 'comboBoxEntityChoose-chooseButton';
	var CHOOSE_BUTTON_TEXT = 'Выбрать';
	var SHOW_BUTTON_ID = 'comboBoxEntityChoose-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var CREATE_BUTTON_ID = 'comboBoxEntityChoose-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var CANCEL_BUTTON_ID = 'comboBoxEntityChoose-cancelButton';
	var CANCEL_BUTTON_TEXT = 'Отмена';

	var CADASTRAL_NUMBER_SEARCH_FIELD_ID = 'comboBoxEntityChooseGrid-cadastralNumberSearchField';
	var CADASTRAL_NUMBER_SEARCH_FIELD_LABEL = 'Кадастровый номер:';

	var window;
	var dataStore;
	var gridPanel;
	var successHandler;

	function processSuccess(comboBoxEntity) {
		if (successHandler)
			successHandler(comboBoxEntity);

		window.close();
	}

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, CADASTRAL_NUMBER_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: CADASTRAL_NUMBER_SEARCH_FIELD_ID, paramName: 'cadastralNumber', width: 250, maxLength: 25 })
			]
		});
	}

	function getButtons() {
		var chooseButton = {
			id: CHOOSE_BUTTON_ID,
			text: CHOOSE_BUTTON_TEXT,
			handler: function() {
				var comboBoxEntity = gridPanel.selModel.getSelected();
				if (!comboBoxEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				processSuccess(comboBoxEntity);
			}
		};

		var createButton = {
			id: CREATE_BUTTON_ID,
			text: CREATE_BUTTON_TEXT,
			handler: function() {
				su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initCreateForm({
					successHandler: function(response) {
						var comboBoxEntity = Ext.decode(response);
						var id = Kefir.getId(comboBoxEntity);

						Kefir.ajaxRequest(GET_URL, { id: id, entityName: VO_CLASS }, function(comboBoxEntityVO) {
							processSuccess(comboBoxEntityVO);
						});
					}
				});
			}
		};

		var showButton = {
			id: SHOW_BUTTON_ID,
			text: SHOW_BUTTON_TEXT,
			handler: function() {
				var comboBoxEntity = gridPanel.selModel.getSelected();
				if (!comboBoxEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initShowForm({
					comboBoxEntity: comboBoxEntity
				});
			}
		};

		var cancelButton = {
			id: CANCEL_BUTTON_ID,
			text: CANCEL_BUTTON_TEXT,
			handler: function() { window.close(); }
		};

		return [ chooseButton, showButton, createButton, cancelButton ];
	}

	function updateDataStore(dataStore) {
		Ext.getCmp(CADASTRAL_NUMBER_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			processSuccess(gridPanel.selModel.getSelected());
		});
	}
	function initGridPanel(callback) {
		var initGrid = function(result) {
			dataStore = result.dataStore;
			gridPanel = result.gridPanel;

			if (callback)
				callback();
		};

		Kefir.initDynamicGrid({
			vo: VO_CLASS,
			callback: initGrid,
			dsUpdate: updateDataStore,
			dsConfig: {
				url: GRID_URL
			},
			gpUpdate: updateGridPanel,
			gpConfig: {
				id: GRID_PANEL_ID,
				tbar: getToolbar(),
				buttons: getButtons()
			}
		});
	}

	function getWindowTitle() {
		return WINDOW_TITLE;
	}
	function initWindow() {
		window = new Ext.Window({
			id: WINDOW_ID,
			title: getWindowTitle(),
			width: WINDOW_WIDTH,
			height: WINDOW_HEIGHT,
			autoScroll: true,
			constrain: true,
			modal: true,
			maximized: false,
			resizable: false,
			layout: 'fit',

			items: [ gridPanel ]
		});
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	return {
		init: function(config) {
			successHandler = config.successHandler;
			initGridPanel(showWindow);
		}
	}
}();