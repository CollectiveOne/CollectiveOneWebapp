package org.collectiveone.modules.c1.data.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.c1.data.dtos.LinkDto;
import org.collectiveone.modules.c1.data.dtos.NodeDataDto;
import org.collectiveone.modules.ipld.IpldService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

@Entity
@Table(name = "node_data")
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

	@ElementCollection
	private List<Link> links = new ArrayList<Link>();
	
	public NodeDataDto toDto() throws JsonProcessingException {
		NodeDataDto dto = new NodeDataDto();
		
		dto.setText(textData.getText());
		
		for (Link link : links) {
			LinkDto linkDto = new LinkDto();
			
			linkDto.setLink(IpldService.decode(link.getLink()));
			linkDto.setAfter(link.getAfter() != null ? IpldService.decode(link.getAfter()) : null);
			linkDto.setBefore(link.getBefore() != null ? IpldService.decode(link.getBefore()) : null);
			
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
