package _GUI_Application;

import java.util.Iterator;

import client.Client;
import game_elements.Game;

@SuppressWarnings("restriction")
public class AusrichterUpdater {
	
	public void newGameEvent(Game game) {
		Ausrichter.games.add(game);
	}

	public void clientConnectEvent(Client c) {
		Ausrichter.clients.add(c);
	}

	public void clientDisconnectEvent(Client c) {
		int id = c.getClientId();
		for (Client c1 : Ausrichter.clients) {
			if (c1.getClientId() == id) {
				Ausrichter.clients.remove(c1);
				break;
			}
		}
		Iterator<Client> clientit = Ausrichter.server.getClients().iterator();
		while(clientit.hasNext()) {
			Client client = clientit.next();
			if(client.getClientId() == c.getClientId()) {
				clientit.remove();
			}
		}
	}
	
	public void serverdown() {
		Ausrichter.games.clear();
		Ausrichter.clients.clear();
	}
}