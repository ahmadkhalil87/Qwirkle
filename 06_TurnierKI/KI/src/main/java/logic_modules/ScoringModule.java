package logic_modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.TileOnPositionEX;
import enumeration.Direction;
import enumeration.Neighbor;
import logic_objects.NeighborshipCertificate;

public class ScoringModule {
	
	Logger logger = LogManager.getLogger();
	int qwirkle;
	int penalty;
	
	public ScoringModule(int qwirkle, int penalty) {
		this.qwirkle = qwirkle;
		this.penalty = penalty;
	}
	
	public int calculateScore(HashMap<TileOnPositionEX, ArrayList<NeighborshipCertificate>> certificate_collection, Direction direction) {
		logger.log(Level.getLevel("SCORECALCULATION"),"------- INITIATING SCORECALCULUS -------" );
		/**
		 * For the main direction, since they will otherwise be counted multiple times
		 */
		int total_score = 0;
		int horizontal_score = 0;
		int vertical_score = 0;
		
		// Only one tile is being placed
		if(direction == Direction.None) {
			//logger.log(Level.getLevel("SCORECALCULATION"),"ONLY ONE TILE WAS PLAYED, APPLYING SINGULAR CALCULUS" );
			for(TileOnPositionEX placed_tile : certificate_collection.keySet()) {
				//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE HORIZONTAL SCORE...." );
				/**
				 * Going over the horizontal line
				 */
				boolean end_of_line = false;
				TileOnPositionEX current_tile = placed_tile;
				while(!end_of_line) {
					if(current_tile.getNeighbor(Neighbor.Left) != null) {
						current_tile = current_tile.getNeighbor(Neighbor.Left);
					}
					else {
						end_of_line = true;
						}
					}
				/**
				 * Horizontal has no tiles, calculating vertical
				 */
				if(current_tile == placed_tile && current_tile.getNeighbor(Neighbor.Right) == null) {
					//logger.log(Level.getLevel("SCORECALCULATION"),"NO HORIZONTAL TILES FOUND" );
					//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE VERTICAL SCORE...." );
					
					/**
					 * Calculating the Vertical
					 */
					end_of_line = false;
					current_tile = placed_tile;
					while(!end_of_line) {
						if(current_tile.getNeighbor(Neighbor.Top) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Top);
						}
						else {
							end_of_line = true;
						}
					}
					boolean no_more_to_go = false;
					while(!no_more_to_go) {
						vertical_score += 1;
						if(current_tile.getNeighbor(Neighbor.Bottom) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Bottom);
						}
						else {
							no_more_to_go = true;
						}
					}
					//logger.log(Level.getLevel("SCORECALCULATION"),"THE VERTICAL LINE SCORED " + vertical_score + " POINTS" );
					if(vertical_score == qwirkle) {
						//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS.");
						vertical_score += qwirkle;
					}
					total_score += vertical_score;
					//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
				}
				
				/**
				 * Horizontal line has tiles
				 */
				else {
					boolean no_more_to_go = false;
					while(!no_more_to_go) {
						horizontal_score += 1;
						if(current_tile.getNeighbor(Neighbor.Right) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Right);
						}
						else {
							no_more_to_go = true;
						}
					}
					//logger.log(Level.getLevel("SCORECALCULATION"),"THE HORIZONTAL LINE SCORED " + horizontal_score + " POINTS" );
					if(horizontal_score == qwirkle) {
						//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS" );
						horizontal_score += qwirkle;
					}
					total_score += horizontal_score;
					//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
					
					//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE VERTICAL SCORE...." );
					
					/**
					 * Calculating the Vertical
					 */
					end_of_line = false;
					current_tile = placed_tile;
					while(!end_of_line) {
						if(current_tile.getNeighbor(Neighbor.Top) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Top);
						}
						else {
							end_of_line = true;
						}
					}
					if(current_tile == placed_tile && current_tile.getNeighbor(Neighbor.Bottom) == null) {
						return total_score;
					}
					no_more_to_go = false;
					while(!no_more_to_go) {
						vertical_score += 1;
						if(current_tile.getNeighbor(Neighbor.Bottom) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Bottom);
						}
						else {
							no_more_to_go = true;
						}
					}
					//logger.log(Level.getLevel("SCORECALCULATION"),"THE VERTICAL LINE SCORED " + vertical_score + " POINTS" );
					if(vertical_score == qwirkle) {
						//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS.");
						vertical_score += qwirkle;
					}
					total_score += vertical_score;
					//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
					
				}
			}
		}
		/**
		 * Score calculation on a horizontal placement
		 */
		if(direction == Direction.Horizontal) {
			//logger.log(Level.getLevel("SCORECALCULATION"),"MOVE WAS HORIZONTAL, APPLYING HORIZONTAL CALCULUS" );
			for(TileOnPositionEX placed_tile : certificate_collection.keySet()) {
				if(horizontal_score == 0) {
					//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE HORIZONTAL SCORE...." );
					/**
					 * Going of the horizontal line to avoid multiple counting of tiles
					 */
					boolean end_of_line = false;
					TileOnPositionEX current_tile = placed_tile;
					while(!end_of_line) {
						if(current_tile.getNeighbor(Neighbor.Left) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Left);
						}
						else {
							end_of_line = true;
						}
					}
					boolean no_more_to_go = false;
					while(!no_more_to_go) {
						horizontal_score += 1;
						if(current_tile.getNeighbor(Neighbor.Right) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Right);
						}
						else {
							no_more_to_go = true;
						}
					}
					//logger.log(Level.getLevel("SCORECALCULATION"),"THE HORIZONTAL LINE SCORED " + horizontal_score + " POINTS" );
					if(horizontal_score == qwirkle) {
						//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS" );
						horizontal_score += qwirkle;
					}
					total_score += horizontal_score;
					//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
				}
				/**
				 * Going over the vertical line made by placing the tile
				 * if there is none, it goes into a continue;
				 */
				//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE VERTICAL FOR TILE: " + placed_tile);
				vertical_score = 0;
				boolean end_of_line = false;
				TileOnPositionEX current_tile = placed_tile;
				while(!end_of_line) {
					if(current_tile.getNeighbor(Neighbor.Top) != null) {
						current_tile = current_tile.getNeighbor(Neighbor.Top);
					}
					else {
						end_of_line = true;
					}
				}
				if(current_tile == placed_tile && current_tile.getNeighbor(Neighbor.Bottom) == null) {
					continue;
				}
				boolean no_more_to_go = false;
				while(!no_more_to_go) {
					vertical_score += 1;
					if(current_tile.getNeighbor(Neighbor.Bottom) != null) {
						current_tile = current_tile.getNeighbor(Neighbor.Bottom);
					}
					else {
						no_more_to_go = true;
					}
				}
				/**
				 * Applying the points made by one tile
				 */
				//logger.log(Level.getLevel("SCORECALCULATION"),"TILE: " + placed_tile + " SCORED " + vertical_score + " ON ITS VERTICAL!");
				if(vertical_score == qwirkle) {
					//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS.");
					vertical_score += qwirkle;
				}
				total_score += vertical_score;
				//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
			}
		}
		if(direction == Direction.Vertical) {
			//logger.log(Level.getLevel("SCORECALCULATION"),"MOVE WAS VERTICAL, APPLYING VERTICAL CALCULUS" );
			for(TileOnPositionEX placed_tile : certificate_collection.keySet()) {
				if(vertical_score == 0) {
					//logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE VERTICAL SCORE...." );
					/**
					 * Going of the vertical line to avoid multiple counting of tiles
					 */
					boolean end_of_line = false;
					TileOnPositionEX current_tile = placed_tile;
					while(!end_of_line) {
						if(current_tile.getNeighbor(Neighbor.Top) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Top);
						}
						else {
							end_of_line = true;
						}
					}
					boolean no_more_to_go = false;
					while(!no_more_to_go) {
						vertical_score += 1;
						if(current_tile.getNeighbor(Neighbor.Bottom) != null) {
							current_tile = current_tile.getNeighbor(Neighbor.Bottom);
						}
						else {
							no_more_to_go = true;
						}
					}
					//logger.log(Level.getLevel("SCORECALCULATION"),"THE VERTICAL LINE SCORED " + vertical_score + " POINTS" );
					if(vertical_score == qwirkle) {
						//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS.");
						vertical_score += qwirkle;
					}
					total_score += vertical_score;
					//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
				}
				/**
				 * Going over the Horizontal line made by placing the tile
				 * if there is none, it goes into a continue;
				 */
				logger.log(Level.getLevel("SCORECALCULATION"),"CALCULATING THE HORIZONTAL FOR TILE: " + placed_tile);
				horizontal_score = 0;
				boolean end_of_line = false;
				TileOnPositionEX current_tile = placed_tile;
				while(!end_of_line) {
					if(current_tile.getNeighbor(Neighbor.Left) != null) {
						current_tile = current_tile.getNeighbor(Neighbor.Left);
					}
					else {
						end_of_line = true;
					}
				}
				if(current_tile == placed_tile && current_tile.getNeighbor(Neighbor.Right) == null) {
					continue;
				}
				boolean no_more_to_go = false;
				while(!no_more_to_go) {
					horizontal_score += 1;
					if(current_tile.getNeighbor(Neighbor.Right) != null) {
						current_tile = current_tile.getNeighbor(Neighbor.Right);
					}
					else {
						no_more_to_go = true;
					}
				}
				/**
				 * Applying the points made by one tile
				 */
				//logger.log(Level.getLevel("SCORECALCULATION"),"TILE: " + placed_tile + " SCORED " + horizontal_score + " ON ITS HORIZONTAL!");
				if(horizontal_score == qwirkle) {
					//logger.log(Level.getLevel("SCORECALCULATION"),"APPLYING THE QWIRKLE BONUS OF " + qwirkle + " POINTS.");
					horizontal_score += qwirkle;
				}
				total_score += horizontal_score;
				//logger.log(Level.getLevel("SCORECALCULATION"),"CURRENT TOTAL SCORE: " + total_score);
			}
		}
		
		logger.log(Level.getLevel("SCORECALCULATION"),"THE WHOLE MOVE SCORED A TOTAL OF " + total_score + " POINTS!");
		return total_score;
		
	}

	public int getQwirkle() {
		return qwirkle;
	}

	public void setQwirkle(int qwirkle) {
		this.qwirkle = qwirkle;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

}
