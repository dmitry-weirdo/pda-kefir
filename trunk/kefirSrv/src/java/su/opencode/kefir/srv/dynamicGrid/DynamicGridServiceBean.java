/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 24.11.2010 12:10:36$
*/
package su.opencode.kefir.srv.dynamicGrid;

import org.json.JSONException;
import org.json.JSONObject;
import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.StringUtils.*;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.srv.VO;
import static su.opencode.kefir.srv.VO.*;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.SortDirection;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Stateless
public class DynamicGridServiceBean implements DynamicGridService
{
	public DynamicGridServiceBean() {
	}
	public DynamicGridServiceBean(EntityManager em) {
		this.em = em;
	}
	private String getLogin() {
//		return context.getCallerPrincipal().getName();    // todo: use only this instead of try-catch construction

		// todo: remove this
		try
		{
			return context.getCallerPrincipal().getName();
		}
		catch (IllegalStateException e)
		{
			return "test_minstroy_login";
		}
	}
	public String[][] getReport(List<? extends VO> list, String entityName) {
		if (list == null || list.size() == 0)
			return null;

		final List<DynamicGrid> grids = getDynamicGridList(entityName);
		int columnCount = 0;
		for (DynamicGrid grid : grids)
		{
			if (grid.getHidden() == null)
				columnCount++;
		}

		final String[][] rv = new String[list.size() + 1][columnCount];
		for (int i = 0; i < list.size(); i++)
		{
			final VO vo = list.get(i);
			fillHeader(grids, rv, entityName, i);
			fillCells(grids, rv, vo, i);
		}

		return rv;
	}
	@SuppressWarnings(value = "unchecked")
	private void fillCells(List<DynamicGrid> grids, String[][] rv, VO vo, int i) {
		int j = 0;
		for (DynamicGrid grid : grids)
		{
			if (grid.getHidden() != null && grid.getHidden())
				continue;

			String columnName = grid.getColumnName();
			if (columnName.contains("_"))
				columnName = columnName.substring(0, columnName.indexOf("_"));

			final Object value = vo.getFieldValue(columnName);
			if (value == null)
			{
				rv[i + 1][j++] = "";
				continue;
			}

			if (value instanceof String)
			{
				rv[i + 1][j++] = getStringFromUTF8(((String) value));
				continue;
			}

			if (value instanceof Date)
			{
				rv[i + 1][j++] = getDayMonthYear(((Date) value));
				continue;
			}

			if (value instanceof List)
			{
				rv[i + 1][j++] = getValue(grid, (List<VO>) value);
				continue;
			}

			rv[i + 1][j++] = value.toString();
		}
	}
	private void fillHeader(List<DynamicGrid> grids, String[][] rv, String entityName, int i) {
		if (i == 0)
		{
			final HashMap<String, HashMap<String, Object>> columnModelMap = VO.getInstance(entityName).getColumnModelMap();
			int j = 0;
			for (DynamicGrid grid : grids)
			{
				if (grid.getHidden() != null && grid.getHidden())
					continue;

				final HashMap<String, Object> map = columnModelMap.get(grid.getColumnName());
				rv[i][j++] = ((String) map.get(ColumnModel.HEADER)).replace("<br/>", "");
			}
		}
	}
	private String getValue(DynamicGrid grid, List<VO> list) {
		for (VO vo : list)
		{
			if (vo.getFieldValue("columnName").equals(grid.getColumnName()))
			{
				final Object value = vo.getFieldValue("value");

				if (value == null)
					return null;

				if (value instanceof String)
					return getStringFromUTF8(((String) value));

				if (value instanceof Date)
					return getDayMonthYear(((Date) value));

				return value.toString();
			}
		}

		return null;
	}
	public JSONObject getDynamicGridParams(String entityName, List<DynamicColumnVO> dynamicColumns) {
		final List<DynamicGrid> grids = getDynamicGridList(entityName);
		final VO vo = VO.getInstance(entityName).getVOClass(dynamicColumns);

		final JSONObject json = new JSONObject();
		final HashMap<String, HashMap<String, Object>> columnModelMap = vo.getColumnModelMap();
		final HashMap<String, Object> defaultSortInfoMap = vo.getDefaultSortInfoMap();
		final HashMap<String, Object> sortInfoMap = fillSortInfoMap(defaultSortInfoMap);

		checkColumns(grids, columnModelMap, entityName, sortInfoMap);

		if (grids.size() != 0)
		{
			for (DynamicGrid dynamicGrid : grids)
				dynamicGrid.updateColumnModelMap(columnModelMap);
		}
		else
		{
			for (String columnName : columnModelMap.keySet())
			{
				final HashMap<String, Object> cm = columnModelMap.get(columnName);
				final DynamicGrid grid = new DynamicGrid(getLogin(), entityName, columnName, cm);
				em.persist(grid);

				grids.add(grid);
			}

			Collections.sort(grids, new DynamicGridComparator());
		}

		try
		{
			json.put("success", "true");
			json.put(COLUMN_MODEL, vo.getColumnModelsJson(columnModelMap));
			json.put(JSON_READER, vo.getJsonReaders(vo.getJsonReaderMap()));
			json.put(PLUGINS, vo.getPlugins(grids));
			json.put(SORT_INFO, sortInfoMap);
			json.put(VIEW_CONFIG, vo.getViewConfig());
			json.put(SEL_MODEL, vo.getSelModel());
		}
		catch (JSONException e)
		{
			throw new RuntimeException(e);
		}

		return json;
	}
	private HashMap<String, Object> fillSortInfoMap(HashMap<String, Object> defaultSortInfoMap) {
		final HashMap<String, Object> sortInfoMap = new HashMap<String, Object>();
		sortInfoMap.put(FIELD, defaultSortInfoMap.get(FIELD));
		sortInfoMap.put(DIRECTION, defaultSortInfoMap.get(DIRECTION));
		sortInfoMap.put(DEFAULT_SORT_INFO, defaultSortInfoMap);

		return sortInfoMap;
	}
	private void checkColumns(List<DynamicGrid> grids, HashMap<String, HashMap<String, Object>> columnModelMap,
														String entityName, HashMap<String, Object> sortInfoMap)
	{
		final String columnsOrderErrorMsg = checkColumnsOrder(columnModelMap);
		if (columnsOrderErrorMsg != null)
			throw new ClientException(concat("Неверный порядок колонок в сущности ", entityName, ": ", columnsOrderErrorMsg));

		if (grids.size() != columnModelMap.size())
			removeAllColumns(grids);

		if (grids.size() == 0)
			return;

		for (DynamicGrid grid : grids)
		{
			if (columnModelMap.get(grid.getColumnName()) == null)
			{
				removeAllColumns(grids);
				break;
			}
		}

		final StringBuffer defaultSortFields = new StringBuffer();
		final StringBuffer defaultSortDir = new StringBuffer();
		final List<DynamicGrid> sortGrids = new ArrayList<DynamicGrid>();
		for (DynamicGrid grid : grids)
			if (grid.getSortOrder() != null)
				sortGrids.add(grid);

		Collections.sort(sortGrids, new DynamicGridSortComparator());

		for (DynamicGrid grid : sortGrids)
		{
			final Integer order = grid.getSortOrder();
			if (order != null)
			{
				defaultSortFields.append(grid.getColumnName()).append(",");
				defaultSortDir.append(grid.getSortDir()).append(",");
			}
		}

		sortInfoMap.put(DIRECTION, defaultSortDir.substring(0, defaultSortDir.length() - 1));
		sortInfoMap.put(FIELD, defaultSortFields.substring(0, defaultSortFields.length() - 1));
	}
	private String checkColumnsOrder(HashMap<String, HashMap<String, Object>> columnModelMap) {
		final List<Integer> orders = new ArrayList<Integer>();
		for (String key : columnModelMap.keySet())
			orders.add((Integer) columnModelMap.get(key).get(ColumnModel.ORDER));

		Collections.sort(orders);

		for (int i = 0; i < orders.size(); i++)
			if (orders.get(i) != i)
				return concat("ожидается номер колонки: ", i, ", указан: ", orders.get(i));

		return null;
	}

