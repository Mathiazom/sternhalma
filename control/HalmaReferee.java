package app.control;

import java.util.ArrayList;
import java.util.stream.IntStream;

import app.ui.PawnHole;

public class HalmaReferee {

	public enum MoveType {

		ONE_STEP, JUMP, ILLEGAL

	}

	private double stepDistance;

	private final ArrayList<PawnHole> pawnHoles;

	public HalmaReferee(double stepDistance, ArrayList<PawnHole> pawnHoles) {

		this.stepDistance = stepDistance;

		this.pawnHoles = pawnHoles;

	}

	public void setStepDistance(double stepDistance) {
		this.stepDistance = stepDistance;
	}

	private boolean hasSingleMoveLength(double distance) {

		return Math.abs(distance - stepDistance) < HalmaControlConstants.MOVE_DISTANCE_TOLERANCE;

	}

	private boolean hasJumpLength(double distance) {

		return Math.abs(distance - 2 * stepDistance) < HalmaControlConstants.MOVE_DISTANCE_TOLERANCE;

	}

	private boolean jumpsPawn(PawnHole fromHole, double angle) {

		boolean jumpsPawn = false;

		final double fromX = fromHole.getCenterX();
		final double fromY = fromHole.getCenterY();

		for (PawnHole hole : pawnHoles) {

			final double holeX = hole.getCenterX();
			final double holeY = hole.getCenterY();

			if (!hole.isOccupied()
					|| !hasSingleMoveLength(PointUtils.distanceBetweenPoints(holeX, holeY, fromX, fromY))) {
				continue;
			}

			final int jumpAngle = PointUtils.angleBetweenPoints(fromX, fromY, hole.getCenterX(), hole.getCenterY());

			if (angle == jumpAngle) {

				jumpsPawn = true;
				
				break;

			}

		}

		return jumpsPawn;

	}

	private boolean isLegalAngle(final int angle) {
		
		return IntStream.of(HalmaControlConstants.LEGAL_MOVE_ANGLES).anyMatch(x -> x == angle);

	}

	public MoveType judgeMove(final PawnHole fromHole, final PawnHole toHole, final MoveType prevMove) {

		if (prevMove == MoveType.ONE_STEP) {
			// Cannot move more after one step
			return MoveType.ILLEGAL;
		}

		if (!fromHole.isOccupied()) {
			// throw new IllegalStateException("Attempted to move from a non-occupied
			// hole");
			return MoveType.ILLEGAL;
		}

		if (toHole.isOccupied()) {
			return MoveType.ILLEGAL;
		}

		final double distance = PointUtils.distanceBetweenHoles(fromHole, toHole);

		if (!hasSingleMoveLength(distance) && !hasJumpLength(distance)) {
			return MoveType.ILLEGAL;
		}

		final int theta = PointUtils.angleBetweenPoints(fromHole.getCenterX(), fromHole.getCenterY(), toHole.getCenterX(),
				toHole.getCenterY());

		if (!isLegalAngle(theta)) {

			return MoveType.ILLEGAL;

		}

		if (hasJumpLength(distance)) {

			if (!jumpsPawn(fromHole, theta)) {

				return MoveType.ILLEGAL;

			}
			return MoveType.JUMP;

		}

		if (prevMove == MoveType.JUMP) {
			// Cannot one step after jump
			return MoveType.ILLEGAL;
		}

		return MoveType.ONE_STEP;

	}

}
