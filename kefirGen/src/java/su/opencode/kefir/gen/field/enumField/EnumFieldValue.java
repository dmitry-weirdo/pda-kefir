/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.enumField;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * �������� �������� �����, ������� ����� ���� ���������� � js-store � js-renderer.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumFieldValue
{
	/**
	 * @return �������� �����, ������� ����� �������������� ��� ��������� � js-����, �������������� ����.
	 * �� ��������� � �������� ���� �����-������ �������� �������.
	 */
	String hashName() default "";

	/**
	 * @return ��������, ������� ����� �������������� ��� ��������� � js-����, �������������� ����.
	 * �� ��������� � ������, ������ �������� ���� ����-������.
	 */
	String hashValue() default "";

	/**
	 * @return ������, ������� ����� ������������ ��� �������� � store (�� ���� ����� ������������ � localComboBox)
	 */
	String storeValue();

	/**
	 * @return ������, ������� ����� ���������� ��� �������� � ���������
	 * �� ��������� � ����� {@linkplain #storeValue ��������, ������������� � store}.
	 */
	String rendererValue() default "";
}