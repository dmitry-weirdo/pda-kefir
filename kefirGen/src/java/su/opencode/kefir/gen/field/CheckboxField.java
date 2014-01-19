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
 * Помечает булево поле, генерируемое в CRUD-формах сущности в виде чекбокса.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckboxField
{
	/**
	 * @return код поля.
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String id() default "";

	/**
	 * @return имя поля.
	 * По умолчанию — равно имени поля, на котором стоит аннотация
	 */
	String name() default "";

	/**
	 * @return метка поля
	 */
	String label();

	/**
	 * @return <code>true</code> — если поле не обязательно для заполнения, <br/>
	 * <code>false</code> — если поле обязательно для заполнения
	 */
	boolean allowBlank() default true;

	/**
	 * @return значение по умолчанию, которое будет выставлено в форме создания
	 */
	boolean defaultValue() default false;

	/**
	 * @return рендерер, выполняемый при заполнении поля.
	 *         По умолчанию — рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}