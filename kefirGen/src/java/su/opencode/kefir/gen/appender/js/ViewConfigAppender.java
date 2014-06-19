/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.appender.js;

import su.opencode.kefir.gen.fileWriter.js.JsHash;
import su.opencode.kefir.srv.json.LegendField;
import su.opencode.kefir.srv.json.ViewConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.*;
import static su.opencode.kefir.gen.field.ExtEntityFieldsUtils.getViewConfigAnnotation;
import static su.opencode.kefir.gen.fileWriter.js.JsFileWriter.*;
import static su.opencode.kefir.util.StringUtils.concat;

public class ViewConfigAppender extends JsAppender
{
	public ViewConfigAppender(String filePath, ViewConfig viewConfig, Class entityClass) {
		this.filePath = filePath;
		this.viewConfig = viewConfig;
		this.entityClass = entityClass;
	}

	public void appendViewConfig() throws IOException {
		File file = new File(filePath);
		List<String> fileLines = readLinesFromFile(file);

		appendLines(fileLines);
		writeLinesToFile(file, fileLines);
	}
	private void appendLines(List<String> fileLines) throws IOException {
		String appendMarker = APPEND_VIEW_CONFIG_MARKER;

		for (int i = 0; i < fileLines.size(); i++)
		{
			String fileString = fileLines.get(i);
			if (fileString.contains(appendMarker))
			{
				List<String> appendedFileLines = new LinkedList<>();

				appendedFileLines.addAll( fileLines.subList(0, i) );
				appendViewConfig(fileLines);
				appendedFileLines.addAll( fileLines.subList(i, fileLines.size()) );

				fileLines.clear();
				fileLines.addAll(appendedFileLines);

				return;
			}
		}
	}
	private void appendViewConfig(List<String> allFileLines) throws IOException {
		for (String fileLine : allFileLines)
		{ // если этот файл уже включен -> ничего не делать
			if (fileLine.contains(viewConfig.viewConfig()))
			{
				logger.info( concat(sb, "View config \"", viewConfig, "\" is already included in file \"", filePath, "\"") );
				return;
			}
		}

		appendViewConfigLines(allFileLines);
	}
	private void appendViewConfigLines(List<String> addFileLines) throws IOException {
		ViewConfig viewConfig = getViewConfigAnnotation(entityClass);
		List<String> viewConfigLabelLines = new ArrayList<>();

		viewConfigLabelLines.add(concat(sb, viewConfig.viewConfig(), " = {"));
		writeChangeLabelLines(viewConfig, viewConfigLabelLines);

		writeClearLabelLines(viewConfig, viewConfigLabelLines);

		writeGetLabelLines(viewConfig, viewConfigLabelLines);

		writeGetRowClassLines(viewConfigLabelLines);

		viewConfigLabelLines.add("};");
		addFileLines.addAll(viewConfigLabelLines);
	}
	private void writeChangeLabelLines(ViewConfig viewConfig, List<String> viewConfigLabelLines) {
		String entityParamName = getSimpleName(entityClass);
		viewConfigLabelLines.add(concat(sb, "\t", CHANGE_LABEL, ": function(", GRID_ID, ", ", entityParamName, ") {"));
		viewConfigLabelLines.add(concat(sb, "\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", GRID_ID, " + 'label').setText('", viewConfig.legendLabel(), "(' + ", GET_VALUE_FUNCTION_NAME, "(", entityParamName, ", '", viewConfig.legendLabelVOField(), "') + '):');"));

		for (LegendField legendField : getViewConfigAnnotation(entityClass).legendFields())
		{
			if (legendField.checkTrue())
				viewConfigLabelLines.add(concat(sb, "\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", GRID_ID, " + '", legendField.fieldName(), "').setVisible(", GET_VALUE_FUNCTION_NAME, "(", entityParamName, ", '", legendField.fieldName(), "'));"));
			else
				viewConfigLabelLines.add(concat(sb, "\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", GRID_ID, " + '", legendField.fieldName(), "').setVisible(", GET_VALUE_FUNCTION_NAME,"(", entityParamName, ", '", legendField.fieldName(), "') === ", false, ");"));
		}
		viewConfigLabelLines.add("\t},");
	}
	private void writeClearLabelLines(ViewConfig viewConfig, List<String> viewConfigLabelLines) {
		viewConfigLabelLines.add(concat(sb, "\t", CLEAR_LABEL, ": function(", GRID_ID, ") {"));
		viewConfigLabelLines.add(concat(sb, "\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", GRID_ID, " + 'label').setText('", viewConfig.legendLabel(), ":');"));

		for (LegendField legendField : getViewConfigAnnotation(entityClass).legendFields())
			viewConfigLabelLines.add(concat(sb, "\t\t", EXT_GET_CMP_FUNCTION_NAME, "(", GRID_ID, " + '", legendField.fieldName(), "').setVisible(", false, ");"));

		viewConfigLabelLines.add("\t},");
	}
	private void writeGetLabelLines(ViewConfig viewConfig, List<String> viewConfigLabelLines) {
		viewConfigLabelLines.add(concat(sb, "\t", GET_LABEL, ": function(", GRID_ID, ") {"));
		viewConfigLabelLines.add("\t\treturn [");

		JsHash config = new JsHash();
		config.put("id", concat(sb, GRID_ID, " + 'label'"));
		config.putString("cls", "bbar-label");
		config.putString("text", viewConfig.legendLabel());
		viewConfigLabelLines.add(concat(sb, "\t\t\t\t", "new ", EXT_LABEL_CLASS_NAME, "(", config, ")"));

		for (LegendField legendField : getViewConfigAnnotation(entityClass).legendFields())
		{
			config.clear();
			config.put("id", concat(sb, GRID_ID, " + '", legendField.fieldName(), "'"));
			config.putString("cls", concat(sb, "bbar-label ", getLegendClassName(legendField)));
			config.putString("text", legendField.legendLabel());
			viewConfigLabelLines.add(concat(sb, "\t\t\t,\t", "new ", EXT_LABEL_CLASS_NAME, "(", config, ")"));
		}

		viewConfigLabelLines.add("\t\t];");
		viewConfigLabelLines.add("\t},");
	}
	private void writeGetRowClassLines(List<String> viewConfigLabelLines) {
		String entityParamName = getSimpleName(entityClass);
		viewConfigLabelLines.add(concat(sb, "\t", GET_ROW_CLASS, ": function(", entityParamName, ") {"));

		for (LegendField legendField : getViewConfigAnnotation(entityClass).legendFields())
		{
			if (legendField.checkTrue())
			{
				viewConfigLabelLines.add(concat(sb, "\t\tif (", GET_VALUE_FUNCTION_NAME, "(", entityParamName, ", '", legendField.fieldName(), "'))"));
				writeReturnInGetRowClass(viewConfigLabelLines, legendField);
			}
			else
			{
				viewConfigLabelLines.add(concat(sb, "\t\tif (", GET_VALUE_FUNCTION_NAME, "(", entityParamName, ", '", legendField.fieldName(), "') === ", false, ")"));
				writeReturnInGetRowClass(viewConfigLabelLines, legendField);
			}
		}

		viewConfigLabelLines.add(concat(sb, "\t\treturn '", ViewConfig.DEFAULT_ROW_CLASS, "';"));
		viewConfigLabelLines.add("\t}");
	}
	private void writeReturnInGetRowClass(List<String> viewConfigLabelLines, LegendField legendField) {
		viewConfigLabelLines.add(concat(sb, "\t\t\treturn '", getRowClassName(legendField), "';"));
		viewConfigLabelLines.add("");
	}

	private ViewConfig viewConfig;
	private Class entityClass;

	public static final String CHANGE_LABEL = "changeLabel";
	public static final String CLEAR_LABEL = "clearLabel";
	public static final String GET_LABEL = "getLabel";
	public static final String GRID_ID = "gridId";
	public static final String GET_ROW_CLASS = "getRowClass";
	public static final String APPEND_VIEW_CONFIG_MARKER = "${APPEND_VIEW_CONFIG}";
}