Ext.namespace('su.opencode.minstroy.ejb.leasing.parcel');

su.opencode.minstroy.ejb.leasing.parcel.ParcelsList = function() {
	var WINDOW_ID = 'parcelsWindow';
	var WINDOW_TITLE = 'Список земельных участков';

	var GRID_URL = '/parcelsListNew';
	var GRID_PANEL_ID = 'parcelsGrid';
	var VO_CLASS = 'su.opencode.minstroy.ejb.leasing.ParcelVO';
	var NOT_CHOSEN_TITLE = 'Земельный участок не выбран';
	var NOT_CHOSEN_MESSAGE = 'Выберите земельный участок';

	var CREATE_BUTTON_ID = 'parcelsList-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var SHOW_BUTTON_ID = 'parcelsList-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var UPDATE_BUTTON_ID = 'parcelsList-updateButton';
	var UPDATE_BUTTON_TEXT = 'Изменить';
	var DELETE_BUTTON_ID = 'parcelsList-deleteButton';
	var DELETE_BUTTON_TEXT = 'Удалить';
	var EXPORT_TO_EXCEL_URL = '/parcelsListToExcel';
	var EXPORT_TO_EXCEL_BUTTON_ID = 'parcelsList-exportToExcelButton';
	var EXPORT_TO_EXCEL_BUTTON_TEXT = 'Выгрузить в Excel';
	var CLOSE_BUTTON_ID = 'parcelsList-closeButton';
	var CLOSE_BUTTON_TEXT = 'Выход';

	var CADASTRAL_NUMBER_SEARCH_FIELD_ID = 'parcelsGrid-cadastralNumberSearchField';
	var CADASTRAL_NUMBER_SEARCH_FIELD_LABEL = 'Кадастровый номер';

	var window;
	var dataStore;
	var gridPanel;

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, CADASTRAL_NUMBER_SEARCH_FIELD_LABEL, ' ', new ru.kg.gtn.form.SearchField({ id: CADASTRAL_NUMBER_SEARCH_FIELD_ID, paramName: 'cadastralNumber', width: 250, maxLength: 25 })
			]
		});
	}

	function getButtons() {
		var createButton = ru.kg.gtn.form.getButton({}, CREATE_BUTTON_ID, CREATE_BUTTON_TEXT, function() {
			su.opencode.minstroy.ejb.leasing.parcel.Parcel.initCreateForm({
				successHandler: function() {
					ru.kg.gtn.reload(dataStore);
				}
			});
		});

		var showButton = ru.kg.gtn.form.getButton({}, SHOW_BUTTON_ID, SHOW_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(parcel) {
				su.opencode.minstroy.ejb.leasing.parcel.Parcel.initShowForm({
					parcel: parcel
				});
			});
		});

		var updateButton = ru.kg.gtn.form.getButton({}, UPDATE_BUTTON_ID, UPDATE_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(parcel) {
				su.opencode.minstroy.ejb.leasing.parcel.Parcel.initUpdateForm({
					parcel: parcel,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			});
		});

		var deleteButton = ru.kg.gtn.form.getButton({}, DELETE_BUTTON_ID, DELETE_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(parcel) {
				su.opencode.minstroy.ejb.leasing.parcel.Parcel.initDeleteForm({
					parcel: parcel,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			});
		});

		var exportToExcelButton = ru.kg.gtn.form.getButton({}, EXPORT_TO_EXCEL_BUTTON_ID, EXPORT_TO_EXCEL_BUTTON_TEXT, function() {
			var dataStoreParams = '';
			for (var param in dataStore.baseParams)
				dataStoreParams += ( '&' + param + '=' + dataStore.baseParams[param] );

			location.href = ru.kg.gtn.contextPath + EXPORT_TO_EXCEL_URL
				+ '?entityName=' + VO_CLASS
				+ '&sort=' + dataStore.sortInfo.field
				+ '&dir=' + dataStore.sortInfo.direction
				+	dataStoreParams;
		});

		var closeButton = ru.kg.gtn.form.getButton({}, CLOSE_BUTTON_ID, CLOSE_BUTTON_TEXT, function() {
			window.close();
		});

		return [ createButton, showButton, updateButton, deleteButton, exportToExcelButton, closeButton ];
	}

	function updateDataStore(dataStore) {
		Ext.getCmp(CADASTRAL_NUMBER_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			su.opencode.minstroy.ejb.leasing.parcel.Parcel.initShowForm({
				parcel: gridPanel.selModel.getSelected()
			});
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
			autoScroll: true,
			constrain: true,
			modal: true,
			maximized: true,
			resizable: false,
			layout: 'fit',

			items: [ gridPanel ]
		});

		window.on('close', function() { ru.kg.gtn.showMainMenu(); });
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	return {
		init: function() {
			initGridPanel(showWindow);
		}
	}
}();