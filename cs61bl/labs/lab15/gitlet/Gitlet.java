package gitlet;


import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashMap;


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Gitlet extends ControlRepo implements Serializable{

    /** Head saved as SHA1 of headPointer  */
   // String head = "HEAD";

    /** headPointer representing for head:  pointer to specific CommitNode */
  //  CommitNode headPointer;

    /** HashMap of branches including the master : {key = name branches, value = sha1 of the CommitNode}*/
    HashMap<String,String> branches = new HashMap<String, String>();

    /** Create a spliting point to remmeber the Commit need to branched*/
    String splitPoint;

    /** Index variable */
    Index index;


    /** Create a file (fileName) in a folder e.g. .gitlet/foo.txt */
    public static void createFile(String fileName) throws IOException {

        File tempfile = new File("user.dir/.gitlet", fileName);
    }

       /** method to write an object to File using serializable */
    public static void serializeObject(Gitlet gitlet) {
        try {
            //Path file = Paths.get(".gitlet/filename1");
            // create new file
//            File pwd = new File(System.getProperty("User.dir"));
            File testSerial = new File("user.dir/", ".gitlet");
            testSerial.mkdir();
            File tempfile = new File("user.dir/.gitlet/testSerial", "idObject");

            // check file exists?
            boolean isExsit = new File("user.dir/.gitlet/testSerial/idObject").isFile();
            System.out.println("testFile exists ? = " + isExsit);


            FileOutputStream fileOut =
                    new FileOutputStream("user.dir/.gitlet/testSerial/idObject");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gitlet);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in pwd/.gitlet/testSerial/idObject");
        }catch(IOException i) {
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
        }catch(IOException i) {
            i.printStackTrace();
            return null;
        }catch(ClassNotFoundException c) {
            System.out.println("Object cannot found");
            c.printStackTrace();
            return null;
        }

    }



    public static void main (String[] arg){

        Gitlet testRepo1 = new Gitlet();
     //    System.out.println("Head of repo1 :" + testRepo1.head);

        // create a file binary file name testRepo1
       // Path file = Paths.get("testRepo1");
        Gitlet.serializeObject(testRepo1);
        testRepo1 = null;
        Gitlet repo1 = deserialzeGitlet("pwd/.gitlet/testSerial/idObject");
   //     System.out.println("Message of repo1 :" + repo1.head);
    }
}


