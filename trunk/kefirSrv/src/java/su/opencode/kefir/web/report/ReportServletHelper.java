package su.opencode.kefir.web.report;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
* Copyright 2010 LLC "Open Code"
* http://www.o-code.ru, http://www.opencode.su
* $HeadURL$
* $Author$ maryin
* $Revision$
* $Date::               $
*/
public class ReportServletHelper
{
	protected static void createStyles(HSSFWorkbook template) {
		headerFirstLeftStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		headerFirstMiddleStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		headerFirstRightStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);
		headerLastLeftStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		headerLastMiddleStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		headerLastRightStyle = createCellStyle(template, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.ALIGN_CENTER_SELECTION, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		topLeftStyle = createCellStyle(template, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		topMiddleStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		topRightStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);
		centerLeftStyle = createCellStyle(template, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		centerMiddleStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		centerRightStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);
		bottomLeftStyle = createCellStyle(template, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		bottomMiddleStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		bottomRightStyle = createCellStyle(template, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
	}
	protected static void setCellStyle(HSSFCell cell, int row, int rows, int col, int cols) {
		if (row == 0)
		{ // first row
			if (col == 0)
				cell.setCellStyle(topLeftStyle);
			else
			{
				if (col != cols - 1)
					cell.setCellStyle(topMiddleStyle);
				else
					cell.setCellStyle(topRightStyle);
			}
		}
		else if (row != rows - 1)
		{ // middle row
			if (col == 0)
				cell.setCellStyle(centerLeftStyle);
			else
			{
				if (col != cols - 1)
					cell.setCellStyle(centerMiddleStyle);
				else
					cell.setCellStyle(centerRightStyle);
			}
		}
		else
		{ // last row
			if (col == 0)
				cell.setCellStyle(bottomLeftStyle);
			else
			{
				if (col != cols - 1)
					cell.setCellStyle(bottomMiddleStyle);
				else
					cell.setCellStyle(bottomRightStyle);
			}
		}
	}

	public static byte[] getBookAsBytes(HSSFWorkbook book) {
		try
		{
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			book.write(out);
			out.flush();
			out.close();

			return out.toByteArray();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	private static HSSFCellStyle createCellStyle(HSSFWorkbook book, short alignHorisontal, short alignVertical,
																							 short borderLeft, short borderTop, short borderRight, short borderBottom)
	{
		final HSSFCellStyle style = book.createCellStyle();
		style.setAlignment(alignHorisontal);
		style.setVerticalAlignment(alignVertical);
		style.setBorderLeft(borderLeft);
		style.setBorderTop(borderTop);
		style.setBorderRight(borderRight);
		style.setBorderBottom(borderBottom);

		return style;
	}
//	private static HSSFRichTextString getText(String value) {
//		return new HSSFRichTextString(value);
//	}
	private static HSSFCellStyle createCellStyle(HSSFWorkbook book, short borderLeft, short borderTop, short borderRight,
																							 short borderBottom)
	{
		return createCellStyle(book, CellStyle.ALIGN_GENERAL, CellStyle.VERTICAL_BOTTOM, borderLeft, borderTop, borderRight, borderBottom);
	}

	protected static HSSFCellStyle headerFirstLeftStyle;
	protected static HSSFCellStyle headerFirstMiddleStyle;
	protected static HSSFCellStyle headerFirstRightStyle;
	protected static HSSFCellStyle headerLastLeftStyle;
	protected static HSSFCellStyle headerLastMiddleStyle;
	protected static HSSFCellStyle headerLastRightStyle;
	protected static HSSFCellStyle topLeftStyle;
	protected static HSSFCellStyle topMiddleStyle;
	protected static HSSFCellStyle topRightStyle;
	protected static HSSFCellStyle centerLeftStyle;
	protected static HSSFCellStyle centerMiddleStyle;
	protected static HSSFCellStyle centerRightStyle;
	protected static HSSFCellStyle bottomLeftStyle;
	protected static HSSFCellStyle bottomMiddleStyle;
	protected static HSSFCellStyle bottomRightStyle;
}