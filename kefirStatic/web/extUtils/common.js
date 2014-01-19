Ext.namespace('Kefir');

Kefir.testServer = null;
Kefir.contextPath = null;
Kefir.user = null;
Kefir.name = null;
Kefir.role = null;
Kefir.displayRole = null;
Kefir.region = null;
Kefir.inspection = null;
Kefir.inspectionName = null;
Kefir.viewPort = null;
Kefir.mask = null;
Kefir.defaultRegionId = null; // id записи в кладр записи с кодом 6300000000000 (Самарская область)
Kefir.dsAutoLoad = false;

Kefir.fillCurrentUser = function(callback) {
	Ext.Ajax.request({
		url: Kefir.contextPath + '/currentUserGet',

		success: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			Kefir.user = result.login;
			Kefir.name = result.name;
			Kefir.role = result.role;
			Kefir.region = result.region;
			Kefir.inspection = result.inspection;
			Kefir.inspectionName = result.inspectionName;
			Kefir.displayRole = Kefir.render.roleRenderer(Kefir.role); // role to display in toolbar
			Kefir.testServer = result.testServer;
			Kefir.dsAutoLoad = result.autoLoad;

			if (callback)
				callback();
		},

		failure: function(response) {
			var result = response.responseText;
			Ext.Msg.alert('Ошибка', 'Ошибка при получении текущего пользователя: ' + result);
		}
	});
}
Kefir.fillDefaultRegionId = function(callback) {
	Ext.Ajax.request({
		url: Kefir.contextPath + '/defaultRegionGet',

		success: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			Kefir.defaultRegionId = result.id;

			if (callback)
				callback();
		},

		failure: function(response) {
			var result = response.responseText;
			Ext.Msg.alert('Ошибка', 'Ошибка при получении КЛАДР кода текущего региона: ' + result);
		}
	});
}

Kefir.showMask = function(message) {
	if (!Kefir.mask)
	{
		Kefir.mask = Ext.getBody().mask( message || 'Пожалуйста, подождите...' );
//		Kefir.mask = new Ext.LoadMask(Ext.getBody(), { msg: message || "Пожалуйста, подождите..." });
		Kefir.mask.setStyle('z-index', Ext.WindowMgr.zseed + 1000); // malicious hag for hide modal windows too
		Kefir.mask.show();
	}
}
Kefir.hideMask = function() {
	if (Kefir.mask)
	{
		Ext.getBody().unmask();
		Kefir.mask = null;
//		Kefir.mask.hide();
	}
}

Kefir.setDisabled = function(disabled, components) {
	for (var i = 0; i < components.length; i++)
		components[i].setDisabled(disabled);
}

Kefir.setReadOnlyFormComponents = function(FORM_FIELDS_IDS, readOnly) {
	for (var i = 0; i < FORM_FIELDS_IDS.length; i++)
	{
		if (readOnly)
			Ext.getCmp(FORM_FIELDS_IDS[i]).disable();
		else
			Ext.getCmp(FORM_FIELDS_IDS[i]).enable();
	}
}
Kefir.setReadOnlyFormComponent = function(componentId, readOnly) {
	Ext.getCmp(componentId).setReadOnly(readOnly);
}

Kefir.getCurrentRegion = function() {
	return Kefir.region || 63;
}
Kefir.getCurrentInspection = function() {
	if (Kefir.role == Kefir.Role.LOCAL)
		return Kefir.inspection;

	return 0;
}
Kefir.isCurrentInspection = function(region, inspection) {
	return (region == Kefir.getCurrentRegion()) && (inspection == Kefir.getCurrentInspection());
}
Kefir.isAdmin = function() {
	return Kefir.role == Kefir.Role.ADMIN;
}
Kefir.isRegional = function() {
	return Kefir.role == Kefir.Role.REGIONAL;
}
Kefir.isLocal = function() {
	return Kefir.role == Kefir.Role.LOCAL;
}
Kefir.isGibdd = function() {
	return Kefir.role == Kefir.Role.GIBDD;
}
Kefir.sameInspection = function(region, inspection) {
	return (Kefir.region == region) && (Kefir.inspection == inspection);
}
Kefir.closeWindow = function(w) {
	w.destroy();
}

