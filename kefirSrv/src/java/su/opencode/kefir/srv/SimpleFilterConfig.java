/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 10.11.2010 10:23:44$
*/
package su.opencode.kefir.srv;


import java.util.Map;

public class SimpleFilterConfig extends FilterConfig
{
	public SimpleFilterConfig(Map<String, String[]> map) {
		super(map);

		if (region == null || inspection == null)
			throw new RuntimeException("Регион и инспекция не могут быть пустыми!");
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public Integer getInspection() {
		return inspection;
	}
	public void setInspection(Integer inspection) {
		this.inspection = inspection;
	}
	public void setSqlWhere(StringBuffer sb) {
		addValue(sb, WHERE, "region", EQUAL, region);

		if (inspection != 0)
			addValue(sb, AND, "inspection", EQUAL, inspection);
	}

	private Integer region;
	private Integer inspection;
}