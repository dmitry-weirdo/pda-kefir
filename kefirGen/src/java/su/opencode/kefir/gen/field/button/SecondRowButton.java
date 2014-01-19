/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.button;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает кнопку дополнительного (второго) ряда в списке сущностей.
 * Как правило, обозначает переход к списку сущностей, связанных с выбранной в основном списке сущностью.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface SecondRowButton
{
	/**
	 * @return название кнопки. Так будет называться переменная кнопки в getSecondRow(),
	 * и исходя из этого названия будут созданы Js-константы для id и text кнопки.
	 * <br/>
	 * По умолчанию — если указана {@linkplain #listEntityClassName() связанная сущность}, то (краткое имя класса связанной сущности + "sButton")
	 * <br/>
	 * Если не указано ни имя, ни связанная сущность, то IllegalArgumentException.
	 */
	String name() default "";

	/**
	 * @return id кнопки.
	 * По умолчанию — (имя класса сущности (основной) с маленькой буквы + "sList-" + {@linkplain #name название кнопки} )
	 */
	String id() default "";

	/**
	 * @return текст кнопки.
	 */
	String text();

	/**
	 * @return Полное имя класса сущности, на список которой будет выполнен переход по нажатию на кнопку.
	 * В {@linkplain #listEntityParamName()}  соответствующий параметр} перехода будет передана выбранная в основном списке (содержащем кнопку) сущность.
	 * Класс должен иметь аннотацию {@linkplain su.opencode.kefir.gen.ExtEntity ExtEntity}<br/>
	 * <br/>
	 * Если не указан, для кнопки генерируется пустой хэндлер.
	 * <br/>
	 * Если указана сущность, не являющаяся ExtEntity, то IllegalArgumentException.
	 */
	String listEntityClassName() default "";

	/**
	 * @return Имя парамера конфига списка сущностей, к которой выполняется переход по кнопке,
	 * в который будет передаваться выбранная в основном списке сущность.
	 * <br/>
	 * По умолчанию — имя класса сущности (основной) с маленькой буквы.
	 */
	String listEntityParamName() default "";
}