/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 15.02.2011 17:38:14$
*/
package su.opencode.kefir.srv.json;

public class JsonReader extends JsonObject
{
	public JsonReader() {
	}
	public JsonReader(String name, String type, String dateFormat) {
		this.name = name;
		this.dateFormat = dateFormat;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	private String name;
	private String dateFormat;
	private String type;
}
