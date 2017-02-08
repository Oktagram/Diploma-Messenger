package com.elekscamp.messenger_javafx_client.Controllers;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.DAL.RegistrationProvider;
import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import com.elekscamp.messenger_javafx_client.Entities.Credential;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AuthenticationAndRegistrationController {

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
	}
	
	public void signInButtonAction() {
		
		username = loginSignInTextField.getText();
		password = passwordSignInTextField.getText();
		usernameLength = username.length();
		passwordLength = password.length();
		
		signInStatusText.setFill(Color.RED);
		if (usernameLength == 0 || passwordLength == 0) {
			signInStatusText.setText("Fields cannot be empty.");
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
		
		if (passwordLength < 8)  {
			signUpStatusText.setText("Password length should be 8 or more.");
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
}