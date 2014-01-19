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
 * ƒополнительный параметр, передаваемый
 * в обработчик кнопки выбора дл€ пол€ выбора.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChooseFieldInitParam
{
	/**
	 * @return название параметра
	 */
	String name();

	/**
	 * @return значение параметра.  авычками автоматические не обрамл€етс€.
	 */
	String value(); // todo: возможность задавать разные типы (обрамл€ть строки итд)

	/**
	 * @return {@linkplain su.opencode.kefir.gen.field.FieldSet#id() код} {@linkplain FieldSet филдсета}, в котором находитс€ поле.
	 * <br/>
	 *  од должен быть одним из кодов филдсетов, определенных в аннотации {@linkplain FieldSets FieldSets} к классу сущности.
	 */
	String fieldSetId() default "";
}