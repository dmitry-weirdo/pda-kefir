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
	@EnumFieldValue(storeValue = "—‡ı‡"/*, hashName = "SuGaR", hashValue = "mySugar", rendererValue = "—¿’¿–"*/)
	sugar,

	@EnumFieldValue(storeValue = "—ÎË‚‡"/*, hashName = "PlUm", hashValue = "myPlum", rendererValue = "—À»¬¿"*/)
	plum,

	@EnumFieldValue(storeValue = "‘Âˇ"/*, hashName = "FaIrY", hashValue = "myFairy", rendererValue = "‘≈ﬂ"*/)
	fairy
}