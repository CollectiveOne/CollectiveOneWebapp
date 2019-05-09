package org.collectiveone.modules.c1.data.dtos;

public class PositionedLinkDto extends LinkDto {
	
	private LinkPositionDL position;

	@Override
	public String toString() {
		return "parent: {" + parent.toString() + "}, " +
			   "perspective: {" + perspective.toString() + "}";
	} 
	
	public LinkPositionDL getPosition() {
		return position;
	}

	public void setPosition(LinkPositionDL position) {
		this.position = position;
	}
	
}
