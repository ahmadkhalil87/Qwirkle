package game_elements;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.Client;
import de.upb.swtpra1819interface.messages.*;
import adjusted_messages.*;
import adjusted_messages.GameJoinAccepted;
import adjusted_messages.SpectatorJoinAccepted;
import adjusted_messages.StartGame;
import adjusted_messages.Winner;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.parser.Parser;
import enumeration.ControlParameters;
import gameValidation.CheckProcessObject;
import gameValidation.TileOnPositionEX;
import server.MessagesFromClientCaseHandling;

import gameboard.Field;
import gameboard.TileOnPosition;

/**
 * This class acts as a Model for a Game
 * @author Lukas Tietz
 *
 */
public class Game {

	/**
	 * Control Elements
	 */
	transient Logger logger = LogManager.getLogger();
	transient TimeThread Timer;

	/**
	 * Message Queues
	 */
	private transient LinkedBlockingQueue<ClientMessage> gameMessages;
	private transient LinkedBlockingQueue<ClientMessage> participantMessages;
	
	/**
	 * Parser
	 */
	private transient Parser parser = new Parser();

	/**
	 * Game Information
	 */
	private int gameId;
	private String gameName;
	private GameState gameState;
	private boolean isTournament;
	private Configuration config;
	private Collection<Client> players;
	

	/**
	 * Additional Serverside information
	 */
	private transient LinkedBlockingQueue<Client> playerturns;
	private transient ArrayList<Client> clients;
	private transient Map<Client, Integer> ScoreBoard;
	private transient Map<Client, ArrayList<Tile>> playerHands;
	private transient Client currentplayer;
	private transient String playerInGame;

	/**
	 * The Field and Bag
	 */
	private transient Bag bag;
	private transient Field playboard;


	public Game(int gameID, String gameName, GameState gameState, boolean isTournament, Configuration config) {

		this.gameId = gameID;
		this.gameName = gameName;
		this.gameState = gameState;
		this.isTournament = isTournament;
		this.players = new ArrayList<>();
		this.config = config;
		this.Timer = new TimeThread(this);

		this.ScoreBoard = new HashMap<>();
		this.clients = new ArrayList<>();
		this.playerHands = new HashMap<>();
		this.playerturns = new LinkedBlockingQueue<Client>();
		this.gameMessages = new LinkedBlockingQueue<ClientMessage>();
		this.setParticipantMessages(new LinkedBlockingQueue<ClientMessage>());
		setPlayerInGame(players.size() + "/" + config.getMaxPlayerNumber());


//		ArrayList<Tile> tempTiles = new ArrayList<Tile>();
//		int tempID = 0;
//		for (int i = 0; i < config.getColorShapeCount(); i++)
//		{
//			for (int j = 0; j < config.getColorShapeCount(); j++)
//			{
//				for(int n = 0; n < config.getTileCount(); n++)
//				{
//					tempTiles.add(new Tile(i, j, tempID));
//					tempID++;
//				}
//			}
//		}
		
		logger.log(Level.getLevel("GAME"), "Game: " + this + " has been made {GAMEID: " + this.gameId + " }");
		InitiateThreads();

	}

