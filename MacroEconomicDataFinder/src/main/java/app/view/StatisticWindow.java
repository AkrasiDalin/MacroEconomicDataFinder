package app.view;

import app.controller.Controller;
import app.view.chart.ChartC;
import app.view.chart.storage.DataStorage;
import app.view.chart.LineChartC;
import app.view.chart.BarChartC;
import app.view.chart.AreaChartC;
import app.view.chart.PieChartC;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.Group;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Calendar;
import java.io.File;

import app.utils.ResourceManager;
import app.utils.Pair;
import app.utils.DataType;

import javafx.event.EventHandler;
import javafx.event.Event;

/**
 *
 * This class shows the user all
 * kind of statistic and data visualization to perfome thier research
 *
 *
 */

public class StatisticWindow extends Tab {
	private ChartC chart; // chart pane in the centre of the window
	private StackPane layout; // main pane where everything is attached to
	private BorderPane mainPane; // primary layout pane to diplay each section
	private VBox leftPane; // left pane for the controll otptions
	private CheckBox countrySelectionMode; // Check box to toggle between insertion and deletion mode
	private BorderPane rightPane; // right pane to diplay the type of chart the user wants to use
	private Button selectionButton; // Button to fire the diplay series on the graph pane
	private Button comparisonButton; // Button to initiate the comparison between countries
	private DataType currentRequestedData; // Property to store which data type the user wants to see when data is requested
	private ToggleGroup dataGroup; // group to allow only one radio button (data type) to be selected at a time
	private GridPane dataTypesPane; // Pane to display all possible radio button (data type) supported by the applicatoion
	private ListView compareCountriesList; // Listview to select multiple countries to be compared against
	private Spinner<Integer> years; // Spinner to choose in which year a set of contries will be compared against
	private List<Country> selectedCountries; // List of the currently selected country
	private List<Country> comparedCountries; // Current sected countries to compare
	private VBox selectionPane; // selection Pane
	private VBox comparisonPane; // Comapasion Pane
	private EventHandler<ActionEvent> eventHandler; //Event hander to handles the event
	private List<RadioButton> radioButtons; // List of radio buttons added in the pane
	private String chartTitle; // String used to set the title to the graph

	private TextField searchTextField ;
	private ObservableList<Country> oCountriesList;
	private ListView countriesListView;
	private VBox countriesListPane; // selection Pane

	private Mode currentMode; // Keeps track of the state of the class whethe is or compare or selection mode
	private Button clearCacheButton;

	// Internal enum type to define the state of the class
	private enum Mode {
		Selection, Compare
	}

	/**
	 * Constructor to create an instance of a StatisticWindow
	 *
	 */

	public StatisticWindow(){
		super();
		setText("STATISTIC");
		this.currentMode = Mode.Selection;
		this.layout = new StackPane();
		this.mainPane = new BorderPane();
		this.selectionPane = new VBox();
		this.comparisonPane = new VBox();
		this.currentRequestedData = DataType.GDP;
		this.selectedCountries = new ArrayList<Country>();
		this.comparedCountries = new ArrayList<Country>();
		this.setPane();
		this.layout.getChildren().add(this.mainPane);
		setContent(this.layout);
	}

	// Get the mode the class is currently in
	private Mode getCurrentMode(){
		return this.currentMode;
	}

	// set Window
	private void setPane(){
		this.setControlsLeftPane();
		this.setChartSelectionRightPane();
		this.setOptionSelectionTopPane();
		this.setGraphCenterPane(null);
	}

    /*----------------------------- Left Pane ---------------------------------------*/

	// Left pane properties
	private void setControlsLeftPane(){
		this.populateLeftPane();

		this.leftPane.setPrefWidth(250);
		this.leftPane.getStyleClass().add("left-Pane");

		//this.addSeparator(this.leftPane);
		this.selectionPane.setPadding(new Insets(30, 15, 10, 15));
		this.selectionPane.setSpacing(10);
		this.selectionPane.getStyleClass().add("selection-Pane");

		this.comparisonPane.setPadding(new Insets(30, 15, 10, 15));
		this.comparisonPane.setSpacing(10);
		this.mainPane.setLeft(this.leftPane);
		//this.addSeparator(this.leftPane);
	}

