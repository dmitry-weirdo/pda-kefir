/*
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 18.07.2011 20:21:28$
*/
package su.opencode.kefir.web;

public abstract class InitiableAction extends Action
{
	public boolean needsInit() {
		return true;
	}
}