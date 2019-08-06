package de.upb.cs.swtpra_03.qwirkle.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

import de.upb.cs.swtpra_03.qwirkle.R;

import de.upb.cs.swtpra_03.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_03.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_03.qwirkle.model.game.HandsAdapter;
import de.upb.cs.swtpra_03.qwirkle.model.game.Player;
import de.upb.cs.swtpra_03.qwirkle.model.game.Tile;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Provide UI Related Functions used for hands overview
 */
public class HandsFragment extends Fragment implements
        GameData.GameDataListener{

    private RecyclerView mRecyclerView;
    private BoardFragment boardFrag;
    private GameData mGameData;

    private int playerCount;
    private int clientID;

    private List<Client> clients;

    private Button tileSwapButton;
    private Button playTilesButton;
    private Button clear_tiles;

    private Button place_left_b;
    private Button place_right_b;
    private Button place_top_b;
    private Button place_bot_b;
    private Button startButton;
    private Button unselectButton;
    private GameActivity gameAct;


    /**
     * Create a new View from Layout and bind functions to UI Elements
     *
     * @param inflater           Layout inflater
     * @param container          Parent Viewgroup
     * @param savedInstanceState Saved State
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hands, container, false);

        mRecyclerView = view.findViewById(R.id.hands_recycler);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(new HandsAdapter(getContext(), new ArrayList<>()));



        tileSwapButton = view.findViewById(R.id.tile_swap_button);
        tileSwapButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click on Tile Swap Button in GameActivity.
             * Lets the Client send a Message containing the information required for a Tile-swap
             *
             * @param v pressed view (in this case bag button)
             */
            @Override
            public void onClick(View v) {
                if (BoardFragment.placedBoardTiles.size() == 0) {
                    if (HandsAdapter.SELECTED_TILE == null && HandsAdapter.SwapTileList.size() > 0) {
                        Context context = v.getContext();
                        CharSequence text = "Sending TileSwapRequest...";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        if (gameAct != null) {
                            Presenter.TileSwapRequest(HandsAdapter.SwapTileList);
                            tileSwapButton.setText("Swap Tile");
                        }
                    } else {
                        if (HandsAdapter.SELECTED_TILE != null) {
                            Tile tile = HandsAdapter.SELECTED_TILE;
                            tileSwapButton.setText("Swap Tile [ " + (HandsAdapter.SwapTileList.size()+1) + " ]");
                            HandsAdapter.SwapTileList.add(tile);
                            for (Player p : mGameData.getPlayers()) {
                                if (p.getId() == Presenter.clientID) {
                                    List<Tile> hand = p.getTiles();
                                    for (Tile handtile : hand) {
                                        if (handtile.getId() == tile.getId()) {
                                            hand.remove(handtile);
                                            mGameData.updatePlayerHands(hand);
                                            HandsAdapter.SELECTED_TILE = null;
                                            break;
                                        }
                                    }
                                    break;
                                }

                            }
                            return;
                        }
                        showToast("Cannot swap tiles with played tile");
                    }
                }
            }
        });

        playTilesButton = view.findViewById(R.id.play_tiles_button);
        playTilesButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click on Play Tiles Button in GameActivity.
             * Lets the Client send a Message containing the information required for a Play-Tiles
             *
             * @param v pressed view (in this case bag button)
             */
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                CharSequence text = "Sending PlayTilesRequest...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                if(gameAct != null)
                {
                    if(BoardFragment.placedBoardTiles.isEmpty()){
                        showToast(" NO TILES SELECTED!");
                        return;
                    }
                    Collection<TileOnPosition> topcoll = new ArrayList<>();
                    for(BoardTile tile : BoardFragment.placedBoardTiles){
                        topcoll.add(tile.toTileOnPosition());
                    }
                    if(BoardFragment.placedBoardTiles.size() > 0){
                        boardFrag.board.removeTiles(BoardFragment.placedBoardTiles);
                    }
                    Presenter.PlayTiles(topcoll);
                    if(HandsAdapter.SELECTED_TILE != null){
                        HandsAdapter.SELECTED_TILE = null;
                    }
                    if(BoardFragment.SELECTED_BOARD_TILE != null) {
                        BoardFragment.SELECTED_BOARD_TILE = null;
                    }
                    BoardFragment.Horizontal = false;
                    BoardFragment.Vertical = false;
                    BoardFragment.setdirection = false;
                }
            }
        });

        unselectButton = view.findViewById(R.id.unselect_button);
        unselectButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click on Play Tiles Button in GameActivity.
             * Lets the Client send a Message containing the information required for a Play-Tiles
             *
             * @param v pressed view (in this case bag button)
             */
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                CharSequence text = "UNSELECTED TILE";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                HandsAdapter.SELECTED_TILE = null;
                }

        });

        clear_tiles = view.findViewById(R.id.clear_tiles);
        clear_tiles.setOnClickListener(new View.OnClickListener() {
            /**
             * Used to clear move selection
             * @param v
             */
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                CharSequence text = "CLEARING MOVE...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                List<Tile> addback = new ArrayList<>();
                List<BoardTile> toRemove = new ArrayList<>();
                if(BoardFragment.placedBoardTiles.size() > 0) {
                    Log.d("CLEARING", "CLEARINGTEST, SIZE OF PLACED :" + BoardFragment.placedBoardTiles);
                    for (BoardTile tile : BoardFragment.placedBoardTiles) {
                        toRemove.add(tile);
                        addback.add(new Tile(tile.toTileOnPosition().getTile()));
                    }
                    BoardFragment.placedBoardTiles.clear();
                    Log.d("CLEARING", "CLEARINGTEST, SIZE OF TOREMOVE :" + BoardFragment.placedBoardTiles);
                    boardFrag.board.removeTiles(toRemove);
                    Log.d("CLEARING", "CLEARINGTEST, SIZE OF ADDBACK :" + BoardFragment.placedBoardTiles);
                    gameAct.addToPlayerHand(addback);
                    BoardFragment.Horizontal = false;
                    BoardFragment.Vertical = false;
                    BoardFragment.setdirection = false;
                    return;
                }
                if(HandsAdapter.SwapTileList.size()>0){
                    addback = HandsAdapter.SwapTileList;
                    gameAct.addToPlayerHand(addback);
                    HandsAdapter.SwapTileList.clear();
                    tileSwapButton.setText("Swap Tile");
                }

            }
        });

        place_bot_b = view.findViewById(R.id.bot_button);
        place_bot_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGameData.getActivePlayer().getId() == Presenter.clientID && HandsAdapter.SwapTileList.size() == 0) {
                    if (HandsAdapter.SELECTED_TILE != null) {
                        Tile tile = HandsAdapter.SELECTED_TILE;
                        if (BoardFragment.SELECTED_BOARD_TILE != null) {
                            BoardTile bt = BoardFragment.SELECTED_BOARD_TILE;
                            boolean worked = boardFrag.place_bot(new BoardTile(new TileOnPosition(bt.getX(), bt.getY(), tile.toInterfaceTile())));
                            if (worked) {
                                HandsAdapter.SELECTED_TILE = null;
                                for (Player p : mGameData.getPlayers()) {
                                    if (p.getId() == Presenter.clientID) {
                                        List<Tile> hand = p.getTiles();
                                        for (Tile handtile : hand) {
                                            if (handtile.getId() == tile.getId()) {
                                                hand.remove(handtile);
                                                mGameData.updatePlayerHands(hand);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });




        place_top_b = view.findViewById(R.id.top_button);
        place_top_b.setOnClickListener(new View.OnClickListener() {
            /**
             * Button used to place a Tile left of the Selected Tile
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mGameData.getActivePlayer().getId() == Presenter.clientID && HandsAdapter.SwapTileList.size() == 0) {
                    if (HandsAdapter.SELECTED_TILE != null) {
                        Tile tile = HandsAdapter.SELECTED_TILE;
                        if (BoardFragment.SELECTED_BOARD_TILE != null) {
                            BoardTile bt = BoardFragment.SELECTED_BOARD_TILE;
                            boolean worked = boardFrag.place_top(new BoardTile(new TileOnPosition(bt.getX(), bt.getY(), tile.toInterfaceTile())));
                            if (worked) {
                                HandsAdapter.SELECTED_TILE = null;
                                for (Player p : mGameData.getPlayers()) {
                                    if (p.getId() == Presenter.clientID) {
                                        List<Tile> hand = p.getTiles();
                                        for (Tile handtile : hand) {
                                            if (handtile.getId() == tile.getId()) {
                                                hand.remove(handtile);
                                                mGameData.updatePlayerHands(hand);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

        });


        place_left_b = view.findViewById(R.id.left_button);
        place_left_b.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                if (mGameData.getActivePlayer().getId() == Presenter.clientID && HandsAdapter.SwapTileList.size() == 0) {
                                                    if (HandsAdapter.SELECTED_TILE != null) {
                                                        Tile tile = HandsAdapter.SELECTED_TILE;
                                                        if (BoardFragment.SELECTED_BOARD_TILE != null) {
                                                            BoardTile bt = BoardFragment.SELECTED_BOARD_TILE;
                                                            boolean worked = boardFrag.place_left(new BoardTile(new TileOnPosition(bt.getX(), bt.getY(), tile.toInterfaceTile())));
                                                            if (worked) {
                                                                HandsAdapter.SELECTED_TILE = null;
                                                                for (Player p : mGameData.getPlayers()) {
                                                                    if (p.getId() == Presenter.clientID) {
                                                                        List<Tile> hand = p.getTiles();
                                                                        for (Tile handtile : hand) {
                                                                            if (handtile.getId() == tile.getId()) {
                                                                                hand.remove(handtile);
                                                                                mGameData.updatePlayerHands(hand);
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        });

        place_right_b = view.findViewById(R.id.right_button);
        place_right_b.setOnClickListener(new View.OnClickListener() {
            /**
             * Button used to place a Tile to the right of the Selected board tile
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mGameData.getActivePlayer().getId() == Presenter.clientID && HandsAdapter.SwapTileList.size() == 0) {
                    if (HandsAdapter.SELECTED_TILE != null) {
                        Tile tile = HandsAdapter.SELECTED_TILE;
                        if (BoardFragment.SELECTED_BOARD_TILE != null) {
                            BoardTile bt = BoardFragment.SELECTED_BOARD_TILE;
                            boolean worked = boardFrag.place_right(new BoardTile(new TileOnPosition(bt.getX(), bt.getY(), tile.toInterfaceTile())));
                            if (worked) {
                                HandsAdapter.SELECTED_TILE = null;
                                for (Player p : mGameData.getPlayers()) {
                                    if (p.getId() == Presenter.clientID) {
                                        List<Tile> hand = p.getTiles();
                                        for (Tile handtile : hand) {
                                            if (handtile.getId() == tile.getId()) {
                                                hand.remove(handtile);
                                                mGameData.updatePlayerHands(hand);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Button used to place a Tile when there is no tile on the Board
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mGameData.getActivePlayer().getId() == Presenter.clientID && HandsAdapter.SwapTileList.size() == 0) {
                    if (HandsAdapter.SELECTED_TILE != null) {
                        Tile tile = HandsAdapter.SELECTED_TILE;
                        boolean worked = boardFrag.place_start(new BoardTile(new TileOnPosition(0, 0, tile.toInterfaceTile())));
                        if (worked) {
                            HandsAdapter.SELECTED_TILE = null;
                            for (Player p : mGameData.getPlayers()) {
                                if (p.getId() == Presenter.clientID) {
                                    List<Tile> hand = p.getTiles();
                                    for (Tile handtile : hand) {
                                        if (handtile.getId() == tile.getId()) {
                                            hand.remove(handtile);
                                            mGameData.updatePlayerHands(hand);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        return view;
    }


    public void showToast(String msg)
    {
        Context context = getActivity();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Updates Hands when joining an already running game.
     * Makes sure that GameActivity is already created so that Context is available.
     *
     * @param bundle    saved state if fragment was already active before (not used here)
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        if (mGameData != null) {
            mRecyclerView.swapAdapter(new HandsAdapter(getContext(), mGameData.getPlayers()), false);
        }
    }

    // this has to be called, before updatePlayerHands() is called
    public void setupOnJoin(GameData gameData) {
        this.mGameData = gameData;
        gameData.listenToGameData(this);
    }

    public void updatePlayerHands() {
        if (mGameData != null) {
            List<Player> temp = mGameData.getPlayers();
            mRecyclerView.swapAdapter(new HandsAdapter(getContext(), temp), false);
        }
    }

    public void onGameDataUpdate() {
        updatePlayerHands();
    }

    public void registerActivity(GameActivity pActivity, int pClientID)
    {
        this.gameAct = pActivity;
        this.clientID = pClientID;
    }

    public BoardFragment getBoardFrag() {
        return boardFrag;
    }

    public void setBoardFrag(BoardFragment boardFrag) {
        this.boardFrag = boardFrag;
    }

    public void setGA(GameActivity gameActivity) {
        this.gameAct = gameActivity;
    }
}
