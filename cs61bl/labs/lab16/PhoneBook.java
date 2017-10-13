import java.util.HashMap;

public class PhoneBook {
    // TODO Add any instance variables necessary

    HashMap<Person, PhoneNumber> phoneBook = new HashMap<>();
    /*
     * Adds a person with this name to the phone book and associates 
     * with the given PhoneNumber.
     */
    public void addEntry(Person personToAdd, PhoneNumber numberToAdd){

        phoneBook.put(personToAdd, numberToAdd);

    }

    /*
     * Access an entry in the phone book. 
     */
    public PhoneNumber getNumber(Person personToLookup){

        if(phoneBook.containsKey((personToLookup))){

            if(personToLookup.getChanged()){
                return null;
            }
            return phoneBook.get(personToLookup);
        }else {
            return null;
        }
    }



    public static void main(String... args){
        PhoneBook book = new PhoneBook();

        Person person = new Person("Test");
        PhoneNumber num = new PhoneNumber("1234567890");
//        PhoneNumber num2 = new PhoneNumber("0987654321");

        book.addEntry(person, num);
        System.out.println(book.getNumber(person));

        num = new PhoneNumber("0987654321");

        book.addEntry(person, num);
        System.out.println(book.getNumber(person));


    }

}
