package zadanie13.util;

import edu.uci.ics.jung.graph.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by tomek on 13.05.17.
 */
public class AutomatonToRegularExpressionConverter {
    final static String epsilon = "e";
    final static String empty = "0";
    final static String OR = "|";

    private AutomatonToRegularExpressionConverter() {
    }

    public static String convert(Graph<List<Integer>, MyEdge> graph, List<Integer> initialState, List<Integer> finalState) {
        Map<List<Integer>, Integer> stateToId = new HashMap<>();
        Map<Integer, List<Integer>> idToState = new HashMap<>();

        int n = 1;
        for (List<Integer> state : graph.getVertices()) {
            stateToId.put(state, n);
            idToState.put(n++, state);
        }

        final Function<List<Integer>, Integer> v2n = (e -> stateToId.get(e));
        final Function<Integer, List<Integer>> n2v = (e -> idToState.get(e));

        n = graph.getVertexCount();
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
                    String r;


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
                        if ("".equals(r)) {
                            r = epsilon;
                        }
                    }

                    String out = "";
                    if (l.equals(empty)) {
                        out = r;
                    }
                    if (r.equals(empty)) {
                        out = l;
                    }

                    if ("".equals(out)) {
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
                }
            }
        }
        return R[v2n.apply(initialState)][v2n.apply(finalState)][n];
    }
}
