package gitlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


/**
 * Assorted utilities.
 *
 * @author P. N. Hilfinger
 */
class Utils implements Serializable {

    /* SHA-1 HASH VALUES. */

    /**
     * Returns the SHA-1 hash of the concatenation of VALS, which may
     * be any mixture of byte arrays and Strings.
     */
    static String sha1(Object... vals) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (Object val : vals) {
                if (val instanceof byte[]) {
                    md.update((byte[]) val);
                } else if (val instanceof String) {
                    md.update(((String) val).getBytes(StandardCharsets.UTF_8));
                } else {
                    throw new IllegalArgumentException("improper type to sha1");
                }
            }
            Formatter result = new Formatter();
            for (byte b : md.digest()) {
                result.format("%02x", b);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }

    /**
     * Returns the SHA-1 hash of the concatenation of the strings in
     * VALS.
     */
    static String sha1(List<Object> vals) {
        return sha1(vals.toArray(new Object[vals.size()]));
    }

    /* FILE DELETION */

    /**
     * Deletes FILE if it exists and is not a directory.  Returns true
     * if FILE was deleted, and false otherwise.  Refuses to delete FILE
     * and throws IllegalArgumentException unless the directory designated by
     * FILE also contains a directory named .gitlet.
     */
    static boolean restrictedDelete(File file) {
        if (!(new File(file.getParentFile(), ".gitlet")).isDirectory()) {
            throw new IllegalArgumentException("not .gitlet working directory");
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Deletes the file named FILE if it exists and is not a directory.
     * Returns true if FILE was deleted, and false otherwise.  Refuses
     * to delete FILE and throws IllegalArgumentException unless the
     * directory designated by FILE also contains a directory named .gitlet.
     */
    static boolean restrictedDelete(String file) {
        return restrictedDelete(new File(file));
    }

    /* READING AND WRITING FILE CONTENTS */

    /**
     * Return the entire contents of FILE as a byte array.  FILE must
     * be a normal file.  Throws IllegalArgumentException
     * in case of problems.
     */
    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Write the entire contents of BYTES to FILE, creating or overwriting
     * it as needed.  Throws IllegalArgumentException in case of problems.
     */
    static void writeContents(File file, byte[] bytes) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            Files.write(file.toPath(), bytes);
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /* OTHER FILE UTILITIES */

    /**
     * Return the concatentation of FIRST and OTHERS into a File designator,
     * analogous to the {@link "java.nio.file.Paths.#get(String, String[])}
     * method.
     */
    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    /**
     * Return the concatentation of FIRST and OTHERS into a File designator,
     * analogous to the {@link "java.nio.file.Paths.#get(String, String[])}
     * method.
     */
    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    /* DIRECTORIES */

    /**
     * Filter out all but plain files.
     */
    private static final FilenameFilter PLAIN_FILES =
        new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        };

    /**
     * Returns a list of the names of all plain files in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> plainFilenamesIn(File dir) {
        String[] files = dir.list(PLAIN_FILES);
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }

    /**
     * Returns a list of the names of all plain files in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> plainFilenamesIn(String dir) {
        return plainFilenamesIn(new File(dir));
    }


    /**
     * Long adds more method
     * <p>
     * /** method to create a new file from a string - default working directory
     */
    public static void createFile(String fileName) {
        try {
            String workingDirectory = System.getProperty("user.dir");
            String absoluteFilePath = "";
            //absoluteFilePath = workingDirectory + System.getProperty("file.separator") + filename;
            absoluteFilePath = workingDirectory + File.separator + fileName;
            //System.out.println("Final filepath : " + absoluteFilePath);
            File file = new File(absoluteFilePath);

            if (file.createNewFile()) {
                //   System.out.println("File is created!");
                return;
            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to write an object to File using serializable
     */

    public static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        }
    }


    /**
     * method to read an file to object using derializable
     */
    public static Object deserialze(File filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object repo = in.readObject();
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

    public static void main(String[] args) throws IOException {
        File pwd = new File(System.getProperty("user.dir"));
        File file = new File("pwd//.gitlet//REPO");
        //boolean isFile = file.createNewFile();
        CommitNode testCommit = new CommitNode();


        Utils.writeContents(Utils.join(pwd.toString(), ".gitlet", "REPO"),
                Utils.serialize(testCommit));
        boolean isExsit = new File("/Users/baovydang/CS61BL/group238/proj2/.gilet/REPO").isFile();
        System.out.println("REPO exists ? : " + isExsit);

        // CommitNode deCommit = (CommitNode)
        // Utils.deserialze(Utils.join(pwd.toString(), "idCommit.ser"));
        // System.out.println(deCommit.message);


    }

}



