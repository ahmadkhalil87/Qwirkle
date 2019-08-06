package server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import adjusted_messages.ClientMessage;
import adjusted_messages.ConfigMessage;
import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.parser.Parser;

/**
 * This Class is used to Manage the Servers Messages
 * It runs on the Message Threads that the Server got.
 * 
 * This class will constantly read the Messagequeue and
 * send the Messages to the right Methods or Handlers
 * 
 * @author Lukas
 *
 */
class ServerMessageHandling implements Runnable {
	
	Server server;
	Parser parser = new Parser();
	MessagesFromClientCaseHandling caseHandler;
	Logger logger = LogManager.getLogger(ServerMessageHandling.class);
	
	public ServerMessageHandling(Server server) {
		this.server = server;
		this.caseHandler = new MessagesFromClientCaseHandling(server);
	}
	
	public void run() {
		logger.log(Level.getLevel("MESSAGE"), "MessageHandler: " + this + " operational");
		while (server.online)
			try {
				ClientMessage msg = server.getMessages().take();
				if (msg == null)
					continue;
				
				logger.log(Level.getLevel("MESSAGE"), "new Message -> " + msg.getClientMessage() + " send by :" + msg.getClient().getClientName());
				Message message = msg.getClientMessage();
				int messageID = msg.getClientMessage().getUniqueId();
				Client client = msg.getClient();
				
				switch(messageID) {
				
				//GameListRequest
				case(300):
					this.caseHandler.GameListRequest(client);
					break;
				
				//GameJoinRequest
				case(302): 
					this.caseHandler.GameJoinRequest(message, client);
					break;
					
				// SpectatorJoinRequest
				case(304):
					this.caseHandler.SpectatorJoinRequest(message, client);
					break;
					
				// MessageSend
				case(306):
					this.caseHandler.MessageSend(message, client);
					break;	
				}	
			} catch (InterruptedException e) {
			} catch (RuntimeException rte) {
				rte.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
	}

}