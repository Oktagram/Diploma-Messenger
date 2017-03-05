package com.elekscamp.messenger_javafx_client.ui.handlers;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ChatObjectsHandler {

	public Stage getAuthenticationStage(ContentProvider provider, User currentUser, Button btnSend) {
		
		FXMLLoader loader;
		Stage stage = null;
		
		if (GlobalVariables.language == Language.ENGLISH)
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AuthenticationWindowEng.fxml"));
		else
			loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AuthenticationWindowUkr.fxml"));
			
		try {
			currentUser.setIsOnline(false);
			provider.getUserProvider().update(currentUser.getId(), currentUser);

			Parent root = loader.load();

			stage = (Stage) btnSend.getScene().getWindow();
			stage.close();

			Scene scene = new Scene(root);

			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Messenger");
			stage.setResizable(false);
			stage.setMinWidth(520);
			stage.setMinHeight(450);
		} catch (HttpErrorCodeException | IOException e) {
			e.printStackTrace();
		}
		
		return stage;
	}
	
	public void customizeUsersPicture(ImageView imageView, String picture, int currentUserId) {
		Image profileImage;
		
		if (picture == null || picture.isEmpty())
			profileImage = new Image("images/default_user_image.png");
		else
			profileImage = new Image(
					RequestManager.getRequestApi() + "/files/downloadpicture/" + Integer.toString(currentUserId));

		imageView.setImage(profileImage);
	}
	
	public void customizeAnnouncementsTabPane(TabPane announcementsTabPane) {
		announcementsTabPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> value, Number oldWidth, Number newWidth) {
                Side side = announcementsTabPane.getSide();
                int numTabs = announcementsTabPane.getTabs().size();
                if ((side == Side.BOTTOM || side == Side.TOP) && numTabs != 0) {
                	announcementsTabPane.setTabMinWidth(newWidth.intValue() / numTabs - 22);
                	announcementsTabPane.setTabMaxWidth(newWidth.intValue() / numTabs - 22);
                }
            }
        });

		announcementsTabPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> value, Number oldHeight, Number newHeight) {
                Side side = announcementsTabPane.getSide();
                int numTabs = announcementsTabPane.getTabs().size();
                if ((side == Side.LEFT || side == Side.RIGHT) && numTabs != 0) {
                	announcementsTabPane.setTabMinWidth(newHeight.intValue() / numTabs - 22);
                	announcementsTabPane.setTabMaxWidth(newHeight.intValue() / numTabs - 22);
               }
           }
        });
	}
}
