public class FixedSizeList implements SimpleList {

    /** List of elements. */
    protected int[] values;
    /** Number of array cells used by the list. */
    int count;

    /** Initializes a FixedSizeList with specified capacity. The capacity is the
     *  the actual size of the array (i.e. the max number of items it can hold).
     */
    public FixedSizeList(int capacity) {
        values = new int[capacity];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return count;
    }

    /** Returns true if the list is empty, else false. */
    public boolean isEmpty() {
        if(count <= 0){
            return true;
        }
        return false;
    }

    /** Adds the int k to the list by placing it in the first unused spot in
     *  values.
     */
    public void add(int k) {
        if(count >= 0 && count < values.length) {
            values[count] = k;
            count++;
        }else{
            throw new ListException("nope");
        }
    }

    /** Removes k from the list if it is present. If k appears multiple times,
     *  it only removes the first occurence of k.
     */

    /** Removes the entry at position i by shifting each element after position
     *  i one entry to the left.
     */
    public void removeIndex(int i) {

        if(i >= 0 && i < count){
            for(int j = i; j < count - 1; j++){
                values[j] = values[j + 1];

            }
            values[count - 1] = 0;
            count--;
        }else{
            throw new IllegalArgumentException("Nope");
        }

    }


    public void remove(int k) {
        for(int i = 0; i < size(); i++){
            if(values[i] == k){
                removeIndex(i);
                break;
            }
        }
    }

    /** Returns if the collection contains k. */
    public boolean contains(int k) {
        for(int i = 0; i < count; i++){
            if(values[i] == k){
                return true;
            }
        }
        return false;
    }

    /** Returns the integer stored at the i-th index in the list. */
    public int get(int i) {
        if(i >= 0 && i < size()){
            return values[i];
        }
        return 0;
    }

    /** Inserts k into the list at position i by shifting each element at index
     *  i and onwards one entry to the right.
     *  Precondition: i is between 0 and count, inclusive.
     */
    public void add(int i, int k) {
        if(i >= 0 && i <= count) {
            for (int j = values.length - 1; j > i; j--) {
                values[j] = values[j - 1];
            }
            values[i] = k;
            count++;
        }else{
            throw new IllegalArgumentException("Nope");
        }

    }

    public static void main(String arg[]){
        FixedSizeList list = new FixedSizeList(10);

        list.add(0,0);
        list.add(1,1);
        list.add(2,2);
        list.add(3,3);
        list.add(4,4);
        list.add(5,5);
        list.add(6,6);
        list.add(7,7);
        list.add(8,8);
        list.add(9,9);


        list.removeIndex(4);
        list.removeIndex(0);
        list.removeIndex(4);
        list.remove(7);
        list.remove(12);

        list.add(0, 12);

        for(int i = 0; i < list.count; i++){
            System.out.print(" " + list.get(i));
        }


    }



}
