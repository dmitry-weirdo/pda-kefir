Ext.namespace('ru.kg.gtn.render');

ru.kg.gtn.render.CAR_NUMBER = false;
ru.kg.gtn.render.setCarNumber = function(type) {
	ru.kg.gtn.render.CAR_NUMBER = (type == ru.kg.gtn.production.ProductionType.CAR_NUMBER || type == ru.kg.gtn.production.ProductionType.TRANSIT);
};

ru.kg.gtn.render.parameterRender = function(value) {
	switch (value)
	{
		case ru.kg.gtn.parameter.DATABASE_PATH_KEY: return 'Путь к БД';
		case ru.kg.gtn.parameter.KLADR_PATH_KEY: return 'Путь к файлам КЛАДР';
		case ru.kg.gtn.parameter.PERFORMANCE_DATE_KEY: return 'Количество дней до исполнения наказания';
		case ru.kg.gtn.parameter.ROAD_TAX_CONVERT_FOLDER_KEY: return 'Папка для выгрузки данных транспортного налога';
		case ru.kg.gtn.parameter.MINIMUM_FINE_KEY: return 'Минимальный размер штрафа (руб.)';
		case ru.kg.gtn.parameter.EXCEL_PASSWORD_KEY: return 'Пароль для файлов отчетов *.xls';
		case ru.kg.gtn.parameter.PORTAL_ENDPOINT_ADDRESS: return 'Адрес сервиса обновления статуса заявок на портале';
		case ru.kg.gtn.parameter.SAVE_OBJECT_WITH_NOT_RECEIVE_PRODUCTION_KEY: return 'Разрешить ввод некорректной (неполученной) спецпродукции';
		case ru.kg.gtn.parameter.AUTO_LOAD_KEY: return 'Автозагрузка таблиц';
		default: alert('Unknown parameter "' + value + '"'); return a;
	}
}
ru.kg.gtn.render.dontShowNilRender = function(value) {
	if (!value || value == '0')
		return '';

	return value;
};
ru.kg.gtn.render.dontShowNilFloatRender = function(value) {
	if (!value || value == '0')
		return '';

	return value.toFixed(2).replace('.', ',');
};
ru.kg.gtn.render.floatRenderer = function(value) {
	if (!value)
		return '';

	return value.toFixed(2).replace('.', ',');
};
ru.kg.gtn.render.dateRenderer = function(value) {
	if (value)
		return value.format('d.m.Y');

	return '';
}
ru.kg.gtn.render.loginRenderer = function(value) {
	if (value != ru.kg.gtn.render.user)
		return value;

	return '<span style=\"font-weight: bold;\">' + value + '</span>';
}
ru.kg.gtn.render.roleRenderer = function(value) {
	switch(value)
	{
		case 'admin': return 'Администратор';
		case 'regional' : return 'Областная инспекция';
		case 'local': return 'Районная инспекция';
		case 'gibdd': return 'Инспектор ГИБДД';
	}
}
ru.kg.gtn.render.demandStateRenderer = function(value) {
	switch(value)
	{
		case 'submitted': return 'Новая';
		case 'examined' : return 'На рассмотрении';
		case 'assigned' : return 'Назначена дата приема';
		case 'accepted': return 'Принята';
		case 'rejected': return 'Отклонена';
	}
}
ru.kg.gtn.render.demandTypeRenderer = function(value) {
	switch(value)
	{
		case 'physical_registration': return 'Регистрация за ФЛ';
		case 'juridical_registration' : return 'Регистрация за ЮЛ';
		case 'physical_removal': return 'Снятие с регистрации за ФЛ';
		case 'juridical_removal': return 'Снятие с регистрации за ЮЛ';
		case 'pawn': return 'Регистрация залога';
		case 'licence': return 'Выдача удостоверения';
		case 'checkup': return 'Прохождение ТО одной машиной';
		case 'checkup_group': return 'Прохождение ТО группой машин';
	}
}

ru.kg.gtn.render.productionNumberRenderer = function(value) {
	if (!value)
		return '';

	if (!ru.kg.gtn.render.CAR_NUMBER)
		return '000000'.substring(0, 6 - value.toString().length) + value;
	else
		return '0000'.substring(0, 4 - value.toString().length) + value;
}

ru.kg.gtn.render.productionModeRenderer = function(value) {
	switch (value)
	{
		case 'receive': return 'Получение';
		case 'writeoff': return 'Списание';
		case 'theft': return 'Хищение';
		case 'issue': return 'Выдача';
	}
}

