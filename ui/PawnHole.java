package app.ui;

import app.game.Player;
import app.game.PlayerUtils;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PawnHole extends Circle {
	

	private Player occupiedBy = Player.NO_PLAYER;
	private Player baseFor = Player.NO_PLAYER;
	
	private boolean isAvailable = true;
	
	public interface OnHoleClickedListener {

		void onClicked(final PawnHole hole);

	}
	
	public PawnHole(double centerX, double centerY, double r, int strokeWidth) {
		this(Player.NO_PLAYER, centerX, centerY, r, strokeWidth);
	}
	
	public PawnHole(Player baseFor, double centerX, double centerY, double r, int strokeWidth) {
		
		super(centerX, centerY, r, HalmaUIConstants.NOT_OCCUPIED_COLOR);
		
		super.setStrokeWidth(strokeWidth);
		
		this.baseFor = baseFor;
		
		setOccupiedBy(baseFor);
		
		setRegularStroke();
		
	}
	
	public void setRegularStroke() {
		
		super.setStroke(baseFor != Player.NO_PLAYER ? baseFor.getColor() : HalmaUIConstants.NOT_BASE_STROKE);
		
	}
	
	public void setSpecialStroke(Color specialStroke) {
		
		super.setStroke(specialStroke);
		
	}
	
	public void setOccupiedBy(Player player) {
		
		if(player == Player.NO_PLAYER) {
			// throw new IllegalArgumentException("Cannot set occupant to null, use removeOccupant() instead");
			return;
		}
		
		this.occupiedBy = player;
		
		super.setFill(!isAvailable ? PlayerUtils.getFinishedPlayerColor(occupiedBy) : occupiedBy.getColor());
		
	}
	
	public void removeOccupant() {
		
		this.occupiedBy = Player.NO_PLAYER;
	
		super.setFill(HalmaUIConstants.NOT_OCCUPIED_COLOR);
		
	}
	
	public void setAvailable(boolean isAvailable) {
		
		this.isAvailable = isAvailable;
		
		super.setFill(
				!isAvailable ?
						PlayerUtils.getFinishedPlayerColor(occupiedBy) :
						isOccupied() ?
								occupiedBy.getColor()
								: HalmaUIConstants.NOT_OCCUPIED_COLOR);
		
	}
	
	
	public boolean isOccupied() {
		
		return this.occupiedBy != Player.NO_PLAYER;
		
	}
	
	public Player getOccupant() {
		
		return this.occupiedBy;
		
	}
	
	public boolean isBase() {
		
		return this.baseFor != Player.NO_PLAYER;
		
	}
	
	public Player getBaseFor() {
		
		return this.baseFor;
		
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (int) super.getCenterX() + ", " + (int) super.getCenterY() + ", " + this.occupiedBy + "]";
	}
	
	
	
	
	

}
