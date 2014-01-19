/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * �������� ������������� ����, ������������ � CRUD-������ �������� � ������� ��������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpinnerField
{
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
	int maxLength() default DEFAULT_MAX_LENGTH;

	/**
	 * @return ������ ���� � ��������
	 */
	int width() default DEFAULT_WIDTH;

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
	int minValue() default DEFAULT_MIN_VALUE;

	/**
	 * @return ������������ ��������
	 */
	int maxValue() default DEFAULT_MAX_VALUE;

	/**
	 * @return �������� �� ���������, ������� ����� ���������� � ����� ��������
	 */
	int defaultValue() default DEFAULT_DEFAULT_VALUE;

	/**
	 * @return <code>true</code> - ���� �� ��������� ���� �� �������� �������� ��������,<br/>
	 * <code>false</code> - ���� � ���� ������������� {@linkplain #defaultValue() �������� �� ���������}.
	 */
	boolean noDefaultValue() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";

	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_MAX_LENGTH = 2;
	public static final int DEFAULT_MIN_VALUE = 0;
	public static final int DEFAULT_MAX_VALUE = 99;
	public static final int DEFAULT_DEFAULT_VALUE = 0;
}