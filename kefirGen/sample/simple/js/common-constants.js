Ext.namespace('su.opencode.minstroy.building.building');
su.opencode.minstroy.building.building.BuildingCommissioningState = { COMMISSIONED: 'commissioned', NOT_COMMISSIONED: 'not_commissioned' };
su.opencode.minstroy.building.building.BuildingDwellingState = { DWELLING: 'dwelling', NOT_DWELLING: 'not_dwelling' };
su.opencode.minstroy.building.building.BuildingPopulatedState = { POPULATED: 'populated', NOT_POPULATED: 'not_populated' };
su.opencode.minstroy.building.building.BuildingProblemState = { PROBLEM: 'problem', NON_PROBLEM: 'non_problem' };

Ext.namespace('su.opencode.minstroy.investing.investmentContract');
su.opencode.minstroy.investing.investmentContract.InvestorType = { INVESTOR: 'investor', CONDOMINIUM: 'condominium' };

Ext.namespace('su.opencode.minstroy.ejb.holding');
su.opencode.minstroy.ejb.holding.IdentityCardType = { RF_CITIZEN_PASSPORT: 13 };

// todo: remove this, add appending marker
Ext.namespace('test');
test.TestEnum = { SUGAR: 'sugar', PLUM: 'plum', FAIRY: 'fairy' };