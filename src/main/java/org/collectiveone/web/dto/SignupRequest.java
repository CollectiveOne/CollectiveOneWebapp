package org.collectiveone.web.dto;

public class SignupRequest {
	private String email;
	private String referral;
	private String comments;
	

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getReferral() {
		return referral;
	}
	public void setReferral(String referral) {
		this.referral = referral;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}

