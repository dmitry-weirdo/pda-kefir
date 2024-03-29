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
 * Помечает целочисленное поле, генерируемое в CRUD-формах сущности с помощью спиннера.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpinnerField
{
	/**
	 * @return код поля.
	 * По умолчанию — ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-поля формы сущности} + "-" + название поля )
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
	int maxLength() default DEFAULT_MAX_LENGTH;

	/**
	 * @return ширина поля в пикселах
	 */
	int width() default DEFAULT_WIDTH;

	/**
	 * @return <code>true</code> — если поле можно не обязательно для заполнения, <br/>
	 * <code>false</code> — если поле обязательно для заполнения
	 */
	boolean allowBlank() default true;

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
	 * @return <code>true</code> — если отрицательные значения разрешены, <br/>
	 * <code>false</code> — в противном случае
	 */
	boolean allowNegative() default false;

	// todo: порешить, как проставлять null в minValue и maxValue (возможно, придется сделать их строками или Object), или же ставить аннотации noMaxValue, noMinValue
	/**
	 * @return минимальное значение.
	 */
	int minValue() default DEFAULT_MIN_VALUE;

	/**
	 * @return максимальное значение
	 */
	int maxValue() default DEFAULT_MAX_VALUE;

	/**
	 * @return значение по умолчанию, которое будет выставлено в форме создания
	 */
	int defaultValue() default DEFAULT_DEFAULT_VALUE;

	/**
	 * @return <code>true</code> - если по умолчанию поле не содержит никакого значения,<br/>
	 * <code>false</code> - если в поле подставляется {@linkplain #defaultValue() значение по умолчанию}.
	 */
	boolean noDefaultValue() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";

	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_MAX_LENGTH = 2;
	public static final int DEFAULT_MIN_VALUE = 0;
	public static final int DEFAULT_MAX_VALUE = 99;
	public static final int DEFAULT_DEFAULT_VALUE = 0;
}