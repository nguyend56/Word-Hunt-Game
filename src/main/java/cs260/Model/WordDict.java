package cs260.Model;

import java.util.*;
import java.io.*;

import cs260.Model.FindAllWords;

public class WordDict {
    Set<String> allRegWords;
    Set<String> allBonusWords;
    ArrayList<String> foundRegWords;
    ArrayList<String> foundBonusWords;
    FindAllWords wordSearch;

    public WordDict(ArrayList<ArrayList<Character>> board) {
        try {
            /* File regWordsfile = new File(System.getProperty("user.dir") + "/src/main/java/cs260/Model/reg_words.txt");
            Scanner sc1 = new Scanner(regWordsfile);
            while (sc1.hasNext()) this.allBonusWords.add(sc1.nextLine());

            File bonusWordsfile = new File(System.getProperty("user.dir") + "/src/main/java/cs260/Model/bonus_words.txt");
            Scanner sc2 = new Scanner(bonusWordsfile);
            while (sc2.hasNext()) this.allBonusWords.add(sc2.nextLine()); */

            wordSearch = new FindAllWords();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Set<String>> allWordSets = this.getAllWords(board);
        this.allRegWords = allWordSets.get(0);
        this.allBonusWords = allWordSets.get(1);

        this.foundRegWords = new ArrayList<>();
        this.foundBonusWords = new ArrayList<>();
    }

    public boolean checkWord(String word) {
        if (this.foundRegWords.contains(word) || this.foundBonusWords.contains(word))
            throw new IllegalArgumentException("The word has already been found.");
        boolean found = false;

        if (this.allRegWords.contains(word)) {
            found = true;
            this.foundRegWords.add(word);
        }

        return found;
    }

    public boolean checkBonusWord(String word) {
        if (this.allBonusWords.contains(word)) {
            System.out.println("Found bonus word");
            this.foundBonusWords.add(word);
            return true;
        }
        return false;
    }

    public ArrayList<String> getWordsFound() {
        ArrayList<String> toRet = new ArrayList<>();
        toRet.addAll(this.foundRegWords);
        toRet.addAll(this.foundBonusWords);
        return toRet;
    }

    public int getFoundWordsCount() {
        return this.foundRegWords.size() + this.foundBonusWords.size();
    }

    private List<Set<String>> getAllWords(ArrayList<ArrayList<Character>> board) {
        return wordSearch.findWords(board);
    }

    public int getAllWordsCount() {
        return this.allRegWords.size();
    }

    private char[][] genRandomBoard() {
        Random random = new Random();
        char[][] grid = new char[4][4];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = (char) ('A' + random.nextInt(26));
            }
        }
        return grid;
    }
 
    /* public static void main(String[] args) {
        WordDict dict = new WordDict();

        System.out.println(dict.checkWord("Hello"));
        System.out.println(dict.getAllWords(dict.genRandomBoard()));
    } */
}