package zadanie13;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;
import java.util.function.Function;

/**
 * Created by tomek on 02.05.17.
 */
public class Main {
    public static void main(String... args) {
        //MutableValueGraph<List<Integer>, String> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = new DirectedSparseMultigraph<>();
        String[] alphabet = {"a", "b", "c"};

        Queue<List<Integer>> nodesToProcess = new LinkedList<>();
        nodesToProcess.add(Arrays.asList(0, 0, 0));

        int modulo = 2;
        while (!nodesToProcess.isEmpty()) {
            List<Integer> node = nodesToProcess.poll();
            for (int i = 0; i < alphabet.length; i++) {
                List<Integer> newNode = new ArrayList<>(node);
                newNode.set(i, (node.get(i) + 1) % modulo);
                MyEdge myEdge = new MyEdge(alphabet[i], node, newNode);
                if (graph.addEdge(myEdge, node, newNode, EdgeType.DIRECTED)) {
                    nodesToProcess.add(newNode);
                } else {//LeftBlank - edge was already present
                }
            }
        }

//        Map<Integer,List<Integer>> idTover = new HashMap<>();
//        Map<List<Integer>,Integer> verToId = new HashMap<>();
//        for(List<Integer> vertex : graph.getVertices())
//        {
//
//        }
        Function<List<Integer>, Integer> v2n = (e -> 1 + e.get(0) * 1 + e.get(1) * modulo + e.get(2) * modulo * modulo);
        Function<Integer, List<Integer>> n2v = (e -> Arrays.asList(
                ((e - 1) % modulo)
                , ((e - 1) % (modulo * modulo)) / (modulo)
                , ((e - 1) % (modulo * modulo * modulo)) / (modulo * modulo)));

        final String epsilon = "";
        final String empty = "";
        final String OR = "|";


        int n = graph.getVertexCount();
        String[][][] R = new String[n + 1][n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    R[i][j][0] = epsilon;
                } else {
                    R[i][j][0] = empty;
                }
                MyEdge myEdge = graph.findEdge(n2v.apply(i), n2v.apply(j));
                if (myEdge != null) {
                    R[i][j][0] += myEdge.label;
                }
            }
        }

        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    String l = R[i][j][k - 1];

                    String r = R[i][k][k - 1]
                            + (!R[k][k][k - 1].equals("") ? "(" + R[k][k][k - 1] + ")*" : "")
                            + R[k][j][k - 1];

                    if (!l.equals("") && !r.equals("")) {
                        R[i][j][k] = l + OR + r;
                    } else {
                        R[i][j][k] = l + r;
                    }

//                    R[i][j][k]=
//                            R[i][j][k-1]
//                            +R[i][k][k-1]
//                            +R[k][k][k-1]!=""?OR+"("+R[k][k][k-1]+")*":""
//                            +R[k][k][k-1]!=""?OR+R[k][j][k-1]:"";

                }
            }
        }

        new zadanie13.EdgeLabelDemo<>(graph);
        //System.out.println(graph.toString());
        System.out.println(R[1][v2n.apply(Arrays.asList(0, 0, 0))][8]);
    }
}
