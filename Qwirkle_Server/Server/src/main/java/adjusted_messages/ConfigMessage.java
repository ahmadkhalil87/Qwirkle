package adjusted_messages;

import client.Client;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Configuration;


public class ConfigMessage extends Message {
	private Configuration config;
	private String gameName;
	public static final int uniqueID = 1;
	
	public ConfigMessage(Configuration config, String Name) {
		super(uniqueID);
		this.setConfig(config);
		this.gameName = Name;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
