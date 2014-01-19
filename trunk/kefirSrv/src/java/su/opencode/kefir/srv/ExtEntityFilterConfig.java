/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv;

import static su.opencode.kefir.srv.EntityQueryBuilder.DEFAULT_ENTITY_PREFIX;
import su.opencode.kefir.srv.json.JsonObject;

public abstract class ExtEntityFilterConfig extends JsonObject
{
	public abstract void addWhereParams(QueryBuilder qb, String entityPrefix);

	public void addWhereParams(QueryBuilder qb) {
		addWhereParams(qb, DEFAULT_ENTITY_PREFIX);
	}
}