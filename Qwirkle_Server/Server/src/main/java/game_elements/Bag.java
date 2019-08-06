package game_elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.upb.swtpra1819interface.models.Tile;
import log4j.jfxAppender;

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
	public Bag(ArrayList<Tile> pTiles)
	{
		this.tiles = pTiles;
	}
	/**
	 * Constructor for Bag.
	 * @param Tiles pTile: a Tile object, 
	 * which will be in the bag after construction
	 */
	public Bag(Tile pTile)
	{
		this.tiles = new ArrayList<Tile>();
		this.tiles.add(pTile);
	}
	/**
	 * Constructor for Bag with no parameters
	 * to construct an empty bag.
	 */
	public Bag()
	{
		this.tiles = new ArrayList<Tile>();
	}
	
	
	/**
	 * returns a random List of tile takes from the bag
	 * and removes it.
	 * @return a Tile object, 'null' if empty
	 */
	public ArrayList<Tile> getTiles(int count){
		ArrayList<Tile> toSend = new ArrayList<Tile>();
		ArrayList<Tile> removeList = new ArrayList<Tile>();
		
		if (!this.isEmpty()){
			for (int tileCount=0; tileCount<count; tileCount++){
				Random gen = new Random();
				int i = gen.nextInt(this.tiles.size());
				toSend.add(this.tiles.get(i));
				removeList.add(this.tiles.get(i));
			}
		}else{
			return null;
		}
		
		removeTiles(removeList);
		return toSend;
	}

	public void removeTiles(ArrayList<Tile> toRemove){
		Iterator<Tile> it = this.tiles.iterator();
		while(it.hasNext()) {
			if(toRemove.isEmpty()) {
				return;
			}
			Tile tile = it.next();
			int id = tile.getUniqueId();
			for(Tile tiletoremove : toRemove){
				if(tiletoremove.getUniqueId() == id) {
					it.remove();
					toRemove.remove(tiletoremove);
					break;
				}
			}
		}
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
