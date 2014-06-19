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
import su.opencode.kefir.gen.field.TextField;

@ExtEntity(
//	jsNamespace = "mySuperJsNamespaceForDeveloper",
//	chooseJsNamespace = "DeveloperChooseNS",
//	chooseInitFunctionName = "mySuperChooseInitFunction",
//	chooseInitConfigSuccessHandlerParamName = "mySuperChooseSuccessHandler",

//	formJsNamespace = "DeveloperFormNS",
//	formShowFunctionName = "developerExtEntityShowFunction",
//	formConfigEntityIdParamName = "developerExtEntityConfigId",

	notChosenMessage = "Застройщик не выбран",
	listWindowTitle = "Список застройщиков",
	chooseWindowTitle = "Выбор застройщика",
	createWindowTitle = "Ввод застройщика",
	showWindowTitle = "Просмотр застройщика",
	updateWindowTitle = "Изменение застройщика",
	deleteWindowTitle = "Удаление застройщика"
)
public class Developer
{
	@TextField(label = "Название", maxLength = 255, width = 200)
	private String name;

//	@FilterConfigField(listInitFunctionParamName = "mySuperParcelParamForDevListInit")
//	private Parcel parcel;
}