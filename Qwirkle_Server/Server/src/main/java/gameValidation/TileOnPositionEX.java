/**
 * The TileOnPosition class simulates the single coordinates
 * with a tile laying on the current position, sets neighbors
 * to this field to get the connections
 * 
 * @author Jan Sadowski, Youssef Ameur
 * @version_1.1
 */

package gameValidation;

import java.util.HashMap;


import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import enumeration.Colors;
import enumeration.Neighbor;
import enumeration.Shapes;

public class TileOnPositionEX extends Tile{

	
	
	
	public int x;
	public int y;
	public HashMap<Neighbor, TileOnPositionEX> neighbors;
	
    /** 
	* public constructor to create a TileOnPosition given its attributes
	* 
	* @param xCoord			the tiles x-Coordinate
	* @param yCoord			the tiles y-Coordinate
	* @param tile2		the new tile which lays here
	* 
	*/
	public TileOnPositionEX(int xCoord, int yCoord, Tile tile) {
		super(tile.getColor(), tile.getShape(), tile.getUniqueId());
		this.x = xCoord;
		this.y = yCoord;
		this.neighbors = new HashMap<>();
		this.neighbors.put(Neighbor.Left, null);
		this.neighbors.put(Neighbor.Top, null);
		this.neighbors.put(Neighbor.Right, null);
		this.neighbors.put(Neighbor.Bottom, null);
	}
	
	public TileOnPosition toTileOnPosition() {
		return (new TileOnPosition(this.x, this.y, getTile()));
	}
	
	public gameboard.TileOnPosition toOwnTileOnPosition() {
		return (new gameboard.TileOnPosition(this.x, this.y, getTile()));
	}
	// Setter & Getter Methods for the Coordinates and the tile placed on those coordinates
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Tile getTile() {
		return new Tile(getColor(), getShape(), getUniqueId());
	}
	

	// Adding and Managing the tile neighbors
	public void addNeighbor(TileOnPositionEX neighborTile, Neighbor position_relative_to_tile) {
		this.neighbors.put(position_relative_to_tile, neighborTile);
	}

	/**
	 * Removes the neighbor of the TileOnPosition (checks where the neighbor lays and removes both connections)
	 * @param neighborTile, the neighborTile which will be deleted as the neighbor
	 */
	public void removeNeighbor(Neighbor direction) {
		this.neighbors.put(direction, null);
	}
	
	// Getting the neighbor (with parameter = Neighbor.Top || Neighbor.Bottom || Neighbor.Right || Neighbor.Left)
	public TileOnPositionEX getNeighbor(Neighbor direction){
		return this.neighbors.get(direction);
	}
	
	@Override
	public String toString() {
        return String.format("{C: " + Colors.fromInteger(getColor()) +", S: " +  Shapes.fromInteger(getShape()) + " , [PosX: " + getX() + ", PosY: " + getY() + "]");
    }

}
