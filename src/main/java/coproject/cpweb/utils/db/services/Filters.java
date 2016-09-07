package coproject.cpweb.utils.db.services;

import java.util.List;

public class Filters {
	private List<String> projectNames;
	private List<String> stateNames;
	private List<String> creatorUsernames;
	private String keyw;
	private int page;
	private int nperpage;
	
	
	public List<String> getProjectNames() {
		return projectNames;
	}
	public void setProjectNames(List<String> projectNames) {
		this.projectNames = projectNames;
	}
	public List<String> getStateNames() {
		return stateNames;
	}
	public void setStateNames(List<String> stateNames) {
		this.stateNames = stateNames;
	}
	public List<String> getCreatorUsernames() {
		return creatorUsernames;
	}
	public void setCreatorUsernames(List<String> creatorUsernames) {
		this.creatorUsernames = creatorUsernames;
	}
	public String getKeyw() {
		return keyw;
	}
	public void setKeyw(String keyw) {
		this.keyw = keyw;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getNperpage() {
		return nperpage;
	}
	public void setNperpage(int nperpage) {
		this.nperpage = nperpage;
	}
	
}
