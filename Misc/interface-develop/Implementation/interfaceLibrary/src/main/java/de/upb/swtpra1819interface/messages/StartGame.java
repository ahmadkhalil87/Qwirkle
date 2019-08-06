package de.upb.swtpra1819interface.messages;

import java.util.ArrayList;

public class StartGame extends Message {
	
	private Configuration config;
	private ArrayList<Client> clients;
	
	public static final int uniqueID = 400;
	
	public StartGame(Configuration config, ArrayList<Client> clients) {
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

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}


}