Ext.namespace('su.opencode.minstroy.ejb.leasing.developer');

su.opencode.minstroy.ejb.leasing.developer.DeveloperChoose = function() {
	var WINDOW_ID = 'developerChooseWindow';
	var WINDOW_TITLE = 'Выбор застройщика';
	var WINDOW_WIDTH = 1000;
	var WINDOW_HEIGHT = 600;

	var GRID_URL = '/developersListNew';
	var GET_URL = '/developerGetNew';
	var GRID_PANEL_ID = 'developerChooseGrid';
	var VO_CLASS = 'su.opencode.minstroy.ejb.leasing.DeveloperVO';
	var NOT_CHOSEN_TITLE = 'Застройщик участок не выбран';
	var NOT_CHOSEN_MESSAGE = 'Выберите застройщика';

	var CHOOSE_BUTTON_ID = 'developerChoose-chooseButton';
	var CHOOSE_BUTTON_TEXT = 'Выбрать';
	var SHOW_BUTTON_ID = 'developerChoose-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var CREATE_BUTTON_ID = 'developerChoose-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var CANCEL_BUTTON_ID = 'developerChoose-cancelButton';
	var CANCEL_BUTTON_TEXT = 'Отмена';

	var NAME_SEARCH_FIELD_ID = 'developerChooseGrid-nameSearchField';
	var NAME_SEARCH_FIELD_LABEL = 'Название:';

	var window;
	var dataStore;
	var gridPanel;
	var successHandler;

	function processSuccess(developer) {
		if (successHandler)
			successHandler(developer);

		window.close();
	}

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, NAME_SEARCH_FIELD_LABEL, ' ', new ru.kg.gtn.form.SearchField({ id: NAME_SEARCH_FIELD_ID, paramName: 'name', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var chooseButton = {
			id: CHOOSE_BUTTON_ID,
			text: CHOOSE_BUTTON_TEXT,
			handler: function() {
				var developer = gridPanel.selModel.getSelected();
				if (!developer)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				processSuccess(developer);
			}
		};

		var createButton = {
			id: CREATE_BUTTON_ID,
			text: CREATE_BUTTON_TEXT,
			handler: function() {
				su.opencode.minstroy.ejb.leasing.developer.Developer.initCreateForm({
					successHandler: function(response) {
						var developer = Ext.util.JSON.decode(response);
						var id = ru.kg.gtn.getId(developer);

						ru.kg.gtn.ajaxRequest(GET_URL, { id: id, entityName: VO_CLASS }, function(developerVO) {
							processSuccess(developerVO);
						});
					}
				});
			}
		};

		var showButton = {
			id: SHOW_BUTTON_ID,
			text: SHOW_BUTTON_TEXT,
			handler: function() {
				var developer = gridPanel.selModel.getSelected();
				if (!developer)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				su.opencode.minstroy.ejb.leasing.developer.Developer.initShowForm({
					developer: developer
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
		}

		ru.kg.gtn.initDynamicGrid({
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

	function initWindow() {
		window = new Ext.Window({
			id: WINDOW_ID,
			title: WINDOW_TITLE,
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