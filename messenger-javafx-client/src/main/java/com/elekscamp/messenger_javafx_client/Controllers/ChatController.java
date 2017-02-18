package com.elekscamp.messenger_javafx_client.Controllers;

import java.io.File;

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
import com.elekscamp.messenger_javafx_client.Entities.Announcement;
import com.elekscamp.messenger_javafx_client.Entities.Conversation;
import com.elekscamp.messenger_javafx_client.Entities.Message;
import com.elekscamp.messenger_javafx_client.Entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserWithImage;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;
import com.elekscamp.messenger_javafx_client.UI.ActiveAnnouncementListCell;
import com.elekscamp.messenger_javafx_client.UI.ClosedAnnouncementsListCell;
import com.elekscamp.messenger_javafx_client.UI.ConversationListCell;
import com.elekscamp.messenger_javafx_client.UI.MessageListCell;
import com.elekscamp.messenger_javafx_client.UI.PersonalInfoDialog;
import com.elekscamp.messenger_javafx_client.UI.UserListCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;
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
	private File attachmentFile;
	private FileChooser fileChooser;
	private Tooltip attachmentTooltip;
	private AnchorPane attachmentAnchor;
	private Button btnRemoveAttachment;
	private Label lbAttachmentName;
	private Popup popup;
	private ObservableList<Announcement> activeAnnouncementsObservableList;
	private ObservableList<Announcement> closedAnnouncementsObservableList;
	
	@FXML private Text txtCurrentUsername;
	@FXML private Button btnLogOut;
	@FXML private Button btnNewConversation;
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
	@FXML private Button btnUpdate;
	@FXML private Button btnSmiles;
	@FXML private Button btnAttachment;
	@FXML private VBox middleVBox;
	@FXML private TabPane announcementsTabPane;
	@FXML private ListView<Announcement> listViewActiveAnnouncements;
	@FXML private ListView<Announcement> listViewClosedAnnouncements;
	@FXML private Button btnAddNewAnnouncement;
	@FXML private TitledPane announcementTitlePane;
	
	public void initData(int currentUserId) {

		this.currentUserId = currentUserId;
		provider = new ContentProvider();
		attachmentAnchor = new AnchorPane();
		attachmentTooltip = new Tooltip();
		timer = new Timer();
		fileChooser = new FileChooser();
		alert = new Alert(AlertType.INFORMATION);
		
		initializeColors();
		initializeAttachmentControls();
		initializeListViews();
		initializeObjects();
		prepareButtons();
		prepareData();

		btnFriends.fire();

		announcementTitlePane.getStylesheets().add(getClass().getResource("/css/titled-pane.css").toExternalForm());
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				txtAreaMessage.requestFocus();
				
				Stage btnSendStage = (Stage) btnSend.getScene().getWindow();
				btnSendStage.getIcons().add(new Image("/images/icon.png"));
			}
		});
	}

	private void initializeObjects() {
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
		DialogPane dialogPane = alert.getDialogPane();
		alert.setTitle("Message");
		alert.setHeaderText(null);
		dialogPane.setStyle("-fx-background-color: " + dialogColor);		
		
		Stage stage = (Stage) dialogPane.getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		announcementsTabPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> value, Number oldWidth, Number newWidth) {
                Side side = announcementsTabPane.getSide();
                int numTabs = announcementsTabPane.getTabs().size();
                if ((side == Side.BOTTOM || side == Side.TOP) && numTabs != 0) {
                	announcementsTabPane.setTabMinWidth(newWidth.intValue() / numTabs - (22));
                	announcementsTabPane.setTabMaxWidth(newWidth.intValue() / numTabs - (22));
                }
            }
        });

		announcementsTabPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> value, Number oldHeight, Number newHeight) {
                Side side = announcementsTabPane.getSide();
                int numTabs = announcementsTabPane.getTabs().size();
                if ((side == Side.LEFT || side == Side.RIGHT) && numTabs != 0) {
                	announcementsTabPane.setTabMinWidth(newHeight.intValue() / numTabs - (22));
                	announcementsTabPane.setTabMaxWidth(newHeight.intValue() / numTabs - (22));
               }
           }
        });
	}

	private void prepareData() {
		try {
			this.currentUser = provider.getUserProvider().getById(currentUserId);
			txtCurrentUsername.setText(currentUser.getLogin());
			personalInfo = provider.getPersonalInfoProvider().getById(currentUserId);
			picture = personalInfo.getPicture();

			updateCurrentUserPicture();

			conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
			
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
		
		for (Announcement item : announcementsList) {
			if (!item.getIsActive()) result.add(item);
		}
		
		return result;
	}
	
	private List<Announcement> getActiveAnnouncements(List<Announcement> announcementsList) {
		List<Announcement> result = new ArrayList<>();
		
		for (Announcement item : announcementsList) {
			if (item.getIsActive()) result.add(item);
		}
		
		return result;
	}
	
	private void initializeAttachmentControls() {
		
		btnRemoveAttachment = new Button("", new ImageView("/images/remove.png"));
		btnRemoveAttachment.setTooltip(new Tooltip("Remove"));
		btnRemoveAttachment.setMinSize(22, 22);
		btnRemoveAttachment.setPadding(new Insets(-10));
		btnRemoveAttachment.getStylesheets().add(getClass().getResource("/css/buttons/with-image.css").toExternalForm());
		btnRemoveAttachment.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				removeAttachment();
			}
		});

		lbAttachmentName = new Label();
		lbAttachmentName.setTextFill(Color.web("red"));
		lbAttachmentName.setTooltip(attachmentTooltip);

		attachmentAnchor.getChildren().addAll(lbAttachmentName, btnRemoveAttachment);

		AnchorPane.setRightAnchor(btnRemoveAttachment, 0d);
		AnchorPane.setLeftAnchor(lbAttachmentName, 10d);
		AnchorPane.setRightAnchor(lbAttachmentName, 30d);
		
		fileChooser.setTitle("Attachment");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("All", "*.jpg", "*.gif", "*.bmp", "*.png", "*.doc", "*.docx", "*.pdf"),
				new ExtensionFilter("JPG", "*.jpg"), 
				new ExtensionFilter("GIF", "*.gif"),
				new ExtensionFilter("BMP", "*.bmp"), 
				new ExtensionFilter("PNG", "*.png"),
				new ExtensionFilter("DOC", "*.doc", "*.docx"), 
				new ExtensionFilter("PDF", "*.pdf"));
	}

	public void btnSmilesOnAction() {
		
		HBox smilesHBox = new HBox();

		smilesHBox.setPrefSize(30 * 5 + 6, 30 + 6);
		smilesHBox.setAlignment(Pos.CENTER);
		smilesHBox.setStyle("-fx-background-color: grey;");

		Button btnFirstSmile = new Button("", new ImageView("images/friendly-smile.jpg"));
		Button btnSecondSmile = new Button("", new ImageView("images/sad-smile.jpg"));
		Button btnThirdSmile = new Button("", new ImageView("images/happy-smile.jpg"));
		Button btnFourthSmile = new Button("", new ImageView("images/very-sad-smile.jpg"));
		Button btnFifthSmile = new Button("", new ImageView("images/angry-smile.jpg"));

		List<Button> smileButtonsList = new ArrayList<Button>();

		smileButtonsList.add(btnFirstSmile);
		smileButtonsList.add(btnSecondSmile);
		smileButtonsList.add(btnThirdSmile);
		smileButtonsList.add(btnFourthSmile);
		smileButtonsList.add(btnFifthSmile);

		prepareSmileButton(smileButtonsList, 30);

		smilesHBox.getChildren().addAll(smileButtonsList);

		popup = new Popup();
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		popup.setAutoFix(true);

		popup.getContent().add(smilesHBox);

		btnFirstSmile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addSmileToMessage("=)");
			}
		});

		btnSecondSmile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addSmileToMessage("=(");
			}
		});

		btnThirdSmile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addSmileToMessage("=D");
			}
		});

		btnFourthSmile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addSmileToMessage("='(");
			}
		});

		btnFifthSmile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				addSmileToMessage("X-(");
			}
		});

		Bounds btnSmilesBounds = btnSmiles.getBoundsInLocal();
		Bounds bounds = btnSmiles.localToScene(btnSmiles.getBoundsInLocal());
		Scene btnSmilesScene = btnSmiles.getScene();

		double posX = btnSmilesBounds.getMinX() 
				+ bounds.getMinX() 
				+ btnSmilesScene.getX()
				+ btnSmilesScene.getWindow().getX() 
				+ btnSmiles.getBoundsInParent().getWidth()
				- smilesHBox.getPrefWidth();
		double posY = btnSmilesBounds.getMinY() 
				+ bounds.getHeight() 
				+ bounds.getMinY()
				+ btnSmilesScene.getWindow().getY() 
				- smilesHBox.getPrefHeight();

		popup.show(btnSmiles, posX, posY);
	}

	private void addSmileToMessage(String smile) {
		txtAreaMessage.appendText(" " + smile + " ");
		popup.hide();
		txtAreaMessage.requestFocus();
	}

	private void prepareSmileButton(List<Button> buttonsList, double size) {
		
		for (Button button : buttonsList) {
			button.setPrefSize(size, size);
			button.setMaxSize(size, size);
			button.setFocusTraversable(false);
			button.setPadding(new Insets(0));
		}
	}

	public void btnAttachmentOnAction() {
		
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
			alert.setContentText("Choose conversation first!");
			alert.showAndWait();
			return;
		}

		messageText = txtAreaMessage.getText();
		messageText = messageText.trim();

		if (messageText.isEmpty() && attachmentFile == null) {
			alert.setContentText("Cannot send empty message!");
			alert.showAndWait();
			return;
		}

		newMessage = new Message(currentUserId, currentConversationId, messageText);

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
		listViewChat.setCache(true);
		listViewChat.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {

			@Override public ListCell<Message> call(ListView<Message> param) {

				MessageListCell listCell = new MessageListCell();

				listCell.prefWidthProperty().bind(listViewChat.widthProperty().subtract(50));
				listCell.setCurrentUserId(currentUserId);

				return listCell;
			}
		});

		listViewUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {

			@Override public ListCell<User> call(ListView<User> param) {

				UserListCell listCell = new UserListCell(provider);

				listCell.prefWidthProperty().bind(listViewUsers.widthProperty().subtract(50));

				return listCell;
			}

		});

		listViewConversations.setCache(true);
		listViewConversations.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
			
			@Override public ListCell<Conversation> call(ListView<Conversation> param) {

				ConversationListCell listCell = new ConversationListCell(provider);

				listCell.prefWidthProperty().bind(listViewUsers.widthProperty().subtract(50));
				listCell.initData(currentUserId, conversationsObservableList);
				
				return listCell;
			}
		});
		
		listViewActiveAnnouncements.setCellFactory(new Callback<ListView<Announcement>, ListCell<Announcement>>() {
			
			@Override public ListCell<Announcement> call(ListView<Announcement> param) {

				ActiveAnnouncementListCell listCell = new ActiveAnnouncementListCell(provider);

				listCell.prefWidthProperty().bind(listViewActiveAnnouncements.widthProperty().subtract(50));
				listCell.initData(activeAnnouncementsObservableList, closedAnnouncementsObservableList);
				
				return listCell;
			}
		});
		
		listViewClosedAnnouncements.setCellFactory(new Callback<ListView<Announcement>, ListCell<Announcement>>() {
			
			@Override public ListCell<Announcement> call(ListView<Announcement> param) {
				
				ClosedAnnouncementsListCell listCell = new ClosedAnnouncementsListCell(provider);
				
				listCell.prefWidthProperty().bind(listViewClosedAnnouncements.widthProperty().subtract(50));
				listCell.initData(currentUserId, activeAnnouncementsObservableList, closedAnnouncementsObservableList);
				
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
		textInputDialog.setTitle("New Conversation");
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText("Name of the Conversation:");
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + dialogColor);

		Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		dialogResult = textInputDialog.showAndWait();
		dialogResult.ifPresent(conversationName -> {
			if (conversationName.isEmpty()) {
				alert.setContentText("Name of the conversation cannot be empty!");
				alert.showAndWait();
			} else {
				createNewConversation(dialogResult.get());
			}
		});
	}

	public void imageViewCurrentUserOnMouseClicked() {

		personalInfoDialog = new PersonalInfoDialog(dialogColor);

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
					imageViewCurrentUser.setImage(new Image(
							RequestManager.getRequestApi() + "/files/downloadpicture/" + personalInfo.getId()));
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

	private void updateMessagesListView() {

		try {
			messagesList = provider.getMessageProvider().getByConversationId(currentConversationId);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}

		int countOfItems = listViewChat.getItems().size();
		
		if (countOfItems != messagesList.size()) {
		
			Collections.reverse(messagesList);
	
			messagesObservableList = FXCollections.observableArrayList();
			messagesObservableList.setAll(messagesList);
	
			listViewChat.setItems(messagesObservableList);
			
			countOfItems = listViewChat.getItems().size();
			listViewChat.scrollTo(countOfItems);
		}
	
		if (countOfItems == 0)
			listViewChat.setPlaceholder(new Text("Send first message in this conversation!"));
	}

	private void updateConversationsListView() {

		try {
			conversationsList = provider.getConversationProvider().getByUserId(currentUserId);
			
			if (conversationsList.size() != listViewConversations.getItems().size())
				fillConversationsListView(conversationsList);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	private void updateCurrentUserPicture() {

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
			} catch (HttpErrorCodeException | IOException e) {
				e.printStackTrace();
			}

			if (personalInfo.getPicture() == null || personalInfo.getPicture().isEmpty())
				image = new Image("images/default_user_image.png");
			else
				image = new Image(
						RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(user.getId()));

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

		UserListCell.setUsersWithImagesList(generateUsersWithImagesList(list));
		usersObservableList = FXCollections.observableArrayList();
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
				button.setStyle("-fx-base: " + activeButtonColor);
			else
				button.setStyle("-fx-base: " + passiveButtonsColor);
		}
	}

	public void btnAddNewAnnouncementOnAction() {
		
		textInputDialog = new TextInputDialog("");
		textInputDialog.setTitle("New Announcement");
		textInputDialog.setHeaderText(null);
		textInputDialog.setContentText("Announcement description:");
		textInputDialog.getDialogPane().setStyle("-fx-background-color: " + dialogColor);		
		textInputDialog.getDialogPane().setPrefWidth(600);
		textInputDialog.setResizable(true);
		
		Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/icon.png"));
		
		dialogResult = textInputDialog.showAndWait();
		dialogResult.ifPresent(conversationName -> {
			if (conversationName.isEmpty()) {
				alert.setContentText("Announcement's description cannot be empty!");
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
			button.setStyle("-fx-base: " + passiveButtonsColor);
		}

		btnUpdate.setStyle("-fx-font-size: 11.5; -fx-base: " + newConversationColor);
		btnAttachment.setStyle("-fx-font-size: 11.5; -fx-base: " + newConversationColor);
		btnSmiles.setStyle("-fx-font-size: 12; -fx-base: " + newConversationColor);
		btnLogOut.setStyle("-fx-base: " + logOutColor);
		btnNewConversation.setStyle("-fx-base: " + newConversationColor);
		btnSend.setStyle("-fx-base: " + findAndSearchColor);
		btnFind.setStyle("-fx-base: " + findAndSearchColor);
		
		btnAddNewAnnouncement.getStylesheets().add(getClass().getResource("/css/buttons/add-new-announcement.css").toExternalForm());
	}
}