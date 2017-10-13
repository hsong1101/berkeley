public class ModNCounter extends Counter{

    int m;

    public ModNCounter(int m) {
        this.m = m;
    }

    public int value(){
        return super.value() % m;
    }


    public static void main(String[] args) {


        ModNCounter m = new ModNCounter(3);
        m.increment();
        m.increment();
        m.increment();
        m.increment();
        m.increment();
        System.out.println(m.value());

        m.reset();
        System.out.println(m.value());

    }
}
