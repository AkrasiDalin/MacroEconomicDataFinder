package app.model;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import app.utils.ResourceManager;
import app.utils.Pair;

/**
 *
 *         This class is used to fetch data from the World Bank API and read
 *         that information and provide it to a controller.
 *
 */
public class DataFetcher {

	private static boolean done = false;
	/**
	 * Private constructor to prevent instantiation of this class as it's
	 * methods are all static.
	 */
	private DataFetcher() {
	}

	/**
	 * This method downloads data from the world bank API and saves it into an
	 * XML file.
	 *
	 * @param link
	 * @param file
	 * @throws IOException
	 */
	private static void getPage(String link, String file) throws IOException {
		File myOutput;
		if (file.contains("-")) {
			myOutput = new File("data/" + file.split("-")[1] + "/" + file + ".xml");
		} else
			myOutput = new File("data/" + file + ".xml");
		myOutput.getParentFile().mkdirs();
		if (myOutput.exists()) {
			LocalDate fileDate = new Date(myOutput.lastModified()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			LocalDate currentDate = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (currentDate.getDayOfYear() - fileDate.getDayOfYear() < 7) {
				return;
			}
		}
		try {
			URL url = new URL(link);
			URLConnection conn = url.openConnection();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(conn.getInputStream());

			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer xform = tfactory.newTransformer();
			xform.transform(new DOMSource(doc), new StreamResult(myOutput));

		} catch (ParserConfigurationException | SAXException | TransformerException e) {
			System.out.println("Error downloading data into XML file: " + e.getMessage());
		}
	}

	/**
	 * This method reads data from an XML file and returns an ArrayList with
	 * that data.
	 *
	 * @param file
	 * @return list of pairs
	 * @throws IOException
	 */
	private static ArrayList<Pair<String, BigDecimal>> parsePage(String file)
			throws IOException, NumberFormatException {
		ArrayList<Pair<String, BigDecimal>> data = new ArrayList<Pair<String, BigDecimal>>();
		try {
			File inputFile = new File("data/" + file.split("-")[1] + "/" + file + ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = (doc.getElementsByTagName("wb:data"));
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String val = eElement.getElementsByTagName("wb:value").item(0).getTextContent();
					if (val.isEmpty()) {
						val = "0";
					}
					data.add(new Pair<String, BigDecimal>(
							eElement.getElementsByTagName("wb:date").item(0).getTextContent(), new BigDecimal(val)));
				}
			}

		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("Error reading XML file: " + e.getMessage() + e.toString());
		}
		data.remove(0);
		return data;
	}

