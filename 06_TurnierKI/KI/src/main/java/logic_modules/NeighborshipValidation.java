package logic_modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.Field;
import Model.TileOnPositionEX;
import enumeration.Check_Type;
import enumeration.Neighbor;
import logic_objects.*;

/**
 * This class inherits methods which are used to gain neighborship information 
 * and validate neighborships.
 * @author Lukas
 *
 */
public class NeighborshipValidation {
	Logger logger = LogManager.getLogger();
	Field field;
	
	/**
	 * This Method determines the specific neighbors directly next to the given tile
	 * 
	 * @param tile The tile you want to determine the neighbors of
	 * @param Ghetto An Arraylist with surrounding tiles, where you wanna find the neighbors in
	 * @return 
	 */
	public NeighborshipObject find_neighborhood(TileOnPositionEX playtile, ArrayList<TileOnPositionEX> Ghetto) {
		HashMap<Neighbor, TileOnPositionEX> neighbors = new HashMap<>();
		
		for(TileOnPositionEX house : Ghetto) {
			// Boardtile is to the Left of the playtile
			if(house.getX() == playtile.getX()-1 && house.getY() == playtile.getY()) {
				if(playtile.getNeighbor(Neighbor.Left) == null && house.getNeighbor(Neighbor.Right) == null) {
					neighbors.put(Neighbor.Left, house);
				}
				else {
					// Playtile cannot be placed, there already is a neighbor in its position
					return new NeighborshipObject(playtile, house, false);
				}
			}
			// Boardtile is to the Right of the playtile
			if(house.getX() == playtile.getX() + 1 && house.getY() == playtile.getY()) {
				if(playtile.getNeighbor(Neighbor.Right) == null && house.getNeighbor(Neighbor.Left) == null) {
					neighbors.put(Neighbor.Right, house);
				}
				else {
					// Playtile cannot be placed, there already is a neighbor in its position
					return new NeighborshipObject(playtile, house, false);
				}
			}
			// Boardtile is to the Top of the playtile
			if(house.getX() == playtile.getX() && house.getY() == playtile.getY() + 1) {
				if(playtile.getNeighbor(Neighbor.Top) == null && house.getNeighbor(Neighbor.Bottom) == null) {
					neighbors.put(Neighbor.Top, house);
				}
				else {
					// Playtile cannot be placed, there already is a neighbor in its position
					return new NeighborshipObject(playtile, house, false);
				}
			}
			//Boardtile is below the playtile
			if(house.getX() == playtile.getX() && house.getY() == playtile.getY() - 1) {
				if(playtile.getNeighbor(Neighbor.Bottom) == null && house.getNeighbor(Neighbor.Top) == null) {
					neighbors.put(Neighbor.Bottom, house);
				}
				else {
					// Playtile cannot be placed, there already is a neighbor in its position
					return new NeighborshipObject(playtile, house, false);
				}
			}
		}
		if(!neighbors.isEmpty()) {
			return new NeighborshipObject(playtile, neighbors, true);
		}
		else {
			return new NeighborshipObject(playtile, playtile, false);
		}
	}
	
