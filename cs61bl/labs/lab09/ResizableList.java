/**
 * Created by carlitoswillis on 7/6/17.
 */
public class ResizableList extends FixedSizeList  {
    /**
     * Initializes a FixedSizeList with specified capacity. The capacity is the
     * the actual size of the array (i.e. the max number of items it can hold).
     *
     * init capacity as 1
     */
    public ResizableList() {
        super(1);
    }

    @Override
    public void add(int a) {
        if (count == values.length) {
            int[] temp = values.clone();
            values = new int[values.length * 2];
            for (int i = 0; i < temp.length; i++) {
                values[i] = temp[i];
            }
        }

        values[count] = a;
        count++;
    }

    @Override
    public void add(int i, int k) {
        if (count == values.length) {
            int[] temp = values.clone();
            values = new int[values.length * 2];
            for (int j = 0; j < temp.length; j++) {
                values[j] = temp[j];
            }
        }

        if(i >= 0 && i <= count) {
            for (int j = values.length - 1; j >= i; j--) {
                values[j] = values[j - 1];
            }
        }


        values[i] = k;
        count++;



    }
}
