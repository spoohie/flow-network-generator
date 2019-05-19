package com.company;

import java.util.HashSet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class FlowNetwork {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<FlowEdge>[] adj;

    /**
     * Initializes an empty flow network with {@code V} vertices and 0 edges.
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public FlowNetwork(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<FlowEdge>();
    }

    public FlowNetwork(int V, int E, int R) {
        this(V);

        // Generate random number of edges from source to random vertexes
        int numOfEdgesFromSource = StdRandom.uniform(1, (int)Math.pow(V, 1/3) + 1);
        HashSet<Integer> generated = new HashSet<>();
        while (generated.size() < numOfEdgesFromSource) {
            int next = StdRandom.uniform(1, V-1);
            generated.add(next);
        }
        for (int w : generated) {
            double capacity = StdRandom.uniform(1, R);
            addEdge(new FlowEdge(0, w, capacity));
        }

        // Generate random number of edges from random vertexes to sink
        int numOfEdgesToSink = StdRandom.uniform(1, (int)Math.pow(V, 1/3) + 1);
        generated.clear();
        while (generated.size() < numOfEdgesToSink) {
            int next = StdRandom.uniform(1, V-1);
            generated.add(next);
        }
        for (int v : generated) {
            double capacity = StdRandom.uniform(1, R);
            addEdge(new FlowEdge(v, V-1, capacity));
        }

        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V-1);
            int w = StdRandom.uniform(v+1, V);
            double capacity = StdRandom.uniform(1, R);
            addEdge(new FlowEdge(v, w, capacity));
        }
    }

    /**
     * Initializes a flow network from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge capacities,
     * with each entry separated by whitespace.
     * @param in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public FlowNetwork(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validateVertex(v);
            validateVertex(w);
            double capacity = in.readDouble();
            addEdge(new FlowEdge(v, w, capacity));
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
        Bag<FlowEdge> list = new Bag<FlowEdge>();
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
}
