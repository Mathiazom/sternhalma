package app.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class PlacementIndicator extends Group {

	public PlacementIndicator(final int placement, final int totalTurns, final Color color) {
		
		final Text placementText = new Text(String.valueOf(placement));
	
		placementText.setFont(new Font("Palatino Linotype Bold", 25));
		placementText.setFill(Color.WHITE);
		
		NodeUtils.centerTextNode(placementText);
		
		final Circle placementCircle = new Circle(
				placementText.getLayoutBounds().getCenterX(),
				placementText.getLayoutBounds().getCenterY(),
				25,
				color
		);
		
		final Text turnsText = new Text(String.valueOf(totalTurns));
		
		turnsText.setFont(new Font("Helvetica", 16));
		turnsText.setFill(Color.WHITE);
		
		turnsText.setBoundsType(TextBoundsType.VISUAL);

		turnsText.setY(placementCircle.getLayoutBounds().getMaxY() + 1.5*turnsText.getLayoutBounds().getHeight());
		
		NodeUtils.centerTextNode(turnsText);
		
		
		this.getChildren().addAll(placementCircle, placementText, turnsText);
		
		
	}
	
	

}
