package fr.carbonit.models;
import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Orientation {
	
	WEST("O"),
	EAST("E"),
	NORTH("N"),
	SOUTH("S");
	
	private String value;
	
	Orientation(String value){
		this.value = value;
	}
	
	public static Orientation forValues(String label) {
		
		Orientation orient = Arrays.stream(values())
					.filter(aType -> aType.value.equals(label))
					.findAny()
					.orElseThrow(() -> new NoSuchElementException("No account type for : " +label));
		
		return orient;
	}
	
	public String getValue() {
		return value;
	}
	
}
