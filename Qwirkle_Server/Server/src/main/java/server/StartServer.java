package server;

import java.util.Scanner;

public class StartServer {
	private Server server;
	
	public StartServer(int PORT) {
		this.server = new Server(8000, 5);
	}

	public Server getServer() {
		return server;
	}
	
}

