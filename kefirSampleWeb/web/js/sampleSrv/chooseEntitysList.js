Ext.namespace('su.opencode.kefir.sampleSrv.chooseEntity');

su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntitysList = function() {
	var WINDOW_ID = 'chooseEntitysWindow';
	var WINDOW_TITLE = 'Список связанных сущностей';

	var GRID_URL = '/chooseEntitysList';
	var GRID_PANEL_ID = 'chooseEntitysGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ChooseEntityVO';
	var NOT_CHOSEN_TITLE = 'Связанная сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите связанную сущность';

	var CREATE_BUTTON_ID = 'chooseEntitysList-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var SHOW_BUTTON_ID = 'chooseEntitysList-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var UPDATE_BUTTON_ID = 'chooseEntitysList-updateButton';
	var UPDATE_BUTTON_TEXT = 'Изменить';
	var DELETE_BUTTON_ID = 'chooseEntitysList-deleteButton';
	var DELETE_BUTTON_TEXT = 'Удалить';
	var EXPORT_TO_EXCEL_URL = '/chooseEntitysListToExcel';
	var EXPORT_TO_EXCEL_BUTTON_ID = 'chooseEntitysList-exportToExcelButton';
	var EXPORT_TO_EXCEL_BUTTON_TEXT = 'Выгрузить в Excel';
	var CLOSE_BUTTON_ID = 'chooseEntitysList-closeButton';
	var CLOSE_BUTTON_TEXT = 'Выход';

	var TEST_ENTITYS_BUTTON_ID = 'chooseEntitysList-testEntitysButton';
	var TEST_ENTITYS_BUTTON_TEXT = 'Перейти к списку тестовых сущностей';

	var NAME_SEARCH_FIELD_ID = 'chooseEntitysGrid-nameSearchField';
	var NAME_SEARCH_FIELD_LABEL = 'Наименование:';

	var window;
	var dataStore;
	var gridPanel;

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, NAME_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: NAME_SEARCH_FIELD_ID, paramName: 'name', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var createButton = Kefir.form.getButton({ tbar: true }, CREATE_BUTTON_ID, CREATE_BUTTON_TEXT, function() {
			su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initCreateForm({
				successHandler: function() {
					Kefir.reload(dataStore);
				}
			});
		});

		var showButton = Kefir.form.getButton({ tbar: true }, SHOW_BUTTON_ID, SHOW_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(chooseEntity) {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initShowForm({
					chooseEntity: chooseEntity
				});
			});
		});

		var updateButton = Kefir.form.getButton({ tbar: true }, UPDATE_BUTTON_ID, UPDATE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(chooseEntity) {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initUpdateForm({
					chooseEntity: chooseEntity,
					successHandler: function() {
						Kefir.reload(dataStore);
					}
				});
			});
		});

		var deleteButton = Kefir.form.getButton({ tbar: true }, DELETE_BUTTON_ID, DELETE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(chooseEntity) {
				su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initDeleteForm({
					chooseEntity: chooseEntity,
					successHandler: function() {
						Kefir.reload(dataStore);
					}
				});
			});
		});

		var exportToExcelButton = Kefir.form.getButton({ tbar: true }, EXPORT_TO_EXCEL_BUTTON_ID, EXPORT_TO_EXCEL_BUTTON_TEXT, function() {
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

		var closeButton = Kefir.form.getButton({ tbar: true }, CLOSE_BUTTON_ID, CLOSE_BUTTON_TEXT, function() {
			window.close();
		});

		return [ createButton, showButton, updateButton, deleteButton, exportToExcelButton, closeButton ];
	}
	function getSecondRowButtons() {
		var testEntitysButton = Kefir.form.getButton({ tbar: true }, TEST_ENTITYS_BUTTON_ID, TEST_ENTITYS_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(chooseEntity) {
				su.opencode.kefir.sampleSrv.testEntity.TestEntitysList.init({
					chooseEntity: chooseEntity
				});
			});
		});

		return [ testEntitysButton ];
	}
	function getFbar() {
		return new Ext.Panel({
			items: [
				{ xtype: 'toolbar', items: [ '->', getSecondRowButtons() ] },
				{ xtype: 'toolbar', items: [ '->', getButtons() ] }
		 	]
		});
	}

	function updateDataStore(dataStore) {
		Ext.getCmp(NAME_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntity.initShowForm({
				chooseEntity: gridPanel.selModel.getSelected()
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
				url: GRID_URL
			},
			gpUpdate: updateGridPanel,
			gpConfig: {
				id: GRID_PANEL_ID,
				tbar: getToolbar(),
				fbar: getFbar()
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
			initGridPanel(showWindow);
		}
	}
}();