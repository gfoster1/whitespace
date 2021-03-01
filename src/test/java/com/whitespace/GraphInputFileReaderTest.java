package com.whitespace;

import com.whitespace.model.Graph;
import com.whitespace.reader.GraphInputFileReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GraphInputFileReaderTest {
    GraphInputFileReader graphInputFileReader = new GraphInputFileReader();

    @Test
    void validFileRead() throws IOException {
        String separator = System.getProperty("file.separator");
        Graph graph = graphInputFileReader.readFile(separator + "TestGraph.txt");
        assertThat(graph.getNodes().size()).isEqualTo(5);
        assertThat(graph.getVertices().size()).isEqualTo(9);
    }

    @Test
    void invalidFileRead() throws IOException {
        String separator = System.getProperty("file.separator");
        Graph graph = graphInputFileReader.readFile(separator + "ConstrainedTestGraph.txt");
        assertThat(graph.getNodes().size()).isEqualTo(2);
        assertThat(graph.getVertices().size()).isEqualTo(1);
    }
}