package gitlet;

//import java.awt.*;

/**
 * Created by Long Quang on 7/15/17.
 */
public class Branch {

    public void doIt(Gitlet repo, String args) {

        // return headID
        String headID = repo.branches.get("HEAD");

        try {
            if (repo.branches.containsKey(args)) {
                throw new IllegalArgumentException("A branch with that name already exists.");
            } else {
                repo.branches.put(args, headID);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
