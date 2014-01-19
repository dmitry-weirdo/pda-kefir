/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.constant;

/**
 * ������� ����. ��������� �������� �� "������" �� "���������"
 */
public enum Quarter
{
	first,

	second,

	third,

	fourth;

	/**
	 * ����������� ����� �������� - 1.
	 */
	public static final int MIN_VALUE = 1;

	/**
	 * ������������ ����� �������� - 4.
	 */
	public static final int MAX_VALUE = 4;

	/**
	 * ������������ ����� ���� � ����, ����������� ��� ���������� ���� � ���������.
	 */
	public static final int SQL_FIELD_LENGTH = 7;
}