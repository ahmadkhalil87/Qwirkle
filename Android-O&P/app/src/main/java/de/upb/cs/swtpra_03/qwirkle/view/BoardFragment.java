package de.upb.cs.swtpra_03.qwirkle.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.devs.vectorchildfinder.VectorChildFinder;

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_03.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_03.qwirkle.model.game.Board;
import de.upb.cs.swtpra_03.qwirkle.model.game.TileIds;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Handles UI representation of the game board
 */
public class BoardFragment extends Fragment {

    private OnBoardFragmentListener listener;

    // objects concerning view
    public GridLayout grid;
    private ImageButton zoomInBtn;
    private ImageButton zoomOutBtn;
    private ScrollView mScrollV;
    private HorizontalScrollView mScrollH;
    public static BoardTile SELECTED_BOARD_TILE = null;
    public static ArrayList<BoardTile> placedBoardTiles = new ArrayList<BoardTile>();
    // Directions
    public static boolean setdirection = false;
    public static boolean Vertical = false;
    public static boolean Horizontal = false;

    private GameActivity ga;

    private float mZoom = (float) 0.0;

    // model objects
    Board board;

    /**
     * Creates Layout and binds Views
     *
     * @param inflater              Layout inflator
     * @param container             parent ViewGroup
     * @param savedInstanceState    data from old instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        mScrollV = view.findViewById(R.id.board_scroll_v);
        mScrollH = view.findViewById(R.id.board_scroll_h);

        zoomInBtn = view.findViewById(R.id.board_zoom_plus);
        zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });

        zoomOutBtn = view.findViewById(R.id.board_zoom_minus);
        zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });

        //Defining Grid with a single starting cell
        grid = (GridLayout) view.findViewById(R.id.board_grid);
        grid.setLayoutTransition(new LayoutTransition());
        grid.removeAllViews();

        grid.setColumnCount(1);
        grid.setRowCount(1);

        return view;
    }


    /**
     * Called when a tile on the board has been clicked
     *
     * @param tile  Pressed Tile
     */
    public void onClickedTile(BoardTile tile) {
        listener.onBoardTileClicked(tile);
        SELECTED_BOARD_TILE = tile;
        if(board != null){
            board.setContext(this);
        }
        Log.d("BOARDCLICK", "CLICKED TILE ON BOARD : " + tile.getX() + " | " + tile.getY());

    }

    /**
     * Method called by the Start button
     * @param tile
     * @return
     */
    public boolean place_start(BoardTile tile){
        try{
            List<TileOnPosition> top = new ArrayList<>();
            top.add(tile.toTileOnPosition());
            ga.updateBoard(top);
            placedBoardTiles.add(tile);
            return true;
        }catch(IllegalArgumentException ia){
            return false;
        }
    }

    /**
     * Method called by the Left button
     * @param tile
     * @return
     */
    public boolean place_left(BoardTile tile){
        tile.setX(tile.getX()-1);
        try{
            if(!Vertical) {
                List<TileOnPosition> top = new ArrayList<>();
                top.add(tile.toTileOnPosition());
                ga.updateBoard(top);
                placedBoardTiles.add(tile);
                if(placedBoardTiles.size() >= 2){
                    setdirection = true;
                    Horizontal = true;
                }
                return true;
            }
            else{
                return false;
            }
        }catch(IllegalArgumentException ia){
            return false;
        }
    }

    /**
     * Method called by the right button
     * @param tile
     * @return
     */
    public boolean place_right(BoardTile tile){
        tile.setX(tile.getX()+1);
        try{
            if(!Vertical) {
                List<TileOnPosition> top = new ArrayList<>();
                top.add(tile.toTileOnPosition());
                ga.updateBoard(top);
                placedBoardTiles.add(tile);
                if(placedBoardTiles.size() >= 2){
                    setdirection = true;
                    Horizontal = true;
                }
                return true;
            }
            else{
                return false;
            }
        }catch(IllegalArgumentException ia){
            return false;
        }
    }

