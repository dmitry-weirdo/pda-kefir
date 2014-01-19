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
 * �������� ����, ��� ����, ������� � ����� �������� ��� ��������� ���������.
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
	@EnumFieldValue(storeValue = "�����"/*, hashName = "SuGaR", hashValue = "mySugar", rendererValue = "�����"*/)
	sugar,

	@EnumFieldValue(storeValue = "�����"/*, hashName = "PlUm", hashValue = "myPlum", rendererValue = "�����"*/)
	plum,

	@EnumFieldValue(storeValue = "���"/*, hashName = "FaIrY", hashValue = "myFairy", rendererValue = "���"*/)
	fairy
}