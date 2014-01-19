/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.field.linkedEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает связанную сущность,
 * в случае существования хотя бы одной которой при удалении основной сущности
 * будет бросаться {@linkplain su.opencode.kefir.srv.ClientException ClientException} с соответствующим сообщением.
 * Сущности должны быть помечены как ExtEntity,
 * и в их FilterConfig должно присуствовать
 * Integer поле с {@linkplain #filterConfigFieldName() соответствующим именем}.
 * Для получения количества связанных сущностей используется
 * {@linkplain su.opencode.kefir.gen.ExtEntity#countMethodName() соответствующий метод}
 * {@linkplain su.opencode.kefir.gen.ExtEntity#serviceClassName() сервиса связанной сущности}.
 */
@Target(ElementType.FIELD) // todo: think about this
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDeleteEntity
{
	/**
	 * @return полное имя класса связанной сущности.
	 */
	String className();

	/**
	 * @return сообщение, выдаваемое в ClientException в случае когда найдены связанные сущности.
	 */
	String message();

	/**
	 * @return имя параметра FilterConfig удаляемой сущности, в который будет подставлен id удаляемой сущности.
	 * <br/>
	 * По умолчанию — (имя класса удаляемой сущности + "Id")
	 */
	String filterConfigFieldName() default "";
}