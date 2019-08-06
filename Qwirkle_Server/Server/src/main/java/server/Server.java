package server;


import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import _GUI_Application.AusrichterUpdater;
import adjusted_messages.ClientMessage;
import adjusted_messages.ConfigMessage;
import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Configuration;
import game_elements.Game;

import java.io.*;

public class Server {
	
	// Logger for logging purposes
	Logger logger = LogManager.getLogger();
	ServerSocket serverSocket;
	final int PORT;
	
	// Updating Tool used for the GUI
	private AusrichterUpdater updater;
	
	// Stores all running clients, mapped by their clientIDs
	
	private ArrayList<Client> Clients;

	// Queue for client-Messages sent to Server
	private LinkedBlockingQueue<ClientMessage> messages;
	
	// Message-Thread amount
	private int messageThreadcount;
	
	// Server - Status
	// This boolean shows if server is initiated
	private volatile boolean initiated;
	// And this boolean shows if server is online
	volatile boolean online;
	
	// Limit of allowed clients
	int clientLimit;
	
	// Amount of time (in Milliseconds) for a Client to respond of auth. and init.
	private static final int TIMEOUT = 5000;
	
	/**
	 * Everything regarding Games goes here
	 */
	Collection<Game> ActiveGames;
	private ArrayList<Game> Tournaments;
	public LinkedBlockingQueue<ConfigMessage> configurations;
	
	

	/**
	 * Constructs a {@code Server} listening for clients on specified port.
	 * Also declares the amount of Threads that handle messages.
	 * 
	 * !!Warning
	 * Having more then 1 messageThread might cause asynchronous messaging
	 * 
	 * This server starts by invoking the {@code start()} method, and stopped by invoking {@code shutDown()}
	 * 
	 * @param port
	 * 			The port to listen for
	 * @param messageThreadcount
	 * 			The amount of message handling threads
	 */
	public Server(int port, int messageThreadcount){
		this.PORT = port;
		this.Clients = new ArrayList<>();
		setMessages(new LinkedBlockingQueue<>());
		ActiveGames = new ArrayList<Game>();
		Tournaments = new ArrayList<>();
		configurations = new LinkedBlockingQueue<>();
		this.messageThreadcount = messageThreadcount;
		clientLimit = -1;
		initiated = true;
		logger.info("Server has been initialised");
	}
	
	public Server(int port) {
		this(port,1);
	}
	
