package app.view.chart;
import app.view.Country;
import app.utils.Pair;

import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
* Class that extends from the ChartC abstract Class
* which is specialized to display data as pie chart
*
*
*
*/

public class PieChartC extends ChartC {

	private PieChart pieChart;
	private ObservableList<PieChart.Data> list;
	private String yearsRange;

	/**
	 * It initialises the PieChart and adds it as children
	 */
	public PieChartC(){
		super();
		pieChart = new PieChart();
		list = FXCollections.observableArrayList();
		this.setPane();
	}

	/**
	 * It initialises the PieChart and adds it as children
	 *
	 *
	 *
	 *@param data the data to be fed to the chart
	 *
	 *
	 */
	public PieChartC(Set<Map.Entry<Country, List<Pair<String, BigDecimal>>>> data){
		super(data);
		pieChart = new PieChart();
		list = FXCollections.observableArrayList();
		this.setPane();
		for(Map.Entry<Country, List<Pair<String, BigDecimal>>> d: data){
			this.addCountrySeries(d.getKey(), d.getValue()); // populating chart with data
		}	
		//this.setEventHanlder();
	}

	@Override
	public void clear(){
		this.pieChart.getData().clear();
	}

	/**
	 * Sets the current pie chart
	 */
	@Override
	public void setPane(){
		getChildren().add(this.pieChart);
	}

	/**
	 *  Method for adding data for a certain country to the graph
	 * @param country country to be added to the chart
	 * @param data is the data of the corrisponded country
	 * @return Country updates the country with the current chart index
	 */
	@Override
	public Country addCountrySeries(Country country, List<Pair<String, BigDecimal>> data) {
		double totalFigure = formatData(data);
		this.list.add(new PieChart.Data(country.getName() + this.yearsRange, totalFigure));
		this.pieChart.setData(this.list);
		country.setChartIndex(this.pieChart.getData().size() -1);
		//this.setEventHanlder();
		return country;
	}

	/**
	 * Removes current country data so that other countries data could be displayed
	 * @param toRemove index of the country to be remove
	 */
	@Override
	public void removeCountrySeries(int toRemove){
		this.pieChart.getData().remove(toRemove);
	}

	/**
	 * Method for setting the title of the chart
	 * @param title title of the chart
	 * @param xTitle title of the x axis
	 */
	@Override
	public void setChartTitle(String title, String xTitle){
		this.pieChart.setTitle(title);
	}


	private void setEventHanlder(){
		Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");

		for(PieChart.Data data : this.pieChart.getData()){
			System.out.println("event");
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
			new EventHandler<MouseEvent>(){	
				@Override
				public void handle(MouseEvent e){
					Point2D sceneLocation = new Point2D(e.getScreenX(), e.getScreenY());
					Point2D sceneLocationInParent = sceneToLocal(sceneLocation);
					caption.relocate(sceneLocationInParent.getX(), sceneLocationInParent.getY());
					//caption.setTranslateX(e.getScreenX());
					//caption.setTranslateY(e.getScreenY());
					caption.setText(String.valueOf(data.getPieValue()) + "%");
				}
			});
		}
		getChildren().add(caption);
	}

	// Method used to format data in a meaningful way for the
	// pie chart to display
	private double formatData(List<Pair<String, BigDecimal>> data){
		double amount = 0;
		this.yearsRange = "";
		for(int i = 0, size = data.size(); i < size; ++i){
			amount += data.get(i).getR().doubleValue();
			if(Pattern.matches("[\\d]{4}", data.get(i).getL())){
				if(i == 0) this.yearsRange += "(" + data.get(i).getL() + "-";
				else if ( i == size - 1) this.yearsRange += data.get(i).getL() + ")";
			}
		}
		return amount;
	}
}
