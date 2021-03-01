package com.whitespace.reader;

import com.whitespace.model.Edge;
import com.whitespace.model.Graph;
import com.whitespace.model.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class GraphInputFileReader {
    public Graph readFile(String inputFilePath) throws IOException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(inputFilePath);
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);

        Graph<Character> graph = new Graph();
        String[] trips = br.readLine().split(",");
        Arrays.stream(trips).forEach(s -> {
            String cleanString = s.trim();
            int distance = Integer.parseInt(cleanString.substring(2));
            if (distance <= 30) {
                char origin = cleanString.charAt(0);
                char destination = cleanString.charAt(1);
                Node originNode = graph.getNodes().getOrDefault(origin, new Node(origin));
                Node destinationNode = graph.getNodes().getOrDefault(destination, new Node(destination));
                Edge edge = new Edge(distance, originNode, destinationNode);
                graph.getVertices().add(edge);
                graph.getNodes().put(origin, originNode);
                graph.getNodes().put(destination, destinationNode);
            }
        });
        br.close();
        return graph;
    }
}