	public void deleteDynamicGrid(String entityName) {
		final Query query = em.createNamedQuery("deleteByLoginAndEntityName")
			.setParameter("login", getLogin())
			.setParameter("entityName", entityName);

		query.executeUpdate();
	}
	private boolean removeAllColumns(List<DynamicGrid> grids) {
		for (DynamicGrid grid : grids)
			em.remove(grid);

		grids.clear();

		em.flush();

		return true;
	}
//	public VO getVOClass(String entityName) {
//		final Class aClass = VO.getClassForName(entityName);
//
//		if (aClass == OffencesVO.class)
//		{
//			final OffencesVO vo = (OffencesVO) VO.getInstance(aClass);
//
//			final List<OffenderOffenceVO> offenderOffenceList = new ArrayList<OffenderOffenceVO>();
//			for (OffenceArticle article : executiveService.offenceArticleList())
//				offenderOffenceList.add(new OffenderOffenceVO(article));
//
//			vo.setOffenderOffenceList(offenderOffenceList);
//
//			return vo;
//		}
//
//		if (aClass == OffencesRegionalVO.class)
//		{
//			final OffencesRegionalVO vo = (OffencesRegionalVO) VO.getInstance(aClass);
//
//			final List<OffenderOffenceVO> offenderOffenceList = new ArrayList<OffenderOffenceVO>();
//			for (OffenceArticle article : executiveService.offenceArticleList())
//				offenderOffenceList.add(new OffenderOffenceVO(article));
//
//			vo.setOffenderOffenceList(offenderOffenceList);
//
//			return vo;
//		}
//
//		if (aClass == DutyVO.class)
//		{
//			final DutyVO vo = (DutyVO) VO.getInstance(aClass);
//
//			final List<DynamicColumnVO> columnVOs = new ArrayList<DynamicColumnVO>();
//			for (DutyType dutyType : dutyService.getDutyTypeListByGroup(null))
//			{
//				final String columnHeader = getStringFromUTF8(dutyType.getName());
//				final DynamicColumnVO dynamicColumnVO =
//					new DynamicColumnVO(dutyType.getFieldName(), columnHeader, DutyVO.DUTY_TYPES_COLUMN_MODEL, (double) 0);
//
//				columnVOs.add(dynamicColumnVO);
//			}
//
//			vo.setDutyTypes(columnVOs);
//
//			return vo;
//		}
//
//		if (aClass == DutyRegionalVO.class)
//		{
//			final DutyRegionalVO vo = (DutyRegionalVO) VO.getInstance(aClass);
//
//			final List<DynamicColumnVO> columnVOs = new ArrayList<DynamicColumnVO>();
//			for (DutyType dutyType : dutyService.getDutyTypeListByGroup(null))
//			{
//				final String columnName = dutyType.getFieldName();
//				final String columnHeader = getStringFromUTF8(dutyType.getName());
//				final DynamicColumnVO dynamicColumnVO =
//					new DynamicColumnVO(columnName, columnHeader, DutyRegionalVO.DUTY_TYPES_COLUMN_MODEL, (double) 0);
//
//				columnVOs.add(dynamicColumnVO);
//			}
//
//			vo.setDutyTypes(columnVOs);
//
//			return vo;
//		}
//
//		return VO.getInstance(aClass);
	//	}
	public void setColumnHidden(String entityName, String columnName, boolean newHidden) {
		if (columnName == null || columnName.isEmpty())
			return;

		final String sql = concat("update DynamicGrid d set d.hidden = :hidden ",
			"where d.login = :login and d.entityName = :entityName and d.columnName = :columnName");

		final Query query = em.createQuery(sql)
			.setParameter("login", getLogin())
			.setParameter("entityName", entityName)
			.setParameter("columnName", columnName)
			.setParameter("hidden", newHidden ? newHidden : null);

		query.executeUpdate();
	}
	public void setColumnWidth(String entityName, String columnName, Integer newWidth) {
		final String sql = getConcatenation("update DynamicGrid d set d.width = :width ",
			"where d.login = :login and d.entityName = :entityName and d.columnName = :columnName");

		final Query query = em.createQuery(sql)
			.setParameter("login", getLogin())
			.setParameter("entityName", entityName)
			.setParameter("columnName", columnName)
			.setParameter("width", newWidth);

		query.executeUpdate();
	}
	public void setDefaultSort(String entityName, String[] columns, String[] directions) {
		final List<DynamicGrid> grids = getDynamicGridListBySort(entityName);
		for (DynamicGrid grid : grids)
		{
			if (grid.getSortOrder() != null)
			{
				grid.setSortOrder(null);
				grid.setSortDir(null);
			}
		}

		for (int i = 0; i < columns.length; i++)
		{
			final String column = columns[i];
			for (DynamicGrid grid : grids)
			{
				if (!column.equals(grid.getColumnName()))
					continue;

				grid.setSortOrder(i);
				grid.setSortDir(SortDirection.valueOf(directions[i]));

				break;
			}

		}
	}
	public void setSort(String entityName, String columnName, Integer sortOrder, SortDirection sortDir, boolean isClear) {
		final List<DynamicGrid> grids = getDynamicGridListBySort(entityName);
		boolean rightMove = false;
		for (DynamicGrid grid : grids)
		{
			if (grid.getColumnName().equals(columnName))
			{
				if (grid.getSortOrder() != null)
					rightMove = true;

				grid.setSortDir(sortDir);
				if (sortOrder != null)
					grid.setSortOrder(sortOrder);
			}
			else
			{
				final Integer order = grid.getSortOrder();
				if (order == null || sortOrder == null)
					continue;

				if (isClear)
				{
					grid.setSortOrder(null);
					grid.setSortDir(null);
					continue;
				}

				if (!rightMove)
				{
					if (sortOrder <= order)
						grid.setSortOrder(order + 1);
				}
				else
				{
					if (sortOrder >= order)
						grid.setSortOrder(order - 1);
				}
			}
		}
	}
	@SuppressWarnings(value = "unchecked")
	private List<DynamicGrid> getDynamicGridListBySort(String entityName) {
		final Query query = em.createNamedQuery("listSortByLoginAndEntityName")
			.setParameter("login", getLogin())
			.setParameter("entityName", entityName);

		return query.getResultList();
	}
	public void changeColumnsOrder(String entityName, String columnName, Integer oldIndex, Integer newIndex) {
		if (oldIndex.equals(newIndex))
			return;

		final List<DynamicGrid> grids = getDynamicGridList(entityName);
		final String groupId = grids.get(oldIndex).getGroupId();
		if (groupId == null)
			simpleColumnReorder(oldIndex, newIndex, grids);
		else
			groupColumnReorder(oldIndex, newIndex, grids, groupId);
	}
	private void groupColumnReorder(Integer oldIndex, Integer newIndex, List<DynamicGrid> grids, String groupId) {
		Integer minGroupId = null;
		Integer maxGroupId = null;
		for (DynamicGrid g : grids)
		{
			if (groupId.equals(g.getGroupId()) && minGroupId == null)
				minGroupId = g.getOrder();

			if (groupId.equals(g.getGroupId()) && minGroupId != null)
				maxGroupId = g.getOrder();

			if (!groupId.equals(g.getGroupId()) && minGroupId != null && maxGroupId != null)
				break;
		}

		if (minGroupId == null || maxGroupId == null)
			throw new RuntimeException();

		final int count = maxGroupId - minGroupId + 1;
		if (minGroupId > newIndex)
		{
			for (int i = newIndex; i < minGroupId; i++)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() + count);
			}

