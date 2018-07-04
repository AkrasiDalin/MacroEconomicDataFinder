package app.view.info;

import app.view.chart.LineChartC;
import app.utils.Pair;
import app.view.Country;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.math.BigDecimal;
import java.util.List;
import javafx.scene.control.Button;

/**
 * This class represents a window which appears when a data-button in the OverviewWindow is pressed
 */

public class DataDialog {

	private static Button closingButton;
	private static Stage window;

	static {
		closingButton = new Button("Close");
	}

	private DataDialog(){}

	private static void setData(LineChartC lineChart, Country country, List<Pair<String, BigDecimal>> data){
		lineChart.addCountrySeries(country, data);
	}

	/**
	 * Given a country, it generates a graph
	 *
	 * @param title the caption of the graph
	 * @param countryName the name of the country selected
	 * @param data the country data
	 */
	public static void display(String title, String countryName, List<Pair<String, BigDecimal>> data){
		LineChartC lineChart = new LineChartC();
		setData(lineChart, new Country(countryName, ""), data);
		BorderPane layout = new BorderPane();

		setChart(lineChart, layout, title);
		setClosingButton(layout);

		window = new Stage();
		Scene scene = new Scene(layout, 400, 500);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);	
		window.setScene(scene);
		window.showAndWait();
	}
	
	private static void setClosingButton(BorderPane layout){
		closingButton.setOnAction(e ->  window.close() );
		closingButton.prefWidthProperty().bind(layout.widthProperty());
		layout.setBottom(closingButton);
	}
	
	private static void setChart(LineChartC lineChart, BorderPane layout, String title){
		lineChart.setChartTitle(title, "Year");
		layout.setCenter(lineChart);
	}
}
