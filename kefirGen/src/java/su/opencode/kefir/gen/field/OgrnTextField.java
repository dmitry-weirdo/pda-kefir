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
 * �������� ��������� ���� ��� ����� ����.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OgrnTextField
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
	String label() default "����";

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
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";

	public static final int OGRN_FIELD_LENGTH = 13;
}