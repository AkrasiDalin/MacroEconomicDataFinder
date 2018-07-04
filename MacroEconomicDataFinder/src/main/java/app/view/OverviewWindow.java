package app.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import app.view.info.DataDialog;
import app.utils.Pair;
import app.utils.ResourceManager;
import app.view.chart.AreaChartC;
import app.view.chart.BarChartC;
import app.view.chart.ChartC;
import app.view.chart.LineChartC;
import app.view.chart.PieChartC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * This class represents the a tab-pane, speciffically the OverviewWindow
 */
public class OverviewWindow extends Tab{

	private BorderPane centreBorder;
	private Pane centreStack;
	private StackPane countryInfo;
	private ListView<String> list;
	private Map<String, String> countryMap;
	private EventHandler<MouseEvent> eventHandler; //Event hander to handles the event
	private EventHandler<ActionEvent> actionHandler; //Event hander to handles the event
	private Button GDPButton;
	private Button GDPPerCapitaButton;
	private Button CPIButton;
	private Button UnemploymentButton;
	private Button CABButton;
	private ChartC chart;
	private String selectedCountryCode;
	private ArrayList<Button> buttonList;
	/**
	 * Constructor, Add Panes to the Tab, and set Tab content
	 */
	public OverviewWindow(){

		//Create a borderPane for the Tab
		BorderPane border = new BorderPane();
		countryInfo = new StackPane();
		border.setLeft(addCountryVBox());
		addCentrePane();

		ArrayList<String> oData = new ArrayList<>();

		border.setCenter(centreBorder);
		this.selectedCountryCode = "";
		this.enableOrDisableButtons();

		//Set Tab title
		setText("COUNTRY DATA");
		setContent(border);

	}
	

	/**
	 * Add the VBox to the Tab, will contain a list of countries for the user to view data for
	 * @return VBox, to be added to tab
	 */
	public VBox addCountryVBox() {

		//Initialise VBox and add padding
		VBox vbox = new VBox();
		vbox.setSpacing(8);
		vbox.getChildren().add(createListView());

		return vbox;
	}


	/**
	 * Creates the List to be presented on the left hand side of the Tab. Takes data from 'Country-Code.csv'
	 * @return ListView<String>,  List of strings, containing country names
	 */
	private ListView<String> createListView(){

		//Read the country code csv and map country names to country codes

		countryMap = new HashMap<String,String>();
		list = null;

		list = new ListView<String>();
		list.getStyleClass().add("list-View");
		//extract the country names and their respective codes from the csv...see ResourceManager
		List<String> countries = ResourceManager.getCountries();
		List<String> countryCodes = ResourceManager.getCountryCodes();
		String countryName = null;
		for(int i = 0, size = countries.size(); i < size; ++i){
			countryName = countries.get(i);
			list.getItems().add(countryName);
			countryMap.put(countryName, countryCodes.get(i));
		}

		//Add the Country names to the ListView, each country has one 'Item' in the ListView
		list.setPrefSize(200, 655);
		return list;
	}


	 //Resets content of countryInfo
	private void remove(){
		if(countryInfo.getChildren().size() == 0) return;
		countryInfo.getChildren().remove(0);
	}


	/**
	 * Add BorderPane to the Tab, holding country overview
	 * @return BorderPane, containing Both the country overview data and @see addIndicatorButtonGrid
	 */
	private void addCentrePane() {
		centreBorder = new BorderPane();
		centreBorder.setPadding(new Insets(0,0,0,0));
		centreStack = new Pane();
		ImageView img = new ImageView(ResourceManager.getImageResource("assets/map2.png"));
		img.setFitHeight(700);
		img.setFitWidth(1000);
		centreStack.getChildren().add(img);
		centreBorder.setCenter(centreStack);
		
		BorderPane bottomBorder = new BorderPane();
		bottomBorder.setCenter(addIndicatorButtonGrid());

		centreBorder.setBottom(bottomBorder);

	}



	/**
	 * sets data to be contained in addCountryText()
	 * @param overviewData the data fro the country to be shown
	 */
	public void setOverview(ArrayList<String> overviewData) {
		centreStack.getChildren().clear();

		ImageView img = new ImageView(ResourceManager.getImageResource("assets/map2.png"));
		img.setFitHeight(700);
		img.setFitWidth(1000);
		
		String countryText = "";

		Double latitude = Double.parseDouble(overviewData.get(6));
		Double longitude = (Double.parseDouble(overviewData.get(5)));
		if(longitude < -47){//South America
			longitude = longitude * 2;
			}
		else if(longitude > 149 && longitude < 170){//Australia
		longitude = longitude * 1.2;
		}else if(latitude > 20){
			latitude = latitude * 1.2;
		}


		Double x = (longitude+180)*(900/360);
		Double latRad = latitude*Math.PI/360;
		Double merCN = Math.log(Math.tan((Math.PI/5)+(latRad)));
		Double y = (520/2)-(900*merCN/(2*Math.PI));


		String[] titles = {"","Capital City:  ","Region:  ","Income Level:  ",
				"Lending Type:  ","Longitude:  ","Latitude:  "};
		
		for(int i = 1; i < overviewData.size();i++){
			countryText = countryText + titles[i]+ overviewData.get(i) + "\n";
		}
		remove();
		countryInfo = addCountryText(countryText);
		Pane cont = new Pane();
		cont.getChildren().add(countryInfo);
		cont.setTranslateX(x);
		cont.setTranslateY(y);
		centreStack.getChildren().addAll(img,cont);
		this.enableOrDisableButtons();
	}

