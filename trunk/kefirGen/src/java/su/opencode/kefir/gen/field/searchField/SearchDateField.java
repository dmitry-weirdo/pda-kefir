/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.searchField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ���������, ���������� ���� � ������� � ������ ������ ����������, �� �������� �����
 * ����������� ����� � �������� TwinTriggerField.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchDateField
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
	String paramDateName() default "";

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


	public static final int DEFAULT_MAX_LENGTH_VALUE = -1;
}