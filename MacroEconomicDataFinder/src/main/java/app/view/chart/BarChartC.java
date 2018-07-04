package app.view.chart;
import app.view.Country;
import app.utils.Pair;

import app.view.chart.tooltip.ToolTip;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;


/**
 * Class that extends from the ChartC abstract class
 * which is specialized to diplay data as a bar chart
 *
 */


public class BarChartC extends ChartC {

	private BarChart<String,Number> barChart;

	/**
	 * It initialises the BarChart and adds it as children
	 */
	public BarChartC(){
		super();
		barChart = new BarChart<String, Number>(xAx, yAx);
		this.setPane();
	}

	public BarChartC(Set<Map.Entry<Country, List<Pair<String, BigDecimal>>>> data){		
		super(data);
		barChart = new BarChart<String, Number>(xAx, yAx);
		this.setPane();
		for(Map.Entry<Country, List<Pair<String, BigDecimal>>> d : data){
			this.addCountrySeries(d.getKey(), d.getValue());
		}
	}

	/**
	 * Clears all the data from the chart
	 */
	@Override
	public void clear(){
		this.barChart.getData().clear();
	}

	/**
	 * Sets the bar chart
	 */
	@Override
	public void setPane() {
		getChildren().add(this.barChart);
	}

	/**
	 *  Method for adding data for a certain country to the graph
	 * @param country country to be added to the chart
	 * @param data the data of the corrisponded country
	 * @return country updates the country with the current chart index
	 */
	@Override
	public Country addCountrySeries(Country country, List<Pair<String, BigDecimal>> data) {
		XYChart.Series series = new XYChart.Series();
		series.setName(country.getName());
		for(int i = 0, size = data.size(); i < size; ++i){
		        XYChart.Data<String,BigDecimal> dataSet = new XYChart.Data<>(data.get(i).getL(), data.get(i).getR());
		        dataSet.setNode(new ToolTip(data.get(i).getR().toString()));
			series.getData().add(dataSet);
		}
		this.barChart.getData().add(series);
		country.setChartIndex(this.barChart.getData().size() - 1);
		return country;
	}

	/**
	 * Removes current country data so that other countries data could be displayed
	 * @param toRemove index of the country to be remove
	 */
	@Override
	public void removeCountrySeries(int toRemove) {
		this.barChart.getData().remove(toRemove);
	}

	/**
	 * Method for setting the title of the chart
	 * @param title title of the chart
	 * @param xTitle title of the x axis
	 */

	@Override
	public void setChartTitle(String title, String xTitle) {
		super.setXAndYTitle(xTitle);
		barChart.setTitle(title);
	}

}

