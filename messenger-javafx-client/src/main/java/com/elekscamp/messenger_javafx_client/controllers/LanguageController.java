package com.elekscamp.messenger_javafx_client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import translation.EnglishDictionary;
import translation.UkrainianDictionary;

public class LanguageController implements Initializable {
	@FXML
	private ImageView ukrainianImageView;
	@FXML
	private ImageView englishImageView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Rectangle shape = new Rectangle(300, 150);
		shape.setArcWidth(20);
		shape.setArcHeight(20);
		
		ukrainianImageView.setClip(shape);
		
		shape = new Rectangle(300, 150);
		shape.setArcWidth(20);
		shape.setArcHeight(20);
		
		englishImageView.setClip(shape);
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Stage stage = (Stage) englishImageView.getScene().getWindow();
				stage.getIcons().add(new Image("/images/icon.png"));
			}
		});
	}
	
	public void btnUkraineLanguageOnAction() {
		GlobalVariables.language = Language.UKRAINIAN;
		GlobalVariables.languageDictionary = new UkrainianDictionary();
		openAuthenticationWindow();
	}
	
	public void btnEnglishLanguageOnAction() {
		GlobalVariables.language = Language.ENGLISH;
		GlobalVariables.languageDictionary = new EnglishDictionary();
		openAuthenticationWindow();
	}
	
	private void openAuthenticationWindow() {
		
		RequestManager.setRequestApi("http://localhost:5000/api");

		Parent root;
		try {
			if (GlobalVariables.language == Language.ENGLISH) 
				root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AuthenticationWindowEng.fxml"));
			else
				root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AuthenticationWindowUkr.fxml"));
				
			Stage stage = (Stage) englishImageView.getScene().getWindow();
			stage.close();
			
			Scene scene = new Scene(root);

			stage.setMinWidth(520);
			stage.setMinHeight(450);
			stage.setTitle("Messenger");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}