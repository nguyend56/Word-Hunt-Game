package cs260.ViewModel;


import cs260.Model.WordDict;
import cs260.Model.WordGridModel;
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
    private WordDict dictionary;
    private WordDict wordDict;

    private WordGridModel wordGridModel;
    private Scoreboard scoreboard;
    private WordGridView wordGridView;
    private SaveLoad saveLoad;


    /**
     * Constructs the MainClient, initializing all the game components.
     */
    public MainClient(Scoreboard scoreboard, WordGridView wordGridView) {
        this.wordGridModel = new WordGridModel();
        this.scoreboard = scoreboard;
        this.wordGridView = wordGridView;
        this.saveLoad = new SaveLoad();
        this.dictionary = new WordDict(this.generateGridArray());

        this.scoreboard.setTotalWordsCount(this.dictionary.getAllWordsCount());
        this.scoreboard.initScoreBoard();

    }

    public MainClient(WordGridView wordGridView) {
        this.wordGridModel = new WordGridModel();
        this.scoreboard = new Scoreboard();
        this.wordGridView = wordGridView;
        this.saveLoad = new SaveLoad();
        this.dictionary = new WordDict(this.generateGridArray());

        this.scoreboard.setTotalWordsCount(this.dictionary.getAllWordsCount());
        this.scoreboard.initScoreBoard();
    }

    public void resetGame() {
        this.scoreboard.reset();
    }

    /**
     * Updates the scoreboard based on the current game state.
     */
    public void updateScoreboard(boolean bonus) {
        if (bonus) this.scoreboard.addBonusScore();
        else this.scoreboard.addCurrentScore(); 
        this.scoreboard.updateScoreBoard();
    }

    // Getter for wordDict
    public WordDict getWordDict() {
        return wordDict;
    }

    // Setter for wordDict
    public void setWordDict(WordDict wordDict) {
        this.wordDict = wordDict;
    }

    /**
     * Saves the current game state to a specified file.
     *
     * @param filename The name of the file to save the game state to.
     */
    public void saveGameState(String filename) throws IOException {
        saveLoad.save(wordGridModel, dictionary, filename);
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
            this.dictionary = loadedState.dictionary;
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
        this.updateScoreboard(false);
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
     * Processes the selection of tiles, updating the scoreboard if a valid word is selected.
     */
    public void tilesSelected() {
        String word = wordGridView.getCurrentWord();
        if (dictionary.checkWord(word)) {
            this.updateScoreboard(false);
            // wordGridView.resetCurrentWord();
        }
        else if (dictionary.checkBonusWord(word)) {
            this.updateScoreboard(true);
        }
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


    /**
     * Generates a grid of letters for the game.
     *
     * @return A 2D array of characters representing the grid.
     */
    public ArrayList<ArrayList<Character>> generateGridArray() {
        char[][] board = wordGridView.getGrid();
        System.out.println(Arrays.deepToString(board));

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


    /**
     * Processes a selected word, updating the scoreboard if the word is valid.
     *
     * @param word The word to process.
     */
    public void processSelectedWord(String word) {
        boolean x = this.dictionary.checkWord(word.toLowerCase());
        System.out.println("Selected Word Valid? " + word + " : " + x);

        if (x) {
            this.updateScoreboard(false);
        } else if (this.dictionary.checkBonusWord(word.toLowerCase())) {
            this.updateScoreboard(true);
        }
    }
}
