package app.tests;


import java.util.ArrayList;
import java.util.Random;

import app.game.HalmaGame;
import app.game.HalmaGame.GameEventListener;
import app.game.Player;
import app.ui.HalmaDimensions;
import app.ui.PawnHole;
import junit.framework.TestCase;

public class HalmaGameTest extends TestCase {
	
	private HalmaGame game;
	
	
	public void setUp() {
		
		System.out.println("Test");
		
		this.game = new HalmaGame(new HalmaDimensions(1920.0, 1080.0), (GameEventListener) new GameEventListener() {
			
			@Override
			public void onPlayerFinished(Player player, int placement) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNextTurn(Player nextPlayer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMoving(Player movingPlayer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIllegalMove(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGameEnded() {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	
	public void testHolesCount() {
		
		assertEquals(121, game.getHoles().size());
		
	}
	
	
	public void testInitialPlayerPick() {
		
		final ArrayList<PawnHole> holes = game.getHoles();
		
		// Pick random occupied pawn hole
		PawnHole randomHole = null;
		while(randomHole == null) {
			
			final PawnHole hole = holes.get(new Random().nextInt(holes.size()));
			
			// Check if hole is a valid pick
			if(hole.isOccupied()) {
				randomHole = hole;
			}
			
		}
		
		// Simulate click on random hole
		randomHole.getOnMouseClicked().handle(null);
		
		assertEquals("Unexpected initial player pick", game.getState().getActivePlayerName(), randomHole.getOccupant().getName());
	}
	
	
	public void testTotalTurnsCount() {
		
		assertEquals("Non-zero turn count on fresh startup", 0, game.getTotalTurns());
		
		for(int i = 0;i<10;i++) {
			game.makeRandomTurn();
		}
		
		assertEquals("Unexpected total turns count", 10, game.getTotalTurns());
		
	}
	
	

}
