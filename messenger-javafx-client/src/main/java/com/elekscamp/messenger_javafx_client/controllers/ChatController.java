package com.elekscamp.messenger_javafx_client.controllers;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.stream.Collectors;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.Services.Computer;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;
import com.elekscamp.messenger_javafx_client.entities.Announcement;
import com.elekscamp.messenger_javafx_client.entities.Conversation;
import com.elekscamp.messenger_javafx_client.entities.Message;
import com.elekscamp.messenger_javafx_client.entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.entities.UserWithImage;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;
import com.elekscamp.messenger_javafx_client.ui.Colors;
import com.elekscamp.messenger_javafx_client.ui.Chat.ChatButtonsHandler;
import com.elekscamp.messenger_javafx_client.ui.Chat.ChatDialogsHandler;
import com.elekscamp.messenger_javafx_client.ui.Chat.ChatLabelsHandler;
import com.elekscamp.messenger_javafx_client.ui.Chat.ChatListViewHandler;
import com.elekscamp.messenger_javafx_client.ui.Chat.ChatObjectsHandler;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.MessageListCell;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.UserListCell;
import com.elekscamp.messenger_javafx_client.ui.PersonalInfo.PersonalInfoDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.util.Pair;

public class ChatController implements Initializable {

	@FXML private Text txtCurrentUsername;
	@FXML private Button btnFriends;
	@FXML private Button btnSend;
	@FXML private Button btnInChat;
	@FXML private Button btnFind;
	@FXML private Button btnAllUsers;
	@FXML private TextArea txtAreaMessage;
	@FXML private TextField tfUsersSearch;
	@FXML private ListView<Message> listViewChat;
	@FXML private ListView<User> listViewUsers;
	@FXML private ListView<Conversation> listViewConversations;
	@FXML private ImageView imageViewCurrentUser;
	@FXML private Button btnSmiles;
	@FXML private VBox middleVBox;
	@FXML private TabPane announcementsTabPane;
	@FXML private ListView<Announcement> listViewActiveAnnouncements;
	@FXML private ListView<Announcement> listViewClosedAnnouncements;

	private User currentUser;
	private PersonalInfo personalInfo;
	private ContentProvider provider;
	private Conversation currentConversation;
	private int currentUserId;
	private int currentConversationId;
	private ObservableList<Conversation> conversationsObservableList;
	private List<User> usersList;
	private String picture;
	private List<Button> buttonsForListViewUsers;
	private Alert alert;
	private Timer timer;
	private File attachmentFile;
	private Tooltip attachmentTooltip;
	private AnchorPane attachmentAnchor;
	private Label lbAttachmentName;
	private Popup popup;
	private ObservableList<Announcement> activeAnnouncementsObservableList;
	private ObservableList<Announcement> closedAnnouncementsObservableList;
	private ChatButtonsHandler buttonsHandler;
	private ChatLabelsHandler labelsHandler; 
	private ChatObjectsHandler objectsHandler;
	private ChatDialogsHandler dialogsHandler;
	private HBox smilesHBox;
	private final double smileSize;
	private final int countOfSmiles;
	private Computer computer;
	private ChatListViewHandler listViewHandler;
	
	public ChatController() {
		smileSize = 30;
		countOfSmiles = 5;
		computer = new Computer();
		labelsHandler = new ChatLabelsHandler();
		objectsHandler = new ChatObjectsHandler();
		dialogsHandler = new ChatDialogsHandler();
		attachmentTooltip = new Tooltip();
		provider = new ContentProvider();
		attachmentAnchor = new AnchorPane();
		buttonsHandler = new ChatButtonsHandler();
		listViewHandler = new ChatListViewHandler();
		alert = dialogsHandler.getAlert();
		
		timer = new Timer();
		/*
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() { 
				Platform.runLater(() -> {
					updateMessagesListView(); 
					updateConversationsListView();
				}); 
			} 
		}, 2000, 5000);
		*/
		
		prepareSmilesPopup();
		initializeAttachmentControls();
	}
	
