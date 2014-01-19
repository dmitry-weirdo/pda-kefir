package su.opencode.kefir.srv.nativeQuery;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class OrderByParam
{
	public OrderByParam(String tableAlias, String field, OrderByDirection direction) {
		this.tableAlias = tableAlias;
		this.field = field;
		this.direction = direction;
	}
	public OrderByParam(String field, OrderByDirection direction) {
		this.tableAlias = null;
		this.field = field;
		this.direction = direction;
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
	public OrderByDirection getDirection() {
		return direction;
	}
	public void setDirection(OrderByDirection direction) {
		this.direction = direction;
	}

	public String tableAlias;
	public String field;
	public OrderByDirection direction;
}