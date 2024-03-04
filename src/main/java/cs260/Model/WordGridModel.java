package cs260.Model;

import java.io.Serializable;
import java.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class WordGridModel implements Serializable {
    public char[][] grid = new char[4][4];
    private static final Random random = new Random();
    private static final String vowels = "AEIOU";
    private static final String consonants = "BCDFGHJKLMNPQRSTVWXYZ";

    public WordGridModel() {
        fillGridWithRandomLetters();
    }

    private void fillGridWithRandomLetters() {
        // List to keep track of used vowel positions for each row and column
        List<Integer> usedRowPositions = new ArrayList<>();
        List<Integer> usedColPositions = new ArrayList<>();
        String vowels = "AEIOU";
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
    
        // Randomly select vowel positions for each row and column
        while (usedRowPositions.size() < 4) {
            int row = random.nextInt(4);
            int col = random.nextInt(4);
    
            if (!usedRowPositions.contains(row) && !usedColPositions.contains(col)) {
                if (usedRowPositions.size() < 2) {
                    grid[row][col] = vowels.charAt(random.nextInt(vowels.length()));
                } else {
                    grid[row][col] = consonants.charAt(random.nextInt(consonants.length()));
                }
                usedRowPositions.add(row);
                usedColPositions.add(col);
            }
        }
    
        // Fill the rest of the grid with random letters
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '\u0000') { // Check if the cell is empty
                    grid[i][j] = (random.nextBoolean()) ? vowels.charAt(random.nextInt(vowels.length())) : consonants.charAt(random.nextInt(consonants.length()));
                }
            }
        }
    }
    


    public void setGridFromInput(String input) {
        if (input.length() == 16) {
            input = input.toUpperCase();
            for (int i = 0, k = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++, k++) {
                    grid[i][j] = input.charAt(k);
                }
            }
        } else {
            throw new IllegalArgumentException("Input must be exactly 16 characters.");
        }
    }

    public void makeGrid() {
        this.fillGridWithRandomLetters();
        System.out.println(Arrays.deepToString(this.getGrid()));
    }
    
    public char[][] getGrid() {
        return this.grid;
    }

    public void setGrid(char[][] newGrid) {
        grid = newGrid;
    }

}
