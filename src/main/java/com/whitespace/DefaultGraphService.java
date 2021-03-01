package com.whitespace;

import com.whitespace.model.Edge;
import com.whitespace.model.Graph;
import com.whitespace.model.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultGraphService<T> implements GraphService<T> {
    private final Graph<T> graph;

    public int calculateDirectPath(T[] values) {
        int totalDistance = 0;
        for (int i = 0; i < values.length - 1; i++) {
            T originValue = values[i];
            T destinationValue = values[i + 1];
            List<List<Edge>> distance = calculateRoutesByMaxStops(originValue, destinationValue, 1);
            if (distance.isEmpty()) {
                return -1;
            }
            totalDistance += distance.get(0).get(0).getWeight();
        }
        return totalDistance;
    }

    public List<List<Edge>> calculateRoutesByMaxStops(T origin, T destination, int maxStops) {
        Node<T> originNode = graph.getNodes().get(origin);
        Node<T> destinationNode = graph.getNodes().get(destination);
        List<List<Edge>> simplePaths = new ArrayList<>();
        depthFirstSearch(originNode, destinationNode,
                new HashSet<>(),
                new Stack<>(), simplePaths,
                edges -> edges.size() > maxStops);
        return simplePaths;
    }

    public List<List<Edge>> calculateRoutesByMaxDistance(T origin, T destination, int maxDistance) {
        Node<T> originNode = graph.getNodes().get(origin);
        Node<T> destinationNode = graph.getNodes().get(destination);
        List<List<Edge>> simplePaths = new ArrayList<>();
        depthFirstSearch(originNode, destinationNode, new HashSet<>(), new Stack<>(), simplePaths,
                edges -> {
                    int i = edges.stream()
                            .map(edge -> edge.getWeight())
                            .reduce(0, Integer::sum);
                    return i > maxDistance;
                });
        return simplePaths;
    }

    public List<List<Edge>> calculateRoutesByExactStops(T origin, T destination, int exactStops) {
        return calculateRoutesByMaxStops(origin, destination, exactStops).stream()
                .filter(vertices -> vertices.size() == exactStops)
                .collect(Collectors.toList());
    }

    private void depthFirstSearch(Node currentNode,
                                  Node destinationNode,
                                  Set<Node> visited,
                                  Stack<Edge> currentPath,
                                  List<List<Edge>> simplePath,
                                  Predicate<Stack<Edge>> depthPredicate) {
        List<Edge> edges = graph.getAdjacentNodes(currentNode).stream()
                .collect(Collectors.toList());
        for (Edge edge : edges) {
            Node node = edge.getDestination();

            currentPath.push(edge);
            visited.add(node);
            if (depthPredicate.test(currentPath)) {
                currentPath.pop();
                visited.remove(node);
                return;
            }

            if (node.equals(destinationNode)) {
                simplePath.add(new ArrayList<>(currentPath));
            }
            depthFirstSearch(node, destinationNode, visited, currentPath, simplePath, depthPredicate);
            currentPath.pop();
        }
    }

    public int calculateShortestPath(T origin, T destination) {
        Set<Node> shortestPath = new HashSet<>();
        Node originNode = graph.getNodes().get(origin);
        NodePath source = new NodePath(originNode, null, 0);

        PriorityQueue<NodePath> queue = new PriorityQueue<>();
        queue.add(source);

        Map<Node, NodePath> pathMap = new HashMap<>();
        pathMap.put(originNode, source);

        while (!queue.isEmpty()) {
            NodePath currentNodePath = queue.poll();
            Node currentNode = currentNodePath.currentNode;

            if (currentNode.getValue().equals(destination) && shortestPath.contains(originNode)) {
                return currentNodePath.totalDistance;
            }

            List<Edge> adjacentNodes = new ArrayList<>(graph.getAdjacentNodes(currentNode));
            for (int i = 0; i < adjacentNodes.size(); i++) {
                Edge edge = adjacentNodes.get(i);
                Node adjacentNode = edge.getDestination();
                int totalDistance = currentNodePath.totalDistance + edge.getWeight();
                if (pathMap.containsKey(adjacentNode)) {
                    // Path map contains neighbor
                    // See if the new distance is less than the current
                    NodePath nodePath = pathMap.get(adjacentNode);
                    if (adjacentNode.getValue().equals(destination) && shortestPath.contains(adjacentNode)) {
                        nodePath.totalDistance = totalDistance;
                    } else if (totalDistance < nodePath.totalDistance) {
                        nodePath.predecessor = currentNodePath;
                        nodePath.totalDistance = totalDistance;
                    }
                } else {
                    // Neighbor not discovered
                    NodePath nodePath = new NodePath(adjacentNode, currentNodePath, totalDistance);
                    pathMap.put(adjacentNode, nodePath);
                    queue.add(nodePath);
                }
            }
            shortestPath.add(currentNode);
        }
        if (pathMap.size() == shortestPath.size() && source.totalDistance > 0) {
            return source.totalDistance;
        }
        return -1;
    }

    @Data
    @AllArgsConstructor
    private static final class NodePath implements Comparable<NodePath> {
        private final Node currentNode;

        private NodePath predecessor;
        private int totalDistance;

        @Override
        public int compareTo(NodePath o) {
            return Integer.compare(totalDistance, o.totalDistance);
        }
    }
}
