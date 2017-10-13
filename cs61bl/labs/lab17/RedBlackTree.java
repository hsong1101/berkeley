import java.util.ArrayList;
import java.util.Collections;

/**
 * Simple Red-Black tree implementation.
 *
 * @param <T> type of items.
 */
public class RedBlackTree<T extends Comparable<T>> {

    /** Root of the tree. */
    RBTreeNode<T> root;
    /**
     * Empty constructor.
     */
    public RedBlackTree() {
        root = null;
    }
    /**
     * Constructor that builds this from given BTree (2-3-4) tree.
     *
     * @param tree BTree (2-3-4 tree).
     */
    public RedBlackTree(BTree<T> tree) {
        BTree.Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }
    /**
     * Builds a RedBlack tree that has isometry with given 2-3-4 tree rooted at
     * given node root, and returns the root node.
     *
     * @param root root of the 2-3-4 tree.
     * @return root of the Red-Black tree for given 2-3-4 tree.
     */
    public RBTreeNode<T> buildRedBlackTree(BTree.Node<T> root) {

        int size = 0;

        if(root != null) {
            size = root.getItemCount();
        }

        if(root == null) {

            return null;

        }else if( size == 1){

            return new RBTreeNode<T>(true, root.getItemAt(0),
                    buildRedBlackTree(root.getChildAt(0)),
                    buildRedBlackTree(root.getChildAt(1)));

        }else if(size == 2){

            return new RBTreeNode<T>(true, root.getItemAt(0),
                    buildRedBlackTree(root.getChildAt(0)),
                    new RBTreeNode<T>(false, root.getItemAt(1),
                            buildRedBlackTree(root.getChildAt(1)),
                            buildRedBlackTree(root.getChildAt(2))));

        }else if(size == 3){
            return new RBTreeNode<T>(true, root.getItemAt(1),
            new RBTreeNode<T>(false, root.getItemAt(0),

                    buildRedBlackTree(root.getChildAt(0)),
                    buildRedBlackTree(root.getChildAt(1))),

            new RBTreeNode<T>(false, root.getItemAt(2),

                    buildRedBlackTree(root.getChildAt(2)),
                    buildRedBlackTree(root.getChildAt(3))));
        }else{
            return null;
        }

    }


    /**
     * Flips the color of the node and its children. Assume that the node has
     * both left and right children.
     *
     * @param node tree node
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }
    /**
     * Returns whether a given node is red. null nodes (children of leaf) are
     * automatically considered black.
     *
     * @param node node
     * @return node is red.
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }
    /**
     * RedBlack tree node.
     *
     * @param <T> type of item.
     */
    static class RBTreeNode<T> {

        /** Item. */
        final T item;

        /** True if the node is black. */
        boolean isBlack;

        /** Pointer to left child. */
        RBTreeNode<T> left;

        /** Pointer to right child. */
        RBTreeNode<T> right;

        /**
         * Constructor with color and item.
         */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /**
         * Constructor with color, item, and pointers to children (can be
         * null).
         */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
            RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }

    }

    public static void main(String[] args){
        RedBlackTree tree = new RedBlackTree();

        tree.buildRedBlackTree(new BTree.TwoThreeFourNode<>(1,2,3));
    }
}
