import java.util.*;

/**
 * An AmoebaFamily is a tree, where nodes are Amoebas, each of which can have
 * any number of children.
 */
public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba> {

    /**
     * ROOT is the root amoeba of this AmoebaFamily
     */
    public Amoeba root = null;

    /**
     * A constructor that starts an Amoeba family with an amoeba
     * @param  name the name of the first Amoeba of this AmoebaFamily
     */
    public AmoebaFamily(String name) {
        root = new Amoeba(name, null);
    }

    /**
     * Adds a new Amoeba with childName to this AmoebaFamily
     * as the youngest child of the amoeba named parentName
     * Precondition: This AmoebaFamily must contain an Amoeba named parentName.
     * @param parentName name of the parent Amoeba
     * @param childName  name of the Amoeba to add as parentName's child
     */
    public void addChild(String parentName, String childName) {
        if (root != null) {
            root.addChild(parentName, childName);
        }
    }

    /**
     * Changes the names for all Amoebas in this AmoebaFamily to use only
     * lowercase letters.
     */
    public void makeNamesLowercase() {
        //Your goal is to make this as similar as possible to addChild
        root.makeNamesLowercase2();
    }

    /**
     * Replaces the name of an amoeba named currentName with the name newName.
     * Precondition: This AmoebaFamily contains exactly one Amoeba named
     * currentName.
     */
    public void replaceName(String currentName, String newName) {
        //Your goal is to make this as similar as possible to addChild
        root.replacenamek(currentName, newName);


    }

    /**
     * Print the names of all amoebas in the family, one on each line.
     * Later you will write print() that has more interesting formatting
     */
    public void printFlat() {
        //Your goal is to make this as similar as possible to addChild
        System.out.println(root.name);
        root.prenting();
    }

    /**
     * Prints the name of all Amoebas in this AmoebaFamily in preorder, with
     * the oldest Amoeba printed first.
     * Members of the AmoebaFamily constructed in the main method should
     * be printed in the following sequence:
     * Amos McCoy, mom/dad, me, Mike, Bart, Lisa, Homer, Marge,
     * Bill, Hilary, Fred, Wilma, auntie
     * This should be formatted as stated in the Pretty Print section of lab.
     */
    public void print() {
        // YOUR CODE HERE
        this.root.cuteprint("");

    }

    /**
     * Returns the length of the longest name in this AmoebaFamily
     */
    public int longestNameLength() {
        if (root != null) {
            return root.longestNameLength();
        }
        return 0;
    }

    /**
     * Returns the longest name in this AmoebaFamily
     */
    public String longestName() {
        // your goal is to make this look as similar as possible to
        // longestNameLength

        return root.longestName();
    }

    /**
     * Returns the height of this AmoebaFamily
     */
    public int height() {
        //YOUR CODE HERE
        return root.heightHelper2();
    }

    /**
     * Returns the size of this AmoebaFamily
     */
    public int size() {
        //YOUR CODE HERE
        return root.sizeHelper();
    }

    /**
     * Returns an Iterator for this AmoebaFamily
     */
    public Iterator<Amoeba> iterator() {
        return new AmoebaIterator();
    }

    /**
     * Creates a new AmoebaFamily and prints it out
     * @param args command line arguments
     */
    public static void main(String[] args) {
        AmoebaFamily family = new AmoebaFamily("Amos McCoy");
        family.addChild("Amos McCoy", "mom/dad");
        family.addChild("Amos McCoy", "auntie");
        family.addChild("mom/dad", "me");
        family.addChild("mom/dad", "Fred");
        family.addChild("mom/dad", "Wilma");
        family.addChild("me", "Mike");
        family.addChild("me", "Homer");
        family.addChild("me", "Marge");
        family.addChild("Mike", "Bart");
        family.addChild("Mike", "Lisa");
        family.addChild("Marge", "Bill");
        family.addChild("Marge", "Hilary");

//        family.printFlat();

        Iterator<Amoeba> it = family.iterator();

        while(it.hasNext()){
            System.out.println(it.next());
        }


    }

    /**
     * Ignore the class below for lab 12!###############################################################################
     *
     * An Iterator class for the AmoebaFamily.
     * Complete enumeration of a family of N amoebas should take
     * O(N) operations.
     */
    public class AmoebaIterator implements Iterator<Amoeba> {

        Amoeba temp;
        LinkedList<Amoeba> list = new LinkedList<>();


        /**
         * AmoebaIterator constructor. Sets up all of the initial information
         * for the AmoebaIterator
         */
        public AmoebaIterator() {
            if(root != null) {
                list.add(root);
            }

        }

        /**
         * Returns true if there is a next element that has not
         * been seen yet
         */
        public boolean hasNext() {

            return !list.isEmpty();
        }

        /**
         * Returns the next element in preorder
         */
        public Amoeba next() {

            if(!hasNext()){
                throw new NoSuchElementException("No Element");
            }
            Amoeba a = list.removeFirst();
            for(int i = 0; i < a.children.size(); i++){
                list.addLast(a.children.get(i));
            }

            return a;

        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * An Amoeba is a node of an AmoebaFamily
     */
    public static class Amoeba {

        /**
         * name is the name of this Amoeba
         * parent is the parent of this Amoeba
         * children contains all of the child Amoebas of this Amoeba
         */
        public String name;
        public Amoeba parent;
        public ArrayList<Amoeba> children;

        /**
         * Amoeba constructor
         * @param  name     the name for this Amoeba
         * @param  parent the parent for this Amoeba
         */
        public Amoeba(String name, Amoeba parent) {
            this.name = name;
            this.parent = parent;
            this.children = new ArrayList<Amoeba>();
        }

        /**
         * Returns a String representation of this Amoeba
         */
        public String toString() {
            return name;
        }

        /**
         * Getter method for the parent of this Amoeba
         */
        public Amoeba parent() {
            return parent;
        }

        /**
         * Adds a child to an Amoeba that matches parentName
         * @param parentName name of Amoeba to give a child to
         * @param childName  name of child Amoeba to add
         */
        public void addChild(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChild(parentName, childName);
                }
            }
        }

        //Add more void recursive functions below

        /**
         * Returns the length of the longest name between this Amoeba and its
         * children
         */
        public int longestNameLength() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen, a.longestNameLength());
            }
            return maxLengthSeen;
        }

        public String longestName() {
            Amoeba temp = this;
            String longest = "";

            if (temp.name.length() > longest.length()) {
                longest = temp.name;
            }

            for (Amoeba a: this.children) {
                if (a.longestName().length() > longest.length()) {
                    longest = a.longestName();
                }
                a.longestName();
            }

            return longest;


        }

        public void makeNamesLowercase2() {

            if (this.children.isEmpty()) {
                this.name = this.name.toLowerCase();
            } else {
                this.name = this.name.toLowerCase();
                for (Amoeba a: this.children) {
                    a.makeNamesLowercase2();
                    if (!a.children.isEmpty()) {
                        for (Amoeba b : a.children) {
                            b.makeNamesLowercase2();
                        }
                    }
                }
            }

        }

        public void prenting() {

            Amoeba dis = this;

            if (dis != null) {
                for (Amoeba a : dis.children) {
                    System.out.println(a.name);
                    a.prenting();
                }

            }




        }

        public void cuteprint(String spaces) {

            System.out.println(spaces + this.name);
            spaces = "    " + spaces;
            for (Amoeba a : this.children) {
                a.cuteprint(spaces);
            }

        }

        public void replacenamek(String currentName, String newName) {
            Amoeba dis = this;
            if (dis.name.equals(currentName)) {
                dis.name = newName;
            } else {
                for (Amoeba a : children) {
                    a.replacenamek(currentName, newName);
                }
            }
        }

        public int heightHelper() {

            int bestSoFar = 0;

            if (this == null) {
                return 0 ; //-1
            } else {
                if (this.children.isEmpty()) {
                    return bestSoFar;

                } else {
                    for (Amoeba a : this.children) {
                        return 1 + Math.max(bestSoFar, a.heightHelper());
                    }
                }

            }

            return bestSoFar;
        }

        public int heightHelper2() {


            int bestsofar = 0;

            if (this.children.isEmpty() && this.parent == null) {
                return bestsofar;
            } else {
                if (this.children.isEmpty()) {
                } else {
                    for (Amoeba a : this.children) {
                        bestsofar = Math.max(a.heightHelper2() + 1, bestsofar);
                    }

                }
            }

            return bestsofar;

        }

        public int sizeHelper() {

            int size = 1;

            if (this.children.isEmpty()) {
                size = 1;
            } else {
                for (Amoeba a : this.children) {
                    size = size + a.sizeHelper() ;
                }

            }
            return size;
        }


    }


}
