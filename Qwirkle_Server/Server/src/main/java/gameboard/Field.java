package gameboard;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import enumeration.Check_Type;
import enumeration.ControlParameters;
import enumeration.Direction;
import enumeration.Neighbor;
import gameValidation.CheckProcessObject;
import gameValidation.HandValidation;
import gameValidation.NeighborshipValidation;
import gameValidation.ScoringModule;
import gameValidation.TileOnPositionEX;
import logic_objects.HandObject;
import logic_objects.NeighborshipCertificate;
import logic_objects.NeighborshipObject;

/**
 * A class which is displaying the current field (for the KI)
 * 
 * @author Jan Sadowski, Maximilian Siegburg
 * 
 */

public class Field {
	Logger logger = LogManager.getLogger();
	ArrayList<TileOnPositionEX> TilesOnBoard;
	
	/**
	 * Modules needed for the validation process
	 */
	HandValidation handvalidation;
	NeighborshipValidation neighborshipValidation;
	ScoringModule scoringModule;
	
	/**
	 * Constructor of the Field
	 * Field NEEDS a x = 0, y = 0 Tile as Start Tile
	 * otherwise, the methods will not work!
	 * @param start_tile
	 */
	public Field(int penalty, int qwirkle) {
		TilesOnBoard = new ArrayList<>();
		this.handvalidation = new HandValidation();
		this.neighborshipValidation = new NeighborshipValidation();
		this.scoringModule = new ScoringModule(qwirkle, penalty);
		
		
	}
	
