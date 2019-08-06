package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.models.ClientType;


/**
 * This Class handles the Authentication and Connection process from a client to the Server.
 * To avoid a potential attack on the Server by overloading it with login requests, following
 * measures have been taken:
 * -Hangtime is set {@code TIMEOUT}
 * -Authentification is handled on a seperate thread for every client {@code AuthoriseYourself()}
 * -Connection requests are being taken on a seperate thread
 * 
 * @author Lukas
 *
 */
public class ConnectHandler implements Runnable {
	
	Logger logger = LogManager.getLogger(ConnectHandler.class);
	Server server;
	
	public ConnectHandler(Server server) {
		this.server = server;
	}
	public void run() {
		
		logger.log(Level.getLevel("CONNECT"), "Connecthandler operationable");
		while(server.isOnline() && !server.serverSocket.isClosed()) {
			Socket clientSocket = null;
			BufferedReader input = null;
			PrintWriter output= null;
		
		try {
			clientSocket = server.serverSocket.accept();
			logger.log(Level.getLevel("CONNECT"), "NEW CLIENT TRIES TO CONNECT FROM: " + clientSocket.getRemoteSocketAddress());
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter(clientSocket.getOutputStream(), true);
			AuthoriseYourself(clientSocket, input, output);
			
		} catch(RuntimeException rte) {
			rte.printStackTrace();
			try {
				server.shutDown();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // If RTE occurs here, there is a problem with the Servers code
		
		} catch(IOException ioe) {}
		
	   }
    }
	
	/**
	 * This Method checks if the Client is not a foreign Client by using a Connection-Protocol.
	 * If the Client is eligible to connect, a new {@code ServerClientConnection} class will be created, 
	 * and a Client will then be able to operate on server basis.
	 * @param socket
	 * @param input
	 * @param output
	 */
	private void AuthoriseYourself(final Socket socket, final BufferedReader input, final PrintWriter output) {
	// Creating a seperate Thread
		Thread auth = new Thread("Auth. Thread") {
			public void run() {
				
				if (!server.isOnline())
					return;
				
				ServerSideClientConnection client = null;
				int clientid = 0;
				
				try {
					
					// Making a new Parser to get information
					String crstring = input.readLine();
					logger.log(Level.getLevel("CONNECT"), "CLIENT SEND THIS MESSAGE: " + crstring);
					ConnectRequest connectrequest = new GsonBuilder().create().fromJson(crstring, ConnectRequest.class);
					
					//Parsing Clients information
					String username = connectrequest.getClientName();
					int messageID = connectrequest.getUniqueId();
					ClientType clientType = connectrequest.getClientType();
					
					// Setting Socket timeout to {@code TIMEOUT} 
					socket.setSoTimeout(server.getTimeOut()); 
					
					if (messageID == 100) {
						// Giving the Client a Unique ID
						while (clientid == 0 || server.hasClientID(clientid))
							clientid += 1;
						
		
						//Checking if there is still space on the Server
						if (server.clientLimit < 0 || server.clientLimit > (Integer) server.getClients().size()) {
							//Adding client to client list, and sending him a response
							client = new ServerSideClientConnection(socket, server, clientid, username, clientType);
							logger.log(Level.getLevel("CONNECT"), "Client: " + username + " IP: " + socket.getInetAddress() + " has been connected.");
						}
						else {
							logger.error("Client: " + username + " IP: " + socket.getInetAddress() + " has been failed during connection.");
							// TODO DECLINE MESSAGE
							socket.close();
							return;
						}
					}
					else {
						logger.error("Client with IP: " + socket.getRemoteSocketAddress() + " is using a foreign Client or sent wrong message");
						return;
					}
				} catch (SocketTimeoutException toe) {
					logger.error("Client with IP: " + socket.getRemoteSocketAddress()  + " is using a foreign Client and timed out");
				
				} catch (SocketException se) {
					logger.error("Client with IP: " + socket.getRemoteSocketAddress() + " is using a foreign Client and something went wrong");
					se.printStackTrace();
					return;
				
				} catch (IOException ioe) {
					logger.error("IOException during connection process with Client : " +  socket.getRemoteSocketAddress());
					ioe.printStackTrace();
					return;
				}
				try {
					socket.setSoTimeout(0);
				} catch (SocketException e) {
					
					e.printStackTrace();
				}
			}
		};
		auth.setDaemon(true);
		auth.start();
	}
	
}
