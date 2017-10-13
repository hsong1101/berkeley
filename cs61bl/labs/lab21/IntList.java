/**
 * A doubly-linked list of integers supporting various sorting algorithms.
 * @author You
 */
public class IntList {

    /* The head of this IntList. */
    IntListNode head;
    /* The tail of this IntList. */
    IntListNode tail;
    /* The size or number of integers in this IntList. */
    int size;

    /**
     * Constructs an empty IntList.
     */
    public IntList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Constructs an IntList with one node. Head and tail are the same.
     */
    public IntList(IntListNode head) {
        this.head = this.tail = head;
        this.size = 1;
    }

    /**
     * Returns true if this list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds a new node with the given value to the front of this list.
     */
    public void addToFront(int k) {
        if (head == null) {
            head = tail = new IntListNode(k);
        } else {
            head = new IntListNode(k, null, head);
            head.next.prev = head;
        }
        size++;
    }

    /**
     * Adds a new node with the given value to the end of this list.
     */
    public void addToEnd(int k) {
        if (head == null) {
            head = tail = new IntListNode(k);
        } else {
            tail.next = new IntListNode(k, tail, null);
            tail = tail.next;
        }
        size++;
    }

    /**
     * Attaches the input list to the end of this list.
     */
    public void append(IntList list) {
        if (list.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            head = list.head;
            tail = list.tail;
            size = list.size;
            return;
        }
        tail.next = list.head;
        list.head.prev = tail;
        tail = list.tail;
        size += list.size;
    }

    /**
     * Removes the node reference by p from this list.
     */
    private void remove(IntListNode p) {
        if (head == tail) {
            head = tail = null;
        } else if (p == head) {
            head = head.next;
            head.prev = null;
        } else if (p == tail) {
            tail = tail.prev;
            tail.next = null;
        } else {
            p.next.prev = p.prev;
            p.prev.next = p.next;
        }
        size--;
    }

    @Override
    public String toString() {
        String s = "";
        for (IntListNode p = head; p != null; p = p.next) {
            s = s + p.item + " ";
        }
        return s;
    }

    private class IntListNode {

        int item;
        IntListNode prev;
        IntListNode next;

        public IntListNode(int k) {
            this.item = k;
            this.prev = this.next = null;
        }

        public IntListNode(int k, IntListNode prev, IntListNode next) {
            this.item = k;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * Returns the result of sorting the values in this list using the insertion
     * sort algorithm. This list is no longer usable after this operation; you
     * have to use the returned list.
     */
    public IntList insertionSort() {
        IntListNode soFar = null;
//        for (IntListNode p = head; p != null; p = p.next) {
            soFar = insert(head);

//        }

        return new IntList(soFar);
    }

    /**
     * Inserts the node p into the list headed by head so that the node values
     * are in increasing order.
     */
    private IntListNode insert(IntListNode head) {
        if (head == null || head.next == null)
            return head;

        IntListNode newHead = new IntListNode(head.item);
        IntListNode pointer = head.next;

        // loop through each element in the list
        while (pointer != null) {
            // insert this element to the new list

            IntListNode innerPointer = newHead;
            IntListNode next = pointer.next;

            if (pointer.item <= newHead.item) {
                IntListNode oldHead = newHead;
                newHead = pointer;
                newHead.next = oldHead;
            } else {
                while (innerPointer.next != null) {

                    if (pointer.item > innerPointer.item && pointer.item <= innerPointer.next.item) {
                        IntListNode oldNext = innerPointer.next;
                        innerPointer.next = pointer;
                        pointer.next = oldNext;
                    }

                    innerPointer = innerPointer.next;
                }

                if (innerPointer.next == null && pointer.item > innerPointer.item) {
                    innerPointer.next = pointer;
                    pointer.next = null;
                }
            }

            pointer = next;
        }

        return newHead;
    }

    /**
     * Sorts this list using the selection sort algorithm.
     */
    public void selectionSort() {
        IntList sorted = new IntList();
        while (head != null) {
            int minSoFar = head.item;
            IntListNode minPtr = head;
            // Find the node in the list pointed to by head
            // whose value is largest.
            for (IntListNode p = head; p != null; p = p.next) {
                if (p.item < minSoFar) {
                    minSoFar = p.item;
                    minPtr = p;
                }
            }
            sorted.addToEnd(minSoFar);
            remove(minPtr);
        }
        head = sorted.head;
    }

    /**
     * Returns the result of sorting the values in this list using the Quicksort
     * algorithm. This list is no longer usable after this operation.
     */
    public IntList quicksort() {
        if (size <= 1) {
            return this;
        }
        // Assume first element is the divider.
        IntList smallElements = new IntList();
        IntList largeElements = new IntList();
        int divider = head.item;
        IntListNode temp = head;

        while(temp.next != null) {
            if(temp.item > divider) {
                largeElements.addToEnd(temp.item);
            } else {
                smallElements.addToEnd(temp.item);
            }
            temp = temp.next;
        }
        if(temp.item > divider) {
            largeElements.addToEnd(temp.item);
        } else {
            smallElements.addToEnd(temp.item);
        }

        IntList list = merge(smallElements.head, largeElements.head);

        IntListNode newHead = insert(list.head);

        return new IntList(newHead);
    }

    /**
     * Returns the result of sorting the values in this list using the merge
     * sort algorithm. This list is no longer usable after this operation.
     */
    public IntList mergeSort() {
        if (size <= 1) {
            return this;
        }

        IntList oneHalf = new IntList();
        IntList otherHalf = new IntList();
        int middle = size / 2;


        IntListNode temp = head;

        for (int i = 0; i < middle; i++) {
            oneHalf.addToEnd(temp.item);
            temp = temp.next;
        }

        for (int i = middle; i < size; i++) {
            otherHalf.addToEnd(temp.item);
            temp = temp.next;
        }

        IntList temp1 = new IntList(insert(oneHalf.head));
        IntList temp2 = new IntList(insert(otherHalf.head));


        return merge(temp1.head, temp2.head);
    }

    /**
     * Returns the result of merging the two sorted lists / represented by list1
     * and list2.
     */
    private static IntList merge(IntListNode list1, IntListNode list2) {
        IntList rtn = new IntList();
        while (list1 != null && list2 != null) {
            if (list1.item < list2.item) {
                rtn.addToEnd(list1.item);
                list1 = list1.next;
            } else {
                rtn.addToEnd(list2.item);
                list2 = list2.next;
            }
        }
        while (list1 != null) {
            rtn.addToEnd(list1.item);
            list1 = list1.next;
        }
        while (list2 != null) {
            rtn.addToEnd(list2.item);
            list2 = list2.next;
        }
        return rtn;
    }

    /**
     * Returns a random integer between 0 and 99.
     */
    private static int randomInt() {
        return (int) (100 * Math.random());
    }

    public static void main(String[] args) {
        IntList values;
        IntList sortedValues;

        values = new IntList();
        System.out.print("Before merge sort: ");
        values.addToFront(0);
        values.addToFront(0);
        values.addToFront(1);
        values.addToFront(2);
        values.addToFront(3);
        values.addToFront(4);
        values.addToFront(5);
        values.addToFront(5);
        values.addToFront(6);
        values.addToFront(7);
        values.addToFront(8);
        values.addToFront(9);
//
        System.out.println(values);
        sortedValues = values.quicksort();
        System.out.print("After quicksort: ");
        System.out.println(sortedValues);
//

    }

}
