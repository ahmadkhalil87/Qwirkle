/**
 * The class for the main field in Qwirkle, will be used as
 * the basic version of the actual playfield
 * 
 * @author Jan Sadowski
 * @version_1.0
 * 
 */

package qwirkle.Desktop.Game;

import java.util.Collection;

import javafx.scene.layout.GridPane;
import qwirkle.Desktop.entity.TileOnPosition;

public class Field extends GridPane{

	private TileOnPosition[][] placedTiles;
	private Collection<TileOnPosition> board;
	private int offset;

	public TileOnPosition[][] getPlacedTiles() {
		return placedTiles;
	}

	public void setPlacedTiles(TileOnPosition[][] placedTiles) {
		this.placedTiles = placedTiles;
	}

	public Collection<TileOnPosition> getBoard() {
		return board;
	}

	public void setBoard(Collection<TileOnPosition> board) {
		this.board = board;
	}
	
	public void setOffset(int offset){
		this.offset = offset;
	}
	
//	public void setBoard(Collection<TileOnPosition> board, int size){
//		this.placedTiles = new TileOnPosition[size*2][size*2];
//		
//		for (TileOnPosition tile : board){
//			int x = ((tile.getX() < 0)? tile.getX() + size : tile.getX());
//			int y = ((tile.getY() < 0)? tile.getY() + size : tile.getY());
//			
//			placedTiles[x][y] = tile;
//		}
//	}

	public void addTile(Collection<TileOnPosition> tiles){	
		for(TileOnPosition tile : tiles){
			this.board.add(tile);
		}
	}	
}


