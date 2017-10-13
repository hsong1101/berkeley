package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/*
    Things to modify

    should print out message when no command is put instead of throwing exception

    checkout
    test2 - add problem
    test23 - add problem
    log() gloabl-log()

    test24 - add problem

    find()
    test20
    test21
    test22

 */


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Checkout {

    public static final int FILE = 1;
    public static final int BRANCH = 2;
    public static final int FILEID = 3;
    public File pwd = new File(System.getProperty("user.dir"));

    String secondArg, thirdArg, fourthArg;
    //test your method here
    public void doIt(Gitlet gitletRepo, String[] args) {

        int option = checkArgs(args);

        switch (option) {
            case BRANCH:
                branch(args[1]);
                break;
            case FILE:
                file(args[2]);
                break;
            case FILEID:
                fileId(args[1], args[3]);
                break;

        }
    }

    public int checkArgs(String[] args){

        if (args == null) {
            System.out.println("Please enter a command.");
            return 0;

        } else if(args.length == 2 && args[1].equals("--")){
            System.out.println("Please enter a file name");
            return 0;

        } else if (args.length == 2 && args[1].length() != 2) {

            System.out.println("Invalid command");
            return 0;

        } else if (args.length == 2) {

            return BRANCH;

        } else if (args.length == 3){

            secondArg = args[1];
            thirdArg = args[2];
            if(secondArg.equals("--")) {
                if(thirdArg == null){
                    System.out.println("Please enter a file name");

                }else {
                    return FILE;
                }
            }
        } else if (args != null && args.length == 4){
            secondArg = args[1];
            thirdArg = args[2];
            fourthArg = args[3];
            if(thirdArg.equals("--")){
                return FILEID;
            }
        } else{
            System.out.println("Invalid command");
            return 0;
        }
        return 0;


    }

    public boolean hasUntracked(){
        String head = RepoUtils.getRepo().branches.get("HEAD");
        CommitNode commit = CommitUtils.deserializeCommitNode(head);
        HashMap<String, String> blobs = commit.blobs;

//        File file = new File(pwd, "/.gitlet/");

        File[] working = new File(pwd, "/").listFiles();

        //if the blob size is smaller, it means there is a newly introduced file.
        if(blobs.size() < working.length){
            return true;
        }else {
            //check each file if it's in the blob
            for (File file : working) {
                if (!blobs.containsKey(file.getName().toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void branch(String name){

        //check if there is a untracked file
        if(RepoUtils.getRepo().branches.containsKey(name)) {

            //check if the branch exists
            if (!hasUntracked()) {

                //check if the current head is in the given branch
                if (RepoUtils.getRepo().branches.get("HEAD").equals(name)) {
                    System.out.println("No need to checkout the current branch.");
                } else {

                    //sha of the current head
                    String id = RepoUtils.getRepo().branches.get(name);

                    CommitNode commit = CommitUtils.deserializeCommitNode(id);

                    //blobs in the current head
                    HashMap<String, String> blobs = commit.blobs;

                    Iterator it = blobs.entrySet().iterator();

                    while (it.hasNext()) {
                        String fileName = it.next().toString();
                        //A new file
                        File file = new File(pwd, "/" + fileName);
                        //A file to be copied
                        File blob = new File(pwd, "/.gitlet/blobs/" + blobs.get(fileName));

                        //Copy file
                        try {
                            FileInputStream ins = null;
                            FileOutputStream outs = null;
                            ins = new FileInputStream(blob);
                            outs = new FileOutputStream(file);
                            byte[] buffer = new byte[1024];
                            int length;

                            while ((length = ins.read(buffer)) > 0) {
                                outs.write(buffer, 0, length);
                            }

                            file.createNewFile();
                            ins.close();
                            outs.close();

                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }

                    //change the head
                    RepoUtils.getRepo().branches.replace("HEAD", name);

                    //erase all staged files
                    File[] files = new File(pwd, "/.gitlet/stage/").listFiles();

                    for (File file : files) {
                        file.delete();
                    }

                }
            } else {
                System.out.println("There is an untracked file in the way; delete it or add it first.");
            }
        }else{
            System.out.println("No such branch exists.");
        }

    }

    public void file(String name){

//        String branch = repo.branches.get("HEAD");
//        String id = RepoUtils.getRepo().branches.get(branch);
//
//        CommitNode commit = CommitUtils.deserializeCommitNode(id);
//        if(commit != null) {
//            copyFile(name, commit);
//        }else{
//            System.out.println("error");
//        }

    }

    public void fileId(String id, String name){

//        File file = new File(System.getProperty("user.dir"), ".gitlet/commitNodes/" + id);
//
//        //check if the given commit exists
//        if(file.exists()) {
//            CommitNode commit = CommitUtils.deserializeCommitNode(id);
//
//            copyFile(name, commit);
//        }else{
//            System.out.println("No commit with that id exists.");
//        }

    }

    public void copyFile(String name, CommitNode commit){


        if(commit.blobs != null) {

        //the blobs is null
//        System.out.println(commit.blobs);


            if (commit.blobs.containsKey(name)) {

                File file = new File(pwd, "/.gitlet/blobs/" + commit.blobs.get(name));
                File file2 = new File(pwd, "/" + name);

                if(file2.exists()){
                    file2.delete();
                }

                File file3 = new File(pwd, "/" + name);
                //copying file to working dir

                try {
                    FileInputStream ins = null;
                    FileOutputStream outs = null;
                    ins = new FileInputStream(file);
                    outs = new FileOutputStream(file3);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = ins.read(buffer)) > 0) {
                        outs.write(buffer, 0, length);
                    }


                    file3.createNewFile();
                    ins.close();
                    outs.close();

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }
            } else {
                System.out.println("File does not exist in that commit.");
            }
        }
    }
}
