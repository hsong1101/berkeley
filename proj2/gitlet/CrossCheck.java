package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CrossCheck {

    public static boolean crossCheck(CommitNode currCommit, CommitNode givenCommit,
                                     CommitNode splitPoint) throws Exception {

        Gitlet repo = RepoUtils.getRepo();

        boolean mergeConflict = false;

        for (HashMap.Entry<String, String> fileKey : splitPoint.getBlobs().entrySet()) {
            if (!currCommit.getBlobs().containsKey(fileKey.getKey())
                && (!givenCommit.getBlobs().containsKey(fileKey.getKey()))) {
                // F0 (do nothing) -> rm i from (cb)(gb)
                currCommit.getBlobs().remove(fileKey.getKey());
                givenCommit.getBlobs().remove(fileKey.getKey());
            } else if (currCommit.getBlobs().containsKey(fileKey.getKey())
                && (!givenCommit.getBlobs().containsKey(fileKey.getKey()))) {
                if (fileKey.getValue().equals(currCommit.getBlobs().get(fileKey.getKey()))) {
                    // F7 -> rm i from (cb)(gb)
                    FileUtils.deleteFile(fileKey.getKey());
                    repo.index.addRemoved(fileKey.getKey(), fileKey.getValue());
                    currCommit.getBlobs().remove(fileKey.getKey());
                    givenCommit.getBlobs().remove(fileKey.getKey());
                } else {
                    // F9  -> rm i from (cb)(gb)
                    mergeConflict = true;
                    currCommit.getBlobs().remove(fileKey.getKey());
                    givenCommit.getBlobs().remove(fileKey.getKey());
                }
            } else if (!currCommit.getBlobs().containsKey(fileKey.getKey())
                && (givenCommit.getBlobs().containsKey(fileKey.getKey()))) {
                if (fileKey.getValue().equals(givenCommit.getBlobs().get(fileKey.getKey()))) {
                    // F8 -> rm i from (cb)(gb)
                    repo.index.getRemoved().put(fileKey.getKey(), fileKey.getValue());
                    currCommit.getBlobs().remove(fileKey.getKey());
                    givenCommit.getBlobs().remove(fileKey.getKey());
                } else {
                    // F9  -> rm i from (cb)(gb)
                    mergeConflict = true;
                    currCommit.getBlobs().remove(fileKey.getKey());
                    givenCommit.getBlobs().remove(fileKey.getKey());
                }
            } else if (currCommit.getBlobs().get(fileKey.getKey())
                .equals(givenCommit.getBlobs().get(fileKey.getKey()))) {
                // F1  -> rm i from (cb)(gb)
                currCommit.getBlobs().remove(fileKey.getKey());
                givenCommit.getBlobs().remove(fileKey.getKey());
                //                addToStage(repo, fileKey);
            } else if (fileKey.getValue().equals(currCommit.getBlobs().get(fileKey.getKey()))
                && !fileKey.getValue().equals(givenCommit.getBlobs().get(fileKey.getValue()))) {
                // F3 -> rm i from (cb)(gb)
                f3(repo, givenCommit, fileKey);
                currCommit.getBlobs().remove(fileKey.getKey());
                givenCommit.getBlobs().remove(fileKey.getKey());
            } else if (fileKey.getValue().equals(givenCommit.getBlobs().get(fileKey.getKey()))
                && !fileKey.getValue().equals(currCommit.getBlobs().get(fileKey.getValue()))) {
                // F2
                f2(repo, currCommit, fileKey);
                currCommit.getBlobs().remove(fileKey.getKey());
                givenCommit.getBlobs().remove(fileKey.getKey());
            } else {
            // F9  -> rm i from (cb)(gb)
                mergeConflict = true;
                currCommit.getBlobs().remove(fileKey.getKey());
                givenCommit.getBlobs().remove(fileKey.getKey());
            }
        }

        // II
        for (HashMap.Entry<String, String> fileKey : currCommit.getBlobs().entrySet()) {
            if (givenCommit.getBlobs().containsValue(fileKey.getValue())) {
                if (currCommit.getBlobs().get(fileKey.getKey())
                    .equals(givenCommit.getBlobs().get(fileKey.getKey()))) {
                    // F1 -> rm i from (gb)
                    givenCommit.getBlobs().remove(fileKey.getKey());
                } else {
                // F9 -> rm i from (gb)
                    mergeConflict = true;
                    givenCommit.getBlobs().remove(fileKey.getKey());
                }
            } else {
                // F5 -> no need to rm
                givenCommit.getBlobs().remove(fileKey.getKey());
                f5(splitPoint, currCommit, givenCommit, fileKey);
            }
        }

        // III
        for (HashMap.Entry<String, String> fileKey : givenCommit.getBlobs().entrySet()) {
            // F6 -> no need to rm
            f6(splitPoint, currCommit, givenCommit, fileKey);
        }
// --------------------------------------------------------------------------
        RepoUtils.storeRepo(repo);
        return mergeConflict;

    }

    public static void addToStage(Gitlet repo, HashMap.Entry<String, String> fileKey) {
        //putting the file in the index
        repo.index.getToBeAdded().put(fileKey.getKey(), fileKey.getValue());

        //actually putting the file in the stage area
        File file = new File(System.getProperty("user.dir"), "/.gitlet/stage/"
                + fileKey.getValue());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void f2(Gitlet repo, CommitNode currCommit,
                          HashMap.Entry<String, String> fileKey) {

        for (HashMap.Entry<String, String> file : currCommit.getBlobs().entrySet()) {
            if (file.getKey().equals(fileKey.getKey())) {
                addToStage(repo, file);
            }
        }
    }

    public static void f3(Gitlet repo, CommitNode givenBranch,
                          HashMap.Entry<String, String> fileKey) {
        for (HashMap.Entry<String, String> file : givenBranch.getBlobs().entrySet()) {
            //if the file name and the file in the given branch is the same
            if (file.getKey().equals(fileKey.getKey())) {
                addToStage(repo, file);
            }
        }
    }

    public static void f5(CommitNode splitCommit, CommitNode currCommit,
                          CommitNode givenCommit, HashMap.Entry<String, String> fileKey) {
        if (!splitCommit.getBlobs().containsKey(fileKey.getKey())
                && !givenCommit.getBlobs().containsKey(fileKey.getKey())) {
            addToStage(RepoUtils.getRepo(), fileKey);
        }
    }

    public static void f6(CommitNode splitCommit, CommitNode currCommit,
                          CommitNode givenCommit, HashMap.Entry<String, String> fileKey) {
        if (!splitCommit.getBlobs().containsKey(fileKey.getKey())
                && !currCommit.getBlobs().containsKey(fileKey.getKey())) {

            Gitlet repo = RepoUtils.getRepo();
            addToStage(repo, fileKey);

            Checkout checkout = new Checkout();
            String[] args = {"checkout", givenCommit.getCommitID(), "--", fileKey.getKey()};
            checkout.doIt(RepoUtils.getRepo(), args);
        }
    }
}
