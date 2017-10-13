package gitlet;
import java.io.File;
import java.io.Serializable;

// written by ADAM
public class Init implements Serializable{

    public static void doIt() {

        // get working directory
        File pwd = new File(System.getProperty("user.dir"));
        File gitlet = new File(pwd, ".gitlet");

        if (gitlet.exists()) {    // 1 check for previous .gitlet directory → fail on true.
            System.out.println("A gitlet version-control system already exists in the current directory");
            return;

        } else {

            // build a new .gitlet directory and all of its sub directories (repo, logs, stage, commitNodes, blobs)
            File gitlet_dir = new File(pwd, ".gitlet");
            gitlet_dir.mkdir();
            File commitNodes_dir = new File(pwd, ".gitlet/commitNodes");
            commitNodes_dir.mkdir();
            File blops_dir = new File(pwd, ".gitlet/blobs");
            blops_dir.mkdir();
            File branches_dir = new File(pwd, ".gitlet/branches");
            branches_dir.mkdir();
            File logs_dir = new File(pwd, ".gitlet/logs");
            logs_dir.mkdir();
            File stg_dir = new File(pwd, ".gitlet/stage");
            stg_dir.mkdir();
            File rpo_dir = new File(pwd, ".gitlet/repo");
            rpo_dir.mkdir();

            // Build FIRST Commit Node. (points to NULL and has ZERO blobs)
            CommitNode firstCommit = new CommitNode();
            byte[] commitByteStream = Utils.serialize(firstCommit);
            firstCommit.commitID = Utils.sha1(commitByteStream);

            // serialize FIRST Commit Node and store it in .gitlet/commitNodes/<commit ID>.
            byte[] commitSerialized = Utils.serialize(firstCommit);
            File firstCommitFile = new File(pwd, ".gitlet/commitNodes/" + firstCommit.commitID);
            Utils.writeContents(firstCommitFile, commitSerialized);

            // create .gitlet/repo/REPO for the FIRST time
            Gitlet initRepo = new Gitlet();
            initRepo.index = new Index();
            initRepo.branches.put("HEAD", "master");
            initRepo.branches.put("master", firstCommit.commitID);
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
        System.out.println("REPO.index.blops = " + testSerialRepo.index.blobs);

        // test deserializing the First Commit
        CommitNode firstCommit = CommitUtils.deserializeCommitNode(testSerialRepo.branches.get("master"));
        System.out.println();
        System.out.println("TESTING deserialize first Commit node");
        System.out.println("firstCommit.prev = " + firstCommit.prev);
        System.out.println("firstCommit.commitID = " + firstCommit.commitID);
        System.out.println("firstCommit.blops = " + firstCommit.blobs);
        System.out.println("firstCommit.message = " + firstCommit.message);
        System.out.println("firstCommit.time = " + firstCommit.time);
    }
}
