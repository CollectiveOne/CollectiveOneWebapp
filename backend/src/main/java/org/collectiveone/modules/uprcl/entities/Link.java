package org.collectiveone.modules.uprcl.entities;

import java.security.MessageDigest;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.c1.data.PositionType;
import org.collectiveone.modules.uprcl.support.LinkDoubleLinked;
import org.collectiveone.modules.uprcl.support.PositionDoubleLinkedList;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "links")
public class Link {
	
	@Id
	private String id;

	private LinkType type;
	
	@ManyToOne
	private Perspective perspective;
	
	@Enumerated(EnumType.STRING)
	private PositionType positionType;
	
	@ManyToOne
	private Perspective before;
	
	@ManyToOne
	private Perspective after;
	
	public String computeId() {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			ObjectMapper objectMapper = new ObjectMapper();
			
			String json = objectMapper.writeValueAsString(this);
			byte[] hash = digestInstance.digest(json.getBytes());
			
			return Base58.encode(hash);	
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	@JsonValue
	public String toJson() throws JsonProcessingException {
		LinkDoubleLinked linkStd = new LinkDoubleLinked();
		
		linkStd.setPerspectiveId(perspective.getId());
		
		PositionDoubleLinkedList position = new PositionDoubleLinkedList();
		
		position.setAfter(after.getId());
		position.setBefore(before.getId());
		linkStd.setPosition(position);
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(linkStd);
	}
	
	public String getId() {
		return id;
	}

	public void setId() {
		this.id = this.computeId();
	}

	public LinkType getType() {
		return type;
	}

	public void setType(LinkType type) {
		this.type = type;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}
	
	public PositionType getOrderType() {
		return positionType;
	}

	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}

	public Perspective getBefore() {
		return before;
	}

	public void setBefore(Perspective before) {
		this.before = before;
	}

	public Perspective getAfter() {
		return after;
	}

	public void setAfter(Perspective after) {
		this.after = after;
	}
	
}
