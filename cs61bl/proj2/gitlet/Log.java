package gitlet;

import java.io.File;
import java.time.format.DateTimeFormatter;


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Log {

    File pwd = new File(System.getProperty("user.dir"));


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
        File file = new File(pwd, "/.gitlet/commitNodes");
        String branch = RepoUtils.getRepo().branches.get("HEAD");
        String id = RepoUtils.getRepo().branches.get(branch);

        while (id != null) {
            CommitNode temp = getCommit(id);
            print(temp);
            id = temp.getPrev();
        }
    }

    public void globalLog() {

        File[] file = new File(pwd, "/.gitlet/commitNodes/").listFiles();

        for (int i = 0; i < file.length; i++) {
            print(getCommit(file[i].getName()));
        }

    }

    public void find(String secondArg) {

        File[] folder = new File(".gitlet/commitNodes/").listFiles();
        CommitNode commit = null;
        boolean found = false;

        for (int i = 0; i < folder.length; i++) {
            String commitName = folder[i].getName();
            commit = getCommit(commitName);
            if (commit.getMessage().equals(secondArg)) {
                printMessage(commit);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void print(CommitNode commit) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("===");
        System.out.println("Commit " + commit.getCommitID());
        System.out.println(commit.getTime().format(format));
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public void printMessage(CommitNode commit) {
        System.out.println(commit.getCommitID());
    }

    public CommitNode getCommit(String id) {
        return CommitUtils.deserializeCommitNode(id);
    }

}
