/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ѕоле VO сущности, дл€ которого при равенстве его значени€ true или false
 * выдел€етс€ строка в списке сущностей и отображаетс€ соответствующее поле легенды,
 * если она есть.
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LegendField
{
	/**
	 * @return им€ провер€емого пол€ VO.
	 */
	String fieldName();

	/**
	 * @return <code>true</code>, если значение пол€ провер€етс€ на равенство true
	 * <br/>
	 * <code>false</code>, если значение пол€ провер€етс€ на равенство true
	 */
	boolean checkTrue() default true;

	/**
	 * @return текст пол€ легенды.
	 */
	String legendLabel();

	/**
	 * @return им€ класса пол€ легенды.
	 * ѕо умолчанию равно {@linkplain #rowClassName() имени класса выделенной строки}
	 */
	String legendClassName() default "";

	/**
	 * @return им€ класса выделенной строки списка.
	 * ѕо умолчанию равно {@linkplain #fieldName() имени пол€}
	 */
	String rowClassName() default "";

	/**
	 * @return цвет фона дл€ css-классов пол€ легенды и невыделенной строки списка.
	 */
	String backgroundColor();

	/**
	 * @return цвет текста дл€ css-классов пол€ легенды и невыделенной строки списка.
	 */
	String color() default "#000000";

	/**
	 * @return цвет фона дл€ css-класса выделенной строки списка.
	 */
	String selectedBackgroundColor() default "#DFE8F6";

	/**
	 * @return цвет текста дл€ css-класса выделенной строки списка.
	 */
	String selectedColor() default "#000000";
}