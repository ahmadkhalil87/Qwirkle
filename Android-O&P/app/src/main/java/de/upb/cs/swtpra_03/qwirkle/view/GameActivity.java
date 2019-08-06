package de.upb.cs.swtpra_03.qwirkle.view;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.controller.GameController;
import de.upb.cs.swtpra_03.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_03.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_03.qwirkle.model.game.HandsAdapter;
import de.upb.cs.swtpra_03.qwirkle.model.game.Player;
import de.upb.cs.swtpra_03.qwirkle.model.game.SectionsPageAdapter;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Handles UI of the game by holding different fragments for various tasks
 *
 * Interfaces:  GameController      Used for communication with server
 *              ChatFragment.OnChatFragmentListener
 *                                  Used to handle chat messages to be sent over the server
 *              BoardFragment.OnBoardFragmentListener
 *                                  Used to handle presses on board tiles
 */
public class GameActivity extends FragmentActivity implements GameController,
        ChatFragment.OnChatFragmentListener, BoardFragment.OnBoardFragmentListener {

    private static final String TAG = "GameActivity";

    private Presenter presenter;

    // objects concerning view
    private ViewPager sectionsPager;
    private TabLayout sectionsTabs;
    private ImageButton bagButton;
    private TextView tilesInBagView;

    private BagFragment bagFrag;
    private BoardFragment boardFrag;
    private ChatFragment chatFrag;
    private HandsFragment handsFrag;

    private CountDownTimer countDownTimer;
    private TextView statusView;

    // model objects
    private GameData mGameData;

    /**
     * Creates Layout and binds Views
     *
     * @param savedInstanceState    data from old instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bagFrag = new BagFragment();
        boardFrag = new BoardFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.top_fragment, boardFrag);
        ft.add(R.id.top_fragment, bagFrag);
        ft.hide(bagFrag);
        ft.commit();

        sectionsPager = findViewById(R.id.sectionsPager);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager(), this);
        sectionsPager.setAdapter(adapter);

        chatFrag = (ChatFragment) adapter.getItem(0);
        handsFrag = (HandsFragment) adapter.getItem(1);
        handsFrag.setBoardFrag(boardFrag);
        handsFrag.setGA(this);
        boardFrag.setGa(this);

        sectionsTabs = findViewById(R.id.sectionsTabs);
        sectionsTabs.setupWithViewPager(sectionsPager);

        statusView = findViewById(R.id.game_status);

        bagButton = findViewById(R.id.go_to_bag_button);
        bagButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click on BagButton in GameActivity.
             * Lets the Board Fragment appear and disappear
             *
             * @param v     pressed view (in this case bag button)
             */
            @Override
            public void onClick(View v) {
                if (true) {
                    return;
                }
                if (bagFrag.isVisible()){
                    onBackPressed();
                    Log.d(TAG, "Fragment Transaction: Hide Bag; Show Board");
                } else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.show(bagFrag);
                    ft.hide(boardFrag);
                    ft.addToBackStack(null);
                    ft.commit();
                    Log.d(TAG, "Fragment Transaction: Show Bag; Hide Board");
                }
            }
        });

        tilesInBagView = findViewById(R.id.bag_sum_of_tiles);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter = Presenter.getInstance();
        presenter.registerGameActivity(this);
        presenter.setActiveController(this);

        Game currentGame = presenter.getJoinedGame();

        if (currentGame.getGameState() != GameState.NOT_STARTED) {
            Log.d(TAG, "Setup running game");
            setupCurrentGameState(currentGame);
        } else {
            Log.d(TAG, "Setup waiting game");
            setupNewGameState(currentGame);
        }
    }

    @Override
    public void onClickSend(String msg) {
        presenter.messageSend(msg);
    }

    /**
     * Handles the event of a board tile being clicked.
     * Called by BoardFragment
     *
     * @param tile      Tile which has been clicked
     */
    public void onBoardTileClicked(BoardTile tile) {
        //TODO: Participant
    }

    /**
     * Creates Settings Button in App Bar
     * App Bar not visible at the moment
     *
     * @param menu  Layout which includes the App Bar Button
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // J.Insert the settings menu.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handles event of pressing the Setting Button in App Bar
     *
     * @param item  Item being pressed (in this case always Settings Button)
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

    /**
     * Handles incoming chat messages from the server.
     *
     * @param client    Client of the Player having sent the message
     * @param message   Message string
     */
    @Override
    public void addChatMessage(Client client, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatFrag.updateChat(client, message);
            }
        });
    }

    /**
     * Updates the board with new Tiles.
     *
     * @param updates   List of Tiles being added
     */
    @Override
    public void updateBoard(List<TileOnPosition> updates) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<BoardTile> tiles = new ArrayList<>();
                for (TileOnPosition tile : updates) {
                    tiles.add(new BoardTile(tile));
                }
                boardFrag.update(tiles);
            }
        });

        //presenter.scoreRequest();
        //presenter.playerHandRequest();
    }

    /**
     * ADDING TILES TO THE PLAYERS HAND
     * @param newTiles
     */
    @Override
    public void addToPlayerHand(List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> newTiles) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.addToPlayerHands(newTiles);
            }
        });

        //presenter.scoreRequest();
        //presenter.playerHandRequest();
    }


    /**
     * Updates Tiles which the players hold.
     *
     * @param newTiles
     */
    @Override
    public void updatePlayerHands(List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> newTiles) {
        // let mGameData update data
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.updatePlayerHands(newTiles);
            }
        });
    }

    /**
     * Updates the current active Player.
     *
     * @param client    new current active Player
     */
    @Override
    public void setCurrentPlayer(Client client) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.setActivePlayer(client);
            }
        });
        presenter.scoreRequest();
    }

    /**
     * Updates the Scores of each player.
     *
     * @param scores    HashMap of new scores with Clients as Key
     */
    @Override
    public void updateScore(HashMap<Client, Integer> scores) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.updateScore(scores);
            }
        });
    }

    /**
     * Updates tile count in the bag.
     *
     * @param bagtiles   Integer with amount of bagtiles
     */
    @Override
    public void updateBag(int bagtiles) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateBagCount(bagtiles);
            }
        });
    }

    @Override
    public void evaluateMove(boolean moveok, String message){
        if(moveok){
            BoardFragment.placedBoardTiles.clear();
        }else{
            List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> addback = new ArrayList<>();
            for(BoardTile tile : BoardFragment.placedBoardTiles){
                addback.add(new de.upb.cs.swtpra_03.qwirkle.model.game.Tile(tile.toTileOnPosition().getTile()));
            }
            addToPlayerHand(addback);
            BoardFragment.placedBoardTiles.clear();
        }
    }

    @Override
    public void evaluateSwap(TileSwapValid tsv){
        if(tsv.isValidation()){
            HandsAdapter.SwapTileList.clear();
        }else{
            ArrayList<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> addback = new ArrayList<>();
            for(de.upb.cs.swtpra_03.qwirkle.model.game.Tile tile : HandsAdapter.SwapTileList){
                addback.add(tile);
            }
            addToPlayerHand(addback);
        }
    }

    private void setupNewGameState(Game currentGame) {
        //Create GameData object
        this.mGameData = new GameData(currentGame.getGameState(), currentGame.isTournament());

        // Update view with status
        updateStatusView();
    }

    /**
     * sets up a new Game by creating a new GameData object and
     * calling each fragments initialization method.
     *
     * @param currentGame     Game which has been joined
     */
    private void setupCurrentGameState(Game currentGame) {
        // Create GameData object
        // Create GameData object
        this.mGameData = new GameData(currentGame.getGameState(), currentGame.isTournament());

        ArrayList<Player> players = new ArrayList<>();
        for (Client client : currentGame.getPlayers()) {
            players.add(new Player(client.getClientId(), client.getClientName()));
        }
        this.mGameData.setPlayers(players);

        // Setup all needed objects
        bagFrag.setupOnJoin(currentGame.getConfig());
        boardFrag.setupOnJoin(this.mGameData);
        handsFrag.setupOnJoin(this.mGameData);

        // Get information on game state from server
       /* presenter.gameDataRequest();
        presenter.playerHandRequest();
        presenter.bagRequest();*/
        presenter.turnTimeLeftRequest();

        // Update Views that depend on game information
        //updateBagCount();
        updateStatusView();
    }

    /**
     * Sets state of the game to be started
     */
    @Override
    public void startGame(Configuration configuration, ArrayList<Client> clients) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.start();
                mGameData.setPlayersFromClients(clients);

                bagFrag.setupOnJoin(configuration);
                boardFrag.setupOnJoin(GameActivity.this.mGameData);
                handsFrag.setupOnJoin(GameActivity.this.mGameData);
                
                updateStatusView();

                turnTimeLeftResponse(configuration.getTurnTime());
            }
        });
    }

    /**
     * Notifies game data that the game has ended and displays GameFinished screen
     */
    @Override
    public void endGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.end();
                Intent intent = new Intent(GameActivity.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivity.this.finish();
            }
        });
    }

    /**
     * Ends game by going back to lobby
     */
    @Override
    public void abortGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Testing
                Toast.makeText(GameActivity.this, "The game has been forcibly ended", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(GameActivity.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivity.this.finish();
            }
        });
    }

    /**
     * Sets state of the game to be paused
     */
    @Override
    public void pauseGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.pause();
                updateStatusView();
            }
        });
    }

    /**
     * Resumes the game after having been paused
     */
    @Override
    public void resumeGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.resume();
                updateStatusView();
            }
        });
    }

    /**
     * Sets the state of the game to paused or running
     *
     * @param gameState  Boolean if the state of the game is paused
     */
    @Override
    public void setGameState(GameState gameState) {
        // TODO switch
    }

    /**
     * Helper method to show state of the game in UI
     */
    private void updateStatusView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mGameData.getState()) {
                    case (GameData.NOT_STARTED):
                        statusView.setText("Waiting for game to start...");
                        break;
                    case (GameData.IN_PROGRESS):
                        statusView.setText("");
                        break;
                    case (GameData.PAUSED):
                        statusView.setText("Paused");
                        break;
                }
            }
        });
    }

    /**
     * Handles event of a different player leaving the game.
     *
     * @param id            ID of left player
     * @param username      User name of left player
     * @param clientType    Type of left player
     */
    @Override
    public void notifyPlayerLeft(int id, String username, ClientType clientType) {
        if (clientType == ClientType.PLAYER) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGameData.playerLeft(id);
                }
            });
            ;
            presenter.scoreRequest();
            presenter.gameDataRequest();
        }
    }

    /**
     * Handles the visualization of left turn time
     *
     * @param time  time left for the turn in milliseconds
     */
    @Override
    public void turnTimeLeftResponse(long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(time, 100) {

                    TextView countDownField = (TextView) findViewById(R.id.countDownTimer);
                    ProgressBar countDownBar = (ProgressBar) findViewById(R.id.progressBar2);

                    SimpleDateFormat sdf = new SimpleDateFormat("ss");

                    @Override
                    public void onTick(long millisUntilFinished) {
                        //J.Determine how much percent of the progress bar should be set
                        int percent = (int) ((((time + 1000.0) - millisUntilFinished) / 1000.0) * (100.0 / ((time / 1000) + 1)));

                        //J.Sets the Textfield and the progressbar for the clock
                        countDownField.setText("" + sdf.format(millisUntilFinished));
                        countDownBar.setProgress(percent);
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();
            }
        });

    }

    @Override
    public void handleNotAllowed(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleParsingError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleAccessDenied(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handles the visualization of total playing time
     *
     * @param time  total playing time in milliseconds
     */
    @Override
    public void totalTimeResponse(long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO totaltimerequest
            }
        });
    }

    public void updateBagCount(int bagtiles) {
        int newValue = bagtiles;
        tilesInBagView.setText(Integer.toString(newValue));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setActiveController(this);
    }

    @Override
    public void onDestroy() {
        presenter.leavingRequest();
        super.onDestroy();
    }

    /**
     * Called on leaving this Activity
     * Used here to destroy fragments in the layout, so they get recreated on next join
     */
    @Override
    public void finish() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(bagFrag);
        ft.remove(boardFrag);
        ft.commit();
        super.finish();
    }
}


