public class Counter {

    private int count;

    public Counter() {

    }

    public void increment() {
        count++;
    }

    public void reset() {
        count = 0;
    }

    public int value() {
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }
}