package gitlet;

import java.io.File;

/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Merge {

    // notes by ADAM
    //test your method here
    public void doIt(Gitlet repo, String givenBranch) throws Exception {

        boolean mergeConflict;

        File pwd = new File(System.getProperty("user.dir"));

        // // return name of currBranch -- curr Commit Node
        String currBranch = repo.branches.get("HEAD");
        String idCurrBranch = repo.branches.get(currBranch);
        CommitNode currCommit = CommitUtils.deserializeCommitNode(idCurrBranch);


        // Failure cases
        if (!repo.branches.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (currBranch.equals(givenBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        } else if (!repo.index.getStaged().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        // get the idGivenBranch -- given Commit Node
        String idGivenBranch = repo.branches.get(givenBranch);
        CommitNode givenCommit = CommitUtils.deserializeCommitNode(idGivenBranch);

        // Finding splitting Point
        CommitNode splitPoint = MergeUtils.findSplitingPoint(givenCommit, currCommit);

//        System.out.println("Split point = " + spitingPoint.message);

        // If the split point is the same commit as the given branch
        if (splitPoint == null) {
            return;
        }
// ------------------------------------------------------- ITS ALL GOOD UP THERE
        mergeConflict = CrossCheck.crossCheck(currCommit, givenCommit, splitPoint);
// -------------------------------------------------------


        // if no merge convicts auto commit w/messagefromscope else prnt merge conflict
        if (mergeConflict) {
            System.out.println("Encountered a merge conflict.");
            return;
        } else {
            String[] commitMerge = {"commit", "Merged "
                    + currBranch + " with " + givenBranch + "."};
            ControlRepo.runCommand(commitMerge);
        }
    }

    public static void main(String[] args) {
        System.out.println("merge test");
    }

}
