package com.elekscamp.messenger_javafx_client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.dal.RegistrationProvider;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;
import com.elekscamp.messenger_javafx_client.entities.Credential;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AuthenticationAndRegistrationController implements Initializable {

	@FXML 
	TabPane authorizationTabPane;
	
	@FXML
	private TextField loginSignInTextField;
	@FXML
	private PasswordField passwordSignInTextField;
	@FXML
	private Text signInStatusText;
	@FXML
	private Button signInButton;

	@FXML
	private TextField loginSignUpTextField;
	@FXML
	private PasswordField passwordSignUpTextField;
	@FXML
	private TextField emailSignUpTextField;
	@FXML
	private Text signUpStatusText;
	@FXML
	private Button signUpButton;

	private RegistrationProvider registrationProvider;
	private Credential credential;
	private String username;
	private String password;
	private String email;
	private int usernameLength;
	private int passwordLength;
	private int emailLength;
	private User currentUser;
	private Parent root;
	private Stage stage;
	private FXMLLoader loader;
	private ChatController controller;
	private Scene scene;
	private String stylesheet;
	private ContentProvider provider;
	
	private final String FIELDS_CANNOT_BE_EMPTY;
	private final String USERNAME_LENGTH;
	private final String PASSWORD_LENGTH;
	private final String ONLY_LETTERS_AND_NUMBERS;
	
	public AuthenticationAndRegistrationController() {
		registrationProvider = new RegistrationProvider(new RequestManager<User>(User.class));
		provider = new ContentProvider();
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Stage stage = (Stage) signInButton.getScene().getWindow();
				stage.getIcons().add(new Image("/images/icon.png"));
			}
		});
		
		if (GlobalVariables.language == Language.ENGLISH) {
			FIELDS_CANNOT_BE_EMPTY = "Fields cannot be empty.";
			USERNAME_LENGTH = "Username length should not be less than 6 characters.";
			PASSWORD_LENGTH = "Password length should not be less than 8 characters.";
			ONLY_LETTERS_AND_NUMBERS = "Username and password can contain only Latin letters and digits.";
		} else {
			FIELDS_CANNOT_BE_EMPTY = "Поля не можуть бути порожні.";
			USERNAME_LENGTH = "Ім'я користувача повинно бути не коротше 6 символів";
			PASSWORD_LENGTH = "Довжина паролю повинна бути не менша за 8 символів.";
			ONLY_LETTERS_AND_NUMBERS = "Ім'я користувача та пароль можуть містити тільки латинські буки та цифри.";
		}
	}

	public void signInButtonAction() {

		username = loginSignInTextField.getText();
		password = passwordSignInTextField.getText();
		usernameLength = username.length();
		passwordLength = password.length();

		signInStatusText.setFill(Color.RED);
		if (usernameLength == 0 || passwordLength == 0) {
			signInStatusText.setText(FIELDS_CANNOT_BE_EMPTY);
			loginSignInTextField.requestFocus();
			return;
		}

		credential = new Credential(username, password);

		try {
			currentUser = RequestManager.authenticateUser(credential);
			currentUser.setIsOnline(true);
			provider.getUserProvider().update(currentUser.getId(), currentUser);
			stage = (Stage) signInButton.getScene().getWindow();
			stage.close();

			openChatWindow();
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			
			if (GlobalVariables.language == Language.UKRAINIAN && errorMessage.equals("Incorrect Login or Password."))
				errorMessage = "Ім'я користувача або пароль введено не вірно.";
			
			signInStatusText.setText(errorMessage);
			loginSignInTextField.requestFocus();
			e.printStackTrace();
		}
	}

	private void openChatWindow() throws IOException {

		if (GlobalVariables.language == Language.ENGLISH)
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainWindowEng.fxml"));
		else
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainWindowUkr.fxml"));
			
		root = loader.load();

		controller = loader.<ChatController>getController();
		controller.initData(currentUser.getId());

		scene = new Scene(root);
		stylesheet = getClass().getClassLoader().getResource("css/chat_style.css").toExternalForm();
		scene.getStylesheets().add(stylesheet);

		stage = new Stage();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {

				currentUser.setIsOnline(false);

				try {
					provider.getUserProvider().update(currentUser.getId(), currentUser);
				} catch (HttpErrorCodeException | IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});

		stage.setMinHeight(540);
		stage.setMinWidth(1116);
		stage.setTitle("Messenger");
		stage.setScene(scene);
		stage.show();
	}
	
	private boolean checkWithRegExp(String stringToCheck){  
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{5,15}$");  
        Matcher m = p.matcher(stringToCheck);  
        System.out.println(m.matches() + " " + stringToCheck);
        return m.matches();  
    }  
	
	public void signUpButtonAction() {

		String response;

		username = loginSignUpTextField.getText();
		password = passwordSignUpTextField.getText();
		email = emailSignUpTextField.getText();
		usernameLength = username.length();
		passwordLength = password.length();
		emailLength = email.length();

		signUpStatusText.setFill(Color.RED);
		
		if (usernameLength == 0 || passwordLength == 0 || emailLength == 0) {
			signUpStatusText.setText(FIELDS_CANNOT_BE_EMPTY);
			return;
		}

		if (usernameLength <= 5) {
			signUpStatusText.setText(USERNAME_LENGTH);
			return;
		}
		
		if (passwordLength <= 8 || passwordLength >= 20) {
			signUpStatusText.setText(PASSWORD_LENGTH);
			return;
		}

		if (!checkWithRegExp(username) || !checkWithRegExp(password)) {
			signUpStatusText.setText(ONLY_LETTERS_AND_NUMBERS);
			return;
		}
		
		try {
			registrationProvider = new RegistrationProvider(new RequestManager<User>(User.class));
			response = registrationProvider.register(new User(username, password, email));
			signUpStatusText.setFill(Color.WHITE);
			signUpStatusText.setText(response);
		} catch (Exception e) {
			signUpStatusText.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	public void signInEnterKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {
			signInButton.fire();
		}
	}

	public void signUpEnterKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {
			signUpButton.fire();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authorizationTabPane.setFocusTraversable(false);
		loginSignUpTextField.setTextFormatter(new TextFormatter<String>(change -> 
				change.getControlNewText().length() <= 15 ? change : null));
		passwordSignUpTextField.setTextFormatter(new TextFormatter<String>(change -> 
				change.getControlNewText().length() <= 20 ? change : null));
		emailSignUpTextField.setTextFormatter(new TextFormatter<String>(change ->
				change.getControlNewText().length() <= 40 ? change : null));
		loginSignInTextField.setTextFormatter(new TextFormatter<String>(change -> 
				change.getControlNewText().length() <= 15 ? change : null));
		passwordSignInTextField.setTextFormatter(new TextFormatter<String>(change -> 
				change.getControlNewText().length() <= 20 ? change : null));
	}
}