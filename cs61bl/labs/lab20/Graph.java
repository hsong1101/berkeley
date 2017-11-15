import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;
    private int startVertex;
    private static final int INFINITY = Integer.MAX_VALUE;

    private class Edge {

        private Integer from;
        private Integer to;
        private Object edgeInfo;

        public Edge(int from, int to, Object info) {
            this.from = new Integer(from);
            this.to = new Integer(to);
            this.edgeInfo = info;
        }

        public Integer to() {
            return to;
        }

        public Object info() {
            return edgeInfo;
        }

        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeInfo + ")";
        }

    }

    public Graph(int numVertices) {
        adjLists = new LinkedList[numVertices];
        startVertex = 0;
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    public void setStartVertex(int v) {
        if (v < vertexCount && v >= 0) {
            startVertex = v;
        } else {
            throw new IllegalArgumentException("Cannot set iteration start vertex to " + v + ".");
        }
    }

    // Add to the graph a directed edge from vertex v1 to vertex v2.
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, null);
    }

    // Add to the graph an undirected edge from vertex v1 to vertex v2.
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, null);
    }

    // Add to the graph a directed edge from vertex v1 to vertex v2,
    // with the given edge information. If the edge already exists,
    // replaces the current edge with a new edge with edgeInfo.
    public void addEdge(int v1, int v2, Object edgeInfo) {
        Edge edge = new Edge(v1, v2, edgeInfo);

        if (adjLists[v1].contains(edge)) {
            adjLists[v1].set(v1, edge);
        } else {
            adjLists[v1].add(edge);
        }


    }

    // Add to the graph an undirected edge from vertex v1 to vertex v2,
    // with the given edge information. If the edge already exists,
    // replaces the current edge with a new edge with edgeInfo.
    public void addUndirectedEdge(int v1, int v2, Object edgeInfo) {
        Edge edge1 = new Edge(v1, v2, edgeInfo);
        Edge edge2 = new Edge(v2, v1, edgeInfo);

        if (adjLists[v1].contains(edge1)) {
            adjLists[v1].set(v1, edge1);
        } else {
            adjLists[v1].add(edge1);
        }

        if (adjLists[v2].contains(edge2)) {
            adjLists[v2].set(v2, edge2);
        } else {
            adjLists[v2].add(edge2);
        }
    }

    // Return true if there is an edge from vertex "from" to vertex "to";
    // return false otherwise.
    public boolean isAdjacent(int from, int to) {

        for (int i = 0; i < vertexCount; i++) {
            if (from == i) {
                continue;
            }

            for (int j = 0; j < adjLists[from].size(); j++) {
                if (adjLists[from].get(j).to() == to) {
                    return true;
                }
            }
        }

        return false;
    }

    // Returns a list of all the neighboring  vertices 'u'
    // such that the edge (VERTEX, 'u') exists in this graph.
    public List<Integer> neighbors(int vertex) {

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            if (i == vertex) {
                continue;
            } else {
                if (isAdjacent(vertex, i)) {
                    list.add(i);
                }
            }
        }
        list.sort(Collections.reverseOrder());

        return list;
    }

    // Return the number of incoming vertices for the given vertex,
    // i.e. the number of vertices v such that (v, vertex) is an edge.
    public int inDegree(int vertex) {
        int count = 0;

        for (int i = 0; i < vertexCount; i++) {
            if (vertexCount == vertex) {
                continue;
            }

            for (int j = 0; j < adjLists[i].size(); j++) {
                if (adjLists[i].get(j).to() == vertex) {
                    count++;
                }
            }
        }
        return count;
    }

    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    // A class that iterates through the vertices of this graph, starting with a given vertex.
    // Does not necessarily iterate through all vertices in the graph: if the iteration starts
    // at a vertex v, and there is no path from v to a vertex w, then the iteration will not
    // include w
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            startVertex = start;

            fringe = new Stack<>();
            visited = new HashSet<>();

            fringe.push(start);

        }

        public boolean hasNext() {
            return !fringe.empty();
        }

        public Integer next() {

            if (hasNext()) {
                int next = fringe.pop();
                visited.add(next);


                ArrayList<Integer> temp = new ArrayList<>();

                for (int i : neighbors(next)) {
                    if (!visited.contains(i)) {
                        temp.add(i);
                    }
                }

                for(int i = 0; i < temp.size(); i++){

                    fringe.push(temp.get(i));
                    visited.add(temp.get(i));
                }

                return next;
            } else {
                return 0;
            }
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    // Return the collected result of iterating through this graph's
    // vertices as an ArrayList, starting from STARTVERTEX.
    public ArrayList<Integer> visitAll(int startVertex) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(startVertex);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }


    // Returns true iff there exists a path from STARVETEX to
    // STOPVERTEX. Assumes both STARTVERTEX and STOPVERTEX are
    // in this graph. If STARVERTEX == STOPVERTEX, returns true.
    public boolean pathExists(int startVertex, int stopVertex) {

        if (startVertex == stopVertex) {
            return true;
        }

        if (isAdjacent(startVertex, stopVertex)) {
            return true;
        } else {
            ArrayList<Integer> list = visitAll(startVertex);
            return list.contains(startVertex) && list.contains(stopVertex);
        }

    }

    // Returns the path from startVertex to stopVertex.
    // If no path exists, returns an empty arrayList.
    // If startVertex == stopVertex, returns a one element arrayList.
    public ArrayList<Integer> path(int startVertex, int stopVertex) {
        ArrayList<Integer> list = new ArrayList<>();

        if (pathExists(startVertex, stopVertex)) {

            if (isAdjacent(startVertex, stopVertex)) {
                list.add(startVertex);
                list.add(stopVertex);
                return list;
            } else {

                ArrayList<Integer> all = visitAll(startVertex);
//                System.out.println(all);



                if(all.get(0) == startVertex && all.get(all.size()-1) == stopVertex) {
                    int temp = all.size() - 1;
                    for (int i = temp; i > 0; i--) {
                        if (!isAdjacent(all.get(i), all.get(i - 1))) {
                            all.remove(all.get(i - 1));
                        }
                    }
                }else{
                    for(int i = 0; i < all.size(); i++){
                        if(all.get(i) == stopVertex){
                            int temp = all.size() - 1 - i;
                            for(int j = 0; j < temp; j++){
                                all.remove(all.size()-1);
                            }
                        }
                    }

                    int temp = all.size() - 1;
                    for (int i = temp; i > 0; i--) {
                        if (!isAdjacent(all.get(i), all.get(i - 1))) {
                            all.remove(all.get(i - 1));
                        }
                    }
                }

                return all;
            }
        } else {
            return new ArrayList<Integer>();
        }
    }

    public ArrayList<Integer> helper(ArrayList<Integer> list) {

        for (int i = list.size() - 2; i >= 0; i--) {
            int temp = 0;
            if (isAdjacent(list.get(i), list.get(list.size() - 1))) {
                temp = list.size() - 2 - i;
                for (int j = 0; j < temp; j++) {
                    list.remove(i + 1);
                }
            }
        }

        return list;
    }

    public ArrayList<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> popped;


        // more instance variables go here

        public TopologicalIterator() {
            fringe = new Stack<Integer>();
            popped = new HashSet<>();

            for (int i = 0; i < vertexCount; i++) {
                if (inDegree(i) == 0) {
                    fringe.push(i);
                }
            }

        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {

            int next = fringe.pop();
            popped.add(next);

            for (int i : neighbors(next)) {
                if (inDegree(i) == 1) {
                    fringe.push(i);
                } else {
                    boolean pass = true;

                    for (int j = 0; j < vertexCount; j++) {

                        if (next == j) {
                            continue;
                        } else {
                            if (isAdjacent(j, i)) {
                                if (popped.contains(j)) {
                                    continue;
                                } else {
                                    pass = false;
                                }
                            }
                        }
                    }

                    if (pass) {
                        fringe.push(i);
                    }
                }
            }


            return next;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException(
                "vertex removal not implemented");
    }

    public ArrayList<Integer> shortestPath (int startVertex, int endVertex) {

        Integer[] distances = new Integer[vertexCount];
        Integer[] predecessor = new Integer[vertexCount];
        PriorityQueue<Integer> fringe = new PriorityQueue<Integer>(vertexCount, ((x, y) -> (Integer) (distances[x] - distances[y])));
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<Integer> shortestP;

        fringe.add(startVertex);

        for (int i = 0; i < distances.length; i++) {
            distances[i] = INFINITY;
        }
        distances[startVertex] = 0;

        for (int i = 0; i < predecessor.length; i++) {
            predecessor[i] = -1;
        }
        while (!fringe.isEmpty()) {
            Integer v = fringe.poll();
            visited.add(v);
            for (Integer u : neighbors(v)) {

                Integer tempDist = (Integer) getEdge(v, u).info() + distances[v];
                if (tempDist < distances[u]) {
                    distances[u] = tempDist;
                    predecessor[u] = v;
                }

                if (!visited.contains(u)) {
                    fringe.add(u);
                }
            }
            if (v == endVertex) {
                break;
            }
        }
        int vertex = endVertex;

        LinkedList<Integer> tempList = new LinkedList<>();
        while (vertex != startVertex) {
            tempList.addFirst(vertex);
            vertex = predecessor[vertex];
        }
        tempList.addFirst(startVertex);
        shortestP = new ArrayList<>(tempList);
        return shortestP;

    }
//    public ArrayList<Integer> shortestPath (int startVertex, int endVertex) {
//        //your code here...
//
//        if(pathExists(startVertex, endVertex)) {
//
//            ArrayList<Integer> weight = new ArrayList<>(vertexCount);
//            ArrayList<Integer> predecessor = new ArrayList<>(vertexCount);
//            HashSet<Integer> visited = new HashSet<>();
//            HashMap<String, ArrayList<Integer>> map = new HashMap<>();
//
//            this.startVertex = startVertex;
//
//            //initialize list and predecessor
//            for (int i = 0; i < vertexCount; i++) {
//                weight.add(INFINITY);
//                predecessor.add(startVertex);
//            }
//
//            //init start
//            weight.set(startVertex, 0);
//            predecessor.set(startVertex, 0);
//
//            map.put("weight", weight);
//            map.put("predecessor", predecessor);
//
//            map = helper(startVertex, map, visited, endVertex);
//
//            ArrayList<Integer> list = new ArrayList<>();
//
//            list.add(endVertex);
//
//            int i = endVertex;
//
//            while (predecessor.get(i) != startVertex) {
//                list.add(predecessor.get(i));
//                i = predecessor.get(i);
//            }
//
//            list.add(startVertex);
//
//            Collections.reverse(list);
//
//            return list;
//        }else{
//            return new ArrayList<>();
//        }
//    }

//    public HashMap<String, ArrayList<Integer>> helper(int start, HashMap<String, ArrayList<Integer>> map, HashSet<Integer> visited, int endVertex){
//
//        ArrayList<Integer> weight = map.get("weight");
//        ArrayList<Integer> predecessor = map.get("predecessor");
//
//
//        if(!visited.contains(start)) {
//
//            for (int i = 0; i < vertexCount; i++) {
//                if (startVertex == i) {
//                    continue;
//                } else {
//                    Edge edge = getEdge(start, i);
//                    if (edge != null) {
//                        predecessor.set(i, start);
//                        if (weight.get(i) == INFINITY) {
//
//                            weight.set(i, (int) edge.edgeInfo + weight.get(predecessor.get(i)));
//
//                        } else if(weight.get(i) > (int) edge.edgeInfo + weight.get(predecessor.get(i))){
//
//                            weight.set(i, (int) edge.edgeInfo + weight.get(predecessor.get(i)));
//
//                        }
//                    }
//                }
//            }
//
//            int small = INFINITY;
//            int j = 0;
//
//            visited.add(start);
//
//            for (int i = 0; i < vertexCount; i++) {
//                if (i == startVertex || visited.contains(i)) {
//                    continue;
//                } else {
//                    if (weight.get(i) < small) {
//                        small = weight.get(i);
//                        j = i;
//                    }
//                }
//            }
//
//            if(visited.contains(endVertex)){
//                return map;
//            }else {
//                map = helper(j, map, visited, endVertex);
//            }
//        }
//
//        return map;
//    }

    public Edge getEdge(int u, int v) {

        if(u >= 0 && u < adjLists.length) {
            for (Edge edge : adjLists[u]) {
                if (edge.from == u && edge.to == v) {
                    return edge;
                }
            }
        }

        return null;
    }


    public static void main(String[] args) {
        ArrayList<Integer> result;

        Graph g1 = new Graph(5);
        g1.addEdge(0,1, 10);
        g1.addEdge(0,3, 30);
        g1.addEdge(0,4, 100);
        g1.addEdge(1,2, 50);
        g1.addEdge(2,4, 10);
        g1.addEdge(3,2,20);
        g1.addEdge(3,4, 60);

        System.out.println(g1.shortestPath(0,4));

    }
}