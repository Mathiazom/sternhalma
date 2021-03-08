package app.game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class PlayerUtils {
	
	
	public static List<Player> duplicatePlayers(final List<Player> players){
		
		final ArrayList<Player> duplicatePlayers = new ArrayList<>();
		
		for(Player player : players) {
			
			final Player duplicatePlayer = player.duplicate();
			
			duplicatePlayers.add(duplicatePlayer);
			
		}
		
		return duplicatePlayers;
		
	}
	
	public static List<Player> setPlayersOpponents(final List<Player> players){
		
		for (int i = 0; i < players.size(); i++) {
			
			players.get(i).setOpponent(players.get((i + 3) % players.size()));
			
		}
		
		return players;
		
	}
	
	public static Color getFinishedPlayerColor(final Player player) {
		
		return player.getColor().darker().darker().darker();
		
	}
	

}
