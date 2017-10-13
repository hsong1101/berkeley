package gitlet;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Long Quang on 7/15/2017.
 */
public class Reset {

    //test your method here
    public void doIt(Gitlet repo, String commitID) {
        File pwd = new File(System.getProperty("user.dir"));  //   pwd = /group238

        //File commitDir = new File(pwd, ".gitlet/commitNodes/" + commitID); // NEED TO CHECK IF THE FILE EXISTS
        File commitDir = new File(pwd, "gitlet/" + "testfile1.txt"); // NEED TO CHECK IF THE FILE EXISTS
        CommitNode givenCommit = null;
        if (commitDir.exists()) {
            givenCommit = CommitUtils.deserializeCommitNode(commitID);
            Checkout checkout = new Checkout();
            for (HashMap.Entry<String, String> fileKey : givenCommit.blobs.entrySet()) {
                String[] arrArg = new String[1];
                arrArg[0] = fileKey.getKey();
                checkout.doIt(repo, arrArg);
            }
            File[] workingFiles = pwd.listFiles();
            /** If a working file is untracked in the current branch and would be overwritten by the reset
             * throws IllegalStateException
             */
            for (int i = 0; i < workingFiles.length; i++) {
                String fileName = workingFiles[i].toString();
                if (givenCommit.blobs.containsKey(fileName)
                        && (!repo.index.getBlobs().containsKey(fileName))
                        || repo.index.getStaged().containsKey(fileName)) {
                    System.out.println("There is an untracked "
                            + "file in the way; delete it or add it first.");
                }
            }

            /** Removes tracked files that are not present in the given commit */
            for (int i = 0; i < workingFiles.length; i++) {
                String fileName = workingFiles[i].toString();
                if (repo.index.getBlobs().containsKey(fileName) && givenCommit.blobs.containsKey(fileName)) {
                    Utils.restrictedDelete(fileName);
                }
            }
        }
        else {
            System.out.println("No commit with that id exists.");
        }

    }
    public static void main (String[] args){
        File pwd = new File(System.getProperty("user.dir"));  //   pwd = /group238
        System.out.println(pwd);
        Gitlet repo = new Gitlet();
        Reset testReset = new Reset();
        testReset.doIt(repo, "testfile1.txt");
    }
}
