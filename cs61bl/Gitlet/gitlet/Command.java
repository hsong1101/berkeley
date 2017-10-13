import java.io.*;

/**
 * Created by hsong1101 on 7/18/2017.
 */
public class Command {

    public static void rm(String arg) throws IOException {

        if(arg == ""){
            System.out.println("Please enter a file.");
        }else{
            boolean exist = false;

            //check stage first
            //if not, put it in removed
            File[] list = new File(".gitlet/stage").listFiles();
            if(list.length > 0){
                for(int i = 0; i < list.length; i++){
                    if(list[i].getName().toString().equals(arg)){
                        list[i].delete();
                        exist = true;
                        break;
                    }
                }
            }

            if(!exist) {
                File[] list2 = new File(".gitlet/commit/" + Command.readCurrentHead()).listFiles();
                if (list2.length > 0) {
                    for (int i = 0; i < list2.length; i++) {
                        if (list2[i].getName().toString().equals(arg)) {
                            File dest = new File(".gitlet/removed/" + list2[i].getName());
                            copyFileUsingStream(list2[i], dest);
                            dest.createNewFile();
                            exist = true;
                            break;
                        }
                    }
                }
            }

            if(!exist){
                System.out.println("File does not exist.");
            }
        }
    }

    public static void log(){

    }

    public static void globalLog(){
        File pwd = new File(System.getProperty("user.dir"));

        File commits = new File(pwd, "/.gitlet/commit");
        File[] list = commits.listFiles();

        for(int i = 0; i < list.length; i++){
            File temp = new File(pwd, "/.gitlet/commit/" + list[i].getName() + "/info");
            printInfo(temp);
        }
    }

    public static void printInfo(File file){
        BufferedReader br = null;
        FileReader fr = null;

        System.out.println();
        System.out.println("===");

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            br = new BufferedReader(new FileReader(file));

            System.out.println(br.readLine());
            System.out.println(br.readLine());
            System.out.println(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void writeInfo(CommitNode cm){
        File pwd = new File(System.getProperty("user.dir"));
        File info = new File(pwd, "/.gitlet/commit/" + cm.getSha() + "/info");
        BufferedWriter bw = null;
        FileWriter fw = null;


        try {
            fw = new FileWriter(info);
            bw = new BufferedWriter(fw);
            bw.write("Commit " + cm.getSha() + "\n");
            bw.write(cm.getDate()+"\n");
            bw.write(cm.getMessage()+"\n");
            bw.write(cm.getPrev()+"\n");

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

    public static String readCurrentHead(){

        File pwd = new File(System.getProperty("user.dir"));
        File HEAD = new File(pwd, "/.gitlet/HEAD");

        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(HEAD);
            br = new BufferedReader(fr);

            br = new BufferedReader(new FileReader(HEAD));

            //return current head's sha
            return br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static boolean fileModified(String secondArg) throws IOException {

        File pwd = new File(System.getProperty("user.dir"));
        String curr = readCurrentHead();

        //get the file of the current head

        File compare = new File(pwd, "/.gitlet/commit/" + curr + "/" + secondArg);
        if(!compare.exists()){
            return true;
        }else {
            File compared = new File(pwd, "/" + secondArg);

            BufferedReader fs1 = new BufferedReader(new FileReader(compare));
            BufferedReader fs2 = new BufferedReader(new FileReader(compared));

            String f1 = fs1.readLine();
            String f2 = fs2.readLine();
            boolean diff = false;

            while(f1 != null || f2 != null){
                if(f1 == null || f2 == null){
                    diff = true;
                    break;
                }else if(!f1.equalsIgnoreCase(f2)){
                    diff = true;
                    break;
                }

                f1 = fs1.readLine();
                f2 = fs2.readLine();
            }

            fs2.close();
            fs1.close();

            return diff;
        }
    }

    static void copyFileUsingStream(File source, File dest) throws IOException {
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
            is.close();
            os.close();
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

}
