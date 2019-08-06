package logic_objects;

import java.util.HashMap;

import Model.TileOnPositionEX;
import enumeration.Neighbor;

/**
 * This class represents the to be determined neighbors of a tile,
 * and will be used to check across them for errors and scoring
 * @author Lukas
 *
 */
public class NeighborshipObject {
	//													      top
	// Has the neighbors as tiles in it				 	       ^
	// POSITIONS ARE RELATIVE OF THE PLAYTILE        left < playtile > right
	//													       v
	//														 bottom
	HashMap<Neighbor, TileOnPositionEX> neighbors;
	TileOnPositionEX playtile;
	TileOnPositionEX fail_neighbor;
	boolean valid;
	

	/**
	 * This constructor is used, if the Neighborship check succeeded
	 * @param playtile
	 * @param neighbors
	 * @param valid
	 */
	public NeighborshipObject(TileOnPositionEX playtile, HashMap<Neighbor, TileOnPositionEX> neighbors, boolean valid) {
		this.neighbors = neighbors;
		this.playtile = playtile;
		this.valid = valid;
	}
	
	/**
	 * This one is used for failure
	 * @param playtile
	 */
	public NeighborshipObject(TileOnPositionEX playtile, TileOnPositionEX boardtile, boolean valid) {
		this.playtile = playtile;
		this.fail_neighbor = boardtile;
		this.valid = valid;
	}

	public void setNeighbors(HashMap<Neighbor, TileOnPositionEX> neighbors) {
		this.neighbors = neighbors;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public TileOnPositionEX getPlaytile() {
		return playtile;
	}

	public void setPlaytile(TileOnPositionEX playtile) {
		this.playtile = playtile;
	}
	
	public TileOnPositionEX getNeighbor(Neighbor position) {
		return this.neighbors.get(position);
	}
	
	public HashMap<Neighbor, TileOnPositionEX> getAllNeighbors() {
		return neighbors;
	}
	
	public TileOnPositionEX getFail_neighbor() {
		return fail_neighbor;
	}

	public void setFail_neighbor(TileOnPositionEX fail_neighbor) {
		this.fail_neighbor = fail_neighbor;
	}
}
