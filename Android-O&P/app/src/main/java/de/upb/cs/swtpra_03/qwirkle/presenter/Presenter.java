package de.upb.cs.swtpra_03.qwirkle.presenter;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;

import de.upb.cs.swtpra_03.qwirkle.controller.Controller;
import de.upb.cs.swtpra_03.qwirkle.controller.GameController;
import de.upb.cs.swtpra_03.qwirkle.controller.GameFinishedController;
import de.upb.cs.swtpra_03.qwirkle.controller.LobbyController;
import de.upb.cs.swtpra_03.qwirkle.model.network.Client;


import de.upb.swtpra1819interface.messages.AbortGame;
import de.upb.swtpra1819interface.messages.AccessDenied;
import de.upb.swtpra1819interface.messages.BagRequest;
import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.EndGame;
import de.upb.swtpra1819interface.messages.GameDataRequest;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.GameListRequest;
import de.upb.swtpra1819interface.messages.GameListResponse;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.LeavingRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MessageSend;
import de.upb.swtpra1819interface.messages.MessageSignal;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.messages.PauseGame;
import de.upb.swtpra1819interface.messages.PlayerHandsRequest;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ResumeGame;
import de.upb.swtpra1819interface.messages.ScoreRequest;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.SpectatorJoinAccepted;
import de.upb.swtpra1819interface.messages.SpectatorJoinRequest;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.TotalTimeRequest;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftRequest;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import de.upb.swtpra1819interface.messages.GameJoinAccepted;
import de.upb.swtpra1819interface.messages.GameJoinRequest;
import de.upb.swtpra1819interface.messages.StartTiles;
import de.upb.swtpra1819interface.messages.SendTiles;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.PlayTiles;
import de.upb.swtpra1819interface.messages.TileSwapRequest;

public class Presenter extends Application {

    private static final String TAG = "Presenter";

    private static Presenter presenter;

    private static Client client;

    private Controller activeController;
    private LobbyController lobbyController;
    private GameController gameController;
    private GameFinishedController gameFinishedController;

    private Game joinedGame;

    public static int clientID;


    @Override
    public void onCreate() {
        super.onCreate();

        presenter = this;
        activeController = null;
        joinedGame = null;
    }

    public void registerLobbyActivity(LobbyController controller) {
        lobbyController = controller;
    }

    public void registerGameActivity(GameController controller) {
        gameController = controller;
    }

    public void registerGameFinishedActivity(GameFinishedController controller) {
        gameFinishedController = controller;
    }

    public void connectClientToServer(String username, String ip, int port) throws IOException {
        Log.d(TAG, "REQUEST: Connect to Server");
        client = new Client(username, ip, port, this);
        client.connectToServer();
    }

    /********* Einem Spiel beitreten und Ingame-Chat ****************/

    public void gameListRequest() {
        Log.d(TAG, "REQUEST: Game List");
        GameListRequest gameListRequest = new GameListRequest();
        client.sendToServer(gameListRequest);
    }

    public void gameJoinRequest(int selectedGame) {
        Log.d(TAG, "REQUEST: Player Join");
        GameJoinRequest gameJoinRequest = new GameJoinRequest(selectedGame);
        client.sendToServer(gameJoinRequest);
    }

    public void messageSend(String message) {
        Log.d(TAG, "REQUEST: Send Message");
        MessageSend messageSend = new MessageSend(message);
        client.sendToServer(messageSend);
    }

    /********* SPIELZUGE *************************/

    public static void PlayTiles(Collection<TileOnPosition> top){
        Log.d(TAG, "PLAYING TILES");
        PlayTiles pt = new PlayTiles(top);
        client.sendToServer(pt);
    }

    /************************* Spiellogik ****************************/

    public void leavingRequest() {
        Log.d(TAG, "REQUEST: Leaving");
        LeavingRequest leavingRequest = new LeavingRequest();
        client.sendToServer(leavingRequest);
    }

    public void scoreRequest() {
        Log.d(TAG, "REQUEST: Score Update");
        ScoreRequest scoreRequest = new ScoreRequest();
        client.sendToServer(scoreRequest);
    }