Kefir.getCurrentYear = function() {
	return new Date().getFullYear();
}

Kefir.bindMask = function(formWindow) {
	formWindow.on('show', function(win) {
		var winHeight = win.y + win.getHeight();
		if (Ext.getBody().getHeight() >= winHeight) // если форма по высоте занимает меньше, чем окно, то скролл показывать не надо
			return;

		var maxHeight = winHeight + 2;
		var masks = Ext.query('.ext-el-mask');
		Ext.each(masks, function(mask) { // растянуть маски на высоту формы
			var maskHeight = Ext.get(mask.id).getHeight();
			if (maskHeight > maxHeight)
				maxHeight = maskHeight;
		});

		Ext.DomHelper.applyStyles(masks[masks.length - 1], { height: maxHeight });
	});
}

Kefir.getLocalComboStore = function(config, url, poClass) {
	if (poClass)
		Ext.apply(config.baseParams, { entityName: poClass });

	Ext.apply(config, {
		autoLoad: true,
		url: Kefir.contextPath + url,
		reader: new Kefir.LiveJsonReader({ id: 'id', fields: [ { name: 'id' }, { name: 'name' } ]})
	});

	return new Ext.data.Store(config);
};

/**
 * Applies config to value. If value does not exist, it is created.
 * @param value value
 * @param config config
 */
Kefir.apply = function(object, property, config) {
	if (!object[property])
		object[property] = config;
	else
		Ext.apply(object[property], config);
}
Kefir.stopStoreAjaxRequest = function(dataStore) {
	var transactionId = dataStore.proxy.conn.transId;
	if (transactionId)
		Ext.Ajax.abort(transactionId);
}
Kefir.reload = function(dataStore, config) {
	if (!dataStore)
		return; // исключить падение в случае неуказанного dataStore

	Kefir.stopStoreAjaxRequest(dataStore);

	Kefir.apply(dataStore, 'baseParams', config);
	dataStore.reload();
}

Kefir.unescapeHTML = function(str) {
	return str.replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>');
}

Kefir.encode = function(value) {
	return value ? Ext.encode(value) : '';
}

Kefir.getValue = function(record, fieldName) {
	if (!record)
		return undefined;

	return record.get ? record.get(fieldName) : record[fieldName];
}
Kefir.getDefinedValue = function(record, fieldName) {
	var value = Kefir.getValue(record, fieldName);
	return value || '';
}
Kefir.getFieldValue = function(field) {
	if (!field || !field.getValue)
		return null;

	return field.getValue();
}
Kefir.getId = function(record) {
	if (!record)
		return null;

	return Kefir.getValue(record, 'id');
}

Kefir.hashFieldsAreEmpty = function(hash) {
	if (!hash)
		return true;

	for (var i in hash)
		if (hash[i])
			return false;

	return true;
}
Kefir.getHash = function(hash) {
	if (Kefir.hashFieldsAreEmpty(hash))
		return null;

	return hash;
}

Kefir.getFieldLabel = function(fieldLabel, allowBlank) { // todo: move this to TextField sublass that will be superclass for all our TextField classes // todo: move this to fieldsFactory.js
	if (!fieldLabel)
		return null;

	return allowBlank ? fieldLabel : fieldLabel + '*';
}
Kefir.setRequireField = function(elementName, require) { // todo: modify and move to fieldsFactory.js
	var el = Ext.getCmp(elementName);
	el.allowBlank = !require;
	el.container.up('div.x-form-item').dom.firstChild.innerHTML = Kefir.getFieldLabel(el.fieldLabel, !require) + ':';
}
Kefir.getDate = function(date) {
	if (typeof(date) == 'string')
		return Date.parseDate(date, 'c');

	return date;
}

