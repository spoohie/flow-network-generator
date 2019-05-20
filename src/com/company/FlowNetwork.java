package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.princeton.cs.algs4.*;

public class FlowNetwork {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int idSource = 0;
    private final int idSecondVertex = 1;
    private final int V;
    private int E;
    private final int idSink;
    private final int networkSizeFactor;
    private HashSet<FlowEdge>[] adj;

    /**
     * Initializes an empty flow network with {@code V} vertices and 0 edges.
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public FlowNetwork(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.idSink = V-1;
        this.networkSizeFactor = (int)Math.pow(V, 1/3) + 1;
        adj = new HashSet[V];
        for (int v = 0; v < V; v++)
            adj[v] = new HashSet<>();
    }

    public FlowNetwork(int V, int maxRandom) {
        this(V);

        // Generate random number of edges from source to random vertices
        int numOfEdgesFromSource = StdRandom.uniform(1, networkSizeFactor);
        HashSet<Integer> generated = new HashSet<>();
        while (generated.size() < numOfEdgesFromSource) {
            int next = StdRandom.uniform(idSecondVertex, V-1);
            generated.add(next);
        }
        for (int w : generated) {
            double capacity = StdRandom.uniform(1, maxRandom);
            addEdge(new FlowEdge(idSource, w, capacity));
        }

        // Generate random number of edges from random vertices to sink
        int numOfEdgesToSink = StdRandom.uniform(1, networkSizeFactor);
        generated.clear();
        while (generated.size() < numOfEdgesToSink) {
            int next = StdRandom.uniform(idSecondVertex, V-1);
            generated.add(next);
        }
        for (int v : generated) {
            double capacity = StdRandom.uniform(1, maxRandom);
            addEdge(new FlowEdge(v, idSink, capacity));
        }

        // Generate connections inside flow network
        for (int vertex = idSecondVertex; vertex < idSink; ++vertex) {
            // Generate random input edges for every middle vertex (from vertices with lower id)
            int maxNumOfInputEdges = Math.min(vertex, networkSizeFactor);
            int numOfInputEdges = StdRandom.uniform(1, maxNumOfInputEdges + 1);
            generated.clear();
            while (generated.size() < numOfInputEdges) {
                int next = StdRandom.uniform(idSource, vertex);
                if (isAdjacency(next, vertex)) {
                    numOfInputEdges--;
                    continue;
                }
                generated.add(next);
            }
            for (int v : generated) {
                double capacity = StdRandom.uniform(1, maxRandom);
                addEdge(new FlowEdge(v, vertex, capacity));
            }

            // Generate random output edges from every middle vertex (to vertices with higher id)
            int maxNumOfOutputEdges = Math.min(networkSizeFactor, V - vertex - 1);
            int numOfOutputEdges = StdRandom.uniform(1, maxNumOfOutputEdges + 1);
            generated.clear();
            while (generated.size() < numOfOutputEdges) {
                int next = StdRandom.uniform(vertex+1, V);
                if (isAdjacency(vertex, next)) {
                    numOfOutputEdges--;
                    continue;
                }
                generated.add(next);
            }
            for (int w : generated) {
                double capacity = StdRandom.uniform(1, maxRandom);
                addEdge(new FlowEdge(vertex, w, capacity));
            }
        }
    }

    /**
     * Returns the number of vertices in the edge-weighted graph.
     * @return the number of vertices in the edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     * @return the number of edges in the edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the edge {@code e} to the network.
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between
     *         {@code 0} and {@code V-1}
     */
    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public boolean isAdjacency(int v, int w) {
        for (FlowEdge e : adj(v)) {
            if (e.to() == w) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the edges incident on vertex {@code v} (includes both edges pointing to
     * and from {@code v}).
     * @param v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<FlowEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    // return list of all edges - excludes self loops
    public Iterable<FlowEdge> edges() {
        LinkedHashSet<FlowEdge> list = new LinkedHashSet<>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adj(v)) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }

    /**
     * Returns a string representation of the flow network.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *    followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adj[v]) {
                if (e.to() != v) s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public ArrayList<String> presentMaxFlowPath() {
        ArrayList<String> listOfPaths = new ArrayList<>();
        for (int v = 0; v < V(); v++) {
            for (FlowEdge e : adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    listOfPaths.add("   " + e);
            }
        }
        return listOfPaths;
    }

    public ArrayList<String> formatToFile() {
        ArrayList<String> listOfEdges = new ArrayList<>();
        Iterable<FlowEdge> edges = edges();
        for (FlowEdge e : edges) {
            listOfEdges.add(e.from() + " " + e.to() + " " + e.capacity());
        }
        return listOfEdges;
    }
}