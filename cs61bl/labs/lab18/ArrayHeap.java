import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
  * store Comparable objects. Instead, it can store any type of object
  * (represented by type T) and an associated priority value.
  * @author CS 61BL Staff */
public class ArrayHeap<T> {

	/*
	ACTUALLY READ THESE METHODS! MAKE SURE YOU KNOW WHAT EACH ONE DOES.
	DO NOT CHANGE THESE METHODS. */

	/* An ArrayList that stores the nodes in this binary heap. */
	private ArrayList<Node> contents;

	/* A constructor that initializes an empty ArrayHeap. */
	public ArrayHeap() {
		contents = new ArrayList<>();
		contents.add(null);
	}

	/* Returns the node at index INDEX. */
	private Node getNode(int index) {
		if (index >= contents.size()) {
			return null;
		} else {
			return contents.get(index);
		}
	}

	private void setNode(int index, Node n) {
		// In the case that the ArrayList is not big enough
		// add null elements until it is the right size
		while (index + 1 >= contents.size()) {
			contents.add(null);
		}
		contents.set(index, n);
	}

	/* Swap the nodes at the two indices. */
	private void swap(int index1, int index2) {
		Node node1 = getNode(index1);
		Node node2 = getNode(index2);
		this.contents.set(index1, node2);
		this.contents.set(index2, node1);
	}

	/* Prints out the heap sideways. Use for debugging. */
	@Override
	public String toString() {
		return toStringHelper(1, "");
	}

	/* Recursive helper method for toString. */
	private String toStringHelper(int index, String soFar) {
		if (getNode(index) == null) {
			return "";
		} else {
			String toReturn = "";
			int rightChild = getRightOf(index);
			toReturn += toStringHelper(rightChild, "        " + soFar);
			if (getNode(rightChild) != null) {
				toReturn += soFar + "    /";
			}
			toReturn += "\n" + soFar + getNode(index) + "\n";
			int leftChild = getLeftOf(index);
			if (getNode(leftChild) != null) {
				toReturn += soFar + "    \\";
			}
			toReturn += toStringHelper(leftChild, "        " + soFar);
			return toReturn;
		}
	}

	/* A Node class that stores items and their associated priorities. */
	public class Node {
		private T item;
		private double priority;

		private Node(T item, double priority) {
			this.item = item;
			this.priority = priority;
		}

		public T item(){
			return this.item;
		}

		public double priority() {
			return this.priority;
		}

		@Override
		public String toString() {
			return this.item.toString() + ", " + this.priority;
		}
	}


	private int getLeftOf(int i) {
		return i * 2;
		//YOUR CODE HERE
	}

	private int getRightOf(int i) {
		return i * 2 + 1;
		//YOUR CODE HERE
	}

	private int getParentOf(int i) {
		return i / 2;
		//YOUR CODE HERE
	}

	private void setLeft(int index, Node n) {
		setNode(getLeftOf(index), n);
	}

	private void setRight(int index, Node n) {
	    setNode(getRightOf(index), n);
	}

	private int min(int index1, int index2) {
	    if(getNode(index1) != null && getNode(index2) != null){
	        if(getNode(index1).priority >= getNode(index2).priority){
	            return index2;
            }else{
	            return index1;
            }
        }else if(getNode(index2) == null){
	        return index1;
        }else if(getNode(index1) == null){
            return index2;
        }

        return 0;
	}

	public Node peek() {

	    if(contents.size() > 1) {
            Node temp = getNode(1);

            for (int i = 2; i < contents.size(); i++) {
                if ((getNode(i) != null && temp != null) && temp.priority() > getNode(i).priority()) {
                    temp = getNode(i);
                }
            }


            return temp;
        }

        return null;
	}

	private boolean bubbleUp(int index) {

	    if(getNode(index) != null && index < contents.size()) {

	        if(getNode(getParentOf(index)) != null) {

                int parent = getParentOf(index);
                int child = index;

                swap(parent, child);
                return true;
                }
            }
        return false;
	}

	private boolean bubbleDown(int index) {
	    if(getNode(index) != null && index < contents.size()) {

            int given = index;
            int left = getLeftOf(index);
            int right = getRightOf(index);

            if(getNode(left) != null && getNode(right) != null) {

                if (min(given, left) == left) {
                    swap(given, left);
                    return true;
                } else if (min(given, right) == right) {
                    swap(given, right);
                    return true;
                }
            }
        }
        return false;
	}

	public void insert(T item, double priority) {

        Node temp = new Node(item, priority);

        contents.add(temp);

        int index = contents.indexOf(temp);
        bubbleUpHelper(index);
	}

	public void bubbleUpHelper(int child){
	    if(child > 0){
	        int parent = getParentOf(child);
	        if(min(child, parent) == child){
	            int index = parent;
	            bubbleUp(child);
	            bubbleUpHelper(index);

            }
        }
    }

	public Node removeMin() {

	    if(peek() != null) {
	        //create a node for the min
            Node min = peek();
            T item = min.item;
            double priority = min.priority;
            Node temp = new Node(item, priority);

            int minIndex = contents.indexOf(min);

            Node lastNode = contents.get(contents.size() - 1);

            swap(minIndex, contents.indexOf(lastNode));

            contents.remove(contents.size () -1);

            bubbleDownHelper(minIndex);

            return temp;
        }
        return null;
	}

	public void bubbleDownHelper(int index){

        Node temp = getNode(index);

        if(bubbleDown(index)){
            int ind = contents.indexOf(temp);
                bubbleDownHelper(ind);
        }





    }

	/* Changes the node in this heap with the given item to have the given
	 * priority. You can assume the heap will not have two nodes with the same
	 * item. Check for item equality with .equals(), not == */
	public void changePriority(T item, double priority) {
		for(int i = 0; i < contents.size(); i++){
		    if(getNode(i) != null && getNode(i).item.equals(item)){
		        getNode(i).priority = priority;
            }
        }
	}

    public static void main(String[] args){
        ArrayHeap<Integer> list = new ArrayHeap<>();

        list.insert(1, 1);
        list.insert(2, 2);
        list.insert(3, 3);
        list.insert(4, 4);
        list.insert(4, 5);
        list.insert(4, 6);
        list.insert(4, 7);

        list.removeMin();

        for(int i = 1; i < list.contents.size(); i++){
            System.out.println(list.getNode(i));
        }

    }

}
