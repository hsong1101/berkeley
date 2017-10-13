package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Checkout {

    public static final int FILE = 1;
    public static final int BRANCH = 2;
    public static final int FILEID = 3;
    private File pwd = new File(System.getProperty("user.dir"));

    String secondArg, thirdArg, fourthArg;

    //test your method here
    public void doIt(Gitlet repo, String[] args) {

        int option = checkArgs(args);

        switch (option) {
            case BRANCH:
                branch(args[1], repo);
                break;
            case FILE:
                file(args[2]);
                break;
            case FILEID:
                fileId(args[1], args[3]);
                break;
            default:
                break;

        }
    }

    public int checkArgs(String[] args) {

        if (args == null) {
            System.out.println("Please enter a command.");
            return 0;

        } else if (args.length == 3 && !args[1].equals("--")) {
            System.out.println("Incorrect operands.");
            return 0;

        } else if (args.length == 2) {

            return BRANCH;

        } else if (args.length == 4 && !args[2].equals("--")) {
            System.out.println("Incorrect operands.");
            return 0;

        } else if (args.length == 3) {

            secondArg = args[1];
            thirdArg = args[2];
            if (secondArg.equals("--")) {
                if (thirdArg == null) {
                    System.out.println("Please enter a command.");

                } else {
                    return FILE;
                }
            }
        } else if (args != null && args.length == 4) {
            secondArg = args[1];
            thirdArg = args[2];
            fourthArg = args[3];
            if (thirdArg.equals("--")) {
                return FILEID;
            }
        } else {
            System.out.println("No command with that name exists.");
            return 0;
        }
        return 0;


    }

    public boolean hasUntracked(Gitlet repo, String checkedoutBranch) {

        String id = repo.branches.get(checkedoutBranch);
        CommitNode givenCommit = CommitUtils.deserializeCommitNode(id);

        String id2 = repo.branches.get(repo.branches.get("HEAD"));
        CommitNode currCommit = CommitUtils.deserializeCommitNode(id2);

        File[] workingFiles = pwd.listFiles();

        if (currCommit.getBlobs() == null && workingFiles.length > 3) {
            return true;
        } else if (currCommit.getBlobs() != null) {

            for (int i = 0; i < workingFiles.length; i++) {
                String temp = "";
                int size = workingFiles[i].getName().length();
                if (workingFiles[i].getName().length() > 4) {
                    temp = workingFiles[i].getName().substring(size - 4, size);
                }

                if (workingFiles[i].getName().equals(".gitignore")
                        || workingFiles[i].getName().equals("index")
                        || workingFiles[i].getName().equals("gitlet")
                        || workingFiles[i].getName().equals(".idea")
                        || workingFiles[i].getName().equals(".gitlet")
                        || workingFiles[i].getName().equals("out")
                        || workingFiles[i].getName().equals("testing")
                        || temp.equals(".iml")) {
                    continue;
                }

                if (currCommit.getBlobs().containsKey(workingFiles[i].getName())) {
                    continue;
                } else {
                    return true;
                }
            }
            return false;
        }

        return true;

    }

    public void branch(String name, Gitlet repo) {

        String error = "There is an untracked file in the way; delete it or add it first.";

        if (!repo.branches.containsKey(name)) {
            System.out.println("No such branch exists.");
        } else {
            if (repo.branches.get("HEAD").equals(name)) {
                System.out.println("No need to checkout the current branch.");
            } else {
                if (hasUntracked(repo, name)) {
                    System.out.println(error);
                } else {
                    String id = repo.branches.get(name);
                    CommitNode givenCommit = CommitUtils.deserializeCommitNode(id);

                    //clear the stage area
                    File[] staged = new File(pwd, "/.gitlet/stage/").listFiles();

                    for (int i = 0; i < staged.length; i++) {
                        File file = staged[i];
                        file.delete();
                    }

                    String id2 = repo.branches.get("HEAD");
                    String id3 = repo.branches.get(id2);
                    CommitNode currentCommit = CommitUtils.deserializeCommitNode(id3);
                    repo.branches.put("HEAD", name);

                    HashMap<String, String> trackedFiles = currentCommit.getBlobs();
                    HashMap<String, String> givenFiles = givenCommit.getBlobs();
                    ArrayList<String> deleteFile = new ArrayList<>();

                    if (givenCommit.getBlobs() != null && currentCommit.getBlobs() != null) {
                        //put toBeRemoved files in the arrayList
                        for (HashMap.Entry<String, String> file : trackedFiles.entrySet()) {
                            if (!givenFiles.containsKey(file.getKey())) {
                                deleteFile.add(file.getKey());
                            }
                        }
                        //delete file in the working dir
                        for (int i = 0; i < deleteFile.size(); i++) {
                            File file = new File(pwd, "/" + deleteFile.get(i));
                            file.delete();
                        }

                        for (HashMap.Entry<String, String> file : givenFiles.entrySet()) {
                            copyFile(file.getKey(), givenCommit);
                        }
                    } else if (currentCommit.getBlobs() == null) {
                        RepoUtils.storeRepo(repo);
                    } else {
                        for (HashMap.Entry<String, String> file : trackedFiles.entrySet()) {
                            deleteFile.add(file.getKey());
                        }
                        for (int i = 0; i < deleteFile.size(); i++) {
                            File file = new File(pwd, "/" + deleteFile.get(i));
                            file.delete();
                        }
                    }
                }
            }
        }

        RepoUtils.storeRepo(repo);
    }

    public void file(String name) {

        String branch = RepoUtils.getRepo().branches.get("HEAD");
        String id = RepoUtils.getRepo().branches.get(branch);

        CommitNode commit = CommitUtils.deserializeCommitNode(id);

        if (commit.getBlobs() != null) {
            if (commit.getBlobs().containsKey(name)) {
                copyFile(name, commit);
            } else {
                System.out.println("File does not exist in that commit.4");
            }
        }
    }

    public void fileId(String id, String name) {

        if (id.length() == 40) {
            File file = new File(pwd, ".gitlet/commitNodes/" + id);
            //check if the given commit exists
            if (file.exists()) {
                CommitNode commit = CommitUtils.deserializeCommitNode(id);
                if (commit.getBlobs().containsKey(name)) {
                    copyFile(name, commit);
                } else {
                    System.out.println("File does not exist in that commit.1");
                }
            } else {
                System.out.println("No commit with that id exists.");
            }
        } else if (id.length() < 40) {
            int count = 0;
            File[] list = new File(pwd, "/.gitlet/commitNodes/").listFiles();
            String commitId = "";

            for (int i = 0; i < list.length; i++) {
                if (id.equals(list[i].getName().substring(0, id.length()))) {
                    commitId = list[i].getName();
                    count++;
                }
            }

            if (count == 1) {
                File file = new File(pwd, "/.gitlet/commitNodes/" + commitId);
                if (file.exists()) {
                    CommitNode commit = CommitUtils.deserializeCommitNode(commitId);
                    if (commit.getBlobs().containsKey(name)) {
                        copyFile(name, commit);
                    } else {
                        System.out.println("File does not exist in that commit.2");
                    }
                } else {
                    System.out.println("No commit with that id exists.");
                }
            } else {
                System.out.println("No commit with that id exists.");
            }
        } else {
            System.out.println("No commit with that id exists.");
        }
    }

    public void copyFile(String name, CommitNode commit) {
        if (commit.getBlobs() != null) {

            if (commit.getBlobs().containsKey(name)) {

                File file = new File(pwd, "/.gitlet/blobs/" + commit.getBlobs().get(name));
                File file2 = new File(pwd, "/" + name);

                if (file2.exists()) {
                    file2.delete();
                }

                File file3 = new File(pwd, "/" + name);
                //copying file to working dir

                try {
                    FileInputStream ins = null;
                    FileOutputStream outs = null;
                    ins = new FileInputStream(file);
                    outs = new FileOutputStream(file3);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = ins.read(buffer)) > 0) {
                        outs.write(buffer, 0, length);
                    }


                    file3.createNewFile();
                    ins.close();
                    outs.close();

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }
            } else {
                System.out.println("File does not exist in that commit.3");
            }
        }
    }
}
