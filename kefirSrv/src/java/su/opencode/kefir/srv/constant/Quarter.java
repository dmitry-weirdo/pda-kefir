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
 * Квартал года. Принимает значения от "первый" до "четвертый"
 */
public enum Quarter
{
	first,

	second,

	third,

	fourth;

	/**
	 * Минимальный номер квартала - 1.
	 */
	public static final int MIN_VALUE = 1;

	/**
	 * Максимальный номер квартала - 4.
	 */
	public static final int MAX_VALUE = 4;

	/**
	 * Максимальная длина поля в базе, необходимая для сохранения поля с кварталом.
	 */
	public static final int SQL_FIELD_LENGTH = 7;
}