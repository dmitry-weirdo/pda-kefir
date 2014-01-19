Ext.namespace('su.opencode.kefir.sampleSrv.address');

su.opencode.kefir.sampleSrv.address.Address = function() {
	var WINDOW_ID = 'addressWindow';
	var WINDOW_WIDTH = 800;

	var SAVE_WINDOW_TITLE = 'Адрес';

	var SAVE_BUTTON_ID = 'address-save';
	var CANCEL_BUTTON_ID = 'address-cancel';
	var FIELDS_IDS = [
		'address-zipCode', 'address-subject', 'address-district',
		'address-city', 'address-cityDistrict', 'address-locality',
		'address-street', 'address-block',
		'address-house', 'address-building', 'address-apartment', 
		SAVE_BUTTON_ID
	];

	var ADDRESS_FIELDS = [ // соответствие полей формы полям переданного для заполнения адреса
		[ 'id', 'id' ],
		[ 'zipCode', 'zipCode', Kefir.render.dontShowNilRender ],
		[ 'subject', 'subject' ],
		[ 'district', 'district' ],
		[ 'city', 'city' ],
		[ 'cityDistrict', 'cityDistrict' ],
		[ 'locality', 'locality' ],
		[ 'street', 'street' ],
		[ 'block', 'block' ],
		[ 'house', 'house' ],
		[ 'building', 'building' ],
		[ 'apartment', 'apartment' ]
	];

	var window;
	var panel;

	var windowTitle;
	var successHandler;
	var address; // адрес, который передается в форму для начального заполнения

	var building; // если true, то заполняется адрес здания, который не содержит: почтового индекса, корпуса и офиса
	var formFieldId; // id поля формы, которая вызвала форму адреса (кнопки изменения формы) . После закрытия формы выполняется focusNext для этого поля 

	function fillFields(address) {
		Kefir.form.fillFormFields(panel.getForm(), address, ADDRESS_FIELDS);
	}

	function getItems() {
		if (building)
		{ // адрес здания не содержит индекса, корпуса и офиса
			return [
				Kefir.form.getHiddenField({}, 'address-id', 'id'),
				Kefir.form.getTextField({ vtype: 'subject' }, 'address-subject', 'subject', 'Регион\\область', 350, 50, false),
				Kefir.form.getTextField({ vtype: 'district' }, 'address-district', 'district', 'Район области', 350, 50, true),
				Kefir.form.getTextField({ vtype: 'city' }, 'address-city', 'city', 'Город', 350, 50, true),
				Kefir.form.getTextField({ vtype: 'cityDistrict' }, 'address-cityDistrict', 'cityDistrict', 'Район города', 350, 50, true),
				Kefir.form.getTextField({ vtype: 'locality' }, 'address-locality', 'locality', 'Нас. пункт', 350, 50, true),
				Kefir.form.getTextField({ vtype: 'street' }, 'address-street', 'street', 'Улица', 350, 50, true),
				Kefir.form.getTextField({}, 'address-block', 'block', 'Квартал', 350, 50, true), // todo: vtype
				Kefir.form.getTextField( { vtype: 'house'}, 'address-house', 'house', 'Дом', 150, 15, true)
			];
		}

		return [
			Kefir.form.getHiddenField({}, 'address-id', 'id'),
			Kefir.form.getTextField({ vtype: 'zipCode' }, 'address-zipCode', 'zipCode', 'Почтовый индекс', 100, 6, true),
			Kefir.form.getTextField({ vtype: 'subject' }, 'address-subject', 'subject', 'Регион\\область', 350, 50, false),
			Kefir.form.getTextField({ vtype: 'district' }, 'address-district', 'district', 'Район области', 350, 50, true),
			Kefir.form.getTextField({ vtype: 'city' }, 'address-city', 'city', 'Город', 350, 50, true),
			Kefir.form.getTextField({ vtype: 'cityDistrict' }, 'address-cityDistrict', 'cityDistrict', 'Район города', 350, 50, true),
			Kefir.form.getTextField({ vtype: 'locality' }, 'address-locality', 'locality', 'Нас. пункт', 350, 50, true),
			Kefir.form.getTextField({ vtype: 'street' }, 'address-street', 'street', 'Улица', 350, 50, true),
			Kefir.form.getTextField({}, 'address-block', 'block', 'Квартал', 350, 50, true), // todo: vtype
			Kefir.form.getTextField( { vtype: 'house'}, 'address-house', 'house', 'Дом', 150, 15, true),
			Kefir.form.getTextField({ vtype: 'building' }, 'address-building', 'building', 'Корпус', 100, 5, true),
			Kefir.form.getTextField({ vtype: 'apartment' }, 'address-apartment', 'apartment', 'Офис', 100, 10, true)
		];
	}
	function getButtons() {
		var saveButton = {
			id: SAVE_BUTTON_ID,
			text: 'Сохранить',
			handler: function() {
				Kefir.checkForm(panel, function() {
					if (successHandler)
						successHandler(panel.getForm().getFieldValues());

					window.close();
				});
			}
		};

		var cancelButton = {
			id: CANCEL_BUTTON_ID,
			text: 'Отмена',
			handler: function() {
				window.close();
			}
		};

		return [ saveButton, cancelButton ];
	}

	function initPanel() {
		panel = new Kefir.FormPanel({
			fieldsIds: FIELDS_IDS,
			autoHeight: true,
			labelWidth: 150,

			items: getItems(),
			buttons: getButtons()
		});
	}
	function initWindow() {
	 	initPanel();

		window = new Ext.Window({
			id: WINDOW_ID,
			title: windowTitle,
			width: WINDOW_WIDTH,
			autoHeight: true,
			resizable: false,
			constrain: true,
			modal: true,
			layout: 'fit',

			items: [ panel ]
		});

		window.on('show', function() { panel.focusFirst() });
		
		if (address)
			window.on('show', function() { fillFields(address); } );

		window.on('close', function() {
			if (!formFieldId)
				return;

			Kefir.focusNext( Ext.getCmp(formFieldId) );
		});
	}
	function showWindow() {
		initWindow();
		window.show();
	}

	return {
		getAddress: function() {
			var form = panel.getForm();

			if (building)
				return	form.findField('subject').getValue() + ',' +
								form.findField('district').getValue() + ',' +
								form.findField('city').getValue() + ',' +
								form.findField('cityDistrict').getValue() + ',' +
								form.findField('locality').getValue() + ',' +
								form.findField('street').getValue() + ',' +
								form.findField('block').getValue() + ',' +
								form.findField('house').getValue();

			return	form.findField('zipCode').getValue() + ',' +
							form.findField('subject').getValue() + ',' +
							form.findField('district').getValue() + ',' +
							form.findField('city').getValue() + ',' +
							form.findField('cityDistrict').getValue() + ',' +
							form.findField('locality').getValue() + ',' +
							form.findField('street').getValue() + ',' +
							form.findField('block').getValue() + ',' +
							form.findField('house').getValue() + ',' +
							form.findField('building').getValue() + ',' +
							form.findField('apartment').getValue();
		},

		getAddressStr: function(address, building) {
			if (building)
			{ // сокращенный адрес для здания
				return	Kefir.getDefinedValue(address, 'subject') + ',' +
								Kefir.getDefinedValue(address, 'district') + ',' +
								Kefir.getDefinedValue(address, 'city') + ',' +
								Kefir.getDefinedValue(address, 'cityDistrict') + ',' +
								Kefir.getDefinedValue(address, 'locality') + ',' +
								Kefir.getDefinedValue(address, 'street') + ',' +
								Kefir.getDefinedValue(address, 'block') + ',' +
								Kefir.getDefinedValue(address, 'house');
			}

			// обычный полный адрес
			return	Kefir.getDefinedValue(address, 'zipCode') + ',' +
							Kefir.getDefinedValue(address, 'subject') + ',' +
							Kefir.getDefinedValue(address, 'district') + ',' +
							Kefir.getDefinedValue(address, 'city') + ',' +
							Kefir.getDefinedValue(address, 'cityDistrict') + ',' +
							Kefir.getDefinedValue(address, 'locality') + ',' +
							Kefir.getDefinedValue(address, 'street') + ',' +
							Kefir.getDefinedValue(address, 'block') + ',' +
							Kefir.getDefinedValue(address, 'house') + ',' +
							Kefir.getDefinedValue(address, 'building') + ',' +
							Kefir.getDefinedValue(address, 'apartment');
		},

		init: function(config) {
			windowTitle = config.windowTitle || SAVE_WINDOW_TITLE;
			successHandler = config.successHandler;
			address = config.address;
			building = config.building;
			formFieldId = config.formFieldId;

			showWindow();
		}
	}
}();