/**
 Copyright 2011 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.web.report;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import su.opencode.kefir.srv.VO;
import su.opencode.kefir.srv.dynamicGrid.DynamicColumnVO;
import su.opencode.kefir.srv.dynamicGrid.DynamicGrid;
import su.opencode.kefir.srv.dynamicGrid.DynamicGridService;
import su.opencode.kefir.srv.json.ColumnGroup;
import su.opencode.kefir.srv.json.ColumnModel;
import su.opencode.kefir.srv.renderer.RenderersFactory;
import su.opencode.kefir.web.util.ServiceFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.DateUtils.getDayMonthYear;
import static su.opencode.kefir.util.StringUtils.getStringFromUTF8;

public class DynamicGridReportHelper extends ReportServletHelper
{
	public HSSFWorkbook getReport(List<? extends VO> list, String entityName, InputStream renderersInputStream) {
		final HSSFWorkbook template = new HSSFWorkbook();
		final HSSFSheet sheet = template.createSheet("Лист1");

		if (list == null || list.size() == 0)
			return template;

		final DynamicGridService dynamicGridService = ServiceFactory.get(DynamicGridService.class);

		final List<DynamicGrid> grids = dynamicGridService.getDynamicGridList(entityName);
		final VO voClass = list.get(0);
		final HashMap<String, HashMap<String, Object>> columnModelMap = voClass.getColumnModelMap();

		createStyles(template);
		final int rowOffset = fillHeader(sheet, voClass, columnModelMap, grids);
		fillSheet(sheet, list, grids, rowOffset, renderersInputStream);

		return template;
	}
	@SuppressWarnings(value = "unchecked")
	private void fillSheet(HSSFSheet sheet, List<? extends VO> list, List<DynamicGrid> grids, int rowOffset, InputStream in) {
		final RenderersFactory renderersFactory = new RenderersFactory();
		renderersFactory.loadRenderProperties(in);

		int cols = 0;
		for (DynamicGrid grid : grids)
		{
			if (grid.getHidden() == null)
				cols++;
		}

		final HashMap<Integer, Cell> stylesMap = new HashMap<Integer, Cell>();
		final int rows = list.size();
		for (int row = 0; row < rows; row++)
		{
			int col = -1;
			final HSSFRow r = sheet.createRow(rowOffset + row);
			for (DynamicGrid grid : grids)
			{
				if (grid.getHidden() != null && grid.getHidden())
					continue;

				col++;
				String columnName = grid.getColumnName();
				if (columnName.contains("_"))
					columnName = columnName.substring(0, columnName.indexOf("_"));

				final VO vo = list.get(row);
				if (row == 0)
				{
					final Class fieldType = vo.getFieldType(columnName);
					final Cell cellStyle = new Cell(sheet.getWorkbook(), col, cols, fieldType, grid.getWidth());
					stylesMap.put(col, cellStyle);
					sheet.setColumnWidth(col, cellStyle.getWidth());
				}

				final Object value = vo.getFieldValue(columnName);
				if (value instanceof List)
				{
					int cellCol = col;
					final List<DynamicColumnVO> values = (List<DynamicColumnVO>) value;
					for (int i = 0; i < values.size(); i++)
					{
						final DynamicColumnVO columnVO = values.get(i);
						final Cell cellStyle = stylesMap.get(col);

						if (row == 0)
						{
							sheet.setColumnWidth(i + col, cellStyle.getWidth());

							if (i == cols - col - 1)
							{
								final Cell cellStyle1 =
									new Cell(sheet.getWorkbook(), cols - 1, cols, cellStyle.getValueType(), grid.getWidth());

								stylesMap.put(cols, cellStyle1);
							}
						}

						final HSSFCell cell = r.createCell(cellCol++);
						final HSSFCellStyle hssfCellStyle = i == cols - col - 1
							? stylesMap.get(cols).getStyle(row, rows)
							: cellStyle.getStyle(row, rows);

						cell.setCellStyle(hssfCellStyle);

						final Object renderedValue = renderersFactory.getCellRenderedValue(columnVO.getRendered(columnName), columnVO.getValue());
						cell.setCellValue(cellStyle.getValue(renderedValue));
					}
					break;
				}
				else
				{
					final Cell cellStyle = stylesMap.get(col);
					final HSSFCell cell = r.createCell(col);
					cell.setCellStyle(cellStyle.getStyle(row, rows));
					cell.setCellValue(cellStyle.getValue(renderersFactory.getCellRenderedValue(vo.getRendered(columnName), value)));
				}
			}
		}
	}
	private int fillHeader(HSSFSheet sheet, VO voClass, HashMap<String, HashMap<String, Object>> columnModelMap,
												 List<DynamicGrid> grids)
	{
		final Map<String, ColumnGroup> columnGroupMap = voClass.getColumnGroupMap(voClass.getClass());
		final boolean isGroupColumns = columnGroupMap.size() != 0;

		return isGroupColumns
			? fillGroupedHeaders(sheet, columnModelMap, grids, columnGroupMap)
			: fillHeaders(sheet, columnModelMap, grids);
	}
	private int fillHeaders(HSSFSheet sheet, HashMap<String, HashMap<String, Object>> columnModelMap,
													List<DynamicGrid> grids)
	{
		final HSSFRow firstRow = sheet.createRow(0);
		final int gridsCount = grids.size();
		int col = -1;
		for (DynamicGrid grid : grids)
		{
			if (grid.getHidden() != null && grid.getHidden())
				continue;

			col++;
			final HashMap<String, Object> map = columnModelMap.get(grid.getColumnName());
			final HSSFCell cell = firstRow.createCell(col);
			cell.setCellValue(((String) map.get(ColumnModel.HEADER)).replace("<br/>", " ").replace("  ", " "));

			final HSSFCellStyle style = col == 0 ? topLeftStyle : col != gridsCount - 2 ? topMiddleStyle : topRightStyle;
			cell.setCellStyle(style);
		}

		return 1;
	}
	private int fillGroupedHeaders(HSSFSheet sheet, HashMap<String, HashMap<String, Object>> columnModelMap,
																 List<DynamicGrid> grids, Map<String, ColumnGroup> columnGroupMap)
	{
		final HSSFRow firstRow = sheet.createRow(0);
		final HSSFRow secondRow = sheet.createRow(1);
		ColumnGroup priorColumnGroup = null;
		int firstColIndex = 0;
		final int gridsCount = grids.size();
		int col = -1;
		for (DynamicGrid grid : grids)
		{
			if (grid.getHidden() != null && grid.getHidden())
				continue;

			col++;
			final HashMap<String, Object> map = columnModelMap.get(grid.getColumnName());
			final ColumnGroup columnGroup = columnGroupMap.get(grid.getGroupId());
			final HSSFCell firstRowCell = firstRow.createCell(col);
			final HSSFCell secondRowCell = secondRow.createCell(col);

			final HSSFCellStyle headerFirstStyle =
				col == 0 ? headerFirstLeftStyle : col != gridsCount - 2 ? headerFirstMiddleStyle : headerFirstRightStyle;

			final HSSFCellStyle headerLastStyle =
				col == 0 ? headerLastLeftStyle : col != gridsCount - 2 ? headerLastMiddleStyle : headerLastRightStyle;

			firstRowCell.setCellStyle(headerFirstStyle);
			secondRowCell.setCellStyle(headerLastStyle);

			final String headerTitle = ((String) map.get(ColumnModel.HEADER)).replace("<br/>", " ").replace("  ", " ");
			if (columnGroup == null)
			{
				firstRowCell.setCellValue(headerTitle);

				sheet.addMergedRegion(new CellRangeAddress(0, 1, col, col));
				if (priorColumnGroup != null)
				{
					sheet.addMergedRegion(new CellRangeAddress(0, 0, firstColIndex, col - 1));
					firstColIndex = 0;
					priorColumnGroup = null;
				}
			}
			else
			{
				secondRowCell.setCellValue(headerTitle);
				if (firstColIndex == 0)
				{
					firstColIndex = col;
					firstRowCell.setCellValue(columnGroup.header());
				}

				if (priorColumnGroup != null && !columnGroup.equals(priorColumnGroup))
				{
					sheet.addMergedRegion(new CellRangeAddress(0, 0, firstColIndex, col - 1));
					firstColIndex = col;
					firstRowCell.setCellValue(columnGroup.header());
				}

				priorColumnGroup = columnGroup;
			}

//			sheet.autoSizeColumn(col, true);
//			sheet.setColumnWidth(col, grid.getWidth() * 50);
		}

		return 2;
	}
	private String getValue(DynamicGrid grid, List<VO> list) {
		for (VO vo : list)
		{
			if (vo.getFieldValue("columnName").equals(grid.getColumnName()))
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
}