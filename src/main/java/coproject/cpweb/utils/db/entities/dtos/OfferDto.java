package coproject.cpweb.utils.db.entities.dtos;

/**
 * @author pepo
 *
 */
public class OfferDto {
	private int bidId;
	private double ppoints;
	private long deliveryDate;
	private String creatorUsername;
	
	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}
	public double getPpoints() {
		return ppoints;
	}
	public void setPpoints(double ppoints) {
		this.ppoints = ppoints;
	}
	public long getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	
}
