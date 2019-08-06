package logic_objects;

import java.util.ArrayList;

import Model.TileOnPositionEX;
import enumeration.Check_Type;

public class NeighborshipCertificate {
	
	TileOnPositionEX playtile;
	boolean validity;
	int score;
	private Check_Type move_basis;
	private TileOnPositionEX failed_neighbor;
	private ArrayList<Integer> checked_shapes_or_colors;

	/**
	 * This constructor is used on a successfull neighborship check
	 * @param playtile
	 * @param validity
	 * @param score
	 * @param move_basis
	 */
	public NeighborshipCertificate(TileOnPositionEX playtile, boolean validity, int score, Check_Type move_basis, ArrayList<Integer> checked_shapes_or_colors) {
		this.playtile = playtile;
		this.validity = validity;
		this.score = score;
		this.setMove_basis(move_basis);
		this.setChecked_shapes_or_colors(checked_shapes_or_colors);
	}
	
	public NeighborshipCertificate(TileOnPositionEX playtile, TileOnPositionEX boardtile, boolean validity) {
		this.playtile = playtile;
		this.setFailed_neighbor(boardtile);
		this.validity = validity;
	}
	
	public TileOnPositionEX getPlaytile() {
		return playtile;
	}

	public void setPlaytile(TileOnPositionEX playtile) {
		this.playtile = playtile;
	}

	public boolean isValidity() {
		return validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Check_Type getMove_basis() {
		return move_basis;
	}

	public void setMove_basis(Check_Type move_basis) {
		this.move_basis = move_basis;
	}

	public TileOnPositionEX getFailed_neighbor() {
		return failed_neighbor;
	}

	public void setFailed_neighbor(TileOnPositionEX failed_neighbor) {
		this.failed_neighbor = failed_neighbor;
	}

	public ArrayList<Integer> getChecked_shapes_or_colors() {
		return checked_shapes_or_colors;
	}

	public void setChecked_shapes_or_colors(ArrayList<Integer> checked_shapes_or_colors) {
		this.checked_shapes_or_colors = checked_shapes_or_colors;
	}
}
