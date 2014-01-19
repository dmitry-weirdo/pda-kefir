/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 30.09.2010 11:30:14$
*/
package su.opencode.kefir.srv;

import static su.opencode.kefir.util.JsonUtils.getIntegerParam;
import static su.opencode.kefir.util.JsonUtils.getStringParam;
import static su.opencode.kefir.util.ObjectUtils.getClassForName;
import static su.opencode.kefir.util.StringUtils.concat;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.ObjectUtils;

import java.util.Map;

public class SortConfig extends JsonObject
{
	public SortConfig() {
		this.start = DEFAULT_START;
		this.limit = DEFAULT_LIMIT;
//		this.sortBy = DEFAULT_SORT_BY; // Нельзя, т.к. не сработает установка поля сортировки по умолчанию, и запрос к сущности, у которой нет поля "name" будет падать на order by по незнакомому полю
		this.sortDir = DEFAULT_SORT_DIR;
	}
	public SortConfig(Map<String, String[]> map) {
		final Integer start = getIntegerParam(map, START_PARAM_NAME);
		final Integer limit = getIntegerParam(map, LIMIT_PARAM_NAME);
		final String sortBy = getStringParam(map, SORT_BY_PARAM_NAME, false);
		final String sortDir = getStringParam(map, SORT_DIR_PARAM_NAME, false);

		this.start = start != null ? start : DEFAULT_START;
		this.limit = limit != null ? limit : DEFAULT_LIMIT;
		this.sortBy = sortBy != null ? sortBy : DEFAULT_SORT_BY;
		this.sortDir = sortDir != null ? sortDir : DEFAULT_SORT_DIR;

		entityName = getStringParam(map, ENTITY_NAME, false);
	}
	public SortConfig(Integer start, Integer limit, String sortBy, String sortDir, String entityName) {
		this.start = start;
		this.limit = limit;
		this.sortBy = sortBy;
		this.sortDir = sortDir;
		this.entityName = entityName;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortDir() {
		return sortDir == null ? "ASC" : sortDir;
	}
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public static SortConfig getEmptyConfig() {
		return new SortConfig();
	}
	public void setCustomStartAndLimit(Integer fromNumber, Integer toNumber) {
		if (fromNumber != null)
			start = fromNumber - 1;

		if (toNumber != null)
			limit = toNumber - (fromNumber == null ? 0 : fromNumber) + 1;
	}

	public String getSortBy(String sortBy) {
		throw new RuntimeException("getSortBy method must be implemented");
	}
	public String getSqlOrderBy() {
		return getSqlOrderBy("o");
	}
	public String getNativeSqlOrderBy() {
		final StringBuffer sb = new StringBuffer("order by ");
		final String[] directions = sortDir.split(",");
		final String[] fields = sortBy.split(",");

		for (int i = 0; i < fields.length; i++)
			appendSqlParam(sb, getSortBy(fields[i]), directions[i]);

		return sb.substring(0, sb.length() - 2);
	}
	public String getSqlOrderBy(String prefix) {
		if (sortBy == null)
			return "";

		if (entityName == null || entityName.isEmpty())
			return concat("order by ", prefix, ".", sortBy, " ", sortDir);

		final StringBuffer sb = new StringBuffer("order by ");
		final String[] directions = sortDir.split(",");
		final String[] fields = sortBy.split(",");
		final Class aClass = getClassForName(entityName);

		for (int i = 0; i < fields.length; i++)
		{
			final String fieldName = fields[i];
			final ColumnModel cm = VO.getColumnModel(aClass, fieldName);
			if (!cm.sortable())
				throw new RuntimeException(concat("Column ", fieldName, " of entity ", entityName, " is not sortable"));

			final String sortParam = cm.sortParam();
			appendSqlParam(sb, prefix, sortParam.isEmpty() ? fieldName : sortParam, directions[i]);
		}

		return sb.substring(0, sb.length() - 2);
	}
	private void appendSqlParam(StringBuffer sb, String prefix, String fieldName, String direction) {
		sb.append(prefix).append(".").append(fieldName).append(" ").append(direction).append(", ");
	}
	protected void appendSqlParam(StringBuffer sb, String fieldName, String direction) {
		sb.append(fieldName).append(" ").append(direction).append(", ");
	}

	public boolean isSortDirAsc() {
		if (sortDir.equalsIgnoreCase("asc"))
			return true;

		if (sortDir.equalsIgnoreCase("desc"))
			return false;

		throw new IllegalArgumentException(concat("Incorrect sortDir: ", sortDir));
	}

	protected Integer start;
	protected Integer limit;

	@Json(name = SORT_BY_PARAM_NAME, uppercase = false)
	protected String sortBy;

	@Json(name = SORT_DIR_PARAM_NAME)
	protected String sortDir;

	@Json(uppercase = false)
	protected String entityName;

	private static final String SORT_BY_PARAM_NAME = "sort";
	private static final String SORT_DIR_PARAM_NAME = "dir";
	private static final String START_PARAM_NAME = "start";
	private static final String LIMIT_PARAM_NAME = "limit";
	private static final String ENTITY_NAME = "entityName";

	public static final int DEFAULT_START = 0;
	public static final int DEFAULT_LIMIT = 200;
	public static final String DEFAULT_SORT_BY = "name";
	public static final String DEFAULT_SORT_DIR = "ASC";
}