	/**
	 * This Method is called after validating the Neighbors
	 * It determines for one neighbor and his neighbors in the direction given,
	 * if the playtile is being put on the field in a valid way.
	 * 
	 * Scores are only applied to the Boardtiles, not the Tile placed itself,
	 * to make calculating scores easier
	 * 
	 * @param playtile -- The tile being put on the board
	 * @param house -- Boardtile, and where it has its home
	 * @param direction -- the direction relative of the playtile to the boardtile
	 * @return 
	 */
	public NeighborshipCertificate Neighbor_approval(TileOnPositionEX playtile, TileOnPositionEX house, Neighbor direction, Check_Type move_basis, ArrayList<Integer> already_checked) {
		
		Check_Type checking_for = move_basis;
		int score = 0;
		boolean postman = true;

//###########################################-----FIRST NEIGHBOR CHECK----##############################################
//######################################################################################################################
		/**
		 * First checking, if the first neighbor is valid
		 */
		if(checking_for == Check_Type.Color) {
			/**
			 * Checking is move is Color-Based
			 */
			if(house.getColor() == playtile.getColor()) {
				if(house.getShape() != playtile.getShape()) {
					if(already_checked.contains(house.getShape())) {
						return new NeighborshipCertificate(playtile, house, false);
					}
					else {
						already_checked.add(house.getShape());
						if(!already_checked.contains(playtile.getShape())) {
							already_checked.add(playtile.getShape());
						}
					}
				}
				else {
					return new NeighborshipCertificate(playtile, house, false);
				}
			}
			else {
				return new NeighborshipCertificate(playtile, house, false);
			}
		}
		else if(checking_for == Check_Type.Shape) {
			/**
			 * Checking of move is Shape-Based
			 */
			if(house.getShape() == playtile.getShape()) {
				if(house.getColor() != playtile.getColor()) {
					if(already_checked.contains(house.getColor())) {
						return new NeighborshipCertificate(playtile, house, false);
					}
					else {
						already_checked.add(house.getColor());
						if(!already_checked.contains(playtile.getColor())) {
							already_checked.add(playtile.getColor());
						}
					}
				}
				else {
					return new NeighborshipCertificate(playtile, house, false);
				}
			}
			else {
				return new NeighborshipCertificate(playtile, house, false);
			}
		}
		else if(checking_for == Check_Type.Unknown) {
			/**
			 * Move might be Shape-Based
			 */
			if(house.getShape() == playtile.getShape()) {
				if(house.getColor() != playtile.getColor()) {
					if(already_checked.contains(house.getColor())) {
						return new NeighborshipCertificate(playtile, house, false);
					}
					else {
						checking_for = Check_Type.Shape;
						already_checked.add(house.getColor());
						if(!already_checked.contains(playtile.getColor())) {
							already_checked.add(playtile.getColor());
						}
					}
				}
				else {
					return new NeighborshipCertificate(playtile, house, false);
				}
			}
			/**
			 * Move might be Color-Based
			 */
			else if(house.getColor() == playtile.getColor()) {
				if(house.getShape() != playtile.getShape()) {
					if(already_checked.contains(house.getShape())) {
						return new NeighborshipCertificate(playtile, house, false);
					}
					else {
						checking_for = Check_Type.Color;
						already_checked.add(house.getShape());
						if(!already_checked.contains(playtile.getShape())) {
							already_checked.add(playtile.getShape());
						}
					}
				}
				else {
					return new NeighborshipCertificate(playtile, house, false);
				}
			}
			/**
			 * Move is none of the above, thus invalid
			 */
			else {
				return new NeighborshipCertificate(playtile, house, false);
			}
		}
		else {
			return new NeighborshipCertificate(playtile, house, false);
		}
//#########################################--Checking other Neighbors--#################################################
//######################################################################################################################
		
		TileOnPositionEX current_house = house;
		while(postman) {
			/**
			 * Letting the postman walk to the next neighbor, if that house exists
			 */
			if(current_house.getNeighbor(direction) != null) {
				current_house = current_house.getNeighbor(direction);
			}
			else {
				postman = false;
				continue;
			}
			/**
			 * Postman now delivers news to the new house, and they confirm or deny
			 */
			if(checking_for == Check_Type.Color) {
				if(current_house.getColor() == playtile.getColor()) {
					if(current_house.getShape() != playtile.getShape()) {
						if(already_checked.contains(current_house.getShape())) {
							return new NeighborshipCertificate(playtile, current_house, false);
						}
						else {
							already_checked.add(current_house.getShape());
							continue;
						}
					}
					else {
						return new NeighborshipCertificate(playtile, current_house, false);
					}
				}
				else {
					return new NeighborshipCertificate(playtile, current_house, false);
				}
			}
			if(checking_for == Check_Type.Shape) {
				if(current_house.getShape() == playtile.getShape()) {
					if(current_house.getColor() != playtile.getColor()) {
						if(already_checked.contains(current_house.getColor())) {
							return new NeighborshipCertificate(playtile, current_house, false);
						}
						else {
							already_checked.add(current_house.getColor());
							continue;
						}
					}
					else {
						return new NeighborshipCertificate(playtile, current_house, false);
					}
				}
				else {
					return new NeighborshipCertificate(playtile, current_house, false);
				}
			}
			
		}
		return new NeighborshipCertificate(playtile, true, score, checking_for, already_checked);
	
	}
}
