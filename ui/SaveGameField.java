package app.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SaveGameField extends VBox {
	
	
	private Text saveTitleText;
	
	private Text timestampText;
	
	
	public SaveGameField(final String saveTitle, final long timestamp){
		
		saveTitleText = new Text(saveTitle);
		getChildren().add(saveTitleText);
		
		saveTitleText.setFont(new Font("Century Gothic", 24));
		saveTitleText.setFill(Paint.valueOf("#fff"));
		
		
		final DateFormat formatter = new SimpleDateFormat("d. MMMM y HH:mm");
		formatter.setTimeZone(TimeZone.getDefault());
		String dateFormatted = formatter.format(timestamp);
		
		timestampText = new Text(dateFormatted);
		getChildren().add(timestampText);
		
		timestampText.setFont(new Font("Helvetica", 16));
		timestampText.setFill(Paint.valueOf("#666"));

		
		setPadding(new Insets(10, 20, 10, 20));
		
		setStyle("-fx-background-color: #333333;-fx-background-radius: 4 4 4 4;");
		
		setMargin(this, new Insets(10));
		
		setMinWidth(400.0);
		
	}
	
	
	public String getTitle() {
		
		return this.saveTitleText.getText();
		
	}
	
	
	

}