	/**
	 * Gets the overview information of a country.
	 *
	 * @param country country to see
	 * @return List list of strings of the overview
	 * @throws IOException thorws exeption if the file is not downloaded
	 */
	public static ArrayList<String> getOverview(String country) throws IOException {
		getPage("http://api.worldbank.org/countries/" + country, country + "-Overview");
		ArrayList<String> data = new ArrayList<String>();
		try {
			File inputFile = new File("data/Overview/" + country + "-Overview" + ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = (doc.getElementsByTagName("wb:country"));
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					data.add(eElement.getElementsByTagName("wb:name").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:capitalCity").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:region").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:incomeLevel").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:lendingType").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:longitude").item(0).getTextContent());
					data.add(eElement.getElementsByTagName("wb:latitude").item(0).getTextContent());
				}
			}
		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("Error getting overview: " + e.getMessage());
		}
		return data;
	}

	/**
	 * Gets the specified data for a specified country. The data it returns
	 * depends on which method calls this method.
	 * 
	 * @param url
	 * @param country
	 * @param dataType
	 * @return list of pairs
	 * @throws IOException
	 */
	private static ArrayList<Pair<String, BigDecimal>> getData(String url, String country, String dataType)
			throws IOException, NumberFormatException {
		getPage("http://api.worldbank.org/countries/" + country + url, country + dataType);
		ArrayList<Pair<String, BigDecimal>> ret = parsePage(country + dataType);
		Collections.reverse(ret);
		return ret;
	}

	/**
	 * Gets the GDP for the last 10 years of a country.
	 *
	 * @param country country code
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 *
	 */
	public static ArrayList<Pair<String, BigDecimal>> getGDP(String country) throws IOException, NumberFormatException {
		return getData("/indicators/NY.GDP.MKTP.CD?MRV=10&frequency=Y", country, "-GDP");
	}

	/**
	 * Gets the GDP per capita for the last 10 years of a country.
	 *
	 * @param country country code
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> getGDPcapita(String country)
			throws IOException, NumberFormatException {
		return getData("/indicators/NY.GDP.PCAP.CD?MRV=10&frequency=Y", country, "-GDPcapita");
	}

	/**
	 * Gets the CPI for the last 10 years of a country.
	 *
	 * @param country country code
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> getCPI(String country) throws IOException, NumberFormatException {
		return getData("/indicators/FP.CPI.TOTL.ZG?MRV=10&frequency=Y", country, "-CPI");
	}

	/**
	 * Gets the Unemployment rate for the last 10 years of a country.
	 *
	 * @param country country code
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> getUnemployment(String country)
			throws IOException, NumberFormatException {
		return getData("/indicators/SL.UEM.TOTL.ZS?MRV=10&frequency=Y", country, "-Unemployment");
	}

	/**
	 * Gets the Account Balance for the last 10 years of a country.
	 *
	 * @param country country code
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> getAccountBalance(String country)
			throws IOException, NumberFormatException {
		return getData("/indicators/BN.CAB.XOKA.CD?MRV=10&frequency=Y", country, "-AccountBalance");
	}

	/**
	 * This method compares data for required data of the given countries in a
	 * given year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	private static ArrayList<Pair<String, BigDecimal>> compareData(String url, String[] countries)
			throws IOException, NumberFormatException {
		Arrays.sort(countries);
		String all = String.join(";", countries);
		ArrayList<Pair<String, BigDecimal>> data = getData(url, all, "-Compare");
		Collections.reverse(data);
		ArrayList<Pair<String, BigDecimal>> pairs = new ArrayList<Pair<String, BigDecimal>>();
		for (int i = 0; i < data.size(); i++) {
			pairs.add(new Pair<String, BigDecimal>(countries[i], data.get(i).getR()));
		}
		new File("data/Compare/" + all + "-Compare.xml").delete();
		return pairs;
	}

	/**
	 * This method compares the GDP of a given list of countries in a given
	 * year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> compareGDP(String[] countries, String year)
			throws IOException, NumberFormatException {
		return compareData("/indicators/NY.GDP.MKTP.CD?MRV=1&date=" + year, countries);
	}

	/**
	 * This method compares the GDP per capita of a given list of countries in a
	 * given year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> compareGDPcapita(String[] countries, String year)
			throws IOException, NumberFormatException {
		return compareData("/indicators/NY.GDP.PCAP.CD?MRV=1&date=" + year, countries);
	}

	/**
	 * This method compares the CPI of a given list of countries in a given
	 * year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> compareCPI(String[] countries, String year)
			throws IOException, NumberFormatException {
		return compareData("/indicators/FP.CPI.TOTL.ZG?MRV=1&date=" + year, countries);
	}

	/**
	 * This method compares the Unemployment rate of a given list of countries
	 * in a given year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> compareUnemployment(String[] countries, String year)
			throws IOException, NumberFormatException {
		return compareData("/indicators/SL.UEM.TOTL.ZS?MRV=1&date=" + year, countries);
	}

	/**
	 * This method compares the Account Balances of a given list of countries in
	 * a given year.
	 *
	 * @param countries list of countries code
	 * @param year year you want to compare against
	 * @return List list of pairs
	 * @throws IOException exception is thrown if data cannot be retrived
	 */
	public static ArrayList<Pair<String, BigDecimal>> compareAccountBalance(String[] countries, String year)
			throws IOException, NumberFormatException {
		return compareData("/indicators/BN.CAB.XOKA.CD?MRV=1&date=" + year, countries);
	}

	/**
	 * Fetches all the data in a separate thread.
	 */
	public static void fetchAllData() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					ArrayList<String> l = ResourceManager.getCountryCodes();
					for (int i = 0; i < l.size(); i++) {
						getOverview(l.get(i));
						getGDP(l.get(i));
						getGDPcapita(l.get(i));
						getCPI(l.get(i));
						getUnemployment(l.get(i));
						getAccountBalance(l.get(i));
					}
					done = true;
				} catch (IOException | NumberFormatException e) {
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
					done = true;
				}
			}
		});
		t.start();
	}

	/**
	* Method that tell when the cache is done fetching
	* regardless if the fetch went well or not
	*
	* @return boolean
	*
	*/
	public static boolean isCacheDone(){
		return done;
	}

	/**
	 * Method to delete all cached data files.
	 * 
	 * @throws IOException clear cache for the appliation
	 */
	public static void clearCache() throws IOException {
		if (new File("data").exists()) {
			Path directory = Paths.get("data");
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			done = false;
		}
	}
}
