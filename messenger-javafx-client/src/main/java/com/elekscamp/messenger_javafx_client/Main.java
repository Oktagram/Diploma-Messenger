package com.elekscamp.messenger_javafx_client;

import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Main instance;
	
	public Main(){ instance = this; }
	
	public static Main getInstance() {
		
		if (instance == null) {
			instance = new Main();
		}
		
		return instance;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		RequestManager.setRequestApi("http://localhost:5000/api");//("http://localhost:55059/api");
		
		Parent root = FXMLLoader.load(getClass().getClassLoader()
				.getResource("fxml/AuthenticationAndRegistration.fxml")); 
	
		Scene scene = new Scene(root);
		
		primaryStage.setMinWidth(520);
		primaryStage.setMinHeight(450);
		primaryStage.setTitle("Messenger");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
