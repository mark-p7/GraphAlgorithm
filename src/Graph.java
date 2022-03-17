import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/** Graph class.
 *  Takes a few arguments to create a topological/DFS sorting algorithm.
 * @author Mark De Guzman, Set: S
 * @studentId A01224862
 */
public class Graph {

    private final ArrayList<String> listOfVertices;
    private final boolean directed;
    private final int N;
    private final int[][] adjMatrix;
    private int[][] tempAdjMatrix;
    ArrayList<String> visitedList;

    /** Takes arraylist of the graph vertices and stores it as instance variables.
     *
     * @param listOfVertices labels/vertices for the graph.
     * @param directed directed: true, undirected: false.
     */
    public Graph(ArrayList<String> listOfVertices, boolean directed) {
        this.listOfVertices = listOfVertices;
        this.directed = directed;
        this.N = listOfVertices.size();
        this.adjMatrix = new int[N][N];
        visitedList = new ArrayList<>();
//        printAdjMatrix(adjMatrix); <-- For testing purposes
    }

//    Used to print adjMatrix for testing purposes
//    private void printAdjMatrix(int[][] adjMatrix) {
//        for (int[] matrix : adjMatrix) {
//            System.out.println(Arrays.toString(matrix));
//        }
//        System.out.println();
//    }

    /** Updates the adjacency matrix to indicate that there is an edge between two vertices.
     * @param firstVertex string
     * @param secondVertex string
     */
    void addEdge(String firstVertex, String secondVertex){

        // Gets position of given vertices
        int positionOfFirstVertex = listOfVertices.indexOf(firstVertex);
        int positionOfSecondVertex = listOfVertices.indexOf(secondVertex);

        // Creates an edge between given vertices
        if (directed) {
            adjMatrix[positionOfFirstVertex][positionOfSecondVertex] = 2;
        } else {
            adjMatrix[positionOfFirstVertex][positionOfSecondVertex] = 1;
        }
        adjMatrix[positionOfSecondVertex][positionOfFirstVertex] = 1;
        // | AD = 2 | DA = 1 | A ---> D |
        // from point A to D, is an arrow hence 2. from point D to A is no arrow hence 1.

        // Prints the matrix
        // printAdjMatrix(adjMatrix);
    }

    /** Returns size of graph.
     * @return N size of graph
     */
    private int size(){
        return N;
    }

    /** Returns vertex of given int iteration.
     * @param iteration position of vertex
     * @return vertex string
     */
    private String getVertex(int iteration){
        return listOfVertices.get(iteration);
    }

    /** Returns directed boolean value.
     * @return directed boolean
     */
    private boolean isDirected() {
        return directed;
    }

    /** Returns a valid adjacency matrix of the vertices.
     * @return int[][] array of ints representing the adjacency matrix of the graph
     */
    private int[][] adjMatrix() {
        return adjMatrix;
    }

    /** Returns a valid "DFS Order" of the vertices.
     * Assume that the given starting vertex is valid.
     * @param vertex the starting vertex
     */
    ArrayList<String> getDFSOrder(String vertex) {

        // Clears elements from visited list
        visitedList.clear();

        // Starts DFS from starting node
        dfsHelper(vertex, false);

        // For each node not yet been visited, continues the DFS search until every node is visited
        for (String v : listOfVertices ) {
            if (!isVisited(v)) {
                dfsHelper(v, false);
            }
        }

        return visitedList;
    }

    /** Recursive call that adds a node to the stack through DFS.
     *
     * @param vertex String
     */
    void dfsHelper(String vertex, boolean topoSort) {

        ArrayList<String> adjacentNodes = getAdjacentNodes(vertex, true);

        if (!topoSort) {
            visitedList.add(vertex);
        }

        for (String node : adjacentNodes) {
            if (!isVisited(node)) {
                dfsHelper(node, topoSort);
            }
        }

        if (topoSort) {
            visitedList.add(vertex);
        }
    }

    /** Returns list of adjacent nodes to given vertex.
     *
     * @param vertex String
     * @return ArrayList that contains all adjacent nodes to given vertex
     */
    ArrayList<String> getAdjacentNodes(String vertex, boolean topoSort){

        ArrayList<String> adjacentNodes = new ArrayList<>();
        int positionOfVertex = listOfVertices.indexOf(vertex);

        for (int i = 0; i < adjMatrix.length; i++){
            if (topoSort) {
                if (!isVisited(getVertex(i)) && adjMatrix[positionOfVertex][i] == 2) {
                    adjacentNodes.add(getVertex(i));
                }
            } else {
                if (adjMatrix[positionOfVertex][i] == 2 && directed) {
                    adjacentNodes.add(getVertex(i));
                }
                if (adjMatrix[positionOfVertex][i] == 1 && !directed) {
                    adjacentNodes.add(getVertex(i));
                }
            }
        }

        return adjacentNodes;
    }

    /** Returns true if given vertex has been visited, else false.
     *
     * @param vertex String
     * @return boolean expression. True if visited else false
     */
    boolean isVisited(String vertex) {
        for (String v : visitedList) {
            if(Objects.equals(v, vertex)) {
                return true;
            }
        }
        return false;
    }

    /** Returns a valid "Topological Sort Order" of the vertices.
     * If the graph is undirected, the method returns null.
     * Assume that the graph is acyclic.
     * @param vertex the starting vertex
     */
    ArrayList<String> getTopologicalOrder(String vertex) {

        // Returns null if graph is undirected
        if (!directed) {
            return null;
        }

        // Clears elements from visited list
        visitedList.clear();

        // Step 1: Checks adjacent nodes of vertex through DFS until no more adjacent nodes
        // Step 2: If none, visits node and backtracks.
        // Step 3: If no more adjacent nodes, move to next node in listOfVertices

        // Starts DFS from starting node
        dfsHelper(vertex, true);

        // For each node not yet been visited, continues the DFS search until every node is visited
        for (String v : listOfVertices ) {
            if (!isVisited(v)) {
                dfsHelper(v, true);
            }
        }
        Collections.reverse(visitedList);
        return visitedList;

    }
}

class Driver {

    public static void main(String[] args) {
        System.out.println("Creating Graph...\n");

        // Creates listOfVertices ArrayList
        ArrayList<String> listOfVertices = new ArrayList<>();
        listOfVertices.add("A");
        listOfVertices.add("B");
        listOfVertices.add("C");
        listOfVertices.add("D");
        listOfVertices.add("E");
        listOfVertices.add("F");

        // Initializes a new graph object of given list of vertices
        Graph graph = new Graph(listOfVertices, true);

        // Add edges to graph object
        graph.addEdge("A", "B");
        graph.addEdge("A", "E");
        graph.addEdge("A", "F");
        graph.addEdge("B", "C");
        graph.addEdge("D", "B");
        graph.addEdge("D", "C");
        graph.addEdge("E", "D");
        graph.addEdge("F", "C");
        graph.addEdge("F", "E");

        // Print DFS
        System.out.println(graph.getTopologicalOrder("B"));
        System.out.println(graph.getDFSOrder("B"));

    }
}