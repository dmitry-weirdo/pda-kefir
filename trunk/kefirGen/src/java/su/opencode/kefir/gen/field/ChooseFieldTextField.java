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
 * Для поля обязательно указание имени, ширины лейбла и ширины всей формпанели, содержащей строку с полем.
 * Рядом с первым полем генерируются кнопки выбора и показа.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseFieldTextField
{
	/**
	 * @return код поля.
	 *         По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
	 */
	String id() default "";

	/**
	 * @return имя поля (выбираемой сущности).
	 */
	String name();

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
	 * @return ширина формпанели, содержащей поле, в пикселах
	 */
	int formPanelWidth() default 450;

	/**
	 * @return ширина лейбла поля в пикселах
	 */
	int labelWidth() default 150;

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
	 * @return рендерер, выполняемый при заполнении поля.
	 *         По умолчанию — рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return vtype поля
	 *         По умолчанию — рендерер отсутствует
	 */
	String vtype() default "";
}