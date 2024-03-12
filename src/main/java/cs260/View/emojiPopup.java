package cs260.View;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
//import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class emojiPopup {
    
    public static void showPopup(Stage startingStage, String emoji){

        Stage toastStage = new Stage();
        toastStage.initOwner(startingStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 100px;");

        StackPane root =  new StackPane(emojiLabel);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20px;");
        Scene sc = new Scene(root); 
        sc.setFill(null);
        toastStage.setScene(sc);

        toastStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> toastStage.close());
        delay.play();

    }
}