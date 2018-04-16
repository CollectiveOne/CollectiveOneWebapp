package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphNode {

	UUID elementId;
	List<GraphNode> parents;
	List<GraphNode> children;
	
	public List<UUID> toList(Boolean addParents, Boolean addChildren) {
		return toList(addParents, addChildren, new ArrayList<UUID>());
	}
	
	public List<UUID> toList(Boolean addParents, Boolean addChildren, List<UUID> list) {
		list.add(elementId);
		
		if (addParents) {
			for (GraphNode parent : parents) {
				list.addAll(parent.toList(true, false, list));
			}	
		}
		
		if (addChildren) {
			for (GraphNode children : children) {
				list.addAll(children.toList(false, true, list));
			}
		}
		
		return list;
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
