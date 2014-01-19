Ext.namespace('Kefir.form');

Kefir.form.ACTION = { CREATE: 0, SHOW: 1, UPDATE: 2, DELETE: 3 };
Kefir.form.MIN_YEAR = 1900;
Kefir.form.WINDOW_TITLE_FILTERS_SEPARATOR = ', ';

Kefir.form.yearValidator = function(value) {
	if (!value)
		return 'Это поле обязательно для заполнения';

	var currentYear = Kefir.getCurrentYear();
	if (value < Kefir.form.MIN_YEAR || value > currentYear)
		return 'Необходимо ввести год в пределах от ' + Kefir.form.MIN_YEAR + ' до ' + currentYear;

	return true;
}
Kefir.form.yearAllowEmptyValidator = function(value) {
	if (!value)
		return true;

	var currentYear = Kefir.getCurrentYear();
	if (value < Kefir.form.MIN_YEAR || value > currentYear)
		return 'Необходимо ввести год в пределах от ' + Kefir.form.MIN_YEAR + ' до ' + currentYear;

	return true;
}

/**
 * @param form форма
 * @param fieldName имя поля
 * @return значение поля указанной формы с указанным именем
 */
Kefir.form.getFieldValue = function(form, fieldName) {
	if (!form)
	{
		alert('form not defined for Kefir.form.getFieldValue');
		return undefined;
	}
	if (!fieldName)
	{
		alert('fieldName not defined for Kefir.form.getFieldValue');
		return undefined;
	}

	var field = form.findField(fieldName);
	if (!field)
	{
		alert('field with name "' + fieldName + '" not found in form');
		return undefined;
	}

	return field.getValue();
}

/**
 * @param gridPanel панель с таблицей, содержащей столбец чекбоксов
 * @return сформированный для параметра сервлета параметр разделенных через запятую кодов (id) записей, выбранных в таблице
 */
Kefir.form.getSelectedIds = function(gridPanel) {
	var selectedIds = '';
	var selections = gridPanel.getSelectionModel().getSelections();
	for (var i = 0; i < gridPanel.getSelectionModel().getCount(); i++)
		selectedIds += (selections[i].json['id'] + ', ');

	selectedIds = selectedIds.substring(0, selectedIds.length - 2); // remove trailing ', '
	return selectedIds;
}

/**
 * Устанавливает значение поля формы значением поля записи с тем же именем. 
 * @param form форма
 * @param record запись
 * @param fieldName если передан строка — имя поля (одинаковое и для записи, и для формы); <br/>	если передан массив, то 0й элемент — имя поля формы, 1й элемент — имя поля сущности, из которого заполняется поле 
 * @param renderer рендерер для значения, записываемого в поле, может быть равен null.
 */
Kefir.form.fillFormField = function(form, record, fieldName, renderer) {
	if (!form || !record)
		return;

	var formFieldName;
	var recordFieldName;

	if (fieldName instanceof Array)
	{ // 0й элемент — имя поля формы, 1й элемент — имя поля сущности
		formFieldName = fieldName[0];
		recordFieldName = fieldName[1];
	}
	else
	{ // одинаковое имя поля сущности и формы
		formFieldName = fieldName;
		recordFieldName = fieldName;
	}

	var value = Kefir.getValue(record, recordFieldName);
	if (renderer)
		value = renderer(value);

	form.findField(formFieldName).setValue(value);
}

/**
 * Заполняет поля указанной формы значениями полей указанной записи, 
 * проходя по указанному массиву соответствий имен полей формы и имен полей записи.
 * @param form форма
 * @param record запись
 * @param fields массив соответствий имен полей формы именам полей записи, в 3м поле массива указан рендерер, который применяется, если указан.  
 */
Kefir.form.fillFormFields = function(form, record, fields) {
	var i;
	var value;
	var field;
	var renderer;

	for (i = 0; i < fields.length; i++)
	{
		value = record.get ? record.get( fields[i][1] ) : record[ fields[i][1] ];

		field = form.findField(fields[i][0])
		if (field)
		{
			renderer = fields[i][2];
			field.setValue(renderer ? renderer(value) : value);
		}
	}
}

/**
 * Очищает поля указанной формы.
 * @param form форма
 * @param fields массив имен полей, которые нужно очистить
 */
Kefir.form.clearFormFields = function(form, fields) {
	var i;
	var field;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i])
		if (field)
		{
			if (field.setValue)
				field.setValue(null);

			if (field.clearValue)
				field.clearValue();
		}
	}
}

/**
 * Делает нередактируемыми указаннные поля указанной формы.
 * @param form форма
 * @param fields массив имен полей формы
 */
Kefir.form.disableFormFields = function(form, fields) {
	var i;
	var field;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i][0])
		if (field)
			Kefir.form.disableField(field);
	}
}

