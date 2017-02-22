package com.elekscamp.messenger_javafx_client.ui.Chat.ListCells;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.entities.Conversation;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ConversationListCell extends ListCell<Conversation> {

	private int currentUserId;
	private AnchorPane anchorPane;
	private HBox usernameHBox;
	private VBox leaveConversationVBox;
	private Label conversationName;
	private Button btnLeaveConversation;
	private Alert alert;
	private ContentProvider provider;
	private ObservableList<Conversation> conversationsList;

	public ConversationListCell(ContentProvider provider) {
		
		this.provider = provider;
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm action");
		alert.setHeaderText(null);
		
		if (GlobalVariables.language == Language.ENGLISH)
			alert.setContentText("Are you sure you want to leave the conversation?");
		else
			alert.setContentText("Ви впевнені, що хочете покинути бесіду?");

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		if (GlobalVariables.language == Language.ENGLISH)
			btnLeaveConversation = new Button("Leave");
		else {
			btnLeaveConversation = new Button("Вийти");
		}
	}

	public void initData(int id, ObservableList<Conversation> conversationsList) {
		currentUserId = id;
		this.conversationsList = conversationsList;
	}

	@Override
	protected void updateItem(Conversation item, boolean empty) {

		super.updateItem(item, empty);

		if (item != null) {

			anchorPane = new AnchorPane();
			usernameHBox = new HBox();
			leaveConversationVBox = new VBox();
			
			conversationName = new Label(item.getName());
			
			anchorPane.getChildren().addAll(usernameHBox, leaveConversationVBox);

			usernameHBox.setAlignment(Pos.CENTER);
			usernameHBox.getChildren().add(conversationName);
			usernameHBox.setPrefHeight(50);

			VBox.setVgrow(usernameHBox, Priority.ALWAYS);
			HBox.setHgrow(anchorPane, Priority.ALWAYS);
			
			leaveConversationVBox.setAlignment(Pos.CENTER);
			leaveConversationVBox.getChildren().add(btnLeaveConversation);
			leaveConversationVBox.setPrefHeight(50);

			AnchorPane.setRightAnchor(usernameHBox, 65d);
			AnchorPane.setLeftAnchor(usernameHBox, 0d);
			AnchorPane.setRightAnchor(leaveConversationVBox, 0d);

			btnLeaveConversation.setMinWidth(60);
			btnLeaveConversation.setMaxWidth(60);
			btnLeaveConversation.setMaxHeight(30);
			btnLeaveConversation.setMinHeight(30);
			btnLeaveConversation.setStyle("-fx-base: #CC5029; -fx-text-fill: white; -fx-font-size: 13;");
			btnLeaveConversation.setTextAlignment(TextAlignment.CENTER);
			btnLeaveConversation.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (alert.showAndWait().get() == ButtonType.OK) {
						try {
							provider.getUserConversationProvider().removeUser(item.getId(), currentUserId);

							for (Conversation c : conversationsList) {
								if (c.getId() != item.getId())
									continue;
								conversationsList.remove(c);
								break;
							}
						} catch (HttpErrorCodeException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			setGraphic(anchorPane);
		} else {
			setGraphic(null);
		}
	}
}