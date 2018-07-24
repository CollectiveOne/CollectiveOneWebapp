package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.tokens.TokenType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "model_sections")
public class ModelSection {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private Boolean isTopModelSection;
	
	@Column(name = "title", length = 42)
	private String title;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "description")
	private String description;
	
	
	@Enumerated(EnumType.STRING)
	private ModelSectionVisibility visibility;

	@ManyToMany
	@OrderColumn(name = "subsections_order")
	private List<ModelSection> subsections = new ArrayList<ModelSection>();
	
	@ManyToMany
	private List<ModelSection> subsectionsTrash = new ArrayList<ModelSection>();
	
	@OneToOne
	private MessageThread messageThread;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ModelSectionStatus status;

	@OneToMany(mappedBy = "modelSection")
	@SortNatural
	private SortedSet<Member> members = new TreeSet<Member>();

	@OneToMany
	private List<TokenType> tokenTypes = new ArrayList<TokenType>();

	@ManyToOne
	private FileStored imageFile;

	public FileStored getImagefile()
	{
		return this.imageFile;
	}

	public void setImagefile(FileStored imageFile)
	{
		this.imageFile = imageFile;
	}

	
	
	public List<TokenType> getTokenTypes() {
		return tokenTypes;
	}
	public void setTokenTypes(List<TokenType> tokenTypes) {
		this.tokenTypes = tokenTypes;
	}

	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ModelSection other = (ModelSection) obj;
		return id.equals(other.getId());
	}
	
	public ModelSectionDto toDtoLight () {
		ModelSectionDto sectionDto = new ModelSectionDto();
		
		sectionDto.setId(id.toString());
		sectionDto.setTitle(title);
		sectionDto.setDescription(description);
		sectionDto.setIsTopModelSection(isTopModelSection);
		
		return sectionDto; 
	}
	
	public ModelSectionDto toDto() {
		ModelSectionDto sectionDto = toDtoLight();		
		sectionDto.setnSubsections(subsections.size());
		
		return sectionDto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	
	public Boolean getIsTopModelSection() {
		return isTopModelSection;
	}

	public void setIsTopModelSection(Boolean isTopModelSection) {
		this.isTopModelSection = isTopModelSection;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModelSection> getSubsections() {
		return subsections;
	}

	public void setSubsections(List<ModelSection> subsections) {
		this.subsections = subsections;
	}

	public List<ModelSection> getSubsectionsTrash() {
		return subsectionsTrash;
	}

	public void setSubsectionsTrash(List<ModelSection> subsectionsTrash) {
		this.subsectionsTrash = subsectionsTrash;
	}

	public MessageThread getMessageThread() {
		return messageThread;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}


	public ModelSectionStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(ModelSectionStatus status)
	{
		this.status = status;
	}

	public SortedSet<Member> getMembers()
	{
		return this.members;
	}

	public void setMembers(SortedSet<Member> members)
	{
		this.members = members;
	}

	public ModelSectionVisibility getVisibility()
	{
		return this.visibility;
	}

	public void setVisibility(ModelSectionVisibility visibility)
	{
		this.visibility = visibility;
	}

		
}
