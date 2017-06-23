package com.elekscamp.messenger_javafx_client.ui.PersonalInfo;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;
import com.elekscamp.messenger_javafx_client.entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PersonalInfoDialog {

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String NAME_PATTERN = "^[a-zA-Zа-яА-Я\\s]+";
	private static final String PHONE_PATTER = 
			"^(([(]?(\\d{2,4})[)]?)|(\\d{2,4})|([+1-9]+\\d{1,2}))?[-\\s]?(\\d{2,3})?[-\\s]?((\\d{7,8})|(\\d{3,4}[-\\s]\\d{3,4}))$";
	
	private SimpleDateFormat dateFormat;
	private Dialog<Pair<User, PersonalInfo>> dialog;
	private GridPane gridPane;
	private TextField tfUsername;
	private PasswordField pfPassword;
	private TextField tfEmail;
	private TextField tfFirstName;
	private TextField tfLastName;
	private TextField tfPhoneNumber;
	private String birthDateStr;
	private String registrationDateStr;
	private Text txtIsOnline;
	private ImageView picture;
	private Text txtRegistrationDate;
	private String backgroundColor;
	private ButtonType btnSave;
	private Alert alert;
	private DatePicker birthDateDatePicker;
	private String[] birthDateSplitted;
	private VBox vBox;
	private HBox hBox;
	private GridPane topGridPane;
	private Button btnChangePicture;
	private Label txtPictureName;
	private File newProfilePicture;
	private FileChooser chooser;
	private Tooltip pictureNameTooltip;
	private Image profileImage;
	private HBox pictureHBox;
	private int gridRow;
	private Button btnDeleteProfilePicture;
	private Button btnRemoveSelectedPicture;
	private final String NO_PICTURE_CHOSEN;
	private final String CHOOSE_PICTURE;
	private final String DELETE_PICTURE;
	private final String PROFILE_PICTURE;
	private final String FIELDS_WITH_STAR;
	private final String YES;
	private final String NO;
	private final String INFORMATION_ABOUT_USER;
	private final String IS_ONLINE;
	private final String REGISTRATION_DATE;
	private final String USERNAME;
	private final String EMAIL;
	private final String FIRST_NAME;
	private final String LAST_NAME;
	private final String PHONE_NUMBER;
	private final String BIRTH_DATE;
	private final String SAVE;
	private final String NEW_PASSWORD;
	private final String MESSAGE;
	private final String PASSWORD_LENGTH;
	
	public PersonalInfoDialog(String backgroundColor) {

		this.backgroundColor = backgroundColor;
		
		if (GlobalVariables.language == Language.ENGLISH) {
			NO_PICTURE_CHOSEN = "No picture chosen.";
			CHOOSE_PICTURE = "Choose picture...";
			DELETE_PICTURE = "Delete Picture";
			PROFILE_PICTURE = "Profile Picture";
			FIELDS_WITH_STAR = "Fields with '*' cannot be empty!";
			YES = "Yes";
			NO = "No";
			INFORMATION_ABOUT_USER = "Information about user ";
			IS_ONLINE = "Is Online: ";
			REGISTRATION_DATE = "Registration date: ";
			USERNAME = "Username: ";
			EMAIL = "Email: ";
			FIRST_NAME = "First name: "; 
			LAST_NAME = "Last name: "; 
			PHONE_NUMBER = "Phone number: "; 
			BIRTH_DATE = "Birth date: "; 
			SAVE = "Save";
			NEW_PASSWORD = "New password: ";
			MESSAGE = "Message";
			PASSWORD_LENGTH = "Password length should not be less than 8 characters.";
		} else {
			NO_PICTURE_CHOSEN = "Картинка не вибрана.";
			CHOOSE_PICTURE = "Вибрати картинку...";
			DELETE_PICTURE = "Видалити картинку";
			PROFILE_PICTURE = "Картинка профілю";
			FIELDS_WITH_STAR = "Поля з * не можуть бути порожні!";
			YES = "Так";
			NO = "Ні";
			INFORMATION_ABOUT_USER = "Інформація про користувача ";
			IS_ONLINE = "В мережі: ";
			REGISTRATION_DATE = "Дата реєстрації: ";
			USERNAME = "Логін: ";
			EMAIL = "Пошта: ";
			FIRST_NAME = "Ім'я: ";
			LAST_NAME = "Прізвище: ";
			PHONE_NUMBER = "Номер телефону: ";
			BIRTH_DATE = "Дата народження: ";
			SAVE = "Зберегти";
			NEW_PASSWORD = "Новий пароль: ";
			MESSAGE = "Повідомлення";
			PASSWORD_LENGTH = "Довжина паролю повинна бути не менша за 8 символів.";
		}
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dialog = new Dialog<Pair<User, PersonalInfo>>();
		gridPane = new GridPane();
		tfUsername = new TextField();
		pfPassword = new PasswordField();
		tfEmail = new TextField();
		tfFirstName = new TextField();
		tfLastName = new TextField();
		tfPhoneNumber = new TextField();
		picture = new ImageView();
		birthDateDatePicker = new DatePicker();
		btnChangePicture = new Button(CHOOSE_PICTURE);
		txtPictureName = new Label(NO_PICTURE_CHOSEN);
		chooser = new FileChooser();
		pictureNameTooltip = new Tooltip();
		pictureHBox = new HBox();
		gridRow = 0;
		btnDeleteProfilePicture = new Button(DELETE_PICTURE);
		btnRemoveSelectedPicture = new Button("X");
	}

	public Optional<Pair<User, PersonalInfo>> show(User user, PersonalInfo info, boolean allowEditing) {

		btnRemoveSelectedPicture.setPrefWidth(btnRemoveSelectedPicture.getHeight());

		chooser.setTitle(PROFILE_PICTURE);
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		chooser.getExtensionFilters().addAll(
				new ExtensionFilter("All Images", "*.jpg", "*.bmp", "*.png"),
				new ExtensionFilter("JPG", "*.jpg"),
				new ExtensionFilter("BMP", "*.bmp"), 
				new ExtensionFilter("PNG", "*.png"));

		btnChangePicture.setMinWidth(128);

		btnDeleteProfilePicture.setMinWidth(btnChangePicture.getMinWidth());

		txtPictureName.setMaxWidth(250);
		txtPictureName.setTooltip(pictureNameTooltip);

		pictureNameTooltip.setText(txtPictureName.getText());

		dialog.setTitle(INFORMATION_ABOUT_USER + user.getLogin());
		dialog.setHeaderText(null);
		dialog.getDialogPane().setStyle(
				"-fx-font-size: 14; -fx-font-family: \"Comic Sans MS\", cursive, sans-serif; -fx-background-color: " + backgroundColor);

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 10, 10, 10));

		tfUsername.setText(user.getLogin());
		tfUsername.setMinWidth(258);
		tfUsername.setMaxWidth(258);

		tfEmail.setText(user.getEmail());
		tfFirstName.setText(info.getFirstName());
		tfLastName.setText(info.getLastName());
		tfPhoneNumber.setText(info.getPhoneNumber());

		birthDateStr = dateFormat.format(new Date(info.getBirthDate()));

		birthDateSplitted = birthDateStr.split("/");
		birthDateDatePicker.setValue(LocalDate.of(Integer.parseInt(birthDateSplitted[2]),
				Integer.parseInt(birthDateSplitted[1]), Integer.parseInt(birthDateSplitted[0])));
		birthDateDatePicker.setEditable(false);

		registrationDateStr = dateFormat.format(new Date(user.getRegistrationDate()));
		txtRegistrationDate = new Text(registrationDateStr);

		txtIsOnline = new Text(user.getIsOnline() ? YES : NO);
		
		StringProperty imageUrl = new SimpleStringProperty(RequestManager.getRequestApi() + "/files/downloadPicture/" + Integer.toString(user.getId()));
		
		if (info.getPicture() == null || info.getPicture().isEmpty())
			imageUrl.set("images/default_user_image.png");
		
		profileImage = new Image(imageUrl.get());

		picture.setImage(profileImage);
		picture.setFitHeight(150);
		picture.setFitWidth(150);
		picture.setPreserveRatio(true);
		picture.setOnMouseClicked(new EventHandler<Event>() {
			@Override public void handle(Event event) {
				try {
					File file = File.createTempFile("temp_image", ".png");	
					file.deleteOnExit();
					
					BufferedImage bufferedImage = ImageIO.read(new URL(imageUrl.get()));
					ImageIO.write(bufferedImage, "png", file);
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		vBox = new VBox();
		hBox = new HBox();
		vBox.setAlignment(Pos.CENTER);
		hBox.setAlignment(Pos.CENTER);

		topGridPane = new GridPane();
		topGridPane.setHgap(10);
		topGridPane.setVgap(10);

		hBox.getChildren().add(vBox);
		vBox.getChildren().add(topGridPane);

		pictureHBox.getChildren().add(picture);
		pictureHBox.setAlignment(Pos.CENTER);

		gridPane.add(pictureHBox, 0, 0);
		gridPane.add(hBox, 1, 0);

		GridPane.setHalignment(hBox, HPos.CENTER);
		GridPane.setValignment(hBox, VPos.CENTER);

		topGridPane.add(new Text(IS_ONLINE), 0, 0);
		topGridPane.add(txtIsOnline, 1, 0);
		topGridPane.add(new Text(REGISTRATION_DATE), 0, 1);
		topGridPane.add(txtRegistrationDate, 1, 1);

		if (allowEditing) {
			gridPane.add(btnChangePicture, 0, ++gridRow);

			AnchorPane selectedPictureAP = new AnchorPane();

			selectedPictureAP.getChildren().add(txtPictureName);
			AnchorPane.setLeftAnchor(txtPictureName, 0d);
			AnchorPane.setTopAnchor(txtPictureName, 0d);
			AnchorPane.setBottomAnchor(txtPictureName, 0d);

			selectedPictureAP.getChildren().add(btnRemoveSelectedPicture);
			AnchorPane.setRightAnchor(btnRemoveSelectedPicture, 0d);
			AnchorPane.setTopAnchor(btnRemoveSelectedPicture, 0d);
			AnchorPane.setBottomAnchor(btnRemoveSelectedPicture, 0d);

			gridPane.add(selectedPictureAP, 1, gridRow);
			gridPane.add(btnDeleteProfilePicture, 0, ++gridRow);
		}

		gridPane.add(new Text(USERNAME), 0, ++gridRow);
		gridPane.add(tfUsername, 1, gridRow);

		gridPane.add(new Text(EMAIL), 0, ++gridRow);
		gridPane.add(tfEmail, 1, gridRow);

		gridPane.add(new Text(FIRST_NAME), 0, ++gridRow);
		gridPane.add(tfFirstName, 1, gridRow);

		gridPane.add(new Text(LAST_NAME), 0, ++gridRow);
		gridPane.add(tfLastName, 1, gridRow);

		gridPane.add(new Text(PHONE_NUMBER), 0, ++gridRow);
		gridPane.add(tfPhoneNumber, 1, gridRow);

		gridPane.add(new Text(BIRTH_DATE), 0, ++gridRow);
		gridPane.add(birthDateDatePicker, 1, gridRow);

		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		if (allowEditing) {
			
			btnSave = new ButtonType(SAVE, ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(btnSave);

			gridPane.add(new Text(NEW_PASSWORD), 0, ++gridRow);
			gridPane.add(pfPassword, 1, gridRow);
			gridPane.add(new Text("*"), 2, 3);
			gridPane.add(new Text("*"), 2, 4);
			
		} else {
			
			tfUsername.setEditable(false);
			tfEmail.setEditable(false);
			tfFirstName.setEditable(false);
			tfLastName.setEditable(false);
			tfPhoneNumber.setEditable(false);
		}

		dialog.setResultConverter(dialogButton -> {

			if (dialogButton == btnSave) {

				ContentProvider provider;
				int userId = user.getId();
				String username = tfUsername.getText();
				String password = pfPassword.getText();
				String email = tfEmail.getText();
				String firstName = tfFirstName.getText();
				String lastName = tfLastName.getText();
				String phoneNumber = tfPhoneNumber.getText();
				LocalDate localDate = birthDateDatePicker.getValue();
				Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));

				if (newProfilePicture != null) {

					provider = new ContentProvider();

					try {
						provider.getFileProvider().uploadProfilePicture(newProfilePicture, userId);
						info.setPicture(provider.getPersonalInfoProvider().getById(userId).getPicture());
					} catch (IOException | HttpErrorCodeException e) {
						e.printStackTrace();
					}
				}

				if (username.isEmpty() || email.isEmpty() || (password.length() < 8 && !password.isEmpty())) {

					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle(MESSAGE);
					alert.setHeaderText(null);
					alert.getDialogPane().setStyle("-fx-background-color: " + backgroundColor);
					alert.setContentText(FIELDS_WITH_STAR);

					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("/images/icon.png"));
					
					if (!password.isEmpty() && password.length() < 8)
						alert.setContentText(PASSWORD_LENGTH);

					alert.showAndWait();
					throw new NullPointerException();
				}

				if ((firstName != null && !firstName.isEmpty() && !firstName.matches(NAME_PATTERN)) 
						|| (lastName != null && !lastName.isEmpty() && !lastName.matches(NAME_PATTERN))){
			    	alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message");
					alert.setHeaderText(null);
					alert.getDialogPane().setStyle("-fx-background-color: " + backgroundColor);
					alert.setContentText("Ім'я або прізвище не валідне!");
					alert.showAndWait();
					throw new NullPointerException();
			    }
			    	
			    if (phoneNumber != null && !phoneNumber.isEmpty() && !phoneNumber.matches(PHONE_PATTER)){
			    	alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message");
					alert.setHeaderText(null);
					alert.getDialogPane().setStyle("-fx-background-color: " + backgroundColor);
					alert.setContentText("Номер телефону не валідний!");
					alert.showAndWait();
					throw new NullPointerException();
			    }
			    	
			    if (!email.matches(EMAIL_PATTERN)){
			    	alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Message");
					alert.setHeaderText(null);
					alert.getDialogPane().setStyle("-fx-background-color: " + backgroundColor);
					alert.setContentText("Емейл не валідний!");
					alert.showAndWait();
					throw new NullPointerException();
			   	}
				
				user.setLogin(username);
				user.setEmail(email);
				if (!password.isEmpty())
					user.setPassword(pfPassword.getText());
				info.setFirstName(firstName);
				info.setLastName(lastName);
				info.setPhoneNumber(phoneNumber);
				info.setBirthDate(instant.toEpochMilli());

				return new Pair<>(user, info);
			}
			return null;
		});

		btnChangePicture.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {

				try {
					newProfilePicture = chooser.showOpenDialog(null);
				} catch (IllegalArgumentException ex) {
					chooser.setInitialDirectory(new File(System.getProperty("user.home")));
					newProfilePicture = chooser.showOpenDialog(null);
				}

				if (newProfilePicture != null)
					txtPictureName.setText(newProfilePicture.getName());
				else
					txtPictureName.setText(NO_PICTURE_CHOSEN);

				pictureNameTooltip.setText(txtPictureName.getText());
			}
		});

		btnDeleteProfilePicture.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				info.setPicture(null);
			}
		});

		btnRemoveSelectedPicture.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				newProfilePicture = null;
				txtPictureName.setText(NO_PICTURE_CHOSEN);
			}
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		Stage dialogStage = (Stage) dialogPane.getScene().getWindow();
		dialogStage.getIcons().add(new Image("/images/icon.png"));
		return dialog.showAndWait();
	}
}