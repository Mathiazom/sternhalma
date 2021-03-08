package app.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.control.HalmaReferee;
import app.control.HalmaReferee.MoveType;
import app.ui.HalmaDimensions;
import app.ui.HalmaUIConstants;
import app.ui.PawnHole;
import app.ui.PawnHolesGenerator;
import app.ui.PawnHole.OnHoleClickedListener;

public class HalmaGame {

	private HalmaDimensions dimensions;

	private List<Player> players;

	private ArrayList<Player> placements = new ArrayList<>();

	private final ArrayList<PawnHole> pawnHoles = new ArrayList<>();

	private HalmaReferee referee;

	private Player activePlayer;

	private MoveType prevMove;

	private PawnHole activeHole;
	private PawnHole originHole;

	private boolean hasEnded;

	
	private OnHoleClickedListener holeClickedListener = new OnHoleClickedListener() {

		@Override
		public void onClicked(final PawnHole clickedHole) {

			if (isMovingPawn()) {

				requestPawnMove(activeHole, clickedHole);

			} else {

				requestPawnPick(clickedHole);

			}

		}

	};
	

	private GameEventListener eventListener;

	public interface GameEventListener {

		public void onGameEnded();

		public void onMoving(Player movingPlayer);

		public void onIllegalMove(String msg);

		public void onNextTurn(Player nextPlayer);

		public void onPlayerFinished(Player player, int placement);

	}

	
	public HalmaGame(final HalmaDimensions dimensions, final GameEventListener listener) {

		this.dimensions = dimensions;

		this.eventListener = listener;

		this.players = Stream.of(HalmaUIConstants.PLAYER_IDENTITIES).map(id -> new Player(id)).collect(Collectors.toList());

		PlayerUtils.setPlayersOpponents(players);

		this.referee = new HalmaReferee(dimensions.getStepDistanceX(), pawnHoles);

		final PawnHolesGenerator holesGenerator = new PawnHolesGenerator(players.toArray(new Player[players.size()]),
				dimensions, holeClickedListener);

		pawnHoles.addAll(holesGenerator.generateHoles());
		
		pawnHoles.get(65).setSpecialStroke(HalmaUIConstants.PAWN_ACTIVE_STROKE);
		
		pawnHoles.get(35).setSpecialStroke(HalmaUIConstants.PAWN_ACTIVE_STROKE);
		
	}

	/**
	 * Restores game from a given state object:
	 * 	- Clears the current board and occupies pawn holes according to saved state
	 * 	- Restores player total turns
	 * 	- Registers any finished players in the correct order (according to total turns)
	 * 	- Restores active player
	 * @param state to be restored into this game
	 */
	public void restoreState(final HalmaGameState state) {

		// Clear the board
		for (PawnHole hole : pawnHoles) {
			hole.removeOccupant();
		}

		final ArrayList<Player> finishedPlayers = new ArrayList<>();
		for (Player player : players) {

			// Restore turn count
			player.setTotalTurns(state.getPlayerTotalTurns(player));

			// Iterate every pawn position associated with player
			for (double[] pawnCoordinates : state.getPlayerPawnCoordinates(player)) {

				// Find pawn hole on board with matching coordinates
				for (PawnHole hole : pawnHoles) {

					if (hole.getCenterX() == pawnCoordinates[0] && hole.getCenterY() == pawnCoordinates[1]) {

						hole.setOccupiedBy(player);

						// Check if hole should be marked as secured
						if (hole.getBaseFor() == player.getOpponent()) {
							player.addToSecuredPawns(hole);
						}

					}

				}

			}

			// Check if player is finished
			if (player.hasSecuredAllPawns()) {
				finishedPlayers.add(player);
			}
		}

		finishedPlayers.sort((p1, p2) -> p1.getTotalTurns() - p2.getTotalTurns());

		for (Player player : finishedPlayers) {
			registerPlayerAsFinished(player);
		}

		// Restore active player
		this.activePlayer = players.stream().filter(p -> p.getName().equals(state.getActivePlayerName())).findFirst().orElseGet(() -> null);
		eventListener.onNextTurn(activePlayer);

	}
	
	/**
	 * Generates a state object with all necessary game information to enable later restoring
	 * @return HalmaGameState object
	 */
	public HalmaGameState getState() {

		return new HalmaGameState(pawnHoles, players, dimensions, activePlayer);

	}

	
	/**
	 * Makes sure the requested move is allowed before actually making the move
	 * @param fromHole: The pawns current hole
	 * @param toHole: The destination hole of the requested pawn move
	 */
	private void requestPawnMove(final PawnHole fromHole, final PawnHole toHole) {

		final MoveType moveType = referee.judgeMove(fromHole, toHole, prevMove);

		if (moveType == MoveType.ILLEGAL) {
			return;
		}

		// Make the actual move
		movePawn(fromHole, toHole);

		prevMove = moveType;

		setAndDisplayActiveHole(toHole);

		if (moveType == MoveType.ONE_STEP) {

			activeHole.setRegularStroke();

		}

	}

