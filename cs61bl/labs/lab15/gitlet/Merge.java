package gitlet;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Merge {

    // notes by ADAM
    //test your method here
    public void doIt(Gitlet repo,String secondArg ){

        // 1 check for files in .gitlet/stage -> throw excetion if files are there

        // 2 iterate through current branch to create list of its ancestors, store as String[]

        // 3 iterate through given branch to create list of its ancestors, store as String[]

        // 4 double iterate through ancestors of each to identify most recent split point (there may be more than one)

        // 5 create 3 hash maps.  (1) currBranchBlobs (2) givenBranchBlobs (3) splitPointBlobs

        // 6 lots of cross checking :
        /*
        Any files that have been modified in the current branch but not in the given branch since the split point should stay as they are.
        Any files that were not present at the split point and are present only in the current branch should remain as they are.
        Any files that were not present at the split point and are present only in the given branch should be checked out and staged.
        Any files present at the split point, unmodified in the current branch, and absent in the given branch should be removed (and untracked).
        Any files present at the split point, unmodified in the given branch, and absent in the current branch should remain absent.
        Any files modified in different ways in the current and given branches are in conflict. "Modified in different ways" can mean that the contents of both are changed and different from other, or the contents of one are changed and the other is deleted, or the file was absent at the split point and have different contents in the given and current branches. In this case, replace the contents of the conflicted file with
        */

        // 7 if no merge conflicts exist -> commit

        // 8 else stage the non conflicting files and concatinate the the conflicting files
        // then give an error message

    }
}