Kefir.parseStringToIntArray = function(s) {
	var result = new Array(s.trim().length)
	for (var i = 0; i < s.length; i++)
		result[i] = parseInt(s.charAt(i));

	return result;
}

Kefir.focusNext = function(field) {
	var formPanel = field.findParentBy( function(container) { return container.focusNext; } );
	formPanel.focusNext(field.id);
}

// todo: remove type parameter and merge this function with Kefir.form.getButton
Kefir.getButton = function(config, id, text, type, successHandler) {
	Ext.apply(config, {
		id: id,
		text: text,
		type: type,
		handler: function() {
			this.blur(); // excludes double "Enter" press on button

			if (successHandler)
				successHandler();
		}
	});

	return new Ext.Button(config);
}

Ext.namespace('Kefir.form');
Kefir.form.getButton = function(config, id, text, successHandler) { // todo: merge this with Kefir.getButton
	if (config.tbar)
	{
		Ext.apply(config, {
			cls: 'x-toolbar-standardbutton',
			minWidth: 75
		});
	}

	Ext.apply(config, {
		id: id,
		text: text,
		handler: function() {
			this.blur(); // excludes double "Enter" press on button

			if (successHandler)
				successHandler();
		}
	});

	return new Ext.Button(config);
}

Kefir.getWindow = function(config, id, width, title) {
	if (width)
	{
		Ext.apply(config, {
			id: id,
			title: title,
			width: width,
			autoHeight: !config.height,
			modal: true,
			layout: 'fit',
			resizable: false
		});
	}
	else
	{
		Ext.apply(config, {
			id: id,
			title: title,
			maximized: true,
			layout: 'fit'
		});
	}

	return new Ext.Window(config);
}

Kefir.getFormWindow = function(config, form, id, width, title, defaultButton) {
	Ext.apply(config, {
		constrain: true
	});

	if (form)
	{
		Ext.apply(config, {
			items: [ form ]
		});

		if (!form.fieldsIds.length && defaultButton >= 0)
			config.defaultButton = defaultButton;

		function beforeshow() {
			var fields = form.fieldsIds;
			for (var i = 0; i < fields.length; i++)
			{
				var field = Ext.getCmp(fields[i]);
				if (field && !field.disabled)
				{
					this.focusEl = field;
					break;
				}
			}
		}

		if (!config.listeners)
		{
			Ext.apply(config, {
				listeners: {
					beforeshow: beforeshow
				}
			});
		}
		else
		{
			 if (!config.listeners.beforeshow)
			 {
				 Ext.apply(config, {
					 beforeshow: beforeshow
				 });
			 }
		}
	}

	return Kefir.getWindow(config, id, width, title);
}

Kefir.getFormPanel = function(config, id, fieldsIds, items, baseParams, productionBlurConfig) {
	Ext.apply(config, {
		id: id,
		fieldsIds: fieldsIds || [],
		autoScroll: true,
		autoHeight: !config.height,
		items: items,
		productionBlurConfig: productionBlurConfig,
		baseParams: baseParams,
		listeners: {
			beforeaction: function(form, action) {
				// Call HtmlEditor's syncValue before actions
				this.items.each(function(f) {
					if (f.isFormField && f.syncValue)
					{
						f.syncValue();
					}
				});
				var o = action.options;
				if (o.waitMsg)
				{
					if (this.waitMsgTarget === true)
					{
						this.el.mask(o.waitMsg, 'x-mask-loading');
					}
					else if (this.waitMsgTarget)
					{
						this.waitMsgTarget = Ext.get(this.waitMsgTarget);
						this.waitMsgTarget.mask(o.waitMsg, 'x-mask-loading');
					}
					else
					{
						Ext.MessageBox.wait(o.waitMsg, o.waitTitle || this.waitTitle);
						Ext.Msg.getDialog().getEl().dom.id = 'waitMessageBox';
					}
				}
			}
		}
	});

	return new Kefir.FormPanel(config);
}
Kefir.parseBoolean = function(s) {
	if (!s)
		return false;

	return /true/i.test(s.trim());
}

