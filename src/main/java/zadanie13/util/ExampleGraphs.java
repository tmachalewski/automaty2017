package zadanie13.util;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

/**
 * Created by tomek on 13.05.17.
 */
public class ExampleGraphs {
    public static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromExampleTask() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = new DirectedSparseMultigraph<>();

        List<Integer> node0 = Arrays.asList(0);
        List<Integer> node1 = Arrays.asList(1);
        List<Integer> node2 = Arrays.asList(2);

        graph.addEdge(new MyEdge("a", node0, node1), node0, node1, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("a", node1, node2), node1, node2, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("a", node2, node0), node2, node0, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node0, node2), node0, node2, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node2, node1), node2, node1, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node1, node0), node1, node0, EdgeType.DIRECTED);

        return graph;
    }

    public static DirectedSparseMultigraph<List<Integer>, MyEdge> getGridGraph(String[] overAlphabet, int modulo) {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = new DirectedSparseMultigraph<>();

        Queue<List<Integer>> nodesToProcess = new LinkedList<>();
        List<Integer> startNode = new ArrayList<>();
        for (int i = 0; i < overAlphabet.length; i++) {
            startNode.add(0);
        }
        nodesToProcess.add(startNode);

        while (!nodesToProcess.isEmpty()) {
            List<Integer> node = nodesToProcess.poll();
            for (int i = 0; i < overAlphabet.length; i++) {
                List<Integer> newNode = new ArrayList<>(node);
                newNode.set(i, (node.get(i) + 1) % modulo);
                MyEdge myEdge = new MyEdge(overAlphabet[i], node, newNode);
                if (graph.addEdge(myEdge, node, newNode, EdgeType.DIRECTED)) {
                    nodesToProcess.add(newNode);
                } else {//LeftBlank - edge was already present
                }
            }
        }
        return graph;
    }


    public static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromAnotherSimplifiedTask() {
        return getGridGraph(new String[]{"a", "b", "c"}, 2);
    }

    public static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromSimplifiedTask() {
        return getGridGraph(new String[]{"a", "b"}, 3);
    }

    public static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromOriginalTask() {
        return getGridGraph(new String[]{"a", "b", "c"}, 3);
    }
}
