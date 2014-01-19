/*
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 18.07.2011 20:21:28$
*/
package su.opencode.kefir.web;

public class NonInitiableAction extends Action
{
	public void doAction() throws Exception {
		// do nothing
	}
	public boolean needsInit() {
		return false;
	}
}