Kefir.form.disableValidFormFields = function(form, fields) {
	var i;
	var field;
	var checkboxField;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i][0])
		if (!field)
			continue;

		if (field.aggrType)
			checkboxField = form.findField( 'no' + Kefir.StringUtils.capitalize(field.getName()) );

		Kefir.form.enableField(field, fields[i][1]);
		if (field.aggrType)
			Kefir.form.enableField(checkboxField); // для агрегатных полей доступными становятся их чекбоксы

		if ( field.textFieldId && !Ext.getCmp(field.textFieldId).isValid() )
			continue; // чекбокс для невалидного поля агрегата остается доступным для редактирования 

		if (field.isValid() || (field.aggrType && ( field.getValue() == Kefir.registration.vehicle.EMPTY_AGGR_NUM_VALUE ))) // ном. отс. в номерах агрегатов считаем валидным
		{
			Kefir.form.disableField(field);
			if (field.aggrType)
				Kefir.form.disableField(checkboxField); // для агрегатных полей недоступными становятся и их чекбоксы
		}
	}
}

/**
 * Делает редактируемыми указаннные поля указанной формы.
 * @param form форма
 * @param fields массив имен полей формы, включающий их значения allowBlank.
 */
Kefir.form.enableFormFields = function(form, fields) {
	var i;
	var field;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i][0])
		if (field)
			Kefir.form.enableField(field, fields[i][1]);
	}
}

/**
 * Прячет указаннные поля указанной формы.
 * @param form форма
 * @param fields массив имен полей формы, включающий их значения allowBlank
 */
Kefir.form.hideFormFields = function(form, fields) {
	var i;
	var field;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i][0])
		if (field)
			Kefir.form.hideField(field);
	}
}

/**
 * Показывает указаннные поля указанной формы.
 * @param form форма
 * @param fields массив имен полей формы, включающий их значения allowBlank
 */
Kefir.form.showFormFields = function(form, fields) {
	var i;
	var field;

	for (i = 0; i < fields.length; i++)
	{
		field = form.findField(fields[i][0])
		if (field)
			Kefir.form.showField(field, fields[i][1]);
	}
}

/**
 * Заполняет указанные датовые поля записи датами, выполняя парсинг строк в полях с указанным именем.
 * @param record запись
 * @param dateFields массив, содержащий названия датовых полей 
 */
Kefir.form.fillDateFields = function(record, dateFields) {
	var fieldName;

	for (var i = 0; i < dateFields.length; i++)
	{
		fieldName = dateFields[i];
		record.set( fieldName, Kefir.getDate(record.get(fieldName)) );
	}
}

/**
 * Возвращает хэш. Для указанных чекбокс полей, если их значение false, в хэше указана пара { "имя поля": false }.
 * @param form форма
 * @param fieldsNames массив имен полей чекбоксов, которые необходимо проверить
 */
Kefir.form.getNegativeCheckboxParams = function(form, fieldsNames) {
	var params = {};

	for (var i = 0; i < fieldsNames.length; i++)
	{
		var field = form.findField(fieldsNames[i]);
		if (field && !field.getValue())
			params[fieldsNames[i]] = false;
	}

	return params;
}

Kefir.form.disableField = function(field) {
	field.setDisabled(true);
	field.allowBlank = true;
	field.validateValue(field.getValue()); // force update
	field.clearInvalid();
}
Kefir.form.enableField = function(field, allowBlank) {
	field.setDisabled(false);
	field.allowBlank = allowBlank;
	field.validateValue(field.getValue()); // force update
	field.clearInvalid();
}

Kefir.form.hideField = function(field) { // todo: move to Kefir.FormPanel.panel if easy and possible
	if (!field)
		return;

	field.setVisible(false);
	field.allowBlank = true;
	field.validateValue(field.getValue()); // force update
	field.clearInvalid();
	
	if (field.container.up('div.x-form-item'))
		field.container.up('div.x-form-item').hide(); // hide label
}
Kefir.form.hideFieldCompletely = function(field) { // todo: move to Kefir.FormPanel.panel if easy and possible
	if (!field)
		return;

	Kefir.form.hideField(field);

	if (field.container.up('div.x-form-item'))
	{
		var containerDiv = field.container.up('div.x-form-item');
		containerDiv.hide();
		containerDiv.setDisplayed(false);
	}
}
Kefir.form.showField = function(field, allowBlank) {
	if (!field)
		return;

	field.setVisible(true);
	field.allowBlank = allowBlank;
	field.validateValue(field.getValue()); // force update
	field.clearInvalid();

	if (field.container.up('div.x-form-item'))
		field.container.up('div.x-form-item').show(); // show label
}
Kefir.form.showFieldCompletely = function(field, allowBlank) {
	if (!field)
		return;

	Kefir.form.showField(field, allowBlank);

	var containerDiv = field.container.up('div.x-form-item');
	containerDiv.show();
	containerDiv.setDisplayed(true);
}

