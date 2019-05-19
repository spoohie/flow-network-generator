package com.company;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;

public class FlowNetworkGenerator {

    private FlowNetwork flowNetwork;
    private FordFulkerson maxFlow;

    public void generate(int numOfVertices, int maxCapacity) {
        flowNetwork = new FlowNetwork(numOfVertices, maxCapacity);
        int idSource = 0;
        int idSink = flowNetwork.V() - 1;

        maxFlow = new FordFulkerson(flowNetwork, idSource, idSink);
    }

    public void presentResults() {
        StdOut.println(flowNetwork);

        StdOut.println("Max flow from source to sink");
        for (int v = 0; v < flowNetwork.V(); v++) {
            for (FlowEdge e : flowNetwork.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    StdOut.println("   " + e);
            }
        }

        StdOut.println("Max flow value = " +  maxFlow.value());

    }

    public void generateOutputFile() {

    }

    public static void main(String[] args) {
        int numOfVertices = Integer.parseInt(args[0]);
        int maxCapacity = Integer.parseInt(args[1]);

        if (numOfVertices < 2 || maxCapacity < 1)
            throw new IllegalArgumentException("Invalid input parameters");

        FlowNetworkGenerator flowNetworkGenerator = new FlowNetworkGenerator();

        flowNetworkGenerator.generate(numOfVertices, maxCapacity);

        flowNetworkGenerator.presentResults();
        flowNetworkGenerator.generateOutputFile();
    }
}
