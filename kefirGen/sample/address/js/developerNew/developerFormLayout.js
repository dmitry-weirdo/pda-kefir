Ext.namespace('su.opencode.minstroy.ejb.leasing.developer');

su.opencode.minstroy.ejb.leasing.developer.getFormItemsLayout = function(config) {
	var juridicalAddressColumnPanel = ru.kg.gtn.form.getColumnPanel({ style: { padding: 5 }}, [
		ru.kg.gtn.form.getFormPanel({ width: 610 }, 150, [ config.juridicalAddressTextField ]),
		config.juridicalAddressUpdateButton
	]);

	var physicalAddressColumnPanel = ru.kg.gtn.form.getColumnPanel({ style: { padding: 5 }}, [
		ru.kg.gtn.form.getFormPanel({ width: 610 }, 150, [ config.physicalAddressTextField ]),
		config.physicalAddressUpdateButton			
	]);

	var licenceAttachmentsFieldSet = {
		xtype: 'fieldset',
		title: 'Сканы лицензии на осуществление вида деятельности',
		items: [ config.licenceAttachmentsPanel ]
	};

	var egrulAttachmentsFieldSet = {
		xtype: 'fieldset',
		title: 'Сканы свидетельства о включении в ЕГРЮЛ',
		items: [ config.egrulAttachmentsPanel ]
	};

	var fnsAttachmentsFieldSet = {
		xtype: 'fieldset',
		title: 'Сканы свидетельства о регистрации в ФНС',
		items: [ config.fnsAttachmentsPanel ]
	};

	var managerFieldSet = {
		xtype: 'fieldset',
		title: 'Руководитель',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.managerLastName ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 50, [ config.managerFirstName ]),
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.managerMiddleName ])
			])
		]
	};

	var contactFieldSet = {
		xtype: 'fieldset',
		title: 'Контактное лицо',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.contactLastName ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 50, [ config.contactFirstName ]),
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.contactMiddleName ])
			])
		]
	};

	var founderFieldSet = {
		xtype: 'fieldset',
		title: 'Учредитель',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.founderLastName ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 50, [ config.founderFirstName ]),
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.founderMiddleName ])
			])
		]
	};

	var bankFieldSet = {
		xtype: 'fieldset',
		title: 'Банковские реквизиты',
		items: [
			  config.bankName
			, config.bik
			, config.ogrn
			, config.inn
			, config.kpp
			, config.currentAccount
			, config.correspondentAccount
		]
	};

	var licenceFieldSet = {
		xtype: 'fieldset',
		title: 'Лицензия на осуществление деятельности',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.licenceNumber ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 100, [ config.licenceIssueDate ]),
				ru.kg.gtn.form.getFormPanel({ width: 750 }, 100, [ licenceAttachmentsFieldSet ])
			])
		]
	};

	var egrulFieldSet = {
		xtype: 'fieldset',
		title: 'Свидетельство о включении в ЕГРЮЛ',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.egrulNumber ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 100, [ config.egrulIssueDate ]),
				ru.kg.gtn.form.getFormPanel({ width: 750 }, 100, [ egrulAttachmentsFieldSet ])
			])
		]
	};

	var fnsFieldSet = {
		xtype: 'fieldset',
		title: 'Свидетельство о регистрации в ФНС',
		items: [
			ru.kg.gtn.form.getColumnPanel({}, [
				ru.kg.gtn.form.getFormPanel({ width: 250 }, 75, [ config.fnsNumber ]),
				ru.kg.gtn.form.getFormPanel({ width: 230 }, 100, [ config.fnsIssueDate ]),
				ru.kg.gtn.form.getFormPanel({ width: 750 }, 100, [ fnsAttachmentsFieldSet ])
			])
		]
	};

	return [
		  config.idHiddenField
		, config.name
		, config.shortName
		, juridicalAddressColumnPanel
		, physicalAddressColumnPanel
		, config.bankrupt
		, managerFieldSet
		, contactFieldSet
		, founderFieldSet
		, bankFieldSet
		, licenceFieldSet
		, egrulFieldSet
		, fnsFieldSet
	];
}