	StackPane layout;
	/**
	 * Adds blue round container with generic infos about the selected country
	 * @param countryString
	 * @return
	 */
	private StackPane addCountryText(String countryString){

		//Add image + Text to stackPane

		Text countryText = new Text(countryString);
		countryText.setFont(Font.font ("Tahoma", 14));
		countryText.setFill(Color.WHITE);
		
		//Add stackpane to grid
		layout = new StackPane();
		layout.getChildren().add(countryText);
		layout.getStyleClass().add("country-infos");
//		layout.getChildren().add(countryText);
		StackPane.setAlignment(countryText,Pos.CENTER);
		return layout;
	}


	/**
	 * Add a grid to the Bottom of the Tab, holding buttons for user to select which indicator to view data for.
	 * @return GridPane, containing buttons for each macro-economic indicator
	 */
	private GridPane addIndicatorButtonGrid() {

		//Create GridPane for the buttons
		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(10);
		grid.setPadding(new Insets(100, 0, 20, 70));
		
		//Create buttons for each Indicator and add them to the grid
		ArrayList<Button> buttonList = addIndicatorButtons();
		for(int i = 0; i < buttonList.size();i++){
			grid.add(buttonList.get(i), i, 0);
		}


		return grid;
	}
	
	/**
	 * Add a mouse event handler for the country list view. Assigned from the Controller
	 * @param controller the event handler  for the mouse
	 */
	public void setEventHandler(EventHandler<MouseEvent> controller){
		this.eventHandler = controller;
		list.setOnMouseClicked(controller);
	}
	
	/**
	 * Add action handler to the view buttons. Assigned from the Controller
	 * @param controller the event handler for the buttons
	 */
	public void setActionHandler(EventHandler<ActionEvent> controller){
		this.actionHandler = controller;
		this.GDPButton.setOnAction(controller);
		this.GDPPerCapitaButton.setOnAction(controller);
		this.CPIButton.setOnAction(controller);
		this.UnemploymentButton.setOnAction(controller);
		this.CABButton.setOnAction(controller);
	}
	
	/**
	 * Return the country code that corresponds to the selected country from the listView
	 * @return Country Code
	 */
	public String getSelectedCountryCode(){
		this.selectedCountryCode = this.getCountryCode(list.getSelectionModel().getSelectedItem().toString());
		return this.selectedCountryCode;
	}

	private String getCountryCode(String s){
		return countryMap.get(s);
	}
	
	private ArrayList<Button> addIndicatorButtons(){
		
		this.buttonList = new ArrayList<Button>();
		
		GDPButton = new Button("GDP");
		GDPButton.setId("GDP");
		GDPButton.getStyleClass().add("selection-button");
		buttonList.add(GDPButton);
		
		GDPPerCapitaButton = new Button("GDP per Capita");
		GDPPerCapitaButton.setId("GDPCapita");
		GDPPerCapitaButton.getStyleClass().add("selection-button");
		buttonList.add(GDPPerCapitaButton);
		
		CPIButton = new Button("Consumer Price (CPI)");
		CPIButton.setId("CPI");
		CPIButton.getStyleClass().add("selection-button");
		buttonList.add(CPIButton);
		
		UnemploymentButton = new Button("Unemployment");
		UnemploymentButton.setId("Unemployment");
		UnemploymentButton.getStyleClass().add("selection-button");
		buttonList.add(UnemploymentButton);
		
		CABButton = new Button("Current Account Balance");
		CABButton.setId("AccountBalance");
		CABButton.getStyleClass().add("selection-button");
		buttonList.add(CABButton);
		
		return buttonList;
	}
	
	private void enableOrDisableButtons(){
		if(this.selectedCountryCode == ""){
			for(Button b: this.buttonList){
				b.setDisable(true);
			}
		}else{
			for(Button b: this.buttonList){
				if(b.isDisable()) b.setDisable(false);
			}
		}
	}
	
	/**
	 * Method to display the single country indicator chart as a dialog box
	 * Called from the Controller, delegated through Window
	 * @param title Title of the chart
	 * @param data Data to be displayed on the chart. @see DataFetcher
	 */
	public void displayOverviewChart(String title, List<Pair<String, BigDecimal>> data) {
		DataDialog.display(title, list.getSelectionModel().getSelectedItem().toString(), data);
	}
}
