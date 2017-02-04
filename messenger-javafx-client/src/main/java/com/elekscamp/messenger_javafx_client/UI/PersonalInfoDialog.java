package com.elekscamp.messenger_javafx_client.UI;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import com.elekscamp.messenger_javafx_client.Entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

public class PersonalInfoDialog {

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
	private final String NO_PICTURE_CHOSEN;
	private Tooltip pictureNameTooltip;
	private Image profileImage;
	private HBox pictureHBox;
	private int gridRow;
	
	public PersonalInfoDialog(String backgroundColor) {
		
		this.backgroundColor = backgroundColor;
		NO_PICTURE_CHOSEN = "No picture chosen.";
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
		btnChangePicture = new Button("Choose picture...");
		txtPictureName = new Label(NO_PICTURE_CHOSEN);
		chooser = new FileChooser();
		pictureNameTooltip = new Tooltip();
		pictureHBox = new HBox();
		gridRow = 0;
	}
	
	public Optional<Pair<User, PersonalInfo>> show(User user, PersonalInfo info, boolean allowEditing) {
		
		chooser.setTitle("Profile Picture");
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));                 
		chooser.getExtensionFilters().addAll(
		    new ExtensionFilter("All Images", "*.jpg", "*.gif", "*.bmp", "*.png"),
		    new ExtensionFilter("JPG", "*.jpg"),
		    new ExtensionFilter("GIF", "*.gif"),
		    new ExtensionFilter("BMP", "*.bmp"),
		    new ExtensionFilter("PNG", "*.png")
		);
		
		btnChangePicture.setMinWidth(128);
		
		txtPictureName.setMaxWidth(250);
		txtPictureName.setTooltip(pictureNameTooltip);
		
		pictureNameTooltip.setText(txtPictureName.getText());
		
		dialog.setTitle("Information about user " + user.getLogin());
		dialog.setHeaderText(null);
		dialog.getDialogPane().setStyle("-fx-font-size: 14; -fx-font-family: \"Comic Sans MS\", cursive, sans-serif; -fx-background-color: " + backgroundColor);
		
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
		birthDateDatePicker.setValue(LocalDate.of(
				Integer.parseInt(birthDateSplitted[2]), 
				Integer.parseInt(birthDateSplitted[1]), 
				Integer.parseInt(birthDateSplitted[0])));
		birthDateDatePicker.setEditable(false);
	
		registrationDateStr = dateFormat.format(new Date(user.getRegistrationDate()));
		txtRegistrationDate = new Text(registrationDateStr);
		
		txtIsOnline = new Text(user.getIsOnline() ? "Yes" : "No");
		
		if (info.getPicture() == null || info.getPicture().isEmpty()) 
			profileImage = new Image("images/default_user_image.png");
		else 
			profileImage = new Image(RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(user.getId()));
		
		picture.setImage(profileImage);
		picture.setFitHeight(150);
		picture.setFitWidth(150);
		picture.setPreserveRatio(true);
		
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
		
		topGridPane.add(new Text("Is online: "), 0, 0);
		topGridPane.add(txtIsOnline, 1, 0);
		topGridPane.add(new Text("Registration date: "), 0, 1);
		topGridPane.add(txtRegistrationDate, 1, 1);
		
		if (allowEditing) {
			gridPane.add(btnChangePicture, 0, ++gridRow);
			gridPane.add(txtPictureName, 1, gridRow);
		}
		
		gridPane.add(new Text("Username: "), 0, ++gridRow);
		gridPane.add(tfUsername, 1, gridRow);
		
		gridPane.add(new Text("Email: "), 0, ++gridRow);
		gridPane.add(tfEmail, 1, gridRow);
		
		gridPane.add(new Text("First name: "), 0, ++gridRow);
		gridPane.add(tfFirstName, 1, gridRow);
		
		gridPane.add(new Text("Last name: "), 0, ++gridRow);
		gridPane.add(tfLastName, 1, gridRow);
		
		gridPane.add(new Text("Phone number: "), 0, ++gridRow);
		gridPane.add(tfPhoneNumber, 1, gridRow);
		
		gridPane.add(new Text("Birth date: "), 0, ++gridRow);
		gridPane.add(birthDateDatePicker, 1, gridRow);
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		if (allowEditing) {
			btnSave = new ButtonType("Save", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(btnSave);
			
			gridPane.add(new Text("New password: "), 0, ++gridRow);
			gridPane.add(pfPassword, 1, gridRow);
			gridPane.add(new Text("*"), 2, 2);
			gridPane.add(new Text("*"), 2, 3);
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
					alert.setTitle("Message");
					alert.setHeaderText(null);
					alert.getDialogPane().setStyle("-fx-background-color: " + backgroundColor);
					alert.setContentText("Fields with '*' cannot be empty!");
					
					if (!password.isEmpty() && password.length() < 8) 
						alert.setContentText("Password length should be 8 or more!");
					
					alert.showAndWait();
					return null;
		    	} 
		    	
		    	user.setLogin(username);
		    	user.setEmail(email);
		    	if (!password.isEmpty()) user.setPassword(pfPassword.getText());
		    	info.setFirstName(firstName);
		    	info.setLastName(lastName);
		    	info.setPhoneNumber(phoneNumber);
		    	info.setBirthDate(instant.toEpochMilli());
		    
		    	return new Pair<>(user, info);
		    }
		    return null;
		});
		
		btnChangePicture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	try {
            		newProfilePicture = chooser.showOpenDialog(null);
            	} catch(IllegalArgumentException ex) {
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
		
		dialog.getDialogPane().setContent(gridPane);
		
		return dialog.showAndWait();
	}
}