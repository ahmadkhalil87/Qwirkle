package de.upb.cs.swtpra_03.qwirkle.model.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.vectorchildfinder.VectorChildFinder;

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.cs.swtpra_03.qwirkle.view.HandsFragment;

/**
 * Handles representation of the tiles a player holds in his hands in the list of the HandsFragment.
 */
public class HandsAdapter extends RecyclerView.Adapter<HandsAdapter.HandsViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<Player> mData;
    public static Tile SELECTED_TILE;
    public static ArrayList<Tile> SwapTileList = new ArrayList<>();

    private final static int imageSize = 60;

    /**
     * Constructor
     *
     * @param context   The context in which the data will be shown
     * @param data      List of Players including their tiles to be shown
     */
    public HandsAdapter(Context context, List<Player> data) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    /**
     * Called when a new ViewHolder is needed in the RecyclerView
     *
     * @param parent    Parent ViewGroup
     * @param viewType  Type of the ViewHolder
     * @return
     */
    @Override
    public HandsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.recycleritem_hand, parent, false);
        return new HandsViewHolder(v);
    }

    /**
     * Called when a new View will be shown on the screen
     *
     * @param holder    ViewHolder to bind information to
     * @param position  Position in the data which is to be shown
     */
    @Override
    public void onBindViewHolder(HandsViewHolder holder, int position) {

        Player playerNow = mData.get(position);
        Log.d("VIEWHOLDER", "STARTING @ " + playerNow.getName() );
        holder.playerName.setText(playerNow.getName());
        holder.score.setText("Score: " + playerNow.getScore());
        holder.active.setChecked(playerNow.isActive());
        Log.d("VIEWHOLDER", "ESSENTIALS IN HOLDER ESTABLISHED @" + playerNow.getName() );
        ImageView iv;
        int i = 0; // number of tiles added

        // Bind LinearLayout with image views of tiles of the player
        Log.d("VIEWHOLDER", "PLAYERTILES TO VIEW @" + playerNow.getTiles() );
        for (Tile t : playerNow.getTiles()) {
            Log.d("VIEWHOLDER", "BUILDING HANDVIEW FOR PLAYER = " + playerNow.getName());
            if (i < holder.mLinearLayout.getChildCount()) { // there already is a view in the layout we can use
                iv = (ImageView) holder.mLinearLayout.getChildAt(i);
            } else { // we need to create a new view
                // create new ImageView with desired size
                iv = new ImageView(context);
                iv.setAdjustViewBounds(true);
                int px = Math.round(TypedValue.applyDimension( // Calculates 50dp to equivalent px
                        TypedValue.COMPLEX_UNIT_DIP, imageSize,
                        context.getResources().getDisplayMetrics()));
                iv.setMaxWidth(px); // uses px, NOT dp
                iv.setMinimumWidth(px);

                // add view to layout
                holder.mLinearLayout.addView(iv);
            }

            // set correct Drawable to image and configure color
            VectorChildFinder vector = new VectorChildFinder(context,
                    TileIds.IDS[t.getShape()], iv);
            vector.findPathByName("color").setFillColor(
                    context.getResources().getColor(
                            TileIds.COLORS[t.getColor()]));
            iv.invalidate();

            // set tag to its Tile for onClick() method
            iv.setTag(t);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tile tile = (Tile) v.getTag();
                    Context context = v.getContext();
                    CharSequence text = "SELECTED TILE : {C:" + tile.getColor() + " | S:" + tile.getShape() + " | ID:" + tile.getId() + " }";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    onClickTile(tile);
                }
            });

            // tile got added, add tile counter by one
            i++;
        }

        // TODO: Testing
        if (i < holder.mLinearLayout.getChildCount()) { // after adding all tiles, there are more unneeded views in the layout
            for( ; i < holder.mLinearLayout.getChildCount(); i++) {
                holder.mLinearLayout.removeViewAt(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size(); // number of players
    }

    /**
     * Handles the click on a tile in the players hand
     *
     * @param tile  Tile that has been clicked on
     */
    public void onClickTile(Tile tile) {
        SELECTED_TILE = tile;
    }

    /**
     * ViewHolder class of this Adapter
     * Stores all Views as attributes for easier access and better performance
     */
    public class HandsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView playerName;
        TextView score;
        RadioButton active;

        /**
         * Constructor
         *
         * @param v     ViewGroup which holds the views
         */
        public HandsViewHolder(View v) {
            super(v);
            mLinearLayout = v.findViewById(R.id.hand_tile_list);
            playerName = v.findViewById(R.id.hand_player_name);
            score = v.findViewById(R.id.hand_score);
            active = v.findViewById(R.id.hand_active);
        }
    }
}