	// Method that initialize all the components for the window to be displayed properly
	private void populateLeftPane(){
		this.leftPane = new VBox();
		this.populateDataTypePane();
		this.setSearchTextField();
		this.setCountriesListView();
		this.setSelectionButtonPane();
		this.populateToggleModePane();
		this.setComparisonButton();
	}

	private void setCountriesListView(){
		this.countriesListView = new ListView();
		this.countriesListView.getStyleClass().add("comparison-List");
		this.countriesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.oCountriesList = FXCollections.observableArrayList();
		this.populateCountriesListView();
	}

	private void populateCountriesListView(){
		this.countriesListPane = new VBox();
		List<String> countries = ResourceManager.getCountries();
		List<String> countryCodes = ResourceManager.getCountryCodes();
		for(int i = 0, size = countries.size(); i < size; ++i){
			this.oCountriesList.add(new Country(countries.get(i), countryCodes.get(i)));
		}
		this.countriesListView.setItems(this.oCountriesList);
		this.countriesListPane.getChildren().add(this.countriesListView);
		this.leftPane.getChildren().add(this.countriesListPane);
	}


	private void setSearchTextField(){
		HBox layout = new HBox();
		layout.setPadding(new Insets(5,5,5,5));
		this.searchTextField = new TextField();
		this.searchTextField.getStyleClass().add("search-Field");
		this.searchTextField.setPromptText("Type here to search for a country");
		this.setSearchTextFieldChangeListener();
		layout.getChildren().add(this.searchTextField);
		this.leftPane.getChildren().add(layout);
	}

