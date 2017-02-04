package com.elekscamp.messenger_javafx_client.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import com.elekscamp.messenger_javafx_client.Entities.Conversation;
import com.elekscamp.messenger_javafx_client.Entities.Message;
import com.elekscamp.messenger_javafx_client.Entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserWithImage;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;
import com.elekscamp.messenger_javafx_client.UI.ConversationListCell;
import com.elekscamp.messenger_javafx_client.UI.MessageListCell;
import com.elekscamp.messenger_javafx_client.UI.PersonalInfoDialog;
import com.elekscamp.messenger_javafx_client.UI.UserListCell;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

public class ChatController {
	
	private User currentUser;
	private PersonalInfo personalInfo;
	private ContentProvider provider; 
	private Conversation currentConversation;
	private int currentUserId;
	private int currentConversationId;
	private String messageText;
	private Message newMessage;
	private Conversation newConversation;
	private ObservableList<User> usersObservableList;
	private ObservableList<Conversation> conversationsObservableList;
	private ObservableList<Message> messagesObservableList;
	private List<User> usersList;
	private List<Conversation> conversationsList;
	private List<Message> messagesList;
	private String picture;
	private FXMLLoader loader;
	private Parent root;
	private Scene scene;
	private Stage stage;
	private List<Button> buttonsForListViewUsers;
	private Alert alert;
	private TextInputDialog textInputDialog;
	private Optional<String> dialogResult;
	private String dialogColor;
	private String findAndSearchColor;
	private String newConversationColor;
	private String logOutColor;
	private String passiveButtonsColor;
	private String activeButtonColor;
	private PersonalInfoDialog personalInfoDialog;
	private Timer timer;
	private Image profileImage;
	
	@FXML
	private Text txtCurrentUsername;
	@FXML
	private Button btnLogOut;
	@FXML
	private Button btnNewConversation;
	@FXML
	private Button btnFriends;
	@FXML
	private Button btnSend;
	@FXML
	private Button btnInChat;
	@FXML
	private Button btnFind;
	@FXML
	private Button btnAllUsers;
	@FXML
	private TextArea txtAreaMessage;
	@FXML
	private TextField tfUsersSearch;
	@FXML
	private ListView<Message> listViewChat;
	@FXML
	private ListView<User> listViewUsers;
	@FXML
	private ListView<Conversation> listViewConversations;
	@FXML
	private ImageView imageViewCurrentUser;
	@FXML
	private Button btnUpdate;
	
