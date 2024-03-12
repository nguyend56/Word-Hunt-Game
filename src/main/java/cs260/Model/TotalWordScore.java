package cs260.Model;

import java.util.*;
import cs260.Model.DictUtility;


public class TotalWordScore extends ScoreCheck {
    ArrayList<String> allWordsFound;
    ArrayList<String> bonusWords;
    
    public TotalWordScore(ArrayList<ArrayList<Character>> grid) {
        super(grid);

        allWordsFound = new ArrayList<>();
        bonusWords = new ArrayList<>();

    }

    @Override
    public boolean regularWordCheck(String word) {
        if (this.wordFinder.checkWord(word)) {
            this.allWordsFound.add(word);
            return true;
        }
        return false;
    }

    @Override
    public boolean bonusWordCheck(String word) {
        if (this.wordFinder.checkBonusWord(word)) {
            this.bonusWords.add(word);
            return true;
        }
        return false;
    }

    public int getNumWordsFound() {
        return this.wordFinder.getFoundWordsCount();
    }

    public ArrayList<ArrayList<Integer>> getLetterWordCount() {
        return this.wordFinder.getLetterWordCount();
    }

    public int getNumWordsInGrid() {
        return this.wordFinder.getAllWordsCount();
    }
}