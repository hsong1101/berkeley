
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.io.File;


public class FileUtils implements Serializable {

    // WARNING THIS OVERWRITES
    public static void moveFile(String from, String to){

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

    public static void deleteFile(String path){

        File pwd = new File(System.getProperty("user.dir"));

        File fileToDelete = new File(pwd, "/" + path);

        if(fileToDelete.exists()){
            fileToDelete.delete();
        }else{
            System.out.println("File does not exist");
        }

    }
    public static void main(String[] args) throws Exception {  // MAIN

        // test FileUtils.moveFile
        deleteFile("TestFile");

    }
}