    /**
     * Method called by the bottom button
     * @param tile
     * @return
     */
    public boolean place_bot(BoardTile tile){
        tile.setY(tile.getY()-1);
        try{
            if(!Horizontal) {
                List<TileOnPosition> top = new ArrayList<>();
                top.add(tile.toTileOnPosition());
                ga.updateBoard(top);
                placedBoardTiles.add(tile);
                if(placedBoardTiles.size() >= 2){
                    setdirection = true;
                    Vertical = true;
                }
                return true;
            }
            else{return false;}
        }catch(IllegalArgumentException ia){
            return false;
        }
    }

    /**
     * Method called by the top button
     * @param tile
     * @return
     */
    public boolean place_top(BoardTile tile){
        tile.setY(tile.getY()+1);
        try{
            if(!Horizontal) {
                List<TileOnPosition> top = new ArrayList<>();
                top.add(tile.toTileOnPosition());
                ga.updateBoard(top);
                placedBoardTiles.add(tile);
                if(placedBoardTiles.size() >= 2){
                    setdirection = true;
                    Vertical = true;
                }
                return true;
            }
            else{return false;}
        }catch(IllegalArgumentException ia){
            return false;
        }
    }


    /**
     * Initialize board right after joining a game
     *
     * @param gameData  game data reference for the board
     */
    public void setupOnJoin(GameData gameData) {
        this.board = new Board(this, gameData);
    }


