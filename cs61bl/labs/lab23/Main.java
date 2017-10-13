import java.io.File;

/** This class holds a timing suite for the two implementations of Kruskal's
 *  algorithm: one that calculates connectivity by BFS or DFS, and the other by
 *  using the UnionFind data structure.
 *
 *  @author Antares Chen
 *  @since  2016-7-17
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("                   Kruskal Timer v1.0");
        System.out.println("============================================================");

        int count = 1000;
        if (args.length > 0) {
            if (args[0].equals("--all")) {
                File dir = new File("inputs/");
                for (File inFile : dir.listFiles()) {
                    Graph input = Graph.loadFromText(inFile.getAbsolutePath());
                    System.out.println(">> Now processing file: " + inFile.getPath());
                    long timeSpent = time(input);
                    System.out.println(">> Processing time: " + timeSpent + " milliseconds\n");
                }
                return;
            } else {
                try {
                    count = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Bad argument! Cannot parse \"" + args[0] + "\"");
                    return;
                }
            }
        }
        Graph input = Graph.randomGraph(count, count * 3, 100);
        System.out.println(">> Now processing number of vertices = " + count);
        long timeSpent = time(input);
        System.out.println(">> Processing time naive: " + timeSpent + " milliseconds");
        long timeSpentFast = timeFast(input);
        System.out.println(">> Processing time fast: " + timeSpentFast + " milliseconds");
    }

    /** Returns the elapsed time on running Kruskal's algorithm with the
     *  INPUT graph and CONNECTED - the function that determines if two
     *  vertices are connected. */
    public static long time(Graph input) {
        long startTime = System.currentTimeMillis();
        Graph mst = Kruskal.minSpanTree(input);
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
    public static long timeFast(Graph input) {
        long startTime = System.currentTimeMillis();
        Graph mst = Kruskal.minSpanTreeFast(input);
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
