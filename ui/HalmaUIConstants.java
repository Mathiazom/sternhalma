package app.ui;

import app.game.PlayerIdentity;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class HalmaUIConstants {

	public static final String APP_NAME = "Sternhalma";

	public static final int STAGE_MIN_HEIGHT = 600;
	public static final int STAGE_MIN_WIDTH = 800;

	public static final HalmaDimensions screenDimensions() {
		final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		return new HalmaDimensions(bounds.getWidth(), bounds.getHeight());
	}

	public static final String NEW_GAME_HEADER = "Make a move to start";
	public static final Color GAME_HEADER_PRIMARY_FILL = Color.rgb(238, 238, 238);
	public static final Color GAME_HEADER_ERROR_FILL = Color.rgb(229, 0, 0);

	public static final Color GREEN_PLAYER_COLOR = Color.rgb(53, 204, 53);
	public static final Color YELLOW_PLAYER_COLOR = Color.rgb(229, 229, 0);
	public static final Color ORANGE_PLAYER_COLOR = Color.rgb(255, 147, 25);
	public static final Color RED_PLAYER_COLOR = Color.rgb(229, 0, 0);
	public static final Color PURPLE_PLAYER_COLOR = Color.rgb(198, 0, 229);
	public static final Color BLUE_PLAYER_COLOR = Color.rgb(66, 66, 255);

	public static final PlayerIdentity[] PLAYER_IDENTITIES = {
			new PlayerIdentity("Green", Color.rgb(53, 204, 53)),
			new PlayerIdentity("Yellow", Color.rgb(229, 229, 0)),
			new PlayerIdentity("Orange", Color.rgb(255, 147, 25)),
			new PlayerIdentity("Red", Color.rgb(229, 0, 0)),
			new PlayerIdentity("Purple", Color.rgb(198, 0, 229)),
			new PlayerIdentity("Blue", Color.rgb(66, 66, 255))
	};

	public static final Color PAWN_ACTIVE_STROKE = Color.WHITE;

	public static final int PAWN_STROKE_WIDTH = 2;

	public static final double PAWN_DIM_DIVISOR = 20;
	public static final double PAWN_X_STEP_MULTIPLIER = 1.2;
	public static final double PAWN_Y_STEP_MULTIPLIER = Math.sqrt(3) / 2.0;

	public static final Color NOT_OCCUPIED_COLOR = Color.rgb(33, 33, 33);
	public static final Color NOT_BASE_STROKE = Color.rgb(33, 33, 33);

	public static final Text noSaveGamesText() {

		final Text noSavesText = new Text("No save games found");
		noSavesText.setFont(new Font("Century Gothic", 22.0));
		noSavesText.setFill(Paint.valueOf("#666"));
		return noSavesText;

	}

}