	private void setSearchTextFieldChangeListener(){
		this.searchTextField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> ob, String oldValue, String newValue){
				StatisticWindow.this.search(oldValue, newValue);
			}

		});
	}

	private void search(String oldV, String newV){
		ObservableList<Country> entries = FXCollections.observableArrayList();
		for(Country c: this.oCountriesList){
			newV = newV.toLowerCase();
			if(c.getName().toLowerCase().contains(newV)){
				entries.add(c);
				this.countriesListView.setItems(entries);
			}
		}

	}

	// Toggle pane to diplay how to switch between insertion
	//  and deletion of a country
	private void populateToggleModePane(){
		HBox toggleModeLayout = new HBox();
		toggleModeLayout.setPadding(new Insets(5,5,5,5));
		this.countrySelectionMode = new CheckBox();
		this.countrySelectionMode.getStyleClass().add("selection-Mode");
		this.countrySelectionMode.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue< ? extends Boolean> ov, Boolean oldV, Boolean newV){
				if(newV) StatisticWindow.this.selectionButton.setText("Remove country");
				else StatisticWindow.this.selectionButton.setText("Add country");
			}
		});

		Label header = new Label("Select to remove country from chart: ");
		//header.setWrapText(true);
		header.setMinWidth(Region.USE_PREF_SIZE);
		toggleModeLayout.getChildren().addAll(header, this.countrySelectionMode);
		this.selectionPane.getChildren().add(toggleModeLayout);
		this.leftPane.getChildren().add(this.selectionPane);
	}

	// this Button is responsible to initiate the graph visualization
	private void setSelectionButtonPane(){
		HBox hBox = new HBox(10);
		this.selectionButton = new Button("Add country");
		this.selectionButton.setId("selection");
		this.selectionButton.getStyleClass().add("selection-button");

		Button removeAll = new Button("Remove all");
		removeAll.getStyleClass().add("selection-button");
		removeAll.setOnAction(e -> { StatisticWindow.this.removeAllFromChart(); });
		hBox.getChildren().addAll(this.selectionButton, removeAll);
		this.selectionPane.getChildren().add(hBox);
	}

	// Diplay all diffente type of data
	private void populateDataTypePane(){
		this.radioButtons = new ArrayList<RadioButton>();
		VBox localLayout = new VBox(25);
		localLayout.setPadding(new Insets(35,25,35,25));
		this.dataTypesPane = new GridPane();
		this.dataTypesPane.setVgap(5.0);
		this.dataTypesPane.setHgap(10.0);
		this.dataGroup = new ToggleGroup();
		String[] types = {"GDP", "GDP Capita", "CPI", "Unemployment", "AccountBalance" };
		for(int i = 0, size = types.length; i < size; ++i){
			RadioButton selection = new RadioButton(types[i]);
			//selection.setWrapText(true);
			selection.setMinWidth(Region.USE_PREF_SIZE);
			if(types[i].equals("GDP")) selection.setSelected(true);
			this.chartTitle = new String("GDP");
			selection.setToggleGroup(dataGroup);
			selection.setUserData(types[i]);
			this.dataTypesPane.add(selection,i % 2, i % 3);
			this.radioButtons.add(selection);
		}
		dataGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldV, Toggle newV){
				if(StatisticWindow.this.dataGroup.getSelectedToggle() != null){
					StatisticWindow.this.updateCurrentRequestedData(StatisticWindow.this.dataGroup.getSelectedToggle().getUserData().toString());
				}
			}
		});
		Label heading = new Label("Types of data:");
		localLayout.getChildren().add(heading);
		localLayout.getChildren().add(this.dataTypesPane);
		this.leftPane.getChildren().add(localLayout);
	}

	// Button use to trigger an envent to compare one to multiple contries
	private void setComparisonButton(){
		VBox layout = new VBox(20);
		this.comparisonButton = new Button("Compare");
		this.comparisonButton.setId("comparison");
		this.comparisonButton.getStyleClass().add("selection-button");

		layout.getChildren().add(this.comparisonButton);

		this.setYearSelection(layout);
	}

	// Method that sets a spinner to select a given year to compare
	private void setYearSelection(VBox layout){
		HBox hlayout = new HBox(10);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		this.years = new Spinner<Integer>(1940, year, year);
		this.years.getStyleClass().add("year-Spinner");
		hlayout.getChildren().addAll(new Label("Select year:"), years);
		layout.getChildren().addAll(hlayout);
		this.comparisonPane.getChildren().add(layout);
		this.leftPane.getChildren().add(this.comparisonPane);
	}
    /*----------------------------- Left Pane End ---------------------------------------*/


    /*------------------------------- Utilities ---------------------------------------*/

	// Query method to get check if country is already used
	// in comparison based on its code
	private boolean comparedCountriesCointains(String code){
		for(Country c : this.comparedCountries){
			if(c.getCode().equals(code)) return true;
		}
		return false;
	}

	// Add countries from selectioin to emtpy list
	private void addCountriesToSelection(Country[] countries){
		for(Country c: countries){
			if(!this.selectedCountries.contains(c)) this.selectedCountries.add(c);
		}
	}

	// Add countries to compare to the list
	private void addCountriesToCompare(Country[] countries){
		for(Country c: countries){
			if(!this.comparedCountries.contains(c)) this.comparedCountries.add(c);
		}
	}

	// Methos to change chart in the centre pane
	private void selectGraph(String name){
		if(!DataStorage.isEmpty()){
			if(name.contains("bar")) this.setGraphCenterPane(new BarChartC(DataStorage.getData()));
			else if(name.contains("area")) this.setGraphCenterPane(new AreaChartC(DataStorage.getData()));
			else if(name.contains("pie")) this.setGraphCenterPane(new PieChartC(DataStorage.getData()));
			else this.setGraphCenterPane(new LineChartC(DataStorage.getData()));
		}else{
			if(name.contains("bar")) this.setGraphCenterPane(new BarChartC());
			else if(name.contains("area")) this.setGraphCenterPane(new AreaChartC());
			else if(name.contains("pie")) this.setGraphCenterPane(new PieChartC());
			else this.setGraphCenterPane(new LineChartC());
		}
		String xTitle = "Year";
		if(this.getCurrentMode() == Mode.Compare) xTitle = "Country";
		this.chart.setChartTitle(this.chartTitle, xTitle);
	}

	// Helper method to convert a list of Country Objects to an array
	private Country[] convertCountriesListToArray(){
		List<Country> toConvert = this.countriesListView.getSelectionModel().getSelectedItems();
		Country[] countries = new Country[toConvert.size()];
		for(int i = 0, size = countries.length; i < size; ++i){
			countries[i] = toConvert.get(i);
		}
		return countries;
	}

	// Remove chart from centre pane
	private void removeChart(){
		this.mainPane.setCenter(null);
	}


	// Method to change the state of the data type to be requested
	private void updateCurrentRequestedData(String type){
		switch(type){
			case "GDP":
				this.currentRequestedData = DataType.GDP;
				this.chartTitle = "GDP";
				break;
			case "GDP Capita":
				this.currentRequestedData = DataType.GDPCapita;
				this.chartTitle = "GDP Capita";
				break;
			case "CPI":
				this.currentRequestedData = DataType.CPI;
				this.chartTitle = "CPI";
				break;
			case "Unemployment":
				this.currentRequestedData = DataType.Unemployment;
				this.chartTitle = "Unemployment";
				break;
			case "AccountBalance":
				this.currentRequestedData = DataType.AccountBalance;
				this.chartTitle = "Account Balance";
				break;
			default:
				this.currentRequestedData = DataType.GDP;
				this.chartTitle = "GDP";
				break;
		}
	}

	// Not USE
	private void cleanCacheFolder(File file){
		File[] files = file.listFiles();
		if(files != null){
			for(File f: files){
				System.out.println(f.toString());
				cleanCacheFolder(f);
			}
		}
		file.delete();
	}

	// Generic method to add a separator to a given pane
	private void addSeparator(Pane pane){
		Separator s = new Separator();
		//s.setAlignment(Pos.CENTER_LEFT);
		pane.getChildren().add(s);
	}

	// Clear all data from chart
	private void removeAllFromChart(){
		this.chart.clear();
		DataStorage.clear();
		if(this.getCurrentMode() == Mode.Selection){
			for(Country c: this.selectedCountries){
				c.resetChartIndex();
			}
			this.selectedCountries.clear();
		}else{
			for(Country c: this.comparedCountries){
				c.resetChartIndex();
			}
			this.comparedCountries.clear();
		}
	}

    /*------------------------------- Utilities End ---------------------------------------*/

    /*------------------------------- Right Pane ---------------------------------------*/

	// Right pane to diplay all possible type of chart
	private void setChartSelectionRightPane(){
		this.rightPane = new BorderPane();
		VBox layout = new VBox(5);
		layout.setPadding(new Insets(50,5,5,5));
		layout.getStyleClass().add("right-Pane");
		layout.setPrefWidth(150);
		String imageNames[] = {"area", "bar", "line", "pie"};
		for(int i = 0, size = imageNames.length; i < size; ++i){
			Image image = ResourceManager.getImageResource("graphIcons/"+ imageNames[i] + "_chart.png");
			Button b = new Button(imageNames[i], new ImageView(image));
			b.getStyleClass().add("right-Buttons");
			b.prefWidthProperty().bind(this.rightPane.widthProperty());
			b.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent e){
					Button btn = (Button) e.getSource();
					StatisticWindow.this.selectGraph(btn.getText());
				}
			});
			layout.getChildren().add(b);
		}
		this.rightPane.setTop(layout);
		this.mainPane.setRight(this.rightPane);
		this.setClearCacheButtonRightPane();
	}

	private void setClearCacheButtonRightPane(){
		HBox layout = new HBox();
		this.clearCacheButton = new Button("Clear cache");
		this.clearCacheButton.setId("cache");
		this.clearCacheButton.getStyleClass().add("cache-Button");
		this.clearCacheButton.prefWidthProperty().bind(this.rightPane.widthProperty());
//		layout.getChildren().add(clearCacheButton);
//		this.rightPane.setBottom(layout);
	}

    /*------------------------------- Right Pane End ---------------------------------------*/


    /* Start Selection pane --------------------------------------------------------- */

	private void selectionInfoPane(){
		VBox layout = new VBox();
		Text description = new Text("Select a country from the drop down: ");
		description.getStyleClass().add("desctiption-Text");
		description.setWrappingWidth(300);
		layout.getChildren().add(description);
		StackPane sp = new StackPane(layout);
		this.selectionPane.getChildren().add(sp);
	}

    /*  End Selection pape ---------------------------------------------------------   */


    /* Start Comparion pane --------------------------------------------------------- */

	// Method to display a description text
	private void comparisonInfoPane(){
		VBox layout = new VBox(10);
		//Text description = new Text("Comparison section to compare multiples countries on given year");
		Text description2 = new Text("The most recent data is shown if the selected year in not available");
		//description.setWrappingWidth(300);
		//description.setWrapText(true);
		//description.setPrefWidth(300);
		description2.setWrappingWidth(300);
		//description2.setWrapText(true);
		//description2.setPrefWidth(300);
		layout.getChildren().add(description2);
		StackPane sp = new StackPane(layout);
		this.comparisonPane.getChildren().add(sp);
	}

    /* End Selection pape --------------------------------------------------------- */

	private void setOptionSelectionTopPane(){
		//this.mainPane.setTop(); TODO
	}

	// Method that switches the type of chart to be used
	private void setGraphCenterPane(ChartC chart){
		this.removeChart();

		if(chart == null) this.chart = new LineChartC();
		else this.chart = chart;

		this.mainPane.setCenter(this.chart);
	}


	/**
	 *
	 * Method used to populare the current chart with the countries to compare
	 *
	 * @param data the data to be passed to the chart 
	 * 
	 */
	public void setCountriesToCompareToChart(List<Pair<String, BigDecimal>> data){
		int i = 0;
		this.removeAllFromChart();
		this.currentMode = Mode.Compare;
		System.out.println("data length: " + data.size());
		for(Country c: this.comparedCountries){
			List<Pair<String, BigDecimal>> l = new ArrayList();
			l.add(data.get(i++));
			DataStorage.cacheData(c, l);
			this.chart.setChartTitle(this.chartTitle, "Country");
			this.chart.addCountrySeries(c, l);
		}
	}


	/**
	 * Get the set of the current selected countries
	 *
	 * @return Country[]
	 *
	 */

	public Country[] getCountriesOnGrapth(){
		Country[] countries = new Country[this.selectedCountries.size()];
		countries = this.selectedCountries.toArray(countries);
		return countries;
	}

	/**
	 * Set event handler for this class
	 * @param controller the controller of for the buttons
	 *
	 */

	public void setEventHandler(EventHandler<ActionEvent> controller){
		this.eventHandler = controller;
		this.selectionButton.setOnAction(this.eventHandler);
		this.comparisonButton.setOnAction(this.eventHandler);
		for(RadioButton rb: this.radioButtons){
			rb.setOnAction(this.eventHandler);
		}
		this.clearCacheButton.setOnAction(this.eventHandler);
		//this.setInternalEventHandler();
	}

	// Internal method to toggle modes within the class
	private void setInternalEventHandler(){
		this.selectionButton.addEventHandler(ActionEvent.ACTION, e -> {
			StatisticWindow.this.currentMode = Mode.Selection;
			if(StatisticWindow.this.getCurrentMode() == Mode.Compare) DataStorage.clear();

		});

		this.comparisonButton.addEventHandler(ActionEvent.ACTION, e -> {
			StatisticWindow.this.currentMode = Mode.Compare;
			if(StatisticWindow.this.getCurrentMode() == Mode.Selection) DataStorage.clear();
		});
	}

    /*-------------------------------- Event methods -------------------------------*/

	/**
	 * Method to get the currenty selected country
	 * to be either compared or viewed
	 *
	 * @return Country
	 *
	 */
	public Country[] getSelectedCountries(){
		if(this.getCurrentMode() == Mode.Compare) this.removeAllFromChart();
		this.currentMode = Mode.Selection;
		return this.convertCountriesListToArray();
	}

	/**
	 * Method called by the compare button fire event
	 * that gets all currently selected countries' codes and
	 * year on which they will be compared
	 *
	 * @return Pair a pair containing an array of countries' codes and the year they are compared against
	 *
	 */
	public Pair<String[], String> getCountriesToCompare(){
		if(this.getCurrentMode() == Mode.Selection) this.removeAllFromChart();
		this.currentMode = Mode.Compare;

		String year = this.years.getValue().toString();
		Country[] cCountries =  this.convertCountriesListToArray();
		String[] sCountries = new String[cCountries.length];

		for(int i = 0, size = sCountries.length; i < size; ++i){
			sCountries[i] = cCountries[i].getCode();
		}
		return new Pair<String[], String>(sCountries, year);
	}

	/**
	 * This method sets a series for the current chart in use
	 * @param data the data for the country
	 * @param isUpdate a boolean value to refreash the data
	 *
	 */
	public void setCompareDataToChart(List<Pair<String, BigDecimal>> data, boolean isUpdate){
		this.removeAllFromChart();
		Country[] countries = this.convertCountriesListToArray();
		for(int i = 0, size = countries.length; i < size; ++i){
			List<Pair<String, BigDecimal>> d = new ArrayList<Pair<String, BigDecimal>>(1);
			d.add(data.get(i));
			Country country = this.chart.addCountrySeries(countries[i], d);
			this.comparedCountries.add(country);
			DataStorage.cacheData(country, d);
		}
		this.chart.setChartTitle(this.chartTitle, "Country");
	}

	/**
	 *
	 * Set data to chart after the selection button has been pressed
	 * @param countries an array of countries
	 * @param data the data of each country to be shown
	 * @param isUpdate boolean value to to determine whether the data in the graph should be refreshed
	 *
	 *
	 */
	public void setSelectionDataToChart(Country[] countries, List<List<Pair<String, BigDecimal>>> data, boolean isUpdate){
		if(isUpdate) this.removeAllFromChart();
		for(int i = 0, size = countries.length; i < size; ++i){
			if(this.selectedCountries.contains(countries[i])) continue;
			Country country = this.chart.addCountrySeries(countries[i], data.get(i));
			this.selectedCountries.add(country);
			DataStorage.cacheData(country, data.get(i));
		}
		this.chart.setChartTitle(this.chartTitle, "Year");
	}

	/**
	 * This method removes a set of countries from the selection
	 *
	 * @param countries the arrays of countries
	 * 
	 */

	public void removeCountriesFromSelected(Country[] countries){
		for(Country country: countries){
			int index = country.getChartIndex();
			if(index < 0) return;
			this.chart.removeCountrySeries(index);
			country.resetChartIndex();
			DataStorage.removeCountry(country);
			this.selectedCountries.remove(index);
			for(int i = 0, size = this.selectedCountries.size(); i<size; ++i){
				Country c = this.selectedCountries.get(i);
				if(c.getChartIndex() > index) c.setChartIndex(c.getChartIndex() - 1);
			}
		}
	}

	/**
	 * This method gets which data type is currently selected by the user
	 *@return boolan check if the window is in removal mode
	 *
	 */

	public boolean isRemovalMode(){
		return this.countrySelectionMode.isSelected();
	}

	/**
	 *
	 * Method to get the current type data
	 * that the user has selected
	 * @return DataType the type of data requested
	 */

	public DataType getRequestedData(){
		return this.currentRequestedData;
	}

	/**
	 *
	 * Check whether the window mode is on selection mode or comparison mode
	 *
	 * @return boolean true if it is in comparison mode
	 *
	 */
	public boolean isSelectionMode(){
		return this.getCurrentMode() == Mode.Selection;
	}

    /*-------------------------------- Event methods End -------------------------------*/
}
