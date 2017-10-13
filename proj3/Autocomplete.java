import java.util.*;

public class Autocomplete {

    /**
     * Field:
     * SortTrie trie
     * PriorityQueue trie: push the TrieNode to
     * comparator based on the weight of words
     * HashMap containing the topList of top terms ( maximum are k words)
     */
    TrieNode2 trie;
    HashSet<String> setOfItems = new HashSet<>();
    Map<Double, String> allList;

    public TrieNode2 getTrie() {
        return trie;
    }

    public Autocomplete(String[] terms, double[] weights) {

        if (terms.length != weights.length) {
            throw new IllegalArgumentException();
        }

        trie = new TrieNode2();
        allList = new TreeMap<>(Collections.reverseOrder());

        /** loop through the array of terms and weights
         * insert word by word
         */
        setOfItems.add(terms[0]);
        insertHelper(terms[0], weights[0], trie);
        allList.put(weights[0], terms[0]);
        for (int i = 1; i < terms.length; i++) {
            if (setOfItems.contains(terms[i])) {
                throw new IllegalArgumentException();
            } else if (weights[i] < 0.0) {
                throw new IllegalArgumentException();
            } else {
                setOfItems.add(terms[i]);
                insertHelper(terms[i], weights[i], trie);
                allList.put(weights[i], terms[i]);
            }
        }
    }

    private void insertHelper(String s, double weight, TrieNode2 n) {

        for (int i = 0; i < s.length(); i++) {
            if (!n.childLetters.containsKey(s.charAt(i))) {
                if (i == s.length() - 1) {
                    TrieNode2 endNode = new TrieNode2(s.charAt(i), weight, weight, true);
                    endNode.wordOfNode = s;
                    n.childLetters.put(s.charAt(i), endNode);

                } else {
                    //double greaterW = Math.max(weight, n.branchWeight);
                    TrieNode2 newNode = new TrieNode2(s.charAt(i), weight);
                    n.childLetters.put(s.charAt(i), newNode);
                    n = n.childLetters.get(s.charAt(i));
                }
            } else {
                if (i == s.length() - 1) {
                    double greaterW = Math.max(weight, n
                            .childLetters.get(s.charAt(i)).branchWeight);
                    n = n.childLetters.get(s.charAt(i));
                    n.endOfWord = true;
                    n.wordOfNode = s;
                    n.endWeight = weight;
                    n.branchWeight = greaterW;
                } else {
                    double greaterW = Math.max(weight, n
                            .childLetters.get(s.charAt(i)).branchWeight);
                    n = n.childLetters.get(s.charAt(i));
                    n.branchWeight = greaterW;
                }

            }
        }

    }

    public class TrieNode2 {
        char letter;
        HashMap<Character, TrieNode2> childLetters = new HashMap<Character, TrieNode2>();
        double branchWeight;  // Default branchWeight = 0.0 (branch:= NonLeaf)
        double endWeight;  //  Default endOfWord  is 1.0  (end:= Leaf)
        boolean endOfWord = false;
        String wordOfNode = null;

        // Head Node Constructor
        public TrieNode2() {
        }

        // Branch Node Constructor
        public TrieNode2(char letter, double weight) {
            this.letter = letter;
            this.branchWeight = weight;
        }

        // End Of Word Node Constructor - USE: TrieNode('c', 4.5, true);
        public TrieNode2(char letter, double branchWeight, double endWeight, boolean endOfWord) {
            this.letter = letter;
            this.branchWeight = branchWeight;
            this.endOfWord = endOfWord;
            this.endWeight = endWeight;
        }
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     *
     * @param term
     * @return
     */
    public double weightOf(String term) {
        /* YOUR CODE HERE. */
        return findWeight(term, getTrie());
    }

