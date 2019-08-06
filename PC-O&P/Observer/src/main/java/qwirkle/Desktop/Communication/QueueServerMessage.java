package qwirkle.Desktop.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import qwirkle.Desktop.Communication.MessageHandlers.Parser;
import qwirkle.Desktop.Communication.MessageHandlers.ParsingException;
import qwirkle.Desktop.Communication.Messages.message_abstract.Message;

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
				client.getMessages().put(message);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					client.disconnect();
					break;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (NullPointerException t) {
				try {
					client.disconnect();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
}
