import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hsong1101 on 7/7/2017.
 */
public class FixedSizeListTest {

    @Test
    public void removeIndex() throws Exception {
        FixedSizeList list = new FixedSizeList(10);
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);

        list.removeIndex(0);
        list.removeIndex(3);

        int[] test = {1, 2, 3, 5, 6, 7, 8, 9, 0, 0};

        assertArrayEquals(test, list.values);


        list.remove(5);

        int[] test3 = {1, 2, 3, 6, 7, 8, 9, 0, 0, 0};
        assertArrayEquals(test3, list.values);

        list.add(1, 12);
        int[] test2 = {1, 12, 2, 3, 6, 7, 8, 9, 0, 0};
        assertArrayEquals(test2, list.values);

        assertEquals(7, list.get(5));
        assertEquals(9, list.get(7));

        list.add(3,12);

        assertEquals(12, list.get(3));

        assertEquals(9, list.count);

        try {
            list.add(12, 0);
            list.removeIndex(13);
            fail();
        }catch(Exception e){

        }

        list.removeIndex(0);
        list.removeIndex(0);
        list.removeIndex(0);
        list.remove(3);
        list.removeIndex(0);
        list.removeIndex(0);
        list.removeIndex(0);
        list.remove(8);
        list.remove(9);

        int[] test4 = {0,0,0,0,0,0,0,0,0,0};
        assertArrayEquals(test4, list.values);
    }

    @Test
    public void remove() throws Exception {
    }

}