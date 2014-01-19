/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.gen.fileWriter;

import su.opencode.kefir.gen.field.VOField;

import java.util.ArrayList;
import java.util.List;

public class VOFieldTreeNode
{
	public VOFieldTreeNode(String name, VOField voField, VOFieldTreeNode parent) {
		this.name = name;
		this.voField = voField;
		this.parent = parent;
		this.children = new ArrayList<VOFieldTreeNode>();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public VOField getVoField() {
		return voField;
	}
	public void setVoField(VOField voField) {
		this.voField = voField;
	}
	public VOFieldTreeNode getParent() {
		return parent;
	}
	public void setParent(VOFieldTreeNode parent) {
		this.parent = parent;
	}
	public List<VOFieldTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<VOFieldTreeNode> children) {
		this.children = children;
	}

	public boolean hasChild(String name) {
		for (VOFieldTreeNode child : children)
			if (child.getName().equals(name))
				return true;

		return false;
	}

	public VOFieldTreeNode getChild(String name) {
		for (VOFieldTreeNode child : children)
			if (child.getName().equals(name))
				return child;

		return null;
	}

	public void addChild(VOFieldTreeNode child) {
		this.children.add(child);
	}

	public boolean hasNoChildren() {
		return children == null || children.isEmpty();
	}
	public boolean hasChildren() {
		return !hasNoChildren();
	}

	public List<VOFieldTreeNode> getChildlessChildren() {
		List<VOFieldTreeNode> nodes = new ArrayList<VOFieldTreeNode>();

		for (VOFieldTreeNode child : children)
			if (child.hasNoChildren())
				nodes.add(child);

		return nodes;
	}
	public List<VOFieldTreeNode> getChildfulChildren() {
		List<VOFieldTreeNode> nodes = new ArrayList<VOFieldTreeNode>();

		for (VOFieldTreeNode child : children)
			if (child.hasChildren())
				nodes.add(child);

		return nodes;
	}

	/**
	 * название пол€ на текущем уровне иерархии. явл€етс€ ключом доступа к полю.
	 */
	private String name;

	private VOField voField;

	private VOFieldTreeNode parent;

	private List<VOFieldTreeNode> children;
}