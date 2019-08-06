package de.upb.cs.swtpra_03.qwirkle.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOBBY_ACTIVITY";

    private Presenter presenter;

    private EditText username;
    private EditText ipAddress;
    private EditText port;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        ipAddress = (EditText) findViewById(R.id.ip_adress);
        port = (EditText) findViewById(R.id.port);
        btnConnect = (Button) findViewById(R.id.btnconnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector connector = new Connector();
                connector.execute();
            }
        });

        presenter = Presenter.getInstance();
    }

    private class Connector extends AsyncTask<Void, Void, Void> {

        private boolean connected = false;

        private LoadingDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new LoadingDialog(LoginActivity.this, "Connecting to server...");
            dialog.showDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!username.getText().equals("") && !ipAddress.getText().equals("") && !port.getText().toString().equals("")) {
                    presenter.connectClientToServer(username.getText().toString(), ipAddress.getText().toString(), Integer.parseInt(port.getText().toString()));
                    //Connecting success
                    connected = true;
                }
            } catch (IOException e) {
                // Connecting failed
                connected = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (this.connected) {
                Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Unable to connect to server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
