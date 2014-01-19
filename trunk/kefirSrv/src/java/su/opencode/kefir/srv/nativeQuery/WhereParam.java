package su.opencode.kefir.srv.nativeQuery;

import su.opencode.kefir.srv.Relation;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class WhereParam
{
	public WhereParam(String tableAlias, String field, String value, Relation relation) {
		this.tableAlias = tableAlias;
		this.field = field;
		this.value = value;
		this.relation = relation;
	}
	public WhereParam(String field, String value, Relation relation) {
		this.tableAlias = null;
		this.field = field;
		this.value = value;
		this.relation = relation;
	}
	public WhereParam(String field, String value) {
		this.tableAlias = null;
		this.field = field;
		this.value = value;
		this.relation = Relation.equal; // relation defaults to equal
	}

	public String getTableAlias() {
		return tableAlias;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String tableAlias;
	public String field;
	public String value;
	public Relation relation;
}