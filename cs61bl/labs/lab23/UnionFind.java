import java.util.ArrayList;
import java.util.HashSet;

/** A simple implementation of the UnionFind abstract data structure with path
 *  compression. This UnionFind structure only holds integers and there are two
 *  critical operations: union and find. When unioning two elements, the element
 *  contained in a tree of smaller size is placed as a subtree to the root
 *  vertex of the larger tree. Meanwhile finding an element implements path
 *  compression. When we find a particular vertex, that vertex and the 
 *  other vertices from that vertex to the root are automatically
 *  connected to the root of that tree.
 *
 *  Using the union find data structure allows for a fast implementation of
 *  Kruskal's algorithm as well as other set based operations.
 *
 *  @author
 *  @since
 */
public class UnionFind {

    /** Instance variables go here? */
    int[] list;
    int totalSize;

    /** Returns a UnionFind data structure holding N vertices. Initially, all
     *  vertices are in disjoint sets. */
    public UnionFind(int n) {
        list = new int[n];

        totalSize = n;
        for (int i = 0; i < n; i++) {
            list[i] = -1;
        }
    }

    /** Returns the size of the set V1 belongs to. */
    public int sizeOf(int v1) {

        if (isInRange(v1)) {
            int root = getRoot(v1);

            if (root == v1) {
                int size = list[v1];
                size = Math.abs(size);
                return size;
            } else {
                return sizeOf(parent(v1));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Returns the parent of v1. If v1 is the root of a tree,
     *  returns the negative size of the tree for which v1 is the root. */
    public int parent(int v1) {

        if (isInRange(v1)) {
            if (getRoot(v1) == v1) {
                return -sizeOf(v1);
            } else {
                return list[v1];
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getRoot (int v1) {

        if (isInRange(v1)) {
            if (list[v1] < 0) {
                return v1;
            } else {
                return getRoot(list[v1]);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Returns true if nodes V1 and V2 are connected. */
    public boolean isConnected(int v1, int v2) {

        if (isInRange(v1) && isInRange(v2)) {
            return getRoot(v1) == getRoot(v2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isInRange(int v1) {
        return (v1 >= 0 && v1 < totalSize);
    }

    /** Returns the root of the set VERTEX belongs to. Path-compression, where the
     *  vertices along the search path from VERTEX to its root and VERTEX are linked
     *  directly to the root, is employed allowing for fast search-time. */
    public int find(int vertex) {

        if (vertex >= 0 && vertex < totalSize) {
            if (vertex == getRoot(vertex)) {
                return vertex;
            } else {
                if (parent(vertex) == getRoot(vertex)) {
                    return getRoot(vertex);
                } else {
                    setParent(parent(vertex), getRoot(vertex));
                    list[vertex] = getRoot(vertex);
                    return getRoot(vertex);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Connects two elements V1 and V2 together in the UnionFind structure. V1
     *  and V2 can be any element and a union-by-size heuristic is used.
     *  If the sizes are equal, tie break by connecting the first to the second.
     *  Union-ing a vertex with itself or vertices already connected should not 
     *  change anything. */
    public void union(int v1, int v2) {

        if (isInRange(v1) && isInRange(v2)) {
            if (getRoot(v1) != getRoot(v2)) {
                if (sizeOf(v1) == sizeOf(v2) || sizeOf(v1) < sizeOf(v2)) {
                    list[getRoot(v2)] = -sizeOf(v1) + -sizeOf(v2);
                    list[getRoot(v1)] = getRoot(v2);
                } else {
                    list[getRoot(v1)] = -sizeOf(v1) + -sizeOf(v2);
                    list[getRoot(v2)] = getRoot(v1);
                }
            }
        }
    }

    public void setParent(int v1, int root) {

        list[v1] = root;
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(5);

        uf.union(0,1);
        uf.union(2,3);
        uf.union(0,3);
        uf.union(3,4);

        System.out.println(uf.parent(0));
        System.out.println(uf.parent(1));
        System.out.println(uf.parent(2));
        System.out.println(uf.parent(3));
        System.out.println();
        System.out.println(uf.getRoot(0));
        System.out.println(uf.getRoot(1));
        System.out.println(uf.getRoot(2));
        System.out.println(uf.getRoot(3));
        System.out.println();
        System.out.println(uf.find(0));
        System.out.println(uf.parent(0));
        System.out.println(uf.parent(1));
        System.out.println(uf.parent(2));
        System.out.println(uf.parent(3));
        System.out.println();
        System.out.println(uf.sizeOf(0));
        System.out.println(uf.sizeOf(1));
        System.out.println(uf.sizeOf(2));
        System.out.println(uf.sizeOf(3));

    }
}
