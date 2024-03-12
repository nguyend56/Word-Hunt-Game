package cs260.View;

import cs260.Model.WordGridModel;
import cs260.ViewModel.MainClient;
import cs260.ViewModel.SaveLoad;
import cs260.View.Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.*;
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
    private Button[][] buttons;
    private VBox layout;
    private Set<String> selectedTiles = new HashSet<>();
    private StringBuilder currentWord = new StringBuilder();
    private Button newGameButton; // Button to start a new game
    private Button saveButton = new Button("Save");
    private Button loadButton = new Button("Load");
    private SaveLoad saveLoad = new SaveLoad();
    private MainClient viewModel;
    private char[][] grid;
    private ArrayList<ArrayList<Integer>> wordStartCounts = new ArrayList<>();

    private boolean dragInProgress = false;

    public WordGridView() {
        this.grid = new char[WordGridModel.DEFAULT_GRID_SIZE][WordGridModel.DEFAULT_GRID_SIZE];
        this.buttons = new Button[WordGridModel.DEFAULT_GRID_SIZE][WordGridModel.DEFAULT_GRID_SIZE];
        this.viewModel = new MainClient(this);
        this.wordGridModel = new WordGridModel();
        this.gridPane = new GridPane();
        this.layout = new VBox(10);
        this.newGameButton = new Button("New Game");
        initializeGrid();
        setUpButtons();
        setupLayout();
        setupNewGameButton();
        setupSaveLoadButtons();
    }

    public WordGridView(char[][] grid, Button[][] buttons) {
        this.grid = grid;
        this.buttons = buttons;
        this.viewModel = new MainClient(this);
        this.wordGridModel = new WordGridModel(grid);
        this.gridPane = new GridPane();
        this.layout = new VBox(10);
        this.newGameButton = new Button("New Game");
        initializeGridNoFill();
        setUpButtons();
        setupLayout();
        setupNewGameButton();
        setupSaveLoadButtons();
    }

    private void initializeGridNoFill() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
