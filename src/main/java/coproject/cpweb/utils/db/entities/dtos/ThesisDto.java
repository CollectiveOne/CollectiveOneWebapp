package coproject.cpweb.utils.db.entities.dtos;

import java.sql.Timestamp;

public class ThesisDto {
	private int id;
	private int value;
	private Timestamp castDate;
	private UserDto authorDto;
	private double weight;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Timestamp getCastDate() {
		return castDate;
	}
	public void setCastDate(Timestamp castDate) {
		this.castDate = castDate;
	}
	public UserDto getAuthorDto() {
		return authorDto;
	}
	public void setAuthorDto(UserDto authorDto) {
		this.authorDto = authorDto;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
