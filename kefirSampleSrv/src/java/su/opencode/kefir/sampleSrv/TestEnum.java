/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.sampleSrv;

import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;

/**
 * Тестовый энум, для поля, которое в форме выглядит как локальный комбобокс.
 */
@EnumField(
//	hashNamespace = "mySuperHashPackage.test",
//	hashName = "MySuperEnumHash",
//
//	storeNamespace = "mySuperStore.test",
//	storeName = "MySuperEnumStore",
//	storeValueFieldName = "myId",
//	storeDisplayFieldName = "myName"

//	rendererNamespace = "mySuperRenderer.test",
//	rendererName = "MyRenderer"
//	rendererConstantName = "MY_SUPER_RENDERER_CONSTANT"
)
public enum TestEnum
{
	@EnumFieldValue(storeValue = "Сахар"/*, hashName = "SuGaR", hashValue = "mySugar", rendererValue = "САХАР"*/)
	sugar,

	@EnumFieldValue(storeValue = "Слива"/*, hashName = "PlUm", hashValue = "myPlum", rendererValue = "СЛИВА"*/)
	plum,

	@EnumFieldValue(storeValue = "Фея"/*, hashName = "FaIrY", hashValue = "myFairy", rendererValue = "ФЕЯ"*/)
	fairy
}