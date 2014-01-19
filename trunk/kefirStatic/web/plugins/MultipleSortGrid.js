Ext.namespace('App.view');	

App.view.MultiSortGrid = function(config, store, columnModel) {
	var reorderer = new Ext.ux.ToolbarReorderer();

	var droppable = new Ext.ux.ToolbarDroppable({
		// Creates the new toolbar item from the drop event
		createItem: function(data) {
			var column = this.getColumnFromDragDrop(data);
			return createSorterButton({
				text: column.header,
				sortData: {
					field: column.dataIndex,
					direction: "ASC"
				}
			});
		},

		/**
		 * Custom canDrop implementation which returns true if a column can be added to the toolbar
		 * @param {Object} data Arbitrary data from the drag source
		 * @return {Boolean} True if the drop is allowed
		 */
		canDrop: function(dragSource, event, data) {
			var sorters = getSorters();
			var column = this.getColumnFromDragDrop(data);

			for (var i = 0; i < sorters.length; i++)
				if (sorters[i].field == column.dataIndex)
					return false;
			
			return true;
		},
		afterLayout: doSort,

		/**
		 * Helper function used to find the column that was dragged
		 * @param {Object} data Arbitrary data from
		 */
		getColumnFromDragDrop: function(data) {
			var index = data.header.cellIndex;
			var colModel = obj.colModel;
			return colModel.getColumnById(colModel.getColumnId(index));
		}
	});

	var tbar = new Ext.Toolbar({
		items: [ 'Порядок сортировки:', ' ' ],
		plugins: [ reorderer, droppable ],
		style: { padding: '5px' },

		listeners: {
			scope: this,
			reordered: function(button) {
				changeSortDirection(button, false);
			}
		}
	});

	config = config || {
//		region: 'west',
		width: 500,
		bubbleEvents: [ 'load', 'rowclick' ],
		store: store,
		colModel: columnModel,
		selModel: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),

//		tbar: fullTbar,
		tbar: tbar,
		bbar: new Ext.PagingToolbar({
			store: store,
			beforePageText: 'Page&nbsp;',
			afterPageText: '&nbsp;of {0}',
			displayInfo: true,
			displayMsg: 'Displaying results {0} - {1} of {2}',
			emptyMsg: 'No results to display',
			pageSize: 30
		})
	};

//	var obj = new App.view.GridPanel(config);
	var obj = new Ext.grid.GridPanel(config);

	// The following functions are used to get the sorting data
	//  from the toolbar and apply it to the store
	function doSort() {
		//store.sort(getSorters(), "ASC");
		var last = store.lastOptions;
		var s = '';
		var f = '';

		Ext.each(getSorters(), function(item) {
			f = f.concat(item.field, ','); // Array of fields, respective
			s = s.concat(item.direction, ','); // Array of directions
		});

		Ext.apply(last.params, { fields: f, sorts: s } );
		store.reload();
	};

	/**
	 * Callback handler used when a sorter button is clicked or reordered
	 * @param {Ext.Button} button The button that was clicked
	 * @param {Boolean} changeDirection True to change direction (default). Set to false for reorder
	 * operations as we wish to preserve ordering there
	 */
	function changeSortDirection(button, changeDirection) {
		var sortData = button.sortData;
		var iconCls = button.iconCls;

		if (sortData != undefined)
		{
			if (changeDirection !== false)
			{
				button.sortData.direction = button.sortData.direction.toggle("ASC", "DESC");
				button.setIconClass(iconCls.toggle("sort-asc", "sort-desc"));
			}
			
			store.clearFilter();
			doSort();
		}
	};

	/**
	 * Returns an array of sortData from the sorter buttons
	 * @return {Array} Ordered sort data from each of the sorter buttons
	 */
	function getSorters() {
		var sorters = [];
		Ext.each(tbar.findByType('button'), function(button) {
			sorters.push(button.sortData);
		}, this);
		
		return sorters;
	}

	/**
	 * Convenience function for creating Toolbar Buttons that are tied to sorters
	 * @param {Object} config Optional config object
	 * @return {Ext.Button} The new Button object
	 */
	function createSorterButton(config) {
		config = config || {};
		Ext.applyIf(config, {
			listeners: {
				click: function(button, e) {
					changeSortDirection(button, true);
				}
			},
			iconCls: 'sort-' + config.sortData.direction.toLowerCase(),
			reorderable: true
		});
		return new Ext.Button(config);
	};

	/**
	 * Returns an array of fake data
	 * @param {Number} count The number of fake rows to create data for
	 * @return {Array} The fake record data, suitable for usage with an ArrayReader
	 */
	obj.on({
		'render': function() {
			var dragProxy = this.getView().columnDrag;
			droppable.addDDGroup(dragProxy.ddGroup);

			var topToolbar = obj.getTopToolbar();
			var tbarPanel = obj.getTopToolbar().ownerCt;
			tbarPanel.remove(topToolbar);

			tbarPanel.add( new Ext.Toolbar({ items: [ 'Dummy toolbar' ]}) );
			tbarPanel.add(topToolbar);
		}
	});
	
	return obj;
};