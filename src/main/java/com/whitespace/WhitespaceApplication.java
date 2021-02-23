package com.whitespace;

import com.whitespace.reader.GraphInputFileReader;

import java.io.IOException;

public class WhitespaceApplication {
    private final GraphInputFileReader graphInputFileReader = new GraphInputFileReader();
    private GraphService<Character> graphService;

    public void execute(String inputFile) throws IOException {
        Graph graph = graphInputFileReader.readFile(inputFile);
        graphService = new DefaultGraphService(graph);
    }

    public void routeDistance(Character[] chars) {
        int i = graphService.calculateDirectPath(chars);
        if (i < 0) {
            System.out.println("NO SUCH ROUTE");
        } else {
            System.out.println(i);
        }
    }

    public void numberOfTripsWithMaxStops(char origin, char destination, int maxStops) {
        System.out.println(graphService.calculateRoutesByMaxStops(origin, destination, maxStops).size());
    }

    public void numberOfTripsWithExactStops(char origin, char destination, int exactStops) {
        System.out.println(graphService.calculateRoutesByExactStops(origin, destination, exactStops).size());
    }

    public void shortTestPath(char origin, char destination) {
        System.out.println(graphService.calculateShortestPath(origin, destination));
    }

    public void numberOfTripsWithMaximumDistance(char origin, char destination, int maxDistance) {
        System.out.println(graphService.calculateRoutesByMaxDistance(origin, destination, maxDistance).size());
    }
}
