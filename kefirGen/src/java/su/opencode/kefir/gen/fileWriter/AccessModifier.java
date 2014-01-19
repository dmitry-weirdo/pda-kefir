/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import static su.opencode.kefir.util.StringUtils.concat;

public enum AccessModifier
{
	public_, private_, protected_;

	public String getModifier() {
		switch (this)
		{
			case public_: return "public";
			case private_: return "private";
			case protected_: return "protected";
			default: throw new IllegalArgumentException( concat("Incorrect ", AccessModifier.class.getName(), " value: ", this) );
		}
	}
}