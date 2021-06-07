package ca.sheridancollege.doricha.beans;

import lombok.*;

@Data
@NoArgsConstructor
public class Bike {

	private Long bikeID;
	private Long manufacturerID;
	private String model;
	private int year;
	private String color;
	private double price;
	
}
