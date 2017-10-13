import java.util.*;

/** A class that runs Kruskal's algorithm on a Graph. Given a graph G, Kruskal's
 *  algorithm constructs a new graph T such that T is a spanning tree of G and
 *  the sum of its edge weights is less than the sum of the edge weights for
 *  every possible spanning tree T* of G. This is called the Minimum Spanning
 *  Tree (MST).
 *
 *  @author
 */
public class Kruskal {

    /**
     * Returns the MST of INPUT using a naive isConnected implementation.
     */
    public static Graph minSpanTree(Graph input) {
        Graph mst = new Graph();
        TreeSet<Edge> allEdges = input.getAllEdges();

        UnionFind uf = new UnionFind(input.getAllVertices().size());
        ArrayList<Edge> edges = new ArrayList<>();

        for (Edge edge : allEdges) {
            if( !allConnected(uf)) {
                if (uf.find(edge.getDest()) != uf.find(edge.getSource())) {
                    uf.union(edge.getDest(), edge.getSource());
                    edges.add(new Edge(edge.getSource(), edge.getDest(), edge.getLabel()));
                }
            } else {
                break;
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            mst.addEdge(edges.get(i));
        }

        return mst;
    }

    public static boolean allConnected(UnionFind uf) {

        int count = 0;

        for (int i = 0; i < uf.totalSize; i++) {
            if (uf.find(i) == i) {
                count++;
            }
        }

        if (count > 1) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * Returns the MST of INPUT using the Union Find data structure.
     */
    public static Graph minSpanTreeFast(Graph input) {
        Graph mst = new Graph();
        TreeSet<Edge> allEdges = input.getAllEdges();
        HashSet<Integer> vertex = new HashSet<>();

        UnionFind uf = new UnionFind(input.getAllVertices().size());
        ArrayList<Edge> edges = new ArrayList<>();

        for (Edge edge : allEdges) {
            if( !allConnected(uf)) {
                if (uf.find(edge.getDest()) != uf.find(edge.getSource())) {
                    uf.union(edge.getDest(), edge.getSource());
                    edges.add(new Edge(edge.getSource(), edge.getDest(), edge.getLabel()));
                }
            } else {
                break;
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            mst.addEdge(edges.get(i));
        }

        return mst;
    }

    public static boolean isConnected(Graph g, int v1, int v2) {
        Queue<Integer> fringe = new LinkedList<>();

        if (isInRange(g, v1) && isInRange(g, v2) && !g.equals(new Graph())) {
            if (v1 == v2) {
                return true;
            }

            fringe.add(v1);

            return connectHelper(v1, v2, g, new HashSet<Integer>(), fringe);
        } else if(g.equals(new Graph())){
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean connectHelper(int v1, int v2, Graph g, HashSet<Integer> visited, Queue<Integer> fringe) {

        visited.add(v1);

        if (v1 == v2) {
            return true;
        } else {
            if (g.getNeighbors(v1).size() > 0) {
                for (int i : g.getNeighbors(v1)) {
                    System.out.println(i);
                    if (!visited.contains(i)) {
                        if (i == v2) {
                            return true;
                        } else {
                            fringe.add(i);
                        }
                    }
                }

                return connectHelper(fringe.poll(), v2, g, visited, fringe);
            }
        }

        return false;
    }

    public static boolean isInRange (Graph g, int i) {
        return g.getAllVertices().size() > i && i >= 0;
    }

    public static void main(String[] arg){
        Graph g = new Graph();

        g.addEdge(0,1);
        g.addEdge(1,2);
        g.addEdge(2,1);
        g.addEdge(1,3);
        g.addEdge(3,5);

        System.out.println(minSpanTree(g));
    }
}
