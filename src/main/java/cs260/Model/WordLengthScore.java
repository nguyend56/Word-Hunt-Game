package cs260.Model;

import java.util.*;

import cs260.Model.DictUtility;


public class WordLengthScore extends ScoreCheck {

    public WordLengthScore(ArrayList<ArrayList<Character>> grid) {
        super(grid);
    }

    public int getWordScore(String word) {
        int score = 0;
        try {
            if (this.regularWordCheck(word)) score = (word.length() - 4) + 1;
        } catch (IllegalArgumentException e) {
            score = (word.length() - 4) + 1;
        }
        System.out.println(score);
        return score;
    }

    public int getTotalScore() {
        int total = 0;
        for (String word: this.wordFinder.allRegWords) {
            total += (word.length() - 4) + 1;
        }
        return total;
    }

    @Override
    public boolean regularWordCheck(String word) {
        return this.wordFinder.checkWord(word);
    }
}