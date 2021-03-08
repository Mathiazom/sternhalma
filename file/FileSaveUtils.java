package app.file;

public class FileSaveUtils {

	// Use system time to generate unique save path
	public static String generateUniqueFilePath() {
		
		return String.format(HalmaFileConstants.HALMA_GAME_STATE_SAVE_PATH_TEMPLATE, String.valueOf(System.currentTimeMillis()));
		
	}
	
	
}
