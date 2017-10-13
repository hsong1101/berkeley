package gitlet;

import java.io.File;
import java.io.Serializable;


public class FileUtils implements Serializable {

    // WARNING THIS OVERWRITES
    public static void moveFile(String from, String to) {

        File pwd = new File(System.getProperty("user.dir"));

        File fromFile = new File(pwd, "/" + from);

        if (!fromFile.exists()) {
//            System.out.println("The File: " + from + " does not exist.");
            return;
        }
        byte[] fileContentsByteStream = Utils.readContents(fromFile); //read
        File toFile = new File(pwd, "/" + to);
        Utils.writeContents(toFile, fileContentsByteStream); //write
//        System.out.println("FileUtils: wrote " + from + " to: " + to);

    }

    
    //It works. When you pass in the path, you don't need to put user.dir.
    // Also if a file is in the working dir,
    //put only file name.
    //Ex) for file in working : "fileName"
    //Ex) for file in other dir : ".gitlet/commitNode/FileName"
    public static void deleteFile(String path) {

        File pwd = new File(System.getProperty("user.dir"));

        File fileToDelete = new File(pwd, "/" + path);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        } else {
            System.out.println("File does not exist");
        }

    }
    public static void main(String[] args) throws Exception {  // MAIN

        // test FileUtils.moveFile
        // moveFile("gitlet/Index.java", ".gitlet/branches/FindBackup.test");
        // deleteFile(".gitlet/branches/FindBackup.test");
    }
}
