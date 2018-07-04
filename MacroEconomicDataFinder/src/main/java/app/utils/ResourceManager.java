package app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.io.File;
import javafx.scene.image.Image;

/**
 * This class is used to read the Country-Codes file.
 */
public class ResourceManager {

	private static ArrayList<String> countries;
	private static ArrayList<String> codes;
	private static ArrayList<String> chartImageNames;

	/**
	 * Private constructor for this class to not allow instances of it.
	 */
	private ResourceManager() {
	}

	/**
	 * This method reads the Country-Codes file and builds a list.
	 * 
	 * 
	 */
	private static void readFile() {
		if (countries == null || codes == null) {
			countries = new ArrayList<String>();
			codes = new ArrayList<String>();
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("data/Country-Codes.csv");
			Scanner scanner = new Scanner(in);
			while (scanner.hasNext()) {
				String[] line = scanner.nextLine().split(",");
				countries.add(line[0]);
				codes.add(line[1]);
			}
			scanner.close();
		}
	}

	/**
	 * This method returns the list of country codes from the Country-Codes
	 * file.
	 * 
	 * @return List a list of country codes.
	 */
	public static ArrayList<String> getCountryCodes() {
		readFile();
		return codes;
	}

	/**
	 * This method returns the list of countries from the Country-Codes file.
	 * 
	 * @return List a list of countries
	 */
	public static ArrayList<String> getCountries() {
		readFile();
		return countries;
	}

	/**	
	*
	* Helper method to fetch an image from a given resource folder
	* @param path the path to the image
	* @return Image returns the image 
	*
	*/
	public static Image getImageResource(String path){
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
		return new Image(in);
	}
}
