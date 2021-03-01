package com.whitespace.model;

import lombok.Value;

@Value
public class Edge {
    int weight;
    Node origin;
    Node destination;
}
