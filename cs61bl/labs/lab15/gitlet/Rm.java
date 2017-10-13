package gitlet;
import java.io.File;

/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Rm {

    public Rm() {
    }

    //comment
    /* Untrack the file; that is, indicate (somewhere in the
    .gitlet directory) that it is not to be included in the
    next commit, even if it is tracked in the current commit
    (which will become the next commit's parent). This command
    breaks down as follows:

    If the file is tracked by the current commit, delete the file
    from the working directory, unstage it if it was staged, and
    mark the file to be untracked by the next commit.

    If the file isn't tracked by the current commit but it is staged,
    unstage the file and do nothing else. */

    //test your method here



    public void doIt(Gitlet repo, String filename) {



        CommitNode lastCommitNode = CommitUtils.deserializeCommitNode
                (repo.branches.get(repo.branches.get("HEAD")));

        if (lastCommitNode.blobs.containsKey(filename)) {
            // delete file from working directory


            String shaf = Utils.sha1(lastCommitNode.blobs.get(filename));

            File pwd = new File(System.getProperty("user.dir"));
            File toBeDeleted = new File(pwd, filename);
            toBeDeleted.delete();

            if (repo.index.removed.containsKey(filename)) {
                repo.index.removed.replace(filename, shaf);
            } else {
                repo.index.removed.put(filename, shaf);
            }
        }

        if (repo.index.toBeAdded.containsKey(filename)) {
            repo.index.toBeAdded.remove(filename);
        }

        RepoUtils.storeRepo(repo);

    }
}
