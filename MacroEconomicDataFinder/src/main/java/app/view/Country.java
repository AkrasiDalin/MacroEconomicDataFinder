package app.view;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import javafx.scene.layout.StackPane;

public class Country {
    private String name;
    private String code;
    private int index;

    /**
     * It takes country name and country code which get saved
     *
     * @param name the country name
     * @param code the country code
     */
    public Country(String name, String code){
	this.name = name;
	this.code = code;
	this.index = -1;
    }

    /**
     * Returns country's code
     * @return String the country code
     */
    public String getCode(){
	return this.code;
    }


    /**
     * Returns country's name
     * @return String the country name
     */
    public String getName(){
	return this.name;
    }
	
    @Override 
    public String toString(){
	return this.getName();
    }

    /**
     * Sets index to chart
     * @param chartIndex set this country chart index
     */
    public void setChartIndex(int chartIndex){
	this.index = chartIndex;
    }

    /**
     * Returns index of chart
     * @return int get the chart index of this country 
     */
    public int getChartIndex(){
	return this.index;
    }

    /**
     * resets the index of chart
     */
    public void resetChartIndex(){
	this.index = -1;
    }

    /**
     * Compares to countries to evaluate if they are equal
     * @param o the object to be compared against
     * @return boolean true if they have the same name value
     */
    @Override 
    public boolean equals(Object o){
	if(o instanceof Country){
		Country c = (Country) o;
		return this.getName().equals(c.getName());
	}
	return false;
    }
}
