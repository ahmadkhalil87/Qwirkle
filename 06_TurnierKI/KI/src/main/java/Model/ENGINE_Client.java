package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.ClientMessageHandler;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

public class ENGINE_Client extends Thread {
	// Server address
	private transient String HOST;
		
	// Port to listen to
	private transient int PORT;
		
	// The connection to the Server
	/*
	 * TRANSIENT FOR THE DISCONNECT SIGNAL, SO THAT IT DOESNT GET PARSED!
	 */
	private transient Socket ConnectionSocket;
	private transient BufferedReader input;
	private transient PrintWriter output;
	/**
	 * Parser
	 */
	private transient Parser parser = new Parser();
		
	// All the messages from the Server will be queued here
	private transient LinkedBlockingQueue<Message> messages;
	
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
	private int clientID;
		
	// Brain of the Whole operation and Logger
	private transient Logger logger = LogManager.getLogger();
	
	/**
	 * Constructor of the Engine Client
	 * Initiates the Class, login has to be called by the connect method
	 * @param serverAddress
	 * @param port
	 * @param main
	 */
	public ENGINE_Client(String serverAddress, int port) {
		this.HOST = serverAddress;
		this.PORT = port;
		setClientName("[ABC] Papabär");
		setClientType(ClientType.PLAYER);
		setMessages(new LinkedBlockingQueue<Message>());
		initiated = true;
		logger.log(Level.getLevel("CLIENT"), "INITIALIZED");
	}
	
	/**
	 * connects the client from the Server.
	 * @return 
	 * @throws IOException 
	 * @throws ParsingException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("resource")
	public boolean connect() throws IOException, ParsingException, InterruptedException {
		try {
			// Establishing Essentials for connecting to the Server
			ConnectionSocket = new Socket(HOST,PORT);
			input = new BufferedReader(new InputStreamReader(ConnectionSocket.getInputStream()));
			output = new PrintWriter(ConnectionSocket.getOutputStream(), true);
			logger.log(Level.getLevel("CONNECT"), "ESTABLISHED ESSENTIALS");
			
			// Sending ConnectRequest and Handling Response
			sendMessage(new ConnectRequest(this.clientName, this.clientType));
			logger.log(Level.getLevel("CONNECT"), "SENDING CONNECTREQUEST, HOOKED TO: " + ConnectionSocket.getRemoteSocketAddress());
			ConnectionSocket.setSoTimeout(TIMEOUT);
			String response = input.readLine();
			ConnectionSocket.setSoTimeout(0);
			Message message = parser.deserialize(response);
			logger.log(Level.getLevel("CONNECT"), "RECIEVED RESPONSE: " + message);
			
			// Checking for success
			assert(message.getUniqueId() == 101);
			
			// SUCCESSFULL
			ConnectAccepted CA = (ConnectAccepted) message;
			this.clientID = CA.getClientId();
			this.start();
			Thread MessageHandler = new Thread(new ClientMessageHandler(this), "Message Handler thread");
			MessageHandler.setDaemon(true);
			MessageHandler.start();
			logger.log(Level.getLevel("CONNECT"), "CONNECTION TO SERVER SUCCESSFULL, MY ID IS: " + this.clientID + " WAITING FOR GAMEJOIN");
			return true;
			
		}catch(UnknownHostException uhException){
			uhException.printStackTrace();
			return false;
		}catch(SocketException se) {
			logger.log(Level.getLevel("CONNECT"), "CONNECTING TIMED OUT, SOMETHING WENT WRONG");
			return false;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		catch (AssertionError ae) {
			//FAILURE
			logger.log(Level.getLevel("CONNECT"), "CONNECTION TO SERVER FAILED");
			ConnectionSocket.close();
			input.close();
			output.close();
			return false;
		}
	}
	
	/**
	 * Runs a Thread that puts ServerMessages on the Queue
	 */
	@SuppressWarnings("finally")
	public void run() {
		logger.info("MESSAGEQUEUER OPERATIONAL");
		try {
			while (!this.ConnectionSocket.isClosed()) {
				String messagestring = input.readLine();
				if(messagestring != null) {
					Message message = parser.deserialize(messagestring);
					logger.log(Level.getLevel("MESSAGE"), "Recieved new Message : { " + message + " } content : " + messagestring );
					getMessages().put(message);
				}
				else {
					if(!this.ConnectionSocket.isConnected()) {
						logger.log(Level.getLevel("MESSAGE"), "NOT CONNECTED ANYMORE!" );
						disconnect();
					}
					else {
						continue;
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException @" + this + " Shutting Connection down");
			disconnect();
			return;
		} catch (NullPointerException t) {
			t.printStackTrace();
			logger.error("NullPointerException @" + this + " SERVERSOCKET MUST'VE BEEN KILLED // Shutting Connection down");
			disconnect();
			return;
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		finally {logger.log(Level.getLevel("MESSAGE"), "MESSAGEQUEUER SHUTTING DOWN");
		return;}
	}
	
	/**
	 * Disconnects the client from the Server.
	 * The Client itself remains active, and can connect again after invoking a new connection {@code connect()}
	 * @throws IOException 
	 */
	public void disconnect() throws IOException {
		this.ConnectionSocket.close();
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
		logger.log(Level.getLevel("MESSAGE"), "SENDING MESSAGE : { " + msg + " } content : " + jsonmsg );
		output.println(jsonmsg);
	}
	
	public String getHOST() {
		return HOST;
	}

	public void setHOST(String hOST) {
		HOST = hOST;
	}

	public int getPORT() {
		return PORT;
	}

	public void setPORT(int pORT) {
		PORT = pORT;
	}

	public Socket getConnectionSocket() {
		return ConnectionSocket;
	}

	public void setConnectionSocket(Socket connectionSocket) {
		ConnectionSocket = connectionSocket;
	}

	public BufferedReader getInput() {
		return input;
	}

	public void setInput(BufferedReader input) {
		this.input = input;
	}

	public PrintWriter getOutput() {
		return output;
	}

	public void setOutput(PrintWriter output) {
		this.output = output;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public LinkedBlockingQueue<Message> getMessages() {
		return messages;
	}

	public void setMessages(LinkedBlockingQueue<Message> messages) {
		this.messages = messages;
	}

	public boolean isInitiated() {
		return initiated;
	}

	public void setInitiated(boolean initiated) {
		this.initiated = initiated;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

}
