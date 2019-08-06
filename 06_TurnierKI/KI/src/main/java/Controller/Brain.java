package Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TreeMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.ENGINE_Client;
import Model.Field;
import Model.TileOnPositionEX;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.PlayTiles;
import de.upb.swtpra1819interface.messages.TileSwapRequest;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import enumeration.Check_Type;
import enumeration.ControlParameters;
import enumeration.Direction;
import logic_objects.CheckProcessObject;

public class Brain {
	
	/**
	 * Controlvariables
	 */
	Logger logger = LogManager.getLogger();
	ArrayList<Tile> handtiles = new ArrayList<Tile>();
	Timer timer = new Timer(true);
	
	/**
	 * Variables keeping track of the Current best move to play
	 */
	ArrayList<TileOnPositionEX> current_best_move = new ArrayList<>();
	ArrayList<Tile> used_hand_tiles = new ArrayList<>();
	int current_best_points = 0;
	
	/**
	 * Models
	 */
	ENGINE_Client client;
	Field field;
	Message message_to_send = null;
	
	
	public Brain(ENGINE_Client client, Field field) {
		this.client = client;
		this.field = field;
	}
	
	/**
	 * Removes the handtile from the Hand and adds it as a tile in use
	 * @param tile
	 */
	public void use_Tile_for_Move(Tile tile) {
		Iterator<Tile> handit = handtiles.iterator();
		while(handit.hasNext()) {
			Tile handtile = handit.next();
			if(handtile.getUniqueId() == tile.getUniqueId()) {
				handit.remove();
				used_hand_tiles.add(handtile);
				break;
			}
		}
	}
	