Kefir.FormPanel = function(config) {
	Kefir.FormPanel.superclass.constructor.call(this, config);
	this.bindFormFields();
	this.fillSpinners();
	if (config.productionBlurConfig)
		Kefir.production.Production.addBlurHandlers(this);
}

Ext.extend(Kefir.FormPanel, Ext.FormPanel, {
	bodyStyle: 'padding: 5px;',
	defaultType: 'textField',
	listeners: {
		destroy: function() {
			for (var i = 0; i < this.spinners.length; i++)
			{
				var field = this.spinners[i];
				field.beforeBlur = Ext.emptyFn;
				field.getValue = function() { return field.startValue; }
			}
		}
	},
	fillSpinners: function() {
		this.spinners = new Array();

		for (var i = 0; i < this.fieldsIds.length; i++)
		{
			var field = Ext.getCmp(this.fieldsIds[i]);
			if (field && field.spinner)
				this.spinners.push(field);
		}
	},
	bindFormFields: function() {
		if (!this.fieldsIds)
		{
			alert('this.fieldsIds is not defined');
			return; // предотвратить падение в случае неуказанных fieldsIds
		}

		for (var i = 0; i < this.fieldsIds.length - 1; i++)
		{
			// todo: also set tabindex for fields
			var form = this.getForm();

			var field_i = Ext.getCmp(this.fieldsIds[i]);
			if (field_i)
			{
				field_i.on('specialkey', function(field, event) {
					var formIds;

					formIds = form.fieldsIds;

					if (event.getKey() == event.ENTER)
					{
						for (var j = 0; j < formIds.length - 1; j++)
						{
							if (formIds[j] == field.id)
							{
								// search first visible field from next of current field
								j++;
								var nextField = Ext.getCmp(formIds[j]);
								while (!( nextField && nextField.isVisible() && !nextField.disabled && !nextField.readOnly ) && j < formIds.length)
								{
									j++;
									nextField = Ext.getCmp(formIds[j]);
								}

								if (nextField && nextField.isVisible() && !nextField.disabled && !nextField.readOnly)
								{
									event.stopPropagation(); // prevent form submitting on last button focus
									event.stopEvent();

									if (field.xtype == 'datefield') /*|| field.id == 'districtCombo') // todo: solve this malicious hag normally*/
										field.fireEvent('blur', field);

									if (field.blurHandler)
										field.blurHandler(field);

									nextField.focus();
								}

								return;
							}
						}
					}
				});
			}
		}
	},

	focusFirst: function() {
		if (!this.fieldsIds)
			return;

		var field;
		for (var i = 0; i < this.fieldsIds.length; i++)
		{
			field = Ext.getCmp(this.fieldsIds[i]);
			if (field && !field.disabled && !field.readOnly && field.isVisible())
			{
				field.focus(true, 1000);
				break;
			}
		}

		this.getForm().clearInvalid();
	},

	focusNext: function(fieldId) {
		if (!this.fieldsIds)
			return;

		var i;
		var startPos = -1;

		for (i = 0; i < this.fieldsIds.length; i++)
		{
			if (this.fieldsIds[i] == fieldId)
			{
				startPos = i;
				break;
			}
		}

		if (startPos == -1) // fieldId not found in this.fieldsIds
			return;

		var field;
		for (i = startPos + 1; i < this.fieldsIds.length; i++)
		{
			field = Ext.getCmp(this.fieldsIds[i]);
			if (field && !field.readOnly && !field.disabled && field.isVisible())
			{ // next field to focus is found
				if (field.isDateField)
				{ // для случая выхода из окна совпадения спецпродукции по пробелу\энтеру, приходится вызывать событие focus вручную
					field.wasFocused = false;
					field.focus();
					if (!field.wasFocused)
						field.fireEvent('focus', field);

					break;
				}

				field.focus();
				break;
			}
		}
	},

	bbar: [ '* - обязательные для заполнения поля' ]
});

