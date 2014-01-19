Ext.namespace('su.opencode.minstroy.store');

su.opencode.minstroy.store.BuildingCommissioningStateStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.minstroy.building.building.BuildingCommissioningState.COMMISSIONED, 'Сдан' ],
		[ su.opencode.minstroy.building.building.BuildingCommissioningState.NOT_COMMISSIONED, 'Не сдан' ]
	]
});
su.opencode.minstroy.store.BuildingDwellingStateStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.minstroy.building.building.BuildingDwellingState.DWELLING, 'Жилой' ],
		[ su.opencode.minstroy.building.building.BuildingDwellingState.NOT_DWELLING, 'Не жилой' ]
	]
});
su.opencode.minstroy.store.BuildingPopulatedStateStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.minstroy.building.building.BuildingPopulatedState.POPULATED, 'Заселен' ],
		[ su.opencode.minstroy.building.building.BuildingPopulatedState.NOT_POPULATED, 'Не заселен' ]
	]
});
su.opencode.minstroy.store.BuildingProblemStateStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.minstroy.building.building.BuildingProblemState.PROBLEM, 'Проблемный' ],
		[ su.opencode.minstroy.building.building.BuildingProblemState.NON_PROBLEM, 'Не проблемный' ]
	]
});

su.opencode.minstroy.store.InvestorTypeStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ su.opencode.minstroy.investing.investmentContract.InvestorType.INVESTOR, 'Инвестор' ],
		[ su.opencode.minstroy.investing.investmentContract.InvestorType.CONDOMINIUM, 'ТСЖ' ]
	]
});

// todo: remove this, add append marker
// todo: think about common namespace or namespace for each enum
Ext.namespace('test');
test.TestEnumStore = new Ext.data.SimpleStore({
	fields: [ 'id', 'name' ],
	data: [
		[ test.TestEnum.SUGAR, 'Сахар' ],
		[ test.TestEnum.PLUM, 'Слива' ],
		[ test.TestEnum.FAIRY, 'Фея' ]
	]
});