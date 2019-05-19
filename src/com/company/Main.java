package com.company;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;

public class Main {
    public static void main(String[] args) {
//        int numOfVertices = Integer.parseInt(args[0]);
//        int numOfEdges = Integer.parseInt(args[1]);
//        int maxCapacity = Integer.parseInt(args[2]);
        int numOfVertices = 10;
        int numOfEdges = 3;
        int maxCapacity = 10;

        if (numOfVertices < 0 || numOfEdges < 0 || maxCapacity < 0)
            throw new IllegalArgumentException("Number of edges must be nonnegative");

        FlowNetwork flowNetwork = new FlowNetwork(numOfVertices, numOfEdges, maxCapacity);
        int idSource = 0;
        int idSink = flowNetwork.V() - 1;
        StdOut.println(flowNetwork);

        // compute maximum flow and minimum cut
        FordFulkerson maxFlow = new FordFulkerson(flowNetwork, idSource, idSink);

        StdOut.println("Max flow from " + idSource + " to " + idSink);
        for (int v = 0; v < flowNetwork.V(); v++) {
            for (FlowEdge e : flowNetwork.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    StdOut.println("   " + e);
            }
        }

        // print min-cut
        StdOut.print("Min cut: ");
        for (int v = 0; v < flowNetwork.V(); v++) {
            if (maxFlow.inCut(v)) StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.println("Max flow value = " +  maxFlow.value());
    }
}
