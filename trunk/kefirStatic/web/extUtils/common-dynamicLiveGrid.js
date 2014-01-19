Kefir.BUFFER_SIZE = 200;
Kefir.NEAR_LIMIT = 100;

Kefir.setGridParam = '/setGridParam';
Kefir.getGridParams = '/getGridParams';
Kefir.blockColumnMove = false;
var CLEAR_SORT_BUTTON_ID = 'ClearSortButton';
var DEFAULT_TOOLBAR_COUNT = 4;

var GRID_SET_COLUMN_HIDDEN = 0;
var GRID_SET_COLUMN_WIDTH = 1;
var GRID_CHANGE_COLUMNS_ORDER = 2;
var GRID_SORT = 3;
var GRID_DEFAULT_SORT = 4;

Kefir.LiveJsonReader = Ext.extend(Ext.ux.grid.livegrid.JsonReader, {
	constructor: function(config) {
		Ext.apply(config, {
			root: 'results',
			totalProperty: 'total',
			//	versionProperty: 'version',
			id: config.id || 'id'
		});

		var args = [ config, config.fields ]; // Ext.ux.grid.livegrid.JsonReader accepts 2 params, config and fields
		Kefir.LiveJsonReader.superclass.constructor.apply(this, args);
	}
});

Kefir.LiveGridView = Ext.extend(Ext.ux.grid.livegrid.GridView, {
	constructor: function(config) {
		Ext.apply(this, {
			nearLimit: Kefir.NEAR_LIMIT,
			loadMask: { msg: 'Загрузка данных. Пожалуйста, подождите...' },

			// for save scroll state after store is reloaded (works only in FF)
			listeners: {
				beforerefresh: function(v) {
					v.scrollTop = v.lastScrollPos; // save scrollTop
				},

				refresh: function(v) {
					v.adjustScrollerPos(v.scrollTop, true); // restore scrollTop (does not work in ie because scrollTop is not working
				}
			}
		});

		Kefir.LiveGridView.superclass.constructor.apply(this, arguments);
	}
});

Kefir.DynamicLiveStore = Ext.extend(Ext.ux.grid.livegrid.Store, {
	constructor: function(config) {
		Ext.apply(this, {
			id: config.id || 'dynamicLiveStore',
			url: config.url,
			method: 'POST',
			remoteSort: true,
			bufferSize: Kefir.BUFFER_SIZE,
			sortInfo: config.sortInfo || { field: 'name', direction: 'asc' },

			proxy: new Ext.data.HttpProxy(
				new Ext.data.Connection({
					url: config.url,
					timeout: config.timeout || 30000
			})),
			selectionsProxy: new Ext.data.HttpProxy(
				new Ext.data.Connection({
					url: config.url,
					timeout: config.timeout || 30000
			})),
			listeners: {
				exception: function (dataProxy, type, action, options, response) {
					Kefir.showAjaxResponseMessage(this.url, response);
				}
			}
		});

		Kefir.DynamicLiveStore.superclass.constructor.apply(this, arguments);
	}
});

Kefir.DynamicLiveGridPanel = Ext.extend(Ext.ux.grid.livegrid.GridPanel, {
	constructor: function(config) {
		Ext.apply(config, {
			enableDragDrop: false,
			loadMask: { msg: 'Загрузка данных. Пожалуйста, подождите...' },
			selModel: config.selModel ? config.selModel : new Ext.ux.grid.livegrid.RowSelectionModel({ singleSelect: true })
		});

		Kefir.DynamicLiveGridPanel.superclass.constructor.apply(this, arguments);
	}
});

function columnResizeHandler(columnIndex, newSize) {
	Kefir.showMask();

	var params = {
		methodType: GRID_SET_COLUMN_WIDTH,
		entityName: this.entityName,
		columnName: this.colModel.columns[columnIndex].dataIndex,
		columnIndex: columnIndex,
		newWidth: newSize
	};
	Kefir.ajaxRequest(Kefir.setGridParam, params, function() { Kefir.hideMask(); });
}