	/**
	 * Makes sure the request pawn pick is allowed before actually performing pick
	 * @param hole: The hole of the pawn to be picked
	 */
	private void requestPawnPick(final PawnHole hole) {

		if (!hole.isOccupied()) {

			return;

		}

		// Check if this is the first move of the game (all pawns are available)
		if (activePlayer == null) {

			activePlayer = hole.getOccupant();

		} else if (hole.getOccupant() != activePlayer) {

			// Active player does not own this pawn, cancel selection
			return;

		}

		setAndDisplayActiveHole(hole);

		// Save origin hole for potential revertion later
		originHole = hole;

		eventListener.onMoving(activePlayer);

	}

	/**
	 * Performs pawn move by interchanging the occupation details of the given pawn holes
	 * @param fromHole: The original hole of the moving pawn. This hole will be emptied.
	 * @param toHole: The destination hole of the moving pawn. This hole will inherit occupation details from {@code fromHole}
	 */
	private void movePawn(PawnHole fromHole, PawnHole toHole) {

		toHole.setOccupiedBy(fromHole.getOccupant());
		fromHole.removeOccupant();

	}

	/**
	 * Undoes any moves of the current turn.
	 * This is done by performing a move from the hole of the active pawn to the original hole.
	 * (The original hole is registered every time a pawn pick is requested)
	 */
	public void undoTurnMoves() {

		if (hasEnded) {
			// Ignore undo request
			return;
		}

		if (originHole == null) {
			// No undo information
			return;
		}

		if (activeHole != originHole) {

			// Move pawn back to original position
			movePawn(activeHole, originHole);

		}

		clearTurn();

		eventListener.onNextTurn(activePlayer);

	}

	/**
	 * Attempts to end the active turn and activate next player.
	 * The turn will not be ended in any of these conditions:
	 * 	- No move has been performed (i.e. a pass is not allowed)
	 * 	- A secured pawn has been moved out of security
	 * 	- The active pawn sits in a base that is not its own or its opponents base
	 * If the turn can be ended a check will be performed to see if player has finished
	 * @return true if turn was ended and next player was activated, false otherwise.
	 */
	public boolean endTurn() {

		if (hasEnded) {
			// Ignore end turn request
			return false;
		}

		if (originHole == activeHole) {

			// Cannot pass turn

			return false;

		}

		if (activePlayer.hasSecuredPawn(originHole)
				&& (!activeHole.isBase() || activeHole.getBaseFor() != activePlayer.getOpponent())) {

			// Cannot move secured pawn outside of base

			eventListener.onIllegalMove("Cannot move secured pawn outside of base");

			return false;

		}

		if (activeHole.isBase()) {

			final Player basePlayer = activeHole.getBaseFor();

			if (basePlayer == activePlayer.getOpponent()) {

				// Register as secured if not already done

				if (!activePlayer.hasSecuredPawn(activeHole)) {

					if (activePlayer.hasSecuredPawn(originHole)) {

						activePlayer.removeFromSecuredPawns(originHole);

					}

					activePlayer.addToSecuredPawns(activeHole);

				}

			} else {

				if (basePlayer != activePlayer) {

					eventListener.onIllegalMove("Cannot end turn in this base");

					return false;

				}

			}

		}
		
		// Check if player finished
		if(activePlayer.hasSecuredAllPawns()) {
			registerPlayerAsFinished(activePlayer);
		}

		clearTurn();

		nextTurn();

		return true;

	}

	/**
	 * Resets all turn-specific information and visual styling
	 */
	private void clearTurn() {

		activeHole.setRegularStroke();

		activeHole = null;

		originHole = null;

		prevMove = null;

	}

	/**
	 * Activates next unfinished player.
	 * If no such player exists, the game is ended.
	 */
	private void nextTurn() {

		for (int i = 0; i < players.size(); i++) {

			// Get next player in list (with wrap around)
			activePlayer = players.get((players.indexOf(activePlayer) + 1) % players.size());

			if (!activePlayer.hasSecuredAllPawns()) {

				// Found next unfinished player
				eventListener.onNextTurn(activePlayer);

				return;

			}
		}

		// No unfinished players, aka. the game has ended

		this.hasEnded = true;

		eventListener.onGameEnded();

	}

