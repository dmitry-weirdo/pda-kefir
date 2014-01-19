/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 07.09.2010 12:01:35$
*/
package su.opencode.kefir.srv.dynamicGrid;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * <br/>
 * <br/>
 * <br/>
 */
@Entity
public class DynamicGridPK implements Serializable
{
	public DynamicGridPK() {
	}
	public DynamicGridPK(String login, String entityName, String columnName) {
		this.login = login;
		this.entityName = entityName;
		this.columnName = columnName;
	}
	@Id
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@Id
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	@Id
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DynamicGridPK)) return false;

		final DynamicGridPK that = (DynamicGridPK) o;

		return columnName.equals(that.columnName) && entityName.equals(that.entityName) && login.equals(that.login);

	}
	public int hashCode() {
		int result;
		result = login.hashCode();
		result = 31 * result + entityName.hashCode();
		result = 31 * result + columnName.hashCode();

		return result;
	}

	private String login;
	private String entityName;
	private String columnName;
}