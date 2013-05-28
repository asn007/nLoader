package eu.q_b.asn007.nloader.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalWindow {

	public ModalWindow(String title, String text) {
		final Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		
		Scene scene = new Scene(new VBox());
		scene.getStylesheets().add("/metro.css");
		Label titleLabel = new Label(title);
		titleLabel.getStyleClass().add("header");
		
		HBox hb1 = new HBox();
		hb1.setPrefHeight(15);
		
		HBox hb0 = new HBox();
		hb0.setPrefHeight(30);
		
		HBox hb2 = new HBox();
		hb2.setPrefHeight(15);
		
		HBox hb3 = new HBox();
		hb3.setPrefHeight(30);
		
		Button OK = new Button("OK");
		OK.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				dialogStage.close();
			}
			
		});
		((VBox)scene.getRoot()).setPadding(new Insets(10));
		((VBox)scene.getRoot()).setAlignment(Pos.CENTER);
		((VBox)scene.getRoot()).getChildren().add(hb0);
		((VBox)scene.getRoot()).getChildren().add(titleLabel);
		((VBox)scene.getRoot()).getChildren().add(hb1);
		((VBox)scene.getRoot()).getChildren().add(new Label(text));
		((VBox)scene.getRoot()).getChildren().add(hb2);
		((VBox)scene.getRoot()).getChildren().add(OK);
		((VBox)scene.getRoot()).getChildren().add(hb3);
		
		dialogStage.setScene(scene);
		dialogStage.showAndWait();
	}

}
