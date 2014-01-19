/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.button;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ������ ��������������� (�������) ���� � ������ ���������.
 * ��� �������, ���������� ������� � ������ ���������, ��������� � ��������� � �������� ������ ���������.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface SecondRowButton
{
	/**
	 * @return �������� ������. ��� ����� ���������� ���������� ������ � getSecondRow(),
	 * � ������ �� ����� �������� ����� ������� Js-��������� ��� id � text ������.
	 * <br/>
	 * �� ��������� � ���� ������� {@linkplain #listEntityClassName() ��������� ��������}, �� (������� ��� ������ ��������� �������� + "sButton")
	 * <br/>
	 * ���� �� ������� �� ���, �� ��������� ��������, �� IllegalArgumentException.
	 */
	String name() default "";

	/**
	 * @return id ������.
	 * �� ��������� � (��� ������ �������� (��������) � ��������� ����� + "sList-" + {@linkplain #name �������� ������} )
	 */
	String id() default "";

	/**
	 * @return ����� ������.
	 */
	String text();

	/**
	 * @return ������ ��� ������ ��������, �� ������ ������� ����� �������� ������� �� ������� �� ������.
	 * � {@linkplain #listEntityParamName()}  ��������������� ��������} �������� ����� �������� ��������� � �������� ������ (���������� ������) ��������.
	 * ����� ������ ����� ��������� {@linkplain su.opencode.kefir.gen.ExtEntity ExtEntity}<br/>
	 * <br/>
	 * ���� �� ������, ��� ������ ������������ ������ �������.
	 * <br/>
	 * ���� ������� ��������, �� ���������� ExtEntity, �� IllegalArgumentException.
	 */
	String listEntityClassName() default "";

	/**
	 * @return ��� �������� ������� ������ ���������, � ������� ����������� ������� �� ������,
	 * � ������� ����� ������������ ��������� � �������� ������ ��������.
	 * <br/>
	 * �� ��������� � ��� ������ �������� (��������) � ��������� �����.
	 */
	String listEntityParamName() default "";
}