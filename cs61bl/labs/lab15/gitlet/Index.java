package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by LongQuang on 7/14/17.
 */
public class Index implements Serializable{

    /** HashMap to save staged files
     * Key is SHA-1, value is file name
     */
    public HashMap<String,String> toBeAdded;



    /**HashMap to save removed files from staged files
     * Key is SHA-1, value is file name
     */
    public HashMap<String,String> removed;

    /** HashMap to save blobs
     * Key is SHA-1, value is file name
     */
    public HashMap<String,String> blobs;



    // default constructor
    public Index() {
        this.toBeAdded = new HashMap<String,String>();
        this.removed = new HashMap<String,String>();
        this.blobs = new HashMap<String,String>();
    }

    /** check the a file having the same name and SHA-1 (idBlob) in
     * the blobs
     */

    public boolean isInBlobs(String fileName, String idBlob){
        return this.blobs.containsKey(fileName) && this.blobs.containsValue(idBlob);
    }

    /**
     * Add a blob into blobs if there is no blob having the same idBlob
     *
     */

    public void addBlob(String idBlob, String nameBlob) throws Exception {
        if(blobs.containsKey(idBlob)){
            throw new Exception("There exists the same blob!");
        } else{
            blobs.put(idBlob,nameBlob);
        }
    }

    /**
     * rm a blob by ID if there exists this blob
     *
     */

    public void removeBlob(String idBlob) throws Exception {
        if(!blobs.containsKey(idBlob)){
            throw new Exception("There does not exist this blob!");
        } else{
            blobs.remove(idBlob);
        }
    }

    /**
     * get all blobs in in the blob
     */

    public HashMap<String,String> getStaged() {
        return this.toBeAdded;
    }

    /**
     * get all blobs in in the blob
     */

    public HashMap<String,String> getBlobs() {
        return this.blobs;
    }

    /**
     * Add a file into staged area if there no exist file having the same ID
     *
     */

    public void addStagedFile(String idFile, String nameFile) throws Exception {
        if(toBeAdded.containsKey(idFile)){
            throw new Exception("There exists the same file!");
        } else{
            toBeAdded.put(idFile,nameFile);
        }
    }

    /**
     * rm all files from staged area.
     *
     */

    public void clearStage() {
        this.toBeAdded.clear();
    }

    /**
     * Add a file into removed area if there no exist file having the same ID
     *
     */

    public void addRemoved(String idFile, String nameFile) throws Exception {
        if(removed.containsKey(idFile)){
            throw new Exception("There exists the same file!");
        } else{
            removed.put(idFile,nameFile);
        }
    }

    /**
     * rm all files from removed area.
     *
     */

    public void clearRemoved() {
        this.removed.clear();
    }

    public static void main(String[] agrs) throws Exception {
        Index index1 = new Index();

        //Adding elements to index1
        index1.addBlob("id1", "file1.txt");
        index1.addBlob("id2","file1.txt"); // version 2 of file 1
        index1.addStagedFile("id3", "file2.txt");
        index1.addStagedFile("id2", "file2.txt");
        // get a file name in blobs
        System.out.println("File name of id1 in blobs : " + index1.blobs.get("id1"));
        // get a file name in toBeAdded
        System.out.println("File name of id3 in staged area : " + index1.toBeAdded.get("id3"));
    }
}
