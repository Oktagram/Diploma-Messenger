package com.elekscamp.messenger_javafx_client.UI;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.elekscamp.messenger_javafx_client.DAL.ContentProvider;
import com.elekscamp.messenger_javafx_client.Entities.Announcement;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

public class ClosedAnnouncementsListCell extends ListCell<Announcement> {
	private HBox mainHBox;
	private ContentProvider provider;
	private SimpleDateFormat formatter;
	
	public ClosedAnnouncementsListCell(ContentProvider provider) {
		
		this.provider = provider;
		formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
		mainHBox = new HBox();
	}
	
	@Override protected void updateItem(Announcement announcement, boolean empty) {

		super.updateItem(announcement, empty);

		if (announcement != null) {

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
			
			descriptionTooltipStr += description.getText() + "\nCreated: " + formatter.format(creationDate) + "\nBy: " + userThatCreatedAnnouncement
					+ "\nClosed: " + formatter.format(closingDate); 
	
			descriptionTooltip.setText(descriptionTooltipStr);
			descriptionTooltip.setWrapText(true);
			descriptionTooltip.setMaxWidth(500);
			
			description.setTooltip(descriptionTooltip);
			
			btnOpenAnnouncement.setStyle("-fx-font-size: 12px;");
			btnOpenAnnouncement.setMinSize(26, 26);
			
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
