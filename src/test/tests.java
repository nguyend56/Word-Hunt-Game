import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import cs260.Model.FindAllWords;
import cs260.Model.WordDict;


@RunWith(JUnit4.class)
public class WordSearchTest {

    private WordDict wordDict;
    private FindAllWords wordSearch;

    @Before
    public void setUp() throws FileNotFoundException {
        ArrayList<ArrayList<Character>> board = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList('B', 'A', 'I', 'L')),
            new ArrayList<>(Arrays.asList('E', 'F', 'F', 'I')),
            new ArrayList<>(Arrays.asList('U', 'X', 'E', 'Z')),
            new ArrayList<>(Arrays.asList('I', 'I', 'W', 'E'))
        ));

        wordDict = new WordDict(board);
        wordSearch = new FindAllWords();
    }

    @Test
    public void testWordDictCheckWord() {
        assertTrue(wordDict.checkWord("BAIL")); // Regular word
        assertFalse(wordDict.checkWord("HELLO")); // Not found word
        assertFalse(wordDict.checkWord("BAIL")); // Already found word
    }

    @Test
    public void testWordDictCheckBonusWord() {
        assertTrue(wordDict.checkBonusWord("BAILIFF")); // Bonus word
        assertFalse(wordDict.checkBonusWord("HELLO")); // Not found bonus word
        assertFalse(wordDict.checkBonusWord("BAILIFF")); // Already found bonus word
    }

    @Test
    public void testWordDictGetWordsFound() {
        wordDict.checkWord("BAIL");
        wordDict.checkBonusWord("BAILIFF");

        List<String> expectedWordsFound = Arrays.asList("BAIL", "BAILIFF");
        assertEquals(expectedWordsFound, wordDict.getWordsFound());
    }

    @Test
    public void testFindAllWordsFindWords() {
        ArrayList<ArrayList<Character>> board = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList('B', 'A', 'I', 'L')),
            new ArrayList<>(Arrays.asList('E', 'F', 'F', 'I')),
            new ArrayList<>(Arrays.asList('U', 'X', 'E', 'Z')),
            new ArrayList<>(Arrays.asList('I', 'I', 'W', 'E'))
        ));

        List<Set<String>> foundWords = wordSearch.findWords(board);

        assertTrue(foundWords.get(0).contains("BAIL")); // Regular word
        assertTrue(foundWords.get(1).contains("BAILIFF")); // Bonus word
    }
}
