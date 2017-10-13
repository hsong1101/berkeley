
package gitlet;

import java.io.File;
import java.io.Serializable;


/**
 * Written by ADAM july 18, 11:07pm
 * <p>
 * See IndexUtils.main (at the bottom of this file) for how to use.
 */

class CommitUtils implements Serializable {

    static File pwd = new File(System.getProperty("user.dir"));

    // Makes a New CommitID.
    // WARNING:  ONLY THE COMMIT CLASS and INIT CLASS SHOULD MAKE COMMIT IDs
    public static String getNewCommitID(CommitNode currCommit) {
        byte[] commitByteStream = Utils.serialize(currCommit);
        return Utils.sha1(commitByteStream);
    }

    // SERIALIZE a Commit Node and stores it in .gitlet/commitNodes/<commit ID>.
    // WARNING:  ONLY THE COMMIT CLASS and INIT CLASS SHOULD SERIALIZE COMMIT NODES
    public static void serializeCommitNode(CommitNode currCommit) {
        byte[] commitSerialized = Utils.serialize(currCommit);
        File firstCommitFile = new File(pwd, ".gitlet/commitNodes/" + currCommit.getCommitID());
        Utils.writeContents(firstCommitFile, commitSerialized);
    }

    // DESERIALIZE a CommitNode

    /**
     * EXAMPLES:
     * CommitNode lastCommitNode = CommitUtils
     * .deserializeCommitNode
     * (repo.branches.get(repo.branches.get("HEAD")));
     * CommitNode secondToLast = CommitUtils.deserializeCommitNode(lastCommitNode.prev);
     */
    public static CommitNode deserializeCommitNode(String commitID) {
        File commitRecovedFile = new File(pwd, ".gitlet/commitNodes/" + commitID);
        return (CommitNode) Utils.deserialze(commitRecovedFile);
    }


    // Get the commitID that HEAD points to [NOT TESTED]
    public static String headPointsTo(Gitlet repo) {
        // Gitlet repo = deseriize(.gitlet/repo/REPO)
        return repo.branches.get(repo.branches.get("HEAD"));
    }

    // ----------------------------------------------------------- CommitUtils.main
    public static void main(String[] args) {

//        // TESTS
//
//        // build test commit node
//        CommitNode testNode = new CommitNode();
//        testNode.set = new HashMap<>();
//        testNode.blobs.put("key88", "val88");
//        testNode.prev = "prev88";
//        testNode.message = "have a nice day!";
//
//        // test getNewCommitID()
//        String testID = CommitUtils.getNewCommitID(testNode);
//        System.out.println("Testing getNewCommitID(), new ID = " + testID);
//        testNode.commitID = testID;
//
//        // test serializeCommitNode()
//        serializeCommitNode(testNode);
//        System.out.println("check for: .gitlet/commitNodes/" + testID);
//
//        // test deserializeCommitNode()
//        CommitNode testFirstCommit = deserializeCommitNode(testID);
//        System.out.println("testFirstCommit prev = " + testFirstCommit.prev);
//        System.out.println("testFirstCommit commitID = " + testFirstCommit.commitID);
//        System.out.println("testFirstCommit message = " + testFirstCommit.message);
//        System.out.println("testFirstCommit blobs = " + testFirstCommit.blobs);


    }
}
