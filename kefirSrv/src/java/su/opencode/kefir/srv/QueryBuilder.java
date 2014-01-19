/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 26.09.2010 12:53:17$
*/
package su.opencode.kefir.srv;

import su.opencode.kefir.util.DateUtils;

import static su.opencode.kefir.util.StringUtils.concat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryBuilder
{
	public void addParam(String name, String value, Relation relation) {
		params.add(new Param(name, value, relation));
	}
	public void addParam(String name, Integer value, Relation relation) {
		params.add(new Param(name, value.toString(), relation));
	}
	public void addEqualParam(String name, String value) {
		params.add(new Param(name, value, Relation.equal));
	}
	public void addEqualStringParam(String name, String value) {
		params.add(new Param(name, getStringParam(value), Relation.equal));
	}
	public void addEqualEnumParam(String name, Enum value) {
		params.add(new Param(name, getStringParam(value.toString()), Relation.equal));
	}
	public void addEqualParam(String name, Integer value) {
		params.add(new Param(name, value.toString(), Relation.equal));
	}
	public void addEqualParam(String name, Long value) {
		params.add(new Param(name, value.toString(), Relation.equal));
	}
	public void addEqualParam(String name, Date value) {
		params.add(new Param(name, getStringParam(DateUtils.getFirebirdSqlDateFormat().format(value)), Relation.equal));
	}
	public void addEqualParam(String name, Boolean value) {
		params.add(new Param(name, value.toString(), Relation.equal));
	}

	public void addNotEqualParam(String name, String value) {
		params.add(new Param(name, value, Relation.not_equal));
	}
	public void addNotEqualParam(String name, Integer value) {
		params.add(new Param(name, value.toString(), Relation.not_equal));
	}
	public void addNotEqualParam(String name, Boolean value) {
		params.add(new Param(name, value.toString(), Relation.not_equal));
	}
	public void addLikeParam(String name, String value) {
		params.add(new Param(name, value, Relation.like));
	}
	public void addLikeParam(String name, String value, boolean addLike) { // todo: варианты c % впереди и сзади, если нужно
		StringBuilder sb = new StringBuilder();

		if (!addLike)
		{
//			addLikeParam(name, value);
			addLikeParam(getUpper(name), getUpper(value)); // case insensitive like
			return;
		}

//		params.add(new Param(name, getSearchParam(value), Relation.like));
		params.add(new Param(getUpper(name), getUpper(getSearchParam(value)), Relation.like)); // case insensitive like
	}

	public void addParam(String clause) {
		if (clause == null || clause.isEmpty())
			return;

		params.add(new Param(null, clause, null)); // специальный параметр, задаваемый целиком, в нем relation выставлен в null
	}

	public boolean isStartFromAnd() {
		return startFromAnd;
	}
	public void setStartFromAnd(boolean startFromAnd) {
		this.startFromAnd = startFromAnd;
	}


	public static String getStringParam(String str) {
		if (str == null || str.isEmpty())
			return null;

		return concat("'", str.replaceAll("'", "''"), "'");
	}
	public static String getUpper(String name) {
		return concat("upper(", name, ")");
	}

	/**
	 * @param search параметр поиска
	 * @return строка, подготовленная для поиска в sql-выражении like
	 */
	public static String getSearchParam(String search) {
		if (search == null || search.isEmpty())
			return null;

		String result = search;

		if (!search.contains("%"))
			result = concat("%", search, "%");

		return concat("'", result.replaceAll("'", "''"), "'");
	}

	/**
	 * @param search параметр поиска
	 * @return строка, подготовленная для поиска в like в EJB-QL
	 */
	public static String getEjbQlSearchParam(String search) {
		if (search == null || search.isEmpty())
			return null;

		String result = search;
		if (!search.contains("%"))
			result = concat("%", search, "%");

		return result;
	}

	public void clear() {
		params.clear();
	}
	public boolean isEmpty() {
		return params.isEmpty();
	}

	public String getQuery() {
		if (params.isEmpty())
			return "";

		StringBuffer sb = new StringBuffer();
		sb.append(startFromAnd ? "and " : "where ");
		params.get(0).append(sb);

		for (int i = 1; i < params.size(); i++)
		{
			sb.append("and ");
			params.get(i).append(sb);
		}

		return sb.toString();
	}

	public class Param {
		public Param(String name, String value) {
			this.name = name;
			this.value = value;
			this.relation = Relation.equal;
		}
		public Param(String name, String value, Relation relation) {
			this.name = name;
			this.value = value;
			this.relation = relation;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		public void append(StringBuffer sb) {
			if (relation == null)
			{ // специальный случай - параметр, который задается целиком, без relation
				sb.append(value).append(" ");
				return;
			}

			sb.append(name);

			// спец случай - равенство null
			if (relation == Relation.equal && value == null)
			{
				sb.append(" is null ");
				return;
			}

			// спец случай - неравенство null
			if (relation == Relation.not_equal && value == null)
			{
				sb.append(" is not null ");
				return;
			}

			switch (relation)
			{
				case equal: sb.append(" = ");	break;
				case not_equal: sb.append(" <> ");	break;
				case more: sb.append(" > "); break;
				case more_equal: sb.append(" >= "); break;
				case less: sb.append(" < "); break;
				case less_equal: sb.append(" <= "); break;
				case like: sb.append(" like ");	break;
			}

			sb.append(value).append(" ");
		}

		private String name;
		private String value;
		private Relation relation;
	}

	private List<Param> params = new ArrayList<Param>();
	private boolean startFromAnd = false;
}