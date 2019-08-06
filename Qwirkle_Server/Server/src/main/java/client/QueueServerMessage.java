package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

/**
 * This Class recieves the Servers messages and puts them into the Clients
 * Message Queue.
 * @author Lukas
 *
 */
class QueueServerMessage implements Runnable {
	
	Parser p = new Parser();
	Client client;
	BufferedReader input;
	
	public QueueServerMessage(Client client, BufferedReader input) {
		this.client = client;
		this.input = input;
	}
	public void run() {
		while (client.isOnline()) {
			try {
				String messagestring = input.readLine();
				Message message = p.deserialize(messagestring);
				System.out.println("( " + client.clientName + " ) Queuing new Server Message -> " + message + "\n");
				client.messages.put(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				client.disconnect("Servermessage Error");
			} catch (NullPointerException t) {
				t.printStackTrace();
				client.disconnect("Servermessage Error");
				break;
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {System.out.println(this + " Shutting Down");
			return;}
		
		}
	}
}
