/*
 * Copyright (c) 2003, The JUNG Authors
 * All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or https://github.com/jrtom/jung/blob/master/LICENSE for a description.
 * 
 */
package zadanie13;

import com.google.common.base.Functions;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer.InsidePositioner;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Demonstrates VisualizationImageServer.
 *
 * @author Tom Nelson
 */
public class VisualizationImageServerDemo<V, E> {

    /**
     * the graph
     */
    DirectedSparseMultigraph<V, E> graph;

    /**
     * the visual component and renderer for the graph
     */
    VisualizationImageServer<V, E> vv;

    /**
     *
     */
    public VisualizationImageServerDemo(DirectedSparseMultigraph<V, E> passedGraph) {

        // create a simple graph for the demo
        graph = passedGraph;

        vv = new VisualizationImageServer<V, E>(new KKLayout<V, E>(graph), new Dimension(600, 600));

        vv.getRenderer().setVertexRenderer(
                new GradientVertexRenderer<V, E>(
                        Color.white, Color.red,
                        Color.white, Color.blue,
                        vv.getPickedVertexState(),
                        false));
        vv.getRenderContext().setEdgeDrawPaintTransformer(Functions.constant(Color.lightGray));
        vv.getRenderContext().setArrowFillPaintTransformer(Functions.constant(Color.lightGray));
        vv.getRenderContext().setArrowDrawPaintTransformer(Functions.constant(Color.lightGray));

        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<E>(vv.getPickedEdgeState(), Color.black, Color.cyan));

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPositioner(new InsidePositioner());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);

        // create a frome to hold the graph
        final JFrame frame = new JFrame();
        Container content = frame.getContentPane();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image im = vv.getImage(new Point2D.Double(300, 300), new Dimension(600, 600));
        Icon icon = new ImageIcon(im);
        JLabel label = new JLabel(icon);
        content.add(label);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //new VisualizationImageServerDemo();

    }
}
