import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AlphabetSortRunner {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You must specify a file to feed to AlphabetSort.");
            System.exit(0);
        }
        FileInputStream is = new FileInputStream(new File(args[0]));
        System.setIn(is);
        AlphabetSort.main(args);
    }
}