	/**
	 * Adds player to placement list, marks all associated pawns as finished and alerts game listener
	 * @param player: The player to be registered as finished
	 */
	private void registerPlayerAsFinished(final Player player) {
		
		// Add newly finished player to placement list
		placements.add(player);

		// Mark all player's pawns as finished
		for (PawnHole pawn : player.getSecuredPawns()) {
			pawn.setFill(PlayerUtils.getFinishedPlayerColor(player));
		}

		eventListener.onPlayerFinished(player, placements.size());
		
	}

	public boolean hasEnded() {

		return this.hasEnded;

	}

	/**
	 * Generates template pawn holes with new dimensions and adjusts original holes accordingly.
	 * Also updates game referee data to make correct evaluations.
	 * @param dimensions: The new game dimensions
	 */
	public void updateDimensions(HalmaDimensions dimensions) {

		this.dimensions = dimensions;

		final List<Player> duplicatePlayers = PlayerUtils.duplicatePlayers(players);

		final ArrayList<PawnHole> templateHoles = new PawnHolesGenerator(
				duplicatePlayers.toArray(new Player[duplicatePlayers.size()]), dimensions, holeClickedListener)
						.generateHoles();

		for (int i = 0; i < templateHoles.size(); i++) {

			final PawnHole templateHole = templateHoles.get(i);
			final PawnHole hole = pawnHoles.get(i);

			hole.setCenterX(templateHole.getCenterX());
			hole.setCenterY(templateHole.getCenterY());
			hole.setRadius(templateHole.getRadius());

		}

		referee.setStepDistance(dimensions.getStepDistanceX());

	}

	private boolean isMovingPawn() {

		return activeHole != null;

	}

	/**
	 * Deactivates currently active hole and activates given hole.
	 * @param hole: The hole to be activated and replace the currently active hole
	 */
	private void setAndDisplayActiveHole(final PawnHole hole) {

		if (hole == null) {
			//throw new IllegalArgumentException("Cannot set active hole to null");
			return;
		}

		if (activeHole != null) {
			// Remove special stroke from current active hole
			activeHole.setRegularStroke();
		}

		// Add special stroke to the soon to be active hole
		hole.setSpecialStroke(HalmaUIConstants.PAWN_ACTIVE_STROKE);

		// Actually set the new active hole
		activeHole = hole;

	}

	public HalmaDimensions getDimensions() {
		return this.dimensions;
	}

	public ArrayList<PawnHole> getHoles() {

		return this.pawnHoles;

	}

	public int getTotalTurns() {

		return players.stream().mapToInt(player -> player.getTotalTurns()).sum();

	}

	
	public void makeRandomTurn() {

		if (hasEnded) {
			// Ignore random move request
			return;
		}

		PawnHole randPawn = null;

		PawnHole randToHole = null;

		while (randToHole == null) {

			randPawn = getRandomPawn(activePlayer);

			if (randPawn == null) {

				// Could not find any pickable pawn
				return;

			}

			randToHole = getRandomToHole(randPawn);

		}

		requestPawnPick(randPawn);

		// Classify random move
		final MoveType randomMoveType = referee.judgeMove(randPawn, randToHole, prevMove);

		movePawn(activeHole, randToHole);

		prevMove = randomMoveType;

		// The pawn hole that was moved to should now be active
		setAndDisplayActiveHole(randToHole);

		// Make sure this move is a legal end state for player turn
		if (!endTurn()) {

			undoTurnMoves();

			return;

		}

		activePlayer.incrementTotalTurns();

	}

	private PawnHole getRandomPawn(final Player player) {

		PawnHole randPawn = null;

		final ArrayList<PawnHole> nonAttemptedRandPawns = new ArrayList<>();
		nonAttemptedRandPawns.addAll(pawnHoles);

		while (!nonAttemptedRandPawns.isEmpty() && (randPawn == null || !randPawn.isOccupied()
				|| (player != null && randPawn.getOccupant() != player))) {

			if (randPawn != null) {
				nonAttemptedRandPawns.remove(randPawn);
			}

			randPawn = nonAttemptedRandPawns.get((int) (Math.random() * nonAttemptedRandPawns.size()));

		}

		return randPawn;

	}

	private PawnHole getRandomToHole(final PawnHole randPawn) {

		final ArrayList<PawnHole> nonAttemptedToHoles = new ArrayList<>();
		nonAttemptedToHoles.addAll(pawnHoles);

		PawnHole randToHole = null;
		MoveType moveType = MoveType.ILLEGAL;

		while (randToHole == null || moveType == MoveType.ILLEGAL) {

			if (randToHole != null) {
				nonAttemptedToHoles.remove(randToHole);
			}

			if (nonAttemptedToHoles.isEmpty()) {
				return null;
			}

			randToHole = nonAttemptedToHoles.get((int) (Math.floor(Math.random() * nonAttemptedToHoles.size())));

			moveType = referee.judgeMove(randPawn, randToHole, prevMove);

		}

		return randToHole;

	}

}
