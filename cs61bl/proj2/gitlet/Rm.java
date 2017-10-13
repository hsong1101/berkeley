package gitlet;

import java.io.File;

/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Rm {

    public static void doIt(Gitlet repo, String filename) {

        // PWD
        File pwd = new File(System.getProperty("user.dir"));

        // Deserialize the most recent commit
        CommitNode lastCommitNode = CommitUtils.deserializeCommitNode
                (repo.branches.get(repo.branches.get("HEAD")));

        // No reason to rm file
        if (lastCommitNode.getBlobs() == null) {
            if (!repo.index.getToBeAdded().containsKey(filename)) {  // file not in TBA
                //print error
                System.out.println("No reason to remove the file.");
            }
        } else if (!lastCommitNode.getBlobs()
                .containsKey(filename)) {  // check that file is not in LCB
            if (!repo.index.getToBeAdded().containsKey(filename)) {  // file not in TBA
                //print error
                System.out.println("No reason to remove the file.");
            }
        }

        String file = repo.index.getToBeAdded().get(filename);
        // Not tracked but staged
        if (lastCommitNode.getBlobs() == null) {
            if (repo.index.getToBeAdded().containsKey(filename)) {  // file in TBA
                // Delete File from the stage directory -- and remove from TBA
                File toBeDeleted = new File(pwd + "/.gitlet/stage/", file);
                repo.index.getToBeAdded().remove(filename);
                toBeDeleted.delete();
            }
        } else if (!lastCommitNode.getBlobs()
                .containsKey(filename)) {  // check that file is not in LCB
            if (repo.index.getToBeAdded().containsKey(filename)) {  // file in TBA
                // Delete File from the stage directory -- and remove from TBA
                file = repo.index.getToBeAdded().get(filename);
                File toBeDeleted = new File(pwd + "/.gitlet/stage/", file);
                repo.index.getToBeAdded().remove(filename);
                toBeDeleted.delete();
            }
        }

        // check LCB null and not in LCB
        if (lastCommitNode.getBlobs() != null) {
            if (lastCommitNode.getBlobs().containsKey(filename)) {
                // delet for working
                File toBeDeleted = new File(pwd, filename);
                toBeDeleted.delete();
                repo.index.getRemoved().put(filename, repo.index.getToBeAdded().get(filename));
                // ustage
                if (repo.index.getToBeAdded().containsKey(filename)) {
                    // mark removed
                    File toBeDeletedStage = new File(pwd + "/.gitlet/stage/",
                            repo.index.getToBeAdded().get(filename));
                    toBeDeleted.delete();
                    repo.index.getRemoved().put(filename, repo.index.getToBeAdded().get(filename));
                    repo.index.getToBeAdded().remove(filename);
                }

            }
        }

        // Store Repo
        RepoUtils.storeRepo(repo);
    }

    public static void main(String[] args) {
        Gitlet testRepo = RepoUtils.getRepo();
        Rm.doIt(testRepo, "wug.txt");
    }
}
