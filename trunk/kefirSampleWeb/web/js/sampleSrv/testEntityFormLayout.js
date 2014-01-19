Ext.namespace('su.opencode.kefir.sampleSrv.testEntity');

su.opencode.kefir.sampleSrv.testEntity.getFormItemsLayout = function(config) {
	var juridicalAddressColumnPanel = Kefir.form.getColumnPanel({ style: { padding: 5 } }, [
		Kefir.form.getFormPanel({ width: 610 }, 150, [ config.juridicalAddressTextField ]),
		config.juridicalAddressUpdateButton
	]);

	var physicalAddressColumnPanel = Kefir.form.getColumnPanel({ style: { padding: 5 } }, [
		Kefir.form.getFormPanel({ width: 610 }, 150, [ config.physicalAddressTextField ]),
		config.physicalAddressUpdateButton
	]);

	var chooseEntityFieldSet = {
		xtype: 'fieldset',
		title: 'Связанная сущность',
		layout: 'table',
		layoutConfig: { columns: 3 },

		items: [
			  Kefir.form.getFormPanel({ width: 450 }, 240, [ config.chooseEntityNameField ])
			, config.chooseEntityChooseButton
			, config.chooseEntityShowButton

			, Kefir.form.getFormPanel({ width: 440 }, 150, [ config.chooseEntityShortNameField ])
			, Kefir.form.getFormPanel({}, 1, [])
			, Kefir.form.getFormPanel({}, 1, [])

			, Kefir.form.getFormPanel({ width: 450 }, 240, [ config.chooseEntityCorrespondentAccountField ])
			, Kefir.form.getFormPanel({}, 1, [])
			, Kefir.form.getFormPanel({}, 1, [])
		]
	};

	var attachmentsFieldFieldSet = {
		xtype: 'fieldset',
		title: 'Вложения',
		items: [ config.attachmentsFieldPanel ]
	};

	var otherAttachmentsFieldFieldSet = {
		xtype: 'fieldset',
		title: 'Другие файловые вложения',
		items: [ config.otherAttachmentsFieldPanel ]
	};

	return [
		  config.idHiddenField
		, config.strField
		, config.intField
		, config.intSpinnerField
		, config.doubleField
		, config.dateField
		, config.booleanField
		, config.enumField
		, config.comboBoxEntity

		// fields of chooseEntity
		, config.chooseEntityIdHiddenField
		, chooseEntityFieldSet

		, attachmentsFieldFieldSet
		, otherAttachmentsFieldFieldSet
		, config.ogrn
		, config.kpp
		, config.inn
		, juridicalAddressColumnPanel
		, physicalAddressColumnPanel
	];
}