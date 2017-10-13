/** Represent a set of nonnegative ints from 0 to maxElement - 1 for some
 *	initially specified maxElement.
 */
public class BooleanSet implements SimpleSet {

	/** contains[k] is true if k is in the set, false if it isn't. */
	private boolean[] contains;

	/** Initializes a set of ints from 0 to maxElement - 1. */
	public BooleanSet (int maxElement) {
		contains = new boolean[maxElement];
	}

	/** Adds k to the set.
	 *	precondition: 0 <= k < maxElement.
	 *	postcondition: k is in this set.
	 */
	public void add(int k) {
		if(k >= 0 && k < contains.length) {
			if (contains[k] == false) {
				contains[k] = true;
			}
		}else{
			throw new ArrayIndexOutOfBoundsException("NOOOO");
		}
	}

	/** Removes k from the set.
	 *	precondition: 0 <= k < maxElement.
	 *	postcondition: k is not in this set.
	 */
	public void remove(int k) {
		if(contains[k] == true){
			contains[k] = false;
		}
	}

	/** Return true if k is in this set, false otherwise.
	 *	precondition: 0 <= k < maxElement
	 */
	public boolean contains (int k) {
		return contains[k];
	}

	/** Return true if this set is empty, false otherwise. */
	public boolean isEmpty() {
		for(int i = 0; i < contains.length; i++){
			if(contains[i] == true){
				return false;
			}
		}
		return true;
	}

	/** Returns the number of items in the set. */
	public int size() {
		int size = 0;

		for(int i = 0; i < contains.length; i++){
			if(contains[i] == true){
				size++;
			}
		}

		return size;
		// YOUR CODE HERE
	}
}
