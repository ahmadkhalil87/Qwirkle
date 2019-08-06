package de.upb.cs.swtpra_03.qwirkle.model.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.GameState;

/**
 * Holds intern representation of the current game.
 */
public class GameData {

    public static final int NOT_STARTED = 1;
    public static final int IN_PROGRESS = 2;
    public static final int PAUSED = 3;
    public static final int ENDED = 4;

    private int state;
    private boolean isTournament;
    private List<Player> players;
    private Player activePlayer;

    private List<GameDataListener> listeners = new ArrayList<>();

    /**
     * Constructor
     * Extracts needed information from LobbyGame object
     *
     * @param state            State of the game; e.g. in_progress, paused, etc.
     * @param isTournament     Type of the game
     */
    public GameData(GameState state, boolean isTournament) {

        this.isTournament = isTournament;

        switch (state) {
            case NOT_STARTED:
                this.state = NOT_STARTED;
                break;
            case IN_PROGRESS:
                this.state = IN_PROGRESS;
                break;
            case PAUSED:
                this.state = PAUSED;
                break;
            case ENDED:
                this.state = ENDED;
                break;
        }
    }


    /**
     * REWRITTEN FOR PLAYERCLIENT
     * @param newTileList
     */
    public void updatePlayerHands(List<Tile> newTileList) {
        // find player in player list and update his tiles
        for (Player curPlayer : players) {
            if (Presenter.clientID == curPlayer.getId()) {
                curPlayer.setTiles(newTileList);
                Log.d("UPDPH", "ADDING TILES TO PLAYER : " + curPlayer.getName() + " " + curPlayer.getTiles() );
            }
            else{
                curPlayer.setTiles(new ArrayList<>());
            }
        }

        notifyOnUpdate();
    }

    /**
     * ADDING TILES TO PLAYERHAND, THEN UPDATING
     * @param newTileList
     */
    public void addToPlayerHands(List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> newTileList){
        for (Player curPlayer : players) {
            if (Presenter.clientID == curPlayer.getId()) {
                List<Tile> handtiles = curPlayer.getTiles();
                for(Tile tile : newTileList){
                    handtiles.add(tile);
                }
                updatePlayerHands(handtiles);
                break;
            }

        }
    }
    /**
     * Updates the scores of every player
     *
     * @param scores    HashMap of Scores with Client as Key
     */
    public void updateScore(HashMap<Client, Integer> scores) {
        // iterate over every player and update score
        for (Map.Entry<Client, Integer> entry : scores.entrySet()) {
            int updatedScore = entry.getValue();
            this.getPlayerById(entry.getKey().getClientId()).setScore(updatedScore);
        }
        notifyOnUpdate();
    }

    private Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Handles the event of a leaving player
     *
     * @param id    ID of the leaving player
     */
    public void playerLeft(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                players.remove(player);
            }
        }
    }

    public int getState() {
        return this.state;
    }

    public void start() {
        if (state == NOT_STARTED) {
            state = IN_PROGRESS;
            notifyOnUpdate();
        }
    }

    public void pause() {
        if (state == IN_PROGRESS) {
            state = PAUSED;
            notifyOnUpdate();
        }
    }

    public void resume() {
        if (state == PAUSED) {
            state = IN_PROGRESS;
            notifyOnUpdate();
        }
    }

    public void end() {
        if (state == IN_PROGRESS || state == PAUSED) {
            state = ENDED;
            notifyOnUpdate();
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPlayersFromClients(List<Client> clients) {
        this.players = new ArrayList<>();
        for (Client client : clients) {
            this.players.add(new Player(client.getClientId(), client.getClientName()));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * sets currently active player as inactive and the player of given client as active
     *
     * @param client    client of new active player
     */
    public void setActivePlayer(Client client) {
        if (getActivePlayer() != null) {
            getActivePlayer().setActive(false);
        }

        for (Player player : getPlayers()) {
            if (player.getId() == client.getClientId()) {
                activePlayer = player;
                // used for gui changes in hands fragment
                player.setActive(true);
            }
        }
        notifyOnUpdate();
    }

    public interface GameDataListener {
        public void onGameDataUpdate();
    }

    public void listenToGameData(GameDataListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Notifies every listener of an update to the data.
     */
    private void notifyOnUpdate() {
        for (GameDataListener L : listeners) {
            L.onGameDataUpdate();
        }
    }
}
