package com.elekscamp.messenger_javafx_client.ui.Chat;

import java.io.File;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.ui.Colors;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;

public class ChatDialogsHandler {

	public Popup getSmilesPopup(HBox smilesHBox) {
		Popup popup =  new Popup();
		
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		popup.setAutoFix(true);
		popup.getContent().add(smilesHBox);
		
		return popup;
	}
	
	public TextInputDialog getTextInputDialogForNewAnnouncement() {
		TextInputDialog textInputDialog = new TextInputDialog("");
		
		textInputDialog.setTitle(GlobalVariables.languageDictionary.getNewAnnouncement());
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText(GlobalVariables.languageDictionary.getAnnouncementDescription());
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + Colors.dialogBackground);		
		textInputDialog.getDialogPane().setPrefWidth(600);
		textInputDialog.setResizable(true);
		
		Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		return textInputDialog;
	}
	
	public TextInputDialog getTextInputDialogForNewConversation() {
		TextInputDialog textInputDialog = new TextInputDialog("");
		
		textInputDialog.setTitle(GlobalVariables.languageDictionary.getNewConversation());
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText(GlobalVariables.languageDictionary.getNameOfTheConversation());
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + Colors.dialogBackground);

		Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		return textInputDialog;
	}
	
	public Alert getAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(GlobalVariables.languageDictionary.getMessage());
		alert.setHeaderText(null);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: " + Colors.dialogBackground);		
		
		Stage stage = (Stage) dialogPane.getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		return alert;
	}
	
	public FileChooser getAttachmentFileChooser() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("Attachment");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("All", "*.jpg", "*.gif", "*.bmp", "*.png", "*.doc", "*.docx", "*.pdf", "*.txt"),
				new ExtensionFilter("JPG", "*.jpg"), 
				new ExtensionFilter("GIF", "*.gif"),
				new ExtensionFilter("BMP", "*.bmp"), 
				new ExtensionFilter("PNG", "*.png"),
				new ExtensionFilter("DOC", "*.doc", "*.docx"), 
				new ExtensionFilter("PDF", "*.pdf"),
				new ExtensionFilter("TXT", "*.txt"));
		
		return fileChooser;
	}
}
