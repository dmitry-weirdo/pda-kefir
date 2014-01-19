/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���� VO ��������, ��� �������� ��� ��������� ��� �������� true ��� false
 * ���������� ������ � ������ ��������� � ������������ ��������������� ���� �������,
 * ���� ��� ����.
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LegendField
{
	/**
	 * @return ��� ������������ ���� VO.
	 */
	String fieldName();

	/**
	 * @return <code>true</code>, ���� �������� ���� ����������� �� ��������� true
	 * <br/>
	 * <code>false</code>, ���� �������� ���� ����������� �� ��������� true
	 */
	boolean checkTrue() default true;

	/**
	 * @return ����� ���� �������.
	 */
	String legendLabel();

	/**
	 * @return ��� ������ ���� �������.
	 * �� ��������� ����� {@linkplain #rowClassName() ����� ������ ���������� ������}
	 */
	String legendClassName() default "";

	/**
	 * @return ��� ������ ���������� ������ ������.
	 * �� ��������� ����� {@linkplain #fieldName() ����� ����}
	 */
	String rowClassName() default "";

	/**
	 * @return ���� ���� ��� css-������� ���� ������� � ������������ ������ ������.
	 */
	String backgroundColor();

	/**
	 * @return ���� ������ ��� css-������� ���� ������� � ������������ ������ ������.
	 */
	String color() default "#000000";

	/**
	 * @return ���� ���� ��� css-������ ���������� ������ ������.
	 */
	String selectedBackgroundColor() default "#DFE8F6";

	/**
	 * @return ���� ������ ��� css-������ ���������� ������ ������.
	 */
	String selectedColor() default "#000000";
}