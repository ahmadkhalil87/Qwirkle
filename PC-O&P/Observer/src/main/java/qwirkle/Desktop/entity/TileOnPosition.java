/**
 * The TileOnPosition class simulates the single coordinates
 * with a tile laying on the current position, sets neighbors
 * to this field to get the connections
 * 
 * @author Jan Sadowski, Youssef Ameur
 * @version_1.1
 */

package qwirkle.Desktop.entity;

import java.util.HashMap;
import qwirkle.Desktop.enumeration.Neighbor;

public class TileOnPosition {

	public int coordX;
	public int coordY;
	Tile tile;
//	private HashMap<Neighbor, TileOnPosition> neighbors = new HashMap<>();
	
	/** 
	* public constructor to create a TileOnPosition given its attributes
	* 
	* @param xCoord			the tiles x-Coordinate
	* @param yCoord			the tiles y-Coordinate
	* @param tile2		the new tile which lays here
	* 
	* */
	public TileOnPosition(int coordX, int coordY, Tile tile) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.tile = tile;
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
//	public void addNeighbor(TileOnPosition neighborTile) {
//		if(neighborTile.getX() == this.x) {
//			if (neighborTile.getY() == this.y - 1) {
//				neighbors.put(Neighbor.Top, neighborTile);
//			}else if(neighborTile.getY() == this.y + 1) {
//				neighbors.put(Neighbor.Bottom, neighborTile);
//			}
//		}else if (neighborTile.getY() == this.y) {
//			if (neighborTile.getX() == this.x - 1) {
//				neighbors.put(Neighbor.Left, neighborTile);
//			}else if(neighborTile.getX() == this.x + 1) {
//				neighbors.put(Neighbor.Right, neighborTile);
//			}
//		}
//	}

	
//	public void removeNeighbor(TileOnPosition neighborTile) {
//		if (neighborTile != null) {
//			if (neighborTile.getNeighbor(Neighbor.Top) == this) {
//				this.neighbors.remove(Neighbor.Bottom);
//				neighborTile.neighbors.remove(Neighbor.Top);
//			}
//			if (neighborTile.getNeighbor(Neighbor.Right) == this) {
//				this.neighbors.remove(Neighbor.Left);
//				neighborTile.neighbors.remove(Neighbor.Right);
//			}
//			if (neighborTile.getNeighbor(Neighbor.Bottom) == this) {
//				this.neighbors.remove(Neighbor.Top);
//				neighborTile.neighbors.remove(Neighbor.Bottom);
//			}
//			if (neighborTile.getNeighbor(Neighbor.Left) == this) {
//				this.neighbors.remove(Neighbor.Right);
//				neighborTile.neighbors.remove(Neighbor.Left);
//			}
//		}
//	}

	
//	// Getting the neighbor (with parameter = Neighbor.Top || Neighbor.Bottom || Neighbor.Right || Neighbor.Left)
//	public TileOnPosition getNeighbor(Neighbor direction){
//		return this.neighbors.get(direction);
//	}

//	@Override
//	public boolean equals(Object o) {
//		TileOnPosition other = (TileOnPosition) o;
//		if (this.x == other.x && this.y == other.y && this.tile == other.tile) {
//			return true;
//		}else {
//			return false;
//		}
//	}

}
