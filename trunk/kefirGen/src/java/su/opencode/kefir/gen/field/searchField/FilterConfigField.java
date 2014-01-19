/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.searchField;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ���������, ���������� ����, ������� ����������� � FilterConfig,
 * �� �� ��������� ��� ���� ������ � ������ ���������.
 */
@Target(ElementType.FIELD) // todo: ��������, ����� ����������� ������� �� ����� ��� �����, ������� ��� � ��������
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterConfigField
{
	/**
	 * @return ��� ����, ������� ����� ����������� � FilterConfig.
	 * �� ��������� � ��� ������� ����� � ��� ����.
	 * ��� ����� ������ �� ��������� � (�������� ���� ������ + {@linkplain #chooseFieldFieldName() �������� ���� ���������� ��������}).<br/>
	 */
	String name() default "";

	/**
	 * @return ��� ����, ������� ����� ����������� � FilterConfig.
	 * �� ��������� � ��� ������� ����� � ��� ����.
	 */
	String type() default "";

	/**
	 * @return ��� ���������, ������� ����� �������������� � FilterConfig ��� ������ (�������) �� ����.
	 * �� ��������� � ��� ������� ����� � ��� ����.
	 * ��� ����� ������ �� ��������� � (�������� ���� ������ + "." + {@linkplain #chooseFieldFieldName() �������� ���� ���������� ��������}).<br/>
	 */
	String sqlParamName() default "";

	/**
	 * @return ��������, �� ��������� �������� ����� ����������� ������.
	 * �� ��������� � �������� ���� FilterConfig.
	 */
	String sqlParamValue() default "";

	/**
	 * @return <code>true</code> � ���� ����� sql ���������� � ������� ����� ����������� (entityPrefix + ".").<br/>
	 * <code>false</code> � ���� ����� ����������� �������� "��� ����" (��������� ��� ����� join-���������)
	 */
	boolean addEntityPrefix() default true;

	/**
	 * @return ���������, ������� ����� ����������� � sql-������� ��� ����� ����.
	 */
	Relation relation() default Relation.equal;

	/**
	 * @return ��� ���� ��������� ��������, �� �������� ����������� �����.
	 * ������������ ������ ��� ��������� ��������� (ChooseField).
	 */
	String chooseFieldFieldName() default ID_FIELD_NAME;

	/**
	 * @return ��� ���� ��������� ��������, �� �������� ����������� �����.
	 * ������������ ������ ��� ��������� ��������� (ChooseField).
	 */
	String chooseFieldFieldType() default "Integer";

	/**
	 * @return <code>true</code> - ���� ��� fromJson ���� ������������� � ������� �������,
	 * <code>false</code> - � ��������� ������.
	 */
	boolean uppercase() default true;

	/**
	 * @return <code>true</code> - ���� � �������� ���������� ��������
	 * ����� ���������� �� ���� �� ��������, � ���� ���������� �������
	 * <code>false</code> - ���� � �������� ����� ���������� �������� ����������
	 */
	boolean filterPassSelfAsParam() default false;

	/**
	 * @return ��� ����, ������� ����� ������� �� ��������� �������� ��� ����������.
	 * ���� ���� �� �������, �� ������������ ���������� {@linkplain #name ����� ���� FilterConfig}.
	 */
	String filterFieldName() default "";

	/**
	 * @return <code>true</code> � ���� ���� ����� ���������
	 * � ��������� ���������� ������ ��������� � ������ �� ������ ���������.
	 * <br/>
	 * <code>false</code> � � ��������� ������.
	 */
	boolean listFilterParam() default true;

	/**
	 * @return ��� ��������� ������� ������� �������������
	 * ������� ������ � ������ �� ������ �������, � ������� ����� ������������ �������� ����������.
	 * <br/>
	 * �� ��������� � ��� ������ ���� � ��������� �����.
	 */
	String listInitFunctionParamName() default "";

	/**
	 * @return ������, �������������� ��� ����� ��������� ������ ���������
	 * ��� ������� ������� �� ����.
	 * (��� ����� ����� ��� &lt;����&gt;: &lt;��������&gt;)
	 */
	String listWindowFilterTitle() default "";

	/**
	 * @return <code>true</code> - ���� � ��������� ���� ������ �� ��������� ����������
	 * ������� ������ {@linkplain #listWindowFilterTitle() �������� �����}.
	 * <br/>,
	 * <code>false</code> - ���� � ��������� ���� ������ ������� � �������� �����,
	 * �, ����� ���������, �������� ��������.
	 */
	boolean listWindowTitleOnly() default false;

	/**
	 * @return ��� ��������� ��������� ��������,
	 * �������� ������� ����� ������������� � ����� ��������� ������ ���������.
	 * (��� ����� ����� ��� &lt;����&gt;: &lt;��������&gt;)
	 */
	String listWindowTitleParamName() default "name";

	/**
	 * @return <code>true</code> � ���� ���� ����� ���������
	 * � ��������� ���������� CRUD-�����.
	 * <br/>
	 * <code>false</code> � � ��������� ������.
	 */
	boolean formFilterParam() default true;

	/**
	 * @return ��� ��������� ������� ������� �������������
	 * CRUD-�����, � ������� ����� ������������ �������� ����������.
	 * <br/>
	 * �� ��������� � ��� ������ ���� � ��������� �����.
	 */
	String formInitFunctionsParamName() default "";


	public static final String ID_FIELD_NAME = "id";
	public static final String NULL_VALUE = "null";
}