package de.upb.cs.swtpra_03.qwirkle.view;



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

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.R;

import de.upb.cs.swtpra_03.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_03.qwirkle.model.game.HandsAdapter;
import de.upb.cs.swtpra_03.qwirkle.model.game.Player;
import de.upb.swtpra1819interface.models.Client;

/**
 * Provide UI Related Functions used for hands overview
 */
public class TEST extends Fragment implements
        GameData.GameDataListener{

    private RecyclerView mRecyclerView;

    private GameData mGameData;

    private int playerCount;

    private List<Client> clients;

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

        return view;
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
        mRecyclerView.swapAdapter(new HandsAdapter(getContext(), mGameData.getPlayers()), false);
    }

    public void onGameDataUpdate() {
        updatePlayerHands();
    }
}

