/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.srv;

import org.apache.log4j.Logger;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.srv.VO.getVOList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public abstract class EntityQueryBuilder
{
	/**
	 * @return класс сущности, используемый дл€ построени€ списка VO.
	 */
	public abstract Class getEntityClass();

	/**
	 * @return им€ сущности дл€ "select o from EntityName" в запросах.
	 */
	public abstract String getSqlEntityName();

	/**
	 * @return join предикат, по умолчанию пустой. ¬ставл€етс€ в запросы list и count
	 * поcле "select o from EntityName o "
	 */
	public String getJoin(String entityPrefix) {
		return "";
	}

	/**
	 * @return join предикат с добавленным пробелом в конце в случае необходимости.
	 */
	private String getJoinStatement() {
		String join = getJoin(DEFAULT_ENTITY_PREFIX);
		if (join == null || join.isEmpty())
			return "";

		return concat(join, " " );
	}

	@SuppressWarnings(value = "unchecked")
	public <T extends VO> List<T> getList(ExtEntityFilterConfig filterConfig, SortConfig sortConfig, EntityManager em, Class<T> voClass) {
		String queryStr = getSelectQuery(filterConfig, sortConfig);
		logger.info( concat(sb, "get", getEntityClass().getSimpleName(), "s query: ", queryStr) ); // todo: make method name configurable

		Query query = em.createQuery(queryStr)
			.setFirstResult(sortConfig.getStart())
			.setMaxResults(sortConfig.getLimit());

		return VO.getVOList(query, getEntityClass(), voClass);
	}

	public String getSelectQuery(ExtEntityFilterConfig filterConfig, SortConfig sortConfig) {
		sb.delete(0, sb.length());
		sb.append("select ").append(DEFAULT_ENTITY_PREFIX).append(" from ").append(getSqlEntityName()).append(" ").append(DEFAULT_ENTITY_PREFIX).append(" ").append(getJoinStatement());

		QueryBuilder qb = new QueryBuilder();
		filterConfig.addWhereParams(qb);

		sb.append(qb.getQuery());
		sb.append(sortConfig.getSqlOrderBy());

		return sb.toString();
	}

	public int getCount(ExtEntityFilterConfig filterConfig, EntityManager em) {
		String queryStr = getCountQuery(filterConfig);
		logger.info( concat(sb, "get", getEntityClass().getSimpleName(), "sCount query: ", queryStr) ); // todo: make method name configurable

		Query query = em.createQuery(queryStr);
		return ((Number) query.getSingleResult()).intValue();
	}

	public String getCountQuery(ExtEntityFilterConfig filterConfig) {
		sb.delete(0, sb.length());
		sb.append("select count(").append(DEFAULT_ENTITY_PREFIX).append(") from ").append(getSqlEntityName()).append(" ").append(DEFAULT_ENTITY_PREFIX).append(" ").append(getJoinStatement());

		QueryBuilder qb = new QueryBuilder();
		filterConfig.addWhereParams(qb);

		sb.append(qb.getQuery());

		return sb.toString();
	}

	private StringBuffer sb = new StringBuffer();
	public static final String DEFAULT_ENTITY_PREFIX = "o";

	private static final Logger logger = Logger.getLogger(EntityQueryBuilder.class);
}