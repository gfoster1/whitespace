package com.whitespace;

import java.util.List;

public interface GraphService<T> {
    int calculateDirectPath(T[] values);

    List<List<Edge>> calculateRoutesByMaxStops(T origin, T destination, int maxStops);

    List<List<Edge>> calculateRoutesByMaxDistance(T origin, T destination, int maxDistance);

    List<List<Edge>> calculateRoutesByExactStops(T origin, T destination, int exactStops);

    int calculateShortestPath(T origin, T destination);
}
