package com.elekscamp.messenger_javafx_client.ui.list_cells;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.GlobalVariables.Language;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.entities.Announcement;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ActiveAnnouncementListCell extends ListCell<Announcement> {

	private static int currentUserId;
	private ContentProvider provider;
	private SimpleDateFormat formatter;
	private ObservableList<Announcement> activeAnnouncementList;
	private ObservableList<Announcement> closedAnnouncementList;
	private final String EDIT_ANNOUNCEMENT;
	private final String ANNOUNCEMENT_DESCRIPTION;
	private final String ANNOUNCEMENT_DESCRIPTION_EMPTY;
	private final String EDIT;
	private final String CLOSE;
	private final String MESSAGE;
	private final String NOT_ANNOUNCEMENT_CREATOR;
	
	public ActiveAnnouncementListCell(ContentProvider provider) {
		
		this.provider = provider;
		formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
		
		if (GlobalVariables.language == Language.ENGLISH) {
			EDIT_ANNOUNCEMENT = "Edit Announcement";
			ANNOUNCEMENT_DESCRIPTION = "Announcement description:";
			ANNOUNCEMENT_DESCRIPTION_EMPTY = "Announcement's description cannot be empty!";
			EDIT = "Edit";
			CLOSE = "Close";
			MESSAGE = "Message";
			NOT_ANNOUNCEMENT_CREATOR = "Only creator can delete the announcement.";
		} else {
			EDIT_ANNOUNCEMENT = "Редагувати Оголошення";
			ANNOUNCEMENT_DESCRIPTION = "Опис оголошення:";
			ANNOUNCEMENT_DESCRIPTION_EMPTY = "Опис оголошення не може бути порожній!";
			EDIT = "Редагувати";
			CLOSE = "Закрити";
			MESSAGE = "Повідомлення";
			NOT_ANNOUNCEMENT_CREATOR = "Ви не можете видалити чуже оголошення.";
		}
	}
	
	public static void SetCurrentUserId(int userId){
		currentUserId = userId;
	}
	
	public void initData(ObservableList<Announcement> activeAnnouncementList, ObservableList<Announcement> closedAnnouncementList) {
		this.activeAnnouncementList = activeAnnouncementList;
		this.closedAnnouncementList = closedAnnouncementList;
	}
	
	@Override protected void updateItem(Announcement announcement, boolean empty) {

		super.updateItem(announcement, empty);

		if (announcement != null) {

			HBox mainHBox = new HBox();
			HBox buttonsHBox = new HBox();
			AnchorPane anchorPane = new AnchorPane();
			Label description = new Label(announcement.getDescription());
			Button btnEditDescription = new Button();
			Button btnCloseAnnouncement = new Button();
			Tooltip descriptionTooltip = new Tooltip(description.getText());
			String descriptionTooltipStr = "";
			String userThatCreatedAnnouncement = "";
			String buttonStylesheet = getClass().getResource("/css/buttons/with-image.css").toExternalForm();
			
			try {
				userThatCreatedAnnouncement = provider.getUserProvider().getById(announcement.getUserId()).getLogin();
			} catch (HttpErrorCodeException | IOException e) {
				e.printStackTrace();
			}
	
		    try {
		        Field fieldBehavior = descriptionTooltip.getClass().getDeclaredField("BEHAVIOR");
		        fieldBehavior.setAccessible(true);
		        Object objBehavior = fieldBehavior.get(descriptionTooltip);

		        Field fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
		        fieldTimer.setAccessible(true);
		        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

		        objTimer.getKeyFrames().clear();
		        objTimer.getKeyFrames().add(new KeyFrame(new Duration(25000)));
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    
			btnEditDescription.setMinSize(22, 22);
			btnEditDescription.setPadding(new Insets(-10));
		    btnEditDescription.setGraphic(new ImageView("/images/edit-announcement.png"));
		    btnEditDescription.getStylesheets().add(buttonStylesheet);
		    btnEditDescription.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TextInputDialog textInputDialog = new TextInputDialog(description.getText());
					textInputDialog.setTitle(EDIT_ANNOUNCEMENT);
					textInputDialog.setHeaderText(null);
					textInputDialog.setContentText(ANNOUNCEMENT_DESCRIPTION);
					textInputDialog.getDialogPane().setStyle("-fx-background-color: #ffd272");		
					textInputDialog.getDialogPane().setPrefWidth(600);
					textInputDialog.setResizable(true);
					
					Stage stage = (Stage) textInputDialog.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("/images/icon.png"));
					
					Optional<String> dialogResult = textInputDialog.showAndWait();
					dialogResult.ifPresent(conversationName -> {
						if (conversationName.isEmpty()) {
							
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle(MESSAGE);
							alert.setHeaderText(null);
							alert.setContentText(ANNOUNCEMENT_DESCRIPTION_EMPTY);
							alert.showAndWait();
						} else {
							description.setText(dialogResult.get());
							announcement.setDescription(dialogResult.get());
							
							try {
								provider.getAnnouncementProvider().update(announcement.getId(), announcement);
							} catch (HttpErrorCodeException | IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			});
		    HBox.setMargin(btnEditDescription, new Insets(0, 5, 0, 0));
		    btnEditDescription.setTooltip(new Tooltip(EDIT));
		    
			btnCloseAnnouncement.setMinSize(22, 22);
			btnCloseAnnouncement.setPadding(new Insets(-10));
		    btnCloseAnnouncement.setGraphic(new ImageView("/images/remove.png"));
		    btnCloseAnnouncement.getStylesheets().add(buttonStylesheet);
		    btnCloseAnnouncement.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (announcement.getUserId() != currentUserId){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle(MESSAGE);
						alert.setHeaderText(null);
						alert.setContentText(NOT_ANNOUNCEMENT_CREATOR);
						alert.showAndWait();
						return;
					}
					try {
						announcement.setIsActive(false);
						Announcement updatedAnnouncement = provider.getAnnouncementProvider().update(announcement.getId(), announcement);
						
						for (Announcement a : activeAnnouncementList) {
							if (a.getId() != announcement.getId()) continue;
							
							activeAnnouncementList.remove(a);
							break;
						}
						
						closedAnnouncementList.add(0, updatedAnnouncement);
					} catch (HttpErrorCodeException | IOException e) {
						e.printStackTrace();
					}
				}
			});
		    btnCloseAnnouncement.setTooltip(new Tooltip(CLOSE));
		    
			Date creationDate = new Date(announcement.getCreationDate());
	
			if (GlobalVariables.language == Language.ENGLISH)
				descriptionTooltipStr += description.getText() + "\nCreated: " + formatter.format(creationDate) + "\nBy: " + userThatCreatedAnnouncement; 
			else
				descriptionTooltipStr += description.getText() + "\nСтворено: " + formatter.format(creationDate) + "\nСтворив: " + userThatCreatedAnnouncement;
			
			
			descriptionTooltip.setText(descriptionTooltipStr);
			descriptionTooltip.setWrapText(true);
			descriptionTooltip.setMaxWidth(500);
			
			description.setTooltip(descriptionTooltip);
			AnchorPane.setLeftAnchor(description, 0d);
			AnchorPane.setRightAnchor(description, 50d);
			
			buttonsHBox.getChildren().addAll(btnEditDescription, btnCloseAnnouncement);
			buttonsHBox.setAlignment(Pos.CENTER);
			AnchorPane.setRightAnchor(buttonsHBox, 0d);
			
			anchorPane.getChildren().addAll(description, buttonsHBox);
			HBox.setHgrow(anchorPane, Priority.ALWAYS);
			
			mainHBox.getChildren().add(anchorPane);

			setGraphic(mainHBox);
		} else {
			setGraphic(null);	
		}
	}
}