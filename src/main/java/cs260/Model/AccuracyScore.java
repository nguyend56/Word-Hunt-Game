package cs260.Model;

import java.util.*;
import java.io.*;
import java.lang.*;

import cs260.Model.DictUtility;


public class AccuracyScore extends ScoreCheck {
    int numAttempts, numCorrect, numWrong;

    public AccuracyScore(ArrayList<ArrayList<Character>> grid) {
        super(grid);
        numAttempts = 0;
        numCorrect = 0;
    }

    @Override
    public void wordAttempted(String word) {
        System.out.println("word: " + word);
        this.numAttempts++;
    }

    public int getAccuracy() {
        return Math.round(100 / numAttempts);
    }

    public void resetNumAttempts() {
        this.numAttempts = 0;
    }
}