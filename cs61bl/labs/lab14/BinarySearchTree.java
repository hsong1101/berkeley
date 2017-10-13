
/** A class implementing a BST.
  * @author CS 61BL Staff.
  */
public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

	/** Constructs an empty BST. */
	public BinarySearchTree() {

		root = null;

	}

	/** BST Constructor.
         *  @param root node to use for constructing new BST.
         */
	public BinarySearchTree(TreeNode root) {

		this.root = root;

	}

	/** Method for checking if BST has a given key.
         *  @param  key to search for
         *  @return true if this BST contains key
         *          false if this BST does not contain key
         */
	public boolean contains(T key) {

		if(root != null){
			return containHelper(root, key);
		}else{
			return false;
		}

	}

	public boolean containHelper(TreeNode temp, T key){
		if (temp.item.equals(key)) {
			return true;
		} else {
			if (temp.right != null && temp.left != null) {
				return containHelper(temp.right, key) || containHelper(temp.left, key);
			} else if (temp.right != null) {
				return containHelper(temp.right, key);
			} else if (temp.left != null) {
				return containHelper(temp.left, key);
			}

			return false;
		}
	}

	/** Adds a node for KEY iff it isn't in the BST already.
         *  @param key to be added
         */
	public void add(T key) {

		if(root != null){
			if(!containHelper(root, key)) {
				addHelper(root, key);
			}
		}else{
			root = new TreeNode(key, null, null);
		}
	}



	public void addHelper(TreeNode temp, T key){

		if(temp != null) {
			if (temp.item.compareTo(key) == 1) {
				if(temp.left == null){
					temp.left = new TreeNode(key);
				}else if(temp.left != null){
					addHelper(temp.left, key);
				}
			} else if (temp.item.compareTo(key) == -1) {
				if(temp.right == null){
					temp.right = new TreeNode(key);
				}else if(temp.right != null) {
					addHelper(temp.right, key);
				}
			}
		}else{
			temp = new TreeNode(key);
		}
	}

	/** Deletes a node from the BST.
         *  @param  key to be deleted
         *  @return key that was deleted
         */
	public T delete(T key) {
		TreeNode parent = null;
		TreeNode curr = root;
		TreeNode delNode = null;
		TreeNode replacement = null;
		boolean rightSide = false;
		
		while (curr != null && !curr.item.equals(key)) {
			if (((Comparable<T>) curr.item).compareTo(key) > 0) {
				parent = curr;
				curr = curr.left;
				rightSide = false;
			} else {
				parent = curr;
				curr = curr.right;
				rightSide = true;
			}
		}
		delNode = curr;
		if (curr == null) {
			return null;
		}
		
		if (delNode.right == null) {
			if (root == delNode) {
				root = root.left;
			} else {
				if (rightSide) {
					parent.right = delNode.left;
				} else {
					parent.left = delNode.left;
				}
			}
		} else {
			curr = delNode.right;
			replacement = curr.left;
			if (replacement == null) {
				replacement = curr;
			} else {
				while (replacement.left != null) {
					curr = replacement;
					replacement = replacement.left;
				}
				curr.left = replacement.right;
				replacement.right = delNode.right;
			}
			replacement.left = delNode.left;
			if (root == delNode) {
				root = replacement;
			} else {
				if (rightSide) {
					parent.right = replacement;
				} else {
					parent.left = replacement;
				}
			}
		}
		return delNode.item;
	}
}
