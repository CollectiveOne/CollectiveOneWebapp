package org.collectiveone.modules.model;

import java.util.List;
import java.util.UUID;

public class GraphNode {

	UUID elementId;
	List<GraphNode> parents;
	List<GraphNode> children;
	
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