Kefir.getDynamicLiveGridPanel = function(config, entityName, columns, dataStore) {
	function doSort() {
		var s = '';
		var f = '';

		Ext.each(getSorters(), function(item) {
			f = f.concat(item.field, ',');
			s = s.concat(item.direction, ',');
		});

		f = f.substring(0, f.length - 1);
		s = s.substring(0, s.length - 1);

		dataStore.sortInfo = { field: f, direction: s };
		dataStore.reload();
	}

	function changeSortDirection(button, changeDirection) {
		var sortData = button.sortData;
		var iconCls = button.iconCls;

		if (sortData != undefined)
		{
			if (changeDirection !== false)
			{
				var newDirection = button.sortData.direction.toggle("ASC", "DESC");
				button.sortData.direction = newDirection;
				button.setIconClass(iconCls.toggle("sort-asc", "sort-desc"));

				saveSort(sortData.field, null, newDirection, false);
			}

			dataStore.clearFilter();
			doSort();
		}
	}

	function getSorters() {
		var sorters = [];
		Ext.each(sortBar.findByType('button'), function(button) {
			if (button.id.indexOf(CLEAR_SORT_BUTTON_ID) < 0)
				sorters.push(button.sortData);
		}, this);

		return sorters;
	}

	function createSorterButton(config) {
		config = config || {};
		Ext.applyIf(config, {
			listeners: {
				click: function(button) {
					changeSortDirection(button, true);
				}
			},
			iconCls: 'sort-' + config.sortData.direction.toLowerCase(),
			reorderable: true
		});

		return new Ext.Button(config);
	}

	function saveSort(columnName, order, direction, clear) {
		var params = {
			methodType: GRID_SORT,
			entityName: entityName,
			columnName: columnName,
			sortOrder: order,
			sortDir: direction,
			clear: clear
		};
		Kefir.ajaxRequest(Kefir.setGridParam, params, null);
	}

	function saveDefaultSort(columnName, direction) {
		var params = {
			methodType: GRID_DEFAULT_SORT,
			entityName: entityName,
			columnName: columnName,
			sortDir: direction
		};
		Kefir.ajaxRequest(Kefir.setGridParam, params, null);
	}

	function getHeader(columns, fields) {
		for (var i = 0; i < columns.length; i++)
		{
			var column = columns[i];
			if (column.dataIndex == fields)
				return column.header;
		}

		return null;
	}

	function getSortLength(items) {
		return items.length - DEFAULT_TOOLBAR_COUNT;
	}

	function clearSort() {
		var length = getSortLength(tbarItems);
		for (var i = 0; i < length; i++)
			sortBar.remove(tbarItems[DEFAULT_TOOLBAR_COUNT]);
	}

	function createSort(sortInfo) {
		var fields = sortInfo.field.split(',');
		var directions = sortInfo.direction.split(',');

		for (var i = 0; i < fields.length; i++)
			droppable.addItem(fields[i], getHeader(columns, fields[i]), directions[i]);

		sortBar.doLayout();
	}

	var reorderer = new Ext.ux.ToolbarReorderer({
		onMovedLeft: function(item, newIndex, oldIndex) {
			var tbar = this.target,
				items = tbar.items.items;

			if (newIndex != undefined && newIndex != oldIndex)
			{
				var column = item.sortData;
				saveSort(column.field, newIndex - DEFAULT_TOOLBAR_COUNT, column.direction, false);
				
				//move the button currently under drag to its new location
				tbar.remove(item, false);
				tbar.insert(newIndex, item);

				//set the correct x location of each item in the toolbar
				this.updateButtonXCache();
				for (var index = 0; index < items.length; index++)
				{
					var obj = items[index],
						newX = this.buttonXCache[obj.id];

					if (item == obj)
					{
						item.dd.startPosition[0] = newX;
					}
					else
					{
						var el = obj.getEl();

						el.moveTo(newX, el.getY(), {duration: this.animationDuration});
					}
				}
			}
		}
	});

	var droppable = new Ext.ux.ToolbarDroppable({
		afterLayout: doSort,
		addItem: function(field, header, direction) {
			sortBar.add(createSorterButton({
				text: header,
				sortData: {
					field: field,
					direction: direction
				}
			}));
		},

		notifyDrop: function(dragSource, event, data) {
			var canAdd = this.canDrop(dragSource, event, data),
			tbar = this.toolbar;

			if (canAdd) {
				var entryIndex = this.calculateEntryIndex(event);

				if (entryIndex <= DEFAULT_TOOLBAR_COUNT)
					entryIndex =  DEFAULT_TOOLBAR_COUNT;

				tbar.insert(entryIndex, this.createItem(data, entryIndex));
				tbar.doLayout();
	
				this.afterLayout();
			}

			return canAdd;
		},

		createItem: function(data, entryIndex) {
			var column = this.getColumnFromDragDrop(data);
			var askDirection = 'ASC';
			var columnName = column.dataIndex;

			saveSort(columnName, entryIndex - DEFAULT_TOOLBAR_COUNT, askDirection, false);

			return createSorterButton({
				text: column.header,
				sortData: {
					field: columnName,
					direction: askDirection
				}
			});
		},

		canDrop: function(dragSource, event, data) {
			var sorters = getSorters();
			var column = this.getColumnFromDragDrop(data);

			if (!column.sortable)
				return false;

			for (var i = 0; i < sorters.length; i++)
				if (sorters[i].field == column.dataIndex)
					return false;

			return true;
		},

		getColumnFromDragDrop: function(data) {
			var index = data.header.cellIndex;
			var colModel = grid.colModel;

			return colModel.getColumnById(colModel.getColumnId(index));
		}
	});

	function getClearSortButton() {
		var defaultSortInfo = dataStore.defaultSortInfo;

		return new Ext.Button({
			id: config.id + CLEAR_SORT_BUTTON_ID,
			text: 'Очистить',
			cls: 'x-toolbar-standardbutton',
			tooltip: 'Сортировка по умолчанию (' + defaultSortInfo.header + ')',
			handler: function() {
				clearSort();
				createSort(defaultSortInfo);
				saveDefaultSort(defaultSortInfo.field, defaultSortInfo.direction);

				dataStore.sortInfo = defaultSortInfo;
				dataStore.reload();
			}
		});
	}

	var fullTbar;
	if (config.sortBar === false)
	{
		fullTbar = config.tbar;
	}
	else
	{
		var sortBar = new Ext.Toolbar({
			items: [ getClearSortButton(), ' ', 'Порядок сортировки:', ' ' ],
			plugins: [ reorderer, droppable ],
			style: { padding: '5px' },

			listeners: {
				scope: this,
				reordered: function(button) {
					changeSortDirection(button, false);
				}
			}
		});
		var tbarItems = sortBar.items.items;

		var configTbarPresent = false;
		if (!config.tbar)
			fullTbar = sortBar;
		else
		{
			configTbarPresent = true;
			fullTbar = new Ext.Panel({
				items: [ config.tbar, sortBar ]
			});
		}
	}

	var gridView = config.viewConfig ? new Kefir.LiveGridView(config.viewConfig) : new Kefir.LiveGridView();
	var bbar = config.bbar ? config.bbar : new Ext.ux.grid.livegrid.Toolbar({ view: gridView, displayInfo: true });

	var columnModel = new Ext.grid.ColumnModel({
		columns: columns,
		listeners: {
			hiddenchange : function(cm, columnIndex, hidden) {
				Kefir.showMask();

				var params = {
					methodType: GRID_SET_COLUMN_HIDDEN,
					entityName: entityName,
					columnName: cm.columns[columnIndex].dataIndex,
					columnIndex: columnIndex,
					hidden: hidden
				};
				Kefir.ajaxRequest(Kefir.setGridParam, params, function() { Kefir.hideMask(); });
			}
		}
	});

	var buttons = config.buttons;
	var fullButtons;
	if (config.label)
	{
		Ext.apply(config, { buttonAlign: 'left' });
		var gridId = config.id;
		fullButtons = [ gridView.getLabel(gridId), new Ext.form.Label({ id: gridId + 'width-100' }) ];
		if (buttons)
			fullButtons.push(buttons);
	}
	else
	{
		if (buttons)
			fullButtons = buttons;
	}

	Ext.apply(config, {
		cm: columnModel,
		view: gridView,
		tbar: fullTbar,
		bbar: bbar,
		store: dataStore,
		buttons: fullButtons,
		disableFocusFirst: config.disableFocusFirst,
		entityName: entityName
	});

	var grid = new Kefir.DynamicLiveGridPanel(config);
	grid.on({
		columnresize: columnResizeHandler,
		columnmove: function(oldIndex, newIndex) {
			if (oldIndex == newIndex || Kefir.blockColumnMove)
				return false;

			Kefir.blockColumnMove = true;
			Kefir.showMask();

			var checker = this.colModel.columns[0].id == 'checker' ? 1 : 0;
			var params = {
				methodType: GRID_CHANGE_COLUMNS_ORDER,
				entityName: this.entityName,
				columnName: this.colModel.columns[newIndex].dataIndex,
				columnIndex: oldIndex - checker,
				newIndex: newIndex - checker
			};
			Kefir.ajaxRequest(Kefir.setGridParam, params, function() {
					Kefir.blockColumnMove = false;
					Kefir.hideMask();
					grid.fireEvent('refreshColumns');
				},
				function() {
					Kefir.blockColumnMove = false;
			});
		},
		refreshColumns: function() {
			if (!sortBar || tbarItems.length == DEFAULT_TOOLBAR_COUNT + 1)
				return false;

			function getColumnOrder(columns, columnName) {
				for (var i = 0; i < columns.length; i++)
				{
					if (columns[i].dataIndex == columnName)
						return i;
				}

				return -1;
			}

			for (var i = DEFAULT_TOOLBAR_COUNT; i < tbarItems.length; i++)
			{
				var sortData = tbarItems[i].sortData;
				var order = getColumnOrder(grid.colModel.columns, sortData.field);
				var columnDom = Ext.query('.x-grid3-hd-row', Ext.get(grid.id).dom);
				var node = columnDom[columnDom.length - 1].childNodes[order];
				(new Ext.Element(node)).addClass(sortData.direction == 'ASC' ? 'sort-asc' : 'sort-desc');
			}
		},
		headerclick: function(grid, columnIndex) {
			if (!sortBar)
				return false;

			var column = grid.colModel.columns[columnIndex];
			if (!column.sortable)
				return false;

			var columnName = column.dataIndex;
			var length = getSortLength(tbarItems);
			if (length == 1)
			{
				var tbarButton = tbarItems[DEFAULT_TOOLBAR_COUNT];
				var sortData = tbarButton.sortData;
				if (sortData.field == columnName)
				{
					sortData.direction = sortData.direction.toggle('ASC', 'DESC');
					tbarButton.setIconClass(tbarButton.iconCls.toggle('sort-asc', 'sort-desc'));
					saveSort(columnName, 0, sortData.direction, false);
					return;
				}
			}

			clearSort();
			droppable.addItem(columnName, column.header, 'ASC');
			saveSort(columnName, 0, 'ASC', true);
			sortBar.doLayout();
		},
		afterrender: function(_gridPanel) {
			if (_gridPanel.getStore().dynamicGridAutoLoad)
				grid.loadMask.show();
		},
		render: function() {
			if (!sortBar)
				return;

			if (configTbarPresent)
			{ // переназначить dropTarget на элемент полного тулбара, т.к. у сортировочного тулбара элемент не определен
				droppable.dropTarget = new Ext.dd.DropTarget(fullTbar.getEl(), {
					notifyOver: droppable.notifyOver.createDelegate(droppable),
					notifyDrop: droppable.notifyDrop.createDelegate(droppable)
				});
			}

			var dragProxy = this.getView().columnDrag;
			droppable.addDDGroup(dragProxy.ddGroup);

			createSort(dataStore.sortInfo);
		},
		destroy: function() {
			Kefir.stopStoreAjaxRequest(dataStore);
		},
		keypress: function(e) {
			if (e.getKey() == e.ENTER)
			{
				var i = grid.getStore().indexOf(grid.getSelectionModel().getSelected())
				grid.fireEvent('rowdblclick', grid, i, e);
			}
		}
	});
	grid.view.on({
		refresh: function(gridView) {
			grid.fireEvent('refreshColumns');
			if (!grid.disableFocusFirst)
			{
				grid.getSelectionModel().selectFirstRow();
				gridView.focusRow(0);
			}
		}
	});
	if (config.label)
	{
		grid.getSelectionModel().on({
			rowselect: function(sm, rowIndex, r) {
				var gridId = config.id;
				gridView.changeLabel(gridId, r);
				Ext.get(Ext.get(gridId + 'width-100').dom.parentNode.id).setStyle({ width: '100%' });
			}
		});
	}

	return grid;
}

