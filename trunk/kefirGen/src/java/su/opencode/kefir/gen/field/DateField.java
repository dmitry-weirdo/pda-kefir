/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.fileWriter.js.JsFileWriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ������� ����, ������������ � CRUD-������ ��������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateField
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
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

	/**
	 * @return ��������, ����������� ��� ���������� ����.
	 *         �� ��������� � �������� �����������.
	 */
	String renderer() default DEFAULT_FORM_RENDERER;

	/**
	 * @return ����������� ���������� �������� ����.
	 *         �� ��������� � �� ����������.
	 */
	String minValue() default "";

	/**
	 * @return ������������ ���������� �������� ����.
	 *         �� ��������� � ������� ����.
	 */
	String maxValue() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";


	public static final String NULL_DATE_VALUE = "null";
	public static final String DEFAULT_FORM_RENDERER = JsFileWriter.GET_DATE_FUNCTION_NAME;
}