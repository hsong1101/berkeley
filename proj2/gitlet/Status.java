package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by hsong1101 on 7/15/2017.
 */
public class Status {
    //test your method here

    private ArrayList<String> items = new ArrayList<>();

    public void doIt(Gitlet repo, String secondarg) {
        Index e = repo.index;

        ArrayList<String> itemsList = new ArrayList<>();

        System.out.println("=== Branches ===");
        String head = repo.branches.get("HEAD");
        for (HashMap.Entry<String, String> branchKey : repo.branches.entrySet()) {
            if (branchKey.getKey().equals(head)) {
                itemsList.add("*" + branchKey.getKey());
            }
            if (!branchKey.getKey().equals("HEAD") && !branchKey.getKey().equals(head)) {
                itemsList.add(branchKey.getKey());
            }
        }
        Collections.sort(itemsList);
        printList(itemsList);
        itemsList = new ArrayList<>();

        System.out.println("");
        System.out.println("=== Staged Files ===");
        for (HashMap.Entry<String, String> addKey : repo.index.getToBeAdded().entrySet()) {
            itemsList.add(addKey.getKey());
        }
        Collections.sort(itemsList);
        printList(itemsList);
        itemsList = new ArrayList<>();

        System.out.println("");
        System.out.println("=== Removed Files ===");
        //this should instead print removed files not the toberemoved files.
        //should check the last commit's blobs and files in working dir
        //compare and the ones present in the blob but not in working dir are considered as removed


        String temp = RepoUtils.getRepo().branches.get("HEAD");
        String id = RepoUtils.getRepo().branches.get(temp);
        CommitNode commit = CommitUtils.deserializeCommitNode(id);
        File[] file = new File(System.getProperty("user.dir"), "/").listFiles();
        ArrayList<String> removed = new ArrayList<>();

        if (commit.getBlobs() != null) {
            HashMap<String, String> files = new HashMap<>();
            for (int i = 0; i < file.length; i++) {
                if (!files.containsKey(file[i])) {
                    files.put(file[i].getName(), "");
                }
            }

            Iterator<HashMap.Entry<String, String>> it = commit.getBlobs().entrySet().iterator();

            while (it.hasNext()) {
                String name = it.next().getKey();
                if (files.containsKey(name)) {
                    continue;
                } else {
                    removed.add(name);
                }
            }
        }

        Collections.sort(removed);
        printList(removed);
        itemsList = new ArrayList<>();

        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        RepoUtils.storeRepo(repo);
    }

    public void printList(ArrayList it) {
        ArrayList<String> item = it;
        for (int i = 0; i < item.size(); i++) {
            System.out.println(item.get(i));
        }
    }
}
