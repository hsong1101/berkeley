import ucb.junit.textui;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Autocomplete class.
 *  @author
 */
public class TestAutocomplete {

    // fail when terms and weights are different lengths
    @Test
    public void testFailOnWrongArrayLength() {
        String[] terms = new String[100];
        double[] weights = new double[99];

        Autocomplete testTrie;
        try {
            testTrie = new Autocomplete(terms, weights);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }

    // fail when there are duplicate input terms
    @Test
    public void testFailOnDuplicateInputTerms() {
        String[] terms = {"cat", "car", "cow", "cat"};
        double[] weights = {3.0, 3.0, 3.0, 3.0};

        Autocomplete testTrie;
        try {
            testTrie = new Autocomplete(terms, weights);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }

    // fail when there are negative weights
    @Test
    public void testFailOnNegativeWeights() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {3.0, 3.0, -3.0};

        Autocomplete testTrie;
        try {
            testTrie = new Autocomplete(terms, weights);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }

    // fail when k is not positive
    @Test
    public void testFailOnNegativeK() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {3.0, 3.0, 3.0};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
//        testTrie.topMatches("ca", -2);
        try {
            testTrie.topMatches("ca", -2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }

    // test basic returns top words on empty input string
    @Test
    public void testBasicTopEmptyString() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {1.1, 2.2, 3.3};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        Iterable<String> matches = testTrie.topMatches("", 2);
        String testMatch = matches.toString();
        assertTrue(testMatch.equals("[cow, car]"));
    }

    // test basic functions of top match
    @Test
    public void testBasicTopMatches() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {3.0, 3.0, 3.0};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        Iterable<String> matches = testTrie.topMatches("ca", 2);
        String testMatch = matches.toString();
        assertTrue(testMatch.equals("[car, cat]"));
    }

    // test basic weights
    @Test
    public void testWeights() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {1.1, 1.2, 1.3};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        assertTrue(testTrie.weightOf("c") == 0.0);
        assertTrue(testTrie.weightOf("ca") == 0.0);
        assertTrue(testTrie.weightOf("co") == 0.0);
        assertTrue(testTrie.weightOf("cat") == 1.1);
        assertTrue(testTrie.weightOf("car") == 1.2);
        assertTrue(testTrie.weightOf("cow") == 1.3);
    }



    // test basic functions of top match with weights
    @Test
    public void testBasicTopMatches2() {
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {1.1, 1.2, 1.3};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        Iterable<String> matches = testTrie.topMatches("c", 2);
        String testMatch = matches.toString();
        assertTrue(testMatch.equals("[cow, car]"));
    }

    // Test wiktionary topMatch basic functionality
    @Test
    public void testWiktionaryBasicFunction() {
        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        Iterable<String> matches = testTrie.topMatches("ca", 5);
        String testMatch = matches.toString();
        assertTrue(testMatch.equals("[can, came, called, cannot, case]"));
    }

    // Test wiktionary topMatch return on empty input string
    @Test
    public void testWiktionaryTopMatchReturnOnEmpty() {
        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        Iterable<String> matches = testTrie.topMatches("", 5);
        int count = 0;
        Iterator testMatches = matches.iterator();
        while (testMatches.hasNext()) {
            testMatches.next();
            count++;
        }
        assertEquals(5, count);
    }

    // Test wiktionary weights
    @Test
    public void testWiktionaryWeights() {
        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        System.out.println(testTrie.weightOf("automobile"));
        assertEquals(6197.0, testTrie.weightOf("automobile"), 0.1);
        assertEquals(4250.0, testTrie.weightOf("automatic"), 0.1);
        assertEquals(133159.0, testTrie.weightOf("company"), 0.1);
        assertEquals(78039.8, testTrie.weightOf("complete"), 0.1);
        assertEquals(60384.9, testTrie.weightOf("companion"), 0.1);
        assertEquals(52050.3, testTrie.weightOf("completely"), 0.1);
        assertEquals(56271872.0, testTrie.weightOf("the"), 0.1);
        assertEquals(3340398.0, testTrie.weightOf("they"), 0.1);
        assertEquals(2820265.0, testTrie.weightOf("their"), 0.1);
        assertEquals(2509917.0, testTrie.weightOf("them"), 0.1);
        assertEquals(1961200.0, testTrie.weightOf("there"), 0.1);
    }

    // Test basic Trie build time is less than 10 milliseconds
    // tested on Soda Hall lab machine
    @Test
    public void testBasicBuildTime() {
        long startTime = System.currentTimeMillis();
        String[] terms = {"cat", "car", "cow"};
        double[] weights = {3.0, 3.0, 3.0};
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        long stopTime = System.currentTimeMillis();
        assertTrue(stopTime - startTime < 10);
    }

    // Test wiktionary Trie build time is less than 1000 milliseconds
    // tested on Soda Hall lab machine
    @Test
    public void testWikBuildTime() {
        long startTime = System.currentTimeMillis();

        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);

        long stopTime = System.currentTimeMillis();
        assertTrue(stopTime - startTime < 1000);
    }

    // Test wiktionary TopMatch Speed
    // tested on Soda Hall lab machine
    @Test
    public void testWiktionaryTopMatchSpeed() {
        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }
        Autocomplete testTrie;
        testTrie = new Autocomplete(terms, weights);
        long startTime = System.currentTimeMillis();
        testTrie.topMatches("ca", 5);
        long stopTime = System.currentTimeMillis();
        assertTrue(stopTime - startTime < 200);
    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}