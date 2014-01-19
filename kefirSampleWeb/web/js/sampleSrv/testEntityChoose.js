Ext.namespace('su.opencode.kefir.sampleSrv.testEntity');

su.opencode.kefir.sampleSrv.testEntity.TestEntityChoose = function() {
	var WINDOW_ID = 'testEntityChooseWindow';
	var WINDOW_TITLE = 'Выбор тестовой сущности';
	var WINDOW_WIDTH = 1000;
	var WINDOW_HEIGHT = 600;

	// filter constants
	var COMBO_BOX_ENTITY_FILTER_WINDOW_TITLE = 'комбо сущность';
	var CHOOSE_ENTITY_FILTER_WINDOW_TITLE = 'связанная сущность';

	var GRID_URL = '/testEntitysList';
	var GET_URL = '/testEntityGet';
	var GRID_PANEL_ID = 'testEntityChooseGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.TestEntityVO';
	var NOT_CHOSEN_TITLE = 'Тестовая сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите тестовую сущность';

	var CHOOSE_BUTTON_ID = 'testEntityChoose-chooseButton';
	var CHOOSE_BUTTON_TEXT = 'Выбрать';
	var SHOW_BUTTON_ID = 'testEntityChoose-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var CREATE_BUTTON_ID = 'testEntityChoose-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var CANCEL_BUTTON_ID = 'testEntityChoose-cancelButton';
	var CANCEL_BUTTON_TEXT = 'Отмена';

	var STR_FIELD_SEARCH_FIELD_ID = 'testEntityChooseGrid-strFieldSearchField';
	var STR_FIELD_SEARCH_FIELD_LABEL = 'Поиск по строковому полю';

	var CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID = 'testEntityChooseGrid-chooseEntityNameSearchField';
	var CHOOSE_ENTITY_NAME_SEARCH_FIELD_LABEL = 'Поиск по имени связанной сущности';

	var window;
	var dataStore;
	var gridPanel;
	var successHandler;

	// filter params
	var comboBoxEntity;
	var chooseEntity;

	function processSuccess(testEntity) {
		if (successHandler)
			successHandler(testEntity);

		window.close();
	}

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, STR_FIELD_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: STR_FIELD_SEARCH_FIELD_ID, paramName: 'strField', width: 250, maxLength: 200 })
				, '-'
				, CHOOSE_ENTITY_NAME_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID, paramName: 'chooseEntityName', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var chooseButton = {
			id: CHOOSE_BUTTON_ID,
			text: CHOOSE_BUTTON_TEXT,
			handler: function() {
				var testEntity = gridPanel.selModel.getSelected();
				if (!testEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				processSuccess(testEntity);
			}
		};

		var createButton = {
			id: CREATE_BUTTON_ID,
			text: CREATE_BUTTON_TEXT,
			handler: function() {
				su.opencode.kefir.sampleSrv.testEntity.TestEntity.initCreateForm({
					comboBoxEntity: comboBoxEntity,
					chooseEntity: chooseEntity,
					successHandler: function(response) {
						var testEntity = Ext.decode(response);
						var id = Kefir.getId(testEntity);

						Kefir.ajaxRequest(GET_URL, { id: id, entityName: VO_CLASS }, function(testEntityVO) {
							processSuccess(testEntityVO);
						});
					}
				});
			}
		};

		var showButton = {
			id: SHOW_BUTTON_ID,
			text: SHOW_BUTTON_TEXT,
			handler: function() {
				var testEntity = gridPanel.selModel.getSelected();
				if (!testEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				su.opencode.kefir.sampleSrv.testEntity.TestEntity.initShowForm({
					testEntity: testEntity
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
		Ext.getCmp(STR_FIELD_SEARCH_FIELD_ID).store = dataStore;
		Ext.getCmp(CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID).store = dataStore;
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
				url: GRID_URL,
				baseParams: {
					comboBoxEntityId: Kefir.getId(comboBoxEntity),
					chooseEntityId: Kefir.getId(chooseEntity)
				}
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
		var windowTitle = '';

		if (comboBoxEntity)
			windowTitle += ( COMBO_BOX_ENTITY_FILTER_WINDOW_TITLE + ': ' + Kefir.getValue(comboBoxEntity, 'cadastralNumber') + Kefir.form.WINDOW_TITLE_FILTERS_SEPARATOR );

		if (chooseEntity)
			windowTitle += ( CHOOSE_ENTITY_FILTER_WINDOW_TITLE + ': ' + Kefir.getValue(chooseEntity, 'name') + Kefir.form.WINDOW_TITLE_FILTERS_SEPARATOR );

		if (!windowTitle)
			return WINDOW_TITLE;

		return WINDOW_TITLE + ' (' + windowTitle.substring(0, windowTitle.length - Kefir.form.WINDOW_TITLE_FILTERS_SEPARATOR.length) + ')';
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
			comboBoxEntity = config.comboBoxEntity;
			chooseEntity = config.chooseEntity;

			initGridPanel(showWindow);
		}
	}
}();