Kefir.reloadStores = function(storeArrays) {
	if (!storeArrays)
		return;

	if (!storeArrays.length)
	{
		storeArrays.reload();
		return;
	}

	for(var i = 0; i < storeArrays.length; i++)
	{
		var store = storeArrays[i];
		if (store)
			store.reload();
	}
}
Kefir.getSaveButtonId = function(windowId) {
	return windowId + 'SaveButton';
}
Kefir.getCancelButtonId = function(windowId) {
	return windowId + 'CancelButton';
}
Kefir.getDeleteButtonId = function(windowId) {
	return windowId + 'DeleteButton';
}
Kefir.getSaveButton = function(form, url, paramsHandler, storeArrays, window, buttonText) {
	return Kefir.getButton({}, Kefir.getSaveButtonId(window.getId()), buttonText || 'Сохранить', 'submit', function() {
		Kefir.checkForm(form, function() {
			Kefir.formSubmit(form, url, paramsHandler ? paramsHandler() : null, function() {
				Kefir.reloadStores(storeArrays);
				window.close();
			});
		});
	});
}
Kefir.getDeleteButton = function(msg, url, params, storeArrays, window) {
	return new Ext.Button({
		id: Kefir.getDeleteButtonId(window.getId()),
		text: 'Удалить',
		handler: function() {
			Ext.Msg.confirm(
				'Подтверждение удаления',
				msg,
				function(btn) {
					if (btn != 'yes')
						return;

					Kefir.ajaxRequest(url, params, function() {
						Kefir.reloadStores(storeArrays);
						window.destroy();
					});
				});
		}
	});
}
Kefir.getCancelButton = function(window) {
	return {
		id: window ? Kefir.getCancelButtonId(window.getId()) : 'CancelButton',
		text: 'Отмена',
		handler: function() {
			window.close();
		}
	};
}
Kefir.getCloseButton = function(window, title, closeHandler) {
	return {
		id: window.getId() + 'CloseButton',
		text: title ? title : 'Закрыть',
		handler: function() {
			window.close();

			if (closeHandler)
				closeHandler();
		}
	};
}
Kefir.getExitButton = function(window, gridId) {
	return {
		id: gridId ? window.getId() + gridId + 'ExitButton' : window.getId() + 'ExitButton',
		text: 'Выход',
		handler: function() {
			window.close();
		}
	};
}

Kefir.simpleVOGetByName = function(entityName, name, successHandler) {
	Kefir.ajaxRequest('/simpleVOGetByName', { name: name, entityName: entityName }, function(resp) {
		successHandler(resp);
	});
}

