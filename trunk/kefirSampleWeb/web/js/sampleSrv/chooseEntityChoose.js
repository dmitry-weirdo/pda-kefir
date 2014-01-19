Ext.namespace('su.opencode.kefir.sampleSrv.chooseEntity');

su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntityChoose = function() {
	var WINDOW_ID = 'chooseEntityChooseWindow';
	var WINDOW_TITLE = 'Выбор связанной сущности';
	var WINDOW_WIDTH = 1000;
	var WINDOW_HEIGHT = 600;

	var GRID_URL = '/chooseEntitysList';
	var GET_URL = '/chooseEntityGet';
	var GRID_PANEL_ID = 'chooseEntityChooseGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ChooseEntityVO';
	var NOT_CHOSEN_TITLE = 'Связанная сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите связанную сущность';

	var CHOOSE_BUTTON_ID = 'chooseEntityChoose-chooseButton';
	var CHOOSE_BUTTON_TEXT = 'Выбрать';
	var SHOW_BUTTON_ID = 'chooseEntityChoose-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var CREATE_BUTTON_ID = 'chooseEntityChoose-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var CANCEL_BUTTON_ID = 'chooseEntityChoose-cancelButton';
	var CANCEL_BUTTON_TEXT = 'Отмена';

	var NAME_SEARCH_FIELD_ID = 'chooseEntityChooseGrid-nameSearchField';
	var NAME_SEARCH_FIELD_LABEL = 'Наименование:';

	var window;
	var dataStore;
	var gridPanel;
	var successHandler;

	function processSuccess(chooseEntity) {
		if (successHandler)
			successHandler(chooseEntity);

		window.close();
	}

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, NAME_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: NAME_SEARCH_FIELD_ID, paramName: 'name', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var chooseButton = {
			id: CHOOSE_BUTTON_ID,
			text: CHOOSE_BUTTON_TEXT,
			handler: function() {
				var chooseEntity = gridPanel.selModel.getSelected();
				if (!chooseEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				processSuccess(chooseEntity);
			}
		};

		var createButton = {
			id: CREATE_BUTTON_ID,
			text: CREATE_BUTTON_TEXT,
			handler: function() {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initCreateForm({
					successHandler: function(response) {
						var chooseEntity = Ext.decode(response);
						var id = Kefir.getId(chooseEntity);

						Kefir.ajaxRequest(GET_URL, { id: id, entityName: VO_CLASS }, function(chooseEntityVO) {
							processSuccess(chooseEntityVO);
						});
					}
				});
			}
		};

		var showButton = {
			id: SHOW_BUTTON_ID,
			text: SHOW_BUTTON_TEXT,
			handler: function() {
				var chooseEntity = gridPanel.selModel.getSelected();
				if (!chooseEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initShowForm({
					chooseEntity: chooseEntity
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
		Ext.getCmp(NAME_SEARCH_FIELD_ID).store = dataStore;
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