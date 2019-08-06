package de.upb.cs.swtpra_03.qwirkle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

public interface GameController extends Controller {

    public void addChatMessage(Client client, String message);
    public void updateBoard(List<TileOnPosition> updates);

    public void startGame(Configuration configuration, ArrayList<Client> clients);
    public void endGame();
    public void abortGame();
    public void pauseGame();
    public void resumeGame();

    public void notifyPlayerLeft(int id, String username, ClientType clientType);

    public void turnTimeLeftResponse(long time);
    public void totalTimeResponse(long time);
    public void updateBag(int bagtiles);
    public void updatePlayerHands(List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> newTiles);
    public void addToPlayerHand(List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> newTiles);
    public void setCurrentPlayer(Client client);
    public void updateScore(HashMap<Client, Integer> scores);
    public void setGameState(GameState gameState);

    public void evaluateMove(boolean validation, String message);

    public void evaluateSwap(TileSwapValid tileSwapValid);
}
