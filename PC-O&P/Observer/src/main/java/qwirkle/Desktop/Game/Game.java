package qwirkle.Desktop.Game;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.message_abstract.ClientMessage;
import qwirkle.Desktop.entity.Tile;
import qwirkle.Desktop.enumeration.GameState;

/**
 * This is the ClientSide Model that can be used to store Temporary data of a Game
 * for the View
 * 
 * @author Lukas
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
	 * Game Information
	 */
	private int gameId;
	private String gameName;
	private String tr;
	private GameState gameState;
	private boolean isTournament;
	private Configuration config;
	
	/**
	 * Client information
	 */
	public ArrayList<Client> players;
	private ArrayList<Client> spectators;
	private transient LinkedList<Client> playerturns;
	private transient ArrayList<Client> clients;
	public transient Map<Client, Collection<Tile>> playerHands;
	public transient Map<Client, Integer> playersScores;
	private transient Client currentplayer;
	
	/**
	 * The Field and Bag
	 */
	private transient Bag bag;
	public transient Field playboard;
	

	public Game(int gameID, String gameName, GameState state, 
					boolean tournament, ArrayList<Client> players, Configuration config) {

		this.gameId = gameID;
		this.gameName = gameName;
		this.gameState = state;
		this.isTournament = tournament;
		if (tournament)
			setTr("X");
		else
			setTr("");
		this.players = players;
		this.config = config;
		

		this.spectators = new ArrayList<>();
		this.playerturns = new LinkedList<Client>();
		this.gameMessages = new LinkedBlockingQueue<ClientMessage>();
		this.setParticipantMessages(new LinkedBlockingQueue<ClientMessage>());
		
		
		ArrayList<Tile> tempTiles = new ArrayList<Tile>();
		int tempID = 0;
		for (int i = 0; i < config.getColorShapeCount(); i++)
		{
			for (int j = 0; j < config.getColorShapeCount(); j++)
			{
				for(int n = 0; n < config.getTileCount(); n++)
				{
					tempTiles.add(new Tile(i, j, tempID));
					tempID++;
				}
			}
		}
		this.bag = new Bag(tempTiles);
		
		logger.log(Level.getLevel("GAME"), "Game: " + this + " has been made {GAMEID: " + this.gameId + " }" );
	}
	
	
	public String getTr() {
		return tr;
	}


	public void setTr(String tr) {
		this.tr = tr;
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

	public boolean getisTournament() {
		return isTournament;
	}

	public void setTournament(boolean isTournament) {
		this.isTournament = isTournament;
	}

	public LinkedList<Client> getPlayerturns() {
		return playerturns;
	}

	public void setPlayerturns(LinkedList<Client> playerturns) {
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

	public ArrayList<Client> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Client> players) {
		this.players = players;
	}




	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
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

	public Map<Client, Collection<Tile>> getPlayerHands() {
		return playerHands;
	}

	public void setPlayerHands(Map<Client, Collection<Tile>> playerHands) {
		this.playerHands = playerHands;
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
}
