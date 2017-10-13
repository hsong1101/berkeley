import java.util.HashMap;

/**
 * Prefix-SortTrie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * SortTrie or a prefix.
 *
 * @author
 */
public class Trie {

    private TrieNode head;

    public Trie() {
        head = new TrieNode();
    }

    public TrieNode getHead() {
        return head;
    }

    public boolean find(String s, boolean isFullWord) {
        /* YOUR CODE HERE. */
        if (s == null) {
            throw new IllegalArgumentException("Can not search for a NULL string");
        } else if (s.equals("")) {
            throw new IllegalArgumentException("Can not search for an EMPTY string");
        } else {
            return findHelper(s, isFullWord, head);
        }
    }

    private boolean findHelper(String s, boolean isFullWord, TrieNode n) {
        char firstLetter = s.charAt(0);
        String restOfWord = s.substring(1);
        if (!n.childLetters.containsKey(firstLetter)) { // last letter of s is NOT in the tree
            return false;
        } else if (s.length() == 1) {  // at end of string s
            if (isFullWord) { // if find is trying to match a full word
                return n.childLetters.get(firstLetter).endOfWord;
            } else {
                return true;
            }
        } else {  // not at end of word but this letter is in tree.
            return findHelper(restOfWord, isFullWord, n.childLetters.get(firstLetter));
        }
    }

    // Default constructor without weight
    public void insert(String s) {
        insert(s, 1.0);
    }

    // Constructor with weight
    public void insert(String s, double weight) {
        /* YOUR CODE HERE. */
        if (s == null) {
            throw new IllegalArgumentException("Can not insert a NULL string");
        } else if (s.equals("")) {
            throw new IllegalArgumentException("Can not insert an EMPTY string");
        } else {
            insertHelper(s, weight, head);
        }
    }

    private void insertHelper(String s, double weight, TrieNode n) {
        if (s.length() == 0) { // past the end of the word (finished inserting)
            return;
        } else {
            char firstLetter = s.charAt(0);
            String restOfWord = s.substring(1);
            if (!n.childLetters.containsKey(firstLetter)) {
                // if SortTrie doesn't contain  this letter, add it.
                if (s.length() == 1) { // at end of word
                    n.childLetters.put(s.charAt(0), new TrieNode(s.charAt(0), weight, true));
                } else {  // not at end of word
                    n.childLetters.put(s.charAt(0), new TrieNode(s.charAt(0), weight));
                }
            }
            // recursively call insertHelper on rest of the word
            insertHelper(restOfWord, weight, n.childLetters.get(firstLetter));
        }
    }

    public class TrieNode {

        char letter;
        HashMap<Character, TrieNode> childLetters = new HashMap<Character, TrieNode>();
        double branchWeight;  // Default branchWeight = 0.0 (branch:= NonLeaf)
        double endWeight;  //  Default endOfWord  is 1.0  (end:= Leaf)
        boolean endOfWord = false;

        // Head Node Constructor
        public TrieNode() {
        }

        // Branch Node Constructor
        public TrieNode(char letter, double weight) {
            this.letter = letter;
            this.branchWeight = weight;
        }

        // End Of Word Node Constructor - USE: TrieNode('c', 4.5, true);
        public TrieNode(char letter, double weight, boolean endOfWord) {
            this.letter = letter;
            this.branchWeight = weight;
            if (endOfWord) {
                this.endWeight = weight;
                this.endOfWord = endOfWord;
            }
        }
    }
}
