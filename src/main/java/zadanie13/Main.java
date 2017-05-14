package zadanie13;

import zadanie13.util.ExampleGraphs;

/**
 * Created by tomek on 02.05.17.
 */
public class Main {

    public static void main(String... args) {
        new zadanie13.EdgeLabelDemo<>(ExampleGraphs.getGraphFromAnotherSimplifiedTask());
        new zadanie13.EdgeLabelDemo<>(ExampleGraphs.getGraphFromExampleTask());
        new zadanie13.EdgeLabelDemo<>(ExampleGraphs.getGraphFromOriginalTask());
        new zadanie13.EdgeLabelDemo<>(ExampleGraphs.getGraphFromSimplifiedTask());
    }
}
