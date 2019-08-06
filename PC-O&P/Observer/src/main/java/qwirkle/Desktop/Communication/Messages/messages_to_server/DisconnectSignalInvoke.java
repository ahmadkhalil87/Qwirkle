package qwirkle.Desktop.Communication.Messages.messages_to_server;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.Communication.Messages.messages_from_server.DisconnectSignal;

public class DisconnectSignalInvoke extends DisconnectSignal{

	public DisconnectSignalInvoke(String reason, Client client) {
		super(reason, client);
	}
	
}
