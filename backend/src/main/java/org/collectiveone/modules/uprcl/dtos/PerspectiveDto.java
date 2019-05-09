package org.collectiveone.modules.uprcl.dtos;

import org.collectiveone.modules.c1.views.ViewDto;
import org.collectiveone.modules.uprcl.entities.PerspectiveType;

public class PerspectiveDto {
	
	private String id;
	private ContextDto context;
	private String creator;
	private Long timestamp;
	private ViewDto view;
	private String name;
	private CommitDto head;
	private PerspectiveType type;
	
	@Override
	public String toString() {
		return "   name: " + name + "\n" +
			   "     id: " + id  + "\n" + 
			   "context: " + (context != null ? context.getId() : "null")  + "\n" +
			   "   head: " + (head != null ? head.getId() : "null");
	}
	
	public PerspectiveDto() {
		super();
	}
	public PerspectiveDto(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ContextDto getContext() {
		return context;
	}
	public void setContext(ContextDto context) {
		this.context = context;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public ViewDto getView() {
		return view;
	}
	public void setView(ViewDto view) {
		this.view = view;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CommitDto getHead() {
		return head;
	}
	public void setHead(CommitDto head) {
		this.head = head;
	}
	public PerspectiveType getType() {
		return type;
	}
	public void setType(PerspectiveType type) {
		this.type = type;
	}
	
	
}
