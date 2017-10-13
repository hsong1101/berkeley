package gitlet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;


public class CommitNode implements Serializable {

    /**
     * the parent
     */
    private String prev;

    /**
     * ID of Commit (SHA1)
     */
    private String commitID;

    /**
     * the message
     */
    private String message;

    /**
     * the date.
     */
    private LocalDateTime time;

    /**
     * pointer to blobs in Index
     */
    private HashMap<String, String> blobs;

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public void setBlobs(HashMap<String, String> blobs) {
        this.blobs = blobs;
    }

    public CommitNode() {
        prev = null;
        message = "initial commit";
        time = LocalDateTime.now();
    }
}
