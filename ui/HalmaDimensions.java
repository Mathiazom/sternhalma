package app.ui;

import java.io.Serializable;

public class HalmaDimensions implements Serializable {

	private static final long serialVersionUID = 4741741296232806737L;
	
	private final double screenWidth;
	private final double screenHeight;
	
	private double screenCenterX;
	private double screenCenterY;
	
	private final int pawnDim;
	
	private double stepDistanceX;
	private double stepDistanceY;

	public HalmaDimensions(double screenWidth, double screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenCenterX = screenWidth / 2.0;
		this.screenCenterY = screenHeight / 2.0;
		this.pawnDim = (int) (Math.min(screenWidth, screenHeight) / HalmaUIConstants.PAWN_DIM_DIVISOR);
		this.stepDistanceX = this.pawnDim * HalmaUIConstants.PAWN_X_STEP_MULTIPLIER;
		this.stepDistanceY = this.stepDistanceX * HalmaUIConstants.PAWN_Y_STEP_MULTIPLIER;
	}

	public double getScreenWidth() {
		return this.screenWidth;
	}

	public double getScreenHeight() {
		return this.screenHeight;
	}

	public double getScreenCenterX() {
		return this.screenCenterX;
	}

	public double getScreenCenterY() {
		return this.screenCenterY;
	}

	public int getPawnDim() {
		return this.pawnDim;
	}
	
	public double getStepDistanceX() {
		return this.stepDistanceX;
	}
	
	public double getStepDistanceY() {
		return this.stepDistanceY;
	}

	public int getPawnStrokeWidth() {
		return HalmaUIConstants.PAWN_STROKE_WIDTH;
	}

}
