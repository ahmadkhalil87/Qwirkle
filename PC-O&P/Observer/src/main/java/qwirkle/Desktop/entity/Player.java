package qwirkle.Desktop.entity;

import java.util.ArrayList;

import qwirkle.Desktop.enumeration.ClientType;
import qwirkle.Desktop.entity.Tile;

/**
 * this Class defines the player
 * 
 * 
 * @author Houman Mahtabi
 *
 */
public class Player {
	
	private int clientID;
	private String clientName;
	private ClientType clientType;
	private ArrayList<Tile> hand;
	private int points;
	
	
/**
 * The constructor of the class
 * 
 * @param id The clinetId
 * @param name The name of the client
 * @param type The type of the client (player, observer, or KI)
 * @param score of the player. 0 at the beginning
 * @param ArrayList of the Tiles currently in the hand of the player
 */
	public Player(int id, String name, ClientType type)
	{
		this.clientID=id;
		this.clientName=name;
		this.clientType=type;
		this.hand = new ArrayList<>();
		this.points = 0;
	}
	
	
	
	// The getter and setter of the class
	
	public int getClientID()
	{
		return this.clientID;
	}
	public void setClientID(int newID)
	{
		this.clientID= newID;
	}
	public String getClientName()
	{
		return this.clientName;
	}
	public void setClientName(String newName)
	{
		this.clientName= newName;
	}
	public ClientType getClientType()
	{
		return this.clientType;
	}
	public void setClientType(ClientType newType)
	{
		this.clientType= newType;
	}

	public ArrayList<Tile> getHand() {
		return hand;
	}
	public void setHand(ArrayList<Tile> hand) {
		this.hand = hand;
	}



	public int getPoints() {
		return points;
	}



	public void addPoints(int points) {
		this.points += points;
	}

}