Kefir.form.setFieldVisibility = function(field, isVisible, require) {
	if (!field)
		return;

	field.setDisabled(!isVisible);
	field.setVisible(isVisible);
	field.allowBlank = !require;

	var container = field.container.up('div.x-form-item');
	if (container)
	{
		container.setDisplayed(isVisible);
		container.dom.firstChild.innerHTML = Kefir.getFieldLabel(field.fieldLabel, !require) + ':';
	}
}

Kefir.form.changeTextFieldState = function(checked, textFieldId, textFieldAllowBlank, inverse) {
	var disable = inverse ? !checked : checked;

	if (disable)
		Kefir.form.disableField(Ext.getCmp(textFieldId)) // check -> disable field // todo: set 'textFieldId' and textFieldAllowBlank as fields in checkbox connected
	else
		Kefir.form.enableField(Ext.getCmp(textFieldId), textFieldAllowBlank) // uncheck -> enable field
}

Kefir.form.hidePanel = function(panel) { // todo: move to Kefir.FormPanel.panel if easy and possible
	panel.setVisible(false);
	var containerDiv = panel.getEl();
	containerDiv.hide(); // hide label
	containerDiv.setDisplayed(false); // hide label
}
Kefir.form.showPanel = function(panel) { // todo: move to Kefir.FormPanel.panel if easy and possible
	panel.setVisible(true);
	var containerDiv = panel.getEl();
	containerDiv.show(); // hide label
	containerDiv.setDisplayed(true); // hide label
}

Kefir.form.Panel = Ext.extend(Ext.Panel, {
	constructor: function(config) {
		Ext.apply(this, {
			border: false,
			items: config.items
		})

		Kefir.form.Panel.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getPanel = function(config, width, items) {
	Ext.apply(config, {
		width: width,
		items: [ items ]
	});

	return new Kefir.form.Panel(config);
}

Kefir.form.FormPanel = Ext.extend(Ext.Panel, {
	constructor: function(config) {
		Ext.apply(this, {
			layout: 'form',
			border: false,
			labelWidth: config.labelWidth,
			items: config.items
		});
		
		Kefir.form.FormPanel.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
})

Kefir.form.getFormPanel = function(config, labelWidth, items) {
	Ext.apply(config, {
		labelWidth: labelWidth,
		items: [ items ]
	});

	return new Kefir.form.FormPanel(config);
}

Kefir.form.ColumnPanel = Ext.extend(Ext.Panel, {
	constructor: function(config) {
		Ext.apply(this, {
			layout: 'column',
			border: false,
			items: config.items
		});

		Kefir.form.ColumnPanel.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getColumnPanel = function(config, items) {
	Ext.apply(config, {
		items: [ items ]
	});
	
	return new Kefir.form.ColumnPanel(config);
}

Kefir.form.TextField = Ext.extend(Ext.form.TextField, {
	constructor: function(config) {
		if (config.style && !config.style.textTransform)
			Ext.apply(config.style, { textTransform: 'uppercase' });

		Ext.apply(this, {
			labelStyle: 'text-align: right',
			style: { textTransform: 'uppercase' },
			autoCreate: { tag: 'input', type: 'text', maxLength: config.maxLength }
		});

		Kefir.form.TextField.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});
Kefir.form.getTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: maxLength,
		allowBlank: allowBlank
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.TextArea = Ext.extend(Ext.form.TextArea, {
	constructor: function(config){
		Ext.apply(this, {
			labelStyle: 'text-align: right',
			autoCreate: { tag: 'textarea', type: 'text', maxLength: config.maxLength, rows: config.rows, cols: config.cols }
		});

		Kefir.form.TextArea.superclass.constructor.apply(this, arguments);
	}
});
Kefir.form.getTextArea = function(config, id, name, fieldLabel, width, maxLength, rows, cols, allowBlank) {
	Ext.apply(config, {
			id: id,
			name: name,
			fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
			width: width,
			maxLength: maxLength,
			rows: rows,
			cols: cols,
			allowBlank: allowBlank
		}
	);

	return new Kefir.form.TextArea(config);
}

Kefir.form.getLicenceCategoryField = function(config, id, name, fieldLabel, allowBlank, category, isTmp) {
	Ext.apply(config, {
		readOnly: true,
		value: category,
		listeners: {
			focus: function(f) {
				var windowId = 'chooseLicenceCategoryWindow';
				var items;
				if (!isTmp)
				{
					items = [
						Kefir.form.getCheckbox({}, windowId + 'AI', 'AI', 'AI', true),
						Kefir.form.getCheckbox({}, windowId + 'AII', 'AII', 'AII', true),
						Kefir.form.getCheckbox({}, windowId + 'AIII', 'AIII', 'AIII', true),
						Kefir.form.getCheckbox({}, windowId + 'AIV', 'AIV', 'AIV', true),
						Kefir.form.getCheckbox({}, windowId + 'B', 'B', 'B', true),
						Kefir.form.getCheckbox({}, windowId + 'C', 'C', 'C', true),
						Kefir.form.getCheckbox({}, windowId + 'D', 'D', 'D', true),
						Kefir.form.getCheckbox({}, windowId + 'E', 'E', 'E', true),
						Kefir.form.getCheckbox({}, windowId + 'F', 'F', 'F', true)
					];
				}
				else
				{
					items = [
						Kefir.form.getCheckbox({}, windowId + 'B', 'B', 'B', true),
						Kefir.form.getCheckbox({}, windowId + 'C', 'C', 'C', true),
						Kefir.form.getCheckbox({}, windowId + 'D', 'D', 'D', true),
						Kefir.form.getCheckbox({}, windowId + 'E', 'E', 'E', true),
						Kefir.form.getCheckbox({}, windowId + 'F', 'F', 'F', true)
					];
				}

				var fieldsIds = Kefir.getFormFieldsIds(items);
				var chooseButtonId = windowId + 'ChooseButton';
				fieldsIds.push(chooseButtonId);

				category = f.getValue();
				if (category)
				{
					var values = category.split(', ');//parse new value "AI, AII, AIII, AIV, B, C, D, E, F"
					if (
						values.length == 1 && values[0].length == category.length
						&& !( category == 'AI' || category == 'AII' || category == 'AIII' || category == 'AIV' )
					)
						values = category.split('');//parse old value "ABCDEF"

					for (var i = 0; i < values.length; i++)
					{
						var cmp = Ext.getCmp(windowId + values[i]);
						if (cmp)
							cmp.setValue(true);
					}
				}

				var form = Kefir.getFormPanel({ bbar: null }, null, fieldsIds, Kefir.form.getFormPanel({}, 80, items), null, null);
				var window = Kefir.getFormWindow({}, form, windowId, 200, 'Выбор категорий', null);
				window.addButton(new Ext.Button({
					id: chooseButtonId,
					text: 'Выбрать',
					handler: function() {
						var value = '';
						for (var i = 0; i < items.length; i++)
						{
							var cmp = items[i];
							if (cmp.getValue())
								value += cmp.fieldLabel + ', ';
						}
						f.setValue(value.substring(0, value.length - 2));
						window.close();
						Kefir.focusNext(f);
					}
				}));
				window.addButton(Kefir.getCancelButton(window));
				window.show();
			}
		}
	});

	return Kefir.form.getTextField(config, id, name, fieldLabel, 190, 33, allowBlank);
}

Kefir.form.getRusTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		vtype: 'rus'
	});

	return Kefir.form.getTextField(config, id, name, fieldLabel, width, maxLength, allowBlank);
}

Kefir.form.getRusNameTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		vtype: 'rusName'
	});

	return Kefir.form.getTextField(config, id, name, fieldLabel, width, maxLength, allowBlank);
}

Kefir.form.getRusTextAndNumbersField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		vtype: 'rusAndNumbers'
	});

	return Kefir.form.getTextField(config, id, name, fieldLabel, width, maxLength, allowBlank);
}

