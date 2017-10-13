import org.junit.Assert;
import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Trie class.
 *  @author
 */
public class TestTrie {

    // Test that inserting a null or empty string throws and exception
    @Test
    public void testFailOnNullOrEmpty() {
        Trie t1 = new Trie();
        try {
            t1.insert(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            t1.insert("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    // Test basic insert() method
    @Test
    public void basicInputTest() {
        Trie t1 = new Trie();
        t1.insert("AB");
        char childLetter = t1.getHead().childLetters.get('A').letter;
        assertEquals(childLetter, 'A');
        childLetter = t1.getHead().childLetters.get('A').childLetters.get('B').letter;
        assertEquals(childLetter, 'B');
    }

    // Test inserting multiple strings
    @Test
    public void multipleStringsInsertTest() {
        Trie t1 = new Trie();
        t1.insert("cat");
        t1.insert("cow");
        t1.insert("calvin");
        t1.insert("cheese");
        t1.insert("cat");
        t1.insert("dog");
        t1.insert("frog");
        t1.insert("+*&");

        char childLetter = t1.getHead().childLetters.get('c').letter;
        assertEquals(childLetter, 'c');

        childLetter = t1.getHead()
                .childLetters.get('c')
                .childLetters.get('a')
                .letter;
        assertEquals(childLetter, 'a');

        childLetter = t1.getHead()
                .childLetters.get('c')
                .childLetters.get('h')
                .letter;
        assertEquals(childLetter, 'h');

        assertTrue(t1.getHead()
                .childLetters.get('c')
                .childLetters.get('a')
                .childLetters.get('t')
                .endOfWord);
    }

    // test find()
    @Test
    public void findTest() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertTrue(t.find("hell", false)); // true
        assertTrue(t.find("hello", true)); // true
        assertTrue(t.find("good", false)); // true
        assertFalse(t.find("bye", false)); // false
        assertFalse(t.find("heyy", false)); // false
        assertFalse(t.find("hell", true)); // false
    }

    // test branch and end weights
    @Test
    public void weightTest() {
        Trie t1 = new Trie();
        t1.insert("cat", 2.3);
        t1.insert("cow", 1.5);
        t1.insert("car", 3.8);

        assertTrue(1.5 == t1.getHead()
                .childLetters.get('c')
                .childLetters.get('o')
                .childLetters.get('w')
                .endWeight);

        assertTrue(2.3 == t1.getHead()
                .childLetters.get('c')
                .childLetters.get('a')
                .childLetters.get('t')
                .endWeight);

        assertTrue(3.8 == t1.getHead()
                .childLetters.get('c')
                .childLetters.get('a')
                .childLetters.get('r')
                .endWeight);
    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) { textui.runClasses(TestTrie.class);
    }
}
