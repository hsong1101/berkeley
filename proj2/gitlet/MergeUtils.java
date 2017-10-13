package gitlet;

import java.util.ArrayList;

public class MergeUtils {

    // Find Splitting point
    public static CommitNode findSplitingPoint(CommitNode given, CommitNode curr) {
        ArrayList<String> currHist = new ArrayList<>();
        ArrayList<String> givenHist = new ArrayList<>();

        // Build currBranch history
        String pointer = curr.getCommitID();
        while (pointer != null) {
            currHist.add(pointer);
            CommitNode currCommit = CommitUtils.deserializeCommitNode(pointer);
            pointer = currCommit.getPrev();
        }

        // Build givenBranch history
        pointer = given.getCommitID();
        while (pointer != null) {
            givenHist.add(pointer);
            CommitNode currCommit = CommitUtils.deserializeCommitNode(pointer);
            pointer = currCommit.getPrev();
        }

        String splitP = new String();
        for (String id : currHist) {
            if (givenHist.contains(id)) {
                splitP = id;
                break;
            }
        }
        if (given.getCommitID().equals(splitP)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return null;
        } else if (curr.getCommitID().equals(splitP)) {
            System.out.println("Current branch fast-forwarded.");
            return null;
        } else {
            return CommitUtils.deserializeCommitNode(splitP);
        }
    }


}
