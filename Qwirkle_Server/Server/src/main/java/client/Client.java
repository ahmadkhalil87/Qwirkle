package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.messages.Error;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.parser.Parser;
import game_elements.Game;


public class Client {
	
	// Server address
	private transient String HOST;
	
	// Port to listen to
	private transient int PORT;
	
	// Parser
	private transient Parser parser = new Parser();
	
	// The connection to the Server
	/*
	 * TRANSIENT FOR THE DISCONNECT SIGNAL, SO THAT IT DOESNT GET PARSED!
	 */
	private transient Socket ConnectionSocket;
	private transient BufferedReader input;
	private transient PrintWriter output;
	
	// All the messages from the Server will be queued here
	transient LinkedBlockingQueue<Message> messages;
	
	// Amount of time (in Milliseconds) for this client to respond of auth. and init.
	private transient static final int TIMEOUT = 5000;

	// Client - Status
	// This boolean shows if client is initiated
	private transient volatile boolean initiated;
	// And this boolean shows if client is connected to Server 
	private transient volatile boolean online = false;
	
	// Parameters that need to be set before connecting
	String clientName = "USER"; // USERNAME
	private ClientType clientType; // See Types @{ClientType}
	private int clientId;
	
	// Parameters used by the Server-Side Client to evaluate Lobby status
	private transient boolean isIngame;
	private transient boolean isPlayer;
	private transient Game game;
	
	/**
	 * Constructs a {@code Client} and tries to connect to the specified server
	 * 
	 * This Clinet starts by invoking the {@code start()} method, and stopped by invoking {@code shutDown()}
	 * 
	 * @param port
	 * 			The port to listen for (as int)
	 * @param serverAddress
	 * 			Address of the Hosting Server (as String)
	 */
	public Client(String serverAddress, int port) {
		this.HOST = serverAddress;
		this.PORT = port;
		messages = new LinkedBlockingQueue<>();
		initiated = true;
		System.out.println("Done Initializing");
	}
	
	/**
	 * Constructor used to make a dummy client class
	 * This is used for Parsing the Client serverside
	 * This Dummy is unable to connect, it is used as a Model.
	 * It has no functionality otherwise.
	 */
	public Client(String clientName, ClientType clientType, int clientId) {
		this.clientName = clientName;
		this.clientType = clientType;
		this.clientId = clientId;
	}
	
