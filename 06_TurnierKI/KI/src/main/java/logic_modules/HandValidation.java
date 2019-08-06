package logic_modules;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.TileOnPositionEX;
import enumeration.Check_Type;
import enumeration.Direction;
import logic_objects.HandObject;

public class HandValidation {
	Logger logger = LogManager.getLogger();
	
	/**
	 * Method validating the plain Tiles that are being played, without comparison to the board
	 * @param tiles
	 * @return
	 */
	public HandObject validateHand(ArrayList<TileOnPositionEX> tiles) {
		logger.log(Level.getLevel("HANDVALIDATION"), "------------- Initiating HANDVALIDATION ---------------");
		
		Direction move_direction = Direction.None;
		Check_Type move_basis = Check_Type.Unknown;
		
		/**
		 * Setting the Tile, to which to compare all the other tiles to
		 * and also variables to determine the move direction
		 */
		TileOnPositionEX compareTo = tiles.get(0);
		int colorToCompareTo = compareTo.getColor();
		int shapeToCompareTo = compareTo.getShape();
		boolean colorbased = false;
		boolean shapebased = false;
		boolean horizontal = false;
		boolean vertical = false;
		
		/**
		 * If played move has a Tile that has the same color and Shape
		 * as another Tile, return false
		 * [Max 12*12*2 = 288 checks]
		 */
		//logger.log(Level.getLevel("HANDVALIDATION"),"[COLOR & SHAPE EQUALITY CHECK] START");
		for(TileOnPositionEX top : tiles) {
			for(TileOnPositionEX top2 : tiles) {
				if(top != top2) {
					if(top.getColor() == top2.getColor() && top.getShape() == top2.getShape()) {
						logger.log(Level.getLevel("HANDVALIDATION"),"[COLOR & SHAPE CHECK] FAILURE");
						return new HandObject(null, null, false, "Tiles : " + top + " and " + top2 + " have the same color and shape!");
					}
				}
			}
		}
		//logger.log(Level.getLevel("HANDVALIDATION"),"[COLOR & SHAPE CHECK] SUCCESS");
		
		/**
		 * Checking, if move is either Color or Shape based
		 * [Max 1 + 2*11 = 23 checks]
		 */
		//logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] START");
		if(tiles.size() > 1) {
			//logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] MOVE HAS MORE THEN 1 TILE");
			for(TileOnPositionEX top : tiles) {
				if(top == compareTo) {
					continue; // Skipping first tile, since we compare to that
				}
				else {
					if(colorToCompareTo == top.getColor()) {
						colorbased = true;
						continue;
					}
					if(shapeToCompareTo == top.getShape()) {
						shapebased = true;
						continue;
					}
					// Move neither has the same shape or the same color, thus invalid
					logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] FAILURE");
					return new HandObject(null, null, false, "Tiles : " + top + " has neither the same color or shape as " + compareTo);
				}
			}
			
			// There is a mismatch in the move, either 2 different colors 
			if(colorbased == shapebased) {
				logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] FAILURE");
				return new HandObject(null, null, false, "Move has matching Tile-Shapes and Tile-Colors, which cant be!");

			}
			
			if(colorbased) {
				move_basis = Check_Type.Color;
				//logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] SUCCESS | MOVE BASED ON COLOR");
			}
			if(shapebased) {
				move_basis = Check_Type.Shape;
				//logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] SUCCESS | MOVE BASED ON SHAPE");
			}
			
			/**
			 * Comparing all played tiles to the first Tile,
			 * to find out, if play was vertical or horizonal
			 * and also confirming that no tile is played ontop of another tile
			 * [Max 12*12*3 = 432 checks]
			 */
			//logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] START");
			for(TileOnPositionEX thisTop : tiles) {
				for(TileOnPositionEX otherTop : tiles) {
					// Skipping over first item in the List
					if(thisTop.getUniqueId() == otherTop.getUniqueId()) {
						continue; // Skipping first tile, since we compare to that
					}
				
					if(thisTop.getX() == otherTop.getX()) {
						if(thisTop.getY() != otherTop.getY()) {
							vertical = true;
						}
						else {
							logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] FAILURE");
							return new HandObject(null, null, false, "Tiles: " + thisTop + " and " + otherTop + " are placed ontop of eachother!");
							// Tile must be on the same Position
						}
					}
					if(thisTop.getY() == otherTop.getY() ) {
						if(thisTop.getX() != otherTop.getX()) {
							horizontal = true;
						}
						else {
							logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] FAILURE");
							return new HandObject(null, null, false, "Tiles: " + thisTop + " and " + otherTop + " are placed ontop of eachother!");
							// Tile must be on the same Position
						}
					}
				}
			}
			// Either cannot be determined what move (Already caught in Loop)
			// Or there is a diagonal move, which is invalid
			if(horizontal == vertical) {
				logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] FAILURE");
				return new HandObject(null, null, false, "Move is played horizontal and vertical, invalid!");
			}
			// In total about ~700 Checks maximum per hand (with 12 tiles)
			if(horizontal) {
				//logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] SUCCESS | MOVE PLAYED HORIZONTAL");
				move_direction = Direction.Horizontal;
				return new HandObject(move_direction, move_basis, true, "The Hand itself is valid to use");
			}
			
			if(vertical) {
				//logger.log(Level.getLevel("HANDVALIDATION"),"[DIRECTION-TEST] SUCCESS | MOVE PLAYED VERTICAL");
				move_direction = Direction.Vertical;
				logger.log(Level.getLevel("HANDVALIDATION"),"--------------------[VALIDATING MOVE] COMPLETED--------------------");
				return new HandObject(move_direction, move_basis, true, "The Hand itself is valid to use");
			}
		}
		else {
			//logger.log(Level.getLevel("HANDVALIDATION"),"[MOVE-BASE] MOVE HAS JUST 1 TILE");
			logger.log(Level.getLevel("HANDVALIDATION"),"--------------------[VALIDATING MOVE] COMPLETED--------------------");
			return new HandObject(move_direction, move_basis, true, "The Hand itself is valid to use");
		}
		logger.log(Level.getLevel("HANDVALIDATION"),"SOMETHING WENT TERRIBLY WRONG!");
		return new HandObject(move_direction, move_basis, true, "SOMETHING WENT HORRIFICLY WRONG!");
		
	}
}
