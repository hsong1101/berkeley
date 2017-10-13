package gitlet;

import java.io.File;

public class MergeFiles {

    public static void mergeFiles(String fileName, String currBlob, String givenBlob) {
        File pwd = new File(System.getProperty("user.dir"));
        File toFile = new File(pwd, "/" + fileName);
        File currFile = new File(pwd, "/blobs/" + currBlob);
        File givenFile = new File(pwd, "/blobs/" + givenBlob);
//        File currFile = new File(pwd, "/" + currBlob);
//        File givenFile = new File(pwd, "/" + givenBlob);

        String msg1 = "<<<<<<< HEAD";
        byte[] top = msg1.getBytes();

        byte[] currStream = Utils.readContents(currFile);
//        byte[] currTest = currBlob.getBytes();

        String msg2 = "=======";
        byte[] mid = msg2.getBytes();

        byte[] givenStream = Utils.readContents(givenFile);
//        byte[] givenTest = currBlob.getBytes();

        String msg3 = ">>>>>>>";
        byte[] end = msg3.getBytes();

        byte[] merge = new byte[top.length + currStream.length + mid.length + givenStream.length + end.length];
//        byte[] merge = new byte[top.length + currStream.length + mid.length + givenStream.length + end.length];
        int destPos = 0;
        System.arraycopy(top, 0, merge, destPos, top.length);

        destPos += top.length;
        System.arraycopy(currStream, 0, merge, destPos, currStream.length);
//        System.arraycopy(currStream, 0, merge, top.length, currStream.length);

        destPos += currStream.length;
        System.arraycopy(mid, 0, merge, destPos, mid.length);

        destPos += mid.length;
        System.arraycopy(givenStream, 0, merge, destPos, givenStream.length);
//        System.arraycopy(currStream, 0, merge, top.length, currStream.length);

        destPos += givenStream.length;
        System.arraycopy(end, 0, merge, destPos, end.length);

        Utils.writeContents(toFile, merge); //write
    }

    public static void main(String[] args) {
//        mergeFiles("testMerge.txt","currtext", "givenText");
        mergeFiles("wugMerge.txt","wug.txt", "wug2.txt");
    }

}