Kefir.form.getReadOnlyTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		readOnly: true,
		disabled: true
	});

	return Kefir.form.getTextField(config, id, name, fieldLabel, width, maxLength, allowBlank);
}

Kefir.form.getSeriesTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: 2, // todo: 2 - constant
		allowBlank: allowBlank,
		vtype: config.vtype || 'series'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.getAxleNumberTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	function checkAxleNumbers(field) {
		var transit = Ext.getCmp('transit-axleNumber1');

		var id1;
		var id2;
		var id3;

		if (transit)
		{
			id1 = 'transit-axleNumber1';
			id2 = 'transit-axleNumber2';
			id3 = 'transit-axleNumber3';
		}
		else
		{
			id1 = 'vehicle-axleNumber1';
			id2 = 'vehicle-axleNumber2';
			id3 = 'vehicle-axleNumber3';
		}

		var field1 = Ext.getCmp(id1);
		var field2 = Ext.getCmp(id2);
		var field3 = Ext.getCmp(id3);

		var number1 = field1.getValue().trim();
		var number2 = field2.getValue().trim();
		var number3 = field3.getValue().trim();

		var errorMsg = '';

		if (number1 && (number1 == number2))
		{
			errorMsg = 'Номера мостов 1 и 2 совпадают';
		}
		if (number1 && (number1 == number3))
		{
			errorMsg = 'Номера мостов 1 и 3 совпадают';
		}
		if (number2 && (number2 == number3))
		{
			errorMsg = 'Номера мостов 2 и 3 совпадают';
		}

		if ( number1 && (number1 == number2) && (number1 == number3) )
		{
			errorMsg = 'Номера мостов 1, 2 и 3 совпадают';
		}

		if (errorMsg)
		{
			Ext.Msg.alert('Внимание', errorMsg, function() { // выдать предупреждение, если какие-то из номеров мостов совпали
				Kefir.focusNext(field);
			});
		}
	}

	var field = Kefir.form.getTextField(config, id, name, fieldLabel, width, maxLength, allowBlank);
	field.on('blur', checkAxleNumbers);

	return field;
}