Kefir.ajaxRequest = function(url, params, successHandler, failureHandler, dontShowMask) {
	if (!dontShowMask)
		Kefir.showMask(!params ? null : params.waitMsg);

	Ext.Ajax.request({
		url: Kefir.contextPath + url,
		params: params,
		timeout: 600000,
		success: function(response, opts) {
			var responseText = response.responseText;
			Kefir.hideMask();
			var resp;
			try
			{
				resp = Ext.util.JSON.decode(responseText);
			}
			catch(ex)
			{
				if (successHandler)
					successHandler(responseText, opts);

				return;
			}

			if (!resp.success)
				Kefir.showResponseMessage(responseText, null, failureHandler)
			else
			{
				if (successHandler)
					successHandler(resp, opts);
			}
		},
		failure: function(response) {
			Kefir.hideMask();
			Kefir.showAjaxResponseMessage(this.url, response, failureHandler);
		}
	});
}
Kefir.showResponseMessage = function(responseText, errorText, failureHandler) {
	var errorMsg;
	try
	{
		errorMsg = Ext.util.JSON.decode(responseText).msg;
	}
	catch (e)
	{
		errorMsg = responseText;
	}

	var errorPrefix = errorText ? errorText + '<br/><br/>' : '';
	var fullErrorMsg = errorPrefix + (!errorMsg ? 'Неизвестная ошибка приложения' : errorMsg);
	Ext.Msg.alert('Ошибка', fullErrorMsg, function(btn, text) {
		if (btn == 'ok' && failureHandler)
			failureHandler();
	});
	Ext.Msg.getDialog().getEl().dom.id = 'errorMessageBox';
	Kefir.hideMask();
}
Kefir.showFormResponseMessage = function(url, action, form, errorText) {
	if (form && !form.isValid())
		return;

	var response = action.response;
	if (response.isTimeout)
		Ext.MessageBox.alert('Ошибка', 'Сервер не отвечает. "' + url + '" недоступен.');
	else
		Kefir.showResponseMessage(response.responseText, errorText);

	Kefir.hideMask();
}
Kefir.showAjaxResponseMessage = function(url, response, failureHandler) {
	if (response.isTimeout)
		Ext.MessageBox.alert('Ошибка', 'Сервер не отвечает. "' + url + '" недоступен.');
	else
		Kefir.showResponseMessage(response.responseText, null, failureHandler);

	Kefir.hideMask();
}

Kefir.selectGridPanelRecord = function(gridPanel, notChosenTitle, notChosenMessage, callback) {
	var selectedRecord = gridPanel.selModel.getSelected();
	if (!selectedRecord)
	{ // gridPanel item not selected
		Ext.Msg.alert(notChosenTitle, notChosenMessage);
		return;
	}

	if (callback)
		callback(selectedRecord);
}

Kefir.getSelectedJson = function(gridPanel, msg) {
	var selected = gridPanel.getSelectionModel().getSelected();
	if (selected)
		return selected.json;

	Ext.Msg.alert('Ошибка', msg);
	return null;
}
Kefir.getSelectedJsonById = function(gridPanel, msg, url, successHandler) {
	var selected = gridPanel.getSelectionModel().getSelected();
	if (!selected)
	{
		Ext.Msg.alert('Ошибка', msg);
		return null;
	}

	Kefir.ajaxRequest(url, { id: selected.json.id}, successHandler);
}
Kefir.chooseSelectedJson = function(gridPanel, msg, handler) {
	var selected = gridPanel.getSelectionModel().getSelected();
	if (selected)
		handler(selected.json ? selected.json : selected.data);
	else
		Ext.Msg.alert('Ошибка', msg);
}
var dblClick;
Kefir.isDblClick = function() {
	var now = new Date().getTime();
	if (dblClick && now - dblClick < 50)
		return true;

	dblClick = now;
	return false;
}
Kefir.setEnableButtons = function(buttons, setEnable) {
	if (!buttons)
		return;

	if (!buttons.length)
	{
		if (buttons.disabled == !setEnable)
			return;

		if (setEnable)
			buttons.enable();
		else
			buttons.disable();

		return;
	}

	if (buttons[0].disabled == !setEnable)
		return;

	for (var i = 0; i < buttons.length; i++)
	{
		if (setEnable)
			buttons[i].enable();
		else
			buttons[i].disable();
	}
}
Kefir.getWidth = function(render) {
	return render.getWidth() - 30;
}
Kefir.getHeight = function(render) {
	return render.getHeight() - 30;
}
Kefir.getFormFieldsIds = function(fields) {
	if (!fields)
		return [];

	if (!fields.length)
		return [ fields.getId() ];

	var items = [];
	for (var i = 0; i < fields.length; i++)
	{
		if (fields[i])
			items.push(fields[i].getId());
	}

	return items;
}
Kefir.chooseCmp = function(id, successHandler) {
	var cmp = Ext.getCmp(id);
	return cmp ? successHandler(cmp) : null;
}

