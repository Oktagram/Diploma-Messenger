package com.elekscamp.messenger_javafx_client.ui.handlers;

import com.elekscamp.messenger_javafx_client.GlobalVariables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;

public class ChatButtonsHandler {

	public Button getRemoveAttachmentButton(Runnable action) {
		Button btnRemoveAttachment = new Button("", new ImageView("/images/remove.png"));
		
		btnRemoveAttachment.setTooltip(new Tooltip(GlobalVariables.languageDictionary.getRemove()));
		btnRemoveAttachment.setMinSize(22, 22);
		btnRemoveAttachment.setPadding(new Insets(-10));
		btnRemoveAttachment.getStylesheets().add(getClass().getResource("/css/buttons/with-image.css").toExternalForm());
		btnRemoveAttachment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				action.run();
			}
		});
		AnchorPane.setRightAnchor(btnRemoveAttachment, 0d);
		
		return btnRemoveAttachment;
	}
	
	public Button getSmileButton(TextArea textAreaMessage, Popup popup, String smileImage, double size, String smileStr) {
		Button smileButton = new Button("", new ImageView(smileImage));
		
		smileButton.setPrefSize(size, size);
		smileButton.setMaxSize(size, size);
		smileButton.setFocusTraversable(false);
		smileButton.setPadding(new Insets(0));
		smileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textAreaMessage.appendText(" " + smileStr + " ");
				popup.hide();
				textAreaMessage.requestFocus();
			}
		});
		
		return smileButton;
	}
}