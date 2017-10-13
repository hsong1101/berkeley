import java.util.*;

/**
 * A BinaryTree is a tree with nodes that have up to two children.
 */
public class BinaryTree {

    /**
     * root is the root of this BinaryTree
     */
    private TreeNode root;
    private ArrayList alreadySeen;


    /**
     * The BinaryTree constructor
     */
    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode t) {
        root = t;
    }

    public TreeNode getRoot() {
        return root;
    }

    /**
     * Print the values in the tree in preorder: root value first, then values
     * in the left subtree (in preorder), then values in the right subtree
     * (in preorder).
     */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /**
     * Print the values in the tree in inorder: values in the left subtree
     * first (in inorder), then the root value, then values in the first
     * subtree (in inorder).
     */
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /**
     * Fills this BinaryTree with values a, b, and c.
     * DO NOT MODIFY THIS METHOD.
     */
    public void fillSampleTree1() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c"));
    }

    /**
     * Fills this BinaryTree with values a, b, and c, d, e, f.
     * DO NOT MODIFY THIS METHOD.
     */
    public void fillSampleTree2() {
        root = new TreeNode("a", new TreeNode("b", new TreeNode("d",
            new TreeNode("e"), new TreeNode("f")), null), new TreeNode("c"));
    }

    /**
     * Fills this BinaryTree with the values a, b, c, d, e, f in the way that the spec pictures.
     */
    public void fillSampleTree3() {
        //YOUR CODE HERE.
        TreeNode leafNodE = new TreeNode("e");
        TreeNode leafNodeF = new TreeNode("f");
        root = new TreeNode("a", new TreeNode("b"),
                new TreeNode("c", new TreeNode("d",
                        leafNodE, leafNodeF), null));
    }

    /**                                                                          
     * Fills this BinaryTree with the same leaf TreeNode.                        
     * DO NOT MODIFY THIS METHOD.                                                
     */ 
    public void fillSampleTree4() {
        TreeNode leafNode = new TreeNode("c");                                   
        root = new TreeNode("a", new TreeNode("b", leafNode, leafNode), new TreeNode("d", leafNode, leafNode));
    }

    public void fillSampleTree5(){
        TreeNode leafNode = new TreeNode("c");
        root = new TreeNode("a", new TreeNode("b", null, null), new TreeNode("c", new TreeNode("d", null, null),
                new TreeNode("e", null, null)));
    }

    /**
     * Like the Amoeba class, returns the height of the deepest node.
     **/
    public int height() {
        //YOUR CODE HERE

        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return 1;
        } else {
            return root.heightHelper2();
        }
    }

    public boolean isCompletelyBalanced() {

        if(root == null || (root.left == null && root.right == null)){
            return true;
        } else {
            return (root.left.heightHelper2() == root.right.heightHelper2() );
        }
    }

    public boolean check() {
        alreadySeen = new ArrayList();
        try {
            isOK(root);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public void isOK(TreeNode temp) throws IllegalStateException{

        if(temp != null){
            if(!alreadySeen.contains(temp)){

                alreadySeen.add(temp);

                isOK(temp.left);
                isOK(temp.right);

            }else{
                throw new IllegalStateException();
            }


        }
    }

    public static BinaryTree fibTree(int n) {
        BinaryTree result = new BinaryTree();

        if(n == 0){
            result.root = new TreeNode(0, null, null);
        }else if(n == 1){
            result.root = new TreeNode(1, null, null);
        }
        else{
            TreeNode temp = new TreeNode(null, null, null);
            temp = temp.treeNode(n);
            result.root = temp;
        }

        return result;
    }

    public static BinaryTree exprTree(String s) {
        BinaryTree result = new BinaryTree();
        result.root = result.exprTreeHelper((s));
        return result;
    }

    public void print() {
        if (root != null) {
            root.print(0);
        }
    }

    public void optimize(){

        BinaryTree tree = this;

        root = root.optHelper();

    }


    /**
     * Creates two BinaryTrees and prints them out in inorder
     */
    public static void main(String[] args) {

        BinaryTree temp = exprTree("(a+(3+5))");

        temp.optimize();

        temp.print();

//        TreeNode test = new TreeNode("34", null, null);

    }

    // Return the tree corresponding to the given arithmetic expression.
// The expression is legal, fully parenthesized, contains no blanks,
// and involves only the operations + and *.
    private TreeNode exprTreeHelper(String expr) {
        if (expr.charAt(0) != '(') {
            return new TreeNode(expr);
        } else {
            // expr is a parenthesized expression.
            // Strip off the beginning and ending parentheses,
            // find the main operator (an occurrence of + or * not nested
            // in parentheses, and construct the two subtrees.
            int count = 0;
            int opPos = 0;
            for (int k = 1; k < expr.length() - 1; k++) {

                if(expr.charAt(k) == '('){
                    count++;
                }else if(expr.charAt(k) == ')'){
                    count--;
                }

                if(k != expr.length()-2 && count == 0){
                    opPos = k;
                }
            }

            String opnd1 = expr.substring(1, opPos);
            String opnd2 = expr.substring(opPos + 1, expr.length() - 1);
            String op = expr.substring(opPos, opPos + 1);
            System.out.println("expression = " + expr);
            System.out.println("operand 1  = " + opnd1);
            System.out.println("operator   = " + op);
            System.out.println("operand 2  = " + opnd2);
            System.out.println();
//                return null;
            return new TreeNode(op, exprTreeHelper(opnd1), exprTreeHelper(opnd2));
        }
    }

    /**
     * Prints out the contents of a BinaryTree with a description in both
     * preorder and inorder
     * @param t           the BinaryTree to print out
     * @param description a String describing the BinaryTree t
     */
    private static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    /**
     * A TreeNode is a Node this BinaryTree
     * Note: this class is public in this lab for testing purposes.
     * However, in professional settings as well as the rest of
     * your labs and projects, we recommend that you keep your
     * classes private.
     */
    public static class TreeNode {

        /**
         * item is the item that is contained in this TreeNode
         * left is the left child of this TreeNode
         * right is the right child of this TreeNode
         */
        public Object item;
        public TreeNode left;
        public TreeNode right;

        /**
         * A TreeNode constructor that creates a node with obj as its item
         * @param  obj the item to be contained in this TreeNode
         */
        TreeNode(Object obj) {
            item = obj;
            left = null;
            right = null;
        }

        /**
         * A TreeNode constructor that creates a node with obj as its item and
         * left and right as its children
         * @param  obj   the item to be contained in this TreeNode
         * @param  left  the left child of this TreeNode
         * @param  right the right child of this TreeNode
         */
        TreeNode(Object obj, TreeNode left, TreeNode right) {
            item = obj;
            this.left = left;
            this.right = right;
        }

        public TreeNode optHelper(){

            TreeNode temp = this;


            String op = (String)temp.item;

            if(!validateInt(op)){
                if(validateInt(temp.right.getItem().toString()) && validateInt(temp.left.getItem().toString())){
                    int result = 0;

                    if(op.equals("*")){
                        result = Integer.parseInt(temp.right.getItem().toString())
                                * Integer.parseInt(temp.left.getItem().toString());
                    }else if(op.equals("+")){
                        result = Integer.parseInt(temp.right.getItem().toString())
                                + Integer.parseInt(temp.left.getItem().toString());
                    }

                    temp.item = result + "";
                    temp.right = null;
                    temp.left = null;

                }else if(!validateInt(temp.right.getItem().toString())){
                    temp.right.optHelper();

                }else if(!validateInt(temp.left.getItem().toString())){
                    temp.left.optHelper();
                }

            }else{
                return temp;
            }

            return temp;

        }

        public boolean validateInt(String s){

            try{
                int test = Integer.parseInt(s);
                return true;

            }catch(NumberFormatException e){

                return false;

            }
        }


        public TreeNode treeNode(int num){

            if(num == 0){
                return new TreeNode(0, null, null);
            }else if(num == 1){
                return new TreeNode(1, null, null);
            }else{
                int x = (int)treeNode(num-2).getItem();
                int y = (int)treeNode(num-1).getItem();
                return new TreeNode(x+y, treeNode(num-1), treeNode(num-2));
            }
        }

        public Object getItem() {
            return item;
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        /**
         * Prints this TreeNode and the subtree rooted at it in preorder
         */
        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        /**
         * Prints this TreeNode and the subtree rooted at it in inorder
         */
        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        public int heightHelper2() {



            int bestsofar = 1;

            if (this.left == null && this.right == null) {
                return bestsofar;
            } else if (this.left == null) {
                bestsofar = Math.max(this.right.heightHelper2() + 1, bestsofar);
            } else if (this.right ==null) {
                bestsofar = Math.max(this.left.heightHelper2() + 1, bestsofar);
            } else {
                bestsofar = Math.max(Math.max(this.left.heightHelper2() + 1, 1 + this.right.heightHelper2()) , bestsofar);
            }
            return bestsofar;

        }

        private static final String indent1 = "    ";

        private void print(int indent) {

            TreeNode temp = this;

            if(temp.right != null){
                temp.right.print(indent+1);
            }

            println (temp.item, indent);

            if(temp.left != null) {
                temp.left.print(indent + 1);
            }

        }

        private static void println(Object obj, int indent) {
            for (int k=0; k<indent; k++) {
                System.out.print(indent1);
            }
            System.out.println(obj);
        }
    }
}
