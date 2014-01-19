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
 * ��������� ����, ������������, ��� ���� �������� �����������, ���������� �������� �� ���������� js-���������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalComboBoxField
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
	 * @return ��� �������� ����, � ������� ����� ����������� �������� ����������.
	 *         �� ��������� � ����� {@linkplain #name() ����� ����}.
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxHiddenName(LocalComboBoxField, java.lang.reflect.Field)
	 */
	String hiddenName() default "";

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
	 * @return ������ ������ � ��������.
	 *         �� ��������� � ����� ������ ����
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxListWidth(LocalComboBoxField, java.lang.reflect.Field)
	 */
	int listWidth() default DEFAULT_LIST_WIDTH;

	/**
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> � ���� ���� ����������� ���������� �� ������, <br/>
	 *         <code>false</code> � ���� �������� ������������ ���������� ����.
	 */
	boolean forceSelection() default true;

	/**
	 * @return <code>true</code> � ���� � ���� ����� ������� ��������, <br/>
	 *         <code>false</code> � ���� � ���� ������ ������� ��������.
	 */
	boolean editable() default false;

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

	/**
	 * @return �������� js-����������, ���������� ��������� ��������� ��� ����������.
	 *         �� ��������� � (������ ��� ������ ���� (�������������� enum) + "Store").
	 * @see su.opencode.kefir.gen.field.ExtEntityFieldsUtils#getLocalComboBoxStore(LocalComboBoxField, java.lang.reflect.Field)
	 */
	String store() default "";

	/**
	 * @return �������� id-���� �������.
	 * �������� ����� ���� ������������ �������� � ��������� ��� ���������� ����� ��� ���������\���������\��������.
	 */
	String valueField() default DEFAULT_VALUE_FIELD_NAME;

	/**
	 * @return ���� �������, ����������� �� �������� ������� ������������ � ����������.
	 */
	String displayField() default DEFAULT_DISPLAY_FIELD_NAME;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";


	public static final int DEFAULT_LIST_WIDTH = 0;
	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
}