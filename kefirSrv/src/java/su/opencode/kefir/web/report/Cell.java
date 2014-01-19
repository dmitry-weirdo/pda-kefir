/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.web.report;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import su.opencode.kefir.srv.VO;

import java.util.Date;
import java.util.List;

import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.StringUtils.getStringFromUTF8;

public class Cell
{
	public Cell(HSSFWorkbook workbook, int col, int cols, Class valueType, Integer width) {
		this.valueType = valueType;
		this.width = getWidth(valueType, width);
		createStyles(workbook, valueType, col, cols);
	}
	public int getWidth() {
		return width;
	}
	public Class getValueType() {
		return valueType;
	}
	private int getWidth(Class valueType, Integer width) {
		if (valueType.equals(Date.class))
			return (10 * 10 + 5) / 10 * 256;

		return width * 40;
	}
	public HSSFCellStyle getStyle(int row, int rows) {
		return row == 0 ? firstRowStyle : row != rows - 1 ? moddleRowStyle : lastRowStyle;
	}
	public String getValue(Object value) {
		if (value == null)
			return "";

		if (value instanceof String)
			return getStringFromUTF8((String) value);

		if (value instanceof Date)
			return getDayMonthYear((Date) value);

		return value.toString();
	}
	private String getValue(String columnName, List<VO> list) {
		for (VO vo : list)
		{
			if (vo.getFieldValue("columnName").equals(columnName))
			{
				final Object value = vo.getFieldValue("value");

				if (value == null)
					return null;

				if (value instanceof String)
					return getStringFromUTF8(((String) value));

				if (value instanceof Date)
					return getDayMonthYear(((Date) value));

				return value.toString();
			}
		}

		return null;
	}
	private void createStyles(HSSFWorkbook workbook, Class valueType, int col, int cols) {
		final short leftBorder = col == 0 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
		final short rightBorder = col == cols - 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
		firstRowStyle = getNewStyle(workbook, getAlign(valueType), leftBorder, CellStyle.BORDER_MEDIUM, rightBorder, CellStyle.BORDER_THIN);
		moddleRowStyle = getNewStyle(workbook, getAlign(valueType), leftBorder, CellStyle.BORDER_THIN, rightBorder, CellStyle.BORDER_THIN);
		lastRowStyle = getNewStyle(workbook, getAlign(valueType), leftBorder, CellStyle.BORDER_THIN, rightBorder, CellStyle.BORDER_MEDIUM);
	}
	private short getAlign(Class valueType) {
		if (valueType.equals(Date.class))
			return CellStyle.ALIGN_CENTER;

		if (valueType.equals(Number.class))
			return CellStyle.ALIGN_RIGHT;

		return CellStyle.ALIGN_LEFT;
	}
	private HSSFCellStyle getNewStyle(HSSFWorkbook workbook, short alignHorisontal,
																		short borderLeft, short borderTop, short borderRight, short borderBottom)
	{
		final HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(alignHorisontal);
		style.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
		style.setBorderLeft(borderLeft);
		style.setBorderTop(borderTop);
		style.setBorderRight(borderRight);
		style.setBorderBottom(borderBottom);

		return style;
	}

	private int width;
	private Class valueType;
	private HSSFCellStyle firstRowStyle;
	private HSSFCellStyle moddleRowStyle;
	private HSSFCellStyle lastRowStyle;
}
