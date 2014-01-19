/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.project.sql.SqlTableField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ������� ����, ������������ � CRUD-������ ��������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberField
{
	/**
	 * @return ����� ���������� �������� ���� � sql-������� ��� ����.
	 */
	int digits() default SqlTableField.DEFAULT_NUMERIC_FIELD_DIGITS;

	/**
	 * @return ����� ���������� ���������� ���� � sql-������� ��� ����.
	 */
	int precision() default SqlTableField.DEFAULT_NUMERIC_FIELD_PRECISION;

	/**
	 * @return ��� ����.
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� )
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
	int maxLength() default 10;

	/**
	 * @return ������ ���� � ��������
	 */
	int width();

	/**
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 * <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

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
	 * @return <code>true</code> � ���� ������������� �������� ���������, <br/>
	 * <code>false</code> � � ��������� ������
	 */
	boolean allowNegative() default false;

	// todo: ��������, ��� ����������� null � minValue � maxValue (��������, �������� ������� �� �������� ��� Object), ��� �� ������� ��������� noMaxValue, noMinValue
	/**
	 * @return ����������� ��������.
	 */
	double minValue() default DEFAULT_MIN_VALUE;

	/**
	 * @return ������������ ��������
	 */
	double maxValue() default DEFAULT_MAX_VALUE;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";


	public static final double DEFAULT_MIN_VALUE = 0.01;
	public static final double DEFAULT_MAX_VALUE = 0;
}