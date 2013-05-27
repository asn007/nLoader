package eu.q_b.asn007.nloader.fx;

import eu.q_b.asn007.nloader.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

public class SceneUtils {

	public static void changeScene(int duration, Node first, final Node second) {
		duration = (int)duration / 2;
		final DoubleProperty opacity = first.opacityProperty();
		Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)), new KeyFrame(new Duration(duration), new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				DoubleProperty opa = second.opacityProperty();
				second.setOpacity(0);
				Main._instance.primaryStage.setScene(new Scene((Parent)second));
				Timeline fadeIn = new Timeline(
	                      new KeyFrame(Duration.ZERO,
	                             new KeyValue(opa, 0.0)),
	                      new KeyFrame(new Duration(800),
	                             new KeyValue(opa, 1.0)));
	            fadeIn.play();
			}			
		}, new KeyValue(opacity, 0.0)));
		fade.play();
	}
	
}
