package qwirkle.Desktop.entity;

/**
 * Pretty self explanatory, but hey... 
 * This class defines the tiles gives each one of it's characteristics an ID.
 *  
 * @author 
 *
 */
public class Tile {
	
	// Tile identifiers 
	private int color;
	private int shape;
	private int uniqueId;
	
	public Tile(int color, int shape, int uniqueId) {
		this.color = color;
		this.shape = shape;
		this.uniqueId = uniqueId;
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
		return uniqueId;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueId = uniqueID;
	}

	
}
