package de.upb.cs.swtpra_03.qwirkle.model.lobby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.view.LobbyActivity;

/**
 * Display Games in RecyclerView and keep references to all Items
 */
public class GameListingAdapter extends RecyclerView.Adapter<GameListingAdapter.ViewHolder> {
    private static final String TAG = "GameListingAdapter"; // For debugging

    private LobbyActivity context;
    private ArrayList<LobbyGame> lobbyGamesList;

    /**
     * Constructor
     *
     * @param context Reference to the current context
     * @param lobbyGames   Reference to a list of Games to display. Can be updated by updating
     *                the reference and invoking notifyDataSetChanged()
     */
    public GameListingAdapter(Context context, ArrayList<LobbyGame> lobbyGames) {
        if (!(context instanceof OnClickJoinListener)) {
            throw new ClassCastException(context.toString() +
                    " must implement GameListingAdapter.OnClickJoinListener");
        } else {
            this.context = (LobbyActivity) context;
            this.lobbyGamesList = lobbyGames;
        }
    }

    /**
     * Create a new View and store it in a ViewHolder instance
     *
     * @param viewGroup parent View group
     * @param i         Index of the current Item
     * @return Viewholder containing the created View
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()
        ).inflate(R.layout.recycleritem_lobby_game, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**
     * Apply Data to the current Item in list
     *
     * @param viewHolder Reference to the viewHolder
     * @param position   position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // Get the n-th game from our list of games
        LobbyGame currentGame = lobbyGamesList.get(position);

        // Update name
        viewHolder.gameName.setText(currentGame.getName());
        // Update player counter
        viewHolder.gamePlayerCount.setText(
                "[" + currentGame.getCurPlayerCount() + "/" + currentGame.getMaxPlayerCount() + "]"
        );

        // Update game's state using a string resource
        viewHolder.gameState.setText(CurrentGameState.getStringRes(currentGame.getState()));

        // Update game's type.
        // Use translatable resource instead of a plain string; See Statement above
        viewHolder.gameType.setText(currentGame.getType());

        // Handle click on join button, use LobbyGame as identifier
        viewHolder.joinGame.setTag(currentGame);
        viewHolder.joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnClickJoinListener) context).onClickedJoin((LobbyGame) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lobbyGamesList.size();
    }

    /**
     * Data structure used by the RecyclerView to keep references to every View in the layout
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        Button joinGame;
        TextView gameName;
        TextView gamePlayerCount;
        TextView gameState;
        TextView gameType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            joinGame = itemView.findViewById(R.id.join_game);
            gameName = itemView.findViewById(R.id.game_name);
            gamePlayerCount = itemView.findViewById(R.id.game_playercount);
            gameState = itemView.findViewById(R.id.game_state);
            gameType = itemView.findViewById(R.id.game_type);
        }
    }

    /**
     * Defines a method to notify parent about a pressed join button
     */
    public interface OnClickJoinListener {
        public void onClickedJoin(LobbyGame lobbyGame);
    }


}
