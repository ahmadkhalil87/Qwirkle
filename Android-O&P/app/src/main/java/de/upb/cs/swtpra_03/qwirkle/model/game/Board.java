package de.upb.cs.swtpra_03.qwirkle.model.game;

import android.util.Log;
import android.view.View;
import android.view.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.view.BoardFragment;

/**
 * Holds data for the board
 *
 * Interface:   GameData.GameDataListener
 *                              Used to get notice of changes to the game data
 */
public class Board implements GameData.GameDataListener {

    private static final String TAG = "Board";

    private BoardFragment context;
    private List<BoardTile> tilesOnBoard = new ArrayList<>();
    private List<BoardTile> newOnBoard = new ArrayList<>(); // Tiles in here have a frame around them on the board

    private boolean paused;

    private GameData mGameData;

    private int curRowCnt = 1; // count of rows and columns initialized in the grid
    private int curColCnt = 1; // initially, there is only one cell

    int[] zeroZero = {0, 0}; // {x, y} of the first tile as reference, x: column ; y: row
    /*
        -----
        | | | <- y-coordinate
        -----
        | | | <-
        -----
         ^ ^
         | |
         x-coordinate
     */


    /**
     * Constructor
     *
     * @param context       parent Activity
     * @param gameData      data of current game
     */
    public Board(BoardFragment context, GameData gameData){
        this.context = context;
        this.mGameData = gameData;
        mGameData.listenToGameData(this);
    }

    /**
     * Tries to add a single Tile to the board.
     * Throws IllegalArgumentException if the position of the tile is already blocked by a different tile.
     *
     * @param newTile   Tile to be added to the board
     * @return          true, if successful; false if tried to set tile outside of currently defined grid
     */
    private boolean addTile(BoardTile newTile) {
        // Tile already on the board, return true
        Iterator<BoardTile> boardit = tilesOnBoard.iterator();
        while(boardit.hasNext()){
            BoardTile bt = boardit.next();
            int X = bt.getX();
            int Y = bt.getY();
            int ID = bt.getId();
            int color = bt.getColor();
            int shape = bt.getShape();
            if(newTile.getId() == ID){
                if(newTile.getX() == X){
                    if(newTile.getY() == Y){
                        if(newTile.getColor() == color){
                            if(newTile.getShape() == shape){
                                // THEN THIS TILE IS OBVIOUSLY ON THE BOARD RIGHT THERE
                                return true;
                            }
                        }

                    }
                }
            }
        }

        int posX = newTile.getX() + zeroZero[0]; // actual position on the board instead of
        int posY = -(newTile.getY()) + zeroZero[1]; // relative position to the first tile

        Log.d(TAG, "Row: " + curRowCnt + " Col: " + curColCnt + " posX: " + posX + " posY: " + posY
                + " zeroZero: (" + zeroZero[0] + "," + zeroZero[1] + ")");

        if (posY > curRowCnt-1 || posX > curColCnt-1 || posY < 0 || posX < 0) {
            return false; // trying to set a tile outside the grid
        }

        for (BoardTile t : tilesOnBoard) { // check if position is free for the new tile
            if (t.getAbsX() == posX && t.getAbsY() == posY){
                throw new IllegalArgumentException("Tried to place new tile on already occupied position. x=" + t.getX() + " y=" + t.getY());
            }
        }

        // setting absolute values for the tile
        newTile.setAbsX(posX);
        newTile.setAbsY(posY);

        // adding view to the grid (uses absolute coordinates of the tile)
        context.addTileToGrid(newTile);

        // as tile is new, it will have a frame around it now. To remove it later:
        newOnBoard.add(newTile);

        // adding tile to the tile list
        tilesOnBoard.add(newTile);

        // adding a new row or column if new tile is set to the edge of the grid
        if (posX == curColCnt-1) addColumnRight();
        if (posY == curRowCnt-1) addRowBottom();
        if (posX == 0) addColumnLeft();
        if (posY == 0) addRowTop();

        return true;
    }

    /**
     * Tries to add multiple Tiles to the board
     * Throws IllegalArgumentException if the position of the tile is already blocked by a different tile.
     *
     * @param list      List of Tiles to be added to the board
     */
    public void addTiles(List<BoardTile> list) {
        // delete frames around current new tiles, they will be old now
        removeNewTiles();

        int loop = list.size(); // Stops this from going on forever
        List<BoardTile> listCopy = new ArrayList<BoardTile>(list);

        while (listCopy.size() > 0 && loop >= 0) {
            loop--;
            Iterator<BoardTile> iterator = listCopy.iterator();
            while (iterator.hasNext()) {
                if (addTile(iterator.next())) {
                    iterator.remove();
                    Log.d(TAG, "CURRENT TILES ON BOARD : " + tilesOnBoard);
                }
            }
        }

        if (loop < 0) {
            Log.e(TAG, "NOT ALL TILES COULD BE PLACED");
        }
    }

    private void removeNewTiles() {
        for (BoardTile t : newOnBoard) {
            t.setNewTile(false);
        }
        newOnBoard.clear();
    }

    /**
     * removes a single Tile from the board
     *
     * @param tile  Tile to be removed
     */
    private void removeTile(BoardTile tile) {
        Log.d("REMOVETILES","REMOVING TILE: " + tile + " right now");
        Log.d("CONTEXTCHECK","THIS IS THE BOARDFRAG:" + context);
        Log.d("CONTEXTCHECK","CHILDREN COUNT: " + context.grid.getChildAt(0));
        for(Tile boardtile : tilesOnBoard){
            if(tile.getId() == boardtile.getId()){
                tilesOnBoard.remove(boardtile);
                break;
            }
        }
    }

    // TODO: will this be used by participants?
    /**
     * removes multiple Tiles from the board
     *
     * @param list  List of Tiles to be removed
     */
    public void removeTiles(List<BoardTile> list) {
        Log.d("REMOVETILES","REMOVING TILES OF LIST: " + list);
        Log.d("REMOVETILES","SIZE TILESONBOARD BEFORE" + tilesOnBoard.size());
        for (BoardTile t : list) {
            removeTile(t);
        }
        Log.d("REMOVETILES","SIZE TILESONBOARD AFTER" + tilesOnBoard.size());
        context.grid.removeAllViews();
        for(BoardTile tile : tilesOnBoard){
            tile.setView(null);
            context.addTileToGrid(tile);
        }
    }

    /**
     * Extending grid by adding rows and columns
     */
    private void addRowTop() {
        context.addRowTop(tilesOnBoard);
        zeroZero[1]++;
        curRowCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addRowBottom() {
        context.addRowBottom();
        curRowCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addColumnRight() {
        context.addColumnRight();
        curColCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addColumnLeft() {
        context.addColumnLeft(tilesOnBoard);
        curColCnt++;
        zeroZero[0]++;
    }

    public List<BoardTile> getTilesOnBoard() {
        return this.tilesOnBoard;
    }

    /**
     * Reacts to updates to the game data
     */
    public void onGameDataUpdate() {
        // TODO for participants (if needed)
    }

    /**
     * Set Context
     */
    public void setContext(BoardFragment context){
        this.context = context;
    }
}
