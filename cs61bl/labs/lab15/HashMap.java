import java.util.Iterator;


public class HashMap<K, V> implements Map61BL<K, V> {

    // make array of Entrys
    private int arrayLength ;
    private int size;
    public float loadFactor ;// = (float) 0.75;
    public Entry[] list;

    public HashMap(){
        this.arrayLength = 16;
        list = new Entry[this.arrayLength];
        loadFactor = (float) 0.75;
        size = 0;
    }

    /** Create a new hash map with an array of size INITIALCAPACITY and default load factor of 0.75. */
    HashMap(int initCap){
        list = new Entry[initCap];
        this.arrayLength = initCap;
        this.loadFactor = (float) 0.75;
        this.size = 0;
    }

    /** Create a new hash map with INITIALCAPACITY and LOADFACTOR. */
    HashMap(int initCap, float loadFactor){
        this.arrayLength = initCap;
        list = new Entry[initCap];
        this.loadFactor = loadFactor;
        this.size = 0;

    }

    /** A wrapper class for holding each (KEY, VALUE) pair. */
    private static class Entry <K,V> {

        /** The key used for lookup. */
        private K _key;
        /** The associated value. */
        private V _value;
        /** The next Entry in the bucket */
        private Entry _next;

        /** Create a new (KEY, VALUE) pair. */
        public Entry(K key, V value) {
            _key = key;
            _value = value;
            _next = null;
        }

        /** Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return _key.equals(other._key);
        }

        /** Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry &&
                    _key.equals(((Entry) other)._key) &&
                    _value.equals(((Entry) other)._value));
        }


    }

    /** Returns true if the given KEY is a valid name that starts with A-Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    public int myHashCode(K key) {
        int hash = key.hashCode();
        return (hash & 0x7FFFFFFF ) % list.length;
    }

    /** Returns true if the map contains the KEY. */
    public boolean containsKey(K key){

        if (list[myHashCode(key)] != null) {
            Entry tempEntry = list[this.myHashCode(key)];
            while (tempEntry != null){
                if ( tempEntry._key.equals(key)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key){

        Entry temp = list[this.myHashCode(key)];

        while (temp != null){
            if ( temp._key.equals(key)){
                return (V) temp._value;
            } else{
                temp = temp._next;
            }
        }

        return null;
    }

    /** Put a (KEY, VALUE) pair into this map. */
    public void put(K key, V value){
        // if currLoad > load factor -> resize
        if (loadFactor < (float)(this.size()/ list.length)) {
            resizeArray();
        }
        //  Entry currBucKet = list[this.myHashCode(key)];
        if(list[this.myHashCode(key)] == null){
            list[this.myHashCode(key)] = new Entry(key, value);
        } else {
            Entry currBucKet = list[this.myHashCode(key)];
            while (currBucKet != null) {
                if (currBucKet._key == key) {
                    currBucKet._value = value;
                    return;
                } else if (currBucKet._next != null) {
                    currBucKet = currBucKet._next;
                } else {
                    break;
                }
            }
            currBucKet._next = new Entry(key, value);
        }
        size++;
        loadFactor = (float)(this.size / list.length);
    }

    /**
     * Remove and return a key and its associated value. If you don't
     * implement this, throw an UnsupportedOperationException.
     */
    public V remove(K key){
        if (list[this.myHashCode(key)]._next != null) {
            //iterate
            Entry tempEntry = list[this.myHashCode(key)];
            Entry tempEntryPrev  = list[this.myHashCode(key)];
            do {
                if (tempEntry._key.equals(key)) {
                    // cut
                    tempEntryPrev._next = tempEntryPrev._next._next;
                    // possible BUG if first entry in chain
                }
                tempEntryPrev = tempEntry;
                tempEntry = tempEntry._next;
            } while (tempEntry._next != null);
            size--;
            this.loadFactor = (float)this.size/ (float) list.length;
            return (V) tempEntry._value; // cast to V
        } else {
            V val = get(key);
            list[myHashCode(key)] = null;
            size--;
            this.loadFactor = (float)this.size/ (float) list.length;
            return val;
        }
    }

    public void resizeArray() {

        Entry[] tempList = new Entry[list.length * 2];

        Entry tempEntry;

        for (int i = 0; i < list.length; i++){
            tempEntry = list[i];
            while(tempEntry != null){
                int hash = tempEntry._key.hashCode() % tempList.length;
                if(tempList[hash] == null){
                    tempList[hash] = new Entry(tempEntry._key, tempEntry._value);
                } else {
                    Entry temp = tempList[hash];
                    while (temp != null) {
                        if (temp._key == tempEntry._key) {
                            temp._value = tempEntry._value;
                            return;
                        } else if (temp._next != null) {
                            temp = temp._next;
                        }
                    }
                    temp._next = new Entry(tempEntry._key, tempEntry._value);
                }

            }
        }
        list = tempList;
    }

    public int capacity(){
        return list.length;
    }

    /** Removes all of the mappings from this map. */
    public void clear(){
        list = new Entry[this.capacity()];
        size = 0;
    }

    /** Returns an Iterator over the keys in this map. */
    public Iterator<K> iterator(){
        return new HashMapIterator(list);
    }

    public class HashMapIterator implements Iterator<K>{
        Entry[] arrEntry;
        Entry currEntry;
        Entry nextEntry;
        int indMyList;
        public HashMapIterator(Entry[] entryArr){
            this.arrEntry = entryArr;
            this.indMyList = 0;
            nextEntry = entryArr[indMyList];
        }
        public boolean hasNext(){
            while(indMyList < list.length){
                if(nextEntry != null){
                    currEntry = nextEntry;
                    nextEntry = nextEntry._next;
                    return true;
                } else {
                    indMyList++;
                    if(indMyList == list.length){
                        return false;
                    } else {
                        nextEntry = list[indMyList];
                    }
                }
            }

            return false;
        }
        public K next(){
            return (K) currEntry._key;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Remove a particular key-value mapping and return true if successful.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    public boolean remove(K key, V value){
        throw new UnsupportedOperationException();
    }

    /** Returns the number of key-value mappings in this map. */
    public int size(){
        return this.size;
    }

}