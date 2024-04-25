package fr.carbonit.models;

public class Mountain {
	
	private int posX;
	private int posY;
	
	public Mountain(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
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

	@Override
	public String toString() {
		return "Mountain => [Axe horizontale=" + posX + ", Axe verticale=" + posY + "]";
	}
	
	
}
