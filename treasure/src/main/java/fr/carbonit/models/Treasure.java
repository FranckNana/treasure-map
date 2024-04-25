package fr.carbonit.models;

public class Treasure {
	
	private int posX;
	private int posY;
	private int nbTreas;
	
	public Treasure(int posX, int posY, int nbTreas) {
		this.posX = posX;
		this.posY = posY;
		this.nbTreas = nbTreas;
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

	public int getNbTreas() {
		return nbTreas;
	}

	public void setNbTreas(int nbTreas) {
		this.nbTreas = nbTreas;
	}

	@Override
	public String toString() {
		return "Treasure => [Axe horizontale=" + posX + ", Axe verticale=" + posY + ", Nb. trésors=" + nbTreas + "]";
	}
	
}
