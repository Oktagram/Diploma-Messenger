package com.elekscamp.messenger_javafx_client.services;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Computer {
	public double computePosXForSmilesPopup(Bounds btnSmilesBounds, Bounds localToSceneBounds, Scene btnSmilesScene, Button btnSmiles, HBox smilesHBox) {
		return btnSmilesBounds.getMinX() 
				+ localToSceneBounds.getMinX() 
				+ btnSmilesScene.getX()
				+ btnSmilesScene.getWindow().getX() 
				+ btnSmiles.getBoundsInParent().getWidth()
				- smilesHBox.getPrefWidth();
	}
	
	public double computerPosYForSmilesPopup(Bounds btnSmilesBounds, Bounds localToSceneBounds, Scene btnSmilesScene, HBox smilesHBox) {
		return btnSmilesBounds.getMinY() 
				+ localToSceneBounds.getHeight() 
				+ localToSceneBounds.getMinY()
				+ btnSmilesScene.getWindow().getY() 
				- smilesHBox.getPrefHeight();
	}
}
