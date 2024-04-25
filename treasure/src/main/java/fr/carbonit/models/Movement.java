package fr.carbonit.models;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Movement {
	
	FORWARD("A"),
	RIGHT("D"),
	LEFT("G");
	
	private String value;
	
	Movement(String value){
		this.value = value;
	}
	
	public static Movement forValues(String label) {
		
		Movement orient = Arrays.stream(values())
					.filter(aType -> aType.value.equals(label))
					.findAny()
					.orElseThrow(() -> new NoSuchElementException("No account type for : " +label));
		
		return orient;
	}
	
	public String getValue() {
		return value;
	}
	
}
