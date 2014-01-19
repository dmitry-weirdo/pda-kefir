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
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextField
{
	/**
	 * @return ��� ����.
	 *         �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� )
	 */
	String id() default "";

	/**
	 * @return ��� ����.
	 *         �� ��������� � ����� ����� ����, �� ������� ����� ���������
	 */
	String name() default "";

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
	 * @return ����� �������� ����. ��� �������� ���������� � �������� style ������� textField.
	 * ���� ���������� ������ ����� ������ � uppercase, &laquo;textTransform: 'none'&raquo; ����� �������� � ���� �����.
	 */
	String style() default "";

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

	/**
	 * @return ����� ����� ����. ����� �������������� ��� �������� ��� Ext.ux.InputTextMask.
	 * <br/>
	 * �� ��������� � ����� �����������.
	 */
	String mask() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";
}