//        wordGridModel.makeGrid();
        this.grid = wordGridModel.getGrid();
    }

    private void initializeGrid() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        wordGridModel.makeGrid();
        this.grid = wordGridModel.getGrid();
    }

    public void setLetterWordCount(ArrayList<ArrayList<Integer>> count){
        this.wordStartCounts = count;
    }

    private void setUpButtons(){
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

    public void updateWordStartCountsOnTiles() {
        for (int i = 0; i < wordStartCounts.size(); i++) {
            for (int j = 0; j < wordStartCounts.get(i).size(); j++) {
                int count = wordStartCounts.get(i).get(j);
                Button tile = buttons[i][j]; // Access the existing button

                // Create or update a label for the count
                Label countLabel = new Label(String.valueOf(count));
                countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10)); // Smaller font for the count
                StackPane.setAlignment(countLabel, Pos.TOP_LEFT); // Position the label at the top-left corner
                countLabel.setPadding(new Insets(5)); // Adjust padding to position the count inside the tile

                // Check if the tile's parent is already a StackPane (from a previous update)
                // If so, update the count label; if not, create a new StackPane
                if (tile.getParent() instanceof StackPane) {
                    StackPane stackPane = (StackPane) tile.getParent();
                    // Find the existing count label and update it, or add a new one
                    boolean labelFound = false;
                    for (Node node : stackPane.getChildren()) {
                        if (node instanceof Label) {
                            Label existingLabel = (Label) node;
                            existingLabel.setText(String.valueOf(count));
                            labelFound = true;
                            break;
                        }
                    }
                    if (!labelFound) {
                        stackPane.getChildren().add(countLabel);
                    }
                } else {
                    // This case should technically not happen if setUpButtons is correctly implemented,
                    // but let's handle it just in case
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(tile, countLabel);
                    gridPane.add(stackPane, j, i); // Add the StackPane to the grid at the correct position
                }
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

    public void reset() {
        this.wordGridModel = new WordGridModel(grid);
        layout.getChildren().clear();
        setUpButtons();
        setupLayout();
        setupNewGameButton();
        setupSaveLoadButtons();
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

    public void updateLetterWordCount() {
        String[] idx = this.selectedTiles.iterator().next().split(":");
        int row = Integer.parseInt(idx[0]);
        int col = Integer.parseInt(idx[1]);
        int value = this.wordStartCounts.get(row).get(col);
        System.out.println("row: " + row + ", col: " + col + ", value: " + value);
        assert value >= 0;
        this.wordStartCounts.get(row).set(col, value-1);
        this.updateWordStartCountsOnTiles();
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

    private void setupNewGameButton() {
        newGameButton.setOnAction(e -> openNewGameWindow());
        newGameButton.setAlignment(Pos.CENTER);
//        viewModel.resetGame();
    }

    private void openNewGameWindow() {
        int gridSize = getUserInputForGridSize();
        Stage newGameStage = new Stage();
        newGameStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
        newGameStage.setTitle("New Game Setup");

        GridPane newGameGrid = new GridPane();
        newGameGrid.setAlignment(Pos.CENTER);
        newGameGrid.setPadding(new Insets(10));
        newGameGrid.setHgap(10);
        newGameGrid.setVgap(10);

        Button[][] newGameButtons = new Button[gridSize][gridSize];
        char[][] newGrid = new char[gridSize][gridSize];
        WordGridView newWordGridView = new WordGridView(newGrid, newGameButtons);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Button tile = new Button();
                tile.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                double buttonSize = 50; // Set a default button size
                if (gridSize > 10) {
                    buttonSize = 35; // Adjust button size for larger grids
                } else if (gridSize > 15) {
                    buttonSize = 25; // Adjust button size for even larger grids
                }
                tile.setPrefSize(buttonSize, buttonSize);
                final int fi = i;
                final int fj = j;
                tile.setOnAction(event -> promptForTileLetter(newGameButtons, fi, fj, newGameStage, newGrid));
                newGameGrid.add(tile, j, i);
                newGameButtons[i][j] = tile;
            }
        }

        double sceneWidth = 150 + (gridSize * (100 + 10)); // Calculate scene width
        double sceneHeight = 150 + (gridSize * (100 + 10)); // Calculate scene height

        Scene scene = new Scene(newGameGrid, sceneWidth, sceneHeight); // Use calculated scene size
        newGameStage.setScene(scene);
        newGameStage.showAndWait();
        (new Main()).resetGame(newGameStage, new WordGridView(newGrid, newGameButtons)); // Show the new game setup window and wait for it to close
    }

    private void promptForTileLetter(Button[][] buttons, int i, int j, Stage stage, char[][] newGrid) {
        TextInputDialog letterInput = new TextInputDialog();
        letterInput.setTitle("Input Letter");
        letterInput.setHeaderText(null);
        letterInput.setContentText("Enter letter for tile (" + (i+1) + "," + (j+1) + "):");

        // Calculate the dialog's position based on the grid's size
        double dialogX = stage.getX() + gridPane.getLayoutX() + gridPane.getWidth() / 2 - 125;
        double dialogY = stage.getY() + gridPane.getLayoutY() + gridPane.getHeight() / 2 - 125;
        letterInput.initOwner(stage);
        letterInput.setX(dialogX);
        letterInput.setY(dialogY);

        Optional<String> result = letterInput.showAndWait();
        result.ifPresent(letter -> {
            if (letter.length() == 1 && Character.isLetter(letter.charAt(0))) {
                buttons[i][j].setText(letter.toUpperCase()); // Update button text
                newGrid[i][j] = letter.toUpperCase().charAt(0); // Update newGrid with the letter
//                newWordGridView.updateWordStartCountsOnTiles(); // Update tile labels with word start counts
                // Check if the entire grid is filled and then proceed
                if (checkIfGridIsFull(buttons)) {
                    updateModelAndView(newGrid, buttons);
                    stage.close(); // Close the window once the grid is fully populated
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Please enter a single letter.");
                alert.showAndWait();
            }
        });
    }

    public static int getUserInputForGridSize() {
        // Create a dialog to ask for grid size
        TextInputDialog gridSizeDialog = new TextInputDialog("4");
        gridSizeDialog.setTitle("Grid Size");
        gridSizeDialog.setHeaderText("Enter the size for the new game grid:");
        gridSizeDialog.setContentText("Size:");

        Optional<String> result = gridSizeDialog.showAndWait();
        if (result.isPresent()) {
            try {
                int size = Integer.parseInt(result.get());
                if (size > 0) {
                    return size; // Return the user provided size
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return WordGridModel.DEFAULT_GRID_SIZE; // Default size or handle the error accordingly
    }


    // Call this method after newGameStage.showAndWait() in openNewGameWindow()
    private void updateModelAndView(char[][] newGrid, Button[][] newGameButtons) {
        wordGridModel.setGrid(newGrid); // Update the model
        buttons = newGameButtons;
        viewModel = new MainClient(this);
        updateGrid(newGrid); // Refresh the view to reflect the new grid
    }


    private boolean checkIfGridIsFull(Button[][] buttons) {
        for (Button[] row : buttons) {
            for (Button tile : row) {
                try {
                    if (!Character.isLetter(tile.getText().charAt(0))) {
                        return false;
                    }
                } catch (Exception e) {
                    if (tile.getText().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void setupSaveLoadButtons() {
        // Assuming you have a WordDict instance named wordDict in your viewModel or elsewhere

        saveButton.setOnAction(e -> {
            try {
                // Assuming "gameState.json" as the filename. Adjust path as needed.
                this.viewModel.saveGameState("gameState.json");
                System.out.println("Game saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                // Optionally, show an alert dialog to the user
                new Alert(AlertType.ERROR, "Failed to save the game.").show();
            }
        });

        loadButton.setOnAction(e -> {
            try {
                this.viewModel.loadGameState("gameState.json");
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

