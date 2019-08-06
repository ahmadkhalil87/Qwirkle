package de.upb.cs.swtpra_03.qwirkle.model.lobby;

import de.upb.cs.swtpra_03.qwirkle.model.game.Player;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used to represent a game within the lobby.
 * It holds data provided by the server to use with the RecyclerView.
 */
public class LobbyGame {

    private int gameId;
    private String name;
    private int maxPlayerCount;
    private int curPlayerCount;
    private CurrentGameState state;
    private List<Player> players;

    // Change to ENUM; Use resources instead of plain strings for transability.
    private String type;

    /**
     * Constructor
     *
     * @param id             id of the game
     * @param name           name of the game
     * @param curPlayerCount how many players are in the game right now
     * @param maxPlayerCount how many players are allowed in this game
     * @param state          what state the game currently is in (e.g. running, ended, ...)
     */
    public LobbyGame(int id, String name, int curPlayerCount, int maxPlayerCount, GameState state) {
        this.gameId = id;
        this.name = name;
        this.maxPlayerCount = maxPlayerCount;
        this.curPlayerCount = curPlayerCount;
        this.type = "";
        this.players = new ArrayList<>();

        switch (state) {
            case NOT_STARTED:
                this.state = CurrentGameState.NOT_STARTED;
                break;
            case IN_PROGRESS:
                this.state = CurrentGameState.RUNNING;
                break;
            case PAUSED:
                this.state = CurrentGameState.PAUSED;
                break;
            case ENDED:
                this.state = CurrentGameState.FINISHED;
                break;
        }
    }

    /**
     * Constructor
     *
     * @param game Game object from interface class from which all information are extracted
     */
    public LobbyGame(Game game) {
        this.name = game.getGameName();
        this.maxPlayerCount = game.getConfig().getMaxPlayerNumber();
        this.curPlayerCount = game.getPlayers().size();
        this.gameId = game.getGameId();

        if (game.isTournament()) {
            this.type = "Tournament";
        } else {
            this.type = "Normal";
        }

        switch (game.getGameState()) {
            case NOT_STARTED:
                this.state = CurrentGameState.NOT_STARTED;
                break;
            case IN_PROGRESS:
            case PAUSED:
                this.state = CurrentGameState.RUNNING;
                break;
            case ENDED:
                this.state = CurrentGameState.FINISHED;
        }

        this.players = new ArrayList<>();
        for (Client client : game.getPlayers()) {
            players.add(new Player(client.getClientId(), client.getClientName()));
        }
    }

    public int getId() {
        return gameId;
    }
    /**
     * returns the game's name
     */
    public String getName() {
        return name;
    }

    /**
     * returns how many players can join the game
     */
    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    /**
     * returns how many players are currently in this game
     */
    public int getCurPlayerCount() {
        return curPlayerCount;
    }

    /**
     * sets how many players are currently in this game
     */
    public void setCurPlayerCount(byte CurPlayerCount) {
        this.curPlayerCount = CurPlayerCount;
    }

    /**
     * returns which state the game is currently in
     */
    public CurrentGameState getState() {
        return state;
    }

    /**
     * sets the current game's state
     */
    public void setState(CurrentGameState state) {
        this.state = state;
    }

    /**
     * returns which type of game this is
     */
    public String getType() {
        return type;
    }

    /**
     * set the list of players
     */
    public void setPlayers(List<Player> players){
        this.players = players;
    }

    /**
     * returns list of players
     */
    public List<Player> getPlayers() {
        return this.players;
    }
}

