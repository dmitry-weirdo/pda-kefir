Ext.namespace('Kefir.render');

Kefir.render.TestEnumRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case su.opencode.kefir.sampleSrv.TestEnum.SUGAR: return 'Сахар';
		case su.opencode.kefir.sampleSrv.TestEnum.PLUM: return 'Слива';
		case su.opencode.kefir.sampleSrv.TestEnum.FAIRY: return 'Фея';
		default: Kefir.showError('Unknown su.opencode.kefir.sampleSrv.TestEnum value: ' + value);
	}
};

// ${APPEND_RENDERER}