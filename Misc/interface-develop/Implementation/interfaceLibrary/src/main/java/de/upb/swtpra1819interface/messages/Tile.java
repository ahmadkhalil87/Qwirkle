package de.upb.swtpra1819interface.messages;

public class Tile {
	
	private int color;
	private int shape;
	private int uniqueID;
	
	public Tile(int color, int shape, int uniqueID) {
		this.color = color;
		this.shape = shape;
		this.uniqueID = uniqueID;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}
}
