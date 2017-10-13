import java.util.ArrayList;

/**
 *  A simple mapping from string names to string values backed by an array.
 *  Supports only A-Z for the first character of the key name. Values can be
 *  any valid string.
 *
 *  @author You
 */
public class SimpleNameMap {

    public ArrayList<Entry> entry;

    public SimpleNameMap(){
        entry = new ArrayList<>((int)'z' - 'A');

        for(int i = 0; i < (int) 'z' - 'A'; i++){
            entry.add(null);
        }
    }

    /** A wrapper class for holding each (KEY, VALUE) pair. */
    private static class Entry {

        /** The key used for lookup. */
        private String _key;
        /** The associated value. */
        private String _value;

        private Entry _next;
        /** Create a new (KEY, VALUE) pair. */
        public Entry(String key, String value) {
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

    /** Returns true if the map contains the KEY. */
    public boolean containsKey(String key){

        if (isValidName(key)){
            if(entry.get((hash(key) % entry.size())) == null) {
                return false;
            }else {
                Entry temp = entry.get((hash(key) % entry.size()));

                if(temp._key.equals(key)){
                    return true;
                } else {
                    while (temp._next != null) {
                        if(temp._next._key.equals(key)){
                            return true;
                        } else {
                            temp = temp._next;
                        }
                    }
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /** Returns the value for the specified KEY. */
    public String gethelp(String key, Entry entrytemp) {

        if (containsKey(key)) {
            if (entrytemp._key == key) {
                return entrytemp._value;
            } else if (entrytemp._next == null) {
                return null;
            } else {
                return gethelp(key, entrytemp._next);
            }
        }

        return null;
    }

    /** Returns the value for the specified KEY. */
    public String get(String key){

        if(containsKey(key)){
            if (entry.get((hash(key) % entry.size()))._key.equals(key)) {
                return entry.get((hash(key) % entry.size()))._value;
            } else {
                return gethelp(key, entry.get((hash(key) % entry.size()))._next);
            }

        }

        return null;
    }

    /** Put a (KEY, VALUE) pair into this map. */
    public void put(String key, String value){

        if (isValidName(key) && !containsKey(key)) {

            Entry newentrytemp = new Entry(key, value);

            if (entry.get((hash(key) % entry.size())) == null){
                entry.add((hash(key) % entry.size()), newentrytemp);
            } else {

                Entry temp = entry.get((hash(key) % entry.size()));
                while(temp._next != null){
                    temp = temp._next;
                }

                temp._next = newentrytemp;
            }


        }else if(containsKey(key)){
            throw new IllegalArgumentException("It already has the key");
        }
    }

    /** Remove a single entry, KEY, from this table and return the VALUE if successful or NULL otherwise. */
    public String remove(String key){
//
//        if(isValidName(key) && containsKey(key)){
//            String value = get(key);
//            entry.remove((hash(key) % entry.size()));
//            return value;
//        }
//
        return null;
    }
    public int hash(String key){
        return (int) (key.charAt(0) - 'A');
    }

    /** Returns true if the given KEY is a valid name that starts with A-Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    public static void main(String ...args){

        SimpleNameMap test = new SimpleNameMap();

        test.put("A", "some weird value1");
        test.put("B", "some weird value1b");
        test.put("C", "some weird value2");
        test.put("D", "some weird value3");
        test.put("E", "some weird value4");
        test.put("F", "some weird value5");
        test.put("G", "some weird value6");
        test.put("H", "some weird value7");
        test.put("I", "some weird value33");
        test.put("Ab", "some weird value331");
        test.put("HHHH", "some weird value332");
        test.put("HHHHHHAb", "some weird value333");
        test.put("Hi", "some weird value334");

        System.out.println(test.get("Hi"));


//        System.out.println(test.entry.size());
//        System.out.println(test.entry.size());

    }

}