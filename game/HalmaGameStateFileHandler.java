package app.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import app.file.FileHandler;
import app.file.HalmaFileConstants;
import app.ui.HalmaDimensions;

public class HalmaGameStateFileHandler implements FileHandler<HalmaGameState> {

	@Override
	public void saveToFile(String filePath, HalmaGameState gameState) {
		
		try{
			
			final File file = new File(filePath);
			
			// Make sure directory and file exists
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
			}
			
			final FileOutputStream fileOut = new FileOutputStream(file);
			final ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			
			objectOut.writeObject(gameState);
			
			objectOut.close();
			fileOut.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public HalmaGameState readFromFile(String filePath) {
	
		try {
			
			final FileInputStream fileIn = new FileInputStream(filePath);
			final ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			
			final HalmaGameState gameState = (HalmaGameState) objectIn.readObject();
			
			objectIn.close();
			fileIn.close();
			
			return gameState;
			
			//return new HalmaGame(new HalmaDimensions(1920.0, 1080.0), null).getState();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public List<HalmaGameState> readAllFiles(){
		
		final ArrayList<HalmaGameState> states = new ArrayList<>();
		
		final File directory = new File(HalmaFileConstants.HALMA_GAME_STATE_SAVE_DIR);
		
		if(!directory.exists()) {
			return states;
		}
		
		for(File file : directory.listFiles()) {
			
			final HalmaGameState state = readFromFile(file.getPath());
			
			if(state != null) {
				states.add(state);
			}
			
		}
		
		return states;
		
	}
	
	

	
	
}
