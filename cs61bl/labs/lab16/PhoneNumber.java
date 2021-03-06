public class PhoneNumber {

    private String number;
    private boolean changed = false;

    /*
     * Constructor takes a String that represents a phone number. The String
     * should contain numbers only and should be of length 9.
     */
    public PhoneNumber(String num) {
        if (num.length() != 10) {
            throw new IllegalArgumentException("phone number was not 10 numbers");
        }
        this.number = num;
    }

    public void changeNumber(String num){
        if (num.length() != 10) {
            throw new IllegalArgumentException("phone number was not 10 numbers");
        }
        this.number = num;
        changed = true;
    }

    public boolean getChanged(){
        return changed;
    }

    @Override
    public String toString() {
        return number;
    }

    public String getNumber(){
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        PhoneNumber num = (PhoneNumber) obj;
        if(this.number.equals(num.getNumber())){
            return true;
        }
        return false;
    }
    // TODO Add additional methods?

}
