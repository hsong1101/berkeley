package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;


public class CommitNode implements Serializable {

    /** the parent */
    public String prev;

    /** ID of Commit (SHA1) */
    public String commitID;

    /** the message */
    public String message;

    /** the date.*/
    public LocalDateTime time;

    /** pointer to blobs in Index*/
    public HashMap<String,String> blobs;

    public CommitNode() {
        prev = null;
        message = "initial commit";
        time = LocalDateTime.now();
    }
}