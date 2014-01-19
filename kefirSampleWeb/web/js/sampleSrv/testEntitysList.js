Ext.namespace('su.opencode.kefir.sampleSrv.testEntity');

su.opencode.kefir.sampleSrv.testEntity.TestEntitysList = function() {
	var WINDOW_ID = 'testEntitysWindow';
	var WINDOW_TITLE = 'Список тестовых сущностей';

	// filter constants
	var COMBO_BOX_ENTITY_FILTER_WINDOW_TITLE = 'комбо сущность';
	var CHOOSE_ENTITY_FILTER_WINDOW_TITLE = 'связанная сущность';

	var GRID_URL = '/testEntitysList';
	var GRID_PANEL_ID = 'testEntitysGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.TestEntityVO';
	var NOT_CHOSEN_TITLE = 'Тестовая сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите тестовую сущность';

	var CREATE_BUTTON_ID = 'testEntitysList-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var SHOW_BUTTON_ID = 'testEntitysList-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var UPDATE_BUTTON_ID = 'testEntitysList-updateButton';
	var UPDATE_BUTTON_TEXT = 'Изменить';
	var DELETE_BUTTON_ID = 'testEntitysList-deleteButton';
	var DELETE_BUTTON_TEXT = 'Удалить';
	var EXPORT_TO_EXCEL_URL = '/testEntitysListToExcel';
	var EXPORT_TO_EXCEL_BUTTON_ID = 'testEntitysList-exportToExcelButton';
	var EXPORT_TO_EXCEL_BUTTON_TEXT = 'Выгрузить в Excel';
	var CLOSE_BUTTON_ID = 'testEntitysList-closeButton';
	var CLOSE_BUTTON_TEXT = 'Выход';

	var STR_FIELD_SEARCH_FIELD_ID = 'testEntitysGrid-strFieldSearchField';
	var STR_FIELD_SEARCH_FIELD_LABEL = 'Поиск по строковому полю';

	var CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID = 'testEntitysGrid-chooseEntityNameSearchField';
	var CHOOSE_ENTITY_NAME_SEARCH_FIELD_LABEL = 'Поиск по имени связанной сущности';

	var window;
	var dataStore;
	var gridPanel;

	// filter params
	var comboBoxEntity;
	var chooseEntity;

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
		var createButton = Kefir.form.getButton({}, CREATE_BUTTON_ID, CREATE_BUTTON_TEXT, function() {
			su.opencode.kefir.sampleSrv.testEntity.TestEntity.initCreateForm({
				comboBoxEntity: comboBoxEntity,
				chooseEntity: chooseEntity,
				successHandler: function() {
					Kefir.reload(dataStore);
				}
			});
		});

		var showButton = Kefir.form.getButton({}, SHOW_BUTTON_ID, SHOW_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {
				su.opencode.kefir.sampleSrv.testEntity.TestEntity.initShowForm({
					testEntity: testEntity
				});
			});
		});

		var updateButton = Kefir.form.getButton({}, UPDATE_BUTTON_ID, UPDATE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {
				su.opencode.kefir.sampleSrv.testEntity.TestEntity.initUpdateForm({
					testEntity: testEntity,
					comboBoxEntity: comboBoxEntity,
					chooseEntity: chooseEntity,
					successHandler: function() {
						Kefir.reload(dataStore);
					}
				});
			});
		});

		var deleteButton = Kefir.form.getButton({}, DELETE_BUTTON_ID, DELETE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(testEntity) {
				su.opencode.kefir.sampleSrv.testEntity.TestEntity.initDeleteForm({
					testEntity: testEntity,
					successHandler: function() {
						Kefir.reload(dataStore);
					}
				});
			});
		});

		var exportToExcelButton = Kefir.form.getButton({}, EXPORT_TO_EXCEL_BUTTON_ID, EXPORT_TO_EXCEL_BUTTON_TEXT, function() {
			var dataStoreParams = '';
			for (var param in dataStore.baseParams)
				if (dataStore.baseParams[param])
					dataStoreParams += ( '&' + param + '=' + dataStore.baseParams[param] );

			location.href = Kefir.contextPath + EXPORT_TO_EXCEL_URL
				+ '?entityName=' + VO_CLASS
				+ '&sort=' + dataStore.sortInfo.field
				+ '&dir=' + dataStore.sortInfo.direction
				+ dataStoreParams;
		});

		var closeButton = Kefir.form.getButton({}, CLOSE_BUTTON_ID, CLOSE_BUTTON_TEXT, function() {
			window.close();
		});

		return [ createButton, showButton, updateButton, deleteButton, exportToExcelButton, closeButton ];
	}

	function updateDataStore(dataStore) {
		Ext.getCmp(STR_FIELD_SEARCH_FIELD_ID).store = dataStore;
		Ext.getCmp(CHOOSE_ENTITY_NAME_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			su.opencode.kefir.sampleSrv.testEntity.TestEntity.initShowForm({
				testEntity: gridPanel.selModel.getSelected()
			});
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
			autoScroll: true,
			constrain: true,
			modal: true,
			maximized: true,
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
			comboBoxEntity = config.comboBoxEntity;
			chooseEntity = config.chooseEntity;

			initGridPanel(showWindow);
		}
	}
}();