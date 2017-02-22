package com.elekscamp.messenger_javafx_client.ui.Chat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.control.TabPane;

public class ChatObjectsHandler {

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
