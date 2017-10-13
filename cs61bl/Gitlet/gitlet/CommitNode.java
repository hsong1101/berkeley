import java.io.Serializable;

/**
 * Created by hsong1101 on 7/18/2017.
 */
public class CommitNode implements Serializable {

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String prev;
    private String sha;
    private String date;
    private String message;

    public String getPrev() {
        return prev;
    }

    public String getSha() {
        return sha;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }



}
