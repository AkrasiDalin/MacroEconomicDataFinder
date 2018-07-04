package app.view.chart.storage;

import app.view.Country;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import app.utils.Pair;

/** 
* 
* This static class is used to cache data from a  
* a graph and use the same data to populate a different graph
*
*/

public class DataStorage {
	private static Map<Country, List<Pair<String, BigDecimal>> > data;

	static {
		data = new HashMap<Country, List<Pair<String, BigDecimal>>>();
	}
	
	// Private constructor to disallow the creation of objects
	private DataStorage(){}
	
	/**
	* 
	* Method to save data that will be send to a grapth series
	* @param country cache data for the specific country
	* @param series a list of related data
	* 
	*/
	public static void cacheData(Country country, List<Pair<String, BigDecimal>> series){
		data.put(country,series);
	}
	
	
	/**
	* 
	* Empty the cached data 
	* 
	*/
	public static void clear(){
		data.clear();
	}
	
	/**
	* 
	* Remove a country from the cached data set
	* @param country remove the country
	* 
	*/
	public static void removeCountry(Country country){
		data.remove(country);
	} 

	/**
	* 
	* Get all the cached data 
	* @return Set the set of all the data
	* 
	*/
	public static Set<Map.Entry<Country, List<Pair<String, BigDecimal>>> > getData(){
		return data.entrySet();
	}
	
	/**
	* 
	* Check if the cache is empty
	*   
	* @return  boolean return true if it is emtpy
	* 
	*/
	public static boolean isEmpty(){
		return data.isEmpty();
	}
	
	/**
	* 
	* Check if the cache is empty
	*   
	* @return  int return the number of data inside the cache 
	* 
	*/
	public static int size(){
		return data.size();
	}
}
