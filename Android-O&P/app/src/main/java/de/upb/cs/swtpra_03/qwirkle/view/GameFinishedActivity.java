package de.upb.cs.swtpra_03.qwirkle.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.controller.GameFinishedController;
import de.upb.cs.swtpra_03.qwirkle.model.gameFinished.Player;
import de.upb.cs.swtpra_03.qwirkle.model.gameFinished.ScoreAdapter;
import de.upb.cs.swtpra_03.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;

public class GameFinishedActivity extends AppCompatActivity implements GameFinishedController {

    private List<Player> playerList;

    private Presenter presenter;

    private TextView gameEnded;
    private TextView winnerText;
    private RecyclerView scoreList;
    private Button newGame;

    static class ViewModel {
        final List<Player> scores;

        ViewModel(List<Player> scores) {
            this.scores = scores;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        gameEnded = (TextView) findViewById(R.id.game_ended);
        winnerText = (TextView) findViewById(R.id.winnertext);
        scoreList = (RecyclerView) findViewById(R.id.scorelist);
        newGame = (Button) findViewById(R.id.new_game);

        playerList = new ArrayList<>();

        ViewModel vm = new ViewModel(playerList);
        load(vm);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to LobbyActivity
                onBackPressed();
                finish();
            }
        });

        presenter = Presenter.getInstance();
        presenter.registerGameFinishedActivity(this);
        presenter.setActiveController(this);
    }

    private void load(ViewModel vm) {
        Player winner = vm.scores.get(0);
        String winnerScore = getString(
                R.string.winner_score,
                winner.getName(),
                winner.getScore(),
                winner.getMin(),
                winner.getSec()
        );

        scoreList.setLayoutManager(new LinearLayoutManager(this));
        ScoreAdapter scoreAdapter = new ScoreAdapter(vm.scores.subList(1, vm.scores.size()));
        scoreList.setAdapter(scoreAdapter);

        winnerText.setText(winnerScore);
    }

    @Override
    public void setWinner(int id, String username, int score) {
        Player winner = new Player(username, score, 0, 0);
        playerList.add(winner);
    }

    @Override
    public void setLeaderboard(HashMap<Client, Integer> leaderboard) {
        for (Map.Entry<Client, Integer> entry : leaderboard.entrySet()) {
            playerList.add(new Player(entry.getKey().getClientName(), entry.getValue(), 0, 0));
        }
    }

    @Override
    public void handleNotAllowed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleParsingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleAccessDenied(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setActiveController(this);
    }

}
