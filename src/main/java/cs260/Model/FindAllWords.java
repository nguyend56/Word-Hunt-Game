package cs260.Model;

import java.io.*;
import java.util.*;

public class FindAllWords {

    private Set<String> allRegWords = new HashSet<>();
    private Set<String> allBonusWords = new HashSet<>();
    private Set<String> regWords = new HashSet<>();
    private Set<String> bonusWords = new HashSet<>();

    public FindAllWords() throws FileNotFoundException {
        loadWordsFromFile("/src/main/java/cs260/Model/reg_words.txt", allRegWords);
        loadWordsFromFile("/src/main/java/cs260/Model/bonus_words.txt", allBonusWords);
    }

    private void loadWordsFromFile(String filePath, Set<String> wordSet) throws FileNotFoundException {
        File file = new File(System.getProperty("user.dir") + filePath);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            wordSet.add(scanner.nextLine().trim().toLowerCase());
        }
        scanner.close();
    }

    public List<Set<String>> findWords(ArrayList<ArrayList<Character>> board) {
        int rows = board.size();
        int cols = board.get(0).size();

        boolean[][] visited = new boolean[rows][cols];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(board, visited, i, j, sb);
            }
        }

        if (regWords.isEmpty() && !bonusWords.isEmpty()) {
            regWords = bonusWords;
            bonusWords = new HashSet<>();
        }

        List<Set<String>> result = new ArrayList<>();
        result.add(regWords);
        result.add(bonusWords);
        System.out.println(result);
        return result;
    }

    private void dfs(ArrayList<ArrayList<Character>> board, boolean[][] visited, int row, int col, StringBuilder sb) {
        int rows = board.size();
        int cols = board.get(0).size();

        if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col]) {
            return;
        }

        sb.append(board.get(row).get(col));
        visited[row][col] = true;

        String word = sb.toString().toLowerCase();
        if (word.length() > 1) {
            processWord(word);
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            dfs(board, visited, newRow, newCol, sb);
        }

        sb.deleteCharAt(sb.length() - 1);
        visited[row][col] = false;
    }

    private void processWord(String word) {
        if (allRegWords.contains(word)) {
            regWords.add(word);
        } else if (allBonusWords.contains(word)) {
            bonusWords.add(word);
        }
    }
}


    
    /* public static void main(String[] args) throws FileNotFoundException {
        char[][] board = {
            {'W', 'Q', 'O', 'J'},
            {'C', 'N', 'D', 'U'},
            {'I', 'F', 'D', 'D'},
            {'W', 'A', 'Y', 'C'}
        };

        ArrayList<ArrayList<Character>> board2 = new ArrayList<>();
        for (char[] row : board) {
            ArrayList<Character> temp = new ArrayList<>();
            for (char c : row) {
                temp.add(c);
            }
            board2.add(temp);
        }

        FindAllWords wordSearch = new FindAllWords();
        Set<String> foundWords = wordSearch.findWords(board2);

        System.out.println("Found words:");
        for (String word : foundWords) {
            System.out.println(word);
        }
    } */
