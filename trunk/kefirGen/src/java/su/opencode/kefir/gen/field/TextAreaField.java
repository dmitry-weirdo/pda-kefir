/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ��������� ����, ������������ � CRUD-������ ��������.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextAreaField
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
	String label();

	/**
	 * @return <code>true</code> - ���� � ���� ��� ���� ��������� lob ���� � ������������ ������ {@linkplain #BLOB_MAX_LENGTH 100000 ��������} � ���� �����.
	 * <br/>
	 * <code>false</code> - ���� � ���� ��������� varchar ���� � ��������� {@linkplain #maxLength() ������������ ������}.
	 */
	boolean blob() default false;

	/**
	 * @return ������������ ����� ����
	 */
	int maxLength();

	/**
	 * @return ������ ���� � ��������
	 */
	int width();

	// todo: add javadocs

	/**
	 * @return ���������� ����� � html �������� textArea.
	 */
	int rows();

	/**
	 * @return ���������� �������� � html �������� textArea.
	 */
	int cols();

	/**
	 * @return <code>true</code> � ���� ���� ����� �� ����������� ��� ����������, <br/>
	 *         <code>false</code> � ���� ���� ����������� ��� ����������
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> � ���� ���� ����������� ���� �������� � uppercase, <br/>
	 *         <code>false</code> � � ��������� ������
	 */
//	boolean uppercase() default false;

	/**
	 * @return ����� ���������� ����. ��� �������� ���������� � �������� style ������� textField.
	 * ���� ���������� ������ ����� ������ � uppercase, &laquo;textTransform: 'none'&raquo; ����� �������� � ���� �����.
	 */
	String style() default "";

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
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";

	/**
	 * ������������ ����� ���� �����, ������������ � ������, ���� ��������� ���� {@linkplain #blob blob}
	 */
	public static final int BLOB_MAX_LENGTH = 100000;
}