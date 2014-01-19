/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 16.02.2011 10:54:37$
*/
package su.opencode.kefir.srv.dynamicGrid;

import org.json.JSONObject;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.SortDirection;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DynamicGridService
{
	JSONObject getDynamicGridParams(String entityName, List<DynamicColumnVO> dynamicColumns);
	void setColumnWidth(String entityName, String columnName, Integer newWidth);
	void setColumnHidden(String entityName, String columnName, boolean newHidden);
	void changeColumnsOrder(String entityName, String columnName, Integer oldIndex, Integer newIndex);
	void setSort(String entityName, String columnName, Integer sortOrder, SortDirection sortDir, boolean isClear);
	void setDefaultSort(String entityName, String[] columns, String[] directions);
	String[][] getReport(List<? extends VO> list, String entityName);
	@SuppressWarnings(value = "unchecked")
	List<DynamicGrid> getDynamicGridList(String entityName);
}