/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 17.04.2012 14:48:45$
*/
package su.opencode.kefir.gen.field;

import su.opencode.kefir.gen.project.sql.SqlTableField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает поля сущности, которых нет в форме, но которые присутствуют в sql-таблице, хранящей сущность.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlColumn
{
	/**
	 * @return название sql-столбца.
	 * По умолчанию — равно имени поля, на котором стоит аннотация в котором слова разделены подчерком.
	 */
	String columnName() default "";

	/**
	 * @return тип sql-столбца
	 */
	String type() default SqlTableField.VARCHAR_TYPE;

	/**
	 * @return длина sql-столбца. Актуальна для {@linkplain SqlTableField#VARCHAR_TYPE varchar} полей.
	 */
	int length() default 200;

	/**
	 * @return общее количество значащих цифр в sql-столбце для поля. Актуально для {@linkplain SqlTableField#NUMERIC_TYPE numeric} полей
	 */
	int digits() default SqlTableField.DEFAULT_NUMERIC_FIELD_DIGITS;

	/**
	 * @return общее количество десятичных цифр в sql-столбце для поля. Актуально для {@linkplain SqlTableField#NUMERIC_TYPE numeric} полей.
	 */
	int precision() default SqlTableField.DEFAULT_NUMERIC_FIELD_PRECISION;

	/**
	 * @return not null значение для sql-столбца
	 */
	boolean notNull() default false;

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находится поле.
	 * <br/>
	 * Код должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}