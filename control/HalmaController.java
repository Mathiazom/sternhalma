package app.control;


import java.util.List;

import app.file.FileSaveUtils;
import app.game.HalmaGame;
import app.game.HalmaGameState;
import app.game.HalmaGameStateFileHandler;
import app.game.Player;
import app.game.HalmaGame.GameEventListener;
import app.ui.HalmaDimensions;
import app.ui.HalmaUIConstants;
import app.ui.PlacementIndicator;
import app.ui.SaveGameField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HalmaController {

	@FXML
	private Pane menuPane;
	
	@FXML
	private Text gameHeader;

	@FXML
	private HBox placementsList;

	@FXML
	private Pane holesPane;

	@FXML
	private Pane endGamePane;

	@FXML
	private Text endGameMovesText;

	@FXML
	private HBox endGamePlacementsList;
	
	@FXML
	private Pane restoreFromSavePane;
	
	@FXML
	private VBox saveGameListContainer;
	
	@FXML
	private Pane saveStatePane;
	
	@FXML
	private TextField saveStateTitleField;
	
	@FXML
	private Button saveStateButton;
	
	@FXML
	private Button saveGameMenuButton;

	
	private HalmaGame activeGame;
	
	private boolean gamePaused;

	
	private final GameEventListener gameInterface = new GameEventListener() {
		
		@Override
		public void onGameEnded() {
			displayEndGame();
		}

		@Override
		public void onMoving(Player movingPlayer) {
			gameHeader.setFill(HalmaUIConstants.GAME_HEADER_PRIMARY_FILL);
			gameHeader.setText(movingPlayer.getName() + " is moving ...");
		}

		@Override
		public void onIllegalMove(String msg) {
			gameHeader.setFill(HalmaUIConstants.GAME_HEADER_ERROR_FILL);
			gameHeader.setText(msg);
		}

		@Override
		public void onNextTurn(Player nextPlayer) {
			gameHeader.setFill(HalmaUIConstants.GAME_HEADER_PRIMARY_FILL);
			gameHeader.setText(nextPlayer.getName() + "'s turn");
		}

		@Override
		public void onPlayerFinished(Player player, int placement) {
			placementsList.getChildren().add(new PlacementIndicator(
					placement,
					player.getTotalTurns(),
					player.getColor()
			));
		}
		
	};

	private final EventHandler<KeyEvent> keyPressedHandler = (event) -> {

		switch (event.getCode()) {
		
			case M:
				
				// Prevent user from saving ended or non-existing game
				saveGameMenuButton.setDisable(activeGame == null || activeGame.hasEnded());
				
				togglePane(menuPane);
				
				break;

			case SPACE:
			case ENTER:
	
				if (!activeGame.hasEnded() && !gamePaused) {
					activeGame.endTurn();
				}
	
				break;
	
			case BACK_SPACE:
	
				if (!activeGame.hasEnded() && !gamePaused) {
					activeGame.undoTurnMoves();
				}
	
				break;
	
			case R:
				
				if(gamePaused) {
					return;
				}
	
				for (int i = 0; i < HalmaControlConstants.RANDOM_TURNS_PER_KEYPRESS; i++) {
					if (!activeGame.hasEnded()) {
						activeGame.makeRandomTurn();
					}
				}
	
				break;
				
			case S:
				
				if(!activeGame.hasEnded()) {
					togglePane(saveStatePane);
				}
				
				break;
	
			case N:
				
				if(!gamePaused) {
					initializeNewGame();
				}
	
				break;
				
			case O:
				
				displaySaveGames();
				
				break;
				
			case Q:
		
				hideAllMenuPanes();
				
				break;
	
			default:
	
				break;

		}

	};

	private final ChangeListener<Number> widthChangedListener = (obs, oldVal, newVal) -> {

		activeGame.updateDimensions(
				new HalmaDimensions((int) newVal.doubleValue(), activeGame.getDimensions().getScreenHeight()));

	};

	private final ChangeListener<Number> heightChangedListener = (obs, oldVal, newVal) -> {

		activeGame.updateDimensions(
				new HalmaDimensions(activeGame.getDimensions().getScreenWidth(), (int) newVal.doubleValue()));

	};

	
	@FXML
	private void initialize() {

		System.out.println("Saluton mondo");
		
	}
	
	
	public void initWithStage(final Stage stage) {

		// Specify app window preferences
		stage.setTitle(HalmaUIConstants.APP_NAME);
		stage.setMinHeight(HalmaUIConstants.STAGE_MIN_HEIGHT);
		stage.setMinWidth(HalmaUIConstants.STAGE_MIN_WIDTH);
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");

		// Attach key listener
		((Scene) stage.getScene()).setOnKeyPressed(keyPressedHandler);
		
		// Disable/enable save button depending on text input (avoids empty save titles)
		saveStateTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
			saveStateButton.setDisable(newValue.length() == 0);
		});
		saveStateButton.setDisable(true);

		// Attach dimension change listeners
		stage.widthProperty().addListener(widthChangedListener);
		stage.heightProperty().addListener(heightChangedListener);
		
		initializeNewGame();

		stage.show();

	}
	
	
	private void initializeNewGame() {
		
		initializeGame(new HalmaGame(HalmaUIConstants.screenDimensions(), gameInterface));
		
	}

	private void initializeGame(final HalmaGame game) {

		this.activeGame = game;
		
		// Remove previous game nodes from stage
		holesPane.getChildren().clear();
		placementsList.getChildren().clear();
		endGamePlacementsList.getChildren().clear();

		// Render pawn holes
		holesPane.getChildren().addAll(activeGame.getHoles());

		hideAllMenuPanes();
		endGamePane.setVisible(false);

		gameHeader.setText(HalmaUIConstants.NEW_GAME_HEADER);
		gameHeader.setVisible(true);

	}

	
	private void displayEndGame() {

		endGamePlacementsList.getChildren().addAll(placementsList.getChildren());

		
		// Display game results
		
		endGameMovesText.setText("after " + activeGame.getTotalTurns() + " turns");

		gameHeader.setVisible(false);

		endGamePane.setVisible(true);

	}

	private void displaySaveGames() {
		
		prepareSaveGameFields();
		
		togglePane(restoreFromSavePane);
		
	}
	
	private void prepareSaveGameFields() {
		
		// Reset container
		saveGameListContainer.getChildren().clear();
		
		final List<HalmaGameState> savedStates = new HalmaGameStateFileHandler().readAllFiles();
		
		if(savedStates.isEmpty()) {
			
			saveGameListContainer.getChildren().add(HalmaUIConstants.noSaveGamesText());
			
			return;
			
		}
		
		for(HalmaGameState savedState : savedStates) {
			
			final SaveGameField saveGameField = new SaveGameField(savedState.getTitle(), savedState.getTimestamp());
			
			saveGameField.setOnMouseClicked((event)->{
				
				// Apply saved dimensions to correctly map pawn holes
				initializeGame(new HalmaGame(savedState.getDimensions(), gameInterface));
				activeGame.restoreState(savedState);

				// Return to optimal dimensions
				activeGame.updateDimensions(HalmaUIConstants.screenDimensions());
				
			});
			
			saveGameListContainer.getChildren().add(saveGameField);
			
		}
		
	}
	
	private void togglePane(final Pane pane) {
		
		final boolean wasVisible = pane.isVisible();
		
		hideAllMenuPanes();
		
		pane.setVisible(!wasVisible);
		
		this.gamePaused = !wasVisible;
		
	}
	
	private void hideAllMenuPanes() {
		
		restoreFromSavePane.setVisible(false);
		saveStatePane.setVisible(false);
		menuPane.setVisible(false);
		
		this.gamePaused = false;
		
	}

	
	@FXML
	public void onSaveButtonClicked() {

		// Double check that text is not empty
		if(saveStateTitleField.getText().length() == 0) {
			
			// Abort saving
			saveStateButton.setDisable(true);
			return;
			
		}
		
		final HalmaGameState stateToSave = activeGame.getState();
		stateToSave.setTitle(saveStateTitleField.getText());
		stateToSave.setTimestamp(System.currentTimeMillis());
		
		new HalmaGameStateFileHandler().saveToFile(
				FileSaveUtils.generateUniqueFilePath(),
				stateToSave
		);
		
		hideAllMenuPanes();
		
	}
	
	@FXML
	public void onCancelSaveButtonClicked(){
		
		hideAllMenuPanes();
		
	}
	
	@FXML
	public void onSaveGameMenuButtonClicked() {
		
		togglePane(saveStatePane);
		
	}
	
	@FXML
	public void onLoadGameMenuButtonClicked() {
		
		displaySaveGames();
		
	}
	
	@FXML
	public void onQuitGameMenuButtonClicked() {
		
		Platform.exit();
		
	}

	
}
