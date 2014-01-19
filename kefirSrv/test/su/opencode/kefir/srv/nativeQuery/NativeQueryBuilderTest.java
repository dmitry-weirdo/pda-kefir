package su.opencode.kefir.srv.nativeQuery;

import org.apache.log4j.Logger;
import org.junit.Test;
import su.opencode.kefir.srv.Relation;
import su.opencode.kefir.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class NativeQueryBuilderTest
{
	@Test
	public void testNativeQuery() {
		NativeQueryBuilder qb = new NativeQueryBuilder();
		qb.setTableName(TABLE_NAME);
		qb.setTableAlias(TABLE_ALIAS);
		qb.setJoins(JOINS);

		List<String> fieldsToSelect = new ArrayList<>();
		fieldsToSelect.add("tt.testField1");
		fieldsToSelect.add("ss.secondField1");
		qb.setFieldsToSelect(fieldsToSelect);

		// fill where params
		List<WhereParam> whereParams = new ArrayList<>();
		whereParams.add( new WhereParam("testField1", "'tf1'", Relation.equal) );
		whereParams.add( new WhereParam("ss", "secondField1", "100", Relation.more) );
		qb.setWhereParams(whereParams);


		// fill order by params
		List<OrderByParam> orderByParams = new ArrayList<>();
		orderByParams.add( new OrderByParam(TABLE_ALIAS, "testField1", OrderByDirection.asc) );
		orderByParams.add( new OrderByParam(JOIN_TABLE_ALIAS, "secondField1", OrderByDirection.desc) );
		qb.setOrderByParams(orderByParams);

		String query = qb.getQuery();
		assertEquals( StringUtils.concat(
				  "select"
				, " tt.testField1, ss.secondField1"
				, " from ", TABLE_NAME, " ", TABLE_ALIAS
				, " ", JOINS
				, " where ( tt.testField1 = 'tf1' ) and ( ss.secondField1 > 100 )"
				, " order by tt.testField1 asc, ss.secondField1 desc"
			), query
		);
	}

	public static final String TABLE_NAME = "TestTable";
	public static final String TABLE_ALIAS = "tt";
	public static final String JOINS = "join SecondTable ss on (tt.second_id = ss.id)";
	public static final String JOIN_TABLE_ALIAS = "ss";

	private static final Logger logger = Logger.getLogger(NativeQueryBuilderTest.class);
}