	public void initData(int currentUserId) {
		
		this.currentUserId = currentUserId;
		provider = new ContentProvider();
		
		try {
			this.currentUser = provider.getUserProvider().getById(currentUserId);
			txtCurrentUsername.setText(currentUser.getLogin());
			personalInfo = provider.getPersonalInfoProvider().getById(currentUserId);
			picture = personalInfo.getPicture();
			
			updateCurrentUserPicture();
			
			conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
			fillConversationsListView(conversationsList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
		
		timer = new Timer();
	/*	timer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            Platform.runLater(() -> {
					updateMessagesListView();
	            });
	        }
	    }, 2000, 5000);*/
		
		initializeListViews();
		initializeColors();
		prepareButtons();
		btnFriends.fire();
	}

	private void initializeColors() {
		dialogColor = "#ffd272";
		findAndSearchColor = "#fcc05f";
		newConversationColor = "#10385E";
		logOutColor = "#994646";
		passiveButtonsColor = "#435F7A";
		activeButtonColor = "#FFAC66";
	}
	
	private void initializeListViews() {
		
		listViewChat.setPlaceholder(new Text("Choose conversation!"));
		
		listViewChat.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {

            @Override
            public ListCell<Message> call(ListView<Message> param) {
                
            	MessageListCell listCell = new MessageListCell();
            	
            	listCell.prefWidthProperty().bind(listViewChat.widthProperty().subtract(50));
            	listCell.setCurrentUserId(currentUserId);
            	
            	return listCell;
            }
        });
		
		listViewUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {

			@Override
			public ListCell<User> call(ListView<User> param) {
				
				UserListCell listCell = new UserListCell(provider);
            	
            	listCell.prefWidthProperty().bind(listViewUsers.widthProperty().subtract(50));
            	
            	return listCell;
			}
			
		});
		
		listViewConversations.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
			@Override
			public ListCell<Conversation> call(ListView<Conversation> param) {
				
				ConversationListCell listCell = new ConversationListCell(provider);
            	
            	listCell.prefWidthProperty().bind(listViewUsers.widthProperty().subtract(50));
            	listCell.initData(currentUserId, conversationsObservableList);
            	
            	return listCell;
			}
		});
	}
	
	public void listViewConversationsOnMouseClicked() {
		
		currentConversation = listViewConversations.getSelectionModel().getSelectedItem();
		
		if (currentConversation == null) return;
		
		currentConversationId = currentConversation.getId();
		UserListCell.setCurrentConversationId(currentConversationId);
		
		btnInChat.fire();
		
		updateMessagesListView();
	}
	
	public void btnLogOutOnAction() {

		timer.cancel();
		loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AuthenticationAndRegistration.fxml"));
		
		try {
			currentUser.setIsOnline(false);
			provider.getUserProvider().update(currentUser.getId(), currentUser);
			
			root = loader.load();
			
			stage = (Stage) btnLogOut.getScene().getWindow();
		    stage.close();
			
			scene = new Scene(root);
			
			stage = new Stage();
			stage.setScene(scene);
		    stage.setTitle("Messenger");

	        stage.setMinWidth(520);
	        stage.setMinHeight(450);

	        stage.show();
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void btnNewConversationOnAction() {
	
		textInputDialog = new TextInputDialog("");
		textInputDialog.setTitle("New conversation");
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText("Name of the conversation:");
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + dialogColor);
		
		dialogResult = textInputDialog.showAndWait();
		dialogResult.ifPresent(conversationName -> {
			if (conversationName.isEmpty()) {
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Message");
				alert.setHeaderText(null);
				alert.getDialogPane().setStyle("-fx-background-color: " + dialogColor);
				alert.setContentText("Name of the conversation cannot be empty!");
				alert.showAndWait();
			} else 		
				createNewConversation(dialogResult.get());
		});
	}
	
	public void imageViewCurrentUserOnMouseClicked() {

		personalInfoDialog = new PersonalInfoDialog(dialogColor);
		
		try {
			personalInfo = provider.getPersonalInfoProvider().getById(currentUserId);
			
			Optional<Pair<User, PersonalInfo>> result = personalInfoDialog.show(currentUser, personalInfo, true);
			
			result.ifPresent(pair -> {
			    try {
					provider.getUserProvider().update(currentUserId, pair.getKey());
					currentUser = pair.getKey();
					picture = pair.getValue().getPicture();
					provider.getPersonalInfoProvider().update(currentUserId, pair.getValue());
				} catch (HttpErrorCodeException | IOException e) {
					e.printStackTrace();
				}
			});
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createNewConversation(String conversationName) {
		
		newConversation = new Conversation(conversationName);
		
		try {
			newConversation = provider.getConversationProvider().add(newConversation);
			provider.getUserConversationProvider().addUser(newConversation.getId(), currentUserId);
			listViewConversations.getItems().add(0, newConversation);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	public void btnSendOnAction() {
		
		messageText = txtAreaMessage.getText();
		
		if (currentConversation == null || messageText.isEmpty()) return;
		
		newMessage = new Message(currentUserId, currentConversationId, messageText);
		
		try {
			provider.getMessageProvider().add(newMessage);
			listViewChat.getItems().add(newMessage);
			txtAreaMessage.clear();
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateMessagesListView() {

		try {
			messagesList = provider.getMessageProvider().getByConversationId(currentConversationId);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
		
		Collections.reverse(messagesList);
		
		messagesObservableList = FXCollections.observableArrayList();
		messagesObservableList.setAll(messagesList);
		
		listViewChat.setItems(messagesObservableList);

		int countOfItems = listViewChat.getItems().size();
		
		if (countOfItems == 0) listViewChat.setPlaceholder(new Text("Send first message in this conversation!"));
		
		listViewChat.scrollTo(countOfItems);
	}
	
	private void updateConversationsListView() {
		
		try {
			conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
		fillConversationsListView(conversationsList);
	}
	
	private void updateCurrentUserPicture() {
		
		if (picture == null || picture.isEmpty()) 
			profileImage = new Image("images/default_user_image.png");
		else
			profileImage = new Image(RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(currentUserId));
		
		imageViewCurrentUser.setImage(profileImage);
	}
	
	public void btnUpdateOnAction() {

		updateMessagesListView();
		updateConversationsListView();
		updateCurrentUserPicture();
	}
	
	public void txtAreaMessageOnKeyPressed(KeyEvent ke) {
		
		if (doesKeyCodeEqualsEnter(ke.getCode())) {
			btnSend.fire();
			ke.consume();
		}
	}
	
	private List<UserWithImage> generateUsersWithImagesList(List<User> users) {
		
		List<UserWithImage> result = new ArrayList<UserWithImage>();
		UserWithImage userWithImg;
		Image image;
		PersonalInfo personalInfo = null;
		
		for (User user : users) {
			
			userWithImg = new UserWithImage();
			userWithImg.setUser(user);
			
			try {
				personalInfo = provider.getPersonalInfoProvider().getById(user.getId());
			} catch (HttpErrorCodeException | IOException e) {
				e.printStackTrace();
			}
			
			if (personalInfo.getPicture() == null || personalInfo.getPicture().isEmpty())
				image = new Image("images/default_user_image.png");
			else
				image = new Image(RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(user.getId()));
			
			userWithImg.setImage(image);
			
			result.add(userWithImg);
		}
		
		return result;
	}
	
	public void btnInChatOnAction() {
		
		changeButtonsColors(btnInChat);
		
		try {
			usersList = provider.getUserProvider().getByConversationId(currentConversationId);
			MessageListCell.setUsersList(generateUsersWithImagesList(usersList));
			UserListCell.setUsersInCurrentConversation(usersList);
			fillUsersListView(usersList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void btnFriendsOnAction() {
		
		changeButtonsColors(btnFriends);
		
		try {
			usersList = provider.getUserProvider().getFriendsById(currentUserId);
			fillUsersListView(usersList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void btnAllUsersOnAction() {
		
		changeButtonsColors(btnAllUsers);
		
		try {
			usersList = provider.getUserProvider().getAll();
			fillUsersListView(usersList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("-------------------------------------------------------------------------");
	}
	
	public void tfUsersSearchOnKeyPressed(KeyEvent ke) {
		
		if (doesKeyCodeEqualsEnter(ke.getCode())) {
			btnFind.fire();
			ke.consume();
		}
	}

	public void btnFindOnAction() {
		
		String searchingStr = tfUsersSearch.getText().toLowerCase();
	
		fillUsersListView(usersList.stream()
				.filter(item -> item.getLogin().toLowerCase().contains(searchingStr))
				.collect(Collectors.toList()));
	}
	
	private boolean doesKeyCodeEqualsEnter(KeyCode code) {
		return KeyCode.ENTER == code;
	}
	
	private void fillUsersListView(List<User> list) {
		
		usersObservableList = FXCollections.observableArrayList();
		usersObservableList.addAll(list);
		listViewUsers.setItems(usersObservableList);
	}
	
	private void fillConversationsListView(List<Conversation> list) {
		
		conversationsObservableList = FXCollections.observableArrayList();
		conversationsObservableList.addAll(list);
		listViewConversations.setItems(conversationsObservableList);
	}
	
	private void changeButtonsColors(Button buttonPicked) {
		
		for (Button button : buttonsForListViewUsers) {
			if (button.equals(buttonPicked)) button.setStyle("-fx-base: " + activeButtonColor);
			else button.setStyle("-fx-base: " + passiveButtonsColor);
		}
	}
	
	private void prepareButtons() {
		
		buttonsForListViewUsers = new ArrayList<Button>();
		
		buttonsForListViewUsers.add(btnInChat);
		buttonsForListViewUsers.add(btnAllUsers);
		buttonsForListViewUsers.add(btnFriends);
		
		for (Button button : buttonsForListViewUsers) {
			button.setStyle("-fx-base: " + passiveButtonsColor);
		}
		
		btnUpdate.setStyle("-fx-font-size: 11.5;" + "-fx-base: " + newConversationColor);
		btnLogOut.setStyle("-fx-base: " + logOutColor);
		btnNewConversation.setStyle("-fx-base: " + newConversationColor);
		btnSend.setStyle("-fx-base: " + findAndSearchColor);
		btnFind.setStyle("-fx-base: " + findAndSearchColor);
	} 
}