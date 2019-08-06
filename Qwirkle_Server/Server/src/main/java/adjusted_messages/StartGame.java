package adjusted_messages;

import java.util.Collection;

import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Configuration;

public class StartGame extends Message {
	
	private Configuration config;
	private Collection<Client> clients;
	
	public static final int uniqueID = 400;
	
	public StartGame(Configuration config, Collection<Client> clients) {
		super(uniqueID);
		this.config = config;
		this.clients = clients;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public Collection<Client> getClients() {
		return clients;
	}

	


}