Kefir.initDynamicGrid = function(config) {
	Kefir.checkParams(config.vo, 'config.vo');
	Kefir.checkParams(config.callback, 'config.callback');
	Kefir.checkParams(config.dsConfig, 'config.dsConfig');
	Kefir.checkParams(config.dsConfig.url, 'config.dsConfig.url');
	Kefir.checkParams(config.gpConfig, 'config.gpConfig');
	Kefir.checkParams(config.gpConfig.id, 'config.gpConfig.id');
	Kefir.checkParams(config.dsUpdate, 'config.dsUpdate');
	Kefir.checkParams(config.gpUpdate, 'config.gpUpdate');

	Kefir.ajaxRequest(Kefir.getGridParams, { entityName: config.vo, services: config.services }, function(response) {
		function getSelectionModel(result) {
			if (!result.selModel)
				return null;

			function evaluate(obj) {
				for (var name in obj)
					obj[name] = eval(obj[name]);

				return obj;
			}

			switch (result.selModel.name) {
				case 'Ext.ux.grid.livegrid.CheckboxSelectionModel':
					Ext.grid.GridView.SplitDragZone.override({
						allowHeaderDrag : function(e) {
							var hc = this.view.columnDrop.getTargetFromEvent(e),
								i = this.view.getCellIndex(hc);

							return i !== 0;
						}
					});

					Ext.grid.HeaderDropZone.prototype.onNodeDrop = Ext.grid.HeaderDropZone.prototype.onNodeDrop.createInterceptor(
						function (n, dd, e, data) {
							var h = data.header;
							if (h != n)
							{
								var newIndex = this.view.getCellIndex(n);
								return newIndex !== 0;
							}

							return false;
						});

					var sm = result.selModel;
					result.checkedFieldName = sm.checkedFieldName;
					result.selModel = new Ext.ux.grid.livegrid.CheckboxSelectionModel(evaluate(sm.config));
					result.columns = [ result.selModel ].concat(result.columns);

					return result.selModel;
			}

			return null;
		}

		var manager = Ext.state.Manager;
		if (manager.get(config.gpConfig.id))
			manager.set(config.gpConfig.id, { columns: null });

		var i, cm;
		for (i = 0; i < response.columns.length; i++)
		{
			cm = response.columns[i];
			if (cm.indexOf && cm.indexOf('new Ext.grid.CheckColumn') != -1)
			{
				response.columns[i] = eval(cm);
				continue;
			}

			for (var name in cm)
			{
				if (name == 'hidden' || name == 'sortable' || name == 'header' || name == 'tooltip' || name == 'width'
					|| name == 'order' || name == 'dataIndex')
				{
					continue;
				}

				var value = cm[name];
				if (typeof(value) == 'string' && value.split('.').length > 1)
					cm[name] = eval(value);
			}
		}

		var pluginsLength = response.plugins.length;
		if (pluginsLength > 0)
		{
			var plugins = config.gpConfig.plugins;
			if (!plugins)
				plugins = [];

			for (i = 0; i < pluginsLength; i++)
			{
				cm = response.plugins[i];
				if (cm.Ext_ux_grid_ColumnHeaderGroup)
				{
					var columnHeaderGroup = new Ext.ux.grid.ColumnHeaderGroup({ rows: [ cm.Ext_ux_grid_ColumnHeaderGroup ] });
					plugins.push(columnHeaderGroup);
				}
			}

			config.gpConfig.plugins = plugins;
		}

		// сделать копию config.dsConfig.baseParams, чтобы не портить переданный хэш
		var dsBaseParams = {};
		Ext.apply(dsBaseParams, config.dsConfig.baseParams);
		Ext.apply(dsBaseParams, { entityName: config.vo });
		config.dsConfig.baseParams = dsBaseParams;

		var dsConfig = { sortInfo: response.sortInfo, defaultSortInfo: response.sortInfo.defaultSortInfo, timeout: 600000 };
		Ext.apply(dsConfig, config.dsConfig);

		var dataStore = Kefir.getDynamicLiveStore(dsConfig, response.jsonReaderFields);

		Ext.apply(config.gpConfig, {
			stripeRows: true,
			selModel: getSelectionModel(response),
			viewConfig: !response.viewConfig ? null : eval(response.viewConfig)
		});

		var gridPanel = Kefir.getDynamicLiveGridPanel(config.gpConfig, config.vo, response.columns, dataStore);

		if (response.checkedFieldName)
		{
			dataStore.addListener({
				load: function(store, records, options) {
					var selectedRows = [];
					for (var i = 0; i < records.length; i++)
					{
						if (records[i].json[response.checkedFieldName])
							selectedRows.push(i);
					}

					gridPanel.getSelectionModel().selectRows(selectedRows);
				}
			});
		}

		config.dsUpdate(dataStore); // на случай если dataStore нужно проставить элементам, зависящим от создания gridPanel (например, кнопкам) 
		config.gpUpdate(gridPanel);

		config.callback({ dataStore: dataStore, gridPanel: gridPanel });
	});
}