	public void calculateMove() {
		Thread calcThread = new Thread("MOVE CALCULATION THREAD") {
			
			public void run() {
				logger.info("-------------------[CALCULATING] INITIATING MOVE CALCULUS -------------------");
				logger.info("MY HAND TILES : " + handtiles );
				current_best_points = 0;
				used_hand_tiles = new ArrayList<>();
				current_best_move = new ArrayList<>();
				try {
					sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/**
				 * Selecting most optimal amount of Start-Tiles, since Field is empty
				 */
				if(field.getTilesOnBoard().isEmpty()) {
					//logger.info("[CALCULATING] FIELD IS EMPTY || CALCULATING OPTIMAL MOVE FOR FIELD");
					ArrayList<TileOnPosition> best_move = emptyFieldMove();
					for(TileOnPosition tile_to_play : best_move) {
						current_best_move.add(new TileOnPositionEX(tile_to_play.getCoordX(), tile_to_play.getCoordY(), tile_to_play.getTile()));
					}
					client.sendMessage(new PlayTiles(best_move));
					return;
				}
				/**
				 * Calculating the next move
				 */
				else {
					//logger.info("[CALCULATING] FIELD HAS TILES ON IT || CALCULATING OPTIMAL FIRST TILE TO PLACE ON FIELD");
					TileOnPositionEX firstTile = calcOptFirstTile();
					if(firstTile == null) {
						client.sendMessage(new TileSwapRequest(handtiles));
						return;
					}
					current_best_move.add(firstTile);
					logger.info("[CALCULATING] FOUND A OPTIMAL FIRST TILE : " + firstTile);
					/**
					 * Current handtiles cannot make a valid move, swap all of them out
					 */
					if(current_best_points <= 0) {
						//converting the tiles into the SWTPRA convention
						client.sendMessage(new TileSwapRequest(handtiles));
						logger.info("[CALCULATING] CURRENT HAND CANNOT BE PLACED ON THE BOARD || TILESWAPPING");
						return;
					}
					else {
						logger.info("[CALCULATING] FOUND A TILE THAT CAN BE PLACED ON THE BOARD || CALCULATING OPTIMAL FOLLOW-UP TILES TO PLACE ON FIELD");
						
						use_Tile_for_Move(firstTile.getTile());
						boolean no_better_follow_ups = false;
						boolean horizontal = false;
						boolean vertical = false;
						/**
						 * Loop that determines best followup sequence
						 */
						while(!no_better_follow_ups) {
								
							if(horizontal) {
								ArrayList<TileOnPositionEX> new_best_move = calcOptFollowUp(current_best_move, Direction.Horizontal);
								// No tiles have been added to the move, so must still be current_best_move
								logger.info("[CALCULATING] RESULT OF THE FOLLOWUP CALC @HORI : " + new_best_move);
								if(new_best_move.size() == current_best_move.size()) {
									ArrayList<TileOnPosition> send_to_server = new ArrayList<>();
									//converting the tiles into the SWTPRA convention
									for(TileOnPositionEX tile_to_play : current_best_move) {
										send_to_server.add(tile_to_play.toTileOnPosition());
									}
									client.sendMessage(new PlayTiles(send_to_server));
									logger.info("[CALCULATING] COUDLN'T FIND ANOTHER FOLLOWUP TILE @HORIZONTAL || CURRENT MOVESIZE : " + current_best_move.size() + " CURRENT MAXPOINTS : " + current_best_points);
									no_better_follow_ups = true;
									continue;
								}
								else {
									current_best_move = new_best_move;
									for(TileOnPositionEX new_playtile : current_best_move) {
										boolean is_in = false;
										for(Tile used_tiles : used_hand_tiles) {
											if(new_playtile.getUniqueId() == used_tiles.getUniqueId()) {
												is_in = true;
												break;
											}
										}
										if(!is_in) {
											use_Tile_for_Move(new_playtile.getTile());
										}
									}
									logger.info("[CALCULATING] FOUND ANOTHER FOLLOWUP TILE @HORIZONTAL || CURRENT MOVESIZE : " + current_best_move.size() + " CURRENT MAXPOINTS : " + current_best_points);
									logger.info("[CALCULATING] CURRENT BEST MOVE TILES : " + current_best_move);
									continue;
								}		
							}
							else if(vertical) {
								ArrayList<TileOnPositionEX> new_best_move = calcOptFollowUp(current_best_move, Direction.Vertical);
								// No tiles have been added to the move, so must still be current_best_move
								logger.info("[CALCULATING] RESULT OF THE FOLLOWUP CALC @VERT : " + new_best_move);
								if(new_best_move.size() == current_best_move.size()) {
									ArrayList<TileOnPosition> send_to_server = new ArrayList<>();
									//converting the tiles into the SWTPRA convention
									for(TileOnPositionEX tile_to_play : current_best_move) {
										send_to_server.add(tile_to_play.toTileOnPosition());
									}
									client.sendMessage(new PlayTiles(send_to_server));
									logger.info("[CALCULATING] COUDLN'T FIND ANOTHER FOLLOWUP TILE @VERTICAL || CURRENT MOVESIZE : " + current_best_move.size() + " CURRENT MAXPOINTS : " + current_best_points);
									no_better_follow_ups = true;
									continue;
								}
								else {
									current_best_move = new_best_move;
									for(TileOnPositionEX new_playtile : current_best_move) {
										boolean is_in = false;
										for(Tile used_tiles : used_hand_tiles) {
											if(new_playtile.getUniqueId() == used_tiles.getUniqueId()) {
												is_in = true;
												break;
											}
										}
										if(!is_in) {
											use_Tile_for_Move(new_playtile.getTile());
										}
									}
								logger.info("[CALCULATING] FOUND ANOTHER FOLLOWUP TILE @VERTICAL || CURRENT MOVESIZE : " + current_best_move.size() + " CURRENT MAXPOINTS : " + current_best_points);
								logger.info("[CALCULATING] CURRENT BEST MOVE TILES : " + current_best_move);
								continue;
								}	
							}
							// No follow-up move has been made
							else {
									ArrayList<TileOnPositionEX> new_best_move = calcOptFollowUp(current_best_move, Direction.None);
									// No tiles have been added to the move, so must still be current_best_move
									logger.info("[CALCULATING] RESULT OF THE FOLLOWUP CALC @SINGULAR : " + new_best_move);
									if(new_best_move.size() == current_best_move.size()) {
										ArrayList<TileOnPosition> send_to_server = new ArrayList<>();
										//converting the tiles into the SWTPRA convention
										for(TileOnPositionEX tile_to_play : current_best_move) {
											send_to_server.add(tile_to_play.toTileOnPosition());
										}
										client.sendMessage(new PlayTiles(send_to_server));
										logger.info("[CALCULATING] COUDLN'T FIND ANOTHER FOLLOWUP TILE @HORIZONTAL || CURRENT MOVESIZE : " + current_best_move.size() + " CURRENT MAXPOINTS : " + current_best_points);
										no_better_follow_ups = true;
										continue;
									}
									// Tile has been added, better move has been made
									else {
										current_best_move = new_best_move;
										TileOnPositionEX first_tile = current_best_move.get(0);
										TileOnPositionEX second_tile = current_best_move.get(1);
										
										// Determining if move has to be played horizontal, vertical, shapebased, colorbased
										// Vertical move has been made
										if(first_tile.getX() == second_tile.getX() && first_tile.getY() != second_tile.getY()) {
											vertical = true;
											if(first_tile.getColor() == second_tile.getColor() && first_tile.getShape() != second_tile.getShape()) {
												logger.info("[CALCULATING] FOUND A FOLLOWUP TILE || ALL COMING TILES HAVE TO BE PLAYED VERTICAL AND COLORBASED");
											}
											else if (first_tile.getColor() != second_tile.getColor() && first_tile.getShape() == second_tile.getShape()) {
												logger.info("[CALCULATING] FOUND A FOLLOWUP TILE || ALL COMING TILES HAVE TO BE PLAYED VERTICAL AND SHAPEBASED");
											}
											else {
												logger.info("[CALCULATING] SOMETHING WENT HORRIFICLY WRONG @VERTICAL, ABORT!!");
												return;
											}
											use_Tile_for_Move(second_tile.getTile());
										}
										// Horizontal move has been made
										else if(first_tile.getX() != second_tile.getX() && first_tile.getY() == second_tile.getY()) {
											horizontal = true;
											if(first_tile.getColor() == second_tile.getColor() && first_tile.getShape() != second_tile.getShape()) {
												logger.info("[CALCULATING] FOUND A FOLLOWUP TILE || ALL COMING TILES HAVE TO BE PLAYED HORIZONTAL AND COLORBASED");
											}
											else if (first_tile.getColor() != second_tile.getColor() && first_tile.getShape() == second_tile.getShape()) {
												logger.info("[CALCULATING] FOUND A FOLLOWUP TILE || ALL COMING TILES HAVE TO BE PLAYED HORIZONTAL AND SHAPEBASED");
											}
											else {
												logger.info("[CALCULATING] SOMETHING WENT HORRIFICLY WRONG @HORIZONTAL, ABORT!!");
												return;
											}
											use_Tile_for_Move(second_tile.getTile());
										}
										else {
											logger.info("[CALCULATING] SOMETHING WENT HORRIFICLY WRONG, COULDNT NOT EVEN PLACE A MOVE-DIRECTION ABORT!!");
											return;
										}		
									}
								}
							}
						return;//for
					}
				}//else
			}
		};
		calcThread.start();
		logger.info("[CALCULATING] CALC THREAD DONE CALCULATING");
		return;
	}
	
	/**
	 * This Method calculates the most optimal Assortment of tiles to place as a first move
	 * @return ArrayList<TileOnPosition> with the most optimal move
	 */
	public ArrayList<TileOnPosition> emptyFieldMove() {
		logger.info("-------------------[CALCULATING] START FIRST-MOVE CALCULUS-------------------");
		/**
		 * Comparing amount of different shapes with distinct colors to
		 * overall colors with distinct shapes
		 */
		//logger.info("[CALCULATING] COMPARING SHAPEAMOUNT TO COLORS");
		TreeMap<Integer, ArrayList<Tile>> colormapping = new TreeMap<>(); // HashMap with keys : Score , values : Color
		TreeMap<Integer, ArrayList<Tile>> shapeMapping = new TreeMap<>(); // HashMap with key : Score, value : Shape
		
		// ########################################### COLOR LOOP [START]
		for(Tile handtile_toCompare : handtiles) {
			ArrayList<Integer> shapes_checked = new ArrayList<>();
			ArrayList<Tile> move = new ArrayList<Tile>();
			int score = 1;
			shapes_checked.add(handtile_toCompare.getShape());
			move.add(handtile_toCompare);
			Iterator<Tile> handit = handtiles.iterator();
			
			while(handit.hasNext()) {
				Tile currentTile = handit.next();
				int color = currentTile.getColor();
				if(handtile_toCompare.getColor() == color) {
					boolean is_in = false;
					Iterator<Integer> checkedit = shapes_checked.iterator();
					while(checkedit.hasNext()) {
						if (checkedit.next() == currentTile.getShape()) {
							is_in = true;
							break;
						}
					}
					if(!is_in) {
						score += 1;
						shapes_checked.add(currentTile.getShape());
						move.add(currentTile);								
						continue;
					}
				}
				if(!handit.hasNext()) {
					//logger.info("[CALCULATING] ADDING A RESULT TO THE COLORMAP -> { SCORE:"  + score + " | MOVE: " + move + " }");
					if(score == field.getScoringModule().getQwirkle()) {
						score += field.getScoringModule().getQwirkle();
					}
					colormapping.put(score, move);
				}
			}
		}		
		// ########################################### COLOR LOOP [END]
		
		// ########################################### SHAPE LOOP [START]
		for(Tile handtile_toCompare : handtiles) {
			ArrayList<Integer> colors_checked = new ArrayList<>();
			ArrayList<Tile> move = new ArrayList<Tile>();
			int score = 1;
			colors_checked.add(handtile_toCompare.getColor());
			move.add(handtile_toCompare);
			Iterator<Tile> handit = handtiles.iterator();
			
			while(handit.hasNext()) {
				Tile currentTile = handit.next();
				int shape = currentTile.getShape();
				if(handtile_toCompare.getShape() == shape) {
					boolean is_in = false;
					Iterator<Integer> checkedit = colors_checked.iterator();
					while(checkedit.hasNext()) {
						if (checkedit.next() == currentTile.getColor()) {
							is_in = true;
							break;
						}
					}
					if(!is_in) {
						score += 1;
						colors_checked.add(currentTile.getShape());
						move.add(currentTile);								
						continue;
					}
				}
				if(!handit.hasNext()) {
					//logger.info("[CALCULATING] ADDING A RESULT TO THE SHAPEMAP -> { SCORE:"  + score + " | MOVE: " + move + " }");
					if(score == field.getScoringModule().getQwirkle()) {
						score += field.getScoringModule().getQwirkle();
					}
					shapeMapping.put(score, move);
				}
			}
		}	
		// ########################################### SHAPE LOOP [END]
		// colors are the better option
		if(colormapping.lastEntry().getKey() > shapeMapping.lastEntry().getKey()) {
			logger.info("[CALCULATING] THE MOST OPTIMAL MOVE IS A COLOR BASED MOVE OF SIZE : " + colormapping.lastEntry().getKey() + " THE MOVE :" + colormapping.lastEntry().getValue() );
			// Randomizing move to make it move flashy
			Random rand = new Random();
			int nxt = rand.nextInt(2); // [0,2)
			ArrayList<TileOnPosition> MoveToPlay = new ArrayList<>();
			// Doing a vertical move
			if(nxt == 0) {
				//logger.info("[CALCULATING] PUTTING IT DOWN VERTICALLY.....");
				int x = 0;
				int y = 0;
				for(Tile tile : colormapping.lastEntry().getValue()) {
					MoveToPlay.add(new TileOnPosition(x, y, tile));
					y ++;
				}
				logger.info("-------------------[CALCULATING] FINISHED FIRST-MOVE CALCULUS-------------------");
				return MoveToPlay;
			}
			// Doing Horizontal move
			if(nxt == 1) {
				//logger.info("[CALCULATING] PUTTING IT DOWN HORIZONTALLY.....");
				int x = 0;
				int y = 0;
				for(Tile tile : colormapping.lastEntry().getValue()) {
					MoveToPlay.add(new TileOnPosition(x, y, tile));
					x ++;
				}
				logger.info("-------------------[CALCULATING] FINISHED FIRST-MOVE CALCULUS-------------------");
				return MoveToPlay;
			}
		}
		// shapes are equal or better in score compared to colors
		else {
			logger.info("[CALCULATING] THE MOST OPTIMAL MOVE IS A SHAPE BASED MOVE OF SIZE : " + shapeMapping.lastEntry().getKey() + " THE MOVE :" + shapeMapping.lastEntry().getValue() );
			// Randomizing move to make it move flashy
			Random rand = new Random();
			int nxt = rand.nextInt(1);
			ArrayList<TileOnPosition> MoveToPlay = new ArrayList<>();
			// Doing a vertical move
			if(nxt == 0) {
				//logger.info("[CALCULATING] PUTTING IT DOWN VERTICALLY.....");
				int x = 0;
				int y = 0;
				for(Tile tile : shapeMapping.lastEntry().getValue()) {
					MoveToPlay.add(new TileOnPosition(x, y, tile));
					y ++;
				}
				logger.info("-------------------[CALCULATING] FINISHED FIRST-MOVE CALCULUS-------------------");
				return MoveToPlay;
			}
			// Doing Horizontal move
			if(nxt == 1) {
				//logger.info("[CALCULATING] PUTTING IT DOWN HORIZONTALLY.....");
				int x = 0;
				int y = 0;
				for(Tile tile : shapeMapping.lastEntry().getValue()) {
					MoveToPlay.add(new TileOnPosition(x, y, tile));
					x ++;
				}
				logger.info("-------------------[CALCULATING] FINISHED FIRST-MOVE CALCULUS-------------------");
				return MoveToPlay;
			}
		}
		return null; // Cannot get into the State
	}
	
	/**
	 * Method that determines the Most efficient single tile to be put ontop of the Board
	 */
	public TileOnPositionEX calcOptFirstTile() {
		logger.info("-------------------[CALCULATING] START OPTIMAL FIRST TILE CALCULUS-------------------");
		/**
		 * Calculating for every Handtile, where it can or cannot be placed,
		 * then calculating the most optimal score for this specific boardtile
		 * Afterwards comparing all boardtiles with one another, then adding new boardtiles to those tiles
		 */
		TreeMap<Integer, TileOnPositionEX> move_TreeMap = new TreeMap<>(); // This treemap compares all the best moves for every tile to oneanother
		int index = 0;
		
		for(Tile handtile : handtiles) {
			logger.info("%%%%%%%%%%%% [CALCULATING] EVALUATING TILE IN HAND ON POSITION: " + (index+1) + " %%%%%%%%%%%%%%%%%%");
			/**
			 * Determining Boardtiles that can be placed next to
			 */
			ArrayList<TileOnPositionEX> boardtiles_in_consideration = new ArrayList<>();
			
			for (TileOnPositionEX boardtile : field.getTilesOnBoard()) {
				if(handtile.getColor() == boardtile.getColor() && handtile.getShape() != boardtile.getShape()) {
					boardtiles_in_consideration.add(boardtile);
				}
				if(handtile.getShape() == boardtile.getShape() && handtile.getColor() != boardtile.getColor()) {
					boardtiles_in_consideration.add(boardtile);
				}
			}
			
			/**
			 * Calculating the most optimal placement for this one Tile 
			 */
			TreeMap<Integer, TileOnPositionEX> most_efficient_for_specific_board_tiles = new TreeMap<>(); // This treemap compares all the Moves of the current Tile on the whole board compared to one another
			for(TileOnPositionEX validboardtile : boardtiles_in_consideration) {
				// TreeMap to evaluate highest key = highest score
				TreeMap<Integer, TileOnPositionEX> evaluating_tree = new TreeMap<>();// This Treemap compares all the Moves around one tile to one another
				// Making an arraylist to put into the Validation
				ArrayList<TileOnPositionEX> try_on_board = new ArrayList<>();
				
				// Making the Tiles to try a move
				TileOnPositionEX topMove = new TileOnPositionEX(validboardtile.getX(), validboardtile.getY()+1, handtile);
				TileOnPositionEX rightMove = new TileOnPositionEX(validboardtile.getX()+1, validboardtile.getY(), handtile);
				TileOnPositionEX leftMove = new TileOnPositionEX(validboardtile.getX()-1, validboardtile.getY(), handtile);
				TileOnPositionEX bottomMove = new TileOnPositionEX(validboardtile.getX(), validboardtile.getY()-1, handtile);
				
				// Trying the right of the boardtile
				try_on_board.add(rightMove);
				CheckProcessObject result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					evaluating_tree.put(result.getScore(), rightMove);
				}
				try_on_board.clear();
				
				// Trying the left of the boardtile
				try_on_board.add(leftMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					evaluating_tree.put(result.getScore(), leftMove);
				}
				try_on_board.clear();
				
				// Trying the top of the boardtile
				try_on_board.add(topMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					evaluating_tree.put(result.getScore(), topMove);
				}
				try_on_board.clear();
				
				// Trying the bottom of the boardtile
				try_on_board.add(bottomMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					evaluating_tree.put(result.getScore(), bottomMove);
				}
				try_on_board.clear();
				
				if(!evaluating_tree.isEmpty()) {
					logger.info("------------- ADDED NEW ENTRY : { SCORE: " + evaluating_tree.lastEntry().getKey() + " || COORDINATES: [" + evaluating_tree.lastEntry().getValue().getX() + " | " + evaluating_tree.lastEntry().getValue().getY() + "] -------------------");
					most_efficient_for_specific_board_tiles.put(evaluating_tree.lastEntry().getKey(), evaluating_tree.lastEntry().getValue());
				}
			}
			if(!most_efficient_for_specific_board_tiles.isEmpty()) {
				move_TreeMap.put(most_efficient_for_specific_board_tiles.lastEntry().getKey(), most_efficient_for_specific_board_tiles.lastEntry().getValue());
			}
			index ++;
		}
		if(!move_TreeMap.isEmpty()) {
		logger.info("EVALUATED BEST TILE TO PLACE : " + move_TreeMap.lastEntry().getValue() + " @[" + move_TreeMap.lastEntry().getValue().getX() + " | " + move_TreeMap.lastEntry().getValue().getY() + "]  WHICH WILL SCORE :" + move_TreeMap.lastEntry().getKey() + " POINTS");
		logger.info("-------------------[CALCULATING] END OPTIMAL FIRST TILE CALCULUS-------------------");
		current_best_points = move_TreeMap.lastEntry().getKey();
		return move_TreeMap.lastEntry().getValue();
		}
		else {
			logger.info("THERE WAS NO BEST TILE, IT COULDN'T GET PLACED ANYWHERE");
			logger.info("-------------------[CALCULATING] END OPTIMAL FIRST TILE CALCULUS-------------------");
			return null;
		}
	}
	
