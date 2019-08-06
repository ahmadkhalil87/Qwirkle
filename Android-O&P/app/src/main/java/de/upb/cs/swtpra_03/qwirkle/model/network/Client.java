package de.upb.cs.swtpra_03.qwirkle.model.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import de.upb.swtpra1819interface.messages.ConnectRequest;

import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

public class Client extends Thread {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private Presenter presenter;

    private Parser parser;

    private String username;
    private String ip;
    private int port;

    public Client(String pUsername, String pIP, int pPort, Presenter pPresenter) {
        username = pUsername;
        ip = pIP;
        port = pPort;

        presenter = pPresenter;

        parser = new Parser();
    }

    public void connectToServer() throws IOException {
        socket = new Socket(ip, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.start();

        ConnectRequest connectRequest = new ConnectRequest(username, ClientType.PLAYER);
        this.sendToServer(connectRequest);
    }

    public void run() {
        String receivedMessage;
        while(!this.isInterrupted()) {
            try {
                receivedMessage = reader.readLine();
                if (receivedMessage != null) {
                    Log.d("PROCESS RECIEVER", "MESSAGE CONTENTS :" + receivedMessage);
                    Message message = parser.deserialize(receivedMessage);

                    presenter.processMessage(message);
                }
            } catch (IOException e) {

            } catch (ParsingException e) {

            }
        }
    }

    public void sendToServer(Message pMessage) {
        String jsonString = parser.serialize(pMessage);
        Messager messager = new Messager(jsonString);
        messager.execute();
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    private class Messager extends AsyncTask<Void, Void, Void> {

        private String message;

        public Messager(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            writer.println(message);
            writer.flush();

            return null;
        }
    }
}