	public void initData(int currentUserId) {

		this.currentUserId = currentUserId;
		
		prepareData();
		prepareButtons();
		initializeListViews();

		btnFriends.fire();

		Platform.runLater(() -> {
				Stage btnSendStage = (Stage) btnSend.getScene().getWindow();
				btnSendStage.getIcons().add(new Image("/images/icon.png"));
				
				txtAreaMessage.requestFocus();
		});
	}

	public void btnSmilesOnAction() {
		Bounds btnSmilesBounds = btnSmiles.getBoundsInLocal();
		Bounds localToSceneBounds = btnSmiles.localToScene(btnSmiles.getBoundsInLocal());
		Scene btnSmilesScene = btnSmiles.getScene();

		double posX = computer.computePosXForSmilesPopup(btnSmilesBounds, localToSceneBounds, btnSmilesScene, btnSmiles, smilesHBox);
		double posY = computer.computerPosYForSmilesPopup(btnSmilesBounds, localToSceneBounds, btnSmilesScene, smilesHBox);

		popup.show(btnSmiles, posX, posY);
	}

	private void prepareSmilesPopup() {
		smilesHBox = new HBox();
		
		int borderWidth = 4;
		smilesHBox.setPrefSize(smileSize * countOfSmiles + borderWidth, smileSize + borderWidth);
		smilesHBox.setAlignment(Pos.CENTER);
		smilesHBox.setStyle("-fx-background-color: " + Colors.smilesPopupBorder);

		Button btnFirstSmile = buttonsHandler.getSmileButton("images/friendly-smile.jpg", 30, () -> addSmileToMessage("=)"));
		Button btnSecondSmile = buttonsHandler.getSmileButton("images/sad-smile.jpg", 30, () -> addSmileToMessage("=("));
		Button btnThirdSmile = buttonsHandler.getSmileButton("images/happy-smile.jpg", 30, () -> addSmileToMessage("=D"));
		Button btnFourthSmile = buttonsHandler.getSmileButton("images/very-sad-smile.jpg", 30, () -> addSmileToMessage("='("));
		Button btnFifthSmile = buttonsHandler.getSmileButton("images/angry-smile.jpg", 30, () -> addSmileToMessage("X-("));

		smilesHBox.getChildren().addAll(btnFirstSmile, btnSecondSmile, btnThirdSmile, btnFourthSmile, btnFifthSmile);

		popup = dialogsHandler.getSmilesPopup(smilesHBox);
	}
	
