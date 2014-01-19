/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 20.12.2010 13:16:44$
*/
package su.opencode.kefir.srv.dynamicGrid;

import static su.opencode.kefir.util.StringUtils.getConcatenation;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.json.JsonReader;

import java.util.HashMap;

public class DynamicColumnVO extends VO
{

	public DynamicColumnVO() {
	}
	public DynamicColumnVO(String columnName, String columnHeader, ColumnModel columnModel, Object value) {
		this.columnName = columnName;
		this.columnHeader = columnHeader;
		this.columnModel = columnModel;
		this.value = value;
	}
	public DynamicColumnVO(String columnName, Object value) {
		this.columnName = columnName;
		this.value = value;
	}
	protected void addToColumnModelMap(HashMap<String, HashMap<String, Object>> map, int order, String dataIndex,
																		 String header, ColumnModel columnModel)
	{
		final String aHeader = getHeader(header);
		final HashMap<String, Object> columnModelJson = getColumnModelMap(dataIndex, columnModel, aHeader, order);

		map.put(dataIndex, columnModelJson);
	}
	private String getHeader(String header) {
		if (header.length() <= 15)
			return header;

		final String[] arrays = header.split(" ");
		switch (arrays.length) //todo: сделать универсальный метод!
		{
			case 1:
				return header;

			case 2:
				return getConcatenation(arrays[0], "<br/>", arrays[1]);

			case 3:
				if (arrays[0].length() >= arrays[1].length() + arrays[2].length())
					return getConcatenation(arrays[0], "<br/>", arrays[1], " ", arrays[2]);

				return getConcatenation(arrays[0], " ", arrays[1], "<br/>", arrays[2]);

			case 4:
				if (arrays[0].length() >= arrays[1].length() + arrays[2].length() + arrays[3].length())
					return getConcatenation(arrays[0], "<br/>", arrays[1], " ", arrays[2], " ", arrays[3]);

				if (arrays[0].length() + arrays[1].length() >= arrays[2].length() + arrays[3].length())
					return getConcatenation(arrays[0], " ", arrays[1], "<br/>", arrays[2], " ", arrays[3]);

				return getConcatenation(arrays[0], " ", arrays[1], " ", arrays[2], "<br/>", arrays[3]);

			case 5:
				if (arrays[0].length() >= arrays[1].length() + arrays[2].length() + arrays[3].length() + arrays[4].length())
					return getConcatenation(arrays[0], "<br/>", arrays[1], " ", arrays[2], " ", arrays[3], " ", arrays[4]);

				if (arrays[0].length() + arrays[1].length() >= arrays[2].length() + arrays[3].length() + arrays[4].length())
					return getConcatenation(arrays[0], " ", arrays[1], "<br/>", arrays[2], " ", arrays[3], " ", arrays[4]);

				if (arrays[0].length() + arrays[1].length() + arrays[2].length() >= arrays[3].length() + arrays[4].length())
					return getConcatenation(arrays[0], " ", arrays[1], " ", arrays[2], "<br/>", arrays[3], " ", arrays[4]);

				return getConcatenation(arrays[0], " ", arrays[1], " ", arrays[2], " ", arrays[3], "<br/>", arrays[4]);

			default:
//				final StringBuffer sb = new StringBuffer();
//				int middle = arrays.length / 2;
				//todo: (arrays[0] + arrays[1]) - (arrays[2] + arrays[3] + arrays[4]) >= 5 символов
				return header;
		}
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getColumnHeader() {
		return columnHeader;
	}
	public Object getValue() {
		return value;
	}
	public void setColumnModel(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}
	public void addToColumnModelMap(HashMap<String, HashMap<String, Object>> map, int order) {
		addToColumnModelMap(map, order, columnName, columnHeader, columnModel);
	}
	public void addToJsonReaderMap(HashMap<String, JsonReader> map) {
		map.put(columnName, new JsonReader(columnName, getType(value.getClass().getName()), null));
	}

	protected String columnName;
	protected String columnHeader;
	protected Object value;

	protected static final String COLUMN_NAME = "columnName";

	private ColumnModel columnModel;
}