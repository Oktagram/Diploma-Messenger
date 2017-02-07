package com.elekscamp.messenger_javafx_client.UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.elekscamp.messenger_javafx_client.Main;
import com.elekscamp.messenger_javafx_client.DAL.RequestManager;
import com.elekscamp.messenger_javafx_client.Entities.Message;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserWithImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

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
	private HBox imageHBox;
	private String attachmentUrl;
	private Hyperlink attachmentLink;
	private Rectangle clip;
	
	private static List<UserWithImage> usersList;
	
	public MessageListCell() {
		formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
		attachmentUrl = RequestManager.getRequestApi() + "/files/downloadAttachment/";
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
			mainHBox.setStyle("-fx-border-color: black; -fx-border-radius: 10px;");
			
        	anchorPane = new AnchorPane();

        	user = searchUserInListById(item.getUserId());
      
        	username = new Label(user.getLogin());
            username.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            image = searchImageInListByUserId(item.getUserId());
    		imageView = new ImageView(image);
    		imageView.setFitHeight(50);
    		imageView.setPreserveRatio(true);
    		
            text = new Label(item.getText());
            text.setWrapText(true);
            text.setStyle("-fx-font-size: 15px;");
                 
            vBox = new VBox();
                 
            date = new Date(item.getSendDate());
            time = new Label(formatter.format(date));
            time.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");    
            
            imageHBox = new HBox();
            imageHBox.getChildren().add(imageView);
             
            if (currentUserId == user.getId()) {
            	
            	anchorPane.getChildren().addAll(time, username);
            	vBox.setPadding(new Insets(0, 5, 2, 7));
	            AnchorPane.setRightAnchor(username, 0d);
	            mainHBox.getChildren().addAll(vBox, imageHBox);
	            imageHBox.setAlignment(Pos.TOP_RIGHT);
	            
            } else {
            	
            	anchorPane.getChildren().addAll(username, time);
	            AnchorPane.setRightAnchor(time, 2d);
	            mainHBox.getChildren().addAll(imageHBox, vBox);
	            imageHBox.setAlignment(Pos.TOP_LEFT);
	            imageView.setFitWidth(50);
	            imageHBox.setMinWidth(50);   
	            
            }
            
            AnchorPane.setTopAnchor(time, 2d);
            
            clip = new Rectangle(
            		imageView.getBoundsInParent().getWidth(), 
            		imageView.getBoundsInParent().getHeight()
            		);

            clip.setArcWidth(18);
            clip.setArcHeight(18);
            imageView.setClip(clip);
            
            vBox.getChildren().add(anchorPane);
            if (item.getText() != null && !text.getText().isEmpty()) vBox.getChildren().add(text);
            
            if (item.getAttachment() != null) {
            	
            	attachmentLink = new Hyperlink(getAttachmentName(item.getAttachment()));
            	vBox.getChildren().add(attachmentLink);
            	
            	attachmentLink.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                    	Main.getInstance().getHostServices().showDocument(attachmentUrl + Integer.toString(item.getId()));
                    }

                });
            }
            
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

	private String getAttachmentName(String attachmentPath) {
		
		return attachmentPath.substring(attachmentPath.lastIndexOf("\\") + 37);
	}
	
	private Image searchImageInListByUserId(int id) {
		
		for (UserWithImage userWithImg : usersList) {
			if (userWithImg.getUser().getId() == id) return userWithImg.getImage();
		}
		
		throw new NullPointerException("User image is null in MessageListCell.");
	}
}
