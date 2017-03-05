package com.elekscamp.messenger_javafx_client.ui.list_cells;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

public class ClosedAnnouncementsListCell extends ListCell<Announcement> {
	private HBox mainHBox;
	private ContentProvider provider;
	private SimpleDateFormat formatter;
	private ObservableList<Announcement> activeAnnouncementList;
	private ObservableList<Announcement> closedAnnouncementList;
	private int userId;
	
	public void initData(int userId, ObservableList<Announcement> activeAnnouncementList, ObservableList<Announcement> closedAnnouncementList) {
		this.activeAnnouncementList = activeAnnouncementList;
		this.closedAnnouncementList = closedAnnouncementList;
		this.userId = userId;
	}
	
	public ClosedAnnouncementsListCell(ContentProvider provider) {
		this.provider = provider;
		formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
	}
	
	@Override 
	protected void updateItem(Announcement announcement, boolean empty) {

		super.updateItem(announcement, empty);

		if (announcement != null) {

			mainHBox = new HBox();
			AnchorPane anchorPane = new AnchorPane();
			Label description = new Label(announcement.getDescription());
			Button btnOpenAnnouncement = new Button();
			Tooltip descriptionTooltip = new Tooltip(description.getText());
			String descriptionTooltipStr = "";
			String userThatCreatedAnnouncement = "";
			
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
			
			Date creationDate = new Date(announcement.getCreationDate());
			Date closingDate = new Date(announcement.getClosingDate());
			
			if (GlobalVariables.language == Language.ENGLISH)
				descriptionTooltipStr += description.getText() + "\nCreated: " + formatter.format(creationDate) 
						+ "\nBy: " + userThatCreatedAnnouncement + "\nClosed: " + formatter.format(closingDate);
			else
				descriptionTooltipStr += description.getText() + "\nСтворено: " + formatter.format(creationDate) 
				+ "\nСтворив: " + userThatCreatedAnnouncement + "\nЗакрито: " + formatter.format(closingDate);
	
			descriptionTooltip.setText(descriptionTooltipStr);
			descriptionTooltip.setWrapText(true);
			descriptionTooltip.setMaxWidth(500);
			
			description.setTooltip(descriptionTooltip);
			
			btnOpenAnnouncement.setGraphic(new ImageView("/images/restore-announcement.png"));
			btnOpenAnnouncement.getStylesheets().add(getClass().getResource("/css/buttons/with-image.css").toExternalForm());
			btnOpenAnnouncement.setMinSize(22, 22);
			btnOpenAnnouncement.setPadding(new Insets(-10));
			btnOpenAnnouncement.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						announcement.setIsActive(true);
						announcement.setUserId(userId);
						Announcement updatedAnnouncement = provider.getAnnouncementProvider().update(announcement.getId(), announcement);
						
						for (Announcement a : closedAnnouncementList) {
							if (a.getId() != announcement.getId()) continue;
							
							closedAnnouncementList.remove(a);
							break;
						}
						
						activeAnnouncementList.add(0, updatedAnnouncement);
					} catch (HttpErrorCodeException | IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			if (GlobalVariables.language == Language.ENGLISH)
				btnOpenAnnouncement.setTooltip(new Tooltip("Restore"));
			else
				btnOpenAnnouncement.setTooltip(new Tooltip("Відновити"));
			
			anchorPane.getChildren().addAll(description, btnOpenAnnouncement);

			AnchorPane.setRightAnchor(btnOpenAnnouncement, 0d);
			AnchorPane.setLeftAnchor(description, 0d);
			AnchorPane.setRightAnchor(description, 30d);
			
			mainHBox.getChildren().add(anchorPane);

			HBox.setHgrow(anchorPane, Priority.ALWAYS);

			setGraphic(mainHBox);
		} else {
			setGraphic(null);	
		}
	}
}