Kefir.form.getInnPhysicalTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: Kefir.vtype.INN_PHYSICAL_LENGTH,
		allowBlank: allowBlank,
		vtype: 'innPhysical'
	});

	return new Kefir.form.TextField(config);
}
Kefir.form.getInnJuridicalTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: Kefir.vtype.INN_JURICAL_LENGTH,
		allowBlank: allowBlank,
		vtype: 'innJuridical'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.getIMNSTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: 4, // todo: 4 - constant
		allowBlank: allowBlank,
		vtype: 'imns'
	});

	return new Kefir.form.TextField(config);
}
Kefir.form.getOgrnTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: Kefir.vtype.OGRN_LENGTH,
		allowBlank: allowBlank,
		vtype: 'ogrn'
	});

	return new Kefir.form.TextField(config);
}
Kefir.form.getKppTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: Kefir.vtype.KPP_LENGTH,
		allowBlank: allowBlank,
		vtype: 'kpp'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.getGosNumberTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: 4, // todo: 4 - constant
		allowBlank: allowBlank,
		vtype: 'productionGosNumber'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.getNumberTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: 6, // todo: 6 - constant
		allowBlank: allowBlank,
		vtype: 'productionNumber'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.getLoginTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		allowBlank: allowBlank,

		style: { textTransform: 'none' },
		maxLength: config.maxLength || 32, // todo: 32 - constant
		vtype: 'alphanum' // allow only english letters, digits and underline
	});

	return new Kefir.form.TextField(config);
}
Kefir.form.getPasswordTextField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		allowBlank: allowBlank,

		style: { textTransform: 'none' },
		maxLength: config.maxLength || 32, // todo: 32 - constant
		inputType: 'password'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.NumberField = Ext.extend(Ext.form.NumberField, {
	constructor: function(config) {
		Ext.apply(this, {
			decimalSeparator: ',',
			allowNegative: config.allowNegative || false,
			minValue: config.allowZero ? 0.00 : 0.01,
			labelStyle: 'text-align: right',
			autoCreate: { tag: 'input', type: 'text', maxLength: config.maxLength }
//			vtype: 'number'
			// todo: restrict fraction to 2 digits
		})

		Kefir.form.NumberField.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getNumberField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: maxLength,
		allowBlank: allowBlank
	});

	return new Kefir.form.NumberField(config);
}

Kefir.form.getBrandCodeField = function(config, id, name, fieldLabel, width, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		allowBlank: allowBlank,
		maxLength: 5,
		vtype: 'brandCode'
	});

	return new Kefir.form.TextField(config);

}
Kefir.form.getPositiveIntegerTextField = function(config, id, name, fieldLabel, width, maxLength, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		width: width,
		maxLength: maxLength,
		allowBlank: allowBlank,
		vtype: 'positiveNum'
	});

	return new Kefir.form.TextField(config);
}

