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
 * ���������, ���������� ���� ��������, ���������� ������� �������� �������� (attachment).
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttachmentsField
{
	/**
	 * @return �������� ��� ����� CRUD-�����, ������� ����� �������������� ��� �������� js-����������, ��������� � ����� �����������.
	 * �� ��������� � ����� ����� ����.
	 */
	String name() default "";

	/**
	 * @return ��� ���� ��������, ������� ����� �������������� ��� ���������� ����������.
	 * �� ��������� � ����� ����� ����.
	 */
	String entityFieldName() default "";

	/**
	 * @return id ��� UploadPanel, �������������� ����������, �������� � ���� ����.
	 * �� ��������� � ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() ������� js-���� ����� ��������} + "-" + �������� ���� )
	 */
	String panelId() default "";

	/**
	 * @return ������ UploadPanel � ��������
	 */
	int panelWidth() default 600;

	/**
	 * @return ������ UploadPanel � ��������
	 */
	int panelHeight() default 200;

	/**
	 * @return �������� ��������, � ������� ��������� UploadPanel ��� ����������� ����.
	 */
	String fieldSetTitle() default "��������";

	/**
	 * @return ��� ���������, � ������� � �������� CRUD ���������� ���� �����������, ��������� ����� MultiUploadPanel
	 * �� ��������� � (��� ���� + "Ids")
	 */
	String idsParamName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() ���} {@linkplain FieldSet ��������}, � ������� ��������� ����.
	 * <br/>
	 * ��� ������ ���� ����� �� ����� ���������, ������������ � ��������� {@linkplain FieldSets FieldSets} � ������ ��������.
	 */
	String fieldSetId() default "";

	public static final String SERVICE_CLASS_NAME = "su.opencode.kefir.srv.attachment.AttachmentService";
}