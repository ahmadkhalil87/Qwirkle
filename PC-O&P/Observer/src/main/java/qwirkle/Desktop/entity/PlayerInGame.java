package qwirkle.Desktop.entity;

import java.util.ArrayList;

import qwirkle.Desktop.Communication.Client;
import qwirkle.Desktop.entity.Player;
import qwirkle.Desktop.entity.Tile;

public class PlayerInGame {
	
	Client player;
	ArrayList<Tile> playerTiles;
	int playerScore;
	boolean turn;
	
	public PlayerInGame(Client player, ArrayList<Tile> playerTiles, int score, boolean turn)
	{
		this.player = player;
		this.playerTiles=playerTiles;
		this.playerScore=score;
		this.turn=turn;
		
	}

	public Client getPlayer()
	{
		return this.player;
	}

	public void setPlayer(Client newPlayer)
	{
		this.player= newPlayer;
	}

	public ArrayList<Tile> getPlayerTiles()
	{
		return this.playerTiles;
	}

	public void setPlayerTiles(ArrayList<Tile> newPlayerTiles)
	{
		this.playerTiles=newPlayerTiles;
	}

	public int getPlayerScore()
	{
		return this.playerScore;
	}

	public void setPlayerScore(int newScore)
	{
		this.playerScore=newScore;
	}

	public boolean getPlayerTurn()
	{
		return this.turn;
	}

	public void setPlayerTurn(boolean newTurnValue)
	{
		this.turn=newTurnValue;
	}

}
