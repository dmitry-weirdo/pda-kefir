/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.css;

import su.opencode.kefir.gen.appender.js.JsAppender;
import su.opencode.kefir.gen.fileWriter.css.CssStyle;
import su.opencode.kefir.srv.json.LegendField;
import su.opencode.kefir.srv.json.ViewConfig;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getLegendClassName;
import static su.opencode.kefir.gen.ExtEntityUtils.getRowClassName;
import static su.opencode.kefir.gen.fileWriter.css.CssStyle.*;
import static su.opencode.kefir.gen.project.xml.jsp.JspFileWriter.TD_ELEMENT_NAME;
import static su.opencode.kefir.srv.json.ViewConfig.SELECTED_ROW_CLASS;
import static su.opencode.kefir.util.StringUtils.concat;

public class ViewConfigStylesAppender extends JsAppender
{
	public ViewConfigStylesAppender(String filePath, ViewConfig viewConfig, Class entityClass) {
		this.filePath = filePath;
		this.viewConfig = viewConfig;
		this.entityClass = entityClass;
	}

	public void appendStyles() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_STYLE_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<>();

				appendedFileLines.addAll(fileLines.subList(0, i));
				appendMainCssLines(fileLines);
				appendedFileLines.addAll(fileLines.subList(i, fileLines.size()));

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendMainCssLines(List<String> fileLines) throws IOException {
		for (LegendField legendField : viewConfig.legendFields())
		{
			if (atLeastOneStyleNotPresent(fileLines, legendField))
				appendEmptyLine(fileLines);

			writeLabelStyle(fileLines, legendField);
			writeUnselectedRowStyle(fileLines, legendField);
			writeSelectedRowStyle(fileLines, legendField);
		}
	}
	private void writeLabelStyle(List<String> fileLines, LegendField legendField) {
		String styleName = getLabelStyleName(legendField);
		if (styleIsPresent(fileLines, styleName))
			return;

		CssStyle cssStyle = new CssStyle(styleName);
		cssStyle.putHexColor(BACKGROUND_COLOR_PROPERTY_NAME, legendField.backgroundColor());
		cssStyle.putHexColor(COLOR_PROPERTY_NAME, concat(sb, legendField.color()));
		fileLines.add(cssStyle.toString());
	}
	private void writeUnselectedRowStyle(List<String> fileLines, LegendField legendField) {
		String styleName = getUnselectedRowStyleName(legendField);
		if (styleIsPresent(fileLines, styleName))
			return;

		CssStyle cssStyle = new CssStyle(styleName);
		cssStyle.putHexColor(BACKGROUND_COLOR_PROPERTY_NAME, concat(sb, legendField.backgroundColor()));
		cssStyle.putHexColor(COLOR_PROPERTY_NAME, concat(sb, legendField.color()));
		fileLines.add(cssStyle.toString());
	}
	private void writeSelectedRowStyle(List<String> fileLines, LegendField legendField) {
		String styleName = getSelectedRowStyleName(legendField);
		if (styleIsPresent(fileLines, styleName))
			return;

		CssStyle cssStyle = new CssStyle(styleName);
		cssStyle.putHexColor(BACKGROUND_COLOR_PROPERTY_NAME, concat(sb, legendField.selectedBackgroundColor(), " ", IMPORTANT));
		cssStyle.putHexColor(COLOR_PROPERTY_NAME, concat(sb, legendField.selectedColor(), " ", IMPORTANT));
		fileLines.add(cssStyle.toString());
	}

	private String getLabelStyleName(LegendField legendField) {
		return getCssClassName(getLegendClassName(legendField));
	}
	private String getUnselectedRowStyleName(LegendField legendField) {
		return concat(sb, getCssClassName(getRowClassName(legendField)), " ", TD_ELEMENT_NAME);
	}
	private String getSelectedRowStyleName(LegendField legendField) {
		return concat(sb, getCssClassName(SELECTED_ROW_CLASS), getCssClassName(getRowClassName(legendField)), " ", TD_ELEMENT_NAME);
	}

	private boolean styleIsPresent(List<String> fileLines, String styleName) {
		String styleNameWithOpenBracket = concat(sb, styleName, " ", CssStyle.STYLE_OPEN_BRACKET);

		for (String fileLine : fileLines)
		{
			if (fileLine.startsWith(styleNameWithOpenBracket))
			{
				logger.info(concat(sb, "Css style \"", styleName, "\" is already present in file \"", filePath, "\"."));
				return true;
			}
		}

		return false;
	}
	private boolean atLeastOneStyleNotPresent(List<String> fileLines, LegendField legendField) {
		return !styleIsPresent(fileLines, getLabelStyleName(legendField))
			|| !styleIsPresent(fileLines, getUnselectedRowStyleName(legendField))
			|| !styleIsPresent(fileLines, getSelectedRowStyleName(legendField));
	}

	private ViewConfig viewConfig;
	private Class entityClass;

	public static final String APPEND_STYLE_MARKER = "${APPEND_STYLE}";
}