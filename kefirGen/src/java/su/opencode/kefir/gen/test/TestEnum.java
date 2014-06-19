package su.opencode.kefir.gen.test;

import su.opencode.kefir.gen.field.enumField.EnumField;
import su.opencode.kefir.gen.field.enumField.EnumFieldValue;

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
	rendererClassPackage = "my.super.package",
	rendererClassName = "MySuperCellRenderer"
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