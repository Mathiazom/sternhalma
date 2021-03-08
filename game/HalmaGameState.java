package app.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.ui.HalmaDimensions;
import app.ui.PawnHole;

public class HalmaGameState implements Serializable {

	private static final long serialVersionUID = 4948490867099659166L;
	

	private final HashMap<String, List<double[]>> playerPawns = new HashMap<>();

	private final HashMap<String, Integer> playerTurns = new HashMap<>();

	private final HalmaDimensions dimensions;

	private final String activePlayerName;
	
	
	private String title;

	private long timestamp;
	
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
            return false;
        }
		
		final HalmaGameState otherState = (HalmaGameState) o;
		
		boolean storesSamePawns = true;
		
		for(Map.Entry<String, List<double[]>> entry : this.playerPawns.entrySet()) {
			
			if(otherState.playerPawns.get(entry.getKey()).equals(entry.getValue())) {
				storesSamePawns = false;
				break;
			}
			
		}
		
		return 
				storesSamePawns && 
				(this.playerTurns == otherState.playerTurns || this.playerTurns.equals(otherState.playerTurns)) &&
				this.dimensions.getScreenHeight() == otherState.dimensions.getScreenHeight() &&
				this.dimensions.getScreenWidth() == otherState.dimensions.getScreenWidth() &&
				(this.activePlayerName == otherState.activePlayerName || this.activePlayerName.equals(otherState.activePlayerName)) &&
				this.title.equals(otherState.title) &&
				this.timestamp == otherState.timestamp;
		
	}
	
	

	public HalmaGameState(final ArrayList<PawnHole> pawnHoles, final List<Player> players, final HalmaDimensions dimensions,
			final Player activePlayer) {

		this.dimensions = dimensions;

		this.activePlayerName = activePlayer == null ? null : activePlayer.getName();

		for (Player player : players) {

			// Initialize pawn holes list
			playerPawns.put(player.getName(), new ArrayList<>());

			// Store player turn total
			playerTurns.put(player.getName(), player.getTotalTurns());

		}

		for (PawnHole hole : pawnHoles) {

			if (!hole.isOccupied()) {
				// Pawn hole is empty, no need to store state
				continue;
			}

			// Store pawn hole coordinates
			playerPawns.get(hole.getOccupant().getName()).add(new double[] { hole.getCenterX(), hole.getCenterY() });

		}

	}

	public List<double[]> getPlayerPawnCoordinates(final Player player) {

		return playerPawns.get(player.getName());

	}

	public int getPlayerTotalTurns(final Player player) {

		return playerTurns.get(player.getName());

	}

	public HalmaDimensions getDimensions() {

		return this.dimensions;

	}

	public void setTimestamp(final long timestamp) {

		this.timestamp = timestamp;

	}

	public long getTimestamp() {

		return this.timestamp;

	}

	public void setTitle(final String title) {

		this.title = title;

	}

	public String getTitle() {

		return this.title;

	}
	
	public String getActivePlayerName() {
		
		return this.activePlayerName;
		
	}

}
