package tests;
import java.util.ArrayList;

import Controller.Brain;
import Model.Field;
import Model.TileOnPositionEX;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import enumeration.ControlParameters;
import enumeration.Neighbor;

/*
 * Just a temporary testing class for certain cases of the KI
 */
public class Testing {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		// initializing a field with parameters: penalty = 0; qwirkle = 6
		Field field = new Field(0,6);
		ArrayList<TileOnPositionEX> move = new ArrayList<TileOnPositionEX>();
		
		// Colors
		int blue 	= 1;
		int green 	= 5;
		int purple 	= 4;
		int orange 	= 3;
		int red 	= 0;
		int yellow 	= 2;
		// Shapes
		int circle	= 0;
		int club 	= 11;
		int diamond	= 3;
		int square	= 1;
		int star	= 7;
		int sun		= 4;
		
		/* TEST CASE	TILEONPOSITION(X,Y,TILE)	TILE(COLOR,SHAPE,UNIQUEID) */
		// CAUTION: EVERY TILEONPOSITION WITH THE SAME TILE HAS CURRENTLY THE SAME UNIQUEID!
		
		// BLUE
		Tile blueCircle		= new Tile(blue,circle,0);
		Tile blueClub		= new Tile(blue,club,1);
		Tile blueDiamond	= new Tile(blue,diamond,2);
		Tile blueSquare		= new Tile(blue,square,3);
		Tile blueStar		= new Tile(blue,star,4);
		Tile blueSun		= new Tile(blue,sun,5);
		// GREEN
		Tile greenCircle	= new Tile(green,circle,6);
		Tile greenClub		= new Tile(green,club,7);
		Tile greenDiamond	= new Tile(green,diamond,8);
		Tile greenSquare	= new Tile(green,square,9);
		Tile greenStar		= new Tile(green,star,10);
		Tile greenSun		= new Tile(green,sun,11);
		// PURPLE
		Tile purpleCircle	= new Tile(purple,circle,12);
		Tile purpleClub		= new Tile(purple,club,13);
		Tile purpleDiamond	= new Tile(purple,diamond,14);
		Tile purpleSquare	= new Tile(purple,square,15);
		Tile purpleStar		= new Tile(purple,star,16);
		Tile purpleSun		= new Tile(purple,sun,17);
		// ORANGE
		Tile orangeCircle	= new Tile(orange,circle,18);
		Tile orangeClub		= new Tile(orange,club,19);
		Tile orangeDiamond	= new Tile(orange,diamond,20);
		Tile orangeSquare	= new Tile(orange,square,21);
		Tile orangeStar		= new Tile(orange,star,22);
		Tile orangeSun		= new Tile(orange,sun,23);
		// RED
		Tile redCircle		= new Tile(red,circle,24);
		Tile redClub		= new Tile(red,club,25);
		Tile redDiamond		= new Tile(red,diamond,26);
		Tile redSquare		= new Tile(red,square,27);
		Tile redStar		= new Tile(red,star,28);
		Tile redSun			= new Tile(red,sun,29);
		// YELLOW
		Tile yellowCircle	= new Tile(yellow,circle,30);
		Tile yellowClub		= new Tile(yellow,club,31);
		Tile yellowDiamond	= new Tile(yellow,diamond,32);
		Tile yellowSquare	= new Tile(yellow,square,33);
		Tile yellowStar		= new Tile(yellow,star,34);
		Tile yellowSun		= new Tile(yellow,sun,35);
		
		move.add(new TileOnPositionEX(0,1,redCircle));
		move.add(new TileOnPositionEX(0,0,blueCircle));
		
		field.processMove(move, ControlParameters.Server);
		ArrayList<TileOnPositionEX> tilesOnField = field.getTilesOnBoard();
		move.clear();
		
		move.add(new TileOnPositionEX(2,1,redClub));
		move.add(new TileOnPositionEX(4,1, redStar));
		move.add(new TileOnPositionEX(3,1, redDiamond));
		move.add(new TileOnPositionEX(1,1, redSun));
		move.add(new TileOnPositionEX(5,1, redSquare));
		field.processMove(move, ControlParameters.Server);
		tilesOnField = field.getTilesOnBoard();
		move.clear();

		
		System.out.println("\nAMOUNT OF TILES ON FIELD: " + tilesOnField.size() + "\n");
		for (TileOnPositionEX tile : tilesOnField) {
			System.out.println("The tile: " + tile);
			if(tile.getNeighbor(Neighbor.Top)!=null) {System.out.println("TOP: " + tile.getNeighbor(Neighbor.Top));}else {System.out.println("TOP: NULL");}
			if(tile.getNeighbor(Neighbor.Right)!=null) {System.out.println("RIGHT: " + tile.getNeighbor(Neighbor.Right));}else {System.out.println("RIGHT: NULL");}
			if(tile.getNeighbor(Neighbor.Bottom)!=null) {System.out.println("BOTTOM: " + tile.getNeighbor(Neighbor.Bottom));}else {System.out.println("BOTTOM: NULL");}
			if(tile.getNeighbor(Neighbor.Left)!=null) {System.out.println("LEFT: " + tile.getNeighbor(Neighbor.Left) + "\n");}else {System.out.println("LEFT: NULL\n");}
		}
	}
}
