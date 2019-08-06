package qwirkle.Desktop.Game;

import java.util.ArrayList;
import java.util.Random;

import qwirkle.Desktop.entity.Tile;


/**
 * A class to model a bag of Tiles.
 * 
 * @author Kai Kriener
 *
 */
public class Bag
{
	private ArrayList<Tile> tiles;	
	
	/**
	 * Constructor for Bag.
	 * @param ArrayList<Tiles> pTiles: a ArrayList of tiles, 
	 * which will be in the bag after construction
	 */
	public Bag(ArrayList<Tile> tempTiles){
		this.tiles = tempTiles;
	}
	
	/**
	 * Method to get all Tiles that are currently in the bag.
	 * @return ArrayList<Tiles> of all tiles.
	 */
	public ArrayList<Tile> getAllTiles()
	{
		return this.tiles;
	}
	
	/**
	 * Allows to add a Tile or a set of Tiles to the bag,
	 * @param Tile pTile: a Tile object to add
	 * @param ArrayList<Tiles> pTiles; a set of Tiles to add
	 */
	public void addTile(Tile pTile)
	{
		this.tiles.add(pTile);
	}
	
	/**
	 * Allows to add a Tile or a set of Tiles to the bag,
	 * @param Tile pTile: a Tile object to add
	 * @param ArrayList<Tiles> pTiles; a set of Tiles to add
	 */
	public void addTile(ArrayList<Tile> pTiles)
	{
		this.tiles.addAll(pTiles);
	}
	
	/**
	 *Method to check, if bag is empty. 
	 * @return boolean true, if empty, false if not empty
	 */
	public boolean isEmpty()
	{
		return this.tiles.isEmpty();
	}
	
	/**
	 * Method to gat the number of Tiles in the bag.
	 * @return Integer of Number of Tiles
	 */
	public int getSize()
	{
		return tiles.size();
	}


}