    /**
     * Add a new tile on the board as specified within the tile
     *
     * @param newTile   new tile to be placed on the board
     */
    public void addTileToGrid(BoardTile newTile) {
        // TODO: Testing
        ImageView iv;

        if (newTile.getView() == null) {
            // creating a new view for the tile
            iv = new ImageView(getContext());
            setViewParameters(iv);

            // setting view as attribute for the tile
            newTile.setView(iv);
        } else {
            iv = (ImageView) newTile.getView();
        }

         // set correct Drawable to image and configure color
        VectorChildFinder vector = new VectorChildFinder(getContext(),
                TileIds.IDS[newTile.getShape()], iv);
        vector.findPathByName("color").setFillColor(
                getContext().getResources().getColor(
                        TileIds.COLORS[newTile.getColor()]));
        iv.invalidate();

        // set the tile on the grid as "new Tile" (uses view from the tile)
        newTile.setNewTile(true);
        setViewOnGridOf(newTile);

        //set onClickListener, set tag to its BoardTile as identifier
        iv.setTag(newTile);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardTile tile = (BoardTile) v.getTag();
                onClickedTile(tile);
            }
        });

    }

    private void setViewParameters(ImageView iv) {
        iv.setAdjustViewBounds(true);
        int px = Math.round(TypedValue.applyDimension( // Calculates current zoom to equivalent px
                TypedValue.COMPLEX_UNIT_DIP, (float) (Math.exp(mZoom)*50),
                getResources().getDisplayMetrics()));
        iv.setMaxWidth(px); // uses px, NOT dp
        iv.setMinimumWidth(px);
        int padding = (int) (Math.exp(mZoom)*10);
        iv.setPadding(padding,padding,padding,padding);
    }


    /**
     * Removes a tile from the board, tile has to be on the board.
     * No logic on whether removal is allowed
     *
     * @param tile  Tile to be removed from the board
     */
    public void removeTileFromGrid(BoardTile tile) {
        System.out.print("REMOVING TILE :" + tile + " OF GRIDVIEW");
        for(int i = 0 ; i < grid.getChildCount() ; i++){
            View child = grid.getChildAt(i);
            if(child == tile.getView()){
                grid.removeViewAt(i);
                break;
            }
        }
    }

    /**
     * Helper method which puts the tile on position saved within the tile
     * View has to be already set
     *
     * @param t     tile to be set
     */
    private void setViewOnGridOf(BoardTile t) {
        // update view specifications
        GridLayout.Spec row = GridLayout.spec(t.getAbsY());
        GridLayout.Spec col = GridLayout.spec(t.getAbsX());

        // set view on the grid with updated specifications
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                grid.addView(t.getView(), new GridLayout.LayoutParams(row, col));

            }
        });
    }


    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    public void addRowTop(List<BoardTile> tiles) {
        /* Create a new row on the bottom and move every view one cell down.*/
        grid.setRowCount( grid.getRowCount() +1 );
        grid.removeAllViews();
        for (BoardTile t : tiles) {
            // increase position in y direction by one
            t.setAbsY(t.getAbsY()+1);

            setViewOnGridOf(t);
        }
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    public void addRowBottom() {
        grid.setRowCount( grid.getRowCount() +1 );
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    public void addColumnRight() {
        grid.setColumnCount( grid.getColumnCount() +1 );
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    public void addColumnLeft(List<BoardTile> tiles) {
        /* Create a new column on the right and move every view one cell to the right.*/
        grid.setColumnCount(grid.getColumnCount() + 1);
        grid.removeAllViews();
        for (BoardTile t : tiles) {
            // increase position in x direction by one
            t.setAbsX(t.getAbsX()+1);

            setViewOnGridOf(t);
        }
    }

    /**
     * Adds new Tiles to the board
     *
     * @param update    List of all new tiles
     */
    public void update(List<BoardTile> update){
        // TODO: Testing
        board.addTiles(update);
    }

    /**
     * Increases zoom by enlarging all tiles on the board
     */
    private void zoomIn() {
        if (mZoom < 1) {
            float oldZoom = mZoom;
            mZoom = (float) Math.round((mZoom + 0.1)*10) / 10;
            updateViewSizeOnBoard(oldZoom);
        }
    }

    /**
     * Decreases zoom by shrinking all tiles on the board
     */
    private void zoomOut() {
        if (mZoom > -1.5) {
            float oldZoom = mZoom;
            mZoom = (float) Math.round((mZoom - 0.1)*10) / 10;
            updateViewSizeOnBoard(oldZoom);
        }
    }

    /**
     * Updates all views placed on the board grid with the current zoom
     * and moves scroll views to a new position to show approximately
     * the same spot on the board as before.
     * Zoom is calculated logarithmically.
     */
    private void updateViewSizeOnBoard(float oldZoom) {
        // TODO Testing: new Tiles added to board
        List<BoardTile> tiles = board.getTilesOnBoard();
        ImageView iv;

        for (BoardTile t : tiles) {
            iv = (ImageView) t.getView();
            setViewParameters(iv);
        }


        // get values to determine new position of scrollviews
        // variables ending with V are needed for the vertical scroll view, H for horizontal scroll view
        int oldTotalV = grid.getHeight();
        int oldTotalH = grid.getWidth();
        int posV = mScrollV.getScrollY();
        int posH = mScrollH.getScrollX();
        int padding = grid.getPaddingBottom();  // padding is the same on all sides

        // the factor by which the view has changed
        float factor = (float) (Math.exp(mZoom)/Math.exp(oldZoom));

        // Calculate new size of the grid. (The grid doesn't yet know its new size, will get updated on drawing)
        // Padding doesn't change; only the tiles will get larger
        int newTotalV = Math.round((oldTotalV - 2*padding)*factor + 2*padding);
        int newTotalH = Math.round((oldTotalH - 2*padding)*factor + 2*padding);

        // Calculate new positions of the scroll views to stay approximately in the same spot
        int newPosV = (Integer) ((posV*newTotalV)/oldTotalV);
        int newPosH = (Integer) ((posH*newTotalH)/oldTotalH);

        // for vertical scroll view position in horizontal direction doesn't matter, same for horizontal scroll view
        mScrollV.scrollTo(mScrollV.getScrollX(), newPosV);
        mScrollH.scrollTo(newPosH, mScrollH.getScrollY());

    }

    /**
     * Defines Method to parent know which tile on the board has been clicked
     */
    public interface OnBoardFragmentListener {
        void onBoardTileClicked(BoardTile tile);
    }

    /**
     * Attaches this fragment to the life cycle of the parent activity.
     * Android intern method.
     * Used here to make sure parent activity uses our interface.
     *
     * @param context   parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBoardFragmentListener) {
            listener = (OnBoardFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BoardFragment.OnBoardFragmentListener");
        }
    }

    /**
     * Detaches this fragment from parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setGa(GameActivity ga) {
        this.ga = ga;
    }
}



