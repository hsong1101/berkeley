public class IntList {

    private int item;
    private IntList next;

    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    public IntList(int item) {
        this(item, null);
    }

    public static IntList list(int... items) {

        /* Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }

            /* Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;



            /* Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }


        return head;
    }

    public int item() {
        return item;
    }

    public IntList next() {
        return next;
    }

    public int get(int position){

        IntList list = this;

        if(position >= 0) {
            for (int i = 0; i < position; i++) {
                if (list.next != null) {
                    list = list.next();
                } else {
                    throw new IllegalArgumentException("Out of Bound");
                }
            }
        }else{
            throw new IllegalArgumentException("Cannot use negative position");
        }

        return list.item();
    }

    public int size(){
        int size;
        IntList list = this;

        if(this != null){
            size = 1;
            while(list.next != null){
                list = list.next;
                size++;
            }
        }else{
            size = 0;
        }

        return size;
    }

    public String toString(){

        String list = "( ";
        IntList tempList = this;

        for(int i = 0; i < size() - 1; i++){
            list = list + tempList.item() + " ";
            tempList = tempList.next();
        }

        list = list + get(size() - 1) + " )";

        return list;
    }

    public boolean equals(IntList list){

        int size = size();

        if(size == list.size()){
            for(int i = 0; i < size; i++){
                if(get(i) != list.get(i)){
                    System.out.println("not equal");
                    return false;
                }
            }
            return true;
        }else{
            System.out.println("size not matched");
            return false;
        }
    }

    public void add(int item){

        IntList last = this;

        while(last.next != null){
            last = last.next;
        }

        last.next = new IntList(item);
        last = last.next;
    }

    public int smallest(){
        int smallest = get(0);

        for(int i = 1; i < size(); i++){
            if(smallest > get(i)){
                smallest = get(i);
            }
        }
        return smallest;
    }

    public int squaredSum(){
        int sum = 0;

        for(int i = 0; i < size(); i++){
            sum += (get(i) * get(i));
        }
        return sum;
    }

    public static IntList append(IntList first, IntList second){
        IntList list = IntList.list(first.get(0));

        for(int i = 1; i < first.size(); i++){
            list.add(first.get(i));
        }

        for(int i = 0; i < second.size(); i++){
            list.add(second.get(i));
        }

        return list;
    }

}