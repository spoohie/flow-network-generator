package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class FlowNetworkGenerator {
    private FlowNetwork flowNetwork;
    private FordFulkerson maxFlow;
    private int numFile = 0;

    private void generate(int numOfVertices, int netSize, int maxCapacity) {
        flowNetwork = new FlowNetwork(numOfVertices, netSize, maxCapacity);
        maxFlow = new FordFulkerson(flowNetwork);
    }

    private void presentResults() {
        ArrayList<String> listOfPaths= flowNetwork.presentMaxFlowPaths();

        System.out.print(flowNetwork);
        System.out.println("Flow paths from source to sink:");
        for (String s : listOfPaths) {
            System.out.println(s);
        }
        System.out.print("Max flow value = " +  maxFlow.value());
    }

    private void generateOutputFile() {
        ArrayList<String> listOfEdges = flowNetwork.formatToFile();

        try (PrintStream out = new PrintStream(new FileOutputStream("out" + (numFile++) + ".txt"))) {
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
        int netSize= Integer.parseInt(args[1]);
        int maxCapacity = Integer.parseInt(args[2]);

        if (args.length != 3)
            throw new IllegalArgumentException("Invalid number of arguments (3)");

        if (numOfVertices < 3 || netSize < 1 || maxCapacity < 1)
            throw new IllegalArgumentException("Invalid input parameters");

        FlowNetworkGenerator flowNetworkGenerator = new FlowNetworkGenerator();

        flowNetworkGenerator.generate(numOfVertices, netSize, maxCapacity);
        flowNetworkGenerator.presentResults();
        flowNetworkGenerator.generateOutputFile();
    }
}