			for (int i = minGroupId; i <= maxGroupId; i++)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() - (minGroupId - newIndex));
			}

			Collections.sort(grids, new DynamicGridComparator());
			return;
		}

		if (newIndex > maxGroupId)
		{
			for (int i = newIndex; i > maxGroupId; i--)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() - count);
			}

			for (int i = minGroupId; i <= maxGroupId; i++)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() + (newIndex - maxGroupId));
			}

			Collections.sort(grids, new DynamicGridComparator());
			return;
		}

		simpleColumnReorder(oldIndex, newIndex, grids);
	}
	private void simpleColumnReorder(Integer oldIndex, Integer newIndex, List<DynamicGrid> grids) {
		if (oldIndex > newIndex)
		{
			for (int i = newIndex; i < oldIndex; i++)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() + 1);
			}
		}
		else
		{
			for (int i = newIndex; i > oldIndex; i--)
			{
				final DynamicGrid grid = grids.get(i);
				grid.setOrder(grid.getOrder() - 1);
			}
		}

		grids.get(oldIndex).setOrder(newIndex);
	}

	@SuppressWarnings(value = "unchecked")
	public List<DynamicGrid> getDynamicGridList(String entityName) {
		final Query query = em.createNamedQuery("listByLoginAndEntityName")
			.setParameter("login", getLogin())
			.setParameter("entityName", entityName);

		return query.getResultList();
	}

	private static final String FIELD = "field";
	private static final String DIRECTION = "direction";

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;
}