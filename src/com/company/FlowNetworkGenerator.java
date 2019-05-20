package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class FlowNetworkGenerator {
    private FlowNetwork flowNetwork;
    private FordFulkerson maxFlow;

    private void generate(int numOfVertices, int maxCapacity) {
        flowNetwork = new FlowNetwork(numOfVertices, maxCapacity);
        maxFlow = new FordFulkerson(flowNetwork);
    }

    private void presentResults() {
        ArrayList<String> listOfPaths= flowNetwork.presentMaxFlowPath();

        System.out.print(flowNetwork);
        System.out.print("Flow paths from source to sink:\n");
        for (String s : listOfPaths) {
            System.out.println(s);
        }
        System.out.print("Max flow value = " +  maxFlow.value());
    }

    private void generateOutputFile() {
        ArrayList<String> listOfEdges = flowNetwork.formatToFile();

        try (PrintStream out = new PrintStream(new FileOutputStream("out.txt"))) {
            out.println(maxFlow.value());
            for (String s : listOfEdges) {
                out.println(s);
            }
        }
        catch (FileNotFoundException e) {
            System.out.print("Cannot create or edit existing file. Aborting.");
        }
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
