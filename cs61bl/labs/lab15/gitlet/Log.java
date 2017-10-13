package gitlet;

import java.io.File;
import java.time.format.DateTimeFormatter;


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Log {


    public void doIt(String firstArg, String secondArg) {
        if (firstArg.equals("global-log")) {
            globalLog();
        } else if (firstArg.equals("log")) {
            log();
        } else if (firstArg.equals("find")) {
            find(secondArg);
        } else {
            System.out.println("Invalid command");
        }
    }

    public void log() {
        File pwd = new File(System.getProperty("user.dir"));
        File file = new File(pwd, "/.gitlet./commitNodes");
        String branch = RepoUtils.getRepo().branches.get("HEAD");
        String id = RepoUtils.getRepo().branches.get(branch);

        while (id != null) {
            CommitNode temp = getCommit(id);
            print(temp);
            id = temp.prev;
        }
    }

    public void globalLog() {

        File folder = new File(".gitlet/commitNodes");
        File[] listOfFiles = folder.listFiles();
        CommitNode commit = null;

        for (int i = 0; i < listOfFiles.length; i++) {
            String commitName = listOfFiles[i].getName();
            commit = CommitUtils.deserializeCommitNode(commitName);
            print(commit);
        }

    }

    public void find(String secondArg) {
        File folder = new File(".gitlet/commitNodes");
        File[] listOfFiles = folder.listFiles();
        CommitNode commit = null;

        for (int i = 0; i < listOfFiles.length; i++) {
            String commitName = listOfFiles[i].getName();
            commit = CommitUtils.deserializeCommitNode(commitName);
            if (commit.message.equals(secondArg)) {
                printMessage(commit);
            }
        }
    }

    public void print(CommitNode commit) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("===");
        System.out.println("Commit " + commit.commitID);
        System.out.println(commit.time.format(format));
        System.out.println(commit.message);
        System.out.println();
    }

    public void printMessage(CommitNode commit) {
        System.out.println(commit.commitID);
    }

    public CommitNode getCommit(String id) {
        return CommitUtils.deserializeCommitNode(id);
    }

}
