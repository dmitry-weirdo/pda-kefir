Ext.namespace('su.opencode.kefir.sampleSrv.chooseEntity');

su.opencode.kefir.sampleSrv.chooseEntity.getFormItemsLayout = function(config) {
	return [
		  config.idHiddenField
		, config.name
		, config.shortName
		, config.correspondentAccount
		, config.info
	];
};