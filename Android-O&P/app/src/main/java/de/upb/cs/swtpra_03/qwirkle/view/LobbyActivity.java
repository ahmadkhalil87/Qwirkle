package de.upb.cs.swtpra_03.qwirkle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import de.upb.cs.swtpra_03.qwirkle.controller.LobbyController;
import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.model.lobby.LobbyGame;
import de.upb.cs.swtpra_03.qwirkle.model.lobby.GameListingAdapter;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Game;

/**
 * Handles the view and behaviour of the lobby.
 * This is the activity shown right after connecting to the server.
 * The active games on the server will be displayed here.
 * The GameActivity will be started from here.
 *
 * Super Class: AppCompatActivity   Android Class for Activities with App Bar
 * Interfaces:  LobbyController     Used for communication with Presenter
 *              GameListingAdapter.OnClickJoinListener
 *                                  Let the Adapter notify this about a pressed button
 */
public class LobbyActivity extends AppCompatActivity implements LobbyController,
        GameListingAdapter.OnClickJoinListener{
    private static final String TAG = "Lobby Activity";

    private Presenter presenter;

    private RecyclerView gamesView;
    private ArrayList<LobbyGame> gamesList = new ArrayList<>();

    /**
     * Creates the activity by setting layout and binding UI Elements
     *
     * @param savedInstanceState used when activity saved data from the last instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        gamesView = findViewById(R.id.gameList);

        gamesView.setHasFixedSize(true);

        gamesView.setLayoutManager(new LinearLayoutManager(this));

        gamesView.setAdapter(new GameListingAdapter(this, gamesList));

        final Button refresh = findViewById(R.id.button_refresh);
        final Button disconnect = findViewById(R.id.button_disconnect);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.gameListRequest();
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    presenter.disconnect();
                } catch (IOException e) {

                }
                LobbyActivity.super.onBackPressed();
            }
        });

        presenter = Presenter.getInstance();
        presenter.registerLobbyActivity(this);

        presenter.gameListRequest();
    }

    /**
     * Called upon pressing the back button
     */
    @Override
    public void onBackPressed() {
        try {
            presenter.disconnect();
        } catch (IOException e) {

        }
        super.onBackPressed();
    }

    /**
     * Called when Activity reactivates after having been paused
     */
    @Override
    public void onResume() {
        super.onResume();
        presenter.setActiveController(this);
    }

    /**
     * Updates game list with active games
     * Called when server message with newest updates arrives
     *
     * @param games List of all games active at the moment
     */
    @Override
    public void updateGameList(ArrayList<Game> games) {
        runOnUiThread(() -> {
            gamesList = new ArrayList<>();
            gamesView.removeAllViews();

            for (Game game : games) {
                gamesList.add(new LobbyGame(game));
            }

            gamesView.swapAdapter(new GameListingAdapter(LobbyActivity.this, gamesList), false);
        });
    }

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game  the game that will be joined
     */
    @Override
    public void acceptSpectatorJoinRequest(Game game) {
        presenter.setJoinedGame(game);
        Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(intent);
    }

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game  the game that will be joined
     */
    @Override
    public void acceptGameJoinRequest(Game game) {
        presenter.setJoinedGame(game);
        Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleNotAllowed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleParsingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleAccessDenied(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Requests join from server when join button has been pressed
     * Called by GameListingAdapter
     *
     * @param lobbyGame Representation of the game which is to be joined
     */
    public void onClickedJoin(LobbyGame lobbyGame) {
        // TODO: Participants: Handle your join differently (or you join as spectator)
        presenter.gameJoinRequest(lobbyGame.getId());
    }

    /**
     * Inflates App Bar with Settings menu button
     * Called by Android at creation
     *
     * @param menu Layout which contains the Settings Button
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // J.Insert the settings menu.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handles presses on App Bar menu buttons
     * In our case only handles press on Settings button to start SettingsActivity
     *
     * @param item The pressed menu item, in our case always the settings button
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //J.Get the item Id
        int id = item.getItemId();

        //J.Start a Settings Activity if the menu is clicked
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

