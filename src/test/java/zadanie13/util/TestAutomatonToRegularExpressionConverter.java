package zadanie13.util;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomek on 13.05.17.
 */
public class TestAutomatonToRegularExpressionConverter {

    @Test
    public void RegularExpressionForExample() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = ExampleGraphs.getGraphFromExampleLink();

        List<Integer> initialState = Arrays.asList(0);
        List<Integer> finalState = Arrays.asList(0); // same as intial state

        Map<String, Boolean> tests = new HashMap<>();
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

        String regularExpressionPattern = AutomatonToRegularExpressionConverter.convert(graph, initialState, finalState);
        Pattern p = Pattern.compile(regularExpressionPattern);
        Matcher m;
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            m = p.matcher(entry.getKey());
            Assert.assertTrue(m.matches() == entry.getValue());
        }
    }

    @Test
    public void RegularExpressionForSimplifiedTaskGraph() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = ExampleGraphs.getGridGraph(new String[]{"a", "b"}, 3);
        //DirectedSparseMultigraph<List<Integer>, MyEdge> sameGraph = ExampleGraphs.getSimplifiedGraphFromTask();

        List<Integer> initialState = Arrays.asList(0, 0);
        List<Integer> finalState = Arrays.asList(2, 2); // (number of "a")=3*k1+2 and (number of "b")=3*k2+2

        Map<String, Boolean> tests = new HashMap<>();
        //tests.put("",false);
        tests.put("a", false);
        tests.put("ab", false);
        tests.put("aab", false);
        tests.put("abb", false);
        tests.put("aabb", true);
        tests.put("abab", true);
        tests.put("ababababab", true);

        String regularExpressionPattern = AutomatonToRegularExpressionConverter.convert(graph, initialState, finalState);
        Pattern p = Pattern.compile(regularExpressionPattern);
        Matcher m;
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            m = p.matcher(entry.getKey());
            Assert.assertTrue(m.matches() == entry.getValue());
        }
    }

    @Test
    @Ignore //OutOfMemoryError
    /*Comment on expected memory usage
    27 states
    R[2][27][27] of Strings
    2 for remembering penultimate and current iteration over k
    27 for iterations over i
    27 for iterations over j

    Length of String = 4^k

    Expected memory space usage in GB
    27*27*Sum(4^i,{i,26,27})/(1000*1000*1000*8)
    https://www.wolframalpha.com/input/?i=27*27*Sum(4%5Ei,%7Bi,26,27%7D)%2F(1000*1000*1000*8)
    =~2*10^9 2 trilion GBs

    In reality up to k==8 most strings were one character long
    27*27*Sum(4^i,{i,26-8,27-8})/(1000*1000*1000*8)
    https://www.wolframalpha.com/input/?i=27*27*Sum(4%5Ei,%7Bi,26-8,27-8%7D)%2F(1000*1000*1000*8)
    =~31 GBs
    */
    public void RegularExpressionForTaskGraph() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = ExampleGraphs.getGridGraph(new String[]{"a", "b", "c"}, 3);
        //DirectedSparseMultigraph<List<Integer>, MyEdge> sameGraph = ExampleGraphs.getGraphFromTask();

        List<Integer> initialState = Arrays.asList(0, 0, 0);
        List<Integer> finalState = Arrays.asList(0, 1, 2);
        // (number of "a")=3*k1+0 and (number of "b")=3*k2+1 and (number of "c")=3*k3+2
        Map<String, Boolean> tests = new HashMap<>();
        //tests.put("",false);
        tests.put("abc", false);
        tests.put("aaabbbbccccc", true);

        String regularExpressionPattern = AutomatonToRegularExpressionConverter.convert(graph, initialState, finalState);
        Pattern p = Pattern.compile(regularExpressionPattern);
        Matcher m;
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            m = p.matcher(entry.getKey());
            Assert.assertTrue(m.matches() == entry.getValue());
        }
    }

    @Test
    public void RegularExpressionForAnotherSimplifiedGraph() {
        DirectedSparseMultigraph<List<Integer>, MyEdge> graph = ExampleGraphs.getAnotherSimplifiedGraph();

        List<Integer> initialState = Arrays.asList(0, 0, 0);
        List<Integer> finalState = Arrays.asList(0, 1, 1); //for modulo 2 - final = |a|===0 |b|===1 |c|===1

        Map<String, Boolean> tests = new HashMap<>();

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
        String regularExpressionPattern = AutomatonToRegularExpressionConverter.convert(graph, initialState, finalState);
        Pattern p = Pattern.compile(regularExpressionPattern);
        Matcher m;
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            m = p.matcher(entry.getKey());
            Assert.assertTrue(m.matches() == entry.getValue());
        }
    }
}
