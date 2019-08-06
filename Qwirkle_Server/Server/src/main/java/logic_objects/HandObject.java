package logic_objects;

import enumeration.Check_Type;
import enumeration.Direction;

/**
 * This Class is used to pass information of the Handcheck back to the procedure
 * @author Lukas
 *
 */
public class HandObject {
	
	Direction movedirection;
	Check_Type move_basis;
	
	boolean validHand;
	String Reason;
	
	public HandObject(Direction movedirection, Check_Type move_basis, boolean was_valid, String Reason) {
		this.movedirection = movedirection;
		this.move_basis = move_basis;
		this.validHand = was_valid;
		this.Reason = Reason;
	}

	public Direction getMovedirection() {
		return movedirection;
	}

	public void setMovedirection(Direction movedirection) {
		this.movedirection = movedirection;
	}

	public Check_Type getMove_basis() {
		return move_basis;
	}

	public void setMove_basis(Check_Type move_basis) {
		this.move_basis = move_basis;
	}

	public boolean isValidHand() {
		return validHand;
	}

	public void setValidHand(boolean validHamd) {
		this.validHand = validHamd;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	
}
