package server;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import adjusted_messages.ClientMessage;
import client.Client;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.LeavingRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

/**
 * Handles the Messages being sent from the Client to the Server
 * @author Lukas
 *
 */
class QueueMessageToServer implements Runnable{

	Logger logger = LogManager.getLogger();
	Parser parser = new Parser();
	private BufferedReader input;
	private Client client;
	private Server server;
	private int errorcounter = 0;

	public QueueMessageToServer(Server server, Client client) {
		this.client = client;
		this.server = server;
		this.input = client.getInput();
	}

	@SuppressWarnings("finally")
	public void run() {
		
		try {
			logger.log(Level.getLevel("CLIENT"), "MessageThread established");
			while (server.isOnline()) {
				ClientMessage cm = null;
				String serializedmessage = input.readLine();
				if(serializedmessage != null) {
					Message messageclass = parser.deserialize(serializedmessage);
					logger.log(Level.getLevel("CLIENT"), "Client: {ID: " + this.client.getClientId() + " NAME: " + this.client.getClientName() + " } sent : { " + messageclass + " }" );
					logger.log(Level.getLevel("CLIENT"), serializedmessage );
					cm = new ClientMessage(client, messageclass);

					if (!client.getIsIngame()) {
						server.getMessages().put(cm);
						logger.log(Level.getLevel("CLIENT"), "Sending message to SERVER" );
					}
					else {
						client.getGame().sendMessage(cm);
						logger.log(Level.getLevel("CLIENT"), "Sending message to GAME { " + client.getGame() + " }" );
					}
					errorcounter = 0;
					continue;
				}
				else {
					continue;
				}
			}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return;

			} catch (ParsingException e) {
				logger.log(Level.getLevel("CLIENT"), "PARSINGERROR OCCURED" );

			// SocketConnection fails or Parsing error
			} catch (NullPointerException np) {
					if (errorcounter == 10) {
						logger.log(Level.getLevel("CLIENT"), "Nullpointer Error : " + np.getMessage());
					}
					else {
						errorcounter++;
					}
			}catch (InterruptedException ie) {
				if (errorcounter == 10) {
					logger.log(Level.getLevel("CLIENT"), "InterruptedException: " + ie.getMessage());
					return;
				}
				else {
					errorcounter++;
				}
				
			}
			finally {
					logger.log(Level.getLevel("CLIENT"),
							"MessageThread for Client: {ID: " + this.client.getClientId() + " NAME: " + this.client.getClientName() + " } shutting Down");
					if(client.getIsIngame()) {
						try {
							client.getGame().sendMessage(new ClientMessage(client, new LeavingRequest()));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					server.getUpdater().clientDisconnectEvent(client);
					return;
			}
		}
	}

