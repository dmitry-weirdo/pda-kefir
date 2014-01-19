/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.srv.json.ColumnModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ���� ��������� �������� ({@linkplain su.opencode.kefir.gen.test.ComboBoxEntity ComboBoxEntity} ��� {@linkplain su.opencode.kefir.gen.test.ChooseEntity ChooseEntity}),
 * ���� ����� ���� ����� �� ����� ��������� ��������, � ����� �� ����, ����������� ���������, ���.
 * � ���� ������ ������ �������� ����������� ������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VOField
{
	/**
	 * @return �������� ���� ��������� ��������.
	 */
	String name();

	/**
	 * @return �������� ����, ������������ � VO ������.
	 * <br/>
	 * �� ��������� �
	 * (
	 * 	&lt;��� ����, �� ������� ����� ��������� @ChooseField ��� @ComboBoxField&gt;
	 * +
	 * {@linkplain #name �������� ���� ��������� ��������}, � ������� ���� ����� �����, ����� �������, �������� � ������� �����, � ����� �������
	 * )
	 */
	String voFieldName() default "";

	/**
	 * @return ��� ����, ������� ����� ������� � VO ������.
	 * <br/>
	 * �� ��������� � ����� �������� ���� ���� ��������� ��������.
	 */
	String voFieldClassName() default "";

	/**
	 * @return �������� �� ���������� �� ����� ��������� ��������
	 * <br/>
	 * �� ��������� - ����� ����������� ���������� @ColumnModel ���������, ������� �� ���� ��������� ��������.
	 */
	boolean sortable() default ColumnModel.DEFAULT_SORTABLE;

	/**
	 * @return {@linkplain su.opencode.kefir.srv.json.ColumnModel#header() ��������� ���� �������}, ������� ����� ������� � VO ������.
	 * <br/>
	 * �� ��������� - ����� ���������� @ColumnModel ���������, ������� �� ���� ��������� ��������,
	 * � ���� ��������� ��� - �� ������ ���� ����� ��������� ��������.
	 */
	String header() default "";

	/**
	 * @return �������� ����, ������� ����� ������� � VO ������.
	 * <br/>
	 * �� ��������� � ����� ��������� ���� ��������� ��������.
	 */
	String renderer() default "";

	/**
	 * @return �������� ��������� ����������, ������� ����� �������������� ��� ����.
	 * <br/>
	 * �� ��������� � ����� {@linkplain #name �������� ���� ��������� ��������}.
	 */
	String sortParam() default "";

	/**
	 * @return ��� ������ �������� �������, � ������� �������� ������� ���� ��������� ��������.
	 * ���� ��� ������ ���� ����� �� {@linkplain su.opencode.kefir.srv.json.ColumnGroup#id id} ��������� {@linkplain su.opencode.kefir.srv.json.ColumnGroup @ColumngGroup},
	 * ������������ � ��������� {@linkplain su.opencode.kefir.srv.json.ColumnGroups @ColumnGroups} � ������ ��������.
	 * <br/>
	 * �� ��������� � ������� �� ��������� � ������ ��������.
	 */
	String groupId() default "";

	/**
	 * @return ����� ����������� ��������� � ���������������� ������� �������.
	 * <br/>
	 * �� ��������� � ����� ������ �� �����, � ����������� ��������� ����� �� ��������� ����� {@linkplain ColumnModel#header ��������� �������}.
	 */
	String tooltip() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";
}