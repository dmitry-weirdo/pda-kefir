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
 * ��������� ����, ������������, ��� ���� �������� �����������, ���������� �������� �� ������, ����������� �� ���.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComboBoxField
{
	/**
	 * @return <code>true</code> ���� ��� ���� ����� �������� many-to-one ������� � ������� �������� � orm.xml,<br/>
	 * <code>false</code> � ���� ������� ��������� �� ����
	 */
	boolean addManyToOneMapping() default true;

	/**
	 * @return ��� sql-�������, � ������� �������� ��������� �������� ����������� � �������� ��������.
	 * �� ��������� � (��� ���� � ������ ��������, ��� ������ ����� ����� �������� ��������� + "_id").
	 */
	String joinColumnName() default "";

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
	 */
	int listWidth() default 0;

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
	boolean editable() default true;

	/**
	 * @return <code>true</code> � ���� ���� ����������� ���� �������� � uppercase, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
	boolean uppercase() default true;

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
	 * @return ��� ��������, ������� ������ ������ �������� � json ��� ����������.
	 */
	String url();

	/**
	 * @return �������� �������, ������� ���������� � ������� ��� ����� �������� � ����.
	 */
	String queryParam() default DEFAULT_QUERY_PARAM;

	/**
	 * @return ���� ��������� ����������.
	 * �� ��������� � id ���� int � name ���� string
	 * // todo: ��������, ������������ ���� id ������ �������������, ����� ��������� ������ ������ ����
	 */
	ComboBoxStoreField[] fields() default {};

	/**
	 * @return ���� ��������� �����-��������, ������� ����� ��������� � ��������������� VO ����� � {@linkplain su.opencode.kefir.srv.json.ColumnModel ��������} ��� ������� �������.
	 */
	VOField[] voFields() default {};

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
	 * @return ���� ����������, ������� ���������� � �������
	 */
	String sortBy() default "name";

	/**
	 * @return ������� ����������, ������� ���������� � �������
	 */
	String sortDir() default "asc";

	/**
	 * @return <code>true</code> � ���� ��� ������ �������� � ���������� � ��������� ����������� ��������������� ��������������, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
	boolean typeAhead() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";

	public static final String DEFAULT_QUERY_PARAM = "name";
	public static final String DEFAULT_VALUE_FIELD_NAME = "id";
	public static final String DEFAULT_DISPLAY_FIELD_NAME = "name";
}