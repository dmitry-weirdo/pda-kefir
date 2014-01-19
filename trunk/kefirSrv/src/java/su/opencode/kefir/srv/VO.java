/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 13.11.2010 12:00:15$
*/
package su.opencode.kefir.srv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.opencode.kefir.srv.dynamicGrid.DynamicColumnVO;
import su.opencode.kefir.srv.dynamicGrid.DynamicGrid;
import su.opencode.kefir.srv.json.*;

import javax.persistence.Query;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static su.opencode.kefir.srv.renderer.RenderersFactory.DATE_RENDERER;
import static su.opencode.kefir.srv.renderer.RenderersFactory.STRING_RENDERER;
import static su.opencode.kefir.util.JsonUtils.putToJson;
import static su.opencode.kefir.util.ObjectUtils.*;
import static su.opencode.kefir.util.StringUtils.concat;

public abstract class VO extends JsonObject
{
	protected VO() {
	}

	protected VO(JsonObject po) {
		fromPO(po);
	}

	/**
	 * Создает VO указанного класса, передавая в качестве аргумента конструктора указанный объект.
	 *
	 * @param po			объект значений
	 * @param voClass класс VO
	 * @return созданный VO
	 */
	public static <T, N extends JsonObject> T newInstance(N po, Class<T> voClass) {
		try
		{
			return voClass.getConstructor(po.getClass()).newInstance(po);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Class<? extends JsonObject> getPoClass() {
		throw new RuntimeException("getPoClass must be implemented");
	}

	public Class getFieldType(String fieldName) {
		return getField(fieldName).getType();
	}

	public Object getFieldValue(String fieldName) {
		return invokeMethod(this, returnGetMethod(aClass, getField(fieldName)));
	}

	private Field getField(String fieldName) {
		try
		{
			return aClass.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected String getType(String fieldType) {
		if (fieldType.equals(Integer.class.getName()))
			return "int";

		if (fieldType.equals(Float.class.getName()))
			return "float";

		if (fieldType.equals(Boolean.class.getName()))
			return "boolean";

		if (fieldType.equals(Date.class.getName()))
			return "date";

		return "auto";
	}

	public HashMap<String, JsonReader> getJsonReaderMap() {
		final HashMap<String, JsonReader> map = new HashMap<String, JsonReader>();

		final Class<? extends VO> thisClass = this.getClass();
		for (Field f : thisClass.getDeclaredFields())
		{
			if (isJsonPresent(f))
				continue;

			final String name = f.getName();
			final String type = getType(f.getType().getName());
			final String dateFormat = type.equals("date") ? "c" : null;

			map.put(name, new JsonReader(name, type, dateFormat));
		}

		return map;
	}

	private boolean isJsonPresent(Field f) {
		for (Annotation annotation : f.getDeclaredAnnotations())
		{
			if (annotation instanceof Json)
				return ((Json) annotation).exclude();
		}

		return false;
	}

	@Transient
	public String getViewConfig() {
		final Class<? extends VO> aClass = this.getClass();
		for (Annotation annotation : aClass.getDeclaredAnnotations())
		{
			if (annotation instanceof ViewConfig)
				return ((ViewConfig) annotation).viewConfig();
		}

		return null;
	}

	@Transient
	public JSONObject getSelModel() {
		final Class<? extends VO> aClass = this.getClass();
		for (Annotation annotation : aClass.getDeclaredAnnotations())
		{
			if (annotation instanceof SelectionModel)
			{
				final JSONObject smJson = new JSONObject();
				final SelectionModel sm = (SelectionModel) annotation;

				putToJson(smJson, "name", sm.name());
				putToJson(smJson, "checkedFieldName", sm.checkedFieldName());

				final JSONObject configJson = new JSONObject();
				for (Config config : sm.config())
					putToJson(configJson, config.key(), config.value());

				putToJson(smJson, "config", configJson);

				return smJson;
			}
		}

		return null;
	}

	@Transient
	public JSONArray getPlugins(List<DynamicGrid> grids) {
		final JSONArray jsonArray = new JSONArray();

		putToJson(jsonArray, getColumnHeaderGroup(grids));

		return jsonArray;
	}

	public JSONObject getColumnHeaderGroup(List<DynamicGrid> grids) {
		final Class<? extends VO> aClass = this.getClass();
		boolean checkBoxPresent = false;
		for (Annotation annotation : aClass.getDeclaredAnnotations())
		{
			if (annotation instanceof SelectionModel)
			{
				final SelectionModel sm = (SelectionModel) annotation;
				if (sm.name().equals(SelectionModel.LIVEGRID_CHECKBOX_SELECTION_MODEL))
					checkBoxPresent = true;
			}
		}

		final Map<String, ColumnGroup> columnGroups = getColumnGroupMap(aClass);
		if (columnGroups.size() == 0)
			return null;

		for (DynamicGrid grid : grids)
		{
			final String groupId = grid.getGroupId();
			if (groupId == null)
				continue;

			if (columnGroups.get(groupId) == null)
				throw new RuntimeException(concat("Incorrect column groupId: ", groupId, " in VO ", aClass.getName()));
		}

		final JSONObject rv = new JSONObject();
		final JSONArray jsonArray = new JSONArray();
		if (checkBoxPresent)
			jsonArray.put(new JSONObject());

		final int columnModelSize = grids.size();
		int colspan = 0;
		DynamicGrid priorGrid = null;
		for (int i = 0; i < columnModelSize; i++)
		{
			final DynamicGrid grid = grids.get(i);
			final String groupId = grid.getGroupId();
			if (groupId == null || groupId.isEmpty())
			{
				if (priorGrid != null)
					addColumnGroup(jsonArray, columnGroups.get(priorGrid.getGroupId()), colspan);

				colspan = 0;
				jsonArray.put(new JSONObject());
				priorGrid = null;
			}
			else
			{
				if (priorGrid == null)
					priorGrid = grid;
				else
				{
					final String priorGroupId = priorGrid.getGroupId();
					if (!groupId.equals(priorGroupId))
					{
						addColumnGroup(jsonArray, columnGroups.get(priorGroupId), colspan);
						priorGrid = grid;
						colspan = 0;
					}
				}

				colspan++;
				if (i == columnModelSize - 1)
					addColumnGroup(jsonArray, columnGroups.get(priorGrid.getGroupId()), colspan);
			}
		}

		putToJson(rv, EXT_UX_GRID_COLUMNHEADERGROUP, jsonArray);

		return rv;
	}

	public Map<String, ColumnGroup> getColumnGroupMap(Class<? extends VO> aClass) {
		final Map<String, ColumnGroup> columnGroups = new HashMap<String, ColumnGroup>();
		for (Annotation annotation : aClass.getDeclaredAnnotations())
		{
			if (annotation instanceof ColumnGroup)
			{
				final ColumnGroup group = (ColumnGroup) annotation;
				columnGroups.put(group.id(), group);
			}

			if (annotation instanceof ColumnGroups)
			{
				final ColumnGroups groups = (ColumnGroups) annotation;
				for (ColumnGroup group : groups.groups())
					columnGroups.put(group.id(), group);
			}
		}

		return columnGroups;
	}

	private void addColumnGroup(JSONArray jsonArray, ColumnGroup columnGroup, int colspan) {
		final JSONObject json = new JSONObject();

		putToJson(json, "header", columnGroup.header());
		if (colspan > 1)
			putToJson(json, "colspan", colspan);

		jsonArray.put(json);
	}

	@Transient
	public JSONArray getJsonReaders(HashMap<String, JsonReader> jsonReaderMap) {
		final JSONArray jsonArray = new JSONArray();
		for (String key : jsonReaderMap.keySet())
			jsonArray.put(jsonReaderMap.get(key).toJson());

		return jsonArray;
	}

	protected static ColumnModel getColumnModel(Class aClass, String fieldName) {
		try
		{
			final Field field = aClass.getDeclaredField(fieldName);
			for (Annotation annotation : field.getDeclaredAnnotations())
			{
				if (annotation instanceof ColumnModel)
				{
					return (ColumnModel) annotation;
				}
			}
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}

		throw new ClientException(concat(aClass.getName(), ".", fieldName, " не имеет аннотации @ColumnModel"));
	}

	protected HashMap<String, Object> getColumnModelMap(String dataIndex, ColumnModel cm, String newHeader, Integer newOrder) {
		final boolean checkbox = cm.checkbox();
		final boolean hidden = cm.hidden();
		final boolean sortable = cm.sortable();
		final String header = newHeader == null || newHeader.isEmpty() ? cm.header() : newHeader;
		final String tooltip = cm.tooltip();
		final String renderer = cm.renderer();
		final String editor = cm.editor();
		final String groupId = cm.groupId();
		final int sortOdrer = cm.sort().sortOrder();
		final int order = newOrder == null || newOrder < INITIAL_ORDER ? this.order++ : newOrder;
		final int width = cm.width();
		final Map<String, String> plugins = getPlugins(cm.plugins());

		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(ColumnModel.DATA_INDEX, dataIndex);

		if (hidden)
			map.put(ColumnModel.HIDDEN, hidden);

		if (checkbox)
			map.put(ColumnModel.CHECKBOX, checkbox);

		map.put(ColumnModel.SORTABLE, sortable);
		map.put(ColumnModel.HEADER, header);

		if (tooltip.isEmpty())
			map.put(ColumnModel.TOOLTIP, header);
		else
			map.put(ColumnModel.TOOLTIP, tooltip);

		if (!renderer.isEmpty())
			map.put(ColumnModel.RENDERER, renderer);

		if (!editor.isEmpty())
			map.put(ColumnModel.EDITOR, editor);

		if (!groupId.isEmpty())
			map.put(ColumnModel.GROUP_ID, groupId);

		if (sortOdrer != -1)
		{
			map.put(ColumnModel.SORT_DIR, cm.sort().sortDir());
			map.put(ColumnModel.SORT_ORDER, sortOdrer);
		}

		map.put(ColumnModel.ORDER, order);
		map.put(ColumnModel.WIDTH, width);

		for (String key : plugins.keySet())
			map.put(key, plugins.get(key));

		return map;
	}

	protected Map<String, String> getPlugins(ColumnModelPlugin[] columnModelPlugins) {
		final Map<String, String> plugins = new HashMap<String, String>();
		for (ColumnModelPlugin plugin : columnModelPlugins)
			plugins.put(plugin.name(), plugin.value());

		return plugins;
	}

	public HashMap<String, Object> getDefaultSortInfoMap() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		final StringBuffer defaultSortFields = new StringBuffer();
		final StringBuilder defaultSortDir = new StringBuilder();
		final StringBuffer defaultHeader = new StringBuffer();
		int maxOrder = -1;
		boolean firstColumnName = false;
		boolean order = false;
		final Class<? extends VO> thisClass = this.getClass();
		for (Field f : thisClass.getDeclaredFields())
		{
			for (Annotation annotation : f.getDeclaredAnnotations())
			{
				if (annotation instanceof ColumnModel)
				{
					if (f.getType().equals(List.class))
						continue;

					final String name = f.getName();
					final ColumnModel cm = (ColumnModel) annotation;

					if (!firstColumnName)
					{
						firstColumnName = true;
						defaultHeader.append(cm.header());
						defaultSortFields.append(name);
					}

					final int sortOrder = cm.sort().sortOrder();
					if (sortOrder >= INITIAL_ORDER)
					{
						if (!order)
						{
							defaultHeader.delete(0, defaultHeader.length());
							defaultSortFields.delete(0, defaultSortFields.length());
						}

						order = true;

						defaultHeader.append(cm.header()).append(", ");
						defaultSortFields.append(name).append(",");
						defaultSortDir.append(cm.sort().sortDir().name()).append(",");

						if (maxOrder < sortOrder)
							maxOrder = sortOrder;
					}

					break;
				}
			}
		}

		if (!order)
		{
			map.put("field", defaultSortFields);
			map.put("direction", SortDirection.ASC.name());
			map.put("header", defaultHeader);
		}
		else
		{
			final String sortDir = defaultSortDir.substring(0, defaultSortDir.length() - 1);
			map.put("direction", sortDir);
			map.put("field", defaultSortFields.substring(0, defaultSortFields.length() - 1));
			map.put("header", defaultHeader.substring(0, defaultHeader.length() - 2));

			if (maxOrder != sortDir.split(",").length - 1)
				throw new RuntimeException("Неверная сортировка полей!");
		}

		return map;
	}

	@Transient
	public HashMap<String, HashMap<String, Object>> getColumnModelMap() {
		final HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
		String firstColumnName = null;
		boolean order = false;
		final Class<? extends VO> thisClass = this.getClass();
		for (Field f : thisClass.getDeclaredFields())
		{
			for (Annotation annotation : f.getDeclaredAnnotations())
			{
				if (annotation instanceof ColumnModel)
				{
					if (
						f.getType().equals(List.class)
						|| (f.isAnnotationPresent(Json.class) && f.getAnnotation(Json.class).exclude())
						)
					{
						continue;
					}

					final String name = f.getName();
					final ColumnModel cm = (ColumnModel) annotation;
					final HashMap<String, Object> columnModelMap = getColumnModelMap(name, cm, null, null);

					if (firstColumnName == null)
						firstColumnName = name;

					final Integer sortOrder = (Integer) columnModelMap.get(ColumnModel.SORT_ORDER);
					if (sortOrder != null && sortOrder >= INITIAL_ORDER)
						order = true;

					map.put(name, columnModelMap);
					break;
				}
			}
		}

		if (!order)
		{
			final HashMap<String, Object> columnModelMap = map.get(firstColumnName);
			columnModelMap.put(ColumnModel.SORT_ORDER, INITIAL_ORDER);
			columnModelMap.put(ColumnModel.SORT_DIR, SortDirection.ASC);
		}

		return map;
	}

	public JSONArray getColumnModelsJson(HashMap<String, HashMap<String, Object>> map) {
		final JSONArray jsonArray = new JSONArray();
		for (String key : map.keySet())
		{
			try
			{
				final HashMap<String, Object> value = map.get(key);
				final Integer index = (Integer) value.get(ColumnModel.ORDER);
				final Boolean checkbox = (Boolean) value.get(ColumnModel.CHECKBOX);
				final JSONObject ob = new JSONObject(value);
				jsonArray.put(index, checkbox == null || !checkbox ? ob : concat("new Ext.grid.CheckColumn(", ob, ")"));
			}
			catch (JSONException e)
			{
				throw new RuntimeException(e);
			}
		}

		return jsonArray;
	}

	public static VO getInstance(String className) {
		return getInstance(getClassForName(className));
	}

	public static VO getInstance(Class aClass) {
		return getNewInstance(aClass);
	}

	public static <T extends VO> Class getPoClass(Class<T> voClass) {
		return getInstance(voClass).getPoClass();
	}

	@SuppressWarnings("unchecked")
	public static <T extends JsonObject> Class<T> getPoClass(String className) {
		return (Class<T>) getInstance(getClassForName(className)).getPoClass();
	}

	public void fromPO(JsonObject po) {
		final Class voClass = this.getClass();
		for (Field f : voClass.getDeclaredFields())
		{
			final Method getMethod = returnGetMethod(po.getClass(), f);
			final Class voType = f.getType();
			if (getMethod == null || !voType.equals(getMethod.getReturnType()))
				continue;

			final Method setMethod = returnSetMethod(voClass, f, voType);
			if (setMethod == null)
				continue;

			invokeMethod(this, setMethod, invokeMethod(po, getMethod));
		}
	}

	@SuppressWarnings(value = "unchecked")
	public static <T extends VO> List<T> fromPO(List<? extends JsonObject> list, Class<T> aClass) {
		final List<T> rv = new ArrayList<T>();
		for (JsonObject po : list)
		{
			try
			{
				T vo2 = (T) Class.forName(aClass.getName()).newInstance();
				vo2.fromPO(po);
				rv.add(vo2);
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}

		return rv;
	}

	@SuppressWarnings(value = "unchecked")
	public static <T extends VO, E> List<T> getVOList(List resultList, Class<E> entityClass, Class<T> voClass) {
		try
		{
			List<T> result = new ArrayList<T>();

			Constructor<T> voConstructor = voClass.getConstructor(entityClass);

			for (Object o : resultList)
				result.add(voConstructor.newInstance((E) o));

			return result;
		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static <T extends VO> List<T> getVOList(Query query, Class<T> voClass) {
		return getVOList((List<Object[]>) query.getResultList(), Object[].class, voClass);
	}

	public static <T extends VO, E extends JsonObject> List<T> getVOList(Query query, Class<E> entityClass, Class<T> voClass) {
		return getVOList(query.getResultList(), entityClass, voClass);
	}

	public String[] getServices() {
		return new String[] { };
	}

	public VO getVOClass(List<DynamicColumnVO> dynamicColumns) {
		return this;
	}

	public List<DynamicColumnVO> getDynamicGridColumns(Map<String, Object> services) {
		return Collections.emptyList();
	}

	public String getRendered(String columnName) {
		final Field field = getField(columnName);
		final Class fieldType = field.getType();
		for (Annotation a : field.getAnnotations())
		{
			if (a instanceof ColumnModel)
			{
				final String renderer = ((ColumnModel) a).renderer();
				if (!renderer.isEmpty())
					return renderer;

				if (fieldType.equals(Date.class))
					return DATE_RENDERER;

				return STRING_RENDERER;
			}
		}

		throw new RuntimeException(concat("Can't find CellRenderer for ", fieldType));
	}

	public String toString() {
		sb.delete(0, sb.length())
			.append("\n");

		for (Field f : this.getClass().getDeclaredFields())
		{
			for (Annotation a : f.getDeclaredAnnotations())
			{
				if (!(a instanceof ColumnModel))
					continue;

				sb.append(getFieldValue(f.getName())).append("\t|\t");
				break;
			}
		}

		return sb.toString();
	}

	protected void fillQueryConstruction(StringBuffer sb) {
		sb.append("o");
	}

	private static final StringBuffer sb = new StringBuffer();
	private static final int INITIAL_ORDER = 0;

	public static final String COLUMN_MODEL = "columns";
	public static final String JSON_READER = "jsonReaderFields";
	public static final String PLUGINS = "plugins";
	public static final String SORT_INFO = "sortInfo";
	public static final String DEFAULT_SORT_INFO = "defaultSortInfo";
	public static final String VIEW_CONFIG = "viewConfig";
	public static final String SEL_MODEL = "selModel";
	public static final String EXT_UX_GRID_COLUMNHEADERGROUP = "Ext_ux_grid_ColumnHeaderGroup";

	private final Class<? extends VO> aClass = this.getClass();
	protected int order = 0;
}