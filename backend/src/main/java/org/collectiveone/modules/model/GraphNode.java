package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphNode {

	UUID elementId;
	List<GraphNode> parents = new ArrayList<GraphNode>();
	List<GraphNode> children = new ArrayList<GraphNode>();
	
	public List<UUID> toList(Boolean addParents, Boolean addChildren) {
		List<UUID> list = new ArrayList<UUID>();
		
		/* add elements by reference */
		addNeighborsRec(addParents, addChildren, list);
		
		return list;
	}
	
	public void addNeighborsRec(Boolean addParents, Boolean addChildren, List<UUID> list) {
		list.add(elementId);
		
		if (addParents) {
			for (GraphNode parent : parents) {
				parent.addNeighborsRec(true, false, list);;
			}	
		}
		
		if (addChildren) {
			for (GraphNode children : children) {
				children.addNeighborsRec(false, true, list);;
			}
		}
	}
	
	public UUID getElementId() {
		return elementId;
	}
	public void setElementId(UUID elementId) {
		this.elementId = elementId;
	}
	public List<GraphNode> getParents() {
		return parents;
	}
	public void setParents(List<GraphNode> parents) {
		this.parents = parents;
	}
	public List<GraphNode> getChildren() {
		return children;
	}
	public void setChildren(List<GraphNode> children) {
		this.children = children;
	}
	
}
