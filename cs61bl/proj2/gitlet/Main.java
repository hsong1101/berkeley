package gitlet;

public class Main {

    //We are going to make each of our methods a class first to test to avoid merge conflict
    //And if it works, the method is going to be moved into a command class

    //Any global variables needed here such as Pointers.
    //If there is only one branch, we need two pointers.
    //Pointing the currentHead and the HEAD of the branch.
    //currentHead will be moving around if there are 2 or more branches.

    //Master Pointer here
    //Another branch Pointer here, if any

    public static void main(String[] args) throws Exception {

        ControlRepo.runCommand(args);

//        String[] testInit = {"init"};
//        ControlRepo.runCommand(testInit);

//        String[] testAdd = {"add", "f.txt"};
//        ControlRepo.runCommand(testAdd);
//
//        String[] testCommit = {"commit", "11"};
//        ControlRepo.runCommand(testCommit);

//        String[] testCheckout = {"checkout", "other"};
//        ControlRepo.runCommand(testCheckout);
//
////        SHOW WHAT IS IN THE REPO
//        Gitlet testRepo = RepoUtils.getRepo();
//        System.out.println("Branches =" + testRepo.branches);
//        System.out.println("ToBeAdded =" + testRepo.index.toBeAdded);
//        System.out.println("removed =" + testRepo.index.removed);
//        System.out.println("blobs =" + testRepo.index.blobs);
//
//        // SHOW THE COMMIT NODES
//        String pointer = testRepo.branches.get(testRepo.branches.get("HEAD"));
//        while (pointer != null) {
//            CommitNode currCommit = CommitUtils.deserializeCommitNode(pointer);
//            System.out.println("-------------------------------------------");
//            System.out.println("CommitNode: " + currCommit.message + " - PREV->"
// + currCommit.prev + " ID-> " + currCommit.commitID);
//            System.out.println(">>>>>>>>BLOBS=" + currCommit.blobs);
//            pointer = currCommit.prev;
//        }

    }
}
