package com.elekscamp.messenger_javafx_client.ui.handlers;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ChatLabelsHandler {

	public Label getAttachmentNameLabel(Tooltip attachmentTooltip) {
		Label lbAttachmentName = new Label();
		
		lbAttachmentName.setTextFill(Color.web("red"));
		lbAttachmentName.setTooltip(attachmentTooltip);
		
		AnchorPane.setLeftAnchor(lbAttachmentName, 10d);
		AnchorPane.setRightAnchor(lbAttachmentName, 30d);
		
		return lbAttachmentName;
	}
}
