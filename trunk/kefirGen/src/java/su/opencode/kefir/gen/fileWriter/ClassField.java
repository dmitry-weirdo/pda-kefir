/*
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 04.04.2012 18:13:41$
*/
package su.opencode.kefir.gen.fileWriter;

public class ClassField
{
	public ClassField(String name, Class type) {
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}

	private String name;
	private Class type;
}