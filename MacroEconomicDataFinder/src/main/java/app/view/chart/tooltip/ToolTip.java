package app.view.chart.tooltip;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

/**
 * This class generates serves to generate a label containing a String given at costruction.
 * The label will be displayed when a node will be hovered.
 */
public class ToolTip extends StackPane {
	
	private Label label;

	/**
	 * Takes a String value a generates a label out of it.
	 * @param value the value the the toolTip should display when the mouse hovers over it
	 */
	public ToolTip(String value){
		super();
		this.label = new Label(value);
		this.label.getStyleClass().add("tool-Tip");
		this.setLabelProperty();
		this.setEventHandler();
	}



	 //sets up label with default size=10 and weight=bold
	private void setLabelProperty(){
		this.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
		this.label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
	}



	//sets event handler which displays values on mouse-hover
	private void setEventHandler(){
		this.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				ToolTip.this.getChildren().setAll(ToolTip.this.label);
				ToolTip.this.setCursor(Cursor.NONE);
				ToolTip.this.toFront();
			}
		});

		this.setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				ToolTip.this.getChildren().clear();
				ToolTip.this.setCursor(Cursor.CROSSHAIR);
			}
		});
	}
}
