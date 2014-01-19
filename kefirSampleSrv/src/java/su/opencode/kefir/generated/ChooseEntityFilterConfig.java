/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.generated;

import su.opencode.kefir.srv.ExtEntityFilterConfig;
import su.opencode.kefir.srv.QueryBuilder;
import static su.opencode.kefir.util.StringUtils.concat;

public class ChooseEntityFilterConfig extends ExtEntityFilterConfig
{
	public void addWhereParams(QueryBuilder qb, String entityPrefix) {
		if (name != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".name"), name, true );

	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String name;

	private StringBuffer sb = new StringBuffer();
}