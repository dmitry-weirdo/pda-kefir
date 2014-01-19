Ext.ns('Kefir');

Kefir.LiveGridSummary = function(config, gridId, summaryUrl) {
	Ext.apply(this, config);
	Ext.apply(this, {
		summaryUrl: Kefir.contextPath + summaryUrl
	});
	Ext.apply(this.params, { gridId: gridId });
};
Ext.extend(Kefir.LiveGridSummary, Ext.ux.grid.GridSummary, {
	id: 'liveSummary',

	calculate: function(json, cm) {
		var data = {}, cfg = cm.config;
		for (var i = 0, len = cfg.length; i < len; i++)
		{ // loop through all columns in ColumnModel
			var cf = cfg[i], // get column's configuration
				cname = cf.dataIndex; // get column dataIndex

			if (cf.summary)
				data[cname] = json[cname];
			else
				data[cname] = 0;
		}

		return data;
	},

	getCorrectStyleWidth: function(style) {
		var widthPos = style.indexOf('width:');
		var tmp = style.substring(widthPos + 6);
		var pxPos = tmp.indexOf('px;');
		var val = parseInt(tmp.substring(0, pxPos)) - 1;

		return style.substring(0, widthPos + 6) + val + tmp.substring(pxPos);
	},

	renderSummary : function(o, cs, cm) {
		cs = cs || this.view.getColumnData();
		var cfg = cm.config,
			buf = [],
			last = cs.length - 1;

		for (var i = 0, len = cs.length; i < len; i++)
		{
			var c = cs[i], cf = cfg[i], p = {};

			p.id = c.id;
			p.style = this.getCorrectStyleWidth(c.style);
			p.css = i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');

			if (cf.summary || cf.summaryRenderer)
				p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
			else
				p.value = '';

			if (p.value === undefined || p.value === "")
				p.value = "&#160;";

			buf[buf.length] = this.cellTpl.apply(p);
		}

		return this.rowTpl.apply({
			tstyle: 'width:' + this.view.getTotalWidth() + ';',
			cells: buf.join('')
		});
	},

	refreshSummary: function() {
		var store = this.grid.store;

		if (store.getRange().length == 0)
		{//create empty summary
			var cs = this.view.getColumnData(),
				cm = this.cm,
				data = [],
				buf = this.renderSummary({data: data}, cs, cm);

			if (!this.view.summaryWrap)
			{
				this.view.summaryWrap = Ext.DomHelper.insertAfter(this.view.scroller, {
					tag: 'div',
					cls: 'x-grid3-gridsummary-row-inner'
				}, true);
			}
			this.view.summary = this.view.summaryWrap.update(buf).first();

			return;
		}

		Ext.apply(this.params, store.baseParams);
		Ext.apply(this.params, store.params);

		Ext.Ajax.request({
			url: this.summaryUrl,
			params: this.params,

			success: function(response) {
				var result = Ext.util.JSON.decode(response.responseText);
				var json = result.json;
				var gridId = result.gridId;
				var summary;
				var cmp = Ext.getCmp(gridId);
				if (!cmp)
					return;
				
				var plugins = cmp.plugins;
				for (var i = 0; i < plugins.length; i++)
				{
					if (plugins[i].id == 'liveSummary')
						summary = plugins[i];
				}

				var	cs = summary.view.getColumnData(),
					cm = summary.cm,
					data = summary.calculate(json, cm),
					buf = summary.renderSummary({data: data}, cs, cm);

				if (!summary.view.summaryWrap)
				{
					summary.view.summaryWrap = Ext.DomHelper.insertAfter(summary.view.scroller, {
						tag: 'div',
						cls: 'x-grid3-gridsummary-row-inner'
					}, true);
				}
				
				summary.view.summary = summary.view.summaryWrap.update(buf).first();
			},

			failure: function(response) {
				Ext.Msg.alert('Îøèáêà', response.responseText);
			}
		});
	}
});
Ext.reg('livegridsummary', Kefir.LiveGridSummary);
//*/