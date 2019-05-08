package org.collectiveone.modules.c1.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.collectiveone.modules.c1.data.dtos.LinkPositionDL;
import org.collectiveone.modules.c1.data.dtos.NodeDataDto;
import org.collectiveone.modules.c1.data.dtos.PositionedLinkDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonPropertyOrder({ "text" })
public class NodeData {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private UUID id;

	@ManyToOne 
	private TextData textData;

	@OneToMany
	private List<Link> links = new ArrayList<Link>();
	
	public NodeDataDto toDto() {
		NodeDataDto dto = new NodeDataDto();
		
		dto.setText(textData.getText());
		
		for (Link link : links) {
			PositionedLinkDto linkDto = new PositionedLinkDto();
			
			linkDto.setPerspective(link.getPerspective().toDto());
			
			LinkPositionDL position = new LinkPositionDL();
			position.setAfter(linkDto.getPosition().getAfter());
			position.setBefore(linkDto.getPosition().getBefore());
			
			linkDto.setPosition(position);
			
			dto.getLinks().add(linkDto);	
		}
		
		return dto;
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public TextData getTextData() {
		return textData;
	}
	public void setTextData(TextData textData) {
		this.textData = textData;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
}
