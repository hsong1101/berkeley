package gitlet;

import java.io.Serializable;

/* written by ADAM */
public class BranchNode implements Serializable {

    /**
     * What does this Node Point To?
     * Example1:  master -> current commit ID
     * Usage:
     * BranchNode master = new BranchNode(".gitlet/CommitNodes/<SHA_1 Commit ID>");
     * serialize(master);
     * <p>
     * Example2:  HEAD -> master
     * Usage:
     * BranchNode HEAD = new BranchNode(".gitlet/branches/master");
     * serialize(HEAD);
     */

    private String pointsTo;

    public BranchNode(String pointsTo) {
        this.pointsTo = pointsTo;
    }
}
