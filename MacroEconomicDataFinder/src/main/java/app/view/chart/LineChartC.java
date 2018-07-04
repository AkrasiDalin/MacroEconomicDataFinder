package app.view.chart;

import app.view.chart.tooltip.ToolTip;

import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.view.Country;
import app.utils.Pair;


/**
 * 
 * 
 * Class to display data on a line chart
 * 
 */

public class LineChartC extends ChartC {

    private LineChart<String,Number> lineChart;

    /**
     * It initialises the LineChart and adds it as children
     */
    public LineChartC() {
        super();
        lineChart = new LineChart<String, Number>(xAx, yAx);
        setPane();
    }
	/**
	 * It takes a Set type input from which it extracts key and value
	 * @param data data to be passed in
	 */

    public LineChartC(Set<Map.Entry<Country, List<Pair<String, BigDecimal>>>> data){		
	super(data);
        lineChart = new LineChart<String, Number>(xAx, yAx);
        setPane();
	for(Map.Entry<Country, List<Pair<String, BigDecimal>>> d : data){
		this.addCountrySeries(d.getKey(), d.getValue()); // populates chart with data
	}
    }

    /**
    * Method to reset the graph
    *
    */
    @Override
    public void clear(){
	this.lineChart.getData().clear();
    }

    /**
    * Sets the current chart
    *
    */
    @Override
    public void setPane() {
        getChildren().add(this.lineChart);
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
        for (int i = 0, size = data.size(); i < size; ++i) {
	    XYChart.Data<String,BigDecimal> dataSet = new XYChart.Data<>(data.get(i).getL(), data.get(i).getR());
	    dataSet.setNode(new ToolTip(data.get(i).getR().toString()));
            series.getData().add(dataSet);
        }
        this.lineChart.getData().add(series);
        country.setChartIndex(this.lineChart.getData().size() - 1);
        return country;
    }

	/**
	 * Removes current country data so that other countries data could be displayed
	 * @param toRemove index of the country to be remove
	 */
    @Override
    public void removeCountrySeries(int toRemove) {
        this.lineChart.getData().remove(toRemove);
    }

	/**
	 * Method for setting the title of the chart
	 * @param title title of the chart
	 * @param xTitle title of the x axis
	 */

    @Override
    public void setChartTitle(String title, String xTitle){
	super.setXAndYTitle(xTitle);
	lineChart.setTitle(title);
    }

}
