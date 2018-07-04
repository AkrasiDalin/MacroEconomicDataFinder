package app;

import javafx.application.Application;
import javafx.stage.Stage;

import app.view.Window;
import app.controller.Controller;
import app.model.DataFetcher;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
    	}

	@Override
	public void start(Stage primaryStage){
		Window view = new Window();
		Controller controller = new Controller(view);
		view.start();
	}		

}