	public CheckProcessObject processMove(ArrayList<TileOnPositionEX> playtiles, ControlParameters control) {
		logger.info("--------- [FIELD] STARTING TO PROCESS MOVE.... -----------");
		Check_Type check_for = Check_Type.Unknown;
		Direction go_To = Direction.None;
		
		/**
		 * First, check if the move itself is valid
		 */
		HandObject hand_validation_certificate = handvalidation.validateHand(playtiles);
		if(hand_validation_certificate.isValidHand()) {
			logger.info("%%%%%%%% RESULT = " + hand_validation_certificate.getReason() + " %%%%%%%%%%%%");
			check_for = hand_validation_certificate.getMove_basis();
			go_To = hand_validation_certificate.getMovedirection();
		}
		else {
			logger.info("%%%%%%%% RESULT = " + hand_validation_certificate.getReason() + " %%%%%%%%%%%%");
			return new CheckProcessObject(false, scoringModule.getPenalty());
		}
		
		/**
		 * If Field is empty, do a seperate check
		 */
		if(TilesOnBoard.isEmpty()) {
			return emptyBoardCheck(playtiles, go_To, control);
		}
		/**
		 * Filter out unneccesary boardtiles
		 */
		ArrayList<TileOnPositionEX> valid_boardtiles = new ArrayList<>();
		if(go_To == Direction.None) {
			logger.info("FILTERING FIELDTILES AROUND : " + playtiles.get(0));
			/**
			 * This can only be reached, if there is only one Tile being played!
			 */
			TileOnPositionEX compareTo = playtiles.get(0);
			for(TileOnPositionEX boardtile : TilesOnBoard) {
				// Boardtile is already on that place, where the playtile was supposed to be placed
				if(boardtile.getX() == compareTo.getX() && boardtile.getY() == compareTo.getY()) {
					logger.info("FILTERING FAILED, THERE ALREADY IS A TILE ON THE PLAYS POSITION!");
					return new CheckProcessObject(false, scoringModule.getPenalty());
				}
				// Boardtile on the Left
				if(boardtile.getX() == compareTo.getX()-1 && boardtile.getY() == compareTo.getY()) {
					valid_boardtiles.add(boardtile);
					continue;
				}
				// Boardtile on the right
				if(boardtile.getX() == compareTo.getX()+ 1 && boardtile.getY() == compareTo.getY()) {
					valid_boardtiles.add(boardtile);
					continue;
				}
				// Boardtile ontop
				if(boardtile.getX() == compareTo.getX() && boardtile.getY() == compareTo.getY()-1) {
					valid_boardtiles.add(boardtile);
					continue;
				}
				// Boardtile below
				if(boardtile.getX() == compareTo.getX() && boardtile.getY() == compareTo.getY()+1) {
					valid_boardtiles.add(boardtile);
					continue;
				}
			}
			logger.info("FILTERING COMPLETED, TILES BEFORE :" + TilesOnBoard.size() + " TILES NOW : " + valid_boardtiles.size());
		}
		
		else if(go_To == Direction.Horizontal) {
			logger.info("FILTERING AROUND THE HORIZONTAL PLAIN Y in [" + (playtiles.get(0).getY()-1) + " | " + (playtiles.get(0).getY()+1) + ']' );
			/**
			 * This can only be reached, if there is only one Tile being played!
			 */
			TileOnPositionEX compareTo = playtiles.get(0);
			for(TileOnPositionEX boardtile : TilesOnBoard) {
				// Boardtile on same plain, check is needed
				if(boardtile.getY() == compareTo.getY()) {
					for(TileOnPositionEX playtile : playtiles) {
						if(boardtile.getX() == playtile.getX()) {
							logger.info("FILTERING FAILED, TILE : " + boardtile.toString() + " is already on position of Playtile :" + playtile.toString());
							return new CheckProcessObject(false, scoringModule.getPenalty());
						}
					}
					valid_boardtiles.add(boardtile);
				}
				//Boardtile above playtiles
				else if(boardtile.getY() == compareTo.getY()-1) {
					valid_boardtiles.add(boardtile);
				}
				//Boardtile below playtiles
				else if(boardtile.getY() == compareTo.getY() +1) {
					valid_boardtiles.add(boardtile);
				}
			}
			logger.info("FILTERING COMPLETED, TILES BEFORE :" + TilesOnBoard.size() + " TILES NOW : " + valid_boardtiles.size());
		}
		
		else if(go_To == Direction.Vertical) {
			logger.info("FILTERING AROUND THE VERTICAL PLAIN X in [" + (playtiles.get(0).getX()-1) + " | " + (playtiles.get(0).getX()+1) + ']' );
			/**
			 * This can only be reached, if there is only one Tile being played!
			 */
			TileOnPositionEX compareTo = playtiles.get(0);
			for(TileOnPositionEX boardtile : TilesOnBoard) {
				// Boardtile on same plain, check is needed
				if(boardtile.getX() == compareTo.getX()) {
					for(TileOnPositionEX playtile : playtiles) {
						if(boardtile.getY() == playtile.getY()) {
							logger.info("FILTERING FAILED, TILE : " + boardtile.toString() + " is already on position of Playtile :" + playtile.toString());
							return new CheckProcessObject(false, scoringModule.getPenalty());
						}
					}
					valid_boardtiles.add(boardtile);
				}
				//Boardtile left of playtiles
				else if(boardtile.getX() == compareTo.getX()-1) {
					valid_boardtiles.add(boardtile);
				}
				//Boardtile right of playtiles
				else if(boardtile.getX() == compareTo.getX() +1) {
					valid_boardtiles.add(boardtile);
				}
			}
			logger.info("FILTERING COMPLETED, TILES BEFORE :" + TilesOnBoard.size() + " TILES NOW : " + valid_boardtiles.size());
		}
		
		/**
		 * FILTERING HAS BEEN DONE, TIME TO FIND THE NEIGHBORS OF THE PLAYTILE, AND CHECK THEM
		 */
		
		// HASHMAP THAT IS VERY IMPORTANT
		// IT COLLECTS ALL CERTIFICATES, AND THEN CALCULATES THE SCORE OUT OF THEM LATER
		HashMap<TileOnPositionEX, ArrayList<NeighborshipCertificate>> certificate_collection = new HashMap<>();
		ArrayList<TileOnPositionEX> checked_playtiles = new ArrayList<>();
		
// ######################################################################################################### FOR - LOOP NEIGHBORSHIP [START]
		for(TileOnPositionEX playtile : playtiles) {
			NeighborshipObject check_result = neighborshipValidation.find_neighborhood(playtile, valid_boardtiles);
			if(check_result.isValid()) {
				/**
				 * Easy logging block
				 */
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"SUCCEEDED, NEIGHBORS OF PLAYTILE :" + playtile.toString() + " ARE");
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"-------------------------------------------------------");
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"TOP: " + check_result.getNeighbor(Neighbor.Top));
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"BOTTOM: " + check_result.getNeighbor(Neighbor.Bottom));
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"LEFT: " + check_result.getNeighbor(Neighbor.Left));
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"RIGHT: " + check_result.getNeighbor(Neighbor.Right));
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"-------------------------------------------------------");
				
				TileOnPositionEX current_playtile = check_result.getPlaytile();
				NeighborshipCertificate top_certificate = null;
				NeighborshipCertificate bottom_certificate = null;
				NeighborshipCertificate left_certificate = null;
				NeighborshipCertificate right_certificate = null;
				
				ArrayList<NeighborshipCertificate> certificate_subcollection = new ArrayList<>();
				ArrayList<Integer> horizontally_checked = new ArrayList<>();
				ArrayList<Integer> vertically_checked = new ArrayList<>();
				
				Check_Type horizontal_checktype = Check_Type.Unknown;
				Check_Type vertical_checktype = Check_Type.Unknown;
				if(go_To == Direction.Horizontal) {
					horizontal_checktype = check_for;
				}
				else if(go_To == Direction.Vertical) {
					vertical_checktype = check_for;
				}
				
				
				/**
				 * Since check was valid, time to evaluate the whole neighborhood
				 * if a neighbor is valid, tile gets pseudo connected to neighbor,
				 * but neighbor doesnt get connected to tile!
				 */
				
				/**
				 * Checking the Right neighbor of playtile, if he exists
				 */
				if(check_result.getNeighbor(Neighbor.Top) != null) {
					top_certificate = neighborshipValidation.Neighbor_approval(current_playtile, check_result.getNeighbor(Neighbor.Top), Neighbor.Top, vertical_checktype, vertically_checked);
					if(top_certificate.isValidity()) {
						certificate_subcollection.add(top_certificate);
						current_playtile.addNeighbor(check_result.getNeighbor(Neighbor.Top), Neighbor.Top);
						vertically_checked.addAll(top_certificate.getChecked_shapes_or_colors());
						if(vertical_checktype == Check_Type.Unknown) {
							vertical_checktype = top_certificate.getMove_basis();
						}
					}
					else {
						logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"THE NEIGHBOR : " + top_certificate.getFailed_neighbor() + " clashes with playtile : " + current_playtile + " invalid move, aborting");
						if(!checked_playtiles.isEmpty()) {
							for(TileOnPositionEX pt : checked_playtiles) {
								pt.neighbors.clear();
							}
						}
						current_playtile.neighbors.clear();
						return new CheckProcessObject(false, scoringModule.getPenalty());
					}
				}
				/**
				 * Checking the Bottom neighbor of playtile, if he exists
				 */
				if(check_result.getNeighbor(Neighbor.Bottom) != null) {
					bottom_certificate = neighborshipValidation.Neighbor_approval(current_playtile, check_result.getNeighbor(Neighbor.Bottom), Neighbor.Bottom, vertical_checktype, vertically_checked);
					if(bottom_certificate.isValidity()) {
						certificate_subcollection.add(bottom_certificate);
						current_playtile.addNeighbor(check_result.getNeighbor(Neighbor.Bottom), Neighbor.Bottom);
						vertically_checked.addAll(bottom_certificate.getChecked_shapes_or_colors());
						if(vertical_checktype == Check_Type.Unknown) {
							vertical_checktype = bottom_certificate.getMove_basis();
						}
					}
					else {
						logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"THE NEIGHBOR : " + bottom_certificate.getFailed_neighbor() + " clashes with playtile : " + current_playtile + " invalid move, aborting");
						if(!checked_playtiles.isEmpty()) {
							for(TileOnPositionEX pt : checked_playtiles) {
								pt.neighbors.clear();
							}
						}
						current_playtile.neighbors.clear();
						return new CheckProcessObject(false, scoringModule.getPenalty());
					}
				}
				/**
				 * Checking the Left neighbor of playtile, if he exists
				 */
				if(check_result.getNeighbor(Neighbor.Left) != null) {
					left_certificate = neighborshipValidation.Neighbor_approval(current_playtile, check_result.getNeighbor(Neighbor.Left), Neighbor.Left, horizontal_checktype, horizontally_checked);
					if(left_certificate.isValidity()) {
						certificate_subcollection.add(left_certificate);
						current_playtile.addNeighbor(check_result.getNeighbor(Neighbor.Left), Neighbor.Left);
						horizontally_checked.addAll(left_certificate.getChecked_shapes_or_colors());
						if(horizontal_checktype == Check_Type.Unknown) {
							horizontal_checktype = left_certificate.getMove_basis();
						}
					}
					else {
						logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"THE NEIGHBOR : " + left_certificate.getFailed_neighbor() + " clashes with playtile : " + current_playtile + " invalid move, aborting");
						if(!checked_playtiles.isEmpty()) {
							for(TileOnPositionEX pt : checked_playtiles) {
								pt.neighbors.clear();
							}
						}
						current_playtile.neighbors.clear();
						return new CheckProcessObject(false, scoringModule.getPenalty());
					}
				}
				/**
				 * Checking the Right neighbor of playtile, if he exists
				 */
				if(check_result.getNeighbor(Neighbor.Right) != null) {
					right_certificate = neighborshipValidation.Neighbor_approval(current_playtile, check_result.getNeighbor(Neighbor.Right), Neighbor.Right, horizontal_checktype, horizontally_checked);
					if(right_certificate.isValidity()) {
						certificate_subcollection.add(right_certificate);
						current_playtile.addNeighbor(check_result.getNeighbor(Neighbor.Right), Neighbor.Right);
						horizontally_checked.addAll(right_certificate.getChecked_shapes_or_colors());
						if(horizontal_checktype == Check_Type.Unknown) {
							horizontal_checktype = right_certificate.getMove_basis();
						}
					}
					else {
						logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"THE NEIGHBOR : " + right_certificate.getFailed_neighbor() + " clashes with playtile : " + current_playtile + " invalid move, aborting");
						if(!checked_playtiles.isEmpty()) {
							for(TileOnPositionEX pt : checked_playtiles) {
								pt.neighbors.clear();
							}
						}
						current_playtile.neighbors.clear();
						return new CheckProcessObject(false, scoringModule.getPenalty());
					}
				}
			
				
				/**
				 * Tile is now completely checked, and is ok
				 */
				checked_playtiles.add(current_playtile);
				valid_boardtiles.add(current_playtile);
				certificate_collection.put(current_playtile, certificate_subcollection);
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"NEIGHBORSHIP VALIDATION CHECKS OUT FOR TILE : " + current_playtile);
				
			}
			// Neighborship Validation failed
			else {
				if(check_result.getFail_neighbor() != check_result.getPlaytile()) {
				logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"FAILED, NEIGHBOR OF PLAYTILE :" + check_result.getFail_neighbor() + " ARE CLASHING WITH PLAYTILE : " + check_result.getPlaytile());
				}
				else {
					logger.log(Level.getLevel("NEIGHBORSHIPVALIDATION"),"FAILED, NO NEIGHBORS COULD BE FOUND FOR PLAYTILE : " + check_result.getPlaytile());
				}
				return new CheckProcessObject(false, scoringModule.getPenalty());
			}
		}
