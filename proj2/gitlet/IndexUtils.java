package gitlet;

import java.io.*;

/**
 * Written by ADAM july 18, 11:07pm
 * <p>
 * See IndexUtils.main (at the bottom of this file) for how to use.
 */

class IndexUtils implements Serializable {

    // Serializes the Index and store it to file.
    public static void storeIndex(Index toStore) {
        File outFile = new File(".gitlet/INDEX");
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


    public static Index retrieveIndex() {
        Index myIndex2;
        File inFile = new File(".gitlet/INDEX");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            myIndex2 = (Index) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("exception: DEserialize");
            myIndex2 = null;
        }
        return myIndex2;
    }

    public static void main(String[] args) {
        Index toStore = new Index();
        toStore.getToBeAdded().put("HEAD100", "maste56456r");
        toStore.getRemoved().put("Cal555", "Go Bears5646584!");
        IndexUtils.storeIndex(toStore);

        Index toRetrieve = IndexUtils.retrieveIndex();

        System.out.println("toRetrieve.toBeAdded HEAD = "
                + toRetrieve.getToBeAdded().get("HEAD100"));
        System.out.println("toRetrieve.removed Cal = " + toRetrieve.getRemoved().get("Cal555"));
    }
}
