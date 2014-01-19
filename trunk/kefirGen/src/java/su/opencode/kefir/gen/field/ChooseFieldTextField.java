/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ��������� ����, ������������ � CRUD-������ ��������.
 * ��� ���� ����������� �������� �����, ������ ������ � ������ ���� ����������, ���������� ������ � �����.
 * ����� � ������ ����� ������������ ������ ������ � ������.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseFieldTextField
{
	/**
	 * @return ��� ����.
	 *         �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� )
	 */
	String id() default "";

	/**
	 * @return ��� ���� (���������� ��������).
	 */
	String name();

	/**
	 * @return ����� ����
	 */
	String label();

	/**
	 * @return ������������ ����� ����
	 */
	int maxLength();

	/**
	 * @return ������ ���� � ��������
	 */
	int width();

	/**
	 * @return ������ ����������, ���������� ����, � ��������
	 */
	int formPanelWidth() default 450;

	/**
	 * @return ������ ������ ���� � ��������
	 */
	int labelWidth() default 150;

	/**
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> � ���� ���� ����������� ���� �������� � uppercase, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
	boolean uppercase() default false;

	/**
	 * @return ��������, ����������� ��� ���������� ����.
	 *         �� ��������� � �������� �����������.
	 */
	String renderer() default "";

	/**
	 * @return vtype ����
	 *         �� ��������� � �������� �����������
	 */
	String vtype() default "";
}