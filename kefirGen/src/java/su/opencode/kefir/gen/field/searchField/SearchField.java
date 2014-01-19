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
 * ���������, ���������� ���� � ������� � ������ ������ ����������, �� �������� �����
 * ����������� ����� � �������� TwinTriggerField.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchField
{
	/**
	 * @return id ��� SearchField.
	 * �� ��������� � (id ����� (������� ������ ��� ������) + '-' + ��� ���� + "SearchField").
	 * ��� ����� �� ���� ������ (ChooseField) �� ��������� � (id ����� (������� ������ ��� ������) + '-' + ��� ���� ������ + ��� ���� ���������� �������� + "SearchField")
	 */
	String id() default "";

	/**
	 * @return �����, ������������� � ������� ����� ����� ������ ����� ������
	 */
	String label();

	/**
	 * @return ����� ����, � ������� ��������� ���� ������.
	 */
	 int row() default FIRST_ROW_NUM;

	/**
	 * @return ��� ���������, ������� ����� ������������ � ������� ������\������ �� ������ ���������.
	 * �� ��������� � ��� ������� ����� � ��� ����.
	 * ��� ����� ������ �� ��������� � (�������� ���� ������ + {@linkplain #chooseFieldFieldName() �������� ���� ���������� ��������}).<br/>
	 * � ���� �� ������ ����� ���������� �������� ��� FilterConfig.
	 */
	String paramName() default "";

	/**
	 * @return ��� ���������, ������� ����� �������������� � FilterConfig ��� ������ (�������) �� ����.
	 * �� ��������� � ��� ������� ����� � ��� ����.
	 * ��� ����� ������ �� ��������� � (�������� ���� ������ + "." + {@linkplain #chooseFieldFieldName() �������� ���� ���������� ��������}).<br/>
	 */
	String sqlParamName() default "";

	/**
	 * @return ���������, ������� ����� ����������� � sql-������� ��� ����� ����.
	 */
	Relation relation() default Relation.like;

	/**
	 * @return ��� ���� ��������� ��������, �� �������� ����������� �����.
	 * ������������ ������ ��� ��������� ��������� (ChooseField).
	 */
	String chooseFieldFieldName() default "name";

	/**
	 * @return ������������ ����� ����
	 * �� ��������� � ����� maxLength �� ��������� ����, �� ������� ����� SearchField.
	 */
	int maxLength() default DEFAULT_MAX_LENGTH_VALUE;

	/**
	 * @return ������ ���� � ��������
	 */
	int width() default 300;

	/**
	 * @return <code>true</code> � ���� ���� ����������� ���� �������� � uppercase, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
	boolean uppercase() default false;

	/**
	 * @return ����� ����� ����. ����� �������������� ��� �������� ��� Ext.ux.InputTextMask.
	 * <br/>
	 * �� ��������� � ����� �����������.
	 */
	String mask() default "";

	/**
	 * @return <code>true</code> � ���� ���� ������ ���������� �������������� ��� �������� ������ � ������ �� ������ ���������.
	 * <br/>
	 * <code>false</code> � ���� ���� �� ���������� ��������������.
	 * � ������, ���� ��� ����� � ��������� ��������� true, ������������ ������ ������ ���� ������ (���� ��� ����).
	 */
	boolean defaultFocused() default false;

	public static final int DEFAULT_MAX_LENGTH_VALUE = -1;
	public static final int FIRST_ROW_NUM = 0;
}