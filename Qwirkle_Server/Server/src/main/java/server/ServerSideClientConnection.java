package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import client.Client;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.models.ClientType;



/**
 * This Class creates Serverside Clients used to establish a connection.
 * This Class is not used directly, but passively, by recieving the needed values and 
 * establishing necessary threads and classes for further usage.
 * 
 * @author Lukas
 *
 */
public class ServerSideClientConnection{
	
	/**
	 * Server and Connection Params
	 */
	Server server;
	Socket clientSocket;
	BufferedReader input;
	PrintWriter output;
	
	/**
	 * Constructor to make a new Instance representing a clients connection.
	 * 
	 * @param socket
	 * 			The clients socket
	 * @param id
	 * 			The clients unique ID
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public ServerSideClientConnection(Socket clientsocket, Server server, int clientID, String clientName, ClientType clientType) throws IOException {
		if (clientsocket.isClosed())
			throw new IllegalStateException("Client #" + clientID + "closed his socket.");
		this.clientSocket = clientsocket;
		this.server = server;
		this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.output = new PrintWriter(clientSocket.getOutputStream(), true);
		
		Client serverClient = new Client(clientName, clientType, clientID);
		server.getUpdater().clientConnectEvent(serverClient);
		serverClient.setInput(input);
		serverClient.setOutput(output);
		clientonlineStart(serverClient);
		server.getClients().add(serverClient);
		serverClient.sendMessage(new ConnectAccepted(clientID));
	}
	
	/**
	 * Initiates the Thread that reads Messages from the Client to the Server, and lets server know, that a client is online
	 */
	public void clientonlineStart(Client client) {
		
		Thread read = new Thread(new QueueMessageToServer(this.server, client), "Client: { " + client.getClientId() + " || " + client.getClientName() + " } " + " Reading Thread");
		read.setDaemon(true);
		read.start();
		System.out.println(read);
	}
	
	
	
	
}
