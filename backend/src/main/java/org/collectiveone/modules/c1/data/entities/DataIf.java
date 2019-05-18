package org.collectiveone.modules.c1.data.entities;

import org.collectiveone.modules.c1.data.enums.DataType;

public interface DataIf {
	
	public DataType getType();
	public void setType(DataType type);
	public TextData getTextData();
	public void setTextData(TextData textData);
	public NodeData getNodeData();
	public void setNodeData(NodeData nodeData);

}
