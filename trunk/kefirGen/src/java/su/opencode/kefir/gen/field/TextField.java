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
 * Помечает текстовое поле, генерируемое в CRUD-формах сущности.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextField
{
	/**
	 * @return код поля.
	 *         По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String id() default "";

	/**
	 * @return имя поля.
	 *         По умолчанию — равно имени поля, на котором стоит аннотация
	 */
	String name() default "";

	/**
	 * @return метка поля
	 */
	String label();

	/**
	 * @return максимальная длина поля
	 */
	int maxLength();

	/**
	 * @return ширина поля в пикселах
	 */
	int width();

	/**
	 * @return <code>true</code> — если поле можно не обязательно для заполнения, <br/>
	 *         <code>false</code> — если поле обязательно для заполнения
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> — если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> — в противном случае
	 */
	boolean uppercase() default false;

	/**
	 * @return стиль тектвого поля. Это значение включается в параметр style конфига textField.
	 * Если необходимо помимо стиля задать и uppercase, &laquo;textTransform: 'none'&raquo; нужно включить в этот стиль.
	 */
	String style() default "";

	/**
	 * @return рендерер, выполняемый при заполнении поля.
	 *         По умолчанию — рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return vtype поля
	 *         По умолчанию — рендерер отсутствует
	 */
	String vtype() default "";

	/**
	 * @return маска ввода поля. Будет использоваться как параметр для Ext.ux.InputTextMask.
	 * <br/>
	 * По умолчанию — маска отсутствует.
	 */
	String mask() default "";

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}