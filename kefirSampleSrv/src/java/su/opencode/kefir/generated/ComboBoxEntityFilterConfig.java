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

public class ComboBoxEntityFilterConfig extends ExtEntityFilterConfig
{
	public void addWhereParams(QueryBuilder qb, String entityPrefix) {
		if (cadastralNumber != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".cadastralNumber"), cadastralNumber, true );

	}

	public String getCadastralNumber() {
		return cadastralNumber;
	}
	public void setCadastralNumber(String cadastralNumber) {
		this.cadastralNumber = cadastralNumber;
	}

	private String cadastralNumber;

	private StringBuffer sb = new StringBuffer();
}