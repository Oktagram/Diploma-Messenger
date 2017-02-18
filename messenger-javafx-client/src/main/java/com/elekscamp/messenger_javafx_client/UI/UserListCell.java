package com.elekscamp.messenger_javafx_client.UI;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.Entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserWithImage;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class UserListCell extends ListCell<User> {

	private Alert alert;
	private HBox mainHBox;
	private ImageView imageView;
	private AnchorPane anchorPane;
	private HBox usernameAndOnlineStatusHBox;
	private VBox addToConversationVBox;
	private Label username;
	private Label onlineStatus;
	private Button btnAddToConversation;
	private ContentProvider provider;
	private PersonalInfo userInfo;
	private PersonalInfoDialog userInfoDialog;
	private String dialogColor;

	private static int conversationId;
	private static List<User> usersInCurrentConversation;
	private static List<UserWithImage> usersWithImages;

	public UserListCell(ContentProvider provider) {
		dialogColor = "#ffd272";
		this.provider = provider;
		userInfoDialog = new PersonalInfoDialog(dialogColor);
	}

	public static void setCurrentConversationId(int id) {
		UserListCell.conversationId = id;
	}

	public static void setUsersInCurrentConversation(List<User> usersInCurrentConversation) {
		UserListCell.usersInCurrentConversation = usersInCurrentConversation;
	}

	public static void setUsersWithImagesList(List<UserWithImage> list) {
		UserListCell.usersWithImages = list;
	}

	@Override
	protected void updateItem(User item, boolean empty) {

		super.updateItem(item, empty);

		if (item != null) {

			alert = new Alert(AlertType.INFORMATION);
			mainHBox = new HBox();
			imageView = new ImageView();
			anchorPane = new AnchorPane();
			usernameAndOnlineStatusHBox = new HBox();
			addToConversationVBox = new VBox();
			username = new Label(item.getLogin());
			btnAddToConversation = new Button("+");
			onlineStatus = new Label();
			
			boolean isUserOnline = item.getIsOnline();
			
			if (isUserOnline) {
				onlineStatus.setText("[Online]");
				onlineStatus.setStyle("-fx-text-fill: green;");
				
			} else {
				onlineStatus.setText("[Offline]");
				onlineStatus.setStyle("-fx-text-fill: darkred;");
			}
			
			DialogPane dialogPane = alert.getDialogPane();
			
			alert.setTitle("Message");
			alert.setHeaderText(null);
			dialogPane.setStyle("-fx-background-color: " + dialogColor);
			alert.setContentText("User is already in this conversation!");
			
			Stage stage = (Stage) dialogPane.getScene().getWindow();
			stage.getIcons().add(new Image("/images/icon.png"));

			imageView = new ImageView(searchImageInUsersWithImageListByUserId(item.getId()));
			imageView.setFitHeight(50);
			imageView.setFitWidth(50);
			imageView.setPreserveRatio(true);

			HBox imageHBox = new HBox();
			imageHBox.getChildren().add(imageView);
			imageHBox.setAlignment(Pos.CENTER);
			imageHBox.setMinWidth(50);

			mainHBox.getChildren().addAll(imageHBox, anchorPane);

			anchorPane.getChildren().addAll(usernameAndOnlineStatusHBox, addToConversationVBox);
			HBox.setHgrow(anchorPane, Priority.ALWAYS);

			AnchorPane.setRightAnchor(usernameAndOnlineStatusHBox, 50d);
			AnchorPane.setLeftAnchor(usernameAndOnlineStatusHBox, 0d);
			AnchorPane.setRightAnchor(addToConversationVBox, 0d);

			usernameAndOnlineStatusHBox.setAlignment(Pos.CENTER);
			usernameAndOnlineStatusHBox.setPrefHeight(50);
			usernameAndOnlineStatusHBox.setPrefWidth(150);

			addToConversationVBox.setAlignment(Pos.CENTER_RIGHT);
			addToConversationVBox.setPrefHeight(50);
			addToConversationVBox.setPrefWidth(50);

			usernameAndOnlineStatusHBox.getChildren().addAll(username, onlineStatus);

			username.setPadding(new Insets(10));

			addToConversationVBox.getChildren().add(btnAddToConversation);

			btnAddToConversation.setPrefWidth(40);
			btnAddToConversation.setPrefHeight(40);
			btnAddToConversation.setPadding(new Insets(-12, -11, -10, -10));
			btnAddToConversation.setStyle("-fx-base: #63A388; -fx-text-fill: white; -fx-font-size: 25;");
			btnAddToConversation.setTextAlignment(TextAlignment.CENTER);
			btnAddToConversation.setTooltip(new Tooltip("Add to current conversation"));
			btnAddToConversation.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (conversationId == 0) {
						alert.setContentText("Choose conversation.");
						alert.showAndWait();
						return;
					}

					if (searchUserInListById(item.getId()) == null) {
						try {
							provider.getUserConversationProvider().addUser(conversationId, item.getId());
							usersInCurrentConversation = provider.getUserProvider().getByConversationId(conversationId);
							alert.setContentText("User successfully aded to conversation.");
						} catch (HttpErrorCodeException | IOException e) {
							e.printStackTrace();
						}
					}
					alert.showAndWait();
				}
			});

			imageView.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {

					try {
						userInfoDialog = new PersonalInfoDialog(dialogColor);
						userInfo = provider.getPersonalInfoProvider().getById(item.getId());
						userInfoDialog.show(item, userInfo, false);
					} catch (HttpErrorCodeException | IOException e) {
						e.printStackTrace();
					}
				}
			});

			setGraphic(mainHBox);
		} else {
			setGraphic(null);
		}
	}

	private User searchUserInListById(int id) {

		for (User user : usersInCurrentConversation) {
			if (user.getId() == id)
				return user;
		}

		return null;
	}

	private Image searchImageInUsersWithImageListByUserId(int id) {

		for (UserWithImage userWithImg : usersWithImages) {
			if (userWithImg.getUser().getId() == id)
				return userWithImg.getImage();
		}

		return null;
	}
}