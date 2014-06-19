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
 * Аннотация, помечающая поле сущности, являющееся списком файловых вложений (attachment).
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttachmentsField
{
	/**
	 * @return основное имя полей CRUD-формы, которое будет использоваться для названия js-переменных, связанных с полем аттачментов.
	 * По умолчанию — равно имени поля.
	 */
	String name() default "";

	/**
	 * @return имя поля сущности, которое будет использоваться при сохранении аттачмента.
	 * По умолчанию — равно имени поля.
	 */
	String entityFieldName() default "";

	/**
	 * @return id для UploadPanel, обрабатывающей аттачменты, хранимые в этом поле.
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String panelId() default "";

	/**
	 * @return ширина UploadPanel в пикселах
	 */
	int panelWidth() default 600;

	/**
	 * @return высота UploadPanel в пикселах
	 */
	int panelHeight() default 200;

	/**
	 * @return название филдсета, в который заключена UploadPanel для аттачментов поля.
	 */
	String fieldSetTitle() default "Вложения";

	/**
	 * @return имя параметра, в котором в сервлеты CRUD передаются коды аттачментов, созданных через MultiUploadPanel
	 * По умолчанию — (имя поля + "Ids")
	 */
	String idsParamName() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";

	public static final String SERVICE_CLASS_NAME = "su.opencode.kefir.srv.attachment.AttachmentService";
}