/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 20.12.2010 14:39:58$
*/
package su.opencode.kefir.srv;


import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.util.ObjectUtils.getClassForName;

public class SimpleQueryBuilder
{
	public SimpleQueryBuilder(EntityManager em, FilterConfig filterConfig) {
		this(em, filterConfig, null);
	}

	public SimpleQueryBuilder(EntityManager em, SortConfig sortConfig) {
		this(em, null, sortConfig);
	}

	@SuppressWarnings(value = "unchecked")
	public SimpleQueryBuilder(EntityManager em, FilterConfig filterConfig, SortConfig sortConfig) {
		this.em = em;
		this.filterConfig = filterConfig;
		this.sortConfig = sortConfig;

		String voClassName = filterConfig != null ? filterConfig.getEntityName() : null;
		if (voClassName == null)
			voClassName = sortConfig != null ? sortConfig.getEntityName() : null;

		if (voClassName == null)
			throw new RuntimeException("Can't get voClassName from filterConfig or sortConfig");

		voClass = getClassForName(voClassName);
		voInstance = VO.getInstance(voClass);
		poClass = voInstance.getPoClass();
	}

	@Deprecated
	public Query getQuery(FilterConfig filterConfig, SortConfig sortConfig) {
		this.filterConfig = filterConfig;
		this.sortConfig = sortConfig;

		return getQuery(sortConfig == null);
	}

	private Query getQuery(boolean count) {
		final StringBuffer sb = new StringBuffer();
		sb.append("select ")
			.append(count ? "count(o) " : "o ")
			.append("from ").append(poClass.getSimpleName()).append(" o ");

		if (filterConfig != null)
			filterConfig.setSqlWhere(sb);

		if (!count)
			sb.append(sortConfig.getSqlOrderBy());

		final Query query = em.createQuery(sb.toString());

		if (!count)
		{
			query.setFirstResult(sortConfig.getStart())
				.setMaxResults(sortConfig.getLimit());
		}

		return query;
	}

	private Query getQuery2(boolean count) {
		sb.delete(0, sb.length())
			.append("select ");

		if (count)
			sb.append("count(o)");
		else
			voInstance.fillQueryConstruction(sb);

		sb.append(" from ").append(poClass.getSimpleName()).append(" o ");

		if (filterConfig != null)
			filterConfig.setSqlWhere(sb);

		if (!count)
			sb.append(sortConfig.getSqlOrderBy());

		final Query query = em.createQuery(sb.toString());

		if (!count)
		{
			query.setFirstResult(sortConfig.getStart())
				.setMaxResults(sortConfig.getLimit());
		}

		return query;
	}

	@Deprecated
	public <T extends VO> List<T> getVOList(FilterConfig filterConfig, SortConfig sortConfig) {
		return getVOList(getQuery(filterConfig, sortConfig));
	}

	@Deprecated
	public int getVOListCount(FilterConfig filterConfig) {
		return ((Number) getQuery(filterConfig, null).getSingleResult()).intValue();
	}

	@SuppressWarnings(value = "unchecked")
	public <T extends VO> List<T> getVOList2() {
		return getQuery2(false).getResultList();
	}

	public int getVOListCount2() {
		return ((Number) getQuery2(true).getSingleResult()).intValue();
	}

	public <T extends VO> List<T> getVOList() {
		return getVOList(getQuery(false));
	}

	public int getVOListCount() {
		return ((Number) getQuery(true).getSingleResult()).intValue();
	}

	@Deprecated
	@SuppressWarnings(value = "unchecked")
	public <T extends VO, E extends JsonObject> List<T> getVOList(Query query) {
		try
		{
			final List<T> result = new ArrayList<T>();

			final Constructor<T> voConstructor = voClass.getConstructor(poClass);

			for (Object o : query.getResultList())
				result.add(voConstructor.newInstance((E) o));

			return result;
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final StringBuffer sb = new StringBuffer();

	private Class<? extends JsonObject> poClass;
	private Class voClass;
	private VO voInstance;
	private EntityManager em;
	private FilterConfig filterConfig;
	private SortConfig sortConfig;
}