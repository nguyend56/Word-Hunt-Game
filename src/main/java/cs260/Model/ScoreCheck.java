package cs260.Model;

import java.util.*;
import cs260.Model.DictUtility;


public abstract class ScoreCheck {

    protected DictUtility wordFinder;

    public ScoreCheck(ArrayList<ArrayList<Character>> grid) {
        this.wordFinder = new DictUtility(grid);
    }

    public boolean regularWordCheck(String word) {
        return false;
    }

    public boolean bonusWordCheck(String word) {
        return false;
    }

    public void wordAttempted(String word) {}
}