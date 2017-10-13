package gitlet;

import org.junit.Test;
import ucb.junit.textui;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() throws Exception {

        //setup
//        String[] arg = {"init"};
//        Main.main(arg);
//        String[] arg1 = {"add", "g.txt"};
//        Main.main(arg1);
//        String[] arg2 = {"add", "f.txt"};
//        Main.main(arg2);
//        String[] arg3 = {"commit", "commit1"};
//        Main.main(arg3);
//        String[] arg4 = {"branch", "other"};
//        Main.main(arg4);
//
//        String[] arg5 = {"add", "h.txt"};
//        Main.main(arg5);
//        String[] arg6 = {"rm", "g.txt"};
//        Main.main(arg6);
//        String[] arg7 = {"commit", "commit2"};
//        Main.main(arg7);

//        String[] arg8 = {"checkout", "other"};
//        Main.main(arg8);
//        String[] arg9 = {"rm", "f.txt"};
//        Main.main(arg9);
//        String[] arg11 = {"add", "k.txt"};
//        Main.main(arg11);
//        String[] arg12 = {"commit", "commit3"};
//        Main.main(arg12);

//        String[] arg10 = {"checkout", "master"};
//        Main.main(arg10);
//        String[] arg9 = {"add", "m.txt"};
//        Main.main(arg9);
        String[] arg11 = {"reset", "d20a3ac221df89fa50308d4c208272c3562a7280"};
        Main.main(arg11);
//        String[] arg12 = {"commit", "commit3"};
//        Main.main(arg12);

    }

}


