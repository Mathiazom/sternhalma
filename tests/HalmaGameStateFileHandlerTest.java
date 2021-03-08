package app.tests;

import java.util.ArrayList;
import java.util.List;

import app.file.FileSaveUtils;
import app.game.HalmaGame;
import app.game.HalmaGameState;
import app.game.HalmaGameStateFileHandler;
import app.ui.HalmaDimensions;
import junit.framework.TestCase;

public class HalmaGameStateFileHandlerTest extends TestCase {
	
	
	private HalmaGameStateFileHandler fileHandler; 
	
	
	public void setUp() {
	
		this.fileHandler = new HalmaGameStateFileHandler();
		
	}
	
	public void testSaveAndReadState() {
		
		final String path = FileSaveUtils.generateUniqueFilePath();
		
		final HalmaGameState state = getDummyState();

		fileHandler.saveToFile(path, state);
		
		final HalmaGameState retrievedState = fileHandler.readFromFile(path);
		
		assertTrue("Saved game state was not equal to original game state", state.equals(retrievedState));
		
	}
	
	
	public void testSaveAndReadMultipleStates() {
		
		// Save states
		
		final ArrayList<String> paths = new ArrayList<>();
		
		final ArrayList<HalmaGameState> states = new ArrayList<>();
		
		for(int i = 0;i<3;i++) {
			
			final HalmaGameState state = getDummyState();
			
			states.add(state);
			
			final String path = FileSaveUtils.generateUniqueFilePath();
			paths.add(path);
			
			fileHandler.saveToFile(path, state);
			
		}
		
		
		// Retrieve states
		
		final List<HalmaGameState> retrievedStates = fileHandler.readAllFiles();
		
		for(HalmaGameState state : states) {
			
			boolean isSaved = false;
			
			for(HalmaGameState otherState : retrievedStates) {
				
				if(state.equals(otherState)) {
					isSaved = true;
					break;
				}
				
			}
			
			// Test if saved state could be retrieved
			assertTrue("Some of the game states could not be retrieved correctly", isSaved);
			
		}
		
	}
	
	
	private HalmaGameState getDummyState() {
		
		final HalmaGameState state = new HalmaGame(new HalmaDimensions(1080.0, 1920.0), null).getState();

		state.setTimestamp(System.currentTimeMillis());
		state.setTitle(String.valueOf(Math.random()));
		
		return state;
		
	}
	
	

}