	/**
	 * Method used to initialize the connection to the Server
	 * @return
	 * @throws IOException
	 */
	public boolean start() throws IOException {
		if (isOnline() || !isInitiated()) //If Client is already connected OR client hasnt been initiated
			return false;

		Socket clientSocket = null;
		BufferedReader input = null;
		PrintWriter output = null;
		
		try {
			clientSocket = new Socket(HOST,PORT);
			System.out.println("Made New Socket: " + clientName + " " + clientSocket);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter(clientSocket.getOutputStream(), true);
			boolean accepted = AuthoriseYourself(clientSocket, input, output);
			System.out.println(accepted);
			assert(accepted == true);
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		catch (AssertionError ae) {
			clientSocket.close();
			input.close();
			output.close();
			return false;
		}
		
		online = true;
		
		Thread MessageHandler = new Thread(new ClientMessageHandler(this), "Message Handler thread");
		MessageHandler.setDaemon(true);
		MessageHandler.start();
		
		Thread ReadServerMessage = new Thread(new QueueServerMessage(this, this.input), "Client: " + clientName + "reading Thread");
		ReadServerMessage.setDaemon(true);
		ReadServerMessage.start();
		
		return true;
	}
	
	/**
	 * This Method is very important.
	 * It is used to complete the authorisation protocol with the Server
	 * Once this completes, the Client will be connected with the Server, and can start
	 * interacting with it.
	 * 
	 * @param socket
	 * @param input
	 * @param output
	 * @return The Connection established with the Server
	 * @throws IOException 
	 */
	private boolean AuthoriseYourself(Socket socket, BufferedReader input, PrintWriter output) throws IOException {
		
		try {
			socket.setSoTimeout(TIMEOUT);
		} catch (SocketException se) {
			se.printStackTrace();
		}
		
		try {
			// Waiting for Server's authorization message			
			input.readLine();
			// Responding
			output.println(parser.serialize(new ConnectRequest(clientName, clientType)));
			// Waiting for the Server's response to the ConnectRequest
			String serverResponse = input.readLine();
			// Making a special parser to get more information
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(serverResponse);
			JsonObject jo = je.getAsJsonObject();
			int messageID = jo.get("uniqueID").getAsInt();
			
			
			if (messageID == 101) {
				try {
					socket.setSoTimeout(0); // Connection successfull, no need to have a clientside-timeout now
					ConnectAccepted ca = new GsonBuilder().create().fromJson(serverResponse, ConnectAccepted.class);
					this.setClientID(ca.getClientId());
					setConnectionSocket(socket);
					setInput(input);
					setOutput(output);
					
				} catch(SocketException se) {
					se.printStackTrace();
				}
			}
			else {
				
				return false;
			}
		}finally {System.out.println("Authorization process concluded");}
		return true;
	}
		

	
	
	/**
	 * Disconnects the client from the Server.
	 * The Client itself remains active, and can connect again after invoking a new connection {@code connect()}
	 * @throws IOException 
	 */
	public void disconnect(String Reason) throws IOException {
		this.getConnectionSocket().close();
	}
	
	/**
	 * connects the client from the Server.
	 * @throws IOException 
	 */
	public void connect() throws IOException {
		start();
	}
	
	/**
	 * Shuts the Client down and also unblocks all blocked threads.
	 * If it has a Connection, shuts connection down, notifies Server.
	 * @throws IOException 
	 */
	public void shutDown() throws IOException {
		if (!isOnline()) {
			unsyncThreads();
			System.out.println("This is where System.Exit would close this Client :" + this);
		}
		else {
			disconnect("Client Shut down");
			unsyncThreads();
			System.out.println("This is where System.Exit would close this Client :" + this);
		}
	}

	/**
	 * Blocks calling Threads, until Client shuts down or unsyncs
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
	 * Blocks calling Threads for the given time, or until Client {@code shutDown()}
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
	 * Gets the Port of the Clients Socket.
	 * @return
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * Returns if the Client has been instanced, and thus is eligible to try and connect to the Server.
	 * 
	 * @return boolean
	 */
	public boolean isInitiated() {
			return initiated;
		}
	
	/**
	 * Returns if the Client is currently fully connected to Server
	 * @return boolean
	 */
	public boolean isOnline() {
		return online;
	}
	
	/**
	 * Gets the Clients name.
	 * Has to be assigned before connecting to the Server.
	 * @return {@code clientName}
	 */
	public String getClientName() {
		return this.clientName;
	}
	
	/**
	 * Sets the Clients name.
	 * Needs to be used before connecting to the Server.
	 * @param clientName
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	/**
	 * Sets the Clients Type.
	 * Used to differentiate the Clients state and possibilities 
	 * with Serverinteractions
	 * @param clientType
	 */
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	
	/**
	 * Gets the Clients Type.
	 * Has to be assigned before connecting to the Server.
	 * @return {@code clientType}
	 */
	public ClientType getClientType() {
		return clientType;
	}
	
	/**
	 * Sets the Input that recieves messages from the Server.
	 * Used to establish the input when connecting to the Server.
	 * @param BufferedReader input
	 */
	public void setInput(BufferedReader input) {
		this.input = input;
	}
	
	/**
	 * Gets the Input that recieves messages from the Server.
	 * @return the input
	 */
	public BufferedReader getInput() {
		return this.input;
	}
	
	/**
	 * Sets the Output that sends messages to the Server.
	 * Used to establish the output when connecting to the Server.
	 * @param PrintWriter output
	 */
	public void setOutput(PrintWriter output) {
		this.output = output;
	}
	
	/**
	 * Gets the Output that sends messages to the Server.
	 * @return the output
	 */
	public PrintWriter getOutput() {
		return output;
	}
	
	/**
	 * Sets the Socket that connects this client to the Server.
	 * Used to establish the connection socket when connecting to the Server.
	 * @param Socket
	 */
	public void setConnectionSocket(Socket socket) {
		this.ConnectionSocket = socket;
	}
	
	/**
	 * Gets the Socket that connects this client to the Server
	 * @param socket
	 * @return The Server connectionsocket
	 */
	public Socket getConnectionSocket() {
		return ConnectionSocket;
	}
	
	/**
	 * Method used to send a Message to the Server
	 * The ServerSide Version uses this Method, to send Messages back to the Client
	 * @param msg
	 */
	public void sendMessage(Message msg) {
		if(output == null)
			return;
		
		Parser p = new Parser();
		Message m = msg;
		String jsonmsg = p.serialize(m);
		System.out.println("SENDMESSAGE INVOKED! SENDING: " + msg + " " + jsonmsg);
		output.println(jsonmsg);
	}
	
	/**
	 * Method used to send a Error-Messages to the Server
	 * The ServerSide Version uses this Method, to send Messages back to the Client
	 * @param msg
	 */
	public void sendMessage(Error msg) {
		if(output == null)
			return;
		
		Parser p = new Parser();
		Message m = msg;
		String jsonmsg = p.serialize(m);
		System.out.println("SENDMESSAGE INVOKED! SENDING: " + msg + " " + jsonmsg);
		output.println(jsonmsg);
	}
	
	public void sendHands(Message msg) {
		if (output == null){
			return;
		}
		System.out.println("SENDMESSAGE INVOKED! SENDING :" + msg);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.enableComplexMapKeySerialization()
				.create();
		Type type = new TypeToken<PlayerHandsResponse>(){}.getType();
		String jsonString = gson.toJson(msg, type);
		System.out.println(jsonString);
		output.println(jsonString);
	}
	
	public int getClientId() {
		return clientId;
	}

	public void setClientID(int clientId) {
		this.clientId = clientId;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public boolean getIsIngame() {
		return isIngame;
	}

	public void setIsIngame(boolean isIngame) {
		this.isIngame = isIngame;
	}

	/**
	 * Method used Serverside to format clients to the Interface Client
	 * @return
	 */
	public de.upb.swtpra1819interface.models.Client clientformatter() {
		de.upb.swtpra1819interface.models.Client conformclient = new de.upb.swtpra1819interface.models.Client(this.getClientId(), this.getClientName(), this.getClientType());
		return conformclient;
	}
	
}//CLIENT CLASS FULL