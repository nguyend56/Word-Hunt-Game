package cs260.Model;

import java.io.*;
import java.util.*;

public class FindAllWords {

    private Set<String> allRegWords = new HashSet<>();
    private Set<String> allBonusWords = new HashSet<>();
    private Set<String> regWords = new HashSet<>();
    private Set<String> bonusWords = new HashSet<>();
    // private Map<Character, Integer> letterCountMap = new HashMap<>();
    private int letterWordCount;
    ArrayList<ArrayList<Integer>> letterCountMap = new ArrayList<>();
    private ArrayList<ArrayList<Character>> board = new ArrayList<>();

    public FindAllWords(ArrayList<ArrayList<Character>> board) throws FileNotFoundException {
        loadWordsFromFile("/src/main/java/cs260/Model/reg_words.txt", allRegWords);
        loadWordsFromFile("/src/main/java/cs260/Model/bonus_words.txt", allBonusWords);
        this.board = board;
        this.letterWordCount = 0;
    }

    private void loadWordsFromFile(String filePath, Set<String> wordSet) throws FileNotFoundException {
        File file = new File(System.getProperty("user.dir") + filePath);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            wordSet.add(scanner.nextLine().trim().toLowerCase());
        }
        scanner.close();
    }

    public List<Set<String>> findWords() {
        int rows = this.board.size();
        int cols = this.board.get(0).size();

        boolean[][] visited = new boolean[rows][cols];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            ArrayList<Integer> rowCounts = new ArrayList<>(rows);
            for (int j = 0; j < cols; j++) {
                this.letterWordCount = 0;
                dfs(this.board, visited, i, j, sb);
                rowCounts.add(j, this.letterWordCount);
            }
            this.letterCountMap.add(i, rowCounts);
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
        if (word.length() > 3 && isWord(word) && !alreadyFound(word)) {
            this.processWord(word);
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
            this.letterWordCount++;
            regWords.add(word);
        } else if (allBonusWords.contains(word)) {
            bonusWords.add(word);
        }
    }

    private boolean alreadyFound(String word) {
        return regWords.contains(word) || bonusWords.contains(word);
    }
    
    private boolean isWord(String word) {
        return this.allRegWords.contains(word) || this.allBonusWords.contains(word);
    }

    public ArrayList<ArrayList<Integer>> getLetterWordCount(List<Set<String>> words) {
        return this.letterCountMap;
    }
}
