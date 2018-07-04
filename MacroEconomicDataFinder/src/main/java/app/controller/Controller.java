package app.controller;
import app.model.DataFetcher;
import app.utils.Pair;
import app.view.Country;
import app.view.Window;
import app.utils.DataType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.lang.Enum;

/**
 *
 *         This class is used to transfer data from
 *         the model class to the view.
 */

public class Controller implements EventHandler<Event>{

    private Window window;
    private EventMouse eventMouse;
    private EventAction eventAction;

    public Controller(Window win){
        window = win;
        eventAction = new EventAction();
        eventMouse = new EventMouse();
        window.setEventHandler(this);
        DataFetcher.fetchAllData();
    }

    private void showWarningDialogWindow(String title, String header, String content){
	Alert alert = new Alert(AlertType.WARNING);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	alert.showAndWait();
    }


    private void showErrorDialogWindow(String title, String header, String content){
	Alert alert = new Alert(AlertType.ERROR);
	alert.setHeaderText(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	alert.showAndWait();
    }

    @Override
    public void handle(Event event) {}


    public EventHandler<MouseEvent> getMouseHandler(){
        return eventMouse;
    }

    public EventHandler<ActionEvent> getActionHandler(){
        return eventAction;
    }

    // Class used to handle any event that supports Mouse Event
    private class EventMouse implements  EventHandler<MouseEvent>{
        public EventMouse(){}

        @Override
        public void handle(MouseEvent event) {
            try {
                window.updateOverview(DataFetcher.getOverview(window.getSelectedCountryCode()));
            }catch (IOException e){}
        }
    }

    // Class used to handle any event that supports Action Event
    private class EventAction implements  EventHandler<ActionEvent>{

	/**
	*
	* Constructor to initialize the EventAction class to an object
	*
	*
	*/
        public EventAction(){}

        @Override
        public void handle(ActionEvent event) {
		if(event.getSource() instanceof Button){
			this.handleButtonEvent((Button) event.getSource());
		}else{
			this.handleRadioButton();
		}
        }

	// Medthod that handles all event fired by a button pressed
	private void handleButtonEvent(Button b){
		if(b.getId().equals("selection")){
			this.handleSelectionEvent();
		}else if(b.getId().equals("comparison")){
			this.handleComparisonEvent();
		}else{
			this.handleOverviewEvent(b);
		}
	}

	// Method called when the overview buttons controls are clicked
	private void handleOverviewEvent(Button b){
		List<Pair<String, BigDecimal>> data = getData(DataType.valueOf(b.getId()), window.getSelectedCountryCode());
		if(data == null){
			showWarningDialogWindow("Warning",
				"Something went wrong when processing your request",
				"Please check that you have Internet connection or that a country is selected");
			return;
		}
		window.displayOverviewChart(b.getText(), data);
	}


	// Method called when the selection button is pressed
	private void handleSelectionEvent(){
		Country[] countries = window.getSelectedCountries();
		if(window.isRemovalMode()){
			window.removeCountriesFromSelected(countries);
			return;
		}
		List<List<Pair<String, BigDecimal>>> data = collectData(countries);
		window.setSelectionDataToChart(countries, data, false);
	}

	// Method do update the current selected countries to display the new requested data
	private void handleRadioButton(){
		if(window.isSelectionMode()){
			Country[] countries = window.getSelectedCountries();
			List<List<Pair<String, BigDecimal>>> data = collectData(countries);
			window.setSelectionDataToChart(countries, data, true);
		}else{
			Pair<String[], String> pair = window.getCountriesToCompare();
			List<Pair<String, BigDecimal>> data = getCompareData(window.getRequestedData(),pair);
			window.setCompareDataToChart(data, true);
		}
	}

	// Method to handle the event when the comparison button is pressed
	private void handleComparisonEvent(){
		Pair<String[], String> pair = window.getCountriesToCompare();
		if(pair.getL().length == 0){
			// warning user did not select a country
			showWarningDialogWindow("Warning",
				"Something went wrong when processing your request",
				"Please check that you have Internet connection or that a country is selected");
			return;
		}
		List<Pair<String, BigDecimal>> data = getCompareData(window.getRequestedData(),pair);
		if(data == null){
			// show error data corrupt
			showErrorDialogWindow("Error",
				"The data retrived is corrupted or not present",
				"Please check that you have Internet connection or try to reboot the application");
			return;
		}
		window.setCompareDataToChart(data, false);
	}

	// Helper method to get a collection of selection data
	private List<List<Pair<String, BigDecimal>>> collectData(Country[] countries){
		List<List<Pair<String, BigDecimal>>> data = new ArrayList<List<Pair<String, BigDecimal>>>();
		for(Country c : countries){
			data.add(getData(window.getRequestedData(),c.getCode()));
		}
		return data;
	}

	// Get compare data from the model
        private List<Pair<String, BigDecimal >> getCompareData(DataType type, Pair<String[], String> pair) {
            List<Pair<String, BigDecimal>> data = null;
            try {
                switch (type) {
                    case AccountBalance:
                        data = DataFetcher.compareAccountBalance(pair.getL(), pair.getR());
                        break;
                    case Unemployment:
                        data = DataFetcher.compareUnemployment(pair.getL(), pair.getR());
                        break;
                    case CPI:
                        data = DataFetcher.compareCPI(pair.getL(), pair.getR());
                        break;
                    case GDPCapita:
                        data = DataFetcher.compareGDPcapita(pair.getL(), pair.getR());
                        break;
                    case GDP:
                        data = DataFetcher.compareGDP(pair.getL(), pair.getR());
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {}
            return data;
        }

	// Get series data from a set of countries from the model
        private List<Pair<String, BigDecimal >> getData(DataType type, String country) {
            List<Pair<String, BigDecimal>> data = null;
            try {
                switch (type) {
                    case AccountBalance:
                        data = DataFetcher.getAccountBalance(country);
                        break;
                    case Unemployment:
                        data = DataFetcher.getUnemployment(country);
                        break;
                    case CPI:
                        data = DataFetcher.getCPI(country);
                        break;
                    case GDPCapita:
                        data = DataFetcher.getGDPcapita(country);
                        break;
                    case GDP:
                        data = DataFetcher.getGDP(country);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {}
            return data;
        }
    }
}
