package com.elekscamp.messenger_javafx_client.ui.handlers;

import java.io.File;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.ui.Colors;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;

public class ChatDialogsHandler {

	public Popup getSmilesPopup(TextArea textAreaMessage, ChatButtonsHandler buttonsHandler) {
		double smileSize = 30;
		int countOfSmiles = 5;
		int borderWidth = 4;
		
		HBox smilesHBox = new HBox();
		
		smilesHBox.setPrefSize(smileSize * countOfSmiles + borderWidth, smileSize + borderWidth);
		smilesHBox.setAlignment(Pos.CENTER);
		smilesHBox.setStyle("-fx-background-color: " + Colors.SMILES_POPUP_BORDER);
		
		Popup popup =  new Popup();
		
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		popup.setAutoFix(true);
		popup.getContent().add(smilesHBox);
		
		Button btnFirstSmile = buttonsHandler.getSmileButton(textAreaMessage, popup, "images/friendly-smile.jpg", smileSize, "=)");
		Button btnSecondSmile = buttonsHandler.getSmileButton(textAreaMessage, popup, "images/sad-smile.jpg", smileSize, "=(");
		Button btnThirdSmile = buttonsHandler.getSmileButton(textAreaMessage, popup, "images/happy-smile.jpg", smileSize, "=D");
		Button btnFourthSmile = buttonsHandler.getSmileButton(textAreaMessage, popup, "images/very-sad-smile.jpg", smileSize, "='(");
		Button btnFifthSmile = buttonsHandler.getSmileButton(textAreaMessage, popup, "images/angry-smile.jpg", smileSize, "X-(");
		
		smilesHBox.getChildren().addAll(btnFirstSmile, btnSecondSmile, btnThirdSmile, btnFourthSmile, btnFifthSmile);
		
		return popup;
	}
	
	public TextInputDialog getTextInputDialogForNewAnnouncement() {
		TextInputDialog textInputDialog = new TextInputDialog("");
		
		textInputDialog.setTitle(GlobalVariables.languageDictionary.getNewAnnouncement());
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText(GlobalVariables.languageDictionary.getAnnouncementDescription());
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + Colors.DIALOG_BACKGROUND);		
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
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + Colors.DIALOG_BACKGROUND);

		Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		return textInputDialog;
	}
	
	public Alert getAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(GlobalVariables.languageDictionary.getMessage());
		alert.setHeaderText(null);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: " + Colors.DIALOG_BACKGROUND);		
		
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