package su.opencode.kefir.srv.nativeQuery;

import su.opencode.kefir.srv.Relation;
import su.opencode.kefir.util.ObjectUtils;
import su.opencode.kefir.util.SqlUtils;
import su.opencode.kefir.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class NativeQueryBuilder
{
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public List<String> getFieldsToSelect() {
		return fieldsToSelect;
	}
	public void setFieldsToSelect(List<String> fieldsToSelect) {
		this.fieldsToSelect = fieldsToSelect;
	}
	public String getJoins() {
		return joins;
	}
	public void setJoins(String joins) {
		this.joins = joins;
	}
	public List<WhereParam> getWhereParams() {
		return whereParams;
	}
	public void setWhereParams(List<WhereParam> whereParams) {
		this.whereParams = whereParams;
	}
	public List<OrderByParam> getOrderByParams() {
		return orderByParams;
	}
	public void setOrderByParams(List<OrderByParam> orderByParams) {
		this.orderByParams = orderByParams;
	}

	public String getQuery() {
		// todo: check builder state correctness

		String fieldsToSelect = StringUtils.getSeparatedString(SqlUtils.COMMA_OPERATOR_WITH_SPACE, this.fieldsToSelect);

		StringBuffer sb = new StringBuffer();
		sb.append(SqlUtils.SELECT_OPERATOR_WITH_SPACE);
		sb.append(fieldsToSelect);
		sb.append(SqlUtils.FROM_OPERATOR_WITH_SPACES).append(tableName);

		if ( StringUtils.notEmpty(tableAlias) )
			sb.append(SqlUtils.SPACE).append(tableAlias);

		if ( StringUtils.notEmpty(joins) )
			sb.append(SqlUtils.SPACE).append(joins);

		if ( whereClauseIsPresent() )
		{
			appendWhereClause(sb);
		}

		if ( orderByClauseIsPresent() )
		{
			appendOrderByClause(sb);
		}

		return sb.toString();
	}

	private boolean whereClauseIsPresent() {
		return ObjectUtils.notEmpty(whereParams);
	}
	private void appendWhereClause(StringBuffer sb) {
		sb.append(SqlUtils.WHERE_OPERATOR_WITH_SPACES);
		// todo: implement method

		StringBuilder sb2 = new StringBuilder();

		List<String> clauses = new ArrayList<>();
		for (WhereParam param : whereParams)
		{
			if ( StringUtils.empty(param.getField()) )
				throw new IllegalStateException("WhereParam has no field name");

			sb2.delete(0, sb.length());
			sb2.append(SqlUtils.OPENING_BRACKET_WITH_SPACE);

			// add field name
			if ( StringUtils.empty(param.getTableAlias()) )
			{ // defaults to this table alias
				if ( StringUtils.notEmpty(this.tableAlias) )
				{
					sb2.append(this.tableAlias); // defaults to this table alias
					sb2.append(SqlUtils.TABLE_FIELD_DELIMITER);
				}

				sb2.append( param.getField() );
			}
			else
			{ // use table alias from OrderByParam
				sb2.append( param.getTableAlias() );
				sb2.append(SqlUtils.TABLE_FIELD_DELIMITER);
				sb2.append( param.getField() );
			}

			// space between field name and relation
			sb2.append(SqlUtils.SPACE);

			switch (param.getRelation())
			{
				case equal: sb2.append(SqlUtils.EQUALS_OPERATOR); break;
				case not_equal: sb2.append(SqlUtils.NOT_EQUALS_OPERATOR); break;
				case more: sb2.append(SqlUtils.MORE_OPERATOR); break;
				case more_equal: sb2.append(SqlUtils.MORE_OR_EQUALS_OPERATOR); break;
				case less: sb2.append(SqlUtils.LESS_OPERATOR); break;
				case less_equal: sb2.append(SqlUtils.LESS_OR_EQUALS_OPERATOR); break;
				case like: sb2.append(SqlUtils.LIKE_OPERATOR); break;
				case like_case_insenstive: sb2.append(SqlUtils.LIKE_OPERATOR); break;
				case is_null: sb2.append(SqlUtils.IS_NULL); break;
				case is_not_null: sb2.append(SqlUtils.IS_NOT_NULL); break;
			}

			// space between relation and value
			sb2.append(SqlUtils.SPACE);

			// todo: add value
			if ( param.getRelation() == Relation.is_null )
			{ // special case - is null

			}
			else if ( param.getRelation() == Relation.is_not_null )
			{ // special case - is not null

			}
			else
			{
				sb2.append( param.getValue() );
				// todo: escaping in case of like relation
				// todo: special collation for like case insensitive
			}

			sb2.append(SqlUtils.CLOSING_BRACKET_WITH_SPACE);

			clauses.add( sb2.toString() );
		}

		sb.append( StringUtils.getSeparatedString(SqlUtils.AND_OPERATOR_WITH_SPACES, clauses) );
	}

	private boolean orderByClauseIsPresent() {
		return ObjectUtils.notEmpty(orderByParams);
	}
	private void appendOrderByClause(StringBuffer sb) {
		sb.append(SqlUtils.ORDER_BY_OPERATOR_WITH_SPACES);

		StringBuilder sb2 = new StringBuilder();

		List<String> clauses = new ArrayList<>();
		for (OrderByParam param : orderByParams)
		{
			if ( StringUtils.empty(param.getField()) )
				throw new IllegalStateException("OrderByParam has no field name");

			sb2.delete(0, sb2.length());

			// add field name
			if ( StringUtils.empty(param.getTableAlias()) )
			{ // defaults to this table alias
				if ( StringUtils.notEmpty(this.tableAlias) )
				{
					sb2.append(this.tableAlias); // defaults to this table alias
					sb2.append(SqlUtils.TABLE_FIELD_DELIMITER);
				}

				sb2.append( param.getField() );
			}
			else
			{ // use table alias from OrderByParam
				sb2.append( param.getTableAlias() );
				sb2.append(SqlUtils.TABLE_FIELD_DELIMITER);
				sb2.append( param.getField() );
			}

			// space between order by field name and sort directory
			sb2.append(SqlUtils.SPACE);

			// add sort directory
			if ( param.getDirection() == null )
			{ // defaults to 'asc' directory
				sb2.append(SqlUtils.ASC_OPERATOR);
			}
			else
			{ // use sort directory from OrderByParam
				switch (param.getDirection())
				{
					case asc: sb2.append(SqlUtils.ASC_OPERATOR); break;
					case desc: sb2.append(SqlUtils.DESC_OPERATOR); break;
					default: throw new IllegalStateException( StringUtils.concat(sb2, "Unknown OrderByParam direction: \"", param.getDirection(), "\"") );
				}
			}

			clauses.add( sb2.toString() );
		}

		sb.append( StringUtils.getSeparatedString(SqlUtils.COMMA_OPERATOR_WITH_SPACE, clauses) );
	}

	private String tableName;
	private String tableAlias;
	private String joins;
	private List<String> fieldsToSelect;
	private List<WhereParam> whereParams;
	private List<OrderByParam> orderByParams;
}