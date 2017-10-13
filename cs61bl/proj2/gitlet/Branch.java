package gitlet;

//import java.awt.*;

/**
 * Created by Long Quang on 7/15/17.
 */
public class Branch {

    public void doIt(Gitlet repo, String args) {

        // return headID
        String branch = repo.branches.get("HEAD");
        String id = RepoUtils.getRepo().branches.get(branch);


        if (repo.branches.containsKey(args)) {
            System.out.println("A branch with that name already exists.");
        } else {
/** Change the Head*/
            repo.branches.put(args, id);
//            repo.branches.replace("HEAD", args);
        }
        RepoUtils.storeRepo(repo);
    }
}
