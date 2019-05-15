package org.collectiveone.modules.c1.data;

import java.security.MessageDigest;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.uprcl.entities.Commit;
import org.collectiveone.modules.uprcl.entities.Data;
import org.collectiveone.modules.uprcl.entities.Perspective;

@Entity
@Table(name = "links")
public class Link {
	
	@Id
	private String id;
	
	private LinkType type;
	
	private LinkObjectType objectType;
	
	/** External Links */
	private ExternalLink link;
	
	/** Local Links */
	@ManyToOne
	private Commit commit;
	
	@ManyToOne
	private Data data;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Perspective parent;
	
	@ManyToOne
	private Perspective before;
	
	@ManyToOne
	private Perspective after;
	
	
	public String computeId() {
		return computeId(this.toString());
	}
	
	public static String computeId(ExternalLink link) {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			byte[] hash = digestInstance.digest(link.toString().getBytes());
			return Base58.encode(hash);	
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	public Link() {
		super();
	}
	
	public Link(String string) {
		super();
		
		this.type = LinkType.EXTERNAL;
		this.link = new ExternalLink(string);
	}
	
	public Link(Data data) {
		super();
		
		this.type = LinkType.LOCAL;
		this.objectType = LinkObjectType.DATA;
		this.data = data;
	}
	
	public Link(Commit commit) {
		super();
		
		this.type = LinkType.LOCAL;
		this.objectType = LinkObjectType.COMMIT;
		this.commit = commit;
	}
	
	public Link(Perspective perspective, Perspective parent, Perspective before, Perspective after) {
		super();
		
		this.type = LinkType.LOCAL;
		this.objectType = LinkObjectType.PERSPECTIVE;
		this.perspective = perspective;
		this.parent = parent;
		this.before = before;
		this.after = after;
	}
	
	@Override
	public String toString() {
		
		switch (type) {
		case EXTERNAL:
			return this.link.toString();
		
		case LOCAL:
			ExternalLink local = null;
			
			switch (objectType) {
				case DATA:
					local = new ExternalLink();
					local.setLocalElement(data.getId());
				break;
				
				case COMMIT:
					local = new ExternalLink();
					local.setLocalElement(commit.getId());
				break;
				
				case PERSPECTIVE:
					local = new ExternalLink();
					local.setLocalElement(perspective.getId());
				break;
			}
			
			return local.toString();
		}
		
		return null;
		
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
	
	public LinkObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(LinkObjectType objectType) {
		this.objectType = objectType;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public Perspective getParent() {
		return parent;
	}

	public void setParent(Perspective parent) {
		this.parent = parent;
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

	public ExternalLink getLink() {
		return link;
	}

	public void setLink(ExternalLink link) {
		this.link = link;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
}
