package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ADAM
 */
public class Commit implements Serializable {

    public static void doIt(Gitlet repo, String args) throws Exception {
        File pwd = new File(System.getProperty("user.dir"));
        Gitlet commitRepo = RepoUtils.getRepo();
        if (args.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (commitRepo.index.getToBeAdded().isEmpty() && commitRepo.index.getRemoved().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        CommitNode newCommit = new CommitNode();
        newCommit.setMessage(args);
        newCommit.setPrev(commitRepo.branches.get(commitRepo.branches.get("HEAD")));
        newCommit.setBlobs(new HashMap<>());
        CommitNode prevCommit = CommitUtils.deserializeCommitNode(
                commitRepo.branches.get(commitRepo.branches.get("HEAD")));
        if (prevCommit.getBlobs() != null) {
            newCommit.getBlobs().putAll(prevCommit.getBlobs());
        }
        for (HashMap.Entry<String, String> fileKey : commitRepo.index.getToBeAdded().entrySet()) {
            if (newCommit.getBlobs().containsKey(fileKey.getKey())) {
                //replace
                newCommit.getBlobs().replace(fileKey.getKey(),
                        commitRepo.index.getToBeAdded().get(fileKey.getKey()));
            } else {
                //add
                newCommit.getBlobs().put(fileKey.getKey(), fileKey.getValue());
            }
        }
        for (HashMap.Entry<String, String> fileKey
                : commitRepo.index.getRemoved().entrySet()) {
            if (newCommit.getBlobs().containsKey(fileKey.getKey())) {
                newCommit.getBlobs().remove(fileKey.getKey());
            }
        }
// ----------------------------------------------------------------- ITS ALL GOOD UP THERE!
        for (HashMap.Entry<String, String> fileKey : newCommit.getBlobs().entrySet()) {
            // move to new file
            FileUtils.moveFile(".gitlet/stage/"
                    + fileKey.getValue(), ".gitlet/blobs/" + fileKey.getValue());
            File toBeDeleted = new File(pwd + "/.gitlet/stage/", fileKey.getValue());
            // delete old file
            if (toBeDeleted.delete()) {
                repo.splitPoint = "hi";
//                System.out.println("Deleting File (blob)  =   " + fileKey.getValue());
            }
            commitRepo.index.getBlobs().put(fileKey.getKey(), fileKey.getValue());
        }

        // serialize the Commit node;
        newCommit.setCommitID(CommitUtils.getNewCommitID(newCommit));
//        System.out.println("new commitID = " + newCommit.commitID);
        CommitUtils.serializeCommitNode(newCommit);

        // wipe index
        commitRepo.index.getToBeAdded().clear();
        commitRepo.index.getRemoved().clear();
//        System.out.println("AddWIPE= " + commitRepo.index.toBeAdded);
//        System.out.println("RemoveWIPE= " + commitRepo.index.removed);

        // Head and branch pointers now point to new Commit
//        System.out.println("get(HEAD) = " + commitRepo.branches.get("HEAD"));
//        System.out.println("get(get(HEAD)) = "
// + commitRepo.branches.get(commitRepo.branches.get("HEAD")));
//        System.out.println("newCommit.commitID = " + newCommit.commitID);


        commitRepo.branches.replace(commitRepo.branches.get("HEAD"), newCommit.getCommitID());
//        System.out.println("commit branches = " + commitRepo.branches);

        // STORE REPO
        RepoUtils.storeRepo(commitRepo);

    }

    public boolean checkArg(String[] args) {
        return args.length == 1;
    }

    public static void main(String[] args) throws Exception {

        // Commit tests
        Gitlet testRepo = new Gitlet();

//        Add.doIt(testRepo, "gitlet/Branch.java");
//        Commit.doIt(testRepo, "Commit 2 = First real commit after Init()");

//        Add.doIt(testRepo, "gitlet/Main.java");
//        Commit.doIt(testRepo, "Commit 3");

//        Add.doIt(testRepo, "gitlet/Merge.java");
        Commit.doIt(testRepo, "Commit 4");

        // potential BUG if someone calls commit when there are no files stage it still commits.


    }
}
