/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.ExtEntity;

@ExtEntity(
	listWindowTitle = "Список земельных участков",
	notChosenTitle = "Земельный участок не выбран",
	notChosenMessage = "Выберите земельный участок",

	chooseWindowTitle = "Выбор земельного участка",
	createWindowTitle = "Ввод земельного участка",
	showWindowTitle = "Просмотр земельного участка",
	updateWindowTitle = "Изменение земельного участка",
	deleteWindowTitle = "Удаление земельного участка",

	createSaveErrorMessage = "Ошибка при сохранении земельного участка",
	updateSaveErrorMessage = "Ошибка при изменении земельного участка",
	deleteSaveErrorMessage = "Ошибка при удалении земельного участка",

	formWindowWidth = 500,

	jsDirectory = "parcelNew",
	serviceClassName = "su.opencode.minstroy.ejb.leasing.LeasingServiceNew",
	serviceBeanClassName = "su.opencode.minstroy.ejb.leasing.LeasingServiceNewBean",
	queryBuilderClassName = "su.opencode.minstroy.ejb.leasing.ParcelQueryBuilderNew",
	filterConfigClassName = "su.opencode.minstroy.ejb.leasing.ParcelFilterConfigNew",

	listServletUrl = "/parcelsListNew",
	getServletUrl = "/parcelGetNew",
	createServletUrl = "/pacrelCreateNew",

	listInitFunctionName = "initParcelsList"
)
public class Parcel
{
}