Kefir.getDynamicLiveStore = function(config, fields) {
	var autoLoad = true;
	if (config.autoLoad === false) // в config.dsConfig можно установить autoLoad: false
		autoLoad = false;

	Ext.apply(config, {
		autoLoad: autoLoad,
		dynamicGridAutoLoad: autoLoad, // просто autoLoad == true не виден в grid.afterrender 
		url: Kefir.contextPath + config.url,
		reader: new Kefir.LiveJsonReader({ id: config.jsonReaderId, fields: fields })
	});

	return new Kefir.DynamicLiveStore(config);
}

Kefir.dynamicGridToExcel = function(store, dynamicGridExcelType) {
	Kefir.gridToExcel(store, '/dynamicGridExcel?dynamicGridExcelType=' + dynamicGridExcelType);
}
Kefir.gridToExcel = function(store, dynamicGridExcelType) {
	Kefir.showMask();
	var params = store.baseParams;
	var sortInfo = store.sortInfo;
	Ext.apply(params, {
		sort: sortInfo.field,
		dir: sortInfo.direction
	});
	Ext.apply(params, store.params);
	if (store.lastOptions)
		Ext.apply(params, store.lastOptions.params);

	var isFirstParam = str.indexOf('?') == -1;
	for (var p in params)
	{
		var value = params[p];
		str += (isFirstParam ? '?' : '&') + p + '=' + (value instanceof Date ? value.format('d.m.Y') : value);
		isFirstParam = false;
	}

	location.href = ru.kg.gtn.contextPath + str;
	Kefir.hideMask();//todo: think about mask, this is not work
}