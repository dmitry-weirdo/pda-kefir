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
 * �������� ���� ������ ��������.
 * VO �������� ������ ��������� ���� ������.
 * ��� ���� ������ � ����� �������� ��������� �������� ����, ���������� �������� ������ ��� ������,
 * � ������ "��������", �� ������� �� ������� ����������� ����� �������������� ������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressField
{
	/**
	 * @return <code>true</code> ���� ��� ��������� ���� ����� �������� one-to-one ������� � ������� �������� � orm.xml,<br/>
	 * <code>false</code> � ���� ������� ��������� �� ����
	 */
	boolean addOneToOneMapping() default true;

	/**
	 * @return ��� sql-�������, � ������� �������� ��������� �������� ����������� � �������� ������.
	 * �� ��������� � (��� ���� � ������ ��������, ��� ������ ����� ����� �������� ��������� + "_id").
	 */
	String joinColumnName() default "";

	/**
	 * @return �������� js-������� ��������� ������ � ���� ������.
	 * ������� ������ ���������� � ���������� ����� ������.
	 */
	String getAddressStrFunctionName() default "getAddressStr";

	/**
	 * @return �������� js-������� ������������� ����� ������.
	 */
	String initFunctionName() default "init";

	/**
	 * @return <code>true</code> � ���� ������������ ����������� ����� ������ ��� ������ (��� �������, ������� � �����),<br/>
	 * <code>false</code> � � ������ ��������, ������� ������.
	 */
	boolean building() default false;

	/**
 	 * @return id ������ ��������� ������.
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ����)
	 */
	String textFieldId() default "";

	/**
	 * @return ��� ���������� ����, ����������� ����� ��� ������.
	 * <br/>
	 * �� ��������� � (��� ���� + "Full")
	 */
	String textFieldName() default "";

	/**
	 * @return ����� ���������� ����
	 */
	String textFieldLabel();

	/**
	 * @return ������������ ����� ���������� ����
	 */
	int textFieldMaxLength() default 400;

	/**
	 * @return ������ ���������� ���� � ��������
	 */
	int textFieldWidth() default 400;

	/**
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������.
	 * <br/>
	 * ������ �� ���������������� ������� ���������� ���� � ������� ��� �������.
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> � ���� ���� �����, ���� ���������� ������ �������� ������, ����������� ���� �������� � uppercase, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
	boolean uppercase() default false;

	/**
 	 * @return id ������ ��������� ������.
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� + "-update" )
	 */
	String updateButtonId() default "";

	/**
	 * @return ����� ������ ��������� ������.
	 */
	String updateButtonText() default "��������";

	/**
	 * @return ����� ������ ��������� ������
	 */
	String updateButtonStyle() default "{ marginLeft: 10 }";

	/**
	 * @return ����� columnPanel, ���������� ��������� ���� � ������ ���������.
	 */
	String columnPanelStyle() default "{ padding: 5 }";

	/**
	 * @return ������ formPanel, ���������� ��������� ����.
	 */
	int textFieldFormPanelWidth() default 610;

	/**
	 * @return ������ ������� � formPanel, ���������� ��������� ����.
	 */
	int textFieldFormPanelLabelWidth() default 150;

	/**
	 * @return ��������� ����� � �������, ������������ ��� �������������� ����.
	 */
	String addressWindowTitle();

	/**
	 * @return ���� � VO, � ������� ����� ����������� Json-������ ��� Address ����.
	 * �� ��������� � ����� ����� ����.
	 */
	String voFieldName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";
}