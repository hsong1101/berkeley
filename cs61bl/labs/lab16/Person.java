public class Person {

	private String myName;
	private boolean changed = false;

	public Person(String name) {
		this.myName = name;
	}

	// return a String representation of the Person object
	public String toString() {
		return myName;
	}

	// Change the name of the person
	public void changeName(String newName) {
		this.myName = newName;
		changed = true;
	}

	public String getName(){
		return myName;
	}

	public boolean getChanged(){
		return changed;
	}

	@Override
	public boolean equals(Object obj) {
		Person person = (Person) obj;

		if(myName.equals(person.getName())){
			return true;
		}
		return false;
	}

	// TODO add additional methods
	
}
