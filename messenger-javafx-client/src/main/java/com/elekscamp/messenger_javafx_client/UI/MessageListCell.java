package com.elekscamp.messenger_javafx_client.UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.Entities.Message;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserWithImage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageListCell extends ListCell<Message> {

	private HBox mainHBox;
	private ImageView imageView;
	private Image image;
	private User user;
	private Label username;
	private VBox vBox;
	private Label text;
	private SimpleDateFormat formatter;
	private Date date;
	private Label time;
	private AnchorPane anchorPane;
	private int currentUserId;
	private static List<UserWithImage> usersList;
	public MessageListCell() {
        formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
        new ContentProvider();
        System.out.println("MessageListCellCreated");
	}
	
	public void setCurrentUserId(int id) {
		currentUserId = id;
	}
	
	public static void setUsersList(List<UserWithImage> list) {
		usersList = list;
	}
	
	@Override
    protected void updateItem(Message item, boolean empty) {

		super.updateItem(item, empty);
         
		if (item != null) {

			mainHBox = new HBox();
        	anchorPane = new AnchorPane();
        	 
        	user = searchUserInListById(item.getUserId());
      
        	username = new Label(user.getLogin());
            username.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            image = searchImageInListByUserId(item.getUserId());
    		imageView = new ImageView(image);
    		imageView.setFitHeight(50);
    		imageView.setFitWidth(50);
    		imageView.setPreserveRatio(true);
    		
            text = new Label(item.getText());
            text.setWrapText(true);
            text.setStyle("-fx-font-size: 15px;");
                 
            vBox = new VBox();
            vBox.setPadding(new Insets(0, 5, 5, 5));
                 
            date = new Date(item.getSendDate());
            time = new Label(formatter.format(date));
            time.setStyle("-fx-font-size: 12px;");    
            
            HBox imageHBox = new HBox();
            imageHBox.getChildren().add(imageView);
            imageHBox.setAlignment(Pos.CENTER);
            imageHBox.setMinWidth(50);
            
            if (currentUserId == user.getId()) {
            	anchorPane.getChildren().addAll(time, username);
	            AnchorPane.setRightAnchor(username, (double)0);
	            mainHBox.getChildren().addAll(vBox, imageHBox);
            } else {
            	anchorPane.getChildren().addAll(username, time);
	            AnchorPane.setRightAnchor(time, (double)0);
	            mainHBox.getChildren().addAll(imageHBox, vBox);
            }
                 
            vBox.getChildren().addAll(anchorPane, text);
            anchorPane.prefWidthProperty().bind(this.widthProperty());
                 
            setGraphic(mainHBox);  
		} else {
            setGraphic(null);
        }
	}
	
	private User searchUserInListById(int id) {
		
		for (UserWithImage userWithImg : usersList) {
			if (userWithImg.getUser().getId() == id) return userWithImg.getUser();
		}
		
		throw new NullPointerException("User is null in MessageListCell.");
	}
	
	private Image searchImageInListByUserId(int id) {
		
		for (UserWithImage userWithImg : usersList) {
			if (userWithImg.getUser().getId() == id) return userWithImg.getImage();
		}
		
		throw new NullPointerException("User image is null in MessageListCell.");
	}
}
