package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ADAM
 */
public class Commit implements Serializable {  // INCOMPLETE

    public static void doIt(Gitlet repo, String args) throws Exception{

        // get the working directory
        File pwd = new File(System.getProperty("user.dir"));


        // deserilalize the repo
        Gitlet commitRepo = RepoUtils.getRepo();

        // Check for empty message
        if (args == "") {
            System.out.println("Please enter a commit message.");
            return;
        }

        // Check for empty toBeAdded
        if (commitRepo.index.toBeAdded.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        // creates new current commit node
        CommitNode newCommit = new CommitNode();
        newCommit.message = args;
        newCommit.prev = commitRepo.branches.get(commitRepo.branches.get("HEAD"));
        newCommit.blobs = new HashMap<>();
//        System.out.println("newCommit.prev = " + newCommit.prev);
//        System.out.println("newCommit.blobs = " + newCommit.blobs);

        //deserialize the previous commit
        CommitNode prevCommit = CommitUtils.deserializeCommitNode(commitRepo.branches.get(commitRepo.branches.get("HEAD")));
//        System.out.println("deserialized: " + prevCommit.message);

        // copy all elements from PrevCommit.blobs to CurrCommit.blobs;
        if (prevCommit.blobs != null) {
            newCommit.blobs.putAll(prevCommit.blobs);
//            System.out.println("prevCommit has blobs, copying");
        }
//        System.out.println("Line37: newCommit blobs = " + newCommit.blobs);

        // copy all elemnts from toBeAdded to newCommit.blpbs {overWrite older blobs}
//        System.out.println("Line47: copying toBeAdded = " + commitRepo.index.toBeAdded);
        for (HashMap.Entry<String, String> fileKey : commitRepo.index.toBeAdded.entrySet()) {
            if (newCommit.blobs.containsKey(fileKey.getKey())) {
                //replace
                newCommit.blobs.replace(fileKey.getKey(), commitRepo.index.toBeAdded.get(fileKey.getKey()));
            } else {
                //add
                newCommit.blobs.put(fileKey.getKey(), fileKey.getValue());
            }
 //           System.out.println("fileKey:" + fileKey.getKey() + " - fileValue:" + fileKey.getValue());
        }
//        System.out.println("newCommit.blobs: " + newCommit.blobs);


        // remove all elements from newCommit that are in remove
//        System.out.println("Line55: STUFFING THE REMOVE BOX  ");
//        commitRepo.index.removed.put("gitlet/Checkout.java", "d93cd71b858a7626db8886e1c757d5d980998dfc");
//        System.out.println("Line58: removing = " + commitRepo.index.removed);
        for (HashMap.Entry<String, String> fileKey : commitRepo.index.removed.entrySet()) {
            if (newCommit.blobs.containsKey(fileKey.getKey())) {
                //remove blob
                newCommit.blobs.remove(fileKey.getKey());
                // delete file
                File toBeDeleted = new File(pwd + "/.gitlet/stage/", fileKey.getValue());
                if (toBeDeleted.delete()) {
                    repo.splitPoint = "hi";
                    //                    System.out.println("Deleting File (blob)  =   " + fileKey.getValue());
                }
//                System.out.println("Deleting File (rm)  =   " + fileKey.getValue());
            }
//            System.out.println("fileKey:" + fileKey.getKey() + " - fileValue:" + fileKey.getValue());
        }
//        System.out.println("newCommit.blobs: " + newCommit.blobs);
// ----------------------------------------------------------------- ITS ALL GOOD UP THERE!

        // move all of staging area files to .getlit/blobs (write a move blops util)
//        System.out.println("Line75: moving stage files to blops (commits only)");
        for (HashMap.Entry<String, String> fileKey : newCommit.blobs.entrySet()) {
            // move to new file
            FileUtils.moveFile(".gitlet/stage/" + fileKey.getValue(), ".gitlet/blobs/" + fileKey.getValue());
            File toBeDeleted = new File(pwd + "/.gitlet/stage/", fileKey.getValue());
            // delete old file
            if (toBeDeleted.delete()) {
                repo.splitPoint = "hi";
//                System.out.println("Deleting File (blob)  =   " + fileKey.getValue());
            }
        }

        // serialize the Commit node;
        newCommit.commitID = CommitUtils.getNewCommitID(newCommit);
//        System.out.println("new commitID = " + newCommit.commitID);
        CommitUtils.serializeCommitNode(newCommit);

        // wipe index
        commitRepo.index.toBeAdded.clear();
        commitRepo.index.removed.clear();
//        System.out.println("AddWIPE= " + commitRepo.index.toBeAdded);
//        System.out.println("RemoveWIPE= " + commitRepo.index.removed);

        // Head and branch pointers now point to new Commit
//        System.out.println("get(HEAD) = " + commitRepo.branches.get("HEAD"));
//        System.out.println("get(get(HEAD)) = " + commitRepo.branches.get(commitRepo.branches.get("HEAD")));
//        System.out.println("newCommit.commitID = " + newCommit.commitID);


        commitRepo.branches.replace(commitRepo.branches.get("HEAD"), newCommit.commitID);
//        System.out.println("commit branches = " + commitRepo.branches);

        // STORE REPO
        RepoUtils.storeRepo(commitRepo);

    }
    public boolean checkArg(String[] args){
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
