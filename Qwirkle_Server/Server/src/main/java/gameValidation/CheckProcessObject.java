package gameValidation;

/**
 * This Class is a representation of the result of the Check made by the Field
 * on trying to add tiles or not
 * @author Lukas
 *
 */
public class CheckProcessObject {
	
	private boolean validity;
	private int score;

	/**
	 * This Constructor is used for either successfull or failed validation
	 */
	public CheckProcessObject(boolean validity, int score) {
		this.setScore(score);
		this.setValidity(validity);
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
	
}