    public void turnTimeLeftRequest() {
        Log.d(TAG, "REQUEST: Turn time left");
        TurnTimeLeftRequest turnTimeLeftRequest = new TurnTimeLeftRequest();
        client.sendToServer(turnTimeLeftRequest);
    }

    public void totalTimeRequest() {
        Log.d(TAG, "REQUEST: Total time");
        TotalTimeRequest totalTimeRequest = new TotalTimeRequest();
        client.sendToServer(totalTimeRequest);
    }

    public void gameDataRequest() {
        Log.d(TAG, "REQUEST: Game Data");
        GameDataRequest gameDataRequest = new GameDataRequest();
        client.sendToServer(gameDataRequest);
    }

    public void playTiles(Collection tilestoplay) {
        Log.d(TAG, "REQUEST: Play Tiles");
        PlayTiles playTiles = new PlayTiles(tilestoplay);
        client.sendToServer(playTiles);
    }

    public static void TileSwapRequest(Collection tilestoswap) {
        Log.d(TAG, "REQUEST: Tile swap");
        TileSwapRequest tileSwapRequest = new TileSwapRequest(tilestoswap);
        client.sendToServer(tileSwapRequest);
    }

    public void processMessage(Message message) {
        int id = message.getUniqueId();
        Log.d(TAG, "Received Message ID: " + id);

        switch (id) {
            case 301:
                GameListResponse gameListResponse = (GameListResponse) message;
                lobbyController.updateGameList((ArrayList<Game>)gameListResponse.getGames());
                System.out.print("recieving gameList");
                break;
            case 305:
                SpectatorJoinAccepted spectatorJoinAccepted = (SpectatorJoinAccepted) message;
                // getGameId() means getGame()
                lobbyController.acceptSpectatorJoinRequest(spectatorJoinAccepted.getGameId());
                break;
            case 307:
                MessageSignal messageSignal = (MessageSignal) message;
                if(gameController != null) {
                    gameController.addChatMessage(messageSignal.getClient(), messageSignal.getMessage());
                }
                break;
            case 400:
                StartGame startGame = (StartGame) message;
                gameController.startGame(startGame.getConfig(), (ArrayList<de.upb.swtpra1819interface.models.Client>) startGame.getClients());
                Configuration config = startGame.getConfig();
                int bagtiles = (config.getTileCount()*config.getColorShapeCount()*config.getColorShapeCount()) - (config.getMaxHandTiles() * startGame.getClients().size());
                gameController.updateBag(bagtiles);
                break;
            case 401:
                EndGame endGame = (EndGame) message;
                gameController.endGame();
                break;
            case 402:
                AbortGame abortGame = (AbortGame) message;
                gameController.abortGame();
                break;
            case 403:
                PauseGame pauseGame = (PauseGame) message;
                gameController.pauseGame();
                break;
            case 404:
                ResumeGame resumeGame = (ResumeGame) message;
                gameController.resumeGame();
                break;
            case 406:
                LeavingPlayer leavingPlayer = (LeavingPlayer) message;
                gameController.notifyPlayerLeft(leavingPlayer.getClient().getClientId(), leavingPlayer.getClient().getClientName(), leavingPlayer.getClient().getClientType());
                break;
            case 407:
                Winner winner = (Winner) message;
                gameFinishedController.setWinner(winner.getClient().getClientId(), winner.getClient().getClientName(), winner.getScore());
                gameFinishedController.setLeaderboard((HashMap<de.upb.swtpra1819interface.models.Client, Integer>)winner.getLeaderboard());
                break;
            case 409:
                CurrentPlayer currentPlayer = (CurrentPlayer) message;
                gameController.setCurrentPlayer(currentPlayer.getClient());
                break;
            case 416:
                Update update = (Update) message;
                gameController.updateBoard((List<TileOnPosition>)update.getUpdates());
                gameController.updateBag(update.getNumberTilesInBag());
                break;
            case 418:
                ScoreResponse scoreResponse = (ScoreResponse) message;
                gameController.updateScore((HashMap<de.upb.swtpra1819interface.models.Client, Integer>) scoreResponse.getScores());
                break;
            case 420:
                TurnTimeLeftResponse turnTimeLeftResponse = (TurnTimeLeftResponse) message;
                gameController.turnTimeLeftResponse(turnTimeLeftResponse.getTime());
                break;
            case 422:
                TotalTimeResponse totalTimeResponse = (TotalTimeResponse) message;
                gameController.totalTimeResponse(totalTimeResponse.getTime());
                break;
                /*
            case 424:
                BagResponse bagResponse = (BagResponse) message;
                gameController.updateBag();
                break;*/
            /*
            case 426:sda
                PlayerHandsResponse playerHandsResponse = (PlayerHandsResponse) message;
                gameController.updatePlayerHands((HashMap<de.upb.swtpra1819interface.models.Client, ArrayList<Tile>>)playerHandsResponse.getHands());
                break;*/
            case 499:
                GameDataResponse gameDataResponse = (GameDataResponse) message;
                gameController.updateBoard((List<TileOnPosition>)gameDataResponse.getBoard());
                gameController.setCurrentPlayer(gameDataResponse.getCurrentClient());
                gameController.setGameState(gameDataResponse.getGameState());
                break;
            case 900:
                AccessDenied accessDenied = (AccessDenied) message;
                activeController.handleAccessDenied(accessDenied.getMessage());
                break;
            case 910:
                ParsingError parsingError = (ParsingError) message;
                activeController.handleParsingError(parsingError.getMessage());
                break;
            case 920:
                NotAllowed notAllowed = (NotAllowed) message;
                activeController.handleNotAllowed(notAllowed.getMessage());
                break;


            /*WIP*/
            case 101:
                ConnectAccepted connectAccepted = (ConnectAccepted) message;
                this.clientID = connectAccepted.getClientId();
                break;
            case 303:
                GameJoinAccepted gameJoinAccepted = (GameJoinAccepted) message;
                lobbyController.acceptGameJoinRequest(gameJoinAccepted.getGame());
                break;
            case 408:
                StartTiles startTiles = (StartTiles) message;
                List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> starttilelist = new ArrayList<de.upb.cs.swtpra_03.qwirkle.model.game.Tile>();
                for(Tile tile : startTiles.getTiles()){
                    starttilelist.add(new de.upb.cs.swtpra_03.qwirkle.model.game.Tile(tile));
                }
                gameController.updatePlayerHands(starttilelist);
                break;

            case 410:
                SendTiles sendTiles = (SendTiles) message;
                List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> sendtilelist = new ArrayList<de.upb.cs.swtpra_03.qwirkle.model.game.Tile>();
                for(Tile tile : sendTiles.getTiles()){
                    sendtilelist.add(new de.upb.cs.swtpra_03.qwirkle.model.game.Tile(tile));
                }
                gameController.addToPlayerHand(sendtilelist);
                break;
            case 412:
                TileSwapValid tileSwapValid = (TileSwapValid) message;
                gameController.evaluateSwap(tileSwapValid);
                break;
            case 413:
                TileSwapResponse tileSwapResponse = (TileSwapResponse) message;
                List<de.upb.cs.swtpra_03.qwirkle.model.game.Tile> swaptilelist = new ArrayList<de.upb.cs.swtpra_03.qwirkle.model.game.Tile>();
                for(Tile tile : tileSwapResponse.getTiles()){
                    swaptilelist.add(new de.upb.cs.swtpra_03.qwirkle.model.game.Tile(tile));
                }
                gameController.addToPlayerHand(swaptilelist);
                break;
            case 415:
                MoveValid moveValid = (MoveValid) message;
                gameController.evaluateMove(moveValid.isValidation(), moveValid.getMessage());
                break;
                /*WIP*/
        }
    }

    public void setActiveController(Controller controller) {
        activeController = controller;
    }

    public void setJoinedGame(Game joinedGame) {
        this.joinedGame = joinedGame;
    }

    public Game getJoinedGame() {
        return joinedGame;
    }

    public int getClientID(){return this.clientID;}

    public void disconnect() throws IOException {
        client.disconnect();
    }

    public static Presenter getInstance() {
        return presenter;
    }
}