	private void prepareData() {
		try {
			this.currentUser = provider.getUserProvider().getById(currentUserId);
			txtCurrentUsername.setText(currentUser.getLogin());
			personalInfo = provider.getPersonalInfoProvider().getById(currentUserId);
			picture = personalInfo.getPicture();

			updateCurrentUserPicture();

			List<Conversation> conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
			List<Announcement> announcementsList = provider.getAnnouncementProvider().getAll();
			List<Announcement> activeAnnouncementsList = getActiveAnnouncements(announcementsList);
			List<Announcement> closedAnnouncemetsList = getClosedAnnouncements(announcementsList);
			
			fillConversationsListView(conversationsList);
			fillActiveAnnouncementListView(activeAnnouncementsList);
			fillClosedAnnouncementListView(closedAnnouncemetsList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private List<Announcement> getClosedAnnouncements(List<Announcement> announcementsList) {
		List<Announcement> result = new ArrayList<>();
		
		for (Announcement item : announcementsList) 
			if (!item.getIsActive()) result.add(item);
		
		return result;
	}
	
	private List<Announcement> getActiveAnnouncements(List<Announcement> announcementsList) {
		List<Announcement> result = new ArrayList<>();
		
		for (Announcement item : announcementsList) 
			if (item.getIsActive()) result.add(item);
		
		return result;
	}
	
	private void initializeAttachmentControls() {
		Button btnRemoveAttachment = buttonsHandler.getRemoveAttachmentButton(() -> removeAttachment());
		lbAttachmentName = labelsHandler.getAttachmentNameLabel(attachmentTooltip);

		attachmentAnchor.getChildren().addAll(lbAttachmentName, btnRemoveAttachment);
	}

	private void addSmileToMessage(String smile) {
		txtAreaMessage.appendText(" " + smile + " ");
		popup.hide();
		txtAreaMessage.requestFocus();
	}

	public void btnAttachmentOnAction() {
		
		FileChooser fileChooser = dialogsHandler.getAttachmentFileChooser();
		try {
			attachmentFile = fileChooser.showOpenDialog(null);
		} catch (IllegalArgumentException ex) {
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			attachmentFile = fileChooser.showOpenDialog(null);
		}

		if (attachmentFile != null) {
			lbAttachmentName.setText(attachmentFile.getName());
			attachmentTooltip.setText(attachmentFile.getName());

			middleVBox.getChildren().remove(attachmentAnchor);
			middleVBox.getChildren().add(attachmentAnchor);

			VBox.setMargin(attachmentAnchor, new Insets(5));
		}
	}

	public void btnSendOnAction() {

		if (currentConversation == null) {
			alert.setContentText(GlobalVariables.languageDictionary.getChooseConversaion());
			alert.showAndWait();
			return;
		}

		String messageText = txtAreaMessage.getText();
		messageText = messageText.trim();

		if (messageText.isEmpty() && attachmentFile == null) {
			alert.setContentText(GlobalVariables.languageDictionary.getEmptyMessage());
			alert.showAndWait();
			return;
		}

		Message newMessage = new Message(currentUserId, currentConversationId, messageText);

		try {
			int messageId = provider.getMessageProvider().add(newMessage).getId();

			if (attachmentFile != null) {
				provider.getFileProvider().uploadAttachment(attachmentFile, messageId);

				newMessage = provider.getMessageProvider().getById(messageId);

				removeAttachment();
			}

			listViewChat.getItems().add(newMessage);
			txtAreaMessage.clear();
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void removeAttachment() {
		attachmentFile = null;
		middleVBox.getChildren().remove(attachmentAnchor);
	}

	private void initializeListViews() {
		listViewHandler.customizeChatListView(listViewChat, currentUserId);
		listViewHandler.customizeListViewUsers(listViewUsers, provider);
		listViewHandler.customizeListViewConversations(listViewConversations, provider, currentUserId, conversationsObservableList);
		listViewHandler.customizeListViewActiveAnnouncements(listViewActiveAnnouncements, provider, 
				activeAnnouncementsObservableList, closedAnnouncementsObservableList);
		listViewHandler.customizeListViewClosedAnnouncements(listViewClosedAnnouncements, provider, currentUserId, 
				activeAnnouncementsObservableList, closedAnnouncementsObservableList);
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
		
		FXMLLoader loader;
		if (GlobalVariables.language == Language.ENGLISH)
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AuthenticationWindowEng.fxml"));
		else
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AuthenticationWindowUkr.fxml"));
			
		try {
			currentUser.setIsOnline(false);
			provider.getUserProvider().update(currentUser.getId(), currentUser);

			Parent root = loader.load();

			Stage stage = (Stage) btnSend.getScene().getWindow();
			stage.close();

			Scene scene = new Scene(root);

			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Messenger");
			stage.setResizable(false);
			stage.setMinWidth(520);
			stage.setMinHeight(450);

			stage.show();
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	public void btnNewConversationOnAction() {

		TextInputDialog textInputDialog = dialogsHandler.getTextInputDialogForNewConversation(); 
		
		Optional<String> dialogResult = textInputDialog.showAndWait();
		dialogResult.ifPresent(conversationName -> {
			if (!conversationName.isEmpty()) createNewConversation(dialogResult.get());
			else {
				alert.setContentText(GlobalVariables.languageDictionary.getNameOfTheConversationCannotBeEmpty());
				alert.showAndWait();
			}
		});
	}

	public void imageViewCurrentUserOnMouseClicked() {

		PersonalInfoDialog personalInfoDialog = new PersonalInfoDialog(Colors.dialogBackground);
		try {
			personalInfo = provider.getPersonalInfoProvider().getById(currentUserId);

			Optional<Pair<User, PersonalInfo>> result = personalInfoDialog.show(currentUser, personalInfo, true);

			result.ifPresent(pair -> {
				try {
					currentUser = pair.getKey();
					provider.getUserProvider().update(currentUserId, currentUser);
					
					PersonalInfo personalInfo = pair.getValue();
					picture = personalInfo.getPicture();
					provider.getPersonalInfoProvider().update(currentUserId, personalInfo);
					
					Image profileImage;
					if (picture == null || picture.isEmpty())
						profileImage = new Image("images/default_user_image.png");
					else
						profileImage = new Image(RequestManager.getRequestApi() + "/files/downloadpicture/" + personalInfo.getId());
					
					imageViewCurrentUser.setImage(profileImage);
				} catch (HttpErrorCodeException | IOException e) {
					e.printStackTrace();
				}
			});
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void createNewConversation(String conversationName) {

		Conversation newConversation = new Conversation(conversationName);

		try {
			newConversation = provider.getConversationProvider().add(newConversation);
			provider.getUserConversationProvider().addUser(newConversation.getId(), currentUserId);
			listViewConversations.getItems().add(0, newConversation);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void updateMessagesListView() {

		List<Message> messagesList = null;
		try {
			messagesList = provider.getMessageProvider().getByConversationId(currentConversationId);
			
			int countOfItems = listViewChat.getItems().size();
			
			if (countOfItems != messagesList.size()) {
			
				Collections.reverse(messagesList);
		
				ObservableList<Message> messagesObservableList = FXCollections.observableArrayList();
				messagesObservableList.setAll(messagesList);
		
				listViewChat.setItems(messagesObservableList);
				
				countOfItems = listViewChat.getItems().size();
				listViewChat.scrollTo(countOfItems);
			}
		
			if (countOfItems == 0)
				listViewChat.setPlaceholder(new Text(GlobalVariables.languageDictionary.getSendFirstMessage()));
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void updateConversationsListView() {

		try {
			List<Conversation> conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
			
			if (conversationsList.size() != listViewConversations.getItems().size())
				fillConversationsListView(conversationsList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void updateCurrentUserPicture() {

		Image profileImage;
		
		if (picture == null || picture.isEmpty())
			profileImage = new Image("images/default_user_image.png");
		else
			profileImage = new Image(
					RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(currentUserId));

		imageViewCurrentUser.setImage(profileImage);
	}

	private void updateAnnouncementsListViews() {
		
		List<Announcement> announcementsList = null;
		
		try {
			announcementsList = provider.getAnnouncementProvider().getAll();
			
			List<Announcement> activeAnnouncementsList = getActiveAnnouncements(announcementsList);
			List<Announcement> closedAnnouncemetsList = getClosedAnnouncements(announcementsList);
			
			if (activeAnnouncementsList.size() != listViewActiveAnnouncements.getItems().size())
				fillActiveAnnouncementListView(activeAnnouncementsList);
			if (closedAnnouncemetsList.size() != listViewClosedAnnouncements.getItems().size())
				fillClosedAnnouncementListView(closedAnnouncemetsList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void btnUpdateOnAction() {
		updateMessagesListView();
		updateConversationsListView();
		updateCurrentUserPicture();
		updateAnnouncementsListViews();
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
				
				if (personalInfo.getPicture() == null || personalInfo.getPicture().isEmpty())
					image = new Image("images/default_user_image.png");
				else
					image = new Image(RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(user.getId()));

				userWithImg.setImage(image);
				result.add(userWithImg);
			} catch (HttpErrorCodeException | IOException e) {
				e.printStackTrace();
			}
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
	}

	public void tfUsersSearchOnKeyPressed(KeyEvent ke) {
		if (doesKeyCodeEqualsEnter(ke.getCode())) {
			btnFind.fire();
			ke.consume();
		}
	}

	public void btnFindOnAction() {
		String searchingStr = tfUsersSearch.getText().toLowerCase();

		fillUsersListView(usersList.stream().filter(item -> item.getLogin().toLowerCase().contains(searchingStr))
				.collect(Collectors.toList()));
	}

	private boolean doesKeyCodeEqualsEnter(KeyCode code) {
		return KeyCode.ENTER == code;
	}

	private void fillUsersListView(List<User> list) {

		list.sort(new Comparator<User>() {
			@Override
			public int compare(User leftUser, User rightUser) {
				boolean left = leftUser.getIsOnline();
				boolean right = rightUser.getIsOnline();
				
				if (left && !right) return -1;
				if (!left && right) return 1;
				
				return 0;
			}
		});
		UserListCell.setUsersWithImagesList(generateUsersWithImagesList(list));
		
		ObservableList<User> usersObservableList = FXCollections.observableArrayList();
		usersObservableList.addAll(list);
		listViewUsers.setItems(usersObservableList);
	}

	private void fillConversationsListView(List<Conversation> list) {
		conversationsObservableList = FXCollections.observableArrayList();
		conversationsObservableList.addAll(list);
		listViewConversations.setItems(conversationsObservableList);
	}

	private void fillActiveAnnouncementListView(List<Announcement> active) {
		activeAnnouncementsObservableList = FXCollections.observableArrayList();
		activeAnnouncementsObservableList.addAll(active);
		listViewActiveAnnouncements.setItems(activeAnnouncementsObservableList);
	}
	
	private void fillClosedAnnouncementListView(List<Announcement> closed) {
		closedAnnouncementsObservableList = FXCollections.observableArrayList();
		closedAnnouncementsObservableList.addAll(closed);
		listViewClosedAnnouncements.setItems(closedAnnouncementsObservableList);
	}
	
	private void changeButtonsColors(Button buttonPicked) {
		for (Button button : buttonsForListViewUsers) {
			if (button.equals(buttonPicked))
				button.setStyle("-fx-base: " + Colors.activeUserTab);
			else
				button.setStyle("-fx-base: " + Colors.passiveUserTab);
		}
	}

	public void btnAddNewAnnouncementOnAction() {
		TextInputDialog textInputDialog = dialogsHandler.getTextInputDialogForNewAnnouncement(); 
				
		Optional<String> dialogResult = textInputDialog.showAndWait();
		dialogResult.ifPresent(conversationName -> {
			if (conversationName.isEmpty()) {
				alert.setContentText(GlobalVariables.languageDictionary.getAnnouncementDescriptionEmpty());
				alert.showAndWait();
			} else {
				createNewAnnouncement(dialogResult.get());
			}
		});
	}
	
	private void createNewAnnouncement(String description) {
		Announcement newAnnouncement = new Announcement(description, currentUserId);
		try {
			newAnnouncement = provider.getAnnouncementProvider().add(newAnnouncement);
			listViewActiveAnnouncements.getItems().add(0, newAnnouncement);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareButtons() {
		buttonsForListViewUsers = new ArrayList<Button>();
		buttonsForListViewUsers.add(btnInChat);
		buttonsForListViewUsers.add(btnAllUsers);
		buttonsForListViewUsers.add(btnFriends);

		for (Button button : buttonsForListViewUsers) {
			button.setStyle("-fx-base: " + Colors.passiveUserTab);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtAreaMessage.setTextFormatter(new TextFormatter<String>(change -> 
			change.getControlNewText().length() <= 200 ? change : null));
		objectsHandler.customizeAnnouncementsTabPane(announcementsTabPane);
	}
}