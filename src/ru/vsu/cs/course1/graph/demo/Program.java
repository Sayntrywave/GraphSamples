package ru.vsu.cs.course1.graph.demo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;

import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;

import guru.nidi.graphviz.attribute.LinkAttr;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Records;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.attribute.Records.turn;
import static guru.nidi.graphviz.model.Compass.*;

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import static guru.nidi.graphviz.model.Factory.between;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;
import static guru.nidi.graphviz.model.Factory.port;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import guru.nidi.graphviz.parse.Parser;

import java.awt.EventQueue;

import static java.awt.Frame.MAXIMIZED_BOTH;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.UIManager;

import ru.vsu.cs.course1.graph.AdjListsGraph;
import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.course1.graph.WeightedGraph;
import ru.vsu.cs.util.SwingUtils;

public class Program {

    public static void dijkstraDemo() {
    }

    /**
     * Основная функция программы
     *
     * @param args Параметры командной строки
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        SwingUtils.setDefaultFont("Arial", 20);

        EventQueue.invokeLater(() -> {
            try {
                JFrame mainFrame = new GraphDemoFrame();
                mainFrame.setVisible(true);
                mainFrame.setExtendedState(MAXIMIZED_BOTH);
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
    }
}
