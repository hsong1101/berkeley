import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by hsong1101 on 7/18/2017.
 */
public class Commit implements Serializable {


    public void initCommit() throws IOException, ParseException {
        CommitNode initialCommit = new CommitNode();

        initialCommit.setPrev(null);
        initialCommit.setDate(date());
        initialCommit.setMessage("initial commit");
        initialCommit.setSha(Utils.sha1(initialCommit.getDate()));

        File pwd = new File(System.getProperty("user.dir"));
        File f = new File(pwd, ".gitlet/commit/" + initialCommit.getSha());
        File HEAD = new File(pwd, ".gitlet/HEAD");
        File HEAD_MASTER = new File(pwd, ".gitlet/branch/master/HEAD");

        f.mkdir();
        Command.writeInfo(initialCommit);
        HEAD.createNewFile();
        HEAD_MASTER.createNewFile();

        writeToFile(HEAD, initialCommit.getSha());
        writeToFile(HEAD_MASTER, initialCommit.getSha());

    }

    public void commit(String message) throws IOException, ParseException {


        //get all files in the stage
        File stage = new File(".gitlet/stage");
        File removed = new File(".gitlet/removed");
        File[] listOfAdd = stage.listFiles();
        File[] listOfremoved = removed.listFiles();

        //if nothing is in stage

        if(stage.listFiles().length == 0 && removed.listFiles().length == 0) {
            System.out.println("No changes added to the commit.");
        }else {
            File pwd = new File(System.getProperty("user.dir"));

            CommitNode cm = new CommitNode();
            cm.setMessage(message);
            cm.setDate(date());
            cm.setSha(Utils.sha1(cm.getDate()));
            cm.setPrev(Command.readCurrentHead());

            //create a dir for new commit
            File commitFolder = new File(pwd, "/.gitlet/commit/" + cm.getSha());

            if (!commitFolder.exists()) {
                commitFolder.mkdir();
            }

            //Copy files from the prev commit to next TRACKING FILES
            File prev = new File( ".gitlet/commit/" + Command.readCurrentHead());
            File[] dir = prev.listFiles();
            File[] remove = new File(".gitlet/removed/").listFiles();

            여기 해야해요~~~~~~~~~~~~
                    delete remove
                    check copy from prev

            if(remove.length > 0) {
                for(int j = 0; j < remove.length; j++) {
                    for (int i = 0; i < dir.length; i++) {
                        if (!dir[i].getName().equals("info") || !dir[i].getName().toString().equals(remove[j].getName().toString())) {
                            File src = dir[i];
                            File destin = new File(pwd, "/.gitlet/commit/" + cm.getSha() + "/" + dir[i].getName());
                            Command.copyFileUsingStream(src, destin);
                            destin.createNewFile();
                        }
                    }
                }

            }

            //update the HEAD
            headUpdate(cm.getSha());

            //Copy files from stage to commit
            for (int i = 0; i < listOfAdd.length; i++) {

                File src = new File(pwd, "/.gitlet/stage/" + listOfAdd[i].getName());
                File dest = new File(pwd, "/.gitlet/commit/" + cm.getSha() + "/" + listOfAdd[i].getName());

                Command.copyFileUsingStream(src, dest);
                dest.createNewFile();

                listOfAdd[i].delete();
            }

            //make a file containing the info of the commit
            Command.writeInfo(cm);

        }
    }

    //Helper methods

    public void headUpdate(String newSha){
        File pwd = new File(System.getProperty("user.dir"));
        File HEAD = new File(pwd, "/.gitlet/HEAD");
        File HEAD_MASTER = new File(pwd, "/.gitlet/branch/master/HEAD");

        writeToFile(HEAD, newSha);
        writeToFile(HEAD_MASTER, newSha);
    }

    public void writeToFile(File file, String sha){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            String content = sha;

            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public String date() throws ParseException {

        DateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        Date date = new Date();

        return dt.format(date);
    }
}
