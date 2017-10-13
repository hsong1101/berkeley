package gitlet;

/**
 * Created by Long Quang on 7/15/2017.
 */
public class RemoveBranch {

    //test your method here
    public void doIt( Gitlet repo, String args){

        // get an ID of Head
        String headID = repo.branches.get("HEAD");
        repo.splitPoint = null;

        try {
            if (!repo.branches.containsKey(args)) {
                throw new IllegalArgumentException("A branch with that name does not exist.");
            }

            // If you try to remove the branch you're currently on
            else if(repo.branches.get(args) == headID) {
                throw new IllegalArgumentException("Cannot remove the current branch.");
            }
            else {
                repo.branches.remove(args);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