	/**
	 * Calculates the followup move based on the tiles already placed
	 * @param tiles THE CURRENT_BEST_MOVE TILES
	 * @return 
	 */
	public ArrayList<TileOnPositionEX> calcOptFollowUp(ArrayList<TileOnPositionEX> tiles, Direction direction) {
		// Only one tile has been put
		if(direction == Direction.None) {
			TreeMap<Integer, ArrayList<TileOnPositionEX>> most_optimal_addition_tree = new TreeMap<>();
			most_optimal_addition_tree.put(current_best_points, tiles);
			
			for(Tile tile : handtiles) {
				for(TileOnPositionEX solid_tile : tiles) {
				// Setting up Data_Models to Store and Use data
				TreeMap<Integer, ArrayList<TileOnPositionEX>> evaluating_tree = new TreeMap<>();
				ArrayList<TileOnPositionEX> try_on_board = new ArrayList<>();
				
				// Setting up the Array with the move
				try_on_board.addAll(tiles);
				
				TileOnPositionEX topMove = new TileOnPositionEX(solid_tile.getX(), solid_tile.getY() + 1, tile);
				TileOnPositionEX bottomMove = new TileOnPositionEX(solid_tile.getX(), solid_tile.getY() - 1, tile);
				TileOnPositionEX rightMove = new TileOnPositionEX(solid_tile.getX() + 1, solid_tile.getY(), tile);
				TileOnPositionEX leftMove = new TileOnPositionEX(solid_tile.getX() - 1, solid_tile.getY() , tile);
				
				// Trying the right of the boardtile
				try_on_board.add(rightMove);
				CheckProcessObject result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					ArrayList<TileOnPositionEX> valid_right_move = new ArrayList<>();
					valid_right_move.addAll(try_on_board);
					evaluating_tree.put(result.getScore(), valid_right_move);
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				// Trying the left of the boardtile
				try_on_board.add(leftMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					ArrayList<TileOnPositionEX> valid_left_move = new ArrayList<>();
					valid_left_move.addAll(try_on_board);
					evaluating_tree.put(result.getScore(), valid_left_move);
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				// Trying the top of the boardtile
				try_on_board.add(topMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					ArrayList<TileOnPositionEX> valid_top_move = new ArrayList<>();
					valid_top_move.addAll(try_on_board);
					evaluating_tree.put(result.getScore(), valid_top_move);
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				// Trying the bottom of the boardtile
				try_on_board.add(bottomMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					ArrayList<TileOnPositionEX> valid_bottom_move = new ArrayList<>();
					valid_bottom_move.addAll(try_on_board);
					evaluating_tree.put(result.getScore(), valid_bottom_move);
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				if(!evaluating_tree.isEmpty()) {
					most_optimal_addition_tree.put(evaluating_tree.lastEntry().getKey(), evaluating_tree.lastEntry().getValue());
				}
				}
			}
			current_best_points = most_optimal_addition_tree.lastEntry().getKey();
			return most_optimal_addition_tree.lastEntry().getValue();
			
		}
	
		if(direction == Direction.Horizontal) {
			TreeMap<Integer, ArrayList<TileOnPositionEX>> most_optimal_addition_tree = new TreeMap<>();
			most_optimal_addition_tree.put(current_best_points, tiles);
			TileOnPositionEX leftmost_solid_tile = null;
			TileOnPositionEX rightmost_solid_tile = null;
			
			int tracker = 0;
			// Finding the left-most tile
			for(TileOnPositionEX playtile : tiles) {
				if(leftmost_solid_tile == null) {
					leftmost_solid_tile = playtile;
					tracker = leftmost_solid_tile.getX();
					continue;
				}
				if(playtile.getX() < tracker) {
					leftmost_solid_tile = playtile;
					tracker = leftmost_solid_tile.getX();
					continue;
				}
			}
			
			// Finding the right-most tile
			for(TileOnPositionEX playtile : tiles) {
				if(rightmost_solid_tile == null) {
					rightmost_solid_tile = playtile;
					tracker = rightmost_solid_tile.getX();
					continue;
				}
				if(playtile.getX() > tracker) {
					rightmost_solid_tile = playtile;
					tracker = rightmost_solid_tile.getX();
					continue;
				}
			}
			
			ArrayList<TileOnPositionEX> try_on_board = new ArrayList<>();
			try_on_board.addAll(tiles);
			int right_score = 0;
			int left_score = 0;
			
			for(Tile tile : handtiles) {
				ArrayList<TileOnPositionEX> valid_left_move = new ArrayList<>();
				ArrayList<TileOnPositionEX> valid_right_move = new ArrayList<>();
				// Setting up Data_Models to Store and Use data
				TileOnPositionEX rightMove = new TileOnPositionEX(rightmost_solid_tile.getX() + 1, rightmost_solid_tile.getY(), tile);
				TileOnPositionEX leftMove = new TileOnPositionEX(leftmost_solid_tile.getX() - 1, leftmost_solid_tile.getY(), tile);
				
				// Trying the right of the boardtile
				try_on_board.add(rightMove);
				
				CheckProcessObject result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					valid_right_move.addAll(try_on_board);
					right_score = result.getScore();
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				// Trying the left of the boardtile
				try_on_board.add(leftMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					valid_left_move.addAll(try_on_board);
					left_score = result.getScore();
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				if (valid_left_move.isEmpty() && valid_right_move.isEmpty()) {
					continue;
				}
				else {
					if(right_score > left_score) {
						most_optimal_addition_tree.put(right_score, valid_right_move);
					}else {
						most_optimal_addition_tree.put(left_score, valid_left_move);
					}
				}
				
				
			}
			current_best_points = most_optimal_addition_tree.lastEntry().getKey();
			return most_optimal_addition_tree.lastEntry().getValue();
			
		}
		if(direction == Direction.Vertical) {
			TreeMap<Integer, ArrayList<TileOnPositionEX>> most_optimal_addition_tree = new TreeMap<>();
			most_optimal_addition_tree.put(current_best_points, tiles);
			TileOnPositionEX Upmost_solid_tile = null;
			TileOnPositionEX bottommost_solid_tile = null;
			
			int tracker = 0;
			// Finding the highest tile
			for(TileOnPositionEX playtile : tiles) {
				if(Upmost_solid_tile == null) {
					Upmost_solid_tile = playtile;
					tracker = Upmost_solid_tile.getY();
					continue;
				}
				if(playtile.getY() > tracker) {
					Upmost_solid_tile = playtile;
					tracker = Upmost_solid_tile.getY();
					continue;
				}
			}
			
			// Finding the lowest tile
			for(TileOnPositionEX playtile : tiles) {
				if(bottommost_solid_tile == null) {
					bottommost_solid_tile = playtile;
					tracker = bottommost_solid_tile.getY();
					continue;
				}
				if(playtile.getY() < tracker) {
					bottommost_solid_tile = playtile;
					tracker = bottommost_solid_tile.getY();
					continue;
				}
			}
			
			ArrayList<TileOnPositionEX> try_on_board = new ArrayList<>();
			try_on_board.addAll(tiles);
			int top_score = 0;
			int bottom_score = 0;
			
			for(Tile tile : handtiles) {
				ArrayList<TileOnPositionEX> valid_top_move = new ArrayList<>();
				ArrayList<TileOnPositionEX> valid_bottom_move = new ArrayList<>();
				// Setting up Data_Models to Store and Use data
				TileOnPositionEX topMove = new TileOnPositionEX(Upmost_solid_tile.getX(), Upmost_solid_tile.getY()+1, tile);
				TileOnPositionEX bottomMove = new TileOnPositionEX(bottommost_solid_tile.getX(), bottommost_solid_tile.getY()-1, tile);
				
				// Trying the top of the boardtile
				try_on_board.add(topMove);
				
				CheckProcessObject result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					valid_top_move.addAll(try_on_board);
					top_score = result.getScore();
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				// Trying the left of the boardtile
				try_on_board.add(bottomMove);
				result = field.processMove(try_on_board,ControlParameters.ENGINE);
				if(result.isValidity()) {
					valid_bottom_move.addAll(try_on_board);
					bottom_score = result.getScore();
					try_on_board.remove(try_on_board.size()-1);
				}
				else {
					try_on_board.remove(try_on_board.size()-1);
				}
				
				if (valid_top_move.isEmpty() && valid_bottom_move.isEmpty()) {
					continue;
				}
				else {
					if(top_score > bottom_score) {
						most_optimal_addition_tree.put(top_score, valid_top_move);
					}else {
						most_optimal_addition_tree.put(bottom_score, valid_bottom_move);
					}
				}
				
				
			}
			current_best_points = most_optimal_addition_tree.lastEntry().getKey();
			return most_optimal_addition_tree.lastEntry().getValue();
		}
		
		else {
			return tiles;
		}
		
	}

	public void addHandTiles(ArrayList<Tile> newTiles) {
		for(Tile tile : newTiles) {
			handtiles.add(tile);
		}
		
	}
}
