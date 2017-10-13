import java.io.*;
import java.util.*;

public class AlphabetSort {


    public static void main(String[] args) {

        String permutation = "";
        BufferedReader mReader
                = new BufferedReader(new InputStreamReader(System.in));
        HashMap<Character, Integer> order = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();

        try {
            while ((permutation = mReader.readLine()) != null) {
                if (permutation.trim().length() > 0) {
                    break;
                }
            }

            boolean valid = true;

            for (int i = 0; i < permutation.length(); i++) {
                if (order.containsKey(permutation.charAt(i))) {
                    valid = false;
                    break;
                } else {
                    order.put(permutation.charAt(i), i);
                }
            }

            if (valid) {
                String word;
                while ((word = mReader.readLine()) != null) {
                    if (word.trim().length() > 0) {
                        list.add(word);
                    }
                }

                if (list.isEmpty()) {

                    throw new IllegalArgumentException();

                } else {

                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).length(); j++) {
                            if (!order.containsKey(list.get(i).charAt(j))) {
                                list.remove(i);
                                i--;
                                break;
                            }
                        }
                    }

                    String[] words = new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        words[i] = list.get(i);
                    }

                    //sort the list
                    Arrays.sort(words, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {

                            int length = Math.min(o1.length(), o2.length());

                            int diff = 0;

                            for (int i = 0; i < length; i++) {

                                if (order.get(o1.charAt(i)) < order.get(o2.charAt(i))) {
                                    diff = -1;
                                } else if (order.get(o1.charAt(i)) > order.get(o2.charAt(i))) {
                                    diff = 1;
                                }

                                if (diff != 0) {
                                    break;
                                }
                            }

                            if (diff == 0) {
                                diff = o1.compareTo(o2);
                            }

                            return diff;
                        }
                    });

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

                    for (int i = 0; i < list.size(); i++) {
                        bw.write(words[i] + "\n");
                        bw.flush();
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
