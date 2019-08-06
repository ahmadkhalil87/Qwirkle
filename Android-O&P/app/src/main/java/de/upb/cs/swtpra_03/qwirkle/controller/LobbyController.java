package de.upb.cs.swtpra_03.qwirkle.controller;

import java.util.ArrayList;

import de.upb.swtpra1819interface.models.Game;

public interface LobbyController extends Controller {

    public void updateGameList(ArrayList<Game> games);
    public void acceptSpectatorJoinRequest(Game game);
    public void acceptGameJoinRequest(Game game);
}
