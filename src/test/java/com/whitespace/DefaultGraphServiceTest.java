package com.whitespace;

import com.whitespace.model.Edge;
import com.whitespace.model.Graph;
import com.whitespace.model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultGraphServiceTest {
    DefaultGraphService defaultGraphService;

    @BeforeEach
    void setupGraph() {
        Graph graph = new Graph();
        Node a = new Node('a');
        Node b = new Node('b');
        Node c = new Node('c');
        Node d = new Node('d');
        Node e = new Node('e');
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);

        graph.getEdges().add(new Edge(5, a, b));
        graph.getEdges().add(new Edge(4, b, c));
        graph.getEdges().add(new Edge(5, a, d));
        graph.getEdges().add(new Edge(8, d, c));
        graph.getEdges().add(new Edge(7, a, e));
        graph.getEdges().add(new Edge(8, c, d));
        graph.getEdges().add(new Edge(6, d, e));
        graph.getEdges().add(new Edge(2, c, e));
        graph.getEdges().add(new Edge(3, e, b));
        defaultGraphService = new DefaultGraphService(graph);
    }

    @Test
    void pathToNonSelf_shortestPath_distanceIs9() {
        int a2b = defaultGraphService.calculateShortestPath('a', 'c');
        assertThat(a2b).isEqualTo(9);
    }

    @Test
    public void pathToSelf_shortestPath_distanceIs9() {
        int d2c = defaultGraphService.calculateShortestPath('b', 'b');
        assertThat(d2c).isEqualTo(9);
    }

    @Test
    public void definedPath_calculateDirectPathLength_pathIsKnown() {
        Object[] values = {'a', 'b', 'c'};
        int a2b = defaultGraphService.calculateDirectPath(values);
        assertThat(a2b).isEqualTo(9);

        values = new Object[]{'a', 'd'};
        int a2d = defaultGraphService.calculateDirectPath(values);
        assertThat(a2d).isEqualTo(5);

        values = new Object[]{'a', 'd', 'c'};
        int a2e = defaultGraphService.calculateDirectPath(values);
        assertThat(a2e).isEqualTo(13);

        values = new Object[]{'a', 'e', 'b', 'c', 'd'};
        int b2d = defaultGraphService.calculateDirectPath(values);
        assertThat(b2d).isEqualTo(22);
    }

    @Test
    void noPathDefined_calculateDirectPathLength_noPathReturned() {
        int e2d = defaultGraphService.calculateDirectPath(new Object[]{'a', 'e', 'd'});
        assertThat(e2d).isEqualTo(-1);
    }

    @Test
    public void pathToNonSelf_exactNumberOfStops_pathIsReturned() {
        assertThat(defaultGraphService.calculateRoutesByExactStops('a', 'c', 4).size()).isEqualTo(3);
    }

    @Test
    void pathToSelf_routesByMaxStops_multiplePathsReturned() {
        assertThat(defaultGraphService.calculateRoutesByMaxStops('c', 'c', 3).size()).isEqualTo(2);
    }

    @Test
    public void pathToSelf_routesByMaxDistance_multiplePathsReturned() {
        assertThat(defaultGraphService.calculateRoutesByMaxDistance('c', 'c', 30).size())
                .isEqualTo(9);
    }

}