    /**
     * helper function to find the weight of a TrieNode of the end of term s
     */
    private double findWeight(String s, TrieNode2 n) {
        char firstLetter = s.charAt(0);
        String restOfWord = s.substring(1);
        if (!n.childLetters.containsKey(firstLetter)) {
            return 0.0;
        } else if (s.length() == 1) {  // at end of string s
            // if find is trying to match a full word
            return n.childLetters.get(firstLetter).endWeight;
        } else {
            return findWeight(restOfWord, n.childLetters.get(firstLetter));
        }
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     *
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {

        String word = "";

        if (prefix != null) {
            if (prefix.length() > 0) {
                Iterable<String> firstMatch = helperTopMatches(prefix, trie, 1);
                List<String> tempArray = toList(firstMatch);

                word = tempArray.get(0);
                tempArray = new ArrayList<>();
                firstMatch = new ArrayList<>();

            } else {
                for (Map.Entry<Double, String> map : allList.entrySet()) {
                    word = map.getValue();
                    break;
                }
            }

        } else {
            throw new IllegalArgumentException();
        }
        return word;
    }

    public static <String> List<String> toList(Iterable<String> iterable) {
        if (iterable instanceof List) {
            return (List<String>) iterable;
        }
        ArrayList<String> list = new ArrayList<>();
        if (iterable != null) {
            for (String e : iterable) {
                list.add(e);
            }
        }
        return list;
    }

    public Iterable<String> topMatches(String prefix, int k) {
        /* YOUR CODE HERE. */
        LinkedList<String> list = new LinkedList<>();

        if (prefix != null) {
            if (k > 0) {
                if (prefix.length() > 1) {
                    return helperTopMatches(prefix, getTrie(), k);
                } else if (prefix.length() == 1) {
                    return onePrefix(prefix, k);
                } else if (prefix.length() == 2) {
                    return twoPrefix(prefix, k);
                } else {
                    int i = 0;
                    for (Map.Entry map : allList.entrySet()) {
                        list.addLast((String) map.getValue());
                        i++;
                        if (i == k) {
                            break;
                        }
                    }
                    return new ArrayList<String>(list);
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Iterable<String> twoPrefix(String pref, int k) {
        LinkedList<String> list = new LinkedList<>();
        for (Map.Entry map : allList.entrySet()) {
            String temp = map.getValue().toString().substring(0, 2);
            if (temp.equals(pref) && list.size() < k) {
                list.addLast(map.getValue().toString());
            }
        }
        return list;
    }

    public Iterable<String> onePrefix(String pref, int k) {
        LinkedList<String> list = new LinkedList<>();
        char letter = pref.charAt(0);
        for (Map.Entry map : allList.entrySet()) {
            char temp = map.getValue().toString().charAt(0);
            if (temp == letter && list.size() < k) {
                list.addLast(map.getValue().toString());
            }
        }
        return list;
    }

    private Iterable<String> helperTopMatches(String prefix,
                                              TrieNode2 n, int k) {
        PriorityQueue<TrieNode2> pQ // pQ is minHeap
                = new PriorityQueue<>(k, (u1, u2) -> (int) (u1.branchWeight - u2.branchWeight));
        PriorityQueue<TrieNode2> maxHeap // create a maxHeap to copy from pQ to maxHeap
                = new PriorityQueue<>(k, (u1, u2) -> (int) (u2.branchWeight - u1.branchWeight));
        int sizeMaxHeap = 0;
        ArrayList<String> bestAnswers;
        PriorityQueue<TrieNode2> pQ2  // Save completed words
                = new PriorityQueue<>((u1, u2) -> (int) -(u1.endWeight - u2.endWeight));
        LinkedList<String> tempList = new LinkedList<>();
        int sizeOfBestAnswer = 0;
        char firstLetter = prefix.charAt(0);
        String restOfWord = prefix.substring(1);
        if (n.childLetters.isEmpty()) { // n = n.childLetters.get(firstLetter);
            return new ArrayList<String>();
        }
        if (!n.childLetters.containsKey(firstLetter)) {
            return new ArrayList<String>();
        } else {  // at end of string s
            if (prefix.length() == 1) {
                if (n.childLetters == null) { // n is a completed word.
                    pQ2.add(n);
                } else {
                    if (n != null) {
                        n = n.childLetters.get(firstLetter);
                        if (n.endOfWord) {
                            pQ2.add(n);
                        }
                        for (HashMap.Entry<Character, TrieNode2> entry : n
                                .childLetters.entrySet()) {
                            TrieNode2 childNode = entry.getValue();
                            if (pQ.size() < k) { // if sizeOfPQ < k => add childNode to pQ
                                pQ.add(childNode);
                            } else {
                                TrieNode2 lightestNode = pQ.peek();
                                if ((int) childNode.branchWeight
                                        > (int) lightestNode.branchWeight) {
                                    lightestNode = pQ.poll();
                                    pQ.add(childNode);
                                }
                            }
                        } // move from minHeap to max Heap , now pQ is empty
                        for (int i = 0; i < pQ.size(); i++) {
                            maxHeap.add(pQ.poll());
                            sizeMaxHeap++;
                        }
                    }
                    while (sizeOfBestAnswer < k && sizeMaxHeap > 0) {
                        TrieNode2 heaviestNode = maxHeap.poll();
                        sizeMaxHeap--;  // move all elements of maxHeap to pQ, now maxHeap is empty
                        int tempSizeMaxHeap = sizeMaxHeap;
                        for (int i = 0; i < tempSizeMaxHeap; i++) {
                            pQ.add(maxHeap.poll());
                            sizeMaxHeap--;
                        }
                        if (heaviestNode.endOfWord) {
                            pQ2.add(heaviestNode);
                        }
                        for (HashMap.Entry<Character, TrieNode2> entry : heaviestNode
                                .childLetters.entrySet()) {
                            TrieNode2 child = entry.getValue();
                            if (pQ.size() < k) { // if sizeOfPQ < k, add childNode to pQ
                                pQ.add(child);
                            } else {
                                TrieNode2 lightestNode = pQ.peek();
                                if ((int) child.branchWeight > (int) lightestNode.branchWeight) {
                                    lightestNode = pQ.poll();
                                    pQ.add(child);
                                }
                            }
                        } // move all elements of pQ to maxHeap, now pQ is empty
                        for (int i = 0; i < pQ.size(); i++) {
                            maxHeap.add(pQ.poll());
                            sizeMaxHeap++;
                        }
                    }
                }
            } else {  // not at end of word but this letter is in tree.
                return helperTopMatches(restOfWord, n.childLetters.get(firstLetter), k);
            }
        }
        while (pQ2.size() > 0) {
            TrieNode2 temp = pQ2.poll();
            if (!tempList.contains(temp.wordOfNode)) {
                if (tempList.size() < k) {
                    tempList.addLast(temp.wordOfNode);
                    sizeOfBestAnswer++;
                } else {
                    break;
                }
            }
        }
        bestAnswers = new ArrayList<>(tempList);
        if (sizeOfBestAnswer >= k) {
            return bestAnswers.subList(0, k);
        } else {
            return bestAnswers.subList(0, sizeOfBestAnswer);
        }
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}
