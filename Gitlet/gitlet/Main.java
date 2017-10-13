
import java.io.*;
import java.text.ParseException;

/**
 * Created by hsong1101 on 7/18/2017.
 */
public class Main {

    public static void main(String... args) throws IOException, ParseException {

        String firstArg ="", secondArg = "";

        if(args != null && args.length == 1){
            firstArg = args[0];
        }else if(args != null && args.length == 2){
            firstArg = args[0];
            secondArg = args[1];
        }else{
            throw new IllegalArgumentException("Invalid Command");
        }

        if (args[0] != null) {
            firstArg = args[0];
        }

        //Check if there is .gitlet already or not
        switch(firstArg){
            case "init":
                init();
                break;
            case "add":
                add(secondArg);
                break;
            case "commit":
                if(secondArg.equals("")){
                    System.out.println("Please enter a commit message.");
                    break;
                }else {
                    Commit commit = new Commit();
                    commit.commit(secondArg);
                    break;
                }
            case "log":
                Command.log();
                break;
            case "global-log":
                Command.globalLog();
                break;
            case "rm":
                Command.rm(secondArg);
                break;
        }

    }

    public static void add(String secondArg) throws IOException {

        File pwd = new File(System.getProperty("user.dir"));
        File src = new File(pwd, "/" + secondArg);
        File dest = new File(pwd, "/.gitlet/stage/" + secondArg);

//if the this version and the version in the current commit are the same, ignore and proceed
        if (src.exists()) {
            if (Command.fileModified(secondArg)) {
                copyFileUsingStream(src, dest);
                dest.createNewFile();
            } else {
                System.out.println("No changes added to the commit.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void init() throws IOException, ParseException {
        File pwd = new File(System.getProperty("user.dir"));
        File f = new File(pwd, ".gitlet");
        File commit = new File(pwd, ".gitlet/commit");
        File stage = new File(pwd, ".gitlet/stage");
        File removed = new File(pwd, ".gitlet/removed");
        File branch = new File(pwd, ".gitlet/branch");
        File master = new File(pwd, ".gitlet/branch/master");

        if(f.exists()){
            System.out.println("A gitlet version-control system already exists in the current directory.");
        }else{
            f.mkdir();
            commit.mkdir();
            stage.mkdir();
            branch.mkdir();
            master.mkdir();
            removed.mkdir();

            Commit comm = new Commit();
            comm.initCommit();
        }
    }

}
