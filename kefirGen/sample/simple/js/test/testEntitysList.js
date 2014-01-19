Ext.namespace('test.testEntity');

test.testEntity.TestEntitysList = function() {
	var WINDOW_ID = 'testEntitysWindow';
	var WINDOW_TITLE = 'Список тестовых сущностей';

	var GRID_URL = '/testEntitysList';
	var GRID_PANEL_ID = 'testEntitysGrid';
	var VO_CLASS = 'test.TestEntityVO';
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

	var CHOOSE_FIELD_NAME_SEARCH_FIELD_ID = 'testEntitysGrid-chooseFieldNameSearchField';
	var CHOOSE_FIELD_NAME_SEARCH_FIELD_LABEL = 'Поиск по имени связанной сущности';

	var window;
	var dataStore;
	var gridPanel;

	function getToolbar() {
		return new Ext.Toolbar({
			items: [
				'->'
				, STR_FIELD_SEARCH_FIELD_LABEL, ' ', new ru.kg.gtn.form.SearchField({ id: STR_FIELD_SEARCH_FIELD_ID, paramName: 'strField', width: 250, maxLength: 200 })
				, '-'
				, CHOOSE_FIELD_NAME_SEARCH_FIELD_LABEL, ' ', new ru.kg.gtn.form.SearchField({ id: CHOOSE_FIELD_NAME_SEARCH_FIELD_ID, paramName: 'chooseFieldName', width: 250, maxLength: 255 })
			]
		});
	}

	function getButtons() {
		var createButton = {
			id: CREATE_BUTTON_ID,
			text: CREATE_BUTTON_TEXT,
			handler: function() {
				test.testEntity.TestEntity.initCreateForm({
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
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

				test.testEntity.TestEntity.initShowForm({
					testEntity: testEntity
				});
			}
		};

		var updateButton = {
			id: UPDATE_BUTTON_ID,
			text: UPDATE_BUTTON_TEXT,
			handler: function() {
				var testEntity = gridPanel.selModel.getSelected();
				if (!testEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				test.testEntity.TestEntity.initUpdateForm({
					testEntity: testEntity,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			}
		};

		var deleteButton = {
			id: DELETE_BUTTON_ID,
			text: DELETE_BUTTON_TEXT,
			handler: function() {
				var testEntity = gridPanel.selModel.getSelected();
				if (!testEntity)
				{
					Ext.Msg.alert(NOT_CHOSEN_TITLE, NOT_CHOSEN_MESSAGE);
					return;
				}

				test.testEntity.TestEntity.initDeleteForm({
					testEntity: testEntity,
					successHandler: function() {
						ru.kg.gtn.reload(dataStore);
					}
				});
			}
		};

		var exportToExcelButton = {
			id: EXPORT_TO_EXCEL_BUTTON_ID,
			text: EXPORT_TO_EXCEL_BUTTON_TEXT,
			handler: function() {
				// fill data store params as url "&param=value" pairs
				var dataStoreParams = '';
		 		for (var param in dataStore.baseParams)
					dataStoreParams += ( '&' + param + '=' + dataStore.baseParams[param] );

				location.href = ru.kg.gtn.contextPath + EXPORT_TO_EXCEL_URL
					+ '?entityName=' + VO_CLASS
					+ '&sort=' + dataStore.sortInfo.field
					+ '&dir=' + dataStore.sortInfo.direction
					+	dataStoreParams;
			}
		};

		var closeButton = {
			id: CLOSE_BUTTON_ID,
			text: CLOSE_BUTTON_TEXT,
			handler: function() { window.close(); }
		};

		return [ createButton, showButton, updateButton, deleteButton, exportToExcelButton, closeButton ];
	}

	function updateDataStore(dataStore) {
		Ext.getCmp(STR_FIELD_SEARCH_FIELD_ID).store = dataStore;
		Ext.getCmp(CHOOSE_FIELD_NAME_SEARCH_FIELD_ID).store = dataStore;
	}
	function updateGridPanel(gridPanel) {
		gridPanel.on('rowdblclick', function(gridPanel) {
			test.testEntity.TestEntity.initShowForm({
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