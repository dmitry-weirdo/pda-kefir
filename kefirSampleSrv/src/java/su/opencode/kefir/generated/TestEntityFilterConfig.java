/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.generated;

import su.opencode.kefir.srv.ExtEntityFilterConfig;
import su.opencode.kefir.srv.QueryBuilder;
import static su.opencode.kefir.util.StringUtils.concat;

public class TestEntityFilterConfig extends ExtEntityFilterConfig
{
	public void addWhereParams(QueryBuilder qb, String entityPrefix) {
		if (strField != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".strField"), strField, true );

		if (chooseEntityName != null)
			qb.addLikeParam( concat(sb, entityPrefix, ".chooseEntity.name"), chooseEntityName, true );

		if (comboBoxEntityId != null)
			qb.addEqualParam( concat(sb, entityPrefix, ".comboBoxEntity.id"), comboBoxEntityId );

		if (chooseEntityId != null)
			qb.addEqualParam( concat(sb, entityPrefix, ".chooseEntity.id"), chooseEntityId );

	}

	public String getStrField() {
		return strField;
	}
	public void setStrField(String strField) {
		this.strField = strField;
	}
	public String getChooseEntityName() {
		return chooseEntityName;
	}
	public void setChooseEntityName(String chooseEntityName) {
		this.chooseEntityName = chooseEntityName;
	}
	public Integer getComboBoxEntityId() {
		return comboBoxEntityId;
	}
	public void setComboBoxEntityId(Integer comboBoxEntityId) {
		this.comboBoxEntityId = comboBoxEntityId;
	}
	public Integer getChooseEntityId() {
		return chooseEntityId;
	}
	public void setChooseEntityId(Integer chooseEntityId) {
		this.chooseEntityId = chooseEntityId;
	}

	private String strField;
	private String chooseEntityName;
	private Integer comboBoxEntityId;
	private Integer chooseEntityId;

	private StringBuffer sb = new StringBuffer();
}