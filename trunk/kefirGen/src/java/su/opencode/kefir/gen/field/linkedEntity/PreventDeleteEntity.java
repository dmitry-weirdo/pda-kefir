/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.linkedEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �������� ��������� ��������,
 * � ������ ������������� ���� �� ����� ������� ��� �������� �������� ��������
 * ����� ��������� {@linkplain su.opencode.kefir.srv.ClientException ClientException} � ��������������� ����������.
 * �������� ������ ���� �������� ��� ExtEntity,
 * � � �� FilterConfig ������ �������������
 * Integer ���� � {@linkplain #filterConfigFieldName() ��������������� ������}.
 * ��� ��������� ���������� ��������� ��������� ������������
 * {@linkplain su.opencode.kefir.gen.ExtEntity#countMethodName() ��������������� �����}
 * {@linkplain su.opencode.kefir.gen.ExtEntity#serviceClassName() ������� ��������� ��������}.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDeleteEntity
{
	/**
	 * @return ������ ��� ������ ��������� ��������.
	 */
	String className();

	/**
	 * @return ���������, ���������� � ClientException � ������ ����� ������� ��������� ��������.
	 */
	String message();

	/**
	 * @return ��� ��������� FilterConfig ��������� ��������, � ������� ����� ���������� id ��������� ��������.
	 * <br/>
	 * �� ��������� � (��� ������ ��������� �������� + "Id")
	 */
	String filterConfigFieldName() default "";
}