Kefir.form.Radio = Ext.extend(Ext.form.Radio, {
	constructor: function(config) {

		Ext.apply(this, {
			labelStyle: 'text-align: right'
		});

		Kefir.form.Radio.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getRadio = function(config, id, name, fieldLabel, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		allowBlank: allowBlank
	});

	return new Kefir.form.Radio(config);
}

Kefir.form.Checkbox = Ext.extend(Ext.form.Checkbox, {
	constructor: function(config) {

		Ext.apply(this, {
			labelStyle: 'text-align: right'
		});

		Kefir.form.Checkbox.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getCheckbox = function(config, id, name, fieldLabel, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		allowBlank: allowBlank
	});

	return new Kefir.form.Checkbox(config);
}
Kefir.form.getReadOnlyCheckbox = function(config, id, name, fieldLabel, allowBlank) {
	Ext.apply(config, {
		readOnly: true,
		disabled: true
	});

	return Kefir.form.getCheckbox(config, id, name, fieldLabel, allowBlank);
}
Kefir.form.getLinkedCheckbox = function(config, id, name, fieldLabel, allowBlank, textFieldId, textFieldAllowBlank, inverse) {
	Ext.apply(config, {
		textFieldId: textFieldId,
		listeners:
		{
			check: function(checkbox, checked) {
				Kefir.form.changeTextFieldState(checked, textFieldId, textFieldAllowBlank, inverse);

				if (checked && !inverse)
					Ext.getCmp(textFieldId).setValue(Kefir.registration.vehicle.EMPTY_AGGR_NUM_VALUE);
				else
					Ext.getCmp(textFieldId).setValue(config.emptyValue || null);

				if (textFieldId.indexOf('vehicle-axleNumber') == 0)
				{
					Ext.getCmp('vehicle-axleNumber1').validate();
					Ext.getCmp('vehicle-axleNumber2').validate();
					Ext.getCmp('vehicle-axleNumber3').validate();
				}
			}
		}
	});

	return Kefir.form.getCheckbox(config, id, name, fieldLabel, allowBlank);
}

Kefir.form.setAggrFieldValue = function(form, fieldName, checkboxFieldName, value) {
	form.findField(fieldName).setValue(value ? value : Kefir.registration.vehicle.EMPTY_AGGR_NUM_VALUE);
	form.findField(checkboxFieldName).setValue(!value);
}
Kefir.form.setReadOnlyAggrFieldValue = function(form, fieldName, checkboxFieldName, value) {
	form.findField(checkboxFieldName).setValue(false); // иначе при смене чекбокса с true на false поле будет чиститься
	form.findField(fieldName).setValue(value ? value : Kefir.registration.vehicle.EMPTY_AGGR_NUM_VALUE);
	form.findField(checkboxFieldName).setValue(!value);
}

Kefir.form.DateField = Ext.extend(Ext.form.DateField, {
	constructor: function(config) {
		Ext.apply(this, {
			xtype: 'datefield',
			format: 'd.m.Y',
			labelStyle: 'text-align: right',
			maxLength: 10,
			maxValue: new Date(),

			plugins: [ new Ext.ux.InputTextMask('99.99.9999', true) ],

			setCaretPos: function(pos) {
				var obj = document.getElementById(this.id);
				if (obj.createTextRange)
				{
					var range = obj.createTextRange();
					range.move('character', pos);
					range.select();
				}
				else if (obj.selectionStart)
				{
					obj.focus();
					obj.setSelectionRange(pos, pos);
				}
			}
		});
		
		Kefir.form.DateField.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});
Kefir.form.getDateField = function(config, id, name, fieldLabel, allowBlank) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		allowBlank: allowBlank,
		isDateField: true
	});

	var dateField = new Kefir.form.DateField(config);
	dateField.on('blur', function(field) {
		if ( !field.isValid() )
			return;

		field.fireEvent('select', field);
	});
	dateField.on('focus', function(field) { // если фокусируется пустое поле, то курсор надо ставить в его начало, а не конец
		field.wasFocused = true;
		if (field.getRawValue() == '__.__.____')
			field.setCaretPos(0);
	});

	return dateField;
}

Kefir.form.getReadOnlyDateField = function(config, id, name, fieldLabel, allowBlank) {
	Ext.apply(config, {
		readOnly: true,
		disabled: true
	});

	return Kefir.form.getDateField(config, id, name, fieldLabel, allowBlank);
}

