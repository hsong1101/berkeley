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
public class Add {

    public static void doIt(Gitlet repo, String args) throws Exception {

        // PWD
        File pwd = new File(System.getProperty("user.dir"));

        // serilized the file to a "blop" and create the BlopID and store the blob in stage
        String fileName = args; // Usage: java gitlet.Main add [file name]
        File workingFile = new File(pwd, "/" + fileName);

        if (!workingFile.exists()) {
            System.out.println("File does not exist.");
        } else {

            // deserilize REPO
            Gitlet addRepo = RepoUtils.getRepo();
//            System.out.println("deserilized REPO");

            // Copy file to .gitlet/stage as a newly created blob
            byte[] fileContentsByteStream = Utils.readContents(workingFile);
            byte[] fname = fileName.getBytes();
            byte[] shaStr = new byte[fname.length + fileContentsByteStream.length];
            System.arraycopy(fileContentsByteStream, 0, shaStr, 0, fileContentsByteStream.length);
            System.arraycopy(fname, 0, shaStr, fileContentsByteStream.length, fname.length);
            String blobID = Utils.sha1(shaStr);
//            String blobID = Utils.sha1(fileContentsByteStream);
            File blobFile = new File(pwd, ".gitlet/stage/" + blobID);


            // Deserialize the last commit
            CommitNode prevCommit = CommitUtils.deserializeCommitNode(addRepo.
                    branches.get(addRepo.branches.get("HEAD")));
            //System.out.println("prevCommit.blobs = " + prevCommit.blobs);

            if (prevCommit.getBlobs() != null) {
                if (prevCommit.getBlobs().containsValue(blobID)
                        && prevCommit.getBlobs().containsKey(args)) {
                    //System.out.println(fileName + " is EXACTLY the same as prevCommit.blobs");
                    return;
                }
            }

            // test 13
//        if (!prevCommit.blobs.containsValue(blobID)) {  // IF
//            System.out.println("two print lines, this exact file NOT in prevCommit");
            Utils.writeContents(blobFile, fileContentsByteStream);
            //System.out.println("wrote " + fileName + " to: .gitlet/stage/" + blobID);
            // has the file been marked for removal
            if (addRepo.index.getRemoved().containsKey(fileName)) {
                addRepo.index.getRemoved().remove(fileName);
            } else if (addRepo.index.getToBeAdded().containsKey(fileName)) {
                if (!addRepo.index.getBlobs().containsValue(blobID)) {
                    // Update toBeAdded in the index
                    addRepo.index.getToBeAdded().replace(fileName, blobID);
                }
            } else {
                // make a new enty in toBeAdded
                addRepo.index.getToBeAdded().put(fileName, blobID);
            }
//        } // ELSE
            // Serialize REPO
            RepoUtils.storeRepo(addRepo);
            //
        }
        // DONT FORGET TO RE SERIALIZE INDEX:
    }

    // copy content of file 1 name to file name 2
    public static void copyFile(String file1, String file2) {
        try {
            FileInputStream ins = null;
            FileOutputStream outs = null;
            File infile = new File(file1);
            File outfile = new File(file2);
            ins = new FileInputStream(infile);
            outs = new FileOutputStream(outfile);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = ins.read(buffer)) > 0) {
                outs.write(buffer, 0, length);
            }
            ins.close();
            outs.close();
            System.out.println("File copied successfully!!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void serializeFile(Gitlet gitlet) {
        try {
            //Path file = Paths.get(".gitlet/filename1");
            // create new file
            Gitlet.createFile("tempFile.txt");
            // check file exists?
            boolean isExsit = new File(".gitlet/myFile22.txt/tempFile.txt").isFile();
            System.out.println("testFile exists ? = " + isExsit);


            FileOutputStream fileOut =
                    new FileOutputStream(".gitlet/myFile22.txt/tempFile.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gitlet);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in .gitlet/testFile.txt");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }
    /** method to read an file to object using derializable */
    public static Gitlet deserialzeGitlet(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Gitlet repo = (Gitlet) in.readObject();
            in.close();
            fileIn.close();
            return repo;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Object cannot found");
            c.printStackTrace();
            return null;
        }

    }

    public boolean checkArg(String[]args) {
        return args.length == 1;
    }

    public static void main(String[] args) throws Exception {  // MAIN

        // test Add()
        Gitlet testRepo = new Gitlet();

        // test making some blobs
        Add.doIt(testRepo, "gitlet/Index.java");
        Add.doIt(testRepo, "gitlet/Log.java");
        Add.doIt(testRepo, "gitlet/Checkout.java");

        // test deseriazing REPO and checking the blobs
        Gitlet newRepo = RepoUtils.getRepo();
        System.out.println("REPO.index.blobs = " + newRepo.index.getBlobs());
        System.out.println("REPO.index.toBeAdded = " + newRepo.index.getToBeAdded());

    }
}
