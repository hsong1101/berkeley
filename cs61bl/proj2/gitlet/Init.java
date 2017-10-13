package gitlet;

import java.io.File;
import java.io.Serializable;

// written by ADAM
public class Init implements Serializable {

    public static void doIt() {

        // get working directory
        File pwd = new File(System.getProperty("user.dir"));
        File gitlet = new File(pwd, ".gitlet");

        if (gitlet.exists()) {
            // 1 check for previous .gitlet directory → fail on true.
            System.out.println("A gitlet version-control system already"
                    + " exists in the current directory");
            return;

        } else {

            // build a new .gitlet directory and all of its sub
            // directories (repo, logs, stage, commitNodes, blobs)
            File gitletDir = new File(pwd, ".gitlet");
            gitletDir.mkdir();
            File commitNodesDir = new File(pwd, ".gitlet/commitNodes");
            commitNodesDir.mkdir();
            File blobsDir = new File(pwd, ".gitlet/blobs");
            blobsDir.mkdir();
            File branchesDir = new File(pwd, ".gitlet/branches");
            branchesDir.mkdir();
            File logsDir = new File(pwd, ".gitlet/logs");
            logsDir.mkdir();
            File stgDir = new File(pwd, ".gitlet/stage");
            stgDir.mkdir();
            File rpoDir = new File(pwd, ".gitlet/repo");
            rpoDir.mkdir();

            // Build FIRST Commit Node. (points to NULL and has ZERO blobs)
            CommitNode firstCommit = new CommitNode();
            byte[] commitByteStream = Utils.serialize(firstCommit);
            firstCommit.setCommitID(Utils.sha1(commitByteStream));

            // serialize FIRST Commit Node and store it in .gitlet/commitNodes/<commit ID>.
            byte[] commitSerialized = Utils.serialize(firstCommit);
            File firstCommitFile = new File(pwd, ".gitlet/commitNodes/"
                    + firstCommit.getCommitID());
            Utils.writeContents(firstCommitFile, commitSerialized);

            // create .gitlet/repo/REPO for the FIRST time
            Gitlet initRepo = new Gitlet();
            initRepo.index = new Index();
            initRepo.branches.put("HEAD", "master");
            initRepo.branches.put("master", firstCommit.getCommitID());
            RepoUtils.storeRepo(initRepo);
        }

    }

    public static void main(String[] args) {
        Gitlet testRepoInit = new Gitlet(); // There is no seriaized repo until Init() makes one.
        Init.doIt();

        // test deseriazing the repo at .gitlet/repo/REPO
        Gitlet testSerialRepo = RepoUtils.getRepo();
        System.out.println("TESTING deserialize REPO");
        System.out.println("REPO.branches = " + testSerialRepo.branches);
        System.out.println("REPO.index.blops = " + testSerialRepo.index.getBlobs());

        // test deserializing the First Commit
        CommitNode firstCommit =
                CommitUtils.deserializeCommitNode(testSerialRepo.branches.get("master"));
        System.out.println();
        System.out.println("TESTING deserialize first Commit node");
        System.out.println("firstCommit.prev = " + firstCommit.getPrev());
        System.out.println("firstCommit.commitID = " + firstCommit.getCommitID());
        System.out.println("firstCommit.blops = " + firstCommit.getBlobs());
        System.out.println("firstCommit.message = " + firstCommit.getMessage());
        System.out.println("firstCommit.time = " + firstCommit.getTime());
    }
}
