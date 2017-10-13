import ucb.junit.textui;
import org.junit.Test;

import java.util.TreeSet;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Trie class.
 *  @author
 */
public class TestKruskal {

    // basic isConnected function
    @Test
    public void isConnectedTest() {
        Graph g = new Graph();
        g.addEdge(3, 6,2);
        g.addEdge(3, 4,6);
        g.addEdge(6, 3,2);
        g.addEdge(6, 4,1);
        g.addEdge(6, 5,9);
        g.addEdge(5, 6,9);
        g.addEdge(5, 4,3);
        g.addEdge(4, 5,3);
        g.addEdge(4, 6,1);
        g.addEdge(4, 3,6);

        // one step test
        assertTrue(Kruskal.isConnected(g,1,2));
        assertTrue(Kruskal.isConnected(g,2,1));
        assertTrue(Kruskal.isConnected(g,3,4));
        assertTrue(Kruskal.isConnected(g,3,6));

        // two step test
        assertTrue(Kruskal.isConnected(g,3,5));
        assertTrue(Kruskal.isConnected(g,6,4));
        assertTrue(Kruskal.isConnected(g,5,3));
        assertTrue(Kruskal.isConnected(g,4,6));

        // disconnected test
        assertFalse(Kruskal.isConnected(g, 3,1));
        assertFalse(Kruskal.isConnected(g, 1,3));

    }

    // basic minSpanTree function
    @Test
    public void minSpanTreeTest() {
        Graph g = new Graph();

        g.addEdge(0, 1, 1);
        g.addEdge(3, 1, 3);
        g.addEdge(1, 5, 1);
        g.addEdge(3, 5, 8);
        g.addEdge(3, 7, 6);
        g.addEdge(5, 2, 6);
        g.addEdge(5, 6, 9);
        g.addEdge(2, 6, 2);
        g.addEdge(2, 4, 6);
        g.addEdge(4, 6, 3);
        g.addEdge(6, 7, 4);

        g = Kruskal.minSpanTree(g);

        System.out.println(g.getAllEdges());





    }
    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestKruskal.class);
    }
}
