package de.upb.cs.swtpra_03.qwirkle.controller;

import java.util.HashMap;

import de.upb.swtpra1819interface.models.Client;

public interface GameFinishedController extends Controller {

    public void setWinner(int id, String username, int score);
    public void setLeaderboard(HashMap<Client, Integer> leaderboard);
}