ru.kg.gtn.render.RoadTaxVOIssue = function(value) {
	return value ? "да" : "нет";
}

ru.kg.gtn.render.RoadTaxVOArchive = function(value) {
	return value ? "архив" : "учет";
}

ru.kg.gtn.render.RoadTaxPropertyVOActive = function(value) {
	return value ? "да" : "";
}

ru.kg.gtn.render.booleanRenderer = function(value, element) {
	element.css += ' x-grid3-check-col-td';
	return '<div class=\"x-grid3-check-col' + (value ? '-on' : '') + '\">&#160;</div>';
}

ru.kg.gtn.render.totalInspections = function(value, params) {
	params.attr = 'ext:qtip="Общее количество инспекций"';

	return value ? 'Количество инспекций: ' + value : '';
}

ru.kg.gtn.render.vehicleAggrNumRenderer = function(value) {
	return value ? value : ru.kg.gtn.registration.vehicle.EMPTY_AGGR_NUM_VALUE;
}

ru.kg.gtn.render.pawnHandedRenderer = function(value) {
	return value ? "Машина передается" : "Машина остается";
}

ru.kg.gtn.render.reportRenderer = function(value) {
	switch (value) {
		case ru.kg.gtn.report.ReportType.AGGREGATE: return "Свидетельство на высвободившийся номерной агрегат";
		case ru.kg.gtn.report.ReportType.CHECKUP_COUPON: return "Талон-допуск";
		case ru.kg.gtn.report.ReportType.EDUCATION: return "Свидетельство о соответствии образовательного учреждения";
		case ru.kg.gtn.report.ReportType.LICENCE: return "Удостоверение";
		case ru.kg.gtn.report.ReportType.TMP_LICENCE: return "Временное удостоверение";
		case ru.kg.gtn.report.ReportType.PAWN_REG: return "Свидетельство о регистрации залога на одну машину";
		case ru.kg.gtn.report.ReportType.PAWN: return "Свидетельство о регистрации залога на группу машин";
		case ru.kg.gtn.report.ReportType.REG_CERT: return "Техпаспорт";
		case ru.kg.gtn.report.ReportType.REG: return "Свидетельство о регистрации";
		case ru.kg.gtn.report.ReportType.TRANSIT: return "Знак \"Транзит\"";
		default: alert('Unknown reportName "' + value + '"');
	}
}

ru.kg.gtn.render.sexRenderer = function(value) {
	switch (value)
	{
		case ru.kg.gtn.registration.owner.Sex.MALE: return 'М';
		case ru.kg.gtn.registration.owner.Sex.FEMALE: return 'Ж';
		default: return '';
	}
}

ru.kg.gtn.render.BuildingCommissioningStateRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case su.opencode.minstroy.building.building.BuildingCommissioningState.COMMISSIONED: return 'Сдан';
		case su.opencode.minstroy.building.building.BuildingCommissioningState.NOT_COMMISSIONED: return 'Не сдан';
		default: alert('Unknown BuildingCommissioningState ' + value);
	}
}

ru.kg.gtn.render.BuildingDwellingStateRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case su.opencode.minstroy.building.building.BuildingDwellingState.DWELLING: return 'Жилой';
		case su.opencode.minstroy.building.building.BuildingDwellingState.NOT_DWELLING: return 'Не жилой';
		default: alert('Unknown BuildingDwellingState: ' + value);
	}
}

ru.kg.gtn.render.BuildingPopulatedStateRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case su.opencode.minstroy.building.building.BuildingPopulatedState.POPULATED: return 'Заселен';
		case su.opencode.minstroy.building.building.BuildingPopulatedState.NOT_POPULATED: return 'Не заселен';
		default: alert('Unknown BuildingPopulatedState: ' + value);
	}
}

ru.kg.gtn.render.BuildingProblemStateRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case su.opencode.minstroy.building.building.BuildingProblemState.PROBLEM: return 'Проблемный';
		case su.opencode.minstroy.building.building.BuildingProblemState.NON_PROBLEM: return 'Не проблемный';
		default: alert('Unknown BuildingProblemState: ' + value);
	}
}

// todo: remove this, add append render
ru.kg.gtn.render.TestEnumRenderer = function(value) {
	if (!value)
		return '';

	switch (value)
	{
		case test.TestEnum.SUGAR: return 'Сахар';
		case test.TestEnum.PLUM: return 'Слива';
		case test.TestEnum.FAIRY: return 'Фея';
		default: ru.kg.gtn.showError('Unknown TestEnum: ' + value);
	}
}