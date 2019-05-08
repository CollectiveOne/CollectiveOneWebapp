package org.collectiveone.modules.c1.data.dtos;

public class PositionedLinkDto extends LinkDto {
	
	private LinkPositionDL position;

	public LinkPositionDL getPosition() {
		return position;
	}

	public void setPosition(LinkPositionDL position) {
		this.position = position;
	}
	
}
