/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package test;

import su.opencode.kefir.srv.ExtEntityFilterConfig;
import su.opencode.kefir.srv.QueryBuilder;

import static su.opencode.kefir.util.StringUtils.concat;

public class TestEntityFilterConfig extends ExtEntityFilterConfig
{
	public void addWhereParams(QueryBuilder qb, String entityPrefix) {
		// str field like
		if (strField != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".strField"), strField, true );

		// choose field field (name)
		if (chooseFieldName != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".chooseField.name"), chooseFieldName, true );

		if (enumField != null)
			qb.addEqualStringParam( concat(sb, entityPrefix, ".enumField"), enumField.toString() );
	}

	public String getStrField() {
		return strField;
	}
	public void setStrField(String strField) {
		this.strField = strField;
	}
	public String getChooseFieldName() {
		return chooseFieldName;
	}
	public void setChooseFieldName(String chooseFieldName) {
		this.chooseFieldName = chooseFieldName;
	}
	public TestEnum getEnumField() {
		return enumField;
	}
	public void setEnumField(TestEnum enumField) {
		this.enumField = enumField;
	}

	private String strField;
	private String chooseFieldName;
	private TestEnum enumField;

	private StringBuffer sb = new StringBuffer();
}