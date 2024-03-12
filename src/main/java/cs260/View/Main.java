package cs260.View;

import cs260.Model.emojiParser;
import cs260.ViewModel.MainClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public void resetGame(Stage primaryStage, WordGridView wordGridView) {
        Scoreboard scoreBoard = new Scoreboard();

        emojiParser ep = new emojiParser();

        wordGridView.reset();

        MainClient viewModel = new MainClient(scoreBoard, wordGridView);
        wordGridView.updateWordStartCountsOnTiles();

        // Create a layout and add WordGridView and Scoreboard to it
        VBox layout = new VBox(10); // Added spacing between children
        layout.getChildren().addAll(wordGridView.getLayout(), scoreBoard.getLayout());
        Scene scene = new Scene(layout, 450, 450); // Adjusted for visibility

        primaryStage.setTitle("Word Hunt Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Adding an event filter for mouse released to handle the end of the swipe
        scene.addEventFilter(MouseDragEvent.MOUSE_RELEASED, event -> {
            if (wordGridView.isDragInProgress()) {
                String selectedWord = wordGridView.getCurrentWord();
                viewModel.processSelectedWord(selectedWord);
                if (viewModel.showEmoji(selectedWord)){
                    String emoji = ep.findEmoji(selectedWord);
                    emojiPopup.showPopup(primaryStage, emoji);
                }
                wordGridView.dragFinished(); // Reset the flag
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        Scoreboard scoreBoard = new Scoreboard();
        emojiParser ep = new emojiParser();


        WordGridView wordGridView = new WordGridView();

        MainClient viewModel = new MainClient(scoreBoard, wordGridView);

        wordGridView.updateWordStartCountsOnTiles();

        // Create a layout and add WordGridView and Scoreboard to it
        VBox layout = new VBox(10); // Added spacing between children
        layout.getChildren().addAll(wordGridView.getLayout(), scoreBoard.getLayout());
        Scene scene = new Scene(layout, 450, 450); // Adjusted for visibility

        primaryStage.setTitle("Word Hunt Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Adding an event filter for mouse released to handle the end of the swipe
        scene.addEventFilter(MouseDragEvent.MOUSE_RELEASED, event -> {
            if (wordGridView.isDragInProgress()) {
                String selectedWord = wordGridView.getCurrentWord();
                viewModel.processSelectedWord(selectedWord);
                if (viewModel.showEmoji(selectedWord)){
                    String emoji = ep.findEmoji(selectedWord);
                    emojiPopup.showPopup(primaryStage, emoji);
                }
                wordGridView.dragFinished(); // Reset the flag
            }
        });

        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
