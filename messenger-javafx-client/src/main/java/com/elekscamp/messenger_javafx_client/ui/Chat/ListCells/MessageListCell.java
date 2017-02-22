package com.elekscamp.messenger_javafx_client.ui.Chat.ListCells;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.Main;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.RequestManager;
import com.elekscamp.messenger_javafx_client.entities.Message;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.entities.UserWithImage;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;

public class MessageListCell extends ListCell<Message> {

	private HBox mainHBox;
	private ImageView imageView;
	private Image image;
	private User user;
	private Label username;
	private VBox messageVBox;
	private TextFlow textFlowMessage;
	private SimpleDateFormat formatter;
	private Date date;
	private Label time;
	private AnchorPane topAnchorPane;
	private int currentUserId;
	private HBox imageHBox;
	private String attachmentUrl;
	private Hyperlink attachmentLink;
	private Rectangle shape;
	private HBox attachmentHBox;
	private Message message;
	private String attachmentExtenstion;

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

	private void createObjects() {
		mainHBox = new HBox();
		topAnchorPane = new AnchorPane();
		messageVBox = new VBox();
		textFlowMessage = new TextFlow();
		imageHBox = new HBox();
	}

	@Override
	protected void updateItem(Message message, boolean empty) {

		super.updateItem(message, empty);

		if (message != null) {

			this.message = message;

			createObjects();

			String messageText = message.getText();
			int userId = message.getUserId();

			mainHBox.getStylesheets().add(getClass().getResource("/css/message-list-cell.css").toExternalForm());//.setStyle("");
			mainHBox.getStyleClass().add("hbox");
			
			user = searchUserInListById(userId);

			username = new Label(user.getLogin());
			username.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

			image = searchImageInListByUserId(userId);

			imageView = new ImageView(image);
			imageView.setFitHeight(50);
			imageView.autosize();
			imageView.setPreserveRatio(true);

			double shapeWidth = imageView.getBoundsInParent().getWidth();
			double shapeHeight = imageView.getBoundsInParent().getHeight();

			shape = new Rectangle(shapeWidth, shapeHeight);
			shape.setArcWidth(18);
			shape.setArcHeight(18);

			imageView.setClip(shape);

			imageHBox.getChildren().add(imageView);

			date = new Date(message.getSendDate());

			time = new Label(formatter.format(date));
			time.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");

			AnchorPane.setTopAnchor(time, 2d);

			if (currentUserId == userId) {

				AnchorPane.setRightAnchor(username, 0d);
				messageVBox.setPadding(new Insets(0, 5, 2, 7));
				imageHBox.setAlignment(Pos.TOP_RIGHT);

				topAnchorPane.getChildren().addAll(time, username);
				mainHBox.getChildren().addAll(messageVBox, imageHBox);

			} else {

				AnchorPane.setRightAnchor(time, 2d);
				messageVBox.setPadding(new Insets(0, 7, 2, 5));
				imageHBox.setAlignment(Pos.TOP_LEFT);

				topAnchorPane.getChildren().addAll(username, time);
				mainHBox.getChildren().addAll(imageHBox, messageVBox);
			}

			topAnchorPane.prefWidthProperty().bind(this.widthProperty());

			messageVBox.getChildren().add(topAnchorPane);

			if (messageText != null && !messageText.isEmpty()) {

				List<Node> nodesInMessage = new ArrayList<>();
				nodesInMessage = messageTextIntoNodes(messageText);
		
				textFlowMessage.getChildren().addAll(nodesInMessage);
				textFlowMessage.setStyle("-fx-font-size: 15px;");
				messageVBox.getChildren().add(textFlowMessage);
			}

			if (message.getAttachment() != null) {
				messageVBox.getChildren().add(createAttachmentHBox());
			}

			setGraphic(mainHBox);
		} else {
			setGraphic(null);
		}
	}

