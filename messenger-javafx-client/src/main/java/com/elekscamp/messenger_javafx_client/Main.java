package com.elekscamp.messenger_javafx_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Main instance;

	public Main() {
		instance = this;
	}

	public static Main getInstance() {

		if (instance == null) {
			instance = new Main();
		}

		return instance;
	}

	@Override public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/LanguageWindow.fxml"));
		
		Scene scene = new Scene(root);
		
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(300);
		primaryStage.setTitle("Language");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
