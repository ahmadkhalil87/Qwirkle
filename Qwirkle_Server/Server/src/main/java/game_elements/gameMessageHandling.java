package game_elements;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import _GUI_Application.AusrichterUpdater;
import adjusted_messages.ClientMessage;
import client.Client;
import de.upb.swtpra1819interface.models.GameState;


/**
 * This Class will run a Thread that once initiated will
 * handle Messages that aren't bound to the Game e.g. PauseGame, LeaveRequests
 * 
 * @author Lukas Tietz
 *
 */
class gameMessageHandling implements Runnable {
	
	MessageToGameCaseHandling caseHandler;
	Game game;
	Logger logger;
	
	public gameMessageHandling(Game game) {
		caseHandler = new MessageToGameCaseHandling(game);
		this.game = game;
		logger = game.logger;
	}
	
	public void run() {
		try {
			while (game.getGameState() != GameState.ENDED) {
				ClientMessage msg = game.getGameQueue().take();
				if (msg == null)
					continue;
				
				int messageID = msg.getClientMessage().getUniqueId();
				Client client = msg.getClient();
				
				switch(messageID) {
				
				// MessageSend
				case(306):
					this.caseHandler.MessageSend(msg.getClientMessage(), client);
					break;
					
				//LeaveRequest		
				case(405):
					game.disconnectClient(client);
					break;
			
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			logger.log(Level.getLevel("GAME"), "Game : " + this + " has come to an end or encountered a Problem, shutting down" );
		}
	}	
}
