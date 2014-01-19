/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.project.sql.SqlTableField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ѕомечает дробное поле, генерируемое в CRUD-формах сущности.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberField
{
	/**
	 * @return общее количество значащих цифр в sql-столбце дл€ пол€.
	 */
	int digits() default SqlTableField.DEFAULT_NUMERIC_FIELD_DIGITS;

	/**
	 * @return общее количество дес€тичных цифр в sql-столбце дл€ пол€.
	 */
	int precision() default SqlTableField.DEFAULT_NUMERIC_FIELD_PRECISION;

	/**
	 * @return код пол€.
	 * ѕо умолчанию Ч ( {@linkplain su.opencode.kefir.gen.ExtEntity#jsFieldPrefix() префикс js-пол€ формы сущности} + "-" + название пол€ )
	 */
	String id() default "";

	/**
	 * @return им€ пол€.
	 *         ѕо умолчанию Ч равно имени пол€, на котором стоит аннотаци€
	 */
	String name() default "";

	/**
	 * @return метка пол€
	 */
	String label();

	/**
	 * @return максимальна€ длина пол€
	 */
	int maxLength() default 10;

	/**
	 * @return ширина пол€ в пикселах
	 */
	int width();

	/**
	 * @return <code>true</code> Ч если поле можно не об€зательно дл€ заполнени€, <br/>
	 * <code>false</code> Ч если поле об€зательно дл€ заполнени€
	 */
	boolean allowBlank() default true;

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

	/**
	 * @return <code>true</code> Ч если отрицательные значени€ разрешены, <br/>
	 * <code>false</code> Ч в противном случае
	 */
	boolean allowNegative() default false;

	// todo: порешить, как проставл€ть null в minValue и maxValue (возможно, придетс€ сделать их строками или Object), или же ставить аннотации noMaxValue, noMinValue
	/**
	 * @return минимальное значение.
	 */
	double minValue() default DEFAULT_MIN_VALUE;

	/**
	 * @return максимальное значение
	 */
	double maxValue() default DEFAULT_MAX_VALUE;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находитс€ поле.
	 * <br/>
	 *  од должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";


	public static final double DEFAULT_MIN_VALUE = 0.01;
	public static final double DEFAULT_MAX_VALUE = 0;
}