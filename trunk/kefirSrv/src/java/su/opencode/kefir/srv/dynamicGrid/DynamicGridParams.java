/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 10.11.2010 10:23:44$
*/
package su.opencode.kefir.srv.dynamicGrid;


import su.opencode.kefir.srv.json.SortDirection;
import static su.opencode.kefir.util.JsonUtils.*;

import java.util.Map;

public class DynamicGridParams
{
	public DynamicGridParams(Map<String, String[]> map) {
		methodType = getIntegerParam(map, "methodType");
		entityName = getStringParam(map, "entityName", false);
		columnName = getStringParam(map, "columnName", false);
		newWidth = getIntegerParam(map, "newWidth");
		columnIndex = getIntegerParam(map, "columnIndex");
		newIndex = getIntegerParam(map, "newIndex");
		hidden = getBooleanParam(map, "hidden");
		sortOrder = getIntegerParam(map, "sortOrder");
		clear = getBooleanParam(map, "clear");
		sortDir = getStringParam(map, "sortDir");

		if (methodType == null || entityName == null)
			throw new RuntimeException("MethodType, entityName and columnName can't be null");
	}
	public Integer getMethodType() {
		return methodType;
	}
	public void setMethodType(Integer methodType) {
		this.methodType = methodType;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getNewWidth() {
		return newWidth;
	}
	public void setNewWidth(Integer newWidth) {
		this.newWidth = newWidth;
	}
	public Integer getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}
	public Integer getNewIndex() {
		return newIndex;
	}
	public void setNewIndex(Integer newIndex) {
		this.newIndex = newIndex;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getSortDir() {
		return sortDir;
	}
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}
	public Boolean getClear() {
		return clear;
	}
	public void setClear(Boolean clear) {
		this.clear = clear;
	}
	public SortDirection getSortDirection() {
		if (sortDir.indexOf(",") == -1)
			return SortDirection.valueOf(sortDir);

		return null;
	}
	public String[] getDirections() {
		return sortDir == null ? null : sortDir.split(",");
	}
	public String[] getColumns() {
		return columnName == null ? null : columnName.split(",");
	}

	private Integer methodType;
	private String entityName;
	private String columnName;
	private Integer newWidth;
	private Integer columnIndex;
	private Integer newIndex;
	private Boolean hidden;
	private Integer sortOrder;
	private String sortDir;
	private Boolean clear;
}