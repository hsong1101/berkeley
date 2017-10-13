package gitlet;
import java.io.*;

/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Status {
    //test your method here
    public void doIt(Gitlet repo, String secondarg) {
        Index e = repo.index;


        System.out.println("=== Branches ===");
        /* find out the name of all branches */
        /* show which one is HEAD by marking it with a star like *branch1 */
        for (String c: repo.branches.keySet()) {
            // print the list of branches stuff
            if (repo.branches.get(c) == repo.branches.get("HEAD")) {
                System.out.println("*" + c);
            } else {

                if (!(c == "HEAD")) {
                    System.out.println(c);
                }
            }
        }

        System.out.println("");
        System.out.println("=== Staged Files ===");

        for (String a : e.toBeAdded.keySet()) {
            // print the list of added stuff
            System.out.println(a);
        }

        System.out.println("");
        System.out.println("=== Removed Files ===");

        for (String b : e.removed.keySet()) {
            // print the list of removed stuff
            System.out.println(b);
        }

        System.out.println("");

        RepoUtils.storeRepo(repo);

    }
}
