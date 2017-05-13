package zadanie13;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import zadanie13.util.ExampleGraphs;
import zadanie13.util.MyEdge;

import java.util.List;

/**
 * Created by tomek on 02.05.17.
 */
public class Main {

    public static void main(String... args) {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = ExampleGraphs.getGridGraph(new String[]{"a", "b"}, 3);
        new zadanie13.EdgeLabelDemo<>(graph);
    }
}
