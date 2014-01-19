/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.gen.ExtEntityUtils;
import su.opencode.kefir.gen.field.VOField;
import su.opencode.kefir.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static su.opencode.kefir.gen.ExtEntityUtils.getExtEntityAnnotation;
import static su.opencode.kefir.gen.fileWriter.ClassFileWriter.TAB;
import static su.opencode.kefir.gen.fileWriter.VOClassFileWriter.getVOFieldName;
import static su.opencode.kefir.util.ObjectUtils.getGetterName;
import static su.opencode.kefir.util.StringUtils.capitalize;
import static su.opencode.kefir.util.StringUtils.concat;

public class VOFieldsTree
{
	public VOFieldsTree(Field field, String entityVarName, boolean assignVO) {
		this.field = field;
		this.root = new VOFieldTreeNode(field.getName(), null, null);
		this.entityVarName = entityVarName;
		this.assignVO = assignVO;
	}

	public void fill(VOField[] fields) {
		fill(Arrays.asList(fields));
	}

	public void fill(List<VOField> voFields) {
		for (VOField voField : voFields)
		{
			VOFieldTreeNode currentNode = root;

			String[] names = getNames(voField);
			for (int i = 0, namesLength = names.length; i < namesLength; i++)
			{ // i is depth index
				String name = names[i];

				if (currentNode.hasChild(name))
				{
					currentNode = currentNode.getChild(name);
				}
				else
				{
					VOFieldTreeNode newNode = new VOFieldTreeNode(name, voField, currentNode);
					currentNode.addChild(newNode);
					currentNode = currentNode.getChild(name);
				}
			}
		}
	}

	public List<String> getFileLines() {
		if (!assignVO && root.hasNoChildren())
			return Collections.emptyList();

		List<String> lines = new ArrayList<String>();

		String fieldVOClassName = ExtEntityUtils.getListVOClassSimpleName(getExtEntityAnnotation(field.getType()), field.getType());
		String rootVarName = field.getName();

		lines.add( concat(sb, field.getType().getSimpleName(), " ", rootVarName, " = ", entityVarName, ".", getGetterName(rootVarName), "();") ); // get choose entity
		lines.add( concat(sb, "if (", rootVarName, " != null)") );
		lines.add("{");

		if (assignVO)
			lines.add( concat(sb, TAB, "this.", rootVarName, " = new ", fieldVOClassName, "(", rootVarName, ");") ); // assign choose entity VO field (only for choose fields itself)

		addVOFieldLines(lines, rootVarName, TAB, field.getType(), root);

		lines.add("}"); // close root

		return lines;
	}
	private void addVOFieldLines(List<String> lines, String rootVarName, String indent, Class<?> rootType, VOFieldTreeNode root) {
		for (VOFieldTreeNode childlessChild : root.getChildlessChildren())
		{
			lines.add( concat(sb, indent, "this.", getVOFieldName(childlessChild.getVoField(), field), " = ", rootVarName, ".", getGetterName(childlessChild.getName()), "();") );
		}

		for (VOFieldTreeNode childfulChild : root.getChildfulChildren())
		{
			String varName = concat(sb, rootVarName, capitalize(childfulChild.getName()));

			lines.add("");
			Class type = ObjectUtils.getType(rootType, childfulChild.getName());
			lines.add( concat(sb, indent, type.getSimpleName(), " ", varName, " = ", rootVarName, ".", getGetterName(childfulChild.getName()), "();") );
			lines.add( concat(sb, indent, "if (", varName, " != null)"));
			lines.add( concat(sb, indent, "{"));
			addVOFieldLines(lines, varName, concat(sb, indent, TAB), type, childfulChild);
			lines.add( concat(sb, indent, "}"));
		}
	}

	private String[] getNames(VOField voField) {
		return voField.name().split("\\.");
	}

	private Field field;
	private VOFieldTreeNode root;
	private String entityVarName;
	private boolean assignVO = true;

	private StringBuffer sb = new StringBuffer();
}