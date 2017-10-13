package gitlet;

import java.io.*;

/**
 * Written by ADAM july 18, 11:07pm
 * <p>
 * See IndexUtils.main (at the bottom of this file) for how to use.
 */

class RepoUtils implements Serializable {

    // Serializes the REPO (instance of Gitlet class) and store it to file .gitlet/repo/REPO
    public static void storeRepo(Gitlet toStore) {
        File outFile = new File(".gitlet/repo/REPO");
        // Serialize
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(toStore);
            out.close();
        } catch (IOException excp) {
            System.out.println("exception: serialize");
        }

    }

    // DE-Serializes the REPO (instance of Gitlet class) and store it to file .gitlet/repo/REPO
    public static Gitlet getRepo() {
        Gitlet repo;
        File inFile = new File(".gitlet/repo/REPO");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            repo = (Gitlet) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("exception: DEserialize");
            repo = null;
        }
        return repo;
    }

    public static void main(String[] args) {

        // test storeRepo();
        Gitlet toStore = new Gitlet();
        toStore.index = new Index();
        toStore.index.getToBeAdded().put("test1", "111111");
        toStore.index.getRemoved().put("test2", "222222");
        toStore.index.getToBeAdded().put("HEAD100-repo", "maste56456r-repo");
        toStore.index.getRemoved().put("Cal555-repo", "Go Bears5646584-repo");
        RepoUtils.storeRepo(toStore);

        // test getRepo()
        Gitlet toGet = RepoUtils.getRepo();
        System.out.println("toRetrieve.toBeAdded HEAD = " + toGet.index.getToBeAdded());
        System.out.println("toRetrieve.removed Cal = " + toGet.index.getRemoved());
    }
}
