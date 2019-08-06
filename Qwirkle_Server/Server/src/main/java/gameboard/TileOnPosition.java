/**
 * The TileOnPosition class simulates the single coordinates
 * with a tile laying on the current position, sets neighbors
 * to this field to get the connections
 * 
 * @author Jan Sadowski, Youssef Ameur
 * @version_1.1
 */

package gameboard;

import java.util.HashMap;

import de.upb.swtpra1819interface.models.Tile;
import enumeration.Neighbor;

public class TileOnPosition {

	public int coordX;
	public int coordY;
	Tile tile;
	private transient HashMap<Neighbor, TileOnPosition> neighbors = new HashMap<Neighbor, TileOnPosition>();
	
    /** 
	* public constructor to create a TileOnPosition given its attributes
	* 
	* @param xCoord			the tiles x-Coordinate
	* @param yCoord			the tiles y-Coordinate
	* @param tile2		the new tile which lays here
	* 
	*/
	public TileOnPosition(int xCoord, int yCoord, Tile tile2) {
		this.coordX = xCoord;
		this.coordY = yCoord;
		this.tile = tile2;
	}

	// Setter & Getter Methods for the Coordinates and the tile placed on those coordinates
	public int getX() {
		return coordX;
	}
	public void setX(int x) {
		this.coordX = x;
	}
	public int getY() {
		return coordY;
	}
	public void setY(int y) {
		this.coordY = y;
	}
	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}	

	// Adding and Managing the tile neighbors
	public void addNeighbor(TileOnPosition neighborTile) {
		if (neighbors == null) {
			neighbors = new HashMap<Neighbor, TileOnPosition>();
		}
		if (neighborTile.getX() == this.coordX) {
			if (neighborTile.getY() == this.coordY - 1) {
				neighbors.put(Neighbor.Top, neighborTile);
				//System.out.print("Added Top Neighbor: ");
				//System.out.print(neighborTile.getTile().getUniqueID());
			}else if (neighborTile.getY() == this.coordY + 1) {
				neighbors.put(Neighbor.Bottom, neighborTile);
				//System.out.print("Added Bottom Neighbor: ");
				//System.out.print(neighborTile.getTile().getUniqueID());
			}
		}else if (neighborTile.getY() == this.coordY) {
			if (neighborTile.getX() == this.coordX - 1) {
				neighbors.put(Neighbor.Left, neighborTile);
				//System.out.print("Added Left Neighbor: ");
				//System.out.print(neighborTile.getTile().getUniqueID());
			}else if (neighborTile.getX() == this.coordX + 1) {
				neighbors.put(Neighbor.Right, neighborTile);
				//System.out.print("Added Right Neighbor: ");
				//System.out.print(neighborTile.getTile().getUniqueID());
			}
		}
		//System.out.print(", for the Tile: ");
		//System.out.println(this.getTile().getUniqueID());
	}

	/**
	 * Removes the neighbor of the TileOnPosition (checks where the neighbor lays and removes both connections)
	 * @param neighborTile, the neighborTile which will be deleted as the neighbor
	 */
	public void removeNeighbor(TileOnPosition neighborTile) {
		if (neighborTile != null) {
			if (neighborTile.getNeighbor(Neighbor.Top) == this) {
				this.neighbors.remove(Neighbor.Bottom);
				neighborTile.neighbors.remove(Neighbor.Top);
			}
			else if (neighborTile.getNeighbor(Neighbor.Right) == this) {
				this.neighbors.remove(Neighbor.Left);
				neighborTile.neighbors.remove(Neighbor.Right);
			}
			else if (neighborTile.getNeighbor(Neighbor.Bottom) == this) {
				this.neighbors.remove(Neighbor.Top);
				neighborTile.neighbors.remove(Neighbor.Bottom);
			}
			else if (neighborTile.getNeighbor(Neighbor.Left) == this) {
				this.neighbors.remove(Neighbor.Right);
				neighborTile.neighbors.remove(Neighbor.Left);
			}
		}
	}
	
	// Getting the neighbor (with parameter = Neighbor.Top || Neighbor.Bottom || Neighbor.Right || Neighbor.Left)
	public TileOnPosition getNeighbor(Neighbor direction){
		try {	
			return this.neighbors.get(direction);
		}
		catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		TileOnPosition other = (TileOnPosition) o;
		if (this.coordX == other.coordX && this.coordY == other.coordY && this.tile == other.tile) {
			return true;
		}else {
			return false;
		}
	}
}
