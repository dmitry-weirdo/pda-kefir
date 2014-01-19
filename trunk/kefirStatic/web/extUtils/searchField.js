Ext.namespace('Kefir.form');

Kefir.form.getSearchField = function(config, id, paramName, dataStore, width, maxLength, vtype) {
	Ext.apply(config, {
		id: id,
		width: width ? width : 100,
		maxLength: maxLength ? maxLength : 100,
		store: dataStore,
		paramName: paramName,
		vtype: vtype
	});

	return new Kefir.form.SearchField(config);
};

Kefir.form.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
	constructor: function(config) {
		if (config.style)
		{
			if (!config.style.textTransform)
				Ext.apply(config.style, { textTransform: 'uppercase' });
		}
		else
		{
			Ext.apply(config, { style: { textTransform: 'uppercase' } });
		}
		
		Ext.apply(this, {
			validationEvent: true,
			validateOnBlur: true,
			trigger1Class: 'x-form-clear-trigger',
			trigger2Class: 'x-form-search-trigger',
			hideTrigger1: true,
			width: 180,
			autoCreate: { tag: 'input', type: 'text', size: '20', autocomplete: 'off', maxLength: config.maxLength },
			hasSearch: false,
			paramName: 'query',
			beforeSearch: Ext.emptyFn,
			beforeClear: Ext.emptyFn,
			enableKeyEvents: true,

			listeners: {
				specialkey: function(f, e) {
					if(e.getKey() == e.ENTER)
						this.onTrigger2Click();
				}
			},
			
			onTrigger1Click: function() {
				this.beforeClear();

				if (this.hasSearch)
				{
					this.el.dom.value = '';
					var o = { start: 0, limit: 200 };

					Ext.apply(o, this.store.params);
					this.store.baseParams = this.store.baseParams || {};
					this.store.baseParams[this.paramName] = '';
					this.store.reload( { params: o } );
					this.triggers[0].hide();
					this.hasSearch = false;
				}
			},

			onTrigger2Click: function(){
				if (!this.validate())
					return;

				this.beforeSearch(this); // allow user to clear this.store.params etc

				var v = this.getRawValue();
				if(v.length < 1)
				{
					this.onTrigger1Click();
					return;
				}

				var o = { start: 0, limit: 200 };

				Ext.apply(o, this.store.params);
				this.store.baseParams = this.store.baseParams || {};
				this.store.baseParams[this.paramName] = v;
				this.store.reload({ params: o });
				this.hasSearch = true;
				this.triggers[0].show();
			}
		});

		Kefir.form.SearchField.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});