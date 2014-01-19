/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 07.09.2010 12:01:35$
*/
package su.opencode.kefir.srv.dynamicGrid;

import static su.opencode.kefir.util.StringUtils.getConcatenation;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.srv.json.SortDirection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.HashMap;

/**
 * <br/>
 * <br/>
 * <br/>
 */
@Entity
@IdClass(value = DynamicGridPK.class)
public class DynamicGrid extends JsonObject
{
	public DynamicGrid() {
	}
	public DynamicGrid(String login, String entityName, String columnName, HashMap<String, Object> cm) {
		setLogin(login);
		setEntityName(entityName);
		setColumnName(columnName);

		setOrder((Integer) cm.get(ColumnModel.ORDER));
		setWidth((Integer) cm.get(ColumnModel.WIDTH));
		setGroupId((String) cm.get(ColumnModel.GROUP_ID));
		setHidden((Boolean) cm.get(ColumnModel.HIDDEN));

		final Integer sortOrder = (Integer) cm.get(ColumnModel.SORT_ORDER);
		if (sortOrder != null && !sortOrder.equals(-1))
		{
			setSortDir((SortDirection) cm.get(ColumnModel.SORT_DIR));
			setSortOrder(sortOrder);
		}
	}

	public DynamicGrid(String login, String entityName, String columnName, Integer order, Integer width) {
		this.login = login;
		this.entityName = entityName;
		this.columnName = columnName;
		this.order = order;
		this.width = width;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Id
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@Id
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	@Id
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public SortDirection getSortDir() {
		return sortDir;
	}
	public void setSortDir(SortDirection sortDir) {
		this.sortDir = sortDir;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public void updateColumnModelMap(HashMap<String, HashMap<String, Object>> map) {
		final HashMap<String, Object> columnModelMap = map.get(getColumnName());

		columnModelMap.put(ColumnModel.WIDTH, getWidth());
		columnModelMap.put(ColumnModel.ORDER, getOrder());

		if (hidden != null)
			columnModelMap.put(ColumnModel.HIDDEN, hidden);

		final Integer sortOrder = getSortOrder();
		if (sortOrder != null && sortOrder > 0)
		{
			columnModelMap.put(ColumnModel.SORT_ORDER, sortOrder);
			columnModelMap.put(ColumnModel.SORT_DIR, getSortDir());
		}
	}
	public String toString() {
		return getConcatenation(order, " ", columnName);
	}

	private String login;
	private String entityName;
	private String columnName;
	private Integer order;
	private Integer width;
	private SortDirection sortDir;
	private Integer sortOrder;
	private String groupId;
	private Boolean hidden;
}