/*
 * Copyright (c) 2003, The JUNG Authors
 * All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or https://github.com/jrtom/jung/blob/master/LICENSE for a description.
 * 
 */
package zadanie13;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.*;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demonstrates jung support for drawing edge labels that
 * can be positioned at any point along the edge, and can
 * be rotated to be parallel with the edge.
 *
 * @author Tom Nelson
 */
public class EdgeLabelDemo<V, E> extends JApplet {
    private static final long serialVersionUID = -6077157664507049647L;

    /**
     * the graph
     */
    Graph<V, E> graph;

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<V, E> vv;

    /**
     */
    VertexLabelRenderer vertexLabelRenderer;
    EdgeLabelRenderer edgeLabelRenderer;

    ScalingControl scaler = new CrossoverScalingControl();

    /**
     * create an instance of a simple graph with controls to
     * demo the label positioning features
     */

    public EdgeLabelDemo(DirectedSparseMultigraph<V, E> myGraph) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = frame.getContentPane();
        content.add(new EdgeLabelDemo(myGraph, 1));
        frame.pack();
        frame.setVisible(true);
    }

    @SuppressWarnings("serial")
    public EdgeLabelDemo(DirectedSparseMultigraph<V, E> myGraph, int safsafs) {

        // create a simple graph for the demo
        //graph = new SparseMultigraph<V, E>();

        graph = myGraph;
        Layout<V, E> layout = new CircleLayout<>(graph);
        vv = new VisualizationViewer<V, E>(layout, new Dimension(600, 400));
        vv.setBackground(Color.white);

        vertexLabelRenderer = vv.getRenderContext().getVertexLabelRenderer();
        edgeLabelRenderer = vv.getRenderContext().getEdgeLabelRenderer();

        Function<? super E, String> stringer = new Function<E, String>() {
            public String apply(E e) {
//                return "Edge:"+graph.getEndpoints(e).toString();
                return ((MyEdge) e).label;
            }
        };
        vv.getRenderContext().setEdgeLabelTransformer(stringer);
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<E>(vv.getPickedEdgeState(), Color.black, Color.cyan));
        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<V>(vv.getPickedVertexState(), Color.red, Color.yellow));
        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());

        // create a frome to hold the graph
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        Container content = getContentPane();
        content.add(panel);

        final DefaultModalGraphMouse<Integer, Number> graphMouse = new DefaultModalGraphMouse<Integer, Number>();
        vv.setGraphMouse(graphMouse);

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

        ButtonGroup radio = new ButtonGroup();
        JRadioButton lineButton = new JRadioButton("Line");
        lineButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(graph));
                    vv.repaint();
                }
            }
        });

        JRadioButton quadButton = new JRadioButton("QuadCurve");
        quadButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.quadCurve(graph));
                    vv.repaint();
                }
            }
        });

        JRadioButton cubicButton = new JRadioButton("CubicCurve");
        cubicButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.cubicCurve(graph));
                    vv.repaint();
                }
            }
        });
        radio.add(lineButton);
        radio.add(quadButton);
        radio.add(cubicButton);

        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        JCheckBox rotate = new JCheckBox("<html><center>EdgeType<p>Parallel</center></html>");
        rotate.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                AbstractButton b = (AbstractButton) e.getSource();
                edgeLabelRenderer.setRotateEdgeLabels(b.isSelected());
                vv.repaint();
            }
        });
        rotate.setSelected(true);
        MutableDirectionalEdgeValue mv = new MutableDirectionalEdgeValue(.5, .7);
        vv.getRenderContext().setEdgeLabelClosenessTransformer(mv);
        JSlider directedSlider = new JSlider(mv.getDirectedModel()) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width /= 2;
                return d;
            }
        };
        JSlider undirectedSlider = new JSlider(mv.getUndirectedModel()) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width /= 2;
                return d;
            }
        };

        JSlider edgeOffsetSlider = new JSlider(0, 50) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width /= 2;
                return d;
            }
        };
        edgeOffsetSlider.addChangeListener(new ChangeListener() {
            @SuppressWarnings("rawtypes")
            public void stateChanged(ChangeEvent e) {
                JSlider s = (JSlider) e.getSource();
                Function<? super E, Shape> edgeShapeFunction
                        = vv.getRenderContext().getEdgeShapeTransformer();
                if (edgeShapeFunction instanceof ParallelEdgeShapeTransformer) {
                    ((ParallelEdgeShapeTransformer) edgeShapeFunction)
                            .setControlOffsetIncrement(s.getValue());
                    vv.repaint();
                }
            }
        });

        Box controls = Box.createHorizontalBox();

        JPanel zoomPanel = new JPanel(new GridLayout(0, 1));
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Scale"));
        zoomPanel.add(plus);
        zoomPanel.add(minus);

        JPanel edgePanel = new JPanel(new GridLayout(0, 1));
        edgePanel.setBorder(BorderFactory.createTitledBorder("EdgeType Type"));
        edgePanel.add(lineButton);
        edgePanel.add(quadButton);
        edgePanel.add(cubicButton);

        JPanel rotatePanel = new JPanel();
        rotatePanel.setBorder(BorderFactory.createTitledBorder("Alignment"));
        rotatePanel.add(rotate);

        JPanel labelPanel = new JPanel(new BorderLayout());
        JPanel sliderPanel = new JPanel(new GridLayout(3, 1));
        JPanel sliderLabelPanel = new JPanel(new GridLayout(3, 1));
        JPanel offsetPanel = new JPanel(new BorderLayout());
        offsetPanel.setBorder(BorderFactory.createTitledBorder("Offset"));
        sliderPanel.add(directedSlider);
        sliderPanel.add(undirectedSlider);
        sliderPanel.add(edgeOffsetSlider);
        sliderLabelPanel.add(new JLabel("Directed", JLabel.RIGHT));
        sliderLabelPanel.add(new JLabel("Undirected", JLabel.RIGHT));
        sliderLabelPanel.add(new JLabel("Edges", JLabel.RIGHT));
        offsetPanel.add(sliderLabelPanel, BorderLayout.WEST);
        offsetPanel.add(sliderPanel);
        labelPanel.add(offsetPanel);
        labelPanel.add(rotatePanel, BorderLayout.WEST);

        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
        modePanel.add(graphMouse.getModeComboBox());

        controls.add(zoomPanel);
        controls.add(edgePanel);
        controls.add(labelPanel);
        controls.add(modePanel);
        content.add(controls, BorderLayout.SOUTH);
        quadButton.setSelected(true);
    }

    /**
     * subclassed to hold two BoundedRangeModel instances that
     * are used by JSliders to move the edge label positions
     *
     * @author Tom Nelson
     */
    class MutableDirectionalEdgeValue extends ConstantDirectionalEdgeValueTransformer<V, E> {
        BoundedRangeModel undirectedModel = new DefaultBoundedRangeModel(5, 0, 0, 10);
        BoundedRangeModel directedModel = new DefaultBoundedRangeModel(7, 0, 0, 10);

        public MutableDirectionalEdgeValue(double undirected, double directed) {
            super(undirected, directed);
            undirectedModel.setValue((int) (undirected * 10));
            directedModel.setValue((int) (directed * 10));

            undirectedModel.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setUndirectedValue(new Double(undirectedModel.getValue() / 10f));
                    vv.repaint();
                }
            });
            directedModel.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setDirectedValue(new Double(directedModel.getValue() / 10f));
                    vv.repaint();
                }
            });
        }

        /**
         * @return Returns the directedModel.
         */
        public BoundedRangeModel getDirectedModel() {
            return directedModel;
        }

        /**
         * @return Returns the undirectedModel.
         */
        public BoundedRangeModel getUndirectedModel() {
            return undirectedModel;
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        Container content = frame.getContentPane();
//        content.add(new EdgeLabelDemo());
//        frame.pack();
//        frame.setVisible(true);
//    }
}
