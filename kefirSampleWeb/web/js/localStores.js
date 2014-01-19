Ext.namespace('su.opencode.kefir.sampleSrv');
su.opencode.kefir.sampleSrv.TestEnumStore = new Ext.data.ArrayStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.kefir.sampleSrv.TestEnum.SUGAR, 'Сахар' ],
		[ su.opencode.kefir.sampleSrv.TestEnum.PLUM, 'Слива' ],
		[ su.opencode.kefir.sampleSrv.TestEnum.FAIRY, 'Фея' ]
	]
});

// ${APPEND_STORE}