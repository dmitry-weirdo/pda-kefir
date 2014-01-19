/**
 * Copyright 2012 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ��������� ����, ������������, ��� ���� �������� ����� ������ �� ������ ������ ��������� ����� ������� ������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseField
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
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

	/**
	 * @return ��� ���� CRUD-�����, ���������� id ��������� ��������.
	 *         �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� )
	 */
	String idFieldId() default "";

	/**
	 * @return ��� ���� CRUD-�����, ���������� id ��������� ��������.
	 * �� ��������� � ����� ����� ����.
	 */
	String name() default "";

	/**
	 * @return ��� ���� ���������� ��������, ����������� id ���������� ��������
	 */
	String idFieldName() default "id";

	/**
	 * @return �������������� ���������, ������������ � ���������� ������ ������.
	 */
	ChooseFieldInitParam[] initParams() default {};

	/**
	 * @return �������� ������� ����, ������� ���������� �� ��������, <b>�� ������� id</b>.
	 * ���� ����� ������������ � readonly ��������� ����
	 * // todo: ��������, ����� ����� ������� ����������� ��������� � ������������ ������
	 */
	ChooseFieldTextField[] fields();

	/**
	 * @return ���� ��������� ��������, ������� ����� ��������� � ��������������� VO ����� � {@linkplain su.opencode.kefir.srv.json.ColumnModel ��������} ��� ������� �������.
	 */
	VOField[] voFields() default {};

	/**
	 * @return �������� ������ �����, ������� ����� ��������� ��������� ���� ��������� �������� � �������� �����.
	 */
	String fieldSetName();

	/**
 	 * @return id ������ ������
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� + "-choose" )
	 */
	String chooseButtonId() default "";

	/**
	 * @return ����� �� ������ ������ �������� � ����� � �����
	 */
	String chooseButtonText() default "�������";

	/**
	 * @return ����� ������ ������
	 */
	String chooseButtonStyle() default "{ marginLeft: 10, marginBottom: 5 }";

	/**
	 * @return ������ �������� (������� ���������) �������, ������� ����� ���������� ��� ������� ������ ������.
	 * �� ��������� � ������������ �������� ������ ����: ���� ����� ����� ��������� ExtEntity, �� ����������� ������ �� ���, ����� � ������ �� ���������.
	 */
	String chooseFunctionName() default "";

	/**
	 * @return �������� ��������� ������� ������, � ������� ����� ������������ ���������� ��������� ������ (successHandler).
	 * �� ��������� � ������������ �������� ������ ����: ���� ����� ����� ��������� ExtEntity, �� ����������� ������ �� ���, ����� � ������ �� ���������.
	 */
	String chooseFunctionSuccessHandlerParamName() default "";


	/**
 	 * @return id ������ ������
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� + "-show" )
	 */
	String showButtonId() default "";

	/**
	 * @return ����� �� ������ ��������� �������� � ����� � �����
	 */
	String showButtonText() default "�����������";

	/**
	 * @return ����� ������ ��������� ��������
	 */
	String showButtonStyle() default "{ marginLeft: 10, marginBottom: 5 }";

	/**
	 * @return ������ �������� (������� ���������) �������, ������� ����� ���������� ��� ������� ������ ��������� ��������� ��������.
	 * �� ��������� � ������������ �������� ������ ����: ���� ����� ����� ��������� ExtEntity, �� ����������� ������ �� ���, ����� � ������ �� ���������.
	 */
	String showFunctionName() default "";

	/**
	 * @return �������� ��������� ������� ��������� �������, � ������� ����� ������������ id ��������.
	 * �� ��������� � ������������ �������� ������ ����: ���� ����� ����� ��������� ExtEntity, �� ����������� ������ �� ���, ����� � ������ �� ���������.
	 */
	String showFunctionIdParamName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";
}