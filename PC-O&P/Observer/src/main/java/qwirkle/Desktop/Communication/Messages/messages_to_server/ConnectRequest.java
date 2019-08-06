package qwirkle.Desktop.Communication.Messages.messages_to_server;


import qwirkle.Desktop.enumeration.ClientType;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

public class ConnectRequest extends Message {

	private String clientName;
	private ClientType clientType;
	public static final int uniqueID = 100;

	public ConnectRequest(String clientName, ClientType clientType) {
		super(uniqueID);
		this.clientName = clientName;
		this.clientType = clientType;
	}

	public String getClientName() {
		return this.clientName;
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

}