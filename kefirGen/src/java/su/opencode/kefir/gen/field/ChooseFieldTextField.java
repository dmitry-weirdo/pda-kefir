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
 * ѕомечает текстовое поле, генерируемое в CRUD-формах сущности.
 * ƒл€ пол€ об€зательно указание имени, ширины лейбла и ширины всей формпанели, содержащей строку с полем.
 * –€дом с первым полем генерируютс€ кнопки выбора и показа.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseFieldTextField
{
	/**
	 * @return код пол€.
	 *         ѕо умолчанию Ч ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-пол€ формы сущности} + "-" + название пол€ )
	 */
	String id() default "";

	/**
	 * @return им€ пол€ (выбираемой сущности).
	 */
	String name();

	/**
	 * @return метка пол€
	 */
	String label();

	/**
	 * @return максимальна€ длина пол€
	 */
	int maxLength();

	/**
	 * @return ширина пол€ в пикселах
	 */
	int width();

	/**
	 * @return ширина формпанели, содержащей поле, в пикселах
	 */
	int formPanelWidth() default 450;

	/**
	 * @return ширина лейбла пол€ в пикселах
	 */
	int labelWidth() default 150;

	/**
	 * @return <code>true</code> Ч если поле можно не об€зательно дл€ заполнени€, <br/>
	 *         <code>false</code> Ч если поле об€зательно дл€ заполнени€
	 */
	boolean allowBlank() default true;

	/**
	 * @return <code>true</code> Ч если поле транслирует свое значение в uppercase, <br/>
	 *         <code>false</code> Ч в противном случае
	 */
	boolean uppercase() default false;

	/**
	 * @return рендерер, выполн€емый при заполнении пол€.
	 *         ѕо умолчанию Ч рендерер отсутствует.
	 */
	String renderer() default "";

	/**
	 * @return vtype пол€
	 *         ѕо умолчанию Ч рендерер отсутствует
	 */
	String vtype() default "";
}