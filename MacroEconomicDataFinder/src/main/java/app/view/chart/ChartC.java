package app.view.chart;

import app.view.Country;
import app.utils.Pair;
import java.util.List;
import java.math.BigDecimal;

import java.util.Map;
import java.util.Set;
import javafx.scene.layout.StackPane;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;

/**
*
* Abstract Chart class to support multiple charts to visualize data 
*
*
*
*/

public abstract class ChartC extends StackPane {
	protected CategoryAxis xAx;
	protected NumberAxis yAx;

	/**
	* Abstrat constructor to initialize chart class
	*
	*
	*/

	public ChartC(){
		super();
		this.xAx = new CategoryAxis();
		this.yAx = new NumberAxis();
	}
	
	/**
	* Abstract constructor to initiliza graph with data 
	* already provided
	* @param data the whole data set
	*/

	public ChartC(Set<Map.Entry<Country, List<Pair<String, BigDecimal>>>> data){
		super();
		this.xAx = new CategoryAxis();
		this.yAx = new NumberAxis();
	}
	
	/**
	* Method to display the chart into the window
	*
	*/
	abstract public void setPane();

	/**
	* Methos to pass a certain country data to the chart
	* to diplay and return the counry with its chart id update
	*
	* @param country the country to be added
	* @param data the data of the country
	* @return Country the country with the updated chart index
	*/
	abstract public Country addCountrySeries(Country country, List<Pair<String, BigDecimal>> data);

	
	/**
	* Methos to remove a certain country entry from the chart
	* currently diplaing the data
	*
	* @param toRemove the country chart index
	*
	*/
	abstract public void removeCountrySeries(int toRemove);
	
	/**
	* Remove all data from the grapth
	*
	* 
	*
	*/
	abstract public void clear();
	
	/**
	* Set title to chart
	*
	* @param title the title of the chart
	* @param xTitle the title of the x axis
	*
	*/
	abstract public void setChartTitle(String title, String xTitle);

	/**
	*
	* Method to set the x and y label caption on the chart
	* @param xTitle the tile fo the x axis 
	*
	*/

	public void setXAndYTitle(String xTitle){
		xAx.setLabel(xTitle);
		yAx.setLabel("Value");
	}
}
