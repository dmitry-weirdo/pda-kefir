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
 * Помечает дробное поле, генерируемое в CRUD-формах сущности.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberField
{
	/**
	 * @return общее количество значащих цифр в sql-столбце для поля.
	 */
	int digits() default SqlTableField.DEFAULT_NUMERIC_FIELD_DIGITS;

	/**
	 * @return общее количество десятичных цифр в sql-столбце для поля.
	 */
	int precision() default SqlTableField.DEFAULT_NUMERIC_FIELD_PRECISION;

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
	int maxLength() default 10;

	/**
	 * @return ширина поля в пикселах
	 */
	int width();

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
	double minValue() default DEFAULT_MIN_VALUE;

	/**
	 * @return максимальное значение
	 */
	double maxValue() default DEFAULT_MAX_VALUE;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";


	public static final double DEFAULT_MIN_VALUE = 0.01;
	public static final double DEFAULT_MAX_VALUE = 0;
}