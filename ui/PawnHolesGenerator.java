package app.ui;

import java.util.ArrayList;

import app.game.Player;
import app.ui.PawnHole.OnHoleClickedListener;

public class PawnHolesGenerator {

	private final Player[] players;

	private final int pawnDim;
	private final int pawnStrokeWidth;
	private final double stepDistanceX;
	private final double stepDistanceY;
	private final double centerX;
	private final double centerY;

	private final OnHoleClickedListener listener;

	public PawnHolesGenerator(final Player[] players, final HalmaDimensions dimensions, final OnHoleClickedListener listener) {

		this.players = players;

		this.stepDistanceX = dimensions.getStepDistanceX();
		this.stepDistanceY = dimensions.getStepDistanceY();
		
		this.pawnDim = dimensions.getPawnDim();
		this.pawnStrokeWidth = dimensions.getPawnStrokeWidth();

		this.centerX = dimensions.getScreenCenterX();
		this.centerY = dimensions.getScreenCenterY();

		this.listener = listener;

	}

	public ArrayList<PawnHole> generateHoles() {

		final ArrayList<PawnHole> holes = new ArrayList<>();

		holes.addAll(generateHexagon(centerX, centerY));

		holes.addAll(generateSpokes(centerX, centerY));

		return holes;

	}

	private ArrayList<PawnHole> generateSpokes(final double centerX, final double centerY) {

		final ArrayList<PawnHole> holes = new ArrayList<>();

		// TOP
		holes.addAll(generateTriangle(players[0], centerX,
				centerY - stepDistanceY * 6, false));

		// TOP RIGHT
		holes.addAll(
				generateTriangle(players[1], centerX + stepDistanceX * 4.5,
						centerY - stepDistanceY * 3, true));

		// BOTTOM RIGHT
		holes.addAll(
				generateTriangle(players[2], centerX + stepDistanceX * 4.5,
						centerY + stepDistanceY * 3, false));

		// BOTTOM
		holes.addAll(generateTriangle(players[3], centerX,
				centerY + stepDistanceY * 6, true));

		// BOTTOM LEFT
		holes.addAll(
				generateTriangle(players[4], centerX - stepDistanceX * 4.5,
						centerY + stepDistanceY * 3, false));

		// TOP LEFT
		holes.addAll(
				generateTriangle(players[5], centerX - stepDistanceX * 4.5,
						centerY - stepDistanceY * 3, true));

		return holes;

	}

	private ArrayList<PawnHole> generateTriangle(final Player player, final double centerX, final double centerY, final boolean flipped) {

		final ArrayList<PawnHole> holes = new ArrayList<>();

		for (int i = -2; i < 2; i++) {
			holes.addAll(generateHolesRow(player, i + 3, centerX, centerY
					+ (flipped ? -1 : 1) * stepDistanceY * i));
		}

		for (PawnHole hole : holes) {
			
			player.addBaseHole(hole);

		}

		return holes;

	}

	private ArrayList<PawnHole> generateHexagon(final double centerX, final double centerY) {

		final ArrayList<PawnHole> holes = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			holes.addAll(generateHolesRow(Player.NO_PLAYER, i + 5, centerX,
					centerY + stepDistanceY * (4 - i)));
		}

		holes.addAll(generateHolesRow(Player.NO_PLAYER, 9, centerX, centerY));

		for (int i = 0; i < 4; i++) {
			holes.addAll(generateHolesRow(Player.NO_PLAYER, i + 5, centerX,
					centerY + stepDistanceY * (i - 4)));
		}

		return holes;

	}

	private void attackListeners(final PawnHole hole) {

		hole.setOnMouseClicked((event) -> {
			listener.onClicked(hole);
		});

	}

	private ArrayList<PawnHole> generateHolesRow(final Player player, int count, final double centerX, final double centerY) {

		final ArrayList<PawnHole> holes = new ArrayList<>();

		double adjOffset;

		if (count % 2 == 1) {

			final PawnHole hole = new PawnHole(player, centerX, centerY, pawnDim / 2, pawnStrokeWidth);
			attackListeners(hole);

			holes.add(hole);

			adjOffset = stepDistanceX;
			count -= 1;

		} else {

			adjOffset = stepDistanceX / 2;

		}

		for (int i = 0; i < count / 2; i++) {

			final PawnHole hole = new PawnHole(player, centerX + adjOffset + stepDistanceX*i, centerY,
					pawnDim / 2, pawnStrokeWidth);
			attackListeners(hole);

			holes.add(hole);

		}

		for (int i = 0; i < count / 2; i++) {

			final PawnHole hole = new PawnHole(player, centerX - adjOffset - stepDistanceX*i, centerY,
					pawnDim / 2, pawnStrokeWidth);
			attackListeners(hole);

			holes.add(hole);
		}

		return holes;

	}

}
