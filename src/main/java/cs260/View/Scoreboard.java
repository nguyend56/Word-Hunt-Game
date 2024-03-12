package cs260.View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*; //text
import javafx.scene.control.*; //button label progress bar etc
import javafx.stage.Stage;

import cs260.ViewModel.MainClient;

import java.util.*;


public class Scoreboard {
    private ProgressBar pBar;
    private VBox vbox;
    private int currentScore;
    private int bonusScore;
    private int totalWordsNum;

    /**
     * makes scoreboard object
     */
    public Scoreboard() {


        this.vbox = new VBox(10);  //scene content

        currentScore = 0;
        bonusScore = 0;
    }

    public void setTotalWordsCount(int count) {
        this.totalWordsNum = count;
    }


    /**
     * @return current score of the game
     */
    public int getScore() {
        return currentScore;
    }

    /**
     * increments the current score
     */
    public void addCurrentScore(int score) {
        currentScore += score;
    }

    public void addBonusScore(int score) {
        bonusScore += score;
    }

    /**
     * @return number of bonus words
     */
    private int getBonusScore() {
        return bonusScore;
    }

    /**
     * @return total number of possible words
     */
    private int totalNumWords() {
        return totalWordsNum;
    }

    /**
     * @return a text of current score (0), level, and bonus words (0)
     */
    private Text initTextScore() {
        Text text = new Text();
        text.setText(
                "Score: 0 / " + totalNumWords() + "\n" +
                        "Bonus words: 0");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        return text;
    }

    /**
     * @return updated text with current score and bonus words
     */
    private void updateTextScore(int accuracy) {
        Text scoreText = (Text) (vbox.getChildren().get(0));
        Platform.runLater(() -> {
            scoreText.setText(
                    "Score: " + getScore() + " / " + totalNumWords() + "\n" +
                            "Bonus words: " + getBonusScore() + "\n" +
                            "Accuracy: " + String.format("%d%%", accuracy)
            );
        });
    }

    /**
     * uses update score to load changes to progress bar
     */
    private void updateProgressBar() {
        float wordPercentage = (float) getScore() / totalNumWords();
        Platform.runLater(() -> {
            pBar.setProgress(wordPercentage);
        });
    }


    public void initScoreBoard() {
        vbox.getChildren().add(initTextScore());
        this.pBar = new ProgressBar(0);
        vbox.getChildren().add(pBar);
    }


    public VBox getLayout() {
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }


    public void updateScoreBoard(int accuracy) {
        Platform.runLater(() -> {
            updateTextScore(accuracy);
            updateProgressBar();
        });
    }

    public void reset() {
        // Reset the score to initial values, e.g., 0
        // If there are other fields that track game state, they should be reset as well.
        this.currentScore = 0;
        this.bonusScore = 0;
        // ... reset other fields as needed
        // Update the display if necessary
        initScoreBoard();
    }
}