	private HBox createAttachmentHBox() {

		String fileName = getFileName(message.getAttachment());
		attachmentExtenstion = getFileExtension(fileName);

		attachmentLink = new Hyperlink(fileName);	
		attachmentLink.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent t) {
				String concreteAttachmentUrl = attachmentUrl + Integer.toString(message.getId());
				Main.getInstance().getHostServices().showDocument(concreteAttachmentUrl);
			}
		});

		attachmentHBox = new HBox();
		attachmentHBox.getChildren().add(attachmentLink);

		if (isImageExtension(attachmentExtenstion)) {
			addHLinkForAttachedImage();
		} else {
			addHLinkForOpenAttachment();
		}

		return attachmentHBox;
	}

	private void addHLinkForOpenAttachment() {
		
		String openStr;
		
		if (GlobalVariables.language == Language.ENGLISH) {
			openStr = "Open";
		} else {
			openStr = "Відкрити";
		}
		
		Hyperlink openAttachmentHLink = new Hyperlink(openStr);

		openAttachmentHLink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String attachmentUrl = RequestManager.getRequestApi() + "/files/downloadAttachment/"
						+ Integer.toString(message.getId());

				File file;
				
				try {
					file = File.createTempFile("temp_attachment", "." + attachmentExtenstion);
					file.deleteOnExit();

					URL url = new URL(attachmentUrl);
					
					try (InputStream in = url.openStream()) {
						
						try (FileOutputStream out = new FileOutputStream(file)) {
						
							byte[] buffer = new byte[1024];
							int len;
							
							while ((len = in.read(buffer)) != -1) {
								out.write(buffer, 0, len);
							}
						} 
						
						Desktop.getDesktop().open(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		attachmentHBox.getChildren().add(openAttachmentHLink);
	}

	private void addHLinkForAttachedImage() {

		String showStr;
		String hideStr;
		
		if (GlobalVariables.language == Language.ENGLISH){
			showStr = "Show";
			hideStr = "Hide";
		} else {
			showStr = "Показати";
			hideStr = "Сховати";
		}

		Hyperlink showImageHLink = new Hyperlink(showStr);
		Hyperlink hideImageHLink = new Hyperlink(hideStr);
		ImageView attachmentImageView = new ImageView();

		showImageHLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String attachmentImageUrl = RequestManager.getRequestApi() + "/files/downloadAttachment/"
						+ Integer.toString(message.getId());
				Image attachmentImage = new Image(attachmentImageUrl);

				attachmentImageView.setImage(attachmentImage);
				attachmentImageView.setFitHeight(200);
				attachmentImageView.setFitWidth(200);
				attachmentImageView.setPreserveRatio(true);
				attachmentImageView.setOnMouseClicked(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						try {
							File file = File.createTempFile("temp_image", "." + attachmentExtenstion);
							file.deleteOnExit();

							BufferedImage bufferedImage = ImageIO.read(new URL(attachmentImageUrl));
							ImageIO.write(bufferedImage, attachmentExtenstion, file);
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

				int count = messageVBox.getChildren().size();
				messageVBox.getChildren().add(count - 1, attachmentImageView);

				attachmentHBox.getChildren().remove(showImageHLink);
				attachmentHBox.getChildren().add(hideImageHLink);
			}
		});

		hideImageHLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (messageVBox.getChildren().contains(attachmentImageView))
					messageVBox.getChildren().remove(attachmentImageView);

				attachmentHBox.getChildren().remove(hideImageHLink);
				attachmentHBox.getChildren().add(showImageHLink);
			}
		});

		attachmentHBox.getChildren().add(showImageHLink);
	}

	private List<Node> messageTextIntoNodes(String text) {

		String[] splittedText = text.split(" ");
		List<Node> result = new ArrayList<>();
		Label wordLabel;
		double wordMaxWidth = getPrefWidth() - imageView.getBoundsInParent().getWidth();
		Image smile;

		for (String word : splittedText) {

			smile = getSmileImage(word);

			if (smile == null) {
				wordLabel = new Label(word);
				wordLabel.setMaxWidth(wordMaxWidth);
				wordLabel.setWrapText(true);
				result.add(wordLabel);
			} else {
				result.add(new ImageView(smile) {
					public double getBaselineOffset() {
						return getImage().getHeight() * 0.7;
					}
				});
				result.add(new Label(" "));
			}
		}

		return result;
	}

	private Image getSmileImage(String str) {

		switch (str) {
		case "=)":
			return new Image("images/friendly-smile.jpg");
		case "=(":
			return new Image("images/sad-smile.jpg");
		case "=D":
			return new Image("images/happy-smile.jpg");
		case "='(":
			return new Image("images/very-sad-smile.jpg");
		case "X-(":
			return new Image("images/angry-smile.jpg");
		default:
			return null;
		}
	}

	private boolean isImageExtension(String extension) {

		switch (extension.toLowerCase()) {
		case "jpg":
		case "bmp":
		case "png":
			return true;
		default:
			return false;
		}
	}

	private User searchUserInListById(int id) {

		for (UserWithImage userWithImg : usersList) {
			if (userWithImg.getUser().getId() == id)
				return userWithImg.getUser();
		}

		throw new NullPointerException("User is null in MessageListCell.");
	}

	private String getFileName(String attachmentPath) {
		return attachmentPath.substring(attachmentPath.lastIndexOf("\\") + 37);
	}

	private String getFileExtension(String attachmentName) {
		return attachmentName.substring(attachmentName.lastIndexOf(".") + 1);
	}

	private Image searchImageInListByUserId(int id) {

		for (UserWithImage userWithImg : usersList) {
			if (userWithImg.getUser().getId() == id)
				return userWithImg.getImage();
		}

		throw new NullPointerException("User image is null in MessageListCell.");
	}
}
