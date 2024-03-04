package cs260.View;

import cs260.Model.WordDict;
import cs260.Model.WordGridModel;
import cs260.ViewModel.MainClient;
import cs260.ViewModel.SaveLoad;
import cs260.View.Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.*;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class WordGridView {
    private WordGridModel wordGridModel;
    private GridPane gridPane;
    private Button[][] buttons = new Button[4][4];
    private VBox layout;
    private Set<String> selectedTiles = new HashSet<>();
    private StringBuilder currentWord = new StringBuilder();
    private Button newGameButton; // Button to start a new game
    private Button saveButton = new Button("Save");
    private Button loadButton = new Button("Load");
    private SaveLoad saveLoad = new SaveLoad();
    private MainClient viewModel;
    private char[][] grid = new char[4][4];

    private boolean dragInProgress = false;


    public WordGridView() {
        this.viewModel = new MainClient(this);
        this.wordGridModel = new WordGridModel();
        this.gridPane = new GridPane();
        this.layout = new VBox(10);
        this.newGameButton = new Button("New Game");
        initializeGrid();
        setupLayout();
        setupNewGameButton();
        setupSaveLoadButtons();
    }

    private void initializeGrid() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        wordGridModel.makeGrid();
        this.grid = wordGridModel.getGrid();

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                Button tile = new Button(String.valueOf(this.grid[i][j]));
                tile.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                tile.setPrefSize(50, 50);
                gridPane.add(tile, j, i);

                int finalI = i;
                int finalJ = j;
                tile.setOnDragDetected(event -> {
                    tile.startFullDrag();
                    handleTilePressed(finalI, finalJ);
                });

                tile.setOnMouseDragEntered(this::handleMouseDragEntered);
                buttons[i][j] = tile;
            }
        }
    }

    public char[][] getGrid() {
        return this.grid;
    }

    private void setupLayout() {
        HBox buttonBox = new HBox(10); // Adjust the spacing as needed
        buttonBox.setAlignment(Pos.CENTER);

        newGameButton.setStyle("-fx-background-color: #F0E68C;");
        saveButton.setStyle("-fx-background-color: #7FFFD4;");
        loadButton.setStyle("-fx-background-color: orange;");

        // Ensure buttons take up equal space within the HBox
        newGameButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(newGameButton, Priority.ALWAYS);
        HBox.setHgrow(saveButton, Priority.ALWAYS);
        HBox.setHgrow(loadButton, Priority.ALWAYS);

        buttonBox.getChildren().addAll(newGameButton, saveButton, loadButton);

        VBox.setVgrow(gridPane, Priority.ALWAYS);

        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.getChildren().addAll(gridPane, buttonBox);
        gridPane.prefWidthProperty().bind(layout.widthProperty());
        gridPane.prefHeightProperty().bind(layout.heightProperty().subtract(50)); // Example static value

    }

    private void handleTilePressed(int row, int col) {
        dragInProgress = true;
        selectedTiles.clear();
        currentWord.setLength(0);
        selectTile(row, col);
    }

    private void handleMouseDragEntered(MouseDragEvent event) {
        dragInProgress = true;
        Button tile = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(tile);
        Integer col = GridPane.getColumnIndex(tile);
        selectTile(row, col);
    }

    public void dragFinished() {
        dragInProgress = false;
    }

    public boolean isDragInProgress() {
        return dragInProgress;
    }


    private void selectTile(int row, int col) {
        String tileId = row + ":" + col;
        if (!selectedTiles.contains(tileId)) {
            selectedTiles.add(tileId);
            Button tile = buttons[row][col];
            currentWord.append(tile.getText());
        }
    }

    public VBox getLayout() {
        return layout;
    }

    public String getCurrentWord() {
        return currentWord.toString();
    }

    public void updateGrid(char[][] newGrid) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText(String.valueOf(newGrid[i][j]));
            }
        }
    }

    public void promptNewGrid() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Game");
        dialog.setHeaderText("Enter a new grid (16 letters):");
        dialog.setContentText("Grid:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::processNewGridInput);
    }

    private void processNewGridInput(String input) {
        if (input.length() == 16) {
            try {
                wordGridModel.setGridFromInput(input); // Set the new grid based on input
                updateGrid(wordGridModel.getGrid()); // Update the view with the new grid
            } catch (IllegalArgumentException e) {
                // If input is invalid, re-prompt the user or show an error message
                promptNewGrid(); // Recursive call to re-prompt
            }
        } else {
            // If input is not 16 characters, show an error and re-prompt
            promptNewGrid(); // Recursive call to re-prompt
        }
    }

    // You might want to implement this method to reset the styles of the tiles
    public void resetTileStyles() {
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                // Reset the button style to the initial state if you had changed it on tile selection
                // button.setStyle("-fx-background-color: transparent;");
            }
        }
    }

    private void setupNewGameButton() {
        newGameButton.setOnAction(e -> openNewGameWindow());
        newGameButton.setAlignment(Pos.CENTER);
        viewModel.resetGame();
    }

    private void openNewGameWindow() {
        Stage newGameStage = new Stage();
        newGameStage.initModality(Modality.APPLICATION_MODAL);
        newGameStage.setTitle("New Game Setup");

        GridPane newGameGrid = new GridPane();
        newGameGrid.setAlignment(Pos.CENTER);
        newGameGrid.setPadding(new Insets(10));
        newGameGrid.setHgap(10);
        newGameGrid.setVgap(10);

        Button[][] newGameButtons = new Button[4][4];
        char[][] newGrid = new char[4][4];
        for (int i = 0; i < newGameButtons.length; i++) {
            for (int j = 0; j < newGameButtons[i].length; j++) {
                Button tile = new Button();
                tile.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                tile.setPrefSize(50, 50);
                final int fi = i;
                final int fj = j;
                tile.setOnAction(event -> promptForTileLetter(newGameButtons, fi, fj, newGameStage, newGrid));
                newGameGrid.add(tile, j, i);
                newGameButtons[i][j] = tile;
            }
        }
        this.grid = newGrid;
        Scene scene = new Scene(newGameGrid, 300, 300);
        newGameStage.setScene(scene);
        newGameStage.showAndWait();
        (new Main()).resetGame(newGameStage, this); // Show the new game setup window and wait for it to close
    }

    private int promptForGridDimension() {
        TextInputDialog dimensionInput = new TextInputDialog("4");
        dimensionInput.setTitle("Grid Dimension");
        dimensionInput.setHeaderText("Enter the dimension for the grid (e.g., 4 for a 4x4 grid):");
        dimensionInput.setContentText("Dimension:");

        Optional<String> result = dimensionInput.showAndWait();
        try {
            return result.map(Integer::parseInt).filter(dim -> dim > 0).orElse(4); // Default to 4 if input is invalid
        } catch (NumberFormatException e) {
            return 4; // Default to 4 if input is not a number
        }
    }


    private void promptForTileLetter(Button[][] buttons, int i, int j, Stage stage, char[][] newGrid) {
        TextInputDialog letterInput = new TextInputDialog();
        letterInput.setTitle("Input Letter");
        letterInput.setHeaderText(null);
        letterInput.setContentText("Enter letter for tile (" + (i+1) + "," + (j+1) + "):");

        Optional<String> result = letterInput.showAndWait();
        result.ifPresent(letter -> {
            if (letter.length() == 1 && Character.isLetter(letter.charAt(0))) {
                buttons[i][j].setText(letter.toUpperCase()); // Update button text
                newGrid[i][j] = letter.toUpperCase().charAt(0); // Update newGrid with the letter

                // Check if the entire grid is filled and then proceed
                if (checkIfGridIsFull(buttons)) {
                    // Assuming you have a method to update the model and refresh the view
                    updateModelAndView(newGrid);
                    stage.close(); // Close the window once the grid is fully populated
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Please enter a single letter.");
                alert.showAndWait();
            }
        });
    }

    // Call this method after newGameStage.showAndWait() in openNewGameWindow()
    private void updateModelAndView(char[][] newGrid) {
        wordGridModel.setGrid(newGrid); // Update the model
        updateGrid(newGrid); // Refresh the view to reflect the new grid
    }


    private boolean checkIfGridIsFull(Button[][] buttons) {
        for (Button[] row : buttons) {
            for (Button tile : row) {
                if (tile.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private char[][] buttonsToGrid(Button[][] buttons) {
        char[][] grid = new char[4][4];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                grid[i][j] = buttons[i][j].getText().charAt(0);
            }
        }
        return grid;
    }

    private void setupSaveLoadButtons() {
        // Assuming you have a WordDict instance named wordDict in your viewModel or elsewhere

        saveButton.setOnAction(e -> {
            try {
                // Assuming "gameState.json" as the filename. Adjust path as needed.
                saveLoad.save(wordGridModel, viewModel.getWordDict(), "gameState.json");
                System.out.println("Game saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                // Optionally, show an alert dialog to the user
                new Alert(AlertType.ERROR, "Failed to save the game.").show();
            }
        });

        loadButton.setOnAction(e -> {
            try {
                SaveLoad.GameState loadedState = saveLoad.load("gameState.json");
                wordGridModel.setGrid(loadedState.grid.getGrid());
                viewModel.setWordDict(loadedState.dictionary);
                updateGrid(wordGridModel.getGrid());
                System.out.println("Game loaded successfully.");
                // You may need to update the UI or internal state as necessary
            } catch (IOException ex) {
                ex.printStackTrace();
                // Optionally, show an alert dialog to the user
                new Alert(AlertType.ERROR, "Failed to load the game.").show();
            }
        });
    }
}

