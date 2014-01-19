/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv.attachment;

import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.srv.ExtEntityFilterConfig;
import su.opencode.kefir.srv.QueryBuilder;
import su.opencode.kefir.srv.json.Json;

public class AttachmentFilterConfig extends ExtEntityFilterConfig
{
	public void addWhereParams(QueryBuilder qb, String entityPrefix) {
		if (entityName != null)
			qb.addEqualStringParam( concat(sb, entityPrefix, ".entityName"), entityName );

		if (entityFieldName != null)
			qb.addEqualStringParam( concat(sb, entityPrefix, ".entityFieldName"), entityFieldName );

		if (entityId != null)
			qb.addEqualParam( concat(sb, entityPrefix, ".entityId"), entityId );

	}

	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityFieldName() {
		return entityFieldName;
	}
	public void setEntityFieldName(String entityFieldName) {
		this.entityFieldName = entityFieldName;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Json(uppercase = false)
	private String entityName;

	@Json(uppercase = false)
	private String entityFieldName;

	private Integer entityId;

	private StringBuffer sb = new StringBuffer();
}