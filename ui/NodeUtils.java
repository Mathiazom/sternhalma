package app.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class NodeUtils {
	
	
	public static void centerTextNode(final Text textNode) {
		
		textNode.setBoundsType(TextBoundsType.VISUAL);

		textNode.setX(2*textNode.getX() - textNode.getLayoutBounds().getCenterX());
		textNode.setY(2*textNode.getY() - textNode.getLayoutBounds().getCenterY());
		
	}
	
	

}
