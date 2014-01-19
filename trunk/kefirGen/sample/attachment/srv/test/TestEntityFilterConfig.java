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
		if (strField != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".strField"), strField, true );

		if (chooseFieldName != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".chooseField.name"), chooseFieldName, true );

		if (intField != null)
			qb.addEqualParam( concat(sb, entityPrefix, ".intField"), intField );

		if (enumField != null)
			qb.addEqualEnumParam( concat(sb, entityPrefix, ".enumField"), enumField );

		if (chooseFieldId != null)
			qb.addEqualParam( concat(sb, entityPrefix, ".chooseField.id"), chooseFieldId );

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
	public Integer getIntField() {
		return intField;
	}
	public void setIntField(Integer intField) {
		this.intField = intField;
	}
	public TestEnum getEnumField() {
		return enumField;
	}
	public void setEnumField(TestEnum enumField) {
		this.enumField = enumField;
	}
	public Integer getChooseFieldId() {
		return chooseFieldId;
	}
	public void setChooseFieldId(Integer chooseFieldId) {
		this.chooseFieldId = chooseFieldId;
	}

	private String strField;
	private String chooseFieldName;
	private Integer intField;
	private TestEnum enumField;
	private Integer chooseFieldId;

	private StringBuffer sb = new StringBuffer();
}