// ######################################################################################################### FOR - LOOP NEIGHBORSHIP [END]
		/**
		 * Check went through, and everything was valid, if this was a move made given by an Update
		 * Or later in the Server to recieve as a move, adding tiles and neighborships to the Field
		 */
		ArrayList<TileOnPositionEX> connected_playtiles = new ArrayList<>();
		for(TileOnPositionEX playtile : certificate_collection.keySet()) {
			if(playtile.getNeighbor(Neighbor.Top) != null) {
				playtile.getNeighbor(Neighbor.Top).addNeighbor(playtile, Neighbor.Bottom);
			}
			if(playtile.getNeighbor(Neighbor.Bottom) != null) {
				playtile.getNeighbor(Neighbor.Bottom).addNeighbor(playtile, Neighbor.Top);
			}
			if(playtile.getNeighbor(Neighbor.Left) != null) {
				playtile.getNeighbor(Neighbor.Left).addNeighbor(playtile, Neighbor.Right);
			}
			if(playtile.getNeighbor(Neighbor.Right) != null) {
				playtile.getNeighbor(Neighbor.Right).addNeighbor(playtile, Neighbor.Left);
			}
			connected_playtiles.add(playtile);
		}
		CheckProcessObject result = null;
		if(rowCheck(connected_playtiles, go_To)){
			if(control == ControlParameters.Server) {
				result = new CheckProcessObject(true, scoringModule.calculateScore(certificate_collection, go_To));
				TilesOnBoard.addAll(connected_playtiles);
			}
			else if(control == ControlParameters.ENGINE) {
				result = new CheckProcessObject(true, scoringModule.calculateScore(certificate_collection, go_To));
				for(TileOnPositionEX connectedTiles : connected_playtiles) {
					remove_neighborships(connectedTiles);
				}
			}
		}
		else {
			for(TileOnPositionEX connectedTiles : connected_playtiles) {
				remove_neighborships(connectedTiles);
			}
			result = new CheckProcessObject(false, scoringModule.getPenalty());
		}
		
		/**
		 * Returning the result of the process, if not already happend
		 */
		return result;
	}// PROCESS[END]
	
	
	/**
	 * Method that checks, if after a move tiles can see and reach eachother
	 * if so, neighborship has been completely validated
	 * @param playtiles
	 * @return
	 */
	public boolean rowCheck(ArrayList<TileOnPositionEX> playtiles, Direction direction) {
		logger.log(Level.getLevel("ROWCHECK"),"------- INITIATING ROWCHECK FOR TILES : " + playtiles + " -------" );
		// This is the tile, that all tiles are trying to reach
		// If they can, then they can reach eachother too
		TileOnPositionEX connectTo = playtiles.get(0);
		for(TileOnPositionEX playtile : playtiles) {
			boolean found = false;
			boolean top_checked = false;
			boolean bot_checked = false;
			boolean left_checked = false;
			boolean right_checked = false;
			TileOnPositionEX current_tile = playtile;
			while(!found) {
				if(connectTo.getUniqueId() == current_tile.getUniqueId()) {
					logger.log(Level.getLevel("ROWCHECK"),"FOUND A WAY FROM :" + playtile + " TO : " + current_tile + " CHECKING NEXT TILE");
					break;
				}
				// Testing is row reaches itself horizontally   <---------->
				if(direction == Direction.Horizontal) {
					if(!left_checked) {
						if(current_tile.getNeighbor(Neighbor.Left) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Left);
							continue;
						}
						else {
							left_checked = true;
							current_tile = playtile;
							continue;
						}
					}
					if(!right_checked) {
						if(current_tile.getNeighbor(Neighbor.Right) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Right);
							continue;
						}
						else {
							right_checked = true;
							current_tile = playtile;
							continue;
						}
					}
					logger.log(Level.getLevel("ROWCHECK"),"COULDN'T FIND A WAY FROM :" + playtile + " TO : " + current_tile + " ABORTING");
					logger.log(Level.getLevel("ROWCHECK"),"------- ROWCHECK CONCLUDED --------" );
					return false;
				}
				// Testing if row reaches itself vertically  ^^^^^^^^^^^^^vvvvvvvvv
				else if(direction == Direction.Vertical) {
					if(!top_checked) {
						if(current_tile.getNeighbor(Neighbor.Top) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Top);
							continue;
						}
						else {
							top_checked = true;
							current_tile = playtile;
							continue;
						}
					}
					if(!bot_checked) {
						if(current_tile.getNeighbor(Neighbor.Bottom) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Bottom);
							continue;
						}
						else {
							bot_checked = true;
							current_tile = playtile;
							continue;
						}
					}
					logger.log(Level.getLevel("ROWCHECK"),"COULDN'T FIND A WAY FROM :" + playtile + " TO : " + current_tile + " ABORTING");
					logger.log(Level.getLevel("ROWCHECK"),"------- ROWCHECK CONCLUDED --------" );
					return false;
				}
			}
		}
		logger.log(Level.getLevel("ROWCHECK"),"FOUND A WAY FOR ALL PLAYTILES, ROW IS CORRECT!");
		logger.log(Level.getLevel("ROWCHECK"),"------- ROWCHECK CONCLUDED --------" );
		return true;
	}
	
	/**
	 * This Method is used to check a play when there is no Tile on the field
	 * @param playtiles
	 * @param direction
	 * @param control
	 * @return
	 */
	public CheckProcessObject emptyBoardCheck(ArrayList<TileOnPositionEX> playtiles, Direction direction, ControlParameters control) {
		logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- INITIATING EMPTY FIELD VALIDATION WITH TILES : " + playtiles + " -------" );
		if(direction == Direction.None) {
			logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"ONLY ONE TILE WAS PLAYED" );
			if(playtiles.get(0).getX() == 0 && playtiles.get(0).getY() == 0) {
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"TILE WAS PLACED ON [0,0] CORRECTLY" );
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
				return new CheckProcessObject(true, 1);
			}
			else {
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"TILE WAS NOT PLACED ON [0,0], FAILURE" );
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
				return new CheckProcessObject(false, scoringModule.getPenalty());
			}
		}
		else {
			logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"MULTIPLE TILES WERE PLAYED, TRYING TO FIND STARTTILE" );
			
			int saved_size = playtiles.size();
			/**
			 * Trying to find [0,0] tile before continueing calculations
			 */
			TileOnPositionEX start_tile = null;
			for(TileOnPositionEX tile : playtiles) {
				if(tile.getX() == 0 && tile.getY() == 0) {
					start_tile = tile;
					playtiles.remove(tile);
					break;
				}
			}
			if(start_tile == null) {
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"COULDN'T FIND STARTTILE, ABORT" );
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
				return new CheckProcessObject(false, scoringModule.getPenalty());
			}
			logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"FOUND A STARTTILE : " + start_tile + " TRYING TO CONNECT ALL TILES " + direction + " TO IT NOW");
			/**
			 * Found a maintile, time to put them into a row
			 */
			int index = 0;
			ArrayList<TileOnPositionEX> tiles_connected = new ArrayList<>();
			tiles_connected.add(start_tile);
			TileOnPositionEX leftmost_tile = start_tile;
			TileOnPositionEX rightmost_tile = start_tile;
			TileOnPositionEX highest_tile = start_tile;
			TileOnPositionEX lowest_tile = start_tile;
			logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"Playtiles size : " + playtiles.size() );
			while(index < playtiles.size()) {
				TileOnPositionEX current_tile = playtiles.get(index);
				if(direction == Direction.Horizontal) {
					// current_tile is to the left of leftmost tile
					if(current_tile.getX() == leftmost_tile.getX()-1) {
						if(leftmost_tile.getNeighbor(Neighbor.Left) == null) {
							current_tile.addNeighbor(leftmost_tile, Neighbor.Right);
							leftmost_tile.addNeighbor(current_tile, Neighbor.Left);
							tiles_connected.add(current_tile);
							leftmost_tile = current_tile;
							playtiles.remove(index);
							index = 0;
							continue;
						}
					}
					// current_tile is to the right of rightmost tile
					if(current_tile.getX() == rightmost_tile.getX()+1) {
						if(rightmost_tile.getNeighbor(Neighbor.Right) == null) {
							current_tile.addNeighbor(rightmost_tile, Neighbor.Left);
							rightmost_tile.addNeighbor(current_tile, Neighbor.Right);
							tiles_connected.add(current_tile);
							rightmost_tile = current_tile;
							playtiles.remove(index);
							index = 0;
							continue;
						}
					}
					index ++;
					continue;
				}
				if(direction == Direction.Vertical) {
					// current_tile is above the highest tile
					if(current_tile.getY() == highest_tile.getY()-1) {
						if(highest_tile.getNeighbor(Neighbor.Top) == null) {
							current_tile.addNeighbor(highest_tile, Neighbor.Bottom);
							highest_tile.addNeighbor(current_tile, Neighbor.Top);
							tiles_connected.add(current_tile);
							highest_tile = current_tile;
							playtiles.remove(index);
							index = 0;
							continue;
						}
					}
					// current_tile is below the lowest tile
					if(current_tile.getY() == lowest_tile.getY()+1) {
						if(lowest_tile.getNeighbor(Neighbor.Bottom) == null) {
							current_tile.addNeighbor(lowest_tile, Neighbor.Top);
							lowest_tile.addNeighbor(current_tile, Neighbor.Bottom);
							tiles_connected.add(current_tile);
							lowest_tile = current_tile;
							playtiles.remove(index);
							index = 0;
							continue;
						}
					}
					index ++;
					continue;
				}
			}
		

			
			if(saved_size == tiles_connected.size()) {
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"SUCCESS, ALL TILES CONNECTED, TIME TO CHECK NEIGHBORSHIPS");
				if(rowCheck(tiles_connected, direction)) {
					if(control == ControlParameters.Server) {
						TilesOnBoard.addAll(tiles_connected);
						int score = tiles_connected.size();
						if(score == scoringModule.getQwirkle()) {
							score += scoringModule.getQwirkle();
						}
						logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"SUCCESS, NEIGHBORSHIPS ARE FINE, PUTTING TILES ON BOARD");
						logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
						return new CheckProcessObject(true,score);
					}
					else if(control == ControlParameters.ENGINE) {
						for(TileOnPositionEX connected : tiles_connected) {
							remove_neighborships(connected);
						}
						int score = tiles_connected.size();
						if(score == scoringModule.getQwirkle()) {
							score += scoringModule.getQwirkle();
						}
						logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"SUCCESS, NEIGHBORSHIPS ARE FINE, SINCE KI-MOVE REMOVING ALL NEIGHBORSHIPS");
						logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
						return new CheckProcessObject(true,score);
					}
				}
				else {
					logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"FAILURE, ROWCHECK FAILED");
					logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
					return new CheckProcessObject(false, scoringModule.getPenalty());
				}
			}
			else {
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"FAILURE, COULDN'T CONNECT ALL TILES, ABORTING");
				logger.log(Level.getLevel("EMPTYFIELDVALIDATION"),"------- EMPTY FIELD VALIDATION CONCLUDED --------" );
				return new CheckProcessObject(false, scoringModule.getPenalty());
			}
		}
		return null;
	}
	
	/**
	 * Used to remove newly added tiles that didn't pass
	 * @param tile
	 */
	public void remove_neighborships(TileOnPositionEX tile) {
		// Boardtile is left neighbor of tile
		if(tile.getNeighbor(Neighbor.Left) != null) {
			tile.getNeighbor(Neighbor.Left).removeNeighbor(Neighbor.Right);
			tile.removeNeighbor(Neighbor.Left);
		}
		// Boardtile is top neighbor of tile
		if(tile.getNeighbor(Neighbor.Top) != null) {
			tile.getNeighbor(Neighbor.Top).removeNeighbor(Neighbor.Bottom);
			tile.removeNeighbor(Neighbor.Top);
		}
		// Boardtile is bottom neighbor of tile
		if(tile.getNeighbor(Neighbor.Bottom) != null) {
			tile.getNeighbor(Neighbor.Bottom).removeNeighbor(Neighbor.Top);
			tile.removeNeighbor(Neighbor.Bottom);
		}
		// Boardtile is right neighbor of tile
		if(tile.getNeighbor(Neighbor.Right) != null) {
			tile.getNeighbor(Neighbor.Right).removeNeighbor(Neighbor.Left);
			tile.removeNeighbor(Neighbor.Right);
		}
	}
	/**
	 * Method for easy access to tile removal
	 * @param tile_to_remove
	 */
	public void remove_tile_on_board(TileOnPositionEX tile_to_remove) {
		for(TileOnPositionEX tile : TilesOnBoard) {
			if(tile.getUniqueId() == tile_to_remove.getUniqueId()) {
				remove_neighborships(tile);
				TilesOnBoard.remove(tile);
				break;
			}
		}
	}

	/**
	 * Method the easily get a Tile on the board
	 * @return 
	 * @return
	 */
	public TileOnPositionEX get_tile_on_board(TileOnPositionEX get_tile) {
		for(TileOnPositionEX tile : TilesOnBoard) {
			if(tile.getUniqueId() == get_tile.getUniqueId()) {
				return tile;
			}
		}
		return null;
	}
	public ArrayList<TileOnPositionEX> getTilesOnBoard() {
		return TilesOnBoard;
	}

	public void setTilesOnBoard(ArrayList<TileOnPositionEX> tilesOnBoard) {
		TilesOnBoard = tilesOnBoard;
	}

	public HandValidation getHandvalidation() {
		return handvalidation;
	}

	public void setHandvalidation(HandValidation handvalidation) {
		this.handvalidation = handvalidation;
	}

	public NeighborshipValidation getNeighborshipValidation() {
		return neighborshipValidation;
	}

	public void setNeighborshipValidation(NeighborshipValidation neighborshipValidation) {
		this.neighborshipValidation = neighborshipValidation;
	}

	public ScoringModule getScoringModule() {
		return scoringModule;
	}

	public void setScoringModule(ScoringModule scoringModule) {
		this.scoringModule = scoringModule;
	}
}
