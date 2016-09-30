package coproject.cpweb.utils.db.entities.dtos;

public class UsernameAndPps {
	private String username;
	private double pps;
	
	public int compareTo(UsernameAndPps o)	{
	     return Double.compare(pps, o.getPps());
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getPps() {
		return pps;
	}
	public void setPps(double pps) {
		this.pps = pps;
	}
}
