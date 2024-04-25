package fr.carbonit.models;

import java.util.List;

public class Adventurer {
	
	private String name;
	private int posX;
	private int posY;
	private Orientation orientation;
	private List<Movement> movement;
	private int nbTreasure;

	public Adventurer(String name, int posX, int posY, Orientation orientation, List<Movement> movement,
			int nbTreasure) {
		super();
		this.name = name;
		this.posX = posX;
		this.posY = posY;
		this.orientation = orientation;
		this.movement = movement;
		this.nbTreasure = nbTreasure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public List<Movement> getMovement() {
		return movement;
	}

	public void setMovement(List<Movement> movement) {
		this.movement = movement;
	}
	
	public int getNbTreasure() {
		return nbTreasure;
	}

	public void setNbTreasure(int nbTreasure) {
		this.nbTreasure = nbTreasure;
	}

	@Override
	public String toString() {
		return "Adventurer => [name=" + name + ", Axe horizontal=" + posX + ", Axe verticale=" + posY + ", orientation=" + orientation
				+ ", nbTreasure=" + nbTreasure + ", movement=" + movement  +"]";
	}
	

}
