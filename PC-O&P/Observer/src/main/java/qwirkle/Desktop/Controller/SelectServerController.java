package qwirkle.Desktop.Controller;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.enumeration.ClientType;
import qwirkle.Desktop.Main.Mainstarter;

import java.io.IOException;


public class SelectServerController {
		
	public static void connectToServer (String pName, String ipAddress, String portNr, boolean spectator) throws IOException{
		
		int port = Integer.parseInt(portNr);
		
		// Check if the given Informations are present
//		if (config.get("Player Name").equals(pName)
//				&& config.get("ip Address").equals(ipAddress)
//					&& config.get("port").equals(portNr)) {
//			System.out.println("Already Connected to this server");
//		}	
		
		Client meClient = new Client(ipAddress, port);
		Mainstarter.meClient = meClient;
		Mainstarter.meClient.setClientName(pName);
		if (spectator) {
			meClient.setClientType(ClientType.SPECTATOR);
		}else {
			meClient.setClientType(ClientType.PLAYER);
		}
		Mainstarter.meClient.start();

		if (Mainstarter.meClient.isOnline()) {
			return;
		}
	}
		
}
