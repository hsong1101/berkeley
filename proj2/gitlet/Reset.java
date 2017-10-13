
package gitlet;

import java.io.File;
import java.util.HashMap;

public class Reset {

    public void doIt(Gitlet repo, String commitID) {
        File pwd = new File(System.getProperty("user.dir"));
        String branch = repo.branches.get("HEAD");
        String idCurrCommit = RepoUtils.getRepo().branches.get(branch);
        CommitNode currCommit = CommitUtils.deserializeCommitNode(idCurrCommit);

        File commitDir = new File(pwd, ".gitlet/commitNodes/" + commitID);
        CommitNode givenCommit = null;
        if (commitDir.exists()) {
            File[] workingFiles = pwd.listFiles();
            for (int i = 0; i < workingFiles.length; i++) {
                String temp = "";
                int size = workingFiles[i].getName().length();
                if (workingFiles[i].getName().length() > 4) {
                    temp = workingFiles[i].getName().substring(size - 4, size);
                }

                if (workingFiles[i].getName().equals(".gitignore")
                        || workingFiles[i].getName().equals("gitlet")
                        || workingFiles[i].getName().equals(".idea")
                        || workingFiles[i].getName().equals(".gitlet")
                        || workingFiles[i].getName().equals("out")
                        || workingFiles[i].getName().equals("testing")
                        || temp.equals(".iml")) {
                    continue;
                } else {

                    if (!currCommit.getBlobs().containsKey(workingFiles[i].getName())) {
                        System.out.println("There is an untrakced "
                                + "file in the way; delete it or add it first.");
                        return;
                    } else {
                        continue;
                    }
                }
            }

            givenCommit = CommitUtils.deserializeCommitNode(commitID);
            Checkout checkout = new Checkout();
            for (HashMap.Entry<String, String> fileKey : givenCommit.getBlobs().entrySet()) {
                String[] arrArg = new String[4];
                arrArg[0] = "checkout";
                arrArg[1] = commitID;
                arrArg[2] = "--";
                arrArg[3] = fileKey.getKey();
                checkout.doIt(repo, arrArg);
            }
            repo.branches.replace("HEAD", "master");
            repo.branches.replace("master", commitID);
            for (int i = 0; i < workingFiles.length; i++) {
                File file = workingFiles[i];

                if (file.isFile()) {
                    File[] staged = new File(pwd, "/.gitlet/stage/").listFiles();
                    byte[] fileContentsByteStream = Utils.readContents(file);
                    byte[] fname = workingFiles[i].getName().getBytes();
                    byte[] shaStr = new byte[fname.length + fileContentsByteStream.length];
                    System.arraycopy(fileContentsByteStream, 0, shaStr, 0, fileContentsByteStream.length);
                    System.arraycopy(fname, 0, shaStr, fileContentsByteStream.length, fname.length);
                    String blobID = Utils.sha1(shaStr);
                    if (currCommit.getBlobs().containsKey(file.getName())
                            && !givenCommit.getBlobs().containsKey(file.getName())) {
                        for (int j = 0; j < staged.length; j++) {
                            if (staged[j].getName().equals(blobID)) {
                                continue;
                            } else {
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                        }
                    }
                }
            }
            repo.index.clearStage();
            repo.index.clearRemoved();
            RepoUtils.storeRepo(repo);
        } else {
            System.out.println("No commit with that id exists.");
        }
    }
}
