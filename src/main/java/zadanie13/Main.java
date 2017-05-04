package zadanie13;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomek on 02.05.17.
 */
public class Main {
    final static String epsilon = "e";
    final static String empty = "0";
    final static String OR = "|";
    static int modulo;
    final static Function<List<Integer>, Integer> v2n = (e -> 1 + e.get(0) * 1 + e.get(1) * modulo + e.get(2) * modulo * modulo);
    final static Function<Integer, List<Integer>> n2v = (e -> Arrays.asList(
            ((e - 1) % modulo)
            , ((e - 1) % (modulo * modulo)) / (modulo)
            , ((e - 1) % (modulo * modulo * modulo)) / (modulo * modulo)));
    static int showOption = 2;

    static {
        if (showOption == 1) {
            modulo = 12345;
        } else {
            modulo = showOption;
        }
    }

    private static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromExampleLink() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = new DirectedSparseMultigraph<>();

        List<Integer> node0 = Arrays.asList(0, 0, 0);
        List<Integer> node1 = Arrays.asList(1, 0, 0);
        List<Integer> node2 = Arrays.asList(2, 0, 0);

        graph.addEdge(new MyEdge("a", node0, node1), node0, node1, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("a", node1, node2), node1, node2, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("a", node2, node0), node2, node0, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node0, node2), node0, node2, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node2, node1), node2, node1, EdgeType.DIRECTED);
        graph.addEdge(new MyEdge("b", node1, node0), node1, node0, EdgeType.DIRECTED);

        return graph;
    }

    private static DirectedSparseMultigraph<List<Integer>, MyEdge> getGraphFromTask() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = new DirectedSparseMultigraph<>();
        String[] alphabet = {"a", "b", "c"};

        Queue<List<Integer>> nodesToProcess = new LinkedList<>();
        nodesToProcess.add(Arrays.asList(0, 0, 0));

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

        return graph;
    }

    public static void main(String... args) {
        //MutableValueGraph<List<Integer>, String> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph;// = new DirectedSparseMultigraph<>();

        if (showOption == 1) {
            graph = getGraphFromExampleLink();
        } else {
            graph = getGraphFromTask();
        }

        new zadanie13.EdgeLabelDemo<>(graph);

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
                //R[i][j][0] += myEdge.label;
                if (myEdge != null) {
                    if (R[i][j][0].equals(epsilon)) {
                        R[i][j][0] = myEdge.label + "?";
                    } else {
                        R[i][j][0] = myEdge.label;
                    }

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


                    if (R[i][k][k - 1].equals(empty) || R[k][k][k - 1].equals(empty) || R[k][j][k - 1].equals(empty)) {
                        r = empty;
                    } else {
                        String rleft = R[i][k][k - 1];
                        String rmiddle = R[k][k][k - 1];
                        String rright = R[k][j][k - 1];

                        if (rleft.equals(epsilon)) {
                            rleft = "";
                        }
                        if (rright.equals(epsilon)) {
                            rright = "";
                        }
                        if (rmiddle.equals(epsilon)) {
                            rmiddle = "";
                        } else {
                            rmiddle = "(" + rmiddle + ")*";
                        }
                        r = rleft + rmiddle + rright;
                        if (r.equals("")) {
                            r = epsilon;
                        }
                    }

                    List<String> w = new ArrayList<>();
                    String out = "";
                    if (l.equals(empty)) {
                        out = r;
                    }
                    if (r.equals(empty)) {
                        out = l;
                    }

                    if (out.equals("")) {
                        if (l.equals(epsilon) || r.equals(epsilon)) {
                            if (l.equals(epsilon) && r.equals(epsilon)) {
                                out = epsilon;
                            } else {
                                if (l.equals(epsilon)) {
                                    out = "(" + r + ")" + "?";/////////////////////////////////
                                } else {
                                    if (r.equals(epsilon)) {
                                        out = epsilon;
                                    } else {
                                        out = "(" + l + ")" + "?";/////////////////////////////////
                                    }
                                }
                            }
                        } else {
                            if (l.equals(r)) {
                                out = l;
                            } else {
                                out = "(" + l + OR + r + ")";
                            }
                        }
                    }

                    R[i][j][k] = out;
                    //R[i][j][k]=R[i][j][k-1]+OR+R[i][k][k-1]+"("+R[k][k][k-1]+")*"+R[k][j][k-1];

//                    if (!l.equals("") && !r.equals("")) {
//                        if (l.equals(r)) {
//                            R[i][j][k] = "(" + l + ")";
//                        } else {
//                            R[i][j][k] = "(" + l + OR + r + ")";
//                        }
//                    } else {
//                        if (l.equals("") && r.equals("")) {
//                            R[i][j][k] = "";
//                        } else {
//                            R[i][j][k] = "(" + l + r + ")";
//                        }
//
//                    }
                }
            }
        }


        //System.out.println(graph.toString());

        //System.out.println(R[1][finalState][n]);

        Map<String, Boolean> tests = new HashMap<>();
        if (showOption == 1) {
            tests.put("ab", true);
            tests.put("abababab", true);
            tests.put("abbbb", true);
            tests.put("abbbbab", true);
            String longTest = "";
            for (int i = 1; i < 20; i++) {
                longTest += "aaa";
            }
            tests.put(longTest, true);
            tests.put(longTest + "a", false);
            tests.put("", true);
            tests.put("aa", false);
            tests.put("bba", false);
        } else {//for modulo 2 - final = |a|===0 |b|===1 |c|===1
            tests.put("aabc", true);
            tests.put("abc", false);
            tests.put("aaabc", false);
            tests.put("aaabca", true);
            tests.put("aaaabc", true);
            tests.put("bc", true);
            String longTest = "";
            for (int i = 1; i < 20; i++) {
                longTest += "aabbcc";
            }
            longTest += "bc";
            tests.put(longTest, true);
        }

        int finalState = v2n.apply(Arrays.asList(0, 0, 0));
        if (showOption == 1) {
            finalState = v2n.apply(Arrays.asList(0, 0, 0));
        } else {
            finalState = v2n.apply(Arrays.asList(0, 1, 1));//0,1,2
        }

        Pattern p = Pattern.compile(R[1][finalState][n]);
        Matcher m;
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            m = p.matcher(entry.getKey());

            if (m.matches() == entry.getValue()) {
                System.out.print("PASSED : ");
            } else {
                System.out.print("FAILED : ");
            }
            System.out.println("key=" + entry.getKey() + ", expectedValue=" + entry.getValue());
        }
    }
}