	/**
	 * Starts the Server 
	 * 
	 * @return Initialisation went successfully or not.
	 */
	public boolean start() {
		if (!isInitiated() || isOnline()) {
			logger.error("Another instance of this Server is already running");
			return false;
		}
		
		try {
			serverSocket = new ServerSocket(this.PORT);
			logger.info("Server Socket has been established {PORT: " + this.PORT + " IP: " + this.serverSocket.getInetAddress());
			online = true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// Creating Threads for all the Message Handling
		logger.info("------------- Initialising Server Threads -------------");
		Thread ConnectHandler = new Thread(new ConnectHandler(this), "Client connection Thread");
		ConnectHandler.setDaemon(true);
		ConnectHandler.start();
		
		Thread GameMaker = new Thread(new GameMaker(this), "GameMaker Thread");
		GameMaker.setDaemon(true);
		GameMaker.start();
		
		for (int i=0; i<messageThreadcount; i++) {
			Thread m = new Thread(new ServerMessageHandling(this), "MessageHandler Thread #" + i);
			m.setDaemon(true);
			m.start();
		}
		logger.info("------------- [DONE] Initialising Server Threads -------------");
		return true; // Confirmation that Server has been initialised
	
	}
	
	/**
	 * Very important getter and setter Methods.
	 * Since this Server stops incoming connections once the limit has
	 * been reached and opens up again, once a client disconnects and the 
	 * limit has been freed, this parameter can be used to 
	 * enable more people to connect or less people to connect.
	 * 
	 * Set the parameter of the setter to a negative number, to disable the limit
	 * 
	 * @param limit
	 */
	public void setClientLimit(int limit) {
		clientLimit = limit;
	}
	
	public int getClientLimit() {
		return clientLimit;
	}
	
	/**
	 * Gets the TIMEOUT time (in Milliseconds).
	 * This Timer is used to time a sockets connection,
	 * and thus will help identifying foreign clients.
	 * @return Timeout time as int (in Milliseconds)
	 */
	public int getTimeOut() {
		return TIMEOUT;
	}
		
	
	public ArrayList<Game> getTournaments() {
		return Tournaments;
	}

	public Collection<Game> getActiveGames(){
		return ActiveGames;
	}
	
	public ArrayList<Client> getClients() {
		return Clients;
	}

	public void setClients(ArrayList<Client> clients) {
		Clients = clients;
	}

	/**
	 * Returns if the Server has been instanced, and thus is eligible to start.
	 * 
	 * @return boolean
	 */
	public boolean isInitiated() {
			return initiated;
		}
	
	/**
	 * Returns if the Server is currently online
	 * @return boolean
	 */
	public boolean isOnline() {
		return online;
	}
	
	/**
	 * Method used to check for clientIDs to make sure
	 * to not have duplicate IDs
	 * 
	 * @param id
	 * @return Boolean if ID is taken or not
	 */
	public boolean hasClientID(int id) {
		if (Clients == null) {return false;}
		for(Client client : Clients) {
			if(client.getClientId() == id) {
				return true;
			}else {
				continue;
			}
		}
		return false;
	}
	
	public LinkedBlockingQueue<ConfigMessage> getConfig(){
		return configurations;
	}
	
	public boolean hasGameID(int id) {
		for(Game game : ActiveGames) {
			if(game.getGameId() == id) {
				return true;
			}else {
				continue;
			}
		}
		return false;
	}
	
	public Game getGame(int id) {
		for(Game game : ActiveGames) {
			if(game.getGameId() == id) {
				return game;
			}else {
				continue;
			}
		}
		return null;
	}
	
	
	/**
	* Shuts down the whole server, including all its clients.
	* 
	* !!Warning
	* Once this Method is called, and server has been shut down, there 
	* is no way to recover this instance.
	* A new instance must be created to start a new Server.
	 * @throws IOException 
	* 
	*/
	public void shutDown() throws IOException {
		if (!isOnline())
			return;
		
		synchronized (this) {
			if (!isOnline())
				return;
			online = false;
			initiated = false;
			unsyncThreads();
			serverSocket.close();
			updater.serverdown();
			System.out.println("Shutting Down");
		}
	}
	
	/**
	 * Blocks calling Threads, until Server shuts down or unsyncs
	 * 
	 * @throws InterruptedException
	 * 			Thread interrupted while waiting
	 */
	public void syncThreads() throws InterruptedException {
		if (!online)
			return;
		
		synchronized (this) {
			if (!online)
				return;
			wait();
		}
	}
	
	/**
	 * Blocks calling Threads for the given time, or until Server {@code shutDown()}
	 * or {@code unsyncThreads()}
	 * 
	 * @param timeout
	 * 			in milliseconds
	 * @throws InterruptedException
	 * 			Thread interrupted while waiting
	 */			
	public void syncThreads(int timeout) throws InterruptedException{
		if (!online)
			return;
		synchronized(this) {
			if (!online)
				return;
			wait(timeout);
				
		}
	}
	
	/**
	 * Unblocks all blocked Threads, which got blocked by {@code syncThreads()}
	 */
	synchronized public void unsyncThreads() {
		notifyAll();
	}
	
	/**
	 * Send a Message to all clients that are connected to the Server
	 * and are currently in the Lobby.
	 * @param message
	 */
	public void sendAll(Message message) {
		for(Client client : Clients) {
			if(client.getIsIngame() == false) {
				client.sendMessage(message);
			}else {
				continue;
			}
		}
	}
	
	/**
	 * Send a Message to all Spectators that are in the Game
	 * @return
	 */

	public LinkedBlockingQueue<ClientMessage> getMessages() {
		return messages;
	}

	public void setMessages(LinkedBlockingQueue<ClientMessage> messages) {
		this.messages = messages;
	}

	public AusrichterUpdater getUpdater() {
		return updater;
	}

	public void setUpdater(AusrichterUpdater updater) {
		this.updater = updater;
	}

	
}// Server class

