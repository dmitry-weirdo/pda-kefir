Ext.namespace('su.opencode.minstroy.ejb.leasing.developer');

su.opencode.minstroy.ejb.leasing.developer.DevelopersList = function() {
	var WINDOW_ID = 'developersWindow';
	var WINDOW_TITLE = 'Список застройщиков';

	var GRID_URL = '/developersListNew';
	var GRID_PANEL_ID = 'developersGrid';
	var VO_CLASS = 'su.opencode.minstroy.ejb.leasing.DeveloperVO';
	var NOT_CHOSEN_TITLE = 'Застройщик участок не выбран';
	var NOT_CHOSEN_MESSAGE = 'Выберите застройщика';

	var CREATE_BUTTON_ID = 'developersList-createButton';
	var CREATE_BUTTON_TEXT = 'Создать';
	var SHOW_BUTTON_ID = 'developersList-showButton';
	var SHOW_BUTTON_TEXT = 'Просмотр';
	var UPDATE_BUTTON_ID = 'developersList-updateButton';
	var UPDATE_BUTTON_TEXT = 'Изменить';
	var DELETE_BUTTON_ID = 'developersList-deleteButton';
	var DELETE_BUTTON_TEXT = 'Удалить';
	var EXPORT_TO_EXCEL_URL = '/developersListToExcel';
	var EXPORT_TO_EXCEL_BUTTON_ID = 'developersList-exportToExcelButton';
	var EXPORT_TO_EXCEL_BUTTON_TEXT = 'Выгрузить в Excel';
	var CLOSE_BUTTON_ID = 'developersList-closeButton';
	var CLOSE_BUTTON_TEXT = 'Выход';

	var LEASE_CONTRACTS_BUTTON_ID = 'developersList-leaseContractsButton';
	var LEASE_CONTRACTS_BUTTON_TEXT = 'Просмотреть договоры аренды';
	var RENT_PAYMENTS_BUTTON_ID = 'developersList-rentPaymentsButton';
	var RENT_PAYMENTS_BUTTON_TEXT = 'Просмотреть платежи за аренду';
	var BUILDING_PERMISSIONS_BUTTON_ID = 'developersList-buildingPermissionsButton';
	var BUILDING_PERMISSIONS_BUTTON_TEXT = 'Просмотреть разрешения на строительство';
	var BUILDINGS_BUTTON_ID = 'developersList-buildingsButton';
	var BUILDINGS_BUTTON_TEXT = 'Просмотреть строительные объекты';

	var NAME_SEARCH_FIELD_ID = 'developersGrid-nameSearchField';
	var NAME_SEARCH_FIELD_LABEL = 'Название:';

	var window;
	var dataStore;
	var gridPanel;

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, NAME_SEARCH_FIELD_LABEL, ' ', new ru.kg.gtn.form.SearchField({ id: NAME_SEARCH_FIELD_ID, paramName: 'name', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var createButton = ru.kg.gtn.form.getButton({ tbar: true }, CREATE_BUTTON_ID, CREATE_BUTTON_TEXT, function() {
			su.opencode.minstroy.ejb.leasing.developer.Developer.initCreateForm({
				successHandler: function() {
					ru.kg.gtn.reload(dataStore);
				}
			});
		});

		var showButton = ru.kg.gtn.form.getButton({ tbar: true }, SHOW_BUTTON_ID, SHOW_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.ejb.leasing.developer.Developer.initShowForm({
					developer: developer
				});
			});
		});

		var updateButton = ru.kg.gtn.form.getButton({ tbar: true }, UPDATE_BUTTON_ID, UPDATE_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.ejb.leasing.developer.Developer.initUpdateForm({
					developer: developer,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			});
		});

		var deleteButton = ru.kg.gtn.form.getButton({ tbar: true }, DELETE_BUTTON_ID, DELETE_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.ejb.leasing.developer.Developer.initDeleteForm({
					developer: developer,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			});
		});

		var exportToExcelButton = ru.kg.gtn.form.getButton({ tbar: true }, EXPORT_TO_EXCEL_BUTTON_ID, EXPORT_TO_EXCEL_BUTTON_TEXT, function() {
			var dataStoreParams = '';
			for (var param in dataStore.baseParams)
				dataStoreParams += ( '&' + param + '=' + dataStore.baseParams[param] );

			location.href = ru.kg.gtn.contextPath + EXPORT_TO_EXCEL_URL
				+ '?entityName=' + VO_CLASS
				+ '&sort=' + dataStore.sortInfo.field
				+ '&dir=' + dataStore.sortInfo.direction
				+ dataStoreParams;
		});

		var closeButton = ru.kg.gtn.form.getButton({ tbar: true }, CLOSE_BUTTON_ID, CLOSE_BUTTON_TEXT, function() {
			window.close();
		});

		return [ createButton, showButton, updateButton, deleteButton, exportToExcelButton, closeButton ];
	}
	function getSecondRowButtons() {
		var leaseContractsButton = ru.kg.gtn.form.getButton({ tbar: true }, LEASE_CONTRACTS_BUTTON_ID, LEASE_CONTRACTS_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.leasing.leaseContract.LeaseContractsList.init({
					developer: developer
				});
			});
		});

		var rentPaymentsButton = ru.kg.gtn.form.getButton({ tbar: true }, RENT_PAYMENTS_BUTTON_ID, RENT_PAYMENTS_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.leasing.rentPayment.RentPaymentsList.init({
					developer: developer
				});
			});
		});

		var buildingPermissionsButton = ru.kg.gtn.form.getButton({ tbar: true }, BUILDING_PERMISSIONS_BUTTON_ID, BUILDING_PERMISSIONS_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.building.buildingPermission.BuildingPermissionsList.init({
					developer: developer
				});
			});
		});

		var buildingsButton = ru.kg.gtn.form.getButton({ tbar: true }, BUILDINGS_BUTTON_ID, BUILDINGS_BUTTON_TEXT, function() {
			ru.kg.gtn.selectGridPanelRecord(gridPanel, NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE, function(developer) {
				su.opencode.minstroy.building.building.BuildingsList.init({
					developer: developer
				});
			});
		});

		return [ leaseContractsButton, rentPaymentsButton, buildingPermissionsButton, buildingsButton ];
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
			su.opencode.minstroy.ejb.leasing.developer.Developer.initShowForm({
				developer: gridPanel.selModel.getSelected()
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
				fbar: getFbar()
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