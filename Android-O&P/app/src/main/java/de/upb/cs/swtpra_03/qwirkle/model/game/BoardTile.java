package de.upb.cs.swtpra_03.qwirkle.model.game;

import android.view.View;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Holds data to represent a tile on the board
 */
public class BoardTile extends Tile {
    private int x; // specifies the column relative to first tile
    private int y; // specifies the row relative to first tile

    //used by views, do not operate on these fields elsewhere
    private int absX; // specifies the column in absolute term on the grid
    private int absY; // specifies the row in absolute term on the grid
    private View mView; // save view for improved performance

    /**
     * Constructor
     *
     * @param tile      Board tile of interface type
     */
    public BoardTile(TileOnPosition tile) {
        super(tile.getTile());
        this.x = tile.getCoordX();
        this.y = tile.getCoordY();
    }

    public void setNewTile(boolean isNewTile) {
        if(isNewTile) {
            this.mView.setBackgroundColor(mView.getContext().getResources().getColor(R.color.newTileFrame));
        } else {
            this.mView.setBackgroundColor(mView.getContext().getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public String toString() {
        return String.format("BoardTile with Color: %d, Shape: %d, PosX: %d, PosY: %d", getColor(), getShape(), getX(), getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAbsX() {
        return absX;
    }

    public void setAbsX(int absX) {
        this.absX = absX;
    }

    public int getAbsY() {
        return absY;
    }

    public void setAbsY(int absY) {
        this.absY = absY;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    /**
     * Helper method to recreate interface Board Tile
     * @return
     */
    public TileOnPosition toTileOnPosition() {
        return new TileOnPosition(
                this.getX(), this.getY(), this.toInterfaceTile()
        );
    }
}
