/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 17.04.2012 14:48:45$
*/
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.project.sql.SqlTableField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ���� ��������, ������� ��� � �����, �� ������� ������������ � sql-�������, �������� ��������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlColumn
{
	/**
	 * @return �������� sql-�������.
	 * �� ��������� � ����� ����� ����, �� ������� ����� ��������� � ������� ����� ��������� ���������.
	 */
	String columnName() default "";

	/**
	 * @return ��� sql-�������
	 */
	String type() default SqlTableField.VARCHAR_TYPE;

	/**
	 * @return ����� sql-�������. ��������� ��� {@linkplain SqlTableField#VARCHAR_TYPE varchar} �����.
	 */
	int length() default 200;

	/**
	 * @return ����� ���������� �������� ���� � sql-������� ��� ����. ��������� ��� {@linkplain SqlTableField#NUMERIC_TYPE numeric} �����
	 */
	int digits() default SqlTableField.DEFAULT_NUMERIC_FIELD_DIGITS;

	/**
	 * @return ����� ���������� ���������� ���� � sql-������� ��� ����. ��������� ��� {@linkplain SqlTableField#NUMERIC_TYPE numeric} �����.
	 */
	int precision() default SqlTableField.DEFAULT_NUMERIC_FIELD_PRECISION;

	/**
	 * @return not null �������� ��� sql-�������
	 */
	boolean notNull() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";
}