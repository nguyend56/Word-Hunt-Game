package cs260.ViewModel;


import cs260.Model.*;
import cs260.View.Scoreboard;
import cs260.View.WordGridView;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.*;


/**
 * Main client class that initializes and manages the game components including
 * dictionary, word grid model, scoreboard, and word grid view.
 */
public class MainClient {

    private AccuracyScore accuracyScore;
    private TotalWordScore totalWordScore;
    private WordLengthScore wordLengthScore;

    private WordGridModel wordGridModel;
    private Scoreboard scoreboard;
    private WordGridView wordGridView;
    private SaveLoad saveLoad;
    private emojiParser emojiParser;


    /**
     * Constructs the MainClient, initializing all the game components.
     */
    public MainClient(Scoreboard scoreboard, WordGridView wordGridView) {
        this.wordGridModel = new WordGridModel(wordGridView.getGrid());
        this.scoreboard = scoreboard;
        this.wordGridView = wordGridView;
        this.saveLoad = new SaveLoad();
        this.totalWordScore = new TotalWordScore(this.generateGridArray());
        this.accuracyScore = new AccuracyScore(this.generateGridArray());
        this.wordLengthScore = new WordLengthScore(this.generateGridArray());
        this.emojiParser = new emojiParser();
        this.scoreboard.setTotalWordsCount(this.totalWordScore.getNumWordsInGrid());
        this.scoreboard.initScoreBoard();
        this.wordGridView.setLetterWordCount(this.getLetterWordCount());
    }

    public MainClient(WordGridView wordGridView) {
        this.wordGridModel = new WordGridModel(wordGridView.getGrid());
        this.scoreboard = new Scoreboard();
        this.wordGridView = wordGridView;
        this.saveLoad = new SaveLoad();
        this.totalWordScore = new TotalWordScore(this.generateGridArray());
        this.accuracyScore = new AccuracyScore(this.generateGridArray());
        this.wordLengthScore = new WordLengthScore(this.generateGridArray());
        this.emojiParser = new emojiParser();
        this.scoreboard.setTotalWordsCount(this.totalWordScore.getNumWordsInGrid());
        this.scoreboard.initScoreBoard();
        this.wordGridView.setLetterWordCount(this.getLetterWordCount());
    }

    public void resetGame() {
        this.scoreboard.reset();
    }

    /**
     * Updates the scoreboard based on the current game state.
     */
    public void updateScoreboard(boolean bonus, int score) {
        if (bonus) this.scoreboard.addBonusScore(score);
        else this.scoreboard.addCurrentScore(score); 
        this.scoreboard.updateScoreBoard(this.accuracyScore.getAccuracy());
        this.accuracyScore.resetNumAttempts();
    }

    /**
     * Saves the current game state to a specified file.
     *
     * @param filename TselectedWordhe name of the file to save the game state to.
     */
    public void saveGameState(String filename) throws IOException {
        saveLoad.save(wordGridModel, totalWordScore, filename);
    }


    /**
     * Loads the game state from a specified file.
     *
     * @param filename The name of the file from which to load the game state.
     */
    public void loadGameState(String filename) throws IOException {
        SaveLoad.GameState loadedState = saveLoad.load(filename);
        if (loadedState != null) {
            this.wordGridModel = loadedState.grid;
            this.totalWordScore = loadedState.dictionary;
            updateUIFromLoadedState(loadedState);
        } else {
            System.err.println("Failed to load game state from file: " + filename);
        }
    }


    /**
     * Updates the UI components based on the loaded game state.
     *
     * @param loadedState The loaded game state.
     */
    private void updateUIFromLoadedState(SaveLoad.GameState loadedState) {
        wordGridView.updateGrid(loadedState.grid.getGrid());
        this.updateScoreboard(false, 0);
    }


    /**
     * Returns the current instance of the scoreboard.
     *
     * @return The scoreboard.
     */
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }


    /**
     * Returns the current instance of the word grid view.
     *
     * @return The word grid view.
     */
    public WordGridView getWordGridView() {
        return wordGridView;
    }


    /**
     * Constructs and returns the primary view of the game.
     *
     * @return The primary game view as a Parent object.
     */
    public Parent getPrimaryView() {
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(this.scoreboard.getLayout(), wordGridView.getLayout());
        return mainLayout;
    }

    public ArrayList<ArrayList<Integer>> getLetterWordCount() {
        return this.totalWordScore.getLetterWordCount();
    }


    /**
     * Generates a grid of letters for the game.
     *
     * @return A 2D array of characters representing the grid.
     */
    public ArrayList<ArrayList<Character>> generateGridArray() {
        char[][] board = wordGridView.getGrid();

        ArrayList<ArrayList<Character>> board2 = new ArrayList<>();
        for (char[] row : board) {
            ArrayList<Character> temp = new ArrayList<>();
            for (char c : row) {
                temp.add(c);
            }
            board2.add(temp);
        }
        return board2;
    }

    public boolean showEmoji(String word){
        if(emojiParser.findEmoji(word) != null){
            return true;
        }

        return false;
    }


    /**
     * Processes a selected word, updating the scoreboard if the word is valid.
     *
     * @param word The word to process.
     */
    public void processSelectedWord(String word) {
        this.accuracyScore.wordAttempted(word.toLowerCase());
        boolean x = this.totalWordScore.regularWordCheck(word.toLowerCase());
        
        System.out.println("Selected Word Valid? " + word + " : " + x);

        if (x) {
            this.updateScoreboard(false, this.wordLengthScore.getWordScore(word.toLowerCase()));
            this.wordGridView.updateLetterWordCount();
        } else if (this.totalWordScore.bonusWordCheck(word.toLowerCase())) {
            this.updateScoreboard(true, 1);
        }
    }
}
