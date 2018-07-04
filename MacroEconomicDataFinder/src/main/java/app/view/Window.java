package app.view;

import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import app.utils.DataType;
import app.utils.Pair;
import app.utils.ResourceManager;
import app.controller.Controller;



public class Window extends Stage {

	private StatisticWindow statisticWindow; // reference to the statistic window
	private OverviewWindow overviewWindow; // reference to the overview window
	private Scene scene; // main sceen of the 
	private TabPane mainTabPane; // master tab that holds all tabs
	private BorderPane mainPane; //  main layout used on this class

	/**
	* Cunstruct to show the main app on screen
	*/	
	public Window(){
		super();
		setTitle("Macro-ecomomic Data Finder");
		this.mainPane = new BorderPane();
		this.mainTabPane = new TabPane();
		this.setTabPaneTabs();
		this.setBottomPane();
		//addTitleBar();
		this.mainTabPane.getStyleClass().add("main-pane");

		// Set the scene
		this.scene = new Scene(this.mainPane, 1190, 735);
		this.setScene(this.scene);

		this.scene.getStylesheets().add("static/StatisticWindow.css");
		this.scene.getStylesheets().add("static/OverviewWindow.css");
	}

	/**
	* This method starts the main window to be displayed
	* 
	*
	*/	
	public void start() {
		this.show();
	}

	// Set the bottom pane for the main window
	private void setBottomPane(){
		HBox bottomLayout = new HBox();
		bottomLayout.setPadding(new Insets(10,10,10,10));
		bottomLayout.setAlignment(Pos.CENTER_RIGHT);
		bottomLayout.getStyleClass().add("bottom-Line");
		Text appInfo = new Text("All the data available is provided by the worldbank.org API. Please visit their website for more information.");
		appInfo.getStyleClass().add("bottom-Text");
		bottomLayout.getChildren().add(appInfo);
		this.mainPane.setBottom(bottomLayout);
	}

	//Sets tabs to be displayed by the main window
	private void setTabPaneTabs(){
		this.statisticWindow = new StatisticWindow();
		this.overviewWindow = new OverviewWindow();

		this.mainTabPane.getTabs().add(this.overviewWindow);
		this.mainTabPane.getTabs().add(this.statisticWindow);

		this.setTabPaneSettings();
		// add tabs to the main tabPane
		this.mainPane.setCenter(this.mainTabPane);
	}

	// set properties of the tab pane
	private void setTabPaneSettings(){
		this.mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	}

	
	/*------------------------- Overview ---------------------------------*/	

	/**	
	* Method use to set the controller to the window
	*
	* @param controller the controller for this window 
	*
	*/
	public void setEventHandler(Controller controller){
		if(controller == null) return;
		this.statisticWindow.setEventHandler(controller.getActionHandler());
		this.overviewWindow.setEventHandler(controller.getMouseHandler());
		this.overviewWindow.setActionHandler(controller.getActionHandler());
	}

	/**
	*
	* Update the overview of a specific country selected on the list view
	* The data will be displayed as a bubble hovering on top of the selected
	* country
	* @param infos the info for the country to be viewed
	* 
	*
	*/ 
	public void updateOverview(ArrayList<String> infos){
		overviewWindow.setOverview(infos);
	}
	

	public void displayOverviewChart(String title, List<Pair<String, BigDecimal>> data){
		this.overviewWindow.displayOverviewChart(title, data);
	}

	public String getSelectedCountryCode(){
		return overviewWindow.getSelectedCountryCode();
	}

	/*------------------------- End Overview ---------------------------------*/	
		
	/*-------------------- Selection -------------------------*/	

	public Country[] getSelectedCountries(){
		return this.statisticWindow.getSelectedCountries();	
	}
	
	public boolean isRemovalMode(){
		return this.statisticWindow.isRemovalMode();
	}

	/**
	 * Returns data fetched
	 * @return DataType the type of the data being requested 
	 */
	public DataType getRequestedData(){
		return this.statisticWindow.getRequestedData();
	}

	/**
	 * Given country array and fetched data, updates data chart
	 * @param countries the array of countries to be displayed
	 * @param data the data of each country being passed
	 * @param isUpdate boolean value in to determine whether the graph should be update or not 
	 *
	 */
	public void setSelectionDataToChart(Country[] countries, List<List<Pair<String, BigDecimal>>> data, boolean isUpdate){
		this.statisticWindow.setSelectionDataToChart(countries, data, isUpdate);
	}

	/**
	 * Removes countries from chart
	 * @param countries the number of countries to be removed
	 */
	public void removeCountriesFromSelected(Country[] countries){
		this.statisticWindow.removeCountriesFromSelected(countries);
	}

	/**
	 * Checks current mode
	 * @return boolean determine the mode of the window. True means is in selection mode false is on comparison mode
	 */
	public boolean isSelectionMode(){
		return this.statisticWindow.isSelectionMode();
	}
	/*-------------------- End Selection -------------------------*/	

	/*-------------------- Comparison  -------------------------*/

	/**
	 * Returns array of countries
	 * @return Pair get the pair of countries and the year to send to the model 
	 */
	public Pair<String[], String> getCountriesToCompare(){
		return this.statisticWindow.getCountriesToCompare();
	}

	/**
	 * Updates chart with given data
	 * @param data the data of each country retrived from the model 
	 * @param isUpdate boolean value to check if the graph needs to be updated or not
	 */
	public void setCompareDataToChart(List<Pair<String, BigDecimal>> data, boolean isUpdate){
		this.statisticWindow.setCompareDataToChart(data, isUpdate);
	}

	/*-------------------- End Comparison  -------------------------*/	
	
}
