/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 29.11.2010 13:14:38$
*/
package su.opencode.kefir.srv.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SelectionModel
{
	String name();
	String checkedFieldName() default "";
	Config[] config() default {};

	public static final String LIVEGRID_CHECKBOX_SELECTION_MODEL = "Ext.ux.grid.livegrid.CheckboxSelectionModel";
}