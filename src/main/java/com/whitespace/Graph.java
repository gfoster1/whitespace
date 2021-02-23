package com.whitespace;

import lombok.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class Graph<T> {
    Map<T, Node<T>> nodes = new HashMap<>();
    Set<Edge> vertices = new HashSet<>();

    public void addNode(Node<T> node) {
        nodes.put(node.getValue(), node);
    }

    public Set<Edge> getAdjacentNodes(Node target) {
        return vertices.stream()
                .filter(edge -> edge.getOrigin().equals(target))
                .collect(Collectors.toSet());
    }
}