Kefir.form.ComboBox = Ext.extend(Ext.form.ComboBox, {
	constructor: function(config) {
		Ext.apply(this, {
			valueField: 'id',
			displayField: 'name',
			labelStyle: 'text-align: right;',

			editable: true,
			minChars: 0,
			typeAhead: Kefir.IS_GTN_PROJECT,
			queryParam: Kefir.IS_GTN_PROJECT ? 'nameQuery' : 'name',

			style: { textTransform: 'uppercase' },
			autoCreate: { tag: 'input', type: 'text', maxLength: config.maxLength },

			mode: 'remote',
			triggerAction: 'all',

			store: new Ext.data.JsonStore({
				url: Kefir.contextPath + config.url,
				baseParams: config.params,
				method: 'POST',
				root: 'results',
				totalProperty: 'total',
				autoLoad: !config.noAutoLoad,
				fields: config.fields || [ { name: 'id', type: 'int' }, { name: 'name', type: 'string' } ]
			})
		});

		Kefir.form.ComboBox.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getComboBox = function(config, url, id, name, hiddenName, fieldLabel, width, listWidth, maxLength, allowBlank, forceSelection) {
	Ext.apply(config, {
		url: url,
		id: id,
		name: name,
		hiddenName: hiddenName,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		allowBlank: allowBlank,
		width: width,
		listWidth: listWidth,
		maxLength: maxLength,
		forceSelection: forceSelection
	});

	return new Kefir.form.ComboBox(config);
}

Kefir.form.LocalComboBox = Ext.extend(Ext.form.ComboBox, {
	constructor: function(config) {
		Ext.apply(this, {
			valueField: 'id',
			displayField: 'name',
			labelStyle: 'text-align: right;',

			editable: true,
			minChars: 0,
			typeAhead: true,

			style: { textTransform: 'uppercase' },
			autoCreate: { tag: 'input', type: 'text', maxLength: config.maxLength },

			mode: 'local',
			triggerAction: 'all',

			store: config.store
		});

		Kefir.form.LocalComboBox.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getLocalComboBox = function(config, store, id, name, hiddenName, fieldLabel, width, listWidth, maxLength, allowBlank, forceSelection) {
	Ext.apply(config, {
		store: store,
		id: id,
		name: name,
		hiddenName: hiddenName,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, allowBlank),
		allowBlank: allowBlank,
		width: width,
		listWidth: listWidth,
		maxLength: maxLength,
		forceSelection: forceSelection
	});

	var combo = new Kefir.form.LocalComboBox(config);

	combo.addListener({
		specialkey: function(field, e) {
			if (e.getKey() == e.ENTER)
				this.triggerBlur();
		}
	});

	return combo;
}

Kefir.form.Label = Ext.extend(Ext.form.Label, {
	constructor: function(config) {
		Ext.apply(this, {
			cls: 'x-form-item'
		});

		Kefir.form.Label.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getLabel = function(config, text, width) {
	Ext.apply(config, {
		text: text,
		width: width
	});

	return new Kefir.form.Label(config);
}

Kefir.form.getCenteredLabel = function(config, text, width) {
	Ext.apply(config, {
		style: 'text-align: center;'
	});

	return Kefir.form.getLabel(config, text, width);
}

Kefir.form.Tip = Ext.extend(Ext.QuickTip, {
	constructor: function(config) {
		Ext.apply(this, {
			dismissDelay: 6000,
			width: 150,
			height: 50
		});

		Kefir.form.Tip.superclass.constructor.apply(this, arguments);
	}
});

Kefir.getTip = function(config, target, title, html) {
	Ext.apply(config, {
		target: target,
		title: title,
		html: html
	});

	return new Kefir.form.Tip(config);
}

Kefir.form.SpinnerField = Ext.extend(Ext.ux.form.SpinnerField, {
	constructor: function(config) {
		Ext.apply(this, {
			allowBlank: config.allowBlank || false,

			minValue: config.minValue || 0,
			maxValue: config.maxValue || 99,

			width: config.width || 40,
			maxLength: config.maxLength || 2,

			allowNegative: config.allowNegative || false,

			validateValue: config.validateValue || Ext.emptyFn, // отключить валидацию по дефолту (для тулбарных фильтров региона и инспекции) // todo: do not use such bydlocode, because it makes the field always invalid for forms
			enableKeyEvents: true,

			onBlur: function() {
				var val = this.getValue();
				var startValue = this.startValue;
				if (val !== startValue)
				{
					this.fireEvent('change', this, val, startValue);
					this.startValue = val;
				}
			},

			setRawValue : function(v) {
				var newVal = Ext.ux.form.SpinnerField.superclass.setRawValue.call(this, v);
				var val = this.getValue();
				var startValue = this.startValue;
				if (val !== startValue)
				{
					this.fireEvent('change', this, val, startValue);
					this.startValue = val;
				}

				return(newVal);
			},

			listeners: {
				specialkey: function(spinner, e) { // onSpecialkey and onSpecialKey do not work in constructor somewhy
					if (e.getKey() == e.ENTER)
					{
						var val = this.getValue();
						var startValue = this.startValue;
						if (val !== startValue)
						{
							this.fireEvent('change', this, val, startValue);
							this.startValue = val;
						}
					}
				},

				keydown: function(field, event) {
					var value = field.getValue();
					var charCode = event.getCharCode();

					if (Kefir.StringUtils.isDigit(charCode))
					{
						event.stopPropagation();
						event.stopEvent();

						if (field.getValue().toString().length >= this.maxLength)
							return;

						field.setValue(value + Kefir.StringUtils.getDigit(charCode));
					}
				}
			}
		});

		if (config.validateInspection)
		{ // если нужна валидация инспекции (учет некорректных 0 и пустого значения) 
			Ext.apply(this, {
				validateValue: function(value) {
					// для других инспекций можно указывать 0 в качестве номера региона
					if (this.regionFieldId)
					{ // указано связанное поле с номером региона -> можно делать проверки, зависящие от региона
						var regionField = Ext.getCmp(this.regionFieldId);
						if ( regionField && (regionField.getValue() != Kefir.getCurrentRegion()) && (this.getValue() === 0) )
						{ // установлен нетекущий регион и инспекция равна 0 -> допустимый случай 
							this.clearInvalid();
							return true;
						}

						if (this.denyCurrentInspection)
						{ // если запрещен выбор текущей инспекции
							if ( regionField && (regionField.getValue() == Kefir.getCurrentRegion()) && (this.getValue() == Kefir.getCurrentInspection()) )
							{ // указан текущий регион и текущая инспекция -> показать ошибку
								this.markInvalid('Нельзя указывать текущую инспекцию');
								return false;
							}
						}
					}

					if (value <= this.minValue || value > this.maxValue)
					{
						this.markInvalid('Неверно указан номер инспекции');
						return false;
					}

					this.clearInvalid();
					return true;
				},

				clearValidation: function() {
					Ext.destroyMembers(this, 'validateValue');
					this.clearInvalid();
				}
			});
		}
		else if (config.validateRegion)
		{ // если нужна валидация региона (учет некорректных 0 и пустого значения)
			Ext.apply(this, {
				validateValue: function(value) {
					if (this.inspectionFieldId)
					{ // указано связанное поле с номером инспекции -> нужно провалидировать и его (его валидность может зависеть от номера региона)
						var inspectionField = Ext.getCmp(this.inspectionFieldId);
						if (inspectionField)
							inspectionField.validateValue(inspectionField.getValue());
					}

					if (this.denyCurrentRegion)
					{ // запрещен выбор текущего региона
						if (this.getValue() == Kefir.getCurrentRegion())
						{ // указан номер текущего региона -> показать ошибку
							this.markInvalid('Нельзя указывать текущий регион');
							return false;
						}
					}

					if (value <= this.minValue || value > this.maxValue)
					{
						this.markInvalid('Неверно указан номер региона');
						return false;
					}

					this.clearInvalid();
					return true;
				},

				clearValidation: function() {
					Ext.destroyMembers(this, 'validateValue');
					this.clearInvalid();
				}
			});
		}
		else
		{
			Ext.apply(this, {
				validateValue: function(value) {
					if (!this.allowBlank && !value)
					{
						this.markInvalid('Это поле обязательно для заполнения');
						return false;
					}

					if ( value && !(value >= this.minValue && value <= this.maxValue) )
					{
						this.markInvalid('Необходимо ввести значение от ' + this.minValue + ' до ' + this.maxValue);
						return false;
					}

					this.clearInvalid();
					return true;
				}
			});
		}

		Kefir.form.SpinnerField.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getSpinnerField = function(config, id, name, fieldLabel, value) {
	Ext.apply(config, {
		id: id,
		name: name,
		fieldLabel: Kefir.getFieldLabel(fieldLabel, config.allowBlank || false),
		labelStyle: 'text-align: right',
		value: value
	});

	return new Kefir.form.SpinnerField(config);
}

Kefir.form.getReadOnlySpinnerField = function(config, id, name, fieldLabel, value) {
	Ext.apply(config, {
		readOnly: true,
		disabled: true
	});

	return Kefir.form.getSpinnerField(config, id, name, fieldLabel, value);
}

Kefir.form.getDisabledSpinnerField = function(config, value) {
	Ext.apply(config, {
		value: value,
		readOnly: true,
		disabled: true
	});

	return new Kefir.form.SpinnerField(config);
}

Kefir.form.getHiddenField = function(config, id, name) {
	Ext.apply(config, {
		id: id,
		name: name
	});

	return new Ext.form.Hidden(config);
}

Kefir.form.getValueOrEmpty = function(json, paramName) {
	return Kefir.form.getValue(json, paramName, true);
}
Kefir.form.getValue = function(json, paramName, returnEmpty) {
	if (!json)
		return returnEmpty ? '' : null;

	var value = json[paramName];
	if (!value)
		return returnEmpty ? '' : null;

	if (typeof(value) != 'string')
		return value;

	var date = Date.parseDate(value, 'c');
	return date ? date : value;
}

Kefir.form.setComboValue = function(combo, json, paramName) {
	var val = !paramName ? json : json[paramName];
	if (!val)
		return;
	
	var list = combo.store.data.items;
	for (var i = 0; i < list.length; i++)
	{
		var value = list[i].json;
		if (val == value[1])
		{
			Kefir.form.setValue(combo, value[0]);
			return;
		}
	}

	throw new Error('Can\'t set combo value');
}

/**
 * Устанавливает в комбобокс значение только при первой загрузке.
 * @param combo комбобокс
 * @param value значение
 */
Kefir.form.setComboBoxValue = function(combo, value) {
	var comboStore = combo.getStore();
	comboStore.loadCount = 0;
	comboStore.on('load', function(store) {
		if ( !store.loadCount )
			combo.setValue( value );

		store.loadCount++;
	});

	comboStore.load();
}

Kefir.form.setValue = function(comp, json, paramName) {
	function getValue(val) {
		return ( (typeof(val) == 'string') && (val.length >= 10) ) ? Date.parseDate(val, 'c') || val : val;
	}
	if (!paramName)
		return !json ? null : comp.setValue(getValue(json));

	var value = !json ? null : json[paramName];
	if (value)
		comp.setValue(getValue(value));
}

Kefir.form.getFieldSet = function(height, hidden, items) {
	return { xtype: 'fieldset', height: height, hidden: hidden, items: items }
};