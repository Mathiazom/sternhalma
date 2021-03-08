package app;

import app.control.HalmaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HalmaApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {

		// FXML file loader
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("HalmaApp.fxml"));
		
		// Load FXML elements in a scene and place scene on the stage
		primaryStage.setScene(new Scene(loader.load()));
		
		// Initialize stage controller
		((HalmaController) loader.getController()).initWithStage(primaryStage);
		
	}

	public static void main(final String[] args) {
		HalmaApp.launch(args);
	}
}