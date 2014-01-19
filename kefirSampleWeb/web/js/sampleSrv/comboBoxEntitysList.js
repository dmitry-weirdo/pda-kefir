Ext.namespace('su.opencode.kefir.sampleSrv.comboBoxEntity');

su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntitysList = function() {
	var WINDOW_ID = 'comboBoxEntitysWindow';
	var WINDOW_TITLE = 'Список комбо сущностей';

	var GRID_URL = '/comboBoxEntitysList';
	var GRID_PANEL_ID = 'comboBoxEntitysGrid';
	var VO_CLASS = 'su.opencode.kefir.sampleSrv.ComboBoxEntityVO';
	var NOT_CHOSEN_TITLE = 'Комбо сущность не выбрана';
	var NOT_CHOSEN_MESSAGE = 'Выберите комбо сущность';

	var CREATE_BUTTON_ID = 'comboBoxEntitysList-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var SHOW_BUTTON_ID = 'comboBoxEntitysList-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var UPDATE_BUTTON_ID = 'comboBoxEntitysList-updateButton';
	var UPDATE_BUTTON_TEXT = 'Изменить';
	var DELETE_BUTTON_ID = 'comboBoxEntitysList-deleteButton';
	var DELETE_BUTTON_TEXT = 'Удалить';
	var EXPORT_TO_EXCEL_URL = '/comboBoxEntitysListToExcel';
	var EXPORT_TO_EXCEL_BUTTON_ID = 'comboBoxEntitysList-exportToExcelButton';
	var EXPORT_TO_EXCEL_BUTTON_TEXT = 'Выгрузить в Excel';
	var CLOSE_BUTTON_ID = 'comboBoxEntitysList-closeButton';
	var CLOSE_BUTTON_TEXT = 'Выход';

	var TEST_ENTITYS_BUTTON_ID = 'comboBoxEntitysList-testEntitysButton';
	var TEST_ENTITYS_BUTTON_TEXT = 'Перейти к списку тестовых сущностей';

	var CADASTRAL_NUMBER_SEARCH_FIELD_ID = 'comboBoxEntitysGrid-cadastralNumberSearchField';
	var CADASTRAL_NUMBER_SEARCH_FIELD_LABEL = 'Кадастровый номер:';

	var window;
	var dataStore;
	var gridPanel;

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, CADASTRAL_NUMBER_SEARCH_FIELD_LABEL, ' ', new Kefir.form.SearchField({ id: CADASTRAL_NUMBER_SEARCH_FIELD_ID, paramName: 'cadastralNumber', width: 250, maxLength: 25 })
			]
		});
	}

	function getButtons() {
		var createButton = Kefir.form.getButton({ tbar: true }, CREATE_BUTTON_ID, CREATE_BUTTON_TEXT, function() {
			su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initCreateForm({
				successHandler: function() {
					Kefir.reload(dataStore);
				}
			});
		});

		var showButton = Kefir.form.getButton({ tbar: true }, SHOW_BUTTON_ID, SHOW_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(comboBoxEntity) {
				su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initShowForm({
					comboBoxEntity: comboBoxEntity
				});
			});
		});

		var updateButton = Kefir.form.getButton({ tbar: true }, UPDATE_BUTTON_ID, UPDATE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(comboBoxEntity) {
				su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initUpdateForm({
					comboBoxEntity: comboBoxEntity,
					successHandler: function() {
						Kefir.reload(dataStore);
					}
				});
			});
		});

		var deleteButton = Kefir.form.getButton({ tbar: true }, DELETE_BUTTON_ID, DELETE_BUTTON_TEXT, function() {
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(comboBoxEntity) {
				su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initDeleteForm({
					comboBoxEntity: comboBoxEntity,
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
			Kefir.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(comboBoxEntity) {
				su.opencode.kefir.sampleSrv.testEntity.TestEntitysList.init({
					comboBoxEntity: comboBoxEntity
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
		Ext.getCmp(CADASTRAL_NUMBER_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntity.initShowForm({
				comboBoxEntity: gridPanel.selModel.getSelected()
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