/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

public class MethodArgument
{
	public MethodArgument(String type, String name) {
		this.type = type;
		this.name = name;
	}
	public MethodArgument(Class type, String name) {
		this.type = type.getSimpleName();
		this.name = name;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String type;
	private String name;
}