package com.elekscamp.messenger_javafx_client.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.DAL.RegistrationProvider;
import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import com.elekscamp.messenger_javafx_client.Entities.Credential;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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

	public AuthenticationAndRegistrationController() {
		registrationProvider = new RegistrationProvider(new RequestManager<User>(User.class));
		provider = new ContentProvider();
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				Stage stage = (Stage) signInButton.getScene().getWindow();
				stage.getIcons().add(new Image("/images/icon.png"));
			}
		});
	}

	public void signInButtonAction() {

		username = "Ananas";//loginSignInTextField.getText();
		password = "1234";//passwordSignInTextField.getText();
		usernameLength = username.length();
		passwordLength = password.length();

		signInStatusText.setFill(Color.RED);
		if (usernameLength == 0 || passwordLength == 0) {
			signInStatusText.setText("Fields cannot be empty.");
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
			signInStatusText.setText(e.getMessage());
			loginSignInTextField.requestFocus();
			e.printStackTrace();
		}
	}

	private void openChatWindow() throws IOException {

		loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Chat.fxml"));
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
			signUpStatusText.setText("Fields cannot be empty.");
			return;
		}

		if (usernameLength <= 5 || usernameLength >= 15) {
			signUpStatusText.setText("Username length should be more than 5 and less than 15.");
			return;
		}
		
		if (passwordLength <= 8 || passwordLength >= 20) {
			signUpStatusText.setText("Password length should be more than 8 and less than 20.");
			return;
		}

		if (!checkWithRegExp(username) || !checkWithRegExp(password)) {
			signUpStatusText.setText("Username and password can contain only Latin letters and digits.");
			return;
		}
		
		try {
			registrationProvider = new RegistrationProvider(new RequestManager<User>(User.class));
			response = registrationProvider.register(new User(username, password, email));
			signUpStatusText.setFill(Color.GREEN);
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