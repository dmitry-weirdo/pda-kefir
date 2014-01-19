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
	listWindowTitle = "������ ��������� ��������",
	notChosenTitle = "��������� ������� �� ������",
	notChosenMessage = "�������� ��������� �������",

	chooseWindowTitle = "����� ���������� �������",
	createWindowTitle = "���� ���������� �������",
	showWindowTitle = "�������� ���������� �������",
	updateWindowTitle = "��������� ���������� �������",
	deleteWindowTitle = "�������� ���������� �������",

	createSaveErrorMessage = "������ ��� ���������� ���������� �������",
	updateSaveErrorMessage = "������ ��� ��������� ���������� �������",
	deleteSaveErrorMessage = "������ ��� �������� ���������� �������",

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