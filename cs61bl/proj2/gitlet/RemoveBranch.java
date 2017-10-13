
package gitlet;

/**
 * Created by Long Quang on 7/15/2017.
 */
public class RemoveBranch {

    //test your method here
    public void doIt(Gitlet repo, String args) {

        // get an branch of Head
        String branch = repo.branches.get("HEAD");

        if (!repo.branches.containsKey(args)) {
            System.out.println("A branch with that name does not exist.");
        } else if (args.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            repo.branches.remove(args);
        }

        RepoUtils.storeRepo(repo);
    }
}