	/**
	 * Instances the Game and starts all needed Threads to handle the Messages sent from the Clients
	 */
	public void InitiateThreads() {
		Thread gameMessageHandler = new Thread(new gameMessageHandling(this), "gameMessageHandler Thread of Game #"+ gameId);
		gameMessageHandler.setDaemon(true);
		gameMessageHandler.start();


		Thread participantHandler = new Thread(new participantMessageHandling(this), "participantMessageHandler Thread of Game #"+ gameId);
		participantHandler.setDaemon(true);
		participantHandler.start();
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameID(int gameID) {
		this.gameId = gameID;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setState(GameState gameState) {
		this.gameState = gameState;
	}

	public boolean isTournament() {
		return isTournament;
	}

	public void setTournament(boolean isTournament) {
		this.isTournament = isTournament;
	}

	public LinkedBlockingQueue<Client> getPlayerturns() {
		return playerturns;
	}

	public void setPlayerturns(LinkedBlockingQueue<Client> playerturns) {
		this.playerturns = playerturns;
	}

	public LinkedBlockingQueue<ClientMessage> getGameQueue(){
		return gameMessages;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public int getBagSize()
	{
		return this.bag.getSize();
	}

	public void addBagTiles(ArrayList<Tile> pTiles)
	{
		this.bag.addTile(pTiles);
	}

	public void addBagTile(Tile pTile)
	{
		this.bag.addTile(pTile);
	}

	public ArrayList<Tile> getBagTiles(int count)
	{
		return this.bag.getTiles(count);
	}

	public ArrayList<Tile> getAllBagTiles()
	{
		return this.bag.getAllTiles();
	}

	public LinkedBlockingQueue<ClientMessage> getGameMessages() {
		return gameMessages;
	}

	public void setGameMessages(LinkedBlockingQueue<ClientMessage> gameMessages) {
		this.gameMessages = gameMessages;
	}

	public Collection<Client> getPlayers() {
		return players;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	public Map<Client, Integer> getScoreBoard() {
		return ScoreBoard;
	}

	public Bag getBag() {
		return bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public Field getPlayboard() {
		return playboard;
	}

	public void setPlayboard(Field playboard) {
		this.playboard = playboard;
	}

	public void setParticipantMessages(LinkedBlockingQueue<ClientMessage> participantMessages) {
		this.participantMessages = participantMessages;
	}

	public Map<Client, ArrayList<Tile>> getPlayerHands() {
		return playerHands;
	}

	public Client getCurrentplayer() {
		return currentplayer;
	}

	public void setCurrentplayer(Client currentplayer) {
		this.currentplayer = currentplayer;
	}

	public LinkedBlockingQueue<ClientMessage> getParticipantMessages() {
		return participantMessages;
	}

	public String getPlayerInGame() {
		return playerInGame;
	}

	public void setPlayerInGame(String playerInGame) {
		this.playerInGame = playerInGame;
	}

	public CheckProcessObject playTile(ArrayList<TileOnPositionEX> newTiles) {
		CheckProcessObject testresult = this.playboard.processMove(newTiles, ControlParameters.Server);
		return testresult;
	}



	/**
	 * Method used to send a message to all of the games participants
	 * @param message
	 */
	public void sendAll(Message message) {
		for (Client client : clients) {
			client.sendMessage(message);
		}
	}

	/**
	 * Method used to connect clients, invoked by {@linkplain MessagesFromClientCaseHandling}
	 * @param client
	 */
	public void ConnectClient(Client client) {
		if (client.getClientType() == ClientType.PLAYER) {
			int playeramount = this.getPlayers().size();
			if (playeramount >= config.getMaxPlayerNumber()) {
				client.sendMessage(new AccessDenied("Game is already full.", playeramount));
				logger.log(Level.getLevel("GAME"), "Client: " + client + " tried to connect with wrong role." );
				setPlayerInGame(players.size() + "/" + config.getMaxPlayerNumber());
				return;
			}else {
				this.getPlayers().add(client);
				this.getClients().add(client);
				client.setIsIngame(true); client.setPlayer(true); client.setGame(this);
				client.sendMessage(new GameJoinAccepted(this));
				logger.log(Level.getLevel("GAME"), "Client: " + client + " has been succesfully connected as a PLAYER @" + this + " " + this.getClients());
				sendAll(new MessageSignal("has joined the game", client.clientformatter()));
			}
		}
		else {
			this.getClients().add(client);
			client.setIsIngame(true);
			client.setGame(this);
			client.setPlayer(false);
			client.setGame(this);
			client.sendMessage(new SpectatorJoinAccepted(this));
			logger.log(Level.getLevel("GAME"), "Client: " + client + " has been succesfully connected as a SPECTATOR @" + this );
			for(Client spectator : clients) {
				if(spectator.getClientType() == ClientType.SPECTATOR) {
					spectator.sendMessage(new MessageSignal("has joined the game", client.clientformatter()));
				}
			}
		}

	}

	/**
	 * Method used to disconnect a client when he wants to leave
	 * @param scc
	 * @throws InterruptedException 
	 */
	public void disconnectClient(Client c) throws InterruptedException {
		int clientID = c.getClientId();

		if(c.isPlayer()) {
			Iterator<Client> playerit = players.iterator();
			while(playerit.hasNext()) {
				Client client = playerit.next();
				int clientid = client.getClientId();
				if(clientid == clientID) {
					playerit.remove();
					break;
				}
			}
			
			Iterator<Client> clientit = clients.iterator();
			while(clientit.hasNext()) {
				Client client = clientit.next();
				int clientid = client.getClientId();
				if(clientid == clientID) {
					clientit.remove();
					break;
				}
			}
			
			
			if(gameState != GameState.NOT_STARTED) {
				Iterator<Client> playerhandsit = playerHands.keySet().iterator();
				while(playerhandsit.hasNext()) {
					Client client = playerhandsit.next();
					int clientid = client.getClientId();
					if(clientid == clientID) {
						playerhandsit.remove();
						break;
					}
				}
				
				Iterator<Client> scoreboardit = ScoreBoard.keySet().iterator();
				while(scoreboardit.hasNext()) {
					Client client = scoreboardit.next();
					int clientid = client.getClientId();
					if(clientid == clientID) {
						scoreboardit.remove();
						break;
					}
				}
				
				if(players.size() < 2) {
					EndGame();
				}
			}
			c.setIsIngame(false);
			c.setPlayer(false);
			sendAll(new LeavingPlayer(c.clientformatter()));
			
		}
		else {
			Iterator<Client> clientit = clients.iterator();
			while(clientit .hasNext()) {
				Client client = clientit.next();
				int clientid = client.getClientId();
				if(clientid == clientID) {
					clientit.remove();
					break;
				}
			}
			c.setIsIngame(false);
		}
	}
		
	


	/**
	 * Method to be used by the Players/Observers of a Game.
	 * Sends the Message to the Games Messagehandling.
	 * @param cm
	 * @throws InterruptedException
	 */
	public void sendMessage(ClientMessage cm) throws InterruptedException {
		logger.log(Level.getLevel("GAME"), "Client: " + cm.getClient() + " { " + cm.getClient().getClientType() + " } " + "send Message to Game :" + this + " -> " + cm.getClientMessage());
		int id = cm.getClientMessage().getUniqueId();
		// Checking if the Message is a Disconnect or Pause message
		if (id <= 405) {
			gameMessages.put(cm);
			logger.log(Level.getLevel("GAME"), "Client: " + cm.getClient() + " [ " + cm.getClient().getClientName() + " ] " + " send GameMessage" + cm.getClientMessage() );
		}
		else {
			getParticipantMessages().put(cm);
		}
	}

	/**
	 * Method used to find out who is the next player,
	 * and draws new Tiles, if needed.
	 * @throws InterruptedException
	 */
	public void nextPlayerCheck() throws InterruptedException {
		Client nextplayer = this.playerturns.take();
		Client playhandclient = null;
		if (nextplayer.getClientId() == -1) {
			return;
		}
		
		Collection<Tile> playertiles = new ArrayList<Tile>();
		for(Client client : playerHands.keySet()) {
			if(client.getClientId() == nextplayer.getClientId()) {
				playertiles = playerHands.get(nextplayer);
				playhandclient = client;
			}
		}
		logger.log(Level.getLevel("GAME"), "PLAYERHANDS SIZE CONTROL PRINT : " + playertiles.size());
		if(playertiles.size() < this.config.getMaxHandTiles()) {

			// Bag is empty
			if(this.bag.isEmpty()) {
				for(Client player : players) {
					if (playerHands.get(nextplayer).size() != 0) {
						EndGame();
						return;
					}
					else {
						continue;
					}
				}
			}
			// Bag still has tiles
			else {
				int bagsizebefore = this.bag.getSize();
				Collection<Tile> drawtiles = new ArrayList<>();
				
				for (Tile tile : this.bag.getTiles(this.config.getMaxHandTiles() - playertiles.size())) {
					playertiles.add(tile);
					drawtiles.add(tile);
				}
				playerHands.put(playhandclient, (ArrayList<Tile>) playertiles);
				logger.log(Level.getLevel("GAME"), "[NEXTPLAYER CHECK] DRAWING NEW TILES : " + playertiles + " BAGSIZE {BEFORE = " + bagsizebefore + " | NOW} = " + this.getBagSize() );
			
				for(Client player : players) {
					if (player.getClientId() == nextplayer.getClientId()) {
						player.sendMessage(new SendTiles(drawtiles));
					}
				}
			}
		}
			// check if bag is empty after drawing
		try {
			assert(this.bag.getSize() != 0);
			} catch (AssertionError e) {
				for(Client player : players) {
					if (playerHands.get(player).size() == 0) {
						EndGame();
					}
					else {
						continue;
					}
				}
			}
			this.currentplayer = nextplayer;
			sendAll(new CurrentPlayer(nextplayer.clientformatter()));
			playerturns.add(nextplayer);
			this.Timer.startTurnTimer();
			sendAll(new TurnTimeLeftResponse(this.config.getTurnTime()));
		}


	/**
	 * Method called once the Game comes to an end by
	 * non-forced means.
	 * Tallies up players and decides winners and placements of the Players
	 */
	public void EndGame() {
		sendAll(new EndGame());
		Client winner = null;
		int i = 0;
		for (Client player : players) {
			int playerscore = 0;
			for(Client scoreboardclient : ScoreBoard.keySet()) {
				if(player.getClientId() == scoreboardclient.getClientId()) {
					playerscore = ScoreBoard.get(scoreboardclient);
				}
			}
			if(playerscore > i) {
				winner = player;
				i = playerscore;
			}
			else {
				continue;
			}

		sendAll(new Winner(winner, i, getScoreBoard()));
		setState(GameState.ENDED);
		}
	}

	/**
	 * Method used by the Ausrichter GUI to start the Game.
	 * @param collection
	 * @throws InterruptedException
	 */
	public void StartGame() throws InterruptedException {
		// Initiate Gametimer
		this.Timer.startTimer();

		// Notify everyone that game is starting
		this.setState(GameState.IN_PROGRESS);
		sendAll(new StartGame(this.config, this.players));

		// Sanity check : Is game actually fair?
		if (config.getMaxHandTiles()*players.size() > bag.getSize()) {
			AbortGame();
		}

		for (Client player : players) {
			ArrayList<Tile> playerhand = this.bag.getTiles(config.getMaxHandTiles());
			playerHands.put(player, playerhand);
			playerturns.add(player);
			ScoreBoard.put(player, 0);

			StartTiles stMsg = new StartTiles(playerHands.get(player));
			player.sendMessage(stMsg);
		}
		setState(GameState.IN_PROGRESS);
		// Game Starts
		nextPlayerCheck();
		logger.log(Level.getLevel("GAME"), "Game: " + this + " has been started: { PLAYERORDER: " + playerturns + " }" );
		logger.log(Level.getLevel("GAME"), "PLAYERHANDS ARE : " + playerHands  );
	}
	
	public void Overtime() throws InterruptedException {
		if(config.getSlowMove() == SlowMove.POINT_LOSS) {
			Iterator<Client> it2 = this.getScoreBoard().keySet().iterator();
			while(it2.hasNext()) {
				Client ScoreUpdateClient = it2.next();
				int id = ScoreUpdateClient.getClientId();
				if(id == currentplayer.getClientId()) {
					int score = this.getScoreBoard().get(ScoreUpdateClient);
					this.getScoreBoard().put(ScoreUpdateClient, score - config.getSlowMovePenalty());
					logger.info("[SCOREBOARD] UPDATED SCORE OF PLAYER : " + ScoreUpdateClient.getClientName() + " SCOREBOARD NOW: " + this.getScoreBoard());
				}
			}
		}
		else {
			disconnectClient(currentplayer);
		}
		nextPlayerCheck();
	}
	
	/**
	 * Method used by Ausrichter GUI to stop the Game
	 */
	public void AbortGame() {
		sendAll(new AbortGame());
		this.gameState = GameState.ENDED;
		//TODO IMPLEMENT THIS FURTHER
		logger.log(Level.getLevel("GAME"), "Game: " + this + " Aborts!" );
	}

	/**
	 * Used to Pause the Game
	 * @throws InterruptedException
	 */

	public void PauseGame() throws InterruptedException {
		setState(GameState.PAUSED);
		sendAll(new PauseGame());
		//TODO implement pausing Timer

	}

	/**
	 * Used to Resume the Game
	 * @throws InterruptedException
	 */
	public void ResumeGame() throws InterruptedException {
		setState(GameState.IN_PROGRESS);
		sendAll(new ResumeGame());
		//TODO implement unpausing Timer

	}

}
