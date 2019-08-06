package logic_objects;

import java.util.ArrayList;

import enumeration.Check_Type;

/**
 * Class used to recieve Information regarding the Scoring
 * and Validity of a Move after the neighborship check
 * @author Lukas
 *
 */
public class ScoreObject {
	private int score;
	private boolean valid_move;
	private Check_Type move_basis;
	private ArrayList<Integer> Passed_colors_or_shapes;
	
	/**
	 * This constructor is used for Passing needed Data inbetween methods
	 * @param score
	 * @param valid_move
	 * @param move_basis
	 */
	public ScoreObject(int score, boolean valid_move, Check_Type move_basis) {
		this.setScore(score);
		this.setValid_move(valid_move);
		this.setMove_basis(move_basis);
		setPassed_colors_or_shapes(new ArrayList<>());
	}
	
	/**
	 * This Constructor is used to pass an Array of passed shapes or colors through the Validation methods
	 * @param score
	 * @param valid_move
	 * @param move_basis
	 * @param passed
	 */
	public ScoreObject(int score, boolean valid_move, Check_Type move_basis, ArrayList<Integer> passed) {
		this.setScore(score);
		this.setValid_move(valid_move);
		this.setMove_basis(move_basis);
		this.Passed_colors_or_shapes = passed;
	}

	public boolean isValid_move() {
		return valid_move;
	}

	public void setValid_move(boolean valid_move) {
		this.valid_move = valid_move;
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
	public ArrayList<Integer> getPassed_colors_or_shapes() {
		return Passed_colors_or_shapes;
	}
	public void setPassed_colors_or_shapes(ArrayList<Integer> passed_colors_or_shapes) {
		Passed_colors_or_shapes = passed_colors_or_shapes;
	}
	
}