/**
 * Функция делает submit формы
 *
 * @param form
 * @param url
 * @param params
 * @param successHandler
 */
Kefir.formSubmit = function(form, url, params, successHandler, failureHandler) {
	form.getForm().submit({
		scope: this,
		timeout: 60000,
		url: Kefir.contextPath + url,
		waitMsg: 'Выполняется сохранение. Пожалуйста, подождите...',
		params: params,
		success: function(form, action) {
			var responseText = action.response.responseText;
			var resp;
			try
			{
				resp = Ext.util.JSON.decode(responseText);
			}
			catch(ex)
			{
				if (successHandler)
					successHandler(responseText);

				return;
			}

			if (!resp.success)
			{
				Kefir.showFormResponseMessage(this.url, action, form);
			}
			else
			{
				if (successHandler)
					successHandler(resp);
			}
		},
		failure: function(form, action) {
			if (failureHandler)
				failureHandler();

			Kefir.showFormResponseMessage(this.url, action, form);
		}
	});
}

/**
 * Функция проверяет на валидность форму и если она валидна, то выполняется successHandler.
 * В противном случае фокусируется первое невалидное поле формы.
 * @param form форма
 * @param successHandler функция, которая выполняется, если форма валидна
 */
Kefir.checkForm = function(form, successHandler) {
	if (!form)
	{
		alert('form not defined for Kefir.checkForm');
		return;
	}
	if (!successHandler)
	{
		alert('successHandler not defined for Kefir.checkForm');
		return;
	}
	
	if (form.getForm().isValid())
	{
		successHandler();
		return;
	}

	// поставить курсор в первое незаполненное поле
	var fieldsIds = form.fieldsIds;
	for (var i in fieldsIds)
	{
		var field = Ext.getCmp(fieldsIds[i]);
		if (field && field.isValid && !field.isValid())
		{
			field.focus();
			return;
		}
	}
}

Kefir.submitForm = function(form, url, successHandler, waitMsg) {
	form.getForm().submit({
		url: Kefir.contextPath + url,
		waitMsg: waitMsg || 'Выполняется сохранение. Пожалуйста, подождите...',

		success: function() {
			successHandler();
		},

		failure: function(form, action) {
			Kefir.showResponseMessage(action.response.responseText);
		}
	});
}

Kefir.checkParams = function(value, msg, bool) {
	if (bool)
	{ // allows 'false' value
		if (value === false)
			return;
	}

	if (!value)
		throw new Error(msg + ' not defined');
}

/**
 * Обрабатывает произошедшее исключение.
 * @param msg сообщение ошибки
 */
Kefir.showError = function(msg) {
	alert(msg);
}

Kefir.loadJsFile = function(filename, successHandler) {
	Kefir.loadJsCssFile(filename, 'js', successHandler);
}

Kefir.loadCssFile = function(filename, successHandler) {
	Kefir.loadJsCssFile(filename, 'css', successHandler);
}

var scripts = {};
Kefir.loadJsCssFile = function(filename, filetype, successHandler) {
	if (scripts[filename])
	{
		successHandler();
		return;
	}

	var head = document.getElementsByTagName('head')[0];

	scripts[filename] = filename;
	var fileref;
	switch (filetype) {
		case 'js':
			fileref = document.createElement('script');
			fileref.setAttribute('charset', 'UTF-8');
			fileref.setAttribute('src', filename);
			fileref.setAttribute('type', 'text/javascript');
			fileref.onload = fileref.onreadystatechange = function() {
				successHandler();
				head.removeChild(fileref);
			};
			break;

		case 'css':
			fileref = document.createElement('link');
			fileref.setAttribute('rel', 'stylesheet');
			fileref.setAttribute('type', 'text/css');
			fileref.setAttribute('href', filename);
			break;
	}

	if (typeof(fileref) != 'undefined')
		head.appendChild(fileref);
}