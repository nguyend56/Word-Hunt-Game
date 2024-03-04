package cs260.ViewModel;

import cs260.Model.WordDict;
import cs260.Model.WordGridModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Handles saving and loading the game state to and from a file in JSON format.
 */
public class SaveLoad {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the current game state to a specified file in JSON format.
     *
     * @param grid The current state of the word grid model.
     * @param dictionary The current state of the word dictionary.
     * @param filename The name of the file to save the game state to.
     * @throws IOException If an I/O error occurs during saving.
     */
    public void save(WordGridModel grid, WordDict dictionary, String filename) throws IOException {
        // Creating a wrapper object to hold both grid and dictionary
        GameState gameState = new GameState(grid, dictionary);
        objectMapper.writeValue(new File(filename), gameState);
    }

    /**
     * Loads the game state from a specified JSON file.
     *
     * @param filename The name of the file from which to load the game state.
     * @return The loaded game state as a {@link GameState} object.
     * @throws IOException If an I/O error occurs during loading.
     */
    public GameState load(String filename) throws IOException {
        return objectMapper.readValue(new File(filename), GameState.class);
    }

    /**
     * Inner class to hold the game state for serialization and deserialization.
     */
    public static class GameState {
        public WordGridModel grid;
        public WordDict dictionary;

        /**
         * Default constructor for JSON deserialization.
         */
        public GameState() {}

        /**
         * Constructs a GameState object with specified word grid model and dictionary.
         *
         * @param grid The word grid model.
         * @param dictionary The word dictionary.
         */
        public GameState(WordGridModel grid, WordDict dictionary) {
            this.grid = grid;
            this.dictionary = dictionary;
        }
    }

    /**
     * Placeholder method for updating the score. Implement this method based on game logic.
     *
     * @return Always returns true as a placeholder.
     */
    public boolean updateScore(){
        return true;
    }
}

